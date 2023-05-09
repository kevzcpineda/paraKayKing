package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class QisSOPRequest implements Serializable {

	private static final long serialVersionUID = 6353160277562147713L;

	@NotNull(message = "Covered Date From is required.")
	@Pattern(message = "Invalid Covered Date From format(YYYYMMDD)", regexp = "^\\d{4}\\d{2}\\d{2}$")
	private String coveredDateFrom;

	@NotNull(message = "Covered Date To is required.")
	@Pattern(message = "Invalid Covered Date To  format(YYYYMMDD)", regexp = "^\\d{4}\\d{2}\\d{2}$")
	private String coveredDateTo;

	@NotNull(message = "Covered Statement Date is required.")
	@Pattern(message = "Invalid Statement Date format(YYYYMMDD)", regexp = "^\\d{4}\\d{2}\\d{2}$")
	private String statementDate;

	@NotNull(message = "SOA Amount is required.")
	@Digits(integer = 12, fraction = 4)
	private String sopAmount;

	@NotNull(message = "Transactions is required.")
	@NotEmpty(message = "Transactions should not be empty.")
	private Set<Long> transactions;
	
	private String purchaseOrder;
	
	public QisSOPRequest() {
		super();
	}

	public String getCoveredDateFrom() {
		return coveredDateFrom;
	}

	public String getCoveredDateTo() {
		return coveredDateTo;
	}

	public String getStatementDate() {
		return statementDate;
	}

	public String getSopAmount() {
		return sopAmount;
	}

	public Set<Long> getTransactions() {
		return transactions;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setCoveredDateFrom(String coveredDateFrom) {
		this.coveredDateFrom = coveredDateFrom;
	}

	public void setCoveredDateTo(String coveredDateTo) {
		this.coveredDateTo = coveredDateTo;
	}

	public void setStatementDate(String statementDate) {
		this.statementDate = statementDate;
	}

	public void setSopAmount(String sopAmount) {
		this.sopAmount = sopAmount;
	}

	public void setTransactions(Set<Long> transactions) {
		this.transactions = transactions;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	
}
