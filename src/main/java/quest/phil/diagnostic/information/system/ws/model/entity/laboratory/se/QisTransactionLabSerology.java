package quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_se_serology")
public class QisTransactionLabSerology implements Serializable {

	private static final long serialVersionUID = 841908990592739667L;

	@JsonIgnore
	@Id
	private Long id;

	@Column(name = "hbs_ag", nullable = true, columnDefinition = "boolean default null")
	private Boolean hbsAg;

	@Column(name = "anti_hav", nullable = true, columnDefinition = "boolean default null")
	private Boolean antiHav;

	@Column(name = "vdrl_rpr", nullable = true, columnDefinition = "boolean default null")
	private Boolean vdrlRpr;

	@Column(name = "anti_hbs", nullable = true, columnDefinition = "boolean default null")
	private Boolean antiHbs;

	@Column(name = "hbe_ag", nullable = true, columnDefinition = "boolean default null")
	private Boolean hbeAg;

	@Column(name = "anti_hbe", nullable = true, columnDefinition = "boolean default null")
	private Boolean antiHbe;

	@Column(name = "anti_hbc", nullable = true, columnDefinition = "boolean default null")
	private Boolean antiHbc;

	@Column(name = "tppa", nullable = true, columnDefinition = "boolean default null")
	private Boolean tppa;
	
	@Column(name = "anti_hcv", nullable = true, columnDefinition = "boolean default null")
	private Boolean antihcv;
	
	@Column(name = "abs", nullable = true )
	private String abs;
	
	@Column(name = "cut_off_value", nullable = true )
	private String cutOffValue;

	@Column(name = "pregnancy_test", nullable = true, columnDefinition = "boolean default null")
	private Boolean pregnancyTest;
	
	@JsonIgnore
	@Column(name = "created_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdAt = Calendar.getInstance();

	@JsonIgnore
	@Column(name = "updated_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar updatedAt = Calendar.getInstance();

	@JsonIgnore
	@Column(name = "created_by", nullable = true)
	private Long createdBy = null;

	@JsonIgnore
	@Column(name = "updated_by", nullable = true)
	private Long updatedBy = null;

	@JsonIgnore
	@Column(name = "molecular_lab_id", nullable = true)
	private Long referenceLabId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "molecular_lab_id", insertable = false, updatable =false)
	@JsonManagedReference 
	private QisReferenceLaboratory referenceLab; 
	
	public QisTransactionLabSerology() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getHbsAg() {
		return hbsAg;
	}

	public void setHbsAg(Boolean hbsAg) {
		this.hbsAg = hbsAg;
	}

	public Boolean getAntiHav() {
		return antiHav;
	}

	public void setAntiHav(Boolean antiHav) {
		this.antiHav = antiHav;
	}

	public Boolean getVdrlRpr() {
		return vdrlRpr;
	}

	public void setVdrlRpr(Boolean vdrlRpr) {
		this.vdrlRpr = vdrlRpr;
	}

	public Boolean getAntiHbs() {
		return antiHbs;
	}

	public void setAntiHbs(Boolean antiHbs) {
		this.antiHbs = antiHbs;
	}

	public Boolean getHbeAg() {
		return hbeAg;
	}

	public void setHbeAg(Boolean hbeAg) {
		this.hbeAg = hbeAg;
	}

	public Boolean getAntiHbe() {
		return antiHbe;
	}

	public void setAntiHbe(Boolean antiHbe) {
		this.antiHbe = antiHbe;
	}

	public Boolean getAntiHbc() {
		return antiHbc;
	}

	public void setAntiHbc(Boolean antiHbc) {
		this.antiHbc = antiHbc;
	}

	public Boolean getTppa() {
		return tppa;
	}

	public String getAbs() {
		return abs;
	}

	public void setAbs(String abs) {
		this.abs = abs;
	}

	public String getCutOffValue() {
		return cutOffValue;
	}

	public void setCutOffValue(String cutOffValue) {
		this.cutOffValue = cutOffValue;
	}

	public Boolean getAntiHcv() {
		return antihcv;
	}

	public void setAntiHcv(Boolean antihcv) {
		this.antihcv = antihcv;
	}

	public void setTppa(Boolean tppa) {
		this.tppa = tppa;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Boolean getPregnancyTest() {
		return pregnancyTest;
	}

	public void setPregnancyTest(Boolean pregnancyTest) {
		this.pregnancyTest = pregnancyTest;
	}

	public Long getReferenceLabId() {
		return referenceLabId;
	}

	public void setReferenceLabId(Long referenceLabId) {
		this.referenceLabId = referenceLabId;
	}

	public QisReferenceLaboratory getReferenceLab() {
		return referenceLab;
	}

	public void setReferenceLab(QisReferenceLaboratory referenceLab) {
		this.referenceLab = referenceLab;
	}

}
