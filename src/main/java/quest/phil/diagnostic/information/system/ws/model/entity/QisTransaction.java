package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionClass;

@Entity
@DynamicUpdate
@Table(name = "qis_transactions", uniqueConstraints = { @UniqueConstraint(columnNames = { "transaction_id" }) })
public class QisTransaction extends QisTransactionClass implements Serializable, Comparable<QisTransaction> {

	private static final long serialVersionUID = -4079601932537435707L;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private Set<QisTransactionItem> transactionItems = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private Set<QisTransactionPayment> transactionPayments = new HashSet<>();

	@Transient
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionDiscount specialDiscount;

	public Integer taxRate = 0;
	public Integer discountRate = 0;
	public Double totalItemGrossAmount;
	public Double totalDiscountableAmount;
	public Double totalItemDiscountAmount;
	public Double totalTaxableAmount;
	public Double totalItemTaxAmount;
	public Double totalItemAmountDue;
	public Double totalItemNetAmount;

	public String currency = null;
	public Double totalPaymentAmount;
	public Double totalChangeAmount;
	public Double totalCashAmount;
	public Double totalCashOut;

	public Double specialDiscountAmount = 0d;
	public String biller = null;
	public String paymentType = null;
	public String paymentMode = null;

	public QisTransaction() {
		super();
	}

	public void setBiller(String biller) {
		this.biller = biller;
	}

	public Set<QisTransactionItem> getTransactionItems() {
		computeItems();
		return transactionItems;
	}

	public void setTransactionItems(Set<QisTransactionItem> transactionItems) {
		this.transactionItems = transactionItems;
	}

	public String getCurrency() {
		return currency;
	}

