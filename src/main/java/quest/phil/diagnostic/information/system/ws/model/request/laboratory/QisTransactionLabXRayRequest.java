package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabXRayRequest implements Serializable {

	private static final long serialVersionUID = -3108582327853196532L;

	@NotNull(message = "Findings is required.")
	@NotEmpty(message = "Findings is required.")
	@Size(max = 1000, message = "Findings must not exceed to {max} characters.")
	private String findings;

	@NotNull(message = "Impressions is required.")
	@NotEmpty(message = "Impressions is required.")
	@Size(max = 250, message = "Impressions must not exceed to {max} characters.")
	private String impressions;

	@NotNull(message = "Radiologist Id is required.")
	@NotEmpty(message = "Radiologist Id must not be empty.")
	@Size(max = 20, message = "Radiologist Id should not exceed {max} characters.")
	private String radiologistId;

	@NotNull(message = "Remarks value is required.")
	private Boolean remarks;

	public QisTransactionLabXRayRequest() {
		super();
	}

	public String getFindings() {
		return findings;
	}

	public void setFindings(String findings) {
		this.findings = findings;
	}

	public String getImpressions() {
		return impressions;
	}

	public void setImpressions(String impressions) {
		this.impressions = impressions;
	}

	public String getRadiologistId() {
		return radiologistId;
	}

	public void setRadiologistId(String radiologistId) {
		this.radiologistId = radiologistId;
	}

	public Boolean getRemarks() {
		return remarks;
	}

	public void setRemarks(Boolean remarks) {
		this.remarks = remarks;
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
