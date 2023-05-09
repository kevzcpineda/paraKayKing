package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionClass;

@Entity
@DynamicUpdate
@Table(name = "qis_transactions")
public class QisTransactionData extends QisTransactionClass implements Serializable {

	private static final long serialVersionUID = 6783364429558349009L;

	private String biller = null;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private Set<QisQualityTransactionItem> transactionItems = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private Set<QisTransactionPayment> transactionPayments = new HashSet<>();
	
	public QisTransactionData() {
		super();
	}

	public String getBiller() {
		return biller;
	}

	public void setBiller(String biller) {
		this.biller = biller;
	}

	public Set<QisQualityTransactionItem> getTransactionItems() {
		return transactionItems;
	}

	public void setTransactionItems(Set<QisQualityTransactionItem> transactionItems) {
		this.transactionItems = transactionItems;
	}

	public Set<QisTransactionPayment> getTransactionPayments() {
		return transactionPayments;
	}

	public void setTransactionPayments(Set<QisTransactionPayment> transactionPayments) {
		this.transactionPayments = transactionPayments;
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
}
