package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class QisIndustrialRequirementRequest implements Serializable {

	private static final long serialVersionUID = -7572408407115352175L;

	@NotNull(message = "Item Laboratory Id is required.")
	private Long id;
	
	@Valid
	private QisTransactionLabCMRequest clinicalMicroscopy;
	
	@Valid
	private QisTransactionLabHERequest hematology;
	
	@Valid
	private QisTransactionLabCHRequest chemistry;

	@Valid
	private QisTransactionLabSERequest serology;

	@Valid
	private QisTransactionLabToxicologyRequest toxicology;
	
	@Valid
	private QisTransactionLabMBRequest microbiology;
	
	public QisIndustrialRequirementRequest() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public QisTransactionLabCMRequest getClinicalMicroscopy() {
		return clinicalMicroscopy;
	}

	public void setClinicalMicroscopy(QisTransactionLabCMRequest clinicalMicroscopy) {
		this.clinicalMicroscopy = clinicalMicroscopy;
	}

	public QisTransactionLabMBRequest getMicrobiology() {
		return microbiology;
	}

	public void setMicrobiology(QisTransactionLabMBRequest microbiology) {
		this.microbiology = microbiology;
	}

	public QisTransactionLabHERequest getHematology() {
		return hematology;
	}

	public void setHematology(QisTransactionLabHERequest hematology) {
		this.hematology = hematology;
	}

	public QisTransactionLabCHRequest getChemistry() {
		return chemistry;
	}

	public void setChemistry(QisTransactionLabCHRequest chemistry) {
		this.chemistry = chemistry;
	}

	public QisTransactionLabSERequest getSerology() {
		return serology;
	}

	public void setSerology(QisTransactionLabSERequest serology) {
		this.serology = serology;
	}

	public QisTransactionLabToxicologyRequest getToxicology() {
		return toxicology;
	}

	public void setToxicology(QisTransactionLabToxicologyRequest toxicology) {
		this.toxicology = toxicology;
	}

}
