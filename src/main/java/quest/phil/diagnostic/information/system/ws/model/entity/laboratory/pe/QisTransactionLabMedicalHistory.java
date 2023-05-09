package quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_medical_history")
public class QisTransactionLabMedicalHistory implements Serializable {

	private static final long serialVersionUID = -4990148005441166749L;

	@JsonIgnore
	@Id
	private Long id;

	@Column(name = "asthma", nullable = false, columnDefinition = "boolean default false")
	private Boolean asthma = false;

	@Column(name = "tuberculosis", nullable = false, columnDefinition = "boolean default false")
	private Boolean tuberculosis = false;

	@Column(name = "diabetes_mellitus", nullable = false, columnDefinition = "boolean default false")
	private Boolean diabetesMellitus = false;

	@Column(name = "high_blood_pressure", nullable = false, columnDefinition = "boolean default false")
	private Boolean highBloodPressure = false;

	@Column(name = "heart_problem", nullable = false, columnDefinition = "boolean default false")
	private Boolean heartProblem = false;

	@Column(name = "kidney_problem", nullable = false, columnDefinition = "boolean default false")
	private Boolean kidneyProblem = false;

	@Column(name = "abdominal_hernia", nullable = false, columnDefinition = "boolean default false")
	private Boolean abdominalHernia = false;

	@Column(name = "joint_back_scoliosis", nullable = false, columnDefinition = "boolean default false")
	private Boolean jointBackScoliosis = false;

	@Column(name = "psychiatric_problem", nullable = false, columnDefinition = "boolean default false")
	private Boolean psychiatricProblem = false;

	@Column(name = "migraine_headache", nullable = false, columnDefinition = "boolean default false")
	private Boolean migraineHeadache = false;

	@Column(name = "fainting_seizures", nullable = false, columnDefinition = "boolean default false")
	private Boolean faintingSeizures = false;

	@Column(name = "allergies", nullable = false, columnDefinition = "boolean default false")
	private Boolean allergies = false;

	@Column(name = "cancer_tumor", nullable = false, columnDefinition = "boolean default false")
	private Boolean cancerTumor = false;

	@Column(name = "hepatitis", nullable = false, columnDefinition = "boolean default false")
	private Boolean hepatitis = false;

	@Column(name = "std_plhiv", nullable = false, columnDefinition = "boolean default false")
	private Boolean stdplhiv = false;
	
	@Column(name = "travel_history", nullable = false, columnDefinition = "boolean default false")
	private Boolean travelhistory = false;

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

	public QisTransactionLabMedicalHistory() {
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

	public Boolean getAsthma() {
		return asthma;
	}

	public void setAsthma(Boolean asthma) {
		this.asthma = asthma;
	}

	public Boolean getTuberculosis() {
		return tuberculosis;
	}

	public void setTuberculosis(Boolean tuberculosis) {
		this.tuberculosis = tuberculosis;
	}

	public Boolean getDiabetesMellitus() {
		return diabetesMellitus;
	}

	public void setDiabetesMellitus(Boolean diabetesMellitus) {
		this.diabetesMellitus = diabetesMellitus;
	}

	public Boolean getHighBloodPressure() {
		return highBloodPressure;
	}

	public void setHighBloodPressure(Boolean highBloodPressure) {
		this.highBloodPressure = highBloodPressure;
	}

	public Boolean getHeartProblem() {
		return heartProblem;
	}

	public void setHeartProblem(Boolean heartProblem) {
		this.heartProblem = heartProblem;
	}

	public Boolean getKidneyProblem() {
		return kidneyProblem;
	}

	public void setKidneyProblem(Boolean kidneyProblem) {
		this.kidneyProblem = kidneyProblem;
	}

	public Boolean getAbdominalHernia() {
		return abdominalHernia;
	}

	public void setAbdominalHernia(Boolean abdominalHernia) {
		this.abdominalHernia = abdominalHernia;
	}

	public Boolean getJointBackScoliosis() {
		return jointBackScoliosis;
	}

	public void setJointBackScoliosis(Boolean jointBackScoliosis) {
		this.jointBackScoliosis = jointBackScoliosis;
	}

	public Boolean getPsychiatricProblem() {
		return psychiatricProblem;
	}

	public void setPsychiatricProblem(Boolean psychiatricProblem) {
		this.psychiatricProblem = psychiatricProblem;
	}

	public Boolean getMigraineHeadache() {
		return migraineHeadache;
	}

	public void setMigraineHeadache(Boolean migraineHeadache) {
		this.migraineHeadache = migraineHeadache;
	}

	public Boolean getFaintingSeizures() {
		return faintingSeizures;
	}

	public void setFaintingSeizures(Boolean faintingSeizures) {
		this.faintingSeizures = faintingSeizures;
	}

	public Boolean getAllergies() {
		return allergies;
	}

	public void setAllergies(Boolean allergies) {
		this.allergies = allergies;
	}

	public Boolean getCancerTumor() {
		return cancerTumor;
	}

	public void setCancerTumor(Boolean cancerTumor) {
		this.cancerTumor = cancerTumor;
	}

	public Boolean getHepatitis() {
		return hepatitis;
	}

	public void setHepatitis(Boolean hepatitis) {
		this.hepatitis = hepatitis;
	}

	public Boolean getStdplhiv() {
		return stdplhiv;
	}

	public void setStdplhiv(Boolean stdplhiv) {
		this.stdplhiv = stdplhiv;
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

	public Boolean getTravelhistory() {
		return travelhistory;
	}

	public void setTravelhistory(Boolean travelhistory) {
		this.travelhistory = travelhistory;
	}
}
