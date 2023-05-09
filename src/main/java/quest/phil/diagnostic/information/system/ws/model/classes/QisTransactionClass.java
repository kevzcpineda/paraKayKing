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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;
//import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferral;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;

@MappedSuperclass
public class QisTransactionClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NaturalId
	@Column(name = "transaction_id", nullable = false, length = 20, unique = true)
	private String transactionid;

	@Column(name = "transaction_date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar transactionDate;

	@Column(name = "status", nullable = false, columnDefinition = "varchar(4) default 'SHO'") // hold
	private String status = "SHO";

	@Column(name = "transaction_type", nullable = false, columnDefinition = "varchar(4) default 'TWI'") // walk in
	private String transactionType = "TWI";

	@Column(name = "dispatch", nullable = false, columnDefinition = "varchar(4) default 'PU'") // PU-PICK-UP, E-EMAIL,OL-ONLINE
	private String dispatch = "PU";
		
	@JsonIgnore
	@Column(name = "branch_id", nullable = false, columnDefinition = "bigint default 1")
	private Long branchId = 1L;
	@ManyToOne(optional = false)
	@JoinColumn(name = "branch_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisBranch branch;

	@JsonIgnore
	@Column(name = "patient_id", nullable = false)
	private Long patientId;
	@ManyToOne(optional = false)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisPatient patient;

	@JsonIgnore
	@Column(name = "referral_id", nullable = true)
	private Long referralId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "referral_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisReferral referral;

//	@JsonIgnore
//	@Column(name = "corporate_id", nullable = true)
//	private Long corporateId;
//	@ManyToOne(optional = true)
//	@JoinColumn(name = "corporate_id", insertable = false, updatable = false)
//	@JsonManagedReference
//	private QisCorporate corporate;

	@JsonIgnore
	@NotNull(message = "Cashier ID is required.")
	@Column(name = "cashier_id", nullable = false)
	private Long cashierId;
	@ManyToOne(optional = false)
	@JoinColumn(name = "cashier_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUser cashier;

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

	@Column(name = "discount_type", nullable = true, length = 4)
	private String discountType = null;

	@Column(name = "senior_citizen_id", nullable = true, length = 20)
	@Size(max = 20, message = "Senior Citizen ID should not exceed {max} characters.")
	private String seniorCitizenID;

	@Column(name = "pwd_id", nullable = true, length = 20)
	@Size(max = 20, message = "Person With Disability(PWD) ID should not exceed {max} characters.")
	private String pwdID;

	// 0-ACTIVE, 1-FULL REFUND, 2-ITEM REFUND
	@Column(name = "refund_status", nullable = false, columnDefinition = "int default 0")
	private int refundStatus = 0;

	@Column(name = "remarks", nullable = true, length = 80)
	@Size(max = 80, message = "Remarks should not exceed {max} characters.")
	private String remarks;

	@Column(name = "email_to", nullable = true, length = 120)
	@Size(max = 120, message = "Email To should not exceed {max} characters.")
	private String emailTo;
	
	@Column(name = "soa_status", columnDefinition = "boolean default false")
	private boolean soaStatus;
	
	public QisTransactionClass() {
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

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	public Calendar getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Calendar transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public QisBranch getBranch() {
		return branch;
	}

	public void setBranch(QisBranch branch) {
		this.branch = branch;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public QisPatient getPatient() {
		return patient;
	}

	public void setPatient(QisPatient patient) {
		this.patient = patient;
	}

//	public Long getCorporateId() {
//		return corporateId;
//	}
//
//	public void setCorporateId(Long corporateId) {
//		this.corporateId = corporateId;
//	}
//
//	public QisCorporate getCorporate() {
//		return corporate;
//	}
//
//	public void setCorporate(QisCorporate corporate) {
//		this.corporate = corporate;
//	}

	public Long getCashierId() {
		return cashierId;
	}

	public void setCashierId(Long cashierId) {
		this.cashierId = cashierId;
	}

	public QisUser getCashier() {
		return cashier;
	}

	public void setCashier(QisUser cashier) {
		this.cashier = cashier;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getSeniorCitizenID() {
		return seniorCitizenID;
	}

	public void setSeniorCitizenID(String seniorCitizenID) {
		this.seniorCitizenID = seniorCitizenID;
	}

	public String getPwdID() {
		return pwdID;
	}

	public void setPwdID(String pwdID) {
		this.pwdID = pwdID;
	}

	public int getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(int refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getReferralId() {
		return referralId;
	}

	public void setReferralId(Long referralId) {
		this.referralId = referralId;
	}

	public QisReferral getReferral() {
		return referral;
	}

	public void setReferral(QisReferral referral) {
		this.referral = referral;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public boolean isSoaStatus() {
		return soaStatus;
	}

	public void setSoaStatus(boolean soaStatus) {
		this.soaStatus = soaStatus;
	}
	
}
