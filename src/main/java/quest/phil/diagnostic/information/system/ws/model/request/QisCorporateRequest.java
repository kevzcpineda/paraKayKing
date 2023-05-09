package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class QisCorporateRequest implements Serializable {

	private static final long serialVersionUID = -6433562240958373786L;

	@NotEmpty(message = "Company Name is required.")
	@Size(min = 1, max = 120, message = "Company Name must between {min} and {max} characters.")
	private String companyName;

	@Size(max = 250, message = "Company Address must not exceed to {max} characters.")
	private String companyAddress;

	@Size(max = 120, message = "Contact Person must not exceed to {max} characters.")
	private String contactPerson;

	@Size(max = 120, message = "Contact number must not exceed to {max} characters.")
	private String contactNumber;

	@Size(max = 120, message = "Email must not exceed to {max} characters.")
	private String email;

	@Size(max = 200, message = "Result Email must not exceed to {max} characters.")
	private String resultEmail;

	@NotNull(message = "Charge Type is required.")
	@NotEmpty(message = "Charge Type should not be empty.")
	private String chargeType;
	
	@NotNull(message = "SOA Code is required.")
	@NotEmpty(message = "SOA Code should not be empty.")
	@Size(min = 3, max = 8, message = "SOA Code must between {min} and {max} characters.")
	private String soaCode;
	
	private Boolean isActive;

	public QisCorporateRequest() {
		super();
	}
	
	public QisCorporateRequest(
			@NotEmpty(message = "Company Name is required.") @Size(min = 1, max = 120, message = "Company Name must between {min} and {max} characters.") String companyName) {
		super();
		this.companyName = companyName;
	}

	public String getCompanyName() {
		if(companyName != null) {
			return companyName.trim().toUpperCase();
		} 
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResultEmail() {
		return resultEmail;
	}

	public void setResultEmail(String resultEmail) {
		this.resultEmail = resultEmail;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getSoaCode() {
		return soaCode;
	}

	public void setSoaCode(String soaCode) {
		this.soaCode = soaCode;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
