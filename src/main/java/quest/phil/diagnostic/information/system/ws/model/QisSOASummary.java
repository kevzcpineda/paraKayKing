package quest.phil.diagnostic.information.system.ws.model;

import java.io.Serializable;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisSOASummary implements Serializable, Comparable<QisSOASummary> {

	private static final long serialVersionUID = -812631441535161241L;
	private Calendar date;
	private String transaction;
	private String otherNotes;
	private double amount;
	private double otherAmount;
	private int type;
	private int taxWithHeld;

	public int getTaxWithHeld() {
		return taxWithHeld;
	}

	public void setTaxWithHeld(int taxWithHeld) {
		this.taxWithHeld = taxWithHeld;
	}

	public QisSOASummary() {
		super();
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}

	public double getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(double otherAmount) {
		this.otherAmount = otherAmount;
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
	public int compareTo(QisSOASummary o) {
		if (this.getDate().compareTo(o.getDate()) == 0) {
			return this.getType() - o.getType();
		} else {
			return this.getDate().compareTo(o.getDate());
		}
	}
}
