package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "qis_packages", uniqueConstraints = { @UniqueConstraint(columnNames = { "package_name" }),
		@UniqueConstraint(columnNames = { "package_id" }) })
public class QisPackage implements Serializable {

	private static final long serialVersionUID = 3412194375445004501L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@NaturalId
	@Column(name = "package_id", nullable = false, length = 20, unique = true)
	private String packageid;

	@NotEmpty(message = "Package Name is required.")
	@Size(min = 1, max = 120, message = "Package Name must between {min} and {max} characters.")
	@Column(name = "package_name", nullable = false, length = 120, unique = true)
	private String packageName;

	@NotEmpty(message = "Package Description is required.")
	@Size(min = 1, max = 250, message = "Package Description must between {min} and {max} characters.")
	@Column(name = "package_decription", nullable = false, length = 250)
	private String packageDescription;

	@NotNull(message = "Package Price is required.")
	@Column(name = "package_price", nullable = false)
	private Double packagePrice;

	@NotEmpty(message = "Package Type is required.")
	@Size(min = 1, max = 12, message = "Package Type must between {min} and {max} characters.")
	@Column(name = "package_type", nullable = false, length = 12)
	private String packageType;

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
	private Boolean isActive = true;

	@Column(name = "is_taxable", columnDefinition = "boolean default true")
	private Boolean isTaxable;
	
	@Column(name = "is_discountable", columnDefinition = "boolean default true")
	private Boolean isDiscountable;

	@Column(name = "is_on_menu", columnDefinition = "boolean default false")
	private Boolean isOnMenu;
	
	@Column(name = "print", columnDefinition = "boolean default false")
	private Boolean print;
	
	@Column(name = "send", columnDefinition = "boolean default false")
	private Boolean send;

	@ManyToMany(fetch = FetchType.EAGER) // LAZY or EAGER
	@JoinTable(name = "qis_package_items", joinColumns = @JoinColumn(name = "qis_package_id"), inverseJoinColumns = @JoinColumn(name = "qis_item_id"))
	private Set<QisItem> packageItems = new HashSet<>();
	
	@NotEmpty(message = "Type test package is required.")
	@Size(min = 1, max = 30, message = "Type test package must between {min} and {max} characters.")
	@Column(name = "qis_type_test_packages", nullable = false, length = 12 , columnDefinition = "IndustrialPackage")
	private String typeTestPackage;

	public QisPackage() {
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

	public String getPackageid() {
		return packageid;
	}

	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageDescription() {
		return packageDescription;
	}

	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}

	public Double getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(Double packagePrice) {
		this.packagePrice = packagePrice;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
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

	public Boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(Boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	public Boolean isDiscountable() {
		return isDiscountable;
	}

	public void setDiscountable(Boolean isDiscountable) {
		this.isDiscountable = isDiscountable;
	}

	public Boolean isOnMenu() {
		return isOnMenu;
	}

	public void setOnMenu(Boolean isOnMenu) {
		this.isOnMenu = isOnMenu;
	}	
	
	public Boolean getPrint() {
		return print;
	}

	public void setPrint(Boolean print) {
		this.print = print;
	}

	public Boolean getSend() {
		return send;
	}

	public void setSend(Boolean send) {
		this.send = send;
	}

	public Set<QisItem> getPackageItems() {
		return packageItems;
	}

	public void setPackageItems(Set<QisItem> packageItems) {
		this.packageItems = packageItems;
	}

	public String getTypeTestPackage() {
		return typeTestPackage;
	}

	public void setTypeTestPackage(String typeTestPackage) {
		this.typeTestPackage = typeTestPackage;
	}
}
