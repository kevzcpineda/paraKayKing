package quest.phil.diagnostic.information.system.ws.controller;

import java.util.Calendar;

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

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionDiscount;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionDiscountRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionDiscountResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionDiscountRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/")
public class QisTransactionDiscountController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionDiscountController.class);
	private final String CATEGORY = "TRANSACTION-DISCOUNT";

	@Autowired
	private QisTransactionRepository qisTransactionRepository;

	@Autowired
	private QisTransactionDiscountRepository qisTransactionDiscountRepository;

	@Autowired
	private QisUserRepository qisUserRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	// ADD TRANSACTION SPECIAL DISCOUNT
	@PostMapping("special_discount")
	public QisTransaction addTransactionSpecialDiscount(@PathVariable String transactionId,
			@Valid @RequestBody QisTransactionDiscountRequest txnDiscReq,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Add Transaction Discount:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		if (qisTransaction.getSpecialDiscountAmount() > 0) {
			throw new RuntimeException("Transaction already have special discount.");
		}

		QisUser qisAuthorize = qisUserRepository.findByUserid(txnDiscReq.getAuthorizeId());
		if (qisAuthorize == null) {
			throw new RuntimeException("Invalid authorization id[" + txnDiscReq.getAuthorizeId() + "]", new Throwable(
					"transactionDiscount.authorizeId: Invalid authorization id[" + txnDiscReq.getAuthorizeId() + "]"));
		}

		Double discountAmount = appUtility.parseDoubleAmount(txnDiscReq.getDiscountAmount());
		if (discountAmount == null) {
			throw new RuntimeException("Invalid discount amount.",
					new Throwable("transactionDiscount.discountAmount: Invalid discount amount."));
		}

		QisTransactionDiscount qisTransactionDiscount = new QisTransactionDiscount();
		qisTransactionDiscount.setTransactionid(qisTransaction.getId());
		qisTransactionDiscount.setAuthorizationId(qisAuthorize.getId());
		qisTransactionDiscount.setAuthorizedBy(qisAuthorize);
		qisTransactionDiscount.setDiscount(discountAmount);
		qisTransactionDiscount.setDescription(txnDiscReq.getDescription());
		qisTransaction.setSpecialDiscountAmount(qisTransactionDiscount.getDiscount());

		try {
			qisTransactionDiscountRepository.save(qisTransactionDiscount);

			qisTransaction.computeItems();
			qisTransaction.setUpdatedBy(authUser.getId());
			qisTransaction.setUpdatedAt(Calendar.getInstance());
			qisTransactionRepository.save(qisTransaction);
		} catch (Exception e) {
			throw new RuntimeException("Error saving transaction special discount.", e);
		}

		QisTransactionDiscountResponse discountResponse = getTransactionDiscountRequest(qisTransactionDiscount);

		qisLogService.info(authUser.getId(), QisTransactionDiscountController.class.getSimpleName(), "ADDED",
				discountResponse.toString(), qisTransaction.getId(), CATEGORY);

		qisTransaction.setSpecialDiscount(qisTransactionDiscount);
		return qisTransaction;
	}

	// READ TRANSACTION SPECIAL DISCOUNT
	@GetMapping("special_discount")
	public QisTransactionDiscount getTransactionSpecialDiscount(@PathVariable String transactionId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Discount:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		if (qisTransaction.getSpecialDiscountAmount() <= 0) {
			throw new RuntimeException("Transaction do not have special discount.");
		}

		QisTransactionDiscount qisTransactionDiscount = qisTransactionDiscountRepository
				.getTransactionDiscountByTransactionId(qisTransaction.getId());
		if (qisTransactionDiscount == null) {
			throw new RuntimeException("Transaction do not have special discount.");
		}

		return qisTransactionDiscount;
	}

	// DELETE TRANSACTION SPECIAL DISCOUNT
	@DeleteMapping("special_discount")
	public QisTransaction deleteTransactionSpecialDiscount(@PathVariable String transactionId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Delete Transaction Discount:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		if (qisTransaction.getSpecialDiscountAmount() <= 0) {
			throw new RuntimeException("Transaction do not have special discount.");
		}

		QisTransactionDiscount qisTransactionDiscount = qisTransactionDiscountRepository
				.getTransactionDiscountByTransactionId(qisTransaction.getId());
		if (qisTransactionDiscount != null) {
			qisTransaction.setSpecialDiscountAmount(0d);
			qisTransaction.setSpecialDiscount(null);

			qisTransaction.computeItems();
			qisTransaction.setUpdatedBy(authUser.getId());
			qisTransaction.setUpdatedAt(Calendar.getInstance());
			qisTransactionRepository.save(qisTransaction);
			qisTransactionDiscountRepository.delete(qisTransactionDiscount);

			QisTransactionDiscountResponse discountResponse = getTransactionDiscountRequest(qisTransactionDiscount);
			qisLogService.error(authUser.getId(), QisTransactionDiscountController.class.getSimpleName(), "DELETED",
					discountResponse.toString(), qisTransaction.getId(), CATEGORY);
		}

		return qisTransaction;
	}

	// UPDATE TRANSACTION SPECIAL DISCOUNT
	@PutMapping("special_discount")
	public QisTransaction updateTransactionSpecialDiscount(@PathVariable String transactionId,
			@Valid @RequestBody QisTransactionDiscountRequest txnDiscReq,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Update Transaction Discount:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		if (qisTransaction.getSpecialDiscountAmount() <= 0) {
			throw new RuntimeException("Transaction do not have special discount.");
		}

		QisUser requestAuthorize = qisUserRepository.findByUserid(txnDiscReq.getAuthorizeId());
		if (requestAuthorize == null) {
			throw new RuntimeException("Invalid authorization id[" + txnDiscReq.getAuthorizeId() + "]", new Throwable(
					"transactionDiscount.authorizeId: Invalid authorization id[" + txnDiscReq.getAuthorizeId() + "]"));
		}

		Double discountAmount = appUtility.parseDoubleAmount(txnDiscReq.getDiscountAmount());
		if (discountAmount == null) {
			throw new RuntimeException("Invalid discount amount.",
					new Throwable("transactionDiscount.discountAmount: Invalid discount amount."));
		}

		QisTransactionDiscount qisTransactionDiscount = qisTransactionDiscountRepository
				.getTransactionDiscountByTransactionId(qisTransaction.getId());
		if (qisTransactionDiscount == null) {
			throw new RuntimeException("Transaction Discount Not Found.");
		}

		boolean isUpdate = false;
		boolean updateComputation = false;
		String updateData = "";

		if (requestAuthorize.getId() != qisTransactionDiscount.getAuthorizationId()) {
			updateData = appUtility.formatUpdateData(updateData, "Authorize",
					qisTransactionDiscount.getAuthorizedBy().getUserid() + ":"
							+ qisTransactionDiscount.getAuthorizedBy().getUsername(),
					requestAuthorize.getUserid() + ":" + requestAuthorize.getUsername());
			isUpdate = true;
			qisTransactionDiscount.setAuthorizationId(requestAuthorize.getId());
			qisTransactionDiscount.setAuthorizedBy(requestAuthorize);
		}

		if (!discountAmount.equals(qisTransactionDiscount.getDiscount())) {
			updateData = appUtility.formatUpdateData(updateData, "Discount Amount",
					String.valueOf(qisTransactionDiscount.getDiscount()), String.valueOf(discountAmount));
			isUpdate = true;
			qisTransactionDiscount.setDiscount(discountAmount);
			qisTransaction.setSpecialDiscountAmount(discountAmount);
			updateComputation = true;
		}

		if (!txnDiscReq.getDescription().equals(qisTransactionDiscount.getDescription())) {
			updateData = appUtility.formatUpdateData(updateData, "Description", qisTransactionDiscount.getDescription(),
					txnDiscReq.getDescription());
			isUpdate = true;
			qisTransactionDiscount.setDescription(txnDiscReq.getDescription());
		}

		if (isUpdate) {
			qisTransactionDiscount.setUpdatedBy(authUser.getId());
			qisTransactionDiscount.setUpdatedAt(Calendar.getInstance());
			qisTransactionDiscountRepository.save(qisTransactionDiscount);

			if (updateComputation) {
				qisTransaction.computeItems();
				qisTransaction.setUpdatedBy(authUser.getId());
				qisTransaction.setUpdatedAt(Calendar.getInstance());
				qisTransactionRepository.save(qisTransaction);
			}

			qisLogService.info(authUser.getId(), QisTransactionDiscountController.class.getSimpleName(), "UPDATE",
					updateData, qisTransaction.getId(), CATEGORY);
		}

		qisTransaction.setSpecialDiscount(qisTransactionDiscount);
		return qisTransaction;
	}

	private QisTransactionDiscountResponse getTransactionDiscountRequest(QisTransactionDiscount txnDiscount) {
		QisTransactionDiscountResponse discountRes = new QisTransactionDiscountResponse();
		BeanUtils.copyProperties(txnDiscount, discountRes);
		discountRes.setAuthorizeId(txnDiscount.getAuthorizedBy().getUserid());
		discountRes.setAuthorizeName(appUtility.getUsername(txnDiscount.getAuthorizedBy()));

		return discountRes;
	}

}
