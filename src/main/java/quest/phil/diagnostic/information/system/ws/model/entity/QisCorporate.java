package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DynamicUpdate
@Table(name = "qis_corporates", uniqueConstraints = { @UniqueConstraint(columnNames = { "corporate_id" }),
		@UniqueConstraint(columnNames = { "company_name" }), @UniqueConstraint(columnNames = { "soa_code" }) })
public class QisCorporate implements Serializable {
	private static final long serialVersionUID = -6679504157334336360L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@NaturalId
	@Column(name = "corporate_id", nullable = false, length = 20, unique = true)
	private String corporateid;

	@NotEmpty(message = "Company Name is required.")
	@Size(min = 1, max = 120, message = "Company Name must between {min} and {max} characters.")
	@Column(name = "company_name", nullable = false, length = 120, unique = true)
	private String companyName;

	@Size(max = 250, message = "Company Address must not exceed to {max} characters.")
	@Column(name = "company_address", nullable = true, length = 250)
	private String companyAddress;

	@Size(max = 120, message = "Contact Person must not exceed to {max} characters.")
	@Column(name = "contact_person", nullable = true, length = 120)
	private String contactPerson;

	@Size(max = 120, message = "Contact number must not exceed to {max} characters.")
	@Column(name = "contact_number", nullable = true, length = 120)
	private String contactNumber;

	@Size(max = 120, message = "Email must not exceed to {max} characters.")
	@Column(name = "email", nullable = true, length = 120)
	private String email;

	@NotNull(message = "SOA Code is required.")
	@NotEmpty(message = "SOA Code should not be empty.")
	@Size(min = 3, max = 8, message = "SOA Code must between {min} and {max} characters.")
	@Column(name = "soa_code", nullable = false, length = 8)
	private String soaCode;

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
	
	@Column(name = "advace_payment", nullable = false)
	private Double advPayment = 0.0;

	@JsonIgnore
	@Column(name = "updated_by", nullable = true)
	private Long updatedBy = null;

	@Column(name = "is_active", columnDefinition = "boolean default true")
	private Boolean isActive;

	@Size(max = 200, message = "Result Email must not exceed to {max} characters.")
	@Column(name = "result_email", nullable = true, length = 200)
	private String resultEmail;

	@NotNull(message = "Charge Type is required.")
	@NotEmpty(message = "Charge Type should not be empty.")
	@Column(name = "charge_type", nullable = false, length = 4, columnDefinition = "varchar(4) DEFAULT 'CORP'")
	private String chargeType;
	
	public QisCorporate() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public Double getAdvPayment() {
		return advPayment;
	}

	public void setAdvPayment(Double advPayment) {
		this.advPayment = advPayment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCorporateid() {
		return corporateid;
	}

	public void setCorporateid(String corporateid) {
		this.corporateid = corporateid;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
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

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getResultEmail() {
		return resultEmail;
	}

	public void setResultEmail(String resultEmail) {
		this.resultEmail = resultEmail;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getSoaCode() {
		return soaCode;
	}

	public void setSoaCode(String soaCode) {
		this.soaCode = soaCode;
	}

}
