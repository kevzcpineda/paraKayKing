package quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_vital_sign")
public class QisTransactionLabVitalSign implements Serializable {

	private static final long serialVersionUID = 3792317213005538993L;

	@JsonIgnore
	@Id
	private Long id;

	@NotNull(message = "Height is required in centimeters(cm).")
	@Column(name = "height", nullable = false)
	public Float height = 0f;

	@NotNull(message = "Weight is required in kilograms(kg).")
	@Digits(integer = 5, fraction = 4)
	@Column(name = "weight", nullable = false)
	public Float weight = 0f;

	@NotNull(message = "Blood Pressure is required.")
	@Column(name = "bloodPressure", nullable = false, length = 12)
	public String bloodPressure;

	@NotNull(message = "Pulse Rate is required in centimeters(cm).")
	@Column(name = "pulse_rate", nullable = false, columnDefinition = "int default 0")
	public Integer pulseRate;

	@NotNull(message = "Respiratory Rate is required in centimeters(cm).")
	@Column(name = "respiratory_rate", nullable = false, columnDefinition = "int default 0")
	public Integer respiratoryRate;

//	@NotNull(message = "Uncorrected Oculus Dexter is required.")
	@Column(name = "uod", nullable = true, length = 12)
	public String uncorrectedOD = null;

//	@NotNull(message = "Uncorrected Oculus Sinister is required.")
	@Column(name = "uos", nullable = true, length = 12)
	public String uncorrectedOS = null;

	// @NotNull(message = "Corrected Oculus Dexter is required.")
	@Column(name = "cod", nullable = true, length = 12)
	public String correctedOD = null;

	// @NotNull(message = "Corrected Oculus Sinister is required.")
	@Column(name = "cos", nullable = true, length = 12)
	public String correctedOS = null;

	@Size(max = 120, message = "Ishihara must not exceed to {max} characters.")
	@Column(name = "ishihara", nullable = true, length = 120)
	public String ishihara;

	@Size(max = 120, message = "Hearing must not exceed to {max} characters.")
	@Column(name = "hearing", nullable = true, length = 120)
	public String hearing;

	@Size(max = 120, message = "Hospitalization must not exceed to {max} characters.")
	@Column(name = "hospitalization", nullable = true, length = 120)
	public String hospitalization;

	@Size(max = 120, message = "Operations must not exceed to {max} characters.")
	@Column(name = "opearations", nullable = true, length = 120)
	public String opearations;

	@Size(max = 120, message = "Medications must not exceed to {max} characters.")
	@Column(name = "medications", nullable = true, length = 120)
	public String medications;

	@Size(max = 120, message = "Smoker must not exceed to {max} characters.")
	@Column(name = "smoker", nullable = true, length = 120)
	public String smoker;

	@Size(max = 120, message = "Alcoholic must not exceed to {max} characters.")
	@Column(name = "alcoholic", nullable = true, length = 120)
	public String alcoholic;

	@Size(max = 120, message = "Last Menstrual must not exceed to {max} characters.")
	@Column(name = "last_menstrual", nullable = true, length = 120)
	public String lastMenstrual;

	@Column(name = "pregnant", nullable = true, columnDefinition = "boolean default null")
	public Boolean pregnant;

	@Size(max = 250, message = "Notes must not exceed to {max} characters.")
	@Column(name = "notes", nullable = true, length = 250)
	public String notes;

	@JsonIgnore
	@Column(name = "created_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar createdAt = Calendar.getInstance();

	@JsonIgnore
	@Column(name = "updated_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar updatedAt = Calendar.getInstance();

	@JsonIgnore
	@Column(name = "created_by", nullable = true)
	public Long createdBy = null;

	@JsonIgnore
	@Column(name = "updated_by", nullable = true)
	public Long updatedBy = null;

	@Column(name = "bmi", nullable = false)
	public Float bmi = 0f;

	@Column(name = "bmi_category", nullable = true, length = 20)
	public String bmiCategory;

	public QisTransactionLabVitalSign() {
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

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getBloodPressure() {
		return bloodPressure;
	}

	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	public Integer getPulseRate() {
		return pulseRate;
	}

	public void setPulseRate(Integer pulseRate) {
		this.pulseRate = pulseRate;
	}

	public Integer getRespiratoryRate() {
		return respiratoryRate;
	}

	public void setRespiratoryRate(Integer respiratoryRate) {
		this.respiratoryRate = respiratoryRate;
	}

	public String getUncorrectedOD() {
		return uncorrectedOD;
	}

	public void setUncorrectedOD(String uncorrectedOD) {
		this.uncorrectedOD = uncorrectedOD;
	}

	public String getUncorrectedOS() {
		return uncorrectedOS;
	}

	public void setUncorrectedOS(String uncorrectedOS) {
		this.uncorrectedOS = uncorrectedOS;
	}

	public String getCorrectedOD() {
		return correctedOD;
	}

	public void setCorrectedOD(String correctedOD) {
		this.correctedOD = correctedOD;
	}

	public String getCorrectedOS() {
		return correctedOS;
	}

	public void setCorrectedOS(String correctedOS) {
		this.correctedOS = correctedOS;
	}

	public String getIshihara() {
		return ishihara;
	}

	public void setIshihara(String ishihara) {
		this.ishihara = ishihara;
	}

	public String getHearing() {
		return hearing;
	}

	public void setHearing(String hearing) {
		this.hearing = hearing;
	}

	public String getHospitalization() {
		return hospitalization;
	}

	public void setHospitalization(String hospitalization) {
		this.hospitalization = hospitalization;
	}

	public String getOpearations() {
		return opearations;
	}

	public void setOpearations(String opearations) {
		this.opearations = opearations;
	}

	public String getMedications() {
		return medications;
	}

	public void setMedications(String medications) {
		this.medications = medications;
	}

	public String getSmoker() {
		return smoker;
	}

	public void setSmoker(String smoker) {
		this.smoker = smoker;
	}

	public String getAlcoholic() {
		return alcoholic;
	}

	public void setAlcoholic(String alcoholic) {
		this.alcoholic = alcoholic;
	}

	public String getLastMenstrual() {
		return lastMenstrual;
	}

	public void setLastMenstrual(String lastMenstrual) {
		this.lastMenstrual = lastMenstrual;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public Float getBmi() {
		return bmi;
	}

	public void setBmi(Float bmi) {
		this.bmi = bmi;
	}

	public String getBmiCategory() {
		return bmiCategory;
	}

	public void setBmiCategory(String bmiCategory) {
		this.bmiCategory = bmiCategory;
	}

	public Boolean getPregnant() {
		return pregnant;
	}

	public void setPregnant(Boolean pregnant) {
		this.pregnant = pregnant;
	}

	public void computeBMI() {
		String category = "";
		this.bmi = 0f;

		if (this.height > 0 && this.weight > 0) {
			float meter = this.height / 100f;
			float value = this.weight / (meter * meter);
			this.bmi = (float) Math.round(value * 10f) / 10f;

			if (this.bmi < 18.5) {
				category = "Underweight";
			} else if (this.bmi >= 18.5 && this.bmi <= 24.9) {
				category = "Normal";
			} else if (this.bmi >= 25 && this.bmi <= 29.9) {
				category = "Overweight";
			} else if (this.bmi >= 30) {
				category = "Obesity";
			}
		}

		this.bmiCategory = category;
	}
}
