package quest.phil.diagnostic.information.system.ws.common.exports;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quest.phil.diagnostic.information.system.ws.common.AppExcelUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransactionLaboratory;

public class QuestQualityExport {
	private String applicationName;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private AppUtility appUtility;
	private AppExcelUtility appExcelUtility;
	private List<QisQualityTransaction> mainList;
	private String dateTimeFrom;
	private String dateTimeTo;
	private final String LONGDATE = "yyyyMMddHHmm";
	private final String pLONGDATE = "MMM-dd-yyyy HH:mm aa";

	public QuestQualityExport(List<QisQualityTransaction> mainList, String applicationName, AppUtility appUtility,
			AppExcelUtility appExcelUtility, String dateTimeFrom, String dateTimeTo) throws IOException {
		super();

		workbook = new XSSFWorkbook();
		this.mainList = mainList;
		this.applicationName = applicationName;
		this.appUtility = appUtility;
		this.appExcelUtility = appExcelUtility;
		this.dateTimeFrom = dateTimeFrom;
		this.dateTimeTo = dateTimeTo;

	}

	public void export(HttpServletResponse response) throws IOException {

		sheet = workbook.createSheet("QuestQuality");
		sheet.setColumnWidth(0, 6 * 256);
		sheet.setColumnWidth(1, 20 * 256);
		sheet.setColumnWidth(2, 20 * 256);
		sheet.setColumnWidth(3, 30 * 256);
		sheet.setColumnWidth(4, 10 * 256);
		sheet.setColumnWidth(5, 15 * 256);
		sheet.setColumnWidth(6, 12 * 256);
		sheet.setColumnWidth(7, 12 * 256);
		sheet.setColumnWidth(8, 10 * 256);
		sheet.setColumnWidth(9, 20 * 256);
		sheet.setColumnWidth(10, 26 * 256);
		sheet.setColumnWidth(11, 20 * 256);
		sheet.setColumnWidth(12, 20 * 256);

		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	private void writeDataLines() throws FileNotFoundException, IOException {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		font.setBold(true);
		style.setFont(font);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);

		Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
		Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);
		String start = appUtility.calendarToFormatedDate(txnDateTimeFrom, "yyyy-MM-dd HH:mm:ss");
		String end = appUtility.calendarToFormatedDate(txnDateTimeTo, "yyyy-MM-dd HH:mm:ss");

		int rowCount = 1;

