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
@Table(name = "qis_transaction_laboratory_se_tphawt")
public class QisTransactionLabTPHAwTiter implements Serializable {

	private static final long serialVersionUID = -4194825914666597600L;
	@JsonIgnore
	@Id
	private Long id;
	
	
	@Column(name = "test1", nullable = true, columnDefinition = "boolean default null")
	private Boolean test1;
	
	@Column(name = "test2", nullable = true, columnDefinition = "boolean default null")
	private Boolean test2;
	
	@Column(name = "test3", nullable = true, columnDefinition = "boolean default null")
	private Boolean test3;
	
	@Column(name = "test4", nullable = true, columnDefinition = "boolean default null")
	private Boolean test4;
	
	@Column(name = "test5", nullable = true, columnDefinition = "boolean default null")
	private Boolean test5;
	
	@Column(name = "test6", nullable = true, columnDefinition = "boolean default null")
	private Boolean test6;
	
	@Column(name = "test7", nullable = true, columnDefinition = "boolean default null")
	private Boolean test7;
	
	@Column(name = "test8", nullable = true, columnDefinition = "boolean default null")
	private Boolean test8;
	
	@Column(name = "test9", nullable = true, columnDefinition = "boolean default null")
	private Boolean test9;
	
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
	
	public QisTransactionLabTPHAwTiter() {
		super();
	}

	public Long getId() {
		return id;
	}

	public Boolean getTest1() {
		return test1;
	}

	public Boolean getTest2() {
		return test2;
	}

	public Boolean getTest3() {
		return test3;
	}

	public Boolean getTest4() {
		return test4;
	}

	public Boolean getTest5() {
		return test5;
	}

	public Boolean getTest6() {
		return test6;
	}

	public Boolean getTest7() {
		return test7;
	}

	public Boolean getTest8() {
		return test8;
	}

	public Boolean getTest9() {
		return test9;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTest1(Boolean test1) {
		this.test1 = test1;
	}

	public void setTest2(Boolean test2) {
		this.test2 = test2;
	}

	public void setTest3(Boolean test3) {
		this.test3 = test3;
	}

	public void setTest4(Boolean test4) {
		this.test4 = test4;
	}

	public void setTest5(Boolean test5) {
		this.test5 = test5;
	}

	public void setTest6(Boolean test6) {
		this.test6 = test6;
	}

	public void setTest7(Boolean test7) {
		this.test7 = test7;
	}

	public void setTest8(Boolean test8) {
		this.test8 = test8;
	}

	public void setTest9(Boolean test9) {
		this.test9 = test9;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public Long getReferenceLabId() {
		return referenceLabId;
	}

	public QisReferenceLaboratory getReferenceLab() {
		return referenceLab;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setReferenceLabId(Long referenceLabId) {
		this.referenceLabId = referenceLabId;
	}

	public void setReferenceLab(QisReferenceLaboratory referenceLab) {
		this.referenceLab = referenceLab;
	}
}
