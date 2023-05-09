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

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabAPTT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabBloodTyping;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabCBC;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabCTBT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabESR;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabFerritin;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabPRMS;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabProthrombinTime;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabRCT;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisLaboratoryHematology implements Serializable {

	private static final long serialVersionUID = -7115548509941629261L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabCBC cbc;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabBloodTyping bloodTyping;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabCTBT ctbt;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabProthrombinTime prothombinTime;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabPRMS prms;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabAPTT aptt;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabESR esr;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabFerritin ferritin;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabRCT rct;

	public QisLaboratoryHematology() {
		super();
	}

	public QisTransactionLabFerritin getFerritin() {
		return ferritin;
	}

	public void setFerritin(QisTransactionLabFerritin ferritin) {
		this.ferritin = ferritin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public QisTransactionLabCBC getCbc() {
		return cbc;
	}

	public void setCbc(QisTransactionLabCBC cbc) {
		this.cbc = cbc;
	}

	public QisTransactionLabBloodTyping getBloodTyping() {
		return bloodTyping;
	}

	public void setBloodTyping(QisTransactionLabBloodTyping bloodTyping) {
		this.bloodTyping = bloodTyping;
	}

	public QisTransactionLabCTBT getCtbt() {
		return ctbt;
	}

	public void setCtbt(QisTransactionLabCTBT ctbt) {
		this.ctbt = ctbt;
	}

	public QisTransactionLabProthrombinTime getProthombinTime() {
		return prothombinTime;
	}

	public void setProthombinTime(QisTransactionLabProthrombinTime prothombinTime) {
		this.prothombinTime = prothombinTime;
	}

	public QisTransactionLabPRMS getPrms() {
		return prms;
	}

	public void setPrms(QisTransactionLabPRMS prms) {
		this.prms = prms;
	}

	public QisTransactionLabAPTT getAptt() {
		return aptt;
	}

	public void setAptt(QisTransactionLabAPTT aptt) {
		this.aptt = aptt;
	}

	public QisTransactionLabESR getEsr() {
		return esr;
	}

	public void setEsr(QisTransactionLabESR esr) {
		this.esr = esr;
	}

}
