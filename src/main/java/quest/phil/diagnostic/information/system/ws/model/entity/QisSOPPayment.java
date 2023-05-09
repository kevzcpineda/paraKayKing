package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import quest.phil.diagnostic.information.system.ws.model.classes.QisSOPPaymentClass;

@Entity
@DynamicUpdate
@Table(name = "qis_sop_payment")
public class QisSOPPayment  extends QisSOPPaymentClass implements Serializable {

	private static final long serialVersionUID = 5892273957078046027L;
	
	
	@OneToMany(fetch = FetchType.EAGER) // LAZY or EAGER
	@JoinTable(name = "qis_sop_payment_transactions", joinColumns = @JoinColumn(name = "qis_sop_payment_id"), inverseJoinColumns = @JoinColumn(name = "qis_sop_id"))
	private Set<QisSOP> soaList = new HashSet<>();
	
	@Lob
	@Column(name = "receipt", nullable = true, columnDefinition = "LONGBLOB DEFAULT NULL")
	protected byte[] receipt;
	
	@Column(name = "image_type", nullable = true, length = 40)
	private String imageType;	
	
	
	public QisSOPPayment() {
		super();
	}
	
	public Set<QisSOP> getSoaList() {
		return soaList;
	}


	public byte[] getReceipt() {
		return receipt;
	}


	public String getImageType() {
		return imageType;
	}


	public void setSoaList(Set<QisSOP> soaList) {
		this.soaList = soaList;
	}


	public void setReceipt(byte[] receipt) {
		this.receipt = receipt;
	}


	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

}
