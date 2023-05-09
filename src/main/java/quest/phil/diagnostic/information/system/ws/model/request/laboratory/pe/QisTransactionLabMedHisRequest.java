package quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabMedHisRequest implements Serializable {

	private static final long serialVersionUID = 13183781151241950L;
	@NotNull(message = "Asthma value is required.")
	public Boolean asthma;

	@NotNull(message = "Tuberculosis value is required.")
	public Boolean tuberculosis;

	@NotNull(message = "Diabetes Mellitus value is required.")
	public Boolean diabetesMellitus;

	@NotNull(message = "High Blood Pressure value is required.")
	public Boolean highBloodPressure;

	@NotNull(message = "Heart Problem value is required.")
	public Boolean heartProblem;

	@NotNull(message = "Kidney Problem value is required.")
	public Boolean kidneyProblem;

	@NotNull(message = "Abdominal/Hernia value is required.")
	public Boolean abdominalHernia;

	@NotNull(message = "Joint/Back/Scoliosis value is required.")
	public Boolean jointBackScoliosis;

	@NotNull(message = "Psychiatric Problem value is required.")
	public Boolean psychiatricProblem;

	@NotNull(message = "Migraine/Headache value is required.")
	public Boolean migraineHeadache;

	@NotNull(message = "Fainting/Seizures value is required.")
	public Boolean faintingSeizures;

	@NotNull(message = "Allergies value is required.")
	public Boolean allergies;

	@NotNull(message = "Cancer/Tumor value is required.")
	public Boolean cancerTumor;

	@NotNull(message = "Hepatitis value is required.")
	public Boolean hepatitis;

	@NotNull(message = "STD/PLHIV value is required.")
	public Boolean stdplhiv;

	@NotNull(message = "Travel History value is required.")
	public Boolean travelhistory;
	
	public QisTransactionLabMedHisRequest() {
		super();
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
	
	public Boolean getTravelhistory() {
		return travelhistory;
	}

	public void setTravelhistory(Boolean travelhistory) {
		this.travelhistory = travelhistory;
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
