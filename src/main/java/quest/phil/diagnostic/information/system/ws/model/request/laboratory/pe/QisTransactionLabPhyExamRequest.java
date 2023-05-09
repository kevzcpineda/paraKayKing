package quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabPhyExamRequest implements Serializable {

	private static final long serialVersionUID = 4167067185120052012L;
	@NotNull(message = "Skin value is required.")
	public Boolean skin;

	@NotNull(message = "Head and Neck value is required.")
	public Boolean headNeck;

	@NotNull(message = "Chest/Breast/Lungs value is required.")
	public Boolean chestBreastLungs;

	@NotNull(message = "Cardiac/Heart value is required.")
	public Boolean cardiacHeart;

	@NotNull(message = "Abdomen value is required.")
	public Boolean abdomen;

	@NotNull(message = "Extremities value is required.")
	public Boolean extremities;

	@NotNull(message = "Fatigue/Aches/Pain value is required.")
	public Boolean fatigueachespains;
	
	@Size(max = 250, message = "Notes must not exceed to {max} characters.")
	public String notes;

	@Size(max = 250, message = "Findings must not exceed to {max} characters.")
	public String findings;

	@NotNull(message = "Doctor Id is required.")
	@Size(max = 20, message = "Doctor Id should not exceed {max} characters.")
	public String doctorId;

	@NotNull(message = "License is required.")
	@Size(max = 20, message = "License should not exceed {max} characters.")
	public String license;

	@NotNull(message = "Remarks value is required.")
	public Boolean remarks;

	public QisTransactionLabPhyExamRequest() {
		super();
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

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public Boolean getRemarks() {
		return remarks;
	}

	public void setRemarks(Boolean remarks) {
		this.remarks = remarks;
	}
	

	public Boolean getFatigueachespains() {
		return fatigueachespains;
	}

	public void setFatigueachespains(Boolean fatigueachespains) {
		this.fatigueachespains = fatigueachespains;
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
