package quest.phil.diagnostic.information.system.ws.model.response;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisSOAResponse implements Serializable {

	private static final long serialVersionUID = 1654344439842176350L;
	private String soaNumber;
	private int soaCount;
	private double soaAmount = 0;
	private Long chargeToId;
	private String coverageDateFrom;
	private String coverageDateTo;
	private String statementDate;
	private Set<Long> transactions;

	public QisSOAResponse() {
		super();
	}

	public String getSoaNumber() {
		return soaNumber;
	}

	public void setSoaNumber(String soaNumber) {
		this.soaNumber = soaNumber;
	}

	public int getSoaCount() {
		return soaCount;
	}

	public void setSoaCount(int soaCount) {
		this.soaCount = soaCount;
	}

	public double getSoaAmount() {
		return soaAmount;
	}

	public void setSoaAmount(double soaAmount) {
		this.soaAmount = soaAmount;
	}

	public Long getChargeToId() {
		return chargeToId;
	}

	public void setChargeToId(Long chargeToId) {
		this.chargeToId = chargeToId;
	}

	public String getCoverageDateFrom() {
		return coverageDateFrom;
	}

	public void setCoverageDateFrom(String coverageDateFrom) {
		this.coverageDateFrom = coverageDateFrom;
	}

	public String getCoverageDateTo() {
		return coverageDateTo;
	}

	public void setCoverageDateTo(String coverageDateTo) {
		this.coverageDateTo = coverageDateTo;
	}

	public String getStatementDate() {
		return statementDate;
	}

	public void setStatementDate(String statementDate) {
		this.statementDate = statementDate;
	}

	public Set<Long> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Long> transactions) {
		this.transactions = transactions;
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
