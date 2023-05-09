package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOA;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionPayment;

public class ChargeToSOA {
	private String applicationName;
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private final String PAYTO = "QUESTDIAGNOSTICS, INC.";

	public ChargeToSOA(String applicationName, String applicationHeader, String applicationFooter,
			AppUtility appUtility) {
		super();
		this.applicationName = applicationName;
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getChargeToSOA(Document document, PdfWriter pdfWriter, QisSOA soa, boolean withHeaderFooter,
			Boolean withRunningBalance, List<QisSOA> soaList) throws DocumentException, IOException {

		Certificate certificate = new Certificate(appUtility);

		if ("HMO".equals(soa.getChargeTo().getChargeType())) {
			printHMOSOA(document, pdfWriter, soa, certificate, withHeaderFooter);
		} else {
			printSOA(document, pdfWriter, soa, certificate, withHeaderFooter, withRunningBalance, soaList);
		}

		return document;
	}

	public void printHMOSOA(Document document, PdfWriter pdfWriter, QisSOA soa, Certificate certificate,
			boolean withHeaderFooter) throws BadElementException, IOException {
		QisHeaderFooterImageEvent event = new QisHeaderFooterImageEvent(applicationHeader, applicationFooter,
				certificate, withHeaderFooter);
		pdfWriter.setPageEvent(event);

		document.setPageSize(PageSize.LETTER.rotate());
		document.setMargins(10, 20, 10, 30);
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

		PdfPTable soaTable = new PdfPTable(new float[] { 3, 2, 5, 3, 3, 4, 3, 5, 3 });
		soaTable.setSpacingBefore(5);
		soaTable.setWidthPercentage(100f);
		soaTable.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell.setColspan(9);
		certificate.addToTable(soaTable, cell, applicationName, fontTitle, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "STATEMENT OF ACCOUNT", fontTable, Element.ALIGN_CENTER);

		QisCorporate chargeTo = soa.getChargeTo();
		cell.setColspan(5);
		certificate.addToTable(soaTable, cell, "NAME OF COMPANY : " + chargeTo.getCompanyName(), fontTable,
				Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(soaTable, cell, soa.getSoaNumber(), fontTable, Element.ALIGN_RIGHT);

		cell.setColspan(5);
		certificate.addToTable(soaTable, cell,
				"ADDRESS : " + (chargeTo.getCompanyAddress() != null ? chargeTo.getCompanyAddress() : ""), fontTable,
				Element.ALIGN_LEFT);
		cell.setColspan(4);
		String statementDate = "DATE OF STATEMENT : "
				+ appUtility.calendarToFormatedDate(soa.getStatementDate(), "MMM dd, yyyy");
		certificate.addToTable(soaTable, cell, statementDate, fontTable, Element.ALIGN_RIGHT);

		cell.setColspan(9);
		String attention = "ATTENTION : " + (chargeTo.getContactPerson() != null ? chargeTo.getContactPerson() : "");
		certificate.addToTable(soaTable, cell, attention, fontTable, Element.ALIGN_LEFT);

		cell.setFixedHeight(10);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_RIGHT);
		cell.setFixedHeight(0);
		cell.setColspan(0);

		cell.setBorder(Rectangle.BOX);
		certificate.addToTable(soaTable, cell, "Date", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "Receipt #", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "Full Name", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "Company", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "LOE", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "Acc. Number", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "Approval Code", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "Procedure", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "Total", fontTable, Element.ALIGN_CENTER);

		List<QisTransaction> sortedList = new ArrayList<QisTransaction>(soa.getTransactions());
		Collections.sort(sortedList);

		cell.setBorder(Rectangle.NO_BORDER);
		cell.setCellEvent(new DottedCell(PdfPCell.BOX));
		int txnUnpaid = 0;
		double totalUnpaid = 0;
		for (QisTransaction data : sortedList) {
			if (data.isSoaStatus() == false) {
				String procedure = "";
				txnUnpaid++;
				List<QisTransactionItem> sortedItems = new ArrayList<QisTransactionItem>(data.getTransactionItems());
				Collections.sort(sortedItems);
				for (QisTransactionItem itm : sortedItems) {
					if ("".equals(procedure)) {
						procedure = appUtility.getTransactionItemDescription(itm);
					} else {
						procedure += ", " + appUtility.getTransactionItemDescription(itm);
					}
				}

				String company = "";
				if (data.getPatient().getCorporate() != null) {
					company = data.getPatient().getCorporate().getCompanyName();
				}

				List<QisTransactionPayment> payments = new ArrayList<QisTransactionPayment>(
						data.getTransactionPayments());
				QisTransactionPayment pay = payments.get(0);
				String loe = pay.getHmoLOE() != null ? pay.getHmoLOE() : "";
				String accntNo = pay.getHmoAccountNumber() != null ? pay.getHmoAccountNumber() : "";
				String approvalCode = pay.getHmoApprovalCode() != null ? pay.getHmoApprovalCode() : "";

				certificate.addToTable(soaTable, cell,
						appUtility.calendarToFormatedDate(data.getTransactionDate(), "yyyy-MM-dd HH:mm"), fontData,
						Element.ALIGN_CENTER);
				certificate.addToTable(soaTable, cell, appUtility.numberFormat(data.getId(), "000000"), fontData,
						Element.ALIGN_CENTER);
				certificate.addToTable(soaTable, cell, appUtility.getPatientFullname(data.getPatient()), fontData,
						Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, company, fontData, Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, loe, fontData, Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, accntNo, fontData, Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, approvalCode, fontData, Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, procedure, fontData, Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell,
						appUtility.amountFormat(data.getTotalItemAmountDue(), "###,###,##0.00"), fontData,
						Element.ALIGN_RIGHT);

				totalUnpaid += data.getTotalItemAmountDue();
			}
		}

		if (txnUnpaid != 0) {
			cell.setCellEvent(null);
			cell.setColspan(8);
			certificate.addToTable(soaTable, cell, "Total Unpaid", fontTable, Element.ALIGN_RIGHT);
			cell.setColspan(1);
			certificate.addToTable(soaTable, cell, appUtility.amountFormat(totalUnpaid, "###,###,##0.00"), fontTable,
					Element.ALIGN_RIGHT);

			cell.setColspan(9);
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_RIGHT);
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_RIGHT);
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_RIGHT);
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_RIGHT);
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_RIGHT);
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_RIGHT);
		}
		cell.setColspan(0);
		int txnPaid = 0;
		double totalPaid = 0;
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setCellEvent(new DottedCell(PdfPCell.BOX));
		for (QisTransaction data : sortedList) {
			if (data.isSoaStatus() == true) {
				String procedure = "";
				txnPaid++;
				List<QisTransactionItem> sortedItems = new ArrayList<QisTransactionItem>(data.getTransactionItems());
				Collections.sort(sortedItems);
				for (QisTransactionItem itm : sortedItems) {
					if ("".equals(procedure)) {
						procedure = appUtility.getTransactionItemDescription(itm);
					} else {
						procedure += ", " + appUtility.getTransactionItemDescription(itm);
					}
				}

				String company = "";
				if (data.getPatient().getCorporate() != null) {
					company = data.getPatient().getCorporate().getCompanyName();
				}

				List<QisTransactionPayment> payments = new ArrayList<QisTransactionPayment>(
						data.getTransactionPayments());
				QisTransactionPayment pay = payments.get(0);
				String loe = pay.getHmoLOE() != null ? pay.getHmoLOE() : "";
				String accntNo = pay.getHmoAccountNumber() != null ? pay.getHmoAccountNumber() : "";
				String approvalCode = pay.getHmoApprovalCode() != null ? pay.getHmoApprovalCode() : "";

				certificate.addToTable(soaTable, cell,
						appUtility.calendarToFormatedDate(data.getTransactionDate(), "yyyy-MM-dd HH:mm"), fontData,
						Element.ALIGN_CENTER);
				certificate.addToTable(soaTable, cell, appUtility.numberFormat(data.getId(), "000000"), fontData,
						Element.ALIGN_CENTER);
				certificate.addToTable(soaTable, cell, appUtility.getPatientFullname(data.getPatient()), fontData,
						Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, company, fontData, Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, loe, fontData, Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, accntNo, fontData, Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, approvalCode, fontData, Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, procedure, fontData, Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell,
						appUtility.amountFormat(data.getTotalItemAmountDue(), "###,###,##0.00"), fontData,
						Element.ALIGN_RIGHT);

				totalPaid += data.getTotalItemAmountDue();
			}
		}

		if (txnPaid != 0) {
			cell.setColspan(8);
			certificate.addToTable(soaTable, cell, "Total Paid", fontTable, Element.ALIGN_RIGHT);
			cell.setColspan(1);
			certificate.addToTable(soaTable, cell, appUtility.amountFormat(totalPaid, "###,###,##0.00"), fontTable,
					Element.ALIGN_RIGHT);
		}

		cell.setCellEvent(null);

		cell.setColspan(9);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_RIGHT);
		cell.setColspan(8);
		certificate.addToTable(soaTable, cell, "Grand Total (NET OF TAXES)", fontTable, Element.ALIGN_RIGHT);
		cell.setColspan(1);
		certificate.addToTable(soaTable, cell, appUtility.amountFormat(totalPaid + totalUnpaid, "###,###,##0.00"),
				fontTable, Element.ALIGN_RIGHT);

		addReminder(cell, certificate, soaTable, fontData, 9);

		cell.setFixedHeight(15);
		cell.setColspan(2);
		certificate.addToTable(soaTable, cell, "Prepared by:", fontData, Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setColspan(2);
		certificate.addToTable(soaTable, cell, "Verified by:", fontData, Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setColspan(2);
		certificate.addToTable(soaTable, cell, "Noted by:", fontData, Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(9);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(2);
		cell.setBorder(Rectangle.TOP);
		certificate.addToTable(soaTable, cell, appUtility.getUserPersonelFullName(soa.getPreparedUser()), fontTable,
				Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(Rectangle.TOP);
		certificate.addToTable(soaTable, cell, appUtility.getUserPersonelFullName(soa.getVerifiedUser()), fontTable,
				Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(Rectangle.TOP);
		certificate.addToTable(soaTable, cell, appUtility.getUserPersonelFullName(soa.getNotedUser()), fontTable,
				Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(soaTable, cell, "Management Trainee", fontData, Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setColspan(2);
		certificate.addToTable(soaTable, cell, "Finance Officer", fontData, Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setColspan(2);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setColspan(2);
		certificate.addToTable(soaTable, cell, PAYTO, fontData, Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);

		document.add(soaTable);

		document.close();
	}

	public void printSOA(Document document, PdfWriter pdfWriter, QisSOA soa, Certificate certificate,
			boolean withHeaderFooter, Boolean withRunningBalance, List<QisSOA> soaList)
			throws BadElementException, IOException {

		QisHeaderFooterImageEvent event = new QisHeaderFooterImageEvent(applicationHeader, applicationFooter,
				certificate, withHeaderFooter);
		pdfWriter.setPageEvent(event);

		document.setMargins(10, 35, 10, 30);
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
		certificate.addToTable(soaTable, cell, soa.getSoaNumber(), fontTable, Element.ALIGN_RIGHT);

		QisCorporate chargeTo = soa.getChargeTo();
		cell.setColspan(3);
		certificate.addToTable(soaTable, cell, "NAME OF COMPANY : " + chargeTo.getCompanyName(), fontTable,
				Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "PO NUMBER : " + soa.getPurchaseOrder(), fontTable, Element.ALIGN_RIGHT);
		certificate.addToTable(soaTable, cell,
				"ADDRESS : " + (chargeTo.getCompanyAddress() != null ? chargeTo.getCompanyAddress() : ""), fontTable,
				Element.ALIGN_LEFT);
		String coverage = "DATE OF COVERAGE : "
				+ appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), "MMM dd, yyyy") + " to "
				+ appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), "MMM dd, yyyy");
		certificate.addToTable(soaTable, cell, coverage, fontTable, Element.ALIGN_RIGHT);
		String attention = "ATTENTION : " + (chargeTo.getContactPerson() != null ? chargeTo.getContactPerson() : "");
		certificate.addToTable(soaTable, cell, attention, fontTable, Element.ALIGN_LEFT);
		String statementDate = "DATE OF STATEMENT : "
				+ appUtility.calendarToFormatedDate(soa.getStatementDate(), "MMM dd, yyyy");
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

		List<QisTransaction> sortedList = new ArrayList<QisTransaction>(soa.getTransactions());
		Collections.sort(sortedList);
		int txnUnpaid = 0;
		double totalUnpaid = 0;
		double subTotalUnpaid = 0;
		for (QisTransaction data : sortedList) {
			if (data.isSoaStatus() == false) {
				txnUnpaid++;
				List<QisTransactionItem> sortedItems = new ArrayList<QisTransactionItem>(data.getTransactionItems());
				Collections.sort(sortedItems);

				QisTransactionItem item = sortedItems.get(0);
				sortedItems.remove(0);

				totalUnpaid += data.getTotalItemAmountDue();
				subTotalUnpaid += item.getAmountDue();

				certificate.addToTable(soaTable, cell,
						appUtility.calendarToFormatedDate(data.getTransactionDate(), "yyyy-MM-dd HH:mm"), fontData,
						Element.ALIGN_CENTER);
				certificate.addToTable(soaTable, cell, appUtility.numberFormat(data.getId(), "000000"), fontData,
						Element.ALIGN_CENTER);
				certificate.addToTable(soaTable, cell, appUtility.getPatientFullname(data.getPatient()), fontData,
						Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, appUtility.getTransactionItemDescription(item), fontData,
						Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, appUtility.amountFormat(item.getAmountDue(), "###,###,##0.00"),
						fontData, Element.ALIGN_RIGHT);
				certificate.addToTable(soaTable, cell,
						appUtility.amountFormat(data.getTotalItemAmountDue(), "###,###,##0.00"), fontData,
						Element.ALIGN_RIGHT);

				if (sortedItems.size() > 0) {
					for (QisTransactionItem itm : sortedItems) {
						certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_CENTER);
						certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_CENTER);
						certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
						certificate.addToTable(soaTable, cell, appUtility.getTransactionItemDescription(itm), fontData,
								Element.ALIGN_LEFT);
						certificate.addToTable(soaTable, cell,
								appUtility.amountFormat(itm.getAmountDue(), "###,###,##0.00"), fontData,
								Element.ALIGN_RIGHT);
						certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_RIGHT);
						subTotalUnpaid += itm.getAmountDue();
					}
				}
			}
		}
		if (withRunningBalance) {
			if (txnUnpaid != 0) {
				certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
				certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
				certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.TOP);
				certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, appUtility.amountFormat(subTotalUnpaid, "###,###,##0.00"),
						fontTable, Element.ALIGN_RIGHT);
				certificate.addToTable(soaTable, cell, appUtility.amountFormat(totalUnpaid, "###,###,##0.00"),
						fontTable, Element.ALIGN_RIGHT);
				cell.setBorder(Rectangle.NO_BORDER);
			}

			for (QisSOA perSoa : soaList) {
				double totalSoa = 0d;
				if (soa.getSoaNumber() != perSoa.getSoaNumber()) {
					for (QisTransaction txn : perSoa.getTransactions()) {
						if (txn.isSoaStatus() == false) {
							totalSoa += txn.getTotalItemAmountDue();
						}
					}
					if (totalSoa != 0) {
						certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
						certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
						certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);
						certificate.addToTable(soaTable, cell, perSoa.getSoaNumber(), fontData, Element.ALIGN_LEFT);
						certificate.addToTable(soaTable, cell, appUtility.amountFormat(totalSoa, "###,###,##0.00"),
								fontData, Element.ALIGN_RIGHT);
						certificate.addToTable(soaTable, cell, appUtility.amountFormat(totalSoa, "###,###,##0.00"),
								fontData, Element.ALIGN_RIGHT);
						subTotalUnpaid += totalSoa;
						totalUnpaid += totalSoa;
					}
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

		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
		certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);

		int txnPaid = 0;
		double totalPaid = 0;
		double subTotalPaid = 0;
		for (QisTransaction data : sortedList) {
			if (data.isSoaStatus() == true) {
				txnPaid++;
				List<QisTransactionItem> sortedItems = new ArrayList<QisTransactionItem>(data.getTransactionItems());
				Collections.sort(sortedItems);

				QisTransactionItem item = sortedItems.get(0);
				sortedItems.remove(0);

				totalPaid += data.getTotalItemAmountDue();
				subTotalPaid += item.getAmountDue();

				certificate.addToTable(soaTable, cell,
						appUtility.calendarToFormatedDate(data.getTransactionDate(), "yyyy-MM-dd HH:mm"), fontData,
						Element.ALIGN_CENTER);
				certificate.addToTable(soaTable, cell, appUtility.numberFormat(data.getId(), "000000"), fontData,
						Element.ALIGN_CENTER);
				certificate.addToTable(soaTable, cell, appUtility.getPatientFullname(data.getPatient()), fontData,
						Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, appUtility.getTransactionItemDescription(item), fontData,
						Element.ALIGN_LEFT);
				certificate.addToTable(soaTable, cell, appUtility.amountFormat(item.getAmountDue(), "###,###,##0.00"),
						fontData, Element.ALIGN_RIGHT);
				certificate.addToTable(soaTable, cell,
						appUtility.amountFormat(data.getTotalItemAmountDue(), "###,###,##0.00"), fontData,
						Element.ALIGN_RIGHT);

				if (sortedItems.size() > 0) {
					for (QisTransactionItem itm : sortedItems) {
						certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_CENTER);
						certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_CENTER);
						certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
						certificate.addToTable(soaTable, cell, appUtility.getTransactionItemDescription(itm), fontData,
								Element.ALIGN_LEFT);
						certificate.addToTable(soaTable, cell,
								appUtility.amountFormat(itm.getAmountDue(), "###,###,##0.00"), fontData,
								Element.ALIGN_RIGHT);
						certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_RIGHT);
						subTotalPaid += itm.getAmountDue();
					}
				}
			}
		}

		if (txnPaid != 0) {
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_CENTER);
			certificate.addToTable(soaTable, cell, "", fontTable, Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.TOP);

			certificate.addToTable(soaTable, cell, "Total Paid", fontTable, Element.ALIGN_LEFT);
			certificate.addToTable(soaTable, cell, appUtility.amountFormat(subTotalPaid, "###,###,##0.00"), fontTable,
					Element.ALIGN_RIGHT);
			certificate.addToTable(soaTable, cell, appUtility.amountFormat(totalPaid, "###,###,##0.00"), fontTable,
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
		certificate.addToTable(soaTable, cell, appUtility.amountFormat(subTotalUnpaid + subTotalPaid, "###,###,##0.00"),
				fontTable, Element.ALIGN_RIGHT);
		certificate.addToTable(soaTable, cell, appUtility.amountFormat(totalUnpaid + subTotalPaid, "###,###,##0.00"),
				fontTable, Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);

//		certificate.addToTable(soaTable, cell, "TOTAL (NET OF TAX) ", fontTable, Element.ALIGN_LEFT);

		addReminder(cell, certificate, soaTable, fontData, 6);

		cell.setColspan(2);
		cell.setFixedHeight(15);
		certificate.addToTable(soaTable, cell, "Prepared by:", fontData, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "Noted by:", fontData, Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(2);
//		cell.setBorder(Rectangle.TOP);
		certificate.addToTable(soaTable, cell, appUtility.getUserPersonelFullName(soa.getPreparedUser()), fontTable,
				Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
//		cell.setBorder(Rectangle.TOP);
		certificate.addToTable(soaTable, cell, appUtility.getUserPersonelFullName(soa.getNotedUser()), fontTable,
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(soaTable, cell, "Management Trainee", fontData, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setColspan(2);
		certificate.addToTable(soaTable, cell, PAYTO, fontData, Element.ALIGN_LEFT);

		certificate.addToTable(soaTable, cell, "Verified by:", fontData, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(soaTable, cell, appUtility.getUserPersonelFullName(soa.getVerifiedUser()), fontTable,
				Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);

		cell.setColspan(2);
		certificate.addToTable(soaTable, cell, "Finance Officer", fontData, Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setFixedHeight(0);

		document.add(soaTable);

		document.close();
	}

	private void addReminder(PdfPCell cell, Certificate certificate, PdfPTable soaTable, Font fontData, int columns) {
		cell.setColspan(columns);
		cell.setFixedHeight(10);
		certificate.addToTable(soaTable, cell, "", fontData, Element.ALIGN_LEFT);
		cell.setFixedHeight(0);
		certificate.addToTable(soaTable, cell, "Please make check payable to " + PAYTO, fontData, Element.ALIGN_LEFT);
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

//SOA electronically signed out no need adding signature in SOA.
