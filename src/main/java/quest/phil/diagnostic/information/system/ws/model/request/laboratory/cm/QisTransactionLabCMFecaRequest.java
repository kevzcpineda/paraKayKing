package quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm;

import java.io.Serializable;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCMFecaRequest implements Serializable {

	private static final long serialVersionUID = 3575590432784190471L;

	@Size(max = 4, message = "Color should not exceed {max} characters.")
	private String color;

	@Size(max = 4, message = "Consistency should not exceed {max} characters.")
	private String consistency;

	@Size(max = 120, message = "Microscopic Findings should not exceed {max} characters.")
	private String microscopicFindings;

	@Size(max = 200, message = "Other Notes should not exceed {max} characters.")
	private String otherNotes;
	
	private String referenceLabId;

	public QisTransactionLabCMFecaRequest() {
		super();
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getConsistency() {
		return consistency;
	}

	public void setConsistency(String consistency) {
		this.consistency = consistency;
	}

	public String getMicroscopicFindings() {
		return microscopicFindings;
	}

	public void setMicroscopicFindings(String microscopicFindings) {
		this.microscopicFindings = microscopicFindings;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
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
