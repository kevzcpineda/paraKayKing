package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabINDRequest implements Serializable {

	private static final long serialVersionUID = -6146322761444964904L;

	@NotEmpty(message = "Transaction Item Laboratories should not be empty.")
	@Valid
	private Set<QisIndustrialRequirementRequest> transactionItemLaboratories;
	
	@NotNull(message = "Pathologist Id is required.")
	@NotEmpty(message = "Pathologist Id must not be empty.")
	@Size(max = 20, message = "Pathologist Id should not exceed {max} characters.")
	private String pathologistId;

	public QisTransactionLabINDRequest() {
		super();
	}

	public Set<QisIndustrialRequirementRequest> getTransactionItemLaboratories() {
		return transactionItemLaboratories;
	}

	public void setTransactionItemLaboratories(Set<QisIndustrialRequirementRequest> transactionItemLaboratories) {
		this.transactionItemLaboratories = transactionItemLaboratories;
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
