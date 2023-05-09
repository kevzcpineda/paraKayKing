package quest.phil.diagnostic.information.system.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisLabelInformation implements Serializable, Comparable<QisLabelInformation> {

	private static final long serialVersionUID = 3967651512138800356L;
	private Long id;
	private String fullname;
	private String ageGender;
	private String biller;
	private String date;
	private boolean withXRay;

	public QisLabelInformation(Long id, String fullname, String ageGender, String biller, String date,
			boolean withXRay) {
		super();
		this.id = id;
		this.fullname = fullname;
		this.ageGender = ageGender;
		this.biller = biller;
		this.date = date;
		this.withXRay = withXRay;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getAgeGender() {
		return ageGender;
	}

	public void setAgeGender(String ageGender) {
		this.ageGender = ageGender;
	}

	public String getBiller() {
		return biller;
	}

	public void setBiller(String biller) {
		this.biller = biller;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isWithXRay() {
		return withXRay;
	}

	public void setWithXRay(boolean withXRay) {
		this.withXRay = withXRay;
	}

	@Override
	public int compareTo(QisLabelInformation o) {
		return this.getId().compareTo(o.getId());
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
