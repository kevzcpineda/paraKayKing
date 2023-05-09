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
public class QisTransactionHematology extends QisTransactionLaboratoryRequestClass implements Serializable {

	private static final long serialVersionUID = 1209521868366704985L;

	@ManyToOne(optional = true)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionInfo transaction;

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

	public QisTransactionHematology() {
		super();
	}

	public QisTransactionLabRCT getRct() {
		return rct;
	}

	public void setRct(QisTransactionLabRCT rct) {
		this.rct = rct;
	}

	public QisTransactionLabFerritin getFerritin() {
		return ferritin;
	}

	public void setFerritin(QisTransactionLabFerritin ferritin) {
		this.ferritin = ferritin;
	}

	public QisTransactionInfo getTransaction() {
		return transaction;
	}

	public void setTransaction(QisTransactionInfo transaction) {
		this.transaction = transaction;
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
