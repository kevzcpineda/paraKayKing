package quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisTransactionLabCMUChemRequest implements Serializable {

	private static final long serialVersionUID = -955616688688503739L;

	@Size(max = 4, message = "Color should not exceed {max} characters.")
	private String color;

	@Size(max = 4, message = "Transparency should not exceed {max} characters.")
	private String transparency;

	@Size(max = 8, message = "RBC should not exceed {max} characters.")
	private String RBC;

	@Size(max = 8, message = "WBC should not exceed {max} characters.")
	private String WBC;
	
	@Size(max = 4, message = "E.Cells should not exceed {max} characters.")
	private String eCells;
	
	@Size(max = 4, message = "M.Threads should not exceed {max} characters.")
	private String mThreads;

	@Size(max = 4, message = "Bacteria should not exceed {max} characters.")
	private String bacteria;

	@Size(max = 4, message = "Amorphous should not exceed {max} characters.")
	private String amorphous;

	@Size(max = 4, message = "CaOX should not exceed {max} characters.")
	private String caOX;
	
	@Digits(integer = 10, fraction = 4)
	private String ph;

	@Digits(integer = 10, fraction = 4)
	private String spGravity;

	@Size(max = 4, message = "Protien should not exceed {max} characters.")
	private String protien;

	@Size(max = 4, message = "Glucose should not exceed {max} characters.")
	private String glucose;

	@Size(max = 4, message = "Leukocyte Esterase should not exceed {max} characters.")
	private String leukocyteEsterase;

	private Boolean nitrite;

	@Size(max = 4, message = "Urobilinogen should not exceed {max} characters.")
	private String urobilinogen;

	@Size(max = 4, message = "Blood should not exceed {max} characters.")
	private String blood;

	@Size(max = 4, message = "Ketone should not exceed {max} characters.")
	private String ketone;

	@Size(max = 4, message = "Bilirubin should not exceed {max} characters.")
	private String bilirubin;

	@Size(max = 200, message = "Other Notes should not exceed {max} characters.")
	private String otherNotes;
	
	private String referenceLabId;

	public QisTransactionLabCMUChemRequest() {
		super();
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTransparency() {
		return transparency;
	}

	public void setTransparency(String transparency) {
		this.transparency = transparency;
	}

	public String getRBC() {
		return RBC;
	}

	public void setRBC(String rBC) {
		RBC = rBC;
	}

	public String getWBC() {
		return WBC;
	}

	public void setWBC(String wBC) {
		WBC = wBC;
	}

	public String geteCells() {
		return eCells;
	}

	public void seteCells(String eCells) {
		this.eCells = eCells;
	}

	public String getmThreads() {
		return mThreads;
	}

	public void setmThreads(String mThreads) {
		this.mThreads = mThreads;
	}

	public String getBacteria() {
		return bacteria;
	}

	public void setBacteria(String bacteria) {
		this.bacteria = bacteria;
	}

	public String getAmorphous() {
		return amorphous;
	}

	public void setAmorphous(String amorphous) {
		this.amorphous = amorphous;
	}

	public String getCaOX() {
		return caOX;
	}

	public void setCaOX(String caOX) {
		this.caOX = caOX;
	}

	public String getPh() {
		return ph;
	}

	public void setPh(String ph) {
		this.ph = ph;
	}

	public String getSpGravity() {
		return spGravity;
	}

	public void setSpGravity(String spGravity) {
		this.spGravity = spGravity;
	}

	public String getProtien() {
		return protien;
	}

	public void setProtien(String protien) {
		this.protien = protien;
	}

	public String getGlucose() {
		return glucose;
	}

	public void setGlucose(String glucose) {
		this.glucose = glucose;
	}

	public String getLeukocyteEsterase() {
		return leukocyteEsterase;
	}

	public void setLeukocyteEsterase(String leukocyteEsterase) {
		this.leukocyteEsterase = leukocyteEsterase;
	}

	public Boolean getNitrite() {
		return nitrite;
	}

	public void setNitrite(Boolean nitrite) {
		this.nitrite = nitrite;
	}

	public String getUrobilinogen() {
		return urobilinogen;
	}

	public void setUrobilinogen(String urobilinogen) {
		this.urobilinogen = urobilinogen;
	}

	public String getBlood() {
		return blood;
	}

	public void setBlood(String blood) {
		this.blood = blood;
	}

	public String getKetone() {
		return ketone;
	}

	public void setKetone(String ketone) {
		this.ketone = ketone;
	}

	public String getBilirubin() {
		return bilirubin;
	}

	public void setBilirubin(String bilirubin) {
		this.bilirubin = bilirubin;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
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
