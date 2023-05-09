package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class QisTransactionLabRequirementRequest implements Serializable {

	private static final long serialVersionUID = 4986773147990466096L;

	@NotEmpty(message = "Laboratory Requirements should not be empty.")
	@Valid
	private Set<QisLaboratoryRequirementRequest> submitRequirements;

	public QisTransactionLabRequirementRequest() {
		super();
	}

	public Set<QisLaboratoryRequirementRequest> getSubmitRequirements() {
		return submitRequirements;
	}

	public void setSubmitRequirements(Set<QisLaboratoryRequirementRequest> submitRequirements) {
		this.submitRequirements = submitRequirements;
	}	
}
