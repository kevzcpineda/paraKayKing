package quest.phil.diagnostic.information.system.ws.model.request.laboratory.se;

import java.io.Serializable;

public class QisTransactionLabSEDegueRequest implements Serializable{

	private static final long serialVersionUID = -9101834635895325939L;
	
	private Boolean result;
	private String referenceLabId;
	
	public QisTransactionLabSEDegueRequest() {
		super();
	}

	public Boolean getResult() {
		return result;
	}

	public String getReferenceLabId() {
		return referenceLabId;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public void setReferenceLabId(String referenceLabId) {
		this.referenceLabId = referenceLabId;
	}
	
	
}
