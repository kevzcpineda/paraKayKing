package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHOGTTRequest implements Serializable {

	private static final long serialVersionUID = -2868390265785070999L;

	@Digits(integer = 10, fraction = 4)
	private String ogtt1Hr;

	@Digits(integer = 10, fraction = 4)
	private String ogtt1HrConventional;

	@Digits(integer = 10, fraction = 4)
	private String ogtt2Hr;

	@Digits(integer = 10, fraction = 4)
	private String ogtt2HrConventional;
	
	private String referenceLabId;

	public QisTransactionLabCHOGTTRequest() {
		super();
	}

	public String getOgtt1Hr() {
		return ogtt1Hr;
	}

	public void setOgtt1Hr(String ogtt1Hr) {
		this.ogtt1Hr = ogtt1Hr;
	}

	public String getOgtt1HrConventional() {
		return ogtt1HrConventional;
	}

	public void setOgtt1HrConventional(String ogtt1HrConventional) {
		this.ogtt1HrConventional = ogtt1HrConventional;
	}

	public String getOgtt2Hr() {
		return ogtt2Hr;
	}

	public void setOgtt2Hr(String ogtt2Hr) {
		this.ogtt2Hr = ogtt2Hr;
	}

	public String getOgtt2HrConventional() {
		return ogtt2HrConventional;
	}

	public void setOgtt2HrConventional(String ogtt2HrConventional) {
		this.ogtt2HrConventional = ogtt2HrConventional;
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
