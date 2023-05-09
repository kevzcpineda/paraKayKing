package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSEAntigenRequest implements Serializable {

	private static final long serialVersionUID = 4310957198839973941L;

	@Digits(integer = 10, fraction = 4)
	private String psa;
	
	@Digits(integer = 10, fraction = 4)
	private String cea;

	@Digits(integer = 10, fraction = 4)
	private String afp;

	@Digits(integer = 10, fraction = 4)
	private String ca125;

	@Digits(integer = 10, fraction = 4)
	private String ca199;

	@Digits(integer = 10, fraction = 4)
	private String ca153;
	
	private String referenceLabId;
	
	
	public QisTransactionLabSEAntigenRequest() {
		super();
	}

	public String getPsa() {
		return psa;
	}

	public void setPsa(String psa) {
		this.psa = psa;
	}
	
	public String getCea() {
		return cea;
	}

	public void setCea(String cea) {
		this.cea = cea;
	}

	public String getAfp() {
		return afp;
	}

	public void setAfp(String afp) {
		this.afp = afp;
	}

	public String getCa125() {
		return ca125;
	}

	public void setCa125(String ca125) {
		this.ca125 = ca125;
	}

	public String getCa199() {
		return ca199;
	}

	public void setCa199(String ca199) {
		this.ca199 = ca199;
	}

	public String getCa153() {
		return ca153;
	}

	public void setCa153(String ca153) {
		this.ca153 = ca153;
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
