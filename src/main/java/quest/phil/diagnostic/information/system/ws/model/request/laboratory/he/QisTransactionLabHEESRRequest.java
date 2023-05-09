package quest.phil.diagnostic.information.system.ws.model.request.laboratory.he;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabHEESRRequest implements Serializable {

	private static final long serialVersionUID = -4576094121826547516L;

	@Digits(integer = 10, fraction = 4)
	private String rate;

	@Size(max = 20, message = "Method should not exceed {max} characters.")
	private String method;
	
	private String referenceLabId;

	public QisTransactionLabHEESRRequest() {
		super();
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
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
