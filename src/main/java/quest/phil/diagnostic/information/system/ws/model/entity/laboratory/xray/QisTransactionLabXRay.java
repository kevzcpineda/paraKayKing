package quest.phil.diagnostic.information.system.ws.model.entity.laboratory.xray;

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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_xray")
public class QisTransactionLabXRay implements Serializable {

	private static final long serialVersionUID = -4768679659846930153L;

	@JsonIgnore
	@Id
	private Long id;

	@NotNull(message = "Findings is required.")
	@NotEmpty(message = "Findings is required.")
	@Size(max = 1000, message = "Findings must not exceed to {max} characters.")
	@Column(name = "findings", nullable = true, length = 1000)
	private String findings;

	@NotNull(message = "Impressions is required.")
	@NotEmpty(message = "Impressions is required.")
	@Size(max = 250, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "impressions", nullable = true, length = 250)
	private String impressions;

	@JsonIgnore
	@NotNull(message = "Radiologist Id is required.")
	@Column(name = "radiologist_id", nullable = false)
	private Long radiologistId;
	@ManyToOne(optional = false)
	@JoinColumn(name = "radiologist_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisDoctor radiologist;

	@NotNull(message = "Remarks value is required.")
	@Column(name = "remarks", nullable = false, columnDefinition = "boolean default false")
	private Boolean remarks = false;

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
	private QisUserPersonel technician;
	
	@JsonIgnore
	@Column(name = "molecular_lab_id", nullable = true)
	private Long referenceLabId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "molecular_lab_id", insertable = false, updatable =false)
	@JsonManagedReference 
	private QisReferenceLaboratory referenceLab;

	public QisTransactionLabXRay() {
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

	public String getFindings() {
		return findings;
	}

	public void setFindings(String findings) {
		this.findings = findings;
	}

	public String getImpressions() {
		return impressions;
	}

	public void setImpressions(String impressions) {
		this.impressions = impressions;
	}

	public Long getRadiologistId() {
		return radiologistId;
	}

	public void setRadiologistId(Long radiologistId) {
		this.radiologistId = radiologistId;
	}

	public QisDoctor getRadiologist() {
		return radiologist;
	}

	public void setRadiologist(QisDoctor radiologist) {
		this.radiologist = radiologist;
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

	public Boolean getRemarks() {
		return remarks;
	}

	public void setRemarks(Boolean remarks) {
		this.remarks = remarks;
	}

	public QisUserPersonel getTechnician() {
		return technician;
	}

	public void setTechnician(QisUserPersonel technician) {
		this.technician = technician;
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
