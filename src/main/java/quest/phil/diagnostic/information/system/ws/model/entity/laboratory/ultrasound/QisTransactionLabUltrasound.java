package quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ultrasound;

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
import javax.persistence.UniqueConstraint;
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
@Table(name = "qis_transaction_laboratory_ultrasound",  uniqueConstraints = { @UniqueConstraint(columnNames = { "id" })})
public class QisTransactionLabUltrasound implements Serializable {

	private static final long serialVersionUID = 6170765698400417072L;
	
	@JsonIgnore
	@Id
	private Long id;
	
	@Size(max = 10000, message = "Findings must not exceed to {max} characters.")
	@Column(name = "findings", nullable = true, length = 10000)
	private String findings = null;

	@Size(max = 10000, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "impressions", nullable = true, length = 10000)
	private String impressions = null;

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
	
	@Size(max = 1000, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "finding_header_pelvic", nullable = true, length = 1000)
	private String finding_header_pelvic = null;
	
	@Size(max = 1000, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "finding_footer_pelvic", nullable = true, length = 1000)
	private String finding_footer_pelvic = null;
	
	@Size(max = 11, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "bpd_size", nullable = true, length = 11)
	private String bpd_size = null;
	
	@Size(max = 255, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "bpd_old", nullable = true, length = 255)
	private String bpd_old = null;
	
	@Size(max = 11, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "hc_size", nullable = true, length = 11)
	private String hc_size = null;
	
	@Size(max = 255, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "hc_old", nullable = true, length = 255)
	private String hc_old = null;
	
	@Size(max = 11, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "ac_size", nullable = true, length = 11)
	private String ac_size = null;
	
	@Size(max = 255, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "ac_old", nullable = true, length = 255)
	private String ac_old = null;
	
	@Size(max = 11, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "fl_size", nullable = true, length = 11)
	private String fl_size = null;
	
	@Size(max = 255, message = "Impressions must not exceed to {max} characters.")
	@Column(name = "fl_old", nullable = true, length = 255)
	private String fl_old = null;
	
	@JsonIgnore
	@Column(name = "molecular_lab_id", nullable = true)
	private Long referenceLabId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "molecular_lab_id", insertable = false, updatable =false)
	@JsonManagedReference 
	private QisReferenceLaboratory referenceLab;
	
	public QisTransactionLabUltrasound() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
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

	public Long getId() {
		return id;
	}

	public String getFindings() {
		return findings;
	}

	public String getImpressions() {
		return impressions;
	}

	public Long getRadiologistId() {
		return radiologistId;
	}

	public QisDoctor getRadiologist() {
		return radiologist;
	}

	public Boolean getRemarks() {
		return remarks;
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

	public QisUserPersonel getTechnician() {
		return technician;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFindings(String findings) {
		this.findings = findings;
	}

	public void setImpressions(String impressions) {
		this.impressions = impressions;
	}

	public void setRadiologistId(Long radiologistId) {
		this.radiologistId = radiologistId;
	}

	public void setRadiologist(QisDoctor radiologist) {
		this.radiologist = radiologist;
	}

	public void setRemarks(Boolean remarks) {
		this.remarks = remarks;
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

	public void setTechnician(QisUserPersonel technician) {
		this.technician = technician;
	}

	public String getBpd_size() {
		return bpd_size;
	}

	public String getBpd_old() {
		return bpd_old;
	}

	public String getHc_size() {
		return hc_size;
	}

	public String getHc_old() {
		return hc_old;
	}

	public String getAc_size() {
		return ac_size;
	}

	public String getAc_old() {
		return ac_old;
	}

	public String getFl_size() {
		return fl_size;
	}

	public String getFl_old() {
		return fl_old;
	}

	public void setBpd_size(String bpd_size) {
		this.bpd_size = bpd_size;
	}

	public void setBpd_old(String bpd_old) {
		this.bpd_old = bpd_old;
	}

	public void setHc_size(String hc_size) {
		this.hc_size = hc_size;
	}

	public void setHc_old(String hc_old) {
		this.hc_old = hc_old;
	}

	public void setAc_size(String ac_size) {
		this.ac_size = ac_size;
	}

	public void setAc_old(String ac_old) {
		this.ac_old = ac_old;
	}

	public void setFl_size(String fl_size) {
		this.fl_size = fl_size;
	}

	public void setFl_old(String fl_old) {
		this.fl_old = fl_old;
	}

	public String getFinding_header_pelvic() {
		return finding_header_pelvic;
	}

	public String getFinding_footer_pelvic() {
		return finding_footer_pelvic;
	}

	public void setFinding_header_pelvic(String finding_header_pelvic) {
		this.finding_header_pelvic = finding_header_pelvic;
	}

	public void setFinding_footer_pelvic(String finding_footer_pelvic) {
		this.finding_footer_pelvic = finding_footer_pelvic;
	}
	
	
}
