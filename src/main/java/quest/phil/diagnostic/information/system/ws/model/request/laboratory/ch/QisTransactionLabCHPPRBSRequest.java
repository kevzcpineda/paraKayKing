package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHPPRBSRequest implements Serializable {

	private static final long serialVersionUID = 7126734926271282967L;

	@Digits(integer = 10, fraction = 4)
	private String pprbs;

	@Digits(integer = 10, fraction = 4)
	private String conventional;

	private String referenceLabId;
	
	public QisTransactionLabCHPPRBSRequest() {
		super();
	}

	public String getPprbs() {
		return pprbs;
	}

	public void setPprbs(String pprbs) {
		this.pprbs = pprbs;
	}

	public String getConventional() {
		return conventional;
	}

	public void setConventional(String conventional) {
		this.conventional = conventional;
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
