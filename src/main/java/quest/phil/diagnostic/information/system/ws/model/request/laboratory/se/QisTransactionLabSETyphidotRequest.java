package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSETyphidotRequest implements Serializable {

	private static final long serialVersionUID = -6453360958116275305L;

	private Boolean igm;
	
	private Boolean igg;

	private String referenceLabId;
	
	public QisTransactionLabSETyphidotRequest() {
		super();
	}

	public Boolean getIgm() {
		return igm;
	}

	public void setIgm(Boolean igm) {
		this.igm = igm;
	}

	public Boolean getIgg() {
		return igg;
	}

	public void setIgg(Boolean igg) {
		this.igg = igg;
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
