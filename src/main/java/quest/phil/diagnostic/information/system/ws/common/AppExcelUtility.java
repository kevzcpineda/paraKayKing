package quest.phil.diagnostic.information.system.ws.common;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class AppExcelUtility {

	public int qisExcelHeader(XSSFWorkbook workbook, XSSFSheet sheet, int count, int mergeEnd, String title,
			String subTitle, String reportDate) {
		int currentRow = count;

		CellStyle styleTitle = workbook.createCellStyle();
		XSSFFont fontTitle = workbook.createFont();
		fontTitle.setBold(true);
		fontTitle.setFontHeight(14);
		styleTitle.setFont(fontTitle);
		styleTitle.setAlignment(HorizontalAlignment.CENTER);

		CellStyle styleSubTitle = workbook.createCellStyle();
		XSSFFont fontSubTitle = workbook.createFont();
		fontSubTitle.setBold(true);
		fontSubTitle.setFontHeight(12);
		styleSubTitle.setFont(fontSubTitle);
		styleSubTitle.setAlignment(HorizontalAlignment.CENTER);

		Row rowTitle = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, mergeEnd));
		createCell(rowTitle, 0, title, styleTitle);
		currentRow++;

		Row rowSubTitle = sheet.createRow(currentRow);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, mergeEnd));
		createCell(rowSubTitle, 0, subTitle, styleSubTitle);
		currentRow++;

		if (reportDate != null && !"".equals(reportDate)) {
			Row rowReportDate = sheet.createRow(currentRow);
			sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, mergeEnd));
			createCell(rowReportDate, 0, reportDate, styleSubTitle);
			currentRow++;
		}

		currentRow++;

		return currentRow;
	}

	public void createCell(Row row, int columnCount, Object value, CellStyle style) {
//		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Float) {
			cell.setCellValue((Float) value);
		} else if (value instanceof Double) {
			cell.setCellValue((Double) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	public int createCellHeader(int i, int j, String string, CellStyle style) {
		return 0;
	}

}
