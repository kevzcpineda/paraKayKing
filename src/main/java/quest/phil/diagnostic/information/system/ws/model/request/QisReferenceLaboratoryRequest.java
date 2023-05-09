package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

public class QisReferenceLaboratoryRequest implements Serializable {

	private static final long serialVersionUID = 7073239720723062985L;

	
	@NotEmpty(message = "Reference Laboratory Name is required.")
	private String name;
	
	private String address;
	private String contactPerson;
	private String contactNumber;
	private String sopEmail;
	private String resultsEmail;
	
	@NotEmpty(message = "Sop Code is required.")
	private String sopCode;
	
	private Boolean isActive;
	
	private Set<QisReferenceLaboratoryItemsRequest> ReferenceItems;
	
	private Set<QisReferencePackageRequest> ReferencePackages;

	private String referenceId;
	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public String getSopEmail() {
		return sopEmail;
	}

	public String getResultsEmail() {
		return resultsEmail;
	}

	public String getSopCode() {
		return sopCode;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public void setSopEmail(String sopEmail) {
		this.sopEmail = sopEmail;
	}

	public void setResultsEmail(String resultsEmail) {
		this.resultsEmail = resultsEmail;
	}

	public void setSopCode(String sopCode) {
		this.sopCode = sopCode;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Set<QisReferenceLaboratoryItemsRequest> getReferenceItems() {
		return ReferenceItems;
	}

	public void setReferenceItems(Set<QisReferenceLaboratoryItemsRequest> referenceItems) {
		ReferenceItems = referenceItems;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public Set<QisReferencePackageRequest> getReferencePackages() {
		return ReferencePackages;
	}

	public void setReferencePackages(Set<QisReferencePackageRequest> referencePackages) {
		ReferencePackages = referencePackages;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
}
