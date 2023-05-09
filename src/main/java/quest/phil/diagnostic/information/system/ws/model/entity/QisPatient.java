package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@DynamicUpdate
@Table(name = "qis_patients", uniqueConstraints = { @UniqueConstraint(columnNames = { "patient_id" }) })
public class QisPatient implements Serializable {

	private static final long serialVersionUID = -5768940609234962551L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@NaturalId
	@Column(name = "patient_id", nullable = false, length = 20, unique = true)
	private String patientid;

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

	@JsonIgnore
	@Column(name = "date_of_birth", nullable = false, columnDefinition = "DATE")
	@Temporal(TemporalType.DATE)
	private Calendar dateOfBirth;
	@Transient
	private String birthDate;

	@NotEmpty(message = "Contact is required.")
	@Size(min = 1, max = 20, message = "Contact Number must between {min} and {max} characters.")
	@Column(name = "contact_number", nullable = false, length = 20)
	private String contactNumber;

//	@NotEmpty(message = "Email is required.")
//	@Size(min = 1, max = 120, message = "Email must between {min} and {max} characters.")
	@Column(name = "email", nullable = true, length = 120)
	private String email;

	@NotEmpty(message = "Address is required.")
	@Size(max = 250, message = "Address must not exceed to {max} characters.")
	@Column(name = "address", nullable = true, length = 250)
	private String address;
	
	@Size(max = 20, message = "Senior ID must not exceed to {max} characters.")
	@Column(name = "senior_id", nullable = true, length = 20)
	private String seniorId;

	@Size(max = 20, message = "PWD ID must not exceed to {max} characters.")
	@Column(name = "pwd_id", nullable = true, length = 20)
	private String pwdId;
	
	@Size(max = 30, message = "passport ID must not exceed to {max} characters.")
	@Column(name = "passport", nullable = true, length = 30)
	private String passport;

	@JsonIgnore
	@Column(name = "corporate_id", nullable = true)
	private Long corporateId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "corporate_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisCorporate corporate;
	
	@JsonIgnore
	@Column(name = "nationality_id", nullable = true, columnDefinition = "BIGINT DEFAULT 167")
	private Long nationalityId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "nationality_id", insertable = false, updatable = false)
	@JsonManagedReference
	private Country nationality;

	@JsonIgnore
	@Column(name = "created_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdAt = Calendar.getInstance();

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

	// 1-male 0-female
	@NotNull(message = "Gender is required.")
	@Column(name = "gender", length = 1, columnDefinition = "varchar(1) default 'M'")
	private String gender;
	
	@Column(name = "height", nullable = true)
	public Float height = 0f;
	
	@Column(name = "weight", nullable = true)
	public Float weight = 0f;
	
	@Lob
	@Column(name = "image", nullable = true, columnDefinition = "LONGBLOB DEFAULT NULL")
	protected byte[] image;
	
	@Column(name = "image_type", nullable = true, length = 40)
	private String imageType;

	public QisPatient() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Float getHeight() {
		return height;
	}

	public Float getWeight() {
		return weight;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPatientid() {
		return patientid;
	}

	public void setPatientid(String patientid) {
		this.patientid = patientid;
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

	public String getSeniorId() {
		return seniorId;
	}

	public void setSeniorId(String seniorId) {
		this.seniorId = seniorId;
	}

	public String getPwdId() {
		return pwdId;
	}

	public void setPwdId(String pwdId) {
		this.pwdId = pwdId;
	}

	public Long getCorporateId() {
		return corporateId;
	}

	public void setCorporateId(Long corporateId) {
		this.corporateId = corporateId;
	}

	public QisCorporate getCorporate() {
		return corporate;
	}

	public void setCorporate(QisCorporate corporate) {
		this.corporate = corporate;
	}

	public Long getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(Long nationalityId) {
		this.nationalityId = nationalityId;
	}

	public Country getNationality() {
		return nationality;
	}

	public void setNationality(Country nationality) {
		this.nationality = nationality;
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

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
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
