package quest.phil.diagnostic.information.system.ws.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppExcelUtility;
import quest.phil.diagnostic.information.system.ws.common.AppQuestQualityUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.common.documents.ConsolidatedCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.TxnPatientRecords;
import quest.phil.diagnostic.information.system.ws.common.exports.QuestQualityCupmedarExport;
import quest.phil.diagnostic.information.system.ws.common.exports.QuestQualityExport;
import quest.phil.diagnostic.information.system.ws.common.exports.TransactionList;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;
import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItemLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionPhysicalExamination;
import quest.phil.diagnostic.information.system.ws.repository.QisQualityTransactionRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemLaboratoriesRepositories;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoriesRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryRequestRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionPhysicalExaminationRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRepository;

@RestController
@RequestMapping("/api/v1/transactions/")
public class QisTransactionSummaryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionSummaryController.class);
	private final String SHORTDATE = "yyyyMMdd";
	private final String LONGDATE = "yyyyMMddHHmm";
	private final String LABORATORY = "LAB";

	@Autowired
	AppUtility appUtility;

	@Value("${application.name}")
	private String applicationName;
	
	@Value("${application.address}")
	private String applicationAddress;

	@Value("${application.header}")
	private String applicationHeader;

	@Value("${application.footer}")
	private String applicationFooter;

	@Autowired
	private AppExcelUtility appExcelUtility;

	@Autowired
	private QisTransactionRepository qisTransactionRepository;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;

	@Autowired
	private QisTransactionLaboratoriesRepository qisTransactionLaboratoriesRepository;

	@Autowired
	private QisTransactionItemLaboratoriesRepositories qisTransactionItemLaboratoriesRepositories;

	@Autowired
	private AppQuestQualityUtility appQuestQualityUtility;

	@Autowired
	private QisQualityTransactionRepository qisQualityRepository;

	@Autowired
	private QisTransactionLaboratoryRequestRepository qistransactionILabReq;

	@Autowired
	private QisTransactionPhysicalExaminationRepository qisTransactionPhysicalExaminationRepository;

