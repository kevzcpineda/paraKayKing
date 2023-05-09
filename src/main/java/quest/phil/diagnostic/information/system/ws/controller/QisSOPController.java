package quest.phil.diagnostic.information.system.ws.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppEmailUtility;
import quest.phil.diagnostic.information.system.ws.common.AppExcelUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.common.documents.ReferenceToSOP;
import quest.phil.diagnostic.information.system.ws.common.exports.SOPExport;
import quest.phil.diagnostic.information.system.ws.model.QISSOPSummary;
import quest.phil.diagnostic.information.system.ws.model.QisEmailConfig;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOP;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOPPayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;
import quest.phil.diagnostic.information.system.ws.model.request.QisSOPPaymentRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisSOPRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisReferenceLaboratoryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisSOPPaymentRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisSOPRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoriesRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryRequestRepository;

@RestController
@RequestMapping("/api/v1/transactions/")
public class QisSOPController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisSOPController.class);
	private final String LONGDATE = "yyyyMMddHHmm";
	private final String SHORT_DATE = "yyyyMMdd";

	@Value("${application.name}")
	private String applicationName;

	@Value("${application.header}")
	private String applicationHeader;

	@Value("${application.footer}")
	private String applicationFooter;

	private QisEmailConfig emailConfig;

	public QisSOPController(QisEmailConfig QisEmailConfig) {
		this.emailConfig = QisEmailConfig;
	}

	@Autowired
	private AppExcelUtility appExcelUtility;

	@Autowired
	private QisTransactionLaboratoriesRepository qisTransactionLaboratoriesRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	QisReferenceLaboratoryRepository qisReferenceLaboratoryRepository;

	@Autowired
	QisSOPRepository qisSopRepository;

	@Autowired
	QisTransactionLaboratoryRequestRepository qisTransationRequestRepository;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;

	@Autowired
	private QisSOPPaymentRepository qisSOPPaymentRepository;

	@Autowired
	private AppEmailUtility emailUtility;

	// CREATE SOP
	@PostMapping("create_sop/{referenceId}")
	public QisSOP createSop(@PathVariable String referenceId, @RequestBody QisSOPRequest sopRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		QisSOP sop = new QisSOP();
		sop.setCoverageDateFrom(appUtility.stringToCalendarDate(sopRequest.getCoveredDateFrom(), SHORT_DATE));
		sop.setCoverageDateTo(appUtility.stringToCalendarDate(sopRequest.getCoveredDateTo(), SHORT_DATE));
		sop.setStatementDate(appUtility.stringToCalendarDate(sopRequest.getStatementDate(), SHORT_DATE));

		Double amount = appUtility.parseDoubleAmount(sopRequest.getSopAmount());
		if (amount == null) {
			throw new RuntimeException("Invalid SOA Total Amount.",
					new Throwable("soaAmount: Invalid SOA Total Amount."));
		}

		if (sop.getCoverageDateFrom() == null) {
			throw new RuntimeException("Invalid Coverage Date From.",
					new Throwable("coveredDateFrom: Invalid Coverage Date From."));
		}

		if (sop.getCoverageDateTo() == null) {
			throw new RuntimeException("Invalid Coverage Date To.",
					new Throwable("coveredDateTo: Invalid Coverage Date From."));
		}

		if (sop.getStatementDate() == null) {
			throw new RuntimeException("Invalid Statement Date.",
					new Throwable("statementDate: Invalid Statement Date."));
		}

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceId);
		if (referenceLab == null) {
			throw new RuntimeException("Record not found.", new Throwable("referenceId: Record not found."));
		}

		Set<QisTransactionLaboratories> transactions = getTransactionList(sopRequest.getTransactions(), referenceId);
		if (transactions.isEmpty()) {
			throw new RuntimeException("Transaction List should not be empty.",
					new Throwable("transactions: Transaction List should not be empty."));
		}
		int count = 0;
		QisSOP prev = qisSopRepository.getLastSOPStatements(referenceLab.getId());

		if (prev != null) {
			count = prev.getSopCount();
		}
		count++;

		sop.setReferenceId(referenceLab.getId());
		sop.setSopAmount(amount);
		sop.setCreatedBy(authUser.getId());
		sop.setUpdatedBy(authUser.getId());
		sop.setTransactions(transactions);
		sop.setSopCount(count);
		sop.setSopNumber(appUtility.calendarToFormatedDate(sop.getStatementDate(), "YY") + referenceLab.getSopCode()
				+ appUtility.numberFormat(Long.valueOf(count), "0000"));

		qisSopRepository.save(sop);

		return sop;
	}

	// Verify
	@PutMapping("sop/{referenceId}/verify/{sopId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOP verifySOA(@PathVariable String referenceId, @PathVariable Long sopId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Verify of SOP:" + referenceId + " id:" + sopId);

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceId);
		if (referenceLab == null) {
			throw new RuntimeException("Record not found.", new Throwable("referenceId: Record not found."));
		}

		QisSOP sop = qisSopRepository.getSopById(sopId, referenceLab.getId());
		if (sop == null) {
			throw new RuntimeException("Record not found.", new Throwable("soaId: Record not found."));
		}

		if (sop.getVerifiedBy() != null) {
			throw new RuntimeException("SOA already verified.", new Throwable("soaId: SOP already verified."));
		}

		if (sop.getCreatedBy().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to verify the same transaction.",
					new Throwable("You are not authorize to verify the same transaction."));
		}

		sop.setVerifiedBy(authUser.getId());
		sop.setVerifiedDate(Calendar.getInstance());

		sop.setUpdatedBy(authUser.getId());
		sop.setUpdatedAt(Calendar.getInstance());
		qisSopRepository.save(sop);

		return sop;
	}

	// NOTED SOA
	@PutMapping("sop/{referenceId}/noted/{sopId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOP notedSOA(@PathVariable String referenceId, @PathVariable Long sopId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Notify of SOA:" + referenceId + " id:" + sopId);
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceId);
		if (referenceLab == null) {
			throw new RuntimeException("Record not found.", new Throwable("referenceId: Record not found."));
		}

		QisSOP sop = qisSopRepository.getSopById(sopId, referenceLab.getId());
		if (sop == null) {
			throw new RuntimeException("Record not found.", new Throwable("soaId: Record not found."));
		}

		if (sop.getVerifiedBy() == null) {
			throw new RuntimeException("SOA is not yet verified.", new Throwable("soaId: SOA is not yet verified."));
		}

		if (sop.getNotedBy() != null) {
			throw new RuntimeException("SOA already notified.", new Throwable("soaId: SOA already notified."));
		}

		if (sop.getCreatedBy().equals(authUser.getId()) || sop.getVerifiedBy().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to notify the same transaction.",
					new Throwable("You are not authorize to notify the same transaction."));
		}

		sop.setNotedBy(authUser.getId());
		sop.setNotedDate(Calendar.getInstance());

		sop.setUpdatedBy(authUser.getId());
		sop.setUpdatedAt(Calendar.getInstance());
		qisSopRepository.save(sop);

		return sop;
	}

	private Set<QisTransactionLaboratories> getTransactionList(Set<Long> transactions, String referenceId) {
		Set<QisTransactionLaboratories> list = new HashSet<>();

		for (Long id : transactions) {
			QisTransactionLaboratories txn = qisTransactionLaboratoriesRepository.getSOPTransactionById(id);
			if (txn != null) {
				List<QisTransactionLaboratories> transactionlaboratoryRequest = qisTransactionLaboratoriesRepository
						.getTransactionsByTransactionById(txn.getId());
				for (QisTransactionLaboratories labReq : transactionlaboratoryRequest) {
					QisTransactionLaboratories dataReqLab = qisTransactionLaboratoriesRepository
							.getTransactionsRequestById(labReq.getId());
					for (QisTransactionLaboratoryInfo reqLab : dataReqLab.getTransactionLabRequests()) {
						if (reqLab.getReferenceLab() != null) {
							QisTransactionLaboratoryDetails laboratoryRequest = qisTransactionLaboratoryDetailsRepository
									.getTransactionsById(reqLab.getId());
							if (referenceId.equals(laboratoryRequest.getReferenceLab().getReferenceid())) {
								laboratoryRequest.setSopStatus(true);
								qisTransactionLaboratoryDetailsRepository.save(laboratoryRequest);
							}
						}
					}
				}
				list.add(txn);
			}
		}
		return list;
	}


	// Get list of transaction with send outs
	@GetMapping("list_send_outs")
	public List<QisTransactionLaboratories> getAllTransactionsLaboratories(Pageable pageable,
			@RequestParam(required = false) String dateTimeFrom, @RequestParam(required = false) String dateTimeTo,
			@RequestParam(required = false) String referenceLab, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {

		LOGGER.info(authUser.getId() + "-View Send Out Lists");

		appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
		Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
		Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);

		Page<QisTransactionLaboratories> lists = null;
		List<QisTransactionLaboratories> labreq = new ArrayList<QisTransactionLaboratories>();
		List<QisTransactionLaboratories> mainList = new ArrayList<QisTransactionLaboratories>();
		List<QisTransactionLaboratories> mainListWithReference = new ArrayList<QisTransactionLaboratories>();
		QisReferenceLaboratory referenceLabData = null;
		if (!"".equals(referenceLab)) {
			referenceLabData = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceLab);
		}
		int page = 0;
		int size = 20;
		do {
			pageable = PageRequest.of(page, size);
			page++;
			lists = qisTransactionLaboratoriesRepository.getTransactionsByTransaction(txnDateTimeFrom, txnDateTimeTo,
					pageable);

			labreq.addAll(lists.getContent());
		} while (page < lists.getTotalPages());

		for (QisTransactionLaboratories txn : labreq) {
			for (QisTransactionLaboratoryInfo item : txn.getTransactionLabRequests()) {
				if (!item.getSopStatus()) {

					// Ultrasound
					if (item.getUltrasound() != null) {
						if (referenceLabData != null) {
							if (item.getUltrasound().getReferenceLabId() == referenceLabData.getId()) {
								if (!mainListWithReference.contains(txn)) {
									mainListWithReference.add(txn);
								}
							}
						} else {
							if (!mainList.contains(txn)) {
								mainList.add(txn);
							}
						}
					}
					// Xray
					if (item.getXray() != null) {
						if (referenceLabData != null) {
							if (item.getXray().getReferenceLabId() == referenceLabData.getId()) {
								if (!mainListWithReference.contains(txn)) {
									mainListWithReference.add(txn);
								}
							}
						} else {
							if (!mainList.contains(txn)) {
								mainList.add(txn);
							}
						}
					}

					// CHEMISTRY
					if (item.getChemistry().getBilirubin() != null) {
						if (item.getChemistry().getBilirubin().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getBilirubin().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getBun() != null) {
						if (item.getChemistry().getBun().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getBun().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getCpk() != null) {
						if (item.getChemistry().getCpk().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getCpk().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getCreatinine() != null) {
						if (item.getChemistry().getCreatinine().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getCreatinine().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getElectrolytes() != null) {
						if (item.getChemistry().getElectrolytes().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getElectrolytes().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getEnzymes() != null) {
						if (item.getChemistry().getEnzymes().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getEnzymes().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getFbs() != null) {
						if (item.getChemistry().getFbs().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getFbs().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getHemoglobin() != null) {
						if (item.getChemistry().getHemoglobin().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getHemoglobin().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getLipidProfile() != null) {
						if (item.getChemistry().getLipidProfile().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getLipidProfile().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getOgct() != null) {
						if (item.getChemistry().getOgct().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getOgct().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getOgtt() != null) {
						if (item.getChemistry().getOgtt().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getOgtt().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getPprbs() != null) {
						if (item.getChemistry().getPprbs().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getPprbs().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getProtein() != null) {
						if (item.getChemistry().getProtein().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getProtein().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getRbs() != null) {
						if (item.getChemistry().getRbs().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getRbs().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getChemistry().getUricAcid() != null) {
						if (item.getChemistry().getUricAcid().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getChemistry().getUricAcid().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					// HEMATOLOGY
					if (item.getHematology().getAptt() != null) {
						if (item.getHematology().getAptt().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getHematology().getAptt().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getHematology().getBloodTyping() != null) {
						if (item.getHematology().getBloodTyping().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getHematology().getBloodTyping().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getHematology().getCbc() != null) {
						if (item.getHematology().getCbc().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getHematology().getCbc().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getHematology().getCtbt() != null) {
						if (item.getHematology().getCtbt().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getHematology().getCtbt().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getHematology().getEsr() != null) {
						if (item.getHematology().getEsr().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getHematology().getEsr().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getHematology().getPrms() != null) {
						if (item.getHematology().getPrms().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getHematology().getPrms().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getHematology().getProthombinTime() != null) {
						if (item.getHematology().getProthombinTime().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getHematology().getProthombinTime().getReferenceLab()
										.getId() == referenceLabData.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					// CLINICAL MICROSCOPY
					if (item.getClinicalMicroscopy().getAfb() != null) {
						if (item.getClinicalMicroscopy().getAfb().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getClinicalMicroscopy().getAfb().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getClinicalMicroscopy().getFecalysis() != null) {
						if (item.getClinicalMicroscopy().getFecalysis().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getClinicalMicroscopy().getFecalysis().getReferenceLab()
										.getId() == referenceLabData.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getClinicalMicroscopy().getPtobt() != null) {
						if (item.getClinicalMicroscopy().getPtobt().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getClinicalMicroscopy().getPtobt().getReferenceLab()
										.getId() == referenceLabData.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getClinicalMicroscopy().getUrineChemical() != null) {
						if (item.getClinicalMicroscopy().getUrineChemical().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getClinicalMicroscopy().getUrineChemical().getReferenceLab()
										.getId() == referenceLabData.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					// TOXICOLOGY
					if (item.getToxicology() != null) {
						if (item.getToxicology().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getToxicology().getReferenceLabId() == referenceLabData.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					// SEROLOGY
					if (item.getSerology().getSerology() != null) {
						if (item.getSerology().getSerology().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getSerology().getSerology().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getSerology().getAntigen() != null) {
						if (item.getSerology().getAntigen().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getSerology().getAntigen().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getSerology().getCovid() != null) {
						if (item.getSerology().getCovid().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getSerology().getCovid().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getSerology().getCrp() != null) {
						if (item.getSerology().getCrp().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getSerology().getCrp().getReferenceLab().getId() == referenceLabData.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getSerology().getHiv() != null) {
						if (item.getSerology().getHiv().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getSerology().getHiv().getReferenceLab().getId() == referenceLabData.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getSerology().getRtpcr() != null) {
						if (item.getSerology().getRtpcr().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getSerology().getRtpcr().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getSerology().getThyroid() != null) {
						if (item.getSerology().getThyroid().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getSerology().getThyroid().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}

					if (item.getSerology().getTyphidot() != null) {
						if (item.getSerology().getTyphidot().getReferenceLab() != null) {
							if (referenceLabData != null) {
								if (item.getSerology().getTyphidot().getReferenceLab().getId() == referenceLabData
										.getId()) {
									if (!mainListWithReference.contains(txn)) {
										mainListWithReference.add(txn);
									}
								}
							} else {
								if (!mainList.contains(txn)) {
									mainList.add(txn);
								}
							}
						}
					}
				}
			}
		}

		return referenceLabData != null ? mainListWithReference : mainList;
	}

	// EXCEL SOP
	@GetMapping("sop/{referenceId}/excel/{sopId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public void getSOPExcel(HttpServletResponse response, @PathVariable String referenceId, @PathVariable Long sopId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Excel of SOA:" + referenceId + " id:" + sopId);
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceId);
		if (referenceLab == null) {
			throw new RuntimeException("Record not found.", new Throwable("referenceId: Record not found."));
		}

		QisSOP sop = qisSopRepository.getSopById(sopId, referenceLab.getId());
		if (sop == null) {
			throw new RuntimeException("Record not found.", new Throwable("soaId: Record not found."));
		}

		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=sop_" + sop.getSopNumber() + ".xlsx";
		response.setHeader(headerKey, headerValue);

		SOPExport sopExport = new SOPExport(applicationName, appUtility, appExcelUtility);
		sopExport.export(response, sop);

	}

	// GET LIST OF SOP
	@GetMapping("list_sop")
	public List<QisSOP> getListSOP(@RequestParam(required = false) String referenceId, Pageable pageable,
			@RequestParam(required = false) String dateTimeFrom, @RequestParam(required = false) String dateTimeTo,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		Page<QisSOP> list = null;
		List<QisSOP> mainLists = new ArrayList<QisSOP>();
		int page = 0;
		int size = 20;
		QisReferenceLaboratory referenceLabDetails = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(referenceId);
		appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
		Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
		Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);

		
		do {
			pageable = PageRequest.of(page, size);
			page++;
			list = qisSopRepository.getListSopByDate(txnDateTimeFrom, txnDateTimeTo, referenceLabDetails.getId(), pageable);
			mainLists.addAll(list.getContent());
		} while (page < list.getTotalPages());
		return mainLists;
	}

	// CREATE SOP PAYMENT
	@PostMapping("sop/{referenceId}/payment")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOPPayment SOAPayment(@PathVariable String referenceId,
			@Valid @RequestBody QisSOPPaymentRequest sopPayRequest, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-pay SOA Corporate:" + referenceId);
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceId);
		if (referenceLab == null) {
			throw new RuntimeException("Record not found.", new Throwable("referenceId: Record not found."));
		}

		Set<QisSOP> sopList = getSOAList(sopPayRequest.getSopList(), referenceLab.getId());
		if (sopList.isEmpty()) {
			throw new RuntimeException("SOA List should not be empty.",
					new Throwable("soaList: SOA List should not be empty."));
		}

		QisSOPPayment sopPayment = new QisSOPPayment();
		sopPayment.setPaymentDate(appUtility.stringToCalendarDate(sopPayRequest.getPaymentDate(), SHORT_DATE));
		Double amount = appUtility.parseDoubleAmount(sopPayRequest.getPaymentAmount());
		if (amount == null) {
			throw new RuntimeException("Invalid Payment Amount.",
					new Throwable("paymentAmount: Invalid Payment Amount."));
		}

		Double otherAmount = 0d;
		if (sopPayRequest.getOtherAmount() != null) {
			otherAmount = appUtility.parseDoubleAmount(sopPayRequest.getOtherAmount());
			if (otherAmount == null) {
				throw new RuntimeException("Invalid Other Amount.",
						new Throwable("otherAmount: Invalid Other Amount."));
			}
		}

		Double taxWithHeld = 0d;
		if (sopPayRequest.getOtherAmount() != null) {
			taxWithHeld = appUtility.parseDoubleAmount(sopPayRequest.getTaxWithHeld());
			if (taxWithHeld == null) {
				throw new RuntimeException("Invalid Other Amount.",
						new Throwable("otherAmount: Invalid Other Amount."));
			}
		}

		if (sopPayment.getPaymentDate() == null) {
			throw new RuntimeException("Invalid Payment Date.", new Throwable("paymentDate: Invalid Payment Date."));
		}

		String paymentType = appUtility.getSOAPaymentType(sopPayRequest.getPaymentType());
		if (paymentType == null) {
			throw new RuntimeException("Invalid Payment Type.", new Throwable("paymentType: Invalid Payment Type."));
		}

		if (!sopPayRequest.getPaymentType().equals("CA")) {
			if (sopPayRequest.getPaymentBank() == null || "".equals(sopPayRequest.getPaymentBank().trim())) {
				throw new RuntimeException("Payment Bank is required.",
						new Throwable("paymentBank: Payment Bank is required."));
			}

			String bank = appUtility.getBankType(sopPayRequest.getPaymentBank());
			if (bank == null) {
				throw new RuntimeException("Invalid Payment Bank.",
						new Throwable("paymentBank: Invalid Payment Bank."));
			}

			if (sopPayRequest.getAccountNumber() == null || "".equals(sopPayRequest.getAccountNumber().trim())) {
				throw new RuntimeException("Account Number is required.",
						new Throwable("accountNumber: Account Number is required."));
			}
		}
		sopPayment.setReferenceId(referenceLab.getId());
		sopPayment.setPaymentAmount(amount);
		sopPayment.setPaymentType(sopPayRequest.getPaymentType());
		sopPayment.setPaymentBank(sopPayRequest.getPaymentBank());
		sopPayment.setAccountNumber(sopPayRequest.getAccountNumber());
		sopPayment.setOtherAmount(otherAmount);
		sopPayment.setOtherNotes(sopPayRequest.getOtherNotes());
		sopPayment.setTaxWithHeld(taxWithHeld);
		sopPayment.setSoaList(sopList);
		sopPayment.setCreatedBy(authUser.getId());
		sopPayment.setUpdatedBy(authUser.getId());

		qisSOPPaymentRepository.save(sopPayment);

		return sopPayment;
	}

	private Set<QisSOP> getSOAList(Set<Long> idList, Long referenceId) {

		Set<QisSOP> list = new HashSet<>();

		for (Long ids : idList) {
			QisSOP sop = qisSopRepository.getSopById(ids, referenceId);
			if (sop != null) {
				sop.setSopStatus(1);
				qisSopRepository.save(sop);
				list.add(sop);

			}
		}

		return list;
	}

	@GetMapping("sop/{referenceId}/payments/{year}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public Page<QisSOPPayment> getSOAPaymentsYear(@PathVariable String referenceId, @PathVariable int year,
			Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-List of SOP:" + referenceId + " year:" + year);
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceId);
		if (referenceLab == null) {
			throw new RuntimeException("Record not found.", new Throwable("referenceId: Record not found."));
		}

		return qisSOPPaymentRepository.getSOPPaymentYear(referenceLab.getId(), year, pageable);
	}

	// VERIFY SOA PAYMENT
	@PutMapping("sop/{referenceId}/payment/{paymentId}/verify")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOPPayment verifySOAPayment(@PathVariable String referenceId, @PathVariable Long paymentId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-pay SOA Corporate:" + referenceId);
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceId);
		if (referenceLab == null) {
			throw new RuntimeException("Record not found.", new Throwable("referenceId: Record not found."));
		}

		QisSOPPayment sopPayment = qisSOPPaymentRepository.getSOPPaymentById(referenceLab.getId(), paymentId);
		if (sopPayment == null) {
			throw new RuntimeException("Record not found.", new Throwable("paymentId: Record not found."));
		}

		if (sopPayment.getVerifiedBy() != null) {
			throw new RuntimeException("SOA already verified.", new Throwable("soaId: SOA already verified."));
		}

		if (sopPayment.getCreatedBy().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to verify the same transaction.",
					new Throwable("You are not authorize to verify the same transaction."));
		}

		sopPayment.setVerifiedBy(authUser.getId());
		sopPayment.setVerifiedDate(Calendar.getInstance());

		sopPayment.setUpdatedBy(authUser.getId());
		sopPayment.setUpdatedAt(Calendar.getInstance());
		qisSOPPaymentRepository.save(sopPayment);

		return sopPayment;
	}

	// PRINT SOP
	@GetMapping("sop/{referenceId}/print/{sopId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public void getSOAPrint(HttpServletResponse response, @PathVariable String referenceId, @PathVariable Long sopId,
			@RequestParam(required = false) Boolean withHeaderFooter, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-Print of SOA:" + referenceId + " id:" + sopId);

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceId);
		if (referenceLab == null) {
			throw new RuntimeException("Record not found.", new Throwable("referenceId: Record not found."));
		}

		QisSOP sop = qisSopRepository.getSopById(sopId, referenceLab.getId());
		if (sop == null) {
			throw new RuntimeException("Record not found.", new Throwable("soaId: Record not found."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.addTitle(sop.getSopNumber());

		ReferenceToSOP referenceToSOP = new ReferenceToSOP(applicationName, applicationHeader, applicationFooter,
				appUtility);
		referenceToSOP.getChargeToSOA(document, pdfWriter, sop, withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + sop.getSopNumber() + "_soa.pdf";
		response.setHeader(headerKey, headerValue);

	}

	// SOP SUMMARY
	@GetMapping("sop/{referenceId}/summary/{year}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public List<QISSOPSummary> getSOASummaryYear(@PathVariable String referenceId, @PathVariable int year,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-SOA Summary:" + referenceId + " year:" + year);
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceId);
		if (referenceLab == null) {
			throw new RuntimeException("Record not found.", new Throwable("referenceId: Record not found."));
		}

		int page = 0;
		int size = 20;
		Pageable pageable = PageRequest.of(page, size);

		List<QISSOPSummary> summaryList = new ArrayList<QISSOPSummary>();
		Page<QisSOP> sopList;
		do {
			sopList = qisSopRepository.referenceToSOPYear(referenceLab.getId(), year, pageable);
			addSOPToSummary(sopList, summaryList);
			page++;
			pageable = PageRequest.of(page, size);
		} while (page < sopList.getTotalPages());

		page = 0;
		pageable = PageRequest.of(page, size);
		Page<QisSOPPayment> payList;
		do {
			payList = qisSOPPaymentRepository.getSOPPaymentYear(referenceLab.getId(), year, pageable);
			addSOPPaymentToSummary(payList, summaryList);
			page++;
			pageable = PageRequest.of(page, size);
		} while (page < payList.getTotalPages());

		Collections.sort(summaryList);

		return summaryList;
	}

	private void addSOPToSummary(Page<QisSOP> sopList, List<QISSOPSummary> summaryList) {
		if (sopList != null && summaryList != null) {
			for (QisSOP soa : sopList) {
				QISSOPSummary ss = new QISSOPSummary();
				ss.setDate(soa.getStatementDate());
				ss.setAmount(soa.getSopAmount());
				ss.setTransaction(soa.getSopNumber());
				ss.setType(1); // SOP

				summaryList.add(ss);
			}
		}
	}

	private void addSOPPaymentToSummary(Page<QisSOPPayment> payList, List<QISSOPSummary> summaryList) {
		if (payList != null && summaryList != null) {
			for (QisSOPPayment pay : payList) {
				QISSOPSummary ss = new QISSOPSummary();
				ss.setDate(pay.getPaymentDate());
				ss.setAmount(pay.getPaymentAmount());
				String payDetails = appUtility.getSOAPaymentType(pay.getPaymentType());
				String bank = appUtility.getBankType(pay.getPaymentBank());
				if (bank != null) {
					payDetails += "-" + bank;
				}
				if (pay.getAccountNumber() != null) {
					payDetails += ":" + pay.getAccountNumber();
				}

				ss.setTransaction("PAYMENT:" + payDetails);
				ss.setType(2); // SOP PAYMENTS
				ss.setOtherNotes(pay.getOtherNotes());
				ss.setOtherAmount(pay.getOtherAmount());

				summaryList.add(ss);
			}
		}

	}

	@PutMapping("sop/{referenceId}/payment/{paymentId}/upload_receipt")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOPPayment uploadPaymentReceipt(@PathVariable String referenceId, @PathVariable Long paymentId,
			@RequestParam(name = "uploadFile", required = false) MultipartFile uploadFile,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-upload SOA Corporate:" + referenceId);
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceId);
		if (referenceLab == null) {
			throw new RuntimeException("Record not found.", new Throwable("referenceId: Record not found."));
		}

		QisSOPPayment sopPayment = qisSOPPaymentRepository.getSOPPaymentById(referenceLab.getId(), paymentId);
		if (sopPayment == null) {
			throw new RuntimeException("Record not found.", new Throwable("paymentId: Record not found."));
		}

		if (uploadFile != null) {
			if (uploadFile.getSize() < 0) {

				InputStream inputStream = uploadFile.getInputStream();
				byte[] receipt = StreamUtils.copyToByteArray(inputStream);
				sopPayment.setReceipt(receipt);
				sopPayment.setImageType(uploadFile.getContentType());
				sopPayment.setUpdatedBy(authUser.getId());
				sopPayment.setUpdatedAt(Calendar.getInstance());
				qisSOPPaymentRepository.save(sopPayment);
				throw new RuntimeException("File size is too large.",
						new Throwable("File size is too large."));
			}

			}			

		else {
			if (sopPayment.getReceipt() != null) {
				sopPayment.setReceipt(null);
				sopPayment.setUpdatedBy(authUser.getId());
				sopPayment.setUpdatedAt(Calendar.getInstance());
				qisSOPPaymentRepository.save(sopPayment);


			}
		}

		return sopPayment;
	}

	// SEND SOP
	@PostMapping("sop/{referenceId}/send_email/{sopId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public void getSOASend(HttpServletResponse response, @PathVariable String referenceId, @PathVariable Long sopId,
			@RequestParam(name = "file", required = false) List<MultipartFile> fileInput,
			@RequestParam("sendTo") String sendTo, @RequestParam(name = "sendCc", required = false) String sendCc,
			@RequestParam("emailSubject") String emailSubject, @RequestParam("emailBody") String emailBody,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Print of SOA:" + referenceId + " id:" + sopId);

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository.getReferenceLaboratory(referenceId);
		if (referenceLab == null) {
			throw new RuntimeException("Record not found.", new Throwable("referenceId: Record not found."));
		}

		QisSOP sop = qisSopRepository.getSopById(sopId, referenceLab.getId());
		if (sop == null) {
			throw new RuntimeException("Record not found.", new Throwable("soaId: Record not found."));
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);

		document.addTitle(sop.getSopNumber());

		ReferenceToSOP referenceToSOP = new ReferenceToSOP(applicationName, applicationHeader, applicationFooter,
				appUtility);
		referenceToSOP.getChargeToSOA(document, pdfWriter, sop, true);

		byte[] bytes = baos.toByteArray();
		boolean ans = fileInput.isEmpty();

		if (ans == true) {
			fileInput = new ArrayList<>();
		}

		MultipartFile multipartFile = new MockMultipartFile("file", sop.getSopNumber() + ".pdf", "text/plain", bytes);

		if (multipartFile != null) {
			fileInput.add(multipartFile);
		}

		emailUtility.sendEmailSoa(emailConfig, sendTo, emailSubject, sendCc, emailBody, fileInput);
	}

}
