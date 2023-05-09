package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class QisSOPPaymentRequest implements Serializable {
	
	private static final long serialVersionUID = -9174186871333820895L;

	@NotNull(message = "Payment Date is required.")
	@Pattern(message = "Invalid Payment Date format(YYYYMMDD)", regexp = "^\\d{4}\\d{2}\\d{2}$")
	private String paymentDate;
	
	@NotNull(message = "Payment Amount is required.")
	@Digits(integer = 12, fraction = 4)
	private String paymentAmount;

	@NotEmpty(message = "Payment Type is required.")
	@Size(min = 1, max = 4, message = "Payment Type must between {min} and {max} characters.")
	private String paymentType;

	@Size(max = 8, message = "Payment Bank must not exceed to {max} characters.")
	private String paymentBank;
	
	@Size(max = 30, message = "Account Number must not exceed to {max} characters.")
	private String accountNumber;
	
	@NotNull(message = "SOP List is required.")
	@NotEmpty(message = "SOP List should not be empty.")
	private Set<Long> sopList;

	@Size(max = 200, message = "Other Notes must not exceed to {max} characters.")
	private String otherNotes;
	
	@Digits(integer = 12, fraction = 4)
	private String otherAmount;
	
	@Digits(integer = 12, fraction = 4)
	private String taxWithHeld;
	
//	@NotNull(message = "Transaction List is required.")
//	@NotEmpty(message = "Transaction List should not be empty.")
	private List<String> transactionIds;
	
	
	public QisSOPPaymentRequest() {
		super();
	}


	public String getPaymentDate() {
		return paymentDate;
	}


	public String getPaymentAmount() {
		return paymentAmount;
	}


	public String getPaymentType() {
		return paymentType;
	}


	public String getPaymentBank() {
		return paymentBank;
	}


	public String getAccountNumber() {
		return accountNumber;
	}


	public Set<Long> getSopList() {
		return sopList;
	}


	public String getOtherNotes() {
		return otherNotes;
	}


	public String getOtherAmount() {
		return otherAmount;
	}


	public String getTaxWithHeld() {
		return taxWithHeld;
	}


	public List<String> getTransactionIds() {
		return transactionIds;
	}


	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}


	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}


	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}


	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}


	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	public void setSopList(Set<Long> sopList) {
		this.sopList = sopList;
	}


	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}


	public void setOtherAmount(String otherAmount) {
		this.otherAmount = otherAmount;
	}


	public void setTaxWithHeld(String taxWithHeld) {
		this.taxWithHeld = taxWithHeld;
	}


	public void setTransactionIds(List<String> transactionIds) {
		this.transactionIds = transactionIds;
	}
}
