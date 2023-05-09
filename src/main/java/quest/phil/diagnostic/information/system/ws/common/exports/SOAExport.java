package quest.phil.diagnostic.information.system.ws.common.exports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quest.phil.diagnostic.information.system.ws.common.AppExcelUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOA;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionPayment;

public class SOAExport {
	private String applicationName;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private AppUtility appUtility;
	private AppExcelUtility appExcelUtility;
	private final String PAYTO = "QUESTDIAGNOSTICS, INC.";

	public SOAExport(String applicationName, AppUtility appUtility, AppExcelUtility appExcelUtility) {
		super();
		workbook = new XSSFWorkbook();
		this.applicationName = applicationName;
		this.appUtility = appUtility;
		this.appExcelUtility = appExcelUtility;
	}

	public void export(HttpServletResponse response, QisSOA soa) throws IOException {
		sheet = workbook.createSheet("SOA"); // work sheet

		if ("HMO".equals(soa.getChargeTo().getChargeType())) {
			sheet.setColumnWidth(0, 20 * 256);
			sheet.setColumnWidth(1, 20 * 256);
			sheet.setColumnWidth(2, 40 * 256);
			sheet.setColumnWidth(3, 20 * 256);
			sheet.setColumnWidth(4, 20 * 256);
			sheet.setColumnWidth(5, 20 * 256);
			sheet.setColumnWidth(6, 20 * 256);
			sheet.setColumnWidth(7, 40 * 256);
			sheet.setColumnWidth(8, 20 * 256);
		} else {
			sheet.setColumnWidth(0, 20 * 256);
			sheet.setColumnWidth(1, 20 * 256);
			sheet.setColumnWidth(2, 40 * 256);
			sheet.setColumnWidth(3, 40 * 256);
			sheet.setColumnWidth(4, 20 * 256);
			sheet.setColumnWidth(5, 20 * 256);
		}

		writeDataLines(soa);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();
	}

	private void writeDataLines(QisSOA soa) {

		if ("HMO".equals(soa.getChargeTo().getChargeType())) {
			HMOSOA(soa);
		} else {
			defaultSOA(soa);
		}

	}

	// DEFAULTS
	private void defaultSOA(QisSOA soa) {
		int rowCount = 0;

		// HEADER
		rowCount = appExcelUtility.qisExcelHeader(workbook, sheet, rowCount, 5, applicationName, "STATEMENT OF ACCOUNT",
				null);
		// CHARGE TO DETAILS
		rowCount = chargeToDetails(rowCount, soa);
		// TRANSACTION HEADER
		rowCount = addTransactionHeader(rowCount, false);
		// TRANSCTION LIST
		rowCount = addTransactionToExcel(rowCount, soa.getTransactions());
		// REMINDERS
		rowCount = addReminderToExcel(rowCount);
		// SIGNATURES
		rowCount = addSignaturesToExcel(rowCount, soa);
	}

