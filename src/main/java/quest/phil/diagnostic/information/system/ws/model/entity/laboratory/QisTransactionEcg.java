package quest.phil.diagnostic.information.system.ws.model.entity.laboratory;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionLaboratoryRequestClass;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ecg.QisTransactionLabEcg;
@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisTransactionEcg extends QisTransactionLaboratoryRequestClass implements Serializable {

	private static final long serialVersionUID = 8255654699173965069L;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionInfo transaction;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabEcg ecg;
	
	public QisTransactionEcg() {
		super();
	}

	public QisTransactionInfo getTransaction() {
		return transaction;
	}

	public QisTransactionLabEcg getEcg() {
		return ecg;
	}

	public void setTransaction(QisTransactionInfo transaction) {
		this.transaction = transaction;
	}

	public void setEcg(QisTransactionLabEcg ecg) {
		this.ecg = ecg;
	}
	

}
