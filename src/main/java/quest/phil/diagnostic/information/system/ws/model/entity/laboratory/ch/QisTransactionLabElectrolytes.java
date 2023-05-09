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
@Table(name = "qis_transaction_laboratory_ch_electrolytes")
public class QisTransactionLabElectrolytes implements Serializable {

	private static final long serialVersionUID = 6604813567243095743L;

	@JsonIgnore
	@Id
	private Long id;

	@Column(name = "sodium", nullable = true)
	private Float sodium;

	@Column(name = "potassium", nullable = true)
	private Float potassium;

	@Column(name = "chloride", nullable = true)
	private Float chloride;

	@Column(name = "inorganic_phosphorus", nullable = true)
	private Float inorganicPhosphorus;

	@Column(name = "total_calcium", nullable = true)
	private Float totalCalcium;
	
	@Column(name = "total_iron", nullable = true)
	private Float totalIron;
	
	@Column(name = "ionized_calcium", nullable = true)
	private Float ionizedCalcium;
	
	@Column(name = "magnesium", nullable = true)
	private Float magnesium;
	
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
	
	public QisTransactionLabElectrolytes() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getSodium() {
		return sodium;
	}

	public void setSodium(Float sodium) {
		this.sodium = sodium;
	}

	public Float getPotassium() {
		return potassium;
	}

	public void setPotassium(Float potassium) {
		this.potassium = potassium;
	}

	public Float getChloride() {
		return chloride;
	}

	public void setChloride(Float chloride) {
		this.chloride = chloride;
	}

	public Float getInorganicPhosphorus() {
		return inorganicPhosphorus;
	}

	public void setInorganicPhosphorus(Float inorganicPhosphorus) {
		this.inorganicPhosphorus = inorganicPhosphorus;
	}

	public Float getTotalCalcium() {
		return totalCalcium;
	}

	public void setTotalCalcium(Float totalCalcium) {
		this.totalCalcium = totalCalcium;
	}

	public Float getIonizedCalcium() {
		return ionizedCalcium;
	}

	public void setIonizedCalcium(Float ionizedCalcium) {
		this.ionizedCalcium = ionizedCalcium;
	}

	public Float getMagnesium() {
		return magnesium;
	}

	public void setMagnesium(Float magnesium) {
		this.magnesium = magnesium;
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

	public Float getTotalIron() {
		return totalIron;
	}

	public void setTotalIron(Float totalIron) {
		this.totalIron = totalIron;
	}

}
