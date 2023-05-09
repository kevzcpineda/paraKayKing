package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSERtAtigenRequest implements Serializable {

	private static final long serialVersionUID = -6481728250464931325L;
	
	private Boolean cov_ag;
	private String collectionDate;
	private String purpose;
	private String referenceLabId;
	
	public QisTransactionLabSERtAtigenRequest() {
		super();
	}
		
	public Boolean getCov_ag() {
		return cov_ag;
	}

	public String getCollectionDate() {
		return collectionDate;
	}

	public void setCov_ag(Boolean cov_ag) {
		this.cov_ag = cov_ag;
	}

	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
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
