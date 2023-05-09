package quest.phil.diagnostic.information.system.ws.model.request.laboratory.he;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabHEPRMSRequest implements Serializable {

	private static final long serialVersionUID = -2039850076626298797L;

	@Digits(integer = 10, fraction = 4)
	private String pr131;

	private Boolean malarialSmear;
	private String referenceLabId;

	public QisTransactionLabHEPRMSRequest() {
		super();
	}

	public String getPr131() {
		return pr131;
	}

	public void setPr131(String pr131) {
		this.pr131 = pr131;
	}

	public Boolean getMalarialSmear() {
		return malarialSmear;
	}

	public void setMalarialSmear(Boolean malarialSmear) {
		this.malarialSmear = malarialSmear;
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
