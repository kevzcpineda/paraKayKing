package quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm;

import java.io.Serializable;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCMAFBRequest implements Serializable {

	private static final long serialVersionUID = 7447089403198709528L;

	@Size(max = 20, message = "Visual Appearance Specimen 1 should not exceed {max} characters.")
	private String visualAppearance1;

	@Size(max = 20, message = "Visual Appearance Specimen 2 should not exceed {max} characters.")
	private String visualAppearance2;

	@Size(max = 20, message = "Reading Specimen 1 should not exceed {max} characters.")
	private String reading1;

	@Size(max = 20, message = "Reading Specimen 2 should not exceed {max} characters.")
	private String reading2;

	@Size(max = 200, message = "Diagnosis should not exceed {max} characters.")
	private String diagnosis;
	
	private String referenceLabId;
	
	public QisTransactionLabCMAFBRequest() {
		super();
	}

	public String getVisualAppearance1() {
		return visualAppearance1;
	}

	public void setVisualAppearance1(String visualAppearance1) {
		this.visualAppearance1 = visualAppearance1;
	}

	public String getVisualAppearance2() {
		return visualAppearance2;
	}

	public void setVisualAppearance2(String visualAppearance2) {
		this.visualAppearance2 = visualAppearance2;
	}

	public String getReading1() {
		return reading1;
	}

	public void setReading1(String reading1) {
		this.reading1 = reading1;
	}

	public String getReading2() {
		return reading2;
	}

	public void setReading2(String reading2) {
		this.reading2 = reading2;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
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
