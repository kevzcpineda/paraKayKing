package quest.phil.diagnostic.information.system.ws.model.eod;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EODData implements Serializable {

	private static final long serialVersionUID = 4662794308783548265L;
	private Calendar requestDate;
	private Calendar dateFrom;
	private Calendar dateTo;
	private String personel;
	private Summary summary = new Summary();
	private Set<Account> hold = new HashSet<>();
	private Set<Account> cash = new HashSet<>();
	private Set<Account> accounts = new HashSet<>();
	private Set<Account> ape = new HashSet<>();
	private Set<Account> hmo = new HashSet<>();
	private Set<Account> medicalMission = new HashSet<>();
	private Set<Account> bank = new HashSet<>();
	private Set<Account> virtual = new HashSet<>();
	private Set<Account> refund = new HashSet<>();
	private Map<String, Double> billerAccount = new HashMap<>();
	private Map<String, Double> branchSales = new HashMap<>();
	private Double medicalServiceAmount = 0d;
	private String eodOtherNotes;
	private String referenceNumber;
	
	
	public EODData(Calendar dateFrom, Calendar dateTo, String personel) {
		super();
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.personel = personel;
		this.requestDate = Calendar.getInstance();
	}

	public Calendar getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Calendar requestDate) {
		this.requestDate = requestDate;
	}

	public Calendar getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Calendar dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Calendar getDateTo() {
		return dateTo;
	}

	public void setDateTo(Calendar dateTo) {
		this.dateTo = dateTo;
	}

	public String getPersonel() {
		return personel;
	}

	public void setPersonel(String personel) {
		this.personel = personel;
	}

	public Summary getSummary() {
		return summary;
	}

	public void setSummary(Summary summary) {
		this.summary = summary;
	}

	public Set<Account> getHold() {
		return hold;
	}

	public void setHold(Set<Account> hold) {
		this.hold = hold;
	}

	public Set<Account> getCash() {
		return cash;
	}

	public void setCash(Set<Account> cash) {
		this.cash = cash;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public Set<Account> getApe() {
		return ape;
	}

	public void setApe(Set<Account> ape) {
		this.ape = ape;
	}

	public Set<Account> getHmo() {
		return hmo;
	}

	public void setHmo(Set<Account> hmo) {
		this.hmo = hmo;
	}

	public Set<Account> getBank() {
		return bank;
	}

	public void setBank(Set<Account> bank) {
		this.bank = bank;
	}

	public Set<Account> getVirtual() {
		return virtual;
	}

	public void setVirtual(Set<Account> virtual) {
		this.virtual = virtual;
	}

	public Set<Account> getRefund() {
		return refund;
	}

	public void setRefund(Set<Account> refund) {
		this.refund = refund;
	}

	public Map<String, Double> getBillerAccount() {
		return billerAccount;
	}

	public void setBillerAccount(Map<String, Double> billerAccount) {
		this.billerAccount = billerAccount;
	}

	public Map<String, Double> getBranchSales() {
		return branchSales;
	}

	public void setBranchSales(Map<String, Double> branchSales) {
		this.branchSales = branchSales;
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

	public Set<Account> getMedicalMission() {
		return medicalMission;
	}

	public void setMedicalMission(Set<Account> medicalMission) {
		this.medicalMission = medicalMission;
	}

	public Double getMedicalServiceAmount() {
		return medicalServiceAmount;
	}

	public void setMedicalServiceAmount(Double medicalServiceAmount) {
		this.medicalServiceAmount = medicalServiceAmount;
	}
	
	

}
