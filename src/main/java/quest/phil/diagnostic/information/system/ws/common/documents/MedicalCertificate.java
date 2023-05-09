package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItemLaboratories;

public class MedicalCertificate {
	private String applicationName;
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;

	public MedicalCertificate(String applicationName, String applicationHeader, String applicationFooter,
			AppUtility appUtility) {
		super();
		this.applicationName = applicationName;
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getTransactionMedicalCertificate(Document document, QisTransaction qisTransaction,
			QisTransactionItemLaboratories qisTxnItem, boolean withHeaderFooter) throws DocumentException, IOException {
		document.open();

		Certificate certificate = new Certificate(appUtility);

		// HEADER IMAGE
		Image imgHeader = Image.getInstance("src/main/resources/images/" + applicationHeader);
		if (!withHeaderFooter) {
			imgHeader = null;
		}
		document.add(certificate.certificateHeader(imgHeader));

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPCell cellBorder = new PdfPCell();
		cellBorder.setBorder(Rectangle.BOTTOM);
		cellBorder.setBorderColor(Color.BLUE);
		cellBorder.setVerticalAlignment(Element.ALIGN_BOTTOM);

		float spacing = formatMedicalCertificate(document, qisTransaction, qisTxnItem, certificate, cell, cellBorder);

		Image imgFooter = Image.getInstance("src/main/resources/images/" + applicationFooter);
		if (!withHeaderFooter) {
			imgFooter = null;
		}
		document.add(certificate.certificateFooter(imgFooter, spacing));

//		final HeaderFooter footer = new HeaderFooter(new Phrase("Footer"), false);
//		footer.setAlignment(Element.ALIGN_LEFT);
//		footer.setBorder(Rectangle.NO_BORDER);
//		document.setFooter(footer);

		document.close();
		return document;

	}

	public float formatMedicalCertificate(Document document, QisTransaction qisTransaction,
			QisTransactionItemLaboratories qisTxnItem, Certificate certificate, PdfPCell cell, PdfPCell cellBorder) {
		Font fontTitle;
		fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(12);
		fontTitle.setColor(Color.WHITE);

		Font fontFooterLabel;
		fontFooterLabel = FontFactory.getFont("GARAMOND");
		fontFooterLabel.setSize(12);
		fontFooterLabel.setColor(Color.BLUE);

		Font fontFooterValue;
		fontFooterValue = FontFactory.getFont("GARAMOND_BOLD");
		fontFooterValue.setSize(12);
		fontFooterValue.setColor(Color.BLACK);

		Font fontLabel;
		fontLabel = FontFactory.getFont("GARAMOND_BOLD");
		fontLabel.setSize(10);
		fontLabel.setColor(Color.BLUE);

		Font fontContent;
		fontContent = FontFactory.getFont("GARAMOND");
		fontContent.setSize(14);
		fontContent.setColor(Color.BLUE);

		Font fontResult;
		fontResult = FontFactory.getFont("GARAMOND_BOLD", 14, Font.UNDERLINE);
		fontResult.setColor(Color.BLACK);

		Font fontResultAlert;
		fontResultAlert = FontFactory.getFont("GARAMOND_BOLD", 14, Font.UNDERLINE);
		fontResultAlert.setColor(Color.RED);

		// REPORT HEADER
		PdfPTable headerReport = new PdfPTable(1);
		headerReport.setSpacingBefore(50);
		headerReport.setWidthPercentage(90f);
		headerReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerReport.getDefaultCell().setBorder(0);
		headerReport.getDefaultCell().setPadding(0);

		cell.setFixedHeight(10);
		certificate.addToTable(headerReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setBackgroundColor(new Color(66, 103, 178));
		cell.setFixedHeight(20);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		certificate.addToTable(headerReport, cell, applicationName + " MEDICAL CERTIFICATE", fontTitle,
				Element.ALIGN_CENTER);
		cell.setBackgroundColor(null);
		cell.setFixedHeight(0);

		certificate.addToTable(headerReport, cell,
				"Medical Examination Rating System (DOH, Bureau of Licensing and Regulation; Administrative Code no. 85-A series 1990)",
				certificate.getFontNotes(), Element.ALIGN_CENTER);

		cell.setFixedHeight(30);
		certificate.addToTable(headerReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setFixedHeight(0);

		document.add(headerReport);

		QisPatient patient = qisTransaction.getPatient();
//		QisCorporate corporate = qisTransaction.getCorporate();

		boolean isError = true;
		float spacing = 360;
		String classification = "UNCLASSIFIED";
		if (qisTxnItem.getClassification() != null) {
			switch (qisTxnItem.getClassification()) {
			case "A":
				classification = "CLASS A - Fit to work";
				isError = false;
				spacing = 360;
				break;

			case "B":
				classification = "CLASS B - Fit to work, but with minor condition curable within a short period of time, that will not adversely affect the workers efficiency";
				isError = false;
				spacing = 310;
				break;

			case "C":
				classification = "CLASS C - With abnormal findings generally not acceptable for employment";
				spacing = 340;
				break;

			case "D":
				classification = "CLASS D - Unemployable";
				spacing = 360;
				break;
				
				case "E":
				classification = "CLASS E - Incomplete";
				spacing = 360;
				break;

				case "F":
				classification = "CLASS F - Pending with findings";
				spacing = 360;
				break;

			case "P":
				classification = "PENDING - These are cases that are equivocal as to the classification are being evaluated further";
				spacing = 340;
				break;

			default:
				break;
			}
		}

		Paragraph p = new Paragraph();
		p.setLeading(25);
		p.setFirstLineIndent(25);
		p.setIndentationLeft(40);
		p.setIndentationRight(40);
		p.setAlignment(Element.ALIGN_LEFT);

		p.add(new Chunk("I certify that I have examined ", fontContent));
		p.add(new Chunk(appUtility.getPatientFullname(patient), fontResult));

		if (qisTransaction.getPatient().getCorporate() != null) {
			p.add(new Chunk(" and found applicant of ", fontContent));
			p.add(new Chunk(qisTransaction.getPatient().getCorporate().getCompanyName(), fontResult));
		} else {
			if (qisTransaction.getBiller() != null) {
				p.add(new Chunk(" and found applicant of ", fontContent));
				p.add(new Chunk(qisTransaction.getBiller(), fontResult));
			}
		}
		p.add(new Chunk(" with a classification of ", fontContent));
		p.add(new Chunk(classification, isError ? fontResultAlert : fontResult));
		p.add(new Chunk(" as of ", fontContent));
		p.add(new Chunk(appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(), "MMMM dd, yyyy"),
				fontResult));
		p.add(new Chunk(".", fontContent));
		document.add(p);

		// REPORT FOOTER
		PdfPTable footerReport = new PdfPTable(new float[] { 1, 6, 1, 6, 1, 6, 1 });
		footerReport.setSpacingBefore(80);
		footerReport.setWidthPercentage(90f);
		footerReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		footerReport.getDefaultCell().setBorder(0);
		footerReport.getDefaultCell().setPadding(0);

		String doctorName = "";
		String licenseNumber = "";
		if (qisTxnItem.getClassifyDoctor() != null) {
			doctorName = appUtility.getDoctorsDisplayName(qisTxnItem.getClassifyDoctor());
			if (qisTxnItem.getClassifyDoctor().getLicenseNumber() != null
					&& !"".equals(qisTxnItem.getClassifyDoctor().getLicenseNumber().trim())) {
				licenseNumber = qisTxnItem.getClassifyDoctor().getLicenseNumber();
			}
		}
		
		certificate.addToTable(footerReport, cell, "Others/Notes:", fontFooterLabel, Element.ALIGN_LEFT);
		certificate.addToTable(footerReport, cellBorder, qisTxnItem.getOverAllFindings(), fontFooterValue,
				Element.ALIGN_LEFT);
		certificate.addToTable(footerReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(footerReport, cell, "Physician:", fontFooterLabel, Element.ALIGN_LEFT);
		certificate.addToTable(footerReport, cellBorder, doctorName, fontFooterValue, Element.ALIGN_CENTER);

		cell.setColspan(3);
		certificate.addToTable(footerReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(footerReport, cell, "License:", fontFooterLabel, Element.ALIGN_LEFT);
		certificate.addToTable(footerReport, cell, licenseNumber, fontFooterValue, Element.ALIGN_CENTER);

		document.add(footerReport);

		return spacing;
	}

	public float formatMedicalCertificateClassification(Document document, QisTransaction qisTransaction,
			QisTransactionItemLaboratories qisTxnItem, Certificate certificate, PdfPCell cell, PdfPCell cellBorder) {

		Font fontTitle;
		fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(10);
		fontTitle.setColor(Color.WHITE);

		Font fontContent;
		fontContent = FontFactory.getFont("GARAMOND");
		fontContent.setSize(10);
		fontContent.setColor(Color.BLUE);

		Font fontResult;
		fontResult = FontFactory.getFont("GARAMOND_BOLD", 10, Font.UNDERLINE);
		fontResult.setColor(Color.BLACK);

		Font fontResultAlert;
		fontResultAlert = FontFactory.getFont("GARAMOND_BOLD", 10, Font.UNDERLINE);
		fontResultAlert.setColor(Color.RED);

		Font fontFooterLabel;
		fontFooterLabel = FontFactory.getFont("GARAMOND");
		fontFooterLabel.setSize(10);
		fontFooterLabel.setColor(Color.BLUE);

		Font fontFooterValue;
		fontFooterValue = FontFactory.getFont("GARAMOND_BOLD");
		fontFooterValue.setSize(10);
		fontFooterValue.setColor(Color.BLACK);

		// REPORT HEADER
		PdfPTable headerReport = new PdfPTable(1);
		headerReport.setSpacingBefore(5);
		headerReport.setWidthPercentage(90f);
		headerReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerReport.getDefaultCell().setBorder(0);
		headerReport.getDefaultCell().setPadding(0);

		certificate.addToTable(headerReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setBackgroundColor(new Color(66, 103, 178));
		certificate.addToTable(headerReport, cell, applicationName + " MEDICAL CERTIFICATE", fontTitle,
				Element.ALIGN_CENTER);
		cell.setBackgroundColor(null);

		certificate.addToTable(headerReport, cell,
				"Medical Examination Rating System (DOH, Bureau of Licensing and Regulation; Administrative Code no. 85-A series 1990)",
				certificate.getFontNotes(), Element.ALIGN_CENTER);

		document.add(headerReport);

		QisPatient patient = qisTransaction.getPatient();
//		QisCorporate corporate = qisTransaction.getCorporate();

		boolean isError = true;
		float spacing = 360;
		String classification = "UNCLASSIFIED";
		if (qisTxnItem.getClassification() != null) {
			switch (qisTxnItem.getClassification()) {
			case "A":
				classification = "CLASS A - Fit to work";
				isError = false;
				spacing = 360;
				break;

			case "B":
				classification = "CLASS B - Fit to work, but with minor condition curable within a short period of time, that will not adversely affect the workers efficiency";
				isError = false;
				spacing = 310;
				break;

			case "C":
				classification = "CLASS C - With abnormal findings generally not acceptable for employment";
				spacing = 340;
				break;

			case "D":
				classification = "CLASS D - Unemployable";
				spacing = 360;
				break;

				case "E":
				classification = "CLASS E - Incomplete";
				spacing = 360;
				break;

				case "F":
				classification = "CLASS F - Pending with findings";
				spacing = 360;
				break;

			case "P":
				classification = "PENDING - These are cases that are equivocal as to the classification are being evaluated further";
				spacing = 340;
				break;

			default:
				break;
			}
		}

		Paragraph p = new Paragraph();
		p.setLeading(15);
		p.setFirstLineIndent(25);
		p.setIndentationLeft(40);
		p.setIndentationRight(40);
		p.setAlignment(Element.ALIGN_LEFT);

		p.add(new Chunk("I certify that I have examined ", fontContent));
		p.add(new Chunk(appUtility.getPatientFullname(patient), fontResult));
		if (qisTransaction.getPatient().getCorporate() != null) {
			p.add(new Chunk(" and found applicant of ", fontContent));
			p.add(new Chunk(qisTransaction.getPatient().getCorporate().getCompanyName(), fontResult));
		} else {
			if (qisTransaction.getBiller() != null) {
				p.add(new Chunk(" and found applicant of ", fontContent));
				p.add(new Chunk(qisTransaction.getBiller(), fontResult));
			}
		}
		p.add(new Chunk(" with a classification of ", fontContent));
		p.add(new Chunk(classification, isError ? fontResultAlert : fontResult));
		p.add(new Chunk(" as of ", fontContent));
		p.add(new Chunk(appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(), "MMMM dd, yyyy"),
				fontResult));
		p.add(new Chunk(".", fontContent));
		document.add(p);

		// REPORT FOOTER
		PdfPTable footerReport = new PdfPTable(new float[] { 2, 5, 1, 2, 5 });
		footerReport.setSpacingBefore(10);
		footerReport.setWidthPercentage(90f);
		footerReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		footerReport.getDefaultCell().setBorder(0);
		footerReport.getDefaultCell().setPadding(0);

		String doctorName = "";
		String licenseNumber = "";
		if (qisTxnItem.getClassifyDoctor() != null) {
			doctorName = appUtility.getDoctorsDisplayName(qisTxnItem.getClassifyDoctor());
			if (qisTxnItem.getClassifyDoctor().getLicenseNumber() != null
					&& !"".equals(qisTxnItem.getClassifyDoctor().getLicenseNumber().trim())) {
				licenseNumber = qisTxnItem.getClassifyDoctor().getLicenseNumber();
			}
		}

		certificate.addToTable(footerReport, cell, "Others/Notes:", fontFooterLabel, Element.ALIGN_LEFT);
		certificate.addToTable(footerReport, cellBorder, qisTxnItem.getOverAllFindings(), fontFooterValue,
				Element.ALIGN_LEFT);
		certificate.addToTable(footerReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(footerReport, cell, "Physician:", fontFooterLabel, Element.ALIGN_LEFT);
		certificate.addToTable(footerReport, cellBorder, doctorName, fontFooterValue, Element.ALIGN_CENTER);

		cell.setColspan(3);
		certificate.addToTable(footerReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(footerReport, cell, "License:", fontFooterLabel, Element.ALIGN_LEFT);
		certificate.addToTable(footerReport, cell, licenseNumber, fontFooterValue, Element.ALIGN_CENTER);

		document.add(footerReport);

		return spacing;
	}
}
