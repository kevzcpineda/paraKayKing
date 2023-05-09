package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
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
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOP;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;

public class ReferenceToSOP {
	private String applicationName;
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private final String PAYTO = "QUESTDIAGNOSTICS, INC.";

	public ReferenceToSOP(String applicationName, String applicationHeader, String applicationFooter,
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

		PdfPTable sopTable = new PdfPTable(new float[] { 3, 2, 6, 6, 3, 3 });
		sopTable.setSpacingBefore(5);
		sopTable.setWidthPercentage(100f);
		sopTable.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell.setColspan(6);
		certificate.addToTable(sopTable, cell, applicationName, fontTitle, Element.ALIGN_CENTER);
		certificate.addToTable(sopTable, cell, "STATEMENT OF PAYABLE", fontTable, Element.ALIGN_CENTER);

		QisReferenceLaboratory reference = sop.getReferenceLab();
		cell.setColspan(3);
		certificate.addToTable(sopTable, cell, "NAME OF REFERENCE LABORATORY : " + reference.getName(), fontTable,
				Element.ALIGN_LEFT);
		certificate.addToTable(sopTable, cell, sop.getSopNumber(), fontTable, Element.ALIGN_RIGHT);
		certificate.addToTable(sopTable, cell,
				"ADDRESS : " + (reference.getAddress() != null ? reference.getAddress() : ""), fontTable,
				Element.ALIGN_LEFT);
		String coverage = "DATE OF COVERAGE : "
				+ appUtility.calendarToFormatedDate(sop.getCoverageDateFrom(), "MMM dd, yyyy") + " to "
				+ appUtility.calendarToFormatedDate(sop.getCoverageDateTo(), "MMM dd, yyyy");
		certificate.addToTable(sopTable, cell, coverage, fontTable, Element.ALIGN_RIGHT);
		String attention = "ATTENTION : " + (reference.getContactPerson() != null ? reference.getContactPerson() : "");
		certificate.addToTable(sopTable, cell, attention, fontTable, Element.ALIGN_LEFT);
		String statementDate = "DATE OF STATEMENT : "
				+ appUtility.calendarToFormatedDate(sop.getStatementDate(), "MMM dd, yyyy");
		certificate.addToTable(sopTable, cell, statementDate, fontTable, Element.ALIGN_RIGHT);
		cell.setColspan(6);
		cell.setFixedHeight(10);
		certificate.addToTable(sopTable, cell, "", fontTable, Element.ALIGN_RIGHT);
		cell.setFixedHeight(0);
		cell.setColspan(0);

		cell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.BOTTOM);
		certificate.addToTable(sopTable, cell, "Date", fontTable, Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
		certificate.addToTable(sopTable, cell, "Receipt #", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(sopTable, cell, "Full Name", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(sopTable, cell, "PROCEDURE", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(sopTable, cell, "SUBTOTAL", fontTable, Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
		certificate.addToTable(sopTable, cell, "TOTAL", fontTable, Element.ALIGN_CENTER);
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

			QisTransactionLaboratoryInfo item = sortedItems.get(0);

			certificate.addToTable(sopTable, cell,
					appUtility.calendarToFormatedDate(data.getTransactionDate(), "yyyy-MM-dd HH:mm"), fontData,
					Element.ALIGN_CENTER);
			certificate.addToTable(sopTable, cell, appUtility.numberFormat(data.getId(), "000000"), fontData,
					Element.ALIGN_CENTER);
			certificate.addToTable(sopTable, cell, appUtility.getPatientFullname(data.getPatient()), fontData,
					Element.ALIGN_LEFT);
			

			for (int j = 0; j <= sortedItems.size(); j++) {
				QisTransactionLaboratoryInfo itm = sortedItems.get(j);
				if (itm.getReferenceLab() != null) {
					sortedItems.remove(j);
					certificate.addToTable(sopTable, cell,
							appUtility.getTransactionItemDescription(itm.getTransactionItem()), fontData,
							Element.ALIGN_LEFT);
					certificate.addToTable(sopTable, cell,
							appUtility.amountFormat(itm.getMolePriceItem(), "###,###,##0.00"), fontData,
							Element.ALIGN_RIGHT);
					break;
				}
			}
			certificate.addToTable(sopTable, cell, appUtility.amountFormat(totalMolePrice, "###,###,##0.00"), fontData,
					Element.ALIGN_RIGHT);

			subTotalUnpaid += totalMolePrice;
			totalUnpaid += totalMolePrice;
			if (sortedItems.size() > 0) {
				for (QisTransactionLaboratoryInfo itm : sortedItems) {
					if (item.getReferenceLab() != null) {
						if (item.getReferenceLab().getId() == sop.getReferenceId()) {
							if (!"PCK".equals(itm.getTransactionItem().getItemType())) {								
								certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_CENTER);
								certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_CENTER);
								certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);
								certificate.addToTable(sopTable, cell,
										appUtility.getTransactionItemDescription(itm.getTransactionItem()), fontData,
										Element.ALIGN_LEFT);
								certificate.addToTable(sopTable, cell,
										appUtility.amountFormat(itm.getMolePriceItem(), "###,###,##0.00"), fontData,
										Element.ALIGN_RIGHT);
								certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_RIGHT);
							}

						}
					}
				}
			}
		}
		if (txnUnpaid != 0) {
			certificate.addToTable(sopTable, cell, "", fontTable, Element.ALIGN_CENTER);
			certificate.addToTable(sopTable, cell, "", fontTable, Element.ALIGN_CENTER);
			certificate.addToTable(sopTable, cell, "", fontTable, Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.TOP);
			certificate.addToTable(sopTable, cell, "Grand Total", fontTable, Element.ALIGN_LEFT);
			certificate.addToTable(sopTable, cell, appUtility.amountFormat(subTotalUnpaid, "###,###,##0.00"), fontTable,
					Element.ALIGN_RIGHT);
			certificate.addToTable(sopTable, cell, appUtility.amountFormat(totalUnpaid, "###,###,##0.00"), fontTable,
					Element.ALIGN_RIGHT);
			cell.setBorder(Rectangle.NO_BORDER);
		}

