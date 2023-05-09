package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransationLabCHTIBCRequest {

	private Double result;
	
	private String referenceLabId;
	
	public QisTransationLabCHTIBCRequest() {
		super();
	}
	
	public Double getResult() {
		return result;
	}

	public String getReferenceLabId() {
		return referenceLabId;
	}

	public void setResult(Double result) {
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
