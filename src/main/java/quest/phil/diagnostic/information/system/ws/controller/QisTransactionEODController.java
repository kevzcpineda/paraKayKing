package quest.phil.diagnostic.information.system.ws.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppExcelUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.common.documents.ReceiptDocument;
import quest.phil.diagnostic.information.system.ws.common.exports.AuditorEODExport;
import quest.phil.diagnostic.information.system.ws.model.classes.QisDenominationReportsClass;
import quest.phil.diagnostic.information.system.ws.model.classes.QisEODClass;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisAdvancePayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOAPayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;
import quest.phil.diagnostic.information.system.ws.model.eod.Account;
import quest.phil.diagnostic.information.system.ws.model.eod.AuditorEODData;
import quest.phil.diagnostic.information.system.ws.model.eod.CashierEODData;
import quest.phil.diagnostic.information.system.ws.model.eod.EODData;
import quest.phil.diagnostic.information.system.ws.model.eod.Item;
import quest.phil.diagnostic.information.system.ws.model.eod.Summary;
import quest.phil.diagnostic.information.system.ws.model.request.DenominationReportsRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisAdvancePaymentRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisDenominationReportRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisEODOtNoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisSOAPaymentRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryRequestRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionSerologyRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/transactions/")
public class QisTransactionEODController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionEODController.class);
	private final String SHORTDATE = "yyyyMMdd";
	private final String LONGDATE = "yyyyMMddHHmm";

	@Value("${application.name}")
	private String applicationName;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private AppExcelUtility appExcelUtility;

	@Autowired
	private QisTransactionRepository qisTransactionRepository;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	private QisEODOtNoRepository eodOtNo;

	@Autowired
	private QisTransactionSerologyRepository qisTransactionSerologyRepository;

	@Autowired
	QisTransactionLaboratoryRequestRepository qisTransactionLaboratoryRequestRepository;

	@Autowired
	private QisEODOtNoRepository qisEODOtNoRepository;

	@Autowired
	private QisDenominationReportRepository qisDenominationRepository;
	
	@Autowired
	private QisSOAPaymentRepository qisSOAPaymentRepository;
	
	@Autowired
	private QisAdvancePaymentRepository qisAdvancePymentRepository;

	@GetMapping("cashier_eod")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR') or hasRole('CASHIER')")
	public CashierEODData getCashierEOD(@RequestParam String dateTimeFrom, @RequestParam String dateTimeTo,
			@RequestParam String branchId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Cashier EOD");
		appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
		Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
		Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);

		QisBranch qisBranch = appUtility.getQisBranchByBranchId(branchId);
		if (qisBranch == null) {
			throw new RuntimeException("Branch is not found.", new Throwable("Branch is not found."));
		}

		if (!appUtility.calendarToFormatedDate(txnDateTimeFrom, SHORTDATE)
				.equals(appUtility.calendarToFormatedDate(txnDateTimeTo, SHORTDATE))) {
			throw new RuntimeException("Dates are not on the same day.",
					new Throwable("Dates are not on the same day."));
		}
		
		Calendar StartDateCal = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
		Calendar EndDateCal = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);
		List<QisDenominationReportsClass> ListDenomination = qisDenominationRepository.findByCreatedDate(StartDateCal,
				EndDateCal, qisBranch.getId());
		int page = 0;
		int size = 20;
		Pageable pageable = PageRequest.of(page, size);
		Page<QisTransaction> transactionList;
		CashierEODData eod = new CashierEODData(txnDateTimeFrom, txnDateTimeTo, authUser.getUsername());
		do {
			transactionList = qisTransactionRepository.getTransactionsByTransactionBranchDateTimeRange(
					qisBranch.getId(), txnDateTimeFrom, txnDateTimeTo, pageable);
			consolidateTransactions(transactionList, eod, false, qisBranch.getId(), ListDenomination);
			page++;
			pageable = PageRequest.of(page, size);
		} while (page < transactionList.getTotalPages());

		return eod;
	}

	@GetMapping("cashier_eod/receipt")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR') or hasRole('CASHIER')")
	public void getCashierEODReceipt(HttpServletResponse response, @RequestParam String dateTimeFrom,
			@RequestParam String dateTimeTo, @RequestParam String branchId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Cashier EOD Receipt");
		appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
		Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
		Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);

		QisBranch qisBranch = appUtility.getQisBranchByBranchId(branchId);
		if (qisBranch == null) {
			throw new RuntimeException("Branch is not found.", new Throwable("Branch is not found."));
		}
		String dateFrom = StringUtils.substring(dateTimeFrom, 0, dateTimeFrom.length() - 4);
		String dateTo = StringUtils.substring(dateTimeTo, 0, dateTimeTo.length() - 4);

		QisEODClass eodNotes = qisEODOtNoRepository.findEodDateFromDateTo(dateFrom, dateTo, qisBranch.getId());
		String othernotes = "";
		String referenceNumber = "";
		if (eodNotes != null) {
			othernotes = eodNotes.getOtherNotes();
			referenceNumber = eodNotes.getReferenceNumber();
		}

		if (!appUtility.calendarToFormatedDate(txnDateTimeFrom, SHORTDATE)
				.equals(appUtility.calendarToFormatedDate(txnDateTimeTo, SHORTDATE))) {
			throw new RuntimeException("Dates are not on the same day.",
					new Throwable("Dates are not on the same day."));
		}

		int page = 0;
		int size = 20;
		Pageable pageable = PageRequest.of(page, size);
		Page<QisTransaction> transactionList;
		CashierEODData eod = new CashierEODData(txnDateTimeFrom, txnDateTimeTo, authUser.getUsername());

		Calendar StartDateCal = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
		Calendar EndDateCal = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);
		List<QisDenominationReportsClass> ListDenomination = qisDenominationRepository.findByCreatedDate(StartDateCal,
				EndDateCal, qisBranch.getId());
		do {
			transactionList = qisTransactionRepository.getTransactionsByTransactionBranchDateTimeRange(
					qisBranch.getId(), txnDateTimeFrom, txnDateTimeTo, pageable);
			consolidateTransactions(transactionList, eod, false, qisBranch.getId(), ListDenomination);
			page++;
			pageable = PageRequest.of(page, size);
		} while (page < transactionList.getTotalPages());

		Document document = new Document(PageSize.LEGAL);
		document.open();
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(15, 310, 10, 10);
		document.addTitle("CASHIER EOD RECEIPT");

		ReceiptDocument receipt = new ReceiptDocument(applicationName, appUtility);
		receipt.getCashierEODReceipt(document, pdfWriter, eod, qisBranch, othernotes, referenceNumber);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + "cashier_eod_receipt" + dateTimeFrom + "_" + dateTimeTo + ".pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionEODController.class.getSimpleName(), "RECEIPT",
				"Cashier EOD Receipt", null, "EOD");

	}

	@GetMapping("auditor_eod")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public AuditorEODData getAuditorEOD(@RequestParam String dateFrom, @RequestParam String dateTo,
			@RequestParam(required = false) String branchId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {

		LOGGER.info(authUser.getId() + "-Auditor EOD");
		appUtility.validateTransactionDate(dateFrom);
		Calendar txnDateFrom = appUtility.stringToCalendarDate(dateFrom, SHORTDATE);

		appUtility.validateTransactionDate(dateTo);
		Calendar txnDateTo = appUtility.stringToCalendarDate(dateTo, SHORTDATE);
		
		

		QisBranch qisBranch = null;
		if (branchId != null) {
			qisBranch = appUtility.getQisBranchByBranchId(branchId);
			if (qisBranch == null) {
				throw new RuntimeException("Branch is not found.", new Throwable("Branch is not found."));
			}
		}
		Calendar StartDateCal = appUtility.stringToCalendarDate(dateFrom+"0500", LONGDATE);
		Calendar EndDateCal = appUtility.stringToCalendarDate(dateTo+"2100", LONGDATE);
		List<QisDenominationReportsClass> ListDenomination = null;
		if (qisBranch != null) {			
			ListDenomination = qisDenominationRepository.findByCreatedDate(StartDateCal,
					EndDateCal,qisBranch.getId());
		}

		return getAuditorEOD(txnDateFrom, txnDateTo, authUser, qisBranch, ListDenomination);
	}

	@GetMapping("auditor_eod/excel")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public void getAuditorEODExcel(HttpServletResponse response, @RequestParam String dateFrom,
			@RequestParam String dateTo, @RequestParam(required = false) String branchId,
			@RequestParam(required = false) String test, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-Auditor EOD");
		appUtility.validateTransactionDate(dateFrom);
		Calendar txnDateFrom = appUtility.stringToCalendarDate(dateFrom, SHORTDATE);

		appUtility.validateTransactionDate(dateTo);
		Calendar txnDateTo = appUtility.stringToCalendarDate(dateTo, SHORTDATE);

		QisBranch qisBranch = null;
		if (branchId != null) {
			qisBranch = appUtility.getQisBranchByBranchId(branchId);
			if (qisBranch == null) {
				throw new RuntimeException("Branch is not found.", new Throwable("Branch is not found."));
			}
		}

		String othernotes = "";
		String referenceNumber = "";

		if (branchId != null) {
			QisEODClass eodNotes = qisEODOtNoRepository.findEodDateFromDateTo(dateFrom, dateTo, qisBranch.getId());

			if (eodNotes != null) {
				othernotes = eodNotes.getOtherNotes();
				referenceNumber = eodNotes.getReferenceNumber();
			}
		}

		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=auditor_eod_"
				+ appUtility.calendarToFormatedDate(txnDateFrom, "YYYYMMdd") + "_"
				+ appUtility.calendarToFormatedDate(txnDateTo, "YYYYMMdd") + ".xlsx";
		response.setHeader(headerKey, headerValue);
		
		Calendar StartDateCal = appUtility.stringToCalendarDate(dateFrom+"0500", LONGDATE);
		Calendar EndDateCal = appUtility.stringToCalendarDate(dateTo+"2100", LONGDATE);
		List<QisDenominationReportsClass> ListDenomination = null;
		
		List<QisSOAPayment> qisSoaPayment = null;
		qisSoaPayment = qisSOAPaymentRepository.getSOAPaymentDate(StartDateCal, EndDateCal);
		
		List<QisAdvancePayment> qisAdvancePayment = null;
		qisAdvancePayment = qisAdvancePymentRepository.getAdvancePaymentDate(StartDateCal, EndDateCal);
		
		
		if (branchId != null) {			
			ListDenomination = qisDenominationRepository.findByCreatedDate(StartDateCal,
					EndDateCal, qisBranch.getId());
		}

		AuditorEODData eod = getAuditorEOD(txnDateFrom, txnDateTo, authUser, qisBranch, ListDenomination);

		AuditorEODExport eodExport = new AuditorEODExport(eod, applicationName, appUtility, appExcelUtility, othernotes,
				referenceNumber, test, qisSoaPayment, qisAdvancePayment);
		eodExport.export(response);

		qisLogService.info(authUser.getId(), QisTransactionEODController.class.getSimpleName(), "EXCEL", "Auditor EOD",
				null, "EOD");
	}

	private AuditorEODData getAuditorEOD(Calendar txnDateFrom, Calendar txnDateTo, QisUserDetails authUser,
			QisBranch qisBranch, List<QisDenominationReportsClass> listDenomination) {

		int page = 0;
		int size = 20;
		Pageable pageable = PageRequest.of(page, size);
		Page<QisTransaction> transactionList;
		AuditorEODData eod = new AuditorEODData(txnDateFrom, txnDateTo, authUser.getUsername());
		Long branchId = null;
		do {
			if (qisBranch != null) {
				transactionList = qisTransactionRepository.getTransactionsByTransactionBranchDateRange(
						qisBranch.getId(), txnDateFrom, txnDateTo, pageable);
				branchId = qisBranch.getId();
			} else {
				transactionList = qisTransactionRepository.getTransactionsByTransactionDateRange(txnDateFrom, txnDateTo,
						pageable);
			}
			consolidateTransactions(transactionList, eod, true, branchId, listDenomination);
			page++;
			pageable = PageRequest.of(page, size);
		} while (page < transactionList.getTotalPages());

		return eod;
	}

	private void consolidateTransactions(Page<QisTransaction> txnList, EODData eod, boolean isAuditor, Long branchid,
			List<QisDenominationReportsClass> listDenomination) {

		
		String dateFrom;
		String dateTo;
		dateFrom = appUtility.calendarToFormatedDate(eod.getDateFrom(), "YYYYMMdd");
		dateTo = appUtility.calendarToFormatedDate(eod.getDateTo(), "YYYYMMdd");
		String auditorOtNo = "";
		String referenceNumber = "";

		Summary summ = eod.getSummary();
		if (branchid != null) {

			QisEODClass eodNotes = qisEODOtNoRepository.findEodDateFromDateTo(dateFrom, dateTo, branchid);
			if (eodNotes != null) {
				auditorOtNo = eodNotes.getOtherNotes();
				referenceNumber = eodNotes.getReferenceNumber();

			}
		}

		int totalSales = summ.getTotalSales();
		double holdAmount = summ.getHoldAmount();
		double cashAmount = summ.getCashAmount();
		double accountsAmount = summ.getAccountsAmount();
		double apeAmount = summ.getApeAmount();
		double hmoAmount = summ.getHmoAmount();
		double medicalMissionAmoint = summ.getMedicalMission();
		double bankAmount = summ.getBankAmount();
		double virtualAmount = summ.getVirtualAmount();
		double refundAmount = summ.getRefundAmount();

		double totalSalesAmount = summ.getTotalSalesAmount();
		double taxAmount = summ.getTaxAmount();

		double discountAmount = summ.getDiscountAmount();
		double netAmount = summ.getNetAmount();

		double cashInAmount = summ.getCashInAmount();
		double cashOutAmount = summ.getCashOutAmount();
		double medicalService = summ.getMedicalService();

		double cashDiscount = summ.getCashDiscount();
		Map<String, Double> branchSales = eod.getBranchSales();
		Map<String, Double> billerAccount = eod.getBillerAccount();
		for (QisTransaction txn : txnList) {
			Account acct = new Account(txn.getId(), txn.getTransactionType(), txn.getPaymentType(),
					txn.getPaymentMode(), txn.getTotalItemAmountDue(), txn.getTotalItemTaxAmount(),
					txn.getTotalItemDiscountAmount(), txn.getTotalItemNetAmount(), txn.getTotalCashAmount(),
					txn.getTotalChangeAmount(), txn.getDiscountRate(), appUtility.getPatientFullname(txn.getPatient()),
					txn.getBiller(), txn.getTransactionDate(), txn.getBranch().getBranchName(),
					txn.getReferral() != null ? txn.getReferral().getReferral() : "", txn.getCashier().getUsername());

			if (isAuditor) {
				Set<Item> items = new HashSet<>();
				for (QisTransactionItem txnItem : txn.getTransactionItems()) {
					String itemName = "";
					if ("ITM".equals(txnItem.getItemType())) {
						itemName = txnItem.getQisItem().getItemDescription();
					} else if ("PCK".equals(txnItem.getItemType())) {
						itemName = txnItem.getQisPackage().getPackageDescription();
					}
					String molecular_lab = "";
					String rtpcrProcedure = "";
					if (txnItem.getQisItem() != null) {
						List<QisTransactionLaboratoryRequest> laboratoryReqList = qisTransactionLaboratoryRequestRepository
								.getTransactionLaboratoryRequestByIdAndLabReqList(txnItem.getTransactionid(), "NAS");
						QisTransactionSerology qisTransactionSerology = null;
						for (QisTransactionLaboratoryRequest laboratoryReq : laboratoryReqList) {
							qisTransactionSerology = qisTransactionSerologyRepository
									.getTransactionLaboratoryRequestById(txnItem.getTransactionid(),
											laboratoryReq.getId());
						}
						for (QisLaboratoryProcedureService rtpcr : txnItem.getQisItem().getServiceRequest()) {
							if ("RTPCR".equals(rtpcr.getLaboratoryRequest().toString())) {
								if (qisTransactionSerology != null) {
									if (qisTransactionSerology.getRtpcr() != null) {
										if (qisTransactionSerology.getRtpcr().getReferenceLab() != null) {											
											molecular_lab = qisTransactionSerology.getRtpcr().getReferenceLab().getName();
										}
									}
								}
								rtpcrProcedure = rtpcr.getRequestName();
							}
							
						}

					}
					

					Item item = new Item(txnItem.getId(), txnItem.getItemid(), itemName, txnItem.getGrossAmount(),
							txnItem.getQuantity(), rtpcrProcedure, molecular_lab);
					items.add(item);
				}
				acct.setItems(items);
			}

			if ("SPD".equals(txn.getStatus()) || "SRE".equals(txn.getStatus())) { // PROCESSED OR REFUND
				totalSalesAmount += txn.getTotalItemGrossAmount();
				taxAmount += txn.getTotalItemTaxAmount();
				discountAmount += txn.getTotalItemDiscountAmount();
				netAmount += txn.getTotalItemNetAmount();

				addToBranch(branchSales, txn.getBranch().getBranchName(), txn.getTotalItemGrossAmount(),
						txn.getTransactionType());
			}

			if ("SPD".equals(txn.getStatus())) { // PROCESSED
				acct.setRecordType(txn.getPaymentType());
				totalSales++;
				switch (txn.getPaymentType()) {
				case "CA": // CASH
					cashAmount += txn.getTotalItemGrossAmount();
					eod.getCash().add(acct);
					cashInAmount += txn.getTotalCashAmount();
					cashOutAmount += txn.getTotalChangeAmount();
					cashDiscount += txn.getTotalItemDiscountAmount();
					if ("TMS".equals(txn.getTransactionType())) {
						medicalService += txn.getTotalItemGrossAmount();
					}
					break;

				case "B": // BANK
					bankAmount += txn.getTotalItemGrossAmount();
					eod.getBank().add(acct);
					break;

				case "C": // CHARGE TO
					if ("ACCT".equals(txn.getPaymentMode())) { // ACCOUNTS
						accountsAmount += txn.getTotalItemGrossAmount();
						eod.getAccounts().add(acct);
						addToBiller(billerAccount, txn.getBiller(), txn.getTotalItemGrossAmount());
					} else if ("APE".equals(txn.getPaymentMode())) { // APE
						apeAmount += txn.getTotalItemGrossAmount();
						eod.getApe().add(acct);
						addToBiller(billerAccount, txn.getBiller(), txn.getTotalItemGrossAmount());
					} else if ("HMO".equals(txn.getPaymentMode())) { // HMO
						hmoAmount += txn.getTotalItemGrossAmount();
						eod.getHmo().add(acct);
						addToBiller(billerAccount, txn.getBiller(), txn.getTotalItemGrossAmount());
					} else if ("MMO".equals(txn.getPaymentMode())) {
						medicalMissionAmoint += txn.getTotalItemGrossAmount();
						eod.getMedicalMission().add(acct);
						addToBiller(billerAccount, txn.getBiller(), txn.getTotalItemGrossAmount());
					}
					break;

				case "VR": // VIRTUAL
					virtualAmount += txn.getTotalItemGrossAmount();
					eod.getVirtual().add(acct);
					break;

				default:
					break;
				}
			} else if ("SRE".equals(txn.getStatus())) { // REFUND
//				refundAmount += txn.getTotalItemGrossAmount();
//				cashOutAmount += Math.abs(txn.getTotalItemGrossAmount());
//				cashAmount += txn.getTotalItemGrossAmount();
				Double amount = txn.getTotalItemGrossAmount() - txn.getTotalItemDiscountAmount();
				refundAmount += amount;
				cashOutAmount += Math.abs(amount);
				cashAmount += txn.getTotalItemGrossAmount();
				cashDiscount += txn.getTotalItemDiscountAmount();

				acct.setRecordType("REFD");
				eod.getRefund().add(acct);
			} else if ("SHO".equals(txn.getStatus())) { // HOLD
				holdAmount += txn.getTotalItemGrossAmount();
				acct.setRecordType("HOLD");
				eod.getHold().add(acct);
			}
		}

		summ.setTotalSales(totalSales);
		summ.setHmoAmount(hmoAmount);
		summ.setHoldAmount(holdAmount);
		summ.setMedicalMission(medicalMissionAmoint);
		summ.setAccountsAmount(accountsAmount);
		summ.setApeAmount(apeAmount);
		summ.setCashAmount(cashAmount);
		summ.setBankAmount(bankAmount);
		summ.setVirtualAmount(virtualAmount);
		summ.setRefundAmount(refundAmount);

		summ.setTotalSalesAmount(totalSalesAmount);
		summ.setTaxAmount(taxAmount);
		summ.setDiscountAmount(discountAmount);
		summ.setNetAmount(netAmount);

		summ.setCashInAmount(cashInAmount);
		summ.setCashOutAmount(cashOutAmount);

		summ.setReferenceNumber(referenceNumber);
		summ.setEodOtherNotes(auditorOtNo);

		summ.setMedicalService(medicalService);
		summ.setCashDiscount(cashDiscount);

		eod.setSummary(summ);
		eod.setBranchSales(branchSales);
		eod.setBillerAccount(billerAccount);
		if (listDenomination != null) {			
			summ.setDenomination(listDenomination);
		}

	}

	private void addToBiller(Map<String, Double> billerAccount, String billerName, double amountDue) {
		double biller = 0;
		if (billerAccount.get(billerName) != null) {
			biller = billerAccount.get(billerName);
		}
		biller += amountDue;
		billerAccount.put(billerName, biller);
	}

	private void addToBranch(Map<String, Double> branchSales, String branchName, double amountDue,
			String transactionType) {
		double sale = 0;
		if (branchSales.get(branchName) != null) {
			sale = branchSales.get(branchName);
		}
		if (!"TMS".equals(transactionType)) {
			sale += amountDue;
		}
		branchSales.put(branchName, sale);
	}

	// Create End of day with notes and reference number
	@PostMapping("auditor_eod/notes")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public ResponseEntity<String> createEodNotes(@RequestParam(required = false) String referenceNumber,
			@RequestParam(required = false) String notes, @RequestParam String dateFrom, @RequestParam String dateTo,
			@RequestParam(required = false) String branchId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {

		QisBranch branch = null;
		if (branchId != null) {
			branch = appUtility.getQisBranchByBranchId(branchId);
			if (branch == null) {
				throw new RuntimeException("Invalid branch id.", new Throwable("branchId: Invalid branch id."));
			}
		}

		String message = "";
		QisEODClass request = qisEODOtNoRepository.findEodDateFromDateTo(dateFrom, dateTo, branch.getId());
		QisEODClass eod = new QisEODClass();

		if (request == null) {
			eod.setBranchId(branch.getId());
			eod.setBranch(branch);
			eod.setDateFrom(dateFrom);
			eod.setDateTo(dateTo);
			eod.setOtherNotes(notes);
			eod.setReferenceNumber(referenceNumber);
			eod.setCashierId(authUser.getId());
			eod.setCreatedBy(authUser.getId());
			eod.setUpdatedBy(authUser.getId());
			eodOtNo.save(eod);
			message = "Added Successfully";
		} else {
			request.setReferenceNumber(referenceNumber);
			request.setOtherNotes(notes);
			request.setCashierId(authUser.getId());
			request.setCreatedBy(authUser.getId());
			request.setUpdatedBy(authUser.getId());
			qisEODOtNoRepository.save(request);

			message = "Updated Successfully";
		}

		LOGGER.info(authUser.getId() + "-Create End Of Day with Notes or Reference Number");

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// Read End of day with notes and reference number
	@GetMapping("auditor_eod/{dateFrom}/{dateTo}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisEODClass getOtNotes(@PathVariable String dateFrom, @PathVariable String dateTo,
			@PathVariable(required = false) String branchId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-View notes/reference number: " + dateFrom + " " + dateTo);

		QisBranch qisBranch = appUtility.getQisBranchByBranchId(branchId);
		if (qisBranch == null) {
			throw new RuntimeException("Branch is not found.", new Throwable("Branch is not found."));
		}

		QisEODClass eod = qisEODOtNoRepository.findEodDateFromDateTo(dateFrom, dateTo, qisBranch.getId());
		if (eod == null) {
			throw new RuntimeException("Record not found.");
		}

		return eod;
	}

	// Create Denomination report
	@PostMapping("cashier/denomination")
	public ResponseEntity<String> createDenominationReport(
			@Valid @RequestBody DenominationReportsRequest denominationReq,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "startDateTimeFrom") String startDateTimeFrom,
			@RequestParam(name = "startDateTimeTo") String startDateTimeTo,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Create Denomination Report");

		QisBranch branch = null;
		if (branchId != null) {
			branch = appUtility.getQisBranchByBranchId(branchId);
			if (branch == null) {
				throw new RuntimeException("Invalid branch id.", new Throwable("branchId: Invalid branch id."));
			}
		}

		Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(startDateTimeFrom, LONGDATE);
		Calendar txnDateTimeTo = appUtility.stringToCalendarDate(startDateTimeTo, LONGDATE);

		String Message = "";

		QisDenominationReportsClass denominationReport = qisDenominationRepository
				.findDenominationReport(txnDateTimeFrom, txnDateTimeTo, branch.getId());

		QisDenominationReportsClass denomination = new QisDenominationReportsClass();

		if (denominationReport == null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
			String strDate = formatter.format(txnDateTimeFrom.getTime());
			String endDate = formatter.format(txnDateTimeTo.getTime());

			denomination.setBranch(branch);
			denomination.setReportName(strDate + " " + endDate);
			denomination.setBranchId(branch.getId());
			denomination.setCashierId(authUser.getId());
			denomination.setCoverageDateAndTimeFrom(txnDateTimeFrom);
			denomination.setCoverageDateAndTimeTo(txnDateTimeTo);
			denomination.setCreatedAt(Calendar.getInstance());
			denomination.setThousands(denominationReq.getThousands());
			denomination.setFiveHundreds(denominationReq.getFiveHundreds());
			denomination.setTwoHundreds(denominationReq.getTwoHundreds());
			denomination.setOneHundreds(denominationReq.getOneHundreds());
			denomination.setFifties(denominationReq.getFifties());
			denomination.setTwenties(denominationReq.getTwenties());
			denomination.setTens(denominationReq.getTens());
			denomination.setFive(denominationReq.getFive());
			denomination.setOne(denominationReq.getOne());
			denomination.setCents(denominationReq.getCents());

			qisDenominationRepository.save(denomination);
			Message = "Added Successfully";
		} else {
			if (denominationReport.getThousands() != denominationReq.getThousands()) {
				denominationReport.setThousands(denominationReq.getThousands());
			}
			if (denominationReport.getFiveHundreds() != denominationReq.getFiveHundreds()) {
				denominationReport.setFiveHundreds(denominationReq.getFiveHundreds());
			}
			if (denominationReport.getTwoHundreds() != denominationReq.getTwoHundreds()) {
				denominationReport.setTwoHundreds(denominationReq.getTwoHundreds());
			}
			if (denominationReport.getOneHundreds() != denominationReq.getOneHundreds()) {
				denominationReport.setOneHundreds(denominationReq.getOneHundreds());
			}
			if (denominationReport.getFifties() != denominationReq.getFifties()) {
				denominationReport.setFifties(denominationReq.getFifties());
			}
			if (denominationReport.getTwenties() != denominationReq.getTwenties()) {
				denominationReport.setTwenties(denominationReq.getTwenties());
			}
			if (denominationReport.getTens() != denominationReq.getTens()) {
				denominationReport.setTens(denominationReq.getTens());
			}
			if (denominationReport.getFive() != denominationReq.getFive()) {
				denominationReport.setFive(denominationReq.getFive());
			}
			if (denominationReport.getOne() != denominationReq.getOne()) {
				denominationReport.setOne(denominationReq.getOne());
			}
			if (denominationReport.getCents() != denominationReq.getCents()) {
				denominationReport.setCents(denominationReq.getCents());
			}
			qisDenominationRepository.save(denominationReport);
			Message = "Updated Successfully";
		}

		return new ResponseEntity<>(Message, HttpStatus.OK);
	}
	
	@PostMapping("auditor/verify")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public ResponseEntity<String> verefiedDenominationReport(
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "startDateTimeFrom") String startDateTimeFrom,
			@RequestParam(name = "startDateTimeTo") String startDateTimeTo,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Verified Denomination Report");
		
		
		QisBranch branch = null;
		if (branchId != null) {
			branch = appUtility.getQisBranchByBranchId(branchId);
			if (branch == null) {
				throw new RuntimeException("Invalid branch id.", new Throwable("branchId: Invalid branch id."));
			}
		}

		Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(startDateTimeFrom, LONGDATE);
		Calendar txnDateTimeTo = appUtility.stringToCalendarDate(startDateTimeTo, LONGDATE);

		String Message = "";

		QisDenominationReportsClass denominationReport = qisDenominationRepository
				.findDenominationReport(txnDateTimeFrom, txnDateTimeTo, branch.getId());
		
		if (denominationReport.getVerifyBy() != null) {
			throw new RuntimeException("Report is already verified.", new Throwable("report: Report already verified."));
		}
		

		if (denominationReport.getCashierId().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to verify the same transaction.",
					new Throwable("You are not authorize to verify the same transaction."));
		}
		
		denominationReport.setVerifyBy(authUser.getId());
		denominationReport.setVerifyAt(Calendar.getInstance());
		qisDenominationRepository.save(denominationReport);
		Message = "Verified!";
		
		return new ResponseEntity<>(Message, HttpStatus.OK);
	}
	
	@PostMapping("auditor/note")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public ResponseEntity<String> notedDenominationReport(
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "startDateTimeFrom") String startDateTimeFrom,
			@RequestParam(name = "startDateTimeTo") String startDateTimeTo,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Noted Denomination Report");
		
		
		QisBranch branch = null;
		if (branchId != null) {
			branch = appUtility.getQisBranchByBranchId(branchId);
			if (branch == null) {
				throw new RuntimeException("Invalid branch id.", new Throwable("branchId: Invalid branch id."));
			}
		}

		Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(startDateTimeFrom, LONGDATE);
		Calendar txnDateTimeTo = appUtility.stringToCalendarDate(startDateTimeTo, LONGDATE);

		String Message = "";

		QisDenominationReportsClass denominationReport = qisDenominationRepository
				.findDenominationReport(txnDateTimeFrom, txnDateTimeTo, branch.getId());
		
		if (denominationReport.getVerifyBy() == null) {
			throw new RuntimeException("Report is not yet verified.", new Throwable("Report: Report is not yet verified."));
		}

		if (denominationReport.getNotedBy() != null) {
			throw new RuntimeException("Report already notified.", new Throwable("Report: Report already notified."));
		}

		if (denominationReport.getCashierId().equals(authUser.getId()) || denominationReport.getVerifyBy().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to notify the same transaction.",
					new Throwable("You are not authorize to notify the same transaction."));
		}
		
		denominationReport.setNotedBy(authUser.getId());
		denominationReport.setNotedAt(Calendar.getInstance());
		
		qisDenominationRepository.save(denominationReport);
		Message = "Noted!";
		
		return new ResponseEntity<>(Message, HttpStatus.OK);
	}
}
