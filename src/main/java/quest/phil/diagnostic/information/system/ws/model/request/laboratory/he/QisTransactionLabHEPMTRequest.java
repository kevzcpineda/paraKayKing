package quest.phil.diagnostic.information.system.ws.model.request.laboratory.he;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabHEPMTRequest implements Serializable {

	private static final long serialVersionUID = 1572073324641141764L;

	@Digits(integer = 10, fraction = 4)
	private String patientTime;

	@Size(max = 20, message = "Patient Time Normal Value should not exceed {max} characters.")
	private String patientTimeNV;

	@Digits(integer = 10, fraction = 4)
	private String control;

	@Size(max = 20, message = "Control Normal Value should not exceed {max} characters.")
	private String controlNV;

	@Digits(integer = 10, fraction = 4)
	private String percentActivity;

	@Size(max = 20, message = "Percent Activity Normal Value should not exceed {max} characters.")
	private String percentActivityNV;

	@Digits(integer = 10, fraction = 4)
	private String inr;

	@Size(max = 20, message = "INR Normal Value should not exceed {max} characters.")
	private String inrNV;
	
	private String referenceLabId;

	public QisTransactionLabHEPMTRequest() {
		super();
	}

	public String getPatientTime() {
		return patientTime;
	}

	public void setPatientTime(String patientTime) {
		this.patientTime = patientTime;
	}

	public String getPatientTimeNV() {
		return patientTimeNV;
	}

	public void setPatientTimeNV(String patientTimeNV) {
		this.patientTimeNV = patientTimeNV;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getControlNV() {
		return controlNV;
	}

	public void setControlNV(String controlNV) {
		this.controlNV = controlNV;
	}

	public String getPercentActivity() {
		return percentActivity;
	}

	public void setPercentActivity(String percentActivity) {
		this.percentActivity = percentActivity;
	}

	public String getPercentActivityNV() {
		return percentActivityNV;
	}

	public void setPercentActivityNV(String percentActivityNV) {
		this.percentActivityNV = percentActivityNV;
	}

	public String getInr() {
		return inr;
	}

	public void setInr(String inr) {
		this.inr = inr;
	}

	public String getInrNV() {
		return inrNV;
	}

	public void setInrNV(String inrNV) {
		this.inrNV = inrNV;
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
