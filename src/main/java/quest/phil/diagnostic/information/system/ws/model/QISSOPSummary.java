package quest.phil.diagnostic.information.system.ws.model;

import java.io.Serializable;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QISSOPSummary implements Serializable, Comparable<QISSOPSummary> {
	
	private static final long serialVersionUID = 3517629052303179592L;
	private Calendar date;
	private String transaction;
	private String otherNotes;
	private double amount;
	private double otherAmount;
	private int type;
	private int taxWithHeld;

	
	public Calendar getDate() {
		return date;
	}


	public String getTransaction() {
		return transaction;
	}


	public String getOtherNotes() {
		return otherNotes;
	}


	public double getAmount() {
		return amount;
	}


	public double getOtherAmount() {
		return otherAmount;
	}


	public int getType() {
		return type;
	}


	public int getTaxWithHeld() {
		return taxWithHeld;
	}


	public void setDate(Calendar date) {
		this.date = date;
	}


	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}


	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public void setOtherAmount(double otherAmount) {
		this.otherAmount = otherAmount;
	}


	public void setType(int type) {
		this.type = type;
	}


	public void setTaxWithHeld(int taxWithHeld) {
		this.taxWithHeld = taxWithHeld;
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


	@Override
	public int compareTo(QISSOPSummary o) {
		if (this.getDate().compareTo(o.getDate()) == 0) {
			return this.getType() - o.getType();
		} else {
			return this.getDate().compareTo(o.getDate());
		}
	}

}
