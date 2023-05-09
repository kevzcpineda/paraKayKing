package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QisItemRequest implements Serializable {

	private static final long serialVersionUID = 6670231984413450673L;

	@NotEmpty(message = "Item Name is required.")
	@Size(min = 1, max = 120, message = "Item Name must between {min} and {max} characters.")
	private String itemName;

	@NotEmpty(message = "Item Description is required.")
	@Size(min = 1, max = 250, message = "Item Description must between {min} and {max} characters.")
	private String itemDescription;

	@NotNull(message = "Item Price is required.")
	@Digits(integer = 10, fraction = 4)
	private String itemPrice;

	@NotEmpty(message = "Item Category is required.")
	@Size(min = 1, max = 12, message = "Item Category must between {min} and {max} characters.")
	private String itemCategory;

	@NotEmpty(message = "Item Laboratory is required.")
	@Size(min = 1, max = 12, message = "Item Laboratory must between {min} and {max} characters.")
	private String itemLaboratory;

	@Size(min = 1, max = 12, message = "Item Laboratory Procedure must between {min} and {max} characters.")
	private String itemLaboratoryProcedure;

	private Boolean isActive;
	private Boolean isTaxable;
	private Boolean isDiscountable;
	private Boolean isOnMenu;
	private String specificTest;

	private Set<String> serviceRequest;
	
	public QisItemRequest() {
		super();
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemLaboratory() {
		return itemLaboratory;
	}

	public void setItemLaboratory(String itemLaboratory) {
		this.itemLaboratory = itemLaboratory;
	}

	public String getItemLaboratoryProcedure() {
		return itemLaboratoryProcedure;
	}

	public void setItemLaboratoryProcedure(String itemLaboratoryProcedure) {
		this.itemLaboratoryProcedure = itemLaboratoryProcedure;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(Boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	public Boolean isDiscountable() {
		return isDiscountable;
	}

	public void setDiscountable(Boolean isDiscountable) {
		this.isDiscountable = isDiscountable;
	}

	public Boolean isOnMenu() {
		return isOnMenu;
	}

	public void setOnMenu(Boolean isOnMenu) {
		this.isOnMenu = isOnMenu;
	}

	public Set<String> getServiceRequest() {
		return serviceRequest;
	}

	public void setServiceRequest(Set<String> serviceRequest) {
		this.serviceRequest = serviceRequest;
	}

	public String getSpecificTest() {
		return specificTest;
	}

	public void setSpecificTest(String specificTest) {
		this.specificTest = specificTest;
	}
	
	
}
