package quest.phil.diagnostic.information.system.ws.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.common.AppTransactionUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferral;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionDiscount;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionPayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionRefund;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionDiscountRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionItemRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionPaymentRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionAuthorizeRefundRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionAuthorizeRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionDiscountResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionItemResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionPaymentResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionResponse;
import quest.phil.diagnostic.information.system.ws.model.response.ReturnResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisBranchRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisPatientRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisReferralRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionDiscountRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryRequestRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionPaymentRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRefundRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisTransactionController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionController.class);
	private final String CATEGORY = "TRANSACTION";
	private double totalCashOut = 0d;
	private int totalRefundedItem = 0;

	@Autowired
	private QisTransactionRepository qisTransactionRepository;

	@Autowired
	private QisTransactionItemRepository qisTransactionItemRepository;

	@Autowired
	private QisTransactionPaymentRepository qisTransactionPaymentRepository;

	@Autowired
	private QisTransactionLaboratoryRequestRepository qisTransactionLaboratoryRequestRepository;

	@Autowired
	private QisTransactionDiscountRepository qisTransactionDiscountRepository;

	@Autowired
	private QisUserRepository qisUserRepository;

	@Autowired
	private QisBranchRepository qisBranchRepository;

	@Autowired
	private QisPatientRepository qisPatientRepository;

	@Autowired
	private QisTransactionRefundRepository qisTransactionRefundRepository;

	@Autowired
	private QisReferralRepository qisReferralRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	AppTransactionUtility appTransactionUtility;

	@Autowired
	private QisLogService qisLogService;

	// CREATE TRANSACTION
	@PostMapping("transaction")
	public QisTransaction createTransaction(@Valid @RequestBody QisTransactionRequest txnRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		System.out.println("QisTransaction");
		LOGGER.info(authUser.getId() + "-Create Transaction");
		String transactionId = appUtility.generateUserId(16);

		if (qisTransactionRepository.existsByTransactionid(transactionId)) {
			System.out.println("qisTransactionRepository.existsByTransactionid(transactionId");
			throw new RuntimeException("Duplicate transaction id.",
					new Throwable("transactionid: Duplicate transaction id."));
		}

		String status = appUtility.getTransactionStatus(txnRequest.getStatus());
		if (status == null) {
			System.out.println("status");
			throw new RuntimeException("Invalid status [" + txnRequest.getStatus() + "]",
					new Throwable("status: Invalid status [" + txnRequest.getStatus() + "]"));
		}

		String transactionType = appUtility.getTransactionType(txnRequest.getTransactionType());
		if (transactionType == null) {
			System.out.println("transactionType");
			throw new RuntimeException("Invalid transaction type [" + txnRequest.getTransactionType() + "]",
					new Throwable(
							"transactionType: Invalid transaction type [" + txnRequest.getTransactionType() + "]"));
		}

		String dispatchType = appUtility.getDispatchType(txnRequest.getDispatch());
		if (dispatchType == null) {
			System.out.println("dispatchType");
			throw new RuntimeException("Invalid dispatch type [" + txnRequest.getDispatch() + "]",
					new Throwable("dispatch: Invalid dispatch type [" + txnRequest.getDispatch() + "]"));
		}
		if ("TREF".equals(txnRequest.getTransactionType())
				&& (txnRequest.getReferralId() == null || "".equals(txnRequest.getReferralId().trim()))) {
			System.out.println("equals(txnRequest.getTransactionType()");
			throw new RuntimeException("Referral Id is required.", new Throwable("Referral Id is required."));
		}

		if ("E".equals(txnRequest.getDispatch())
				&& (txnRequest.getEmailTo() == null || "".equals(txnRequest.getEmailTo().trim()))) {
			System.out.println("equals(txnRequest.getDispatch()");
			throw new RuntimeException("Email To is required.", new Throwable("Email To is required."));
		}

		QisReferral referral = null;
		if ("TREF".equals(txnRequest.getTransactionType())) {
			System.out.println("equals(txnRequest.getTransactionType()");
			referral = qisReferralRepository.findByReferralid(txnRequest.getReferralId());
			if (referral == null) {
				System.out.println("referral");
				throw new RuntimeException("Invalid referral id[" + txnRequest.getReferralId() + "]",
						new Throwable("referralId: Invalid referral id[" + txnRequest.getReferralId() + "]"));

			}
		}

		QisTransaction qisTransaction = new QisTransaction();
		qisTransaction.setTaxRate(appUtility.getTaxRate());

		Set<QisTransactionItem> transactionItems = getTransactionItems(qisTransaction,
				txnRequest.getTransactionItems());
		if (transactionItems.isEmpty()) {
			System.out.println("transactionItems.isEmpty()");
			throw new RuntimeException("Please add transaction items.",
					new Throwable("transactionItems: Please add transaction items."));
		}

		if (qisTransaction.getDiscountType() != null) {
			System.out.println("qisTransaction.getDiscountType() ");
			if ("SCD".equals(qisTransaction.getDiscountType()) && txnRequest.getSeniorCitizenID() == null) {
				System.out.println("equals(qisTransaction.getDiscountType()) ");
				throw new RuntimeException("Senior Citizen ID is required.",
						new Throwable("seniorCitizenID: Senior Citizen ID is required."));
			}
			if ("PWD".equals(qisTransaction.getDiscountType()) && txnRequest.getPwdID() == null) {
				System.out.println("ction.getDiscountType()) && txnRequest.getPwdID()");
				throw new RuntimeException("Person With Disability ID is required.",
						new Throwable("pwdID: Person With Disability ID is required."));
			}
		}

		QisTransactionDiscount qisTransactionDiscount = null;
		if (txnRequest.getTransactionDiscount() != null) {
			System.out.println("xnRequest.getTransactionDiscount() ");

			if (qisTransaction.getDiscountRate() > 0) {
				System.out.println("xnRequest.getTransactionDiscount() ");
				throw new RuntimeException("Special discount is not allowed with discounted items.",
						new Throwable("transactionDiscount: Special discount is not allowed with discounted items."));
			} else {
				System.out.println("qisTransaction.getDiscountRate() else");
				QisTransactionDiscountRequest txnDiscReq = txnRequest.getTransactionDiscount();
				QisUser qisAuthorize = qisUserRepository.findByUserid(txnDiscReq.getAuthorizeId());
				if (qisAuthorize == null) {
					System.out.println("qisAuthorize == null");
					throw new RuntimeException("Invalid authorization id[" + txnDiscReq.getAuthorizeId() + "]",
							new Throwable("transactionDiscount.authorizeId: Invalid authorization id["
									+ txnDiscReq.getAuthorizeId() + "]"));
				}

				Double discountAmount = appUtility.parseDoubleAmount(txnDiscReq.getDiscountAmount());
				if (discountAmount == null) {
					System.out.println("discountAmount == null");
					throw new RuntimeException("Invalid discount amount.",
							new Throwable("transactionDiscount.discountAmount: Invalid discount amount."));
				}

				qisTransactionDiscount = new QisTransactionDiscount();
				qisTransactionDiscount.setAuthorizationId(qisAuthorize.getId());
				qisTransactionDiscount.setAuthorizedBy(qisAuthorize);
				qisTransactionDiscount.setDiscount(discountAmount);
				qisTransactionDiscount.setDescription(txnDiscReq.getDescription());
				qisTransaction.setSpecialDiscountAmount(qisTransactionDiscount.getDiscount());
			}
		}
		Set<QisTransactionPayment> transactionPayments = getTransactionPayments(txnRequest.getTransactionPayments());
		if (!"SHO".equals(txnRequest.getStatus()) && transactionPayments.isEmpty()) {
			System.out.println("equals(txnRequest.getStatus())");
			throw new RuntimeException("Please add transaction payments.",
					new Throwable("transactionPayments: Please add transaction payments."));
		}

		BeanUtils.copyProperties(txnRequest, qisTransaction);

		QisBranch qisBranch = qisBranchRepository.findByBranchid(txnRequest.getBranchId());
		if (qisBranch == null) {
			System.out.println("qisBranch == null");
			throw new RuntimeException("Invalid branch id[" + txnRequest.getBranchId() + "]",
					new Throwable("branchId: Invalid branch id[" + txnRequest.getBranchId() + "]"));
		}

		QisPatient qisPatient = qisPatientRepository.findByPatientid(txnRequest.getPatientId());
		if (qisPatient == null) {
			System.out.println("qisPatient == null");
			throw new RuntimeException("Invalid patient id[" + txnRequest.getPatientId() + "]",
					new Throwable("patientId: Invalid patient id[" + txnRequest.getPatientId() + "]"));
		}

		QisUser qisCashier = qisUserRepository.findByUserid(txnRequest.getCashierId());
		if (qisCashier == null) {
			System.out.println("qisCashier == null");
			throw new RuntimeException("Invalid cashier id[" + txnRequest.getCashierId() + "]",
					new Throwable("cashierId: Invalid cashier id[" + txnRequest.getCashierId() + "]"));
		} else {
			System.out.println("qisCashier == null else");
			Set<String> roles = appUtility.getStrUserRoles(qisCashier.getRoles());
			if (!roles.contains("CASHIER") && !roles.contains("ADMIN")) {
				System.out.println("contains(ADMIN)");
				throw new RuntimeException("Invalid cashier [" + txnRequest.getCashierId() + "]",
						new Throwable("cashierId: Invalid cashier id[" + txnRequest.getCashierId() + "]"));
			}
		}

		if (txnRequest.getTransactionDateTime() == null) {
			System.out.println(".getTransactionDateTime()");
			qisTransaction.setTransactionDate(Calendar.getInstance());
		} else {
			System.out.println("txnRequest.getTransactionDateTime() == null else");
			Calendar transactionDate = appUtility.stringToCalendarDate(txnRequest.getTransactionDateTime(),
					AppTransactionUtility.DATEFORMAT);
			qisTransaction.setTransactionDate(transactionDate);
		}

		qisTransaction.setTransactionItems(transactionItems);
		qisTransaction.setTransactionPayments(transactionPayments);

		qisTransaction.computeItems();
		qisTransaction.computePayments();
		qisTransaction.computeChangeAmount();

		if (qisTransaction.getSpecialDiscountAmount() > 0 && qisTransaction.getTotalItemNetAmount() < 0) {
			System.out.println("isTransaction.getSpecialDiscountAmount()");
			throw new RuntimeException("Invalid discount amount.",
					new Throwable("transactionDiscount.discountAmount: Invalid discount amount."));
		}

		if (!"SHO".equals(txnRequest.getStatus())) {
			System.out.println("getStatus()");
			if (qisTransaction.getTotalItemAmountDue() > qisTransaction.getTotalPaymentAmount()) {
				System.out.println("n.getTotalItemAmountDue() ");
				throw new RuntimeException("Inadequate payment.",
						new Throwable("transactionPayments: Inadequate payment."));
			}
		}

		qisTransaction.setTransactionid(transactionId);
		qisTransaction.setBranchId(qisBranch.getId());
		qisTransaction.setBranch(qisBranch);
		qisTransaction.setPatientId(qisPatient.getId());
		qisTransaction.setPatient(qisPatient);
		qisTransaction.setCashierId(qisCashier.getId());
		qisTransaction.setCashier(qisCashier);
		qisTransaction.setCreatedBy(authUser.getId());
		qisTransaction.setUpdatedBy(authUser.getId());

		if ("TREF".equals(txnRequest.getTransactionType()) && referral != null) {
			System.out.println("getTransactionType())");
			qisTransaction.setReferralId(referral.getId());
			qisTransaction.setReferral(referral);
		}

		QisTransactionResponse qisTransactionResponse = new QisTransactionResponse();
		BeanUtils.copyProperties(qisTransaction, qisTransactionResponse);
		qisTransactionResponse.setTransactionDate(appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(),
				AppTransactionUtility.DATEFORMAT));
		qisTransactionResponse.setBranchId(qisBranch.getBranchCode());
		qisTransactionResponse.setPatientId(qisPatient.getPatientid());
		qisTransactionResponse.setPatientName(appUtility.getPatientFullname(qisPatient));
		qisTransactionResponse.setCashierId(qisCashier.getUserid());
		qisTransactionResponse.setCashierName(appUtility.getUsername(qisCashier));

		qisTransactionRepository.save(qisTransaction);
		if (qisTransaction.getId() != null) {
			System.out.println("qisTransaction.getId() != null");
			try {
				System.out.println("try");
				Set<QisTransactionLaboratoryRequest> transactionLabRequests = saveQisTransactionItem(transactionItems,
						qisTransaction.getId(), authUser.getId());
				saveQisTransactionPayment(transactionPayments, qisTransaction.getId(), authUser.getId());
				saveQisTransactionLaboratoryRequest(transactionLabRequests, qisTransaction.getId(), authUser.getId());
				if (qisTransactionDiscount != null) {
					System.out.println("qisTransactionDiscount != null");
					saveQisTransactionDiscount(qisTransactionDiscount, qisTransaction.getId(), authUser.getId());
					qisTransaction.setSpecialDiscount(qisTransactionDiscount);
				}
			} catch (Exception e) {
				qisTransactionRepository.delete(qisTransaction);
				System.out.println("catch");
				throw new RuntimeException("Error saving transaction .", e);
			}
		} else {
			System.out.println("qisTransaction.getId() != null else");
			throw new RuntimeException("Error saving transaction .");
		}
		System.out.println("kev");
		qisTransaction.setTransactionDate(Calendar.getInstance());
		qisTransactionResponse.setItemTransactions(getTransactionItemsResponse(qisTransaction.getTransactionItems()));
		qisTransactionResponse
				.setPaymentTransactions(getTransactionPaymentsResponse(qisTransaction.getTransactionPayments()));
		System.out.println("qisTransactionResponse");
		if (qisTransactionDiscount != null) {
			System.out.println("qisTransactionDiscount != null");
			qisTransactionResponse.setSpecialDiscount(getTransactionDiscountRequest(qisTransactionDiscount));
		}

		qisLogService.info(authUser.getId(), QisTransactionController.class.getSimpleName(), "CREATE",
				qisTransactionResponse.toString(), qisTransaction.getId(), CATEGORY);
		System.out.println("qisLogService.info");

		return qisTransaction;
	}

	// READ TRANSACTION
	@GetMapping("transaction/{transactionId}")
	public QisTransaction getTransaction(@PathVariable String transactionId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			qisTransaction = qisTransactionRepository.getTransactionById(Long.parseLong(transactionId));
			if (qisTransaction == null) {
				throw new RuntimeException("Transaction not found.",
						new Throwable("transactionId: Transaction not found."));
			}
		}

		QisTransactionResponse qisTransactionResponse = new QisTransactionResponse();
		BeanUtils.copyProperties(qisTransaction, qisTransactionResponse);

		QisBranch qisBranch = qisTransaction.getBranch();
		QisPatient qisPatient = qisTransaction.getPatient();
		QisUser qisCashier = qisTransaction.getCashier();
		if (qisTransaction.getSpecialDiscountAmount() > 0) {
			QisTransactionDiscount qisTransactionDiscount = qisTransactionDiscountRepository
					.getTransactionDiscountByTransactionId(qisTransaction.getId());
			if (qisTransactionDiscount != null) {
				qisTransaction.setSpecialDiscount(qisTransactionDiscount);
			}
		}

		qisTransactionResponse.setTransactionDate(
				appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(), "yyyy-MMM-dd HH:mm:ss"));
		qisTransactionResponse.setBranchId(qisBranch.getBranchCode());
		qisTransactionResponse.setPatientId(qisPatient.getPatientid());
		qisTransactionResponse.setPatientName(appUtility.getPatientFullname(qisPatient));
		qisTransactionResponse.setCashierId(qisCashier.getUserid());
		qisTransactionResponse.setCashierName(appUtility.getUsername(qisCashier));

		qisTransactionResponse.setItemTransactions(getTransactionItemsResponse(qisTransaction.getTransactionItems()));
		qisTransactionResponse
				.setPaymentTransactions(getTransactionPaymentsResponse(qisTransaction.getTransactionPayments()));
		QisTransactionDiscount qisTransactionDiscount = qisTransaction.getSpecialDiscount();
		if (qisTransactionDiscount != null) {
			qisTransactionResponse.setSpecialDiscount(getTransactionDiscountRequest(qisTransactionDiscount));

		}
		qisLogService.info(authUser.getId(), QisTransactionController.class.getSimpleName(), "VIEW",
				qisTransactionResponse.toString(), qisTransaction.getId(), CATEGORY);

		return qisTransaction;
	}

	// UPDATE TRANSACTION
	@PutMapping("transaction/{transactionId}")
	public QisTransaction updateTransaction(@PathVariable String transactionId,
			@Valid @RequestBody QisTransactionRequest txnRequest, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {

		LOGGER.info(authUser.getId() + "-Update Transaction:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId: Transaction not found."));
		}

		// HOLD is not included on the authorization update
		if (!"SHO".equals(qisTransaction.getStatus())) {
			if (txnRequest.getAuthorizeUpdateRequest() == null) {
				throw new RuntimeException("Authorization to update is required.",
						new Throwable("authorizeUpdateRequest: Authorization to update is required."));
			}
		}

		String status = appUtility.getTransactionStatus(txnRequest.getStatus());
		if (status == null) {
			throw new RuntimeException("Invalid status [" + txnRequest.getStatus() + "]",
					new Throwable("status: Invalid status [" + txnRequest.getStatus() + "]"));
		}

		String transactionType = appUtility.getTransactionType(txnRequest.getTransactionType());
		if (transactionType == null) {
			throw new RuntimeException("Invalid transaction type [" + txnRequest.getTransactionType() + "]",
					new Throwable(
							"transactionType: Invalid transaction type [" + txnRequest.getTransactionType() + "]"));
		}

		String dispatchType = appUtility.getDispatchType(txnRequest.getDispatch());
		if (dispatchType == null) {
			throw new RuntimeException("Invalid dispatch type [" + txnRequest.getDispatch() + "]",
					new Throwable("dispatch: Invalid dispatch type [" + txnRequest.getDispatch() + "]"));
		}

		if ("TREF".equals(txnRequest.getTransactionType())
				&& (txnRequest.getReferralId() == null || "".equals(txnRequest.getReferralId().trim()))) {
			throw new RuntimeException("Referral Id is required.", new Throwable("Referral Id is required."));
		}

		if ("E".equals(txnRequest.getDispatch())
				&& (txnRequest.getEmailTo() == null || "".equals(txnRequest.getEmailTo().trim()))) {
			throw new RuntimeException("Email To is required.", new Throwable("Email To is required."));
		}

		QisReferral requestedReferral = null;
		if ("TREF".equals(txnRequest.getTransactionType())) {
			requestedReferral = qisReferralRepository.findByReferralid(txnRequest.getReferralId());
			if (requestedReferral == null) {
				throw new RuntimeException("Invalid referral id[" + txnRequest.getReferralId() + "]",
						new Throwable("referralId: Invalid referral id[" + txnRequest.getReferralId() + "]"));

			}
		}

		QisTransaction qisTransactionReq = new QisTransaction();
		qisTransactionReq.setTaxRate(appUtility.getTaxRate());

		Set<QisTransactionItem> transactionItemsReq = getTransactionItems(qisTransactionReq,
				txnRequest.getTransactionItems());
		if (transactionItemsReq.isEmpty()) {
			throw new RuntimeException("Please add transaction items.",
					new Throwable("transactionItems: Please add transaction items."));
		}

		if (qisTransactionReq.getDiscountType() != null) {
			if ("SCD".equals(qisTransactionReq.getDiscountType()) && txnRequest.getSeniorCitizenID() == null) {
				throw new RuntimeException("Senior Citizen ID is required.",
						new Throwable("seniorCitizenID: Senior Citizen ID is required."));

			}
			if ("PWD".equals(qisTransactionReq.getDiscountType()) && txnRequest.getPwdID() == null) {
				throw new RuntimeException("Person With Disability ID is required.",
						new Throwable("pwdID: Person With Disability ID is required."));

			}
		}

		QisTransactionDiscount qisTransactionDiscount = null;
		if (txnRequest.getTransactionDiscount() != null) {
			if (qisTransactionReq.getDiscountRate() > 0) {
				throw new RuntimeException("Special discount is not allowed with discounted items.",
						new Throwable("transactionDiscount: Special discount is not allowed with discounted items."));
			} else {
				QisTransactionDiscountRequest txnDiscReq = txnRequest.getTransactionDiscount();
				QisUser qisAuthorize = qisUserRepository.findByUserid(txnDiscReq.getAuthorizeId());
				if (qisAuthorize == null) {
					throw new RuntimeException("Invalid authorization id[" + txnDiscReq.getAuthorizeId() + "]",
							new Throwable("transactionDiscount.authorizeId: Invalid authorization id["
									+ txnDiscReq.getAuthorizeId() + "]"));
				}

				Double discountAmount = appUtility.parseDoubleAmount(txnDiscReq.getDiscountAmount());
				if (discountAmount == null) {
					throw new RuntimeException("Invalid discount amount.",
							new Throwable("transactionDiscount.discountAmount: Invalid discount amount."));
				}

				qisTransactionDiscount = new QisTransactionDiscount();
				qisTransactionDiscount.setAuthorizationId(qisAuthorize.getId());
				qisTransactionDiscount.setAuthorizedBy(qisAuthorize);
				qisTransactionDiscount.setDiscount(discountAmount);
				qisTransactionDiscount.setDescription(txnDiscReq.getDescription());
				qisTransactionReq.setSpecialDiscountAmount(qisTransactionDiscount.getDiscount());
			}
		}

		Set<QisTransactionPayment> transactionPaymentsReq = getTransactionPayments(txnRequest.getTransactionPayments());
		if (!"SHO".equals(txnRequest.getStatus()) && transactionPaymentsReq.isEmpty()) {
			throw new RuntimeException("Please add transaction payments.",
					new Throwable("transactionPayments: Please add transaction payments."));
		}

		qisTransactionReq.setTransactionItems(transactionItemsReq);
		qisTransactionReq.setTransactionPayments(transactionPaymentsReq);

		qisTransactionReq.computeItems();
		qisTransactionReq.computePayments();
		qisTransactionReq.computeChangeAmount();

		if (qisTransactionReq.getSpecialDiscountAmount() > 0 && qisTransactionReq.getTotalItemNetAmount() < 0) {
			throw new RuntimeException("Invalid discount amount.",
					new Throwable("transactionDiscount.discountAmount: Invalid discount amount."));
		}

		if (!"SHO".equals(txnRequest.getStatus())) {
			if (qisTransactionReq.getTotalItemAmountDue() > qisTransactionReq.getTotalPaymentAmount()) {
				throw new RuntimeException("Inadequate payment.",
						new Throwable("transactionPayments: Inadequate payment."));
			}
		}

		// UPDATE TRANSACTION HEADER
		boolean isUpdate = false;
		String updateData = "";
		if (!qisTransaction.getStatus().equals(txnRequest.getStatus())) {
			if ("SHO".equals(qisTransaction.getStatus())) { // FROM HOLD TO NEW STATUS
				Calendar transactionDate = Calendar.getInstance();

				Calendar txtDateTime = Calendar.getInstance();
				txtDateTime.setTime(qisTransaction.getTransactionDate().getTime());

				String txtDate = appUtility.calendarToFormatedDate(txtDateTime, AppTransactionUtility.DATEFORMAT);
				String newDate = appUtility.calendarToFormatedDate(transactionDate, AppTransactionUtility.DATEFORMAT);

				updateData = appUtility.formatUpdateData(updateData, "Date", txtDate, newDate);
				qisTransaction.setTransactionDate(Calendar.getInstance());
			}
			updateData = appUtility.formatUpdateData(updateData, "Status", qisTransaction.getStatus(),
					txnRequest.getStatus());
			isUpdate = true;
			qisTransaction.setStatus(txnRequest.getStatus());
		}

		// SET TRANSACTION DATE
		if (txnRequest.getTransactionDateTime() != null) {
			Calendar transactionDate = appUtility.stringToCalendarDate(txnRequest.getTransactionDateTime(),
					AppTransactionUtility.DATEFORMAT);
			if (transactionDate != null) {
				Calendar txtDateTime = Calendar.getInstance();
				txtDateTime.setTime(qisTransaction.getTransactionDate().getTime());

				String txtDate = appUtility.calendarToFormatedDate(txtDateTime, AppTransactionUtility.DATEFORMAT);
				String newDate = appUtility.calendarToFormatedDate(transactionDate, AppTransactionUtility.DATEFORMAT);

				if (!txtDate.equals(newDate)) {
					updateData = appUtility.formatUpdateData(updateData, "Date", txtDate, newDate);
					isUpdate = true;
					qisTransaction.setTransactionDate(transactionDate);
				}
			}
		}

		if (txnRequest.getBranchId() != null) {
			QisBranch requestQisBranch = qisBranchRepository.findByBranchid(txnRequest.getBranchId());
			if (requestQisBranch == null) {
				throw new RuntimeException("Invalid branch id[" + txnRequest.getBranchId() + "]",
						new Throwable("branchId: Invalid branch id[" + txnRequest.getBranchId() + "]"));
			}
			if (requestQisBranch.getId() != qisTransaction.getBranchId()) {
				updateData = appUtility.formatUpdateData(updateData, "Branch",
						qisTransaction.getBranch().getBranchCode(), requestQisBranch.getBranchCode());
				isUpdate = true;
				qisTransaction.setBranchId(requestQisBranch.getId());
				qisTransaction.setBranch(requestQisBranch);
			}

		}

		if (txnRequest.getPatientId() != null) {
			QisPatient requestQisPatient = qisPatientRepository.findByPatientid(txnRequest.getPatientId());
			if (requestQisPatient == null) {
				throw new RuntimeException("Invalid patient id[" + txnRequest.getPatientId() + "]",
						new Throwable("patientId:Invalid patient id[" + txnRequest.getPatientId() + "]"));
			}

			if (requestQisPatient.getId() != qisTransaction.getPatientId()) {
				updateData = appUtility.formatUpdateData(updateData, "Patient",
						qisTransaction.getPatient().getPatientid() + ":"
								+ appUtility.getPatientFullname(qisTransaction.getPatient()),
						requestQisPatient.getPatientid() + ":" + appUtility.getPatientFullname(requestQisPatient));
				isUpdate = true;
				qisTransaction.setPatientId(requestQisPatient.getId());
				qisTransaction.setPatient(requestQisPatient);
			}
		}

		if (txnRequest.getCashierId() != null) {
			QisUser requestQisCashier = qisUserRepository.findByUserid(txnRequest.getCashierId());
			if (requestQisCashier == null) {
				throw new RuntimeException("Invalid cashier id[" + txnRequest.getCashierId() + "]");
			} else {
				Set<String> roles = appUtility.getStrUserRoles(requestQisCashier.getRoles());
				if (!roles.contains("CASHIER") && !roles.contains("ADMIN")) {
					throw new RuntimeException("Invalid cashier [" + txnRequest.getCashierId() + "]",
							new Throwable("cashierId: Invalid cashier id[" + txnRequest.getCashierId() + "]"));
				}
			}

			if (requestQisCashier.getId() != qisTransaction.getCashierId()) {
				updateData = appUtility.formatUpdateData(updateData, "Cashier",
						qisTransaction.getCashier().getUserid() + ":" + qisTransaction.getCashier().getUsername(),
						requestQisCashier.getUserid() + ":" + requestQisCashier.getUsername());
				isUpdate = true;
				qisTransaction.setCashierId(requestQisCashier.getId());
				qisTransaction.setCashier(requestQisCashier);
			}
		}

		if (!txnRequest.getTransactionType().equals(qisTransaction.getTransactionType())) {
			updateData = appUtility.formatUpdateData(updateData, "Type", qisTransaction.getTransactionType(),
					txnRequest.getTransactionType());
			isUpdate = true;
			qisTransaction.setTransactionType(txnRequest.getTransactionType());
		}

		if (!txnRequest.getDispatch().equals(qisTransaction.getDispatch())) {
			updateData = appUtility.formatUpdateData(updateData, "Dispatch", qisTransaction.getDispatch(),
					txnRequest.getDispatch());
			isUpdate = true;
			if ("E".equals(qisTransaction.getDispatch())) {
				updateData = appUtility.formatUpdateData(updateData, "EmailTo", qisTransaction.getEmailTo(), null);
				qisTransaction.setEmailTo(null);
			} else {
				if (qisTransaction.getEmailTo() == null) {
					updateData = appUtility.formatUpdateData(updateData, "EmailTo", null, txnRequest.getEmailTo());
					qisTransaction.setEmailTo(txnRequest.getEmailTo());
				}
			}
			qisTransaction.setDispatch(txnRequest.getDispatch());
		}

		if ("E".equals(qisTransaction.getDispatch())) {
			if (!txnRequest.getEmailTo().equals(qisTransaction.getEmailTo())) {
				isUpdate = true;
				updateData = appUtility.formatUpdateData(updateData, "EmailTo", qisTransaction.getEmailTo(),
						txnRequest.getEmailTo());
				qisTransaction.setEmailTo(txnRequest.getEmailTo());
			}
		}

		if (txnRequest.getRemarks() != null) {
			if (!txnRequest.getRemarks().equals(qisTransaction.getRemarks())) {
				updateData = appUtility.formatUpdateData(updateData, "Remarks", qisTransaction.getRemarks(),
						txnRequest.getRemarks());
				isUpdate = true;
				qisTransaction.setRemarks(txnRequest.getRemarks());
			}
		}

		if ("TREF".equals(txnRequest.getTransactionType())) {
			if (requestedReferral.getId() != qisTransaction.getReferralId()) {
				if (qisTransaction.getReferralId() == null) {	
					updateData = appUtility.formatUpdateData(updateData, "Referral",
							"", requestedReferral.getReferralid());
					isUpdate = true;
					qisTransaction.setReferralId(requestedReferral.getId());
					qisTransaction.setReferral(requestedReferral);
				} else {
					updateData = appUtility.formatUpdateData(updateData, "Referral",
							qisTransaction.getReferral().getReferralid(), requestedReferral.getReferralid());
					isUpdate = true;
					qisTransaction.setReferralId(requestedReferral.getId());
					qisTransaction.setReferral(requestedReferral);
				}
			}

		}

		// UPDATE TRANSACTION ITEMS
		boolean isUpdateItems = false;
		ArrayList<String> updateDataItem = new ArrayList<String>();
		// ADD OR UPDATE ITEMS
		for (QisTransactionItem txnItemReq : transactionItemsReq) {
			if (txnItemReq.getId() == null) { // ADD
				isUpdateItems = true;
				updateDataItem.add(addItemToTransaction(txnItemReq, qisTransaction, authUser));
			} else { // UPDATE
				QisTransactionItem txnItem = qisTransaction.getTransactionItems().stream()
						.filter(itm -> txnItemReq.getId() == itm.getId()).findAny().orElse(null);
				if (txnItem == null) { // ADD
					isUpdateItems = true;
					txnItemReq.setId(null);
					updateDataItem.add(addItemToTransaction(txnItemReq, qisTransaction, authUser));
				} else { // UPDATE
					String updateItem = "";

					if (txnItem.getQuantity() != txnItemReq.getQuantity()) {
						updateItem = appUtility.formatUpdateData(updateItem, "Quantity",
								String.valueOf(txnItem.getQuantity()), String.valueOf(txnItemReq.getQuantity()));
						isUpdateItems = true;
						txnItem.setQuantity(txnItemReq.getQuantity());
					}

					if (txnItem.getDiscountRate() != txnItemReq.getDiscountRate()) {
						updateItem = appUtility.formatUpdateData(updateItem, "Discount Rate",
								String.valueOf(txnItem.getDiscountRate()),
								String.valueOf(txnItemReq.getDiscountRate()));
						isUpdateItems = true;
						txnItem.setDiscountRate(txnItemReq.getDiscountRate());
					}

					if (txnItemReq.getDiscountType() != null
							&& !txnItem.getDiscountType().equals(txnItemReq.getDiscountType())) {
						updateItem = appUtility.formatUpdateData(updateItem, "Discount Type", txnItem.getDiscountType(),
								txnItemReq.getDiscountType());
						isUpdateItems = true;
						txnItem.setDiscountType(txnItemReq.getDiscountType());
					}

					if (!txnItemReq.getItemReference().equals(txnItem.getItemReference())) {// different
						updateItem = appUtility.formatUpdateData(updateItem, "Item Reference",
								txnItem.getItemType() + ":" + txnItem.getItemReference(),
								txnItemReq.getItemType() + ":" + txnItemReq.getItemReference());
						isUpdateItems = true;
						txnItem.setItemReference(txnItemReq.getItemReference());
						txnItem.setItemType(txnItemReq.getItemType());

						// DELETE PREVIOUS LABORATORY LIST
						List<QisTransactionLaboratoryRequest> labRequests = qisTransactionLaboratoryRequestRepository
								.getTransactionLaboratoryRequestByTransactionidItemid(qisTransaction.getId(),
										txnItem.getId());
						if (!labRequests.isEmpty()) {
							qisTransactionLaboratoryRequestRepository.deleteAll(labRequests);
						}

						// ADD NEW LABORATORY LIST
						Set<QisTransactionLaboratoryRequest> newLabRequests = new HashSet<>();
						appTransactionUtility.getLaboratoryRequestList(newLabRequests, txnItemReq);
						saveQisTransactionLaboratoryRequest(newLabRequests, qisTransaction.getId(), authUser.getId());
					}

					if (!"".equals(updateItem)) {
						updateDataItem.add(" UPDATED[" + updateItem + "]");
						qisTransactionItemRepository.save(txnItem);
					}
				}
			}
		}

		// DELETE PREVIOUS ITEMS
		Set<QisTransactionItem> delTxnItems = new HashSet<>();
		for (QisTransactionItem txnItem : qisTransaction.getTransactionItems()) {
			QisTransactionItem txnItemReq = transactionItemsReq.stream().filter(itm -> txnItem.getId() == itm.getId())
					.findAny().orElse(null);
			if (txnItemReq == null) {
				delTxnItems.add(txnItem);
				String updateItem = txnItem.getItemType() + ":" + txnItem.getItemReference() + ":"
						+ txnItem.getQuantity();
				updateDataItem.add(" DELETED[" + updateItem + "]");
				isUpdateItems = true;

				// DELETE PREVIOUS LABORATORY LIST
				List<QisTransactionLaboratoryRequest> labRequests = qisTransactionLaboratoryRequestRepository
						.getTransactionLaboratoryRequestByTransactionidItemid(qisTransaction.getId(), txnItem.getId());
				if (!labRequests.isEmpty()) {
					qisTransactionLaboratoryRequestRepository.deleteAll(labRequests);
				}
			}
		}
		if (!delTxnItems.isEmpty()) {
			qisTransactionItemRepository.deleteAll(delTxnItems);
		}

		if (isUpdateItems) {
			for (String strUpdateItem : updateDataItem) {
				updateData = appUtility.addToFormatedData(updateData, "ITEMS", strUpdateItem);
			}
		}

		boolean isUpdatePayments = false;
		ArrayList<String> updateDataPayments = new ArrayList<String>();
		if (qisTransaction.getTransactionPayments().isEmpty()) { // no payment previously
			saveQisTransactionPayment(transactionPaymentsReq, qisTransaction.getId(), authUser.getId());
			isUpdatePayments = true;
			for (QisTransactionPayment txtPayment : transactionPaymentsReq) {
				QisTransactionPaymentResponse paymentRes = getQisTransactionPaymentResponse(txtPayment);
				updateDataPayments.add(" ADDED[" + paymentRes.toString() + "]");
			}
		} else {
			if (transactionPaymentsReq.isEmpty()) {
				qisTransactionPaymentRepository.deleteAll(qisTransaction.getTransactionPayments());
				isUpdatePayments = true;
				updateDataPayments.add(" DELETED[ALL PAYMENTS]");
			} else {
				for (QisTransactionPayment txnPayReq : transactionPaymentsReq) {
					if (txnPayReq.getId() == null) {
						isUpdatePayments = true;
						updateDataPayments.add(addPaymentToTransaction(txnPayReq, qisTransaction, authUser));
					} else {
						QisTransactionPayment txnPay = qisTransaction.getTransactionPayments().stream()
								.filter(pay -> txnPayReq.getId() == pay.getId()).findAny().orElse(null);
						if (txnPay == null) { // ADD
							isUpdatePayments = true;
							txnPayReq.setId(null);
							updateDataPayments.add(addPaymentToTransaction(txnPayReq, qisTransaction, authUser));
						} else { // UPDATE
							String updatePayment = "";

							if (!txnPay.getPaymentType().equals(txnPayReq.getPaymentType())) {
								String fromPayment = appTransactionUtility.paymentDetails(txnPay);
								String toPayment = appTransactionUtility.paymentDetails(txnPayReq);
								updatePayment = appUtility.formatUpdateData(updatePayment, "Payment Type", fromPayment,
										toPayment);
								isUpdatePayments = true;
								txnPay.setPaymentType(txnPayReq.getPaymentType());
								txnPay.setPaymentMode(txnPayReq.getPaymentMode());
								txnPay.setPaymentBank(txnPayReq.getPaymentBank());
								txnPay.setCardChequeNumber(txnPayReq.getCardChequeNumber());
								txnPay.setCcHolderName(txnPayReq.getCcHolderName());
								txnPay.setCcType(txnPayReq.getCcType());
								txnPay.setHmoAccountNumber(txnPayReq.getHmoAccountNumber());
								txnPay.setHmoLOE(txnPayReq.getHmoLOE());
								txnPay.setHmoApprovalCode(txnPayReq.getHmoApprovalCode());
								txnPay.setReferenceNumber(txnPayReq.getReferenceNumber());
							}

							if (!txnPay.getAmount().equals(txnPayReq.getAmount())) {
								updatePayment = appUtility.formatUpdateData(updatePayment, "Amount",
										String.valueOf(txnPay.getAmount()), String.valueOf(txnPayReq.getAmount()));
								isUpdatePayments = true;
								txnPay.setAmount(txnPayReq.getAmount());
							}

							if (!txnPay.getCurrency().equals(txnPayReq.getCurrency())) {
								updatePayment = appUtility.formatUpdateData(updatePayment, "Currency",
										txnPay.getCurrency(), txnPayReq.getCurrency());
								isUpdatePayments = true;
								txnPay.setCurrency(txnPayReq.getCurrency());
							}

							if (txnPay.getBillerId() != txnPayReq.getBillerId()) {
								String fromBiller = null;
								String toBiller = null;

								if (txnPay.getBillerId() != null) {
									fromBiller = txnPay.getBiller().getCompanyName();
								}

								if (txnPayReq.getBillerId() != null) {
									toBiller = txnPayReq.getBiller().getCompanyName();
								}

								updatePayment = appUtility.formatUpdateData(updatePayment, "Biller", fromBiller,
										toBiller);
								isUpdatePayments = true;
								txnPay.setBillerId(txnPayReq.getBillerId());
								txnPay.setBiller(txnPayReq.getBiller());
							}

							if (!"".equals(updatePayment)) {
								updateDataPayments.add(" UPDATED[" + updatePayment + "]");
								qisTransactionPaymentRepository.save(txnPay);
							}
						}
					}
				}
			} // else
		}

		// DELETE PREVIOUS PAYMENTS
		Set<QisTransactionPayment> delTxnPays = new HashSet<>();
		for (QisTransactionPayment txnPay : qisTransaction.getTransactionPayments()) {
			QisTransactionPayment txnPayReq = transactionPaymentsReq.stream()
					.filter(pay -> txnPay.getId() == pay.getId()).findAny().orElse(null);
			if (txnPayReq == null) {
				delTxnPays.add(txnPay);
				String updatePay = txnPay.getPaymentType() + ":" + txnPay.getAmount() + ":" + txnPay.getCurrency();
				updateDataPayments.add(" DELETED[" + updatePay + "]");
				isUpdatePayments = true;
			}
		}

		if (!delTxnPays.isEmpty()) {
			qisTransactionPaymentRepository.deleteAll(delTxnPays);
		}

		if (isUpdatePayments) {
			for (String strUpdatePay : updateDataPayments) {
				updateData = appUtility.addToFormatedData(updateData, "PAYMENTS", strUpdatePay);
			}
		}

		if (isUpdate) {
			qisTransaction.setTransactionItems(transactionItemsReq);
			qisTransaction.setTransactionPayments(transactionPaymentsReq);

			qisTransaction.computeItems();
			qisTransaction.computePayments();
			qisTransaction.computeChangeAmount();

			qisTransaction.setUpdatedBy(authUser.getId());
			qisTransaction.setUpdatedAt(Calendar.getInstance());
			qisTransactionRepository.save(qisTransaction);
		}

		if (isUpdatePayments || isUpdateItems || isUpdate) {
			qisLogService.info(authUser.getId(), QisTransactionController.class.getSimpleName(), "UPDATE", updateData,
					qisTransaction.getId(), CATEGORY);
		}

		if (txnRequest.getAuthorizeUpdateRequest() != null) {
			QisTransactionAuthorizeRequest txtAuthUpdate = txnRequest.getAuthorizeUpdateRequest();
			QisUser authorizeUser = qisUserRepository.findByUserid(txtAuthUpdate.getAuthorizeId());
			if (authorizeUser == null) {
				throw new RuntimeException("Unauthorize account[" + txtAuthUpdate.getAuthorizeId() + "]");
			} else {
				Set<String> roles = appUtility.getStrUserRoles(authorizeUser.getRoles());
				if (!roles.contains("AUDITOR") && !roles.contains("ADMIN")) {
					throw new RuntimeException("Unauthorize account [" + txtAuthUpdate.getAuthorizeId() + "]",
							new Throwable("authorizeId: Unauthorize account[" + txtAuthUpdate.getAuthorizeId() + "]"));
				}
			}

			qisLogService.info(authorizeUser.getId(), QisTransactionController.class.getSimpleName(), "AUTHORIZE",
					"UPDATE TRANSACTION:" + txtAuthUpdate.getDescription(), qisTransaction.getId(), CATEGORY);
		}

		qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		return qisTransaction;
	}

	// CANCEL TRANSACTION
	@PostMapping("transaction/{transactionId}/cancel")
	public ResponseEntity<?> cancelTransaction(@PathVariable String transactionId,
			@Valid @RequestBody QisTransactionAuthorizeRequest txnAuthRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Cancel Transaction:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId: Transaction not found."));
		}

		QisUser authorizeUser = qisUserRepository.findByUserid(txnAuthRequest.getAuthorizeId());
		if (authorizeUser == null) {
			throw new RuntimeException("Unauthorize account[" + txnAuthRequest.getAuthorizeId() + "]");
		} else {
			Set<String> roles = appUtility.getStrUserRoles(authorizeUser.getRoles());
			if (!roles.contains("AUDITOR") && !roles.contains("ADMIN")) {
				throw new RuntimeException("Unauthorize account [" + txnAuthRequest.getAuthorizeId() + "]",
						new Throwable("authorizeId: Unauthorize account[" + txnAuthRequest.getAuthorizeId() + "]"));
			}
		}

		qisTransaction.setStatus("SCA");
		qisTransaction.setUpdatedBy(authUser.getId());
		qisTransaction.setUpdatedAt(Calendar.getInstance());
		qisTransactionRepository.save(qisTransaction);

		Set<QisTransactionItem> transactionItems = qisTransaction.getTransactionItems();
		if (transactionItems != null) {
			transactionItems.forEach(item -> {
				item.setStatus(0);
				item.setUpdatedBy(authUser.getId());
				item.setUpdatedAt(Calendar.getInstance());
			});
			qisTransactionItemRepository.saveAll(transactionItems);
		}

		qisLogService.info(authorizeUser.getId(), QisTransactionController.class.getSimpleName(), "AUTHORIZE",
				"CANCEL TRANSACTION:" + txnAuthRequest.getDescription(), qisTransaction.getId(), CATEGORY);

		qisLogService.info(authUser.getId(), QisTransactionController.class.getSimpleName(), "CANCEL",
				"TRANSACTION[" + transactionId + "]", qisTransaction.getId(), CATEGORY);

		return ResponseEntity.ok(new ReturnResponse("success", "transaction cancelled"));
	}

	@PostMapping("transaction/{transactionId}/refund")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisTransaction resundTransaction(@PathVariable String transactionId,
			@Valid @RequestBody QisTransactionAuthorizeRefundRequest txnAuthRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Refund Transaction:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId: Transaction not found."));
		}

		if (!"SPD".equals(qisTransaction.getStatus())) {
			throw new RuntimeException("Transaction is not for refund.",
					new Throwable("transactionId: Transaction is not for refund."));
		}

		QisUser authorizeUser = qisUserRepository.findByUserid(txnAuthRequest.getAuthorizeId());
		if (authorizeUser == null) {
			throw new RuntimeException("Unauthorize account[" + txnAuthRequest.getAuthorizeId() + "]");
		} else {
			Set<String> roles = appUtility.getStrUserRoles(authorizeUser.getRoles());
			if (!roles.contains("AUDITOR") && !roles.contains("ADMIN")) {
				throw new RuntimeException("Unauthorize account [" + txnAuthRequest.getAuthorizeId() + "]",
						new Throwable("authorizeId: Unauthorize account[" + txnAuthRequest.getAuthorizeId() + "]"));
			}
		}

		String refundTransactionId = appUtility.generateUserId(16);
		if (qisTransactionRepository.existsByTransactionid(refundTransactionId))
			throw new RuntimeException("Duplicate refund transaction id.",
					new Throwable("refundTransactionid: Duplicate refund transaction id."));

		QisUser qisCashier = qisUserRepository.findByUserid(authUser.getUserid());
		if (qisCashier == null) {
			throw new RuntimeException("Invalid cashier id[" + authUser.getUserid() + "]",
					new Throwable("cashierId: Invalid cashier id[" + authUser.getUserid() + "]"));
		} else {
			Set<String> roles = appUtility.getStrUserRoles(qisCashier.getRoles());
			if (!roles.contains("CASHIER") && !roles.contains("AUDITOR") && !roles.contains("ADMIN")) {
				throw new RuntimeException("Invalid cashier [" + authUser.getUserid() + "]",
						new Throwable("cashierId: Invalid cashier id[" + authUser.getUserid() + "]"));
			}
		}

		QisTransaction refundTransaction = new QisTransaction();
		BeanUtils.copyProperties(qisTransaction, refundTransaction);

		refundTransaction.setId(null);
		refundTransaction.setTransactionid(refundTransactionId);
		refundTransaction.setStatus("SRE");
		refundTransaction.setTransactionDate(Calendar.getInstance());
		refundTransaction.setCreatedBy(authUser.getId());
		refundTransaction.setCreatedAt(Calendar.getInstance());
		refundTransaction.setUpdatedBy(authUser.getId());
		refundTransaction.setUpdatedAt(Calendar.getInstance());

		refundTransaction.setCashier(qisCashier);
		refundTransaction.setTransactionItems(null);
		refundTransaction.setTransactionPayments(null);
		refundTransaction.setSpecialDiscount(null);
		refundTransaction.setSpecialDiscountAmount(0d);

		Set<QisTransactionItem> transactionItems = qisTransaction.getTransactionItems();
		Set<QisTransactionItem> refundedItems = new HashSet<>();
		this.totalCashOut = 0d;
		this.totalRefundedItem = 0;

		transactionItems.forEach(item -> {
			if (txnAuthRequest.getRefundItems().contains(item.getId()) && item.getStatus() == 1) {
				QisTransactionItem refundItem = new QisTransactionItem();
				BeanUtils.copyProperties(item, refundItem);

				item.setUpdatedBy(authUser.getId());
				item.setUpdatedAt(Calendar.getInstance());
				item.setStatus(0); // cancelled

				refundItem.setId(null);
				refundItem.setTransactionid(null);
				refundItem.setCreatedBy(authUser.getId());
				refundItem.setCreatedAt(Calendar.getInstance());
				refundItem.setUpdatedBy(authUser.getId());
				refundItem.setUpdatedAt(Calendar.getInstance());
				refundItem.setStatus(-1);// refunded
				refundedItems.add(refundItem);

				this.totalCashOut += item.getAmountDue();
			}

			if (item.getStatus() == 0) {
				this.totalRefundedItem++;
			}
		});

		if (refundedItems.size() <= 0) {
			throw new RuntimeException("No items to be refunded.", new Throwable("No items to be refunded."));
		}

		Set<QisTransactionPayment> refundPayments = new HashSet<>();
		QisTransactionPayment payment = new QisTransactionPayment();
		payment.setAmount(this.totalCashOut);
		payment.setPaymentType("CA");
		payment.setCurrency(qisTransaction.getCurrency());
		payment.setCreatedBy(authUser.getId());
		payment.setCreatedAt(Calendar.getInstance());
		payment.setUpdatedBy(authUser.getId());
		payment.setUpdatedAt(Calendar.getInstance());
		payment.setStatus(-1); // refunded
		refundPayments.add(payment);

		refundTransaction.setTransactionItems(refundedItems);
		refundTransaction.setTransactionPayments(refundPayments);

		refundTransaction.computeItems();
		refundTransaction.computePayments();
		refundTransaction.computeChangeAmount();
		refundTransaction.setRefundStatus(1);

		int refundStatus = 1;
		if (transactionItems.size() != this.totalRefundedItem) {
			refundStatus = 2;
		}

		qisTransaction.setRefundStatus(refundStatus);
		qisTransaction.setUpdatedBy(authUser.getId());
		qisTransaction.setUpdatedAt(Calendar.getInstance());

		qisTransactionRepository.save(refundTransaction);
		if (refundTransaction.getId() != null) {
			try {
				saveQisTransactionItem(refundedItems, refundTransaction.getId(), authUser.getId());
				saveQisTransactionPayment(refundPayments, refundTransaction.getId(), authUser.getId());

				qisTransactionRepository.save(qisTransaction);
				qisTransactionItemRepository.saveAll(transactionItems);

				QisTransactionRefund authRefund = new QisTransactionRefund();
				authRefund.setBaseTransactionId(qisTransaction.getId());
				authRefund.setRefundTransactionId(refundTransaction.getId());
				authRefund.setAuthorizeId(authorizeUser.getId());
				authRefund.setReason(txnAuthRequest.getDescription());
				authRefund.setCreatedBy(authUser.getId());
				authRefund.setUpdatedBy(authUser.getId());
				qisTransactionRefundRepository.save(authRefund);
			} catch (Exception e) {
				qisTransactionRepository.delete(refundTransaction);
				throw new RuntimeException("Error saving transaction.", e);
			}
		} else {
			throw new RuntimeException("Error saving transaction.");
		}

		QisTransactionResponse qisTransactionResponse = new QisTransactionResponse();
		BeanUtils.copyProperties(refundTransaction, qisTransactionResponse);
		qisTransactionResponse.setTransactionDate(appUtility
				.calendarToFormatedDate(refundTransaction.getTransactionDate(), AppTransactionUtility.DATEFORMAT));
		qisTransactionResponse.setPatientId(refundTransaction.getPatient().getPatientid());
		qisTransactionResponse.setPatientName(appUtility.getPatientFullname(refundTransaction.getPatient()));
		qisTransactionResponse.setCashierId(qisCashier.getUserid());
		qisTransactionResponse.setCashierName(appUtility.getUsername(qisCashier));

		qisTransactionResponse
				.setItemTransactions(getTransactionItemsResponse(refundTransaction.getTransactionItems()));
		qisTransactionResponse
				.setPaymentTransactions(getTransactionPaymentsResponse(refundTransaction.getTransactionPayments()));

		qisLogService.info(authUser.getId(), QisTransactionController.class.getSimpleName(), "REFUND",
				qisTransactionResponse.toString(), qisTransaction.getId(), CATEGORY);
		qisLogService.info(authorizeUser.getId(), QisTransactionController.class.getSimpleName(), "AUTHORIZE",
				"REFUND TRANSACTION:" + txnAuthRequest.getDescription(), qisTransaction.getId(), CATEGORY);

		return refundTransaction;
	}

	private String addItemToTransaction(QisTransactionItem txnItemReq, QisTransaction qisTransaction,
			QisUserDetails authUser) throws Exception {
		txnItemReq.setTransactionid(qisTransaction.getId());
		txnItemReq.setCreatedBy(authUser.getId());
		txnItemReq.setUpdatedBy(authUser.getId());

		qisTransactionItemRepository.save(txnItemReq);
		Set<QisTransactionLaboratoryRequest> labRequests = new HashSet<>();
		appTransactionUtility.getLaboratoryRequestList(labRequests, txnItemReq);
		saveQisTransactionLaboratoryRequest(labRequests, qisTransaction.getId(), authUser.getId());

		QisTransactionItemResponse itmRes = new QisTransactionItemResponse();
		BeanUtils.copyProperties(txnItemReq, itmRes);
		return " ADDED[" + itmRes.toString() + "]";
	}

	private String addPaymentToTransaction(QisTransactionPayment txnPayReq, QisTransaction qisTransaction,
			QisUserDetails authUser) throws Exception {
		txnPayReq.setTransactionid(qisTransaction.getId());
		txnPayReq.setCreatedBy(authUser.getId());
		txnPayReq.setUpdatedBy(authUser.getId());
		qisTransactionPaymentRepository.save(txnPayReq);

		QisTransactionPaymentResponse payReq = getQisTransactionPaymentResponse(txnPayReq);
		return " ADDED[" + payReq.toString() + "]";
	}

	// DELETE TRANSACTION
	@DeleteMapping("transaction/{transactionId}")
	public String deleteTransaction(@PathVariable String transactionId,
			@AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.warn(authUser.getId() + "-Delete Transaction:" + transactionId);
		return "not implemented";
	}

	public Set<QisTransactionLaboratoryRequest> saveQisTransactionItem(Set<QisTransactionItem> transactionItems,
			Long transactionId, Long userId) throws Exception {
		Set<QisTransactionLaboratoryRequest> labRequests = new HashSet<>();
		if (transactionItems != null) {
			transactionItems.forEach(item -> {
				item.setTransactionid(transactionId);
				item.setCreatedBy(userId);
				item.setUpdatedBy(userId);
				qisTransactionItemRepository.save(item);
				appTransactionUtility.getLaboratoryRequestList(labRequests, item);
			});
		}
		return labRequests;
	}

	public void saveQisTransactionPayment(Set<QisTransactionPayment> transactionPayments, Long transactionId,
			Long userId) throws Exception {
		if (transactionPayments != null) {
			transactionPayments.forEach(payment -> {
				payment.setTransactionid(transactionId);
				payment.setCreatedBy(userId);
				payment.setUpdatedBy(userId);
				qisTransactionPaymentRepository.save(payment);
			});
		}
	}

	public void saveQisTransactionLaboratoryRequest(Set<QisTransactionLaboratoryRequest> transactionLabRequests,
			Long transactionId, Long userId) throws Exception {
		if (transactionLabRequests != null) {
			transactionLabRequests.forEach(labRequest -> {
				labRequest.setTransactionid(transactionId);
				labRequest.setCreatedBy(userId);
				labRequest.setUpdatedBy(userId);
				qisTransactionLaboratoryRequestRepository.save(labRequest);
			});
		}
	}

	private void saveQisTransactionDiscount(QisTransactionDiscount transactionDiscount, Long transactionId, Long userId)
			throws Exception {
		if (transactionDiscount != null) {
			transactionDiscount.setTransactionid(transactionId);
			transactionDiscount.setCreatedBy(userId);
			transactionDiscount.setUpdatedBy(userId);
			qisTransactionDiscountRepository.save(transactionDiscount);
		}
	}

	private Set<QisTransactionItem> getTransactionItems(QisTransaction qisTransaction,
			Set<QisTransactionItemRequest> requestItems) throws Exception {
		Set<QisTransactionItem> items = new HashSet<>();
		if (requestItems != null) {
			requestItems.forEach(item -> {
				QisTransactionItem qisTransactionItem = appTransactionUtility.getQisTransactionItem(item);
				if (qisTransactionItem != null) {
					appTransactionUtility.computeTransactionItemAmount(item, qisTransaction, qisTransactionItem);
					items.add(qisTransactionItem);
				} else {
					throw new RuntimeException("Item Not Found.");
				}
			});
		}
		return items;
	}

	private Set<QisTransactionPayment> getTransactionPayments(Set<QisTransactionPaymentRequest> requestPayments)
			throws Exception {
		Set<QisTransactionPayment> payments = new HashSet<>();
		if (requestPayments != null) {
			requestPayments.forEach(payment -> {
				QisTransactionPayment qisTransactionPayment = appTransactionUtility.getQisTransactionPayment(payment);
				if (qisTransactionPayment != null) {
					payments.add(qisTransactionPayment);
				} else {
					throw new IllegalArgumentException("Invalid Payment Type: " + payment.getPaymentType());
				}
			});
		}
		return payments;
	}

	private Set<QisTransactionItemResponse> getTransactionItemsResponse(Set<QisTransactionItem> txnItems) {
		Set<QisTransactionItemResponse> itemsResponse = new HashSet<>();
		if (txnItems != null) {
			txnItems.forEach(txnItem -> {
				QisTransactionItemResponse itemRes = new QisTransactionItemResponse();
				BeanUtils.copyProperties(txnItem, itemRes);
				itemsResponse.add(itemRes);
			});
		}
		return itemsResponse;
	}

	private Set<QisTransactionPaymentResponse> getTransactionPaymentsResponse(Set<QisTransactionPayment> txnPayments) {
		Set<QisTransactionPaymentResponse> paymentsResponse = new HashSet<>();
		if (txnPayments != null) {
			txnPayments.forEach(txnPayment -> {
				QisTransactionPaymentResponse paymentRes = new QisTransactionPaymentResponse();
				BeanUtils.copyProperties(txnPayment, paymentRes);
				if (txnPayment.getBiller() != null) {
					paymentRes.setBillerId(txnPayment.getBiller().getCorporateid());
					paymentRes.setBillerName(txnPayment.getBiller().getCompanyName());
				}
				paymentsResponse.add(paymentRes);
			});
		}
		return paymentsResponse;
	}

	private QisTransactionDiscountResponse getTransactionDiscountRequest(QisTransactionDiscount txnDiscount) {
		QisTransactionDiscountResponse discountRes = new QisTransactionDiscountResponse();
		BeanUtils.copyProperties(txnDiscount, discountRes);
		discountRes.setAuthorizeId(txnDiscount.getAuthorizedBy().getUserid());
		discountRes.setAuthorizeName(appUtility.getUsername(txnDiscount.getAuthorizedBy()));

		return discountRes;
	}

	private QisTransactionPaymentResponse getQisTransactionPaymentResponse(
			QisTransactionPayment qisTransactionPayment) {
		QisTransactionPaymentResponse qisTransactionPaymentResponse = new QisTransactionPaymentResponse();
		BeanUtils.copyProperties(qisTransactionPayment, qisTransactionPaymentResponse);
		if (qisTransactionPayment.getBiller() != null) {
			qisTransactionPaymentResponse.setBillerId(qisTransactionPayment.getBiller().getCorporateid());
			qisTransactionPaymentResponse.setBillerName(qisTransactionPayment.getBiller().getCompanyName());
		}
		return qisTransactionPaymentResponse;
	}

}
