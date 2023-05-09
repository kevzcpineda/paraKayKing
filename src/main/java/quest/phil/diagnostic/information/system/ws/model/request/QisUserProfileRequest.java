package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisUserProfileRequest implements Serializable {

	private static final long serialVersionUID = 3878696375433837161L;

	@NotEmpty(message = "First Name is required.")
	@Size(min = 1, max = 120, message = "First Name must between {min} and {max} characters.")
	private String firstname;

	@NotEmpty(message = "Last Name is required.")
	@Size(min = 1, max = 120, message = "Last Name must between {min} and {max} characters.")
	private String lastname;

	@Size(max = 120, message = "Middle Name must not exceed to {max} characters.")
	private String middlename;

	@Size(max = 20, message = "License Number must not exceed to {max} characters.")
	private String suffix;

	@Size(max = 20, message = "License Number must not exceed to {max} characters.")
	private String licenseNumber;

	@NotEmpty(message = "Date of Birth is required(yyyy-MM-DD).")
	@Pattern(message = "Invalid date format(yyyy-MM-DD)", regexp = "^\\d{4}-\\d{2}-\\d{2}$")
	private String dateOfBirth;

	// 1-male 0-female
	@NotNull(message = "Gender is required('M'-Male, 'F'-Female).")
	@Size(min = 1, max = 1, message = "Gender must be ('M'-Male, 'F'-Female).")
	private String gender;
	
	@NotEmpty(message = "Contact is required.")
	@Size(min = 1, max = 20, message = "Contact Number must between {min} and {max} characters.")
	private String contactNumber;

	@NotEmpty(message = "Email is required.")
	@Size(min = 1, max = 120, message = "Email must between {min} and {max} characters.")
	@Email(message = "Input should be an email format.")
	private String email;

	@NotEmpty(message = "Address is required.")
	@Size(max = 250, message = "Address must not exceed to {max} characters.")
	private String address;

	public QisUserProfileRequest() {
		super();
	}

	public String getFirstname() {
		if (firstname != null) {
			return firstname.trim().toUpperCase();
		}
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		if (lastname != null) {
			return lastname.trim().toUpperCase();
		}
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getMiddlename() {
		if (middlename != null) {
			return middlename.trim().toUpperCase();
		}
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getSuffix() {
		if (suffix != null) {
			return suffix.trim().toUpperCase();
		}
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
