package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSETPNRequest implements Serializable {

	private static final long serialVersionUID = 6994919435025180084L;

	private Boolean result;
	private String referenceLabId;
	
	public QisTransactionLabSETPNRequest() {
		super();
	}

	public Boolean getResult() {
		return result;
	}

	public String getReferenceLabId() {
		return referenceLabId;
	}

	public void setResult(Boolean result) {
		this.result = result;
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
