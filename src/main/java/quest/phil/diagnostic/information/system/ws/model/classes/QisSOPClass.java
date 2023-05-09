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

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;

@MappedSuperclass
public class QisSOPClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NaturalId
	@Column(name = "sop_number", nullable = false, length = 20, unique = true)
	private String sopNumber;

	@Column(name = "sop_count", nullable = false, columnDefinition = "int default 0")
	private int sopCount;

	@Column(name = "sop_amount", nullable = false, columnDefinition = "double DEFAULT 0")
	private double sopAmount = 0;

	@Column(name = "sop_status", nullable = false, columnDefinition = "int default 0")
	private int sopStatus = 0;

	@JsonIgnore
	@Column(name = "reference_id", nullable = true)
	private Long referenceId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "reference_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisReferenceLaboratory referenceLab;

	@Column(name = "coverage_date_from", nullable = false, columnDefinition = "DATE")
	private Calendar coverageDateFrom = Calendar.getInstance();

	@Column(name = "coverage_date_to", nullable = false, columnDefinition = "DATE")
	private Calendar coverageDateTo = Calendar.getInstance();

	@Column(name = "statement_date", nullable = false, columnDefinition = "DATE")
	private Calendar statementDate = Calendar.getInstance();

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

	@JsonIgnore
	@Column(name = "verified_date", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar verifiedDate = null;

	@JsonIgnore
	@Column(name = "verified_by", nullable = true)
	private Long verifiedBy = null;

	@JsonIgnore
	@Column(name = "noted_date", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar notedDate = null;

	@JsonIgnore
	@Column(name = "noted_by", nullable = true)
	private Long notedBy = null;

	@ManyToOne(optional = true)
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserPersonel preparedUser;

	@ManyToOne(optional = true)
	@JoinColumn(name = "noted_by", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserPersonel notedUser;

	@ManyToOne(optional = true)
	@JoinColumn(name = "verified_by", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserPersonel verifiedUser;
	
	@Column(name = "purchase_order", nullable = true)
	private String purchaseOrder;
	
	public QisSOPClass() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public Long getId() {
		return id;
	}

	public String getSopNumber() {
		return sopNumber;
	}

	public int getSopCount() {
		return sopCount;
	}

	public double getSopAmount() {
		return sopAmount;
	}

	public int getSopStatus() {
		return sopStatus;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public QisReferenceLaboratory getChargeTo() {
		return referenceLab;
	}

	public Calendar getCoverageDateFrom() {
		return coverageDateFrom;
	}

	public Calendar getCoverageDateTo() {
		return coverageDateTo;
	}

	public Calendar getStatementDate() {
		return statementDate;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public boolean isActive() {
		return isActive;
	}

	public Calendar getVerifiedDate() {
		return verifiedDate;
	}

	public Long getVerifiedBy() {
		return verifiedBy;
	}

	public Calendar getNotedDate() {
		return notedDate;
	}

	public Long getNotedBy() {
		return notedBy;
	}

	public QisUserPersonel getPreparedUser() {
		return preparedUser;
	}

	public QisUserPersonel getNotedUser() {
		return notedUser;
	}

	public QisUserPersonel getVerifiedUser() {
		return verifiedUser;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSopNumber(String sopNumber) {
		this.sopNumber = sopNumber;
	}

	public void setSopCount(int sopCount) {
		this.sopCount = sopCount;
	}

	public void setSopAmount(double sopAmount) {
		this.sopAmount = sopAmount;
	}

	public void setSopStatus(int sopStatus) {
		this.sopStatus = sopStatus;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public void setChargeTo(QisReferenceLaboratory chargeTo) {
		this.referenceLab = chargeTo;
	}

	public void setCoverageDateFrom(Calendar coverageDateFrom) {
		this.coverageDateFrom = coverageDateFrom;
	}

	public void setCoverageDateTo(Calendar coverageDateTo) {
		this.coverageDateTo = coverageDateTo;
	}

	public void setStatementDate(Calendar statementDate) {
		this.statementDate = statementDate;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setVerifiedDate(Calendar verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public void setVerifiedBy(Long verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public void setNotedDate(Calendar notedDate) {
		this.notedDate = notedDate;
	}

	public void setNotedBy(Long notedBy) {
		this.notedBy = notedBy;
	}

	public QisReferenceLaboratory getReferenceLab() {
		return referenceLab;
	}

	public void setReferenceLab(QisReferenceLaboratory referenceLab) {
		this.referenceLab = referenceLab;
	}

	public void setPreparedUser(QisUserPersonel preparedUser) {
		this.preparedUser = preparedUser;
	}

	public void setNotedUser(QisUserPersonel notedUser) {
		this.notedUser = notedUser;
	}

	public void setVerifiedUser(QisUserPersonel verifiedUser) {
		this.verifiedUser = verifiedUser;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
}
