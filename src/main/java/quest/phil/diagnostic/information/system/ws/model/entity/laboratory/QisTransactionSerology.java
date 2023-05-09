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
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabASO;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabAntigen;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabCRP;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabCovid;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabDengue;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabHIV;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabRFT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabRtAntigen;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabSerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabTPHAwTiter;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabTPN;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabThyroid;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabTyphidot;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionMedSer;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionRTPCR;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisTransactionSerology extends QisTransactionLaboratoryRequestClass implements Serializable {

	private static final long serialVersionUID = 2522895248628823530L;

	@ManyToOne(optional = true)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionInfo transaction;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabSerology serology;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabThyroid thyroid;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabTyphidot typhidot;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabCRP crp;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabHIV hiv;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabAntigen antigen;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabCovid covid;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabRtAntigen rtAntigen;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionRTPCR rtpcr;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabRFT rft;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionMedSer medSer;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabTPHAwTiter tphawt;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabASO aso;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabDengue dengue;
	
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabTPN tpn;
	
	
	public QisTransactionSerology() {
		super();
	}

	public QisTransactionLabDengue getDengue() {
		return dengue;
	}

	public QisTransactionLabTPN getTpn() {
		return tpn;
	}

	public void setDengue(QisTransactionLabDengue dengue) {
		this.dengue = dengue;
	}

	public void setTpn(QisTransactionLabTPN tpn) {
		this.tpn = tpn;
	}

	public QisTransactionLabASO getAso() {
		return aso;
	}

	public void setAso(QisTransactionLabASO aso) {
		this.aso = aso;
	}

	public QisTransactionLabTPHAwTiter getTphawt() {
		return tphawt;
	}

	public void setTphawt(QisTransactionLabTPHAwTiter tphawt) {
		this.tphawt = tphawt;
	}

	public QisTransactionMedSer getMedSer() {
		return medSer;
	}

	public void setMedSer(QisTransactionMedSer medSer) {
		this.medSer = medSer;
	}

	public QisTransactionLabRFT getRft() {
		return rft;
	}

	public void setRft(QisTransactionLabRFT rft) {
		this.rft = rft;
	}

	public QisTransactionInfo getTransaction() {
		return transaction;
	}

	public void setTransaction(QisTransactionInfo transaction) {
		this.transaction = transaction;
	}

	public QisTransactionLabSerology getSerology() {
		return serology;
	}

	public void setSerology(QisTransactionLabSerology serology) {
		this.serology = serology;
	}

	public QisTransactionLabThyroid getThyroid() {
		return thyroid;
	}

	public void setThyroid(QisTransactionLabThyroid thyroid) {
		this.thyroid = thyroid;
	}

	public QisTransactionLabTyphidot getTyphidot() {
		return typhidot;
	}

	public void setTyphidot(QisTransactionLabTyphidot typhidot) {
		this.typhidot = typhidot;
	}

	public QisTransactionLabCRP getCrp() {
		return crp;
	}

	public void setCrp(QisTransactionLabCRP crp) {
		this.crp = crp;
	}

	public QisTransactionLabHIV getHiv() {
		return hiv;
	}

	public void setHiv(QisTransactionLabHIV hiv) {
		this.hiv = hiv;
	}

	public QisTransactionLabAntigen getAntigen() {
		return antigen;
	}

	public void setAntigen(QisTransactionLabAntigen antigen) {
		this.antigen = antigen;
	}

	public QisTransactionLabCovid getCovid() {
		return covid;
	}

	public void setCovid(QisTransactionLabCovid covid) {
		this.covid = covid;
	}

	public QisTransactionLabRtAntigen getRtantigen() {
		return rtAntigen;
	}

	public void setRtantigen(QisTransactionLabRtAntigen rtAntigen) {
		this.rtAntigen = rtAntigen;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public QisTransactionLabRtAntigen getRtAntigen() {
		return rtAntigen;
	}

	public QisTransactionRTPCR getRtpcr() {
		return rtpcr;
	}

	public void setRtAntigen(QisTransactionLabRtAntigen rtAntigen) {
		this.rtAntigen = rtAntigen;
	}

	public void setRtpcr(QisTransactionRTPCR rtpcr) {
		this.rtpcr = rtpcr;
	}
	
	
	

}
