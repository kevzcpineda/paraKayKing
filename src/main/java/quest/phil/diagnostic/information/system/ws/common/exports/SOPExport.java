package quest.phil.diagnostic.information.system.ws.common.exports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quest.phil.diagnostic.information.system.ws.common.AppExcelUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOP;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;

public class SOPExport {
	private String applicationName;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private AppUtility appUtility;
	private AppExcelUtility appExcelUtility;
	private final String PAYTO = "QUESTDIAGNOSTICS, INC.";

	public SOPExport(String applicationName, AppUtility appUtility, AppExcelUtility appExcelUtility) {
		super();
		workbook = new XSSFWorkbook();
		this.applicationName = applicationName;
		this.appUtility = appUtility;
		this.appExcelUtility = appExcelUtility;
	}

	public void export(HttpServletResponse response, QisSOP sop) throws IOException {
		sheet = workbook.createSheet("SOA"); // work sheet

		sheet.setColumnWidth(0, 20 * 256);
		sheet.setColumnWidth(1, 20 * 256);
		sheet.setColumnWidth(2, 40 * 256);
		sheet.setColumnWidth(3, 40 * 256);
		sheet.setColumnWidth(4, 20 * 256);
		sheet.setColumnWidth(5, 20 * 256);

		writeDataLines(sop);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();
	}

	private void writeDataLines(QisSOP sop) {

		defaultSOA(sop);

	}

	private void defaultSOA(QisSOP sop) {
		int rowCount = 0;
		// HEADER
		rowCount = appExcelUtility.qisExcelHeader(workbook, sheet, rowCount, 5, applicationName, "STATEMENT OF PAYABLE",
				null);
		// CHARGE TO DETAILS
		rowCount = referenceToDetails(rowCount, sop);
		// TRANSACTION HEADER
		rowCount = addTransactionHeader(rowCount, false);
		// TRANSCTION LIST
		rowCount = addTransactionToExcel(rowCount, sop.getTransactions(), sop);
//		// REMINDERS
		rowCount = addReminderToExcel(rowCount);
//		// SIGNATURES
		rowCount = addSignaturesToExcel(rowCount, sop);

	}

