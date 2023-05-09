package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSEThyroidRequest implements Serializable {

	private static final long serialVersionUID = -9028647457334244771L;

	@Digits(integer = 10, fraction = 4)
	private String tsh;

	@Digits(integer = 10, fraction = 4)
	private String ft3;

	@Digits(integer = 10, fraction = 4)
	private String ft4;

	@Digits(integer = 10, fraction = 4)
	private String t3;

	@Digits(integer = 10, fraction = 4)
	private String t4;

	private String referenceLabId;
	
	public QisTransactionLabSEThyroidRequest() {
		super();
	}

	public String getTsh() {
		return tsh;
	}

	public void setTsh(String tsh) {
		this.tsh = tsh;
	}

	public String getFt3() {
		return ft3;
	}

	public void setFt3(String ft3) {
		this.ft3 = ft3;
	}

	public String getFt4() {
		return ft4;
	}

	public void setFt4(String ft4) {
		this.ft4 = ft4;
	}

	public String getT3() {
		return t3;
	}

	public void setT3(String t3) {
		this.t3 = t3;
	}

	public String getT4() {
		return t4;
	}

	public void setT4(String t4) {
		this.t4 = t4;
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
