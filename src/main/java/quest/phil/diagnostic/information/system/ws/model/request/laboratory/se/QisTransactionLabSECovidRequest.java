package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSECovidRequest implements Serializable {

	private static final long serialVersionUID = -3426971853670018773L;

	private Boolean covigm;

	private Boolean covigg;
	
	private String purpose;
	
	private String referenceLabId;

	public QisTransactionLabSECovidRequest() {
		super();
	}

	public Boolean getCovigm() {
		return covigm;
	}

	public void setCovigm(Boolean covigm) {
		this.covigm = covigm;
	}

	public Boolean getCovigg() {
		return covigg;
	}

	public void setCovigg(Boolean covigg) {
		this.covigg = covigg;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
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
