package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisSOAPaymentRequest implements Serializable {

	private static final long serialVersionUID = 2188022723885379324L;

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
	
	@NotNull(message = "SOA List is required.")
	@NotEmpty(message = "SOA List should not be empty.")
	private Set<Long> soaList;

	@Size(max = 200, message = "Other Notes must not exceed to {max} characters.")
	private String otherNotes;
	
	@Digits(integer = 12, fraction = 4)
	private String otherAmount;
	
	@Digits(integer = 12, fraction = 4)
	private String taxWithHeld;
	
	@Digits(integer = 12, fraction = 4)
	private String addvancePayment;
	
	@NotNull(message = "Transaction List is required.")
	@NotEmpty(message = "Transaction List should not be empty.")
	private List<String> transactionIds;
	
	public QisSOAPaymentRequest() {
		super();
	}

	public String getAddvancePayment() {
		return addvancePayment;
	}

	public void setAddvancePayment(String addvancePayment) {
		this.addvancePayment = addvancePayment;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentBank() {
		return paymentBank;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}

	public String getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(String otherAmount) {
		this.otherAmount = otherAmount;
	}

	public Set<Long> getSoaList() {
		return soaList;
	}

	public void setSoaList(Set<Long> soaList) {
		this.soaList = soaList;
	}
	
	public String getTaxWithHeld() {
		return taxWithHeld;
	}

	public void setTaxWithHeld(String taxWithHeld) {
		this.taxWithHeld = taxWithHeld;
	}

	public List<String> getTransactionIds() {
		return transactionIds;
	}

	public void setTransactionIds(List<String> transactionIds) {
		this.transactionIds = transactionIds;
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
