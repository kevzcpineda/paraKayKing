package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quest.phil.diagnostic.information.system.ws.model.QisCupmedarExcel;

public class QisCupmedarRequest {

	public static ByteArrayInputStream contactListToExcelFile(List<QisCupmedarExcel> qisCupmedarExcel) {
		try(Workbook workbook = new XSSFWorkbook()){
			Iterable<org.apache.poi.ss.usermodel.Row> sheet = workbook.createSheet("Cupmedar");
			
			Row row = ((Sheet) sheet).createRow(0);
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
	        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        // Creating header
	        Cell cell = row.createCell(0);
	        cell.setCellValue("Date and Time");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(1);
	        cell.setCellValue("Receipt No.");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(2);
	        cell.setCellValue("Patient Name");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(3);
	        cell.setCellValue("Company Name");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(4);
	        cell.setCellValue("Items");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(5);
	        cell.setCellValue("C");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(6);
	        cell.setCellValue("U");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(7);
	        cell.setCellValue("P");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(8);
	        cell.setCellValue("M");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(9);
	        cell.setCellValue("E");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(10);
	        cell.setCellValue("D");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(11);
	        cell.setCellValue("A");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(12);
	        cell.setCellValue("R");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(13);
	        cell.setCellValue("Remarks");
	        cell.setCellStyle(headerCellStyle);
	        
	        // Creating data rows for each customer
	        for(int i = 0; i < qisCupmedarExcel.size(); i++) {
	        	
	        	Row dataRow = ((Sheet) sheet).createRow(i + 1);
	        	dataRow.createCell(0).setCellValue(qisCupmedarExcel.get(i).getDateAndTime());
	        	dataRow.createCell(1).setCellValue(qisCupmedarExcel.get(i).getRecieptNo());
	        	dataRow.createCell(2).setCellValue(qisCupmedarExcel.get(i).getPatientName());
	        	dataRow.createCell(3).setCellValue(qisCupmedarExcel.get(i).getCompanyName());
	        	dataRow.createCell(4).setCellValue(qisCupmedarExcel.get(i).getItems());
	        	dataRow.createCell(5).setCellValue(qisCupmedarExcel.get(i).getPatientCalled());
	        	dataRow.createCell(6).setCellValue(qisCupmedarExcel.get(i).getResultsUploaded());
	        	dataRow.createCell(7).setCellValue(qisCupmedarExcel.get(i).getResultsPrinted());
	        	dataRow.createCell(8).setCellValue(qisCupmedarExcel.get(i).getResultsEmail());
	        	dataRow.createCell(9).setCellValue(qisCupmedarExcel.get(i).getResultsEncoded());
	        	dataRow.createCell(10).setCellValue(qisCupmedarExcel.get(i).getResultsDone());
	        	dataRow.createCell(11).setCellValue(qisCupmedarExcel.get(i).getAcknowledge());
	        	dataRow.createCell(12).setCellValue(qisCupmedarExcel.get(i).getPatientRecieved());
	        	dataRow.createCell(13).setCellValue(qisCupmedarExcel.get(i).getSendOutPending());
	        	dataRow.createCell(14).setCellValue(qisCupmedarExcel.get(i).getFitToWork());
	        	dataRow.createCell(15).setCellValue(qisCupmedarExcel.get(i).getForPickUp());
	        }
	
	        // Making size of column auto resize to fit with data
	        ((Sheet) sheet).autoSizeColumn(0);
	        ((Sheet) sheet).autoSizeColumn(1);
	        ((Sheet) sheet).autoSizeColumn(2);
	        ((Sheet) sheet).autoSizeColumn(3);
	        ((Sheet) sheet).autoSizeColumn(4);
	        ((Sheet) sheet).autoSizeColumn(5);
	        ((Sheet) sheet).autoSizeColumn(6);
	        ((Sheet) sheet).autoSizeColumn(7);
	        ((Sheet) sheet).autoSizeColumn(8);
	        ((Sheet) sheet).autoSizeColumn(9);
	        ((Sheet) sheet).autoSizeColumn(10);
	        ((Sheet) sheet).autoSizeColumn(11);
	        ((Sheet) sheet).autoSizeColumn(12);
	        ((Sheet) sheet).autoSizeColumn(13);
	        ((Sheet) sheet).autoSizeColumn(14);
	        ((Sheet) sheet).autoSizeColumn(15);
	        
	        
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        workbook.write(outputStream);
	        return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
