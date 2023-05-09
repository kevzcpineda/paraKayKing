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
@Table(name = "qis_transaction_laboratory_ch_enzymes")
public class QisTransactionLabEnzymes implements Serializable {

	private static final long serialVersionUID = -4724424062855688840L;

	@JsonIgnore
	@Id
	private Long id;

	@Column(name = "sgpt_alt", nullable = true)
	private Float sgptAlt;

	@Column(name = "sgot_ast", nullable = true)
	private Float sgotAst;

	@Column(name = "amylase", nullable = true)
	private Float amylase;

	@Column(name = "lipase", nullable = true)
	private Float lipase;

	@Column(name = "ggtp", nullable = true)
	private Float ggtp;
	
	@Column(name = "ldh", nullable = true)
	private Float ldh;

	@Column(name = "alp", nullable = true)
	private Float alp;
	
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
	
	public QisTransactionLabEnzymes() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getSgptAlt() {
		return sgptAlt;
	}

	public void setSgptAlt(Float sgptAlt) {
		this.sgptAlt = sgptAlt;
	}

	public Float getSgotAst() {
		return sgotAst;
	}

	public void setSgotAst(Float sgotAst) {
		this.sgotAst = sgotAst;
	}

	public Float getAmylase() {
		return amylase;
	}

	public void setAmylase(Float amylase) {
		this.amylase = amylase;
	}

	public Float getLipase() {
		return lipase;
	}

	public void setLipase(Float lipase) {
		this.lipase = lipase;
	}

	public Float getGgtp() {
		return ggtp;
	}

	public void setGgtp(Float ggtp) {
		this.ggtp = ggtp;
	}

	public Float getLdh() {
		return ldh;
	}

	public void setLdh(Float ldh) {
		this.ldh = ldh;
	}

	public Float getAlp() {
		return alp;
	}

	public void setAlp(Float alp) {
		this.alp = alp;
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
