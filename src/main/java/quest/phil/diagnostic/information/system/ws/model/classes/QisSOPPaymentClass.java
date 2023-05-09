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
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;

@MappedSuperclass
public class QisSOPPaymentClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@Column(name = "reference_id", nullable = true)
	private Long referenceId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "reference_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisReferenceLaboratory referenceLab;

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
	
	@Column(name = "other_amount", nullable = false, columnDefinition = "double DEFAULT 0")
	private double otherAmount = 0;
	
	@Column(name = "taxWithHeld", nullable = false, columnDefinition = "double DEFAULT 0")
	private double taxWithHeld = 0;
	
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
	
	public QisSOPPaymentClass() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public Long getId() {
		return id;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public QisReferenceLaboratory getReferenceLab() {
		return referenceLab;
	}

	public Calendar getPaymentDate() {
		return paymentDate;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public String getPaymentBank() {
		return paymentBank;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public double getOtherAmount() {
		return otherAmount;
	}

	public double getTaxWithHeld() {
		return taxWithHeld;
	}

	public Calendar getVerifiedDate() {
		return verifiedDate;
	}

	public Long getVerifiedBy() {
		return verifiedBy;
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

	public QisUserPersonel getPreparedUser() {
		return preparedUser;
	}

	public QisUserPersonel getVerifiedUser() {
		return verifiedUser;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public void setReferenceLab(QisReferenceLaboratory referenceLab) {
		this.referenceLab = referenceLab;
	}

	public void setPaymentDate(Calendar paymentDate) {
		this.paymentDate = paymentDate;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}

	public void setOtherAmount(double otherAmount) {
		this.otherAmount = otherAmount;
	}

	public void setTaxWithHeld(double taxWithHeld) {
		this.taxWithHeld = taxWithHeld;
	}

	public void setVerifiedDate(Calendar verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public void setVerifiedBy(Long verifiedBy) {
		this.verifiedBy = verifiedBy;
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

	public void setPreparedUser(QisUserPersonel preparedUser) {
		this.preparedUser = preparedUser;
	}

	public void setVerifiedUser(QisUserPersonel verifiedUser) {
		this.verifiedUser = verifiedUser;
	}
	
}
