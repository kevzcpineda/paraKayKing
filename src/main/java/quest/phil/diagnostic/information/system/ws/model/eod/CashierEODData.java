package quest.phil.diagnostic.information.system.ws.model.eod;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties({ "cash" })
public class CashierEODData extends EODData {
	private static final long serialVersionUID = -1749429679351058991L;

	public CashierEODData(Calendar dateFrom, Calendar dateTo, String cashier) {
		super(dateFrom, dateTo, cashier);
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
