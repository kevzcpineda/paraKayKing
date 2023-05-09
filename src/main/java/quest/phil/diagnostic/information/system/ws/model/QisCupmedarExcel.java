package quest.phil.diagnostic.information.system.ws.model;

public class QisCupmedarExcel {
	private String dateAndTime;
	private String RecieptNo;
	private String PatientName;
	private String CompanyName;
	private String Items;
	private String PatientCalled;
	private String ResultsUploaded;
	private String ResultsPrinted;
	private String ResultsEmail;
	private String ResultsEncoded;
	private String ResultsDone;
	private String Acknowledge;
	private String PatientRecieved;
	private String SendOutPending;
	private String FitToWork;
	private String ForPickUp;
	
	
	public QisCupmedarExcel(String dateAndTime, String RecieptNo, String PatientName, String CompanyName,
			String Items, String PatientCalled, String ResulstUploaded, String ResultsPrinted, String ResultsEmail,
			String ResultsEncoded, String ResultsDone, String Acknowledge, String PatientRecieved,
			String SendOutPending, String FitToWork, String ForPickUp) {
		super();
		this.dateAndTime = dateAndTime;
		this.RecieptNo = RecieptNo;
		this.PatientName = PatientName;
		this.CompanyName = CompanyName;
		this.Items = Items;
		this.PatientCalled = PatientCalled;
		this.ResultsUploaded = ResulstUploaded;
		this.ResultsPrinted = ResultsPrinted;
		this.ResultsEmail = ResultsEmail;
		this.ResultsEncoded = ResultsEncoded;
		this.ResultsDone = ResultsDone;
		this.Acknowledge = Acknowledge;
		this.PatientRecieved = PatientRecieved;
		this.SendOutPending = SendOutPending;
		this.FitToWork = FitToWork;
		this.ForPickUp = ForPickUp;
		this.dateAndTime = dateAndTime;
		
		
	}


	public String getDateAndTime() {
		return dateAndTime;
	}


	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}


	public String getRecieptNo() {
		return RecieptNo;
	}


	public void setRecieptNo(String recieptNo) {
		RecieptNo = recieptNo;
	}


	public String getPatientName() {
		return PatientName;
	}


	public void setPatientName(String patientName) {
		PatientName = patientName;
	}


	public String getCompanyName() {
		return CompanyName;
	}


	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}


	public String getItems() {
		return Items;
	}


	public void setItems(String items) {
		Items = items;
	}


	public String getPatientCalled() {
		return PatientCalled;
	}


	public void setPatientCalled(String patientCalled) {
		PatientCalled = patientCalled;
	}


	public String getResultsUploaded() {
		return ResultsUploaded;
	}


	public void setResultsUploaded(String resultsUploaded) {
		ResultsUploaded = resultsUploaded;
	}


	public String getResultsPrinted() {
		return ResultsPrinted;
	}


	public void setResultsPrinted(String resultsPrinted) {
		ResultsPrinted = resultsPrinted;
	}


	public String getResultsEmail() {
		return ResultsEmail;
	}


	public void setResultsEmail(String resultsEmail) {
		ResultsEmail = resultsEmail;
	}


	public String getResultsEncoded() {
		return ResultsEncoded;
	}


	public void setResultsEncoded(String resultsEncoded) {
		ResultsEncoded = resultsEncoded;
	}


	public String getResultsDone() {
		return ResultsDone;
	}


	public void setResultsDone(String resultsDone) {
		ResultsDone = resultsDone;
	}


	public String getAcknowledge() {
		return Acknowledge;
	}


	public void setAcknowledge(String acknowledge) {
		Acknowledge = acknowledge;
	}


	public String getPatientRecieved() {
		return PatientRecieved;
	}


	public void setPatientRecieved(String patientRecieved) {
		PatientRecieved = patientRecieved;
	}


	public String getSendOutPending() {
		return SendOutPending;
	}


	public void setSendOutPending(String sendOutPending) {
		SendOutPending = sendOutPending;
	}


	public String getFitToWork() {
		return FitToWork;
	}


	public void setFitToWork(String fitToWork) {
		FitToWork = fitToWork;
	}


	public String getForPickUp() {
		return ForPickUp;
	}


	public void setForPickUp(String forPickUp) {
		ForPickUp = forPickUp;
	}
        
	
	
}
