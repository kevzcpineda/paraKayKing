package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class QisTransactionRequest implements Serializable {

	private static final long serialVersionUID = -6632853615541425915L;

	@Pattern(message = "Invalid date format(yyyy-MM-DD hh:mm:ss)", regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")
	private String transactionDateTime;

	@NotNull(message = "Branch Id is required.")
	@Size(max = 12, message = "Branch Id should not exceed {max} characters.")
	private String branchId;

	@NotNull(message = "Patient Id is required.")
	@Size(max = 20, message = "Patient Id should not exceed {max} characters.")
	private String patientId;

//	@Size(max = 20, message = "Corporate Id should not exceed {max} characters.")
//	private String corporateId;

	@Size(max = 12, message = "Referral Id should not exceed {max} characters.")
	private String referralId;

	@NotNull(message = "Casher Id is required.")
	@Size(max = 20, message = "Cashier Id should not exceed {max} characters.")
	private String cashierId;

	@NotNull(message = "Status is required.")
	@NotEmpty(message = "Status should not be empty.")
	@Size(max = 4, message = "Status should not exceed {max} characters.")
	private String status;

	@NotNull(message = "Transaction Type is required.")
	@NotEmpty(message = "Transaction Type should not be empty.")
	@Size(max = 4, message = "Transaction Type should not exceed {max} characters.")
	private String transactionType;

	@NotNull(message = "Dispatch Type is required.")
	@NotEmpty(message = "Dispatch Type should not be empty.")
	@Size(max = 4, message = "Dispatch Type should not exceed {max} characters.")
	private String dispatch;

	@NotEmpty(message = "Transaction Items should not be empty.")
	@Valid
	private Set<QisTransactionItemRequest> transactionItems;

	@Valid
	private Set<QisTransactionPaymentRequest> transactionPayments;

	@Valid
	private QisTransactionDiscountRequest transactionDiscount;

	@Size(max = 20, message = "Senior Citizen ID should not exceed {max} characters.")
	private String seniorCitizenID;

	@Size(max = 20, message = "Person With Disability(PWD) ID should not exceed {max} characters.")
	private String pwdID;

	@Valid
	private QisTransactionAuthorizeRequest authorizeUpdateRequest;

	@Size(max = 80, message = "Remarks should not exceed {max} characters.")
	private String remarks;

	@Size(max = 120, message = "Email To should not exceed {max} characters.")
	private String emailTo;
	
	public QisTransactionRequest() {
		super();
	}

	public String getTransactionDateTime() {
		return transactionDateTime;
	}

	public void setTransactionDateTime(String transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
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

//	public String getCorporateId() {
//		return corporateId;
//	}
//
//	public void setCorporateId(String corporateId) {
//		this.corporateId = corporateId;
//	}

	public String getCashierId() {
		return cashierId;
	}

	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
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

	public Set<QisTransactionItemRequest> getTransactionItems() {
		return transactionItems;
	}

	public void setTransactionItems(Set<QisTransactionItemRequest> transactionItems) {
		this.transactionItems = transactionItems;
	}

	public Set<QisTransactionPaymentRequest> getTransactionPayments() {
		return transactionPayments;
	}

	public void setTransactionPayments(Set<QisTransactionPaymentRequest> transactionPayments) {
		this.transactionPayments = transactionPayments;
	}

	public QisTransactionDiscountRequest getTransactionDiscount() {
		return transactionDiscount;
	}

	public void setTransactionDiscount(QisTransactionDiscountRequest transactionDiscount) {
		this.transactionDiscount = transactionDiscount;
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

	public QisTransactionAuthorizeRequest getAuthorizeUpdateRequest() {
		return authorizeUpdateRequest;
	}

	public void setAuthorizeUpdateRequest(QisTransactionAuthorizeRequest authorizeUpdateRequest) {
		this.authorizeUpdateRequest = authorizeUpdateRequest;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getReferralId() {
		return referralId;
	}

	public void setReferralId(String referralId) {
		this.referralId = referralId;
	}

	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	
}
