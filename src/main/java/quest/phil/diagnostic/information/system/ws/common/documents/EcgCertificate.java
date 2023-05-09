package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ecg.QisTransactionLabEcg;


public class EcgCertificate {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private Certificate certificate;
	
	public EcgCertificate(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
		this.certificate = new Certificate(appUtility);
	}
	
	
	public Document getTransactionLaboratoryEcgCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionLabEcg qisEcg, boolean withHeaderFooter)
			throws DocumentException, IOException {
		
		Certificate certificate = new Certificate(appUtility);
		QisHeaderFooterImageEvent event = new QisHeaderFooterImageEvent(applicationHeader, applicationFooter,
				certificate, withHeaderFooter);
		pdfWriter.setPageEvent(event);
		document.open();
		Calendar calndr = Calendar.getInstance();
		document.add(certificatePatientInformation(qisTransaction, calndr, document));

		formatEcg(document, qisTransaction, qisEcg, withHeaderFooter, certificate);
		document.close();
		return document;
	}


	public PdfPTable certificatePatientInformation(QisTransaction qisTransaction, Calendar verifiedDate,
			Document document) {
		PdfPTable tablePatient = new PdfPTable(new float[] { 1, 2, 1, 2, 1, 2 });
		tablePatient.setSpacingBefore(0);
		tablePatient.setWidthPercentage(90f);
		tablePatient.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);

		QisPatient patient = qisTransaction.getPatient();
		certificate.addToTable(tablePatient, cell, "Name:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tablePatient, cell, appUtility.getPatientFullname(patient), certificate.getFontValue(),
				Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(tablePatient, cell, "SR#:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		certificate.addToTable(tablePatient, cell, appUtility.numberFormat(qisTransaction.getId(), "000000"),
				certificate.getFontValue(), Element.ALIGN_LEFT);

		String gender = "MALE";
		if ("F".equals(patient.getGender())) {
			gender = "FEMALE";
		}
		String age = String
				.valueOf(appUtility.calculateAgeInYear(patient.getDateOfBirth(), qisTransaction.getTransactionDate()));

		certificate.addToTable(tablePatient, cell, "Age/Gender:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tablePatient, cell, age + "/" + gender, certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(tablePatient, cell, "QuestID:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		certificate.addToTable(tablePatient, cell, appUtility.numberFormat(patient.getId(), "0000"),
				certificate.getFontValue(), Element.ALIGN_LEFT);

		certificate.addToTable(tablePatient, cell, "Transaction:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		cell.setColspan(3);

		String company = "";
		if (qisTransaction.getBiller() != null) {
			company = " (" + qisTransaction.getBiller() + ")";
		} else {
			if (qisTransaction.getPatient().getCorporate() != null) {
				company = " (" + qisTransaction.getPatient().getCorporate().getCompanyName() + ")";
			}
		}
		certificate.addToTable(tablePatient, cell,
				appUtility.getTransactionType(qisTransaction.getTransactionType()) + company,
				certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(tablePatient, cell, "Dispatch:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		certificate.addToTable(tablePatient, cell, appUtility.getDispatchType(qisTransaction.getDispatch()),
				certificate.getFontValue(), Element.ALIGN_LEFT);

		if (qisTransaction.getReferral() != null) {
			certificate.addToTable(tablePatient, cell, "Clinician:", certificate.getFontLabel(), Element.ALIGN_LEFT);
			cell.setColspan(3);
			certificate.addToTable(tablePatient, cell, qisTransaction.getReferral().getReferral(),
					certificate.getFontValue(), Element.ALIGN_LEFT);
			cell.setColspan(1);
			certificate.addToTable(tablePatient, cell, "", certificate.getFontLabel(), Element.ALIGN_LEFT);
			certificate.addToTable(tablePatient, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
//			certificate.addToTable(tablePatient, cell, "Page:", certificate.getFontLabel(), Element.ALIGN_LEFT);
//			certificate.addToTable(tablePatient, cell, document.getPageNumber() + "", certificate.getFontValue(),
//					Element.ALIGN_LEFT);			
		}

		certificate.addToTable(tablePatient, cell, "Received:", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tablePatient, cell,
				appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(), "MMM-dd-yyyy hh:mm:ss"),
				certificate.getFontDocValue(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tablePatient, cell, "Reported:", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		String reportedDate = "";
		if (verifiedDate != null) {
			reportedDate = appUtility.calendarToFormatedDate(verifiedDate, "MMM-dd-yyyy hh:mm:ss");
		}
		certificate.addToTable(tablePatient, cell, reportedDate, certificate.getFontDocValue(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tablePatient, cell, "Printed:", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tablePatient, cell,
				appUtility.calendarToFormatedDate(Calendar.getInstance(), "MMM-dd-yyyy hh:mm:ss"),
				certificate.getFontDocValue(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);

		return tablePatient;
	}


	private void formatEcg(Document document, QisTransaction qisTransaction, QisTransactionLabEcg qisEcg,
			boolean withHeaderFooter, Certificate certificate) {
		
		Font fontTitle;
		fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(16);

		Font fontSubTitle;
		fontSubTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontSubTitle.setSize(12);
		fontSubTitle.setColor(Color.BLUE);
		
		Font fontResult;
		fontResult = FontFactory.getFont("GARAMOND_BOLD");
		fontResult.setSize(12);
		
		Font fontName;
		fontName = FontFactory.getFont("GARAMOND", 12, Font.UNDERLINE);
		

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setUseAscender(true);
		cell.setUseDescender(true);

		// RESULTS
		PdfPTable tableReport = new PdfPTable(12);
		tableReport.setSpacingBefore(5);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell.setFixedHeight(20);
		cell.setColspan(12);
		certificate.addToTable(tableReport, cell, "", fontTitle, Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "ELECTROCARDIOGRAPHIC REPORT", fontTitle, Element.ALIGN_CENTER);
		cell.setBackgroundColor(null);

		cell.setFixedHeight(50);
		certificate.addToTable(tableReport, cell, "", fontTitle, Element.ALIGN_CENTER);
		cell.setFixedHeight(0);
		
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, "Rhythm", fontSubTitle, Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, " : ", fontSubTitle, Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tableReport, cell, qisEcg.getRhythm(), fontResult, Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", fontTitle, Element.ALIGN_CENTER);
		
		
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, "Axis", fontSubTitle, Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, " : ", fontSubTitle, Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tableReport, cell, qisEcg.getAxis(), fontResult, Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", fontTitle, Element.ALIGN_CENTER);
		
		
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, "PR Interval", fontSubTitle, Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, " : ", fontSubTitle, Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tableReport, cell, qisEcg.getPr_interval(), fontResult, Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", fontTitle, Element.ALIGN_CENTER);
		
		
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, "P-Wave", fontSubTitle, Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, " : ", fontSubTitle, Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tableReport, cell, qisEcg.getP_wave(), fontResult, Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", fontTitle, Element.ALIGN_CENTER);
		
		
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, "Rate Atrial", fontSubTitle, Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, " : ", fontSubTitle, Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tableReport, cell, qisEcg.getRate_atrial(), fontResult, Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", fontTitle, Element.ALIGN_CENTER);
		
		
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, "Ventricular", fontSubTitle, Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, " : ", fontSubTitle, Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tableReport, cell, qisEcg.getVentricular(), fontResult, Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", fontTitle, Element.ALIGN_CENTER);

		
		cell.setColspan(12);
		cell.setFixedHeight(120);
		certificate.addToTable(tableReport, cell, "", fontTitle, Element.ALIGN_CENTER);
		
		cell.setFixedHeight(0);
		cell.setColspan(12);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, "INTERPRETATION     :     ", fontSubTitle, Element.ALIGN_LEFT);
		
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_CENTER);
		cell.setColspan(11);
		certificate.addToTable(tableReport, cell, qisEcg.getInterpretation(), fontResult, Element.ALIGN_LEFT);
		
		
		cell.setColspan(12);
		cell.setFixedHeight(120);
		certificate.addToTable(tableReport, cell, "", fontTitle, Element.ALIGN_CENTER);
		
		cell.setFixedHeight(0);
		cell.setColspan(12);
		certificate.addToTable(tableReport, cell, "FROILAN CANLAS, MD", fontName, Element.ALIGN_LEFT);
		
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(12);
		certificate.addToTable(tableReport, cell, "INTERNAL MEDICINE", fontResult, Element.ALIGN_LEFT);
		
		
		
		document.add(tableReport);

		// SIGNATURES
//		document.add(certificate.certificateSinatureXrayClassification(false, qisXRay.getLabPersonel(),
//				xray.getRadiologist(), "Radiologist", 20));
	}
	
}
