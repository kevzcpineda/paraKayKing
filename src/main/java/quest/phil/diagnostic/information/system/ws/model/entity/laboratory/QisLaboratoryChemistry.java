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

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabBUN;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabBilirubin;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabCPK;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabHemoglobin;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabCreatinine;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabElectrolytes;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabEnzymes;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabFBS;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabLipidProfile;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabOGCT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabOGTT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabPPRBS;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabProtein;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabRBS;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabTIBC;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabUricAcid;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisLaboratoryChemistry implements Serializable {

	private static final long serialVersionUID = 6808484460811666345L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabFBS fbs;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabRBS rbs;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabPPRBS pprbs;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabUricAcid uricAcid;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabBUN bun;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabCreatinine creatinine;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabHemoglobin hemoglobin;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabLipidProfile lipidProfile;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabOGTT ogtt;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabOGCT ogct;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabElectrolytes electrolytes;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabEnzymes enzymes;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabCPK cpk;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabBilirubin bilirubin;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabProtein protein;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabTIBC tibc;

	public QisLaboratoryChemistry() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public QisTransactionLabTIBC getTibc() {
		return tibc;
	}

	public void setTibc(QisTransactionLabTIBC tibc) {
		this.tibc = tibc;
	}

	public QisTransactionLabFBS getFbs() {
		return fbs;
	}

	public void setFbs(QisTransactionLabFBS fbs) {
		this.fbs = fbs;
	}

	public QisTransactionLabRBS getRbs() {
		return rbs;
	}

	public void setRbs(QisTransactionLabRBS rbs) {
		this.rbs = rbs;
	}

	public QisTransactionLabPPRBS getPprbs() {
		return pprbs;
	}

	public void setPprbs(QisTransactionLabPPRBS pprbs) {
		this.pprbs = pprbs;
	}

	public QisTransactionLabUricAcid getUricAcid() {
		return uricAcid;
	}

	public void setUricAcid(QisTransactionLabUricAcid uricAcid) {
		this.uricAcid = uricAcid;
	}

	public QisTransactionLabBUN getBun() {
		return bun;
	}

	public void setBun(QisTransactionLabBUN bun) {
		this.bun = bun;
	}

	public QisTransactionLabCreatinine getCreatinine() {
		return creatinine;
	}

	public void setCreatinine(QisTransactionLabCreatinine creatinine) {
		this.creatinine = creatinine;
	}

	public QisTransactionLabHemoglobin getHemoglobin() {
		return hemoglobin;
	}

	public void setHemoglobin(QisTransactionLabHemoglobin hemoglobin) {
		this.hemoglobin = hemoglobin;
	}

	public QisTransactionLabLipidProfile getLipidProfile() {
		return lipidProfile;
	}

	public void setLipidProfile(QisTransactionLabLipidProfile lipidProfile) {
		this.lipidProfile = lipidProfile;
	}

	public QisTransactionLabOGTT getOgtt() {
		return ogtt;
	}

	public void setOgtt(QisTransactionLabOGTT ogtt) {
		this.ogtt = ogtt;
	}

	public QisTransactionLabOGCT getOgct() {
		return ogct;
	}

	public void setOgct(QisTransactionLabOGCT ogct) {
		this.ogct = ogct;
	}

	public QisTransactionLabElectrolytes getElectrolytes() {
		return electrolytes;
	}

	public void setElectrolytes(QisTransactionLabElectrolytes electrolytes) {
		this.electrolytes = electrolytes;
	}

	public QisTransactionLabEnzymes getEnzymes() {
		return enzymes;
	}

	public void setEnzymes(QisTransactionLabEnzymes enzymes) {
		this.enzymes = enzymes;
	}

	public QisTransactionLabCPK getCpk() {
		return cpk;
	}

	public void setCpk(QisTransactionLabCPK cpk) {
		this.cpk = cpk;
	}

	public QisTransactionLabBilirubin getBilirubin() {
		return bilirubin;
	}

	public void setBilirubin(QisTransactionLabBilirubin bilirubin) {
		this.bilirubin = bilirubin;
	}

	public QisTransactionLabProtein getProtein() {
		return protein;
	}

	public void setProtein(QisTransactionLabProtein protein) {
		this.protein = protein;
	}

}
