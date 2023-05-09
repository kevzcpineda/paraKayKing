package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.persistence.Column;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisEODNotesReferenceRequest implements Serializable {

	private static final long serialVersionUID = 7931183755943777006L;
	
	@Column(name = "dateFrom", nullable = false, length = 120, unique = true)
	private String dateFrom;
	
	@Column(name = "dateTo", nullable = false, length = 120, unique = true)
	private String dateTo;
	
	@Column(name = "referenceNumber", nullable = true, length = 80)
	@Size(max = 80, message = "Reference Number should not exceed {max} characters.")
	private String referenceNumber = null;

	@Column(name = "other_notes", nullable = true, length = 120)
	@Size(max = 120, message = "Other Notes should not exceed {max} characters.")
	private String otherNotes = null;
	
	public QisEODNotesReferenceRequest() {
		super();
	}
	
	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
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
