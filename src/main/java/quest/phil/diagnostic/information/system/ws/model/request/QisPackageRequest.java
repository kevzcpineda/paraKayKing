package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QisPackageRequest implements Serializable {

	private static final long serialVersionUID = 42089103766102807L;

	@NotEmpty(message = "Package Name is required.")
	@Size(min = 1, max = 120, message = "Package Name must between {min} and {max} characters.")
	private String packageName;

	@NotEmpty(message = "Package Description is required.")
	@Size(min = 1, max = 250, message = "Package Description must between {min} and {max} characters.")
	private String packageDescription;

	@NotNull(message = "Package Price is required.")
	@Digits(integer=10, fraction=4)
	private String packagePrice;

	@NotEmpty(message = "Package Type is required.")
	@Size(min = 1, max = 12, message = "Package Type must between {min} and {max} characters.")
	private String packageType;

	@NotNull(message = "Package Item(s) is required.")
	@NotEmpty(message = "Package Item(s) must not be empty.")
	private Set<String> packageItems;

	private Boolean isActive;
	private Boolean isTaxable;
	private Boolean isDiscountable;
	private Boolean isOnMenu;
	
	@NotEmpty(message = "Type Test Package is required.")
	@Size(min = 1, max = 30, message = "Type Test Package must between {min} and {max} characters.")
	private String typeTestPackage;

	public QisPackageRequest() {
		super();
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

	public String getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(String packagePrice) {
		this.packagePrice = packagePrice;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public Set<String> getPackageItems() {
		return packageItems;
	}

	public void setPackageItems(Set<String> packageItems) {
		this.packageItems = packageItems;
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

	public String getTypeTestPackage() {
		return typeTestPackage;
	}

	public void setTypeTestPackage(String typeTestPackage) {
		this.typeTestPackage = typeTestPackage;
	}	
	
	
}
