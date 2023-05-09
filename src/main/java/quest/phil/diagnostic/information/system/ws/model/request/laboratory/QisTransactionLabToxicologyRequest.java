package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabToxicologyRequest implements Serializable {

	private static final long serialVersionUID = 8855105759451682686L;
	private Boolean methamphethamine;
	private Boolean tetrahydrocanabinol;
	

	@NotNull(message = "Pathologist Id is required.")
	@NotEmpty(message = "Pathologist Id must not be empty.")
	@Size(max = 20, message = "Pathologist Id should not exceed {max} characters.")
	private String pathologistId;
	
	private String referenceLabId;
	
	public QisTransactionLabToxicologyRequest() {
		super();
	}

	public Boolean getMethamphethamine() {
		return methamphethamine;
	}

	public void setMethamphethamine(Boolean methamphethamine) {
		this.methamphethamine = methamphethamine;
	}

	public Boolean getTetrahydrocanabinol() {
		return tetrahydrocanabinol;
	}

	public void setTetrahydrocanabinol(Boolean tetrahydrocanabinol) {
		this.tetrahydrocanabinol = tetrahydrocanabinol;
	}

	public String getPathologistId() {
		return pathologistId;
	}

	public void setPathologistId(String pathologistId) {
		this.pathologistId = pathologistId;
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
