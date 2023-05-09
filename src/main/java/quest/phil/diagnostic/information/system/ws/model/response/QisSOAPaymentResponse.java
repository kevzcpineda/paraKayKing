package quest.phil.diagnostic.information.system.ws.model.response;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisSOAPaymentResponse implements Serializable {

	private static final long serialVersionUID = 6357447076988599445L;
	private Long chargeToId;
	private String paymentDate;
	private double paymentAmount = 0;
	private String paymentType;
	private String paymentBank;
	private String accountNumber;
	private double otherAmount = 0;
	private String otherNotes;
	private Set<Long> soaList;

	public QisSOAPaymentResponse() {
		super();
	}
	
	public Long getChargeToId() {
		return chargeToId;
	}

	public void setChargeToId(Long chargeToId) {
		this.chargeToId = chargeToId;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
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

	public double getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(double otherAmount) {
		this.otherAmount = otherAmount;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}

	public Set<Long> getSoaList() {
		return soaList;
	}

	public void setSoaList(Set<Long> soaList) {
		this.soaList = soaList;
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
