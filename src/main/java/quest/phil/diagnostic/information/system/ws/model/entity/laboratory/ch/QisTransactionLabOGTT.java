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
@Table(name = "qis_transaction_laboratory_ch_ogtt")
public class QisTransactionLabOGTT implements Serializable {

	private static final long serialVersionUID = -2072217723699954921L;

	@JsonIgnore
	@Id
	private Long id;

	@Column(name = "ogtt_1hr", nullable = true)
	private Float ogtt1Hr;

	@Column(name = "ogtt_1hr_conventional", nullable = true)
	private Float ogtt1HrConventional;

	@Column(name = "ogtt_2hr", nullable = true)
	private Float ogtt2Hr;

	@Column(name = "ogtt_2hr_conventional", nullable = true)
	private Float ogtt2HrConventional;

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
	
	public QisTransactionLabOGTT() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getOgtt1Hr() {
		return ogtt1Hr;
	}

	public void setOgtt1Hr(Float ogtt1Hr) {
		this.ogtt1Hr = ogtt1Hr;
	}

	public Float getOgtt1HrConventional() {
		return ogtt1HrConventional;
	}

	public void setOgtt1HrConventional(Float ogtt1HrConventional) {
		this.ogtt1HrConventional = ogtt1HrConventional;
	}

	public Float getOgtt2Hr() {
		return ogtt2Hr;
	}

	public void setOgtt2Hr(Float ogtt2Hr) {
		this.ogtt2Hr = ogtt2Hr;
	}

	public Float getOgtt2HrConventional() {
		return ogtt2HrConventional;
	}

	public void setOgtt2HrConventional(Float ogtt2HrConventional) {
		this.ogtt2HrConventional = ogtt2HrConventional;
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
