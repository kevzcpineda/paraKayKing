package quest.phil.diagnostic.information.system.ws.common;

import java.util.Calendar;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quest.phil.diagnostic.information.system.ws.model.QisLaboratoryName;
import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPackage;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionPayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionItemRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionPaymentRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisItemRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisPackageRepository;

@Component
public class AppTransactionUtility {
	public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisItemRepository qisItemRepository;

	@Autowired
	private QisPackageRepository qisPackageRepository;

	public void computeTotalTransaction(QisTransaction qisTransaction, Long userId) {
		qisTransaction.computeItems();
		qisTransaction.computePayments();
		qisTransaction.computeChangeAmount();
		qisTransaction.setUpdatedBy(userId);
		qisTransaction.setUpdatedAt(Calendar.getInstance());
	}

	public QisTransactionResponse getQisTransactionResponse(QisTransaction qisTransaction) {
		QisPatient qisPatient = qisTransaction.getPatient();
		QisUser qisCashier = qisTransaction.getCashier();
//		QisCorporate qisCorporate = qisTransaction.getCorporate();

		QisTransactionResponse qisTransactionResponse = new QisTransactionResponse();
		BeanUtils.copyProperties(qisTransaction, qisTransactionResponse);

		qisTransactionResponse.setTransactionDate(appUtility
				.calendarToFormatedDate(qisTransaction.getTransactionDate(), AppTransactionUtility.DATEFORMAT));
		qisTransactionResponse.setPatientId(qisPatient.getPatientid());
		qisTransactionResponse.setPatientName(appUtility.getPatientFullname(qisPatient));
		qisTransactionResponse.setCashierId(qisCashier.getUserid());
		qisTransactionResponse.setCashierName(appUtility.getUsername(qisCashier));
//		if (qisCorporate != null) {
//			qisTransactionResponse.setCorporateId(qisCorporate.getCorporateid());
//			qisTransactionResponse.setCorporateName(qisCorporate.getCompanyName());
//		}

		return qisTransactionResponse;
	}

	public QisTransactionItem getQisTransactionItem(QisTransactionItemRequest item) {
		QisTransactionItem qisTransactionItem = null;
		if ("ITM".equals(item.getItemType())) {
			QisItem qisItem = qisItemRepository.findByItemid(item.getItemid());
			if (qisItem != null && qisItem.isActive()) {
				qisTransactionItem = new QisTransactionItem(qisItem.getId(), item.getItemType(), item.getQuantity(),
						qisItem.getItemPrice(), qisItem.getItemid(), qisItem.isDiscountable(), qisItem.isTaxable());
				if (item.getId() != null) {
					qisTransactionItem.setId(item.getId());
				}

				qisTransactionItem.setQisItemId(qisItem.getId());
				qisTransactionItem.setQisItem(qisItem);
			} else {
				throw new RuntimeException("Invalid item [" + item.getItemid() + "]");
			}
		} else if ("PCK".equals(item.getItemType())) {
			QisPackage qisPackage = qisPackageRepository.findByPackageid(item.getItemid());
			if (qisPackage != null && qisPackage.isActive()) {
				qisTransactionItem = new QisTransactionItem(qisPackage.getId(), item.getItemType(), item.getQuantity(),
						qisPackage.getPackagePrice(), qisPackage.getPackageid(), qisPackage.isDiscountable(),
						qisPackage.isTaxable());
				if (item.getId() != null) {
					qisTransactionItem.setId(item.getId());
				}
				qisTransactionItem.setQisPackageId(qisPackage.getId());
				qisTransactionItem.setQisPackage(qisPackage);
			} else {
				throw new RuntimeException("Invalid package [" + item.getItemid() + "]");
			}
		}

		return qisTransactionItem;
	}

	public QisTransactionItem computeTransactionItemAmount(QisTransactionItemRequest txnItemRequest,
			QisTransaction qisTransaction, QisTransactionItem qisTransactionItem) {
		if (qisTransactionItem.isTaxable()) {
			qisTransactionItem.setTaxRate(qisTransaction.getTaxRate());
		}

		if (txnItemRequest.getDiscountRate() != null && txnItemRequest.getDiscountRate() > 0) {
			if (txnItemRequest.getDiscountType() == null) {
				throw new RuntimeException("Discount Type is required.",
						new Throwable("transactionItems.discountType: Discount Type is required."));
			}

			String discountType = appUtility.getDiscountType(txnItemRequest.getDiscountType());
			if (discountType == null) {
				throw new RuntimeException("Invalid discount type [" + txnItemRequest.getDiscountType() + "]",
						new Throwable(
								"transactionType: Invalid discount type [" + txnItemRequest.getDiscountType() + "]"));
			}

			if (qisTransaction.getDiscountRate() == null || qisTransaction.getDiscountRate() == 0) {
				qisTransaction.setDiscountRate(txnItemRequest.getDiscountRate());
			} else {
				if (qisTransaction.getDiscountRate() != txnItemRequest.getDiscountRate()) {
					throw new RuntimeException("Items has a different discount rate.",
							new Throwable("Items has a different discount rate."));
				}
			}

			if (qisTransaction.getDiscountType() == null) {
				qisTransaction.setDiscountType(txnItemRequest.getDiscountType());
			} else {
				if (!qisTransaction.getDiscountType().equals(txnItemRequest.getDiscountType())) {
					throw new RuntimeException("Items has a different discount type.",
							new Throwable("Items has a different discount type."));
				}
			}

			qisTransactionItem.setDiscountRate(txnItemRequest.getDiscountRate());
			qisTransactionItem.setDiscountType(txnItemRequest.getDiscountType());
		}

		qisTransactionItem.computeItemAmount();

		return qisTransactionItem;
	}

