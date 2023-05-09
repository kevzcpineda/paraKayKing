package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHBiliRequest implements Serializable {

	private static final long serialVersionUID = -2821354919867280763L;

	@Digits(integer = 10, fraction = 4)
	private String totalAdult;

	@Digits(integer = 10, fraction = 4)
	private String direct;

	@Digits(integer = 10, fraction = 4)
	private String indirect;
	
	private String referenceLabId;

	public QisTransactionLabCHBiliRequest() {
		super();
	}

	public String getTotalAdult() {
		return totalAdult;
	}

	public void setTotalAdult(String totalAdult) {
		this.totalAdult = totalAdult;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

	public String getIndirect() {
		return indirect;
	}

	public void setIndirect(String indirect) {
		this.indirect = indirect;
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
