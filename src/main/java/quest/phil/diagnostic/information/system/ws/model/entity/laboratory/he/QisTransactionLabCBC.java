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
@Table(name = "qis_transaction_laboratory_he_cbc")
public class QisTransactionLabCBC implements Serializable {

	private static final long serialVersionUID = 8432421756735240957L;

	@JsonIgnore
	@Id
	private Long id;

	@Column(name = "white_blood_cells", nullable = true)
	private Float whiteBloodCells;

	@Column(name = "basophils", nullable = true)
	private Float basophils;

	@Column(name = "neutrophils", nullable = true)
	private Float neutrophils;

	@Column(name = "red_blood_cells", nullable = true)
	private Float redBloodCells;

	@Column(name = "lymphocytes", nullable = true)
	private Float lymphocytes;

	@Column(name = "hemoglobin", nullable = true)
	private Float hemoglobin;

	@Column(name = "monocytes", nullable = true)
	private Float monocytes;

	@Column(name = "hematocrit", nullable = true)
	private Float hematocrit;

	@Column(name = "eosinophils", nullable = true)
	private Float eosinophils;

	@Column(name = "platelet", nullable = true)
	private Float platelet;

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
	
	public QisTransactionLabCBC() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getWhiteBloodCells() {
		return whiteBloodCells;
	}

	public void setWhiteBloodCells(Float whiteBloodCells) {
		this.whiteBloodCells = whiteBloodCells;
	}

	public Float getBasophils() {
		return basophils;
	}

	public void setBasophils(Float basophils) {
		this.basophils = basophils;
	}

	public Float getNeutrophils() {
		return neutrophils;
	}

	public void setNeutrophils(Float neutrophils) {
		this.neutrophils = neutrophils;
	}

	public Float getRedBloodCells() {
		return redBloodCells;
	}

	public void setRedBloodCells(Float redBloodCells) {
		this.redBloodCells = redBloodCells;
	}

	public Float getLymphocytes() {
		return lymphocytes;
	}

	public void setLymphocytes(Float lymphocytes) {
		this.lymphocytes = lymphocytes;
	}

	public Float getHemoglobin() {
		return hemoglobin;
	}

	public void setHemoglobin(Float hemoglobin) {
		this.hemoglobin = hemoglobin;
	}

	public Float getMonocytes() {
		return monocytes;
	}

	public void setMonocytes(Float monocytes) {
		this.monocytes = monocytes;
	}

	public Float getHematocrit() {
		return hematocrit;
	}

	public void setHematocrit(Float hematocrit) {
		this.hematocrit = hematocrit;
	}

	public Float getEosinophils() {
		return eosinophils;
	}

	public void setEosinophils(Float eosinophils) {
		this.eosinophils = eosinophils;
	}

	public Float getPlatelet() {
		return platelet;
	}

	public void setPlatelet(Float platelet) {
		this.platelet = platelet;
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
