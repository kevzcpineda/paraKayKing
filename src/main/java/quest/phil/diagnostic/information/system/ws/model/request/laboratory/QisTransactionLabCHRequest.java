package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHBUNRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHBiliRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHHemoRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHCPKRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHCreaRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHElecRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHEnzyRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHFBSRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHLippRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHOGCTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHOGTTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHPPRBSRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHProtRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHRBSRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHUrAcRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransationLabCHTIBCRequest;

public class QisTransactionLabCHRequest implements Serializable {

	private static final long serialVersionUID = -1270050653852693774L;

	@Valid
	private QisTransactionLabCHFBSRequest fastingBloodSugar;
	
	@Valid
	private QisTransactionLabCHRBSRequest randomBloodSugar;
	
	@Valid
	private QisTransactionLabCHPPRBSRequest pprbs;
	
	@Valid
	private QisTransactionLabCHUrAcRequest uricAcid;
	
	@Valid
	private QisTransactionLabCHBUNRequest bloodUreaNitrogen;
	
	@Valid
	private QisTransactionLabCHCreaRequest creatinine;
	
	@Valid
	private QisTransactionLabCHHemoRequest hemoglobin;
	
	@Valid
	private QisTransactionLabCHLippRequest lipidProfile;
	
	@Valid
	private QisTransactionLabCHOGTTRequest ogtt;
	
	@Valid
	private QisTransactionLabCHOGCTRequest ogct;
	
	@Valid
	private QisTransactionLabCHElecRequest electrolytes;
	
	@Valid
	private QisTransactionLabCHEnzyRequest enzymes;
	
	@Valid
	private QisTransactionLabCHCPKRequest creatinePhosphokinase;
	
	@Valid
	private QisTransactionLabCHBiliRequest bilirubin;

	@Valid
	private QisTransactionLabCHProtRequest protein;
	
	@Valid
	private QisTransationLabCHTIBCRequest tibc;
	
	@Size(max = 250, message = "Other Notes should not exceed {max} characters.")
	private String otherNotes;

	@NotNull(message = "Pathologist Id is required.")
	@NotEmpty(message = "Pathologist Id must not be empty.")
	@Size(max = 20, message = "Pathologist Id should not exceed {max} characters.")
	private String pathologistId;
	
	public QisTransactionLabCHRequest() {
		super();
	}

	public QisTransationLabCHTIBCRequest getTibc() {
		return tibc;
	}

	public void setTibc(QisTransationLabCHTIBCRequest tibc) {
		this.tibc = tibc;
	}

	public QisTransactionLabCHFBSRequest getFastingBloodSugar() {
		return fastingBloodSugar;
	}

	public void setFastingBloodSugar(QisTransactionLabCHFBSRequest fastingBloodSugar) {
		this.fastingBloodSugar = fastingBloodSugar;
	}

	public QisTransactionLabCHRBSRequest getRandomBloodSugar() {
		return randomBloodSugar;
	}

	public void setRandomBloodSugar(QisTransactionLabCHRBSRequest randomBloodSugar) {
		this.randomBloodSugar = randomBloodSugar;
	}

	public QisTransactionLabCHPPRBSRequest getPprbs() {
		return pprbs;
	}

	public void setPprbs(QisTransactionLabCHPPRBSRequest pprbs) {
		this.pprbs = pprbs;
	}

	public QisTransactionLabCHUrAcRequest getUricAcid() {
		return uricAcid;
	}

	public void setUricAcid(QisTransactionLabCHUrAcRequest uricAcid) {
		this.uricAcid = uricAcid;
	}

	public QisTransactionLabCHBUNRequest getBloodUreaNitrogen() {
		return bloodUreaNitrogen;
	}

	public void setBloodUreaNitrogen(QisTransactionLabCHBUNRequest bloodUreaNitrogen) {
		this.bloodUreaNitrogen = bloodUreaNitrogen;
	}

	public QisTransactionLabCHCreaRequest getCreatinine() {
		return creatinine;
	}

	public void setCreatinine(QisTransactionLabCHCreaRequest creatinine) {
		this.creatinine = creatinine;
	}

	public QisTransactionLabCHHemoRequest getHemoglobin() {
		return hemoglobin;
	}

	public void setHemoglobin(QisTransactionLabCHHemoRequest hemoglobin) {
		this.hemoglobin = hemoglobin;
	}

	public QisTransactionLabCHLippRequest getLipidProfile() {
		return lipidProfile;
	}

	public void setLipidProfile(QisTransactionLabCHLippRequest lipidProfile) {
		this.lipidProfile = lipidProfile;
	}

	public QisTransactionLabCHOGTTRequest getOgtt() {
		return ogtt;
	}

	public void setOgtt(QisTransactionLabCHOGTTRequest ogtt) {
		this.ogtt = ogtt;
	}

	public QisTransactionLabCHOGCTRequest getOgct() {
		return ogct;
	}

	public void setOgct(QisTransactionLabCHOGCTRequest ogct) {
		this.ogct = ogct;
	}

	public QisTransactionLabCHElecRequest getElectrolytes() {
		return electrolytes;
	}

	public void setElectrolytes(QisTransactionLabCHElecRequest electrolytes) {
		this.electrolytes = electrolytes;
	}

	public QisTransactionLabCHEnzyRequest getEnzymes() {
		return enzymes;
	}

	public void setEnzymes(QisTransactionLabCHEnzyRequest enzymes) {
		this.enzymes = enzymes;
	}

	public QisTransactionLabCHCPKRequest getCreatinePhosphokinase() {
		return creatinePhosphokinase;
	}

	public void setCreatinePhosphokinase(QisTransactionLabCHCPKRequest creatinePhosphokinase) {
		this.creatinePhosphokinase = creatinePhosphokinase;
	}

	public QisTransactionLabCHBiliRequest getBilirubin() {
		return bilirubin;
	}

	public void setBilirubin(QisTransactionLabCHBiliRequest bilirubin) {
		this.bilirubin = bilirubin;
	}

	public QisTransactionLabCHProtRequest getProtein() {
		return protein;
	}

	public void setProtein(QisTransactionLabCHProtRequest protein) {
		this.protein = protein;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}

	public String getPathologistId() {
		return pathologistId;
	}

	public void setPathologistId(String pathologistId) {
		this.pathologistId = pathologistId;
	}
}
