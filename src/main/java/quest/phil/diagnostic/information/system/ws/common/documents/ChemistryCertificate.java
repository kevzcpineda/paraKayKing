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
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionChemistry;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabBilirubin;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabCPK;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabElectrolytes;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabEnzymes;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabLipidProfile;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabOGTT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabProtein;

public class ChemistryCertificate {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private String NO_RESULT = "-NO RESULT-";

	public ChemistryCertificate(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getTransactionLaboratoryChemistryCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionChemistry qisChemistry, boolean withHeaderFooter)
			throws DocumentException, IOException {

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisChemistry.getLabPersonel(), qisChemistry.getQualityControl(),
				qisChemistry.getMedicalDoctor(), "Pathologist", qisChemistry.getVerifiedDate(), withHeaderFooter, false,
				"", null);
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

		cell.setColspan(9);
		certificate.addToTable(tableReport, cell, "LABORATORY RESULT", certificate.getFontTitle(),
				Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "CHEMISTRY", certificate.getFontValue(), Element.ALIGN_LEFT);

		formatChemistryCertificate(qisChemistry, tableReport, certificate, cell, cellBorder, isMale);

		document.add(tableReport);

		document.close();
		return document;
	}

	public void formatChemistryCertificate(QisTransactionChemistry qisChemistry, PdfPTable tableReport,
			Certificate certificate, PdfPCell cell, PdfPCell cellBorder, boolean isMale) {
		QisItem itemDetails = qisChemistry.getItemDetails();
		Set<QisLaboratoryProcedureService> services = itemDetails.getServiceRequest();

		// FASTING BLOOD SUGAR
		QisLaboratoryProcedureService fastingBloodSugar = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("FBS")).findAny().orElse(null);
		if (fastingBloodSugar != null) {
			PdfPTable leftTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			leftTable.setWidthPercentage(100f);
			leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable rightTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			rightTable.setWidthPercentage(100f);
			rightTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(2);
			certificate.addToTable(leftTable, cell, "  Fasting Blood Sugar", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getFbs() != null && qisChemistry.getFbs().getFbs() != null) {
				certificate.addToTable(leftTable, cellBorder, String.valueOf(qisChemistry.getFbs().getFbs()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "mmol/L   3.8 - 6.11", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(leftTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			if (qisChemistry.getFbs() != null && qisChemistry.getFbs().getConventional() != null) {
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder, String.valueOf(qisChemistry.getFbs().getConventional()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  71 -111", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				cell.setColspan(4);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(leftTable);
			tableReport.addCell(rightTable);
		}

		// RANDOM BLOOD SUGAR
		QisLaboratoryProcedureService randomBloodSugar = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("RBS")).findAny().orElse(null);
		if (randomBloodSugar != null) {
			PdfPTable leftTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			leftTable.setWidthPercentage(100f);
			leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable rightTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			rightTable.setWidthPercentage(100f);
			rightTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(2);
			certificate.addToTable(leftTable, cell, "  Random Blood Sugar", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getRbs() != null && qisChemistry.getRbs().getRbs() != null) {
				certificate.addToTable(leftTable, cellBorder, String.valueOf(qisChemistry.getRbs().getRbs()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "mmol/L  < 7.7", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(leftTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			if (qisChemistry.getRbs() != null && qisChemistry.getRbs().getConventional() != null) {
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder, String.valueOf(qisChemistry.getRbs().getConventional()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  < 140", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				cell.setColspan(4);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(leftTable);
			tableReport.addCell(rightTable);
		}

		// POST PRANDIAL RANDOM BLOOD SUGAR
		QisLaboratoryProcedureService ppRandomBloodSugar = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("PPRBS")).findAny().orElse(null);
		if (ppRandomBloodSugar != null) {
			PdfPTable leftTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			leftTable.setWidthPercentage(100f);
			leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable rightTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			rightTable.setWidthPercentage(100f);
			rightTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(2);
			certificate.addToTable(leftTable, cell, "  Post Prandial RBS", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getPprbs() != null && qisChemistry.getPprbs().getPprbs() != null) {
				certificate.addToTable(leftTable, cellBorder, String.valueOf(qisChemistry.getPprbs().getPprbs()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "mmol/L  < 7.7", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(leftTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			if (qisChemistry.getPprbs() != null && qisChemistry.getPprbs().getConventional() != null) {
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder,
						String.valueOf(qisChemistry.getPprbs().getConventional()), certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  < 140", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				cell.setColspan(4);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(leftTable);
			tableReport.addCell(rightTable);
		}

		// URIC ACID
		QisLaboratoryProcedureService uricAcid = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("URAC")).findAny().orElse(null);
		if (uricAcid != null) {
			PdfPTable leftTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			leftTable.setWidthPercentage(100f);
			leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable rightTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			rightTable.setWidthPercentage(100f);
			rightTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(2);
			certificate.addToTable(leftTable, cell, "  Uric Acid", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getUricAcid() != null && qisChemistry.getUricAcid().getUricAcid() != null) {
				certificate.addToTable(leftTable, cellBorder, String.valueOf(qisChemistry.getUricAcid().getUricAcid()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "mmol/L  142 - 428", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(leftTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			if (qisChemistry.getUricAcid() != null && qisChemistry.getUricAcid().getConventional() != null) {
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder,
						String.valueOf(qisChemistry.getUricAcid().getConventional()), certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  2.39 - 7.20", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				cell.setColspan(4);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(leftTable);
			tableReport.addCell(rightTable);
		}

		// BLOOD UREA NITROGEN
		QisLaboratoryProcedureService bloodUreaNitrogen = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("BUN")).findAny().orElse(null);
		if (bloodUreaNitrogen != null) {
			PdfPTable leftTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			leftTable.setWidthPercentage(100f);
			leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable rightTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			rightTable.setWidthPercentage(100f);
			rightTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(2);
			certificate.addToTable(leftTable, cell, "  Blood Urea Nitrogen", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getBun() != null && qisChemistry.getBun().getBun() != null) {
				certificate.addToTable(leftTable, cellBorder, String.valueOf(qisChemistry.getBun().getBun()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "mmol/L  2.90 - 8.20", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(leftTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			if (qisChemistry.getBun() != null && qisChemistry.getBun().getConventional() != null) {
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder, String.valueOf(qisChemistry.getBun().getConventional()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  17.4 - 49.2", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				cell.setColspan(4);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(leftTable);
			tableReport.addCell(rightTable);
		}

		// CREATININE
		QisLaboratoryProcedureService creatinine = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("CREA")).findAny().orElse(null);
		if (creatinine != null) {
			PdfPTable leftTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			leftTable.setWidthPercentage(100f);
			leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable rightTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			rightTable.setWidthPercentage(100f);
			rightTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(2);
			certificate.addToTable(leftTable, cell, "  Creatinine", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getCreatinine() != null && qisChemistry.getCreatinine().getCreatinine() != null) {
				certificate.addToTable(leftTable, cellBorder,
						String.valueOf(qisChemistry.getCreatinine().getCreatinine()), certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "umol/L  " + (isMale ? "M: 45 - 104" : "F: 45 - 104"),
						certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(leftTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			if (qisChemistry.getCreatinine() != null && qisChemistry.getCreatinine().getConventional() != null) {
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder,
						String.valueOf(qisChemistry.getCreatinine().getConventional()), certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  " + (isMale ? "M: 0.51 - 1.18" : "F: 0.51 - 1.18"),
						certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				cell.setColspan(4);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(leftTable);
			tableReport.addCell(rightTable);
		}

		// LIPID PROFILE
		QisLaboratoryProcedureService lipidProfile = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("LIPP")).findAny().orElse(null);
		if (lipidProfile != null) {
			boolean withResult = false;
			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Lipid Profile", certificate.getFontDocValue(),
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			PdfPTable leftTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			leftTable.setWidthPercentage(100f);
			leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable rightTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			rightTable.setWidthPercentage(100f);
			rightTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			QisTransactionLabLipidProfile lipp = qisChemistry.getLipidProfile();

			if (lipp != null && lipp.getCholesterol() != null) {
				cell.setColspan(2);
				certificate.addToTable(leftTable, cell, "  Cholesterol", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(leftTable, cellBorder, String.valueOf(lipp.getCholesterol()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "mmol/L  < 5.20", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);

				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder, String.valueOf(lipp.getCholesterolConventional()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  < 200.0", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (lipp != null && lipp.getTriglycerides() != null) {
				cell.setColspan(2);
				certificate.addToTable(leftTable, cell, "  Triglycerides", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(leftTable, cellBorder, String.valueOf(lipp.getTriglycerides()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "mmol/L  0.3-1.7", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);

				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder, String.valueOf(lipp.getTriglyceridesConventional()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  26.54-150", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (lipp != null && lipp.getHdl() != null) {
				cell.setColspan(2);
				certificate.addToTable(leftTable, cell, "  HDL", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(leftTable, cellBorder, String.valueOf(lipp.getHdl()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "mmol/L  0.91-1.55", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);

				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder, String.valueOf(lipp.getHdlConventional()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  35-60", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (lipp != null && lipp.getCholesterol() != null && lipp.getTriglycerides() != null
					&& lipp.getHdl() != null) {
				if (lipp.getLdl() != null) {
					cell.setColspan(2);
					certificate.addToTable(leftTable, cell, "  LDL", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(leftTable, cellBorder, String.valueOf(lipp.getLdl()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(leftTable, cell, "mmol/L  2.5-4.1", certificate.getFontDocNormal(),
							Element.ALIGN_LEFT);

					certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(rightTable, cellBorder, String.valueOf(lipp.getLdlConventional()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(rightTable, cell, "mg/dl  97.0-159.0", certificate.getFontDocNormal(),
							Element.ALIGN_LEFT);
					certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
					withResult = true;
				}

				if (lipp.getVldl() != null) {
					cell.setColspan(2);
					certificate.addToTable(leftTable, cell, "  VLDL", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(leftTable, cellBorder, String.valueOf(lipp.getVldl()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(leftTable, cell, "mmol/L  0.050-1.04", certificate.getFontDocNormal(),
							Element.ALIGN_LEFT);

					cell.setColspan(4);
					certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					withResult = true;
				}

				if (lipp.getHdlRatio() != null) {
					cell.setColspan(2);
					certificate.addToTable(leftTable, cell, "  Cholesterol/HDL Ratio", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(leftTable, cellBorder, String.valueOf(lipp.getHdlRatio()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(leftTable, cell, "mmol/L  2.5-4.1", certificate.getFontDocNormal(),
							Element.ALIGN_LEFT);

					cell.setColspan(4);
					certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					withResult = true;
				}

			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(leftTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(leftTable);
			tableReport.addCell(rightTable);
		}

		// ORAL GLUCOSE TOLERANCE TEST
		QisLaboratoryProcedureService ogtTest = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("OGTT")).findAny().orElse(null);
		if (ogtTest != null) {
			boolean withResult = false;
			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Oral Glucose Tolerance Test (OGTT)",
					certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			PdfPTable leftTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			leftTable.setWidthPercentage(100f);
			leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable rightTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			rightTable.setWidthPercentage(100f);
			rightTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			QisTransactionLabOGTT ogtt = qisChemistry.getOgtt();

			if (ogtt != null && ogtt.getOgtt1Hr() != null) {
				cell.setColspan(2);
				certificate.addToTable(leftTable, cell, "  OGTT 1Hr", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(leftTable, cellBorder, String.valueOf(ogtt.getOgtt1Hr()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "mmol/L  < 11.0", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);

				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder, String.valueOf(ogtt.getOgtt1HrConventional()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  < 200", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (ogtt != null && ogtt.getOgtt2Hr() != null) {
				cell.setColspan(2);
				certificate.addToTable(leftTable, cell, "  OGTT 2Hr", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(leftTable, cellBorder, String.valueOf(ogtt.getOgtt2Hr()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "mmol/L  < 11.0", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);

				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder, String.valueOf(ogtt.getOgtt2HrConventional()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  < 200", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(leftTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(leftTable);
			tableReport.addCell(rightTable);
		}

		// ORAL GLUCOSE CHALLENGE TEST (50G)
		QisLaboratoryProcedureService ogcTest = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("OGCT")).findAny().orElse(null);
		if (ogcTest != null) {
			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Oral Glucose Challenge Test (OGCT - 50G)",
					certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			PdfPTable leftTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			leftTable.setWidthPercentage(100f);
			leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable rightTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			rightTable.setWidthPercentage(100f);
			rightTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			if (qisChemistry.getOgct() != null && qisChemistry.getOgct().getOgct() != null) {
				cell.setColspan(2);
				certificate.addToTable(leftTable, cell, "  Result", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(leftTable, cellBorder, String.valueOf(qisChemistry.getOgct().getOgct()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(leftTable, cell, "mmol/L  < 7.7", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);

				certificate.addToTable(rightTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cellBorder, String.valueOf(qisChemistry.getOgct().getConventional()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(rightTable, cell, "mg/dl  < 140", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				cell.setColspan(4);
				certificate.addToTable(leftTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(rightTable, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(leftTable);
			tableReport.addCell(rightTable);
		}

		int size = 0;
		// SERUM ELECTROLYTES
		QisLaboratoryProcedureService serumElectrolytes = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("ELEC")).findAny().orElse(null);
		if (serumElectrolytes != null) {
			size++;
			PdfPTable electTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			electTable.setWidthPercentage(100f);
			electTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(electTable, cell, "Serum Electrolytes", certificate.getFontDocValue(),
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			QisTransactionLabElectrolytes elec = qisChemistry.getElectrolytes();

			boolean withResult = false;
			cell.setColspan(1);
			if (elec != null && elec.getSodium() != null) {
				certificate.addToTable(electTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cell, "Sodium", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cellBorder, String.valueOf(elec.getSodium()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(electTable, cell, "mmol/L  135 - 153", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (elec != null && elec.getPotassium() != null) {
				certificate.addToTable(electTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cell, "Potassium", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cellBorder, String.valueOf(elec.getPotassium()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(electTable, cell, "mmol/L  3.50 - 5.30", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (elec != null && elec.getChloride() != null) {
				certificate.addToTable(electTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cell, "Chloride", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cellBorder, String.valueOf(elec.getChloride()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(electTable, cell, "mmol/L  98-107", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (elec != null && elec.getMagnesium() != null) {
				certificate.addToTable(electTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cell, "Magnesium", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cellBorder, String.valueOf(elec.getMagnesium()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(electTable, cell, "mmol/L  0.70-0.98", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (elec != null && elec.getTotalCalcium() != null) {
				certificate.addToTable(electTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cell, "Total Calcium", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cellBorder, String.valueOf(elec.getTotalCalcium()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(electTable, cell, "mmol/L  2.30-2.70", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (elec != null && elec.getIonizedCalcium() != null) {
				certificate.addToTable(electTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cell, "Ionized Calcium", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cellBorder, String.valueOf(elec.getIonizedCalcium()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(electTable, cell, "mmol/L  1.13-1.32", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (elec != null && elec.getInorganicPhosphorus() != null) {
				certificate.addToTable(electTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cell, "Inorganic Phosphorus", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(electTable, cellBorder, String.valueOf(elec.getInorganicPhosphorus()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(electTable, cell, "mmol/L  1.13-1.32", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(electTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
			}

			cell.setColspan(4);
			certificate.addToTable(electTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(electTable);
		}

		// SERUM ENZYMES
		QisLaboratoryProcedureService serumEnzymes = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("ENZY")).findAny().orElse(null);
		if (serumEnzymes != null) {
			size++;
			PdfPTable enzyTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			enzyTable.setWidthPercentage(100f);
			enzyTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(enzyTable, cell, "Serum Enzymes", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			QisTransactionLabEnzymes enzy = qisChemistry.getEnzymes();

			boolean withResult = false;
			cell.setColspan(1);
			if (enzy != null && enzy.getSgptAlt() != null) {
				certificate.addToTable(enzyTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cell, "SGPT/ALT", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cellBorder, String.valueOf(enzy.getSgptAlt()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(enzyTable, cell, "U/L  " + (isMale ? "M: 0 - 40" : "F: 0 - 40"),
						certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withResult = true;
			}

			if (enzy != null && enzy.getSgotAst() != null) {
				certificate.addToTable(enzyTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cell, "SGOT/AST", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cellBorder, String.valueOf(enzy.getSgotAst()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(enzyTable, cell, "U/L  0 - 40", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (enzy != null && enzy.getAmylase() != null) {
				certificate.addToTable(enzyTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cell, "Amylase", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cellBorder, String.valueOf(enzy.getAmylase()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(enzyTable, cell, "U/L  22 - 80", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (enzy != null && enzy.getLipase() != null) {
				certificate.addToTable(enzyTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cell, "Lipase", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cellBorder, String.valueOf(enzy.getLipase()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(enzyTable, cell, "U/L  0 - 62", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (enzy != null && enzy.getGgtp() != null) {
				certificate.addToTable(enzyTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cell, "Gamma-Glutamyl Transferase (GGT)",
						certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cellBorder, String.valueOf(enzy.getGgtp()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(enzyTable, cell, "U/L  10 - 55", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (enzy != null && enzy.getLdh() != null) {
				certificate.addToTable(enzyTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cell, "Lactate Dehydrogenase (LDH)", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cellBorder, String.valueOf(enzy.getLdh()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(enzyTable, cell, "U/L  125-220", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (enzy != null && enzy.getAlp() != null) {
				certificate.addToTable(enzyTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cell, "Alkaline Phosphatase (ALP)", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(enzyTable, cellBorder, String.valueOf(enzy.getAlp()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(enzyTable, cell, "U/L  25 - 140", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(enzyTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
			}

			cell.setColspan(4);
			certificate.addToTable(enzyTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(enzyTable);
		}

		// CREATINE PHOSPHOKINASE
		QisLaboratoryProcedureService creatinePhosphokinase = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("CPK")).findAny().orElse(null);
		if (creatinePhosphokinase != null) {
			size++;
			PdfPTable cpkTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			cpkTable.setWidthPercentage(100f);
			cpkTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(cpkTable, cell, "Creatine Phosphokinase (CPK)", certificate.getFontDocValue(),
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			QisTransactionLabCPK cpk = qisChemistry.getCpk();

			boolean withResult = false;
			cell.setColspan(1);
			if (cpk != null && cpk.getCpkMB() != null) {
				certificate.addToTable(cpkTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(cpkTable, cell, "CPK-MB", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(cpkTable, cellBorder, String.valueOf(cpk.getCpkMB()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(cpkTable, cell, "U/L  0 - 25", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (cpk != null && cpk.getCpkMM() != null) {
				certificate.addToTable(cpkTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(cpkTable, cell, "CPK-MM", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(cpkTable, cellBorder, String.valueOf(cpk.getCpkMM()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(cpkTable, cell, "U/L  25 - 170", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (cpk != null && cpk.getTotalCpk() != null) {
				certificate.addToTable(cpkTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(cpkTable, cell, "Total CPK", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(cpkTable, cellBorder, String.valueOf(cpk.getTotalCpk()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(cpkTable, cell, "U/L  25 - 195", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(cpkTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
			}

			cell.setColspan(4);
			certificate.addToTable(cpkTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(cpkTable);
		}

		// BILIRUBIN
		QisLaboratoryProcedureService bilirubin = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("BILI")).findAny().orElse(null);
		if (bilirubin != null) {
			size++;
			PdfPTable biliTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			biliTable.setWidthPercentage(100f);
			biliTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(biliTable, cell, "Bilirubin", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			QisTransactionLabBilirubin bili = qisChemistry.getBilirubin();

			boolean withResult = false;
			cell.setColspan(1);
			if (bili != null && bili.getTotalAdult() != null) {
				certificate.addToTable(biliTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(biliTable, cell, "Total (Adult)", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(biliTable, cellBorder, String.valueOf(bili.getTotalAdult()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(biliTable, cell, "umol/L  5 - 21", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (bili != null && bili.getDirect() != null) {
				certificate.addToTable(biliTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(biliTable, cell, "Direct", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(biliTable, cellBorder, String.valueOf(bili.getDirect()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(biliTable, cell, "umol/L  0 - 6.9", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (bili != null && bili.getIndirect() != null) {
				certificate.addToTable(biliTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(biliTable, cell, "Indirect", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(biliTable, cellBorder, String.valueOf(bili.getIndirect()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(biliTable, cell, "umol/L  5 - 14.1", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(biliTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
			}

			cell.setColspan(4);
			certificate.addToTable(biliTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(biliTable);
		}

		// SERUM PROTEIN
		QisLaboratoryProcedureService protein = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("PROT")).findAny().orElse(null);
		if (protein != null) {
			size++;
			PdfPTable protTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			protTable.setWidthPercentage(100f);
			protTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(protTable, cell, "Serum Protein", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			QisTransactionLabProtein prot = qisChemistry.getProtein();

			boolean withResult = false;
			cell.setColspan(1);
			if (prot != null && prot.getTotalProtein() != null) {
				certificate.addToTable(protTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(protTable, cell, "Total Protein", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(protTable, cellBorder, String.valueOf(prot.getTotalProtein()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(protTable, cell, "g/L  66-83", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (prot != null && prot.getAlbumin() != null) {
				certificate.addToTable(protTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(protTable, cell, "Albumin", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(protTable, cellBorder, String.valueOf(prot.getAlbumin()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(protTable, cell, "g/L  38-51", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (prot != null && prot.getGlobulin() != null) {
				certificate.addToTable(protTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(protTable, cell, "Globulin", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(protTable, cellBorder, String.valueOf(prot.getGlobulin()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(protTable, cell, "g/L  23-35", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (prot != null && prot.getAGRatio() != null) {
				certificate.addToTable(protTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(protTable, cell, "Albumin-Globulin Ratio", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(protTable, cellBorder, String.valueOf(prot.getAGRatio()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(protTable, cell, "g/L  1.5 - 3.0", certificate.getFontDocNormal(),
						Element.ALIGN_LEFT);
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(protTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
			}

			cell.setColspan(4);
			certificate.addToTable(protTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(protTable);
		}

		// HEMOGLOBIN A1C
		QisLaboratoryProcedureService hemoglobin = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("HBA1C")).findAny().orElse(null);
		if (hemoglobin != null) {
			size++;
			PdfPTable hbTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			hbTable.setWidthPercentage(100f);
			hbTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(20);
			certificate.addToTable(hbTable, cell, "Hemoglobin A1C", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			if (qisChemistry.getHemoglobin() != null && qisChemistry.getHemoglobin().getHemoglobinA1C() != null) {
				cell.setColspan(1);
				certificate.addToTable(hbTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(hbTable, cell, "Result", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(hbTable, cellBorder,
						String.valueOf(qisChemistry.getHemoglobin().getHemoglobinA1C()) + " %",
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
				certificate.addToTable(hbTable, cell, "4.3 - 6.3", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				cell.setColspan(4);
				certificate.addToTable(hbTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
			}

			cell.setColspan(4);
			certificate.addToTable(hbTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			tableReport.addCell(hbTable);
		}

		if (size % 2 == 1) { // odd columns
			tableReport.addCell("");
		}

		// OTHER NOTES
		if (qisChemistry.getOtherNotes() != null && !"".equals(qisChemistry.getOtherNotes().trim())) {
			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Other Notes:", certificate.getFontDocValue(),
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			certificate.addToTable(tableReport, cell, qisChemistry.getOtherNotes(), certificate.getFontDocNormal(),
					Element.ALIGN_LEFT);
		}
	}

	public void formatChemistryConsolidated(QisTransactionChemistry qisChemistry, PdfPTable tableReport,
			Certificate certificate, boolean isMale) {
		QisItem itemDetails = qisChemistry.getItemDetails();
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

		Font fontNoResult = FontFactory.getFont("GARAMOND_BOLD");
		fontNoResult.setSize(8);
		fontNoResult.setColor(Color.RED);

		boolean withData = false;

		// FASTING BLOOD SUGAR
		QisLaboratoryProcedureService fastingBloodSugar = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("FBS")).findAny().orElse(null);
		if (fastingBloodSugar != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "Fasting Blood Sugar", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getFbs() != null && qisChemistry.getFbs().getFbs() != null) {
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisChemistry.getFbs().getFbs(), 3.89, 6.11),
						certificate.getDisplayFontRange(qisChemistry.getFbs().getFbs(), 3.89, 6.11, fontResult,
								fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "3.89 - 6.11", fontUnit, Element.ALIGN_RIGHT);

			if (qisChemistry.getFbs() != null && qisChemistry.getFbs().getConventional() != null) {
				certificate.addToTable(tableReport, cell,
						"   " + certificate.getDisplaySymbol(qisChemistry.getFbs().getConventional(), 71.0, 111.0),
						certificate.getDisplayFontRange(qisChemistry.getFbs().getConventional(), 71.0, 111.0,
								fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "70.0 - 110.0", fontUnit, Element.ALIGN_RIGHT);
			withData = true;
		}

		// RANDOM BLOOD SUGAR
		QisLaboratoryProcedureService randomBloodSugar = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("RBS")).findAny().orElse(null);
		if (randomBloodSugar != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "Random Blood Sugar", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getRbs() != null && qisChemistry.getRbs().getRbs() != null) {
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisChemistry.getRbs().getRbs(), 0.0, 7.7),
						certificate.getDisplayFontRange(qisChemistry.getRbs().getRbs(), 0.0, 7.7, fontResult,
								fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "< 7.7", fontUnit, Element.ALIGN_RIGHT);

			if (qisChemistry.getRbs() != null && qisChemistry.getRbs().getConventional() != null) {
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisChemistry.getRbs().getConventional(), 0.0, 140.0),
						certificate.getDisplayFontRange(qisChemistry.getRbs().getConventional(), 0.0, 140.0, fontResult,
								fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "< 140", fontUnit, Element.ALIGN_RIGHT);
			withData = true;
		}

		// POST PRANDIAL RANDOM BLOOD SUGAR
		QisLaboratoryProcedureService ppRandomBloodSugar = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("PPRBS")).findAny().orElse(null);
		if (ppRandomBloodSugar != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "Post Prandial RBS", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(1);

			if (qisChemistry.getPprbs() != null && qisChemistry.getPprbs().getPprbs() != null) {
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisChemistry.getPprbs().getPprbs(), 0.0, 7.7),
						certificate.getDisplayFontRange(qisChemistry.getPprbs().getPprbs(), 0.0, 7.7, fontResult,
								fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}

			certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "< 7.7", fontUnit, Element.ALIGN_RIGHT);

			if (qisChemistry.getPprbs() != null && qisChemistry.getPprbs().getConventional() != null) {
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisChemistry.getPprbs().getConventional(), 0.0, 140.0),
						certificate.getDisplayFontRange(qisChemistry.getPprbs().getConventional(), 0.0, 140.0,
								fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "< 140", fontUnit, Element.ALIGN_RIGHT);
			withData = true;
		}

		// URIC ACID
		QisLaboratoryProcedureService uricAcid = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("URAC")).findAny().orElse(null);
		if (uricAcid != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "Uric Acid", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getUricAcid() != null && qisChemistry.getUricAcid().getUricAcid() != null) {
				certificate.addToTable(tableReport, cell,
						"   " + certificate.getDisplaySymbol(qisChemistry.getUricAcid().getUricAcid(), 142.0, 428.0),
						certificate.getDisplayFontRange(qisChemistry.getUricAcid().getUricAcid(), 142.0, 428.0,
								fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "142 - 428", fontUnit, Element.ALIGN_RIGHT);

			if (qisChemistry.getUricAcid() != null && qisChemistry.getUricAcid().getConventional() != null) {
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisChemistry.getUricAcid().getConventional(), 2.39, 7.20),
						certificate.getDisplayFontRange(qisChemistry.getUricAcid().getConventional(), 2.39, 7.20,
								fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "2.39 - 7.20", fontUnit, Element.ALIGN_RIGHT);
			withData = true;
		}

		// BLOOD UREA NITROGEN
		QisLaboratoryProcedureService bloodUreaNitrogen = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("BUN")).findAny().orElse(null);
		if (bloodUreaNitrogen != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "Blood Urea Nitrogen", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getBun() != null && qisChemistry.getBun().getBun() != null) {
				certificate.addToTable(tableReport, cell,
						"   " + certificate.getDisplaySymbol(qisChemistry.getBun().getBun(), 2.90, 8.20),
						certificate.getDisplayFontRange(qisChemistry.getBun().getBun(), 2.90, 8.20, fontResult,
								fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "2.90 - 8.20", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "   mmol/L", certificate.getFontDocNormal(), Element.ALIGN_RIGHT);

			if (qisChemistry.getBun() != null && qisChemistry.getBun().getConventional() != null) {
				cell.setRowspan(1);
				certificate.addToTable(tableReport, cell,
						"   " + certificate.getDisplaySymbol(qisChemistry.getBun().getConventional(), 8.12, 22.97),
						certificate.getDisplayFontRange(qisChemistry.getBun().getConventional(), 8.12, 22.97, fontResult,
								fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "8.12 - 22.97", fontUnit, Element.ALIGN_RIGHT);
			withData = true;
		}

		// CREATININE
		QisLaboratoryProcedureService creatinine = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("CREA")).findAny().orElse(null);
		if (creatinine != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "Creatinine", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getCreatinine() != null && qisChemistry.getCreatinine().getCreatinine() != null) {
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisChemistry.getCreatinine().getCreatinine(), isMale ? 45.0 : 45.0,
								isMale ? 104.0 : 104.0),
						certificate.getDisplayFontRange(qisChemistry.getCreatinine().getCreatinine(),
								isMale ? 45.0 : 45.0, isMale ? 104.0 : 104.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "umol/L", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, isMale ? "M: 45 - 104" : "F: 45 - 104", fontUnit,
					Element.ALIGN_RIGHT);

			if (qisChemistry.getCreatinine() != null && qisChemistry.getCreatinine().getConventional() != null) {
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisChemistry.getCreatinine().getConventional(), isMale ? 0.51 : 0.51,
								isMale ? 1.18 : 1.18),
						certificate.getDisplayFontRange(qisChemistry.getCreatinine().getConventional(),
								isMale ? 0.51 : 0.51, isMale ? 1.18 : 1.18, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				cell.setRowspan(2);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, isMale ? "M: 0.51 - 1.18" : "F: 0.51 - 1.18", fontUnit,
					Element.ALIGN_RIGHT);
			withData = true;
		}

		// LIPID PROFILE
		QisLaboratoryProcedureService lipidProfile = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("LIPP")).findAny().orElse(null);

		if (lipidProfile != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			boolean withResult = false;
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(9);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Lipid Profile", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			QisTransactionLabLipidProfile lipp = qisChemistry.getLipidProfile();
			if (lipp != null) {
				if (lipp.getCholesterol() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "  Cholesterol", fontTest, Element.ALIGN_LEFT);

					cell.setColspan(1);
					certificate.addToTable(tableReport, cell,
							certificate.getDisplaySymbol(lipp.getCholesterol(), 0.0, 5.20), certificate
									.getDisplayFontRange(lipp.getCholesterol(), 0.0, 5.20, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "< 5.20", fontUnit, Element.ALIGN_RIGHT);

					certificate.addToTable(tableReport, cell, String.valueOf(lipp.getCholesterolConventional()),
							certificate.getDisplayFontRange(lipp.getCholesterolConventional(), 0.0, 200.0, fontResult,
									fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "< 200", fontUnit, Element.ALIGN_RIGHT);

					withResult = true;
				}

				if (lipp.getTriglycerides() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "  Triglycerides", fontTest, Element.ALIGN_LEFT);

					cell.setColspan(1);
					certificate.addToTable(tableReport, cell,
							certificate.getDisplaySymbol(lipp.getTriglycerides(), 0.3, 1.7), certificate
									.getDisplayFontRange(lipp.getTriglycerides(), 0.3, 1.7, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "0.3-1.7", fontUnit, Element.ALIGN_RIGHT);

					certificate.addToTable(tableReport, cell,
							certificate.getDisplaySymbol(lipp.getTriglyceridesConventional(), 26.54, 150.0),
							certificate.getDisplayFontRange(lipp.getTriglyceridesConventional(), 26.54, 150.0,
									fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "26.54-150", fontUnit, Element.ALIGN_RIGHT);

					withResult = true;
				}

				if (lipp.getHdl() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "  HDL", fontTest, Element.ALIGN_LEFT);

					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(lipp.getHdl(), 0.91, 1.55),
							certificate.getDisplayFontRange(lipp.getHdl(), 0.91, 1.55, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "0.91-1.55", fontUnit, Element.ALIGN_RIGHT);

					certificate.addToTable(tableReport, cell,
							certificate.getDisplaySymbol(lipp.getHdlConventional(), 35.0, 60.0),
							certificate.getDisplayFontRange(lipp.getHdlConventional(), 35.0, 60.0, fontResult,
									fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "35-60", fontUnit, Element.ALIGN_RIGHT);

					withResult = true;
				}

				if (lipp.getCholesterol() != null && lipp.getTriglycerides() != null && lipp.getHdl() != null) {
					if (lipp.getLdl() != null) {
						cell.setColspan(3);
						cell.setRowspan(2);
						certificate.addToTable(tableReport, cell, "  LDL", fontTest, Element.ALIGN_LEFT);

						cell.setColspan(1);
						certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(lipp.getLdl(), 2.5, 4.1),
								certificate.getDisplayFontRange(lipp.getLdl(), 2.5, 4.1, fontResult, fontResultRed),
								Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "2.5-4.1", fontUnit, Element.ALIGN_RIGHT);

						certificate.addToTable(tableReport, cell,
								certificate.getDisplaySymbol(lipp.getLdlConventional(), 97.0, 159.0),
								certificate.getDisplayFontRange(lipp.getLdlConventional(), 97.0, 159.0, fontResult,
										fontResultRed),
								Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "97-159", fontUnit, Element.ALIGN_RIGHT);
					}

					if (lipp.getVldl() != null) {
						cell.setColspan(3);
						cell.setRowspan(2);
						certificate.addToTable(tableReport, cell, "  VLDL", fontTest, Element.ALIGN_LEFT);

						cell.setColspan(1);
						certificate.addToTable(tableReport, cell,
								certificate.getDisplaySymbol(lipp.getVldl(), 0.05, 1.04),
								certificate.getDisplayFontRange(lipp.getVldl(), 0.05, 1.04, fontResult, fontResultRed),
								Element.ALIGN_RIGHT);

						certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "0.050-1.04", fontUnit, Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
					}

					if (lipp.getHdlRatio() != null) {
						cell.setColspan(3);
						cell.setRowspan(2);
						certificate.addToTable(tableReport, cell, "  Cholesterol/HDL Ratio", fontTest,
								Element.ALIGN_LEFT);

						cell.setColspan(1);
						certificate.addToTable(tableReport, cell,
								certificate.getDisplaySymbol(lipp.getHdlRatio(), 2.5, 4.1), certificate
										.getDisplayFontRange(lipp.getHdlRatio(), 2.5, 4.1, fontResult, fontResultRed),
								Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "2.5-4.1", fontUnit, Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
					}
				}
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// ORAL GLUCOSE TOLERANCE TEST
		QisLaboratoryProcedureService ogtTest = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("OGTT")).findAny().orElse(null);
		if (ogtTest != null) {
			boolean withResult = false;
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Oral Glucose Tolerance Test (OGTT)", fontResult,
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			QisTransactionLabOGTT ogtt = qisChemistry.getOgtt();

			if (ogtt != null && ogtt.getOgtt1Hr() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "OGTT 1Hr", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						"   " + certificate.getDisplaySymbol(ogtt.getOgtt1Hr(), 0.0, 11.0),
						certificate.getDisplayFontRange(ogtt.getOgtt1Hr(), 0.0, 11.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "< 11.0", fontUnit, Element.ALIGN_RIGHT);

				certificate.addToTable(tableReport, cell,
						"   " + certificate.getDisplaySymbol(ogtt.getOgtt1Hr(), 0.0, 200.0),
						certificate.getDisplayFontRange(ogtt.getOgtt1Hr(), 0.0, 200.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "< 200", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (ogtt != null && ogtt.getOgtt2Hr() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "OGTT 2Hr", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						"   " + certificate.getDisplaySymbol(ogtt.getOgtt2Hr(), 0.0, 11.0),
						certificate.getDisplayFontRange(ogtt.getOgtt2Hr(), 0.0, 11.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "< 11.0", fontUnit, Element.ALIGN_RIGHT);

				certificate.addToTable(tableReport, cell,
						"   " + certificate.getDisplaySymbol(ogtt.getOgtt2HrConventional(), 0.0, 200.0),
						certificate.getDisplayFontRange(ogtt.getOgtt2HrConventional(), 0.0, 200.0, fontResult,
								fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "< 200", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// ORAL GLUCOSE CHALLENGE TEST (50G)
		QisLaboratoryProcedureService ogcTest = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("OGCT")).findAny().orElse(null);

		if (ogcTest != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
			cell.setColspan(3);
			cell.setRowspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Oral Glucose Challenge Test (OGCT - 50G)", fontResult,
					Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_TOP);
			if (qisChemistry.getOgct() != null && qisChemistry.getOgct().getOgct() != null) {
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisChemistry.getOgct().getOgct(), 0.0, 7.7),
						certificate.getDisplayFontRange(qisChemistry.getOgct().getOgct(), 0.0, 7.7, fontResult,
								fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "< 7.7", fontUnit, Element.ALIGN_RIGHT);

				certificate.addToTable(tableReport, cell, String.valueOf(qisChemistry.getOgct().getConventional()),
						certificate.getDisplayFontRange(qisChemistry.getOgct().getConventional(), 0.0, 140.0,
								fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mg/dl", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "< 140", fontUnit, Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			withData = true;
		}

		// SERUM ELECTROLYTES
		QisLaboratoryProcedureService serumElectrolytes = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("ELEC")).findAny().orElse(null);

		if (serumElectrolytes != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			QisTransactionLabElectrolytes elec = qisChemistry.getElectrolytes();
			if (elec.getTotalIron() == null) {				
				certificate.addToTable(tableReport, cell, "Serum Electrolytes", fontResult, Element.ALIGN_LEFT);
			}else {
				certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
			}
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);

			boolean withResult = false;
			if (elec != null && elec.getSodium() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Sodium", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(elec.getSodium(), 135.0, 153.0),
						certificate.getDisplayFontRange(elec.getSodium(), 135.0, 153.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "135 - 153", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (elec != null && elec.getPotassium() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Potassium", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(elec.getPotassium(), 3.5, 5.3),
						certificate.getDisplayFontRange(elec.getPotassium(), 3.5, 5.3, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);

				certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "3.50 - 5.30", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (elec != null && elec.getChloride() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Chloride", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(elec.getChloride(), 98.0, 107.0),
						certificate.getDisplayFontRange(elec.getChloride(), 98.0, 107.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "98-107", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (elec != null && elec.getMagnesium() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Magnesium", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(elec.getMagnesium(), 0.7, 0.98),
						certificate.getDisplayFontRange(elec.getMagnesium(), 0.7, 0.98, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "0.70-0.98", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (elec != null && elec.getTotalCalcium() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Total Calcium", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(elec.getTotalCalcium(), 2.3, 2.7),
						certificate.getDisplayFontRange(elec.getTotalCalcium(), 2.3, 2.7, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "2.30-2.70", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (elec != null && elec.getIonizedCalcium() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Ionized Calcium", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(elec.getIonizedCalcium(), 1.13, 1.32), certificate
								.getDisplayFontRange(elec.getIonizedCalcium(), 1.13, 1.32, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "1.13-1.32", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (elec != null && elec.getInorganicPhosphorus() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Inorganic Phosphorus", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(elec.getInorganicPhosphorus(), 0.87, 1.45),
						certificate.getDisplayFontRange(elec.getInorganicPhosphorus(), 0.87, 1.45, fontResult,
								fontResultRed),
						Element.ALIGN_RIGHT);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "0.87-1.45", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}
			
			if (elec != null && elec.getTotalIron() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Total Iron(Fe)", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(elec.getTotalIron(), 8.95, 30.43),
						certificate.getDisplayFontRange(elec.getTotalIron(), 8.95, 30.43, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);

				certificate.addToTable(tableReport, cell, "mmol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "8.95 - 30.43", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_RIGHT);
			}
			withData = true;
		}

		// SERUM ENZYMES
		QisLaboratoryProcedureService serumEnzymes = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("ENZY")).findAny().orElse(null);

		if (serumEnzymes != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setColspan(9);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Serum Enzymes", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			QisTransactionLabEnzymes enzy = qisChemistry.getEnzymes();

			boolean withResult = false;
			if (enzy != null && enzy.getSgptAlt() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   SGPT/ALT", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(enzy.getSgptAlt(), isMale ? 0.0 : 0.0, isMale ? 40.0 : 40.0),
						certificate.getDisplayFontRange(enzy.getSgptAlt(), isMale ? 0.0 : 0.0, isMale ? 40.0 : 40.0,
								fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "U/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, isMale ? "M: 0 - 40" : "F: 0 - 40", fontUnit,
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (enzy != null && enzy.getSgotAst() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   SGOT/AST", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(enzy.getSgotAst(), 0.0, 40.0),
						certificate.getDisplayFontRange(enzy.getSgotAst(), 0.0, 40.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "U/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "0 - 40", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (enzy != null && enzy.getAmylase() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Amylase", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(enzy.getAmylase(), 22.0, 80.0),
						certificate.getDisplayFontRange(enzy.getAmylase(), 22.0, 80.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "U/L", certificate.getFontDocNormal(), Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "22 - 80", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (enzy != null && enzy.getLipase() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Lipase", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(enzy.getLipase(), 0.0, 62.0),
						certificate.getDisplayFontRange(enzy.getLipase(), 0.0, 62.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "U/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "0 - 62", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (enzy != null && enzy.getGgtp() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Gamma-Glutamyl Transferase (GGT)", fontTest,
						Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(enzy.getGgtp(), 10.0, 55.0),
						certificate.getDisplayFontRange(enzy.getGgtp(), 10.0, 55.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "U/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "10 - 55", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (enzy != null && enzy.getLdh() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Lactate Dehydrogenase (LDH)", fontTest,
						Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(enzy.getLdh(), 125.0, 220.0),
						certificate.getDisplayFontRange(enzy.getLdh(), 125.0, 220.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "U/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "125-220", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (enzy != null && enzy.getAlp() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Alkaline Phosphatase (ALP)", fontTest,
						Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(enzy.getAlp(), 25.0, 140.0),
						certificate.getDisplayFontRange(enzy.getAlp(), 25.0, 140.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "U/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "25 - 140", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_RIGHT);
			}
			withData = true;
		}

		// CREATINE PHOSPHOKINASE
		QisLaboratoryProcedureService creatinePhosphokinase = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("CPK")).findAny().orElse(null);

		if (creatinePhosphokinase != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setColspan(9);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Creatine Phosphokinase (CPK)", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			QisTransactionLabCPK cpk = qisChemistry.getCpk();
			boolean withResult = false;
			if (cpk != null && cpk.getCpkMB() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   CPK-MB", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cpk.getCpkMB(), 0.0, 25.0),
						certificate.getDisplayFontRange(cpk.getCpkMB(), 0.0, 25.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "U/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "0 - 25", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (cpk != null && cpk.getCpkMM() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   CPK-MM", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cpk.getCpkMM(), 25.0, 170.0),
						certificate.getDisplayFontRange(cpk.getCpkMM(), 25.0, 170.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "U/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "25 - 170", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);

				withResult = true;
			}

			if (cpk != null && cpk.getTotalCpk() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Total CPK", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(cpk.getTotalCpk(), 25.0, 195.0),
						certificate.getDisplayFontRange(cpk.getTotalCpk(), 25.0, 195.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "U/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "25 - 195", fontUnit, Element.ALIGN_RIGHT);
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

		// BILIRUBIN
		QisLaboratoryProcedureService bilirubin = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("BILI")).findAny().orElse(null);

		if (bilirubin != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setColspan(9);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Bilirubin", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			QisTransactionLabBilirubin bili = qisChemistry.getBilirubin();

			boolean withResult = false;
			if (bili != null && bili.getTotalAdult() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Total (Adult)", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(bili.getTotalAdult(), 5.0, 21.0),
						certificate.getDisplayFontRange(bili.getTotalAdult(), 5.0, 21.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "umol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "5 - 21", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (bili != null && bili.getDirect() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Direct", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(bili.getDirect(), 0.0, 6.9),
						certificate.getDisplayFontRange(bili.getDirect(), 0.0, 6.9, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "umol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "0 - 6.9", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (bili != null && bili.getIndirect() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Indirect", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(bili.getIndirect(), 5.0, 14.1),
						certificate.getDisplayFontRange(bili.getIndirect(), 5.0, 14.1, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "umol/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "5 - 14.1", fontUnit, Element.ALIGN_RIGHT);
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

		// SERUM PROTEIN
		QisLaboratoryProcedureService protein = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("PROT")).findAny().orElse(null);

		if (protein != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setColspan(9);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Serum Protein", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			QisTransactionLabProtein prot = qisChemistry.getProtein();
			boolean withResult = false;
			if (prot != null && prot.getTotalProtein() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Total Protein", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(prot.getTotalProtein(), 62.0, 80.0),
						certificate.getDisplayFontRange(prot.getTotalProtein(), 62.0, 80.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "g/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "62 - 80", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (prot != null && prot.getAlbumin() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Albumin", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(prot.getAlbumin(), 35.0, 50.0),
						certificate.getDisplayFontRange(prot.getAlbumin(), 35.0, 50.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "g/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, " 35 - 50", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (prot != null && prot.getGlobulin() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Globulin", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(prot.getGlobulin(), 27.0, 30.0),
						certificate.getDisplayFontRange(prot.getGlobulin(), 27.0, 30.0, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "g/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "27 - 30", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				withResult = true;
			}

			if (prot != null && prot.getAGRatio() != null) {
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Albumin-Globulin Ratio", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(prot.getAGRatio(), 1.24, 1.67),
						certificate.getDisplayFontRange(prot.getAGRatio(), 1.24, 1.67, fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "g/L", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "1.24 - 1.67", fontUnit, Element.ALIGN_RIGHT);
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

		// HEMOGLOBIN A1C
		QisLaboratoryProcedureService hemoglobin = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("HBA1C")).findAny().orElse(null);

		if (hemoglobin != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			cell.setColspan(3);
			cell.setRowspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Hemoglobin A1C", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
			cell.setColspan(1);
			if (qisChemistry.getHemoglobin() != null && qisChemistry.getHemoglobin().getHemoglobinA1C() != null) {
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisChemistry.getHemoglobin().getHemoglobinA1C(), 4.3, 6.3),
						certificate.getDisplayFontRange(qisChemistry.getHemoglobin().getHemoglobinA1C(), 4.3, 6.3,
								fontResult, fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "%", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "4.3 - 6.3", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			withData = true;
		}

		// TOTAL IRON BINDING CAPACITY
		QisLaboratoryProcedureService totalIronBindingCapacity = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("TIBC")).findAny().orElse(null);
		if (totalIronBindingCapacity != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(3);
			cell.setRowspan(2);
			certificate.addToTable(tableReport, cell, "Total Iron Binding Capacity", fontTest, Element.ALIGN_LEFT);
			cell.setColspan(1);
			if (qisChemistry.getTibc() != null && qisChemistry.getTibc().getResult() != null) {
				certificate.addToTable(tableReport, cell,
						certificate.getDisplaySymbol(qisChemistry.getTibc().getResult(), 45.0, 72.0),
						certificate.getDisplayFontRange(qisChemistry.getTibc().getResult(), 45.0, 72.0, fontResult,
								fontResultRed),
						Element.ALIGN_RIGHT);
			} else {
				certificate.addToTable(tableReport, cell, NO_RESULT, fontNoResult, Element.ALIGN_RIGHT);
			}
			certificate.addToTable(tableReport, cell, "umoL/L", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "45-72", fontUnit, Element.ALIGN_RIGHT);

			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
			withData = true;
		}

		// OTHER NOTES
		if (qisChemistry.getOtherNotes() != null && !"".equals(qisChemistry.getOtherNotes().trim())) {
			cell.setColspan(9);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Other Notes:", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			certificate.addToTable(tableReport, cell, qisChemistry.getOtherNotes(), fontTest, Element.ALIGN_LEFT);
			withData = true;
		}

		if (withData) {
			cell.setColspan(9);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
		}
	}

	public void formatChemistryClassification(QisTransactionChemistry qisChemistry, PdfPTable tableReport,
			Certificate certificate, PdfPCell cell, boolean isMale) {

		QisItem itemDetails = qisChemistry.getItemDetails();
		Set<QisLaboratoryProcedureService> services = itemDetails.getServiceRequest();

		// SERUM ENZYMES
		QisLaboratoryProcedureService serumEnzymes = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("ENZY")).findAny().orElse(null);
		if (serumEnzymes != null) {
			QisTransactionLabEnzymes enzy = qisChemistry.getEnzymes();

			cell.setBorderColor(Color.BLUE);
			cell.setColspan(4);
			certificate.addToTable(tableReport, cell, "SERUM ENZYMES", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);

			boolean withResult = false;
			cell.setColspan(1);
			if (enzy != null && enzy.getSgptAlt() != null) {
				certificate.addToTable(tableReport, cell, "  SGPT/ALT", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOTTOM);
				certificate.addToTable(tableReport, cell, String.valueOf(enzy.getSgptAlt()),
						certificate.getDisplayFontRange(enzy.getSgptAlt(), isMale ? 10.0 : 5.0, isMale ? 41.0 : 31.0,
								certificate.getFontDocValue(), certificate.getFontDocAlert()),
						Element.ALIGN_CENTER);
				cell.setBorder(Rectangle.NO_BORDER);
				certificate.addToTable(tableReport, cell, "U/L", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, isMale ? "M: 10 - 41" : "F: 5 - 31",
						certificate.getFontDocLabel(), Element.ALIGN_LEFT);

				withResult = true;
			}

			if (enzy != null && enzy.getSgotAst() != null) {
				certificate.addToTable(tableReport, cell, "  SGOT/AST", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOTTOM);
				certificate
						.addToTable(tableReport, cell, String.valueOf(enzy.getSgotAst()),
								certificate.getDisplayFontRange(enzy.getSgotAst(), 0.0, 40.0,
										certificate.getFontDocValue(), certificate.getFontDocAlert()),
								Element.ALIGN_CENTER);
				cell.setBorder(Rectangle.NO_BORDER);
				certificate.addToTable(tableReport, cell, "U/L", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, "0 - 40", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

				withResult = true;
			}

			if (enzy != null && enzy.getAmylase() != null) {
				certificate.addToTable(tableReport, cell, "  Amylase", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOTTOM);
				certificate
						.addToTable(tableReport, cell, String.valueOf(enzy.getAmylase()),
								certificate.getDisplayFontRange(enzy.getAmylase(), 22.0, 80.0,
										certificate.getFontDocValue(), certificate.getFontDocAlert()),
								Element.ALIGN_CENTER);
				cell.setBorder(Rectangle.NO_BORDER);
				certificate.addToTable(tableReport, cell, "U/L", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, "22 - 80", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

				withResult = true;
			}

			if (enzy != null && enzy.getLipase() != null) {
				certificate.addToTable(tableReport, cell, "  Lipase", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOTTOM);
				certificate
						.addToTable(tableReport, cell, String.valueOf(enzy.getLipase()),
								certificate.getDisplayFontRange(enzy.getLipase(), 0.0, 62.0,
										certificate.getFontDocValue(), certificate.getFontDocAlert()),
								Element.ALIGN_CENTER);
				cell.setBorder(Rectangle.NO_BORDER);
				certificate.addToTable(tableReport, cell, "U/L", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, "0 - 62", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

				withResult = true;
			}

			if (enzy != null && enzy.getGgtp() != null) {
				certificate.addToTable(tableReport, cell, "  Gamma-Glutamyl Transferase (GGT)",
						certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOTTOM);
				certificate
						.addToTable(tableReport, cell, String.valueOf(enzy.getGgtp()),
								certificate.getDisplayFontRange(enzy.getGgtp(), 10.0, 55.0,
										certificate.getFontDocValue(), certificate.getFontDocAlert()),
								Element.ALIGN_CENTER);
				cell.setBorder(Rectangle.NO_BORDER);
				certificate.addToTable(tableReport, cell, "U/L", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, "10 - 55", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

				withResult = true;
			}

			if (enzy != null && enzy.getLdh() != null) {
				certificate.addToTable(tableReport, cell, "  Lactate Dehydrogenase (LDH)",
						certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOTTOM);
				certificate.addToTable(
						tableReport, cell, String.valueOf(enzy.getLdh()), certificate.getDisplayFontRange(enzy.getLdh(),
								125.0, 220.0, certificate.getFontDocValue(), certificate.getFontDocAlert()),
						Element.ALIGN_CENTER);
				cell.setBorder(Rectangle.NO_BORDER);
				certificate.addToTable(tableReport, cell, "U/L", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, "125-220", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

				withResult = true;
			}

			if (enzy != null && enzy.getAlp() != null) {
				certificate.addToTable(tableReport, cell, "  Alkaline Phosphatase (ALP)", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOTTOM);
				certificate.addToTable(
						tableReport, cell, String.valueOf(enzy.getAlp()), certificate.getDisplayFontRange(enzy.getAlp(),
								25.0, 140.0, certificate.getFontDocValue(), certificate.getFontDocAlert()),
						Element.ALIGN_CENTER);
				cell.setBorder(Rectangle.NO_BORDER);
				certificate.addToTable(tableReport, cell, "U/L", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, "25 - 140", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);

				withResult = true;
			}

			if (!withResult) {
			}

			cell.setBorder(Rectangle.NO_BORDER);

			cell.setColspan(4);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
		}

	}
}
