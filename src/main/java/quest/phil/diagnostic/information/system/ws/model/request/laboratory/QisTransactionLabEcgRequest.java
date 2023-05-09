package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabEcgRequest implements Serializable{

	private static final long serialVersionUID = -2161571080950201511L;

	@Size(max = 300, message = "Rhythm must not exceed to {max} characters.")
	@Column(name = "Rhythm", nullable = false, length = 300)
	private String rhythm;
	
	@Size(max = 300, message = "PR Interval must not exceed to {max} characters.")
	@Column(name = "PR_Interval", nullable = false, length = 300)
	private String pr_interval;
	
	@Size(max = 300, message = "Rate Atrial must not exceed to {max} characters.")
	@Column(name = "Rate_Atrial", nullable = false, length = 300)
	private String rate_atrial;
	
	@Size(max = 300, message = "Axis must not exceed to {max} characters.")
	@Column(name = "Axis", nullable = false, length = 300)
	private String axis;
	
	@Size(max = 300, message = "P-Wave must not exceed to {max} characters.")
	@Column(name = "P-Wave", nullable = false, length = 300)
	private String p_wave;
	
	@Size(max = 300, message = "Ventricular must not exceed to {max} characters.")
	@Column(name = "Ventricular", nullable = false, length = 300)
	private String ventricular;
	
	@Size(max = 300, message = "INTERPRETATION must not exceed to {max} characters.")
	@Column(name = "INTERPRETATION", nullable = false, length = 300)
	private String interpretation;
	
	public QisTransactionLabEcgRequest() {
		super();
	}
	
	
	public String getRhythm() {
		return rhythm;
	}


	public String getPr_interval() {
		return pr_interval;
	}


	public String getRate_atrial() {
		return rate_atrial;
	}


	public String getAxis() {
		return axis;
	}


	public String getP_wave() {
		return p_wave;
	}


	public String getVentricular() {
		return ventricular;
	}


	public String getInterpretation() {
		return interpretation;
	}


	public void setRhythm(String rhythm) {
		this.rhythm = rhythm;
	}


	public void setPr_interval(String pr_interval) {
		this.pr_interval = pr_interval;
	}


	public void setRate_atrial(String rate_atrial) {
		this.rate_atrial = rate_atrial;
	}


	public void setAxis(String axis) {
		this.axis = axis;
	}


	public void setP_wave(String p_wave) {
		this.p_wave = p_wave;
	}


	public void setVentricular(String ventricular) {
		this.ventricular = ventricular;
	}


	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
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
