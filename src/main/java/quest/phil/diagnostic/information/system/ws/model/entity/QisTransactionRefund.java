package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_refund")
public class QisTransactionRefund implements Serializable {

	private static final long serialVersionUID = -6233767592737225256L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@NotNull(message = "Base Transaction ID is required.")
	@Column(name = "base_transaction_id", nullable = false)
	private Long baseTransactionId;

	@JsonIgnore
	@NotNull(message = "Refund Transaction ID is required.")
	@Column(name = "refund_transaction_id", nullable = false)
	private Long refundTransactionId;

	@JsonIgnore
	@NotNull(message = "Authorization ID is required.")
	@Column(name = "authorization_id", nullable = false)
	private Long authorizeId;

	@NotNull(message = "Reason is required.")
	@NotEmpty(message = "Reason must not be empty.")
	@Size(max = 250, message = "Reason should not exceed {max} characters.")
	@Column(name = "reason", nullable = false, length = 250)
	private String reason;

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

	public QisTransactionRefund() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBaseTransactionId() {
		return baseTransactionId;
	}

	public void setBaseTransactionId(Long baseTransactionId) {
		this.baseTransactionId = baseTransactionId;
	}

	public Long getRefundTransactionId() {
		return refundTransactionId;
	}

	public void setRefundTransactionId(Long refundTransactionId) {
		this.refundTransactionId = refundTransactionId;
	}

	public Long getAuthorizeId() {
		return authorizeId;
	}

	public void setAuthorizeId(Long authorizeId) {
		this.authorizeId = authorizeId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
}
