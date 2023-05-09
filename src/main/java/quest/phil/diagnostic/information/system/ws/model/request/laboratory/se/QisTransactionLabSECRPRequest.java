package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSECRPRequest implements Serializable {

	private static final long serialVersionUID = -7525845962661424749L;

	@Digits(integer = 10, fraction = 4)
	private String dilution;

	@Digits(integer = 10, fraction = 4)
	private String result;
	
	private String referenceLabId;

	public QisTransactionLabSECRPRequest() {
		super();
	}

	public String getDilution() {
		return dilution;
	}

	public void setDilution(String dilution) {
		this.dilution = dilution;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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
