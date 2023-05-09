package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import quest.phil.diagnostic.information.system.ws.model.QisRoleName;
import quest.phil.diagnostic.information.system.ws.model.classes.QisRoleClass;

@Entity
@DynamicUpdate
@Table(name = "qis_roles")
public class QisRole extends QisRoleClass implements Serializable {
	private static final long serialVersionUID = -2995397095889135779L;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@NotBlank(message = "Rolename is required.")
	@Size(max = 40, message = "Rolename has a maximum of {max} characters.")
	@Column(length = 40)
	private QisRoleName name;

	@Transient
	private String inputName;
	

	public QisRole() {
		super();
	}

	public QisRole(
			@NotBlank(message = "Rolename is required.") @Size(max = 40, message = "Rolename has a maximum of {max} characters.") QisRoleName name) {
		super();
		this.name = name;
	}

	public QisRoleName getName() {
		return name;
	}

	public void setName(QisRoleName name) {
		this.name = name;
	}
	
	public String getInputName() {
		if (inputName == null) {
			String strRole = name.name();
			if (strRole != null && strRole.startsWith("ROLE_")) {
				inputName = strRole.substring(5).toLowerCase();
			}
		}

		return inputName;
	}	
}
