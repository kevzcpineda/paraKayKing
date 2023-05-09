package quest.phil.diagnostic.information.system.ws.common.exports;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quest.phil.diagnostic.information.system.ws.common.AppExcelUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOA;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOAPayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.repository.QisCorporateRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisSOAPaymentRepository;

public class SOAReportExport {
	private String applicationName;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private AppUtility appUtility;
	private AppExcelUtility appExcelUtility;
	private QisCorporateRepository qisCorporateRepository;
	private QisSOAPaymentRepository qisSOAPaymentRepository;
	private final String SHORTDATE = "yyyy-MM-dd";
	private final String SHORTDATE2 = "MMM dd, yyyy";
	private final String YEARDATE = "yyyy";
	private Boolean soaPrepared;
	private Boolean soaVerified;
	private Boolean soaNoted;
	private Boolean soaSend;
	private Boolean soaRecieved;
	private Boolean soaPaymentPrepared;
	private Boolean soaPaymentBalance;
	private Boolean soaPaymentVerified;
	private Boolean soaPaymentAudited;
	private Boolean unbilledTransaction;

	public SOAReportExport(String applicationName, AppUtility appUtility, AppExcelUtility appExcelUtility,
			QisCorporateRepository qisCorporateRepository, QisSOAPaymentRepository qisSOAPaymentRepository,
			Boolean soaPrepared, Boolean soaVerified, Boolean soaNoted, Boolean soaSend, Boolean soaRecieved,
			Boolean soaPaymentPrepared, Boolean soaPaymentBalance, Boolean soaPaymentVerified,
			Boolean soaPaymentAudited, Boolean unbilledTransaction) {
		super();
		workbook = new XSSFWorkbook();
		this.applicationName = applicationName;
		this.appUtility = appUtility;
		this.appExcelUtility = appExcelUtility;
		this.qisCorporateRepository = qisCorporateRepository;
		this.qisSOAPaymentRepository = qisSOAPaymentRepository;
		this.soaPrepared = soaPrepared;
		this.soaVerified = soaVerified;
		this.soaNoted = soaNoted;
		this.soaSend = soaSend;
		this.soaRecieved = soaRecieved;
		this.soaPaymentPrepared = soaPaymentPrepared;
		this.soaPaymentBalance = soaPaymentBalance;
		this.soaPaymentVerified = soaPaymentVerified;
		this.soaPaymentAudited = soaPaymentAudited;
		this.unbilledTransaction = unbilledTransaction;
	}

	public void export(HttpServletResponse response, List<QisSOA> soa, List<QisTransaction> unbilledList)
			throws IOException {
		sheet = workbook.createSheet("SOA REPORT"); // work sheet

		sheet.setColumnWidth(0, 20 * 256);
		sheet.setColumnWidth(1, 20 * 256);
		sheet.setColumnWidth(2, 35 * 256);
		sheet.setColumnWidth(3, 15 * 256);
		sheet.setColumnWidth(4, 22 * 256);
		sheet.setColumnWidth(5, 22 * 256);
		sheet.setColumnWidth(5, 27 * 256);
		sheet.setColumnWidth(6, 20 * 256);

		writeDataLines(soa, unbilledList);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();
	}

	private void writeDataLines(List<QisSOA> soa, List<QisTransaction> unbilledList) {
		int rowCount = 0;
		// HEADER
		rowCount = appExcelUtility.qisExcelHeader(workbook, sheet, rowCount, 5, applicationName,
				"STATEMENT OF ACCOUNT REPORT", null);
		// SOA HEADER
		rowCount = addTransactionHeader(rowCount);
		// SOA LIST
		rowCount = addSoaToExcel(rowCount, soa);

		if (unbilledTransaction || (!soaPrepared && !soaVerified && !soaNoted && !soaSend && !soaRecieved && !soaPaymentPrepared
				&& !soaPaymentBalance && !soaPaymentVerified && !soaPaymentAudited)) {
			// TRANSACTION HEADER
			rowCount = addTransactionHeaderUnbilled(rowCount);
			// UNBILLED TRANSACTION
			rowCount = addTransactionToExcel(rowCount, unbilledList);
		}
	}

