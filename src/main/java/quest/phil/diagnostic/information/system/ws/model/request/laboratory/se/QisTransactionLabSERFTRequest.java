package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSERFTRequest implements Serializable {

	private static final long serialVersionUID = -820408307136018393L;

	private Boolean first;
	private Boolean second;
	private Boolean third;
	private Boolean fourth;
	private Boolean fifth;
	private String referenceLabId;
	
	public QisTransactionLabSERFTRequest() {
		super();
	}
	
	public Boolean getFirst() {
		return first;
	}
	
	public Boolean getSecond() {
		return second;
	}

	public Boolean getThird() {
		return third;
	}

	public Boolean getFourth() {
		return fourth;
	}

	public Boolean getFifth() {
		return fifth;
	}

	public String getReferenceLabId() {
		return referenceLabId;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}

	public void setSecond(Boolean second) {
		this.second = second;
	}

	public void setThird(Boolean third) {
		this.third = third;
	}

	public void setFourth(Boolean fourth) {
		this.fourth = fourth;
	}

	public void setFifth(Boolean fifth) {
		this.fifth = fifth;
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
