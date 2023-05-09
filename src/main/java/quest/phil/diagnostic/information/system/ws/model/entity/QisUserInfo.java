package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.classes.QisUserClass;

@Entity
@DynamicUpdate
@Table(name = "qis_users", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_name" }),
		@UniqueConstraint(columnNames = { "user_id" }), @UniqueConstraint(columnNames = { "email" }) })
public class QisUserInfo extends QisUserClass implements Serializable {

	private static final long serialVersionUID = -4130875760156046846L;

	@ManyToMany(fetch = FetchType.EAGER) // LAZY or EAGER
	@JoinTable(name = "qis_user_roles", joinColumns = @JoinColumn(name = "qis_user_id"), inverseJoinColumns = @JoinColumn(name = "qis_role_id"))
	private Set<QisRole> roles = new HashSet<>();
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserProfile profile;
	
	public QisUserInfo() {
		super();
	}

	public Set<QisRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<QisRole> roles) {
		this.roles = roles;
	}

	public QisUserProfile getProfile() {
		return profile;
	}

	public void setProfile(QisUserProfile profile) {
		this.profile = profile;
	}
}
