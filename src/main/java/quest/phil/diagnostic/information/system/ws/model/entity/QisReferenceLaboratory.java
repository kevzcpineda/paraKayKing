package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
@Table(name = "qis_reference_lab", uniqueConstraints = { @UniqueConstraint(columnNames = { "company_name" }),
		@UniqueConstraint(columnNames = { "reference_lab_id" }) })
public class QisReferenceLaboratory implements Serializable {

	private static final long serialVersionUID = -6446194609432527256L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@NaturalId
	@Column(name = "reference_lab_id", nullable = false, length = 20, unique = true)
	private String referenceid;

	@NotEmpty(message = "Company Name is required.")
	@Size(min = 1, max = 120, message = "Company Name must between {min} and {max} characters.")
	@Column(name = "company_name", nullable = false, length = 120, unique = true)
	private String name;

	@Size(max = 250, message = "Company Address must not exceed to {max} characters.")
	@Column(name = "company_address", nullable = true, length = 250)
	private String address;

	@Size(max = 120, message = "Contact Person must not exceed to {max} characters.")
	@Column(name = "contact_person", nullable = true, length = 120)
	private String contactPerson;

	@Size(max = 120, message = "Contact number must not exceed to {max} characters.")
	@Column(name = "contact_number", nullable = true, length = 120)
	private String contactNumber;

	@Size(max = 120, message = "Email must not exceed to {max} characters.")
	@Column(name = "sop_email", nullable = true, length = 120)
	private String sopEmail;

	@Size(max = 120, message = "Email must not exceed to {max} characters.")
	@Column(name = "results_email", nullable = true, length = 120)
	private String resultsEmail;

	@NotNull(message = "SOP Code is required.")
	@NotEmpty(message = "SOP Code should not be empty.")
	@Size(min = 3, max = 8, message = "SOA Code must between {min} and {max} characters.")
	@Column(name = "sop_code", nullable = false, length = 8)
	private String sopCode;

	@Column(name = "is_active", columnDefinition = "boolean default true")
	private Boolean isActive;
	
	@JsonIgnore
	@Column(name = "created_by", nullable = true)
	private Long createdBy = null;

	@JsonIgnore
	@Column(name = "updated_by", nullable = true)
	private Long updatedBy = null;
	
	@JsonIgnore
	@Column(name = "created_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdAt = Calendar.getInstance();


	@JsonIgnore
	@Column(name = "updated_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar updatedAt = Calendar.getInstance();
	
	
	@Column(nullable=true)
	@ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL) // LAZY or EAGER
	@JoinTable(name = "qis_reference_laboratory_items", joinColumns = @JoinColumn(name = "qis_reference_lab_id"), inverseJoinColumns = @JoinColumn(name = "qis_reference_items_id"))
	private Set<QisReferenceLaboratoryItems> collectionItems = new HashSet<>();
	
	@Column(nullable=true)
	@ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL) // LAZY or EAGER
	@JoinTable(name = "qis_reference_package_items", joinColumns = @JoinColumn(name = "qis_reference_lab_id"), inverseJoinColumns = @JoinColumn(name = "qis_reference_package_id"))
	private Set<QisReferencePackageItems> collectionPackage = new HashSet<>();

	public Long getId() {
		return id;
	}

	public String getReferenceid() {
		return referenceid;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public String getSopEmail() {
		return sopEmail;
	}

	public String getResultsEmail() {
		return resultsEmail;
	}

	public String getSopCode() {
		return sopCode;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setReferenceid(String referenceid) {
		this.referenceid = referenceid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public void setSopEmail(String sopEmail) {
		this.sopEmail = sopEmail;
	}

	public void setResultsEmail(String resultsEmail) {
		this.resultsEmail = resultsEmail;
	}

	public void setSopCode(String sopCode) {
		this.sopCode = sopCode;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

//	public Set<QisReferenceLaboratoryItems> getItems() {
//		return items;
//	}
//
//	public void setItems(Set<QisReferenceLaboratoryItems> items) {
//		this.items = items;
//	}

	public Set<QisReferenceLaboratoryItems> getCollectionItems() {
		return collectionItems;
	}

	public void setCollectionItems(Set<QisReferenceLaboratoryItems> collectionItems) {
		this.collectionItems = collectionItems;
	}
	public Long getCreatedBy() {
		return createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Set<QisReferencePackageItems> getCollectionPackage() {
		return collectionPackage;
	}

	public void setCollectionPackage(Set<QisReferencePackageItems> collectionPackage) {
		this.collectionPackage = collectionPackage;
	}

}
