package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.Set;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabAPTT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabCBC;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabProthrombinTime;

public class HematologyCertificate {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private String NO_RESULT = "- NO RESULT -";
	private String NR = "NO RESULT";
	private String NA = "N/A";

	public HematologyCertificate(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getTransactionLaboratoryHematologyCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionHematology qisHematology, boolean withHeaderFooter)
			throws DocumentException, IOException {

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisHematology.getLabPersonel(), qisHematology.getQualityControl(),
				qisHematology.getMedicalDoctor(), "Pathologist", qisHematology.getVerifiedDate(), withHeaderFooter,
				false, "", null);
		pdfWriter.setPageEvent(event);
		document.open();

		Certificate certificate = new Certificate(appUtility);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPCell cellBorder = new PdfPCell();
		cellBorder.setBorder(Rectangle.BOTTOM);
		cellBorder.setBorderColor(Color.BLUE);
		cellBorder.setVerticalAlignment(Element.ALIGN_BOTTOM);

		boolean isMale = "M".equals(qisTransaction.getPatient().getGender());

		// RESULTS
		PdfPTable tableReport = new PdfPTable(2);
		tableReport.setSpacingBefore(20);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(0);

		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "LABORATORY RESULT", certificate.getFontTitle(),
				Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "HEMATOLOGY", certificate.getFontValue(), Element.ALIGN_LEFT);

		formatHematologyCertificate(qisHematology, tableReport, certificate, cell, cellBorder, isMale);

		document.add(tableReport);

		document.close();
		return document;
	}

	public void formatHematologyCertificate(QisTransactionHematology qisHematology, PdfPTable tableReport,
			Certificate certificate, PdfPCell cell, PdfPCell cellBorder, boolean isMale) {
		QisItem itemDetails = qisHematology.getItemDetails();
		Set<QisLaboratoryProcedureService> services = itemDetails.getServiceRequest();
		int size = 0;

		// COMPLETE BLOOD COUNT
		QisLaboratoryProcedureService completeBloodCount = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("CBC")).findAny().orElse(null);
		if (completeBloodCount != null) {
			size = 2;

			QisTransactionLabCBC cbc = qisHematology.getCbc();

			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Complete Blood Count", certificate.getFontDocValue(),
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			int item = 0;
			PdfPTable cbcLeft = new PdfPTable(new float[] { 1, 4, 4, 4 });
			cbcLeft.setWidthPercentage(100f);
			cbcLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable cbcRight = new PdfPTable(new float[] { 1, 4, 4, 4 });
			cbcRight.setWidthPercentage(100f);
			cbcRight.setHorizontalAlignment(Element.ALIGN_LEFT);

			boolean withResult = false;
			// RBC
			if (cbc != null && cbc.getRedBloodCells() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cbcLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cbcRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Red Blood Cells", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(cbc.getRedBloodCells()) + " x10 ^6/L",
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "4.32~5.72", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			// HEMOGLOBIN
			if (cbc != null && cbc.getHemoglobin() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cbcLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cbcRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Hemoglobin", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(cbc.getHemoglobin()) + " g/L",
							certificate.getFontDocValue(), Element.ALIGN_CENTER);

					certificate.addToTable(cbcTable, cell, isMale ? "M: 137.00~175.00" : "F: 112.00~157.00",
							certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			// HEMATOCRIT
			if (cbc != null && cbc.getHematocrit() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cbcLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cbcRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Hematocrit", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(cbc.getHematocrit()) + " Vol. Fraction",
							certificate.getFontDocValue(), Element.ALIGN_CENTER);

					certificate.addToTable(cbcTable, cell, isMale ? "M: 0.40~0.51" : "F: 0.34~0.45",
							certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			// WBC
			if (cbc != null && cbc.getWhiteBloodCells() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cbcLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cbcRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "White Blood Cells", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder,
							String.valueOf(cbc.getWhiteBloodCells()) + " % x10^9/L", certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "4.23~11.07", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			// NEURO
			if (cbc != null && cbc.getNeutrophils() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cbcLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cbcRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Neutrophils", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(cbc.getNeutrophils()) + " %",
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "34.00~71.00", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			// LYMPHO
			if (cbc != null && cbc.getLymphocytes() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cbcLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cbcRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Lymphocytes", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(cbc.getLymphocytes()) + " %",
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "22.00~53.00", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			// MONOCYTES
			if (cbc != null && cbc.getMonocytes() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cbcLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cbcRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Monocytes", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(cbc.getMonocytes()) + " %",
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "5.00~12.00", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			// BASOPHILS
			if (cbc != null && cbc.getBasophils() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cbcLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cbcRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Basophils", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(cbc.getBasophils()) + " %",
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "0.00~1.00", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			// EOSINOPHILS
			if (cbc != null && cbc.getEosinophils() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cbcLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cbcRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Eosinophils", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(cbc.getEosinophils()) + " %",
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "0.00~6.00", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			// PLATELET
			if (cbc != null && cbc.getPlatelet() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cbcLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cbcRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Platelet", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(cbc.getPlatelet()) + " x10^3/mm3",
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "150~400", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(cbcLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(cbcRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			} else {
				if (item % 2 == 1) {
					cell.setColspan(4);
					certificate.addToTable(cbcRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				}
			}

			tableReport.addCell(cbcLeft);
			tableReport.addCell(cbcRight);
		}

		// BLOOD TYPING
		QisLaboratoryProcedureService bloodTyping = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("BTYP")).findAny().orElse(null);
		if (bloodTyping != null) {
			size++;
			PdfPTable btypTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			btypTable.setWidthPercentage(100f);
			btypTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(btypTable, cell, "Blood Typing", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setColspan(1);
			certificate.addToTable(btypTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			certificate.addToTable(btypTable, cell, "Blood Type", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			if (qisHematology.getBloodTyping() != null && qisHematology.getBloodTyping().getBloodType() != null) {
				String bloodType = "\"" + qisHematology.getBloodTyping().getBloodType() + "\"";
				certificate.addToTable(btypTable, cellBorder, bloodType, certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(btypTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(btypTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(btypTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			certificate.addToTable(btypTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			certificate.addToTable(btypTable, cell, "Rhesus (Rh) Factor", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			if (qisHematology.getBloodTyping() != null && qisHematology.getBloodTyping().getRhesusFactor() != null) {
				certificate.addToTable(btypTable, cellBorder,
						appUtility.getPositiveNegative(qisHematology.getBloodTyping().getRhesusFactor()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(btypTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(btypTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(btypTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			cell.setColspan(4);
			certificate.addToTable(btypTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(btypTable);
		}

		// CLOTTING TIME
		QisLaboratoryProcedureService clottingTime = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("CTM")).findAny().orElse(null);
		if (clottingTime != null) {
			size++;
			PdfPTable ctmTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			ctmTable.setWidthPercentage(100f);
			ctmTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(ctmTable, cell, "Clotting Time", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			String strClottingTime = "";
			if (qisHematology.getCtbt() != null) {
				if (qisHematology.getCtbt().getClottingTimeMin() != null) {
					strClottingTime = qisHematology.getCtbt().getClottingTimeMin() + " min";
				}
				if (qisHematology.getCtbt().getClottingTimeSec() != null
						&& qisHematology.getCtbt().getClottingTimeSec() > 0) {
					strClottingTime += (", " + qisHematology.getCtbt().getClottingTimeSec() + " sec");
				}
			}

			cell.setColspan(1);
			certificate.addToTable(ctmTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			certificate.addToTable(ctmTable, cell, "Time", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			if (!"".equals(strClottingTime)) {
				certificate.addToTable(ctmTable, cellBorder, strClottingTime, certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(ctmTable, cell, "4-10 Minutes", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(ctmTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(ctmTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			cell.setColspan(4);
			certificate.addToTable(ctmTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(ctmTable);
		}

		// BLEEDING TIME
		QisLaboratoryProcedureService bleedingTime = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("BTM")).findAny().orElse(null);
		if (bleedingTime != null) {
			size++;
			PdfPTable btmTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			btmTable.setWidthPercentage(100f);
			btmTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(btmTable, cell, "Bleeding Time", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			String strBleedingTime = "";
			if (qisHematology.getCtbt() != null) {
				if (qisHematology.getCtbt().getBleedingTimeMin() != null) {
					strBleedingTime = qisHematology.getCtbt().getBleedingTimeMin() + " min";
				}
				if (qisHematology.getCtbt().getBleedingTimeSec() != null
						&& qisHematology.getCtbt().getBleedingTimeSec() > 0) {
					strBleedingTime += (", " + qisHematology.getCtbt().getBleedingTimeSec() + " sec");
				}
			}

			cell.setColspan(1);
			certificate.addToTable(btmTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			certificate.addToTable(btmTable, cell, "Time", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			if (!"".equals(strBleedingTime)) {
				certificate.addToTable(btmTable, cellBorder, strBleedingTime, certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(btmTable, cell, "1-5 Minutes", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(btmTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(btmTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			cell.setColspan(4);
			certificate.addToTable(btmTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(btmTable);
		}

		// PR1.31
		QisLaboratoryProcedureService pr131 = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("PR131")).findAny().orElse(null);
		if (pr131 != null) {
			size++;
			PdfPTable prTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			prTable.setWidthPercentage(100f);
			prTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(prTable, cell, "PR 1.31", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setColspan(1);
			certificate.addToTable(prTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			certificate.addToTable(prTable, cell, "Result", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			if (qisHematology.getPrms() != null && qisHematology.getPrms().getPr131() != null) {
				certificate.addToTable(prTable, cellBorder, String.valueOf(qisHematology.getPrms().getPr131()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(prTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(prTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(prTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			cell.setColspan(4);
			certificate.addToTable(prTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(prTable);
		}

		// MALARIAL SMEAR
		QisLaboratoryProcedureService masm = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("MASM")).findAny().orElse(null);
		if (masm != null) {
			size++;
			PdfPTable msTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			msTable.setWidthPercentage(100f);
			msTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(msTable, cell, "Malarial Smear", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setColspan(1);
			certificate.addToTable(msTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			certificate.addToTable(msTable, cell, "Result", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			if (qisHematology.getPrms() != null && qisHematology.getPrms().getMalarialSmear() != null) {
				certificate.addToTable(msTable, cellBorder,
						appUtility.getPositiveNegative(qisHematology.getPrms().getMalarialSmear()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(msTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(msTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(msTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			cell.setColspan(4);
			certificate.addToTable(msTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(msTable);
		}

		// ERYTHROCYTE SEDIMENTATION RATE
		QisLaboratoryProcedureService esr = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("ESR")).findAny().orElse(null);
		if (esr != null) {
			size++;
			PdfPTable esrTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			esrTable.setWidthPercentage(100f);
			esrTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(esrTable, cell, "Erythrocyte Sedimentation Rate ( ESR )",
					certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setColspan(1);
			certificate.addToTable(esrTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			certificate.addToTable(esrTable, cell, "Rate", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			if (qisHematology.getEsr() != null && qisHematology.getEsr().getRate() != null) {
				certificate.addToTable(esrTable, cellBorder, qisHematology.getEsr().getRate() + " mm/hr",
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(esrTable, cell, isMale ? "M: 0~15 mm/hr" : "F: 1~20 mm/hr",
						certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(esrTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(esrTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			certificate.addToTable(esrTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			certificate.addToTable(esrTable, cell, "ESR Method", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			certificate.addToTable(esrTable, cellBorder,
					qisHematology.getEsr() == null ? "" : qisHematology.getEsr().getMethod(),
					certificate.getFontDocValue(), Element.ALIGN_CENTER);
			certificate.addToTable(esrTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);

			cell.setColspan(4);
			certificate.addToTable(esrTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(esrTable);
		}

		if (size % 2 == 1) { // odd columns
			tableReport.addCell("");
		}

		// PROTHROMBIN TIME
		QisLaboratoryProcedureService prothrombinTime = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("PTM")).findAny().orElse(null);
		if (prothrombinTime != null) {
			QisTransactionLabProthrombinTime ptm = qisHematology.getProthombinTime();

			boolean withResult = false;
			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Prothrombin Time", certificate.getFontDocValue(),
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			PdfPTable ptmLeft = new PdfPTable(new float[] { 1, 4, 4, 4 });
			ptmLeft.setWidthPercentage(100f);
			ptmLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable ptmRight = new PdfPTable(new float[] { 1, 4, 4, 4 });
			ptmRight.setWidthPercentage(100f);
			ptmRight.setHorizontalAlignment(Element.ALIGN_LEFT);

			if (ptm != null && ptm.getPatientTime() != null) {
				cell.setColspan(1);
				certificate.addToTable(ptmLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(ptmLeft, cell, "Patient Time", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(ptmLeft, cellBorder, ptm.getPatientTime() + " sec",
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(ptmLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);

				certificate.addToTable(ptmRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(ptmRight, cell, "Normal Value", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(ptmRight, cellBorder, ptm.getPatientTimeNV() + " sec",
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(ptmRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (ptm != null && ptm.getControl() != null) {
				cell.setColspan(1);
				certificate.addToTable(ptmLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(ptmLeft, cell, "Control", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(ptmLeft, cellBorder, ptm.getControl() + " sec", certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(ptmLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);

				certificate.addToTable(ptmRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(ptmRight, cell, "Normal Value", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(ptmRight, cellBorder, ptm.getControlNV() + " sec", certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(ptmRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (ptm != null && ptm.getPercentActivity() != null) {
				cell.setColspan(1);
				certificate.addToTable(ptmLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(ptmLeft, cell, "% Activity", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(ptmLeft, cellBorder, ptm.getPercentActivity() + " sec",
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(ptmLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);

				certificate.addToTable(ptmRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(ptmRight, cell, "Normal Value", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(ptmRight, cellBorder, ptm.getPercentActivityNV() + " sec",
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(ptmRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (ptm != null && ptm.getInr() != null) {
				cell.setColspan(1);
				certificate.addToTable(ptmLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(ptmLeft, cell, "INR", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(ptmLeft, cellBorder, ptm.getInr() + " ", certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(ptmLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);

				certificate.addToTable(ptmRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(ptmRight, cell, "Normal Value", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(ptmRight, cellBorder, ptm.getInrNV() + " sec", certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(ptmRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(ptmLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(ptmRight, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(ptmLeft);
			tableReport.addCell(ptmRight);
		}

		// APTT
		QisLaboratoryProcedureService apttData = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("APTT")).findAny().orElse(null);
		if (apttData != null) {
			QisTransactionLabAPTT aptt = qisHematology.getAptt();

			boolean withResult = false;
			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Activated Partial Thromboplastin Time ( APTT )",
					certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			PdfPTable apttLeft = new PdfPTable(new float[] { 1, 4, 4, 4 });
			apttLeft.setWidthPercentage(100f);
			apttLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable apttRight = new PdfPTable(new float[] { 1, 4, 4, 4 });
			apttRight.setWidthPercentage(100f);
			apttRight.setHorizontalAlignment(Element.ALIGN_LEFT);

			if (aptt != null && aptt.getPatientTime() != null) {
				cell.setColspan(1);
				certificate.addToTable(apttLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(apttLeft, cell, "Patient Time", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(apttLeft, cellBorder, aptt.getPatientTime() + " sec",
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(apttLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);

				certificate.addToTable(apttRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(apttRight, cell, "Normal Value", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(apttRight, cellBorder, aptt.getPatientTimeNV() + " sec",
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(apttRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (aptt != null && aptt.getControl() != null) {
				cell.setColspan(1);
				certificate.addToTable(apttLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(apttLeft, cell, "Control", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(apttLeft, cellBorder, aptt.getControl() + " sec", certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(apttLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);

				certificate.addToTable(apttRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(apttRight, cell, "Normal Value", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(apttRight, cellBorder, aptt.getControlNV() + " sec",
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(apttRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(apttLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(apttRight, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(apttLeft);
			tableReport.addCell(apttRight);
		}

		// OTHER NOTES
		if (qisHematology.getOtherNotes() != null && !"".equals(qisHematology.getOtherNotes().trim())) {
			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Other Notes:", certificate.getFontDocValue(),
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			certificate.addToTable(tableReport, cell, qisHematology.getOtherNotes(), certificate.getFontDocNormal(),
					Element.ALIGN_LEFT);
		}
	}

	public void formatHematologyClassification(QisTransactionHematology qisHematology, PdfPTable tableReport,
			Certificate certificate, PdfPCell cell, boolean isMale) {
		QisItem itemDetails = qisHematology.getItemDetails();
		Set<QisLaboratoryProcedureService> services = itemDetails.getServiceRequest();

		// COMPLETE BLOOD COUNT
		QisLaboratoryProcedureService completeBloodCount = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("CBC")).findAny().orElse(null);
		if (completeBloodCount != null) {
			QisTransactionLabCBC cbc = qisHematology.getCbc();

			cell.setBorderColor(Color.BLUE);
			cell.setColspan(4);
			certificate.addToTable(tableReport, cell, "HEMATOLOGY", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "Complete Blood Count", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);

			cell.setColspan(1);
			if (cbc != null) {
				if (cbc.getWhiteBloodCells() != null) {
					certificate.addToTable(tableReport, cell, "  White Blood Cells", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.BOTTOM);
					certificate.addToTable(tableReport, cell, String.valueOf(cbc.getWhiteBloodCells()),
							certificate.getDisplayFontRange(cbc.getWhiteBloodCells(), 4.23, 11.07,
									certificate.getFontDocValue(), certificate.getFontDocAlert()),
							Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					certificate.addToTable(tableReport, cell, "%x10^9/L", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "4.23~11.07", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
				}
			}

			cell.setColspan(1);
			if (cbc != null) {
				if (cbc.getNeutrophils() != null) {
					certificate.addToTable(tableReport, cell, "  Neutrophils", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.BOTTOM);
					certificate.addToTable(tableReport, cell, String.valueOf(cbc.getNeutrophils()),
							certificate.getDisplayFontRange(cbc.getNeutrophils(), 34.0, 71.0,
									certificate.getFontDocValue(), certificate.getFontDocAlert()),
							Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, "% 34.00~71.00", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setColspan(1);
				}
			}

			cell.setColspan(1);
			if (cbc != null) {
				if (cbc.getLymphocytes() != null) {
					certificate.addToTable(tableReport, cell, "  Lymphocytes", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.BOTTOM);
					certificate.addToTable(tableReport, cell, String.valueOf(cbc.getLymphocytes()),
							certificate.getDisplayFontRange(cbc.getLymphocytes(), 22.0, 53.0,
									certificate.getFontDocValue(), certificate.getFontDocAlert()),
							Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, "% 22.00~53.00", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setColspan(1);
				}
			}

			cell.setColspan(1);
			if (cbc != null) {
				if (cbc.getMonocytes() != null) {
					certificate.addToTable(tableReport, cell, "  Monocytes", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.BOTTOM);
					certificate.addToTable(tableReport, cell, String.valueOf(cbc.getMonocytes()),
							certificate.getDisplayFontRange(cbc.getMonocytes(), 5.0, 12.0,
									certificate.getFontDocValue(), certificate.getFontDocAlert()),
							Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, "% 5.00~12.00", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setColspan(1);
				}
			}

			cell.setColspan(1);
			if (cbc != null) {
				if (cbc.getEosinophils() != null) {
					certificate.addToTable(tableReport, cell, "  Eosinophils", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.BOTTOM);
					certificate.addToTable(tableReport, cell, String.valueOf(cbc.getEosinophils()),
							certificate.getDisplayFontRange(cbc.getEosinophils(), 0.0, 6.0,
									certificate.getFontDocValue(), certificate.getFontDocAlert()),
							Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, "% 0.00~6.00", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setColspan(1);
				}
			}

			cell.setColspan(1);
			if (cbc != null) {
				if (cbc.getBasophils() != null) {
					certificate.addToTable(tableReport, cell, "  Basophils", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.BOTTOM);
					certificate
							.addToTable(tableReport, cell, String.valueOf(cbc.getBasophils()),
									certificate.getDisplayFontRange(cbc.getBasophils(), 0.0, 1.0,
											certificate.getFontDocValue(), certificate.getFontDocAlert()),
									Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, "% 0.00~1.00", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setColspan(1);
				}
			}

			cell.setColspan(1);
			if (cbc != null) {
				if (cbc.getRedBloodCells() != null) {
					certificate.addToTable(tableReport, cell, "  Red Blood Cells", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.BOTTOM);
					certificate.addToTable(tableReport, cell, String.valueOf(cbc.getRedBloodCells()),
							certificate.getDisplayFontRange(cbc.getRedBloodCells(), 4.32, 5.72,
									certificate.getFontDocValue(), certificate.getFontDocAlert()),
							Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					certificate.addToTable(tableReport, cell, "x10 ^6/L", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "4.32~5.72", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
				}
			}

			cell.setColspan(1);
			if (cbc != null) {
				if (cbc.getHemoglobin() != null) {
					certificate.addToTable(tableReport, cell, "  Hemoglobin", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.BOTTOM);
					certificate.addToTable(tableReport, cell, String.valueOf(cbc.getHemoglobin()),
							certificate.getDisplayFontRange(cbc.getHemoglobin(), isMale ? 137.0 : 112.0,
									isMale ? 175.0 : 157.0, certificate.getFontDocValue(),
									certificate.getFontDocAlert()),
							Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					certificate.addToTable(tableReport, cell, "g/L", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, isMale ? "M: 137.00~175.00" : "F: 112.00~157.00",
							certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				}
			}

			cell.setColspan(1);
			if (cbc != null) {
				if (cbc.getHematocrit() != null) {
					certificate.addToTable(tableReport, cell, "  Hematocrit", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.BOTTOM);
					certificate.addToTable(tableReport, cell, String.valueOf(cbc.getHematocrit()),
							certificate.getDisplayFontRange(cbc.getHematocrit(), isMale ? 0.4 : 0.34,
									isMale ? 0.51 : 0.45, certificate.getFontDocValue(), certificate.getFontDocAlert()),
							Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					certificate.addToTable(tableReport, cell, "VF", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, isMale ? "M: 0.40~0.51" : "F: 0.34~0.45",
							certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				}
			}

			cell.setColspan(1);
			if (cbc != null) {
				if (cbc.getPlatelet() != null) {
					certificate.addToTable(tableReport, cell, "  Platelet", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.BOTTOM);
					certificate.addToTable(tableReport, cell, String.valueOf(cbc.getPlatelet()),
							certificate.getDisplayFontRange(cbc.getPlatelet(), 150.0, 400.0,
									certificate.getFontDocValue(), certificate.getFontDocAlert()),
							Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, "% 150~400", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setColspan(1);
				}
			}

			cell.setColspan(1);
			certificate.addToTable(tableReport, cell, "  Other/Notes", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(3);
			if (qisHematology.getOtherNotes() != null && !"".equals(qisHematology.getOtherNotes().trim())) {
				certificate.addToTable(tableReport, cell, qisHematology.getOtherNotes(), certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(tableReport, cell, "", certificate.getFontDocAlert(), Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);

			cell.setColspan(4);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
		}
	}

	public void formatHematologyConsolidated(QisTransactionHematology qisHematology, PdfPTable tableReport,
			Certificate certificate, boolean isMale) {

		QisItem itemDetails = qisHematology.getItemDetails();
		Set<QisLaboratoryProcedureService> services = itemDetails.getServiceRequest();

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setUseAscender(true);
		cell.setUseDescender(true);

		Font fontTest = FontFactory.getFont("GARAMOND");
		fontTest.setSize(11);
		fontTest.setColor(Color.BLACK);

		Font fontResult = certificate.getFontResult();
		Font fontResultRed = certificate.getFontResultRed();

		Font fontUnit = FontFactory.getFont("GARAMOND");
		fontUnit.setSize(10);
		fontUnit.setColor(Color.BLACK);

		boolean withData = false;

		// COMPLETE BLOOD COUNT
		QisLaboratoryProcedureService completeBloodCount = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("CBC")).findAny().orElse(null);
		if (completeBloodCount != null) {
			QisTransactionLabCBC cbc = qisHematology.getCbc();
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Complete Blood Count", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			boolean withResult = false;

			// WBC
			if (cbc != null && cbc.getWhiteBloodCells() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   White Blood Cells", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(cbc.getWhiteBloodCells(), 3.40, 9.60), certificate
								.getDisplayFontRange(cbc.getWhiteBloodCells(), 3.40, 9.60, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "x10^9/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "3.40~9.60", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cbc.getWhiteBloodCells(), 3.40, 9.60), certificate
						.getDisplayFontRange(cbc.getWhiteBloodCells(), 3.40, 9.60, fontResult, fontResultRed), Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "x10^6/mm^3", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "3.40~9.60", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			// NEUTROPHILS
			if (cbc != null && cbc.getNeutrophils() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Neutrophils", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(cbc.getNeutrophils(), 34.0, 71.0),
						certificate.getDisplayFontRange(cbc.getNeutrophils(), 34.0, 71.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "%", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "34.00~71.00", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(cbc.getNeutrophils(), 34.0, 71.0),
						certificate.getDisplayFontRange(cbc.getNeutrophils(), 34.0, 71.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "%", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "34.00~71.00", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			// LYMPHOCYTES
			if (cbc != null && cbc.getLymphocytes() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Lymphocytes", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(cbc.getLymphocytes(), 22.0, 53.0),
						certificate.getDisplayFontRange(cbc.getLymphocytes(), 22.0, 53.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "%", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "22.00~53.00", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(cbc.getLymphocytes(), 22.0, 53.0),
						certificate.getDisplayFontRange(cbc.getLymphocytes(), 22.0, 53.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "%", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "22.00~53.00", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			// MONOCYTES
			if (cbc != null && cbc.getMonocytes() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Monocytes", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cbc.getMonocytes(), 5.0, 12.0),
						certificate.getDisplayFontRange(cbc.getMonocytes(), 5.0, 12.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "%", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "5.00~12.00", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cbc.getMonocytes(), 5.0, 12.0),
						certificate.getDisplayFontRange(cbc.getMonocytes(), 5.0, 12.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "%", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "5.00~12.00", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			// BASOPHILS
			if (cbc != null && cbc.getBasophils() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Basophils", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cbc.getBasophils(), 0.0, 1.0),
						certificate.getDisplayFontRange(cbc.getBasophils(), 0.0, 1.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "%", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "0.00~1.00", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cbc.getBasophils(), 0.0, 1.0),
						certificate.getDisplayFontRange(cbc.getBasophils(), 0.0, 1.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "%", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "0.00~1.00", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			// EOSINOPHILS
			if (cbc != null && cbc.getEosinophils() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Eosinophils", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cbc.getEosinophils(), 0.0, 6.0),
						certificate.getDisplayFontRange(cbc.getEosinophils(), 0.0, 6.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "%", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "0.00~6.00", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cbc.getEosinophils(), 0.0, 6.0),
						certificate.getDisplayFontRange(cbc.getEosinophils(), 0.0, 6.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "%", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "0.00~6.00", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			// RBC
			if (cbc != null && cbc.getRedBloodCells() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Red Blood Cells", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(cbc.getRedBloodCells(), 4.32, 5.72),
						certificate.getDisplayFontRange(cbc.getRedBloodCells(), 4.32, 5.72, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "x10 ^6/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "4.32~5.72", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(cbc.getRedBloodCells(), 4.32, 5.72),
						certificate.getDisplayFontRange(cbc.getRedBloodCells(), 4.32, 5.72, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "x10^6/mm^3", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "4.32~5.72", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			// HEMOGLOBIN
			if (cbc != null && cbc.getHemoglobin() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Hemoglobin", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(cbc.getHemoglobin(), isMale ? 137.0 : 112.0, isMale ? 175.0 : 157),
						certificate.getDisplayFontRange(cbc.getHemoglobin(), isMale ? 137.0 : 112.0,
								isMale ? 175.0 : 157, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "g/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, isMale ? "M: 137~175" : "F: 112.00~157.00", fontUnit,
						Element.ALIGN_RIGHT);
				
				double hemoglobin = 0d;
				hemoglobin = cbc.getHemoglobin() * .10;
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(hemoglobin, isMale ? 13.70 : 11.20, isMale ? 17.50 : 15.70),
						certificate.getDisplayFontRange(hemoglobin, isMale ? 13.70 : 11.20,
								isMale ? 17.50 : 15.70, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "g/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, isMale ? "M: 13.70~17.50" : "F: 11.20~15.70", fontUnit,
						Element.ALIGN_RIGHT);

				withResult = true;
			}

			// HEMATOCRIT
			if (cbc != null && cbc.getHematocrit() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Hematocrit", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(cbc.getHematocrit(), isMale ? 0.40 : 0.34, isMale ? 0.51 : 0.45),
						certificate.getDisplayFontRange(cbc.getHematocrit(), isMale ? 0.40 : 0.34, isMale ? 0.51 : 0.45,
								fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "Vol. Fraction", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, isMale ? "M: 0.40~0.51" : "F: 0.34~0.45", fontUnit,
						Element.ALIGN_RIGHT);
				double hematocrit = 0d;
				hematocrit = cbc.getHematocrit() * 100;
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(hematocrit, isMale ? 40.00 : 34.00, isMale ? 51.00 : 45.00),
						certificate.getDisplayFontRange(hematocrit, isMale ? 40.00 : 34.00, isMale ? 51.00 : 45.00
								,
								fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "Vol. Fraction", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, isMale ? "M: 40.00~51.00" : "F: 34.00~45.00", fontUnit,
						Element.ALIGN_RIGHT);

				withResult = true;
			}

			// PLATELET
			if (cbc != null && cbc.getPlatelet() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Platelet", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cbc.getPlatelet(), 150.0, 400.0),
						certificate.getDisplayFontRange(cbc.getPlatelet(), 150.0, 400.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "x10^9/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "150~400", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cbc.getPlatelet(), 150.0, 400.0),
						certificate.getDisplayFontRange(cbc.getPlatelet(), 150.0, 400.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "x10^3/mm^3", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "150~400", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// BLOOD TYPING
		QisLaboratoryProcedureService bloodTyping = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("BTYP")).findAny().orElse(null);
		if (bloodTyping != null) {

			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Blood Typing", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
			cell.setVerticalAlignment(Element.ALIGN_TOP);

			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "   Blood Type", fontTest, Element.ALIGN_LEFT);
			if (qisHematology.getBloodTyping() != null) {
				if (qisHematology.getBloodTyping().getBloodType() != null) {
					cell.setColspan(1);
					String bloodType = "\"" + qisHematology.getBloodTyping().getBloodType() + "\"";
					certificate.addToTable(tableReport, cell, bloodType, fontResult, Element.ALIGN_RIGHT);
					cell.setColspan(5);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				} else {
					cell.setColspan(9);
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
				}
			} else {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}

			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "   Rhesus (Rh) Factor", fontTest, Element.ALIGN_LEFT);
			if (qisHematology.getBloodTyping() != null) {
				if (qisHematology.getBloodTyping().getRhesusFactor() != null) {
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							"\t" + appUtility.getPositiveNegative(qisHematology.getBloodTyping().getRhesusFactor()),
							fontResult, Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				} else {
					cell.setColspan(9);
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
				}
			} else {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// CLOTTING TIME
		QisLaboratoryProcedureService clottingTime = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("CTM")).findAny().orElse(null);
		if (clottingTime != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setColspan(3);
			cell.setRowspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Clotting Time", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			String strClottingTime = "";
			if (qisHematology.getCtbt() != null) {
				if (qisHematology.getCtbt().getClottingTimeMin() != null) {
					strClottingTime = qisHematology.getCtbt().getClottingTimeMin() + " min";
				}
				if (qisHematology.getCtbt().getClottingTimeSec() != null
						&& qisHematology.getCtbt().getClottingTimeSec() > 0) {
					strClottingTime += (", " + qisHematology.getCtbt().getClottingTimeSec() + " sec");
				}
			}

			cell.setColspan(2);
			if (!"".equals(strClottingTime)) {
				certificate.addToTable(tableReport, cell, "\t" + strClottingTime, fontResult, Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
			}
			cell.setColspan(4);
			certificate.addToTable(tableReport, cell, "4-10 Minutes", fontUnit, Element.ALIGN_LEFT);
			withData = true;
		}

		// BLEEDING TIME
		QisLaboratoryProcedureService bleedingTime = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("BTM")).findAny().orElse(null);
		if (bleedingTime != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setFixedHeight(20);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "Bleeding Time", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			String strBleedingTime = "";
			if (qisHematology.getCtbt() != null) {
				if (qisHematology.getCtbt().getBleedingTimeMin() != null) {
					strBleedingTime = qisHematology.getCtbt().getBleedingTimeMin() + " min";
				}
				if (qisHematology.getCtbt().getBleedingTimeSec() != null
						&& qisHematology.getCtbt().getBleedingTimeSec() > 0) {
					strBleedingTime += (", " + qisHematology.getCtbt().getBleedingTimeSec() + " sec");
				}
			}

			cell.setColspan(2);
			if (!"".equals(strBleedingTime)) {
				certificate.addToTable(tableReport, cell, "\t" + strBleedingTime, fontResult, Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
			}

			cell.setColspan(4);
			certificate.addToTable(tableReport, cell, "1-5 Minutes", fontUnit, Element.ALIGN_LEFT);
			withData = true;
		}

		// PR1.31
		QisLaboratoryProcedureService pr131 = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("PR131")).findAny().orElse(null);
		if (pr131 != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setFixedHeight(20);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "PR 1.31", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setColspan(1);
			if (qisHematology.getPrms() != null && qisHematology.getPrms().getPr131() != null) {
				certificate.addToTable(tableReport, cell, String.valueOf(qisHematology.getPrms().getPr131()),
						fontResult, Element.ALIGN_RIGHT);
				cell.setColspan(5);
			} else {
				cell.setColspan(2);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
				cell.setColspan(4);
			}

			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			withData = true;
		}

		// MALARIAL SMEAR
		QisLaboratoryProcedureService masm = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("MASM")).findAny().orElse(null);
		if (masm != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setFixedHeight(20);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "Malarial Smear", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setColspan(2);
			if (qisHematology.getPrms() != null && qisHematology.getPrms().getMalarialSmear() != null) {
				certificate.addToTable(tableReport, cell,
						appUtility.getPositiveNegative(qisHematology.getPrms().getMalarialSmear()), certificate
								.getDisplayFont(qisHematology.getPrms().getMalarialSmear(), fontResult, fontResultRed),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
			}
			cell.setColspan(4);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
		}

		// ERYTHROCYTE SEDIMENTATION RATE
		QisLaboratoryProcedureService esr = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("ESR")).findAny().orElse(null);
		if (esr != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setColspan(3);
			cell.setRowspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Erythrocyte Sedimentation Rate ( ESR )", fontResult,
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setColspan(1);
			if (qisHematology.getEsr() != null && qisHematology.getEsr().getRate() != null) {

				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisHematology.getEsr().getRate(), isMale ? 0.0 : 1.0,
								isMale ? 15.0 : 20.0),
						certificate.getDisplayFontRange(qisHematology.getEsr().getRate(), isMale ? 0.0 : 1.0,
								isMale ? 15.0 : 20.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mm/hr", fontUnit, Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			}
			certificate.addToTable(tableReport, cell, isMale ? "M: 0~15 mm/hr" : "F: 1~20 mm/hr", fontUnit,
					Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "   ESR Method", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(2);
			certificate.addToTable(tableReport, cell,
					"\t" + qisHematology.getEsr() == null ? "" : qisHematology.getEsr().getMethod(), fontResult,
					Element.ALIGN_LEFT);
			cell.setColspan(4);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			withData = true;
		}

		// PROTHROMBIN TIME
		QisLaboratoryProcedureService prothrombinTime = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("PTM")).findAny().orElse(null);
		if (prothrombinTime != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			QisTransactionLabProthrombinTime ptm = qisHematology.getProthombinTime();

			boolean withResult = false;
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Prothrombin Time", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			if (ptm != null && ptm.getPatientTime() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Patient Time", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, String.valueOf(ptm.getPatientTime()), fontResult,
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "sec", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, ptm.getPatientTimeNV(), fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (ptm != null && ptm.getControl() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Control", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, String.valueOf(ptm.getControl()), fontResult,
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "sec", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, ptm.getControlNV(), fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (ptm != null && ptm.getPercentActivity() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   % Activity", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, String.valueOf(ptm.getPercentActivity()), fontResult,
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "sec", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, ptm.getPercentActivityNV(), fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (ptm != null && ptm.getInr() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   INR", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, String.valueOf(ptm.getInr()), fontResult,
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, ptm.getInrNV(), fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// APTT
		QisLaboratoryProcedureService apttData = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("APTT")).findAny().orElse(null);
		if (apttData != null) {
			QisTransactionLabAPTT aptt = qisHematology.getAptt();

			boolean withResult = false;
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Activated Partial Thromboplastin Time ( APTT )", fontResult,
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			if (aptt != null && aptt.getPatientTime() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Patient Time", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, String.valueOf(aptt.getPatientTime()), fontResult,
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "sec", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, aptt.getPatientTimeNV(), fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (aptt != null && aptt.getControl() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Control", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, String.valueOf(aptt.getControl()), fontResult,
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "sec", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, aptt.getControlNV(), fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// FERRITIN
		QisLaboratoryProcedureService ferritin = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("FERR")).findAny().orElse(null);
		if (ferritin != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setFixedHeight(20);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "Ferritin", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setColspan(1);
			if (qisHematology.getFerritin() != null && qisHematology.getFerritin().getResult() != null) {
				certificate.addToTable(tableReport, cell, String.valueOf(qisHematology.getFerritin().getResult()),
						fontResult, Element.ALIGN_RIGHT);

				certificate.addToTable(tableReport, cell, "ng/mL", fontResult, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, (isMale ? "M:16.0-220.0" : "F: 10.0 - 124.0"), certificate.getFontDocNormal(),
						Element.ALIGN_RIGHT);
				cell.setColspan(3);
			} else {
				cell.setColspan(2);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
				cell.setColspan(4);
			}

			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			withData = true;
		}
		
		QisLaboratoryProcedureService rct = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("RCT")).findAny().orElse(null);
		if (rct != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setFixedHeight(20);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "Reticulocyte Count", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setColspan(1);
			if (qisHematology.getRct() != null && qisHematology.getRct().getResult() != null) {
				certificate.addToTable(tableReport, cell, String.valueOf(qisHematology.getRct().getResult()),
						fontResult, Element.ALIGN_RIGHT);

				certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "0.5-1.5%", certificate.getFontDocNormal(),
						Element.ALIGN_RIGHT);
				cell.setColspan(3);
			} else {
				cell.setColspan(2);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
				cell.setColspan(4);
			}

			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			withData = true;
		}

		// OTHER NOTES
		if (qisHematology.getOtherNotes() != null && !"".equals(qisHematology.getOtherNotes().trim())) {
			cell.setColspan(9);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Other Notes:", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			certificate.addToTable(tableReport, cell, qisHematology.getOtherNotes(), fontTest, Element.ALIGN_LEFT);
			withData = true;
		}

		if (withData) {
			cell.setColspan(5);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
		}
	}
}
