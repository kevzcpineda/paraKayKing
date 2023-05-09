package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHFBSRequest implements Serializable {

	private static final long serialVersionUID = 9119808422659389527L;

	@Digits(integer = 10, fraction = 4)
	private String fbs;

	@Digits(integer = 10, fraction = 4)
	private String conventional;

	private String referenceLabId;
	
	public QisTransactionLabCHFBSRequest() {
		super();
	}

	public String getFbs() {
		return fbs;
	}

	public void setFbs(String fbs) {
		this.fbs = fbs;
	}

	public String getConventional() {
		return conventional;
	}

	public void setConventional(String conventional) {
		this.conventional = conventional;
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