//	@Autowired
//	private QisQualityTransactionRepository qisQualityTransactionRepository;

	// LIST OF TRANSACTIONS LABORATORY REQUEST BY LABORATORY
	@GetMapping("service_request")
	public Page<QisTransactionLaboratoryDetails> getTransactionByLaboratory(
			@RequestParam(required = false) String transactionDate,
			@RequestParam(required = false) String dateFrom,
			@RequestParam(required = false) String dateTo,
			@RequestParam(required = false) String dateTimeFrom,
			@RequestParam(required = false) String dateTimeTo,
			@RequestParam(required = false) String branchId,
			@RequestParam(required = false) String chargeTo,
			@RequestParam String laboratory, Pageable pageable,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View Transaction Service Request[" + laboratory + "]");

		if (laboratory == null || "".equals(laboratory.trim())) {
			throw new RuntimeException("No laboratory specified", new Throwable("laboratory: No laboratory specified"));
		}

		QisBranch branch = null;
		if (branchId != null) {
			branch = appUtility.getQisBranchByBranchId(branchId);
			if (branch == null) {
				throw new RuntimeException("Invalid branch id.", new Throwable("branchId: Invalid branch id."));
			}
		}

		QisCorporate biller = null;
		if (chargeTo != null) {
			biller = appUtility.getQisCorporateByCorporateId(chargeTo);
			if (biller == null) {
				throw new RuntimeException("Invalid charge to account.",
						new Throwable("chargeTo: Invalid charge to account."));
			}
		}

		int myQuery = 0;
		if (branch != null) {
			myQuery += 1;
		}

		if (biller != null) {
			myQuery += 2;
		}
		if (transactionDate != null) {
			LOGGER.info(authUser.getId() + "-TransactionDate[" + transactionDate + "]");
			appUtility.validateTransactionDate(transactionDate);
			Calendar txnDate = appUtility.stringToCalendarDate(transactionDate, SHORTDATE);

			switch (myQuery) {
			case 1: // branch only
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateBranchLaboratory(
						txnDate, laboratory, branch.getId(), pageable);

			case 2: // biller only
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateBillerLaboratory(
						txnDate, laboratory, biller.getId(), pageable);

			case 3: // branch & biller
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateBranchBillerLaboratory(
						txnDate, laboratory, branch.getId(), biller.getId(), pageable);

			default:
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateLaboratory(txnDate,
						laboratory, pageable);
			}
		} else if (dateFrom != null || dateTo != null) {
			LOGGER.info(authUser.getId() + "-DateFrom[" + dateFrom + "] DateTo[" + dateTo + "]");
			appUtility.validateDateFromTo(dateFrom, dateTo);
			Calendar txnDateFrom = appUtility.stringToCalendarDate(dateFrom, SHORTDATE);
			Calendar txnDateTo = appUtility.stringToCalendarDate(dateTo, SHORTDATE);

			switch (myQuery) {
			case 1: // branch only
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateRangeBranchLaboratory(
						txnDateFrom, txnDateTo, laboratory, branch.getId(), pageable);

			case 2: // biller only
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateRangeBillerLaboratory(
						txnDateFrom, txnDateTo, laboratory, biller.getId(), pageable);

			case 3: // branch & biller
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateRangeBranchBillerLaboratory(txnDateFrom, txnDateTo, laboratory,
								branch.getId(), biller.getId(), pageable);

			default:
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateRangeLaboratory(txnDateFrom, txnDateTo, laboratory, pageable);
			}
		} else if (dateTimeFrom != null || dateTimeTo != null) {
			LOGGER.info(authUser.getId() + "-DateTimeFrom[" + dateTimeFrom + "] DateTimeTo[" + dateTimeTo + "]");
			appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
			Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
			Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);

			switch (myQuery) {
			case 1: // branch only
				System.out.println("case1");
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateTimeRangeBranchLaboratory(txnDateTimeFrom, txnDateTimeTo,
								laboratory, branch.getId(), pageable);

			case 2: // biller only
				System.out.println("case2");
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateTimeRangeBillerLaboratory(txnDateTimeFrom, txnDateTimeTo,
								laboratory, biller.getId(), pageable);

			case 3: // branch & biller
				System.out.println("case3");
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateTimeRangeBranchBillerLaboratory(txnDateTimeFrom, txnDateTimeTo,
								laboratory, branch.getId(), biller.getId(), pageable);

			default:
				System.out.println("dafault");
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateTimeRangeLaboratory(
						txnDateTimeFrom, txnDateTimeTo, laboratory, pageable);
			}
		}

		throw new RuntimeException("No parameter dates added on the request.",
				new Throwable("No parameter dates added on the request."));
	}

	// LIST OF TRANSACTIONS LABORATORY REQUEST BY CATEGORY
	@GetMapping("laboratory_services")
	public Page<QisTransactionLaboratoryDetails> getTransactionByCategory(
			@RequestParam(required = false) String transactionDate, @RequestParam(required = false) String dateFrom,
			@RequestParam(required = false) String dateTo, @RequestParam(required = false) String dateTimeFrom,
			@RequestParam(required = false) String dateTimeTo, @RequestParam(required = false) String branchId,
			@RequestParam(required = false) String chargeTo, @RequestParam(required = false) String procedure,
			Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Services[" + procedure + "]");

		QisBranch branch = null;
		if (branchId != null) {
			branch = appUtility.getQisBranchByBranchId(branchId);
			if (branch == null) {
				throw new RuntimeException("Invalid branch id.", new Throwable("branchId: Invalid branch id."));
			}
		}

		QisCorporate biller = null;
		if (chargeTo != null) {
			biller = appUtility.getQisCorporateByCorporateId(chargeTo);
			if (biller == null) {
				throw new RuntimeException("Invalid charge to account.",
						new Throwable("chargeTo: Invalid charge to account."));
			}
		}

		int myQuery = 0;
		if (branch != null) {
			myQuery += 1;
		}

		if (biller != null) {
			myQuery += 2;
		}

		if (procedure != null) {
			if (appUtility.getLaboratoryProcedure(procedure) == null)
				throw new RuntimeException("Fail! -> Cause: Laboratory Procedure Code[" + procedure + "] not found.",
						new Throwable("procedure: Laboratory Procedure Code[" + procedure + "] not found."));

			myQuery += 4;
		}

		if (transactionDate != null) {
			LOGGER.info(authUser.getId() + "-TransactionDate[" + transactionDate + "]");
			appUtility.validateTransactionDate(transactionDate);
			Calendar txnDate = appUtility.stringToCalendarDate(transactionDate, SHORTDATE);

			switch (myQuery) {
			case 1: // branch only
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateBranchCategory(txnDate,
						LABORATORY, branch.getId(), pageable);

			case 2: // biller only
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateBillerCategory(txnDate,
						LABORATORY, biller.getId(), pageable);

			case 3: // branch & biller
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateBranchBillerCategory(
						txnDate, LABORATORY, branch.getId(), biller.getId(), pageable);

			case 4: // procedure
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateCategoryProcedure(txnDate, LABORATORY, procedure, pageable);

			case 5: // procedure & branch
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateBranchCategoryProcedure(txnDate, LABORATORY, procedure,
								branch.getId(), pageable);

			case 6: // procedure & biller
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateBillerCategoryProcedure(txnDate, LABORATORY, procedure,
								biller.getId(), pageable);

			case 7: // procedure & branch & biller
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateBranchBillerCategoryProcedure(txnDate, LABORATORY, procedure,
								branch.getId(), biller.getId(), pageable);

			default:
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateCategory(txnDate,
						LABORATORY, pageable);
			}

		} else if (dateFrom != null || dateTo != null) {
			LOGGER.info(authUser.getId() + "-DateFrom[" + dateFrom + "] DateTo[" + dateTo + "]");
			appUtility.validateDateFromTo(dateFrom, dateTo);
			Calendar txnDateFrom = appUtility.stringToCalendarDate(dateFrom, SHORTDATE);
			Calendar txnDateTo = appUtility.stringToCalendarDate(dateTo, SHORTDATE);

			switch (myQuery) {
			case 1: // branch only
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateRangeBranchCategory(
						txnDateFrom, txnDateTo, LABORATORY, branch.getId(), pageable);

			case 2: // biller only
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateRangeBillerCategory(
						txnDateFrom, txnDateTo, LABORATORY, biller.getId(), pageable);

			case 3: // branch & biller
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateRangeBranchBillerCategory(txnDateFrom, txnDateTo, LABORATORY,
								branch.getId(), biller.getId(), pageable);

			case 4: // procedure
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateRangeCategoryProcedure(
						txnDateFrom, txnDateTo, LABORATORY, procedure, pageable);

			case 5: // procedure & branch
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateRangeBranchCategoryProcedure(txnDateFrom, txnDateTo,
								LABORATORY, procedure, branch.getId(), pageable);

			case 6: // procedure & biller
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateRangeBillerCategoryProcedure(txnDateFrom, txnDateTo,
								LABORATORY, procedure, biller.getId(), pageable);

			case 7: // procedure & branch & biller
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateRangeBranchBillerCategoryProcedure(txnDateFrom, txnDateTo,
								LABORATORY, procedure, branch.getId(), biller.getId(), pageable);
			default:
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateRangeCategory(txnDateFrom, txnDateTo, LABORATORY, pageable);
			}
		} else if (dateTimeFrom != null || dateTimeTo != null) {
			LOGGER.info(authUser.getId() + "-DateTimeFrom[" + dateTimeFrom + "] DateTimeTo[" + dateTimeTo + "]");
			appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
			Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
			Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);

			switch (myQuery) {
			case 1: // branch only
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateTimeRangeBranchCategory(txnDateTimeFrom, txnDateTimeTo,
								LABORATORY, branch.getId(), pageable);

			case 2: // biller only
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateTimeRangeBillerCategory(txnDateTimeFrom, txnDateTimeTo,
								LABORATORY, biller.getId(), pageable);

			case 3: // branch & biller
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateTimeRangeBranchBillerCategory(txnDateTimeFrom, txnDateTimeTo,
								LABORATORY, branch.getId(), biller.getId(), pageable);

			case 4: // procedure
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateTimeRangeCategoryProcedure(txnDateTimeFrom, txnDateTimeTo,
								LABORATORY, procedure, pageable);

			case 5: // procedure & branch
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateTimeRangeBranchCategoryProcedure(txnDateTimeFrom,
								txnDateTimeTo, LABORATORY, procedure, branch.getId(), pageable);

			case 6: // procedure & biller
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateTimeRangeBillerCategoryProcedure(txnDateTimeFrom,
								txnDateTimeTo, LABORATORY, procedure, biller.getId(), pageable);

			case 7: // procedure & branch & biller
				return qisTransactionLaboratoryDetailsRepository
						.getTransactionsByTransactionDateTimeRangeBranchBillerCategoryProcedure(txnDateTimeFrom,
								txnDateTimeTo, LABORATORY, procedure, branch.getId(), biller.getId(), pageable);

			default:
				return qisTransactionLaboratoryDetailsRepository.getTransactionsByTransactionDateTimeRangeCategory(
						txnDateTimeFrom, txnDateTimeTo, LABORATORY, pageable);
			}
		}

		throw new RuntimeException("No parameters added on the request.",
				new Throwable("No parameters added on the request."));
	}

	// LIST OF TRANSACTIONS WITH LABORATORIES
	@GetMapping("laboratories")
	public Page<QisTransactionLaboratories> getAllTransactionsLaboratories(
			@RequestParam(required = false) String transactionDate, @RequestParam(required = false) String dateFrom,
			@RequestParam(required = false) String dateTo, @RequestParam(required = false) String dateTimeFrom,
			@RequestParam(required = false) String dateTimeTo, @RequestParam(required = false) String transactionType,
			@RequestParam(required = false) String notTransactionType, Pageable pageable,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View Transactions Laboratories");

		if (transactionType != null && notTransactionType != null) {
			throw new RuntimeException(
					"Invalid search parameters, inTypes and notInTypes should not be on the same request.",
					new Throwable(
							"Invalid search parameters, inTypes and notInTypes should not be on the same request."));
		}

		if (transactionDate != null) {
			appUtility.validateTransactionDate(transactionDate);
			Calendar txnDate = appUtility.stringToCalendarDate(transactionDate, SHORTDATE);

			if (transactionType != null) {
				return qisTransactionLaboratoriesRepository.getTransactionsByTransactionDateType(txnDate,
						transactionType, pageable);
			} else if (notTransactionType != null) {
				return qisTransactionLaboratoriesRepository.getTransactionsByTransactionDateNotType(txnDate,
						notTransactionType, pageable);
			} else {
				return qisTransactionLaboratoriesRepository.getTransactionsByTransactionDate(txnDate, pageable);
			}
		} else if (dateFrom != null || dateTo != null) {
			appUtility.validateDateFromTo(dateFrom, dateTo);
			Calendar txnDateFrom = appUtility.stringToCalendarDate(dateFrom, SHORTDATE);
			Calendar txnDateTo = appUtility.stringToCalendarDate(dateTo, SHORTDATE);

			if (transactionType != null) {
				return qisTransactionLaboratoriesRepository.getTransactionsByTransactionDateRangeType(txnDateFrom,
						txnDateTo, transactionType, pageable);
			} else if (notTransactionType != null) {
				return qisTransactionLaboratoriesRepository.getTransactionsByTransactionDateRangeNotType(txnDateFrom,
						txnDateTo, notTransactionType, pageable);
			} else {
				return qisTransactionLaboratoriesRepository.getTransactionsByTransactionDateRange(txnDateFrom,
						txnDateTo, pageable);
			}
		} else if (dateTimeFrom != null || dateTimeTo != null) {
			appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
			Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
			Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);

			if (transactionType != null) {
				return qisTransactionLaboratoriesRepository.getTransactionsByTransactionDateTimeRangeType(
						txnDateTimeFrom, txnDateTimeTo, transactionType, pageable);
			} else if (notTransactionType != null) {
				return qisTransactionLaboratoriesRepository.getTransactionsByTransactionDateTimeRangeNotType(
						txnDateTimeFrom, txnDateTimeTo, notTransactionType, pageable);
			} else {
				return qisTransactionLaboratoriesRepository.getTransactionsByTransactionDateTimeRange(txnDateTimeFrom,
						txnDateTimeTo, pageable);
			}
		}

		throw new RuntimeException("No parameters added on the request.",
				new Throwable("No parameters added on the request."));
	}

	// LIST OF TRANSACTIONS WITH LABORATORIES
	@GetMapping("quest_quality")
	public Page<QisQualityTransaction> getQuestQualityTransactions(
			@RequestParam(required = false) String transactionDate, @RequestParam(required = false) String dateFrom,
			@RequestParam(required = false) String dateTo, @RequestParam(required = false) String dateTimeFrom,
			@RequestParam(required = false) String dateTimeTo, @RequestParam(required = false) String branchId,
			@RequestParam(required = false) String company, @RequestParam(required = false) String chargeTo,
			@RequestParam(required = false) String patientId,
			Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View Quest Quality Transactions");

		Page<QisQualityTransaction> list = appQuestQualityUtility.getQisQualityTransactionList(branchId, chargeTo,
				company, patientId, transactionDate, dateFrom, dateTo, dateTimeFrom, dateTimeTo, pageable);

		if (list != null) {
			return list;
		}

		throw new RuntimeException("No parameters added on the request.",
				new Throwable("No parameters added on the request."));
	}
	
	//LIST OF TRANSACTIONS PER PATIENT
		@GetMapping("quest_quality/patient")
		public ResponseEntity<String> getQuestQualityTransactionsPatient(HttpServletResponse response,
				@RequestParam(required = false) String transactionDate, @RequestParam(required = false) String dateFrom,
				@RequestParam(required = false) String dateTo, @RequestParam(required = false) String dateTimeFrom,
				@RequestParam(required = false) String dateTimeTo, @RequestParam(required = false) String branchId,
				@RequestParam(required = false) String company, @RequestParam(required = false) String chargeTo,
				@RequestParam(required = false) String patientId, 
				@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

			LOGGER.info(authUser.getId() + "-PDF Quest Quality Transactions");

			int page = 0;
			int size = 20;
			Pageable pageable = PageRequest.of(page, size);
			Page<QisQualityTransaction> list;
			List<QisQualityTransaction> mainList = new ArrayList<QisQualityTransaction>();

			
			do {
				pageable = PageRequest.of(page, size);
				page++;

				list = appQuestQualityUtility.getQisQualityTransactionList(branchId, chargeTo, company, patientId, transactionDate,
						dateFrom, dateTo, dateTimeFrom, dateTimeTo, pageable);

				mainList.addAll(list.getContent());

			} while (page < list.getTotalPages());
			
			
			Document document = new Document(PageSize.LEGAL);
			document.open();
			PdfWriter pdfWriter = null;
			if (response != null) {
				pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
			}
			
			TxnPatientRecords certificate = new TxnPatientRecords(applicationName, applicationHeader,
					applicationFooter, appUtility);
			certificate.getPatientTxnRecords(document, pdfWriter, mainList, true);
			document.addTitle("RECORDS");
			
			response.setContentType("application/pdf");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=_results.pdf";
			response.setHeader(headerKey, headerValue);

			return new ResponseEntity<>("Generate Successfully", HttpStatus.OK);
		}

	// LIST OF TRANSACTIONS WITH LABORATORIES
	@GetMapping("quest_quality/excel")
	public ResponseEntity<String> getQuestQualityTransactionsExcel(HttpServletResponse response,
			@RequestParam(required = false) String transactionDate, @RequestParam(required = false) String dateFrom,
			@RequestParam(required = false) String dateTo, @RequestParam(required = false) String dateTimeFrom,
			@RequestParam(required = false) String dateTimeTo, @RequestParam(required = false) String branchId,
			@RequestParam(required = false) String company, @RequestParam(required = false) String chargeTo,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Excel Quest Quality Transactions");

		int page = 0;
		int size = 20;
		Pageable pageable = PageRequest.of(page, size);
		Page<QisQualityTransaction> list;
		List<QisQualityTransaction> mainList = new ArrayList<QisQualityTransaction>();

		do {
			pageable = PageRequest.of(page, size);
			page++;

			list = appQuestQualityUtility.getQisQualityTransactionList(branchId, chargeTo, company, "", transactionDate,
					dateFrom, dateTo, dateTimeFrom, dateTimeTo, pageable);

			mainList.addAll(list.getContent());

		} while (page < list.getTotalPages());
		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=quest_quality.xlsx";
		response.setHeader(headerKey, headerValue);

		QuestQualityExport questQualityExport = new QuestQualityExport(mainList, applicationName, appUtility,
				appExcelUtility, dateTimeFrom, dateTimeTo);
		questQualityExport.export(response);

		return new ResponseEntity<>("Generate Successfully", HttpStatus.OK);
	}

	@GetMapping("quest_quality/cupmedar/excel")
	public void getQuestQualityTransactionsCupmedarExcel(HttpServletResponse response,
			@RequestParam(required = false) String transactionDate, @RequestParam(required = false) String dateFrom,
			@RequestParam(required = false) String dateTo, @RequestParam(required = false) String dateTimeFrom,
			@RequestParam(required = false) String dateTimeTo, @RequestParam(required = false) String branchId,
			@RequestParam(required = false) String chargeTo, @RequestParam(required = false) String company,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Excel Quest Quality Transactions");
		String Chargeto = null;
		if (chargeTo != null) {
			Chargeto = chargeTo;
		}

		int page = 0;
		int size = 20;
		Pageable pageable = PageRequest.of(page, size);
		Page<QisQualityTransaction> list;
		List<QisQualityTransaction> mainList = new ArrayList<QisQualityTransaction>();

		do {
			pageable = PageRequest.of(page, size);
			page++;

			list = appQuestQualityUtility.getQisQualityTransactionList(branchId, chargeTo, company, "", transactionDate,
					dateFrom, dateTo, dateTimeFrom, dateTimeTo, pageable);

			mainList.addAll(list.getContent());
		} while (page < list.getTotalPages());
		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=quest_quality.xlsx";
		response.setHeader(headerKey, headerValue);

		QuestQualityCupmedarExport questQualityExport = new QuestQualityCupmedarExport(mainList, Chargeto, company,
				applicationName, appUtility, appExcelUtility, dateTimeFrom, dateTimeTo);
		questQualityExport.export(response);
	}

	// LIST OF TRANSACTIONS WITH LABORATORIES
	@GetMapping("item_laboratories")
	public Page<QisTransactionItemLaboratories> getAllTransactionItemsLaboratories(
			@RequestParam(required = false) String transactionDate, @RequestParam(required = false) String dateFrom,
			@RequestParam(required = false) String dateTo, @RequestParam(required = false) String dateTimeFrom,
			@RequestParam(required = false) String dateTimeTo, @RequestParam(required = false) String itemType,
			@RequestParam(required = false) String branchId, @RequestParam(required = false) String chargeTo,
			Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View Transaction Items Laboratories");

		if (itemType != null) {
			if (!"ITM".equals(itemType) && !"PCK".equals(itemType)) {
				throw new RuntimeException("Invalid Item Type.", new Throwable("itemType:Invalid Item Type."));
			}
		}

		QisBranch branch = null;
		if (branchId != null) {
			branch = appUtility.getQisBranchByBranchId(branchId);
			if (branch == null) {
				throw new RuntimeException("Invalid branch id.", new Throwable("branchId: Invalid branch id."));
			}
		}

		QisCorporate biller = null;
		if (chargeTo != null) {
			biller = appUtility.getQisCorporateByCorporateId(chargeTo);
			if (biller == null) {
				throw new RuntimeException("Invalid charge to account.",
						new Throwable("chargeTo: Invalid charge to account."));
			}
		}

		int myQuery = 0;
		if (branch != null) {
			myQuery += 1;
		}

		if (biller != null) {
			myQuery += 2;
		}

		if (itemType != null) {
			myQuery += 4;
		}

		if (transactionDate != null) {
			appUtility.validateTransactionDate(transactionDate);
			Calendar txnDate = appUtility.stringToCalendarDate(transactionDate, SHORTDATE);

			switch (myQuery) {
			case 1: // branch only
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateBranch(txnDate,
						branch.getId(), pageable);

			case 2: // biller only
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateBiller(txnDate,
						biller.getId(), pageable);

			case 3: // branch & biller
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateBranchBiller(
						txnDate, branch.getId(), biller.getId(), pageable);

			case 4: // itemType
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateType(txnDate,
						itemType, pageable);

			case 5: // branch & itemType
				return qisTransactionItemLaboratoriesRepositories
						.getTransactionItemsByTransactionDateBranchType(txnDate, itemType, branch.getId(), pageable);

			case 6: // biller & itemType
				return qisTransactionItemLaboratoriesRepositories
						.getTransactionItemsByTransactionDateBillerType(txnDate, itemType, biller.getId(), pageable);

			case 7: // branch & biller & itemType
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateBranchBillerType(
						txnDate, itemType, branch.getId(), biller.getId(), pageable);

			default:
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDate(txnDate,
						pageable);
			}

		} else if (dateFrom != null || dateTo != null) {
			appUtility.validateDateFromTo(dateFrom, dateTo);
			Calendar txnDateFrom = appUtility.stringToCalendarDate(dateFrom, SHORTDATE);
			Calendar txnDateTo = appUtility.stringToCalendarDate(dateTo, SHORTDATE);

			switch (myQuery) {
			case 1: // branch only
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateRangeBranch(
						txnDateFrom, txnDateTo, branch.getId(), pageable);

			case 2: // biller only
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateRangeBiller(
						txnDateFrom, txnDateTo, biller.getId(), pageable);

			case 3: // branch & biller
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateRangeBranchBiller(
						txnDateFrom, txnDateTo, branch.getId(), biller.getId(), pageable);

			case 4: // itemType
				return qisTransactionItemLaboratoriesRepositories
						.getTransactionItemsByTransactionDateRangeType(txnDateFrom, txnDateTo, itemType, pageable);

			case 5: // branch & itemType
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateRangeBranchType(
						txnDateFrom, txnDateTo, itemType, branch.getId(), pageable);

			case 6: // biller & itemType
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateRangeBillerType(
						txnDateFrom, txnDateTo, itemType, biller.getId(), pageable);

			case 7: // branch & biller & itemType
				return qisTransactionItemLaboratoriesRepositories
						.getTransactionItemsByTransactionDateRangeBranchBillerType(txnDateFrom, txnDateTo, itemType,
								branch.getId(), biller.getId(), pageable);

			default:
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateRange(txnDateFrom,
						txnDateTo, pageable);
			}

		} else if (dateTimeFrom != null || dateTimeTo != null) {
			appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
			Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
			Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);

			switch (myQuery) {
			case 1: // branch only
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateTimeRangeBranch(
						txnDateTimeFrom, txnDateTimeTo, branch.getId(), pageable);

			case 2: // biller only
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateTimeRangeBiller(
						txnDateTimeFrom, txnDateTimeTo, biller.getId(), pageable);

			case 3: // branch & biller
				return qisTransactionItemLaboratoriesRepositories
						.getTransactionItemsByTransactionDateTimeRangeBranchBiller(txnDateTimeFrom, txnDateTimeTo,
								branch.getId(), biller.getId(), pageable);

			case 4: // itemType
				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateTimeRangeType(
						txnDateTimeFrom, txnDateTimeTo, itemType, pageable);

			case 5: // branch & itemType
				return qisTransactionItemLaboratoriesRepositories
						.getTransactionItemsByTransactionDateTimeRangeBranchType(txnDateTimeFrom, txnDateTimeTo,
								itemType, branch.getId(), pageable);

			case 6: // biller & itemType
				return qisTransactionItemLaboratoriesRepositories
						.getTransactionItemsByTransactionDateTimeRangeBillerType(txnDateTimeFrom, txnDateTimeTo,
								itemType, biller.getId(), pageable);

			case 7: // branch & biller & itemType
				return qisTransactionItemLaboratoriesRepositories
						.getTransactionItemsByTransactionDateTimeRangeBranchBillerType(txnDateTimeFrom, txnDateTimeTo,
								itemType, branch.getId(), biller.getId(), pageable);

			default:
				return qisTransactionItemLaboratoriesRepositories
						.getTransactionItemsByTransactionDateTimeRange(txnDateTimeFrom, txnDateTimeTo, pageable);
			}

//			if (itemType != null) {
//				return qisTransactionItemLaboratoriesRepositories.getTransactionItemsByTransactionDateTimeRangeType(
//						txnDateTimeFrom, txnDateTimeTo, itemType, pageable);
//			} else {
//				return qisTransactionItemLaboratoriesRepositories
//						.getTransactionItemsByTransactionDateTimeRange(txnDateTimeFrom, txnDateTimeTo, pageable);
//			}
		}

		throw new RuntimeException("No parameters added on the request.",
				new Throwable("No parameters added on the request."));
	}

	// LIST OF TRANSACTIONS
	@GetMapping("summary")
	public Page<QisTransaction> getAllTransactions(@RequestParam(required = false) String transactionDate,
			@RequestParam(required = false) String dateFrom, @RequestParam(required = false) String dateTo,
			@RequestParam(required = false) String dateTimeFrom, @RequestParam(required = false) String dateTimeTo,
			Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View Transaction Summary");

		if (transactionDate != null) {

			appUtility.validateTransactionDate(transactionDate);
			Calendar txnDate = appUtility.stringToCalendarDate(transactionDate, SHORTDATE);

			return qisTransactionRepository.getTransactionsByTransactionDate(txnDate, pageable);
		} else if (dateFrom != null || dateTo != null) {
			appUtility.validateDateFromTo(dateFrom, dateTo);
			Calendar txnDateFrom = appUtility.stringToCalendarDate(dateFrom, SHORTDATE);
			Calendar txnDateTo = appUtility.stringToCalendarDate(dateTo, SHORTDATE);

			return qisTransactionRepository.getTransactionsByTransactionDateRange(txnDateFrom, txnDateTo, pageable);
		} else if (dateTimeFrom != null || dateTimeTo != null) {
			appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
			Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
			Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);

			return qisTransactionRepository.getTransactionsByTransactionDateTimeRange(txnDateTimeFrom, txnDateTimeTo,
					pageable);
		}

		throw new RuntimeException("No parameters added on the request.",
				new Throwable("No parameters added on the request."));
	}

	// EXPORT TRANSACTION
	@GetMapping("exportTransaction")
	public void getAllTransaction(@RequestParam(required = false) String transactionDate, HttpServletResponse response,
			@RequestParam(required = false) String dateFrom, @RequestParam(required = false) String dateTo,
			@RequestParam(required = false) String dateTimeFrom, @RequestParam(required = false) String dateTimeTo,
			Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View Transaction Summary");
		Page<QisTransaction> list = null;
		List<QisTransaction> mainList = new ArrayList<QisTransaction>();
		Page<QisTransactionLaboratoryRequest> listLabReq = null;
		List<QisTransactionLaboratoryRequest> mainListLabReq = new ArrayList<QisTransactionLaboratoryRequest>();
		int page = 0;
		int size = 1000;
		if (dateTimeFrom != null || dateTimeTo != null) {
			appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
			Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
			Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);
			do {
				pageable = PageRequest.of(page, size);
				page++;

				list = qisTransactionRepository.getTransactionsByTransactionDateTimeRange(txnDateTimeFrom,
						txnDateTimeTo, pageable);

				listLabReq = qistransactionILabReq.getLaboratoryRequestByTransactionDateTimeRange(txnDateTimeFrom,
						txnDateTimeTo, pageable, "PE");

				if (listLabReq.getContent().size() != 0) {
					mainListLabReq.addAll(listLabReq.getContent());
				}
				mainList.addAll(list.getContent());
			} while (page < list.getTotalPages());

			QisTransactionPhysicalExamination physicalExam = null;
			List<QisTransactionPhysicalExamination> labreq = new ArrayList<QisTransactionPhysicalExamination>();
			int labsize = mainListLabReq.size() - 1;
			for (int i = 0; i < mainList.size(); i++) {
				int y = 0;
				for (y = 0; y < labsize; y++) {
					int id = mainList.get(i).getId().intValue();
					int labId = mainListLabReq.get(y).getTransactionid().intValue();
					if (id == labId) {
						physicalExam = qisTransactionPhysicalExaminationRepository.getTransactionLaboratoryRequestById(
								mainList.get(i).getId(), mainListLabReq.get(y).getId());
						labreq.add(physicalExam);
					}
				}
			}

			response.setContentType("text/csv");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=transactionList.xlsx";
			response.setHeader(headerKey, headerValue);

			TransactionList TransactionList = new TransactionList(mainList, labreq, applicationName, appUtility,
					appExcelUtility);
			TransactionList.export(response);
		}
	}

	// SEARCH TRANSACTIONS
	@GetMapping("search")
	public Page<QisTransaction> getSearchTransactions(@RequestParam String searchKey, @RequestParam String searchType,
			Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Search Transaction [" + searchKey + "]<" + searchType + ">");

		if ("salesReceipt".equals(searchType)) {
			try {
				Long id = Long.parseLong(searchKey);
				if (id != null && id > 0) {
					return qisTransactionRepository.getTransactionsByTransactionId(id, pageable);
				} else {
					throw new RuntimeException("Invalid search key.", new Throwable("searchKey: Invalid search key."));
				}
			} catch (Exception e) {
				throw new RuntimeException("Invalid search key.", new Throwable("searchKey: Invalid search key."));
			}
		} else if ("patientName".equals(searchType)) {
			String[] name = searchKey.split(",");
			if (name.length < 1 || name.length > 2) {
				throw new RuntimeException("Invalid search key.", new Throwable("searchKey: Invalid search key."));
			}

			if (name.length == 1) {
				return qisTransactionRepository.getTransactionsByPatientName(name[0].trim(), pageable);
			} else if (name.length == 2) {
				return qisTransactionRepository.getTransactionsByPatientLastFirstName(name[0].trim(), name[1].trim(),
						pageable);
			}
			throw new RuntimeException("OK");
		}

		throw new RuntimeException("Invalid search type.", new Throwable("searchType: Invalid search type."));
	}

	// HOLD TRANSACTIONS
	@GetMapping("hold")
	public Page<QisTransaction> getAllHoldTransactions(Pageable pageable,
			@AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info(authUser.getId() + "-On Hold Transactions");
		return qisTransactionRepository.getTransactionsByStatus("SHO", pageable);
	}

	// LIST OF PENDING
	@GetMapping("pending")
	public List<QisQualityTransaction> getAllPendingTransactions(@AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info(authUser.getId() + "-On Pending Transactions");

		return qisQualityRepository.getAllPendingTransaction();
	}

	// LIST OF PENDING BY PAST 3 DAYS
	@GetMapping("pending_three_days")
	public List<QisQualityTransaction> getPendingTransactionsByThreeDays(
			@AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info(authUser.getId() + "-On Pending Transactions");
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, -3);
		Calendar endDate = Calendar.getInstance();

		return qisQualityRepository.getPendingTransactionByDate(startDate, endDate);
	}

	// LIST OF PENDING BY 1 MONTH
	@GetMapping("pending_over_than_three_days")
	public List<QisQualityTransaction> getPendingTransactionsByThirtyDays(
			@AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info(authUser.getId() + "-On Pending Transactions");
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, -30);
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, -4);

		return qisQualityRepository.getPendingTransactionByDate(startDate, endDate);
	}

	// PENDING COUNT
	@GetMapping("pending_count")
	public int getPendingCount(@AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info(authUser.getId() + "-On Pending Count");
		List<QisQualityTransaction> listPending = qisQualityRepository.getAllPendingTransaction();
		int pendingCount = 0;

		for (@SuppressWarnings("unused")
		QisQualityTransaction count : listPending) {
			pendingCount++;
		}
		return pendingCount;
	}

}