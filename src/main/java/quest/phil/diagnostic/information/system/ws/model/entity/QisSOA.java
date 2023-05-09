package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.classes.QisSOAClass;

@Entity
@DynamicUpdate
@Table(name = "qis_soa", uniqueConstraints = { @UniqueConstraint(columnNames = { "soa_number" }) })
public class QisSOA extends QisSOAClass implements Serializable, Comparable<QisSOA> {

	private static final long serialVersionUID = -811832741296004875L;

	@OneToMany(fetch = FetchType.EAGER) // LAZY or EAGER
	@JoinTable(name = "qis_soa_transactions", joinColumns = @JoinColumn(name = "qis_soa_id"), inverseJoinColumns = @JoinColumn(name = "qis_transaction_id"))
	private Set<QisTransaction> transactions = new HashSet<>();

	@ManyToOne
	@JoinTable(name = "qis_soa_payment_transactions", joinColumns = {
			@JoinColumn(name = "qis_soa_id", insertable = false, updatable = false, referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "qis_soa_payment_id", insertable = false, updatable = false, referencedColumnName = "id") })
	private QisSOAPaymentData payment;

	public QisSOA() {
		super();
	}

	public Set<QisTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<QisTransaction> transactions) {
		this.transactions = transactions;
	}

	public QisSOAPaymentData getPayment() {
		return payment;
	}

	public void setPayment(QisSOAPaymentData payment) {
		this.payment = payment;
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
	public int compareTo(QisSOA o) {
		return this.getStatementDate().compareTo(o.getStatementDate());
	}
}
