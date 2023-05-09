package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class QisDoctorRequest implements Serializable {

	private static final long serialVersionUID = 8558788118113391062L;

	@NotEmpty(message = "First Name is required.")
	@Size(min = 1, max = 120, message = "First Name must between {min} and {max} characters.")
	private String firstname;

	@NotEmpty(message = "Last Name is required.")
	@Size(min = 1, max = 120, message = "Last Name must between {min} and {max} characters.")
	private String lastname;

	@Size(max = 120, message = "Middle Name must not exceed to {max} characters.")
	private String middlename;

	@Size(max = 250, message = "Speciallization must not exceed to {max} characters.")
	private String specialization;

	@Size(max = 120, message = "Contact Number must not exceed to {max} characters.")
	private String contactNumber;

	@NotEmpty(message = "License Number is required.")
	@Size(min = 1, max = 20, message = "License Number must between {min} and {max} characters.")
	private String licenseNumber;

	@Size(max = 120, message = "Email must not exceed to {max} characters.")
	@Email(message = "Input should be an email format.")
	private String email;

	@NotEmpty(message = "Suffix is required.")
	@Size(min = 1, max = 20, message = "License Number must between {min} and {max} characters.")
	private String suffix;

	private Integer doctorType;
	
	private Boolean isActive;

	public QisDoctorRequest() {
		super();
	}

	public QisDoctorRequest(
			@NotEmpty(message = "First Name is required.") @Size(min = 1, max = 120, message = "First Name must between {min} and {max} characters.") String firstname,
			@NotEmpty(message = "Last Name is required.") @Size(min = 1, max = 120, message = "Last Name must between {min} and {max} characters.") String lastname,
			@Size(max = 120, message = "Middle Name must not exceed to {max} characters.") String middlename,
			@Size(max = 250, message = "Speciallization must not exceed to {max} characters.") String specialization,
			@Size(max = 120, message = "Contact Number must not exceed to {max} characters.") String contactNumber,
			@NotEmpty(message = "License Number is required.") @Size(min = 1, max = 40, message = "License Number must between {min} and {max} characters.") String licenseNumber,
			@Size(max = 120, message = "Email must not exceed to {max} characters.") @Email(message = "Input should be an email format.") String email) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.middlename = middlename;
		this.specialization = specialization;
		this.contactNumber = contactNumber;
		this.licenseNumber = licenseNumber;
		this.email = email;
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

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getLicenseNumber() {
		if (licenseNumber != null) {
			return licenseNumber.trim().toUpperCase();
		}
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Integer getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(Integer doctorType) {
		this.doctorType = doctorType;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
