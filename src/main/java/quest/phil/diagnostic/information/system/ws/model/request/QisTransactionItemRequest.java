package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QisTransactionItemRequest implements Serializable {

	private static final long serialVersionUID = -7737418320792072955L;

	public Long id;

	@NotNull(message = "Item Id is required.")
	@NotEmpty(message = "Item Id should not be empty.")
	@Size(max = 20, message = "Item Id should not exceed {max} characters.")
	public String itemid;

	@NotNull(message = "Type is required.")
	@NotEmpty(message = "Type should not be empty.")
	@Size(min=3, max = 4, message = "Type must between {min} and {max} characters.")
	public String itemType;

	@NotNull(message = "Quantity is required.")
	@Min(value = 1, message = "Quantity should be minimum of {value}.")
	public Integer quantity;

	public Integer discountRate;
	
	@Size(max = 4, message = "Item Id should not exceed {max} characters.")
	public String discountType;

	public QisTransactionItemRequest() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Integer discountRate) {
		this.discountRate = discountRate;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
}
