package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import quest.phil.diagnostic.information.system.ws.model.QisLaboratoryProcedure;
import quest.phil.diagnostic.information.system.ws.model.QisLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.classes.QisLaboratoryProcedureClass;

@Entity
@DynamicUpdate
@Table(name = "qis_laboratory_services")
public class QisLaboratoryProcedureService extends QisLaboratoryProcedureClass implements Serializable {

	private static final long serialVersionUID = 3770611446487284319L;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@NotBlank(message = "Service Name is required.")
	@Size(max = 20, message = "Service Name has a maximum of {max} characters.")
	@Column(length = 20)
	private QisLaboratoryRequest laboratoryRequest;
	
	@Enumerated(EnumType.STRING)
	@NotBlank(message = "Procedure Name is required.")
	@Size(max = 4, message = "Procedure Name has a maximum of {max} characters.")
	@Column(length = 4)
	private QisLaboratoryProcedure laboratoryProcedure;
	
	
	public QisLaboratoryProcedureService() {
		super();
	}

	public QisLaboratoryRequest getLaboratoryRequest() {
		return laboratoryRequest;
	}

	public void setLaboratoryRequest(QisLaboratoryRequest laboratoryRequest) {
		this.laboratoryRequest = laboratoryRequest;
	}

	public QisLaboratoryProcedure getLaboratoryProcedure() {
		return laboratoryProcedure;
	}

	public void setLaboratoryProcedure(QisLaboratoryProcedure laboratoryProcedure) {
		this.laboratoryProcedure = laboratoryProcedure;
	}
	
	public String getRequestName() {
		return laboratoryRequest.getServiceRequest(); 
	}
}
