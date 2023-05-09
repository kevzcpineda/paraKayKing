package quest.phil.diagnostic.information.system.ws.common.exports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quest.phil.diagnostic.information.system.ws.common.AppExcelUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisDenominationReportsClass;
import quest.phil.diagnostic.information.system.ws.model.entity.QisAdvancePayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOAPayment;
import quest.phil.diagnostic.information.system.ws.model.eod.Account;
import quest.phil.diagnostic.information.system.ws.model.eod.AuditorEODData;
import quest.phil.diagnostic.information.system.ws.model.eod.Item;
import quest.phil.diagnostic.information.system.ws.model.eod.Summary;

public class AuditorEODExport {
	private String applicationName;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private AuditorEODData eod;
	private AppUtility appUtility;
	private AppExcelUtility appExcelUtility;
	private String othernotes;
	private String referenceNumber;
	private String test;
	private List<QisSOAPayment> soaPaymentList;
	private List<QisAdvancePayment> qisAdvancePayment;

	public AuditorEODExport(AuditorEODData eod, String applicationName, AppUtility appUtility,
			AppExcelUtility appExcelUtility, String othernotes, String referenceNumber, String test,
			List<QisSOAPayment> qisSoaPayment, List<QisAdvancePayment> qisAdvancePayment) {
		super();
		workbook = new XSSFWorkbook();
		this.eod = eod;
		this.applicationName = applicationName;
		this.appUtility = appUtility;
		this.appExcelUtility = appExcelUtility;
		this.othernotes = othernotes;
		this.referenceNumber = referenceNumber;
		this.test = test;
		this.soaPaymentList = qisSoaPayment;
		this.qisAdvancePayment = qisAdvancePayment;
	}

	public void export(HttpServletResponse response) throws IOException {
		sheet = workbook.createSheet("Auditor EOD"); // work sheet
		sheet.setColumnWidth(0, 55 * 256);
		sheet.setColumnWidth(1, 20 * 256);
		sheet.setColumnWidth(2, 10 * 256);
		sheet.setColumnWidth(3, 15 * 256);
		sheet.setColumnWidth(4, 20 * 256);
		sheet.setColumnWidth(5, 20 * 256);
		sheet.setColumnWidth(6, 30 * 256);
		sheet.setColumnWidth(7, 20 * 256);
		sheet.setColumnWidth(8, 20 * 256);
		sheet.setColumnWidth(9, 30 * 256);
		sheet.setColumnWidth(10, 10 * 256);
		sheet.setColumnWidth(11, 20 * 256);
		sheet.setColumnWidth(12, 20 * 256);
		sheet.setColumnWidth(13, 20 * 256);
		sheet.setColumnWidth(14, 20 * 256);
		sheet.setColumnWidth(15, 20 * 256);
		sheet.setColumnWidth(16, 20 * 256);
		sheet.setColumnWidth(17, 20 * 256);
		sheet.setColumnWidth(18, 20 * 256);
		sheet.setColumnWidth(19, 20 * 256);

		if ("RTPCR".equals(test)) {
			rtpcrOnly();
		} else if ("REFERRAL".equals(test)) {
			referralOnly();
		} else {
			writeDataLines();
		}

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();
	}

	private void referralOnly() {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		style.setFont(font);
		int rowCount = 0;

		String reportDate = appUtility.calendarToFormatedDate(eod.getDateFrom(), "MMMM dd, yyyy") + " to "
				+ appUtility.calendarToFormatedDate(eod.getDateTo(), "MMMM dd, yyyy");

		// HEADER
		rowCount = appExcelUtility.qisExcelHeader(workbook, sheet, rowCount, 8, applicationName, "END OF DAY REPORT",
				reportDate);

		// Listing
		if (eod.getCash() != null && eod.getCash().size() > 0) {
			rowCount = addRecordsToExcelReferral(eod.getCash(), "Cash", rowCount, style);
		}

		if (eod.getAccounts() != null && eod.getAccounts().size() > 0) {
			rowCount = addRecordsToExcelReferral(eod.getAccounts(), "Accounts", rowCount, style);
		}

		if (eod.getApe() != null && eod.getApe().size() > 0) {
			rowCount = addRecordsToExcelReferral(eod.getApe(), "APE", rowCount, style);
		}

		if (eod.getMedicalMission() != null && eod.getMedicalMission().size() > 0) {
			rowCount = addRecordsToExcelReferral(eod.getMedicalMission(), "Medical Mission", rowCount, style);
		}

		if (eod.getHmo() != null && eod.getHmo().size() > 0) {
			rowCount = addRecordsToExcelReferral(eod.getHmo(), "HMO", rowCount, style);
		}

		if (eod.getBank() != null && eod.getBank().size() > 0) {
			rowCount = addRecordsToExcelReferral(eod.getBank(), "Bank", rowCount, style);
		}

		if (eod.getVirtual() != null && eod.getVirtual().size() > 0) {
			rowCount = addRecordsToExcelReferral(eod.getVirtual(), "Virtual", rowCount, style);
		}

	}

	private void rtpcrOnly() {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		style.setFont(font);
		int rowCount = 0;

		String reportDate = appUtility.calendarToFormatedDate(eod.getDateFrom(), "MMMM dd, yyyy") + " to "
				+ appUtility.calendarToFormatedDate(eod.getDateTo(), "MMMM dd, yyyy");

		// HEADER
		rowCount = appExcelUtility.qisExcelHeader(workbook, sheet, rowCount, 8, applicationName, "END OF DAY REPORT",
				reportDate);

		// Listing
		if (eod.getCash() != null && eod.getCash().size() > 0) {
			rowCount = addRecordsToExcelRtpcr(eod.getCash(), "Cash", rowCount, style);
		}

		if (eod.getAccounts() != null && eod.getAccounts().size() > 0) {
			rowCount = addRecordsToExcelRtpcr(eod.getAccounts(), "Accounts", rowCount, style);
		}

		if (eod.getApe() != null && eod.getApe().size() > 0) {
			rowCount = addRecordsToExcelRtpcr(eod.getApe(), "APE", rowCount, style);
		}

		if (eod.getMedicalMission() != null && eod.getMedicalMission().size() > 0) {
			rowCount = addRecordsToExcelRtpcr(eod.getMedicalMission(), "Medical Mission", rowCount, style);
		}

		if (eod.getHmo() != null && eod.getHmo().size() > 0) {
			rowCount = addRecordsToExcelRtpcr(eod.getHmo(), "HMO", rowCount, style);
		}

		if (eod.getBank() != null && eod.getBank().size() > 0) {
			rowCount = addRecordsToExcelRtpcr(eod.getBank(), "Bank", rowCount, style);
		}

		if (eod.getVirtual() != null && eod.getVirtual().size() > 0) {
			rowCount = addRecordsToExcelRtpcr(eod.getVirtual(), "Virtual", rowCount, style);
		}

	}

