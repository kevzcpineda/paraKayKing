package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DynamicUpdate
@Table(name = "qis_doctors", uniqueConstraints = { @UniqueConstraint(columnNames = { "doctor_id" }),
		@UniqueConstraint(columnNames = { "license_number" }) })
public class QisDoctor implements Serializable {
	private static final long serialVersionUID = 151213836013592101L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@NaturalId
	@Column(name = "doctor_id", nullable = false, length = 20, unique = true)
	private String doctorid;

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

	@Size(max = 250, message = "Speciallization must not exceed to {max} characters.")
	@Column(name = "specialization", nullable = true, length = 250)
	private String specialization;

	@Size(max = 120, message = "Contact Number must not exceed to {max} characters.")
	@Column(name = "contact_number", nullable = true, length = 120)
	private String contactNumber;

	@Size(max = 120, message = "Email must not exceed to {max} characters.")
	@Column(name = "email", nullable = true, length = 120)
	@Email(message = "Input should be an email format.")
	private String email;

	@NotEmpty(message = "License Number is required.")
	@Size(min = 1, max = 20, message = "License Number must between {min} and {max} characters.")
	@Column(name = "license_number", nullable = false, length = 20, unique = true)
	private String licenseNumber;

	@NotEmpty(message = "Suffix is required.")
	@Size(min = 1, max = 20, message = "License Number must between {min} and {max} characters.")
	@Column(name = "suffix", nullable = false, length = 20, columnDefinition = "varchar(20) default 'MD'")
	private String suffix;

	@Lob
	@Column(name = "signature", nullable = true, columnDefinition = "BLOB DEFAULT NULL")
	protected byte[] signature;

	@Column(name = "doctor_type", nullable = false, columnDefinition = "int default 0")
	private int doctorType = 0;
	// 0 - NONE, 1-Physician, 2-Pathologist, 4-Radiologist
	
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
	private Boolean isActive;

	public QisDoctor() {
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

	public String getDoctorid() {
		return doctorid;
	}

	public void setDoctorid(String doctorid) {
		this.doctorid = doctorid;
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

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public int getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(int doctorType) {
		this.doctorType = doctorType;
	}
}
