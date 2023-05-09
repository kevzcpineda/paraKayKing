package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHEAPTTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHEBTypRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHECBCRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHECTBTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHEESRRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHEPMTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHEPRMSRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHERCTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHeFerritinRequest;

public class QisTransactionLabHERequest implements Serializable {

	private static final long serialVersionUID = 2885988597161055529L;

	@Valid
	private QisTransactionLabHECBCRequest cbc;

	@Valid
	private QisTransactionLabHEBTypRequest bloodTyping;

	@Valid
	private QisTransactionLabHECTBTRequest ctbt;

	@Valid
	private QisTransactionLabHEPMTRequest prothrombinTime;

	@Valid
	private QisTransactionLabHEPRMSRequest prms;

	@Valid
	private QisTransactionLabHEAPTTRequest aptt;

	@Valid
	private QisTransactionLabHEESRRequest esr;
	
	@Valid
	private QisTransactionLabHeFerritinRequest ferritin;
	
	@Valid
	private QisTransactionLabHERCTRequest rct;
	

	@Size(max = 250, message = "Other Notes should not exceed {max} characters.")
	private String otherNotes;

	@NotNull(message = "Pathologist Id is required.")
	@NotEmpty(message = "Pathologist Id must not be empty.")
	@Size(max = 20, message = "Pathologist Id should not exceed {max} characters.")
	private String pathologistId;
	
	public QisTransactionLabHERequest() {
		super();
	}

	public QisTransactionLabHECBCRequest getCbc() {
		return cbc;
	}

	public void setCbc(QisTransactionLabHECBCRequest cbc) {
		this.cbc = cbc;
	}

	public QisTransactionLabHEBTypRequest getBloodTyping() {
		return bloodTyping;
	}

	public void setBloodTyping(QisTransactionLabHEBTypRequest bloodTyping) {
		this.bloodTyping = bloodTyping;
	}

	public QisTransactionLabHECTBTRequest getCtbt() {
		return ctbt;
	}

	public void setCtbt(QisTransactionLabHECTBTRequest ctbt) {
		this.ctbt = ctbt;
	}

	public QisTransactionLabHEPMTRequest getProthrombinTime() {
		return prothrombinTime;
	}

	public void setProthrombinTime(QisTransactionLabHEPMTRequest prothrombinTime) {
		this.prothrombinTime = prothrombinTime;
	}

	public QisTransactionLabHEPRMSRequest getPrms() {
		return prms;
	}

	public void setPrms(QisTransactionLabHEPRMSRequest prms) {
		this.prms = prms;
	}

	public QisTransactionLabHEAPTTRequest getAptt() {
		return aptt;
	}

	public void setAptt(QisTransactionLabHEAPTTRequest aptt) {
		this.aptt = aptt;
	}

	public QisTransactionLabHEESRRequest getEsr() {
		return esr;
	}

	public void setEsr(QisTransactionLabHEESRRequest esr) {
		this.esr = esr;
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
	
	public QisTransactionLabHeFerritinRequest getFerritin() {
		return ferritin;
	}

	public void setFerritin(QisTransactionLabHeFerritinRequest ferritin) {
		this.ferritin = ferritin;
	}
	
	public QisTransactionLabHERCTRequest getRtc() {
		return rct;
	}

	public void setRtc(QisTransactionLabHERCTRequest rct) {
		this.rct = rct;
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
