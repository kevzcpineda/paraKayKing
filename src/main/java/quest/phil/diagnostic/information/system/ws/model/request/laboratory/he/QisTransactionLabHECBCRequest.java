package quest.phil.diagnostic.information.system.ws.model.request.laboratory.he;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabHECBCRequest implements Serializable {

	private static final long serialVersionUID = -7581477752600903940L;

	@Digits(integer = 10, fraction = 4)
	private String whiteBloodCells;

	@Digits(integer = 10, fraction = 4)
	private String basophils;

	@Digits(integer = 10, fraction = 4)
	private String neutrophils;

	@Digits(integer = 10, fraction = 4)
	private String redBloodCells;

	@Digits(integer = 10, fraction = 4)
	private String lymphocytes;

	@Digits(integer = 10, fraction = 4)
	private String hemoglobin;

	@Digits(integer = 10, fraction = 4)
	private String monocytes;

	@Digits(integer = 10, fraction = 4)
	private String hematocrit;

	@Digits(integer = 10, fraction = 4)
	private String eosinophils;

	@Digits(integer = 10, fraction = 4)
	private String platelet;
	
	private String referenceLabId;

	public QisTransactionLabHECBCRequest() {
		super();
	}

	public String getWhiteBloodCells() {
		return whiteBloodCells;
	}

	public void setWhiteBloodCells(String whiteBloodCells) {
		this.whiteBloodCells = whiteBloodCells;
	}

	public String getBasophils() {
		return basophils;
	}

	public void setBasophils(String basophils) {
		this.basophils = basophils;
	}

	public String getNeutrophils() {
		return neutrophils;
	}

	public void setNeutrophils(String neutrophils) {
		this.neutrophils = neutrophils;
	}

	public String getRedBloodCells() {
		return redBloodCells;
	}

	public void setRedBloodCells(String redBloodCells) {
		this.redBloodCells = redBloodCells;
	}

	public String getLymphocytes() {
		return lymphocytes;
	}

	public void setLymphocytes(String lymphocytes) {
		this.lymphocytes = lymphocytes;
	}

	public String getHemoglobin() {
		return hemoglobin;
	}

	public void setHemoglobin(String hemoglobin) {
		this.hemoglobin = hemoglobin;
	}

	public String getMonocytes() {
		return monocytes;
	}

	public void setMonocytes(String monocytes) {
		this.monocytes = monocytes;
	}

	public String getHematocrit() {
		return hematocrit;
	}

	public void setHematocrit(String hematocrit) {
		this.hematocrit = hematocrit;
	}

	public String getEosinophils() {
		return eosinophils;
	}

	public void setEosinophils(String eosinophils) {
		this.eosinophils = eosinophils;
	}

	public String getPlatelet() {
		return platelet;
	}

	public void setPlatelet(String platelet) {
		this.platelet = platelet;
	}

	public String getReferenceLabId() {
		return referenceLabId;
	}

	public void setReferenceLabId(String referenceLabId) {
		this.referenceLabId = referenceLabId;
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
