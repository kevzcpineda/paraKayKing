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
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionClinicalMicroscopy;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabAFB;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabFecalysis;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabUrineChemical;

public class ClinicalMicroscopyCertificate {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private String NO_RESULT = "- NO RESULT -";

	public ClinicalMicroscopyCertificate(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getTransactionLaboratoryClinicalMicroscopyCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionClinicalMicroscopy qisClinicalMicroscopy,
			boolean withHeaderFooter) throws DocumentException, IOException {

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisClinicalMicroscopy.getLabPersonel(), qisClinicalMicroscopy.getQualityControl(),
				qisClinicalMicroscopy.getMedicalDoctor(), "Pathologist", qisClinicalMicroscopy.getVerifiedDate(),
				withHeaderFooter, false, "", null);
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
		certificate.addToTable(tableReport, cell, "CLINICAL MICROSCOPY", certificate.getFontValue(),
				Element.ALIGN_LEFT);

		formatClinicalMicroscopyCertificate(qisClinicalMicroscopy, tableReport, certificate, cell, cellBorder, isMale);

		document.add(tableReport);

		document.close();
		return document;
	}

	public void formatClinicalMicroscopyCertificate(QisTransactionClinicalMicroscopy qisClinicalMicroscopy,
			PdfPTable tableReport, Certificate certificate, PdfPCell cell, PdfPCell cellBorder, boolean isMale) {
		QisItem itemDetails = qisClinicalMicroscopy.getItemDetails();
		Set<QisLaboratoryProcedureService> services = itemDetails.getServiceRequest();
		int size = services.size();

		// URINE CHEMICAL
		QisLaboratoryProcedureService uchem = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("UCHEM")).findAny().orElse(null);
		if (uchem != null) {
			size++;
			QisTransactionLabUrineChemical urine = qisClinicalMicroscopy.getUrineChemical();

			if (urine != null) {
				PdfPTable uchemLeft = new PdfPTable(new float[] { 1, 4, 4, 4 });
				uchemLeft.setWidthPercentage(100f);
				uchemLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

				cell.setColspan(4);
				cell.setFixedHeight(20);
				certificate.addToTable(uchemLeft, cell, "Complete Urinalysis", certificate.getFontDocValue(),
						Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setColspan(1);
				certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(uchemLeft, cell, "Color", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				if (urine.getColor() != null) {
					certificate.addToTable(uchemLeft, cellBorder, appUtility.getMacroColor(urine.getColor()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(uchemLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(uchemLeft, cell, "Transparency", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				if (urine.getTransparency() != null) {
					certificate.addToTable(uchemLeft, cellBorder,
							appUtility.getMacroTransparency(urine.getTransparency()), certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(uchemLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				cell.setColspan(4);
				cell.setFixedHeight(20);
				certificate.addToTable(uchemLeft, cell, "Microscopic", certificate.getFontDocValue(),
						Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setColspan(1);
				certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(uchemLeft, cell, "RBC", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				if (urine.getRBC() != null) {
					certificate.addToTable(uchemLeft, cellBorder, urine.getRBC(), certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "/hpf 0~3", certificate.getFontDocNormal(),
							Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(uchemLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(uchemLeft, cell, "WBC", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				if (urine.getWBC() != null) {
					certificate.addToTable(uchemLeft, cellBorder, urine.getWBC(), certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "/hpf 0~5", certificate.getFontDocNormal(),
							Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(uchemLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.geteCells() != null) {
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemLeft, cell, "E.Cells", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemLeft, cellBorder, appUtility.getMicroOptions(urine.geteCells()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getmThreads() != null) {
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemLeft, cell, "M.Threads", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemLeft, cellBorder, appUtility.getMicroOptions(urine.getmThreads()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getAmorphous() != null) {
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemLeft, cell, "Amorphous", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemLeft, cellBorder, appUtility.getMicroOptions(urine.getAmorphous()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getCaOX() != null) {
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemLeft, cell, "CaOX", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemLeft, cellBorder, appUtility.getMicroOptions(urine.getCaOX()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getBacteria() != null) {
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemLeft, cell, "Bacteria", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemLeft, cellBorder, appUtility.getMicroOptions(urine.getBacteria()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine != null && urine.getOtherNotes() != null && !"".equals(urine.getOtherNotes().trim())) {
					certificate.addToTable(uchemLeft, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemLeft, cell, "Other Notes:", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					cellBorder.setColspan(2);
					certificate.addToTable(uchemLeft, cellBorder, urine.getOtherNotes(), certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
					cellBorder.setColspan(1);
				}

				tableReport.addCell(uchemLeft);

				PdfPTable uchemRight = new PdfPTable(new float[] { 1, 4, 4, 4 });
				uchemLeft.setWidthPercentage(100f);
				uchemLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

				cell.setColspan(4);
				cell.setFixedHeight(20);
				certificate.addToTable(uchemRight, cell, "Urine Chemical", certificate.getFontDocValue(),
						Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setColspan(1);
				certificate.addToTable(uchemRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(uchemRight, cell, "pH", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				if (urine.getPh() != null) {
					certificate.addToTable(uchemRight, cellBorder, String.valueOf(urine.getPh()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(uchemRight, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				certificate.addToTable(uchemRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(uchemRight, cell, "Specific Gravity", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				if (urine.getSpGravity() != null) {
					certificate.addToTable(uchemRight, cellBorder,
							appUtility.floatFormat(urine.getSpGravity(), "0.000"), certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(uchemRight, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getProtien() != null) {
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cell, "Protein", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cellBorder, appUtility.getUrineChemOptions(urine.getProtien()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getGlucose() != null) {
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cell, "Glucose", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cellBorder, appUtility.getUrineChemOptions(urine.getGlucose()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getLeukocyteEsterase() != null) {
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cell, "Leukocyte Esterase", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cellBorder,
							appUtility.getUrineChemOptions(urine.getLeukocyteEsterase()), certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getNitrite() != null) {
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cell, "Nitrite", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cellBorder, appUtility.getPositiveNegative(urine.getNitrite()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getUrobilinogen() != null) {
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cell, "Urobilinogen", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cellBorder,
							appUtility.getUrineChemOptions(urine.getUrobilinogen()), certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getBlood() != null) {
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cell, "Blood", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cellBorder, appUtility.getUrineChemOptions(urine.getBlood()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getKetone() != null) {
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cell, "Ketone", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cellBorder, appUtility.getUrineChemOptions(urine.getKetone()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (urine.getBilirubin() != null) {
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cell, "Bilirubin", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(uchemRight, cellBorder, appUtility.getUrineChemOptions(urine.getBilirubin()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(uchemRight, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				cell.setColspan(4);
				certificate.addToTable(uchemRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

				tableReport.addCell(uchemRight);
			}
		}

		// FECALYSIS
		QisLaboratoryProcedureService fecalysis = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("FECA")).findAny().orElse(null);
		if (fecalysis != null) {
			QisTransactionLabFecalysis feca = qisClinicalMicroscopy.getFecalysis();
			if (feca != null) {
				PdfPTable fecaTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
				fecaTable.setWidthPercentage(100f);
				fecaTable.setHorizontalAlignment(Element.ALIGN_LEFT);

				cell.setColspan(4);
				cell.setFixedHeight(20);
				certificate.addToTable(fecaTable, cell, "Routine Fecalysis", certificate.getFontDocValue(),
						Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setColspan(1);
				certificate.addToTable(fecaTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(fecaTable, cell, "Color", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				if (feca.getColor() != null) {
					certificate.addToTable(fecaTable, cellBorder, appUtility.getFecalysisColor(feca.getColor()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(fecaTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(fecaTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
					certificate.addToTable(fecaTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				certificate.addToTable(fecaTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(fecaTable, cell, "Consistency", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				if (feca.getConsistency() != null) {
					certificate.addToTable(fecaTable, cellBorder,
							appUtility.getFecalysisConsistency(feca.getConsistency()), certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
					certificate.addToTable(fecaTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(fecaTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
					certificate.addToTable(fecaTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				cellBorder.setColspan(2);
				certificate.addToTable(fecaTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(fecaTable, cell, "Microscopic Findings", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(fecaTable, cellBorder, feca.getMicroscopicFindings(),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);

				certificate.addToTable(fecaTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(fecaTable, cell, "Other Notes", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				certificate.addToTable(fecaTable, cellBorder, feca.getOtherNotes(), certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
				cellBorder.setColspan(1);

				cell.setColspan(4);
				certificate.addToTable(fecaTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

				tableReport.addCell(fecaTable);
			}
		}

		// PREGNANCY TEST
		QisLaboratoryProcedureService pregnancyTest = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("PREGT")).findAny().orElse(null);

		// OCCULT BLOOD TEST
		QisLaboratoryProcedureService occultBloodTest = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("OBT")).findAny().orElse(null);

		if (pregnancyTest != null || occultBloodTest != null) {
			PdfPTable ptobtTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
			ptobtTable.setWidthPercentage(100f);
			ptobtTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			if (pregnancyTest != null) {
				cell.setColspan(4);
				cell.setFixedHeight(20);
				certificate.addToTable(ptobtTable, cell, "Pregnancy Test", certificate.getFontDocValue(),
						Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setColspan(1);
				certificate.addToTable(ptobtTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(ptobtTable, cell, "Result", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				if (qisClinicalMicroscopy.getPtobt() != null
						&& qisClinicalMicroscopy.getPtobt().getPregnancyTest() != null) {
					if (isMale) {
						certificate.addToTable(ptobtTable, cellBorder, "N/A", certificate.getFontDocValue(),
								Element.ALIGN_CENTER);
					} else {
						certificate.addToTable(ptobtTable, cellBorder,
								appUtility.getPositiveNegative(qisClinicalMicroscopy.getPtobt().getPregnancyTest()),
								certificate.getFontDocValue(), Element.ALIGN_CENTER);
					}
					certificate.addToTable(ptobtTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				} else {
					if (isMale) {
						certificate.addToTable(ptobtTable, cell, " N/A", certificate.getFontDocValue(),
								Element.ALIGN_CENTER);
					} else {
						certificate.addToTable(ptobtTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
								Element.ALIGN_CENTER);
					}
					certificate.addToTable(ptobtTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				cell.setColspan(4);
				certificate.addToTable(ptobtTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);

			}

			if (occultBloodTest != null) {
				cell.setColspan(4);
				cell.setFixedHeight(20);
				certificate.addToTable(ptobtTable, cell, "Occult Blood Test", certificate.getFontDocValue(),
						Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setColspan(1);
				certificate.addToTable(ptobtTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(ptobtTable, cell, "Result", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				if (qisClinicalMicroscopy.getPtobt() != null
						&& qisClinicalMicroscopy.getPtobt().getOccultBloodTest() != null) {
					certificate.addToTable(ptobtTable, cellBorder,
							appUtility.getPositiveNegative(qisClinicalMicroscopy.getPtobt().getOccultBloodTest()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(ptobtTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(ptobtTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
					certificate.addToTable(ptobtTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				cell.setColspan(4);
				certificate.addToTable(ptobtTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			}

			tableReport.addCell(ptobtTable);
		}

		if (pregnancyTest != null && occultBloodTest != null) {
			size--;
		}

		// ACID-FAST BACILLI
		QisLaboratoryProcedureService acidFastBacilli = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("AFB")).findAny().orElse(null);
		if (acidFastBacilli != null) {
			QisTransactionLabAFB afb = qisClinicalMicroscopy.getAfb();
			if (afb != null) {
				PdfPTable afbTable = new PdfPTable(new float[] { 1, 4, 4, 4 });
				afbTable.setWidthPercentage(100f);
				afbTable.setHorizontalAlignment(Element.ALIGN_LEFT);

				cell.setColspan(4);
				cell.setFixedHeight(20);
				certificate.addToTable(afbTable, cell, "Acid - Fast Bacilli ( AFB )", certificate.getFontDocValue(),
						Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				certificate.addToTable(afbTable, cell, "Visual Appearance:", certificate.getFontDocValue(),
						Element.ALIGN_LEFT);

				cell.setColspan(1);
				cellBorder.setColspan(2);
				certificate.addToTable(afbTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(afbTable, cell, "Specimen 1", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				if (afb.getVisualAppearance1() != null) {
					certificate.addToTable(afbTable, cellBorder, afb.getVisualAppearance1(),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
				} else {
					certificate.addToTable(afbTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
					certificate.addToTable(afbTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (afb.getVisualAppearance2() != null) {
					certificate.addToTable(afbTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(afbTable, cell, "Specimen 2", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(afbTable, cellBorder, afb.getVisualAppearance2(),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
				}

				cell.setColspan(4);
				certificate.addToTable(afbTable, cell, "Reading:", certificate.getFontDocValue(), Element.ALIGN_LEFT);

				cell.setColspan(1);
				cellBorder.setColspan(2);
				certificate.addToTable(afbTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(afbTable, cell, "Specimen 1", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				if (afb.getReading1() != null) {
					certificate.addToTable(afbTable, cellBorder, afb.getReading1(), certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
				} else {
					certificate.addToTable(afbTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
					certificate.addToTable(afbTable, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				}

				if (afb.getReading2() != null) {
					certificate.addToTable(afbTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(afbTable, cell, "Specimen 2", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(afbTable, cellBorder, afb.getReading2(), certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
				}

				cell.setColspan(2);
				cellBorder.setColspan(2);
				certificate.addToTable(afbTable, cell, "Diagnosis:", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(afbTable, cellBorder, afb.getDiagnosis(), certificate.getFontDocValue(),
						Element.ALIGN_LEFT);
				cellBorder.setColspan(1);

				cell.setColspan(4);
				certificate.addToTable(afbTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				tableReport.addCell(afbTable);
			}
		}

		if (size % 2 == 1)

		{ // odd columns
			tableReport.addCell("");
		}

		// OTHER NOTES
		if (qisClinicalMicroscopy != null && qisClinicalMicroscopy.getOtherNotes() != null && !"".equals(qisClinicalMicroscopy.getOtherNotes().trim())) {
			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Other Notes:", certificate.getFontDocValue(),
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			certificate.addToTable(tableReport, cell, qisClinicalMicroscopy.getOtherNotes(),
					certificate.getFontDocNormal(), Element.ALIGN_LEFT);
		}
	}

	public void formatClinicalMicroscopyClassification(QisTransactionClinicalMicroscopy qisClinicalMicroscopy,
			PdfPTable leftReport, PdfPTable rightReport, Certificate certificate, PdfPCell cell, boolean isMale) {
		QisItem itemDetails = qisClinicalMicroscopy.getItemDetails();
		Set<QisLaboratoryProcedureService> services = itemDetails.getServiceRequest();

		// PREGNANCY TEST
		QisLaboratoryProcedureService pregnancyTest = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("PREGT")).findAny().orElse(null);

		if (pregnancyTest != null) {
			cell.setBorderColor(Color.BLUE);
			cell.setColspan(1);
			certificate.addToTable(leftReport, cell, "PREGNANCY TEST", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(3);
			if (qisClinicalMicroscopy.getPtobt().getPregnancyTest() != null) {
				if (isMale) {
					certificate.addToTable(leftReport, cell, "N/A", certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
				} else {
					certificate.addToTable(leftReport, cell,
							appUtility.getPositiveNegative(qisClinicalMicroscopy.getPtobt().getPregnancyTest()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
				}
			} else {
				certificate.addToTable(leftReport, cell, "N/A", certificate.getFontDocValue(), Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);

			cell.setColspan(4);
			cell.setFixedHeight(10);
			certificate.addToTable(leftReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
		}

		// FECALYSIS
		QisLaboratoryProcedureService fecalysis = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("FECA")).findAny().orElse(null);
		if (fecalysis != null) {
			QisTransactionLabFecalysis feca = qisClinicalMicroscopy.getFecalysis();

			cell.setColspan(4);
			certificate.addToTable(leftReport, cell, "CLINICAL MICROSCOPY", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			certificate.addToTable(leftReport, cell, "Fecalysis", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(leftReport, cell, "  Color", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(3);
			if (feca != null && feca.getColor() != null) {
				certificate.addToTable(leftReport, cell, appUtility.getFecalysisColor(feca.getColor()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(leftReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);

			cell.setColspan(1);
			certificate.addToTable(leftReport, cell, "  Consistency", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(3);
			if (feca != null && feca.getConsistency() != null) {
				certificate.addToTable(leftReport, cell, appUtility.getFecalysisConsistency(feca.getConsistency()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(leftReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);

			cell.setColspan(1);
			certificate.addToTable(leftReport, cell, "  Mircroscopic Findings", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(3);
			if (feca != null && feca.getMicroscopicFindings() != null) {
				certificate.addToTable(leftReport, cell, feca.getMicroscopicFindings(), certificate.getFontDocValue(),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(leftReport, cell, "", certificate.getFontDocAlert(), Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);

			cell.setColspan(1);
			certificate.addToTable(leftReport, cell, "  Other/Notes", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(3);
			if (feca != null && feca.getOtherNotes() != null && !"".equals(feca.getOtherNotes().trim())) {
				certificate.addToTable(leftReport, cell, feca.getOtherNotes(), certificate.getFontDocValue(),
						Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(leftReport, cell, "", certificate.getFontDocAlert(), Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);

			cell.setColspan(4);
			cell.setFixedHeight(10);
			certificate.addToTable(leftReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
		}

		// URINE CHEMICAL
		QisLaboratoryProcedureService uchem = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("UCHEM")).findAny().orElse(null);
		if (uchem != null) {
			QisTransactionLabUrineChemical urine = qisClinicalMicroscopy.getUrineChemical();

			cell.setColspan(4);
			certificate.addToTable(rightReport, cell, "CLINICAL MICROSCOPY", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			certificate.addToTable(rightReport, cell, "Complete Urinalysis", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			certificate.addToTable(rightReport, cell, "Physical/Chemical", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  Color", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getColor() != null) {
				certificate.addToTable(rightReport, cell, appUtility.getMacroColor(urine.getColor()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  Transparency", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getColor() != null) {
				certificate.addToTable(rightReport, cell, appUtility.getMacroTransparency(urine.getTransparency()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  pH", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getPh() != null) {
				certificate.addToTable(rightReport, cell, String.valueOf(urine.getPh()), certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  Sp.Gravity", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getSpGravity() != null) {
				certificate.addToTable(rightReport, cell, appUtility.floatFormat(urine.getSpGravity(), "0.0000"),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  Protein", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getProtien() != null) {
				certificate.addToTable(rightReport, cell, appUtility.getUrineChemOptions(urine.getProtien()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  Glucose", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getGlucose() != null) {
				certificate.addToTable(rightReport, cell, appUtility.getUrineChemOptions(urine.getGlucose()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(10);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "Microscopic", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			certificate.addToTable(rightReport, cell, "Normal Range", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  RBC", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getRBC() != null) {
				certificate.addToTable(rightReport, cell, urine.getRBC(), certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "/ hpf", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			certificate.addToTable(rightReport, cell, "0-3", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  WBC", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getWBC() != null) {
				certificate.addToTable(rightReport, cell, urine.getWBC(), certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "/ hpf", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			certificate.addToTable(rightReport, cell, "0-5", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(4);
			cell.setFixedHeight(10);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  E.Cells", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.geteCells() != null) {
				certificate.addToTable(rightReport, cell, appUtility.getMicroOptions(urine.geteCells()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  M.Threads", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getmThreads() != null) {
				certificate.addToTable(rightReport, cell, appUtility.getMicroOptions(urine.getmThreads()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  Amorphos", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getAmorphous() != null) {
				certificate.addToTable(rightReport, cell, appUtility.getMicroOptions(urine.getAmorphous()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  Bacteria", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getBacteria() != null) {
				certificate.addToTable(rightReport, cell, appUtility.getMicroOptions(urine.getBacteria()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  CaOx", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(1);
			if (urine != null && urine.getCaOX() != null) {
				certificate.addToTable(rightReport, cell, appUtility.getMicroOptions(urine.getCaOX()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(rightReport, cell, "  Other/Notes", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(3);
			if (urine != null && urine.getOtherNotes() != null && !"".equals(urine.getOtherNotes().trim())) {
				certificate.addToTable(rightReport, cell, urine.getOtherNotes(), certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(rightReport, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
			}
			cell.setBorder(Rectangle.NO_BORDER);

			cell.setColspan(4);
			cell.setFixedHeight(10);
			certificate.addToTable(rightReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
		}
	}

	public void formatClinicalMicroscopyConsolodated(QisTransactionClinicalMicroscopy qisClinicalMicroscopy,
			PdfPTable tableReport, Certificate certificate, boolean isMale) {
		QisItem itemDetails = qisClinicalMicroscopy.getItemDetails();
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

		// URINE CHEMICAL
		QisLaboratoryProcedureService uchem = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("UCHEM")).findAny().orElse(null);
		if (uchem != null) {
			
			QisTransactionLabUrineChemical urine = qisClinicalMicroscopy.getUrineChemical();

			if (urine != null) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_RIGHT);
				cell.setFixedHeight(20);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				certificate.addToTable(tableReport, cell, "Complete Urinalysis", fontResult, Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setVerticalAlignment(Element.ALIGN_TOP);
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Color", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(2);
				if (urine.getColor() != null) {
					certificate.addToTable(tableReport, cell, appUtility.getMacroColor(urine.getColor()), fontResult,
							Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
				}
				cell.setColspan(4);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Transparency", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(2);
				if (urine.getTransparency() != null) {
					certificate.addToTable(tableReport, cell, appUtility.getMacroTransparency(urine.getTransparency()),
							fontResult, Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
				}
				cell.setColspan(4);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

				cell.setColspan(9);
				cell.setFixedHeight(20);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				certificate.addToTable(tableReport, cell, "Microscopic", fontResult, Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setVerticalAlignment(Element.ALIGN_TOP);
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   RBC", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				if (urine.getRBC() != null) {
					certificate.addToTable(tableReport, cell, urine.getRBC(), fontResult, Element.ALIGN_RIGHT);
				} else {
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
				}
				cell.setColspan(2);
				certificate.addToTable(tableReport, cell, "/hpf 0~3", fontResult, Element.ALIGN_RIGHT);
				cell.setColspan(3);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   WBC", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(1);
				if (urine.getWBC() != null) {
					certificate.addToTable(tableReport, cell, urine.getWBC(), fontResult, Element.ALIGN_RIGHT);
				} else {
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
				}
				cell.setColspan(2);
				certificate.addToTable(tableReport, cell, "/hpf 0~5", fontResult, Element.ALIGN_RIGHT);
				cell.setColspan(3);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

				if (urine.geteCells() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   E.Cells", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getMicroOptions(urine.geteCells()), fontResult,
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine.getmThreads() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   M.Threads", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getMicroOptions(urine.getmThreads()),
							fontResult, Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine.getAmorphous() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Amorphous", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getMicroOptions(urine.getAmorphous()),
							fontResult, Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine.getCaOX() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   CaOX", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getMicroOptions(urine.getCaOX()), fontResult,
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine.getBacteria() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Bacteria", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getMicroOptions(urine.getBacteria()),
							fontResult, Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				cell.setFixedHeight(20);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				certificate.addToTable(tableReport, cell, "Urine Chemical", fontResult, Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setVerticalAlignment(Element.ALIGN_TOP);
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   pH", fontTest, Element.ALIGN_LEFT);
				
				if (urine.getPh() != null) {
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, String.valueOf(urine.getPh()), fontResult,
							Element.ALIGN_RIGHT);
					cell.setColspan(5);
				} else {
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
					cell.setColspan(4);
				}
				
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Specific Gravity", fontTest, Element.ALIGN_LEFT);
				
				if (urine.getSpGravity() != null) {
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, appUtility.floatFormat(urine.getSpGravity(), "0.000"),
							fontResult, Element.ALIGN_RIGHT);
					cell.setColspan(5);
				} else {
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
					cell.setColspan(4);
				}
				
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

				if (urine.getProtien() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Protein", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getUrineChemOptions(urine.getProtien()),
							fontResult, Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine.getGlucose() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Glucose", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getUrineChemOptions(urine.getGlucose()),
							fontResult, Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine.getLeukocyteEsterase() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Leukocyte Esterase", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getUrineChemOptions(urine.getLeukocyteEsterase()), fontResult,
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine.getNitrite() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Nitrite", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getPositiveNegative(urine.getNitrite()),
							certificate.getDisplayFont(urine.getNitrite(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine.getUrobilinogen() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Urobilinogen", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getUrineChemOptions(urine.getUrobilinogen()),
							fontResult, Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine.getBlood() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Blood", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getUrineChemOptions(urine.getBlood()),
							fontResult, Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine.getKetone() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Ketone", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getUrineChemOptions(urine.getKetone()),
							fontResult, Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine.getBilirubin() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Bilirubin", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getUrineChemOptions(urine.getBilirubin()),
							fontResult, Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				if (urine != null && urine.getOtherNotes() != null && !"".equals(urine.getOtherNotes().trim())) {
					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   Remarks/Other Notes:", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(6);
					certificate.addToTable(tableReport, cell, urine.getOtherNotes(), fontTest, Element.ALIGN_LEFT);
				}
				withData = true;
			}
		}

		// FECALYSIS
		QisLaboratoryProcedureService fecalysis = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("FECA")).findAny().orElse(null);
		if (fecalysis != null) {
			QisTransactionLabFecalysis feca = qisClinicalMicroscopy.getFecalysis();
			if (feca != null) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				cell.setFixedHeight(20);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				certificate.addToTable(tableReport, cell, "Routine Fecalysis", fontResult, Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setVerticalAlignment(Element.ALIGN_TOP);
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Color", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(2);
				if (feca.getColor() != null) {
					certificate.addToTable(tableReport, cell, appUtility.getFecalysisColor(feca.getColor()), fontResult,
							Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
				}
				cell.setColspan(4);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Consistency", fontTest, Element.ALIGN_LEFT);
				cell.setRowspan(2);
				if (feca.getConsistency() != null) {
					certificate.addToTable(tableReport, cell, appUtility.getFecalysisConsistency(feca.getConsistency()),
							fontResult, Element.ALIGN_LEFT);
				} else {
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
				}
				cell.setColspan(4);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Microscopic Findings", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(6);
				certificate.addToTable(tableReport, cell, feca.getMicroscopicFindings(), fontTest, Element.ALIGN_LEFT);

				cell.setColspan(3);
				certificate.addToTable(tableReport, cell, "   Remarks/Other Notes", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(6);
				certificate.addToTable(tableReport, cell, feca.getOtherNotes(), fontTest, Element.ALIGN_LEFT);
				withData = true;
			}
		}

		// PREGNANCY TEST
		QisLaboratoryProcedureService pregnancyTest = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("PREGT")).findAny().orElse(null);

		// OCCULT BLOOD TEST
		QisLaboratoryProcedureService occultBloodTest = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("OBT")).findAny().orElse(null);

		if (pregnancyTest != null || occultBloodTest != null) {
			
			if (pregnancyTest != null) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				cell.setFixedHeight(20);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "Pregnancy Test", fontResult, Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setColspan(2);
				if (qisClinicalMicroscopy.getPtobt() != null
						&& qisClinicalMicroscopy.getPtobt().getPregnancyTest() != null) {
					if (isMale) {
						certificate.addToTable(tableReport, cell, "N/A", fontResult, Element.ALIGN_CENTER);
					} else {
						certificate.addToTable(tableReport, cell,
								appUtility.getPositiveNegative(qisClinicalMicroscopy.getPtobt().getPregnancyTest()),
								certificate.getDisplayFont(qisClinicalMicroscopy.getPtobt().getPregnancyTest(),
										fontResult, fontResultRed),
								Element.ALIGN_LEFT);
					}
				} else {
					if (isMale) {
						certificate.addToTable(tableReport, cell, " N/A", fontResult, Element.ALIGN_CENTER);
					} else {
						certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
					}
				}

				cell.setColspan(4);
				certificate.addToTable(tableReport, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withData = true;
			}

			if (occultBloodTest != null) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				cell.setFixedHeight(20);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "Occult Blood Test", fontResult, Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				cell.setColspan(2);
				if (qisClinicalMicroscopy.getPtobt() != null
						&& qisClinicalMicroscopy.getPtobt().getOccultBloodTest() != null) {
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisClinicalMicroscopy.getPtobt().getOccultBloodTest()),
							certificate.getDisplayFont(qisClinicalMicroscopy.getPtobt().getOccultBloodTest(),
									fontResult, fontResultRed),
							Element.ALIGN_CENTER);
				} else {
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
				}

				cell.setColspan(4);
				certificate.addToTable(tableReport, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
				withData = true;
			}
		}

		// ACID-FAST BACILLI
		QisLaboratoryProcedureService acidFastBacilli = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("AFB")).findAny().orElse(null);
		if (acidFastBacilli != null) {
			QisTransactionLabAFB afb = qisClinicalMicroscopy.getAfb();
			if (afb != null) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				cell.setFixedHeight(20);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				certificate.addToTable(tableReport, cell, "Acid - Fast Bacilli ( AFB )", fontResult,
						Element.ALIGN_LEFT);
				cell.setFixedHeight(0);

				
				certificate.addToTable(tableReport, cell, "Visual Appearance:", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Specimen 1", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(2);
				if (afb.getVisualAppearance1() != null) {
					certificate.addToTable(tableReport, cell, afb.getVisualAppearance1(), fontResult,
							Element.ALIGN_CENTER);
				} else {
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
				}
				cell.setColspan(4);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

				if (afb.getVisualAppearance2() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Specimen 2", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, afb.getVisualAppearance2(), fontResult,
							Element.ALIGN_CENTER);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, "Reading:", fontTest, Element.ALIGN_LEFT);

				cell.setColspan(3);
				cell.setRowspan(2);
				certificate.addToTable(tableReport, cell, "   Specimen 1", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(2);
				if (afb.getReading1() != null) {
					certificate.addToTable(tableReport, cell, afb.getReading1(), fontResult, Element.ALIGN_CENTER);
				} else {
					certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_CENTER);
				}
				cell.setColspan(4);
				certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

				if (afb.getReading2() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Specimen 2", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, afb.getReading2(), fontResult, Element.ALIGN_CENTER);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
				}

				cell.setColspan(3);
				certificate.addToTable(tableReport, cell, "Diagnosis:", fontTest, Element.ALIGN_LEFT);
				cell.setColspan(6);
				certificate.addToTable(tableReport, cell, afb.getDiagnosis(), fontTest, Element.ALIGN_LEFT);
				withData = true;
			}
		}

		// OTHER NOTES
		if (qisClinicalMicroscopy != null && qisClinicalMicroscopy.getOtherNotes() != null && !"".equals(qisClinicalMicroscopy.getOtherNotes().trim())) {
			cell.setColspan(9);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Other Notes:", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			certificate.addToTable(tableReport, cell, qisClinicalMicroscopy.getOtherNotes(),
					certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			withData = true;
		}

		if (withData) {
			cell.setColspan(9);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
		}
	}
}
