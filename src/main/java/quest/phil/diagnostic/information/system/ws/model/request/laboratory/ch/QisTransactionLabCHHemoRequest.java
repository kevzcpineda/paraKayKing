package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHHemoRequest implements Serializable {

	private static final long serialVersionUID = 2173988433377571836L;

	@Digits(integer = 10, fraction = 4)
	private String hemoglobinA1C;
	private String referenceLabId;

	public QisTransactionLabCHHemoRequest() {
		super();
	}

	public String getHemoglobinA1C() {
		return hemoglobinA1C;
	}

	public void setHemoglobinA1C(String hemoglobinA1C) {
		this.hemoglobinA1C = hemoglobinA1C;
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
