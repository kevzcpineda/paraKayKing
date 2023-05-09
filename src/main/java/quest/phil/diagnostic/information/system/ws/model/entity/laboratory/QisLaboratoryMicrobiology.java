package quest.phil.diagnostic.information.system.ws.model.entity.laboratory;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.mb.QisTransactionLabGS;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_laboratory_requests")
public class QisLaboratoryMicrobiology  implements Serializable  {

	private static final long serialVersionUID = -7022722203649835389L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionLabGS gs;
	
	public QisLaboratoryMicrobiology() {
		super();
	}

	public Long getId() {
		return id;
	}

	public QisTransactionLabGS getGs() {
		return gs;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setGs(QisTransactionLabGS gs) {
		this.gs = gs;
	}
	
}
