package quest.phil.diagnostic.information.system.ws.common.exports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quest.phil.diagnostic.information.system.ws.common.AppExcelUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionPhysicalExamination;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabMedicalHistory;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabPhysicalExam;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabVitalSign;

public class TransactionList {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private AppUtility appUtility;
	private List<QisTransaction> mainList;
	private List<QisTransactionPhysicalExamination> mainListLabReq;

	public TransactionList(List<QisTransaction> mainList, List<QisTransactionPhysicalExamination> labreq,
			String applicationName, AppUtility appUtility, AppExcelUtility appExcelUtility) {

		workbook = new XSSFWorkbook();
		this.mainList = mainList;
		this.appUtility = appUtility;
		this.mainListLabReq = labreq;
	}

	public void export(HttpServletResponse response) throws IOException {

		sheet = workbook.createSheet("TransactionList");
		sheet.setColumnWidth(0, 6 * 256);
		sheet.setColumnWidth(1, 20 * 256);
		sheet.setColumnWidth(2, 20 * 256);
		sheet.setColumnWidth(3, 30 * 256);
		sheet.setColumnWidth(4, 10 * 256);

		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	private void writeDataLines() {

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		font.setBold(true);
		style.setFont(font);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);

		Row rowHeader0 = sheet.createRow(0);

		Cell cellHeader0 = rowHeader0.createCell(0);
		cellHeader0.setCellValue("FirstName");
		cellHeader0.setCellStyle(style);

		Cell cellHeader1 = rowHeader0.createCell(1);
		cellHeader1.setCellValue("MiddleName");
		cellHeader1.setCellStyle(style);

		Cell cellHeader2 = rowHeader0.createCell(2);
		cellHeader2.setCellValue("LastName");
		cellHeader2.setCellStyle(style);

		Cell cellHeader3 = rowHeader0.createCell(3);
		cellHeader3.setCellValue("Address");
		cellHeader3.setCellStyle(style);

		Cell cellHeader4 = rowHeader0.createCell(4);
		cellHeader4.setCellValue("Birthdate (yyyy-mm-dd)");
		cellHeader4.setCellStyle(style);

		Cell cellHeader5 = rowHeader0.createCell(5);
		cellHeader5.setCellValue("Email");
		cellHeader5.setCellStyle(style);

		Cell cellHeader6 = rowHeader0.createCell(6);
		cellHeader6.setCellValue("ContactNo");
		cellHeader6.setCellStyle(style);

		Cell cellHeader7 = rowHeader0.createCell(7);
		cellHeader7.setCellValue("Gender");
		cellHeader7.setCellStyle(style);

		Cell cellHeader8 = rowHeader0.createCell(8);
		cellHeader8.setCellValue("Vital Sign");
		cellHeader8.setCellStyle(style);

		Cell cellHeader9 = rowHeader0.createCell(9);
		cellHeader9.setCellValue("Medical History");
		cellHeader9.setCellStyle(style);

		Cell cellHeader10 = rowHeader0.createCell(10);
		cellHeader10.setCellValue("Physical Exam");
		cellHeader10.setCellStyle(style);

		Cell cellHeader11 = rowHeader0.createCell(11);
		cellHeader11.setCellValue("Age");
		cellHeader11.setCellStyle(style);

		Cell cellHeader12 = rowHeader0.createCell(12);
		cellHeader12.setCellValue("Items");
		cellHeader12.setCellStyle(style);

		Cell cellHeader13 = rowHeader0.createCell(13);
		cellHeader13.setCellValue("Price");
		cellHeader13.setCellStyle(style);

		Cell cellHeader14 = rowHeader0.createCell(14);
		cellHeader14.setCellValue("REFERRAL");
		cellHeader14.setCellStyle(style);
		
		Double Amount = 0d;
		for (int i = 0; i < mainList.size(); i++) {
			QisTransactionLabVitalSign vitSig = null;
			QisTransactionLabMedicalHistory medHis = null;
			QisTransactionLabPhysicalExam phyExam = null;
			@SuppressWarnings("rawtypes")
			List<Comparable> dataVitalsign = new ArrayList<Comparable>();
			if (mainListLabReq.size() != 0) {
				int y = 0;
				for (y = 0; y < mainListLabReq.size(); y++) {
					int id = mainList.get(i).getId().intValue();
					int labId = mainListLabReq.get(y).getTransactionid().intValue();
					if (id == labId) {
						if (mainListLabReq.get(y) != null) {
							if (mainListLabReq.get(y).getVitalSign() != null) {
								vitSig = mainListLabReq.get(y).getVitalSign();
								String alcoholic = ((vitSig.getAlcoholic() == null) ? "" : vitSig.getAlcoholic().replace(",", "_"));
								String blood_pressure = ((vitSig.getBloodPressure() == null) ? ""
										: vitSig.getBloodPressure().replace(",", "_"));
								float bmi = ((vitSig.getBmi() == null) ? 0f : vitSig.getBmi());
								String bmi_category = ((vitSig.getBmiCategory() == null) ? ""
										: vitSig.getBmiCategory().replace(",", "_"));
								String cod = (("".equals(vitSig.getCorrectedOD())) ? "" : vitSig.getCorrectedOD());
								String cos = (("".equals(vitSig.getCorrectedOS())) ? "" : vitSig.getCorrectedOS());
								String hearing = ((vitSig.getHearing() == null) ? "" : vitSig.getHearing().replace(",", "_"));
								Float height = ((vitSig.getHeight() == null) ? null : vitSig.getHeight());
								String hospitalization = ((vitSig.getHospitalization() == null) ? ""
										: vitSig.getHospitalization().replace(",", "_"));
								String ishihara = ((vitSig.getIshihara() == null) ? "" : vitSig.getIshihara().replace(",", "_"));
								String last_menstrual = ((vitSig.getLastMenstrual() == null) ? ""
										: vitSig.getLastMenstrual().replace(",", "_"));
								String medications = ((vitSig.getMedications() == null) ? "" : vitSig.getMedications().replace(",", "_"));
								String notes = ((vitSig.getNotes() == null) ? "" : vitSig.getNotes().replace(",", "_"));
								String opearations = ((vitSig.getOpearations() == null) ? "" : vitSig.getOpearations().replace(",", "_"));
								Boolean pregnant = ((vitSig.getPregnant() == null) ? false : vitSig.getPregnant());
								int pulse_rate = ((vitSig.getPulseRate() == null) ? 0 : vitSig.getPulseRate());
								int respiratory_rate = ((vitSig.getRespiratoryRate() == null) ? 0
										: vitSig.getRespiratoryRate());
								String smoker = ((vitSig.getSmoker() == null) ? "" : vitSig.getSmoker().replace(",", "_"));
								String uod = (("".equals(vitSig.getUncorrectedOD())) ? "" : vitSig.getUncorrectedOD());
								String uos = (("".equals(vitSig.getUncorrectedOS())) ? "" : vitSig.getUncorrectedOS());
								Float weight = ((vitSig.getWeight() == null) ? null : vitSig.getWeight());

								dataVitalsign.add(alcoholic + ":" + blood_pressure + ":" + bmi + ":" + bmi_category
										+ ":" + cod + ":" + cos + ":" + hearing + ":" + height + ":" + hospitalization
										+ ":" + ishihara + ":" + last_menstrual + ":" + medications + ":" + notes + ":"
										+ opearations + ":" + pregnant + ":" + pulse_rate + ":" + respiratory_rate + ":"
										+ smoker + ":" + uod + ":" + uos + ":" + weight);
							}
						}
					}
				}
			}

			@SuppressWarnings("rawtypes")
			List<Comparable> dataMedHistory = new ArrayList<Comparable>();
			if (mainListLabReq.size() != 0) {
				int y = 0;
				for (y = 0; y < mainListLabReq.size(); y++) {
					int id = mainList.get(i).getId().intValue();
					int labId = mainListLabReq.get(y).getTransactionid().intValue();
					if (id == labId) {
						if (mainListLabReq.get(y) != null) {
							if (mainListLabReq.get(y).getMedicalHistory() != null) {
								medHis = mainListLabReq.get(y).getMedicalHistory();

								Boolean abdominal_hernia = ((medHis.getAbdominalHernia() == null) ? false
										: medHis.getAbdominalHernia());
								Boolean allergies = ((medHis.getAllergies() == null) ? false : medHis.getAllergies());
								Boolean asthma = ((medHis.getAsthma() == null) ? false : medHis.getAsthma());
								Boolean cancer_tumor = ((medHis.getCancerTumor() == null) ? false
										: medHis.getCancerTumor());
								Boolean diabetes_mellitus = ((medHis.getDiabetesMellitus() == null) ? false
										: medHis.getDiabetesMellitus());
								Boolean fainting_seizures = ((medHis.getFaintingSeizures() == null) ? false
										: medHis.getFaintingSeizures());
								Boolean heart_problem = ((medHis.getHeartProblem() == null) ? false
										: medHis.getHeartProblem());
								Boolean hepatitis = ((medHis.getHepatitis() == null) ? false : medHis.getHepatitis());
								Boolean high_blood_pressure = ((medHis.getHighBloodPressure() == null) ? false
										: medHis.getHighBloodPressure());
								Boolean joint_back_scoliosis = ((medHis.getJointBackScoliosis() == null) ? false
										: medHis.getJointBackScoliosis());
								Boolean kidney_problem = ((medHis.getKidneyProblem() == null) ? false
										: medHis.getKidneyProblem());
								Boolean migraine_headache = ((medHis.getMigraineHeadache() == null) ? false
										: medHis.getMigraineHeadache());
								Boolean psychiatric_problem = ((medHis.getPsychiatricProblem() == null) ? false
										: medHis.getPsychiatricProblem());
								Boolean std_plhiv = ((medHis.getStdplhiv() == null) ? false : medHis.getStdplhiv());
								Boolean tuberculosis = ((medHis.getTuberculosis() == null) ? false
										: medHis.getTuberculosis());
								
								Boolean travelHistory = ((medHis.getTravelhistory() == null) ? false
										: medHis.getTravelhistory());

								dataMedHistory.add(abdominal_hernia + ":" + allergies + ":" + asthma + ":"
										+ cancer_tumor + ":" + diabetes_mellitus + ":" + fainting_seizures + ":"
										+ heart_problem + ":" + hepatitis + ":" + high_blood_pressure + ":"
										+ joint_back_scoliosis + ":" + kidney_problem + ":" + migraine_headache + ":"
										+ psychiatric_problem + ":" + std_plhiv + ":" + tuberculosis + ":" + travelHistory);
							}
						}
					}
				}
			}

			@SuppressWarnings("rawtypes")
			List<Comparable> dataPhysicalExam = new ArrayList<Comparable>();
			if (mainListLabReq.size() != 0) {
				int y = 0;
				for (y = 0; y < mainListLabReq.size(); y++) {
					int id = mainList.get(i).getId().intValue();
					int labId = mainListLabReq.get(y).getTransactionid().intValue();
					if (id == labId) {
						if (mainListLabReq.get(y) != null) {
							if (mainListLabReq.get(y).getPhysicalExam() != null) {
								phyExam = mainListLabReq.get(y).getPhysicalExam();
								Boolean abdomen = ((phyExam.getAbdomen() == null) ? false : phyExam.getAbdomen());
								Boolean cardiac_heart = ((phyExam.getCardiacHeart() == null) ? false
										: phyExam.getCardiacHeart());
								Boolean chest_breast_lungs = ((phyExam.getChestBreastLungs() == null) ? false
										: phyExam.getChestBreastLungs());
								Boolean extremities = ((phyExam.getExtremities() == null) ? false
										: phyExam.getExtremities());
								Boolean fatigue = ((phyExam.getFatigueachespains() == null) ? false
										: phyExam.getFatigueachespains());
								String findings = ((phyExam.getFindings() == null) ? "" : phyExam.getFindings().replace(",", "_"));
								Boolean head_neck = ((phyExam.getHeadNeck() == null) ? false : phyExam.getHeadNeck());
								String license = ((phyExam.getLicense() == null) ? null : phyExam.getLicense());
								String notes = ((phyExam.getNotes() == null) ? "" : phyExam.getNotes().replace(",", "_"));
								Boolean skin = ((phyExam.getSkin() == null) ? false : phyExam.getSkin());
								Boolean remarks = ((phyExam.getRemarks() == null) ? false : phyExam.getRemarks());

								dataPhysicalExam.add(abdomen + ":" + cardiac_heart + ":" + chest_breast_lungs + ":"
										+ extremities + ":" + findings + ":" + head_neck + ":" + license + ":" + notes
										+ ":" + skin + ":" + remarks + ":" + fatigue);
							}
						}
					}
				}
			}

			Row row = sheet.createRow(1 + i);
			String items = "";
			for (QisTransactionItem value : mainList.get(i).getTransactionItems()) {
				if (value.getQisPackage() != null) {
					items += value.getQisPackage().getPackageDescription() + " | ";
				}
				if (value.getQisItem() != null) {
					items += value.getQisItem().getItemDescription() + " | ";
				}
			}

			Amount = mainList.get(i).getTotalItemAmountDue();
			Cell cell0 = row.createCell(0);
			cell0.setCellValue(mainList.get(i).getPatient().getFirstname().replace(",", "_"));

			Cell cell1 = row.createCell(1);
			if (!"".equals(mainList.get(i).getPatient().getMiddlename())
					|| mainList.get(i).getPatient().getMiddlename() != null) {
				cell1.setCellValue(mainList.get(i).getPatient().getMiddlename());
			} else {
				cell1.setCellValue("");
			}

			Cell cell2 = row.createCell(2);
			cell2.setCellValue(mainList.get(i).getPatient().getLastname().replace(",", "_"));

			Cell cell3 = row.createCell(3);
			cell3.setCellValue(mainList.get(i).getPatient().getAddress().replace(",", "_"));

			Cell cell4 = row.createCell(4);
			cell4.setCellValue(mainList.get(i).getPatient().getBirthDate());

			Cell cell5 = row.createCell(5);
			if (!"".equals(mainList.get(i).getPatient().getEmail())
					|| mainList.get(i).getPatient().getEmail() != null) {
				cell5.setCellValue(mainList.get(i).getPatient().getEmail());
			} else {
				cell5.setCellValue("");
			}

			Cell cell6 = row.createCell(6);
			if (!"".equals(mainList.get(i).getPatient().getContactNumber())
					|| mainList.get(i).getPatient().getContactNumber() != null) {
				cell6.setCellValue(mainList.get(i).getPatient().getContactNumber());
			} else {
				cell6.setCellValue(0);
			}

			Cell cell7 = row.createCell(7);
			cell7.setCellValue(mainList.get(i).getPatient().getGender());

			String vitalSign = dataVitalsign.toString().substring(1, dataVitalsign.toString().length() - 1);
			Cell cell8 = row.createCell(8);
			cell8.setCellValue(vitalSign);

			String MedHistory = dataMedHistory.toString().substring(1, dataMedHistory.toString().length() - 1);
			Cell cell9 = row.createCell(9);
			cell9.setCellValue(MedHistory);

			String PhysicalExam = dataPhysicalExam.toString().substring(1, dataPhysicalExam.toString().length() - 1);
			Cell cell10 = row.createCell(10);
			cell10.setCellValue(PhysicalExam);

			String age = String.valueOf(appUtility.calculateAgeInYear(mainList.get(i).getPatient().getDateOfBirth(),
					mainList.get(i).getTransactionDate()));

			Cell cell11 = row.createCell(11);
			cell11.setCellValue(age);
			if (!"".equals(items)) {
				items = items.substring(0, items.length() - 2);
			}
			Cell cell12 = row.createCell(12);
			cell12.setCellValue(items);

			Cell cell13 = row.createCell(13);
			cell13.setCellValue(Amount);
			
			String referral= "";
			if (mainList.get(i).getReferral() != null) {
				referral = mainList.get(i).getReferral().getReferral();				
			}
			Cell cell14 = row.createCell(14);
			cell14.setCellValue(referral);

		}
	}

}
