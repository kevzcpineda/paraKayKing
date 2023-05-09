package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class QisUpdateTransactionRequest implements Serializable {

	private static final long serialVersionUID = 3789427773751479959L;

	@Pattern(message = "Invalid date format(yyyy-MM-DD hh:mm:ss)", regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")
	private String transactionDateTime;

	@Size(max = 12, message = "Branch Id should not exceed {max} characters.")
	private String branchId;

	@Size(max = 20, message = "Patient Id should not exceed {max} characters.")
	private String patientId;

//	@Size(max = 20, message = "Corporate Id should not exceed {max} characters.")
//	private String corporateId;

	@Size(max = 12, message = "Referral Id should not exceed {max} characters.")
	private String referralId;

	@Size(max = 20, message = "Cashier Id should not exceed {max} characters.")
	private String cashierId;

	@Size(max = 4, message = "Transaction Type should not exceed {max} characters.")
	private String transactionType;

	@Size(max = 4, message = "Dispatch Type should not exceed {max} characters.")
	private String dispatch;

	@Size(max = 80, message = "Remarks should not exceed {max} characters.")
	private String remarks;

	@Size(max = 120, message = "Email To should not exceed {max} characters.")
	private String emailTo;
	
	public QisUpdateTransactionRequest() {
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

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
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
