package quest.phil.diagnostic.information.system.ws.model.response;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisDoctorResponse implements Serializable {

	private static final long serialVersionUID = 8984819372634127188L;
	private String doctorid;
	private String firstname;
	private String lastname;
	private String middlename;
	private String specialization;
	private String contactNumber;
	private String licenseNumber;
	private String email;
	private String suffix;
	private Boolean isActive;
	private int doctorType = 0;

	public QisDoctorResponse() {
		super();
	}

	public QisDoctorResponse(String doctorid, String firstname, String lastname, String middlename,
			String specialization, String contactNumber, String licenseNumber, String email, Boolean isActive) {
		super();
		this.doctorid = doctorid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.middlename = middlename;
		this.specialization = specialization;
		this.contactNumber = contactNumber;
		this.licenseNumber = licenseNumber;
		this.email = email;
		this.isActive = isActive;
	}

	public String getDoctorid() {
		return doctorid;
	}

	public void setDoctorid(String doctorid) {
		this.doctorid = doctorid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getMiddlename() {
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

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public int getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(int doctorType) {
		this.doctorType = doctorType;
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
