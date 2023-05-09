package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

import quest.phil.diagnostic.information.system.ws.model.classes.QisSOPClass;

@Entity
@DynamicUpdate
@Table(name = "qis_sop", uniqueConstraints = { @UniqueConstraint(columnNames = { "sop_number" }) })
public class QisSOP extends QisSOPClass implements Serializable   {

	
	private static final long serialVersionUID = -9048913917851265892L;
	
	@ManyToMany(fetch = FetchType.EAGER) // LAZY or EAGER
	@JoinTable(name = "qis_sop_transactions", joinColumns = @JoinColumn(name = "qis_sop_id"), inverseJoinColumns = @JoinColumn(name = "qis_transaction_id"))
	private Set<QisTransactionLaboratories> transactions = new HashSet<>();
		
	public QisSOP() {
		super();
	}

	public Set<QisTransactionLaboratories> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<QisTransactionLaboratories> transactions) {
		this.transactions = transactions;
	}
}