	private int addSignaturesToExcel(int count, QisSOP sop) {
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
		appExcelUtility.createCell(rowPreparedName, 0, appUtility.getUserPersonelFullName(sop.getPreparedUser()),
				leftBold);
		appExcelUtility.createCell(rowPreparedName, 1, "", leftBold);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 4, 5));
		appExcelUtility.createCell(rowPreparedName, 4, appUtility.getUserPersonelFullName(sop.getNotedUser()),
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
		appExcelUtility.createCell(rowValidatedName, 0, appUtility.getUserPersonelFullName(sop.getVerifiedUser()),
				leftBold);
		appExcelUtility.createCell(rowValidatedName, 1, "", leftBold);
		currentRow++;

		Row rowVerifiedTitle = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1));
		appExcelUtility.createCell(rowVerifiedTitle, 0, "Sales & Billing Officer", leftFont);

		currentRow++;
		return currentRow;
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
		appExcelUtility.createCell(row1, 0, "Please note that payment is made by " + PAYTO, leftFont);
		currentRow++;

		Row row2 = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(row2, 0,
				"Kindly examine your statement of payable immediately upon receipt. If no error", leftFont);
		currentRow++;

		Row row3 = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(row3, 0, "is reported within 7 days, the payable will be considered correct.",
				leftFont);
		currentRow++;

		Row row4 = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(row4, 0, "For question and billing inquiries, please call us at 0917-6260911.",
				leftFont);
		currentRow++;
		
		Row row5 = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(row5, 0, "SOP electronically signed out no need adding signature in SOA.",
				leftFont);
		currentRow++;
		return currentRow;
	}

	private int addTransactionToExcel(int count, Set<QisTransactionLaboratories> list, QisSOP sop) {
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

		List<QisTransactionLaboratories> sortedList = new ArrayList<QisTransactionLaboratories>(list);

		double totalUnpaid = 0;
		double subTotalUnpaid = 0;
		int txnUnpaid = 0;
		for (QisTransactionLaboratories data : sortedList) {

			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
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
			

			appExcelUtility.createCell(row, columnCount++,
					appUtility.calendarToFormatedDate(data.getTransactionDate(), "yyyy-MM-dd HH:mm:ss"), leftFont);
			appExcelUtility.createCell(row, columnCount++, appUtility.numberFormat(data.getId(), "000000"), leftFont);
			appExcelUtility.createCell(row, columnCount++, appUtility.getPatientFullname(data.getPatient()), leftFont);
			
			for (int j = 0; j <= sortedItems.size(); j++) {
				QisTransactionLaboratoryInfo itm = sortedItems.get(j);
				if (itm.getReferenceLab() != null) {					
					sortedItems.remove(j);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.getTransactionItemDescription(itm.getTransactionItem()), leftFont);
					appExcelUtility.createCell(row, columnCount++, itm.getMolePriceItem(), rightFont);
					appExcelUtility.createCell(row, columnCount++, totalMolePrice, rightFont);
					break;
				}
			}
			subTotalUnpaid += totalMolePrice;
			totalUnpaid += totalMolePrice;
			

			if (sortedItems.size() > 0) {
				for (QisTransactionLaboratoryInfo item : sortedItems) {
					if (item.getReferenceLab() != null) {
						if (item.getReferenceLab().getId() == sop.getReferenceId()) {
							columnCount = 3;
							Row rowItem = sheet.createRow(rowCount++);
							appExcelUtility.createCell(rowItem, columnCount++,
									appUtility.getTransactionItemDescription(item.getTransactionItem()), leftFont);
							appExcelUtility.createCell(rowItem, columnCount++, item.getMolePriceItem(), rightFont);
						}
					}
				}
			}
		}

		if (txnUnpaid != 0) {
			Row rowTotal = sheet.createRow(rowCount++);
			int columnCount = 3;
			appExcelUtility.createCell(rowTotal, columnCount++, "GRAND TOTAL", styleTotal);
			appExcelUtility.createCell(rowTotal, columnCount++, subTotalUnpaid, styleTotal);
			appExcelUtility.createCell(rowTotal, columnCount++, totalUnpaid, styleTotal);
		}
		return rowCount;
	}

	private int addTransactionHeader(int count, boolean b) {
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
		appExcelUtility.createCell(row, columnCount++, "Procedure", style);
		appExcelUtility.createCell(row, columnCount++, "Subtotal", style);
		appExcelUtility.createCell(row, columnCount++, "Total", style);
		currentRow++;

		return currentRow;
	}

	private int referenceToDetails(int count, QisSOP sop) {
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

		QisReferenceLaboratory reference = sop.getReferenceLab();
		Row rowSoaNumber = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 5));
		appExcelUtility.createCell(rowSoaNumber, 0, sop.getSopNumber(), styleSubTitle);
		currentRow++;

		Row rowCompany = sheet.createRow(currentRow);
		String company = "Name of Reference Laboratory : " + reference.getName();
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(rowCompany, 0, company, leftTitle);

		String coverage = "DATE OF COVERAGE : "
				+ appUtility.calendarToFormatedDate(sop.getCoverageDateFrom(), "MMM dd, yyyy") + " to "
				+ appUtility.calendarToFormatedDate(sop.getCoverageDateTo(), "MMM dd, yyyy");
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 3, 5));
		appExcelUtility.createCell(rowCompany, 3, coverage, rightTitle);
		currentRow++;

		Row rowAddress = sheet.createRow(currentRow);
		String address = "Address : " + reference.getAddress() != null ? reference.getAddress() : "";
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 2));
		appExcelUtility.createCell(rowAddress, 0, address, leftTitle);

		String statementDate = "DATE OF STATEMENT : "
				+ appUtility.calendarToFormatedDate(sop.getStatementDate(), "MMM dd, yyyy");
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 3, 5));
		appExcelUtility.createCell(rowAddress, 3, statementDate, rightTitle);
		currentRow++;

		Row rowAttention = sheet.createRow(currentRow);
		String attention = "Attention : " + reference.getContactPerson();
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 5));
		appExcelUtility.createCell(rowAttention, 0, attention, leftTitle);
		currentRow++;

		currentRow++;
		return currentRow;
	}
}
