package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;

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
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionToxicology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.toxi.QisTransactionLabToxicology;

public class ToxicologyCertificate {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private String NO_RESULT = "- NO RESULT -";

	public ToxicologyCertificate(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getTransactionLaboratoryToxicologyCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionToxicology qisToxicology, boolean withHeaderFooter)
			throws DocumentException, IOException {

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisToxicology.getLabPersonel(), qisToxicology.getQualityControl(),
				qisToxicology.getMedicalDoctor(), "Pathologist", qisToxicology.getVerifiedDate(), withHeaderFooter,
				false, "", null);
		pdfWriter.setPageEvent(event);
		document.open();

		Certificate certificate = new Certificate(appUtility);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);

		// RESULTS
		PdfPTable tableReport = new PdfPTable(3);
		tableReport.setSpacingBefore(40);
		tableReport.setWidthPercentage(80f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);

		QisTransactionLabToxicology toxicology = qisToxicology.getToxicology();
		cell.setColspan(3);
		cell.setFixedHeight(45);
		certificate.addToTable(tableReport, cell, "LABORATORY RESULT", certificate.getFontTitle(),
				Element.ALIGN_CENTER);

		
		
		cell.setFixedHeight(0);
		certificate.addToTable(tableReport, cell, "TOXICOLOGY", certificate.getFontSubTitle(), Element.ALIGN_LEFT);
		cell.setFixedHeight(20);
		cell.setColspan(3);
		cell.setFixedHeight(20);
		certificate.addToTable(tableReport, cell, "DRUG TEST RESULT", certificate.getFontSubTitle(),
				Element.ALIGN_LEFT);
		cell.setFixedHeight(20);

		boolean withResult = false;
		cell.setColspan(1);
		if (toxicology.getMethamphethamine() != null) {
			certificate.addToTable(tableReport, cell, "   Methamphethamine", certificate.getFontLabel(),
					Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			certificate.addToTable(tableReport, cell, appUtility.getPositiveNegative(toxicology.getMethamphethamine()),
					certificate.getFontValue(), Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			certificate.addToTable(tableReport, cell, "", certificate.getFontLabel(), Element.ALIGN_LEFT);
			withResult = true;
		}

		if (toxicology.getTetrahydrocanabinol() != null) {
			certificate.addToTable(tableReport, cell, "   Tetrahydrocannabinol", certificate.getFontLabel(),
					Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			certificate.addToTable(tableReport, cell,
					appUtility.getPositiveNegative(toxicology.getTetrahydrocanabinol()), certificate.getFontValue(),
					Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			certificate.addToTable(tableReport, cell, "", certificate.getFontLabel(), Element.ALIGN_LEFT);
			withResult = true;
		}

		if (!withResult) {
			cell.setColspan(3);
			certificate.addToTable(tableReport, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
					Element.ALIGN_LEFT);
		}

		document.add(tableReport);

		document.close();
		return document;
	}

	public void formatToxicologyCertificate(QisTransactionToxicology qisToxicology, PdfPTable tableReport,
			Certificate certificate, PdfPCell cell, PdfPCell cellBorder) {
		QisTransactionLabToxicology toxicology = qisToxicology.getToxicology();

		PdfPTable toxiLeft = new PdfPTable(new float[] { 1, 4, 4, 4 });
		toxiLeft.setWidthPercentage(100f);
		toxiLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable toxiRight = new PdfPTable(new float[] { 1, 4, 4, 4 });
		toxiRight.setWidthPercentage(100f);
		toxiRight.setHorizontalAlignment(Element.ALIGN_LEFT);

		cell.setColspan(1);
		certificate.addToTable(toxiLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
		cell.setColspan(2);
		certificate.addToTable(toxiLeft, cell, "Methamphethamine", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
		if (toxicology != null && toxicology.getMethamphethamine() != null) {
			certificate.addToTable(toxiLeft, cellBorder,
					appUtility.getPositiveNegative(toxicology.getMethamphethamine()), certificate.getFontDocValue(),
					Element.ALIGN_CENTER);
		} else {
			cell.setColspan(1);
			certificate.addToTable(toxiLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
					Element.ALIGN_LEFT);
		}
		cell.setColspan(4);
		certificate.addToTable(toxiRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

		cell.setColspan(1);
		certificate.addToTable(toxiLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
		cell.setColspan(2);
		certificate.addToTable(toxiLeft, cell, "Tetrahydrocannabinol", certificate.getFontDocLabel(),
				Element.ALIGN_LEFT);
		if (toxicology != null && toxicology.getTetrahydrocanabinol() != null) {
			certificate.addToTable(toxiLeft, cellBorder,
					appUtility.getPositiveNegative(toxicology.getTetrahydrocanabinol()), certificate.getFontDocValue(),
					Element.ALIGN_CENTER);
		} else {
			cell.setColspan(1);
			certificate.addToTable(toxiLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
					Element.ALIGN_LEFT);
		}
		cell.setColspan(4);
		certificate.addToTable(toxiRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

		tableReport.addCell(toxiLeft);
		tableReport.addCell(toxiRight);
	}

	public void formatToxicologyClassification(QisTransactionToxicology qisToxicology, PdfPTable tableReport,
			Certificate certificate, PdfPCell cell) {
		QisTransactionLabToxicology toxicology = qisToxicology.getToxicology();

		cell.setBorderColor(Color.BLUE);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, "DRUG TESTING", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "  Methamphethamine", certificate.getFontDocLabel(),
				Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setColspan(3);
		if (toxicology != null && toxicology.getMethamphethamine() != null) {
			certificate.addToTable(tableReport, cell,
					appUtility.getPositiveNegative(toxicology.getMethamphethamine()), certificate.getFontDocValue(),
					Element.ALIGN_CENTER);
		} else {
			certificate.addToTable(tableReport, cell, "NO RESULT", certificate.getFontDocAlert(), Element.ALIGN_CENTER);
		}
		cell.setBorder(Rectangle.NO_BORDER);

		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "  Tetrahydrocannabinol", certificate.getFontDocLabel(),
				Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setColspan(3);
		if (toxicology != null && toxicology.getTetrahydrocanabinol() != null) {
			certificate.addToTable(tableReport, cell,
					appUtility.getPositiveNegative(toxicology.getTetrahydrocanabinol()),
					certificate.getFontDocValue(), Element.ALIGN_CENTER);
		} else {
			certificate.addToTable(tableReport, cell, "NO RESULT", certificate.getFontDocAlert(), Element.ALIGN_CENTER);
		}
		cell.setBorder(Rectangle.NO_BORDER);

		cell.setColspan(4);
		cell.setFixedHeight(10);
		certificate.addToTable(tableReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
		cell.setFixedHeight(0);

	}

	public void formatToxicologyConsolidated(QisTransactionToxicology qisToxicology, PdfPTable tableReport,
			Certificate certificate) {
		QisTransactionLabToxicology toxicology = qisToxicology.getToxicology();

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setUseAscender(true);
		cell.setUseDescender(true);

		Font fontTest = FontFactory.getFont("GARAMOND");
		fontTest.setSize(11);
		fontTest.setColor(Color.BLACK);

		Font fontResult = certificate.getFontResult();
		Font fontResultRed = certificate.getFontResultRed();

		Font fontUnit = FontFactory.getFont("GARAMOND");
		fontUnit.setSize(10);
		fontUnit.setColor(Color.BLACK);

		cell.setColspan(9);
		certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
		certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
		cell.setFixedHeight(20);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		certificate.addToTable(tableReport, cell, "Drug Testing", fontResult, Element.ALIGN_LEFT);
		cell.setFixedHeight(0);

		cell.setVerticalAlignment(Element.ALIGN_TOP);

		boolean withResult = false;
		if (toxicology != null) {
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "  Methamphethamine", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(2);
			if (toxicology.getMethamphethamine() != null) {
				certificate.addToTable(tableReport, cell,
						appUtility.getPositiveNegative(toxicology.getMethamphethamine()),
						certificate.getDisplayFont(toxicology.getMethamphethamine(), fontResult, fontResultRed),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
			}
			cell.setColspan(4);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

			cell.setColspan(3);
			certificate.addToTable(tableReport, cell, "  Tetrahydrocannabinol:", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(2);
			if (toxicology.getTetrahydrocanabinol() != null) {
				certificate.addToTable(tableReport, cell,
						appUtility.getPositiveNegative(toxicology.getTetrahydrocanabinol()),
						certificate.getDisplayFont(toxicology.getTetrahydrocanabinol(), fontResult, fontResultRed),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
			}
			cell.setColspan(4);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

			withResult = true;
		}

		if (!withResult) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
		}
	}
}