	private int addRecordsToExcelRtpcr(Set<Account> list, String title, int count, CellStyle style) {
		int rowCount = count;
		rowCount = writeHeaderLine(rowCount, title, "rtpcr");
		double subTotal = 0;
		double amountDue = 0;
		double tax = 0;
		double discount = 0;
		double net = 0;
		double cashAmount = 0;
		double changeAmount = 0;
		int listCount = 0;

		List<Account> sortedList = new ArrayList<Account>(list);
		Collections.sort(sortedList);
		for (Account data : sortedList) {
			List<Item> sortedItems = new ArrayList<Item>(data.getItems());
			Collections.sort(sortedItems);
			Item item = sortedItems.get(0);
			for (Item itm1 : sortedItems) {
				if ("RTPCR".equals(itm1.getLaboratoryRequest())) {
					listCount++;
					Row row = sheet.createRow(rowCount++);

					int columnCount = 0;
					appExcelUtility.createCell(row, columnCount++,
							appUtility.calendarToFormatedDate(data.getDate(), "yyyy-MM-dd HH:mm:ss"), style);
					appExcelUtility.createCell(row, columnCount++, data.getBranch(), style);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.numberFormat(data.getTransactionId(), "000000"), style);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.getTransactionType(data.getTransactionType()), style);
					appExcelUtility.createCell(row, columnCount++, appUtility.getPaymentType(data.getPaymentType()),
							style);
					appExcelUtility.createCell(row, columnCount++,
							"CA".equals(data.getPaymentMode()) ? "" : appUtility.getPaymentMode(data.getPaymentMode()),
							style);
					appExcelUtility.createCell(row, columnCount++, data.getPatient(), style);
					appExcelUtility.createCell(row, columnCount++, itm1.getMolecular_lab(), style);
					appExcelUtility.createCell(row, columnCount++, data.getReferral(), style);
					appExcelUtility.createCell(row, columnCount++, data.getBiller(), style);
					appExcelUtility.createCell(row, columnCount++, itm1.getItemName(), style);
					appExcelUtility.createCell(row, columnCount++, itm1.getQuantity(), style);
					appExcelUtility.createCell(row, columnCount++, itm1.getItemPrice(), style);
					appExcelUtility.createCell(row, columnCount++, data.getAmount(), style);
					appExcelUtility.createCell(row, columnCount++, data.getTax(), style);
					appExcelUtility.createCell(row, columnCount++, data.getDiscount(), style);
					appExcelUtility.createCell(row, columnCount++,
							data.getDiscountRate() > 0 ? data.getDiscountRate() : "", style);
					appExcelUtility.createCell(row, columnCount++, data.getNet(), style);
					appExcelUtility.createCell(row, columnCount++, data.getCashier(), style);
					appExcelUtility.createCell(row, columnCount++, data.getCashAmount(), style);
					appExcelUtility.createCell(row, columnCount++, data.getChangeAmount(), style);

					amountDue += data.getAmount();
					tax += data.getTax();
					discount += data.getDiscount();
					net += data.getNet();
					cashAmount += data.getCashAmount();
					changeAmount += data.getChangeAmount();

					subTotal += item.getItemPrice();
					if (sortedItems.size() > 0) {
						for (Item itm : sortedItems) {
							if ("RTPCR".equals(itm.getLaboratoryRequest())) {
								columnCount = 10;
								appExcelUtility.createCell(row, columnCount++, itm.getItemName(), style);
								appExcelUtility.createCell(row, columnCount++, itm.getQuantity(), style);
								appExcelUtility.createCell(row, columnCount++, itm.getItemPrice(), style);
								subTotal += itm.getItemPrice();
							}
						}
					}

				}

			}

		}
		CellStyle styleTotal = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		styleTotal.setFont(font);

		Row rowTotal = sheet.createRow(rowCount++);
		int columnCount = 10;
		appExcelUtility.createCell(rowTotal, columnCount++, "TOTAL", styleTotal);
		columnCount++;
		appExcelUtility.createCell(rowTotal, columnCount++, subTotal, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, amountDue, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, tax, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, discount, styleTotal);
		columnCount++;
		appExcelUtility.createCell(rowTotal, columnCount++, net, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, listCount + " -- " + title.toUpperCase(), styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, cashAmount, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, changeAmount, styleTotal);

		return rowCount;
	}

	private int addRecordsToExcelReferral(Set<Account> list, String title, int count, CellStyle style) {
		int rowCount = count;
		rowCount = writeHeaderLine(rowCount, title, "");
		double subTotal = 0;
		double amountDue = 0;
		double tax = 0;
		double discount = 0;
		double net = 0;
		double cashAmount = 0;
		double changeAmount = 0;

		List<Account> sortedList = new ArrayList<Account>(list);
		Collections.sort(sortedList, (a, b) -> a.getReferral().compareTo(b.getReferral()));
		for (Account data : sortedList) {

			if (!"".equals(data.getReferral())) {
				Row row = sheet.createRow(rowCount++);
				int columnCount = 0;
				List<Item> sortedItems = new ArrayList<Item>(data.getItems());
				Collections.sort(sortedItems);

				Item item = sortedItems.get(0);
				sortedItems.remove(0);

				appExcelUtility.createCell(row, columnCount++,
						appUtility.calendarToFormatedDate(data.getDate(), "yyyy-MM-dd HH:mm:ss"), style);
				appExcelUtility.createCell(row, columnCount++, data.getBranch(), style);
				appExcelUtility.createCell(row, columnCount++,
						appUtility.numberFormat(data.getTransactionId(), "000000"), style);
				appExcelUtility.createCell(row, columnCount++, appUtility.getTransactionType(data.getTransactionType()),
						style);
				appExcelUtility.createCell(row, columnCount++, appUtility.getPaymentType(data.getPaymentType()), style);
				appExcelUtility.createCell(row, columnCount++,
						"CA".equals(data.getPaymentMode()) ? "" : appUtility.getPaymentMode(data.getPaymentMode()),
						style);
				appExcelUtility.createCell(row, columnCount++, data.getPatient(), style);
				appExcelUtility.createCell(row, columnCount++, data.getReferral(), style);
				appExcelUtility.createCell(row, columnCount++, data.getBiller(), style);
				appExcelUtility.createCell(row, columnCount++, item.getItemName(), style);
				appExcelUtility.createCell(row, columnCount++, item.getQuantity(), style);
				appExcelUtility.createCell(row, columnCount++, item.getItemPrice(), style);
				appExcelUtility.createCell(row, columnCount++, data.getAmount(), style);
				appExcelUtility.createCell(row, columnCount++, data.getTax(), style);
				appExcelUtility.createCell(row, columnCount++, data.getDiscount(), style);
				appExcelUtility.createCell(row, columnCount++, data.getDiscountRate() > 0 ? data.getDiscountRate() : "",
						style);
				appExcelUtility.createCell(row, columnCount++, data.getNet(), style);
				appExcelUtility.createCell(row, columnCount++, data.getCashier(), style);
				appExcelUtility.createCell(row, columnCount++, data.getCashAmount(), style);
				appExcelUtility.createCell(row, columnCount++, data.getChangeAmount(), style);

				amountDue += data.getAmount();
				tax += data.getTax();
				discount += data.getDiscount();
				net += data.getNet();
				cashAmount += data.getCashAmount();
				changeAmount += data.getChangeAmount();

				subTotal += item.getItemPrice();
				if (sortedItems.size() > 0) {
					for (Item itm : sortedItems) {
						Row rowItem = sheet.createRow(rowCount++);
						columnCount = 9;
						appExcelUtility.createCell(rowItem, columnCount++, itm.getItemName(), style);
						appExcelUtility.createCell(rowItem, columnCount++, itm.getQuantity(), style);
						appExcelUtility.createCell(rowItem, columnCount++, itm.getItemPrice(), style);
						subTotal += itm.getItemPrice();
					}
				}
			}
		}

		CellStyle styleTotal = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		styleTotal.setFont(font);

		Row rowTotal = sheet.createRow(rowCount++);
		int columnCount = 9;
		appExcelUtility.createCell(rowTotal, columnCount++, "TOTAL", styleTotal);
		columnCount++;
		appExcelUtility.createCell(rowTotal, columnCount++, subTotal, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, amountDue, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, tax, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, discount, styleTotal);
		columnCount++;
		appExcelUtility.createCell(rowTotal, columnCount++, net, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, list.size() + " -- " + title.toUpperCase(), styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, cashAmount, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, changeAmount, styleTotal);

		rowCount++;

		return rowCount;
	}

	private void writeDataLines() {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		style.setFont(font);
		int rowCount = 0;
		String reportDate = appUtility.calendarToFormatedDate(eod.getDateFrom(), "MMMM dd, yyyy") + " to "
				+ appUtility.calendarToFormatedDate(eod.getDateTo(), "MMMM dd, yyyy");

		// HEADER
		rowCount = appExcelUtility.qisExcelHeader(workbook, sheet, rowCount, 8, applicationName, "END OF DAY REPORT",
				reportDate);

		// SUMMARY
		rowCount = addSummayToExcel(rowCount, style);

		// ACCOUNT LIST
		if (eod.getHold() != null && eod.getHold().size() > 0) {
			rowCount = addRecordsToExcel(eod.getHold(), "Held", rowCount, style);
		}

		if (eod.getAccounts() != null && eod.getAccounts().size() > 0) {
			rowCount = addRecordsToExcel(eod.getAccounts(), "Accounts", rowCount, style);
		}

		if (eod.getApe() != null && eod.getApe().size() > 0) {
			rowCount = addRecordsToExcel(eod.getApe(), "APE", rowCount, style);
		}

		if (eod.getMedicalMission() != null && eod.getMedicalMission().size() > 0) {
			rowCount = addRecordsToExcel(eod.getMedicalMission(), "Medical Mission", rowCount, style);
		}

		if (eod.getHmo() != null && eod.getHmo().size() > 0) {
			rowCount = addRecordsToExcel(eod.getHmo(), "HMO", rowCount, style);
		}

		Set<Account> setCash = new HashSet<>();
		Set<Account> setMedicalService = new HashSet<>();
		for (Account cash : eod.getCash()) {
			if ("TMS".equals(cash.getTransactionType())) {
				setMedicalService.add(cash);
			} else {
				setCash.add(cash);
			}
		}

//		if (eod.getCash() != null && eod.getCash().size() > 0) {
//			rowCount = addRecordsToExcel(eod.getCash(), "Cash", rowCount, style);
//		}

		if (setMedicalService != null && setMedicalService.size() > 0) {
			rowCount = addRecordsToExcel(setMedicalService, "Medical Service", rowCount, style);
		}

		if (setCash != null && setCash.size() > 0) {
			rowCount = addRecordsToExcel(setCash, "Cash", rowCount, style);
		}

		if (eod.getBank() != null && eod.getBank().size() > 0) {
			rowCount = addRecordsToExcel(eod.getBank(), "Bank", rowCount, style);
		}

		if (eod.getVirtual() != null && eod.getVirtual().size() > 0) {
			rowCount = addRecordsToExcel(eod.getVirtual(), "Virtual", rowCount, style);
		}

		if (eod.getRefund() != null && eod.getRefund().size() > 0) {
			rowCount = addRecordsToExcel(eod.getRefund(), "Refund", rowCount, style);
		}

		denominationReport(rowCount, style, eod.getSummary().getDenomination());

	}

	private int denominationReport(int count, CellStyle style, List<QisDenominationReportsClass> denomination) {
		int currentRow = count;

		CellStyle styleBold = workbook.createCellStyle();
		XSSFFont fontBond = workbook.createFont();
		fontBond.setBold(true);
		fontBond.setFontHeight(14);
		styleBold.setFont(fontBond);

		CellStyle styleTitle = workbook.createCellStyle();
		XSSFFont fontTitle = workbook.createFont();
		fontTitle.setBold(true);
		fontTitle.setFontHeight(14);
		fontTitle.setColor(IndexedColors.WHITE.getIndex());
		styleTitle.setFont(fontTitle);
		styleTitle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		styleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle styleBoldHeader = workbook.createCellStyle();
		XSSFFont fontBondHeader = workbook.createFont();
		fontBondHeader.setBold(true);
		fontBondHeader.setColor(IndexedColors.WHITE.getIndex());
		fontBondHeader.setFontHeight(12);
		styleBoldHeader.setFont(fontBondHeader);
		styleBoldHeader.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		styleBoldHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle StyleTitleReport = workbook.createCellStyle();
		XSSFFont fontBoldTitleReport = workbook.createFont();
		fontBoldTitleReport.setBold(true);
		fontBoldTitleReport.setColor(IndexedColors.WHITE.getIndex());
		fontBoldTitleReport.setFontHeight(16);
		StyleTitleReport.setFont(fontBondHeader);
		StyleTitleReport.setFillForegroundColor(IndexedColors.CORAL.getIndex());
		StyleTitleReport.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		StyleTitleReport.setFont(fontBoldTitleReport);

		CellStyle styleTotal = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		font.setBold(true);
		styleTotal.setAlignment(HorizontalAlignment.RIGHT);
		styleTotal.setFont(font);

		CellStyle styleAmount = workbook.createCellStyle();
		XSSFFont fontAmount = workbook.createFont();
		fontAmount.setFontHeight(12);
		styleAmount.setAlignment(HorizontalAlignment.RIGHT);

		CellStyle styleHeader = workbook.createCellStyle();
		XSSFFont fontHeader = workbook.createFont();
		fontHeader.setBold(true);
		fontHeader.setFontHeight(12);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		styleHeader.setFont(fontHeader);

		CellStyle styleBoldRed = workbook.createCellStyle();
		XSSFFont fontBondRed = workbook.createFont();
		fontBondRed.setBold(true);
		fontBondRed.setFontHeight(12);
		fontBondRed.setColor(XSSFFont.COLOR_RED);
		styleBoldRed.setFont(fontBondRed);

		if (denomination != null) {
			for (QisDenominationReportsClass report : denomination) {
				Date dateStart = report.getCoverageDateAndTimeFrom().getTime();
				Date dateEnd = report.getCoverageDateAndTimeTo().getTime();

				List<String> myList = new ArrayList<String>();
				final Set<String> setToReturn = new HashSet<>();
				for (Account acct : eod.getAccounts()) {
					int j = 0;
					Date date2 = acct.getDate().getTime();
					for (Account acct2 : eod.getAccounts()) {
						int k = j + 1;
						if (k != j && acct.getBiller() == acct2.getBiller()) {
							if (dateStart.before(date2) && dateEnd.after(date2)) {
								setToReturn.add(acct.getBiller());
							}
						}
						k++;
					}
					if (dateStart.before(date2) && dateEnd.after(date2)) {
						myList.add(acct.getBiller());
					}
					j++;
				}

				List<String> myListApe = new ArrayList<String>();
				final Set<String> setToReturnApe = new HashSet<>();
				for (Account acct : eod.getApe()) {
					int j = 0;
					Date date2 = acct.getDate().getTime();
					for (Account acct2 : eod.getApe()) {
						int k = j + 1;
						if (k != j && acct.getBiller() == acct2.getBiller()) {
							if (dateStart.before(date2) && dateEnd.after(date2)) {
								setToReturnApe.add(acct.getBiller());
							}
						}
						k++;
					}
					if (dateStart.before(date2) && dateEnd.after(date2)) {
						myListApe.add(acct.getBiller());
					}
					j++;
				}

				List<String> myListHmo = new ArrayList<String>();
				final Set<String> setToReturnHmo = new HashSet<>();
				for (Account acct : eod.getHmo()) {
					int j = 0;
					Date date2 = acct.getDate().getTime();
					for (Account acct2 : eod.getHmo()) {
						int k = j + 1;
						if (k != j && acct.getBiller() == acct2.getBiller()) {
							if (dateStart.before(date2) && dateEnd.after(date2)) {
								setToReturnHmo.add(acct.getBiller());
							}
						}
						k++;
					}
					if (dateStart.before(date2) && dateEnd.after(date2)) {
						myListHmo.add(acct.getBiller());
					}
					j++;
				}

				int countForDisplay = 0;
				Row rowminus1 = sheet.createRow(currentRow++);
				Row row = sheet.createRow(currentRow++);
				Row rowVerify = sheet.createRow(currentRow++);
				Row rowNote = sheet.createRow(currentRow++);
				Row row0 = sheet.createRow(currentRow++);
				Row row1 = sheet.createRow(currentRow++);
				Row row2 = sheet.createRow(currentRow++);
				Row row3 = sheet.createRow(currentRow++);
				Row row4 = sheet.createRow(currentRow++);
				Row row5 = sheet.createRow(currentRow++);
				Row row6 = sheet.createRow(currentRow++);
				Row row7 = sheet.createRow(currentRow++);
				Row row8 = sheet.createRow(currentRow++);
				Row row9 = sheet.createRow(currentRow++);
				Row row10 = sheet.createRow(currentRow++);
				Row row11 = sheet.createRow(currentRow++);
				Row row12 = sheet.createRow(currentRow++);
				Row row13 = sheet.createRow(currentRow++);

				String verify = "";
				String noted = "";
				if (report.getVerify() != null) {
					verify = report.getVerify().getUsername();
				}
				if (report.getNoted() != null) {
					noted = report.getNoted().getUsername();
				}
				int columnCount = 0;
				appExcelUtility.createCell(rowminus1, columnCount, report.getReportName(), StyleTitleReport);
				appExcelUtility.createCell(row, columnCount, "Cashier: " + report.getCashier().getUsername(), style);
				appExcelUtility.createCell(rowVerify, columnCount, "Verified By: " + verify, style);
				appExcelUtility.createCell(rowNote, columnCount, "Noted By: " + noted, style);
				appExcelUtility.createCell(row0, columnCount, "Cash Sales", styleTitle);
				appExcelUtility.createCell(row1, columnCount, "Denomination", styleBoldHeader);
				appExcelUtility.createCell(row2, columnCount, "1000", styleTotal);
				appExcelUtility.createCell(row3, columnCount, "500", styleTotal);
				appExcelUtility.createCell(row4, columnCount, "200", styleTotal);
				appExcelUtility.createCell(row5, columnCount, "100", styleTotal);
				appExcelUtility.createCell(row6, columnCount, "50", styleTotal);
				appExcelUtility.createCell(row7, columnCount, "20", styleTotal);
				appExcelUtility.createCell(row8, columnCount, "10", styleTotal);
				appExcelUtility.createCell(row9, columnCount, "5", styleTotal);
				appExcelUtility.createCell(row10, columnCount, "1", styleTotal);
				appExcelUtility.createCell(row11, columnCount, "0.25", styleTotal);
				appExcelUtility.createCell(row12, columnCount, "", style);

				int columnCount1 = 1;
				appExcelUtility.createCell(row1, columnCount1, "No. of Pieces", styleBoldHeader);
				appExcelUtility.createCell(row2, columnCount1, report.getThousands(), styleTotal);
				appExcelUtility.createCell(row3, columnCount1, report.getFiveHundreds(), styleTotal);
				appExcelUtility.createCell(row4, columnCount1, report.getTwoHundreds(), styleTotal);
				appExcelUtility.createCell(row5, columnCount1, report.getOneHundreds(), styleTotal);
				appExcelUtility.createCell(row6, columnCount1, report.getFifties(), styleTotal);
				appExcelUtility.createCell(row7, columnCount1, report.getTwenties(), styleTotal);
				appExcelUtility.createCell(row8, columnCount1, report.getTens(), styleTotal);
				appExcelUtility.createCell(row9, columnCount1, report.getFive(), styleTotal);
				appExcelUtility.createCell(row10, columnCount1, report.getOne(), styleTotal);
				appExcelUtility.createCell(row11, columnCount1, report.getCents(), styleTotal);
				appExcelUtility.createCell(row12, columnCount1, "Total", styleHeader);

				int columnCount2 = 2;
				appExcelUtility.createCell(row1, columnCount2, "Amount", styleBoldHeader);
				appExcelUtility.createCell(row2, columnCount2, report.getThousands() * 1000, styleAmount);
				appExcelUtility.createCell(row3, columnCount2, report.getFiveHundreds() * 500, styleAmount);
				appExcelUtility.createCell(row4, columnCount2, report.getTwoHundreds() * 200, styleAmount);
				appExcelUtility.createCell(row5, columnCount2, report.getOneHundreds() * 100, styleAmount);
				appExcelUtility.createCell(row6, columnCount2, report.getFifties() * 50, styleAmount);
				appExcelUtility.createCell(row7, columnCount2, report.getTwenties() * 20, styleAmount);
				appExcelUtility.createCell(row8, columnCount2, report.getTens() * 10, styleAmount);
				appExcelUtility.createCell(row9, columnCount2, report.getFive() * 5, styleAmount);
				appExcelUtility.createCell(row10, columnCount2, report.getOne() * 1, styleAmount);
				appExcelUtility.createCell(row11, columnCount2, report.getCents() * .25, styleAmount);
				appExcelUtility.createCell(row12, columnCount2,
						(report.getThousands() * 1000) + (report.getFiveHundreds() * 500)
								+ (report.getTwoHundreds() * 200) + (report.getOneHundreds() * 100)
								+ (report.getFifties() * 50) + (report.getTwenties() * 20) + (report.getTens() * 10)
								+ (report.getFive() * 5) + (report.getOne() * 1) + (report.getCents() * .25),
						styleTotal);
				int columnCount3 = 3;
				int columnCount4 = 4;
				appExcelUtility.createCell(row13, columnCount, "", style);

				Row row16 = sheet.createRow(currentRow++);
				Row row17 = sheet.createRow(currentRow++);
				appExcelUtility.createCell(row16, columnCount, "Account Sales", styleTitle);
				appExcelUtility.createCell(row17, columnCount, "Company", styleBoldHeader);
				appExcelUtility.createCell(row17, columnCount1, "Qty", styleBoldHeader);
				appExcelUtility.createCell(row17, columnCount2, "Account", styleBoldHeader);
				appExcelUtility.createCell(row17, columnCount3, "Amount", styleBoldHeader);

				float totalAccount = 0f;
				// For Account Reports
				for (String company : setToReturn) {
					countForDisplay++;
					int qty = 0;
					double totalAmount = 0d;
					double amount = 0d;
					for (String list : myList) {
						if (list.equals(company)) {
							qty++;
							for (Account acct : eod.getAccounts()) {
								if (list.equals(acct.getBiller())) {
									Date date2 = acct.getDate().getTime();
									if (dateStart.before(date2) && dateEnd.after(date2)) {
										amount = acct.getAmount();
									}
								}
							}
							totalAmount += amount;
						}
					}
					for (int i = 1; i <= countForDisplay; i++) {
						if (i == countForDisplay) {
							Row rowForAccounts = sheet.createRow(currentRow++);
							appExcelUtility.createCell(rowForAccounts, columnCount, company, styleHeader);
							appExcelUtility.createCell(rowForAccounts, columnCount1, qty, styleHeader);
							appExcelUtility.createCell(rowForAccounts, columnCount2, "Charge", styleAmount);
							appExcelUtility.createCell(rowForAccounts, columnCount3, totalAmount, styleAmount);
							totalAccount += totalAmount;
						}
					}

				}

				// For Ape Reports
				for (String company : setToReturnApe) {
					countForDisplay++;
					int qty = 0;
					double totalAmount = 0d;
					double amount = 0d;
					for (String list : myListApe) {
						if (list.equals(company)) {
							qty++;
							for (Account acct : eod.getApe()) {
								if (list.equals(acct.getBiller())) {
									Date date2 = acct.getDate().getTime();
									if (dateStart.before(date2) && dateEnd.after(date2)) {
										amount = acct.getAmount();
									}
								}
							}
							totalAmount += amount;
						}
					}
					for (int i = 1; i <= countForDisplay; i++) {
						if (i == countForDisplay) {
							Row rowForAccounts = sheet.createRow(currentRow++);
							appExcelUtility.createCell(rowForAccounts, columnCount, company, styleHeader);
							appExcelUtility.createCell(rowForAccounts, columnCount1, qty, styleHeader);
							appExcelUtility.createCell(rowForAccounts, columnCount2, "Ape", styleAmount);
							appExcelUtility.createCell(rowForAccounts, columnCount3, totalAmount, styleAmount);
						}
					}
					totalAccount += totalAmount;
				}

				// For Hmo Reports
				for (String company : setToReturnHmo) {
					countForDisplay++;
					int qty = 0;
					double totalAmount = 0d;
					double amount = 0d;
					for (String list : myListHmo) {
						if (list.equals(company)) {
							qty++;
							for (Account acct : eod.getHmo()) {
								if (list.equals(acct.getBiller())) {
									Date date2 = acct.getDate().getTime();
									if (dateStart.before(date2) && dateEnd.after(date2)) {
										amount = acct.getAmount();
									}
								}
							}
							totalAmount += amount;
						}
					}

					for (int i = 1; i <= countForDisplay; i++) {
						if (i == countForDisplay) {
							Row rowForAccounts = sheet.createRow(currentRow++);
							appExcelUtility.createCell(rowForAccounts, columnCount, company, styleHeader);
							appExcelUtility.createCell(rowForAccounts, columnCount1, qty, styleHeader);
							appExcelUtility.createCell(rowForAccounts, columnCount2, "HMO", styleAmount);
							appExcelUtility.createCell(rowForAccounts, columnCount3, totalAmount, styleAmount);
						}
					}
					totalAccount += totalAmount;
				}

				Row row31 = sheet.createRow(currentRow++);
				Row row32 = sheet.createRow(currentRow++);

				appExcelUtility.createCell(row31, columnCount2, "Total", styleHeader);
				appExcelUtility.createCell(row31, columnCount3, totalAccount, styleTotal);
				appExcelUtility.createCell(row32, columnCount4, "", styleAmount);

				Row row33 = sheet.createRow(currentRow++);
				Row row34 = sheet.createRow(currentRow++);

				appExcelUtility.createCell(row33, columnCount, "Other Payments", styleTitle);
				appExcelUtility.createCell(row34, columnCount, "Name", styleBoldHeader);
				appExcelUtility.createCell(row34, columnCount1, "Payment Method", styleBoldHeader);
				appExcelUtility.createCell(row34, columnCount2, "Payment Type", styleBoldHeader);
				appExcelUtility.createCell(row34, columnCount3, "Important Description", styleBoldHeader);
				appExcelUtility.createCell(row34, columnCount4, "Amount", styleBoldHeader);
				int otherPayments = 0;
				for (Account data : eod.getBank()) {
					Date date3 = data.getDate().getTime();
					if (dateStart.before(date3) && dateEnd.after(date3)) {
						Row rowName = sheet.createRow(currentRow++);
						appExcelUtility.createCell(rowName, columnCount, data.getPatient(), style);
						appExcelUtility.createCell(rowName, columnCount1,
								appUtility.getPaymentType(data.getPaymentType()), style);
						appExcelUtility.createCell(rowName, columnCount2,
								appUtility.getPaymentMode(data.getPaymentMode()), style);
						appExcelUtility.createCell(rowName, columnCount3, data.getBiller(), style);
						appExcelUtility.createCell(rowName, columnCount4, data.getAmount(), style);
						otherPayments += data.getAmount();
					}
				}

				for (Account data : eod.getVirtual()) {
					Date date3 = data.getDate().getTime();
					if (dateStart.before(date3) && dateEnd.after(date3)) {
						Row rowName = sheet.createRow(currentRow++);
						appExcelUtility.createCell(rowName, columnCount, data.getPatient(), style);
						appExcelUtility.createCell(rowName, columnCount1,
								appUtility.getPaymentType(data.getPaymentType()), style);
						appExcelUtility.createCell(rowName, columnCount2,
								appUtility.getPaymentMode(data.getPaymentMode()), style);
						appExcelUtility.createCell(rowName, columnCount3, "", style);
						appExcelUtility.createCell(rowName, columnCount4, data.getAmount(), style);
						otherPayments += data.getAmount();
					}
				}
				for (Account data : eod.getBank()) {
					Date date3 = data.getDate().getTime();
					if (dateStart.before(date3) && dateEnd.after(date3)) {
						Row rowName = sheet.createRow(currentRow++);
						appExcelUtility.createCell(rowName, columnCount, data.getPatient(), style);
						appExcelUtility.createCell(rowName, columnCount1,
								appUtility.getPaymentType(data.getPaymentType()), style);
						appExcelUtility.createCell(rowName, columnCount2,
								appUtility.getPaymentMode(data.getPaymentMode()), style);
						appExcelUtility.createCell(rowName, columnCount3, "", style);
						appExcelUtility.createCell(rowName, columnCount4, data.getAmount(), style);
						otherPayments += data.getAmount();
					}
				}
				Row rowOtherPaymentAmount = sheet.createRow(currentRow++);
				appExcelUtility.createCell(rowOtherPaymentAmount, columnCount3, "Total", styleHeader);
				appExcelUtility.createCell(rowOtherPaymentAmount, columnCount4, otherPayments, styleTotal);
				Row Spacing = sheet.createRow(currentRow++);
				appExcelUtility.createCell(Spacing, columnCount3, "", styleHeader);

				Row row35 = sheet.createRow(currentRow++);
				Row row36 = sheet.createRow(currentRow++);

				appExcelUtility.createCell(row35, columnCount, "", styleTitle);
				appExcelUtility.createCell(row35, columnCount, "Payment Collection", styleTitle);
				appExcelUtility.createCell(row36, columnCount, "Company", styleBoldHeader);
				appExcelUtility.createCell(row36, columnCount1, "Cash/ Check", styleBoldHeader);
				appExcelUtility.createCell(row36, columnCount2, "OR NUMBER", styleBoldHeader);
				appExcelUtility.createCell(row36, columnCount3, "Check Number (if Check)", styleBoldHeader);
				appExcelUtility.createCell(row36, columnCount4, "Amount", styleBoldHeader);
				appExcelUtility.createCell(row36, columnCount4, "Receiver", styleBoldHeader);
				float paymentCollection = 0f;
				if (soaPaymentList != null) {
					for (QisSOAPayment soa : soaPaymentList) {
						Date soaCreated = soa.getCreatedAt().getTime();
						if (dateStart.before(soaCreated) && dateEnd.after(soaCreated)) {
							Row rowSoa = sheet.createRow(currentRow++);
							appExcelUtility.createCell(rowSoa, columnCount, soa.getChargeTo().getCompanyName(), style);

							String paymentType = "";
							if ("CA".equals(soa.getPaymentType())) {
								paymentType = "CASH";
							} else if ("BNK".equals(soa.getPaymentType())) {
								paymentType = "BANK";
							} else if ("VT".equals(soa.getPaymentType())) {
								paymentType = "VIRTUAL";
							} else {
								paymentType = "CHEQUE";
							}
							appExcelUtility.createCell(rowSoa, columnCount1, paymentType, style);
							appExcelUtility.createCell(rowSoa, columnCount2, "", style);
							appExcelUtility.createCell(rowSoa, columnCount3,
									soa.getAccountNumber() == null ? "" : soa.getAccountNumber(), style);
							appExcelUtility.createCell(rowSoa, columnCount4, soa.getPaymentAmount()
									+ soa.getOtherAmount() + soa.getTaxWithHeld() + soa.getAdvancePayment(), style);
							appExcelUtility.createCell(rowSoa, columnCount4, soa.getPreparedUser().getUsername(),
									style);
							paymentCollection += soa.getPaymentAmount() + soa.getOtherAmount() + soa.getTaxWithHeld();

						}
					}
				}
				Row rowPaymentCollectionAmount = sheet.createRow(currentRow++);

				appExcelUtility.createCell(rowPaymentCollectionAmount, columnCount3, "Total", styleHeader);
				appExcelUtility.createCell(rowPaymentCollectionAmount, columnCount4, paymentCollection, styleTotal);

				Row Spacing1 = sheet.createRow(currentRow++);
				appExcelUtility.createCell(Spacing1, columnCount3, "", styleHeader);
				appExcelUtility.createCell(Spacing1, columnCount3, "", styleHeader);

				Row row37 = sheet.createRow(currentRow++);
				Row row38 = sheet.createRow(currentRow++);

				appExcelUtility.createCell(row37, columnCount, "", styleTitle);
				appExcelUtility.createCell(row37, columnCount, "Advance Payment Collection", styleTitle);
				appExcelUtility.createCell(row38, columnCount, "Company", styleBoldHeader);
				appExcelUtility.createCell(row38, columnCount1, "Amount", styleBoldHeader);
				appExcelUtility.createCell(row38, columnCount2, "Receiver", styleBoldHeader);

				float AdvancePaymentCollection = 0f;
				if (qisAdvancePayment != null) {
					for (QisAdvancePayment advancePayment : qisAdvancePayment) {
						Date advancePaymentCreated = advancePayment.getCreatedAt().getTime();
						if (dateStart.before(advancePaymentCreated) && dateEnd.after(advancePaymentCreated)) {
							Row rowAdvancePayment = sheet.createRow(currentRow++);
							appExcelUtility.createCell(rowAdvancePayment, columnCount, advancePayment.getChargeTo().getCompanyName(), style);
							appExcelUtility.createCell(rowAdvancePayment, columnCount1, advancePayment.getAmtAdvPayment() , style);
							appExcelUtility.createCell(rowAdvancePayment, columnCount2, advancePayment.getCreateUser().getUsername(),
									style);
							AdvancePaymentCollection += advancePayment.getAmtAdvPayment();

						}
					}
				}
				Row rowAdvancePaymentCollectionAmount = sheet.createRow(currentRow++);

				appExcelUtility.createCell(rowAdvancePaymentCollectionAmount, columnCount, "Total", styleHeader);
				appExcelUtility.createCell(rowAdvancePaymentCollectionAmount, columnCount1, AdvancePaymentCollection,
						styleTotal);
				Row Spacingend = sheet.createRow(currentRow++);
				appExcelUtility.createCell(Spacingend, columnCount3, "", styleHeader);
				appExcelUtility.createCell(Spacingend, columnCount3, "", styleHeader);
				appExcelUtility.createCell(Spacingend, columnCount3, "", styleHeader);
				appExcelUtility.createCell(Spacingend, columnCount3, "", styleHeader);
			}

		}

		return currentRow;
	}

	private int addSummayToExcel(int count, CellStyle style) {
		int currentRow = count;

		Summary summ = eod.getSummary();

		CellStyle styleBold = workbook.createCellStyle();
		XSSFFont fontBond = workbook.createFont();
		fontBond.setBold(true);
		fontBond.setFontHeight(12);
		styleBold.setFont(fontBond);

		CellStyle styleBoldRed = workbook.createCellStyle();
		XSSFFont fontBondRed = workbook.createFont();
		fontBondRed.setBold(true);
		fontBondRed.setFontHeight(12);
		fontBondRed.setColor(XSSFFont.COLOR_RED);
		styleBoldRed.setFont(fontBondRed);

		Row summaryTitle = sheet.createRow(currentRow);
		appExcelUtility.createCell(summaryTitle, 0, "SUMMARY", styleBold);
		currentRow++;

		Row receiptCount = sheet.createRow(currentRow);
		appExcelUtility.createCell(receiptCount, 0, "RECEIPT COUNTS", styleBold);
		currentRow++;

		int refundCount = 0;
		int holdCount = 0;
		int hmoCount = 0;
		int accountsCount = 0;
		int apeCount = 0;
		int cashCount = 0;
		int bankCount = 0;
		int virtualCount = 0;
		int medicalMission = 0;

		if (eod.getRefund() != null && eod.getRefund().size() > 0) {
			refundCount = eod.getRefund().size();
		}

		if (eod.getHold() != null && eod.getHold().size() > 0) {
			holdCount = eod.getHold().size();
		}

		if (eod.getHmo() != null && eod.getHmo().size() > 0) {
			hmoCount = eod.getHmo().size();
		}

		if (eod.getAccounts() != null && eod.getAccounts().size() > 0) {
			accountsCount = eod.getAccounts().size();
		}

		if (eod.getApe() != null && eod.getApe().size() > 0) {
			apeCount = eod.getApe().size();
		}

		if (eod.getMedicalMission() != null && eod.getMedicalMission().size() > 0) {
			medicalMission = eod.getMedicalMission().size();
		}

		if (eod.getCash() != null && eod.getCash().size() > 0) {
			cashCount = eod.getCash().size();
		}

		if (eod.getBank() != null && eod.getBank().size() > 0) {
			bankCount = eod.getBank().size();
		}

		if (eod.getVirtual() != null && eod.getVirtual().size() > 0) {
			virtualCount = eod.getVirtual().size();
		}

		currentRow = addSummaryToExcel(currentRow, "Sales : ", summ.getTotalSales(), "", "", style, style);
		currentRow = addSummaryToExcel(currentRow, "Cash : ", cashCount, "Paid In : ", summ.getCashInAmount(), style,
				style);
		currentRow = addSummaryToExcel(currentRow, "Refund : ", refundCount, "Paid Out : ", summ.getCashOutAmount(),
				style, style);
		currentRow = addSummaryToExcel(currentRow, "Held : ", holdCount, "Total Sales Amount : ",
				summ.getTotalSalesAmount(), style, style);
		currentRow = addSummaryToExcel(currentRow, "HMO : ", hmoCount, "Tax Amount : ", summ.getTaxAmount(), style,
				style);
		currentRow = addSummaryToExcel(currentRow, "Accounts : ", accountsCount, "Discount Amount : ",
				summ.getDiscountAmount(), style, style);
		currentRow = addSummaryToExcel(currentRow, "APE : ", apeCount, "Net Amount : ", summ.getNetAmount(), style,
				style);
		currentRow = addSummaryToExcel(currentRow, "Banks : ", bankCount, "", "", style, style);
		currentRow = addSummaryToExcel(currentRow, "Virtual : ", virtualCount, "", "", style, style);
		currentRow = addSummaryToExcel(currentRow, "Medical Mission : ", medicalMission, "", "", style, style);

		currentRow = addSummaryToExcel(currentRow, "", "", "Total Cash & Account Sales : ", summ.getTotalSalesAmount(),
				style, styleBold);

		// ITERATE BRANCH SALES
		for (Map.Entry<String, Double> entry : eod.getBranchSales().entrySet()) {
			currentRow = addSummaryToExcel(currentRow, "", "", entry.getKey() + " : ", entry.getValue(), style, style);
		}
		currentRow = addSummaryToExcel(currentRow, "", "", "Medical Services : ", summ.getMedicalService(), style,
				style);
		currentRow++;

		currentRow = addSummaryToExcel(currentRow, "", "", "Total Cash : ", summ.getCashAmount(), style, styleBold);

		currentRow = addSummaryToExcel(currentRow, "", "", "Charge to Account : ", summ.getAccountsAmount(), style,
				style);
		currentRow = addSummaryToExcel(currentRow, "", "", "Charge to HMO : ", summ.getHmoAmount(), style, style);
		currentRow = addSummaryToExcel(currentRow, "", "", "Charge to APE : ", summ.getApeAmount(), style, style);

		currentRow = addSummaryToExcel(currentRow, "", "", "Charge to Medical Mission : ", summ.getMedicalMission(),
				style, style);

		currentRow = addSummaryToExcel(currentRow, "", "", "Bank : ", summ.getBankAmount(), style, style);
		currentRow = addSummaryToExcel(currentRow, "", "", "Virtual : ", summ.getVirtualAmount(), style, style);

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

		double availableDeposit = summ.getCashAmount() - discountCash - summ.getRefundAmount()
				- summ.getMedicalService();
		availableDeposit -= summ.getRefundAmount();

		currentRow = addSummaryToExcel(currentRow, "", "", "Total Accounts & HMO : ", totalNonCash, style, styleBold);
		currentRow = addSummaryToExcel(currentRow, "", "", "Total Available Deposit : ", availableDeposit, style,
				styleBold);
		currentRow = addSummaryToExcel(currentRow, "", "", "Total Actual Deposit : ", 0, style, styleBoldRed);

		currentRow = addSummaryToExcel(currentRow, "", "", "External Reference Number : ", referenceNumber, style,
				styleBold);
		currentRow = addSummaryToExcel(currentRow, "", "", "Other Notes : ", othernotes, style, styleBold);

		currentRow += 2;

		return currentRow;
	}

	private int addSummaryToExcel(int count, String leftTag, Object leftValue, String rightTag, Object rightValue,
			CellStyle styleLeft, CellStyle styleRight) {
		int rowCount = count;

		Row row = sheet.createRow(rowCount++);
		appExcelUtility.createCell(row, 0, leftTag, styleLeft);
		appExcelUtility.createCell(row, 2, leftValue, styleLeft);

		appExcelUtility.createCell(row, 4, rightTag, styleRight);
		appExcelUtility.createCell(row, 6, rightValue, styleRight);

		return rowCount;
	}

	private int addRecordsToExcel(Set<Account> list, String title, int count, CellStyle style) {
		int rowCount = count;
		rowCount = writeHeaderLine(rowCount, title, "");
		double subTotal = 0;
		double amountDue = 0;
		double tax = 0;
		double discount = 0;
		double net = 0;
		double cashAmount = 0;
		double changeAmount = 0;

		List<Account> sortedList = new ArrayList<Account>(list);
		Collections.sort(sortedList);
		for (Account data : sortedList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			List<Item> sortedItems = new ArrayList<Item>(data.getItems());
			Collections.sort(sortedItems);

			Item item = sortedItems.get(0);
			sortedItems.remove(0);

			appExcelUtility.createCell(row, columnCount++,
					appUtility.calendarToFormatedDate(data.getDate(), "yyyy-MM-dd HH:mm:ss"), style);
			appExcelUtility.createCell(row, columnCount++, data.getBranch(), style);
			appExcelUtility.createCell(row, columnCount++, appUtility.numberFormat(data.getTransactionId(), "000000"),
					style);
			appExcelUtility.createCell(row, columnCount++, appUtility.getTransactionType(data.getTransactionType()),
					style);
			appExcelUtility.createCell(row, columnCount++, appUtility.getPaymentType(data.getPaymentType()), style);
			appExcelUtility.createCell(row, columnCount++,
					"CA".equals(data.getPaymentMode()) ? "" : appUtility.getPaymentMode(data.getPaymentMode()), style);
			appExcelUtility.createCell(row, columnCount++, data.getPatient(), style);
			appExcelUtility.createCell(row, columnCount++, data.getReferral(), style);
			appExcelUtility.createCell(row, columnCount++, data.getBiller(), style);
			appExcelUtility.createCell(row, columnCount++, item.getItemName(), style);
			appExcelUtility.createCell(row, columnCount++, item.getQuantity(), style);
			appExcelUtility.createCell(row, columnCount++, item.getItemPrice(), style);
			appExcelUtility.createCell(row, columnCount++, data.getAmount(), style);
			appExcelUtility.createCell(row, columnCount++, data.getTax(), style);
			appExcelUtility.createCell(row, columnCount++, data.getDiscount(), style);
			appExcelUtility.createCell(row, columnCount++, data.getDiscountRate() > 0 ? data.getDiscountRate() : "",
					style);
			appExcelUtility.createCell(row, columnCount++, data.getNet(), style);
			appExcelUtility.createCell(row, columnCount++, data.getCashier(), style);
			appExcelUtility.createCell(row, columnCount++, data.getCashAmount(), style);
			appExcelUtility.createCell(row, columnCount++, data.getChangeAmount(), style);

			amountDue += data.getAmount();
			tax += data.getTax();
			discount += data.getDiscount();
			net += data.getNet();
			cashAmount += data.getCashAmount();
			changeAmount += data.getChangeAmount();

			subTotal += item.getItemPrice();
			if (sortedItems.size() > 0) {
				for (Item itm : sortedItems) {
					Row rowItem = sheet.createRow(rowCount++);
					columnCount = 9;
					appExcelUtility.createCell(rowItem, columnCount++, itm.getItemName(), style);
					appExcelUtility.createCell(rowItem, columnCount++, itm.getQuantity(), style);
					appExcelUtility.createCell(rowItem, columnCount++, itm.getItemPrice(), style);
					subTotal += itm.getItemPrice();
				}
			}
		}

		CellStyle styleTotal = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		styleTotal.setFont(font);

		Row rowTotal = sheet.createRow(rowCount++);
		int columnCount = 9;
		appExcelUtility.createCell(rowTotal, columnCount++, "TOTAL", styleTotal);
		columnCount++;
		appExcelUtility.createCell(rowTotal, columnCount++, subTotal, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, amountDue, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, tax, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, discount, styleTotal);
		columnCount++;
		appExcelUtility.createCell(rowTotal, columnCount++, net, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, list.size() + " -- " + title.toUpperCase(), styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, cashAmount, styleTotal);
		appExcelUtility.createCell(rowTotal, columnCount++, changeAmount, styleTotal);

		rowCount++;

		return rowCount;
	}

	private int writeHeaderLine(int cellRow, String title, String rtpcr) {
		int currentRow = cellRow;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		style.setFont(font);

		if (title != null) {
			Row rowTitle = sheet.createRow(currentRow);
			appExcelUtility.createCell(rowTitle, 0, title + " Listing", style);
			currentRow++;
		}

		Row row = sheet.createRow(currentRow);
		int columnCount = 0;
		appExcelUtility.createCell(row, columnCount++, "Date Time", style);
		appExcelUtility.createCell(row, columnCount++, "Branch", style);
		appExcelUtility.createCell(row, columnCount++, "SR#", style);
		appExcelUtility.createCell(row, columnCount++, "Type", style);
		appExcelUtility.createCell(row, columnCount++, "Payment Type", style);
		appExcelUtility.createCell(row, columnCount++, "Payment Mode", style);
		appExcelUtility.createCell(row, columnCount++, "Name", style);
		if ("rtpcr".equals(rtpcr)) {
			appExcelUtility.createCell(row, columnCount++, "Molecular Lab", style);
		}
		appExcelUtility.createCell(row, columnCount++, "Referral", style);
		appExcelUtility.createCell(row, columnCount++, "Charge To", style);
		appExcelUtility.createCell(row, columnCount++, "Items", style);
		appExcelUtility.createCell(row, columnCount++, "QTY", style);
		appExcelUtility.createCell(row, columnCount++, "Sub Total", style);
		appExcelUtility.createCell(row, columnCount++, "Amount Due", style);
		appExcelUtility.createCell(row, columnCount++, "Tax", style);
		appExcelUtility.createCell(row, columnCount++, "Discount", style);
		appExcelUtility.createCell(row, columnCount++, "Discount Rate", style);
		appExcelUtility.createCell(row, columnCount++, "Net", style);
		appExcelUtility.createCell(row, columnCount++, "Cashier", style);
		appExcelUtility.createCell(row, columnCount++, "Amount Tendered", style);
		appExcelUtility.createCell(row, columnCount++, "Given Change", style);

		currentRow++;
		return currentRow;
	}

}
