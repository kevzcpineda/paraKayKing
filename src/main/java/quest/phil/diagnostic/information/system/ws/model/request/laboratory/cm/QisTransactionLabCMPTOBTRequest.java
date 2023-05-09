package quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCMPTOBTRequest implements Serializable {

	private static final long serialVersionUID = -876536658822728771L;

	private Boolean pregnancyTest;
	private Boolean occultBloodTest;
	private String referenceLabId;

	public QisTransactionLabCMPTOBTRequest() {
		super();
	}

	public Boolean getPregnancyTest() {
		return pregnancyTest;
	}

	public void setPregnancyTest(Boolean pregnancyTest) {
		this.pregnancyTest = pregnancyTest;
	}

	public Boolean getOccultBloodTest() {
		return occultBloodTest;
	}

	public void setOccultBloodTest(Boolean occultBloodTest) {
		this.occultBloodTest = occultBloodTest;
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
