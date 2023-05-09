package quest.phil.diagnostic.information.system.ws.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import quest.phil.diagnostic.information.system.ws.model.classes.QisTransactionItemClass;

@Entity
@DynamicUpdate
@Table(name = "qis_transaction_items")
public class QisTransactionItem extends QisTransactionItemClass
		implements Serializable, Comparable<QisTransactionItem> {

	private static final long serialVersionUID = 2182157159997124841L;

	public QisTransactionItem() {
		super();
	}

	public QisTransactionItem(
			@NotNull(message = "Item/Package ID is required.") @NotEmpty(message = "Item/Package ID should not be empty") Long itemid,
			@NotNull(message = "Type is required.") @NotEmpty(message = "Type should not be empty.") @Size(max = 4, message = "Type should not exceed {max} characters.") String itemType,
			@NotNull(message = "Quantity is required.") @Min(value = 1, message = "Quantity should be minimum of {value}.") int quantity,
			@NotNull(message = "Price is required.") Double itemPrice, String itemReference, Boolean isDiscountable,
			Boolean isTaxable) {
		super(itemid, itemType, quantity, itemPrice, itemReference, isDiscountable, isTaxable);
	}

	@Override
	public int compareTo(QisTransactionItem o) {
		return this.getId().compareTo(o.getId());
	}

}
