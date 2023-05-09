package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPackage;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionPayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;
import quest.phil.diagnostic.information.system.ws.model.eod.Account;
import quest.phil.diagnostic.information.system.ws.model.eod.CashierEODData;
import quest.phil.diagnostic.information.system.ws.model.eod.Summary;

public class ReceiptDocument {
	private AppUtility appUtility;
	private String applicationName;
	private Font fontBold;
	private Font fontBoldRed;
	private Font fontNormalSmall;
	private Font fontTableContent;
	private Font fontTableTitle;

	private Font fontRemarks;
	private Font fontName;
	private Font fontNormal;

	public ReceiptDocument(String applicationName, AppUtility appUtility) {
		super();
		this.applicationName = applicationName;
		this.appUtility = appUtility;

		this.fontRemarks = FontFactory.getFont("GARAMOND_BOLD");
		this.fontRemarks.setSize(12);
		this.fontRemarks.setColor(Color.RED);

		this.fontName = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		this.fontName.setSize(10);
		this.fontName.setColor(Color.BLACK);

		this.fontNormal = FontFactory.getFont("GARAMOND");
		this.fontNormal.setSize(10);
		this.fontNormal.setColor(Color.BLACK);

		this.fontBold = FontFactory.getFont("GARAMOND_BOLD");
		this.fontBold.setSize(10);
		this.fontBold.setColor(Color.BLACK);

		this.fontBoldRed = FontFactory.getFont("GARAMOND_BOLD");
		this.fontBoldRed.setSize(10);
		this.fontBoldRed.setColor(Color.RED);

		this.fontNormalSmall = FontFactory.getFont("GARAMOND");
		this.fontNormalSmall.setSize(9);
		this.fontNormalSmall.setColor(Color.BLACK);

		this.fontTableContent = FontFactory.getFont("GARA");
		this.fontTableContent.setSize(8);
		this.fontTableContent.setColor(Color.BLACK);

		this.fontTableTitle = FontFactory.getFont("GARABD");
		this.fontTableTitle.setSize(8);
		this.fontTableTitle.setColor(Color.BLACK);
	}

	public Document getTransactionReceipt(Document document, PdfWriter pdfWriter, QisTransaction qisTransaction)
			throws DocumentException, IOException {
		document.open();

		// HEADER
		addTextToDocument(document, applicationName, fontBold);
		addTextToDocument(document, qisTransaction.getBranch().getBranchName(), fontNormalSmall);
		addTextToDocument(document, qisTransaction.getBranch().getAddress(), fontTableContent);

		// PATIENT
		PdfPTable patientTable = new PdfPTable(new float[] { 2, 4 });
		patientTable.setSpacingBefore(15);
		patientTable.setWidthPercentage(100f);
		patientTable.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);

