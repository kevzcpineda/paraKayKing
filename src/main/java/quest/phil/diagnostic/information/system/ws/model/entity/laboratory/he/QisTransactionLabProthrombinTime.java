package quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_he_prothrombin_time")
public class QisTransactionLabProthrombinTime implements Serializable {

	private static final long serialVersionUID = 4392708041397186860L;

	@JsonIgnore
	@Id
	private Long id;
	
	@Column(name = "patient_time", nullable = true)
	private Float patientTime;
	
	@Size(max = 20, message = "Patient Time Normal Value should not exceed {max} characters.")
	@Column(name = "patient_time_nv", nullable = true, length = 20)
	private String patientTimeNV;

	@Column(name = "control", nullable = true)
	private Float control;
	
	@Size(max = 20, message = "Control Normal Value should not exceed {max} characters.")
	@Column(name = "control_nv", nullable = true, length = 20)
	private String controlNV;
	
	@Column(name = "percent_activity", nullable = true)
	private Float percentActivity;

	@Size(max = 20, message = "Percent Activity Normal Value should not exceed {max} characters.")
	@Column(name = "percent_activity_nv", nullable = true, length = 20)
	private String percentActivityNV;
	
	@Column(name = "inr", nullable = true)
	private Float inr;

	@Size(max = 20, message = "INR Normal Value should not exceed {max} characters.")
	@Column(name = "inr_nv", nullable = true, length = 20)
	private String inrNV;
	
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
	
	@JsonIgnore
	@Column(name = "molecular_lab_id", nullable = true)
	private Long referenceLabId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "molecular_lab_id", insertable = false, updatable =false)
	@JsonManagedReference 
	private QisReferenceLaboratory referenceLab;

	public QisTransactionLabProthrombinTime() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getPatientTime() {
		return patientTime;
	}

	public void setPatientTime(Float patientTime) {
		this.patientTime = patientTime;
	}

	public String getPatientTimeNV() {
		return patientTimeNV;
	}

	public void setPatientTimeNV(String patientTimeNV) {
		this.patientTimeNV = patientTimeNV;
	}

	public Float getControl() {
		return control;
	}

	public void setControl(Float control) {
		this.control = control;
	}

	public String getControlNV() {
		return controlNV;
	}

	public void setControlNV(String controlNV) {
		this.controlNV = controlNV;
	}

	public Float getPercentActivity() {
		return percentActivity;
	}

	public void setPercentActivity(Float percentActivity) {
		this.percentActivity = percentActivity;
	}

	public String getPercentActivityNV() {
		return percentActivityNV;
	}

	public void setPercentActivityNV(String percentActivityNV) {
		this.percentActivityNV = percentActivityNV;
	}

	public Float getInr() {
		return inr;
	}

	public void setInr(Float inr) {
		this.inr = inr;
	}

	public String getInrNV() {
		return inrNV;
	}

	public void setInrNV(String inrNV) {
		this.inrNV = inrNV;
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
