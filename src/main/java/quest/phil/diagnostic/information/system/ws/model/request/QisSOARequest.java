package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisSOARequest implements Serializable {

	private static final long serialVersionUID = -7605013191746001069L;

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
	private String soaAmount;

	@NotNull(message = "Transactions is required.")
	@NotEmpty(message = "Transactions should not be empty.")
	private Set<Long> transactions;
	
	private String purchaseOrder;

	public QisSOARequest() {
		super();
	}

	public String getCoveredDateFrom() {
		return coveredDateFrom;
	}

	public void setCoveredDateFrom(String coveredDateFrom) {
		this.coveredDateFrom = coveredDateFrom;
	}

	public String getCoveredDateTo() {
		return coveredDateTo;
	}

	public void setCoveredDateTo(String coveredDateTo) {
		this.coveredDateTo = coveredDateTo;
	}

	public String getStatementDate() {
		return statementDate;
	}

	public void setStatementDate(String statementDate) {
		this.statementDate = statementDate;
	}

	public String getSoaAmount() {
		return soaAmount;
	}

	public void setSoaAmount(String soaAmount) {
		this.soaAmount = soaAmount;
	}

	public Set<Long> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Long> transactions) {
		this.transactions = transactions;
	}
	
	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
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