		cell.setPhrase(new Phrase("Name:", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		patientTable.addCell(cell);

		QisPatient qisPatient = qisTransaction.getPatient();
		String patientName = appUtility.getPatientFullname(qisPatient);
		cell.setPhrase(new Phrase(patientName, fontName));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		patientTable.addCell(cell);

		if (qisTransaction.getDiscountType() != null) {
			if ("SCD".equals(qisTransaction.getDiscountType())) {
				cell.setPhrase(new Phrase("SC ID:", fontNormal));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				patientTable.addCell(cell);

				cell.setPhrase(new Phrase(qisTransaction.getSeniorCitizenID(), fontNormal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				patientTable.addCell(cell);
			} else if ("PWD".equals(qisTransaction.getDiscountType())) {
				cell.setPhrase(new Phrase("PWD ID:", fontNormal));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				patientTable.addCell(cell);

				cell.setPhrase(new Phrase(qisTransaction.getPwdID(), fontNormal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				patientTable.addCell(cell);
			}
		}

		if (qisTransaction.getBiller() != null) {
			cell.setPhrase(new Phrase("Company:", fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			patientTable.addCell(cell);

			String company = qisTransaction.getBiller();
			cell.setPhrase(new Phrase(company, fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			patientTable.addCell(cell);
		}

		cell.setPhrase(new Phrase("Age/Gen.:", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		patientTable.addCell(cell);

		String ageGender = "";
		Integer age = appUtility.calculateAgeInYear(qisPatient.getDateOfBirth(), qisTransaction.getTransactionDate());
		if (age != null) {
			ageGender = String.valueOf(age) + "/";
		}
		if (qisPatient.getGender().equals("M")) {
			ageGender = ageGender + "MALE";
		} else if (qisPatient.getGender().equals("F")) {
			ageGender = ageGender + "FEMALE";
		}
		cell.setPhrase(new Phrase(ageGender, fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		patientTable.addCell(cell);

		cell.setPhrase(new Phrase("Birthdate:", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		patientTable.addCell(cell);

		String birthDate = appUtility.calendarToFormatedDate(qisPatient.getDateOfBirth(), "MMM-dd-yyyy");
		cell.setPhrase(new Phrase(birthDate, fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		patientTable.addCell(cell);

		cell.setPhrase(new Phrase("Contact#:", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		patientTable.addCell(cell);

		cell.setPhrase(new Phrase(qisPatient.getContactNumber(), fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		patientTable.addCell(cell);

		cell.setPhrase(new Phrase("Email:", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		patientTable.addCell(cell);

		String email = qisPatient.getEmail();
		if (qisTransaction.getEmailTo() != null && !"".equals(qisTransaction.getEmailTo().trim())) {
			email = qisTransaction.getEmailTo();
		}

		cell.setPhrase(new Phrase(email, fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		patientTable.addCell(cell);

		if (qisTransaction.getReferral() != null) {
			cell.setPhrase(new Phrase("Referral:", fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			patientTable.addCell(cell);

			cell.setPhrase(new Phrase(qisTransaction.getReferral().getReferral(), fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			patientTable.addCell(cell);
		}

		if (qisPatient.getCorporate() != null) {
			cell.setPhrase(new Phrase("Company:", fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			patientTable.addCell(cell);

			cell.setPhrase(new Phrase(qisPatient.getCorporate().getCompanyName(), fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			patientTable.addCell(cell);
		}

		cell.setPhrase(new Phrase("Cashier:", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		patientTable.addCell(cell);

		QisUser qisCashier = qisTransaction.getCashier();
		cell.setPhrase(new Phrase(qisCashier.getUsername(), fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		patientTable.addCell(cell);

		cell.setPhrase(new Phrase("Date/Time:", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		patientTable.addCell(cell);

		Calendar transactionDate = qisTransaction.getTransactionDate();
		cell.setPhrase(
				new Phrase(appUtility.calendarToFormatedDate(transactionDate, "MMM-dd-yyyy HH:mm:ss"), fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		patientTable.addCell(cell);

		cell.setPhrase(new Phrase("Dispatch:", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		patientTable.addCell(cell);

		cell.setPhrase(new Phrase(appUtility.getDispatchType(qisTransaction.getDispatch()), fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		patientTable.addCell(cell);

		String paymentType = null;
		String billerName = null;

		for (QisTransactionPayment payment : qisTransaction.getTransactionPayments()) {
			String paymentName = appUtility.getPaymentMode(payment.getPaymentMode());
			if (paymentType == null) {
				paymentType = paymentName;
			} else {
				paymentType = paymentType + "," + paymentName;
			}

			if (payment.getBiller() != null) {
				if (billerName == null) {
					billerName = payment.getBiller().getCompanyName();
				} else {
					billerName = billerName + "," + payment.getBiller().getCompanyName();
				}
			}
		}

		if (paymentType == null) {
			paymentType = "----";
		}

		cell.setPhrase(new Phrase("Payment:", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		patientTable.addCell(cell);

		cell.setPhrase(new Phrase(paymentType, fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		patientTable.addCell(cell);

		if (billerName != null) {
			cell.setPhrase(new Phrase("Biller:", fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			patientTable.addCell(cell);

			cell.setPhrase(new Phrase(billerName, fontBold));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			patientTable.addCell(cell);
		}

		cell.setPhrase(new Phrase("SR#:", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		patientTable.addCell(cell);

		cell.setPhrase(new Phrase(appUtility.numberFormat(qisTransaction.getId(), "000000"), fontName));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		patientTable.addCell(cell);

		document.add(patientTable);

		// ITEMS
		PdfPTable itemsTable = new PdfPTable(new float[] { 4, 2 });
		itemsTable.setSpacingBefore(5);
		itemsTable.setWidthPercentage(100f);
		itemsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

		cell.setBorderWidthTop(1);
		cell.setBorderWidthBottom(1);
		cell.setPhrase(new Phrase("Item(s)", fontBold));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		itemsTable.addCell(cell);

		cell.setPhrase(new Phrase("Price", fontBold));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		itemsTable.addCell(cell);

		cell.setBorder(Rectangle.NO_BORDER);
		for (QisTransactionItem item : qisTransaction.getTransactionItems()) {
			String itemName = " ";
			if ("PCK".equals(item.getItemType())) {
				QisPackage pck = (QisPackage) item.getItemDetails();
				itemName += pck.getPackageDescription();
			} else if ("ITM".equals(item.getItemType())) {
				QisItem itm = (QisItem) item.getItemDetails();
				itemName += itm.getItemDescription();
			}

			itemName = itemName + " @" + appUtility.amountFormat(item.getItemPrice(), "###,###.00");
			if (item.getQuantity() > 1) {
				itemName = itemName + "x" + item.getQuantity();
			}

			cell.setPhrase(new Phrase(itemName, fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			itemsTable.addCell(cell);

			cell.setPhrase(new Phrase(appUtility.amountFormat(item.getGrossAmount(), "###,###,###.00"), fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			itemsTable.addCell(cell);

			if (item.getItemType().equals("PCK")) {
				QisPackage itemDetails = (QisPackage) item.getItemDetails();
				for (QisItem qisItem : itemDetails.getPackageItems()) {
					cell.setPhrase(new Phrase(" *" + qisItem.getItemDescription(), fontNormalSmall));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					itemsTable.addCell(cell);

					cell.setPhrase(new Phrase("", fontNormal));
					itemsTable.addCell(cell);
				}
			}
		}

		cell.setBorderWidthTop(1);
		cell.setPhrase(new Phrase("Receipt Total:", fontBold));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		itemsTable.addCell(cell);

		cell.setPhrase(new Phrase(appUtility.amountFormat(qisTransaction.getTotalItemGrossAmount(), "###,###,###.00"),
				fontBold));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		itemsTable.addCell(cell);

		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPhrase(new Phrase("VAT (" + qisTransaction.getTaxRate() + "%):", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		itemsTable.addCell(cell);

		cell.setPhrase(
				new Phrase(appUtility.amountFormat(Math.abs(qisTransaction.getTotalItemTaxAmount()), "###,###,###.00"),
						fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		itemsTable.addCell(cell);

		if (qisTransaction.getTotalItemDiscountAmount() > 0) {
			String discount = "Discount";
			String discountType = "";

			if (qisTransaction.getDiscountType() != null && !"NRM".equals(qisTransaction.getDiscountType())) {
				discountType = "-" + qisTransaction.getDiscountType();
			}

			if (qisTransaction.getDiscountRate() > 0) {
				discount = discount + "(" + qisTransaction.getDiscountRate() + "%" + discountType + ") :";
			}
			cell.setPhrase(new Phrase(discount, fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			itemsTable.addCell(cell);

			cell.setPhrase(
					new Phrase(appUtility.amountFormat(qisTransaction.getTotalItemDiscountAmount(), "###,###,##0.00"),
							fontNormal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			itemsTable.addCell(cell);
		}

		cell.setPhrase(new Phrase("Amount Due:", fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		itemsTable.addCell(cell);

		cell.setPhrase(new Phrase(appUtility.amountFormat(qisTransaction.getTotalItemAmountDue(), "###,###,###.00"),
				fontNormal));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		itemsTable.addCell(cell);

		cell.setPhrase(new Phrase(paymentType + ":", fontBold));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		itemsTable.addCell(cell);

		cell.setPhrase(new Phrase(appUtility.amountFormat(qisTransaction.getTotalPaymentAmount(), "###,###,##0.00"),
				fontBold));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		itemsTable.addCell(cell);

		if (qisTransaction.getTotalChangeAmount() > 0) {
			cell.setPhrase(new Phrase("Change Due:", fontBold));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			itemsTable.addCell(cell);

			cell.setPhrase(new Phrase(appUtility.amountFormat(qisTransaction.getTotalChangeAmount(), "###,###,##0.00"),
					fontBold));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			itemsTable.addCell(cell);
		}

		document.add(itemsTable);

//		if (qisTransaction.getRemarks() != null) {
//			PdfPTable remarksTable = new PdfPTable(1);
//			remarksTable.setSpacingBefore(15);
//			remarksTable.setWidthPercentage(100f);
//			remarksTable.setHorizontalAlignment(Element.ALIGN_LEFT);
//
//			cell.setPhrase(new Phrase(qisTransaction.getRemarks(), fontRemarks));
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			remarksTable.addCell(cell);
//
//			document.add(remarksTable);
//		}

		if (pdfWriter != null) {
			PdfContentByte pcb = pdfWriter.getDirectContent();
			Barcode39 code39 = new Barcode39();
			code39.setCode(appUtility.numberFormat(qisTransaction.getId(), "000000"));
			code39.setX(1.75f);

			document.add(code39.createImageWithBarcode(pcb, null, null));
		}

		document.close();
		return document;
	}

	public Document getCashierEODReceipt(Document document, PdfWriter pdfWriter, CashierEODData eod, QisBranch branch,
			String notes, String reference) throws DocumentException, IOException {
		document.open();

		// HEADER
		Paragraph p = new Paragraph(appUtility.calendarToFormatedDate(Calendar.getInstance(), "MMM-dd-yyyy HH:mm:ss"),
				fontNormalSmall);
		p.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(p);

		String reportRange = appUtility.calendarToFormatedDate(eod.getDateFrom(), "MMM-dd-yyyy HH:mm:ss") + " to "
				+ appUtility.calendarToFormatedDate(eod.getDateTo(), "MMM-dd-yyyy HH:mm:ss");

		addTextToDocumentCompressed(document, applicationName, fontBold);
		addTextToDocumentCompressed(document, "END OF THE DAY REPORT", fontBold);
		addTextToDocumentCompressed(document, branch.getBranchName(), fontNormalSmall);
		addTextToDocumentCompressed(document, reportRange, fontNormalSmall);

		// RECEIPTS
		PdfPTable receiptTable = new PdfPTable(new float[] { 4, 2, 4, 2 });
		receiptTable.setSpacingBefore(5);
		receiptTable.setWidthPercentage(100f);
		receiptTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		receiptTable.getDefaultCell().setBorder(0);
		receiptTable.getDefaultCell().setPadding(10);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);

		cell.setColspan(4);
		addToTable(receiptTable, cell, "RECEIPT COUNTS", fontBold, Element.ALIGN_CENTER);
		cell.setColspan(1);

		Summary summ = eod.getSummary();
		int noRefund = eod.getRefund().size();
		int noHeld = eod.getHold().size();
		int noHMO = eod.getHmo().size();
		int noAccounts = eod.getAccounts().size();
		int noAPE = eod.getApe().size();
		int noBank = eod.getBank().size();
		int noMedicalMission = eod.getMedicalMission().size();
		int noVirtual = eod.getVirtual().size();

		addToTable(receiptTable, cell, "Sales", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, String.valueOf(summ.getTotalSales()), fontNormal, Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Refund", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, String.valueOf(noRefund), fontNormal, Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Held", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, String.valueOf(noHeld), fontNormal, Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "HMO", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, String.valueOf(noHMO), fontNormal, Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Accounts", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, String.valueOf(noAccounts), fontNormal, Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "APE", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, String.valueOf(noAPE), fontNormal, Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Banks", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, String.valueOf(noBank), fontNormal, Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Virtual", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, String.valueOf(noVirtual), fontNormal, Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Medical Mission", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, String.valueOf(noMedicalMission), fontNormal, Element.ALIGN_RIGHT);

		cell.setColspan(4);
		cell.setFixedHeight(5);
		addToTable(receiptTable, cell, "", fontBold, Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setFixedHeight(0);

		addToTable(receiptTable, cell, "Paid In : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getCashInAmount(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Paid Out : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getCashOutAmount(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Total Sales Amount : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getTotalSalesAmount(), "###,###,##0.00"),
				fontNormal, Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Tax Amount : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getTaxAmount(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Discount Amount : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getDiscountAmount(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Net Amount : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getNetAmount(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		cell.setColspan(4);
		cell.setFixedHeight(5);
		addToTable(receiptTable, cell, "", fontBold, Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setFixedHeight(0);

		addToTable(receiptTable, cell, "Total Cash & Account Sales: ", fontBold, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getTotalSalesAmount(), "###,###,##0.00"), fontBold,
				Element.ALIGN_RIGHT);

		// ITERATE BRANCH SALES
		for (Map.Entry<String, Double> entry : eod.getBranchSales().entrySet()) {
			addToTable(receiptTable, cell, entry.getKey() + " : ", fontNormal, Element.ALIGN_LEFT);
			addToTable(receiptTable, cell, appUtility.amountFormat(entry.getValue(), "###,###,##0.00"), fontNormal,
					Element.ALIGN_RIGHT);
		}
		
		cell.setColspan(2);
		cell.setFixedHeight(0);
		
		addToTable(receiptTable, cell, "Medical Services: ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getMedicalService(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		cell.setColspan(4);
		cell.setFixedHeight(5);
		addToTable(receiptTable, cell, "", fontBold, Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setFixedHeight(0);

		addToTable(receiptTable, cell, "Total Cash: ", fontBold, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getCashAmount(), "###,###,##0.00"), fontBold,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Charge to Account : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getAccountsAmount(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Charge to HMO : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getHmoAmount(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Charge to APE : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getApeAmount(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Charge to Medical Mission : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getMedicalMission(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Bank : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getBankAmount(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Virtual : ", fontNormal, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(summ.getVirtualAmount(), "###,###,##0.00"), fontNormal,
				Element.ALIGN_RIGHT);

		double totalNonCash = summ.getAccountsAmount() + summ.getHmoAmount() + summ.getApeAmount()
				+ summ.getMedicalMission() + summ.getBankAmount() + summ.getVirtualAmount();

		double discountCash = 0;
		for (Account cash : eod.getCash()) {
			if ("CA".equals(cash.getPaymentType())) {
				discountCash += cash.getDiscount();
			}
		}

		for (Account refund : eod.getRefund()) {
			if ("CA".equals(refund.getPaymentType())) {
				discountCash += refund.getDiscount();
			}
		}

		double availableDeposit = summ.getCashAmount() - discountCash - summ.getRefundAmount() - summ.getMedicalService();
		availableDeposit -= summ.getRefundAmount();

		addToTable(receiptTable, cell, "Total Account & HMO: ", fontBold, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(totalNonCash, "###,###,##0.00"), fontBold,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Total Available for Deposit: ", fontBold, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(availableDeposit, "###,###,##0.00"), fontBold,
				Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Total Actual for Deposit: ", fontBold, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, appUtility.amountFormat(0d, "###,###,##0.00"), fontBoldRed, Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "External Reference Numberc: ", fontBoldRed, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, notes, fontBold, Element.ALIGN_RIGHT);

		addToTable(receiptTable, cell, "Other Notes: ", fontBold, Element.ALIGN_LEFT);
		addToTable(receiptTable, cell, reference, fontBold, Element.ALIGN_RIGHT);

		document.add(receiptTable);

		if (eod.getHold() != null && eod.getHold().size() > 0) {
			addAccountsToCashierEOD(document, eod.getHold(), "Held");
		}

		if (eod.getAccounts() != null && eod.getAccounts().size() > 0) {
			addAccountsToCashierEOD(document, eod.getAccounts(), "Accounts");
		}

		if (eod.getHmo() != null && eod.getHmo().size() > 0) {
			addAccountsToCashierEOD(document, eod.getHmo(), "HMO");
		}

		if (eod.getApe() != null && eod.getApe().size() > 0) {
			addAccountsToCashierEOD(document, eod.getApe(), "APE");
		}
		
		if (eod.getApe() != null && eod.getMedicalMission().size() > 0) {
			addAccountsToCashierEOD(document, eod.getMedicalMission(), "Medical Mission");
		}
		
		Set<Account> setMedicalService = new HashSet<>();
		for (Account cash : eod.getCash()) {
			if ("TMS".equals(cash.getTransactionType())) {
				setMedicalService.add(cash);
			}
		}
		if (setMedicalService != null && setMedicalService.size() > 0) {
			addAccountsToCashierEOD(document, setMedicalService, "Medical Service");
		}

		if (eod.getBank() != null && eod.getBank().size() > 0) {
			addAccountsToCashierEOD(document, eod.getBank(), "Bank");
		}

		if (eod.getVirtual() != null && eod.getVirtual().size() > 0) {
			addAccountsToCashierEOD(document, eod.getVirtual(), "Virtual");
		}

		if (eod.getRefund() != null && eod.getRefund().size() > 0) {
			addAccountsToCashierEOD(document, eod.getRefund(), "Refund");
		}

		document.close();
		return document;
	}

	private void addAccountsToCashierEOD(Document document, Set<Account> list, String title) {
		PdfPTable accntTable = new PdfPTable(new float[] { 2, 6, 5, 2 });
		accntTable.setSpacingBefore(5);
		accntTable.setWidthPercentage(100f);
		accntTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		accntTable.getDefaultCell().setBorder(0);
		accntTable.getDefaultCell().setPadding(0);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setUseAscender(true);
		cell.setUseDescender(true);
		cell.setVerticalAlignment(Element.ALIGN_TOP);

		cell.setColspan(4);
		addToTable(accntTable, cell, title + " Listing", fontTableTitle, Element.ALIGN_CENTER);
		cell.setColspan(1);

		cell.setBorder(Rectangle.BOX);
		addToTable(accntTable, cell, "Amount", fontTableTitle, Element.ALIGN_LEFT);
		addToTable(accntTable, cell, "Name", fontTableTitle, Element.ALIGN_LEFT);
		addToTable(accntTable, cell, "Charge To", fontTableTitle, Element.ALIGN_LEFT);
		addToTable(accntTable, cell, "Time", fontTableTitle, Element.ALIGN_LEFT);

		double totalAmount = 0;
		for (Account data : list) {
			addToTable(accntTable, cell, appUtility.amountFormat(data.getAmount(), "###,###,##0.00"), fontTableContent,
					Element.ALIGN_RIGHT);
			addToTable(accntTable, cell, data.getPatient(), fontTableContent, Element.ALIGN_LEFT);
			addToTable(accntTable, cell, data.getBiller(), fontTableContent, Element.ALIGN_LEFT);
			addToTable(accntTable, cell, appUtility.calendarToFormatedDate(data.getDate(), "HH:mm:ss"),
					fontTableContent, Element.ALIGN_CENTER);
			totalAmount += data.getAmount();
		}
		addToTable(accntTable, cell, appUtility.amountFormat(totalAmount, "###,###,##0.00"), fontTableTitle,
				Element.ALIGN_RIGHT);
		addToTable(accntTable, cell, "Total", fontTableTitle, Element.ALIGN_LEFT);
		addToTable(accntTable, cell, list.size() + " -- " + title.toUpperCase(), fontTableTitle, Element.ALIGN_LEFT);
		addToTable(accntTable, cell, "", fontTableTitle, Element.ALIGN_LEFT);

		document.add(accntTable);
	}

	private void addTextToDocument(Document document, String text, Font font) {
		Paragraph p = new Paragraph(text, font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);
	}

	private void addTextToDocumentCompressed(Document document, String text, Font font) {
		Paragraph p = new Paragraph(text, font);
		p.setLeading(10);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);
	}

	private void addToTable(PdfPTable table, PdfPCell cell, String text, Font font, int align) {
		cell.setPhrase(new Phrase(text, font));
		cell.setHorizontalAlignment(align);
		table.addCell(cell);
	}

}
