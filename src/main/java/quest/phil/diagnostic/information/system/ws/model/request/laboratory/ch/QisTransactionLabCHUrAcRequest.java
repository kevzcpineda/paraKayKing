package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHUrAcRequest implements Serializable {

	private static final long serialVersionUID = 8214933665619404933L;

	@Digits(integer = 10, fraction = 4)
	private String uricAcid;

	@Digits(integer = 10, fraction = 4)
	private String conventional;
	
	private String referenceLabId;

	public QisTransactionLabCHUrAcRequest() {
		super();
	}

	public String getUricAcid() {
		return uricAcid;
	}

	public void setUricAcid(String uricAcid) {
		this.uricAcid = uricAcid;
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
