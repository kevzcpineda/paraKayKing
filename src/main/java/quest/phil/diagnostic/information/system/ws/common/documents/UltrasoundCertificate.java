package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionUltrasound;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ultrasound.QisTransactionLabUltrasound;

public class UltrasoundCertificate {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private Certificate certificate;

	public UltrasoundCertificate(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
		this.certificate = new Certificate(appUtility);
	}

	public Document getTransactionLaboratoryUltrasoundCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionLabUltrasound oldUltrasoundData, boolean withHeaderFooter,
			QisTransactionUltrasound qisTransactionUltrasound) throws DocumentException, IOException {

		Certificate certificate = new Certificate(appUtility);
		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, null, null, oldUltrasoundData.getRadiologist(),
				"Radiologist", null, withHeaderFooter, false, "ultrasound", null);
		pdfWriter.setPageEvent(event);
		document.open();
		Calendar calndr = Calendar.getInstance();
//		document.add(certificatePatientInformation(qisTransaction, calndr, document));

		formatUtrasound(document, qisTransaction, oldUltrasoundData, withHeaderFooter, certificate,
				qisTransactionUltrasound);
		document.close();
		return document;
	}

//	public PdfPTable certificatePatientInformation(QisTransaction qisTransaction, Calendar verifiedDate,
//			Document document) {
//		PdfPTable tablePatient = new PdfPTable(new float[] { 1, 2, 1, 2, 1, 2 });
//		tablePatient.setSpacingBefore(0);
//		tablePatient.setWidthPercentage(90f);
//		tablePatient.setHorizontalAlignment(Element.ALIGN_CENTER);
//
//		PdfPCell cell = new PdfPCell();
//		cell.setBorder(Rectangle.NO_BORDER);
//		cell.setBorderColor(Color.BLACK);
//
//		QisPatient patient = qisTransaction.getPatient();
//		certificate.addToTable(tablePatient, cell, "Name:", certificate.getFontLabel(), Element.ALIGN_LEFT);
//		cell.setColspan(3);
//		certificate.addToTable(tablePatient, cell, appUtility.getPatientFullname(patient), certificate.getFontValue(),
//				Element.ALIGN_LEFT);
//		cell.setColspan(1);
//		certificate.addToTable(tablePatient, cell, "SR#:", certificate.getFontLabel(), Element.ALIGN_LEFT);
//		certificate.addToTable(tablePatient, cell, appUtility.numberFormat(qisTransaction.getId(), "000000"),
//				certificate.getFontValue(), Element.ALIGN_LEFT);
//
//		String gender = "MALE";
//		if ("F".equals(patient.getGender())) {
//			gender = "FEMALE";
//		}
//		String age = String
//				.valueOf(appUtility.calculateAgeInYear(patient.getDateOfBirth(), qisTransaction.getTransactionDate()));
//
//		certificate.addToTable(tablePatient, cell, "Age/Gender:", certificate.getFontLabel(), Element.ALIGN_LEFT);
//		cell.setColspan(3);
//		certificate.addToTable(tablePatient, cell, age + "/" + gender, certificate.getFontValue(), Element.ALIGN_LEFT);
//		cell.setColspan(1);
//		certificate.addToTable(tablePatient, cell, "QuestID:", certificate.getFontLabel(), Element.ALIGN_LEFT);
//		certificate.addToTable(tablePatient, cell, appUtility.numberFormat(patient.getId(), "0000"),
//				certificate.getFontValue(), Element.ALIGN_LEFT);
//
//		certificate.addToTable(tablePatient, cell, "Transaction:", certificate.getFontLabel(), Element.ALIGN_LEFT);
//		cell.setColspan(3);
//
//		String company = "";
//		if (qisTransaction.getBiller() != null) {
//			company = " (" + qisTransaction.getBiller() + ")";
//		} else {
//			if (qisTransaction.getPatient().getCorporate() != null) {
//				company = " (" + qisTransaction.getPatient().getCorporate().getCompanyName() + ")";
//			}
//		}
//		certificate.addToTable(tablePatient, cell,
//				appUtility.getTransactionType(qisTransaction.getTransactionType()) + company,
//				certificate.getFontValue(), Element.ALIGN_LEFT);
//		cell.setColspan(1);
//		certificate.addToTable(tablePatient, cell, "Dispatch:", certificate.getFontLabel(), Element.ALIGN_LEFT);
//		certificate.addToTable(tablePatient, cell, appUtility.getDispatchType(qisTransaction.getDispatch()),
//				certificate.getFontValue(), Element.ALIGN_LEFT);
//
//		if (qisTransaction.getReferral() != null) {
//			certificate.addToTable(tablePatient, cell, "Clinician:", certificate.getFontLabel(), Element.ALIGN_LEFT);
//			cell.setColspan(3);
//			certificate.addToTable(tablePatient, cell, qisTransaction.getReferral().getReferral(),
//					certificate.getFontValue(), Element.ALIGN_LEFT);
//			cell.setColspan(1);
//			certificate.addToTable(tablePatient, cell, "", certificate.getFontLabel(), Element.ALIGN_LEFT);
//			certificate.addToTable(tablePatient, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
////			certificate.addToTable(tablePatient, cell, "Page:", certificate.getFontLabel(), Element.ALIGN_LEFT);
////			certificate.addToTable(tablePatient, cell, document.getPageNumber() + "", certificate.getFontValue(),
////					Element.ALIGN_LEFT);			
//		}
//
//		certificate.addToTable(tablePatient, cell, "Received:", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
//		cell.setBorder(Rectangle.BOTTOM);
//		certificate.addToTable(tablePatient, cell,
//				appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(), "MMM-dd-YYYY hh:mm:ss"),
//				certificate.getFontDocValue(), Element.ALIGN_LEFT);
//		cell.setBorder(Rectangle.NO_BORDER);
//		certificate.addToTable(tablePatient, cell, "Reported:", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
//		cell.setBorder(Rectangle.BOTTOM);
//		String reportedDate = "";
//		if (verifiedDate != null) {
//			reportedDate = appUtility.calendarToFormatedDate(verifiedDate, "MMM-dd-YYYY hh:mm:ss");
//		}
//		certificate.addToTable(tablePatient, cell, reportedDate, certificate.getFontDocValue(), Element.ALIGN_LEFT);
//		cell.setBorder(Rectangle.NO_BORDER);
//		certificate.addToTable(tablePatient, cell, "Printed:", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
//		cell.setBorder(Rectangle.BOTTOM);
//		certificate.addToTable(tablePatient, cell,
//				appUtility.calendarToFormatedDate(Calendar.getInstance(), "MMM-dd-YYYY hh:mm:ss"),
//				certificate.getFontDocValue(), Element.ALIGN_LEFT);
//		cell.setBorder(Rectangle.NO_BORDER);
//
//		return tablePatient;
//	}

	private void formatUtrasound(Document document, QisTransaction qisTransaction,
			QisTransactionLabUltrasound oldUltrasoundData, boolean withHeaderFooter, Certificate certificate2,
			QisTransactionUltrasound qisTransactionUltrasound) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);
		
		// RESULTS
		PdfPTable tableReport = new PdfPTable(12);
		tableReport.setSpacingBefore(40);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell.setColspan(12);
		cell.setFixedHeight(40);
		certificate.addToTable(tableReport, cell, "SONOGRAPHIC REPORT", certificate.getFontTitleXray(),
				Element.ALIGN_CENTER);
		cell.setFixedHeight(40);
		certificate.addToTable(tableReport, cell, qisTransactionUltrasound.getItemDetails().getItemDescription(),
				certificate.getFontSubTitleXray(), Element.ALIGN_CENTER);

		if (oldUltrasoundData.getFinding_footer_pelvic() == null) {
			cell.setFixedHeight(300);
			certificate.addToTable(tableReport, cell, " " + oldUltrasoundData.getFindings(), certificate.getLabel12Black(),
					Element.ALIGN_LEFT);			
		}else {
			cell.setFixedHeight(0);
			certificate.addToTable(tableReport, cell, oldUltrasoundData.getFinding_header_pelvic(), certificate.getLabel12Black(),
					Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", certificate.getLabel12Black(),
					Element.ALIGN_LEFT);	
			certificate.addToTable(tableReport, cell, "", certificate.getLabel12Black(),
					Element.ALIGN_LEFT);	
			certificate.addToTable(tableReport, cell, "BIOMETRIC PARAMETERS :", certificate.getFontSubTitle(), Element.ALIGN_LEFT);
			
			cell.setColspan(4);
			cell.setFixedHeight(0);
			
			cell.setBorder(Rectangle.BOX);
			certificate.addToTable(tableReport, cell, "A) Biparietal diameter (BPD)", certificate.getLabel12Black(),
					Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, oldUltrasoundData.getBpd_size() + " cm", certificate.getLabel12Black(),
					Element.ALIGN_CENTER);
			certificate.addToTable(tableReport, cell, oldUltrasoundData.getBpd_old(), certificate.getLabel12Black(),
					Element.ALIGN_CENTER);
			
			certificate.addToTable(tableReport, cell, "B) Head Circumference (HC)  ", certificate.getLabel12Black(),
					Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, oldUltrasoundData.getHc_size() + " cm", certificate.getLabel12Black(),
					Element.ALIGN_CENTER);
			certificate.addToTable(tableReport, cell, oldUltrasoundData.getHc_old(), certificate.getLabel12Black(),
					Element.ALIGN_CENTER);
			
			certificate.addToTable(tableReport, cell, "C) Abdominal Circumference(AC)", certificate.getLabel12Black(),
					Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, oldUltrasoundData.getAc_size() + " cm", certificate.getLabel12Black(),
					Element.ALIGN_CENTER);
			certificate.addToTable(tableReport, cell, oldUltrasoundData.getAc_old(), certificate.getLabel12Black(),
					Element.ALIGN_CENTER);
			
			certificate.addToTable(tableReport, cell, "D) Femoral length (FL)", certificate.getLabel12Black(),
					Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, oldUltrasoundData.getFl_size() + " cm", certificate.getLabel12Black(),
					Element.ALIGN_CENTER);
			certificate.addToTable(tableReport, cell, oldUltrasoundData.getFl_old(), certificate.getLabel12Black(),
					Element.ALIGN_CENTER);
			
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(12);
			cell.setFixedHeight(200);
			certificate.addToTable(tableReport, cell, oldUltrasoundData.getFinding_footer_pelvic(), certificate.getLabel12Black(),
					Element.ALIGN_LEFT);
		}
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(12);
		cell.setFixedHeight(0);
		certificate.addToTable(tableReport, cell, "Impression:", certificate.getFontSubTitleXray(), Element.ALIGN_LEFT);
		cell.setFixedHeight(100);
		certificate.addToTable(tableReport, cell, oldUltrasoundData.getImpressions(), certificate.getFontTitle(),
				Element.ALIGN_LEFT);

		document.add(tableReport);
	}
}
