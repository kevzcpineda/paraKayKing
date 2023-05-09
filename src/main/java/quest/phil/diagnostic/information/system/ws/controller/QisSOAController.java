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
import org.springframework.beans.BeanUtils;
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
import quest.phil.diagnostic.information.system.ws.common.documents.ChargeToSOA;
import quest.phil.diagnostic.information.system.ws.common.exports.SOAExport;
import quest.phil.diagnostic.information.system.ws.common.exports.SOAReportExport;
import quest.phil.diagnostic.information.system.ws.model.QisEmailConfig;
import quest.phil.diagnostic.information.system.ws.model.QisSOASummary;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisAdvancePayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOA;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOAPayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.request.QisSOAPaymentRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisSOARequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisSOAPaymentResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisSOAResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisAdvancePaymentRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisCorporateRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisSOAPaymentRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisSOARepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisSOAController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisSOAController.class);
	private final String CATEGORY = "SOA";
	private final String SHORT_DATE = "yyyyMMdd";
	private final String LONGDATE = "yyyyMMddHHmm";

	@Value("${application.name}")
	private String applicationName;

	@Value("${application.header}")
	private String applicationHeader;

	@Value("${application.footer}")
	private String applicationFooter;

	private QisEmailConfig emailConfig;

	public QisSOAController(QisEmailConfig QisEmailConfig) {
		this.emailConfig = QisEmailConfig;
	}

	@Autowired
	private QisTransactionRepository qisTransactionRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private AppExcelUtility appExcelUtility;

	@Autowired
	private QisCorporateRepository qisCorporateRepository;

	@Autowired
	private QisSOARepository qisSOARepository;

	@Autowired
	private QisSOAPaymentRepository qisSOAPaymentRepository;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	private AppEmailUtility emailUtility;

	@Autowired
	private QisAdvancePaymentRepository qisAdvancePaymentRepository;

	// UNBILLED TRANSACTIONS
	@GetMapping("soa/unbilled")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public Page<QisTransaction> getSOAUnbilledTransactions(@RequestParam(required = false) String chargeTo,
			Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info(authUser.getId() + "-Unbilled Transactions[" + chargeTo + "]");

		Page<QisTransaction> list;
		if (chargeTo != null) {
			QisCorporate biller = appUtility.getQisCorporateByCorporateId(chargeTo);
			if (biller == null) {
				throw new RuntimeException("Invalid charge to account.",
						new Throwable("chargeTo: Invalid charge to account."));
			}
			list = qisTransactionRepository.getUnbilledTransactions(biller.getId(), pageable);
		} else {
			list = qisTransactionRepository.getAllUnbilledTransactions(pageable);
		}

		return list;
	}

	// CREATE SOA
	@PostMapping("soa/{corporateId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOA createSOA(@PathVariable String corporateId, @Valid @RequestBody QisSOARequest soaRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-create SOA Corporate:" + corporateId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		QisSOA soa = new QisSOA();
		soa.setCoverageDateFrom(appUtility.stringToCalendarDate(soaRequest.getCoveredDateFrom(), SHORT_DATE));
		soa.setCoverageDateTo(appUtility.stringToCalendarDate(soaRequest.getCoveredDateTo(), SHORT_DATE));
		soa.setStatementDate(appUtility.stringToCalendarDate(soaRequest.getStatementDate(), SHORT_DATE));
		if (soaRequest.getPurchaseOrder() != null || soaRequest.getPurchaseOrder() != "") {
			soa.setPurchaseOrder(soaRequest.getPurchaseOrder());
		} else {
			soa.setPurchaseOrder(null);
		}
		Double amount = appUtility.parseDoubleAmount(soaRequest.getSoaAmount());
		if (amount == null) {
			throw new RuntimeException("Invalid SOA Total Amount.",
					new Throwable("soaAmount: Invalid SOA Total Amount."));
		}

		if (soa.getCoverageDateFrom() == null) {
			throw new RuntimeException("Invalid Coverage Date From.",
					new Throwable("coveredDateFrom: Invalid Coverage Date From."));
		}

		if (soa.getCoverageDateTo() == null) {
			throw new RuntimeException("Invalid Coverage Date To.",
					new Throwable("coveredDateTo: Invalid Coverage Date From."));
		}

		if (soa.getStatementDate() == null) {
			throw new RuntimeException("Invalid Statement Date.",
					new Throwable("statementDate: Invalid Statement Date."));
		}

		Set<QisTransaction> transactions = getTransactionList(soaRequest.getTransactions(), qisCorporate.getId());
		if (transactions.isEmpty()) {
			throw new RuntimeException("Transaction List should not be empty.",
					new Throwable("transactions: Transaction List should not be empty."));
		}

		int count = 0;
		QisSOA prev = qisSOARepository.getLastSOAStatements(qisCorporate.getId());
		if (prev != null) {
			count = prev.getSoaCount();
		}
		count++;

		soa.setChargeToId(qisCorporate.getId());
		soa.setSoaAmount(amount);
		soa.setCreatedBy(authUser.getId());
		soa.setUpdatedBy(authUser.getId());
		soa.setTransactions(transactions);
		soa.setSoaCount(count);
		soa.setSoaNumber(appUtility.calendarToFormatedDate(soa.getStatementDate(), "YY") + qisCorporate.getSoaCode()
				+ appUtility.numberFormat(Long.valueOf(count), "0000"));

		qisSOARepository.save(soa);

		QisSOAResponse qisSOAResponse = new QisSOAResponse();
		BeanUtils.copyProperties(soa, qisSOAResponse);
		qisSOAResponse.setCoverageDateFrom(appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), SHORT_DATE));
		qisSOAResponse.setCoverageDateTo(appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), SHORT_DATE));
		qisSOAResponse.setStatementDate(appUtility.calendarToFormatedDate(soa.getStatementDate(), SHORT_DATE));
		qisSOAResponse.setTransactions(soaRequest.getTransactions());

		qisLogService.info(authUser.getId(), QisSOAController.class.getSimpleName(), "CREATE",
				qisSOAResponse.toString(), soa.getId(), CATEGORY);

		return soa;
	}

	private Set<QisTransaction> getTransactionList(Set<Long> idList, Long billerId) {
		Set<QisTransaction> list = new HashSet<>();

		for (Long id : idList) {
			QisTransaction txn = qisTransactionRepository.getSOATransactionById(id, billerId);
			if (txn != null) {
				list.add(txn);
			}
		}

		return list;
	}

	@GetMapping("soa/{corporateId}/year/{year}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public Page<QisSOA> getChargeToSOAYear(@PathVariable String corporateId, @PathVariable int year, Pageable pageable,
			@RequestParam(required = false) String startDateTime, @RequestParam(required = false) String endDateTime,
			@RequestParam String byYear, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-List of SOA:" + corporateId + " year:" + year);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		if ("Y".equals(byYear)) {
			return qisSOARepository.chargeToSOAYear(qisCorporate.getId(), year, pageable);
		} else {
			appUtility.validateDateTimeFromTo(startDateTime, endDateTime);
			Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(startDateTime, LONGDATE);
			Calendar txnDateTimeTo = appUtility.stringToCalendarDate(endDateTime, LONGDATE);
			return qisSOARepository.chargeToSOAMonth(qisCorporate.getId(), txnDateTimeFrom, txnDateTimeTo, pageable);
		}
	}

	@GetMapping("soa/year/{year}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public Page<QisSOA> getAllChargeToSOAYear(@PathVariable int year, Pageable pageable,
			@RequestParam(required = false) String startDateTime, @RequestParam(required = false) String endDateTime,
			@RequestParam String byYear, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-List of All SOA year:" + year);

		if ("Y".equals(byYear)) {
			return qisSOARepository.allChargeToSOAYear(year, pageable);
		} else {
			appUtility.validateDateTimeFromTo(startDateTime, endDateTime);
			Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(startDateTime, LONGDATE);
			Calendar txnDateTimeTo = appUtility.stringToCalendarDate(endDateTime, LONGDATE);
			return qisSOARepository.allChargeToSOAMonth(txnDateTimeFrom, txnDateTimeTo, pageable);
		}
	}

	// VERIFY SOA
	@PutMapping("soa/{corporateId}/verify/{soaId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOA verifySOA(@PathVariable String corporateId, @PathVariable Long soaId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Verify of SOA:" + corporateId + " id:" + soaId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		QisSOA soa = qisSOARepository.getSOAById(qisCorporate.getId(), soaId);
		if (soa == null) {
			throw new RuntimeException("Record not found.", new Throwable("soaId: Record not found."));
		}

		if (soa.getVerifiedBy() != null) {
			throw new RuntimeException("SOA already verified.", new Throwable("soaId: SOA already verified."));
		}

		if (soa.getCreatedBy().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to verify the same transaction.",
					new Throwable("You are not authorize to verify the same transaction."));
		}

		soa.setVerifiedBy(authUser.getId());
		soa.setVerifiedDate(Calendar.getInstance());

		soa.setUpdatedBy(authUser.getId());
		soa.setUpdatedAt(Calendar.getInstance());
		qisSOARepository.save(soa);

		qisLogService.info(authUser.getId(), QisSOAController.class.getSimpleName(), "VERIFIED", authUser.getUsername(),
				soa.getId(), CATEGORY);

		return soa;
	}

	// NOTED SOA
	@PutMapping("soa/{corporateId}/noted/{soaId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOA notedSOA(@PathVariable String corporateId, @PathVariable Long soaId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Notify of SOA:" + corporateId + " id:" + soaId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		QisSOA soa = qisSOARepository.getSOAById(qisCorporate.getId(), soaId);
		if (soa == null) {
			throw new RuntimeException("Record not found.", new Throwable("soaId: Record not found."));
		}

		if (soa.getVerifiedBy() == null) {
			throw new RuntimeException("SOA is not yet verified.", new Throwable("soaId: SOA is not yet verified."));
		}

		if (soa.getNotedBy() != null) {
			throw new RuntimeException("SOA already notified.", new Throwable("soaId: SOA already notified."));
		}

		if (soa.getCreatedBy().equals(authUser.getId()) || soa.getVerifiedBy().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to notify the same transaction.",
					new Throwable("You are not authorize to notify the same transaction."));
		}

		soa.setNotedBy(authUser.getId());
		soa.setNotedDate(Calendar.getInstance());

		soa.setUpdatedBy(authUser.getId());
		soa.setUpdatedAt(Calendar.getInstance());
		qisSOARepository.save(soa);

		qisLogService.info(authUser.getId(), QisSOAController.class.getSimpleName(), "NOTED", authUser.getUsername(),
				soa.getId(), CATEGORY);

		return soa;
	}

	// EXCEL SOA
	@GetMapping("soa/{corporateId}/excel/{soaId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public void getSOAExcel(HttpServletResponse response, @PathVariable String corporateId, @PathVariable Long soaId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Excel of SOA:" + corporateId + " id:" + soaId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		QisSOA soa = qisSOARepository.getSOAById(qisCorporate.getId(), soaId);
		if (soa == null) {
			throw new RuntimeException("Record not found.", new Throwable("soaId: Record not found."));
		}

		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=soa_" + soa.getSoaNumber() + ".xlsx";
		response.setHeader(headerKey, headerValue);

		SOAExport soaExport = new SOAExport(applicationName, appUtility, appExcelUtility);
		soaExport.export(response, soa);

		qisLogService.info(authUser.getId(), QisSOAController.class.getSimpleName(), "EXCEL", authUser.getUsername(),
				soa.getId(), CATEGORY);
	}

	// PRINT SOA
	@GetMapping("soa/{corporateId}/print/{soaId}/year/{year}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public void getSOAPrint(HttpServletResponse response, @PathVariable String corporateId, @PathVariable Long soaId,
			@PathVariable int year, @RequestParam(required = false) Boolean withHeaderFooter,
			@RequestParam(required = false) Boolean withRunningBalance,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Print of SOA:" + corporateId + " id:" + soaId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		QisSOA soa = qisSOARepository.getSOAById(qisCorporate.getId(), soaId);

		if (soa == null) {
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
		document.addTitle(soa.getSoaNumber());

		List<QisSOA> soaList = qisSOARepository.chargeToSOAYearList(qisCorporate.getId(), year);
		ChargeToSOA chargeToSOA = new ChargeToSOA(applicationName, applicationHeader, applicationFooter, appUtility);
		chargeToSOA.getChargeToSOA(document, pdfWriter, soa, withHeaderFooter, withRunningBalance, soaList);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + soa.getSoaNumber() + "_soa.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisSOAController.class.getSimpleName(), "PRINT", authUser.getUsername(),
				soa.getId(), CATEGORY);
	}

	// CREATE SOA PAYMENT
	@PostMapping("soa/{corporateId}/payment")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOAPayment SOAPayment(@PathVariable String corporateId,
			@Valid @RequestBody QisSOAPaymentRequest soaPayRequest, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-pay SOA Corporate:" + corporateId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		if (Double.parseDouble(soaPayRequest.getAddvancePayment()) > 0) {
			if (qisCorporate.getAdvPayment() == 0) {
				throw new RuntimeException("No available balance for advance payment.",
						new Throwable("No available balance for advance payment."));
			}
		}

		if (qisCorporate.getAdvPayment() < Double.parseDouble(soaPayRequest.getAddvancePayment())) {
			throw new RuntimeException("Not Enough Balance.", new Throwable("Not Enough Balance."));
		}

		Set<QisSOA> soaList = getSOAList(soaPayRequest.getSoaList(), qisCorporate.getId());
		if (soaList.isEmpty()) {
			throw new RuntimeException("SOA List should not be empty.",
					new Throwable("soaList: SOA List should not be empty."));
		}

		for (String transactionId : soaPayRequest.getTransactionIds()) {

			QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
			qisTransaction.setSoaStatus(true);
			qisTransactionRepository.save(qisTransaction);

		}

		QisSOAPayment soaPayment = new QisSOAPayment();
		soaPayment.setPaymentDate(appUtility.stringToCalendarDate(soaPayRequest.getPaymentDate(), SHORT_DATE));
		Double amount = appUtility.parseDoubleAmount(soaPayRequest.getPaymentAmount());
		if (amount == null) {
			throw new RuntimeException("Invalid Payment Amount.",
					new Throwable("paymentAmount: Invalid Payment Amount."));
		}

		Double otherAmount = 0d;
		if (soaPayRequest.getOtherAmount() != null) {
			otherAmount = appUtility.parseDoubleAmount(soaPayRequest.getOtherAmount());
			if (otherAmount == null) {
				throw new RuntimeException("Invalid Other Amount.",
						new Throwable("otherAmount: Invalid Other Amount."));
			}
		}

		Double taxWithHeld = 0d;
		if (soaPayRequest.getOtherAmount() != null) {
			taxWithHeld = appUtility.parseDoubleAmount(soaPayRequest.getTaxWithHeld());
			if (taxWithHeld == null) {
				throw new RuntimeException("Invalid Other Amount.",
						new Throwable("otherAmount: Invalid Other Amount."));
			}
		}

		if (soaPayment.getPaymentDate() == null) {
			throw new RuntimeException("Invalid Payment Date.", new Throwable("paymentDate: Invalid Payment Date."));
		}

		String paymentType = appUtility.getSOAPaymentType(soaPayRequest.getPaymentType());
		if (paymentType == null) {
			throw new RuntimeException("Invalid Payment Type.", new Throwable("paymentType: Invalid Payment Type."));
		}

		if (!soaPayRequest.getPaymentType().equals("CA")) {
			if (soaPayRequest.getPaymentBank() == null || "".equals(soaPayRequest.getPaymentBank().trim())) {
				throw new RuntimeException("Payment Bank is required.",
						new Throwable("paymentBank: Payment Bank is required."));
			}

			String bank = appUtility.getBankType(soaPayRequest.getPaymentBank());
			if (bank == null) {
				throw new RuntimeException("Invalid Payment Bank.",
						new Throwable("paymentBank: Invalid Payment Bank."));
			}

			if (soaPayRequest.getAccountNumber() == null || "".equals(soaPayRequest.getAccountNumber().trim())) {
				throw new RuntimeException("Account Number is required.",
						new Throwable("accountNumber: Account Number is required."));
			}
		}

		double newAdvancePaymentBalance = qisCorporate.getAdvPayment()
				- Double.parseDouble(soaPayRequest.getAddvancePayment());
		qisCorporate.setAdvPayment(newAdvancePaymentBalance);

		soaPayment.setChargeToId(qisCorporate.getId());
		soaPayment.setPaymentAmount(amount);
		soaPayment.setPaymentType(soaPayRequest.getPaymentType());
		soaPayment.setPaymentBank(soaPayRequest.getPaymentBank());
		soaPayment.setAccountNumber(soaPayRequest.getAccountNumber());
		soaPayment.setOtherAmount(otherAmount);
		soaPayment.setOtherNotes(soaPayRequest.getOtherNotes());
		soaPayment.setTaxWithHeld(taxWithHeld);
		soaPayment.setSoaList(soaList);
		soaPayment.setCreatedBy(authUser.getId());
		soaPayment.setUpdatedBy(authUser.getId());
		soaPayment.setAdvancePayment(Double.parseDouble(soaPayRequest.getAddvancePayment()));

		qisSOAPaymentRepository.save(soaPayment);

		qisCorporateRepository.save(qisCorporate);
		QisSOAPaymentResponse qisSOAPaymentResponse = new QisSOAPaymentResponse();
		BeanUtils.copyProperties(soaPayment, qisSOAPaymentResponse);
		qisSOAPaymentResponse
				.setPaymentDate(appUtility.calendarToFormatedDate(soaPayment.getPaymentDate(), SHORT_DATE));
		qisSOAPaymentResponse.setSoaList(soaPayRequest.getSoaList());

		qisLogService.info(authUser.getId(), QisSOAController.class.getSimpleName(), "PAYMENT",
				qisSOAPaymentResponse.toString(), soaPayment.getId(), CATEGORY);

		return soaPayment;
	}

	// VERIFY SOA PAYMENT
	@PutMapping("soa/{corporateId}/payment/{paymentId}/verify")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOAPayment verifySOAPayment(@PathVariable String corporateId, @PathVariable Long paymentId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-pay SOA Corporate:" + corporateId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		QisSOAPayment soaPayment = qisSOAPaymentRepository.getSOAPaymentById(qisCorporate.getId(), paymentId);
		if (soaPayment == null) {
			throw new RuntimeException("Record not found.", new Throwable("paymentId: Record not found."));
		}

		if (soaPayment.getVerifiedBy() != null) {
			throw new RuntimeException("SOA already verified.", new Throwable("soaId: SOA already verified."));
		}

		if (soaPayment.getCreatedBy().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to verify the same transaction.",
					new Throwable("You are not authorize to verify the same transaction."));
		}

		soaPayment.setVerifiedBy(authUser.getId());
		soaPayment.setVerifiedDate(Calendar.getInstance());

		soaPayment.setUpdatedBy(authUser.getId());
		soaPayment.setUpdatedAt(Calendar.getInstance());
		qisSOAPaymentRepository.save(soaPayment);

		qisLogService.info(authUser.getId(), QisSOAController.class.getSimpleName(), "PAYMENT-VERIFIED",
				authUser.getUsername(), soaPayment.getId(), CATEGORY);

		return soaPayment;
	}

	// AUDITED SOA PAYMENT
	@PutMapping("soa/{corporateId}/payment/{paymentId}/audited")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOAPayment auditedSOAPayment(@PathVariable String corporateId, @PathVariable Long paymentId,
			@RequestParam(required = false) String controlNumber, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-pay SOA Corporate:" + corporateId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		QisSOAPayment soaPayment = qisSOAPaymentRepository.getSOAPaymentById(qisCorporate.getId(), paymentId);
		if (soaPayment == null) {
			throw new RuntimeException("Record not found.", new Throwable("paymentId: Record not found."));
		}

		if (soaPayment.getAuditedBy() != null) {
			throw new RuntimeException("SOA already audited.", new Throwable("soaId: SOA already audited."));
		}

		if (soaPayment.getVerifiedBy().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to audited the same transaction.",
					new Throwable("You are not authorize to audited the same transaction."));
		}

		if (soaPayment.getCreatedBy().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to audited the same transaction.",
					new Throwable("You are not authorize to audited the same transaction."));
		}

		if (!"".equals(controlNumber)) {
			soaPayment.setControlNumber(controlNumber);
		}

		soaPayment.setAuditedBy(authUser.getId());
		soaPayment.setAuditededDate(Calendar.getInstance());

		soaPayment.setUpdatedBy(authUser.getId());
		soaPayment.setUpdatedAt(Calendar.getInstance());
		qisSOAPaymentRepository.save(soaPayment);

		qisLogService.info(authUser.getId(), QisSOAController.class.getSimpleName(), "PAYMENT-VERIFIED",
				authUser.getUsername(), soaPayment.getId(), CATEGORY);

		return soaPayment;
	}

	@PutMapping("soa/{corporateId}/payment/{paymentId}/upload_receipt")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisSOAPayment uploadPaymentReceipt(@PathVariable String corporateId, @PathVariable Long paymentId,
			@RequestParam(name = "uploadFile", required = false) MultipartFile uploadFile,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-upload SOA Corporate:" + corporateId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		QisSOAPayment soaPayment = qisSOAPaymentRepository.getSOAPaymentById(qisCorporate.getId(), paymentId);
		if (soaPayment == null) {
			throw new RuntimeException("Record not found.", new Throwable("paymentId: Record not found."));
		}

		if (uploadFile != null) {
			if (uploadFile.getSize() > 1000000) {
				throw new RuntimeException("File size should not exceed to 1MB.",
						new Throwable("File size should not exceed to 1MB."));
			}

			InputStream inputStream = uploadFile.getInputStream();
			byte[] receipt = StreamUtils.copyToByteArray(inputStream);
			soaPayment.setReceipt(receipt);
			soaPayment.setImageType(uploadFile.getContentType());
			soaPayment.setUpdatedBy(authUser.getId());
			soaPayment.setUpdatedAt(Calendar.getInstance());
			qisSOAPaymentRepository.save(soaPayment);
			qisLogService.warn(authUser.getId(), QisDoctorController.class.getSimpleName(), "UPLOAD", "RECEIPT",
					soaPayment.getId(), CATEGORY);
		} else {
			if (soaPayment.getReceipt() != null) {
				soaPayment.setReceipt(null);
				soaPayment.setUpdatedBy(authUser.getId());
				soaPayment.setUpdatedAt(Calendar.getInstance());
				qisSOAPaymentRepository.save(soaPayment);
				qisLogService.warn(authUser.getId(), QisDoctorController.class.getSimpleName(), "UPLOAD",
						"REMOVE SIGNATURE", soaPayment.getId(), CATEGORY);
			}
		}

		return soaPayment;
	}

	private Set<QisSOA> getSOAList(Set<Long> idList, Long billerId) {
		Set<QisSOA> list = new HashSet<>();

		for (Long id : idList) {
			QisSOA soa = qisSOARepository.getSOAById(billerId, id);
			if (soa != null) {
				list.add(soa);
			}
		}

		return list;
	}

	@GetMapping("soa/{corporateId}/payments/{year}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public Page<QisSOAPayment> getSOAPaymentsYear(@PathVariable String corporateId, @PathVariable int year,
			@RequestParam(required = false) String startDateTime, @RequestParam(required = false) String endDateTime,
			@RequestParam String byYear, Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-List of SOA:" + corporateId + " year:" + year);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		if ("Y".equals(byYear)) {
			return qisSOAPaymentRepository.getSOAPaymentYear(qisCorporate.getId(), year, pageable);
		} else {
			appUtility.validateDateTimeFromTo(startDateTime, endDateTime);
			Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(startDateTime, LONGDATE);
			Calendar txnDateTimeTo = appUtility.stringToCalendarDate(endDateTime, LONGDATE);
			return qisSOAPaymentRepository.chargeToSOAPaymentMonth(qisCorporate.getId(), txnDateTimeFrom, txnDateTimeTo,
					pageable);
		}
	}

	// Advance Payment
	@GetMapping("soa/{corporateId}/advance_payment")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public void AddAdvancePayment(@PathVariable String corporateId, @RequestParam(required = false) double amount,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-ADD advance Payment");
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		QisAdvancePayment advancePayment = new QisAdvancePayment();
		advancePayment.setChargeToId(qisCorporate.getId());
		advancePayment.setCreatedBy(authUser.getId());
//		advancePayment.setCreateUser(authUser);
		advancePayment.setAmtAdvPayment(amount);

		qisAdvancePaymentRepository.save(advancePayment);

		qisCorporate.setAdvPayment(qisCorporate.getAdvPayment() + amount);
		qisCorporateRepository.save(qisCorporate);
	}

	// SEND SOA
	@PostMapping("soa/{corporateId}/send_email/{soaId}/year/{year}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public void getSOASend(HttpServletResponse response, @PathVariable String corporateId, @PathVariable Long soaId,
			@PathVariable int year, @RequestParam(name = "file", required = false) List<MultipartFile> fileInput,
			@RequestParam("sendTo") String sendTo, @RequestParam(name = "sendCc", required = false) String sendCc,
			@RequestParam("emailSubject") String emailSubject, @RequestParam("emailBody") String emailBody,
			@RequestParam(required = false) Boolean withRunningBalance,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Print of SOA:" + corporateId + " id:" + soaId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		QisSOA soa = qisSOARepository.getSOAById(qisCorporate.getId(), soaId);

		if (soa == null) {
			throw new RuntimeException("Record not found.", new Throwable("soaId: Record not found."));
		}
		if (!soa.isSoaSend()) {
			soa.setSoaSend(true);
		}

		qisSOARepository.save(soa);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);

		document.addTitle(soa.getSoaNumber());

		List<QisSOA> soaList = qisSOARepository.chargeToSOAYearList(qisCorporate.getId(), year);
		ChargeToSOA chargeToSOA = new ChargeToSOA(applicationName, applicationHeader, applicationFooter, appUtility);
		chargeToSOA.getChargeToSOA(document, pdfWriter, soa, true, withRunningBalance, soaList);

		byte[] bytes = baos.toByteArray();
		boolean ans = fileInput.isEmpty();

		if (ans == true) {
			fileInput = new ArrayList<>();
		}

		MultipartFile multipartFile = new MockMultipartFile("file", soa.getSoaNumber() + ".pdf", "text/plain", bytes);

		if (multipartFile != null) {
			fileInput.add(multipartFile);
		}

		emailUtility.sendEmailSoa(emailConfig, sendTo, emailSubject, sendCc, emailBody, fileInput);
		qisLogService.info(authUser.getId(), QisSOAController.class.getSimpleName(), "SEND", authUser.getUsername(),
				soa.getId(), CATEGORY);
	}

	// RECIEVE EMAIL SOA
	@PostMapping("soa/{corporateId}/recieve_email/{soaId}/year/{year}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public void getSOARecieved(@PathVariable String corporateId, @PathVariable Long soaId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Recieve of SOA:" + corporateId + " id:" + soaId);

		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		QisSOA soa = qisSOARepository.getSOAById(qisCorporate.getId(), soaId);

		if (soa == null) {
			throw new RuntimeException("Record not found.", new Throwable("soaId: Record not found."));
		}
		if (!soa.isSoaRecieved()) {
			soa.setSoaRecieved(true);
		}

		qisSOARepository.save(soa);

		qisLogService.info(authUser.getId(), QisSOAController.class.getSimpleName(), "RECIEVE", authUser.getUsername(),
				soa.getId(), CATEGORY);
	}

	// GENERATE SOA REPORTS
	@PostMapping("soa/generate_excel/year/{year}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public void getSOAReports(HttpServletResponse response, @PathVariable int year, Pageable pageable,
			@RequestParam(required = false) String dateTimeFrom, @RequestParam(required = false) String dateTimeTo,
			@RequestParam(required = false) String chargeId, @RequestParam String byYear,
			@RequestParam(required = false) Boolean soaPrepared, @RequestParam(required = false) Boolean soaVerified,
			@RequestParam(required = false) Boolean soaNoted, @RequestParam(required = false) Boolean soaSend,
			@RequestParam(required = false) Boolean soaRecieved,
			@RequestParam(required = false) Boolean soaPaymentPrepared,
			@RequestParam(required = false) Boolean soaPaymentBalance,
			@RequestParam(required = false) Boolean soaPaymentVerified,
			@RequestParam(required = false) Boolean soaPaymentAudited, 
			@RequestParam(required = false) Boolean unbilledTransaction, 
			@AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {

		LOGGER.info(authUser.getId() + "-Print of SOA REPORT");
		response.setContentType("application/octet-stream");

		Page<QisSOA> list = null;
		List<QisSOA> mainList = new ArrayList<QisSOA>();

		Page<QisTransaction> unbilled = null;
		List<QisTransaction> unbilledList = new ArrayList<QisTransaction>();
		
		
		int page = 0;
		int size = 40;
		int pageUnbilled = 0;
		int sizeUnbilled = 40;

		do {
			pageable = PageRequest.of(pageUnbilled, sizeUnbilled);
			pageUnbilled++;
			
			if ("Y".equals(byYear)) {				
				if (chargeId != null) {
					QisCorporate biller = appUtility.getQisCorporateByCorporateId(chargeId);
					if (biller == null) {
						throw new RuntimeException("Invalid charge to account.",
								new Throwable("chargeTo: Invalid charge to account."));
					}
					unbilled = qisTransactionRepository.getUnbilledTransactions(biller.getId(), pageable);
				} else {
					unbilled = qisTransactionRepository.getAllUnbilledTransactions(pageable);
				}
			}else {
				appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
				Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
				Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);
				if (chargeId != null) {
					QisCorporate biller = appUtility.getQisCorporateByCorporateId(chargeId);
					if (biller == null) {
						throw new RuntimeException("Invalid charge to account.",
								new Throwable("chargeTo: Invalid charge to account."));
					}
					unbilled = qisTransactionRepository.getUnbilledTransactionsByDate(txnDateTimeFrom, txnDateTimeTo, biller.getId(), pageable);
				} else {
					unbilled = qisTransactionRepository.getAllUnbilledTransactionsByDate(txnDateTimeFrom, txnDateTimeTo, pageable);
				}
			}
		
		
		unbilledList.addAll(unbilled.getContent());
		
		} while (pageUnbilled < unbilled.getTotalPages());
		
		do {
			pageable = PageRequest.of(page, size);
			page++;

			if ("Y".equals(byYear)) {
				if (chargeId != null) {
					QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(chargeId);
					if (qisCorporate != null) {
						list = qisSOARepository.chargeToSOAYear(qisCorporate.getId(), year, pageable);
					}
				} else {
					list = qisSOARepository.allChargeToSOAYear(year, pageable);
				}
				
			} else {

				appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
				Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
				Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);
				if (chargeId != null) {
					QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(chargeId);
					if (qisCorporate != null) {
						list = qisSOARepository.chargeToSOAMonth(qisCorporate.getId(), txnDateTimeFrom, txnDateTimeTo,
								pageable);
					}
				} else {
					list = qisSOARepository.allChargeToSOAMonth(txnDateTimeFrom, txnDateTimeTo, pageable);
				}

			}

			mainList.addAll(list.getContent());
		} while (page < list.getTotalPages());

		SOAReportExport soaReportExport = new SOAReportExport(applicationName, appUtility, appExcelUtility,
				qisCorporateRepository, qisSOAPaymentRepository, soaPrepared, soaVerified, soaNoted, soaSend,
				soaRecieved, soaPaymentPrepared, soaPaymentBalance, soaPaymentVerified, soaPaymentAudited, unbilledTransaction);
		soaReportExport.export(response, mainList, unbilledList);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=soa_.xlsx";
		response.setHeader(headerKey, headerValue);

	}

	// SOA SUMMARY
	@GetMapping("soa/{corporateId}/summary/{year}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public List<QisSOASummary> getSOASummaryYear(@PathVariable String corporateId, @PathVariable int year,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-SOA Summary:" + corporateId + " year:" + year);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		int page = 0;
		int size = 20;
		Pageable pageable = PageRequest.of(page, size);

		List<QisSOASummary> summaryList = new ArrayList<QisSOASummary>();
		Page<QisSOA> soaList;
		do {
			soaList = qisSOARepository.chargeToSOAYear(qisCorporate.getId(), year, pageable);
			addSOAToSummary(soaList, summaryList);
			page++;
			pageable = PageRequest.of(page, size);
		} while (page < soaList.getTotalPages());

		page = 0;
		pageable = PageRequest.of(page, size);
		Page<QisSOAPayment> payList;
		do {
			payList = qisSOAPaymentRepository.getSOAPaymentYear(qisCorporate.getId(), year, pageable);
			addSOAPaymentToSummary(payList, summaryList);
			page++;
			pageable = PageRequest.of(page, size);
		} while (page < payList.getTotalPages());

		Collections.sort(summaryList);

		return summaryList;
	}

	private void addSOAToSummary(Page<QisSOA> soaList, List<QisSOASummary> summary) {
		if (soaList != null && summary != null) {
			for (QisSOA soa : soaList) {
				QisSOASummary ss = new QisSOASummary();
				ss.setDate(soa.getStatementDate());
				ss.setAmount(soa.getSoaAmount());
				ss.setTransaction(soa.getSoaNumber());
				ss.setType(1); // SOA

				summary.add(ss);
			}
		}
	}

	private void addSOAPaymentToSummary(Page<QisSOAPayment> payList, List<QisSOASummary> summary) {
		if (payList != null && summary != null) {
			for (QisSOAPayment pay : payList) {
				QisSOASummary ss = new QisSOASummary();
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
				ss.setType(2); // SOA PAYMENTS
				ss.setOtherNotes(pay.getOtherNotes());
				ss.setOtherAmount(pay.getOtherAmount());

				summary.add(ss);
			}
		}
	}
}