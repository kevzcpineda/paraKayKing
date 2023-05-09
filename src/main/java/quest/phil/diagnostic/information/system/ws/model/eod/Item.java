package quest.phil.diagnostic.information.system.ws.model.eod;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Item implements Serializable, Comparable<Item> {

	private static final long serialVersionUID = -665108494574505088L;
	private long transactionItemId;
	private long itemId;
	private String itemName;
	private String laboratoryRequest;
	private double itemPrice;
	private int quantity;
	private String molecular_lab;

	public Item(long transactionItemId, long itemId, String itemName, double itemPrice, int quantity, String laboratoryRequest, String molecular_lab) {
		super();
		this.transactionItemId = transactionItemId;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.quantity = quantity;
		this.laboratoryRequest = laboratoryRequest;
		this.molecular_lab = molecular_lab;
	}

	public long getTransactionItemId() {
		return transactionItemId;
	}

	public void setTransactionItemId(long transactionItemId) {
		this.transactionItemId = transactionItemId;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getLaboratoryRequest() {
		return laboratoryRequest;
	}

	public void setLaboratoryRequest(String laboratoryRequest) {
		this.laboratoryRequest = laboratoryRequest;
	}

	public String getMolecular_lab() {
		return molecular_lab;
	}

	public void setMolecular_lab(String molecular_lab) {
		this.molecular_lab = molecular_lab;
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
	public int compareTo(Item o) {
		return this.getTransactionItemId() > o.getTransactionItemId() ? 1 : -1;
	}

}
