package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSEHIVRequest implements Serializable {

	private static final long serialVersionUID = -5527612121517876119L;

	private Boolean test1;
	private Boolean test2;
	private String referenceLabId;

	public QisTransactionLabSEHIVRequest() {
		super();
	}

	public Boolean getTest1() {
		return test1;
	}

	public void setTest1(Boolean test1) {
		this.test1 = test1;
	}

	public Boolean getTest2() {
		return test2;
	}

	public void setTest2(Boolean test2) {
		this.test2 = test2;
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
