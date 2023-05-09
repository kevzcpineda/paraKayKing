package quest.phil.diagnostic.information.system.ws.model.request.laboratory.he;

import java.io.Serializable;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabHEBTypRequest implements Serializable {

	private static final long serialVersionUID = 336465841702171647L;

	@Size(max = 4, message = "Blood Type should not exceed {max} characters.")
	private String bloodType;

	private Boolean rhesusFactor;

	private String referenceLabId;
	
	public QisTransactionLabHEBTypRequest() {
		super();
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public Boolean getRhesusFactor() {
		return rhesusFactor;
	}

	public void setRhesusFactor(Boolean rhesusFactor) {
		this.rhesusFactor = rhesusFactor;
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
