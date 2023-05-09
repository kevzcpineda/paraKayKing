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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionLaboratoryRequestClass;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabAFB;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabFecalysis;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabPTOBT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabUrineChemical;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisTransactionClinicalMicroscopy extends QisTransactionLaboratoryRequestClass implements Serializable {

	private static final long serialVersionUID = 4912131812173727748L;

	@ManyToOne(optional = true)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionInfo transaction;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabUrineChemical urineChemical;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabFecalysis fecalysis;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabPTOBT ptobt;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabAFB afb;
	
	public QisTransactionClinicalMicroscopy() {
		super();
	}

	public QisTransactionInfo getTransaction() {
		return transaction;
	}

	public void setTransaction(QisTransactionInfo transaction) {
		this.transaction = transaction;
	}

	public QisTransactionLabUrineChemical getUrineChemical() {
		return urineChemical;
	}

	public void setUrineChemical(QisTransactionLabUrineChemical urineChemical) {
		this.urineChemical = urineChemical;
	}

	public QisTransactionLabFecalysis getFecalysis() {
		return fecalysis;
	}

	public void setFecalysis(QisTransactionLabFecalysis fecalysis) {
		this.fecalysis = fecalysis;
	}

	public QisTransactionLabPTOBT getPtobt() {
		return ptobt;
	}

	public void setPtobt(QisTransactionLabPTOBT ptobt) {
		this.ptobt = ptobt;
	}

	public QisTransactionLabAFB getAfb() {
		return afb;
	}

	public void setAfb(QisTransactionLabAFB afb) {
		this.afb = afb;
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
