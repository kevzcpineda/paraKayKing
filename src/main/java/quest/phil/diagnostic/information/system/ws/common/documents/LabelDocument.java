package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.QisLabelInformation;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionLabelRequest;

public class LabelDocument {
	private String applicationName;
	private AppUtility appUtility;

	public LabelDocument(String applicationName, AppUtility appUtility) {
		super();
		this.applicationName = applicationName;
		this.appUtility = appUtility;
	}

	public Document getSpecimenMarkers(Document document, List<QisLabelInformation> infoList,
			QisTransactionLabelRequest labelRequest) throws DocumentException, IOException {
		Certificate certificate = new Certificate(appUtility);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.BOX);
		cell.setBorderColor(Color.BLACK);
		cell.setFixedHeight(11);
		cell.setUseAscender(true);
		cell.setUseDescender(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_TOP);

		// RESULTS
		PdfPTable tableReport = new PdfPTable(3);
		tableReport.setWidthPercentage(100f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(20);

		PdfPTable specimenLeft = new PdfPTable(new float[] { 6, 6, 4, 4 });
		specimenLeft.setWidthPercentage(100f);
		specimenLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable specimenMiddle = new PdfPTable(new float[] { 6, 6, 4, 4 });
		specimenMiddle.setWidthPercentage(100f);
		specimenMiddle.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable specimenRight = new PdfPTable(new float[] { 6, 6, 4, 4 });
		specimenRight.setWidthPercentage(100f);
		specimenRight.setHorizontalAlignment(Element.ALIGN_LEFT);

		Font fontAppName = FontFactory.getFont("GARAMOND");
		fontAppName.setSize(8);
		fontAppName.setColor(Color.BLACK);

		Font fontNumber = FontFactory.getFont("GARAMOND_BOLD");
		fontNumber.setSize(22);
		fontNumber.setColor(Color.BLACK);

		Font fontInfo = FontFactory.getFont("GARAMOND_BOLD");
		fontInfo.setSize(8);
		fontInfo.setColor(Color.BLACK);

		int count = 0;
		boolean resetPage = false;
		for (QisLabelInformation info : infoList) {
			PdfPTable xrayTable = null;

			if (count % 3 == 0) { // LEFT
				xrayTable = specimenLeft;
			} else if (count % 3 == 1) { // MIDDLE
				xrayTable = specimenMiddle;
			} else if (count % 3 == 2) { // RIGHT
				xrayTable = specimenRight;
			}

			if (xrayTable != null) {
				cell.setColspan(4);
				cell.setBorder(Rectangle.NO_BORDER);

				if (count >= 3) {
					certificate.addToTable(xrayTable, cell, "", fontAppName, Element.ALIGN_LEFT);
				}
				cell.setBorder(Rectangle.BOX);
				certificate.addToTable(xrayTable, cell, applicationName, fontAppName, Element.ALIGN_CENTER);
				cell.setColspan(2);
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				certificate.addToTable(xrayTable, cell, info.getFullname(), fontInfo, Element.ALIGN_LEFT);
				cell.setRowspan(2);
				certificate.addToTable(xrayTable, cell, String.valueOf(info.getId()), fontNumber, Element.ALIGN_RIGHT);
				cell.setRowspan(1);
				certificate.addToTable(xrayTable, cell, info.getAgeGender(), fontInfo, Element.ALIGN_LEFT);
				certificate.addToTable(xrayTable, cell, info.getBiller(), fontInfo, Element.ALIGN_LEFT);
				certificate.addToTable(xrayTable, cell, info.getDate(), fontInfo, Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOX);
				certificate.addToTable(xrayTable, cell, "", fontInfo, Element.ALIGN_LEFT);
				certificate.addToTable(xrayTable, cell, labelRequest.getMedTech(), fontInfo, Element.ALIGN_CENTER);
			}

			count++;

			if (count % 33 == 0) {
				resetPage = true;
			}

			if (resetPage) {
				tableReport.addCell(specimenLeft);
				tableReport.addCell(specimenMiddle);
				tableReport.addCell(specimenRight);
				document.add(tableReport);
				document.newPage();

				specimenLeft.deleteBodyRows();
				specimenMiddle.deleteBodyRows();
				specimenRight.deleteBodyRows();
				tableReport.deleteBodyRows();
				count = 0;
				resetPage = false;
			}
		}

		cell.setColspan(4);
		cell.setBorder(Rectangle.NO_BORDER);
		if (count % 3 == 1) {
			certificate.addToTable(specimenMiddle, cell, "", fontAppName, Element.ALIGN_LEFT);
			certificate.addToTable(specimenRight, cell, "", fontAppName, Element.ALIGN_LEFT);
		} else if (count % 3 == 2) {
			certificate.addToTable(specimenRight, cell, "", fontAppName, Element.ALIGN_LEFT);
		}

		tableReport.addCell(specimenLeft);
		tableReport.addCell(specimenMiddle);
		tableReport.addCell(specimenRight);

		document.add(tableReport);

		return document;
	}

