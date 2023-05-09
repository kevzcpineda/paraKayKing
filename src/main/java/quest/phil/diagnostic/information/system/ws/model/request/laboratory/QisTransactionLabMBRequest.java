package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.request.laboratory.mb.QisTransactionLabGSRequest;

public class QisTransactionLabMBRequest implements Serializable  {

	private static final long serialVersionUID = -3338456797387229666L;

	@Valid
	private QisTransactionLabGSRequest gs;
	
	@NotNull(message = "Pathologist Id is required.")
	@NotEmpty(message = "Pathologist Id must not be empty.")
	@Size(max = 20, message = "Pathologist Id should not exceed {max} characters.")
	private String pathologistId;
	
	public QisTransactionLabMBRequest() {
		super();
	}
	public QisTransactionLabGSRequest getGs() {
		return gs;
	}
	public void setGs(QisTransactionLabGSRequest gs) {
		this.gs = gs;
	}
	public String getPathologistId() {
		return pathologistId;
	}

	public void setPathologistId(String pathologistId) {
		this.pathologistId = pathologistId;
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
