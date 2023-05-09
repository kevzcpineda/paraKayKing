package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DynamicUpdate
@Table(name = "qis_user_profiles")
public class QisUserProfile implements Serializable {

	private static final long serialVersionUID = 7473794448087418566L;

	@JsonIgnore
	@Id
	private Long id;

	@NotEmpty(message = "First Name is required.")
	@Size(min = 1, max = 120, message = "First Name must between {min} and {max} characters.")
	@Column(name = "firstname", nullable = false, length = 120)
	private String firstname;

	@NotEmpty(message = "Last Name is required.")
	@Size(min = 1, max = 120, message = "Last Name must between {min} and {max} characters.")
	@Column(name = "lastname", nullable = false, length = 120)
	private String lastname;

	@Size(max = 120, message = "Middle Name must not exceed to {max} characters.")
	@Column(name = "middlename", nullable = true, length = 120)
	private String middlename;

	@Size(max = 20, message = "License Number must not exceed to {max} characters.")
	@Column(name = "suffix", nullable = true, length = 20, columnDefinition = "varchar(20) default 'MD'")
	private String suffix;

	@Size(max = 20, message = "License Number must not exceed to {max} characters.")
	@Column(name = "license_number", nullable = true, length = 20, unique = true)
	private String licenseNumber;

	@JsonIgnore
	@Column(name = "date_of_birth", nullable = false, columnDefinition = "DATE")
	@Temporal(TemporalType.DATE)
	private Calendar dateOfBirth;
	@Transient
	private String birthDate;

	// 1-male 0-female
	@NotNull(message = "Gender is required.")
	@Column(name = "gender", length = 1, columnDefinition = "varchar(1) default 'M'")
	private String gender;

	@NotEmpty(message = "Contact is required.")
	@Size(min = 1, max = 20, message = "Contact Number must between {min} and {max} characters.")
	private String contactNumber;

	@NotEmpty(message = "Email is required.")
	@Size(min = 1, max = 120, message = "Email must between {min} and {max} characters.")
	@Email(message = "Input should be an email format.")
	private String email;

	@NotEmpty(message = "Address is required.")
	@Size(max = 250, message = "Address must not exceed to {max} characters.")
	private String address;

	@Lob
	@Column(name = "signature", nullable = true, columnDefinition = "BLOB DEFAULT NULL")
	protected byte[] signature;
	
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

	public QisUserProfile() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		this.birthDate = formatDateOfBirth(dateOfBirth);
	}

	public String getBirthDate() {
		if (birthDate == null) {
			birthDate = formatDateOfBirth(dateOfBirth);
		}
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
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
	
	private String formatDateOfBirth(Calendar db) {
		if (db != null) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				return format.format(db.getTime());
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return null;
	}	
}
