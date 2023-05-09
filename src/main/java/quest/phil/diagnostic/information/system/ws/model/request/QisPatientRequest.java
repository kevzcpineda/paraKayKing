package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class QisPatientRequest implements Serializable {

	private static final long serialVersionUID = -6896070801795578266L;

	@NotEmpty(message = "First Name is required.")
	@Size(min = 1, max = 120, message = "First Name must between {min} and {max} characters.")
	private String firstname;

	@NotEmpty(message = "Last Name is required.")
	@Size(min = 1, max = 120, message = "Last Name must between {min} and {max} characters.")
	private String lastname;

	@Size(max = 120, message = "Middle Name must not exceed to {max} characters.")
	private String middlename;

	@NotEmpty(message = "Date of Birth is required(yyyy-MM-DD).")
	@Pattern(message = "Invalid date format(yyyy-MM-DD)", regexp = "^\\d{4}-\\d{2}-\\d{2}$")
	private String dateOfBirth;

	@NotEmpty(message = "Contact is required.")
	@Size(min = 1, max = 20, message = "Contact Number must between {min} and {max} characters.")
	private String contactNumber;

//	@NotEmpty(message = "Email is required.")
//	@Size(min = 1, max = 120, message = "Email must between {min} and {max} characters.")
	private String email;

	@NotEmpty(message = "Address is required.")
	@Size(max = 250, message = "Address must not exceed to {max} characters.")
	private String address;
	
	@Size(max = 20, message = "Senior ID must not exceed to {max} characters.")
	private String seniorId;

	@Size(max = 20, message = "PWD ID must not exceed to {max} characters.")
	private String pwdId;
	
	@Size(max = 30, message = "Passport ID must not exceed to {max} characters.")
	private String passport;

	private String corporateId;

	@NotNull(message = "Nationality is required.")
	private Long nationalityId;

	public String height;

	public String weight;
	
	private Boolean isActive;

	// 1-male 0-female
	@NotNull(message = "Gender is required('M'-Male, 'F'-Female).")
	@Size(min = 1, max = 1, message = "Gender must be ('M'-Male, 'F'-Female).")
	private String gender;

	public QisPatientRequest() {
		super();
	}

	public String getHeight() {
		return height;
	}

	public String getWeight() {
		return weight;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setWeight(String weight) {
		this.weight = weight;
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

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public String getSeniorId() {
		return seniorId;
	}

	public void setSeniorId(String seniorId) {
		this.seniorId = seniorId;
	}

	public String getPwdId() {
		return pwdId;
	}

	public void setPwdId(String pwdId) {
		this.pwdId = pwdId;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getCorporateId() {
		return corporateId;
	}

	public void setCorporateId(String corporateId) {
		this.corporateId = corporateId;
	}

	public Long getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(Long nationalityId) {
		this.nationalityId = nationalityId;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