	private int chargeToDetails(int count, QisSOA soa) {
		int currentRow = count;

		CellStyle styleSubTitle = workbook.createCellStyle();
		XSSFFont fontSubTitle = workbook.createFont();
		fontSubTitle.setBold(true);
		fontSubTitle.setFontHeight(12);
		styleSubTitle.setFont(fontSubTitle);
		styleSubTitle.setAlignment(HorizontalAlignment.RIGHT);

		CellStyle leftTitle = workbook.createCellStyle();
		leftTitle.setFont(fontSubTitle);
		leftTitle.setAlignment(HorizontalAlignment.LEFT);

		CellStyle rightTitle = workbook.createCellStyle();
		rightTitle.setFont(fontSubTitle);
		rightTitle.setAlignment(HorizontalAlignment.RIGHT);

		QisCorporate chargeTo = soa.getChargeTo();
		Row rowSoaNumber = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 5));
		appExcelUtility.createCell(rowSoaNumber, 0, soa.getSoaNumber(), styleSubTitle);
		currentRow++;

		Row rowCompany = sheet.createRow(currentRow);
		String company = "Name of Company : " + chargeTo.getCompanyName();
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(rowCompany, 0, company, leftTitle);

		String coverage = "DATE OF COVERAGE : "
				+ appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), "MMM dd, yyyy") + " to "
				+ appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), "MMM dd, yyyy");
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 3, 5));
		appExcelUtility.createCell(rowCompany, 3, coverage, rightTitle);
		currentRow++;

		Row rowAddress = sheet.createRow(currentRow);
		String address = "Address : " + chargeTo.getCompanyAddress() != null ? chargeTo.getCompanyAddress() : "";
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(rowAddress, 0, address, leftTitle);

		String statementDate = "DATE OF STATEMENT : "
				+ appUtility.calendarToFormatedDate(soa.getStatementDate(), "MMM dd, yyyy");
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 3, 5));
		appExcelUtility.createCell(rowAddress, 3, statementDate, rightTitle);
		currentRow++;

		Row rowAttention = sheet.createRow(currentRow);
		String attention = "Attention : " + chargeTo.getContactPerson();
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 5));
		appExcelUtility.createCell(rowAttention, 0, attention, leftTitle);
		currentRow++;

		currentRow++;
		return currentRow;
	}

	private int addTransactionHeader(int count, boolean isHMO) {
		int currentRow = count;

		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);

		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		style.setFont(font);

		Row row = sheet.createRow(currentRow);
		int columnCount = 0;
		appExcelUtility.createCell(row, columnCount++, "Date", style);
		appExcelUtility.createCell(row, columnCount++, "Receipt #", style);
		appExcelUtility.createCell(row, columnCount++, "Full Name", style);
		if (isHMO) {
			appExcelUtility.createCell(row, columnCount++, "Company", style);
			appExcelUtility.createCell(row, columnCount++, "LOE", style);
			appExcelUtility.createCell(row, columnCount++, "Acc. Number", style);
			appExcelUtility.createCell(row, columnCount++, "Approval Code", style);
		}
		appExcelUtility.createCell(row, columnCount++, "Procedure", style);
		if (!isHMO) {
			appExcelUtility.createCell(row, columnCount++, "Subtotal", style);
		}
		appExcelUtility.createCell(row, columnCount++, "Total", style);
		currentRow++;

		return currentRow;
	}

	private int addTransactionToExcel(int count, Set<QisTransaction> list) {
		int rowCount = count;

		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);

		CellStyle leftFont = workbook.createCellStyle();
		leftFont.setFont(font);
		leftFont.setAlignment(HorizontalAlignment.LEFT);

		CellStyle rightFont = workbook.createCellStyle();
		rightFont.setFont(font);
		rightFont.setAlignment(HorizontalAlignment.RIGHT);
		
		CellStyle styleTotal = workbook.createCellStyle();
		XSSFFont fontTotal = workbook.createFont();
		fontTotal.setBold(true);
		fontTotal.setFontHeight(12);
		styleTotal.setFont(fontTotal);
		styleTotal.setAlignment(HorizontalAlignment.RIGHT);

		List<QisTransaction> sortedList = new ArrayList<QisTransaction>(list);
		Collections.sort(sortedList);

		double totalUnpaid = 0;
		double subTotalUnpaid = 0;
		int txnUnpaid = 0;
		for (QisTransaction data : sortedList) {
			if (data.isSoaStatus()) {				
				Row row = sheet.createRow(rowCount++);
				int columnCount = 0;
				txnUnpaid++;
				
				List<QisTransactionItem> sortedItems = new ArrayList<QisTransactionItem>(data.getTransactionItems());
				Collections.sort(sortedItems);
				
				QisTransactionItem item = sortedItems.get(0);
				sortedItems.remove(0);
				
				totalUnpaid += data.getTotalItemAmountDue();
				subTotalUnpaid += item.getAmountDue();
				
				appExcelUtility.createCell(row, columnCount++,
						appUtility.calendarToFormatedDate(data.getTransactionDate(), "yyyy-MM-dd HH:mm:ss"), leftFont);
				appExcelUtility.createCell(row, columnCount++, appUtility.numberFormat(data.getId(), "000000"), leftFont);
				appExcelUtility.createCell(row, columnCount++, appUtility.getPatientFullname(data.getPatient()), leftFont);
				appExcelUtility.createCell(row, columnCount++, appUtility.getTransactionItemDescription(item), leftFont);
				appExcelUtility.createCell(row, columnCount++, item.getAmountDue(), rightFont);
				appExcelUtility.createCell(row, columnCount++, data.getTotalItemAmountDue(), rightFont);
				
				if (sortedItems.size() > 0) {
					for (QisTransactionItem itm : sortedItems) {
						Row rowItem = sheet.createRow(rowCount++);
						columnCount = 3;
						appExcelUtility.createCell(rowItem, columnCount++, appUtility.getTransactionItemDescription(itm),
								leftFont);
						appExcelUtility.createCell(rowItem, columnCount++, itm.getAmountDue(), rightFont);
						subTotalUnpaid += itm.getAmountDue();
					}
				}
			}
		}
		if (txnUnpaid != 0) {			
			Row rowTotal = sheet.createRow(rowCount++);
			int columnCount = 3;
			appExcelUtility.createCell(rowTotal, columnCount++, "TOTAL Unpaid", styleTotal);
			appExcelUtility.createCell(rowTotal, columnCount++, subTotalUnpaid, styleTotal);
			appExcelUtility.createCell(rowTotal, columnCount++, totalUnpaid, styleTotal);
			
			Row row = sheet.createRow(rowCount++);
			appExcelUtility.createCell(row, columnCount++, "", leftFont);
			appExcelUtility.createCell(row, columnCount++, "", leftFont);
			appExcelUtility.createCell(row, columnCount++, "", leftFont);
			appExcelUtility.createCell(row, columnCount++, "", leftFont);
			appExcelUtility.createCell(row, columnCount++, "", leftFont);
			appExcelUtility.createCell(row, columnCount++, "", leftFont);
		}
		

		
		
		double totalPaid = 0;
		double subTotalPaid = 0;
		int txnPaid = 0;
		for (QisTransaction data : sortedList) {
			if (!data.isSoaStatus()) {				
				Row row = sheet.createRow(rowCount++);
				int columnCount1 = 0;
				txnPaid++;
				
				List<QisTransactionItem> sortedItems = new ArrayList<QisTransactionItem>(data.getTransactionItems());
				Collections.sort(sortedItems);
				
				QisTransactionItem item = sortedItems.get(0);
				sortedItems.remove(0);
				
				totalPaid += data.getTotalItemAmountDue();
				subTotalPaid += item.getAmountDue();
				
				appExcelUtility.createCell(row, columnCount1++,
						appUtility.calendarToFormatedDate(data.getTransactionDate(), "yyyy-MM-dd HH:mm:ss"), leftFont);
				appExcelUtility.createCell(row, columnCount1++, appUtility.numberFormat(data.getId(), "000000"), leftFont);
				appExcelUtility.createCell(row, columnCount1++, appUtility.getPatientFullname(data.getPatient()), leftFont);
				appExcelUtility.createCell(row, columnCount1++, appUtility.getTransactionItemDescription(item), leftFont);
				appExcelUtility.createCell(row, columnCount1++, item.getAmountDue(), rightFont);
				appExcelUtility.createCell(row, columnCount1++, data.getTotalItemAmountDue(), rightFont);
				
				if (sortedItems.size() > 0) {
					for (QisTransactionItem itm : sortedItems) {
						Row rowItem = sheet.createRow(rowCount++);
						columnCount1 = 3;
						appExcelUtility.createCell(rowItem, columnCount1++, appUtility.getTransactionItemDescription(itm),
								leftFont);
						appExcelUtility.createCell(rowItem, columnCount1++, itm.getAmountDue(), rightFont);
						subTotalPaid += itm.getAmountDue();
					}
				}
			}
		}
		
		if (txnPaid != 0) {			
			Row rowTotal1 = sheet.createRow(rowCount++);
			int columnCount1 = 3;
			appExcelUtility.createCell(rowTotal1, columnCount1++, "TOTAL Paid", styleTotal);
			appExcelUtility.createCell(rowTotal1, columnCount1++, totalPaid, styleTotal);
			appExcelUtility.createCell(rowTotal1, columnCount1++, subTotalPaid, styleTotal);
		}


	
		Row rowTotal1 = sheet.createRow(rowCount++);
		int columnCount1 = 3;
		appExcelUtility.createCell(rowTotal1, columnCount1++, "Grand Total (NET OF TAX)", styleTotal);
		appExcelUtility.createCell(rowTotal1, columnCount1++, totalPaid + totalUnpaid, styleTotal);
		appExcelUtility.createCell(rowTotal1, columnCount1++, subTotalPaid + subTotalUnpaid, styleTotal);
		rowCount++;
		return rowCount;
	}

	private int addReminderToExcel(int count) {
		int currentRow = count;

		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);

		CellStyle leftFont = workbook.createCellStyle();
		leftFont.setFont(font);
		leftFont.setAlignment(HorizontalAlignment.LEFT);

		Row row1 = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(row1, 0, "Please make check payable to " + PAYTO, leftFont);
		currentRow++;

		Row row2 = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(row2, 0,
				"Kindly examine your statement of account immediately upon receipt. If no error", leftFont);
		currentRow++;

		Row row3 = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(row3, 0, "is reported within 7 days, the account will be considered correct.",
				leftFont);
		currentRow++;

		Row row4 = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(row4, 0, "For question and billing inquiries, please call us at 0917-6260911.",
				leftFont);
		currentRow++;
		
		Row row5 = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(row5, 0, "SOA electronically signed out no need adding signature in SOA.",
				leftFont);
		currentRow++;
	

		return currentRow;
	}

	private int addSignaturesToExcel(int count, QisSOA soa) {
		int currentRow = count;

		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);

		CellStyle leftFont = workbook.createCellStyle();
		leftFont.setFont(font);
		leftFont.setAlignment(HorizontalAlignment.LEFT);

		XSSFFont fontBold = workbook.createFont();
		fontBold.setBold(true);
		fontBold.setFontHeight(12);

		CellStyle leftBold = workbook.createCellStyle();
		leftBold.setFont(fontBold);
