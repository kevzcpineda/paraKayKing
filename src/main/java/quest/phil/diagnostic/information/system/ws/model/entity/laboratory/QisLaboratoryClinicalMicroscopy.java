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

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabAFB;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabFecalysis;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabPTOBT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabUrineChemical;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisLaboratoryClinicalMicroscopy implements Serializable {

	private static final long serialVersionUID = 2086708055350503367L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	public QisLaboratoryClinicalMicroscopy() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
