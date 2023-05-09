package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe.QisTransactionLabMedHisRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe.QisTransactionLabPhyExamRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe.QisTransactionLabVitalSignRequest;

public class QisTransactionLabPERequest implements Serializable{

	private static final long serialVersionUID = -7271263227126277233L;

	@NotNull(message = "Medical History is required.")
	@Valid
	public QisTransactionLabMedHisRequest medicalHistory;

	@NotNull(message = "Vital Sign is required.")
	@Valid
	public QisTransactionLabVitalSignRequest vitalSign;
	
	@NotNull(message = "Physical Exam is required.")
	@Valid
	public QisTransactionLabPhyExamRequest physicalExam;	
	
	public QisTransactionLabPERequest() {
		super();
	}

	public QisTransactionLabMedHisRequest getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(QisTransactionLabMedHisRequest medicalHistory) {
		this.medicalHistory = medicalHistory;
	}

	public QisTransactionLabVitalSignRequest getVitalSign() {
		return vitalSign;
	}

	public void setVitalSign(QisTransactionLabVitalSignRequest vitalSign) {
		this.vitalSign = vitalSign;
	}

	public QisTransactionLabPhyExamRequest getPhysicalExam() {
		return physicalExam;
	}

	public void setPhysicalExam(QisTransactionLabPhyExamRequest physicalExam) {
		this.physicalExam = physicalExam;
	}
}
