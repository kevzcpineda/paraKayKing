package quest.phil.diagnostic.information.system.ws.model.eod;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import quest.phil.diagnostic.information.system.ws.model.classes.QisDenominationReportsClass;

public class Summary implements Serializable {

	private static final long serialVersionUID = 5327186458414022853L;
	private int totalSales = 0;
	private double holdAmount = 0;
	private double cashAmount = 0;
	private double accountsAmount = 0;
	private double apeAmount = 0;
	private double hmoAmount = 0;
	private double medicalMission = 0;
	private double bankAmount = 0;
	private double virtualAmount = 0;
	private double refundAmount = 0;
	private double medicalService = 0;

	private double cashInAmount = 0;
	private double cashOutAmount = 0;

	private double totalSalesAmount = 0;
	private double taxAmount = 0;
	private double discountAmount = 0;
	private double netAmount = 0;
	
	private double cashDiscount = 0;

	private String eodOtherNotes;
	private String referenceNumber;
	
	private List<QisDenominationReportsClass> denomination;
	
	
	public Summary() {
		super();
	}

	public int getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(int totalSales) {
		this.totalSales = totalSales;
	}

	public double getHoldAmount() {
		return holdAmount;
	}

	public void setHoldAmount(double holdAmount) {
		this.holdAmount = holdAmount;
	}

	public double getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(double cashAmount) {
		this.cashAmount = cashAmount;
	}

	public double getAccountsAmount() {
		return accountsAmount;
	}

	public void setAccountsAmount(double accountsAmount) {
		this.accountsAmount = accountsAmount;
	}

	public double getApeAmount() {
		return apeAmount;
	}

	public void setApeAmount(double apeAmount) {
		this.apeAmount = apeAmount;
	}

	public double getHmoAmount() {
		return hmoAmount;
	}

	public void setHmoAmount(double hmoAmount) {
		this.hmoAmount = hmoAmount;
	}

	public double getBankAmount() {
		return bankAmount;
	}

	public void setBankAmount(double bankAmount) {
		this.bankAmount = bankAmount;
	}

	public double getVirtualAmount() {
		return virtualAmount;
	}

	public void setVirtualAmount(double virtualAmount) {
		this.virtualAmount = virtualAmount;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public double getCashInAmount() {
		return cashInAmount;
	}

	public void setCashInAmount(double cashInAmount) {
		this.cashInAmount = cashInAmount;
	}

	public double getCashOutAmount() {
		return cashOutAmount;
	}

	public void setCashOutAmount(double cashOutAmount) {
		this.cashOutAmount = cashOutAmount;
	}

	public double getTotalSalesAmount() {
		return totalSalesAmount;
	}

	public void setTotalSalesAmount(double totalSalesAmount) {
		this.totalSalesAmount = totalSalesAmount;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getEodOtherNotes() {
		return eodOtherNotes;
	}

	public void setEodOtherNotes(String eodOtherNotes) {
		this.eodOtherNotes = eodOtherNotes;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public double getCashDiscount() {
		return cashDiscount;
	}

	public void setCashDiscount(double cashDiscount) {
		this.cashDiscount = cashDiscount;
	}

	public double getMedicalMission() {
		return medicalMission;
	}

	public void setMedicalMission(double medicalMission) {
		this.medicalMission = medicalMission;
	}

	public double getMedicalService() {
		return medicalService;
	}

	public void setMedicalService(double medicalService) {
		this.medicalService = medicalService;
	}

	public List<QisDenominationReportsClass> getDenomination() {
		return denomination;
	}

	public void setDenomination(List<QisDenominationReportsClass> denomination) {
		this.denomination = denomination;
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
