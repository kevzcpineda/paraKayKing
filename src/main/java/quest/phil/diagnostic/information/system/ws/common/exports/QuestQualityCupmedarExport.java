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
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quest.phil.diagnostic.information.system.ws.common.AppExcelUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransactionLaboratory;

public class QuestQualityCupmedarExport {
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
	private String chargeTo;
	private String company;

	public QuestQualityCupmedarExport(List<QisQualityTransaction> mainList, String chargeTo, String company,
			String applicationName, AppUtility appUtility, AppExcelUtility appExcelUtility, String dateTimeFrom,
			String dateTimeTo) throws IOException {
		super();

		workbook = new XSSFWorkbook();
		this.mainList = mainList;
		this.applicationName = applicationName;
		this.appUtility = appUtility;
		this.appExcelUtility = appExcelUtility;
		this.dateTimeFrom = dateTimeFrom;
		this.dateTimeTo = dateTimeTo;
		this.chargeTo = chargeTo;
		this.company = company;

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
		String print = "";
		String send = "";

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
		rowCount = appExcelUtility.qisExcelHeader(workbook, sheet, rowCount, 11, applicationName, "CUPMEDAR",
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

		Cell cellHeaderItems = rowHeader0.createCell(5);
		cellHeaderItems.setCellValue("ITEMS");
		cellHeaderItems.setCellStyle(style);

		Cell cellHeader5 = rowHeader0.createCell(6);
		cellHeader5.setCellValue("CHEST X-RAY");
		cellHeader5.setCellStyle(style);

		Cell cellHeader6 = rowHeader0.createCell(7);
		cellHeader6.setCellValue("URINALYSIS");
		cellHeader6.setCellStyle(style);

		Cell cellHeader7 = rowHeader0.createCell(8);
		cellHeader7.setCellValue("FECALYSIS");
		cellHeader7.setCellStyle(style);

		Cell cellHeader8 = rowHeader0.createCell(9);
		cellHeader8.setCellValue("CBC");
		cellHeader8.setCellStyle(style);

		Cell cellHeader9 = rowHeader0.createCell(10);
		cellHeader9.setCellValue("PHYSICAL EXAM");
		cellHeader9.setCellStyle(style);

		Cell cellHeader10 = rowHeader0.createCell(11);
		cellHeader10.setCellValue("MEDICAL CLASSIFICATION");
		cellHeader10.setCellStyle(style);

		Cell cellHeader11 = rowHeader0.createCell(12);
		cellHeader11.setCellValue("REMARKS");
		cellHeader11.setCellStyle(style);

		Cell C = rowHeader0.createCell(13);
		C.setCellValue("C");
		C.setCellStyle(style);

		Cell U = rowHeader0.createCell(14);
		U.setCellValue("U");
		U.setCellStyle(style);

		Cell P = rowHeader0.createCell(15);
		P.setCellValue("P");
		P.setCellStyle(style);

		Cell M = rowHeader0.createCell(16);
		M.setCellValue("M");
		M.setCellStyle(style);

		Cell E = rowHeader0.createCell(17);
		E.setCellValue("E");
		E.setCellStyle(style);

		Cell D = rowHeader0.createCell(18);
		D.setCellValue("D");
		D.setCellStyle(style);

		Cell A = rowHeader0.createCell(19);
		A.setCellValue("A");
		A.setCellStyle(style);

		Cell R = rowHeader0.createCell(20);
		R.setCellValue("R");
		R.setCellStyle(style);

		rowCount = appExcelUtility.createCellHeader(3, 3, "Transaction ID", style);
		int account = 6;

		Row accountTitle = sheet.createRow(5);
		Cell cellAccountTitle = accountTitle.createCell(0);
		cellAccountTitle.setCellValue("ACCOUNT");
		if (company == null) {
			for (int i = 0; i < mainList.size(); i++) {
				String classification = "";
				String remarks = "";
				String xRay = "";
				String urine = "";
				String stool = "";
				String blood = "";
				String pe = "";
				String items = "";

				for (QisQualityTransactionItem value : mainList.get(i).getTransactionItems()) {

					remarks = value.getOverAllFindings();
					if (value.getClassification() != null) {
						if ("P".equals(value.getClassification()) || "E".equals(value.getClassification()) || "F".equals(value.getClassification())) {
							classification = "PENDING";
						} else {
							classification += "Class " + value.getClassification();
						}
					}

					for (QisQualityTransactionLaboratory xValue : value.getItemLaboratories()) {
						if (xValue.getTransactionItem().getQisPackage() != null) {
							items = xValue.getTransactionItem().getQisPackage().getPackageDescription();
						}

						if (xValue.getTransactionItem().getQisItem() != null) {
							items += xValue.getTransactionItem().getQisItem().getItemName() + " | ";
						}

						if ("XR".equals(xValue.getItemLaboratory())) {
							if (xValue.getXray() == null) {
								xRay = "";
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
							if (xValue.getClinicalMicroscopy().getUrineChemical() == null) {
								urine += "";
							} else {
								urine += xValue.getClinicalMicroscopy().getUrineChemical().getOtherNotes();
							}
						}

						if ("ST".equals(xValue.getItemLaboratory())) {
							if (xValue.getClinicalMicroscopy().getFecalysis() == null) {
							} else {
								if (xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes() != null
										&& !"".equals(xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes())) {
									if ("NORMAL"
											.equals(xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes())) {
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
							if (xValue.getOtherNotes() == null) {
								blood += "";
							} else {
								blood += xValue.getOtherNotes();
							}
						}

						if ("PE".equals(xValue.getItemLaboratory())) {
							if (xValue.getPhysicalExamination().getPhysicalExam() == null) {
							} else {
								if (xValue.getPhysicalExamination().getPhysicalExam().getRemarks() == true) {
									pe += xValue.getPhysicalExamination().getPhysicalExam().getFindings();
								} else {
									pe += "NORMAL";
								}
							}
						}

					}
					if (value.getPrint()) {
						print = "P";
					} else {
						print = "";
					}

					if (value.getSend()) {
						send = "M";
					} else {
						send = "";
					}
				}

				String gender = "MALE";
				if ("F".equals(mainList.get(i).getPatient().getGender())) {
					gender = "FEMALE";
				}
				String age = String.valueOf(appUtility.calculateAgeInYear(mainList.get(i).getPatient().getDateOfBirth(),
						mainList.get(i).getTransactionDate()));

				String ageGender = age + "/" + gender;
				String transactionDate = appUtility.calendarToFormatedDate(mainList.get(i).getTransactionDate(),
						pLONGDATE);

				if ("TCH".equals(mainList.get(i).getTransactionType())) {
					Row row = sheet.createRow(account++);

					Cell cell0 = row.createCell(0);
					cell0.setCellValue(mainList.get(i).getId());

					Cell cell1 = row.createCell(1);
					cell1.setCellValue(transactionDate);

					Cell cell2 = row.createCell(2);
					if (mainList.get(i).getBiller() != null) {
						cell2.setCellValue(mainList.get(i).getBiller());
					} else if (mainList.get(i).getReferral() != null) {
						cell2.setCellValue(mainList.get(i).getReferral().getReferral());
					} else {
						cell2.setCellValue("WALK-IN");
					}

					Cell cell3 = row.createCell(3);
					cell3.setCellValue(appUtility.getPatientFullname(mainList.get(i).getPatient()));

					Cell cell4 = row.createCell(4);
					cell4.setCellValue(ageGender);

					Cell Citem = row.createCell(5);
					Citem.setCellValue(items);

					Cell cell5 = row.createCell(6);
					cell5.setCellValue(xRay);

					Cell cell6 = row.createCell(7);
					cell6.setCellValue(urine);

					Cell cell7 = row.createCell(8);
					cell7.setCellValue(stool);

					Cell cell8 = row.createCell(9);
					cell8.setCellValue(blood);

					Cell cell9 = row.createCell(10);
					cell9.setCellValue(pe);

					Cell cell10 = row.createCell(11);
					CellStyle stylePending = workbook.createCellStyle();
					XSSFFont fontPending = workbook.createFont();
					fontPending.setColor(XSSFFont.COLOR_RED);
					stylePending.setFont(fontPending);
					if (classification == "PENDING") {
						cell10.setCellStyle(stylePending);
						cell10.setCellValue(classification);
					} else {
						cell10.setCellValue(classification);
					}

					Cell cell11 = row.createCell(12);
					cell11.setCellValue(remarks);

					Cell cResult = row.createCell(13);
					cResult.setCellValue("");

					Cell uResult = row.createCell(14);
					uResult.setCellValue("");

					Cell pResult = row.createCell(15);
					pResult.setCellValue(print);

					Cell mResult = row.createCell(16);
					mResult.setCellValue(send);

					Cell eResult = row.createCell(17);
					eResult.setCellValue("");

					Cell dResult = row.createCell(18);
					dResult.setCellValue("");

					Cell aResult = row.createCell(19);
					aResult.setCellValue("");

					Cell rResult = row.createCell(20);
					rResult.setCellValue("");
				}
			}

			int walkIn = 2;

			Row otherThanAccount = sheet.createRow(account + 1);
			Cell elsess = otherThanAccount.createCell(0);
			elsess.setCellValue("Walk-In/Referral");

			for (int i = 0; i < mainList.size(); i++) {

				String classification = "";
				String remarks = "";
				String xRay = "";
				String urine = "";
				String stool = "";
				String blood = "";
				String pe = "";
				String items = "";

				for (QisQualityTransactionItem value : mainList.get(i).getTransactionItems()) {

					remarks = value.getOverAllFindings();
					if (value.getClassification() != null) {
						if ("P".equals(value.getClassification()) || "E".equals(value.getClassification()) || "F".equals(value.getClassification())) {
							classification = "PENDING";
						} else {
							classification += "Class " + value.getClassification();
						}
					}

					for (QisQualityTransactionLaboratory xValue : value.getItemLaboratories()) {

						if (xValue.getTransactionItem().getQisPackage() != null) {
							items = xValue.getTransactionItem().getQisPackage().getPackageDescription() + "\n";

						}

						if (xValue.getTransactionItem().getQisItem() != null) {
							items += xValue.getTransactionItem().getQisItem().getItemName() + "\n";
//						if(xValue.getTransactionItem().getQisItem().getPrint() == true) {
//							print += "P \n" ;
//						}else {
//							print += "";
//						}
//						System.out.println(print);
//						
						}

						if ("XR".equals(xValue.getItemLaboratory())) {
							if (xValue.getXray() == null) {
								xRay = "";
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
							if (xValue.getClinicalMicroscopy().getUrineChemical() == null) {
								urine += "";
							} else {
								urine += xValue.getClinicalMicroscopy().getUrineChemical().getOtherNotes();
							}
						}

						if ("ST".equals(xValue.getItemLaboratory())) {
							if (xValue.getClinicalMicroscopy().getFecalysis() == null) {
							} else {
								if (xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes() != null
										&& !"".equals(xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes())) {
									if ("NORMAL"
											.equals(xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes())) {
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
							if (xValue.getOtherNotes() == null) {
								blood += "";
							} else {
								blood += xValue.getOtherNotes();
							}
						}

						if ("PE".equals(xValue.getItemLaboratory())) {
							if (xValue.getPhysicalExamination().getPhysicalExam() == null) {
							} else {
								if (xValue.getPhysicalExamination().getPhysicalExam().getRemarks() == true) {
									pe += xValue.getPhysicalExamination().getPhysicalExam().getFindings();
								} else {
									pe += "NORMAL";
								}
							}
						}

						if (value.getPrint()) {
							print = "P";
						} else {
							print += "";
						}

						if (value.getSend()) {
							send = "M";
						} else {
							send += "";
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
				String transactionDate = appUtility.calendarToFormatedDate(mainList.get(i).getTransactionDate(),
						pLONGDATE);

				if (!"TCH".equals(mainList.get(i).getTransactionType())) {
					rowCount = walkIn + account;
					walkIn++;
					Row row = sheet.createRow(rowCount);
					CellStyle alignment = workbook.createCellStyle();
					alignment.setVerticalAlignment(VerticalAlignment.TOP);

					Cell cell0 = row.createCell(0);
					cell0.setCellValue(mainList.get(i).getId());
					cell0.setCellStyle(alignment);

					Cell cell1 = row.createCell(1);
					cell1.setCellValue(transactionDate);
					cell1.setCellStyle(alignment);

					Cell cell2 = row.createCell(2);
					if (mainList.get(i).getBiller() != null) {
						cell2.setCellValue(mainList.get(i).getBiller());
						cell2.setCellStyle(alignment);
					} else if (mainList.get(i).getReferral() != null) {
						cell2.setCellValue(mainList.get(i).getReferral().getReferral());
						cell2.setCellStyle(alignment);
					} else {
						cell2.setCellValue("WALK-IN");
						cell2.setCellStyle(alignment);
					}

					Cell cell3 = row.createCell(3);
					cell3.setCellValue(appUtility.getPatientFullname(mainList.get(i).getPatient()));
					cell3.setCellStyle(alignment);

					Cell cell4 = row.createCell(4);
					cell4.setCellValue(ageGender);
					cell4.setCellStyle(alignment);

					CellStyle wrapStyle = workbook.createCellStyle();
					wrapStyle.setWrapText(true);
					Cell Citem = row.createCell(5);
					Citem.setCellStyle(wrapStyle);
					items = items.substring(0, items.length() - 1);
					Citem.setCellValue(items);

					Cell cell5 = row.createCell(6);
					cell5.setCellValue(xRay);
					cell5.setCellStyle(alignment);

					Cell cell6 = row.createCell(7);
					cell6.setCellValue(urine);
					cell6.setCellStyle(alignment);

					Cell cell7 = row.createCell(8);
					cell7.setCellValue(stool);
					cell7.setCellStyle(alignment);

					Cell cell8 = row.createCell(9);
					cell8.setCellValue(blood);
					cell8.setCellStyle(alignment);

					Cell cell9 = row.createCell(10);
					cell9.setCellValue(pe);
					cell9.setCellStyle(alignment);

					Cell cell10 = row.createCell(11);
					CellStyle stylePending = workbook.createCellStyle();
					XSSFFont fontPending = workbook.createFont();
					fontPending.setColor(XSSFFont.COLOR_RED);
					stylePending.setFont(fontPending);
					if (classification == "PENDING") {
						cell10.setCellStyle(stylePending);
						cell10.setCellValue(classification);
						cell10.setCellStyle(alignment);
					} else {
						cell10.setCellValue(classification);
						cell10.setCellStyle(alignment);
					}

					Cell cell11 = row.createCell(12);
					cell11.setCellValue(remarks);
					cell11.setCellStyle(alignment);

					Cell cResult = row.createCell(13);
					cResult.setCellValue("");
					cResult.setCellStyle(alignment);

					Cell uResult = row.createCell(14);
					uResult.setCellValue("");
					uResult.setCellStyle(alignment);

					Cell pResult = row.createCell(15);
					pResult.setCellStyle(wrapStyle);
					if (!"".equals(print)) {
						print = print.substring(0, print.length() - 1);
					}
					pResult.setCellValue(print);

					Cell mResult = row.createCell(16);
					mResult.setCellValue("");
					mResult.setCellStyle(alignment);

					Cell eResult = row.createCell(17);
					eResult.setCellValue("");
					eResult.setCellStyle(alignment);

					Cell dResult = row.createCell(18);
					dResult.setCellValue("");
					dResult.setCellStyle(alignment);

					Cell aResult = row.createCell(19);
					aResult.setCellValue("");
					aResult.setCellStyle(alignment);

					Cell rResult = row.createCell(20);
					rResult.setCellValue("");
					rResult.setCellStyle(alignment);

				}
			}

		} else {

			for (int i = 0; i < mainList.size(); i++) {
				String classification = "";
				String remarks = "";
				String xRay = "";
				String urine = "";
				String stool = "";
				String blood = "";
				String pe = "";
				String items = "";

				for (QisQualityTransactionItem value : mainList.get(i).getTransactionItems()) {
					remarks = value.getOverAllFindings();
					if (value.getClassification() != null) {
						if ("P".equals(value.getClassification()) || "E".equals(value.getClassification()) || "F".equals(value.getClassification())) {
							classification = "PENDING";
						} else {
							classification += "Class " + value.getClassification();
						}
					}

					for (QisQualityTransactionLaboratory xValue : value.getItemLaboratories()) {

						if (xValue.getTransactionItem().getQisPackage() != null) {
							items = xValue.getTransactionItem().getQisPackage().getPackageDescription();
						}

						if (xValue.getTransactionItem().getQisItem() != null) {
							items += xValue.getTransactionItem().getQisItem().getItemName() + " | ";
						}

						if ("XR".equals(xValue.getItemLaboratory())) {
							if (xValue.getXray() == null) {
								xRay = "";
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
							if (xValue.getClinicalMicroscopy().getUrineChemical() == null) {
								urine += "";
							} else {
								urine += xValue.getClinicalMicroscopy().getUrineChemical().getOtherNotes();
							}
						}

						if ("ST".equals(xValue.getItemLaboratory())) {
							if (xValue.getClinicalMicroscopy().getFecalysis() == null) {
							} else {
								if (xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes() != null
										&& !"".equals(xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes())) {
									if ("NORMAL"
											.equals(xValue.getClinicalMicroscopy().getFecalysis().getOtherNotes())) {
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
							if (xValue.getOtherNotes() == null) {
								blood += "";
							} else {
								blood += xValue.getOtherNotes();
							}
						}

						if ("PE".equals(xValue.getItemLaboratory())) {
							if (xValue.getPhysicalExamination().getPhysicalExam() == null) {
							} else {
								if (xValue.getPhysicalExamination().getPhysicalExam().getRemarks() == true) {
									pe += xValue.getPhysicalExamination().getPhysicalExam().getFindings();
								} else {
									pe += "NORMAL";
								}
							}
						}

					}
					if (value.getPrint()) {
						print = "P";
					} else {
						print = "";
					}

					if (value.getSend()) {
						send = "M";
					} else {
						send = "";
					}
				}

				String gender = "MALE";
				if ("F".equals(mainList.get(i).getPatient().getGender())) {
					gender = "FEMALE";
				}
				String age = String.valueOf(appUtility.calculateAgeInYear(mainList.get(i).getPatient().getDateOfBirth(),
						mainList.get(i).getTransactionDate()));

				String ageGender = age + "/" + gender;
				String transactionDate = appUtility.calendarToFormatedDate(mainList.get(i).getTransactionDate(),
						pLONGDATE);

				if ("TCH".equals(mainList.get(i).getTransactionType()) && company == null) {
					Row row = sheet.createRow(account++);

					Cell cell0 = row.createCell(0);
					cell0.setCellValue(mainList.get(i).getId());

					Cell cell1 = row.createCell(1);
					cell1.setCellValue(transactionDate);

					Cell cell2 = row.createCell(2);
					if (!"".equals(mainList.get(i).getPatient().getCorporate().getCompanyName())) {
						cell2.setCellValue(mainList.get(i).getPatient().getCorporate().getCompanyName());
					}else if (mainList.get(i).getBiller() != null) {
						cell2.setCellValue(mainList.get(i).getBiller());
					} else if (mainList.get(i).getReferral() != null) {
						cell2.setCellValue(mainList.get(i).getReferral().getReferral());
					} else {
						cell2.setCellValue("WALK-IN");
					}

					Cell cell3 = row.createCell(3);
					cell3.setCellValue(appUtility.getPatientFullname(mainList.get(i).getPatient()));

					Cell cell4 = row.createCell(4);
					cell4.setCellValue(ageGender);

					Cell Citem = row.createCell(5);
					Citem.setCellValue(items);

					Cell cell5 = row.createCell(6);
					cell5.setCellValue(xRay);

					Cell cell6 = row.createCell(7);
					cell6.setCellValue(urine);

					Cell cell7 = row.createCell(8);
					cell7.setCellValue(stool);

					Cell cell8 = row.createCell(9);
					cell8.setCellValue(blood);

					Cell cell9 = row.createCell(10);
					cell9.setCellValue(pe);

					Cell cell10 = row.createCell(11);
					CellStyle stylePending = workbook.createCellStyle();
					XSSFFont fontPending = workbook.createFont();
					fontPending.setColor(XSSFFont.COLOR_RED);
					stylePending.setFont(fontPending);
					if (classification == "PENDING") {
						cell10.setCellStyle(stylePending);
						cell10.setCellValue(classification);
					} else {
						cell10.setCellValue(classification);
					}

					Cell cell11 = row.createCell(12);
					cell11.setCellValue(remarks);

					Cell cResult = row.createCell(13);
					cResult.setCellValue("");

					Cell uResult = row.createCell(14);
					uResult.setCellValue("");

					Cell pResult = row.createCell(15);
					pResult.setCellValue(print);

					Cell mResult = row.createCell(16);
					mResult.setCellValue(send);

					Cell eResult = row.createCell(17);
					eResult.setCellValue("");

					Cell dResult = row.createCell(18);
					dResult.setCellValue("");

					Cell aResult = row.createCell(19);
					aResult.setCellValue("");

					Cell rResult = row.createCell(20);
					rResult.setCellValue("");
				} else if (mainList.get(i).getPatient().getCorporate() != null) {
					if (company.equals(mainList.get(i).getPatient().getCorporate().getCorporateid())) {
						Row row = sheet.createRow(account++);

						Cell cell0 = row.createCell(0);
						cell0.setCellValue(mainList.get(i).getId());

						Cell cell1 = row.createCell(1);
						cell1.setCellValue(transactionDate);

						Cell cell2 = row.createCell(2);
						if (!"".equals(mainList.get(i).getPatient().getCorporate().getCompanyName())) {
							cell2.setCellValue(mainList.get(i).getPatient().getCorporate().getCompanyName());
						} else if (mainList.get(i).getReferral() != null) {
							cell2.setCellValue(mainList.get(i).getReferral().getReferral());
						} else if  (mainList.get(i).getBiller() != null){
							cell2.setCellValue(mainList.get(i).getBiller());
						} else {
							cell2.setCellValue("WALK-IN");
						}

						Cell cell3 = row.createCell(3);
						cell3.setCellValue(appUtility.getPatientFullname(mainList.get(i).getPatient()));

						Cell cell4 = row.createCell(4);
						cell4.setCellValue(ageGender);

						Cell Citem = row.createCell(5);
						Citem.setCellValue(items);

						Cell cell5 = row.createCell(6);
						cell5.setCellValue(xRay);

						Cell cell6 = row.createCell(7);
						cell6.setCellValue(urine);

						Cell cell7 = row.createCell(8);
						cell7.setCellValue(stool);

						Cell cell8 = row.createCell(9);
						cell8.setCellValue(blood);

						Cell cell9 = row.createCell(10);
						cell9.setCellValue(pe);

						Cell cell10 = row.createCell(11);
						CellStyle stylePending = workbook.createCellStyle();
						XSSFFont fontPending = workbook.createFont();
						fontPending.setColor(XSSFFont.COLOR_RED);
						stylePending.setFont(fontPending);
						if (classification == "PENDING") {
							cell10.setCellStyle(stylePending);
							cell10.setCellValue(classification);
						} else {
							cell10.setCellValue(classification);
						}

						Cell cell11 = row.createCell(12);
						cell11.setCellValue(remarks);

						Cell cResult = row.createCell(13);
						cResult.setCellValue("");

						Cell uResult = row.createCell(14);
						uResult.setCellValue("");

						Cell pResult = row.createCell(15);
						pResult.setCellValue(print);

						Cell mResult = row.createCell(16);
						mResult.setCellValue(send);

						Cell eResult = row.createCell(17);
						eResult.setCellValue("");

						Cell dResult = row.createCell(18);
						dResult.setCellValue("");

						Cell aResult = row.createCell(19);
						aResult.setCellValue("");

						Cell rResult = row.createCell(20);
						rResult.setCellValue("");
					}
				}
			}
		}
	}

}
