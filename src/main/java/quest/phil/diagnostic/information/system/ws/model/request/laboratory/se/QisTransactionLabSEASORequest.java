package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSEASORequest implements Serializable {

	private static final long serialVersionUID = 5375414709009197530L;

	private Boolean result1;
	private Boolean result2;
	private Boolean result3;
	private Boolean result4;
	private Boolean result5;
	private String referenceLabId;
	
	public QisTransactionLabSEASORequest() {
		super();
	}
	public Boolean getResult1() {
		return result1;
	}

	public Boolean getResult2() {
		return result2;
	}

	public Boolean getResult3() {
		return result3;
	}

	public Boolean getResult4() {
		return result4;
	}

	public Boolean getResult5() {
		return result5;
	}

	public String getReferenceLabId() {
		return referenceLabId;
	}

	public void setResult1(Boolean result1) {
		this.result1 = result1;
	}

	public void setResult2(Boolean result2) {
		this.result2 = result2;
	}

	public void setResult3(Boolean result3) {
		this.result3 = result3;
	}

	public void setResult4(Boolean result4) {
		this.result4 = result4;
	}

	public void setResult5(Boolean result5) {
		this.result5 = result5;
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