	public Integer getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Integer taxRate) {
		this.taxRate = taxRate;
	}

	public Integer getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Integer discountRate) {
		this.discountRate = discountRate;
	}

	public Double getTotalItemGrossAmount() {
		return roundValue(totalItemGrossAmount);
	}

	public Double getTotalDiscountableAmount() {
		return roundValue(totalDiscountableAmount);
	}

	public Double getTotalItemDiscountAmount() {
		return roundValue(totalItemDiscountAmount);
	}

	public Double getTotalTaxableAmount() {
		return roundValue(totalTaxableAmount);
	}

	public Double getTotalItemTaxAmount() {
		return roundValue(totalItemTaxAmount);
	}

	public Double getTotalItemAmountDue() {
		return roundValue(totalItemAmountDue);
	}

	public Double getTotalItemNetAmount() {
		return roundValue(totalItemNetAmount);
	}

	public Set<QisTransactionPayment> getTransactionPayments() {
		computePayments();
		computeChangeAmount();
		return transactionPayments;
	}

	public void setTransactionPayments(Set<QisTransactionPayment> transactionPayments) {
		this.transactionPayments = transactionPayments;
	}

	public QisTransactionDiscount getSpecialDiscount() {
		return specialDiscount;
	}

	public void setSpecialDiscount(QisTransactionDiscount specialDiscount) {
		this.specialDiscount = specialDiscount;
	}

	public Double getSpecialDiscountAmount() {
		return specialDiscountAmount;
	}

	public void setSpecialDiscountAmount(Double specialDiscountAmount) {
		this.specialDiscountAmount = specialDiscountAmount;
	}

	public String getBiller() {
		return biller;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public Double getTotalPaymentAmount() {
		return roundValue(totalPaymentAmount);
	}

	public Double getTotalChangeAmount() {
		return roundValue(totalChangeAmount);
	}

	public Double getTotalCashAmount() {
		return roundValue(totalCashAmount);
	}

	public Double getTotalCashOut() {
		return roundValue(totalCashOut);
	}

	private Double roundValue(Double amount) {
		if (amount != null) {
			return Math.round(amount * 100.0) / 100.0;

		} else {
			return 0d;
		}
	}

	public void computeItems() {
		this.totalItemGrossAmount = 0d;
		this.totalDiscountableAmount = 0d;
		this.totalItemDiscountAmount = 0d;
		this.totalTaxableAmount = 0d;
		this.totalItemTaxAmount = 0d;
		this.totalItemAmountDue = 0d;
		this.totalItemNetAmount = 0d;

		if (transactionItems != null && !transactionItems.isEmpty()) {
			transactionItems.forEach(item -> {
				this.totalItemGrossAmount = this.totalItemGrossAmount + item.getGrossAmount();
				this.totalItemNetAmount = this.totalItemNetAmount + item.getNetAmount();
				this.totalItemAmountDue = this.totalItemAmountDue + item.getAmountDue();

				if (item.isTaxable()) {
					this.totalItemTaxAmount = this.totalItemTaxAmount + item.getTaxAmount();
					this.totalTaxableAmount = this.totalTaxableAmount + item.getGrossAmount();
				}

				if (item.isDiscountable()) {
					this.totalItemDiscountAmount = this.totalItemDiscountAmount + item.getDiscountValue();
					this.totalDiscountableAmount = this.totalDiscountableAmount
							+ (item.getGrossAmount() - Math.abs(item.getTaxAmount()));
				}
			});
		}

		// for special discount
		if (this.specialDiscountAmount > 0) {
			this.totalDiscountableAmount = 0d;
			this.totalItemDiscountAmount = this.specialDiscountAmount;
			this.totalItemAmountDue = this.totalItemAmountDue - this.specialDiscountAmount;
			this.totalItemNetAmount = this.totalItemNetAmount - this.specialDiscountAmount;
		}
	}

	public void computePayments() {
		this.totalPaymentAmount = 0d;
		this.currency = null;
		this.biller = null;
		this.paymentType = null;
		this.paymentMode = null;

		if (transactionPayments != null && !transactionPayments.isEmpty()) {
			transactionPayments.forEach(payment -> {
				this.totalPaymentAmount = this.totalPaymentAmount + payment.getAmount();
				if (this.currency == null) {
					this.currency = payment.getCurrency();
				}
				if (this.paymentType == null) {
					this.paymentType = payment.getPaymentType();
				}
				if (this.paymentMode == null) {
					this.paymentMode = payment.getPaymentMode();
				}
				if (payment.getBiller() != null) {
					if (this.biller == null) {
						this.biller = payment.getBiller().getCompanyName();
					}
				}
			});
		}
	}

	public void computeChangeAmount() {
		this.totalChangeAmount = 0d;
		this.totalCashAmount = 0d;
		this.totalCashOut = 0d;

		if (transactionPayments != null && !transactionPayments.isEmpty()) {
			transactionPayments.forEach(payment -> {
				if ("CA".equals(payment.getPaymentType())) {
					this.totalCashAmount += payment.getAmount();
				}
			});
		}

		double change = roundValue(this.totalPaymentAmount - this.totalItemAmountDue);
		if ("SHO".equals(super.getStatus())) { // hold
			this.totalChangeAmount = 0d;
			this.totalCashOut = 0d;
		} else if ("SRE".equals(super.getStatus())) {// refund
			this.totalCashAmount = this.totalCashAmount * -1;
			this.totalItemAmountDue = this.totalItemAmountDue * -1;
			this.totalItemGrossAmount = this.totalItemGrossAmount * -1;
			this.totalItemNetAmount = this.totalItemNetAmount * -1;
			this.totalItemTaxAmount = this.totalItemTaxAmount * -1;
			this.totalPaymentAmount = this.totalPaymentAmount * -1;
			this.totalChangeAmount = this.totalItemAmountDue * -1;
			this.totalCashOut = this.totalItemAmountDue * -1;
		} else {
			this.totalChangeAmount = change;
			if (this.totalCashAmount >= change) {
				this.totalCashOut = change;
			} else {
				this.totalCashOut = this.totalCashAmount;
			}
		}
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

	@Override
	public int compareTo(QisTransaction o) {
		return this.getTransactionDate().compareTo(o.getTransactionDate());
	}

}
