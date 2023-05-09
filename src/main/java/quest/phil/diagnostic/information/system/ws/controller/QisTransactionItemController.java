package quest.phil.diagnostic.information.system.ws.controller;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionItemRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionItemResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryRequestRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}")
public class QisTransactionItemController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionItemController.class);
	private final String CATEGORY = "TRANSACTION_ITEM";

	@Autowired
	private QisTransactionRepository qisTransactionRepository;

	@Autowired
	private QisTransactionItemRepository qisTransactionItemRepository;

	@Autowired
	private QisTransactionLaboratoryRequestRepository qisTransactionLaboratoryRequestRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	AppTransactionUtility appTransactionUtility;

	@Autowired
	private QisLogService qisLogService;

	// LIST ALL TRANSCTION ITEMS
	@GetMapping("items")
	public List<QisTransactionItem> getAllTransactionItems(@PathVariable String transactionId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction All Items:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		return qisTransactionItemRepository.getTransactionItemsByTransactionid(qisTransaction.getId());
	}

	// READ TRANSCTION ITEM
	@GetMapping("item/{itemId}")
	public QisTransactionItem getTransactionItem(@PathVariable String transactionId, @PathVariable Long itemId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction:" + transactionId + " Item:" + itemId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionItem qisTransactionItem = qisTransactionItemRepository
				.getTransactionItemById(qisTransaction.getId(), itemId);
		if (qisTransactionItem == null) {
			throw new RuntimeException("Transaction Item not found.");
		}

		QisTransactionItemResponse qisTransactionItemResponse = new QisTransactionItemResponse();
		BeanUtils.copyProperties(qisTransactionItem, qisTransactionItemResponse);

		qisLogService.info(authUser.getId(), QisTransactionItemController.class.getSimpleName(), "VIEW",
				qisTransactionItemResponse.toString(), qisTransactionItem.getId(), CATEGORY);

		return qisTransactionItem;
	}

	// ADD TRANSACTION ITEM
	@PostMapping("item")
	public QisTransactionItem getAddTransactionItem(@PathVariable String transactionId,
			@Valid @RequestBody QisTransactionItemRequest txnItemRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Add Transaction Item:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionItem qisTransactionItem = appTransactionUtility.getQisTransactionItem(txnItemRequest);
		if (qisTransactionItem != null) {
			appTransactionUtility.computeTransactionItemAmount(txnItemRequest, qisTransaction, qisTransactionItem);
		} else {
			throw new RuntimeException("Item Not Found.");
		}

		if (qisTransaction.getSpecialDiscountAmount() > 0 && txnItemRequest.getDiscountRate() > 0) {
			throw new RuntimeException("Item discount is not allowed with special discount transaction.");
		}

		qisTransactionItem.setTransactionid(qisTransaction.getId());
		qisTransactionItem.setCreatedBy(authUser.getId());
		qisTransactionItem.setUpdatedBy(authUser.getId());

		qisTransaction.getTransactionItems().add(qisTransactionItem);
		appTransactionUtility.computeTotalTransaction(qisTransaction, authUser.getId());

		qisTransactionItemRepository.save(qisTransactionItem);
		qisTransactionRepository.save(qisTransaction);

		QisTransactionItemResponse qisTransactionItemResponse = new QisTransactionItemResponse();
		BeanUtils.copyProperties(qisTransactionItem, qisTransactionItemResponse);

		qisLogService.info(authUser.getId(), QisTransactionItemController.class.getSimpleName(), "CREATE",
				qisTransactionItemResponse.toString(), qisTransactionItem.getId(), CATEGORY);

		QisTransactionResponse qisTransactionResponse = appTransactionUtility.getQisTransactionResponse(qisTransaction);
		LOGGER.info(authUser.getId() + ":TRANSACTION[" + qisTransaction.getId() + "]=>"
				+ qisTransactionResponse.toString());

		Set<QisTransactionLaboratoryRequest> labRequests = new HashSet<>();
		appTransactionUtility.getLaboratoryRequestList(labRequests, qisTransactionItem);
		saveQisTransactionLaboratoryRequest(labRequests, qisTransaction.getId(), authUser.getId());

		return qisTransactionItem;
	}

	// DELETE TRANSACTION ITEM
	@DeleteMapping("item/{itemId}")
	public String getDeleteTransactionItem(@PathVariable String transactionId, @PathVariable Long itemId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Delete Transaction:" + transactionId + " Item:" + itemId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionItem qisTransactionItem = qisTransactionItemRepository
				.getTransactionItemById(qisTransaction.getId(), itemId);
		if (qisTransactionItem == null) {
			throw new RuntimeException("Transaction Item not found.");
		}

		qisTransaction.getTransactionItems().removeIf(entry -> entry.getId() == qisTransactionItem.getId());
		appTransactionUtility.computeTotalTransaction(qisTransaction, authUser.getId());

		QisTransactionItemResponse qisTransactionItemResponse = new QisTransactionItemResponse();
		BeanUtils.copyProperties(qisTransactionItem, qisTransactionItemResponse);

		qisLogService.warn(authUser.getId(), QisTransactionItemController.class.getSimpleName(), "DELETE",
				qisTransactionItemResponse.toString(), qisTransactionItem.getId(), CATEGORY);

		qisTransactionItemRepository.delete(qisTransactionItem);
		qisTransactionRepository.save(qisTransaction);

		QisTransactionResponse qisTransactionResponse = appTransactionUtility.getQisTransactionResponse(qisTransaction);
		LOGGER.warn(authUser.getId() + ":TRANSACTION[" + qisTransaction.getId() + "]ITEM[" + itemId + "]=>"
				+ qisTransactionResponse.toString());

		List<QisTransactionLaboratoryRequest> labRequests = qisTransactionLaboratoryRequestRepository
				.getTransactionLaboratoryRequestByTransactionidItemid(qisTransaction.getId(),
						qisTransactionItem.getId());
		if (!labRequests.isEmpty()) {
			qisTransactionLaboratoryRequestRepository.deleteAll(labRequests);
		}

		return "Item Deleted:" + qisTransactionItem.getItemReference();
	}

	// UPDATE TRANSACTION ITEM
	@PutMapping("item/{itemId}")
	public QisTransactionItem getUpdateTransactionItem(@PathVariable String transactionId, @PathVariable Long itemId,
			@Valid @RequestBody QisTransactionItemRequest txnItemRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Update Transaction:" + transactionId + " Item:" + itemId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionItem qisTransactionItem = qisTransactionItemRepository
				.getTransactionItemById(qisTransaction.getId(), itemId);
		if (qisTransactionItem == null) {
			throw new RuntimeException("Transaction Item not found.");
		}

		QisTransactionItem reqQisTransactionItem = appTransactionUtility.getQisTransactionItem(txnItemRequest);
		if (reqQisTransactionItem != null) {
			appTransactionUtility.computeTransactionItemAmount(txnItemRequest, qisTransaction, reqQisTransactionItem);
		} else {
			throw new RuntimeException("Item Not Found.");
		}

		boolean isUpdate = false;
		String updateData = "";

//		if (!qisTransactionItem.getItemReference().equals(reqQisTransactionItem.getItemReference())) {
//			String fromItem = qisTransactionItem.getItemType() + ":" + qisTransactionItem.getItemReference() + ":"
//					+ qisTransactionItem.getItemName();
//			String toItem = reqQisTransactionItem.getItemType() + ":" + reqQisTransactionItem.getItemReference() + ":"
//					+ reqQisTransactionItem.getItemName();
//			updateData = appUtility.formatUpdateData(updateData, "Item", fromItem, toItem);
//			isUpdate = true;
//			qisTransactionItem.setItemType(reqQisTransactionItem.getItemType());
//			qisTransactionItem.setItemName(reqQisTransactionItem.getItemName());
//			qisTransactionItem.setItemReference(reqQisTransactionItem.getItemReference());
//		}

		if (qisTransactionItem.getQuantity() != reqQisTransactionItem.getQuantity()) {
			updateData = appUtility.formatUpdateData(updateData, "Quantity",
					String.valueOf(qisTransactionItem.getQuantity()),
					String.valueOf(reqQisTransactionItem.getQuantity()));
			isUpdate = true;
			qisTransactionItem.setQuantity(reqQisTransactionItem.getQuantity());
		}

		if (qisTransactionItem.getDiscountRate() != reqQisTransactionItem.getDiscountRate()) {
			if (qisTransaction.getSpecialDiscountAmount() > 0 && txnItemRequest.getDiscountRate() > 0) {
				throw new RuntimeException("Item discount is not allowed with special discount transaction.");
			}

			updateData = appUtility.formatUpdateData(updateData, "Discount Rate",
					String.valueOf(qisTransactionItem.getDiscountRate()),
					String.valueOf(reqQisTransactionItem.getDiscountRate()));
			isUpdate = true;
			qisTransactionItem.setDiscountRate(reqQisTransactionItem.getDiscountRate());
		}

		if (isUpdate) {
			qisTransactionItem.computeItemAmount();
			qisTransactionItem.setUpdatedBy(authUser.getId());
			qisTransactionItem.setUpdatedAt(Calendar.getInstance());

			qisLogService.info(authUser.getId(), QisTransactionItemController.class.getSimpleName(), "UPDATE",
					updateData, qisTransactionItem.getId(), CATEGORY);

			qisTransaction.getTransactionItems().removeIf(entry -> entry.getId() == qisTransactionItem.getId());
			qisTransaction.getTransactionItems().add(qisTransactionItem);
			appTransactionUtility.computeTotalTransaction(qisTransaction, authUser.getId());

			qisTransactionItemRepository.save(qisTransactionItem);
			qisTransactionRepository.save(qisTransaction);

			QisTransactionResponse qisTransactionResponse = appTransactionUtility
					.getQisTransactionResponse(qisTransaction);
			LOGGER.info(authUser.getId() + ":TRANSACTION[" + qisTransaction.getId() + "]ITEM[" + itemId + "]=>"
					+ qisTransactionResponse.toString());

		}

		return qisTransactionItem;
	}

	private void saveQisTransactionLaboratoryRequest(Set<QisTransactionLaboratoryRequest> transactionLabRequests,
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

}
