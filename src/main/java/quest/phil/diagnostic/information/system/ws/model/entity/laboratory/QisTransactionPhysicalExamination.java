package quest.phil.diagnostic.information.system.ws.model.entity.laboratory;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionLaboratoryRequestClass;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabMedicalHistory;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabPhysicalExam;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabVitalSign;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisTransactionPhysicalExamination extends QisTransactionLaboratoryRequestClass implements Serializable {

	private static final long serialVersionUID = 2503584868297051785L;

	@ManyToOne(optional = true)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionInfo transaction;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabMedicalHistory medicalHistory;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabVitalSign vitalSign;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabPhysicalExam physicalExam;

	public QisTransactionPhysicalExamination() {
		super();
	}

	public QisTransactionInfo getTransaction() {
		return transaction;
	}

	public void setTransaction(QisTransactionInfo transaction) {
		this.transaction = transaction;
	}

	public QisTransactionLabMedicalHistory getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(QisTransactionLabMedicalHistory medicalHistory) {
		this.medicalHistory = medicalHistory;
	}

	public QisTransactionLabVitalSign getVitalSign() {
		return vitalSign;
	}

	public void setVitalSign(QisTransactionLabVitalSign vitalSign) {
		this.vitalSign = vitalSign;
	}

	public QisTransactionLabPhysicalExam getPhysicalExam() {
		return physicalExam;
	}

	public void setPhysicalExam(QisTransactionLabPhysicalExam physicalExam) {
		this.physicalExam = physicalExam;
	}
}
