package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QisLaboratoryRequirementRequest implements Serializable {

	private static final long serialVersionUID = -7476936795118679849L;
	
	@NotNull(message = "Item Laboratory Id is required.")
	private Long id;
	
	@NotEmpty(message = "Item Laboratory is required.")
	@Size(min = 1, max = 12, message = "Item Laboratory must between {min} and {max} characters.")
	private String itemLaboratory;

	@NotNull(message = "Submitted is required.")
	private Boolean isSubmitted;
	
	public QisLaboratoryRequirementRequest() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemLaboratory() {
		return itemLaboratory;
	}

	public void setItemLaboratory(String itemLaboratory) {
		this.itemLaboratory = itemLaboratory;
	}

	public Boolean getIsSubmitted() {
		return isSubmitted;
	}

	public void setIsSubmitted(Boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}	
}
