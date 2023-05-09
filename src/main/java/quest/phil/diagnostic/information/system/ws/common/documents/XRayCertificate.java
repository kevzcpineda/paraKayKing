package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;

import com.lowagie.text.BadElementException;
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
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionXRay;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.xray.QisTransactionLabXRay;

public class XRayCertificate {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;

	public XRayCertificate(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getTransactionLaboratoryXRayCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionXRay qisXRay, boolean withHeaderFooter)
			throws DocumentException, IOException {

		QisTransactionLabXRay xray = qisXRay.getXray();

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisXRay.getLabPersonel(), qisXRay.getQualityControl(), xray.getRadiologist(),
				"Radiologist", qisXRay.getLabPersonelDate(), withHeaderFooter, false, "xray", null);
		pdfWriter.setPageEvent(event);
		document.open();

		Certificate certificate = new Certificate(appUtility);

		formatXRay(document, qisTransaction, qisXRay, withHeaderFooter, certificate);
		
		document.close();
		return document;
	}

	public void formatXRay(Document document, QisTransaction qisTransaction, QisTransactionXRay qisXRay,
			boolean withHeaderFooter, Certificate certificate) throws BadElementException, IOException {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);

		QisItem item = qisXRay.getItemDetails();

		// RESULTS
		PdfPTable tableReport = new PdfPTable(1);
		tableReport.setSpacingBefore(40);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);

		QisTransactionLabXRay xray = qisXRay.getXray();
		cell.setColspan(1);
		cell.setFixedHeight(40);
		certificate.addToTable(tableReport, cell, "RADIOGRAPHIC REPORT", certificate.getFontTitleXray(),
				Element.ALIGN_CENTER);
		cell.setFixedHeight(75);
		certificate.addToTable(tableReport, cell, item.getItemDescription(), certificate.getFontSubTitleXray(),
				Element.ALIGN_CENTER);

		cell.setFixedHeight(0);
		certificate.addToTable(tableReport, cell, "X-Ray Findings:", certificate.getFontSubTitleXray(), Element.ALIGN_LEFT);
		cell.setFixedHeight(120);
		certificate.addToTable(tableReport, cell, " " + xray.getFindings().replace(".", ".\n"), certificate.getFontValueNormalXray(),
				Element.ALIGN_LEFT);

		cell.setFixedHeight(0);
		certificate.addToTable(tableReport, cell, "X-Ray Impression:", certificate.getFontSubTitleXray(),
				Element.ALIGN_LEFT);
		cell.setFixedHeight(70);
		certificate.addToTable(tableReport, cell, xray.getImpressions(), certificate.getFontSubTitleXray(),
				Element.ALIGN_CENTER);

		document.add(tableReport);
	}

	public void formatXRayClassification(Document document, QisTransaction qisTransaction, QisTransactionXRay qisXRay,
			boolean withHeaderFooter, Certificate certificate) throws BadElementException, IOException {

		Font fontTitle;
		fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(10);
		fontTitle.setColor(Color.WHITE);

		Font fontSubTitle;
		fontSubTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontSubTitle.setSize(10);
		fontSubTitle.setColor(Color.BLUE);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setUseAscender(true);
		cell.setUseDescender(true);

		// RESULTS
		PdfPTable tableReport = new PdfPTable(1);
		tableReport.setSpacingBefore(5);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell.setBackgroundColor(new Color(66, 103, 178));
		certificate.addToTable(tableReport, cell, "RADIOLOGY REPORT", fontTitle, Element.ALIGN_CENTER);
		cell.setBackgroundColor(null);
		QisTransactionLabXRay xray = qisXRay.getXray();
		certificate.addToTable(tableReport, cell, xray.getFindings().toUpperCase(), certificate.getFontValueNormal(),
				Element.ALIGN_LEFT);

		certificate.addToTable(tableReport, cell, "IMPRESSION:", fontSubTitle, Element.ALIGN_LEFT);

		certificate.addToTable(tableReport, cell, "                   " + xray.getImpressions().toUpperCase(),
				certificate.getFontValueNormal(), Element.ALIGN_LEFT);

		document.add(tableReport);

		// SIGNATURES
		document.add(certificate.certificateSinatureXrayClassification(false, qisXRay.getLabPersonel(),
				xray.getRadiologist(), "Radiologist", 20));

	}

	public void formatXRayConsolidated(Document document, QisTransaction qisTransaction, QisTransactionXRay qisXRay,
			boolean withHeaderFooter, Certificate certificate, PdfPTable tableReport)
			throws BadElementException, IOException {

		Font fontTitle;
		fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(10);
		fontTitle.setColor(Color.WHITE);

		Font fontSubTitle;
		fontSubTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontSubTitle.setSize(10);
		fontSubTitle.setColor(Color.BLUE);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setUseAscender(true);
		cell.setUseDescender(true);

		cell.setFixedHeight(20);
		certificate.addToTable(tableReport, cell, qisXRay.getItemDetails().getItemDescription().toUpperCase(),
				fontSubTitle, Element.ALIGN_LEFT);
		cell.setFixedHeight(0);

		QisTransactionLabXRay xray = qisXRay.getXray();
		certificate.addToTable(tableReport, cell, xray != null ? xray.getFindings().toUpperCase() : "",
				certificate.getFontValueNormal(), Element.ALIGN_LEFT);

		certificate.addToTable(tableReport, cell, "IMPRESSION:", fontSubTitle, Element.ALIGN_LEFT);

		certificate.addToTable(tableReport, cell,
				xray != null ? "                   " + xray.getImpressions().toUpperCase() : "",
				certificate.getFontValueNormal(), Element.ALIGN_LEFT);

	}
}
