package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@DynamicUpdate
@Table(name = "qis_reference_items", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" })})
public class QisReferenceLaboratoryItems implements Serializable {

	private static final long serialVersionUID = -1542008718034155889L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "orig_price", nullable = false, length = 20)
	private double originalPrice;
	
	@Column(name = "mole_price", nullable = false, length = 20)
	private double molePrice;
	

	@JsonIgnore
	@Column(name = "reference_item_id", nullable = true)
	private Long referenceItemId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "reference_item_id", insertable = false, updatable =false)
	private QisItem referenceLabItems;
	
	public QisReferenceLaboratoryItems() {
		super();
	}
	public long getId() {
		return id;
	}
	public double getOriginalPrice() {
		return originalPrice;
	}
	public double getMolePrice() {
		return molePrice;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}
	public void setMolePrice(double molePrice) {
		this.molePrice = molePrice;
	}
	public Long getReferenceItemId() {
		return referenceItemId;
	}
	public QisItem getReferenceLabItems() {
		return referenceLabItems;
	}
	public void setReferenceItemId(Long referenceItemId) {
		this.referenceItemId = referenceItemId;
	}
	public void setReferenceLabItems(QisItem referenceLabItems) {
		this.referenceLabItems = referenceLabItems;
	}
	
}
