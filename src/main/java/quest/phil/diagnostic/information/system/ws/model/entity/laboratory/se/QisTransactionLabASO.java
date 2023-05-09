package quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se;

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
@Table(name = "qis_transaction_laboratory_se_aso")
public class QisTransactionLabASO implements Serializable  {

	private static final long serialVersionUID = 2168490240046709907L;

	@JsonIgnore
	@Id
	private Long id;
	
	@Column(name = "result1", nullable = true, columnDefinition = "boolean default null")
	private Boolean result1;
	
	@Column(name = "result2", nullable = true, columnDefinition = "boolean default null")
	private Boolean result2;
	
	@Column(name = "result3", nullable = true, columnDefinition = "boolean default null")
	private Boolean result3;
	
	@Column(name = "result4", nullable = true, columnDefinition = "boolean default null")
	private Boolean result4;
	
	@Column(name = "result5", nullable = true, columnDefinition = "boolean default null")
	private Boolean result5;
	
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
	
	public QisTransactionLabASO() {
		super();
	}
	public Long getId() {
		return id;
	}
	public Boolean getResult1() {
		return result1;
	}
	public Boolean getResult2() {
		return result2;
	}
	public Boolean getResult3() {
		return result3;
	}
	public Boolean getResult4() {
		return result4;
	}
	public Boolean getResult5() {
		return result5;
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
	public Long getReferenceLabId() {
		return referenceLabId;
	}
	public QisReferenceLaboratory getReferenceLab() {
		return referenceLab;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setResult1(Boolean result1) {
		this.result1 = result1;
	}
	public void setResult2(Boolean result2) {
		this.result2 = result2;
	}
	public void setResult3(Boolean result3) {
		this.result3 = result3;
	}
	public void setResult4(Boolean result4) {
		this.result4 = result4;
	}
	public void setResult5(Boolean result5) {
		this.result5 = result5;
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
	public void setReferenceLabId(Long referenceLabId) {
		this.referenceLabId = referenceLabId;
	}
	public void setReferenceLab(QisReferenceLaboratory referenceLab) {
		this.referenceLab = referenceLab;
	}
}
