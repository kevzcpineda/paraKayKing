package quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm;

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
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_cm_afb")
public class QisTransactionLabAFB implements Serializable {

	private static final long serialVersionUID = -291501229886044981L;

	@JsonIgnore
	@Id
	private Long id;

	@Size(max = 20, message = "Visual Appearance Specimen 1 should not exceed {max} characters.")
	@Column(name = "visualAppearance1", nullable = true, length = 20)
	private String visualAppearance1;

	@Size(max = 20, message = "Visual Appearance Specimen 2 should not exceed {max} characters.")
	@Column(name = "visualAppearance2", nullable = true, length = 20)
	private String visualAppearance2;

	@Size(max = 20, message = "Reading Specimen 1 should not exceed {max} characters.")
	@Column(name = "reading1", nullable = true, length = 20)
	private String reading1;

	@Size(max = 20, message = "Reading Specimen 2 should not exceed {max} characters.")
	@Column(name = "reading2", nullable = true, length = 20)
	private String reading2;

	@Size(max = 200, message = "Diagnosis should not exceed {max} characters.")
	@Column(name = "diagnosis", nullable = true, length = 200)
	private String diagnosis;

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

	public QisTransactionLabAFB() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVisualAppearance1() {
		return visualAppearance1;
	}

	public void setVisualAppearance1(String visualAppearance1) {
		this.visualAppearance1 = visualAppearance1;
	}

	public String getVisualAppearance2() {
		return visualAppearance2;
	}

	public void setVisualAppearance2(String visualAppearance2) {
		this.visualAppearance2 = visualAppearance2;
	}

	public String getReading1() {
		return reading1;
	}

	public void setReading1(String reading1) {
		this.reading1 = reading1;
	}

	public String getReading2() {
		return reading2;
	}

	public void setReading2(String reading2) {
		this.reading2 = reading2;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
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
