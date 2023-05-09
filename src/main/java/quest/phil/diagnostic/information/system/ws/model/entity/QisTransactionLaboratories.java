package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionClass;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;

@Entity
@DynamicUpdate
@Table(name = "qis_transactions")
public class QisTransactionLaboratories extends QisTransactionClass implements Serializable {

	private static final long serialVersionUID = -3816147273318800446L;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private Set<QisTransactionLaboratoryInfo> transactionLabRequests = new HashSet<>();

	private String biller = null;

	public QisTransactionLaboratories() {
		super();
	}

	public Set<QisTransactionLaboratoryInfo> getTransactionLabRequests() {
		return transactionLabRequests.stream().filter(tli -> tli.getTransactionItem().getStatus() == 1)
				.collect(Collectors.toSet());
	}

	public void setTransactionLabRequests(Set<QisTransactionLaboratoryInfo> transactionLabRequests) {
		this.transactionLabRequests = transactionLabRequests;
	}

	public String getBiller() {
		return biller;
	}

	public void setBiller(String biller) {
		this.biller = biller;
	}

}
