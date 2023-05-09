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
@Table(name = "qis_transaction_laboratory_cm_urine_chemical")
public class QisTransactionLabUrineChemical implements Serializable {

	private static final long serialVersionUID = -4857417569824772509L;

	@JsonIgnore
	@Id
	private Long id;

	@Size(max = 4, message = "Color should not exceed {max} characters.")
	@Column(name = "color", nullable = true, length = 4)
	private String color;

	@Size(max = 4, message = "Transparency should not exceed {max} characters.")
	@Column(name = "transparency", nullable = true, length = 4)
	private String transparency;

	@Size(max = 8, message = "RBC should not exceed {max} characters.")
	@Column(name = "RBC", nullable = true, length = 8)
	private String RBC;

	@Size(max = 8, message = "WBC should not exceed {max} characters.")
	@Column(name = "WBC", nullable = true, length = 8)
	private String WBC;

	@Size(max = 4, message = "E.Cells should not exceed {max} characters.")
	@Column(name = "eCells", nullable = true, length = 4)
	private String eCells;

	@Size(max = 4, message = "M.Threads should not exceed {max} characters.")
	@Column(name = "mThreads", nullable = true, length = 4)
	private String mThreads;

	@Size(max = 4, message = "Bacteria should not exceed {max} characters.")
	@Column(name = "bacteria", nullable = true, length = 4)
	private String bacteria;

	@Size(max = 4, message = "Amorphous should not exceed {max} characters.")
	@Column(name = "amorphous", nullable = true, length = 4)
	private String amorphous;

	@Size(max = 4, message = "CaOX should not exceed {max} characters.")
	@Column(name = "caOX", nullable = true, length = 4)
	private String caOX;
	
	@Column(name = "ph", nullable = true)
	private Float ph;

	@Column(name = "spGravity", nullable = true)
	private Float spGravity;
	
	@Size(max = 4, message = "Protien should not exceed {max} characters.")
	@Column(name = "protien", nullable = true, length = 4)
	private String protien;

	@Size(max = 4, message = "Glucose should not exceed {max} characters.")
	@Column(name = "glucose", nullable = true, length = 4)
	private String glucose;

	@Size(max = 4, message = "Leukocyte Esterase should not exceed {max} characters.")
	@Column(name = "leukocyte_esterase", nullable = true, length = 4)
	private String leukocyteEsterase;

	@Column(name = "nitrite", nullable = true, columnDefinition = "boolean default null")
	private Boolean nitrite;

	@Size(max = 4, message = "Urobilinogen should not exceed {max} characters.")
	@Column(name = "urobilinogen", nullable = true, length = 4)
	private String urobilinogen;

	@Size(max = 4, message = "Blood should not exceed {max} characters.")
	@Column(name = "blood", nullable = true, length = 4)
	private String blood;

	@Size(max = 4, message = "Ketone should not exceed {max} characters.")
	@Column(name = "ketone", nullable = true, length = 4)
	private String ketone;

	@Size(max = 4, message = "Bilirubin should not exceed {max} characters.")
	@Column(name = "bilirubin", nullable = true, length = 4)
	private String bilirubin;

	@Size(max = 200, message = "Other Notes should not exceed {max} characters.")
	@Column(name = "other_notes", nullable = true, length = 200)
	private String otherNotes;
	
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
	
	public QisTransactionLabUrineChemical() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTransparency() {
		return transparency;
	}

	public void setTransparency(String transparency) {
		this.transparency = transparency;
	}

	public String getRBC() {
		return RBC;
	}

	public void setRBC(String rBC) {
		RBC = rBC;
	}

	public String getWBC() {
		return WBC;
	}

	public void setWBC(String wBC) {
		WBC = wBC;
	}

	public String geteCells() {
		return eCells;
	}

	public void seteCells(String eCells) {
		this.eCells = eCells;
	}

	public String getmThreads() {
		return mThreads;
	}

	public void setmThreads(String mThreads) {
		this.mThreads = mThreads;
	}

	public String getBacteria() {
		return bacteria;
	}

	public void setBacteria(String bacteria) {
		this.bacteria = bacteria;
	}

	public String getAmorphous() {
		return amorphous;
	}

	public void setAmorphous(String amorphous) {
		this.amorphous = amorphous;
	}

	public String getCaOX() {
		return caOX;
	}

	public void setCaOX(String caOX) {
		this.caOX = caOX;
	}

	public Float getPh() {
		return ph;
	}

	public void setPh(Float ph) {
		this.ph = ph;
	}

	public Float getSpGravity() {
		return spGravity;
	}

	public void setSpGravity(Float spGravity) {
		this.spGravity = spGravity;
	}

	public String getProtien() {
		return protien;
	}

	public void setProtien(String protien) {
		this.protien = protien;
	}

	public String getGlucose() {
		return glucose;
	}

	public void setGlucose(String glucose) {
		this.glucose = glucose;
	}

	public String getLeukocyteEsterase() {
		return leukocyteEsterase;
	}

	public void setLeukocyteEsterase(String leukocyteEsterase) {
		this.leukocyteEsterase = leukocyteEsterase;
	}

	public Boolean getNitrite() {
		return nitrite;
	}

	public void setNitrite(Boolean nitrite) {
		this.nitrite = nitrite;
	}

	public String getUrobilinogen() {
		return urobilinogen;
	}

	public void setUrobilinogen(String urobilinogen) {
		this.urobilinogen = urobilinogen;
	}

	public String getBlood() {
		return blood;
	}

	public void setBlood(String blood) {
		this.blood = blood;
	}

	public String getKetone() {
		return ketone;
	}

	public void setKetone(String ketone) {
		this.ketone = ketone;
	}

	public String getBilirubin() {
		return bilirubin;
	}

	public void setBilirubin(String bilirubin) {
		this.bilirubin = bilirubin;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
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
