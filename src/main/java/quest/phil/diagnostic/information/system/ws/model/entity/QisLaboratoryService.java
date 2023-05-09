package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import quest.phil.diagnostic.information.system.ws.model.classes.QisLaboratoryProcedureClass;

@Entity
@DynamicUpdate
@Table(name = "qis_laboratory_services")
public class QisLaboratoryService extends QisLaboratoryProcedureClass implements Serializable {

	private static final long serialVersionUID = -6747046373058907313L;

	@NotBlank(message = "Service Name is required.")
	@Size(max = 20, message = "Service Name has a maximum of {max} characters.")
	@Column(length = 20)
	private String laboratoryRequest;
	
	@NotBlank(message = "Procedure Name is required.")
	@Size(max = 4, message = "Procedure Name has a maximum of {max} characters.")
	@Column(length = 4)
	private String laboratoryProcedure;
	
	public QisLaboratoryService() {
		super();
	}
	
	public String getLaboratoryRequest() {
		return laboratoryRequest;
	}

	public void setLaboratoryRequest(String laboratoryRequest) {
		this.laboratoryRequest = laboratoryRequest;
	}

	public String getLaboratoryProcedure() {
		return laboratoryProcedure;
	}

	public void setLaboratoryProcedure(String laboratoryProcedure) {
		this.laboratoryProcedure = laboratoryProcedure;
	}	
}
