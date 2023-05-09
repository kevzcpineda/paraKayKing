package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHCPKRequest implements Serializable {

	private static final long serialVersionUID = 5696404076159372502L;

	@Digits(integer = 10, fraction = 4)
	private String cpkMB;

	@Digits(integer = 10, fraction = 4)
	private String cpkMM;

	@Digits(integer = 10, fraction = 4)
	private String totalCpk;
	
	private String referenceLabId;

	public QisTransactionLabCHCPKRequest() {
		super();
	}

	public String getCpkMB() {
		return cpkMB;
	}

	public void setCpkMB(String cpkMB) {
		this.cpkMB = cpkMB;
	}

	public String getCpkMM() {
		return cpkMM;
	}

	public void setCpkMM(String cpkMM) {
		this.cpkMM = cpkMM;
	}

	public String getTotalCpk() {
		return totalCpk;
	}

	public void setTotalCpk(String totalCpk) {
		this.totalCpk = totalCpk;
	}

	public String getReferenceLabId() {
		return referenceLabId;
	}

	public void setReferenceLabId(String referenceLabId) {
		this.referenceLabId = referenceLabId;
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