		addReminder(cell, certificate, sopTable, fontData, 6);

		cell.setColspan(2);
		cell.setFixedHeight(15);
		certificate.addToTable(sopTable, cell, "Prepared by:", fontData, Element.ALIGN_LEFT);
		certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);
		certificate.addToTable(sopTable, cell, "Noted by:", fontData, Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(2);
//		cell.setBorder(Rectangle.TOP);
		certificate.addToTable(sopTable, cell, appUtility.getUserPersonelFullName(sop.getPreparedUser()), fontTable,
				Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);
//		cell.setBorder(Rectangle.TOP);
		certificate.addToTable(sopTable, cell, appUtility.getUserPersonelFullName(sop.getNotedUser()), fontTable,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(sopTable, cell, "Management Trainee", fontData, Element.ALIGN_LEFT);
		certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);
		certificate.addToTable(sopTable, cell, "President", fontData, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setColspan(2);
		certificate.addToTable(sopTable, cell, PAYTO, fontData, Element.ALIGN_LEFT);

		certificate.addToTable(sopTable, cell, "Verified by:", fontData, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(sopTable, cell, appUtility.getUserPersonelFullName(sop.getVerifiedUser()), fontTable,
				Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(sopTable, cell, "Finance Officer", fontData, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setFixedHeight(0);

		document.add(sopTable);

		document.close();
	}

	private void addReminder(PdfPCell cell, Certificate certificate, PdfPTable sopTable, Font fontData, int columns) {
		cell.setColspan(columns);
		cell.setFixedHeight(10);
		certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setFixedHeight(0);
		certificate.addToTable(sopTable, cell, "Please make check payable to " + PAYTO, fontData, Element.ALIGN_LEFT);
		certificate.addToTable(sopTable, cell,
				"Kindly examine your statement of payable immediately upon receipt. If no error", fontData,
				Element.ALIGN_LEFT);
		certificate.addToTable(sopTable, cell, "is reported within 7 days, the payable will be considered correct.",
				fontData, Element.ALIGN_LEFT);
		certificate.addToTable(sopTable, cell, "For question and billing inquiries, please call us at 0917-6260911.",
				fontData, Element.ALIGN_LEFT);
		certificate.addToTable(sopTable, cell, "*SOP electronically signed out no need adding signature in SOP*",
				fontData, Element.ALIGN_LEFT);
		cell.setFixedHeight(10);
		certificate.addToTable(sopTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setFixedHeight(0);

	}
}
