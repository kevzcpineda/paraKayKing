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
@Table(name = "qis_transaction_laboratory_he_aptt")
public class QisTransactionLabAPTT implements Serializable {

	private static final long serialVersionUID = 6394607949109747712L;

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
	
	public QisTransactionLabAPTT() {
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