//		leftBold.setAlignment(HorizontalAlignment.LEFT);
//		leftBold.setBorderTop(BorderStyle.THIN);

		Row rowPrepared = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1));
		appExcelUtility.createCell(rowPrepared, 0, "Prepared by:", leftFont);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 4, 5));
		appExcelUtility.createCell(rowPrepared, 4, "Noted by:", leftFont);
		currentRow++;
		currentRow++;

		Row rowPreparedName = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1));
		appExcelUtility.createCell(rowPreparedName, 0, appUtility.getUserPersonelFullName(soa.getPreparedUser()),
				leftBold);
		appExcelUtility.createCell(rowPreparedName, 1, "", leftBold);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 4, 5));
		appExcelUtility.createCell(rowPreparedName, 4, appUtility.getUserPersonelFullName(soa.getNotedUser()),
				leftBold);
		appExcelUtility.createCell(rowPreparedName, 5, "", leftBold);
		currentRow++;

		Row rowPreparedTitle = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1));
		appExcelUtility.createCell(rowPreparedTitle, 0, "Management Trainee", leftFont);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 4, 5));
		appExcelUtility.createCell(rowPreparedTitle, 4, "President", leftFont);
		currentRow++;

		Row rowPreparedCompany = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 4, 5));
		appExcelUtility.createCell(rowPreparedCompany, 4, PAYTO, leftFont);
		currentRow++;
		currentRow++;

		Row rowValidated = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1));
		appExcelUtility.createCell(rowValidated, 0, "Verified by:", leftFont);
		currentRow++;
		currentRow++;

		Row rowValidatedName = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1));
		appExcelUtility.createCell(rowValidatedName, 0, appUtility.getUserPersonelFullName(soa.getVerifiedUser()),
				leftBold);
		appExcelUtility.createCell(rowValidatedName, 1, "", leftBold);
		currentRow++;

		Row rowVerifiedTitle = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1));
		appExcelUtility.createCell(rowVerifiedTitle, 0, "Finance Officer", leftFont);

		currentRow++;
		return currentRow;
	}

	// HMO
	private void HMOSOA(QisSOA soa) {
		int rowCount = 0;

		// HEADER
		rowCount = appExcelUtility.qisExcelHeader(workbook, sheet, rowCount, 8, applicationName, "STATEMENT OF ACCOUNT",
				null);
		// CHARGE TO DETAILS
		rowCount = chargeToDetailsHMO(rowCount, soa);
		// TRANSACTION HEADER
		rowCount = addTransactionHeader(rowCount, true);
		// TRANSCTION LIST
		rowCount = addHMOTransactionToExcel(rowCount, soa.getTransactions());
		// REMINDERS
		rowCount = addReminderToExcel(rowCount);
		// SIGNATURES
		rowCount = addHMOSignaturesToExcel(rowCount, soa);
	}

	private int chargeToDetailsHMO(int count, QisSOA soa) {
		int currentRow = count;

		CellStyle styleSubTitle = workbook.createCellStyle();
		XSSFFont fontSubTitle = workbook.createFont();
		fontSubTitle.setBold(true);
		fontSubTitle.setFontHeight(12);
		styleSubTitle.setFont(fontSubTitle);
		styleSubTitle.setAlignment(HorizontalAlignment.RIGHT);

		CellStyle leftTitle = workbook.createCellStyle();
		leftTitle.setFont(fontSubTitle);
		leftTitle.setAlignment(HorizontalAlignment.LEFT);

		CellStyle rightTitle = workbook.createCellStyle();
		rightTitle.setFont(fontSubTitle);
		rightTitle.setAlignment(HorizontalAlignment.RIGHT);

		QisCorporate chargeTo = soa.getChargeTo();
		Row rowCompany = sheet.createRow(currentRow);
		String company = "Name of Company : " + chargeTo.getCompanyName();
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 5));
		appExcelUtility.createCell(rowCompany, 0, company, leftTitle);

		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 6, 8));
		appExcelUtility.createCell(rowCompany, 6, soa.getSoaNumber(), rightTitle);
		currentRow++;

		Row rowAddress = sheet.createRow(currentRow);
		String address = "Address : " + chargeTo.getCompanyAddress();
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 5));
		appExcelUtility.createCell(rowAddress, 0, address, leftTitle);

		String statementDate = "DATE OF STATEMENT : "
				+ appUtility.calendarToFormatedDate(soa.getStatementDate(), "MMM dd, yyyy");
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 6, 8));
		appExcelUtility.createCell(rowAddress, 6, statementDate, rightTitle);
		currentRow++;

		Row rowAttention = sheet.createRow(currentRow);
		String attention = "Attention : " + chargeTo.getContactPerson();
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 5));
		appExcelUtility.createCell(rowAttention, 0, attention, leftTitle);
		currentRow++;

		currentRow++;
		return currentRow;
	}

	private int addHMOTransactionToExcel(int count, Set<QisTransaction> list) {
		int rowCount = count;

		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);

		CellStyle leftFont = workbook.createCellStyle();
		leftFont.setFont(font);
		leftFont.setAlignment(HorizontalAlignment.LEFT);

		CellStyle rightFont = workbook.createCellStyle();
		rightFont.setFont(font);
		rightFont.setAlignment(HorizontalAlignment.RIGHT);

		List<QisTransaction> sortedList = new ArrayList<QisTransaction>(list);
		Collections.sort(sortedList);

		double total = 0;
		for (QisTransaction data : sortedList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			String procedure = "";
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

			List<QisTransactionPayment> payments = new ArrayList<QisTransactionPayment>(data.getTransactionPayments());
			QisTransactionPayment pay = payments.get(0);
			String loe = pay.getHmoLOE() != null ? pay.getHmoLOE() : "";
			String accntNo = pay.getHmoAccountNumber() != null ? pay.getHmoAccountNumber() : "";
			String approvalCode = pay.getHmoApprovalCode() != null ? pay.getHmoApprovalCode() : "";

			appExcelUtility.createCell(row, columnCount++,
					appUtility.calendarToFormatedDate(data.getTransactionDate(), "yyyy-MM-dd HH:mm:ss"), leftFont);
			appExcelUtility.createCell(row, columnCount++, appUtility.numberFormat(data.getId(), "000000"), leftFont);
			appExcelUtility.createCell(row, columnCount++, appUtility.getPatientFullname(data.getPatient()), leftFont);
			appExcelUtility.createCell(row, columnCount++, company, leftFont);
			appExcelUtility.createCell(row, columnCount++, loe, leftFont);
			appExcelUtility.createCell(row, columnCount++, accntNo, leftFont);
			appExcelUtility.createCell(row, columnCount++, approvalCode, leftFont);
			appExcelUtility.createCell(row, columnCount++, procedure, leftFont);
			appExcelUtility.createCell(row, columnCount++, data.getTotalItemAmountDue(), rightFont);

			total += data.getTotalItemAmountDue();
		}

		CellStyle styleTotal = workbook.createCellStyle();
		XSSFFont fontTotal = workbook.createFont();
		fontTotal.setBold(true);
		fontTotal.setFontHeight(12);
		styleTotal.setFont(fontTotal);
		styleTotal.setAlignment(HorizontalAlignment.RIGHT);

		Row rowTotal = sheet.createRow(rowCount++);
		int columnCount = 7;
		appExcelUtility.createCell(rowTotal, columnCount++, "TOTAL (NET OF TAX)", styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, total, styleTotal);

		rowCount++;
		return rowCount;
	}

	private int addHMOSignaturesToExcel(int count, QisSOA soa) {
		int currentRow = count;

		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);

		CellStyle leftFont = workbook.createCellStyle();
		leftFont.setFont(font);
		leftFont.setAlignment(HorizontalAlignment.LEFT);

		XSSFFont fontBold = workbook.createFont();
		fontBold.setBold(true);
		fontBold.setFontHeight(12);

		CellStyle leftBold = workbook.createCellStyle();
		leftBold.setFont(fontBold);
		leftBold.setAlignment(HorizontalAlignment.LEFT);
		leftBold.setBorderTop(BorderStyle.THIN);

		Row rowPrepared = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1));
		appExcelUtility.createCell(rowPrepared, 0, "Prepared by:", leftFont);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 3, 4));
		appExcelUtility.createCell(rowPrepared, 3, "Validated by:", leftFont);
		appExcelUtility.createCell(rowPrepared, 7, "Noted by:", leftFont);
		currentRow++;
		currentRow++;

		Row rowPreparedName = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1));
		appExcelUtility.createCell(rowPreparedName, 0, appUtility.getUserPersonelFullName(soa.getPreparedUser()),
				leftBold);
		appExcelUtility.createCell(rowPreparedName, 1, "", leftBold);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 3, 4));
		appExcelUtility.createCell(rowPreparedName, 3, appUtility.getUserPersonelFullName(soa.getVerifiedUser()),
				leftBold);
		appExcelUtility.createCell(rowPreparedName, 4, "", leftBold);
		appExcelUtility.createCell(rowPreparedName, 7, appUtility.getUserPersonelFullName(soa.getNotedUser()),
				leftBold);
		currentRow++;

		Row rowPreparedTitle = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1));
		appExcelUtility.createCell(rowPreparedTitle, 0, "Mangement Trainee", leftFont);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 3, 4));
		appExcelUtility.createCell(rowPreparedTitle, 3, "Finance Officer", leftFont);
		appExcelUtility.createCell(rowPreparedTitle, 7, "President", leftFont);
		currentRow++;

		Row rowPreparedCompany = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 4, 5));
		appExcelUtility.createCell(rowPreparedCompany, 7, applicationName, leftFont);
		currentRow++;

		return currentRow;
	}

}
