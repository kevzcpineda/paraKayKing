package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QisTransactionLabClassificationRequest implements Serializable {
	private static final long serialVersionUID = 4986773147990466096L;

	@NotNull(message = "Classification is required.")
	@NotEmpty(message = "Classification must not be empty.")
	@Size(min = 1, max = 4, message = "Classification must between {min} and {max} characters.")
	private String classification;
	
	@Size(max = 250, message = "Over All Findings must not exceed to {max} characters.")
	private String overAllFindings;

	@NotNull(message = "Doctor Id is required.")
	@NotEmpty(message = "Doctor Id must not be empty.")
	@Size(max = 20, message = "Doctor Id should not exceed {max} characters.")
	private String doctorId;
	
	public QisTransactionLabClassificationRequest() {
		super();
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getOverAllFindings() {
		return overAllFindings;
	}

	public void setOverAllFindings(String overAllFindings) {
		this.overAllFindings = overAllFindings;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
}
