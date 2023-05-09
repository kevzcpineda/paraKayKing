package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSEASORequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSEAntigenRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSECRPRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSECovidRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSEDegueRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSEHIVRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSERFTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSERTPCRRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSERtAtigenRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSESerologyRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSETPNRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSEThyroidRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSETyphidotRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSeTPHAWTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionMedSerRequest;

public class QisTransactionLabSERequest implements Serializable {

	private static final long serialVersionUID = -206291739960741426L;

	@Valid
	private QisTransactionLabSESerologyRequest serology;

	@Valid
	private QisTransactionLabSEThyroidRequest thyroid;

	@Valid
	private QisTransactionLabSETyphidotRequest typhidot;

	@Valid
	private QisTransactionLabSECRPRequest crp;

	@Valid
	private QisTransactionLabSEHIVRequest hiv;

	@Valid
	private QisTransactionLabSEAntigenRequest antigen;

	@Valid
	private QisTransactionLabSECovidRequest covid;
	
	@Valid
	private QisTransactionLabSERtAtigenRequest rtantigen;
	
	@Valid
	private QisTransactionLabSERTPCRRequest rtpcr;
	
	@Valid
	private QisTransactionLabSERFTRequest rft;
	
	@Valid
	private QisTransactionMedSerRequest medSer;
	
	@Valid
	private QisTransactionLabSeTPHAWTRequest tphawt;
	
	@Valid
	private QisTransactionLabSEASORequest aso;
	
	@Valid
	private QisTransactionLabSEDegueRequest dengue;
	
	@Valid
	private QisTransactionLabSETPNRequest tpn;
	
	@Size(max = 250, message = "Other Notes should not exceed {max} characters.")
	private String otherNotes;

	@NotNull(message = "Pathologist Id is required.")
	@NotEmpty(message = "Pathologist Id must not be empty.")
	@Size(max = 20, message = "Pathologist Id should not exceed {max} characters.")
	private String pathologistId;
	
	private String referenceLab;
	
	public QisTransactionLabSERequest() {
		super();
	}

	public QisTransactionLabSETPNRequest getTpn() {
		return tpn;
	}

	public void setTpn(QisTransactionLabSETPNRequest tpn) {
		this.tpn = tpn;
	}

	public QisTransactionLabSEDegueRequest getDengue() {
		return dengue;
	}

	public void setDengue(QisTransactionLabSEDegueRequest dengue) {
		this.dengue = dengue;
	}

	public QisTransactionLabSEASORequest getAso() {
		return aso;
	}

	public void setAso(QisTransactionLabSEASORequest aso) {
		this.aso = aso;
	}

	public QisTransactionLabSeTPHAWTRequest getTphawt() {
		return tphawt;
	}

	public void setTphawt(QisTransactionLabSeTPHAWTRequest tphawt) {
		this.tphawt = tphawt;
	}

	public QisTransactionMedSerRequest getMedSer() {
		return medSer;
	}

	public void setMedSer(QisTransactionMedSerRequest medSer) {
		this.medSer = medSer;
	}

	public QisTransactionLabSESerologyRequest getSerology() {
		return serology;
	}

	public void setSerology(QisTransactionLabSESerologyRequest serology) {
		this.serology = serology;
	}

	public QisTransactionLabSEThyroidRequest getThyroid() {
		return thyroid;
	}

	public void setThyroid(QisTransactionLabSEThyroidRequest thyroid) {
		this.thyroid = thyroid;
	}

	public QisTransactionLabSETyphidotRequest getTyphidot() {
		return typhidot;
	}

	public void setTyphidot(QisTransactionLabSETyphidotRequest typhidot) {
		this.typhidot = typhidot;
	}


	public QisTransactionLabSECRPRequest getCrp() {
		return crp;
	}

	public void setCrp(QisTransactionLabSECRPRequest crp) {
		this.crp = crp;
	}

	public QisTransactionLabSEHIVRequest getHiv() {
		return hiv;
	}

	public void setHiv(QisTransactionLabSEHIVRequest hiv) {
		this.hiv = hiv;
	}

	public QisTransactionLabSEAntigenRequest getAntigen() {
		return antigen;
	}

	public void setAntigen(QisTransactionLabSEAntigenRequest antigen) {
		this.antigen = antigen;
	}

	public QisTransactionLabSECovidRequest getCovid() {
		return covid;
	}

	public void setCovid(QisTransactionLabSECovidRequest covid) {
		this.covid = covid;
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

	public QisTransactionLabSERtAtigenRequest getRtantigen() {
		return rtantigen;
	}

	public void setRtantigen(QisTransactionLabSERtAtigenRequest rtantigen) {
		this.rtantigen = rtantigen;
	}

	public QisTransactionLabSERTPCRRequest getRtpcr() {
		return rtpcr;
	}

	public void setRtpcr(QisTransactionLabSERTPCRRequest rtpcr) {
		this.rtpcr = rtpcr;
	}

	public String getReferenceLab() {
		return referenceLab;
	}

	public void setReferenceLab(String referenceLab) {
		this.referenceLab = referenceLab;
	}

	public QisTransactionLabSERFTRequest getRft() {
		return rft;
	}

	public void setRft(QisTransactionLabSERFTRequest rft) {
		this.rft = rft;
	}
	
	
}
