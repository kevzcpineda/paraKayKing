package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionItemClass;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_items")
//@JsonIgnoreProperties({"itemDetails"})
public class QisTransactionItemLaboratories extends QisTransactionItemClass implements Serializable {

	private static final long serialVersionUID = -7306142667106839823L;

	@ManyToOne(optional = true)
	@JoinColumn(name = "qis_transaction_id", insertable = false, updatable = false)
	@JsonManagedReference
	private QisTransactionInfo transaction;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "qis_transaction_item_id", insertable = false, updatable = false)
	@JsonManagedReference
	private Set<QisTransactionLaboratoryInfo> transactionLabRequests = new HashSet<>();

	public QisTransactionItemLaboratories() {
		super();
	}

	public QisTransactionItemLaboratories(
			@NotNull(message = "Item/Package ID is required.") @NotEmpty(message = "Item/Package ID should not be empty") Long itemid,
			@NotNull(message = "Type is required.") @NotEmpty(message = "Type should not be empty.") @Size(max = 4, message = "Type should not exceed {max} characters.") String itemType,
			@NotNull(message = "Quantity is required.") @Min(value = 1, message = "Quantity should be minimum of {value}.") int quantity,
			@NotNull(message = "Price is required.") Double itemPrice, String itemReference, Boolean isDiscountable,
			Boolean isTaxable) {
		super(itemid, itemType, quantity, itemPrice, itemReference, isDiscountable, isTaxable);
	}

	public QisTransactionInfo getTransaction() {
		return transaction;
	}

	public void setTransaction(QisTransactionInfo transaction) {
		this.transaction = transaction;
	}

	public Set<QisTransactionLaboratoryInfo> getTransactionLabRequests() {
		return transactionLabRequests;
	}

	public void setTransactionLabRequests(Set<QisTransactionLaboratoryInfo> transactionLabRequests) {
		this.transactionLabRequests = transactionLabRequests;
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
