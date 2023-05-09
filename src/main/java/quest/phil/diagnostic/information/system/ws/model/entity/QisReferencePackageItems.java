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
@Table(name = "qis_reference_package", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" })})
public class QisReferencePackageItems implements Serializable{

	private static final long serialVersionUID = -5386419400594434233L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "orig_price", nullable = false, length = 20)
	private double originalPrice;
	
	@Column(name = "mole_price", nullable = false, length = 20)
	private double molePrice;
	

	@JsonIgnore
	@Column(name = "reference_package_id", nullable = true)
	private Long referencePackageId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "reference_package_id", insertable = false, updatable =false)
	private QisPackage referencePackageItems;
	
	public QisReferencePackageItems() {
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

	public Long getReferencePackageId() {
		return referencePackageId;
	}

	public QisPackage getReferencePackageItems() {
		return referencePackageItems;
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

	public void setReferencePackageId(Long referenceItemId) {
		this.referencePackageId = referenceItemId;
	}

	public void setReferencePackageItems(QisPackage referencePackageItems) {
		this.referencePackageItems = referencePackageItems;
	}
}
