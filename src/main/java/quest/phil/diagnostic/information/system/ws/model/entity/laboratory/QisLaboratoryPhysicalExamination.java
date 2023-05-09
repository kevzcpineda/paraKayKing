package quest.phil.diagnostic.information.system.ws.model.entity.laboratory;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabMedicalHistory;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabPhysicalExam;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabVitalSign;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisLaboratoryPhysicalExamination implements Serializable {

	private static final long serialVersionUID = -8253069590417655532L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
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
	
	public QisLaboratoryPhysicalExamination() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
