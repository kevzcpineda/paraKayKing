package quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCHEnzyRequest implements Serializable {

	private static final long serialVersionUID = 1541091412920226256L;

	@Digits(integer = 10, fraction = 4)
	private String sgptAlt;

	@Digits(integer = 10, fraction = 4)
	private String sgotAst;

	@Digits(integer = 10, fraction = 4)
	private String amylase;

	@Digits(integer = 10, fraction = 4)
	private String lipase;

	@Digits(integer = 10, fraction = 4)
	private String alp;

	@Digits(integer = 10, fraction = 4)
	private String ggtp;

	@Digits(integer = 10, fraction = 4)
	private String ldh;
	
	private String referenceLabId;

	public QisTransactionLabCHEnzyRequest() {
		super();
	}

	public String getSgptAlt() {
		return sgptAlt;
	}

	public void setSgptAlt(String sgptAlt) {
		this.sgptAlt = sgptAlt;
	}

	public String getSgotAst() {
		return sgotAst;
	}

	public void setSgotAst(String sgotAst) {
		this.sgotAst = sgotAst;
	}

	public String getAmylase() {
		return amylase;
	}

	public void setAmylase(String amylase) {
		this.amylase = amylase;
	}

	public String getLipase() {
		return lipase;
	}

	public void setLipase(String lipase) {
		this.lipase = lipase;
	}

	public String getAlp() {
		return alp;
	}

	public void setAlp(String alp) {
		this.alp = alp;
	}

	public String getGgtp() {
		return ggtp;
	}

	public void setGgtp(String ggtp) {
		this.ggtp = ggtp;
	}

	public String getLdh() {
		return ldh;
	}

	public void setLdh(String ldh) {
		this.ldh = ldh;
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
