package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHElecRequest implements Serializable {

	private static final long serialVersionUID = -6443894781143234775L;

	@Digits(integer = 10, fraction = 4)
	private String sodium;

	@Digits(integer = 10, fraction = 4)
	private String potassium;

	@Digits(integer = 10, fraction = 4)
	private String chloride;

	@Digits(integer = 10, fraction = 4)
	private String inorganicPhosphorus;
	
	@Digits(integer = 10, fraction = 4)
	private String totalCalcium;

	@Digits(integer = 10, fraction = 4)
	private String ionizedCalcium;

	@Digits(integer = 10, fraction = 4)
	private String magnesium;
	
	@Digits(integer = 10, fraction = 4)
	private String totalIron;
	
	private String referenceLabId;
	
	public QisTransactionLabCHElecRequest() {
		super();
	}

	public String getSodium() {
		return sodium;
	}

	public void setSodium(String sodium) {
		this.sodium = sodium;
	}

	public String getPotassium() {
		return potassium;
	}

	public void setPotassium(String potassium) {
		this.potassium = potassium;
	}

	public String getChloride() {
		return chloride;
	}

	public void setChloride(String chloride) {
		this.chloride = chloride;
	}

	public String getInorganicPhosphorus() {
		return inorganicPhosphorus;
	}

	public void setInorganicPhosphorus(String inorganicPhosphorus) {
		this.inorganicPhosphorus = inorganicPhosphorus;
	}

	public String getTotalCalcium() {
		return totalCalcium;
	}

	public void setTotalCalcium(String totalCalcium) {
		this.totalCalcium = totalCalcium;
	}

	public String getIonizedCalcium() {
		return ionizedCalcium;
	}

	public void setIonizedCalcium(String ionizedCalcium) {
		this.ionizedCalcium = ionizedCalcium;
	}

	public String getMagnesium() {
		return magnesium;
	}

	public void setMagnesium(String magnesium) {
		this.magnesium = magnesium;
	}

	public String getReferenceLabId() {
		return referenceLabId;
	}

	public void setReferenceLabId(String referenceLabId) {
		this.referenceLabId = referenceLabId;
	}

	public String getTotalIron() {
		return totalIron;
	}

	public void setTotalIron(String totalIron) {
		this.totalIron = totalIron;
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