	public Document getXRayMarkers(Document document, List<QisLabelInformation> infoList,
			QisTransactionLabelRequest labelRequest, boolean withSpace) throws DocumentException, IOException {

		Certificate certificate = new Certificate(appUtility);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.BOX);
		cell.setNoWrap(false);
		cell.setBorderColor(Color.BLACK);
		cell.setFixedHeight(14);
		cell.setUseAscender(true);
		cell.setUseDescender(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		// RESULTS
		PdfPTable tableReport = new PdfPTable(2);
		tableReport.setWidthPercentage(100f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(20);

		PdfPTable xrayLeft = new PdfPTable(new float[] { 5, 5, 4, 4 });
		xrayLeft.setWidthPercentage(100f);
		xrayLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable xrayRight = new PdfPTable(new float[] { 5, 5, 4, 4 });
		xrayRight.setWidthPercentage(100f);
		xrayRight.setHorizontalAlignment(Element.ALIGN_LEFT);

		Font fontAppName = FontFactory.getFont("GARAMOND");
		fontAppName.setSize(12);
		fontAppName.setColor(Color.BLACK);

		Font fontNumber = FontFactory.getFont("GARAMOND_BOLD");
		fontNumber.setSize(40);
		fontNumber.setColor(Color.BLACK);

		Font fontInfo = FontFactory.getFont("GARAMOND_BOLD");
		fontInfo.setSize(10);
		fontInfo.setColor(Color.BLACK);

		Font fontName = FontFactory.getFont("GARAMOND_BOLD");
		fontName.setSize(10);
		fontName.setColor(Color.BLACK);
		
		int count = 0;
		boolean resetPage = false;
		for (QisLabelInformation info : infoList) {
			if(!info.isWithXRay()) {
				continue;
			}
			PdfPTable xrayTable = null;

			if (count % 2 == 0) { // LEFT
				xrayTable = xrayLeft;
			} else if (count % 2 == 1) { // RIGHT
				xrayTable = xrayRight;
			}

			if (xrayTable != null) {
				cell.setColspan(4);
				cell.setBorder(Rectangle.NO_BORDER);

				if (count >= 2) {
					certificate.addToTable(xrayTable, cell, "", fontAppName, Element.ALIGN_LEFT);
				}

				if (withSpace) {
					certificate.addToTable(xrayTable, cell, "", fontAppName, Element.ALIGN_LEFT);
					certificate.addToTable(xrayTable, cell, "", fontAppName, Element.ALIGN_LEFT);
					certificate.addToTable(xrayTable, cell, "", fontAppName, Element.ALIGN_LEFT);
				}

				cell.setBorder(Rectangle.BOX);
				certificate.addToTable(xrayTable, cell, applicationName, fontAppName, Element.ALIGN_CENTER);
				cell.setColspan(2);
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				cell.setFixedHeight(25);
				certificate.addToTable(xrayTable, cell, info.getFullname(), fontInfo, Element.ALIGN_LEFT);
				cell.setRowspan(3);
				cell.setFixedHeight(14);
				cell.setBorder(Rectangle.BOX);
				certificate.addToTable(xrayTable, cell, String.valueOf(info.getId()), fontNumber, Element.ALIGN_CENTER);
				cell.setRowspan(1);
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				certificate.addToTable(xrayTable, cell, info.getAgeGender(), fontInfo, Element.ALIGN_LEFT);
				certificate.addToTable(xrayTable, cell, info.getBiller(), fontInfo, Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOX);
				certificate.addToTable(xrayTable, cell, labelRequest.getXrayType(), fontInfo, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(xrayTable, cell, labelRequest.getRadTech(), fontName, Element.ALIGN_CENTER);
				certificate.addToTable(xrayTable, cell, info.getDate(), fontName, Element.ALIGN_CENTER);
			}

			count++;

			if (withSpace) {
				if (count % 10 == 0) {
					resetPage = true;
				}
			} else {
				if (count % 14 == 0) {
					resetPage = true;
				}
			}

			if (resetPage) {
				tableReport.addCell(xrayLeft);
				tableReport.addCell(xrayRight);
				document.add(tableReport);
				document.newPage();

				xrayLeft.deleteBodyRows();
				xrayRight.deleteBodyRows();
				tableReport.deleteBodyRows();
				count = 0;
				resetPage = false;
			}
		}

		if (count % 2 == 1) {
			cell.setColspan(4);
			cell.setBorder(Rectangle.NO_BORDER);
			certificate.addToTable(xrayRight, cell, "", fontAppName, Element.ALIGN_LEFT);
		}

		tableReport.addCell(xrayLeft);
		tableReport.addCell(xrayRight);

		document.add(tableReport);

		return document;
	}
}
