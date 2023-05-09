package quest.phil.diagnostic.information.system.ws.model.response;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionPaymentResponse implements Serializable {

	private static final long serialVersionUID = -4430097318601913228L;
	private String paymentType;
	private String paymentMode;
	private String paymentBank;
	private Double amount;
	private String currency;
	private String billerId;
	private String billerName;
	private String hmoLOE;
	private String hmoAccountNumber;
	private String hmoApprovalCode;
	private String ccType;
	private String ccHolderName;
	private String referenceNumber;
	private String cardChequeNumber;

	public QisTransactionPaymentResponse() {
		super();
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
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

	public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
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

	public String getPaymentBank() {
		return paymentBank;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	public String getCardChequeNumber() {
		return cardChequeNumber;
	}

	public void setCardChequeNumber(String cardChequeNumber) {
		this.cardChequeNumber = cardChequeNumber;
	}

	@Override
	public String toString() {
		String serialized = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			jpe.printStackTrace();
		}
		return serialized;
	}
}
