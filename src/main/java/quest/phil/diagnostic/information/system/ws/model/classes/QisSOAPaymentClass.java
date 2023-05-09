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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;

@MappedSuperclass
public class QisSOAPaymentClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@Column(name = "charge_to_id", nullable = true)
	private Long chargeToId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "charge_to_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisCorporate chargeTo;

	@Column(name = "payment_date", nullable = false, columnDefinition = "DATE")
	private Calendar paymentDate = Calendar.getInstance();

	@Column(name = "payment_amount", nullable = false, columnDefinition = "double DEFAULT 0")
	private double paymentAmount = 0;

	@Column(name = "payment_type", nullable = false, length = 4)
	private String paymentType;

	@Column(name = "payment_bank", nullable = true, length = 8)
	private String paymentBank;

	@Column(name = "account_number", nullable = true, length = 20)
	private String accountNumber;

	@Column(name = "other_notes", nullable = true, length = 200)
	private String otherNotes;
	
	@Column(name = "control_number", nullable = true, length = 200)
	private String controlNumber;
	
	@Column(name = "other_amount", nullable = false, columnDefinition = "double DEFAULT 0")
	private double otherAmount = 0;
	
	@Column(name = "taxWithHeld", nullable = false, columnDefinition = "double DEFAULT 0")
	private double taxWithHeld = 0;
	
	@Column(name = "advancePayment", nullable = false, columnDefinition = "double DEFAULT 0")
	private double advancePayment = 0;
	
	@JsonIgnore
	@Column(name = "verified_date", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar verifiedDate = null;

	@JsonIgnore
	@Column(name = "verified_by", nullable = true)
	private Long verifiedBy = null;

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

	@ManyToOne(optional = true)
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserPersonel preparedUser;

	@ManyToOne(optional = true)
	@JoinColumn(name = "verified_by", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserPersonel verifiedUser;
	
	@JsonIgnore
	@Column(name = "audited_date", nullable = true, columnDefinition = "DATETIME DEFAULT NULL")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar auditededDate = null;

	@JsonIgnore
	@Column(name = "audited_by", nullable = true)
	private Long auditedBy = null;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "audited_by", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserPersonel auditedUser;

	public QisSOAPaymentClass() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}
	
	public String getControlNumber() {
		return controlNumber;
	}

	public void setControlNumber(String controlNumber) {
		this.controlNumber = controlNumber;
	}

	public Calendar getAuditededDate() {
		return auditededDate;
	}

	public Long getAuditedBy() {
		return auditedBy;
	}

	public QisUserPersonel getAuditedUser() {
		return auditedUser;
	}

	public void setAuditededDate(Calendar auditededDate) {
		this.auditededDate = auditededDate;
	}

	public void setAuditedBy(Long auditedBy) {
		this.auditedBy = auditedBy;
	}

	public void setAuditedUser(QisUserPersonel auditedUser) {
		this.auditedUser = auditedUser;
	}

	public Long getId() {
		return id;
	}

	public double getAdvancePayment() {
		return advancePayment;
	}

	public void setAdvancePayment(double advancePayment) {
		this.advancePayment = advancePayment;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Calendar getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Calendar paymentDate) {
		this.paymentDate = paymentDate;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentBank() {
		return paymentBank;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}

	public double getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(double otherAmount) {
		this.otherAmount = otherAmount;
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

	public QisUserPersonel getPreparedUser() {
		return preparedUser;
	}

	public void setPreparedUser(QisUserPersonel preparedUser) {
		this.preparedUser = preparedUser;
	}

	public QisUserPersonel getVerifiedUser() {
		return verifiedUser;
	}

	public void setVerifiedUser(QisUserPersonel verifiedUser) {
		this.verifiedUser = verifiedUser;
	}

	public double getTaxWithHeld() {
		return taxWithHeld;
	}

	public void setTaxWithHeld(double taxWithHeld) {
		this.taxWithHeld = taxWithHeld;
	}
	
			
}
