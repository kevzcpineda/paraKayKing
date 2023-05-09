package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.xmlbeans.impl.xb.ltgfmt.TestCase.Files;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionPhysicalExamination;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabMedicalHistory;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabPhysicalExam;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabVitalSign;

public class PhysicalExamCertificate {
	private String applicationName;
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;

	public PhysicalExamCertificate(String applicationName, String applicationHeader, String applicationFooter,
			AppUtility appUtility) {
		super();
		this.applicationName = applicationName;
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getTransactionLaboratoryPhysicalExamCertificate(Document document, QisTransaction qisTransaction,
			QisTransactionPhysicalExamination qisPhyExam, boolean withHeaderFooter)
			throws DocumentException, IOException {
		document.open();

		// HEADER IMAGE
		Image imgHeader = Image.getInstance("src/main/resources/images/" + applicationHeader);
		if (!withHeaderFooter) {
			imgHeader = null;
		}

		PdfPTable tableHeader = new PdfPTable(1);
		tableHeader.setWidthPercentage(95f);
		tableHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell cellHeader = new PdfPCell();
		cellHeader.setBorder(Rectangle.NO_BORDER);
		cellHeader.addElement(imgHeader);
		cellHeader.setFixedHeight(80);
		cellHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tableHeader.addCell(cellHeader);
		document.add(tableHeader);

		Certificate certificate = new Certificate(appUtility);

		formatPhysicalExamination(document, qisTransaction, qisPhyExam, withHeaderFooter, certificate);

		Image imgFooter = Image.getInstance("src/main/resources/images/" + applicationFooter);
		if (!withHeaderFooter) {
			imgFooter = null;
		}

		PdfPTable tableFooter = new PdfPTable(1);
		tableFooter.setSpacingBefore(20);
		tableFooter.setWidthPercentage(90f);
		tableFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell cellFooter = new PdfPCell();
		cellFooter.setBorder(Rectangle.NO_BORDER);
		cellFooter.addElement(imgFooter);
		cellFooter.setFixedHeight(50);
		cellFooter.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tableFooter.addCell(cellFooter);
		document.add(tableFooter);

		document.close();
		return document;
	}

	public void formatPhysicalExamination(Document document, QisTransaction qisTransaction,
			QisTransactionPhysicalExamination qisPhyExam, boolean withHeaderFooter, Certificate certificate)
			throws BadElementException, IOException {
		// MEDICAL EXAMINATION REPORT
		PdfPTable tableMedExam = new PdfPTable(new float[] { 1, 3, 1, 2 });
		tableMedExam.setSpacingBefore(1);
		tableMedExam.setWidthPercentage(90f);

		Font fontTitle;
		fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(12);
		fontTitle.setColor(Color.WHITE);

		Font fontLabel = FontFactory.getFont("GARAMOND");
		fontLabel.setSize(11);
		fontLabel.setColor(Color.BLUE);

		Font fontValue = FontFactory.getFont("GARAMOND_BOLD");
		fontValue.setSize(11);
		fontValue.setColor(Color.BLACK);

		Font fontValueRed = FontFactory.getFont("GARAMOND_BOLD");
		fontValueRed.setSize(11);
		fontValueRed.setColor(Color.RED);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setUseAscender(true);
		cell.setUseDescender(true);

		cell.setColspan(4);
		cell.setBackgroundColor(new Color(66, 103, 178));
		certificate.addToTable(tableMedExam, cell, applicationName + " MEDICAL EXAMINATION REPORT", fontTitle,
				Element.ALIGN_CENTER);
		cell.setBackgroundColor(null);

		cell.setColspan(1);
		String company = "";
		if (qisTransaction.getPatient().getCorporate() != null) {
			company = qisTransaction.getPatient().getCorporate().getCompanyName();
		} else {
			company = qisTransaction.getBiller();
		}

		QisPatient patient = qisTransaction.getPatient();
		certificate.addToTable(tableMedExam, cell, "Company:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedExam, cell, company, fontValue, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedExam, cell, "Date:", fontLabel, Element.ALIGN_RIGHT);
		certificate.addToTable(tableMedExam, cell,
				appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(), "MMM-dd-yyyy hh:mm:ss"),
				certificate.getFontValue(), Element.ALIGN_LEFT);

		certificate.addToTable(tableMedExam, cell, "Name:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedExam, cell, appUtility.getPatientFullname(patient), fontValue,
				Element.ALIGN_LEFT);
		certificate.addToTable(tableMedExam, cell, "SR #:", fontLabel, Element.ALIGN_RIGHT);
		certificate.addToTable(tableMedExam, cell, appUtility.numberFormat(qisTransaction.getId(), "000000"), fontValue,
				Element.ALIGN_LEFT);

		String gender = "MALE";
		if ("F".equals(patient.getGender())) {
			gender = "FEMALE";
		}

		certificate.addToTable(tableMedExam, cell, "Address:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedExam, cell, patient.getAddress(), fontValue, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedExam, cell, "Gender:", fontLabel, Element.ALIGN_RIGHT);
		certificate.addToTable(tableMedExam, cell, gender, fontValue, Element.ALIGN_LEFT);

		certificate.addToTable(tableMedExam, cell, "Email:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedExam, cell, patient.getEmail(), fontValue, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedExam, cell, "Age:", fontLabel, Element.ALIGN_RIGHT);
		certificate.addToTable(tableMedExam, cell,
				String.valueOf(
						appUtility.calculateAgeInYear(patient.getDateOfBirth(), qisTransaction.getTransactionDate())),
				fontValue, Element.ALIGN_LEFT);

		certificate.addToTable(tableMedExam, cell, "Mobile:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedExam, cell, patient.getContactNumber(), fontValue, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedExam, cell, "Birthdate:", fontLabel, Element.ALIGN_RIGHT);
		certificate.addToTable(tableMedExam, cell,
				appUtility.calendarToFormatedDate(patient.getDateOfBirth(), "MMM-dd-yyyy"), fontValue,
				Element.ALIGN_LEFT);
		
		
		if (patient.getImage() != null) {
			try {
				Image image = Image.getInstance(patient.getImage());
				image.setAlignment(Element.ALIGN_LEFT);
				image.setAbsolutePosition(515, 629);
				image.scalePercent(13f);
				document.add(image);
			} catch (Exception e) {
				System.out.println("Something went wrong.");
			}
		}
		document.add(tableMedExam);

		// MEDICAL HISTORY VITAL SIGN REPORT
		PdfPTable tableMedHisVitSig = new PdfPTable(new float[] { 4, 6 });
		tableMedHisVitSig.setSpacingBefore(5);
		tableMedHisVitSig.setWidthPercentage(90f);
		tableMedHisVitSig.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableMedHisVitSig.getDefaultCell().setBorder(0);
		tableMedHisVitSig.getDefaultCell().setPadding(1);

		// MEDICAL EXAMINATION REPORT
		PdfPTable tableMedHis = new PdfPTable(new float[] { 6, 2 });
		tableMedHis.setWidthPercentage(100f);
		tableMedHis.setHorizontalAlignment(Element.ALIGN_CENTER);

		QisTransactionLabMedicalHistory medHis = qisPhyExam.getMedicalHistory();
		cell.setColspan(2);
		cell.setBackgroundColor(new Color(66, 103, 178));
		certificate.addToTable(tableMedHis, cell, "MEDICAL HISTORY", fontTitle, Element.ALIGN_CENTER);
		cell.setBackgroundColor(null);
		certificate.addToTable(tableMedHis, cell, "Significant Past Illness", fontLabel, Element.ALIGN_LEFT);

		cell.setColspan(1);
		certificate.addToTable(tableMedHis, cell, "  Asthma/Cold:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell, medHis == null ? "-" : appUtility.getYesNo(medHis.getAsthma()),
				medHis != null && medHis.getAsthma() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Tuberculosis/Cough:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell, medHis == null ? "-" : appUtility.getYesNo(medHis.getTuberculosis()),
				medHis != null && medHis.getTuberculosis() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Diabetes Mellitus:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell,
				medHis == null ? "-" : appUtility.getYesNo(medHis.getDiabetesMellitus()),
				medHis != null && medHis.getDiabetesMellitus() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  High Blood Pressure:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell,
				medHis == null ? "-" : appUtility.getYesNo(medHis.getHighBloodPressure()),
				medHis != null && medHis.getHighBloodPressure() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Heart Problem:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell, medHis == null ? "-" : appUtility.getYesNo(medHis.getHeartProblem()),
				medHis != null && medHis.getHeartProblem() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Kidney Problem:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell, medHis == null ? "-" : appUtility.getYesNo(medHis.getKidneyProblem()),
				medHis != null && medHis.getKidneyProblem() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Abdominal/Hernia/LBM:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell,
				medHis == null ? "-" : appUtility.getYesNo(medHis.getAbdominalHernia()),
				medHis != null && medHis.getAbdominalHernia() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Joint/Back/Scoliosis:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell,
				medHis == null ? "-" : appUtility.getYesNo(medHis.getJointBackScoliosis()),
				medHis != null && medHis.getJointBackScoliosis() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Psychiatric Problem:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell,
				medHis == null ? "-" : appUtility.getYesNo(medHis.getPsychiatricProblem()),
				medHis != null && medHis.getPsychiatricProblem() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Migraine/Headache/Sore Throat:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell,
				medHis == null ? "-" : appUtility.getYesNo(medHis.getMigraineHeadache()),
				medHis != null && medHis.getMigraineHeadache() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Fainting/Seizures:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell,
				medHis == null ? "-" : appUtility.getYesNo(medHis.getFaintingSeizures()),
				medHis != null && medHis.getFaintingSeizures() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Allergies:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell, medHis == null ? "-" : appUtility.getYesNo(medHis.getAllergies()),
				medHis != null && medHis.getAllergies() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Cancer/Tumor:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell, medHis == null ? "-" : appUtility.getYesNo(medHis.getCancerTumor()),
				medHis != null && medHis.getCancerTumor() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Hepatitis:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell, medHis == null ? "-" : appUtility.getYesNo(medHis.getHepatitis()),
				medHis != null && medHis.getHepatitis() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  STD/PLHIV:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell, medHis == null ? "-" : appUtility.getYesNo(medHis.getStdplhiv()),
				medHis != null && medHis.getStdplhiv() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		certificate.addToTable(tableMedHis, cell, "  Travel History:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableMedHis, cell, medHis == null ? "-" : appUtility.getYesNo(medHis.getTravelhistory()),
				medHis != null && medHis.getTravelhistory() ? fontValueRed : fontValue, Element.ALIGN_CENTER);

		cell.setColspan(2);
		certificate.addToTable(tableMedHis, cell, "", certificate.getFontLabel(), Element.ALIGN_LEFT);

		tableMedHisVitSig.addCell(tableMedHis);

		// VITAL SIGNS
		PdfPTable tableVitSig = new PdfPTable(6);
		tableVitSig.setWidthPercentage(100f);
		tableVitSig.setHorizontalAlignment(Element.ALIGN_CENTER);

		QisTransactionLabVitalSign vitSig = qisPhyExam.getVitalSign();
		cell.setColspan(6);
		cell.setBackgroundColor(new Color(66, 103, 178));
		certificate.addToTable(tableVitSig, cell, "VITAL SIGNS", fontTitle, Element.ALIGN_CENTER);
		cell.setBackgroundColor(null);

		cell.setColspan(1);
		certificate.addToTable(tableVitSig, cell, "Height:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getHeight() + "cm", fontValue,
				Element.ALIGN_LEFT);
		certificate.addToTable(tableVitSig, cell, "Weight:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableVitSig, cell,
				vitSig == null ? "" : appUtility.roundToPlaces(vitSig.getWeight(), 10) + "kg", fontValue,
				Element.ALIGN_LEFT);

		certificate.addToTable(tableVitSig, cell, "BMI:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : String.valueOf(vitSig.getBmi()), fontValue,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell,
				vitSig == null ? "" : appUtility.centimeterToFeetInches(vitSig.getHeight()), fontValue,
				Element.ALIGN_RIGHT);
		certificate.addToTable(tableVitSig, cell,
				vitSig == null ? "" : appUtility.kilogramToPounds(vitSig.getWeight()) + "lb", fontValue,
				Element.ALIGN_RIGHT);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getBmiCategory(),
				vitSig != null && "Normal".equals(vitSig.getBmiCategory()) ? fontValue : fontValueRed,
				Element.ALIGN_CENTER);

		boolean isNormal = true;
		if (vitSig != null && vitSig.getBloodPressure() != null) {
			String[] bp = vitSig.getBloodPressure().split("/");
			if (bp.length == 2) {
				Integer bp1 = appUtility.parseIngerValue(bp[0]);
				if (bp1 != null) {
					if (bp1 > 130) {
						isNormal = false;
					}
				} else {
					isNormal = false;
				}

				Integer bp2 = appUtility.parseIngerValue(bp[1]);
				if (bp2 != null) {
					if (bp2 < 60) {
						isNormal = false;
					}
				} else {
					isNormal = false;
				}
			} else {
				isNormal = false;
			}
		} else {
			isNormal = false;
		}

		cell.setColspan(1);
		certificate.addToTable(tableVitSig, cell, "BP:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getBloodPressure(),
				isNormal ? fontValue : fontValueRed, Element.ALIGN_CENTER);
		certificate.addToTable(tableVitSig, cell, "PR:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : String.valueOf(vitSig.getPulseRate()),
				fontValue, Element.ALIGN_CENTER);
		certificate.addToTable(tableVitSig, cell, "RR:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : String.valueOf(vitSig.getRespiratoryRate()),
				fontValue, Element.ALIGN_CENTER);

		cell.setColspan(6);
		certificate.addToTable(tableVitSig, cell, "Visual Acuity Uncorrected", fontLabel, Element.ALIGN_LEFT);

		cell.setColspan(1);
		certificate.addToTable(tableVitSig, cell, "OD", fontLabel, Element.ALIGN_CENTER);
		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getUncorrectedOD(), fontValue,
				Element.ALIGN_CENTER);
		cell.setColspan(1);
		certificate.addToTable(tableVitSig, cell, "OS", fontLabel, Element.ALIGN_CENTER);
		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getUncorrectedOS(), fontValue,
				Element.ALIGN_CENTER);

		cell.setColspan(6);
		certificate.addToTable(tableVitSig, cell, "Visual Acuity Corrected", fontLabel, Element.ALIGN_LEFT);

		cell.setColspan(1);
		certificate.addToTable(tableVitSig, cell, "OD", fontLabel, Element.ALIGN_CENTER);
		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getCorrectedOD(), fontValue,
				Element.ALIGN_CENTER);
		cell.setColspan(1);
		certificate.addToTable(tableVitSig, cell, "OS", fontLabel, Element.ALIGN_CENTER);
		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getCorrectedOS(), fontValue,
				Element.ALIGN_CENTER);

		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, "  Ishihara Test:", fontLabel, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getIshihara(), fontValue,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, "  Hearing:", fontLabel, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getHearing(), fontValue,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, "  Hospitalization:", fontLabel, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getHospitalization(), fontValue,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, "  Operations:", fontLabel, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getOpearations(), fontValue,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, "  Medications:", fontLabel, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getMedications(), fontValue,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, "  Smoker:", fontLabel, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getSmoker(), fontValue,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, "  Alcoholic:", fontLabel, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getAlcoholic(), fontValue,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, "  Are you Pregnant?", fontLabel, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableVitSig, cell,
				vitSig != null && vitSig.getPregnant() != null && "FEMALE".equals(gender)
						? appUtility.getYesNo(vitSig.getPregnant())
						: "",
				fontValue, Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableVitSig, cell, "  Last Menstrual Period:", fontLabel, Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getLastMenstrual(), fontValue,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(tableVitSig, cell, "  Other Notes/ Temp/\n  Loss of Smell/Taste:", fontLabel,
				Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableVitSig, cell, vitSig == null ? "" : vitSig.getNotes(), fontValue,
				Element.ALIGN_LEFT);

//		cell.setColspan(6);
//		certificate.addToTable(tableVitSig, cell, "", certificate.getFontLabel(), Element.ALIGN_LEFT);

		tableMedHisVitSig.addCell(tableVitSig);

		document.add(tableMedHisVitSig);

		// PHYSICAL EXAMINATION
		PdfPTable tablePhyExam = new PdfPTable(2);
		tablePhyExam.setSpacingBefore(5);
		tablePhyExam.setWidthPercentage(90f);
		tablePhyExam.setHorizontalAlignment(Element.ALIGN_CENTER);
		tablePhyExam.getDefaultCell().setBorder(0);
		tablePhyExam.getDefaultCell().setPadding(0);

		QisTransactionLabPhysicalExam phyExam = qisPhyExam.getPhysicalExam();
		cell.setColspan(2);
		cell.setBackgroundColor(new Color(66, 103, 178));
		certificate.addToTable(tablePhyExam, cell, "PHYSICAL EXAMINATION", fontTitle, Element.ALIGN_CENTER);
		cell.setBackgroundColor(null);

		// PHYSICAL EXAMINATION LEFT
		PdfPTable tablePhyExamLeft = new PdfPTable(new float[] { 6, 3 });
		tablePhyExamLeft.setWidthPercentage(100f);
		tablePhyExamLeft.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell.setColspan(1);
		certificate.addToTable(tablePhyExamLeft, cell, null, certificate.getFontLabel(), Element.ALIGN_LEFT);
		certificate.addToTable(tablePhyExamLeft, cell, "NORMAL", fontLabel, Element.ALIGN_LEFT);

		certificate.addToTable(tablePhyExamLeft, cell, null, certificate.getFontLabel(), Element.ALIGN_LEFT);
		certificate.addToTable(tablePhyExamLeft, cell, "YES/NO", fontLabel, Element.ALIGN_LEFT);

		certificate.addToTable(tablePhyExamLeft, cell, " Abdomen:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tablePhyExamLeft, cell, phyExam == null ? "" : appUtility.getYesNo(phyExam.getAbdomen()),
				phyExam != null && !phyExam.getAbdomen() ? fontValueRed : fontValue, Element.ALIGN_LEFT);

		certificate.addToTable(tablePhyExamLeft, cell, " Head and Neck:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tablePhyExamLeft, cell,
				phyExam == null ? "" : appUtility.getYesNo(phyExam.getHeadNeck()),
				phyExam != null && !phyExam.getHeadNeck() ? fontValueRed : fontValue, Element.ALIGN_LEFT);

		certificate.addToTable(tablePhyExamLeft, cell, " Chest/Breast/Lungs:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tablePhyExamLeft, cell,
				phyExam == null ? "" : appUtility.getYesNo(phyExam.getChestBreastLungs()),
				phyExam != null && !phyExam.getChestBreastLungs() ? fontValueRed : fontValue, Element.ALIGN_LEFT);

		certificate.addToTable(tablePhyExamLeft, cell, " Cardiac/Heart:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tablePhyExamLeft, cell,
				phyExam == null ? "" : appUtility.getYesNo(phyExam.getCardiacHeart()),
				phyExam != null && !phyExam.getCardiacHeart() ? fontValueRed : fontValue, Element.ALIGN_LEFT);

		certificate.addToTable(tablePhyExamLeft, cell, " Skin/Covid Rashes:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tablePhyExamLeft, cell, phyExam == null ? "" : appUtility.getYesNo(phyExam.getSkin()),
				phyExam != null && !phyExam.getSkin() ? fontValueRed : fontValue, Element.ALIGN_LEFT);

		certificate.addToTable(tablePhyExamLeft, cell, " Extremities/Disc of finger:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tablePhyExamLeft, cell,
				phyExam == null ? "" : appUtility.getYesNo(phyExam.getExtremities()),
				phyExam != null && !phyExam.getExtremities() ? fontValueRed : fontValue, Element.ALIGN_LEFT);

		certificate.addToTable(tablePhyExamLeft, cell, " Fatigue/Aches/Pain:", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tablePhyExamLeft, cell,
				phyExam == null ? "" : appUtility.getYesNo(phyExam.getFatigueachespains()),
				phyExam != null && !phyExam.getFatigueachespains() ? fontValueRed : fontValue, Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(tablePhyExamLeft, cell, "", fontLabel, Element.ALIGN_LEFT);
		certificate.addToTable(tablePhyExamLeft, cell,
				"(Scalp, Eyes, Ears, Nose, Sinuses, Mouth, Throat, Thyroid, Axilla, Back, Anusrectum, G-U System, Inguinals, Genitals, others)",
				certificate.getFontDocLabel(), Element.ALIGN_LEFT);

		tablePhyExam.addCell(tablePhyExamLeft);

		// PHYSICAL EXAMINATION RIGHT
		PdfPTable tablePhyExamRight = new PdfPTable(8);
		tablePhyExamRight.setWidthPercentage(100f);
		tablePhyExamRight.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell.setColspan(8);
		certificate.addToTable(tablePhyExamRight, cell, null, certificate.getFontLabel(), Element.ALIGN_LEFT);
		certificate.addToTable(tablePhyExamRight, cell, "FINDINGS", fontLabel, Element.ALIGN_LEFT);

		cell.setRowspan(3);
		cell.setColspan(1);
		certificate.addToTable(tablePhyExamRight, cell, "", certificate.getFontLabel(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		cell.setFixedHeight(45);
		certificate.addToTable(tablePhyExamRight, cell, phyExam == null ? "" : phyExam.getFindings(), fontValue,
				Element.ALIGN_LEFT);

		cell.setFixedHeight(0);
		cell.setColspan(1);
		certificate.addToTable(tablePhyExamRight, cell, "", certificate.getFontLabel(), Element.ALIGN_LEFT);

		cell.setRowspan(1);
		cell.setColspan(8);
		certificate.addToTable(tablePhyExamRight, cell, "NOTES", fontLabel, Element.ALIGN_LEFT);

		cell.setRowspan(3);
		cell.setColspan(1);
		certificate.addToTable(tablePhyExamRight, cell, "", certificate.getFontLabel(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		cell.setFixedHeight(45);
		certificate.addToTable(tablePhyExamRight, cell, phyExam == null ? "" : phyExam.getNotes(), fontValue,
				Element.ALIGN_LEFT);

		cell.setFixedHeight(0);
		cell.setColspan(1);
		certificate.addToTable(tablePhyExamRight, cell, "", certificate.getFontLabel(), Element.ALIGN_LEFT);

		cell.setFixedHeight(13);
		cell.setRowspan(1);
		cell.setColspan(2);
		certificate.addToTable(tablePhyExamRight, cell, "Physician:", fontLabel, Element.ALIGN_LEFT);
		cell.setColspan(6);
		certificate.addToTable(tablePhyExamRight, cell,
				phyExam == null ? "" : appUtility.getDoctorsDisplayName(phyExam.getDoctor()), fontValue,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(tablePhyExamRight, cell, "License:", fontLabel, Element.ALIGN_LEFT);
		cell.setColspan(6);
		certificate.addToTable(tablePhyExamRight, cell, phyExam == null ? "" : phyExam.getLicense(), fontValue,
				Element.ALIGN_LEFT);

		cell.setFixedHeight(0);
		tablePhyExam.addCell(tablePhyExamRight);

		cell.setFixedHeight(30);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		certificate.addToTable(tablePhyExam, cell, "  PATIENT SIGNATURE", certificate.getFontDocValue(),
				Element.ALIGN_LEFT);
		cell.setFixedHeight(0);

		cell.setFixedHeight(40);
		certificate.addToTable(tablePhyExam, cell,
				"   I hereby authorized QPD and its physicians to furnish information that the company may need pertaining to my health status and do hereby release them from any and all legal "
						+ "responsibility be doing so, I certify that the medical history is true of my knowledge and any false statement will disqualify me from my employment benefits and claims.",
				certificate.getFontDocLabel(), Element.ALIGN_LEFT);

		document.add(tablePhyExam);

	}
}
