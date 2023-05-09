package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
public class QisUserPersonel extends QisUserClass implements Serializable {

	private static final long serialVersionUID = -3499673586006368655L;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserProfile profile;

	public QisUserPersonel() {
		super();
	}

	public QisUserProfile getProfile() {
		return profile;
	}

	public void setProfile(QisUserProfile profile) {
		this.profile = profile;
	}
}
