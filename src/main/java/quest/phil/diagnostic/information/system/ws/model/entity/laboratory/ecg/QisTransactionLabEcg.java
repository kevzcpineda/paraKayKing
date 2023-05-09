package quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ecg;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_ecg",  uniqueConstraints = { @UniqueConstraint(columnNames = { "id" })})
public class QisTransactionLabEcg implements Serializable  {

	private static final long serialVersionUID = -1715266060357618489L;

	@JsonIgnore
	@Id
	private Long id;
	
	@Size(max = 300, message = "Rhythm must not exceed to {max} characters.")
	@Column(name = "Rhythm", nullable = true, length = 300)
	private String rhythm;
	
	@Size(max = 300, message = "PR Interval must not exceed to {max} characters.")
	@Column(name = "PR_Interval", nullable = true, length = 300)
	private String pr_interval;
	
	@Size(max = 300, message = "Rate Atrial must not exceed to {max} characters.")
	@Column(name = "Rate_Atrial", nullable = true, length = 300)
	private String rate_atrial;
	
	@Size(max = 300, message = "Axis must not exceed to {max} characters.")
	@Column(name = "Axis", nullable = true, length = 300)
	private String axis;
	
	@Size(max = 300, message = "P-Wave must not exceed to {max} characters.")
	@Column(name = "P_Wave", nullable = true, length = 300)
	private String p_wave;
	
	@Size(max = 300, message = "Ventricular must not exceed to {max} characters.")
	@Column(name = "Ventricular", nullable = true, length = 300)
	private String ventricular;
	
	@Size(max = 300, message = "INTERPRETATION must not exceed to {max} characters.")
	@Column(name = "INTERPRETATION", nullable = true, length = 300)
	private String interpretation;
	
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
	
	
	public QisTransactionLabEcg() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}


	public Long getId() {
		return id;
	}


	public String getRhythm() {
		return rhythm;
	}


	public String getPr_interval() {
		return pr_interval;
	}


	public String getRate_atrial() {
		return rate_atrial;
	}


	public String getAxis() {
		return axis;
	}


	public String getP_wave() {
		return p_wave;
	}


	public String getVentricular() {
		return ventricular;
	}


	public String getInterpretation() {
		return interpretation;
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


	public void setId(Long id) {
		this.id = id;
	}


	public void setRhythm(String rhythm) {
		this.rhythm = rhythm;
	}


	public void setPr_interval(String pr_interval) {
		this.pr_interval = pr_interval;
	}


	public void setRate_atrial(String rate_atrial) {
		this.rate_atrial = rate_atrial;
	}


	public void setAxis(String axis) {
		this.axis = axis;
	}


	public void setP_wave(String p_wave) {
		this.p_wave = p_wave;
	}


	public void setVentricular(String ventricular) {
		this.ventricular = ventricular;
	}


	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
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
	
}
