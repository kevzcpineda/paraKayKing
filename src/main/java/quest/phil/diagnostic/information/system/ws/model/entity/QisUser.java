package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

import quest.phil.diagnostic.information.system.ws.model.classes.QisUserClass;

@Entity
@DynamicUpdate
@Table(name = "qis_users", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_name" }),
		@UniqueConstraint(columnNames = { "user_id" }), @UniqueConstraint(columnNames = { "email" }) })

public class QisUser extends QisUserClass implements Serializable {
	private static final long serialVersionUID = 8925520991302955019L;

	@ManyToMany(fetch = FetchType.EAGER) // LAZY or EAGER
	@JoinTable(name = "qis_user_roles", joinColumns = @JoinColumn(name = "qis_user_id"), inverseJoinColumns = @JoinColumn(name = "qis_role_id"))
	private Set<QisRole> roles = new HashSet<>();

	public QisUser() {
		super();
	}

	public Set<QisRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<QisRole> roles) {
		this.roles = roles;
	}
}
