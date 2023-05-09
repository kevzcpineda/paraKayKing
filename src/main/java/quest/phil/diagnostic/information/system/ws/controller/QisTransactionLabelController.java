package quest.phil.diagnostic.information.system.ws.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.common.documents.LabelDocument;
import quest.phil.diagnostic.information.system.ws.model.QisLabelInformation;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisMarker;
import quest.phil.diagnostic.information.system.ws.model.entity.QisMarkerInventory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPackage;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.request.QisFilmInventoryRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionLabelRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisFilmInventoryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisMarkerRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisPatientRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/transactions/")
public class QisTransactionLabelController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLabelController.class);
	private final String SHORTDATE = "yyyyMMdd";
	private final String LONGDATE = "yyyyMMddHHmm";
	private final String CATEGORY = "LABEL";

	@Value("${application.name}")
	private String applicationName;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private QisTransactionRepository qisTransactionRepository;

	@Autowired
	private QisPatientRepository qisPatientRepository;

	@Autowired
	private QisMarkerRepository qisMarkerRepository;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	private QisFilmInventoryRepository qisFilmInventoryRepository;

	@GetMapping("labels")
	public void getTransactionLabels(HttpServletResponse response,
			@RequestParam(required = false) String transactionDate, @RequestParam(required = false) String dateFrom,
			@RequestParam(required = false) String dateTo, @RequestParam(required = false) String dateTimeFrom,
			@RequestParam(required = false) String dateTimeTo, @RequestParam(required = false) Long transactionId,
			@RequestParam(required = false) Boolean xray, @RequestParam(required = false) Boolean specimen,
			@RequestParam(required = false) String xrayType, @RequestParam(required = false) String filmSize,
			@RequestParam(required = false) String radTech, @RequestParam(required = false) String medTech,
			@RequestParam(required = false) String pid, @RequestParam(required = false) Boolean withSpace,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Print Transaction Labels Request");

//		qisMarkerRepository.findById(markerRequest.getId());

		if (xray == null && specimen == null) {
			throw new RuntimeException("No label type specified[xray or specimen].",
					new Throwable("No label type specified[xray or specimen]."));
		}

		if (xray == false && specimen == false) {
			throw new RuntimeException("No label type specified[xray or specimen].",
					new Throwable("No label type specified[xray or specimen]."));
		}

		if (withSpace == null) {
			withSpace = false;
		}

		int page = 0;
		int size = 30;
		Pageable pageable = PageRequest.of(page, size);
		Page<QisTransaction> transactionList;

		Set<QisLabelInformation> infoList = new HashSet<>();
		if (transactionId != null) {
			LOGGER.info(authUser.getId() + "-TransactionID[" + transactionId + "]");
			transactionList = qisTransactionRepository.getTransactionsByTransactionId(transactionId, pageable);
			if (transactionList.getTotalElements() <= 0) {
				throw new RuntimeException("Transaction not found.",
						new Throwable("transactionId: Transaction not found."));
			}
			getTransactionInformation(transactionList, infoList);
			Boolean isSpoiled = false;
			QisMarker markerReq = qisMarkerRepository.getTxnIdAndByXrayType(transactionId.toString(), xrayType, isSpoiled);

			QisPatient qisPatient = qisPatientRepository.findByPatientid(pid);

			boolean isUpdate = false;
			String updateData = "";

			if (markerReq != null) {
				if (markerReq.isSpoiled() == false) {					
					if (!markerReq.getFilmSize().equals(filmSize)) {
						updateData = appUtility.formatUpdateData(updateData, "filmSize", markerReq.getFilmSize(), filmSize);
						isUpdate = true;
						markerReq.setFilmSize(filmSize);
					}
					
					if (!markerReq.getRadTech().equals(radTech)) {
						updateData = appUtility.formatUpdateData(updateData, "radTech", markerReq.getRadTech(), radTech);
						isUpdate = true;
						markerReq.setRadTech(radTech);
					}
					
					if (!markerReq.getXrayType().equals(xrayType)) {
						updateData = appUtility.formatUpdateData(updateData, "xrayType", markerReq.getXrayType(), xrayType);
						isUpdate = true;
						markerReq.setXrayType(xrayType);
					}
					
					if (isUpdate) {
						markerReq.setPatient(qisPatient);
						markerReq.setPatientId(qisPatient.getId());
						markerReq.setQisTransaction(transactionId.toString());
						qisMarkerRepository.save(markerReq);
					}
				}else {
					
					filmCountAndInventory(filmSize, radTech, xrayType, qisPatient, qisPatient.getId(), transactionId.toString()); 
				}
			} else {
				filmCountAndInventory(filmSize, radTech, xrayType, qisPatient, qisPatient.getId(), transactionId.toString()); 
				
			}

		} else {
			if (transactionDate != null) {
				LOGGER.info(authUser.getId() + "-TransactionDate[" + transactionDate + "]");
				appUtility.validateTransactionDate(transactionDate);
				Calendar txnDate = appUtility.stringToCalendarDate(transactionDate, SHORTDATE);

				do {
					transactionList = qisTransactionRepository.getTransactionsByStatusTransactionDate("SPD", txnDate,
							pageable);
					getTransactionInformation(transactionList, infoList);
					page++;
					pageable = PageRequest.of(page, size);
				} while (page < transactionList.getTotalPages());
			} else if (dateFrom != null || dateTo != null) {
				LOGGER.info(authUser.getId() + "-DateFrom[" + dateFrom + "] DateTo[" + dateTo + "]");
				appUtility.validateDateFromTo(dateFrom, dateTo);
				Calendar txnDateFrom = appUtility.stringToCalendarDate(dateFrom, SHORTDATE);
				Calendar txnDateTo = appUtility.stringToCalendarDate(dateTo, SHORTDATE);

				do {
					transactionList = qisTransactionRepository.getTransactionsByStatusTransactionDateRange("SPD",
							txnDateFrom, txnDateTo, pageable);
					getTransactionInformation(transactionList, infoList);
					page++;
					pageable = PageRequest.of(page, size);
				} while (page < transactionList.getTotalPages());
			} else if (dateTimeFrom != null || dateTimeTo != null) {
				LOGGER.info(authUser.getId() + "-DateTimeFrom[" + dateTimeFrom + "] DateTimeTo[" + dateTimeTo + "]");
				appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
				Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
				Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);

				do {
					transactionList = qisTransactionRepository.getTransactionsByStatusTransactionDateTimeRange("SPD",
							txnDateTimeFrom, txnDateTimeTo, pageable);
					getTransactionInformation(transactionList, infoList);
					page++;
					pageable = PageRequest.of(page, size);
				} while (page < transactionList.getTotalPages());
			}
		}

		if (infoList.size() > 0) {
			List<QisLabelInformation> sortedList = new ArrayList<QisLabelInformation>(infoList);
			Collections.sort(sortedList);

			QisTransactionLabelRequest labelRequest = new QisTransactionLabelRequest();
			labelRequest.setXray(xray);
			labelRequest.setSpecimen(specimen);
			labelRequest.setMedTech(medTech);
			labelRequest.setRadTech(radTech);
			labelRequest.setXrayType(xrayType);

			Document document = new Document(PageSize.LETTER);
			if (response != null) {
				PdfWriter.getInstance(document, response.getOutputStream());
			}
			document.setMargins(10, 10, 15, 15);
			document.addTitle("LABELS AND MARKERS");
			document.open();

			LabelDocument label = new LabelDocument(applicationName, appUtility);
			if (specimen) {
				label.getSpecimenMarkers(document, sortedList, labelRequest);
			}

			if (xray) {
				if (specimen) {
					document.newPage();
				}
				label.getXRayMarkers(document, sortedList, labelRequest, withSpace);
			}

			document.close();
			response.setContentType("application/pdf");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=laboratory_labels.pdf";
			response.setHeader(headerKey, headerValue);

			qisLogService.info(authUser.getId(), QisTransactionLabelController.class.getSimpleName(), "PRINT", "Labels",
					null, CATEGORY);
		} else {
			throw new RuntimeException("No records to print.", new Throwable("No records to print."));
		}
	}

	private void getTransactionInformation(Page<QisTransaction> list, Set<QisLabelInformation> infoList) {
		for (QisTransaction txn : list) {
			String ageGender = "";
			Integer age = appUtility.calculateAgeInYear(txn.getPatient().getDateOfBirth(), txn.getTransactionDate());
			if (age != null) {
				ageGender = String.valueOf(age) + "/";
			}
			if (txn.getPatient().getGender().equals("M")) {
				ageGender = ageGender + "MALE";
			} else if (txn.getPatient().getGender().equals("F")) {
				ageGender = ageGender + "FEMALE";
			}

			String biller = "WALK-IN";
			if (txn.getBiller() != null) {
				biller = txn.getBiller();
			} else if (txn.getReferral() != null) {
				biller = txn.getReferral().getReferral();
			} else if (txn.getPatient().getCorporate() != null) {
				biller = txn.getPatient().getCorporate().getCompanyName();
			}

			boolean withXRay = false;
			for (QisTransactionItem item : txn.getTransactionItems()) {
				if (item.getItemType().equals("ITM")) {
					QisItem itm = (QisItem) item.getItemDetails();
					if (itm.getItemLaboratory().equals("XR") || itm.getItemLaboratory().equals("NAS")) {
						withXRay = true;
						break;
					}
				} else if (item.getItemType().equals("PCK")) {
					QisPackage pck = (QisPackage) item.getItemDetails();
					for (QisItem itm : pck.getPackageItems()) {
						if (itm.getItemLaboratory().equals("XR") || itm.getItemLaboratory().equals("NAS")) {
							withXRay = true;
							break;
						}
					}

					if (withXRay) {
						break;
					}
				}
			}

			QisLabelInformation info = new QisLabelInformation(txn.getId(),
					appUtility.getPatientFullname(txn.getPatient()), ageGender, biller,
					appUtility.calendarToFormatedDate(txn.getTransactionDate(), "yyyy-MM-dd"), withXRay);

			infoList.add(info);
		}

	}

	@GetMapping("markers")
	public List<QisMarker> getMarkers(@RequestParam(required = false) String dateTimeFrom,
			@RequestParam(required = false) String dateTimeTo, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		
		appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
		Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
		Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);
		

		return qisMarkerRepository.getMarkserByDateTimeRange(txnDateTimeFrom, txnDateTimeTo);

	}
	
	@GetMapping("markers_Inventory")
	public QisMarkerInventory getMarkersInventory(@AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		
		return qisFilmInventoryRepository.getFilmInventory((long) 1);

	}

	@PostMapping("film_inventory")
	public String savefilmInventory(@Valid @RequestBody QisFilmInventoryRequest markerReq,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		QisMarkerInventory marker = new QisMarkerInventory();
		QisMarkerInventory getFilmInventoty = qisFilmInventoryRepository.getFilmInventory((long) 1);
		String message = "";

		if (getFilmInventoty != null) {

			if (markerReq.getFilm10x12() != 0) {
				if (getFilmInventoty.getFilm10x12() != markerReq.getFilm10x12()) {
					getFilmInventoty.setFilm10x12(markerReq.getFilm10x12());
				}
			} else {
				getFilmInventoty.setFilm10x12(getFilmInventoty.getFilm10x12());
			}

			if (markerReq.getFilm11x14() != 0) {
				if (getFilmInventoty.getFilm11x14() != markerReq.getFilm11x14()) {
					getFilmInventoty.setFilm11x14(markerReq.getFilm11x14());
				}
			} else {
				getFilmInventoty.setFilm11x14(getFilmInventoty.getFilm11x14());
			}

			if (markerReq.getFilm14x14() != 0) {
				if (getFilmInventoty.getFilm14x14() != markerReq.getFilm14x14()) {
					getFilmInventoty.setFilm14x14(markerReq.getFilm14x14());
				}
			} else {
				getFilmInventoty.setFilm14x14(getFilmInventoty.getFilm14x14());
			}

			if (markerReq.getFilm14x17() != 0) {
				if (getFilmInventoty.getFilm14x17() != markerReq.getFilm14x17()) {
					getFilmInventoty.setFilm14x17(markerReq.getFilm14x17());
				}
			} else {
				getFilmInventoty.setFilm14x17(getFilmInventoty.getFilm14x17());
			}

			if (markerReq.getFilm8x10() != 0) {
				if (getFilmInventoty.getFilm8x10() != markerReq.getFilm8x10()) {
					getFilmInventoty.setFilm8x10(markerReq.getFilm8x10());
				}
			} else {
				getFilmInventoty.setFilm8x10(getFilmInventoty.getFilm8x10());
			}

			qisFilmInventoryRepository.save(getFilmInventoty);
			message = "Update Successfully!";
		} else {
			marker.setFilm10x12(markerReq.getFilm10x12());
			marker.setFilm11x14(markerReq.getFilm11x14());
			marker.setFilm14x14(markerReq.getFilm14x14());
			marker.setFilm14x17(markerReq.getFilm14x17());
			marker.setFilm8x10(markerReq.getFilm8x10());
			qisFilmInventoryRepository.save(marker);
			message = "Added Successfully!";
		}

		return message;
	}
	
	private void filmCountAndInventory(String filmSize, String radTech, String xrayType, QisPatient qisPatient, Long id,
			String transactionId) {
		QisMarkerInventory getFilmInventoty = qisFilmInventoryRepository.getFilmInventory((long) 1);
		QisMarker marker = new QisMarker();
		
		marker.setFilmSize(filmSize);
		marker.setRadTech(radTech);
		marker.setXrayType(xrayType);
		marker.setPatient(qisPatient);
		marker.setPatientId(qisPatient.getId());
		marker.setQisTransaction(transactionId);
		marker.setSpoiled(false);
		qisMarkerRepository.save(marker);
		
		if (getFilmInventoty != null) {
			switch (filmSize) {
			case "11x14" :
				getFilmInventoty.setFilm11x14(getFilmInventoty.getFilm11x14() - 1);
				break;
			case "10x12" :
				getFilmInventoty.setFilm10x12(getFilmInventoty.getFilm10x12() - 1);
				break;
			case "14x17" :
				getFilmInventoty.setFilm14x17(getFilmInventoty.getFilm14x17() - 1);
				break;
			case "8x10" :
				getFilmInventoty.setFilm8x10(getFilmInventoty.getFilm8x10() - 1);
				break;
			case "14x14" :
				getFilmInventoty.setFilm14x14(getFilmInventoty.getFilm14x14() - 1);
				break;
			default :
				break;
			}
			qisFilmInventoryRepository.save(getFilmInventoty);
		}
		
	}
}
