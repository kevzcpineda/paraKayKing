package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHBUNRequest implements Serializable {

	private static final long serialVersionUID = -2172388525677692049L;

	@Digits(integer = 10, fraction = 4)
	private String bun;

	@Digits(integer = 10, fraction = 4)
	private String conventional;
	
	private String referenceLabId;

	public String getReferenceLabId() {
		return referenceLabId;
	}

	public void setReferenceLabId(String referenceLabId) {
		this.referenceLabId = referenceLabId;
	}

	public QisTransactionLabCHBUNRequest() {
		super();
	}

	public String getBun() {
		return bun;
	}

	public void setBun(String bun) {
		this.bun = bun;
	}

	public String getConventional() {
		return conventional;
	}

	public void setConventional(String conventional) {
		this.conventional = conventional;
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
