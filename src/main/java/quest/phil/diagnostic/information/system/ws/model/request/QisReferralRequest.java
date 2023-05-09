package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisReferralRequest implements Serializable {

	private static final long serialVersionUID = 2667866622627199709L;

	@NotEmpty(message = "Referral is required.")
	@Size(min = 1, max = 120, message = "Referral must between {min} and {max} characters.")
	private String referral;

	private Boolean isActive;

	public QisReferralRequest() {
		super();
	}

	public String getReferral() {
		return referral;
	}

	public void setReferral(String referral) {
		this.referral = referral;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
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
