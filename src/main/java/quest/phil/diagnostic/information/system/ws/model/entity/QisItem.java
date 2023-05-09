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
@Table(name = "qis_items", uniqueConstraints = { @UniqueConstraint(columnNames = { "item_id" }),
		@UniqueConstraint(columnNames = { "item_name" }) })
public class QisItem implements Serializable {

	private static final long serialVersionUID = -4200945554898588948L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@NaturalId
	@Column(name = "item_id", nullable = false, length = 20, unique = true)
	private String itemid;

	@NotEmpty(message = "Item Name is required.")
	@Size(min = 1, max = 120, message = "Item Name must between {min} and {max} characters.")
	@Column(name = "item_name", nullable = false, length = 120, unique = true)
	private String itemName;

	@NotEmpty(message = "Item Description is required.")
	@Size(min = 1, max = 250, message = "Item Description must between {min} and {max} characters.")
	@Column(name = "item_decription", nullable = false, length = 250)
	private String itemDescription;

	@NotNull(message = "Item Price is required.")
	@Column(name = "item_price", nullable = false)
	private Double itemPrice;

	@NotEmpty(message = "Item Category is required.")
	@Size(min = 1, max = 12, message = "Item Category must between {min} and {max} characters.")
	@Column(name = "item_category", nullable = false, length = 12)
	private String itemCategory;

	@NotEmpty(message = "Item Laboratory is required.")
	@Size(min = 1, max = 12, message = "Item Laboratory must between {min} and {max} characters.")
	@Column(name = "item_laboratory", nullable = false, length = 12)
	private String itemLaboratory;

	@Size(min = 1, max = 12, message = "Item Laboratory Procedure must between {min} and {max} characters.")
	@Column(name = "item_laboratory_procedure", nullable = true, length = 12)
	private String itemLaboratoryProcedure;

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

	@Column(name = "is_taxable", columnDefinition = "boolean default true")
	private Boolean isTaxable;

	@Column(name = "is_discountable", columnDefinition = "boolean default true")
	private Boolean isDiscountable;

	@Column(name = "is_on_menu", columnDefinition = "boolean default false")
	private Boolean isOnMenu;
	
	
	@ManyToMany(fetch = FetchType.EAGER) // LAZY or EAGER
	@JoinTable(name = "qis_item_laboratory_services", joinColumns = @JoinColumn(name = "qis_item_id"), inverseJoinColumns = @JoinColumn(name = "qis_laboratory_services_id"))
	private Set<QisLaboratoryProcedureService> serviceRequest = new HashSet<>();
	
	@Size(min = 1, max = 255, message = "Item Name must between {min} and {max} characters.")
	@Column(name = "specific_test", nullable = false, length = 255, columnDefinition = "varchar(255) default 'all'")
	private String specificTest;

	public QisItem() {
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

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemLaboratory() {
		return itemLaboratory;
	}

	public void setItemLaboratory(String itemLaboratory) {
		this.itemLaboratory = itemLaboratory;
	}

	public String getItemLaboratoryProcedure() {
		return itemLaboratoryProcedure;
	}

	public void setItemLaboratoryProcedure(String itemLaboratoryProcedure) {
		this.itemLaboratoryProcedure = itemLaboratoryProcedure;
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

	public Set<QisLaboratoryProcedureService> getServiceRequest() {
		return serviceRequest;
	}

	public void setServiceRequest(Set<QisLaboratoryProcedureService> serviceRequest) {
		this.serviceRequest = serviceRequest;
	}

	public String getSpecificTest() {
		return specificTest;
	}

	public void setSpecificTest(String specificTest) {
		this.specificTest = specificTest;
	}
	
}
