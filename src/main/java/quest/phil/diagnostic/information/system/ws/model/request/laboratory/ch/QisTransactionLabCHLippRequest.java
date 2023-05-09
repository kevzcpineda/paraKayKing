package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHLippRequest implements Serializable {

	private static final long serialVersionUID = 2283549822502911110L;

	@Digits(integer = 10, fraction = 4)
	private String cholesterol;

	@Digits(integer = 10, fraction = 4)
	private String cholesterolConventional;

	@Digits(integer = 10, fraction = 4)
	private String triglycerides;

	@Digits(integer = 10, fraction = 4)
	private String triglyceridesConventional;

	@Digits(integer = 10, fraction = 4)
	private String hdl;

	@Digits(integer = 10, fraction = 4)
	private String hdlConventional;

	@Digits(integer = 10, fraction = 4)
	private String ldl;

	@Digits(integer = 10, fraction = 4)
	private String ldlConventional;

	@Digits(integer = 10, fraction = 4)
	private String hdlRatio;

	@Digits(integer = 10, fraction = 4)
	private String vldl;
	
	private String referenceLabId;

	public QisTransactionLabCHLippRequest() {
		super();
	}

	public String getCholesterol() {
		return cholesterol;
	}

	public void setCholesterol(String cholesterol) {
		this.cholesterol = cholesterol;
	}

	public String getCholesterolConventional() {
		return cholesterolConventional;
	}

	public void setCholesterolConventional(String cholesterolConventional) {
		this.cholesterolConventional = cholesterolConventional;
	}

	public String getTriglycerides() {
		return triglycerides;
	}

	public void setTriglycerides(String triglycerides) {
		this.triglycerides = triglycerides;
	}

	public String getTriglyceridesConventional() {
		return triglyceridesConventional;
	}

	public void setTriglyceridesConventional(String triglyceridesConventional) {
		this.triglyceridesConventional = triglyceridesConventional;
	}

	public String getHdl() {
		return hdl;
	}

	public void setHdl(String hdl) {
		this.hdl = hdl;
	}

	public String getHdlConventional() {
		return hdlConventional;
	}

	public void setHdlConventional(String hdlConventional) {
		this.hdlConventional = hdlConventional;
	}

	public String getLdl() {
		return ldl;
	}

	public void setLdl(String ldl) {
		this.ldl = ldl;
	}

	public String getLdlConventional() {
		return ldlConventional;
	}

	public void setLdlConventional(String ldlConventional) {
		this.ldlConventional = ldlConventional;
	}

	public String getHdlRatio() {
		return hdlRatio;
	}

	public void setHdlRatio(String hdlRatio) {
		this.hdlRatio = hdlRatio;
	}

	public String getVldl() {
		return vldl;
	}

	public void setVldl(String vldl) {
		this.vldl = vldl;
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
