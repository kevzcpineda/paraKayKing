package quest.phil.diagnostic.information.system.ws.model.classes;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPackage;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;

@MappedSuperclass
public class QisTransactionItemClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@NotNull(message = "Transaction is required.")
	@Column(name = "qis_transaction_id", nullable = false)
	private Long transactionid;

	@JsonIgnore
	@NotNull(message = "Item/Package ID is required.")
	@Column(name = "item_id", nullable = false)
	private Long itemid;

	// PCK-PACKAGE, ITM-ITEM or SERVICE
	@NotNull(message = "Type is required.")
	@NotEmpty(message = "Type should not be empty.")
	@Size(max = 4, message = "Type should not exceed {max} characters.")
	@Column(name = "item_type", nullable = false, length = 4)
	private String itemType;

	@Column(name = "item_reference", nullable = true, length = 20)
	private String itemReference;

	@NotNull(message = "Quantity is required.")
	@Min(value = 1, message = "Quantity should be minimum of {value}.")
	@Column(name = "quantity", nullable = false, columnDefinition = "int default 1")
	private int quantity = 1;

	@NotNull(message = "Price is required.")
	@Column(name = "item_price", nullable = false)
	private Double itemPrice;

	@Column(name = "discount_rate", nullable = false)
	private Integer discountRate = 0;

	@Column(name = "discount_type", nullable = true, length = 4)
	private String discountType;

	@JsonIgnore
	@Column(name = "qis_item_id", nullable = true)
	private Long qisItemId;
	@JsonIgnore
	@ManyToOne(optional = true)
	@JoinColumn(name = "qis_item_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisItem qisItem;

	@JsonIgnore
	@Column(name = "qis_package_id", nullable = true)
	private Long qisPackageId;
	@JsonIgnore
	@ManyToOne(optional = true)
	@JoinColumn(name = "qis_package_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisPackage qisPackage;

	@Transient
	private Object itemDetails;

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

	@Column(name = "gross_amount", nullable = false, columnDefinition = "double default 0")
	private Double grossAmount = 0d;

	@Column(name = "amount_due", nullable = false, columnDefinition = "double default 0")
	private Double amountDue = 0d;

	@Column(name = "discount_value", nullable = false, columnDefinition = "double default 0")
	private Double discountValue = 0d;

	@Column(name = "net_amount", nullable = false, columnDefinition = "double default 0")
	private Double netAmount = 0d;

	@Column(name = "is_discountable", columnDefinition = "boolean default true")
	private Boolean isDiscountable = true;

	@Column(name = "is_taxable", columnDefinition = "boolean default true")
	private Boolean isTaxable = true;

	@Column(name = "tax_rate", nullable = false, columnDefinition = "int default 0")
	private Integer taxRate = 0;

	@Column(name = "tax_amount", nullable = false, columnDefinition = "double default 0")
	private Double taxAmount = 0d;

	@Column(name = "status", nullable = false, columnDefinition = "int default 1")
	private int status = 1;
	
	@Column(name = "called", nullable = false, columnDefinition = "int default 0")
	private int called = 0;

	@JsonIgnore
	@Column(name = "qc_id", nullable = true)
	private Long qcId = null;

	@ManyToOne(optional = true)
	@JoinColumn(name = "qc_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserPersonel qualityControl;

	@Column(name = "qc_log", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar qcDate = null;

	@JsonIgnore
	@Column(name = "lab_personel_id", nullable = true)
	private Long labPersonelId = null;

	@ManyToOne(optional = true)
	@JoinColumn(name = "lab_personel_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserPersonel labPersonel;

	@Column(name = "lab_personel_log", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar labPersonelDate = null;

	@JsonIgnore
	@Column(name = "medical_doctor_id", nullable = true)
	private Long medicalDoctorId = null;

	@ManyToOne(optional = true)
	@JoinColumn(name = "medical_doctor_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisDoctor medicalDoctor;

	@Column(name = "classification", nullable = true, length = 4)
	private String classification;

	@Column(name = "over_all_findings", nullable = true, length = 250)
	private String overAllFindings;

	@JsonIgnore
	@Column(name = "classified_id", nullable = true)
	private Long classifiedId = null;

	@ManyToOne(optional = true)
	@JoinColumn(name = "classified_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserPersonel classifiedBy;

	@Column(name = "classified_log", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar classifiedDate = null;

	@JsonIgnore
	@Column(name = "classify_doctor_id", nullable = true)
	private Long classifyDoctorId = null;

	@ManyToOne(optional = true)
	@JoinColumn(name = "classify_doctor_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisDoctor classifyDoctor;

	@Column(name = "print", nullable = false, columnDefinition = "boolean default false")
	private Boolean print = false;

	@Column(name = "send", nullable = false, columnDefinition = "boolean default false")
	private Boolean send = false;

	public QisTransactionItemClass() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public QisTransactionItemClass(
			@NotNull(message = "Item/Package ID is required.") @NotEmpty(message = "Item/Package ID should not be empty") Long itemid,
			@NotNull(message = "Type is required.") @NotEmpty(message = "Type should not be empty.") @Size(max = 4, message = "Type should not exceed {max} characters.") String itemType,
			@NotNull(message = "Quantity is required.") @Min(value = 1, message = "Quantity should be minimum of {value}.") int quantity,
			@NotNull(message = "Price is required.") Double itemPrice, String itemReference, Boolean isDiscountable,
			Boolean isTaxable) {
		super();
		this.itemid = itemid;
		this.itemType = itemType;
		this.quantity = quantity;
		this.itemPrice = itemPrice;
		this.itemReference = itemReference;
		this.isDiscountable = isDiscountable;
		this.isTaxable = isTaxable;
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(Long transactionid) {
		this.transactionid = transactionid;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemReference() {
		return itemReference;
	}

	public void setItemReference(String itemReference) {
		this.itemReference = itemReference;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Integer getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Integer discountRate) {
		this.discountRate = discountRate;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public Long getQisItemId() {
		return qisItemId;
	}

	public void setQisItemId(Long qisItemId) {
		this.qisItemId = qisItemId;
	}

	@JsonIgnore
	public QisItem getQisItem() {
		return qisItem;
	}

	public void setQisItem(QisItem qisItem) {
		this.qisItem = qisItem;
	}

	public Long getQisPackageId() {
		return qisPackageId;
	}

	public void setQisPackageId(Long qisPackageId) {
		this.qisPackageId = qisPackageId;
	}

	@JsonIgnore
	public QisPackage getQisPackage() {
		return qisPackage;
	}

	public void setQisPackage(QisPackage qisPackage) {
		this.qisPackage = qisPackage;
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

	public int getCalled() {
		return called;
	}

	public void setCalled(int called) {
		this.called = called;
	}

	public Object getItemDetails() {
		Object details = null;

		if ("ITM".equals(this.itemType) && this.qisItem != null) {
			details = this.qisItem;
		} else if ("PCK".equals(this.itemType) && this.qisPackage != null) {
			details = this.qisPackage;
		}

		return details;
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

	public Double getGrossAmount() {
		computeGrossAmount();
		return roundValue(grossAmount);
	}

	public Double getAmountDue() {
		this.amountDue = this.grossAmount;

		if (this.discountType != null) {
			if ("NRM".equals(this.discountType)) {
				this.amountDue = this.grossAmount - this.discountValue;
			} else {
				this.amountDue = this.grossAmount - Math.abs(this.taxAmount) - this.discountValue;
			}
		}

		return roundValue(amountDue);
	}

	public Double getDiscountValue() {
		computeTaxAmount();
		computeDiscountValue();
		return roundValue(discountValue);
	}

	public Double getTaxAmount() {
		computeTaxAmount();
		return roundValue(taxAmount);
	}

	public Double getNetAmount() {
		computeItemAmount();
		return roundValue(netAmount);
	}

	public Boolean isDiscountable() {
		return isDiscountable;
	}

	public void setDiscountable(Boolean isDiscountable) {
		this.isDiscountable = isDiscountable;
	}

	public Boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(Boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	public Integer getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Integer taxRate) {
		this.taxRate = taxRate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getQcId() {
		return qcId;
	}

	public void setQcId(Long qcId) {
		this.qcId = qcId;
	}

	public QisUserPersonel getQualityControl() {
		return qualityControl;
	}

	public void setQualityControl(QisUserPersonel qualityControl) {
		this.qualityControl = qualityControl;
	}

	public Calendar getQcDate() {
		return qcDate;
	}

	public void setQcDate(Calendar qcDate) {
		this.qcDate = qcDate;
	}

	public Long getLabPersonelId() {
		return labPersonelId;
	}

	public void setLabPersonelId(Long labPersonelId) {
		this.labPersonelId = labPersonelId;
	}

	public QisUserPersonel getLabPersonel() {
		return labPersonel;
	}

	public void setLabPersonel(QisUserPersonel labPersonel) {
		this.labPersonel = labPersonel;
	}

	public Calendar getLabPersonelDate() {
		return labPersonelDate;
	}

	public void setLabPersonelDate(Calendar labPersonelDate) {
		this.labPersonelDate = labPersonelDate;
	}

	public Long getMedicalDoctorId() {
		return medicalDoctorId;
	}

	public void setMedicalDoctorId(Long medicalDoctorId) {
		this.medicalDoctorId = medicalDoctorId;
	}

	public QisDoctor getMedicalDoctor() {
		return medicalDoctor;
	}

	public void setMedicalDoctor(QisDoctor medicalDoctor) {
		this.medicalDoctor = medicalDoctor;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getOverAllFindings() {
		return overAllFindings;
	}

	public void setOverAllFindings(String overAllFindings) {
		this.overAllFindings = overAllFindings;
	}

	public Long getClassifiedId() {
		return classifiedId;
	}

	public void setClassifiedId(Long classifiedId) {
		this.classifiedId = classifiedId;
	}

	public QisUserPersonel getClassifiedBy() {
		return classifiedBy;
	}

	public void setClassifiedBy(QisUserPersonel classifiedBy) {
		this.classifiedBy = classifiedBy;
	}

	public Calendar getClassifiedDate() {
		return classifiedDate;
	}

	public void setClassifiedDate(Calendar classifiedDate) {
		this.classifiedDate = classifiedDate;
	}

	public Long getClassifyDoctorId() {
		return classifyDoctorId;
	}

	public void setClassifyDoctorId(Long classifyDoctorId) {
		this.classifyDoctorId = classifyDoctorId;
	}

	public QisDoctor getClassifyDoctor() {
		return classifyDoctor;
	}

	public void setClassifyDoctor(QisDoctor classifyDoctor) {
		this.classifyDoctor = classifyDoctor;
	}

	private Double roundValue(Double amount) {
		return Math.round(amount * 10000.0) / 10000.0;
	}

	public void computeItemAmount() {
		computeGrossAmount();
		computeTaxAmount();
		computeDiscountValue();
		computeNetAmount();
	}

	private double computeItemPrice() {
		return quantity * itemPrice;
	}

	private void computeGrossAmount() {
		this.grossAmount = roundValue(computeItemPrice());
	}

	private void computeDiscountValue() {
		double priceItem = 0;
		if (this.isDiscountable) {
			priceItem = this.grossAmount - Math.abs(this.taxAmount);
		}
		this.discountValue = roundValue((Float.valueOf(discountRate) / 100) * priceItem);
	}

	private void computeTaxAmount() {
		double tax = 0;
		if (this.isTaxable) {
			tax = roundValue((Float.valueOf(taxRate) / 100) * computeItemPrice());

			if (this.isDiscountable && this.discountType != null && !this.discountType.equals("NRM")) {
				tax *= -1;
			}
		}
		this.taxAmount = tax;
	}

	private void computeNetAmount() {
		this.netAmount = this.grossAmount - Math.abs(this.taxAmount) - this.discountValue;
	}

}
