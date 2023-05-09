package quest.phil.diagnostic.information.system.ws.model.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@DynamicUpdate
@Table(name="advance_payment")
public class QisAdvancePayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	
	@Column(name = "amount_adv_pay", nullable = true, length = 250)
	private Double amtAdvPayment = 0.0;
	
	@JsonIgnore
	@Column(name = "charge_to_id", nullable = true)
	private Long chargeToId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "charge_to_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisCorporate chargeTo;
	
	@JsonIgnore
	@Column(name = "created_at", nullable = true, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdAt = Calendar.getInstance();
	
	@JsonIgnore
	@Column(name = "created_by", nullable = true)
	private Long createdBy = null;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	@JsonManagedReference
	private QisUserPersonel createUser;
	
	
	public QisAdvancePayment() {
		super();
		this.createdAt = Calendar.getInstance();
	}


	public Long getId() {
		return id;
	}


	public Double getAmtAdvPayment() {
		return amtAdvPayment;
	}


	public Long getChargeToId() {
		return chargeToId;
	}


	public QisCorporate getChargeTo() {
		return chargeTo;
	}


	public void setChargeToId(Long chargeToId) {
		this.chargeToId = chargeToId;
	}


	public void setChargeTo(QisCorporate chargeTo) {
		this.chargeTo = chargeTo;
	}


	public Calendar getCreatedAt() {
		return createdAt;
	}


	public Long getCreatedBy() {
		return createdBy;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public void setAmtAdvPayment(Double amtAdvPayment) {
		this.amtAdvPayment = amtAdvPayment;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}


	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public QisUserPersonel getCreateUser() {
		return createUser;
	}

	public void setCreateUser(QisUserPersonel createUser) {
		this.createUser = createUser;
	}
	
}
