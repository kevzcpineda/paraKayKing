package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QisTransactionPaymentRequest implements Serializable {

	private static final long serialVersionUID = -5696592892268177983L;

	public Long id;

	@NotNull(message = "Payment Type is required.")
	@NotEmpty(message = "Payment Type should not be empty.")
	@Size(min = 1, max = 4, message = "Payment Type must between {min} and {max} characters.")
	public String paymentType;

	@Size(max = 4, message = "Payment Mode must not exceed to {max} characters.")
	public String paymentMode;

	@Size(max = 4, message = "Payment Bank must not exceed to {max} characters.")
	public String paymentBank;
	
	@NotNull(message = "Amount is required.")
	@Digits(integer = 10, fraction = 4)
	public String amount;

	@NotNull(message = "Currency is required.")
	@NotEmpty(message = "Currency should not be empty.")
	@Size(min = 3, max = 4, message = "Currency must between {min} and {max} characters.")
	public String currency;

	public String billerId;

	@Size(max = 20, message = "HMO LOE Number must not exceed to {max} characters.")
	private String hmoLOE;

	@Size(max = 30, message = "HMO Account Number must not exceed to {max} characters.")
	private String hmoAccountNumber;

	@Size(max = 20, message = "HMO Approval Code must not exceed to {max} characters.")
	private String hmoApprovalCode;

	@Size(max = 20, message = "Credit Card Type must not exceed to {max} characters.")
	private String ccType;

	@Size(max = 256, message = "Credit Card Holder Name must not exceed to {max} characters.")
	private String ccHolderName;

	@Size(max = 20, message = "Reference Number must not exceed to {max} characters.")
	private String referenceNumber;
	
	@Size(max = 20, message = "Card/Check Number must not exceed to {max} characters.")
	private String cardChequeNumber;
	
	public QisTransactionPaymentRequest() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPaymentBank() {
		return paymentBank;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getHmoLOE() {
		return hmoLOE;
	}

	public void setHmoLOE(String hmoLOE) {
		this.hmoLOE = hmoLOE;
	}

	public String getHmoAccountNumber() {
		return hmoAccountNumber;
	}

	public void setHmoAccountNumber(String hmoAccountNumber) {
		this.hmoAccountNumber = hmoAccountNumber;
	}

	public String getHmoApprovalCode() {
		return hmoApprovalCode;
	}

	public void setHmoApprovalCode(String hmoApprovalCode) {
		this.hmoApprovalCode = hmoApprovalCode;
	}

	public String getCcType() {
		return ccType;
	}

	public void setCcType(String ccType) {
		this.ccType = ccType;
	}

	public String getCcHolderName() {
		if (ccHolderName != null) {
			return ccHolderName.trim().toUpperCase();
		}
		return ccHolderName;
	}

	public void setCcHolderName(String ccHolderName) {
		this.ccHolderName = ccHolderName;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getCardChequeNumber() {
		return cardChequeNumber;
	}

	public void setCardChequeNumber(String cardChequeNumber) {
		this.cardChequeNumber = cardChequeNumber;
	}
}
