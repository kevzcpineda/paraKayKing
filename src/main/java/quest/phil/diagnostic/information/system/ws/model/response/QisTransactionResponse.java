package quest.phil.diagnostic.information.system.ws.model.response;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionResponse implements Serializable {

	private static final long serialVersionUID = 1337383391819699848L;
	private String transactionid;
	private String transactionDate;
	private String branchId;
	private String patientId;
	private String patientName;
//	private String corporateId;
//	private String corporateName;
	private String cashierId;
	private String cashierName;
	private String status;
	private String transactionType;
	private String dispatch;
	private Integer taxRate = 0;
	private Integer discountRate = 0;
	private Double totalItemGrossAmount;
	private Double totalDiscountableAmount;
	private Double totalItemDiscountAmount;
	private Double totalTaxableAmount;
	private Double totalItemTaxAmount;
	private Double totalItemNetAmount;
	private Double totalPaymentAmount;
	private Double totalChangeAmount;
	private Double totalCashAmount;
	private Double totalCashOut;
	private Double specialDiscountAmount = 0d;
	private String referralId;
	private String referralName;

	private Set<QisTransactionItemResponse> itemTransactions;
	private Set<QisTransactionPaymentResponse> paymentTransactions;
	private QisTransactionDiscountResponse specialDiscount;

	private String seniorCitizenID;
	private String pwdID;
	private String remarks;
	
	public QisTransactionResponse() {
		super();
	}

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

//	public String getCorporateId() {
//		return corporateId;
//	}
//
//	public void setCorporateId(String corporateId) {
//		this.corporateId = corporateId;
//	}
//
//	public String getCorporateName() {
//		return corporateName;
//	}
//
//	public void setCorporateName(String corporateName) {
//		this.corporateName = corporateName;
//	}

	public String getCashierId() {
		return cashierId;
	}

	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}

	public String getCashierName() {
		return cashierName;
	}

	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Integer getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Integer taxRate) {
		this.taxRate = taxRate;
	}

	public Integer getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Integer discountRate) {
		this.discountRate = discountRate;
	}

	public Double getTotalItemGrossAmount() {
		return totalItemGrossAmount;
	}

	public void setTotalItemGrossAmount(Double totalItemGrossAmount) {
		this.totalItemGrossAmount = totalItemGrossAmount;
	}

	public Double getTotalItemDiscountAmount() {
		return totalItemDiscountAmount;
	}

	public void setTotalItemDiscountAmount(Double totalItemDiscountAmount) {
		this.totalItemDiscountAmount = totalItemDiscountAmount;
	}

	public Double getTotalItemTaxAmount() {
		return totalItemTaxAmount;
	}

	public void setTotalItemTaxAmount(Double totalItemTaxAmount) {
		this.totalItemTaxAmount = totalItemTaxAmount;
	}

	public Double getTotalItemNetAmount() {
		return totalItemNetAmount;
	}

	public void setTotalItemNetAmount(Double totalItemNetAmount) {
		this.totalItemNetAmount = totalItemNetAmount;
	}

	public Double getTotalPaymentAmount() {
		return totalPaymentAmount;
	}

	public void setTotalPaymentAmount(Double totalPaymentAmount) {
		this.totalPaymentAmount = totalPaymentAmount;
	}

	public Double getTotalChangeAmount() {
		return totalChangeAmount;
	}

	public void setTotalChangeAmount(Double totalChangeAmount) {
		this.totalChangeAmount = totalChangeAmount;
	}

	public Double getTotalCashAmount() {
		return totalCashAmount;
	}

	public void setTotalCashAmount(Double totalCashAmount) {
		this.totalCashAmount = totalCashAmount;
	}

	public Double getTotalDiscountableAmount() {
		return totalDiscountableAmount;
	}

	public void setTotalDiscountableAmount(Double totalDiscountableAmount) {
		this.totalDiscountableAmount = totalDiscountableAmount;
	}

	public Double getTotalTaxableAmount() {
		return totalTaxableAmount;
	}

	public void setTotalTaxableAmount(Double totalTaxableAmount) {
		this.totalTaxableAmount = totalTaxableAmount;
	}

	public Double getTotalCashOut() {
		return totalCashOut;
	}

	public void setTotalCashOut(Double totalCashOut) {
		this.totalCashOut = totalCashOut;
	}

	public Double getSpecialDiscountAmount() {
		return specialDiscountAmount;
	}

	public void setSpecialDiscountAmount(Double specialDiscountAmount) {
		this.specialDiscountAmount = specialDiscountAmount;
	}

	public Set<QisTransactionItemResponse> getItemTransactions() {
		return itemTransactions;
	}

	public void setItemTransactions(Set<QisTransactionItemResponse> itemTransactions) {
		this.itemTransactions = itemTransactions;
	}

	public Set<QisTransactionPaymentResponse> getPaymentTransactions() {
		return paymentTransactions;
	}

	public void setPaymentTransactions(Set<QisTransactionPaymentResponse> paymentTransactions) {
		this.paymentTransactions = paymentTransactions;
	}

	public QisTransactionDiscountResponse getSpecialDiscount() {
		return specialDiscount;
	}

	public void setSpecialDiscount(QisTransactionDiscountResponse specialDiscount) {
		this.specialDiscount = specialDiscount;
	}

	public String getSeniorCitizenID() {
		return seniorCitizenID;
	}

	public void setSeniorCitizenID(String seniorCitizenID) {
		this.seniorCitizenID = seniorCitizenID;
	}

	public String getPwdID() {
		return pwdID;
	}

	public void setPwdID(String pwdID) {
		this.pwdID = pwdID;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public String getReferralId() {
		return referralId;
	}

	public void setReferralId(String referralId) {
		this.referralId = referralId;
	}

	public String getReferralName() {
		return referralName;
	}

	public void setReferralName(String referralName) {
		this.referralName = referralName;
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
