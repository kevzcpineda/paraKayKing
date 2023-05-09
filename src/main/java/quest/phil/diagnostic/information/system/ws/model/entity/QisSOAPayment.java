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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.classes.QisSOAPaymentClass;

@Entity
@DynamicUpdate
@Table(name = "qis_soa_payment")
public class QisSOAPayment extends QisSOAPaymentClass implements Serializable {

	private static final long serialVersionUID = 933178299337792114L;

	@OneToMany(fetch = FetchType.EAGER) // LAZY or EAGER
	@JoinTable(name = "qis_soa_payment_transactions", joinColumns = @JoinColumn(name = "qis_soa_payment_id"), inverseJoinColumns = @JoinColumn(name = "qis_soa_id"))
	private Set<QisSOA> soaList = new HashSet<>();

	@Lob
	@Column(name = "receipt", nullable = true, columnDefinition = "LONGBLOB DEFAULT NULL")
	protected byte[] receipt;
	
	@Column(name = "image_type", nullable = true, length = 40)
	private String imageType;	
	
	public QisSOAPayment() {
		super();
	}

	public Set<QisSOA> getSoaList() {
		return soaList;
	}

	public void setSoaList(Set<QisSOA> soaList) {
		this.soaList = soaList;
	}

	public byte[] getReceipt() {
		return receipt;
	}

	public void setReceipt(byte[] receipt) {
		this.receipt = receipt;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
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
