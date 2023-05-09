package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabelRequest implements Serializable {

	private static final long serialVersionUID = 8780705004201083430L;

	private Boolean xray;
	private Boolean specimen;
	private String xrayType;
	private String radTech;
	private String medTech;
	

	public QisTransactionLabelRequest() {
		super();
	}

	public Boolean getXray() {
		return xray;
	}

	public void setXray(Boolean xray) {
		this.xray = xray;
	}

	public Boolean getSpecimen() {
		return specimen;
	}

	public void setSpecimen(Boolean specimen) {
		this.specimen = specimen;
	}

	public String getXrayType() {
		return xrayType;
	}

	public void setXrayType(String xrayType) {
		this.xrayType = xrayType;
	}

	public String getRadTech() {
		return radTech;
	}

	public void setRadTech(String radTech) {
		this.radTech = radTech;
	}

	public String getMedTech() {
		return medTech;
	}

	public void setMedTech(String medTech) {
		this.medTech = medTech;
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
