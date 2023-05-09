package quest.phil.diagnostic.information.system.ws.model.classes;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class QisUserClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@NaturalId
	@Column(name = "user_id", nullable = false, length = 20, unique = true)
	private String userid;

	@NaturalId(mutable = true)
	@NotEmpty(message = "Username is required.")
	@Size(min = 4, max = 120, message = "Username must between {min} and {max} characters.")
	@Column(name = "user_name", nullable = false, length = 120, unique = true)
	private String username;

	@NaturalId(mutable = true)
	@NotEmpty(message = "Email is required.")
	@Size(max = 120, message = "Email has a maximum {max} characters.")
	@Email(message = "Input should be an email format.")
	@Column(name = "email", nullable = false, length = 120, unique = true)
	private String email;

	@NotEmpty(message = "Password is required.")
	@Size(min = 6, max = 120, message = "Password must between {min} and {max} characters.")
	@Column(name = "password", nullable = false, length = 120)
	@JsonIgnore
	private String password;

	@JsonIgnore
	@Column(name = "created_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdAt = Calendar.getInstance();

	@JsonIgnore
	@Column(name = "updated_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar updatedAt = Calendar.getInstance();

	@JsonIgnore
	@Column(name = "created_by", nullable = true)
	private Long createdBy = null;

	@JsonIgnore
	@Column(name = "updated_by", nullable = true)
	private Long updatedBy = null;

	@Column(name = "is_active", columnDefinition = "boolean default true")
	private boolean isActive = true;

	public QisUserClass() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
