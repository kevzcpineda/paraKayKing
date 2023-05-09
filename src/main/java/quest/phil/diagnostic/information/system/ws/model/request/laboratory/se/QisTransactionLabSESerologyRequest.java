package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabSESerologyRequest implements Serializable {

	private static final long serialVersionUID = 1814165538541631125L;

	private Boolean hbsAg;
	private Boolean antiHav;
	private Boolean vdrlRpr;
	private Boolean antiHbs;
	private Boolean hbeAg;
	private Boolean antiHbe;
	private String abs;
	private String cutOffValue;
	private Boolean antiHbc;
	private Boolean tppa;
	private Boolean pregnancyTest;
	private String referenceLabId;
	private Boolean antihcv;

	public QisTransactionLabSESerologyRequest() {
		super();
	}

	public Boolean getHbsAg() {
		return hbsAg;
	}

	public void setHbsAg(Boolean hbsAg) {
		this.hbsAg = hbsAg;
	}

	public Boolean getAntiHav() {
		return antiHav;
	}

	public void setAntiHav(Boolean antiHav) {
		this.antiHav = antiHav;
	}

	public Boolean getVdrlRpr() {
		return vdrlRpr;
	}

	public void setVdrlRpr(Boolean vdrlRpr) {
		this.vdrlRpr = vdrlRpr;
	}

	public Boolean getAntiHbs() {
		return antiHbs;
	}

	public void setAntiHbs(Boolean antiHbs) {
		this.antiHbs = antiHbs;
	}

	public Boolean getHbeAg() {
		return hbeAg;
	}

	public void setHbeAg(Boolean hbeAg) {
		this.hbeAg = hbeAg;
	}

	public Boolean getAntiHbe() {
		return antiHbe;
	}

	public String getAbs() {
		return abs;
	}

	public void setAbs(String abs) {
		this.abs = abs;
	}

	public String getCutOffValue() {
		return cutOffValue;
	}

	public void setCutOffValue(String cutOffValue) {
		this.cutOffValue = cutOffValue;
	}

	public void setAntiHbe(Boolean antiHbe) {
		this.antiHbe = antiHbe;
	}

	public Boolean getAntiHbc() {
		return antiHbc;
	}

	public void setAntiHbc(Boolean antiHbc) {
		this.antiHbc = antiHbc;
	}

	public Boolean getTppa() {
		return tppa;
	}

	public void setTppa(Boolean tppa) {
		this.tppa = tppa;
	}

	public Boolean getAntihcv() {
		return antihcv;
	}

	public void setAntihcv(Boolean antihcv) {
		this.antihcv = antihcv;
	}

	public Boolean getPregnancyTest() {
		return pregnancyTest;
	}

	public void setPregnancyTest(Boolean pregnancyTest) {
		this.pregnancyTest = pregnancyTest;
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
