package quest.phil.diagnostic.information.system.ws.model.eod;

import java.util.Calendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditorEODData extends EODData {
	private static final long serialVersionUID = 1171421010869227859L;

	public AuditorEODData(Calendar dateFrom, Calendar dateTo, String auditor) {
		super(dateFrom, dateTo, auditor);
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
