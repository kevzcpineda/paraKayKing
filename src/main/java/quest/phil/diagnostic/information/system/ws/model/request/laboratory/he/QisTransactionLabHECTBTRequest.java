package quest.phil.diagnostic.information.system.ws.model.request.laboratory.he;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabHECTBTRequest implements Serializable {

	private static final long serialVersionUID = 147860314297289816L;

	@Digits(integer = 2, fraction = 0)
	private String clottingTimeMin;

	@Digits(integer = 2, fraction = 0)
	private String clottingTimeSec;

	@Digits(integer = 2, fraction = 0)
	private String bleedingTimeMin;

	@Digits(integer = 2, fraction = 0)
	private String bleedingTimeSec;
	
	private String referenceLabId;

	public QisTransactionLabHECTBTRequest() {
		super();
	}

	public String getClottingTimeMin() {
		return clottingTimeMin;
	}

	public void setClottingTimeMin(String clottingTimeMin) {
		this.clottingTimeMin = clottingTimeMin;
	}

	public String getClottingTimeSec() {
		return clottingTimeSec;
	}

	public void setClottingTimeSec(String clottingTimeSec) {
		this.clottingTimeSec = clottingTimeSec;
	}

	public String getBleedingTimeMin() {
		return bleedingTimeMin;
	}

	public void setBleedingTimeMin(String bleedingTimeMin) {
		this.bleedingTimeMin = bleedingTimeMin;
	}

	public String getBleedingTimeSec() {
		return bleedingTimeSec;
	}

	public void setBleedingTimeSec(String bleedingTimeSec) {
		this.bleedingTimeSec = bleedingTimeSec;
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
