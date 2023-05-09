package quest.phil.diagnostic.information.system.ws.model.response;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisItemResponse implements Serializable {

	private static final long serialVersionUID = -4871711665259563060L;
	private String itemid;
	private String itemName;
	private String itemDescription;
	private Double itemPrice;
	private String itemCategory;
	private String itemLaboratory;
	private String itemLaboratoryProcedure;
	private Boolean isActive;
	private Boolean isTaxable;
	private Boolean isDiscountable;
	private Boolean isOnMenu;
	private Set<String> serviceRequest;

	public QisItemResponse() {
		super();
	}

	public QisItemResponse(String itemid, String itemName, String itemDescription, Double itemPrice, String itemCategory,
			String itemLaboratory, String itemLaboratoryProcedure, Boolean isActive) {
		super();
		this.itemid = itemid;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.itemPrice = itemPrice;
		this.itemCategory = itemCategory;
		this.itemLaboratory = itemLaboratory;
		this.itemLaboratoryProcedure = itemLaboratoryProcedure;
		this.isActive = isActive;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
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

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
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