	public QisTransactionPayment getQisTransactionPayment(QisTransactionPaymentRequest payment) {
		String currency = null;
		if (payment.getCurrency() != null) {
			currency = appUtility.getCurrencyType(payment.getCurrency());
		}
		if (currency == null) {
			throw new RuntimeException("Invalid currency[" + payment.getCurrency() + "]");
		}

		Double amount = appUtility.parseDoubleAmount(payment.getAmount());
		if (amount == null) {
			throw new RuntimeException("Invalid amount[" + payment.getAmount() + "]");
		}

		QisTransactionPayment qisTransactionPayment = null;
		switch (payment.getPaymentType()) {
		case "CA": { // CASH
			qisTransactionPayment = new QisTransactionPayment(payment.getPaymentType(), amount, payment.getCurrency());
			qisTransactionPayment.setPaymentMode("CA");
		}
			break;

		case "B": { // BANK
			String error = "";
			if (payment.getPaymentMode() == null) {
				error = appUtility.addToFormatedData(error, "paymentMode", " required");
			} else {
				if (payment.getPaymentBank() == null) {
					error = appUtility.addToFormatedData(error, "paymentBank", " required");
				} else {
					String bank = appUtility.getBankType(payment.getPaymentBank());
					if (bank == null) {
						error = appUtility.addToFormatedData(error, "paymentBank",
								" invalid value.[" + payment.getPaymentBank() + "]");
					}
				}

				if (payment.getCardChequeNumber() == null) {
					error = appUtility.addToFormatedData(error, "cardChequeNumber", " required");
				}

				switch (payment.getPaymentMode()) {
				case "CC": {
					if (payment.getCcType() == null) {
						error = appUtility.addToFormatedData(error, "ccType", " required");
					}

					if (payment.getCcHolderName() == null) {
						error = appUtility.addToFormatedData(error, "ccHolderName", " required");
					}

				}
					break;

				case "DC": // DEBIT CARD
				case "CQ": { // CHEQUE
				}
					break;

				default:
					error = appUtility.addToFormatedData(error, "paymentMode",
							" invalid value[" + payment.getPaymentMode() + "]");
				}
			}
			if (!"".equals(error)) {
				throw new RuntimeException("Error saving payments [" + error + "]",
						new Throwable("Error saving payments [" + error + "]."));
			}

			qisTransactionPayment = new QisTransactionPayment(payment.getPaymentType(), amount, payment.getCurrency());
			qisTransactionPayment.setPaymentMode(payment.getPaymentMode());
			qisTransactionPayment.setPaymentBank(payment.getPaymentBank());
			qisTransactionPayment.setCardChequeNumber(payment.getCardChequeNumber());
			if ("CC".equals(payment.getPaymentMode())) {
				qisTransactionPayment.setCcType(payment.getCcType());
				qisTransactionPayment.setCcHolderName(payment.getCcHolderName());
			}
		}
			break;

		case "C": { // CHARGE TO
			String error = "";
			if (payment.getPaymentMode() == null) {
				error = appUtility.addToFormatedData(error, "paymentMode", " required");
			} else {
				switch (payment.getPaymentMode()) {
				case "HMO": {
					if (payment.getHmoLOE() == null) {
						error = appUtility.addToFormatedData(error, "hmoLOE", " required");
					}

					if (payment.getHmoAccountNumber() == null) {
						error = appUtility.addToFormatedData(error, "hmoAccountNumber", " required");
					}

					if (amount > 1000d && payment.getHmoApprovalCode() == null) {
						error = appUtility.addToFormatedData(error, "hmoApprovalCode", " required");
					}
				}
					break;
				case "ACCT": // DEBIT CARD
				case "APE": // CHEQUE
				case "MMO": { 
				}
					break;

				default:
					error = appUtility.addToFormatedData(error, "paymentMode",
							" invalid value[" + payment.getPaymentMode() + "]");
				}

			}

			if (payment.getBillerId() == null) {
				error = appUtility.addToFormatedData(error, "billerId", " required");
			}

			QisCorporate qisBiller = appUtility.getQisCorporateByCorporateId(payment.getBillerId());
			if (qisBiller == null) {
				error = appUtility.addToFormatedData(error, "billerId",
						" invalid value[" + payment.getBillerId() + "]");
			}

			if (!"".equals(error)) {
				throw new RuntimeException("Error saving payments [" + error + "]",
						new Throwable("Error saving payments [" + error + "]."));
			}

			qisTransactionPayment = new QisTransactionPayment(payment.getPaymentType(), amount, payment.getCurrency());
			qisTransactionPayment.setPaymentMode(payment.getPaymentMode());
			qisTransactionPayment.setBillerId(qisBiller.getId());
			qisTransactionPayment.setBiller(qisBiller);
			if ("HMO".equals(payment.getPaymentMode())) {
				qisTransactionPayment.setHmoLOE(payment.getHmoLOE());
				qisTransactionPayment.setHmoAccountNumber(payment.getHmoAccountNumber());
				qisTransactionPayment.setHmoApprovalCode(payment.getHmoApprovalCode());
			}
		}
			break;

		case "VR": { // VITUAL
			String error = "";
			if (payment.getPaymentMode() == null) {
				error = appUtility.addToFormatedData(error, "paymentMode", " required");
			} else {
				switch (payment.getPaymentMode()) {
				case "GCA": // GCASH
				case "PMA": // PAYMAYA
				case "WT": // WIRE TRANSFER
				case "PMO": { // PAYMONGO
				}
					break;

				default:
					error = appUtility.addToFormatedData(error, "paymentMode",
							" invalid value[" + payment.getPaymentMode() + "]");
				}
			}

			if (payment.getReferenceNumber() == null) {
				error = appUtility.addToFormatedData(error, "ReferenceNumber", " required");
			}

			if (!"".equals(error)) {
				throw new RuntimeException("Error saving payments [" + error + "]",
						new Throwable("Error saving payments [" + error + "]."));
			}

			qisTransactionPayment = new QisTransactionPayment(payment.getPaymentType(), amount, payment.getCurrency());
			qisTransactionPayment.setPaymentMode(payment.getPaymentMode());
			qisTransactionPayment.setReferenceNumber(payment.getReferenceNumber());
		}
			break;

		default:
			throw new IllegalArgumentException("Invalid Payment Type: " + payment.getPaymentType());
		}

		qisTransactionPayment.setId(payment.getId());

		return qisTransactionPayment;
	}

