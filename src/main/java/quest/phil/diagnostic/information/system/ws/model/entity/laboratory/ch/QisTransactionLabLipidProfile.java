package quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch;

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

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_ch_lipid_profile")
public class QisTransactionLabLipidProfile implements Serializable {

	private static final long serialVersionUID = 6358715334363246816L;

	@JsonIgnore
	@Id
	private Long id;

	@Column(name = "cholesterol", nullable = true)
	private Float cholesterol;

	@Column(name = "cholesterol_conventional", nullable = true)
	private Float cholesterolConventional;

	@Column(name = "triglycerides", nullable = true)
	private Float triglycerides;

	@Column(name = "triglycerides_conventional", nullable = true)
	private Float triglyceridesConventional;

	@Column(name = "hdl", nullable = true)
	private Float hdl;

	@Column(name = "hdl_conventional", nullable = true)
	private Float hdlConventional;

	@Column(name = "ldl", nullable = true)
	private Float ldl;

	@Column(name = "ldl_conventional", nullable = true)
	private Float ldlConventional;

	@Column(name = "hdl_ratio", nullable = true)
	private Float hdlRatio;

	@Column(name = "vldl", nullable = true)
	private Float vldl;

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

	public QisTransactionLabLipidProfile() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getCholesterol() {
		return cholesterol;
	}

	public void setCholesterol(Float cholesterol) {
		this.cholesterol = cholesterol;
	}

	public Float getCholesterolConventional() {
		return cholesterolConventional;
	}

	public void setCholesterolConventional(Float cholesterolConventional) {
		this.cholesterolConventional = cholesterolConventional;
	}

	public Float getTriglycerides() {
		return triglycerides;
	}

	public void setTriglycerides(Float triglycerides) {
		this.triglycerides = triglycerides;
	}

	public Float getTriglyceridesConventional() {
		return triglyceridesConventional;
	}

	public void setTriglyceridesConventional(Float triglyceridesConventional) {
		this.triglyceridesConventional = triglyceridesConventional;
	}

	public Float getHdl() {
		return hdl;
	}

	public void setHdl(Float hdl) {
		this.hdl = hdl;
	}

	public Float getHdlConventional() {
		return hdlConventional;
	}

	public void setHdlConventional(Float hdlConventional) {
		this.hdlConventional = hdlConventional;
	}

	public Float getLdl() {
		return ldl;
	}

	public void setLdl(Float ldl) {
		this.ldl = ldl;
	}

	public Float getLdlConventional() {
		return ldlConventional;
	}

	public void setLdlConventional(Float ldlConventional) {
		this.ldlConventional = ldlConventional;
	}

	public Float getHdlRatio() {
		return hdlRatio;
	}

	public void setHdlRatio(Float hdlRatio) {
		this.hdlRatio = hdlRatio;
	}

	public Float getVldl() {
		return vldl;
	}

	public void setVldl(Float vldl) {
		this.vldl = vldl;
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
