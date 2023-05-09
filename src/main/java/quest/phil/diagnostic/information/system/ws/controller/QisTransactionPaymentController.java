package quest.phil.diagnostic.information.system.ws.controller;

import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionPayment;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionPaymentRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionPaymentResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionPaymentRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}")
public class QisTransactionPaymentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionPaymentController.class);
	private final String CATEGORY = "TRANSACTION_PAYMENT";

	@Autowired
	private QisTransactionRepository qisTransactionRepository;

	@Autowired
	private QisTransactionPaymentRepository qisTransactionPaymentRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	AppTransactionUtility appTransactionUtility;

	@Autowired
	private QisLogService qisLogService;

	// LIST ALL TRANSCTION PAYMENTS
	@GetMapping("payments")
	public List<QisTransactionPayment> getAllTransactionPayments(@PathVariable String transactionId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction All Payments:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		return qisTransactionPaymentRepository.getTransactionPaymentsByTransactionid(qisTransaction.getId());
	}

	// READ TRANSCTION PAYMENT
	@GetMapping("payment/{paymentId}")
	public QisTransactionPayment getTransactionPayment(@PathVariable String transactionId, @PathVariable Long paymentId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction:" + transactionId + " Payment:" + paymentId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionPayment qisTransactionPayment = qisTransactionPaymentRepository
				.getTransactionPaymentById(qisTransaction.getId(), paymentId);
		if (qisTransactionPayment == null) {
			throw new RuntimeException("Transaction Payment not found.");
		}

		QisTransactionPaymentResponse qisTransactionPaymentResponse = getQisTransactionPaymentResponse(
				qisTransactionPayment);
		qisLogService.info(authUser.getId(), QisTransactionPaymentController.class.getSimpleName(), "VIEW",
				qisTransactionPaymentResponse.toString(), qisTransactionPayment.getId(), CATEGORY);

		return qisTransactionPayment;
	}

	// ADD TRANSACTION PAYMENT
	@PostMapping("payment")
	public QisTransactionPayment getAddTransactionPayment(@PathVariable String transactionId,
			@Valid @RequestBody QisTransactionPaymentRequest txnPaymentRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Add Transaction Payment:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionPayment qisTransactionPayment = appTransactionUtility.getQisTransactionPayment(txnPaymentRequest);
		if (qisTransactionPayment == null) {
			throw new IllegalArgumentException("Invalid Payment Type: " + txnPaymentRequest.getPaymentType());
		}

		qisTransactionPayment.setTransactionid(qisTransaction.getId());
		qisTransactionPayment.setCreatedBy(authUser.getId());
		qisTransactionPayment.setUpdatedBy(authUser.getId());

		qisTransaction.getTransactionPayments().add(qisTransactionPayment);
		appTransactionUtility.computeTotalTransaction(qisTransaction, authUser.getId());

		qisTransactionPaymentRepository.save(qisTransactionPayment);
		qisTransactionRepository.save(qisTransaction);

		QisTransactionPaymentResponse qisTransactionPaymentResponse = getQisTransactionPaymentResponse(
				qisTransactionPayment);
		qisLogService.info(authUser.getId(), QisTransactionPaymentController.class.getSimpleName(), "CREATE",
				qisTransactionPaymentResponse.toString(), qisTransactionPayment.getId(), CATEGORY);

		QisTransactionResponse qisTransactionResponse = appTransactionUtility.getQisTransactionResponse(qisTransaction);
		LOGGER.info(authUser.getId() + ":TRANSACTION[" + qisTransaction.getId() + "]=>"
				+ qisTransactionResponse.toString());

		return qisTransactionPayment;
	}

	// DELETE TRANSACTION PAYMENT
	@DeleteMapping("payment/{paymentId}")
	public String getDeleteTransactionPayment(@PathVariable String transactionId, @PathVariable Long paymentId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Delete Transaction Payment:" + transactionId + " Payment:" + paymentId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionPayment qisTransactionPayment = qisTransactionPaymentRepository
				.getTransactionPaymentById(qisTransaction.getId(), paymentId);
		if (qisTransactionPayment == null) {
			throw new RuntimeException("Transaction Payment not found.");
		}

		qisTransaction.getTransactionPayments().removeIf(entry -> entry.getId() == qisTransactionPayment.getId());
		appTransactionUtility.computeTotalTransaction(qisTransaction, authUser.getId());

		QisTransactionPaymentResponse qisTransactionPaymentResponse = getQisTransactionPaymentResponse(
				qisTransactionPayment);
		qisLogService.warn(authUser.getId(), QisTransactionPaymentController.class.getSimpleName(), "DELETE",
				qisTransactionPaymentResponse.toString(), qisTransactionPayment.getId(), CATEGORY);

		qisTransactionPaymentRepository.delete(qisTransactionPayment);
		qisTransactionRepository.save(qisTransaction);

		QisTransactionResponse qisTransactionResponse = appTransactionUtility.getQisTransactionResponse(qisTransaction);
		LOGGER.warn(authUser.getId() + ":TRANSACTION[" + qisTransaction.getId() + "]PAYMENT[" + paymentId + "]=>"
				+ qisTransactionResponse.toString());

		return "Payment Deleted:" + qisTransactionPayment.getPaymentType() + "=" + qisTransactionPayment.getAmount();
	}

	// UPDATE TRANSACTION PAYMENT
	@PutMapping("payment/{paymentId}")
	public QisTransactionPayment getUpdateTransactionPayment(@PathVariable String transactionId,
			@PathVariable Long paymentId, @Valid @RequestBody QisTransactionPaymentRequest txnPaymentRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Update Transaction Payment:" + transactionId + " Payment:" + paymentId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionPayment qisTransactionPayment = qisTransactionPaymentRepository
				.getTransactionPaymentById(qisTransaction.getId(), paymentId);
		if (qisTransactionPayment == null) {
			throw new RuntimeException("Transaction Payment not found.");
		}

		QisTransactionPayment reqQisTransactionPayment = appTransactionUtility
				.getQisTransactionPayment(txnPaymentRequest);
		if (reqQisTransactionPayment == null) {
			throw new IllegalArgumentException("Invalid Payment Type: " + txnPaymentRequest.getPaymentType());
		}

		boolean isUpdate = false;
		String updateData = "";

		if (!qisTransactionPayment.getPaymentType().equals(reqQisTransactionPayment.getPaymentType())) {
			String fromPayment = appTransactionUtility.paymentDetails(qisTransactionPayment);
			String toPayment = appTransactionUtility.paymentDetails(reqQisTransactionPayment);
			updateData = appUtility.formatUpdateData(updateData, "Payment Type", fromPayment, toPayment);
			isUpdate = true;
			qisTransactionPayment.setPaymentType(reqQisTransactionPayment.getPaymentType());
			qisTransactionPayment.setPaymentMode(reqQisTransactionPayment.getPaymentMode());
			qisTransactionPayment.setPaymentBank(reqQisTransactionPayment.getPaymentBank());
			qisTransactionPayment.setCardChequeNumber(reqQisTransactionPayment.getCardChequeNumber());
			qisTransactionPayment.setCcHolderName(reqQisTransactionPayment.getCcHolderName());
			qisTransactionPayment.setCcType(reqQisTransactionPayment.getCcType());
			qisTransactionPayment.setHmoAccountNumber(reqQisTransactionPayment.getHmoAccountNumber());
			qisTransactionPayment.setHmoLOE(reqQisTransactionPayment.getHmoLOE());
			qisTransactionPayment.setHmoApprovalCode(reqQisTransactionPayment.getHmoApprovalCode());
			qisTransactionPayment.setReferenceNumber(reqQisTransactionPayment.getReferenceNumber());
		}

		if (qisTransactionPayment.getAmount() != reqQisTransactionPayment.getAmount()) {
			updateData = appUtility.formatUpdateData(updateData, "Amount",
					String.valueOf(qisTransactionPayment.getAmount()),
					String.valueOf(reqQisTransactionPayment.getAmount()));
			isUpdate = true;
			qisTransactionPayment.setAmount(reqQisTransactionPayment.getAmount());
		}

		if (!qisTransactionPayment.getCurrency().equals(reqQisTransactionPayment.getCurrency())) {
			updateData = appUtility.formatUpdateData(updateData, "Currency", qisTransactionPayment.getCurrency(),
					reqQisTransactionPayment.getCurrency());
			isUpdate = true;
			qisTransactionPayment.setCurrency(reqQisTransactionPayment.getCurrency());
		}

		if (qisTransactionPayment.getBillerId() != reqQisTransactionPayment.getBillerId()) {
			String fromBiller = null;
			String toBiller = null;

			if (qisTransactionPayment.getBillerId() != null) {
				fromBiller = qisTransactionPayment.getBiller().getCompanyName();
			}

			if (reqQisTransactionPayment.getBillerId() != null) {
				toBiller = reqQisTransactionPayment.getBiller().getCompanyName();
			}

			updateData = appUtility.formatUpdateData(updateData, "Biller", fromBiller, toBiller);
			isUpdate = true;
			qisTransactionPayment.setBillerId(reqQisTransactionPayment.getBillerId());
			qisTransactionPayment.setBiller(reqQisTransactionPayment.getBiller());
		}

		if (isUpdate) {
			qisTransactionPayment.setUpdatedBy(authUser.getId());
			qisTransactionPayment.setUpdatedAt(Calendar.getInstance());

			qisLogService.info(authUser.getId(), QisTransactionPaymentController.class.getSimpleName(), "UPDATE",
					updateData, qisTransactionPayment.getId(), CATEGORY);

			qisTransaction.getTransactionPayments().removeIf(entry -> entry.getId() == qisTransactionPayment.getId());
			qisTransaction.getTransactionPayments().add(qisTransactionPayment);
			appTransactionUtility.computeTotalTransaction(qisTransaction, authUser.getId());

			qisTransactionPaymentRepository.save(qisTransactionPayment);
			qisTransactionRepository.save(qisTransaction);

			QisTransactionResponse qisTransactionResponse = appTransactionUtility
					.getQisTransactionResponse(qisTransaction);
			LOGGER.info(authUser.getId() + ":TRANSACTION[" + qisTransaction.getId() + "]PAYMENT[" + paymentId + "]=>"
					+ qisTransactionResponse.toString());

		}

		return qisTransactionPayment;
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
