package quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_physical_exam")
public class QisTransactionLabPhysicalExam implements Serializable {

	private static final long serialVersionUID = -7735260450884991759L;

	@JsonIgnore
	@Id
	private Long id;

	@NotNull(message = "Skin value is required.")
	@Column(name = "skin", nullable = false, columnDefinition = "boolean default true")
	private Boolean skin = true;

	@NotNull(message = "Head and Neck value is required.")
	@Column(name = "head_neck", nullable = false, columnDefinition = "boolean default true")
	private Boolean headNeck = true;

	@NotNull(message = "Chest/Breast/Lungs value is required.")
	@Column(name = "chest_breast_lungs", nullable = false, columnDefinition = "boolean default true")
	private Boolean chestBreastLungs = true;

	@NotNull(message = "Cardiac/Heart value is required.")
	@Column(name = "cardiac_heart", nullable = false, columnDefinition = "boolean default true")
	private Boolean cardiacHeart = true;

	@NotNull(message = "Abdomen value is required.")
	@Column(name = "abdomen", nullable = false, columnDefinition = "boolean default true")
	private Boolean abdomen = true;

	@NotNull(message = "Extremities value is required.")
	@Column(name = "extremities", nullable = false, columnDefinition = "boolean default true")
	private Boolean extremities = true;

	@NotNull(message = "Remarks value is required.")
	@Column(name = "remarks", nullable = false, columnDefinition = "boolean default false")
	private Boolean remarks = false;

	@Size(max = 250, message = "Notes must not exceed to {max} characters.")
	@Column(name = "notes", nullable = true, length = 250)
	private String notes;

	@Size(max = 250, message = "Findings must not exceed to {max} characters.")
	@Column(name = "findings", nullable = true, length = 250)
	private String findings;

	@JsonIgnore
	@NotNull(message = "Doctor Id is required.")
	@Column(name = "doctor_id", nullable = false)
	private Long doctorId;
	@ManyToOne(optional = false)
	@JoinColumn(name = "doctor_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisDoctor doctor;

	@NotNull(message = "License is required.")
	@Size(max = 20, message = "License should not exceed {max} characters.")
	@Column(name = "license", nullable = false, length = 20)
	private String license;

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

	@ManyToOne(optional = true)
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUser conductedBy;
	
	@NotNull(message = "Fatigue/Aches/Pains value is required.")
	@Column(name = "fatigueachespains", nullable = false, columnDefinition = "boolean default true")
	private Boolean fatigueachespains = true;

	public QisTransactionLabPhysicalExam() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getSkin() {
		return skin;
	}

	public void setSkin(Boolean skin) {
		this.skin = skin;
	}

	public Boolean getHeadNeck() {
		return headNeck;
	}

	public void setHeadNeck(Boolean headNeck) {
		this.headNeck = headNeck;
	}

	public Boolean getChestBreastLungs() {
		return chestBreastLungs;
	}

	public void setChestBreastLungs(Boolean chestBreastLungs) {
		this.chestBreastLungs = chestBreastLungs;
	}

	public Boolean getCardiacHeart() {
		return cardiacHeart;
	}

	public void setCardiacHeart(Boolean cardiacHeart) {
		this.cardiacHeart = cardiacHeart;
	}

	public Boolean getAbdomen() {
		return abdomen;
	}

	public void setAbdomen(Boolean abdomen) {
		this.abdomen = abdomen;
	}

	public Boolean getExtremities() {
		return extremities;
	}

	public void setExtremities(Boolean extremities) {
		this.extremities = extremities;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getFindings() {
		return findings;
	}

	public void setFindings(String findings) {
		this.findings = findings;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public QisDoctor getDoctor() {
		return doctor;
	}

	public void setDoctor(QisDoctor doctor) {
		this.doctor = doctor;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
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

	public Boolean getRemarks() {
		return remarks;
	}

	public void setRemarks(Boolean remarks) {
		this.remarks = remarks;
	}

	public QisUser getConductedBy() {
		return conductedBy;
	}

	public void setConductedBy(QisUser conductedBy) {
		this.conductedBy = conductedBy;
	}

	public Boolean getFatigueachespains() {
		return fatigueachespains;
	}

	public void setFatigueachespains(Boolean fatigueachespains) {
		this.fatigueachespains = fatigueachespains;
	}

}
