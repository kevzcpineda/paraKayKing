package quest.phil.diagnostic.information.system.ws.model.eod;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Account implements Serializable, Comparable<Account> {

	private static final long serialVersionUID = 772717018425622795L;
	private long transactionId;
	private String transactionType;
	private String recordType;
	private double amount;
	private double tax;
	private double discount;
	private double net;
	private double cashAmount;
	private double changeAmount;
	private double discountRate;
	private String patient;
	private String biller;
	private Calendar date;
	private String branch;
	private String referral;
	private String paymentType;
	private String paymentMode;
	private String cashier;

	private Set<Item> items;

	public Account(long transactionId, String transactionType, String paymentType, String paymentMode, double amount,
			double tax, double discount, double net, double cashAmount, double changeAmount, double discountRate,
			String patient, String biller, Calendar date, String branch, String referral, String cashier) {
		super();
		this.transactionId = transactionId;
		this.transactionType = transactionType;
		this.paymentType = paymentType;
		this.paymentMode = paymentMode;
		this.amount = amount;
		this.tax = tax;
		this.discount = discount;
		this.net = net;
		this.cashAmount = cashAmount;
		this.changeAmount = changeAmount;
		this.discountRate = discountRate;
		this.patient = patient;
		this.biller = biller;
		this.date = date;
		this.branch = branch;
		this.referral = referral;
		this.cashier = cashier;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getNet() {
		return net;
	}

	public void setNet(double net) {
		this.net = net;
	}

	public double getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(double cashAmount) {
		this.cashAmount = cashAmount;
	}

	public double getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(double changeAmount) {
		this.changeAmount = changeAmount;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getBiller() {
		return biller;
	}

	public void setBiller(String biller) {
		this.biller = biller;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getReferral() {
		return referral;
	}

	public void setReferral(String referral) {
		this.referral = referral;
	}

	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
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
	public int compareTo(Account o) {
		return this.getDate().compareTo(o.getDate());
	}

}
