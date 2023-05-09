package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_payments")
public class QisTransactionPayment implements Serializable {

	private static final long serialVersionUID = 1566406215106573032L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@NotNull(message = "Transaction is required.")
	@Column(name = "qis_transaction_id", nullable = false)
	private Long transactionid;

	@NotNull(message = "Payment Type is required.")
	@NotEmpty(message = "Payment Type should not be empty.")
	@Size(min = 1, max = 4, message = "Payment Type must between {min} and {max} characters.")
	@Column(name = "payment_type", nullable = false, length = 4)
	private String paymentType;

	@Size(max = 4, message = "Payment Mode must not exceed to {max} characters.")
	@Column(name = "payment_mode", nullable = true, length = 4)
	private String paymentMode;

	@Size(max = 4, message = "Payment Bank must not exceed to {max} characters.")
	@Column(name = "payment_bank", nullable = true, length = 4)
	private String paymentBank;

	@NotNull(message = "Amount is required.")
	@Column(name = "amount", nullable = false)
	private Double amount;

	@NotNull(message = "Currency is required.")
	@NotEmpty(message = "Currency should not be empty.")
	@Size(min = 3, max = 4, message = "Currency must between {min} and {max} characters.")
	@Column(name = "currency", nullable = false, length = 4)
	private String currency;

	@JsonIgnore
	@Column(name = "biller_id", nullable = true)
	private Long billerId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "biller_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisCorporate biller;

	@Size(max = 20, message = "HMO LOE Number must not exceed to {max} characters.")
	@Column(name = "hmo_loe", nullable = true, length = 20)
	private String hmoLOE;

	@Size(max = 30, message = "HMO Account Number must not exceed to {max} characters.")
	@Column(name = "hmo_account_number", nullable = true, length = 20)
	private String hmoAccountNumber;

	@Size(max = 20, message = "HMO Approval Code must not exceed to {max} characters.")
	@Column(name = "hmo_approval_code", nullable = true, length = 20)
	private String hmoApprovalCode;

	@Size(max = 20, message = "Credit Card Type must not exceed to {max} characters.")
	@Column(name = "cc_type", nullable = true, length = 20)
	private String ccType;

	@Size(max = 256, message = "Credit Card Holder Name must not exceed to {max} characters.")
	@Column(name = "cc_holder_name", nullable = true, length = 256)
	private String ccHolderName;

	@Size(max = 20, message = "Reference Number must not exceed to {max} characters.")
	@Column(name = "reference_number", nullable = true, length = 20)
	private String referenceNumber;

	@Size(max = 20, message = "Card/Check Number must not exceed to {max} characters.")
	@Column(name = "card_cheque_number", nullable = true, length = 20)
	private String cardChequeNumber;

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
	@Column(name = "status", nullable = false, columnDefinition = "int default 1")
	private int status = 1;

	public QisTransactionPayment() {
		super();
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public QisTransactionPayment(
			@NotNull(message = "Payment Type is required.") @NotEmpty(message = "Payment Type should not be empty.") @Size(min = 2, max = 4, message = "Payment Type must between {min} and {max} characters.") String paymentType,
			@NotNull(message = "Amount is required.") Double amount,
			@NotNull(message = "Currency Type is required.") @NotEmpty(message = "Currency should not be empty.") @Size(min = 3, max = 4, message = "Currency must between {min} and {max} characters.") String currency) {
		super();
		this.paymentType = paymentType;
		this.amount = amount;
		this.currency = currency;
		this.createdAt = Calendar.getInstance();
		this.updatedAt = Calendar.getInstance();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(Long transactionid) {
		this.transactionid = transactionid;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Long getBillerId() {
		return billerId;
	}

	public void setBillerId(Long billerId) {
		this.billerId = billerId;
	}

	public QisCorporate getBiller() {
		return biller;
	}

	public void setBiller(QisCorporate biller) {
		this.biller = biller;
	}

	public String getHmoLOE() {
		return hmoLOE;
	}

	public void setHmoLOE(String hmoLOE) {
		this.hmoLOE = hmoLOE;
	}

	public String getHmoAccountNumber() {
		return hmoAccountNumber;
	}

	public void setHmoAccountNumber(String hmoAccountNumber) {
		this.hmoAccountNumber = hmoAccountNumber;
	}

	public String getHmoApprovalCode() {
		return hmoApprovalCode;
	}

	public void setHmoApprovalCode(String hmoApprovalCode) {
		this.hmoApprovalCode = hmoApprovalCode;
	}

	public String getCcType() {
		return ccType;
	}

	public void setCcType(String ccType) {
		this.ccType = ccType;
	}

	public String getCcHolderName() {
		return ccHolderName;
	}

	public void setCcHolderName(String ccHolderName) {
		this.ccHolderName = ccHolderName;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getPaymentBank() {
		return paymentBank;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	public String getCardChequeNumber() {
		return cardChequeNumber;
	}

	public void setCardChequeNumber(String cardChequeNumber) {
		this.cardChequeNumber = cardChequeNumber;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
