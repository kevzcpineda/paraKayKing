package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm.QisTransactionLabCMAFBRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm.QisTransactionLabCMFecaRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm.QisTransactionLabCMPTOBTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm.QisTransactionLabCMUChemRequest;

public class QisTransactionLabCMRequest implements Serializable {

	private static final long serialVersionUID = -3783765466381968584L;

	@Valid
	private QisTransactionLabCMUChemRequest urineChemical;

	@Valid
	private QisTransactionLabCMFecaRequest fecalysis;

	@Valid
	private QisTransactionLabCMPTOBTRequest ptobt;

	@Valid
	private QisTransactionLabCMAFBRequest afb;
	
	@Size(max = 250, message = "Other Notes should not exceed {max} characters.")
	private String otherNotes; 

	@NotNull(message = "Pathologist Id is required.")
	@NotEmpty(message = "Pathologist Id must not be empty.")
	@Size(max = 20, message = "Pathologist Id should not exceed {max} characters.")
	private String pathologistId;
	
	public QisTransactionLabCMRequest() {
		super();
	}

	public QisTransactionLabCMUChemRequest getUrineChemical() {
		return urineChemical;
	}

	public void setUrineChemical(QisTransactionLabCMUChemRequest urineChemical) {
		this.urineChemical = urineChemical;
	}

	public QisTransactionLabCMFecaRequest getFecalysis() {
		return fecalysis;
	}

	public void setFecalysis(QisTransactionLabCMFecaRequest fecalysis) {
		this.fecalysis = fecalysis;
	}

	public QisTransactionLabCMPTOBTRequest getPtobt() {
		return ptobt;
	}

	public void setPtobt(QisTransactionLabCMPTOBTRequest ptobt) {
		this.ptobt = ptobt;
	}

	public QisTransactionLabCMAFBRequest getAfb() {
		return afb;
	}

	public void setAfb(QisTransactionLabCMAFBRequest afb) {
		this.afb = afb;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}

	public String getPathologistId() {
		return pathologistId;
	}

	public void setPathologistId(String pathologistId) {
		this.pathologistId = pathologistId;
	}
	
}
