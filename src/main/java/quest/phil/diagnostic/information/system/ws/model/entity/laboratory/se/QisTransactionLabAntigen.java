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
@Table(name = "qis_transaction_laboratory_se_antigen")
public class QisTransactionLabAntigen implements Serializable {

	private static final long serialVersionUID = -838382350077232477L;

	@JsonIgnore
	@Id
	private Long id;

	@Column(name = "psa", nullable = true)
	private Float psa;

	@Column(name = "cea", nullable = true)
	private Float cea;

	@Column(name = "afp", nullable = true)
	private Float afp;

	@Column(name = "ca125", nullable = true)
	private Float ca125;

	@Column(name = "ca199", nullable = true)
	private Float ca199;

	@Column(name = "ca153", nullable = true)
	private Float ca153;

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

	public QisTransactionLabAntigen() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getPsa() {
		return psa;
	}

	public void setPsa(Float psa) {
		this.psa = psa;
	}

	public Float getCea() {
		return cea;
	}

	public void setCea(Float cea) {
		this.cea = cea;
	}

	public Float getAfp() {
		return afp;
	}

	public void setAfp(Float afp) {
		this.afp = afp;
	}

	public Float getCa125() {
		return ca125;
	}

	public void setCa125(Float ca125) {
		this.ca125 = ca125;
	}

	public Float getCa199() {
		return ca199;
	}

	public void setCa199(Float ca199) {
		this.ca199 = ca199;
	}

	public Float getCa153() {
		return ca153;
	}

	public void setCa153(Float ca153) {
		this.ca153 = ca153;
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

	public Long getMolecularLab() {
		return referenceLabId;
	}

	public void setMolecularLab(Long molecularLab) {
		this.referenceLabId = molecularLab;
	}

	public QisReferenceLaboratory getReferenceLab() {
		return referenceLab;
	}

	public void setReferenceLab(QisReferenceLaboratory referenceLab) {
		this.referenceLab = referenceLab;
	}

	public Long getReferenceLabId() {
		return referenceLabId;
	}

	public void setReferenceLabId(Long referenceLabId) {
		this.referenceLabId = referenceLabId;
	}

}
