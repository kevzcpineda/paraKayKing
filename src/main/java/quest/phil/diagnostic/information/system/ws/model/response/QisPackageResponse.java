package quest.phil.diagnostic.information.system.ws.model.response;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisPackageResponse implements Serializable {

	private static final long serialVersionUID = 1596481505208215525L;
	private String packageid;
	private String packageName;
	private String packageDescription;
	private Double packagePrice;
	private String packageType;
	private Boolean isActive;
	private Boolean isTaxable;
	private Boolean isDiscountable;
	private Boolean isOnMenu;
	private Set<String> packageItems;
	
	public QisPackageResponse() {
		super();
	}

	public QisPackageResponse(String packageid, String packageName, String packageDescription, Double packagePrice,
			String packageType, Boolean isActive, Set<String> packageItems) {
		super();
		this.packageid = packageid;
		this.packageName = packageName;
		this.packageDescription = packageDescription;
		this.packagePrice = packagePrice;
		this.packageType = packageType;
		this.isActive = isActive;
		this.packageItems = packageItems;
	}

	public String getPackageid() {
		return packageid;
	}

	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageDescription() {
		return packageDescription;
	}

	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}

	public Double getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(Double packagePrice) {
		this.packagePrice = packagePrice;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
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
	
	public Set<String> getPackageItems() {
		return packageItems;
	}

	public void setPackageItems(Set<String> packageItems) {
		this.packageItems = packageItems;
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
