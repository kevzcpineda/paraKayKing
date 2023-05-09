package quest.phil.diagnostic.information.system.ws.model.response;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisCorporateResponse implements Serializable {

	private static final long serialVersionUID = -7722572546960732800L;
	private String corporateid;
	private String companyName;
	private String companyAddress;
	private String contactPerson;
	private String contactNumber;
	private String email;
	private Boolean isActive;
	private String resultEmail;
	private String chargeType;	
	private String soaCode;

	public QisCorporateResponse() {
		super();
	}

	public QisCorporateResponse(String corporateid, String companyName, String companyAddress, String contactPerson,
			String contactNumber, String email, Boolean isActive) {
		super();
		this.corporateid = corporateid;
		this.companyName = companyName;
		this.companyAddress = companyAddress;
		this.contactPerson = contactPerson;
		this.contactNumber = contactNumber;
		this.email = email;
		this.isActive = isActive;
	}

	public String getCorporateid() {
		return corporateid;
	}

	public void setCorporateid(String corporateid) {
		this.corporateid = corporateid;
	}

	public String getCompanyName() {
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

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
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
