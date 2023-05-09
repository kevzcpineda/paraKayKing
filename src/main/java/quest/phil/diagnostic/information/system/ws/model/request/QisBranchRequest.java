package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class QisBranchRequest implements Serializable {

	private static final long serialVersionUID = -8065549136425011540L;
	@NotEmpty(message = "Branch Code is required.")
	@Size(min = 1, max = 12, message = "Branch Code must between {min} and {max} characters.")
	private String branchCode;

	@NotEmpty(message = "Branch Name is required.")
	@Size(min = 1, max = 40, message = "Branch Name must between {min} and {max} characters.")
	private String branchName;
	
	@Size(max = 250, message = "Address must not exceed to {max} characters.")
	private String address;

	@Size(max = 20, message = "Contact Number must not exceed to {max} characters.")
	private String contactNumber;
	
	private Boolean isActive;

	public QisBranchRequest() {
		super();
	}

	public String getBranchCode() {
		if(branchCode != null) {
			return branchCode.trim().toUpperCase();
		} 
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
