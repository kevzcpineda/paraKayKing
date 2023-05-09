package quest.phil.diagnostic.information.system.ws.model.entity.laboratory;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionLaboratoryRequestClass;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ecg.QisTransactionLabEcg;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.toxi.QisTransactionLabToxicology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ultrasound.QisTransactionLabUltrasound;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.xray.QisTransactionLabXRay;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisTransactionLaboratoryDetails extends QisTransactionLaboratoryRequestClass implements Serializable {

	private static final long serialVersionUID = -7084009065350800745L;

	@ManyToOne(optional = true)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionInfo transaction;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabXRay xray;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisLaboratoryPhysicalExamination physicalExamination;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisLaboratoryClinicalMicroscopy clinicalMicroscopy;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisLaboratoryHematology hematology;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisLaboratoryChemistry chemistry;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisLaboratorySerology serology;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabToxicology toxicology;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionMicrobiology microbiology;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabEcg ecg;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabUltrasound ultrasound;

	public QisTransactionLaboratoryDetails() {
		super();
	}

	public QisTransactionLaboratoryDetails(@NotNull(message = "Transaction Item is required.") Long transactionItemId,
			@NotNull(message = "Item is required.") Long itemId,
			@NotEmpty(message = "Item Laboratory is required.") @Size(min = 1, max = 12, message = "Item Laboratory must between {min} and {max} characters.") String itemLaboratory) {
		super(transactionItemId, itemId, itemLaboratory);
	}

	public QisTransactionMicrobiology getMicrobiology() {
		return microbiology;
	}

	public void setMicrobiology(QisTransactionMicrobiology microbiology) {
		this.microbiology = microbiology;
	}

	public QisTransactionInfo getTransaction() {
		return transaction;
	}

	public void setTransaction(QisTransactionInfo transaction) {
		this.transaction = transaction;
	}

	public QisTransactionLabXRay getXray() {
		return xray;
	}

	public void setXray(QisTransactionLabXRay xray) {
		this.xray = xray;
	}

	public QisTransactionLabEcg getEcg() {
		return ecg;
	}

	public void setEcg(QisTransactionLabEcg ecg) {
		this.ecg = ecg;
	}

	public QisLaboratoryPhysicalExamination getPhysicalExamination() {
		return physicalExamination;
	}

	public void setPhysicalExamination(QisLaboratoryPhysicalExamination physicalExamination) {
		this.physicalExamination = physicalExamination;
	}

	public QisLaboratoryClinicalMicroscopy getClinicalMicroscopy() {
		return clinicalMicroscopy;
	}

	public void setClinicalMicroscopy(QisLaboratoryClinicalMicroscopy clinicalMicroscopy) {
		this.clinicalMicroscopy = clinicalMicroscopy;
	}

	public QisLaboratoryHematology getHematology() {
		return hematology;
	}

	public void setHematology(QisLaboratoryHematology hematology) {
		this.hematology = hematology;
	}

	public QisLaboratoryChemistry getChemistry() {
		return chemistry;
	}

	public void setChemistry(QisLaboratoryChemistry chemistry) {
		this.chemistry = chemistry;
	}

	public QisLaboratorySerology getSerology() {
		return serology;
	}

	public void setSerology(QisLaboratorySerology serology) {
		this.serology = serology;
	}

	public QisTransactionLabToxicology getToxicology() {
		return toxicology;
	}

	public void setToxicology(QisTransactionLabToxicology toxicology) {
		this.toxicology = toxicology;
	}

	public QisTransactionLabUltrasound getUltrasound() {
		return ultrasound;
	}

	public void setUltrasound(QisTransactionLabUltrasound ultrasound) {
		this.ultrasound = ultrasound;
	}
	

}
