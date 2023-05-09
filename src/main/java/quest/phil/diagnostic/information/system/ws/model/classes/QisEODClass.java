package quest.phil.diagnostic.information.system.ws.model.classes;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;


@Entity
@DynamicUpdate
@Table(name = "qis_eod",  uniqueConstraints = { @UniqueConstraint(columnNames = { "id" })})
public class QisEODClass implements Serializable {

	private static final long serialVersionUID = 7233681434605477364L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	
	@JsonIgnore
	@Column(name = "branch_id", nullable = true, columnDefinition = "bigint default 1")
	private Long branchId = 1L;
	@ManyToOne(optional = false)
	@JoinColumn(name = "branch_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisBranch branch;
	
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
	
	@Column(name = "dateFrom", nullable = false, length = 120)
	private String dateFrom;
	
	@Column(name = "dateTo", nullable = false, length = 120)
	private String dateTo;
	
	@Column(name = "referenceNumber", nullable = true, length = 80)
	@Size(max = 80, message = "Reference Number should not exceed {max} characters.")
	private String referenceNumber = null;

	@Column(name = "other_notes", nullable = true, length = 120)
	@Size(max = 120, message = "Other Notes should not exceed {max} characters.")
	private String otherNotes = null;
	
	
	
	public QisEODClass() {
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

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	
	
}
