package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHProtRequest implements Serializable {

	private static final long serialVersionUID = 520965027571303487L;

	@Digits(integer = 10, fraction = 4)
	private String totalProtein;

	@Digits(integer = 10, fraction = 4)
	private String albumin;

	@Digits(integer = 10, fraction = 4)
	private String globulin;

	@Digits(integer = 10, fraction = 4)
	private String AGRatio;
	
	private String referenceLabId;

	public QisTransactionLabCHProtRequest() {
		super();
	}

	public String getTotalProtein() {
		return totalProtein;
	}

	public void setTotalProtein(String totalProtein) {
		this.totalProtein = totalProtein;
	}

	public String getAlbumin() {
		return albumin;
	}

	public void setAlbumin(String albumin) {
		this.albumin = albumin;
	}

	public String getGlobulin() {
		return globulin;
	}

	public void setGlobulin(String globulin) {
		this.globulin = globulin;
	}

	public String getAGRatio() {
		return AGRatio;
	}

	public void setAGRatio(String aGRatio) {
		AGRatio = aGRatio;
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
