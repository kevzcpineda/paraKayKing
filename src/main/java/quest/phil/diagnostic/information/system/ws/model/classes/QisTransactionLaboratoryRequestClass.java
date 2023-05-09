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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;

@MappedSuperclass
public class QisTransactionLaboratoryRequestClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@NotNull(message = "Transaction is required.")
	@Column(name = "qis_transaction_id", nullable = false)
	private Long transactionid;

	@NotNull(message = "Transaction Item is required.")
	@Column(name = "qis_transaction_item_id", nullable = false)
	private Long transactionItemId;

	@JsonIgnore
	@NotNull(message = "Item is required.")
	@Column(name = "qis_item_id", nullable = false)
	private Long itemId;

	@NotEmpty(message = "Item Laboratory is required.")
	@Size(min = 1, max = 12, message = "Item Laboratory must between {min} and {max} characters.")
	@Column(name = "item_laboratory", nullable = false, length = 12)
	private String itemLaboratory;

	@JsonIgnore
	@ManyToOne(optional = true)
	@JoinColumn(name = "qis_item_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisItem itemDetails;

	@JsonIgnore
	@ManyToOne(optional = true)
	@JoinColumn(name = "qis_transaction_item_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionItem transactionItem;
		
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

	@Column(name = "is_submitted", nullable = false, columnDefinition = "boolean default false")
	private boolean isSubmitted = false;

	@Column(name = "verified_date", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar verifiedDate = null;

	@JsonIgnore
	@Column(name = "verified_by", nullable = true)
	private Long verifiedBy = null;
	@ManyToOne(optional = true)
	@JoinColumn(name = "verified_by", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserPersonel verified;

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
	
	@Column(name = "other_notes", nullable = true)
	private String otherNotes;

	@Column(name = "status", nullable = false, columnDefinition = "int default 0")
	private int status = 0;
	
	@JsonIgnore
	@Column(name = "medical_doctor_id", nullable = true)
	private Long medicalDoctorId = null;
	@ManyToOne(optional = true)
	@JoinColumn(name = "medical_doctor_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisDoctor medicalDoctor;
	
	@Column(name = "print", nullable = false, columnDefinition = "boolean default false")
	private Boolean print = false;

	@Column(name = "send", nullable = false, columnDefinition = "boolean default false")
	private Boolean send = false;
	
	@Column(name = "sopStatus", nullable = false, columnDefinition = "boolean default false")
	private Boolean sopStatus = false;
	
	@Column(name = "mole_price_item", nullable = true)
	private Double molePriceItem;
	
	@JsonIgnore
	@Column(name = "molecular_lab_id", nullable = true)
	private Long referenceLabId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "molecular_lab_id", insertable = false, updatable =false)
	@JsonManagedReference 
	private QisReferenceLaboratory referenceLab;
	
	public QisTransactionLaboratoryRequestClass() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public QisTransactionLaboratoryRequestClass(@NotNull(message = "Transaction Item is required.") Long transactionItemId,
			@NotNull(message = "Item is required.") Long itemId,
			@NotEmpty(message = "Item Laboratory is required.") @Size(min = 1, max = 12, message = "Item Laboratory must between {min} and {max} characters.") String itemLaboratory) {
		super();
		this.transactionItemId = transactionItemId;
		this.itemId = itemId;
		this.itemLaboratory = itemLaboratory;
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

	public Long getTransactionItemId() {
		return transactionItemId;
	}

	public void setTransactionItemId(Long transactionItemId) {
		this.transactionItemId = transactionItemId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemLaboratory() {
		return itemLaboratory;
	}

	public void setItemLaboratory(String itemLaboratory) {
		this.itemLaboratory = itemLaboratory;
	}

	public QisItem getItemDetails() {
		return itemDetails;
	}

	public void setItemDetails(QisItem itemDetails) {
		this.itemDetails = itemDetails;
	}

	public QisTransactionItem getTransactionItem() {
		return transactionItem;
	}

	public void setTransactionItem(QisTransactionItem transactionItem) {
		this.transactionItem = transactionItem;
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

	public boolean isSubmitted() {
		return isSubmitted;
	}

	public void setSubmitted(boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}

	public Calendar getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Calendar verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public Long getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(Long verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public QisUserPersonel getVerified() {
		return verified;
	}

	public void setVerified(QisUserPersonel verified) {
		this.verified = verified;
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

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public Boolean getSopStatus() {
		return sopStatus;
	}

	public Double getMolePriceItem() {
		return molePriceItem;
	}

	public void setSopStatus(Boolean sopStatus) {
		this.sopStatus = sopStatus;
	}

	public void setMolePriceItem(Double molePriceItem) {
		this.molePriceItem = molePriceItem;
	}

	public Long getReferenceLabId() {
		return referenceLabId;
	}

	public QisReferenceLaboratory getReferenceLab() {
		return referenceLab;
	}

	public void setReferenceLabId(Long referenceLabId) {
		this.referenceLabId = referenceLabId;
	}

	public void setReferenceLab(QisReferenceLaboratory referenceLab) {
		this.referenceLab = referenceLab;
	}
	
}
