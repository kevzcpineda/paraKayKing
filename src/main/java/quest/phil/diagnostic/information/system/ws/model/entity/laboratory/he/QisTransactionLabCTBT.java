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

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_he_ctbt")
public class QisTransactionLabCTBT implements Serializable {

	private static final long serialVersionUID = 3712127014110581888L;

	@JsonIgnore
	@Id
	private Long id;

	@Column(name = "clotting_time_min", nullable = true)
	private Integer clottingTimeMin;

	@Column(name = "clotting_time_sec", nullable = true)
	private Integer clottingTimeSec;

	@Column(name = "bleeding_time_min", nullable = true)
	private Integer bleedingTimeMin;

	@Column(name = "bleeding_time_sec", nullable = true)
	private Integer bleedingTimeSec;

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

	public QisTransactionLabCTBT() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getClottingTimeMin() {
		return clottingTimeMin;
	}

	public void setClottingTimeMin(Integer clottingTimeMin) {
		this.clottingTimeMin = clottingTimeMin;
	}

	public Integer getClottingTimeSec() {
		return clottingTimeSec;
	}

	public void setClottingTimeSec(Integer clottingTimeSec) {
		this.clottingTimeSec = clottingTimeSec;
	}

	public Integer getBleedingTimeMin() {
		return bleedingTimeMin;
	}

	public void setBleedingTimeMin(Integer bleedingTimeMin) {
		this.bleedingTimeMin = bleedingTimeMin;
	}

	public Integer getBleedingTimeSec() {
		return bleedingTimeSec;
	}

	public void setBleedingTimeSec(Integer bleedingTimeSec) {
		this.bleedingTimeSec = bleedingTimeSec;
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
