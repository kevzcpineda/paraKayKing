package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSERTPCRRequest implements Serializable  {

	private static final long serialVersionUID = 1823084266033018612L;

	private Boolean rtpcrResult;
	private String collectionDate;
	private String purpose;
	private String realeasingDate;
	private String referenceLabId;
	
	public QisTransactionLabSERTPCRRequest() {
		super();
	}

	public Boolean getRtpcrResult() {
		return rtpcrResult;
	}

	public String getCollectionDate() {
		return collectionDate;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setRtpcrResult(Boolean rtpcrResult) {
		this.rtpcrResult = rtpcrResult;
	}

	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	public String getRealeasingDate() {
		return realeasingDate;
	}

	public void setRealeasingDate(String realeasingDate) {
		this.realeasingDate = realeasingDate;
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