		String timeGeneratedet = appUtility.calendarToFormatedDate(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");

		Row rowTime = sheet.createRow(0);

		Cell cellTime = rowTime.createCell(0);
		cellTime.setCellValue(timeGeneratedet);
		// HEADER
		rowCount = appExcelUtility.qisExcelHeader(workbook, sheet, rowCount, 11, applicationName, "SUMMARY REPORT",
				start + " to " + end);

		Row rowHeader0 = sheet.createRow(4);

		Cell cellHeader0 = rowHeader0.createCell(0);
		cellHeader0.setCellValue("SR #");
		cellHeader0.setCellStyle(style);

		Cell cellHeader1 = rowHeader0.createCell(1);
		cellHeader1.setCellValue("Transaction Date");
		cellHeader1.setCellStyle(style);

		Cell cellHeader2 = rowHeader0.createCell(2);
		cellHeader2.setCellValue("COMPANY");
		cellHeader2.setCellStyle(style);

		Cell cellHeader3 = rowHeader0.createCell(3);
		cellHeader3.setCellValue("NAME");
		cellHeader3.setCellStyle(style);

		Cell cellHeader4 = rowHeader0.createCell(4);
		cellHeader4.setCellValue("AGE/SEX");
		cellHeader4.setCellStyle(style);

		Cell cellHeader5 = rowHeader0.createCell(5);
		cellHeader5.setCellValue("CHEST X-RAY");
		cellHeader5.setCellStyle(style);

		Cell cellHeader6 = rowHeader0.createCell(6);
		cellHeader6.setCellValue("URINALYSIS");
		cellHeader6.setCellStyle(style);

		Cell cellHeader7 = rowHeader0.createCell(7);
		cellHeader7.setCellValue("FECALYSIS");
		cellHeader7.setCellStyle(style);

		Cell cellHeader8 = rowHeader0.createCell(8);
		cellHeader8.setCellValue("CBC");
		cellHeader8.setCellStyle(style);

		Cell cellHeader9 = rowHeader0.createCell(9);
		cellHeader9.setCellValue("PHYSICAL EXAM");
		cellHeader9.setCellStyle(style);

		Cell cellHeader10 = rowHeader0.createCell(10);
		cellHeader10.setCellValue("METHAMPHETHAMINE");
		cellHeader10.setCellStyle(style);
		
		Cell cellHeader11 = rowHeader0.createCell(11);
		cellHeader11.setCellValue("TETRAHYDROCANABINOL");
		cellHeader11.setCellStyle(style);

		Cell cellHeader12 = rowHeader0.createCell(12);
		cellHeader12.setCellValue("MEDICAL CLASSIFICATION");
		cellHeader12.setCellStyle(style);

		Cell cellHeader13 = rowHeader0.createCell(13);
		cellHeader13.setCellValue("REMARKS");
		cellHeader13.setCellStyle(style);

		rowCount = appExcelUtility.createCellHeader(3, 3, "Transaction ID", style);
		int y = 6;
		for (int i = 0; i < mainList.size(); i++) {
			String classification = "Class ";
			String remarks = "";
			String xRay = "";
			String urine = "";
			String stool = "";
			String blood = "";
			String pe = "";
			String metaResult = "";
			String tetraResult = "";
			boolean hasDT = false;
			for (QisQualityTransactionItem value : mainList.get(i).getTransactionItems()) {
				remarks = value.getOverAllFindings();
				if (value.getClassification() != null) {
					if ("P".equals(value.getClassification()) || "E".equals(value.getClassification()) || "F".equals(value.getClassification())) {
						classification = "PENDING";
					} else {
						classification += value.getClassification();
					}
				} else {
					classification = "Not Classify";
				}

				for (QisQualityTransactionLaboratory xValue : value.getItemLaboratories()) {
					if ("XR".equals(xValue.getItemLaboratory())) {
						if (xValue.getXray() == null) {
							xRay += "";
						} else {
							if ("NORMAL CHEST FINDINGS".equals(xValue.getXray().getImpressions())
									|| "NORMAL CHEST FINDINGS.".equals(xValue.getXray().getImpressions())) {
								xRay += "NORMAL";
							} else {
								xRay += xValue.getXray().getImpressions();
							}
						}
					}
					if ("UR".equals(xValue.getItemLaboratory())) {
						if (xValue.getToxicology() != null) {
							if (xValue.getToxicology().getMethamphethamine() != null
									&& xValue.getToxicology().getMethamphethamine() == false) {
								metaResult += "NEGATIVE";
								hasDT = true;
							} else {
								metaResult += "POSITIVE";
								hasDT = true;
							}
						}
					}
					
					if ("UR".equals(xValue.getItemLaboratory())) {
						if (xValue.getToxicology() != null) {
							if (xValue.getToxicology().getTetrahydrocanabinol() != null
									&& xValue.getToxicology().getTetrahydrocanabinol() == false) {
								tetraResult += "NEGATIVE";
								hasDT = true;
							} else {
								tetraResult += "POSITIVE";
								hasDT = true;
							}
						}
					}

					if ("UR".equals(xValue.getItemLaboratory())) {
						if (xValue.getClinicalMicroscopy().getUrineChemical() == null) {
							urine += "";
						} else {
							urine += xValue.getClinicalMicroscopy().getUrineChemical().getOtherNotes();
						}
					}

					if ("ST".equals(xValue.getItemLaboratory())) {
						if (xValue.getClinicalMicroscopy().getFecalysis() != null) {
							if (xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes() != null
									&& !"".equals(xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes())) {
								if ("NORMAL".equals(xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes())) {
									stool += "NIPS";
								} else {
									stool += xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes();
								}
							} else {
								stool += xValue.getOtherNotes();
							}
						}
					}

					if ("BL".equals(xValue.getItemLaboratory())) {
						if(xValue.getOtherNotes() == null) {
							blood = "";
						}else {
							blood += xValue.getOtherNotes();
						}
					}

					if ("PE".equals(xValue.getItemLaboratory())) {
						if (xValue.getPhysicalExamination().getPhysicalExam() == null) {
							pe += "";
						} else {
							if (xValue.getPhysicalExamination().getPhysicalExam().getRemarks() == true) {
								pe += xValue.getPhysicalExamination().getPhysicalExam().getFindings();
							} else {
								pe += "NORMAL";
							}
						}
					}
				}
			}

			String gender = "MALE";
			if ("F".equals(mainList.get(i).getPatient().getGender())) {
				gender = "FEMALE";
			}
			String age = String.valueOf(appUtility.calculateAgeInYear(mainList.get(i).getPatient().getDateOfBirth(),
					mainList.get(i).getTransactionDate()));

			String ageGender = age + "/" + gender;
			String transactionDate = appUtility.calendarToFormatedDate(mainList.get(i).getTransactionDate(), pLONGDATE);
			Row row = sheet.createRow(y + i);

			Cell cell0 = row.createCell(0);
			cell0.setCellValue(mainList.get(i).getId());

			Cell cell1 = row.createCell(1);
			cell1.setCellValue(transactionDate);

			Cell cell2 = row.createCell(2);
			cell2.setCellValue(mainList.get(i).getBiller());

			Cell cell3 = row.createCell(3);
			cell3.setCellValue(appUtility.getPatientFullname(mainList.get(i).getPatient()));

			Cell cell4 = row.createCell(4);
			cell4.setCellValue(ageGender);

			Cell cell5 = row.createCell(5);
			cell5.setCellValue(xRay);

			Cell cell6 = row.createCell(6);
			cell6.setCellValue(urine);

			Cell cell7 = row.createCell(7);
			cell7.setCellValue(stool);

			Cell cell8 = row.createCell(8);
			cell8.setCellValue(blood);

			Cell cell9 = row.createCell(9);
			cell9.setCellValue(pe);

			Cell cell10 = row.createCell(10);
			if (hasDT == true) {
				cell10.setCellValue(metaResult);
			} else {
				cell10.setCellValue(metaResult);
			}
			
			Cell cell11 = row.createCell(11);
			if (hasDT == true) {
				cell11.setCellValue(tetraResult);
			} else {
				cell11.setCellValue(tetraResult);
			}

			Cell cell12 = row.createCell(12);
			CellStyle stylePending = workbook.createCellStyle();
			XSSFFont fontPending = workbook.createFont();
			fontPending.setColor(XSSFFont.COLOR_RED);
			stylePending.setFont(fontPending);
			if (classification == "PENDING" || classification == "Not Classify") {
				cell12.setCellStyle(stylePending);
				cell12.setCellValue(classification);
			} else {
				cell12.setCellValue(classification);
			}

			Cell cell13 = row.createCell(13);
			if (classification == "PENDING" || classification == "Not Classify") {
				cell13.setCellValue(remarks);
			} else {
				cell13.setCellValue("FIT TO WORK");
			}

		}

	}
}
