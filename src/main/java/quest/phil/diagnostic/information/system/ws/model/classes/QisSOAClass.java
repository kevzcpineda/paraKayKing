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

import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;

@MappedSuperclass
public class QisSOAClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NaturalId
	@Column(name = "soa_number", nullable = false, length = 20, unique = true)
	private String soaNumber;

	@Column(name = "soa_count", nullable = false, columnDefinition = "int default 0")
	private int soaCount;

	@Column(name = "soa_amount", nullable = false, columnDefinition = "double DEFAULT 0")
	private double soaAmount = 0;

	@Column(name = "soa_status", nullable = false, columnDefinition = "int default 0")
	private int soaStatus = 0;

	@JsonIgnore
	@Column(name = "charge_to_id", nullable = true)
	private Long chargeToId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "charge_to_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisCorporate chargeTo;

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
	
	@Column(name = "soa_send", nullable = false, columnDefinition = "boolean default false")
	private boolean soaSend = false;
	
	@Column(name = "soa_recieve", nullable = false, columnDefinition = "boolean default false")
	private boolean soaRecieved = false;
	
	

	public QisSOAClass() {
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

	public String getSoaNumber() {
		return soaNumber;
	}

	public void setSoaNumber(String soaNumber) {
		this.soaNumber = soaNumber;
	}

	public int getSoaCount() {
		return soaCount;
	}

	public void setSoaCount(int soaCount) {
		this.soaCount = soaCount;
	}

	public double getSoaAmount() {
		return soaAmount;
	}

	public void setSoaAmount(double soaAmount) {
		this.soaAmount = soaAmount;
	}

	public int getSoaStatus() {
		return soaStatus;
	}

	public void setSoaStatus(int soaStatus) {
		this.soaStatus = soaStatus;
	}

	public Long getChargeToId() {
		return chargeToId;
	}

	public void setChargeToId(Long chargeToId) {
		this.chargeToId = chargeToId;
	}

	public QisCorporate getChargeTo() {
		return chargeTo;
	}

	public void setChargeTo(QisCorporate chargeTo) {
		this.chargeTo = chargeTo;
	}

	public Calendar getCoverageDateFrom() {
		return coverageDateFrom;
	}

	public void setCoverageDateFrom(Calendar coverageDateFrom) {
		this.coverageDateFrom = coverageDateFrom;
	}

	public Calendar getCoverageDateTo() {
		return coverageDateTo;
	}

	public void setCoverageDateTo(Calendar coverageDateTo) {
		this.coverageDateTo = coverageDateTo;
	}

	public Calendar getStatementDate() {
		return statementDate;
	}

	public void setStatementDate(Calendar statementDate) {
		this.statementDate = statementDate;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	public Calendar getNotedDate() {
		return notedDate;
	}

	public void setNotedDate(Calendar notedDate) {
		this.notedDate = notedDate;
	}

	public Long getNotedBy() {
		return notedBy;
	}

	public void setNotedBy(Long notedBy) {
		this.notedBy = notedBy;
	}

	public QisUserPersonel getPreparedUser() {
		return preparedUser;
	}

	public void setPreparedUser(QisUserPersonel preparedUser) {
		this.preparedUser = preparedUser;
	}

	public QisUserPersonel getNotedUser() {
		return notedUser;
	}

	public void setNotedUser(QisUserPersonel notedUser) {
		this.notedUser = notedUser;
	}

	public QisUserPersonel getVerifiedUser() {
		return verifiedUser;
	}

	public void setVerifiedUser(QisUserPersonel verifiedUser) {
		this.verifiedUser = verifiedUser;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public boolean isSoaSend() {
		return soaSend;
	}

	public boolean isSoaRecieved() {
		return soaRecieved;
	}

	public void setSoaSend(boolean soaSend) {
		this.soaSend = soaSend;
	}

	public void setSoaRecieved(boolean soaRecieved) {
		this.soaRecieved = soaRecieved;
	}


}
