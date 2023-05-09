package quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabVitalSignRequest implements Serializable {

	private static final long serialVersionUID = 2676665326572793730L;

	@NotNull(message = "Height is required in centimeters(cm).")
	@Digits(integer = 5, fraction = 4)
	public String height;

	@NotNull(message = "Weight is required in kilograms(kg).")
	@Digits(integer = 5, fraction = 4)
	public String weight;

	@NotNull(message = "Blood Pressure is required.")
	@Size(max = 12, message = "Blood Pressure must not exceed to {max} characters.")
	public String bloodPressure;

	@NotNull(message = "Pulse Rate is required in centimeters(cm).")
	public Integer pulseRate;

	@NotNull(message = "Respiratory Rate is required in centimeters(cm).")
	public Integer respiratoryRate;

	// @NotNull(message = "Uncorrected Oculus Dexter is required.")
	@Size(max = 10, message = "Uncorrected Oculus Dexter must not exceed to {max} characters.")
	public String uncorrectedOD = null;

//	@NotNull(message = "Uncorrected Oculus Sinister is required.")
	@Size(max = 10, message = "Uncorrected Oculus Sinister must not exceed to {max} characters.")
	public String uncorrectedOS = null;

//	@NotNull(message = "Corrected Oculus Dexter is required.")
	@Size(max = 10, message = "Corrected Oculus Dexter must not exceed to {max} characters.")
	public String correctedOD = null;

//	@NotNull(message = "Corrected Oculus Sinister is required.")
	@Size(max = 10, message = "Corrected Oculus Sinister must not exceed to {max} characters.")
	public String correctedOS = null;

	@Size(max = 120, message = "Ishihara must not exceed to {max} characters.")
	public String ishihara;

	@Size(max = 120, message = "Hearing must not exceed to {max} characters.")
	public String hearing;

	@Size(max = 120, message = "Hospitalization must not exceed to {max} characters.")
	public String hospitalization;

	@Size(max = 120, message = "Operations must not exceed to {max} characters.")
	public String opearations;

	@Size(max = 120, message = "Medications must not exceed to {max} characters.")
	public String medications;

	@Size(max = 120, message = "Smoker must not exceed to {max} characters.")
	public String smoker;

	@Size(max = 120, message = "Alcoholic must not exceed to {max} characters.")
	public String alcoholic;

	@Size(max = 120, message = "Last Menstrual must not exceed to {max} characters.")
	public String lastMenstrual;

	@Size(max = 250, message = "Notes must not exceed to {max} characters.")
	public String notes;

	public Float bmi = 0f;
	public String bmiCategory;
	public Boolean pregnant;

	public QisTransactionLabVitalSignRequest() {
		super();
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
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

	@Override
	public String toString() {
		String serialized = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			jpe.printStackTrace();
		}
		return serialized;
	}
}
