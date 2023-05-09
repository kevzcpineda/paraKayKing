package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOA;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOP;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;

public class ReferenceSOP {
	private String applicationName;
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private final String PAYTO = "QUESTDIAGNOSTICS, INC.";

	public ReferenceSOP(String applicationName, String applicationHeader, String applicationFooter,
			AppUtility appUtility) {
		super();
		this.applicationName = applicationName;
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getChargeToSOA(Document document, PdfWriter pdfWriter, QisSOP sop, boolean withHeaderFooter)
			throws DocumentException, IOException {

		Certificate certificate = new Certificate(appUtility);
		printSOP(document, pdfWriter, sop, certificate, withHeaderFooter);

		return document;
	}

	private void printSOP(Document document, PdfWriter pdfWriter, QisSOP sop, Certificate certificate,
			boolean withHeaderFooter) {

		QisHeaderFooterImageEvent event = new QisHeaderFooterImageEvent(applicationHeader, applicationFooter,
				certificate, withHeaderFooter);
		pdfWriter.setPageEvent(event);

		document.setMargins(10, 10, 10, 30);
		document.open();

		Font fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(12);
		fontTitle.setColor(Color.BLACK);

		Font fontTable = FontFactory.getFont("GARAMOND_BOLD");
		fontTable.setSize(10);
		fontTable.setColor(Color.BLACK);

		Font fontData = FontFactory.getFont("GARAMOND");
		fontData.setSize(10);
		fontData.setColor(Color.BLACK);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setUseAscender(true);
		cell.setUseDescender(true);

		PdfPTable soaTable = new PdfPTable(new float[] { 3, 2, 6, 6, 3, 3 });
		soaTable.setSpacingBefore(5);
		soaTable.setWidthPercentage(100f);
		soaTable.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell.setColspan(6);
		certificate.addToTable(soaTable, cell, applicationName, fontTitle, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "STATEMENT OF ACCOUNT", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, sop.getSopNumber(), fontTable, Element.ALIGN_RIGHT);

		QisReferenceLaboratory reference = sop.getReferenceLab();
		cell.setColspan(3);
		certificate.addToTable(soaTable, cell, "NAME OF COMPANY : " + reference.getName(), fontTable,
				Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "PO NUMBER : " + sop.getPurchaseOrder(), fontTable, Element.ALIGN_RIGHT);
		certificate.addToTable(soaTable, cell,
				"ADDRESS : " + (reference.getAddress() != null ? reference.getAddress() : ""), fontTable,
				Element.ALIGN_LEFT);
		String coverage = "DATE OF COVERAGE : "
				+ appUtility.calendarToFormatedDate(sop.getCoverageDateFrom(), "MMM dd, yyyy") + " to "
				+ appUtility.calendarToFormatedDate(sop.getCoverageDateTo(), "MMM dd, yyyy");
		certificate.addToTable(soaTable, cell, coverage, fontTable, Element.ALIGN_RIGHT);
		String attention = "ATTENTION : " + (reference.getContactPerson() != null ? reference.getContactPerson() : "");
		certificate.addToTable(soaTable, cell, attention, fontTable, Element.ALIGN_LEFT);
		String statementDate = "DATE OF STATEMENT : "
				+ appUtility.calendarToFormatedDate(sop.getStatementDate(), "MMM dd, yyyy");
		certificate.addToTable(soaTable, cell, statementDate, fontTable, Element.ALIGN_RIGHT);
		cell.setColspan(6);
		cell.setFixedHeight(10);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_RIGHT);
		cell.setFixedHeight(0);
		cell.setColspan(0);

		cell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.BOTTOM);
		certificate.addToTable(soaTable, cell, "Date", fontTable, Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
		certificate.addToTable(soaTable, cell, "Receipt #", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "Full Name", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "PROCEDURE", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "SUBTOTAL", fontTable, Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
		certificate.addToTable(soaTable, cell, "TOTAL", fontTable, Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);

		List<QisTransactionLaboratories> sortedList = new ArrayList<QisTransactionLaboratories>(sop.getTransactions());
		int txnUnpaid = 0;
		double totalUnpaid = 0;
		double subTotalUnpaid = 0;
		for (QisTransactionLaboratories data : sortedList) {

			txnUnpaid++;
			List<QisTransactionLaboratoryInfo> sortedItems = new ArrayList<QisTransactionLaboratoryInfo>(
					data.getTransactionLabRequests());
			double totalMolePrice = 0.0d;
			if (sortedItems.size() > 0) {
				for (QisTransactionLaboratoryInfo item : sortedItems) {
					if (item.getReferenceLab() != null) {
						if (item.getReferenceLab().getId() == sop.getReferenceId()) {
							totalMolePrice += item.getMolePriceItem();
						}
					}
				}
			}

			certificate.addToTable(soaTable, cell,
					appUtility.calendarToFormatedDate(data.getTransactionDate(), "yyyy-MM-dd HH:mm"), fontData,
					Element.ALIGN_CENTER);
			certificate.addToTable(soaTable, cell, appUtility.numberFormat(data.getId(), "000000"), fontData,
					Element.ALIGN_CENTER);
			certificate.addToTable(soaTable, cell, appUtility.getPatientFullname(data.getPatient()), fontData,
					Element.ALIGN_LEFT);

			for (int j = 0; j <= sortedItems.size(); j++) {
				QisTransactionLaboratoryInfo itm = sortedItems.get(j);
				if (itm.getReferenceLab() != null) {
					sortedItems.remove(j);
					certificate.addToTable(soaTable, cell,
							appUtility.getTransactionItemDescription(itm.getTransactionItem()), fontData,
							Element.ALIGN_LEFT);

					certificate.addToTable(soaTable, cell,
							appUtility.amountFormat(itm.getMolePriceItem(), "###,###,##0.00"), fontData,
							Element.ALIGN_RIGHT);

					certificate.addToTable(soaTable, cell, appUtility.amountFormat(totalMolePrice, "###,###,##0.00"),
							fontData, Element.ALIGN_RIGHT);
					break;
				}
			}

			if (sortedItems.size() > 0) {
				for (QisTransactionLaboratoryInfo itm : sortedItems) {
					certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_CENTER);
					certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_CENTER);
					certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
					certificate.addToTable(soaTable, cell,
							appUtility.getTransactionItemDescription(itm.getTransactionItem()), fontData,
							Element.ALIGN_LEFT);
					certificate.addToTable(soaTable, cell,
							appUtility.amountFormat(itm.getMolePriceItem(), "###,###,##0.00"), fontData,
							Element.ALIGN_RIGHT);
					certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_CENTER);
				}
			}
		}
		if (txnUnpaid != 0) {
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.TOP);
			certificate.addToTable(soaTable, cell, "Total Unpaid", fontTable, Element.ALIGN_LEFT);
			certificate.addToTable(soaTable, cell, appUtility.amountFormat(subTotalUnpaid, "###,###,##0.00"), fontTable,
					Element.ALIGN_RIGHT);
			certificate.addToTable(soaTable, cell, appUtility.amountFormat(totalUnpaid, "###,###,##0.00"), fontTable,
					Element.ALIGN_RIGHT);
			cell.setBorder(Rectangle.NO_BORDER);
		}

		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "Grand Total (NET OF TAXES)", fontTable, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, appUtility.amountFormat(subTotalUnpaid, "###,###,##0.00"), fontTable,
				Element.ALIGN_RIGHT);
		certificate.addToTable(soaTable, cell, appUtility.amountFormat(totalUnpaid, "###,###,##0.00"), fontTable,
				Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);

//		certificate.addToTable(soaTable, cell, "TOTAL (NET OF TAX) ", fontTable, Element.ALIGN_LEFT);

		addReminder(cell, certificate, soaTable, fontData, 6);

		document.add(soaTable);

		document.close();

	}

	private void addReminder(PdfPCell cell, Certificate certificate, PdfPTable soaTable, Font fontData, int columns) {
		cell.setColspan(columns);
		cell.setFixedHeight(10);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setFixedHeight(0);
		certificate.addToTable(soaTable, cell, "Please note that payment is made by " + PAYTO, fontData, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell,
				"Kindly examine your statement of account immediately upon receipt. If no error", fontData,
				Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "is reported within 7 days, the account will be considered correct.",
				fontData, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "For question and billing inquiries, please call us at 0917-6260911.",
				fontData, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "*SOA electronically signed out no need adding signature in SOA*",
				fontData, Element.ALIGN_LEFT);
		cell.setFixedHeight(10);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setFixedHeight(0);
	}
}
