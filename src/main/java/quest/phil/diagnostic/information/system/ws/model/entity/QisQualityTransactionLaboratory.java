package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionLaboratoryRequestClass;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryChemistry;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryClinicalMicroscopy;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryPhysicalExamination;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratorySerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionMicrobiology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.toxi.QisTransactionLabToxicology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.xray.QisTransactionLabXRay;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisQualityTransactionLaboratory extends QisTransactionLaboratoryRequestClass implements Serializable {

	private static final long serialVersionUID = -3184435108651051779L;

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

	public QisQualityTransactionLaboratory() {
		super();
	}

	public QisTransactionMicrobiology getMicrobiology() {
		return microbiology;
	}

	public void setMicrobiology(QisTransactionMicrobiology microbiology) {
		this.microbiology = microbiology;
	}

	public QisTransactionLabXRay getXray() {
		return xray;
	}

	public void setXray(QisTransactionLabXRay xray) {
		this.xray = xray;
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