	public String paymentDetails(QisTransactionPayment payment) {
		String details = null;

		if (payment != null) {
			switch (payment.getPaymentType()) {
			case "CA": // cash
				details = "CASH=" + payment.getAmount() + payment.getCurrency();
				break;
			case "B": // bank
				details = "CHARGE TO=" + payment.getAmount() + payment.getCurrency() + "-" + payment.getPaymentMode()
						+ ":" + payment.getPaymentBank() + ":" + payment.getCardChequeNumber();
				if ("CC".equals(payment.getPaymentMode())) {
					details += ":" + payment.getCcType() + ">" + payment.getCcHolderName();
				}
				break;
			case "C": // charge to
				details = "CHARGE TO=" + payment.getAmount() + payment.getCurrency() + "-" + payment.getPaymentMode()
						+ ":" + payment.getBiller().getCorporateid();
				if ("HMO".equals(payment.getPaymentMode())) {
					details += ":" + payment.getHmoAccountNumber() + ":" + payment.getHmoLOE() + ">"
							+ payment.getHmoApprovalCode();
				}
				break;
			case "VR":
				details = "VIRTUAL=" + payment.getAmount() + payment.getCurrency() + "-" + payment.getPaymentMode()
						+ ":" + payment.getReferenceNumber();
				break;
			}
		}

		return details;
	}

	public Object getItemDetails(String itemType, String referenceNumber) {
		Object itemDetail = null;

		if ("ITM".equals(itemType)) {
			itemDetail = qisItemRepository.findByItemid(referenceNumber);
		} else if ("PCK".equals(itemType)) {
			itemDetail = qisPackageRepository.findByPackageid(referenceNumber);
		}

		return itemDetail;
	}

	public void getLaboratoryRequestList(Set<QisTransactionLaboratoryRequest> labRequests, QisTransactionItem item) {
		if ("ITM".equals(item.getItemType())) {
			QisTransactionLaboratoryRequest labRequest = getQisTransactionLaboratoryRequest(item, item.getQisItem());
			if (labRequest != null) {
				labRequests.add(labRequest);
			}
		} else if ("PCK".equals(item.getItemType())) {
			item.getQisPackage().getPackageItems().forEach(qisItem -> {
				QisTransactionLaboratoryRequest labRequest = getQisTransactionLaboratoryRequest(item, qisItem);
				if (labRequest != null) {
					labRequests.add(labRequest);
				}
			});
		}
	}

	private QisTransactionLaboratoryRequest getQisTransactionLaboratoryRequest(QisTransactionItem transactionItem,
			QisItem item) {
		QisTransactionLaboratoryRequest labRequest = null;

		if (!item.getItemLaboratory().equals(QisLaboratoryName.NO.toString())) {
			labRequest = new QisTransactionLaboratoryRequest(transactionItem.getId(), item.getId(),
					item.getItemLaboratory());
			labRequest.setItemDetails(item);
		}

		return labRequest;
	}
}