	private int addTransactionHeaderUnbilled(int rowCount) {
		int currentRow = rowCount;

		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);

		XSSFFont font = workbook.createFont();

		font.setFontHeight(12);
		style.setFont(font);

		Row row = sheet.createRow(currentRow);
		int columnCount = 0;
		appExcelUtility.createCell(row, columnCount++, "Company", style);
		appExcelUtility.createCell(row, columnCount++, "Transaction Date", style);
		appExcelUtility.createCell(row, columnCount++, "SR #", style);
		appExcelUtility.createCell(row, columnCount++, "Fullname", style);
		appExcelUtility.createCell(row, columnCount++, "Soa Aging", style);
		appExcelUtility.createCell(row, columnCount++, "Procedure", style);
		appExcelUtility.createCell(row, columnCount++, "Amount", style);
		appExcelUtility.createCell(row, columnCount++, "Total", style);
		currentRow++;

		return currentRow;
	}

	private int addTransactionHeader(int rowCount) {
		int currentRow = rowCount;

		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);

		XSSFFont font = workbook.createFont();

		font.setFontHeight(12);
		style.setFont(font);

		Row row = sheet.createRow(currentRow);
		int columnCount = 0;
		appExcelUtility.createCell(row, columnCount++, "SOA Number", style);
		appExcelUtility.createCell(row, columnCount++, "Statement Date", style);
		appExcelUtility.createCell(row, columnCount++, "Covered Date", style);
		appExcelUtility.createCell(row, columnCount++, "Soa Amount", style);
		appExcelUtility.createCell(row, columnCount++, "Soa Amount (Balance)", style);
		appExcelUtility.createCell(row, columnCount++, "Control Number", style);
		appExcelUtility.createCell(row, columnCount++, "PO Number", style);
		appExcelUtility.createCell(row, columnCount++, "Soa Aging", style);
		appExcelUtility.createCell(row, columnCount++, "Status", style);
		currentRow++;

		return currentRow;
	}

	private int addTransactionToExcel(int count, List<QisTransaction> unbilledList) {

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

		CellStyle styleGreen = workbook.createCellStyle();
		XSSFFont fontGreen = workbook.createFont();
		fontGreen.setColor(IndexedColors.GREEN.getIndex());
		styleGreen.setAlignment(HorizontalAlignment.RIGHT);
		styleGreen.setFont(fontGreen);

		CellStyle styleRed = workbook.createCellStyle();
		styleRed.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("###0,00"));
		XSSFFont fontRed = workbook.createFont();
		fontRed.setBold(true);
		fontRed.setColor(IndexedColors.RED.getIndex());
		styleRed.setFont(fontRed);

		CellStyle soaAmountStyle = workbook.createCellStyle();
		soaAmountStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("###0,00"));

		CellStyle totalStyle = workbook.createCellStyle();
		totalStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("###0,00"));
		XSSFFont fontTotal2 = workbook.createFont();
		fontTotal2.setBold(true);
		totalStyle.setFont(fontTotal2);
		totalStyle.setBorderTop(BorderStyle.DOUBLE);

		double grandTotal = 0;
		for (QisTransaction unbilled : unbilledList) {
			Row rowUnbilled = sheet.createRow(rowCount++);
			int columnCount = 0;

			int days = (int) ChronoUnit.DAYS.between(unbilled.getTransactionDate().toInstant(),
					Calendar.getInstance().toInstant());

			int day = days % 30;
			int month = days / 30;
			String Aging = "";
			if (0 < month) {
				if (month == 1) {
					if (1 < day) {
						Aging = month + " Month and " + day + " Days";
					} else {
						Aging = month + " Month and " + day + " Day";
					}
				} else {
					if (1 < day) {
						Aging = month + " Months and " + day + " Days";
					} else {
						Aging = month + " Months and " + day + " Day";
					}
				}
			} else {
				if (1 < day) {
					Aging = day + " Days";
				} else {
					Aging = day + " Day";
				}
			}

			List<QisTransactionItem> sortedItems = new ArrayList<QisTransactionItem>(unbilled.getTransactionItems());
			Collections.sort(sortedItems);

			QisTransactionItem item = sortedItems.get(0);
			sortedItems.remove(0);
			appExcelUtility.createCell(rowUnbilled, columnCount++, unbilled.getBiller(), leftFont);
			appExcelUtility.createCell(rowUnbilled, columnCount++,
					appUtility.calendarToFormatedDate(unbilled.getTransactionDate(), "yyyy-MM-dd HH:mm:ss"), leftFont);
			appExcelUtility.createCell(rowUnbilled, columnCount++, appUtility.numberFormat(unbilled.getId(), "000000"),
					leftFont);
			appExcelUtility.createCell(rowUnbilled, columnCount++, appUtility.getPatientFullname(unbilled.getPatient()),
					leftFont);
			appExcelUtility.createCell(rowUnbilled, columnCount++, Aging, leftFont);
			appExcelUtility.createCell(rowUnbilled, columnCount++, appUtility.getTransactionItemDescription(item),
					leftFont);
			appExcelUtility.createCell(rowUnbilled, columnCount++, item.getAmountDue(), rightFont);
			appExcelUtility.createCell(rowUnbilled, columnCount++, unbilled.getTotalItemAmountDue(), rightFont);
			grandTotal += unbilled.getTotalItemAmountDue();
			if (sortedItems.size() > 0) {
				for (QisTransactionItem itm : sortedItems) {
					Row rowItem = sheet.createRow(rowCount++);
					columnCount = 5;
					appExcelUtility.createCell(rowItem, columnCount++, appUtility.getTransactionItemDescription(itm),
							leftFont);
					appExcelUtility.createCell(rowItem, columnCount++, itm.getAmountDue(), rightFont);

				}
			}

		}
		Row row1 = sheet.createRow(rowCount);
		int columnCount2 = 6;
		appExcelUtility.createCell(row1, columnCount2++, "TOTAL", leftFont);
		appExcelUtility.createCell(row1, columnCount2++, grandTotal, totalStyle);

		rowCount++;
		return rowCount;
	}

	private int addSoaToExcel(int count, List<QisSOA> soaList) {
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

		CellStyle styleGreen = workbook.createCellStyle();
		XSSFFont fontGreen = workbook.createFont();
		fontGreen.setColor(IndexedColors.GREEN.getIndex());
		styleGreen.setAlignment(HorizontalAlignment.RIGHT);
		styleGreen.setFont(fontGreen);

		CellStyle styleRed = workbook.createCellStyle();
		styleRed.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("###0,00"));
		XSSFFont fontRed = workbook.createFont();
		fontRed.setBold(true);
		fontRed.setColor(IndexedColors.RED.getIndex());
		styleRed.setFont(fontRed);

		CellStyle soaAmountStyle = workbook.createCellStyle();
		soaAmountStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("###0,00"));

		CellStyle totalStyle = workbook.createCellStyle();
		totalStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("###0,00"));
		XSSFFont fontTotal2 = workbook.createFont();
		fontTotal2.setBold(true);
		totalStyle.setFont(fontTotal2);
		totalStyle.setBorderTop(BorderStyle.DOUBLE);

		Collections.sort(soaList);
		ArrayList<QisSOA> soaListNoDuplicate = new ArrayList<>();

		for (QisSOA i : soaList) {
			if (!soaListNoDuplicate.contains(i)) {
				soaListNoDuplicate.add(i);
			}
		}
		double totalSoaAmount = 0d;
		double totalSoaAmountBlance = 0d;

		for (QisSOA soa : soaListNoDuplicate) {
			double amount = 0d;
			for (QisTransaction listTransaction : soa.getTransactions()) {
				if (!listTransaction.isSoaStatus()) {
					amount += listTransaction.getTotalItemAmountDue();
				}
			}

			String status = "";
			QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(soa.getChargeTo().getCorporateid());
			List<QisSOAPayment> soaPaymentList = null;
			if (qisCorporate != null) {
				int Year = Integer.parseInt(appUtility.calendarToFormatedDate(soa.getStatementDate(), YEARDATE));
				soaPaymentList = qisSOAPaymentRepository.getSOAPaymentYearList(qisCorporate.getId(), Year);
			}
			Long paidVerified = null;
			Long auditedBy = null;
			String controlNumber = "";
			if (soaPaymentList != null) {
				for (QisSOAPayment soaPayment : soaPaymentList) {
					for (QisSOA soaPaid : soaPayment.getSoaList()) {
						if (soaPaid.getId() == soa.getId()) {
							if (soaPayment.getAuditedBy() != null) {
								status = "AUDITED";
								auditedBy = soaPayment.getAuditedBy();
								controlNumber = soaPayment.getControlNumber();
							} else {
								status = "CREDITED";
								paidVerified = soaPayment.getVerifiedBy();
							}
						}
					}
				}
			}

			int days = (int) ChronoUnit.DAYS.between(soa.getStatementDate().toInstant(),
					Calendar.getInstance().toInstant());

			int day = days % 30;
			int month = days / 30;
			String Aging = "";
			if (0 < month) {
				if (month == 1) {
					if (1 < day) {
						Aging = month + " Month and " + day + " Days";
					} else {
						Aging = month + " Month and " + day + " Day";
					}
				} else {
					if (1 < day) {
						Aging = month + " Months and " + day + " Days";
					} else {
						Aging = month + " Months and " + day + " Day";
					}
				}
			} else {
				if (1 < day) {
					Aging = day + " Days";
				} else {
					Aging = day + " Day";
				}
			}

			if (!unbilledTransaction &&!soaPrepared && !soaVerified && !soaNoted && !soaSend && !soaRecieved && !soaPaymentPrepared
					&& !soaPaymentBalance && !soaPaymentVerified && !soaPaymentAudited) {
				Row row = sheet.createRow(rowCount++);
				int columnCount = 0;

				if (auditedBy != null) {
					status = "AUDITED";
				} else if (paidVerified != null) {
					status = "CREDITED";
				} else if (amount == 0) {
					status = "COLLECTED";
				} else if (amount != soa.getSoaAmount()) {
					status = "PAID w/ BALANCE";
				} else if (soa.isSoaRecieved()) {
					status = "SOA RECEIVED";
				} else if (soa.isSoaSend()) {
					status = "SOA SEND";
				} else if (soa.getNotedBy() != null) {
					status = "SOA NOTED";
				} else if (soa.getVerifiedBy() != null) {
					status = "SOA VERIFIED";
				} else if (soa.getCreatedBy() != null) {
					status = "SOA PREPARED";
				}

				totalSoaAmount += soa.getSoaAmount();
				totalSoaAmountBlance += amount;

				appExcelUtility.createCell(row, columnCount++, soa.getSoaNumber(), leftFont);
				appExcelUtility.createCell(row, columnCount++,
						appUtility.calendarToFormatedDate(soa.getStatementDate(), SHORTDATE2), leftFont);
				appExcelUtility.createCell(
						row, columnCount++, appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), SHORTDATE)
								+ " to " + appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), SHORTDATE),
						leftFont);
				appExcelUtility.createCell(row, columnCount++, soa.getSoaAmount(), soaAmountStyle);
				appExcelUtility.createCell(row, columnCount++, amount == 0 ? amount : amount,
						amount == 0 ? styleGreen : styleRed);
				appExcelUtility.createCell(row, columnCount++, auditedBy != null ? controlNumber : "", leftFont);
				appExcelUtility.createCell(row, columnCount++, soa.getPurchaseOrder() != null && !"".equals(soa.getPurchaseOrder()) ? soa.getPurchaseOrder() : "", leftFont);
				appExcelUtility.createCell(row, columnCount++, amount == 0 ? "PAID" : Aging, leftFont);
				appExcelUtility.createCell(row, columnCount++, status, leftFont);

			} else {
				if (auditedBy != null && soaPaymentAudited) {
					Row row = sheet.createRow(rowCount++);
					int columnCount = 0;
					appExcelUtility.createCell(row, columnCount++, soa.getSoaNumber(), leftFont);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.calendarToFormatedDate(soa.getStatementDate(), SHORTDATE2), leftFont);
					appExcelUtility.createCell(
							row, columnCount++, appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), SHORTDATE)
									+ " to " + appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), SHORTDATE),
							leftFont);

					appExcelUtility.createCell(row, columnCount++, soa.getSoaAmount(), soaAmountStyle);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? amount : amount,
							amount == 0 ? styleGreen : styleRed);
					totalSoaAmount += soa.getSoaAmount();
					totalSoaAmountBlance += amount;
					appExcelUtility.createCell(row, columnCount++, auditedBy != null ? controlNumber : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, soa.getPurchaseOrder() != null && !"".equals(soa.getPurchaseOrder()) ? soa.getPurchaseOrder() : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? "PAID" : Aging, leftFont);
					appExcelUtility.createCell(row, columnCount++, "AUDITED", leftFont);
				} else if (paidVerified != null && soaPaymentVerified && auditedBy == null) {
					Row row = sheet.createRow(rowCount++);
					int columnCount = 0;
					appExcelUtility.createCell(row, columnCount++, soa.getSoaNumber(), leftFont);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.calendarToFormatedDate(soa.getStatementDate(), SHORTDATE2), leftFont);
					appExcelUtility.createCell(
							row, columnCount++, appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), SHORTDATE)
									+ " to " + appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), SHORTDATE),
							leftFont);

					appExcelUtility.createCell(row, columnCount++, soa.getSoaAmount(), soaAmountStyle);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? amount : amount,
							amount == 0 ? styleGreen : styleRed);
					totalSoaAmount += soa.getSoaAmount();
					totalSoaAmountBlance += amount;
					appExcelUtility.createCell(row, columnCount++, auditedBy != null ? controlNumber : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, soa.getPurchaseOrder() != null && !"".equals(soa.getPurchaseOrder()) ? soa.getPurchaseOrder() : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? "PAID" : Aging, leftFont);
					appExcelUtility.createCell(row, columnCount++, "CREDITED", leftFont);
				} else if (amount == 0 && soaPaymentPrepared && paidVerified == null && auditedBy == null) {
					Row row = sheet.createRow(rowCount++);
					int columnCount = 0;
					appExcelUtility.createCell(row, columnCount++, soa.getSoaNumber(), leftFont);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.calendarToFormatedDate(soa.getStatementDate(), SHORTDATE2), leftFont);
					appExcelUtility.createCell(
							row, columnCount++, appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), SHORTDATE)
									+ " to " + appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), SHORTDATE),
							leftFont);

					appExcelUtility.createCell(row, columnCount++, soa.getSoaAmount(), soaAmountStyle);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? amount : amount,
							amount == 0 ? styleGreen : styleRed);
					totalSoaAmount += soa.getSoaAmount();
					totalSoaAmountBlance += amount;
					appExcelUtility.createCell(row, columnCount++, auditedBy != null ? controlNumber : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, soa.getPurchaseOrder() != null && !"".equals(soa.getPurchaseOrder()) ? soa.getPurchaseOrder() : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? "PAID" : Aging, leftFont);
					appExcelUtility.createCell(row, columnCount++, "COLLECTED", leftFont);
				} else if (amount != soa.getSoaAmount() && soaPaymentBalance && amount != 0 && paidVerified == null
						&& auditedBy == null) {
					Row row = sheet.createRow(rowCount++);
					int columnCount = 0;
					appExcelUtility.createCell(row, columnCount++, soa.getSoaNumber(), leftFont);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.calendarToFormatedDate(soa.getStatementDate(), SHORTDATE2), leftFont);
					appExcelUtility.createCell(
							row, columnCount++, appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), SHORTDATE)
									+ " to " + appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), SHORTDATE),
							leftFont);

					appExcelUtility.createCell(row, columnCount++, soa.getSoaAmount(), soaAmountStyle);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? amount : amount,
							amount == 0 ? styleGreen : styleRed);
					totalSoaAmount += soa.getSoaAmount();
					totalSoaAmountBlance += amount;
					appExcelUtility.createCell(row, columnCount++, auditedBy != null ? controlNumber : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, soa.getPurchaseOrder() != null && !"".equals(soa.getPurchaseOrder()) ? soa.getPurchaseOrder() : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? "PAID" : Aging, leftFont);
					appExcelUtility.createCell(row, columnCount++, "PAID w/ BALANCE", leftFont);
				} else if (soa.isSoaRecieved() && soaRecieved && amount == soa.getSoaAmount() && amount != 0
						&& paidVerified == null && auditedBy == null) {
					Row row = sheet.createRow(rowCount++);
					int columnCount = 0;
					appExcelUtility.createCell(row, columnCount++, soa.getSoaNumber(), leftFont);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.calendarToFormatedDate(soa.getStatementDate(), SHORTDATE2), leftFont);
					appExcelUtility.createCell(
							row, columnCount++, appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), SHORTDATE)
									+ " to " + appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), SHORTDATE),
							leftFont);

					appExcelUtility.createCell(row, columnCount++, soa.getSoaAmount(), soaAmountStyle);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? amount : amount,
							amount == 0 ? styleGreen : styleRed);
					totalSoaAmount += soa.getSoaAmount();
					totalSoaAmountBlance += amount;
					appExcelUtility.createCell(row, columnCount++, auditedBy != null ? controlNumber : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, soa.getPurchaseOrder() != null && !"".equals(soa.getPurchaseOrder()) ? soa.getPurchaseOrder() : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? "PAID" : Aging, leftFont);
					appExcelUtility.createCell(row, columnCount++, "SOA RECEIVED", leftFont);
				} else if (soa.isSoaSend() && soaSend && !soa.isSoaRecieved() && amount == soa.getSoaAmount()
						&& amount != 0 && paidVerified == null && auditedBy == null) {
					Row row = sheet.createRow(rowCount++);
					int columnCount = 0;
					appExcelUtility.createCell(row, columnCount++, soa.getSoaNumber(), leftFont);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.calendarToFormatedDate(soa.getStatementDate(), SHORTDATE2), leftFont);
					appExcelUtility.createCell(
							row, columnCount++, appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), SHORTDATE)
									+ " to " + appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), SHORTDATE),
							leftFont);

					appExcelUtility.createCell(row, columnCount++, soa.getSoaAmount(), soaAmountStyle);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? amount : amount,
							amount == 0 ? styleGreen : styleRed);
					totalSoaAmount += soa.getSoaAmount();
					totalSoaAmountBlance += amount;
					appExcelUtility.createCell(row, columnCount++, auditedBy != null ? controlNumber : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, soa.getPurchaseOrder() != null && !"".equals(soa.getPurchaseOrder()) ? soa.getPurchaseOrder() : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? "PAID" : Aging, leftFont);
					appExcelUtility.createCell(row, columnCount++, "SOA SEND", leftFont);
				} else if (soa.getNotedBy() != null && soaNoted && !soa.isSoaSend() && !soa.isSoaRecieved()
						&& amount == soa.getSoaAmount() && amount != 0 && paidVerified == null && auditedBy == null) {
					Row row = sheet.createRow(rowCount++);
					int columnCount = 0;
					appExcelUtility.createCell(row, columnCount++, soa.getSoaNumber(), leftFont);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.calendarToFormatedDate(soa.getStatementDate(), SHORTDATE2), leftFont);
					appExcelUtility.createCell(
							row, columnCount++, appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), SHORTDATE)
									+ " to " + appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), SHORTDATE),
							leftFont);

					appExcelUtility.createCell(row, columnCount++, soa.getSoaAmount(), soaAmountStyle);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? amount : amount,
							amount == 0 ? styleGreen : styleRed);
					totalSoaAmount += soa.getSoaAmount();
					totalSoaAmountBlance += amount;
					appExcelUtility.createCell(row, columnCount++, auditedBy != null ? controlNumber : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, soa.getPurchaseOrder() != null && !"".equals(soa.getPurchaseOrder()) ? soa.getPurchaseOrder() : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? "PAID" : Aging, leftFont);
					appExcelUtility.createCell(row, columnCount++, "SOA NOTED", leftFont);
				} else if (soa.getVerifiedBy() != null && soaVerified && soa.getNotedBy() == null && !soa.isSoaSend()
						&& !soa.isSoaRecieved() && amount == soa.getSoaAmount() && amount != 0 && paidVerified == null
						&& auditedBy == null) {
					Row row = sheet.createRow(rowCount++);
					int columnCount = 0;
					appExcelUtility.createCell(row, columnCount++, soa.getSoaNumber(), leftFont);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.calendarToFormatedDate(soa.getStatementDate(), SHORTDATE2), leftFont);
					appExcelUtility.createCell(
							row, columnCount++, appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), SHORTDATE)
									+ " to " + appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), SHORTDATE),
							leftFont);

					appExcelUtility.createCell(row, columnCount++, soa.getSoaAmount(), soaAmountStyle);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? amount : amount,
							amount == 0 ? styleGreen : styleRed);
					totalSoaAmount += soa.getSoaAmount();
					totalSoaAmountBlance += amount;
					appExcelUtility.createCell(row, columnCount++, auditedBy != null ? controlNumber : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, soa.getPurchaseOrder() != null && !"".equals(soa.getPurchaseOrder()) ? soa.getPurchaseOrder() : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? "PAID" : Aging, leftFont);
					appExcelUtility.createCell(row, columnCount++, "SOA VERIFIED", leftFont);
				} else if (soa.getCreatedBy() != null && soaPrepared && soa.getVerifiedBy() == null
						&& soa.getNotedBy() == null && !soa.isSoaSend() && !soa.isSoaRecieved()
						&& amount == soa.getSoaAmount() && amount != 0 && paidVerified == null && auditedBy == null) {
					Row row = sheet.createRow(rowCount++);
					int columnCount = 0;
					appExcelUtility.createCell(row, columnCount++, soa.getSoaNumber(), leftFont);
					appExcelUtility.createCell(row, columnCount++,
							appUtility.calendarToFormatedDate(soa.getStatementDate(), SHORTDATE2), leftFont);
					appExcelUtility.createCell(
							row, columnCount++, appUtility.calendarToFormatedDate(soa.getCoverageDateFrom(), SHORTDATE)
									+ " to " + appUtility.calendarToFormatedDate(soa.getCoverageDateTo(), SHORTDATE),
							leftFont);

					appExcelUtility.createCell(row, columnCount++, soa.getSoaAmount(), soaAmountStyle);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? amount : amount,
							amount == 0 ? styleGreen : styleRed);
					totalSoaAmount += soa.getSoaAmount();
					totalSoaAmountBlance += amount;
					appExcelUtility.createCell(row, columnCount++, auditedBy != null ? controlNumber : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, soa.getPurchaseOrder() != null && !"".equals(soa.getPurchaseOrder()) ? soa.getPurchaseOrder() : "", leftFont);
					appExcelUtility.createCell(row, columnCount++, amount == 0 ? "PAID" : Aging, leftFont);
					appExcelUtility.createCell(row, columnCount++, "SOA PREPARED", leftFont);
				}

			}

		}

		Row row1 = sheet.createRow(rowCount);
		int columnCount2 = 2;
		appExcelUtility.createCell(row1, columnCount2++, "TOTAL", leftFont);
		appExcelUtility.createCell(row1, columnCount2++, totalSoaAmount, totalStyle);
		appExcelUtility.createCell(row1, columnCount2++, totalSoaAmountBlance, totalStyle);

		rowCount++;
		for (int i = 1; i < 10; i++) {
			Row row = sheet.createRow(rowCount);
			int columnCount = 2;
			String lvl = "";

			switch (i) {
			case 1:
				lvl = "SOA PREPARED";
				break;
			case 2:
				lvl = "SOA VERIFIED";
				break;
			case 3:
				lvl = "SOA NOTED";
				break;
			case 4:
				lvl = "SOA SEND";
				break;
			case 5:
				lvl = "SOA RECEIVED";
				break;
			case 6:
				lvl = "COLLECTED";
				break;
			case 7:
				lvl = "PAID with BALANCE";
				break;
			case 8:
				lvl = "CREDITED";
				break;
			case 9:
				lvl = "AUDITED";
				break;
			default:
				break;
			}

			appExcelUtility.createCell(row, columnCount++, "LEVEL " + i + "-" + lvl, leftFont);

			rowCount++;
		}
		rowCount++;

		return rowCount;
	}
}
