package quest.phil.diagnostic.information.system.ws.model.request.laboratory.mb;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabGSRequest implements Serializable {

	private static final long serialVersionUID = 9191955932627749312L;

	private String specimen;
	private String result;
	private String referenceLabId;

	public QisTransactionLabGSRequest() {
		super();
	}

	public String getReferenceLabId() {
		return referenceLabId;
	}

	public void setReferenceLabId(String referenceLabId) {
		this.referenceLabId = referenceLabId;
	}

	public String getSpecimen() {
		return specimen;
	}

	public String getResult() {
		return result;
	}

	public void setSpecimen(String specimen) {
		this.specimen = specimen;
	}

	public void setResult(String result) {
		this.result = result;
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
