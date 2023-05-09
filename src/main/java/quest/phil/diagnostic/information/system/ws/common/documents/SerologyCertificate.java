package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabAntigen;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabHIV;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabSerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabThyroid;

public class SerologyCertificate {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private String NO_RESULT = "- NO RESULT -";
	public static final String DATEFORMATTRANSACTION = "MMM dd, yyyy";

	public SerologyCertificate(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getTransactionLaboratorySerologyCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionSerology qisSerology, boolean withHeaderFooter)
			throws DocumentException, IOException {

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisSerology.getLabPersonel(), qisSerology.getQualityControl(),
				qisSerology.getMedicalDoctor(), "Pathologist", qisSerology.getVerifiedDate(), withHeaderFooter, false,
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
		certificate.addToTable(tableReport, cell, "SEROLOGY", certificate.getFontValue(), Element.ALIGN_LEFT);

		formatSerologyCertificate(qisSerology, tableReport, certificate, cell, cellBorder);

		document.add(tableReport);

		document.close();
		return document;
	}

	public void formatSerologyCertificate(QisTransactionSerology qisSerology, PdfPTable tableReport,
			Certificate certificate, PdfPCell cell, PdfPCell cellBorder) {
		QisItem itemDetails = qisSerology.getItemDetails();
		Set<QisLaboratoryProcedureService> services = itemDetails.getServiceRequest();

		// SEROLOGY
		QisLaboratoryProcedureService serology = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("SER")).findAny().orElse(null);
		if (serology != null) {
			QisTransactionLabSerology sero = qisSerology.getSerology();

			int item = 0;
			PdfPTable seroLeft = new PdfPTable(new float[] { 1, 4, 4, 4 });
			seroLeft.setWidthPercentage(100f);
			seroLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable seroRight = new PdfPTable(new float[] { 1, 4, 4, 4 });
			seroRight.setWidthPercentage(100f);
			seroRight.setHorizontalAlignment(Element.ALIGN_LEFT);

			boolean withResult = false;
			if (sero != null && sero.getHbsAg() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = seroLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = seroRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "HBsAg", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, appUtility.getReactiveNonReactive(sero.getHbsAg()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}
			if (sero != null && sero.getAntiHav() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = seroLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = seroRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Anti-HAV", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, appUtility.getReactiveNonReactive(sero.getAntiHav()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (sero != null && sero.getAntiHbs() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = seroLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = seroRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Anti-HBS", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, appUtility.getReactiveNonReactive(sero.getAntiHbs()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (sero != null && sero.getAntiHbc() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = seroLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = seroRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Anti-Hbc", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, appUtility.getReactiveNonReactive(sero.getAntiHbc()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (sero != null && sero.getAntiHbe() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = seroLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = seroRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Anti-Hbe", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, appUtility.getReactiveNonReactive(sero.getAntiHbe()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (sero != null && sero.getHbeAg() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = seroLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = seroRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "HBeAG", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, appUtility.getReactiveNonReactive(sero.getHbeAg()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (sero != null && sero.getVdrlRpr() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = seroLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = seroRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "VDRL/RPR", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, appUtility.getReactiveNonReactive(sero.getVdrlRpr()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (sero != null && sero.getTppa() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = seroLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = seroRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "TPPA", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, appUtility.getPositiveNegative(sero.getTppa()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (sero != null && sero.getPregnancyTest() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = seroLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = seroRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Pregnancy Test", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder,
							appUtility.getPositiveNegative(sero.getPregnancyTest()), certificate.getFontDocValue(),
							Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(seroLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(seroRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			} else {
				if (item % 2 == 1) {
					cell.setColspan(4);
					certificate.addToTable(seroRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				}
			}

			tableReport.addCell(seroLeft);
			tableReport.addCell(seroRight);
		}

		// THYROID
		QisLaboratoryProcedureService thyroid = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("THYR")).findAny().orElse(null);
		if (thyroid != null) {
			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Thyroid", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			QisTransactionLabThyroid thyr = qisSerology.getThyroid();

			int item = 0;
			PdfPTable thyrLeft = new PdfPTable(new float[] { 1, 4, 4, 4 });
			thyrLeft.setWidthPercentage(100f);
			thyrLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable thyrRight = new PdfPTable(new float[] { 1, 4, 4, 4 });
			thyrRight.setWidthPercentage(100f);
			thyrRight.setHorizontalAlignment(Element.ALIGN_LEFT);

			boolean withResult = false;
			if (thyr != null && thyr.getTsh() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = thyrLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = thyrRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "TSH", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(thyr.getTsh()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "mIU/L  0.3 - 4.2", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (thyr != null && thyr.getFt3() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = thyrLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = thyrRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "FT3", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(thyr.getFt3()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "pmol/L 2.8 - 7.1", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (thyr != null && thyr.getFt4() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = thyrLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = thyrRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "FT4", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(thyr.getFt4()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "pmol/L  12 - 22", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (thyr != null && thyr.getT3() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = thyrLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = thyrRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "T3", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(thyr.getT3()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "nmol/L  1.23 - 3.07", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (thyr != null && thyr.getT4() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = thyrLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = thyrRight;
				}
				if (cbcTable != null) {
					cell.setColspan(1);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "T4", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(thyr.getT4()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "ug/dL  66 - 181", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(thyrLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(thyrRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			} else {
				if (item % 2 == 1) {
					cell.setColspan(4);
					certificate.addToTable(thyrRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				}
			}

			tableReport.addCell(thyrLeft);
			tableReport.addCell(thyrRight);
		}

		int size = 0;
		// TYPHIDOT
		QisLaboratoryProcedureService typhidot = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("TYPH")).findAny().orElse(null);
		if (typhidot != null) {
			size++;
			int item = 0;
			PdfPTable typhidotLeft = new PdfPTable(new float[] { 1, 4, 4, 4 });
			typhidotLeft.setWidthPercentage(100f);
			typhidotLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable typhidotRight = new PdfPTable(new float[] { 1, 4, 4, 4 });
			typhidotRight.setWidthPercentage(100f);
			typhidotRight.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Typhidot", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			boolean withResult = false;
			cell.setColspan(1);
			if (qisSerology.getTyphidot() != null && qisSerology.getTyphidot().getIgm() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = typhidotLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = typhidotRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "IgM", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder,
							appUtility.getPositiveNegative(qisSerology.getTyphidot().getIgm()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (qisSerology.getTyphidot() != null && qisSerology.getTyphidot().getIgg() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = typhidotLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = typhidotRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "IgG", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder,
							appUtility.getPositiveNegative(qisSerology.getTyphidot().getIgg()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(typhidotLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(typhidotRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			} else {
				if (item % 2 == 1) {
					cell.setColspan(4);
					certificate.addToTable(typhidotRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				}
			}

			tableReport.addCell(typhidotLeft);
			tableReport.addCell(typhidotRight);
		}

		// C-REACTIVE PROTEIN
		QisLaboratoryProcedureService cProtein = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("CRP")).findAny().orElse(null);
		if (cProtein != null) {
			int item = 0;
			PdfPTable cProteinLeft = new PdfPTable(new float[] { 1, 4, 4, 4 });
			cProteinLeft.setWidthPercentage(100f);
			cProteinLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable cProteinRight = new PdfPTable(new float[] { 1, 4, 4, 4 });
			cProteinRight.setWidthPercentage(100f);
			cProteinRight.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "C-Reactive Protein", certificate.getFontDocValue(),
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			boolean withResult = false;
			cell.setColspan(1);
			if (qisSerology.getCrp() != null && qisSerology.getCrp().getDilution() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cProteinLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cProteinRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Dilution", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(qisSerology.getCrp().getDilution()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "uIU/mL  0.4 - 5.0", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (qisSerology.getCrp() != null && qisSerology.getCrp().getResult() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = cProteinLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = cProteinRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Result", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(qisSerology.getCrp().getResult()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "mg/L  < 6", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(cProteinLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(cProteinRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			} else {
				if (item % 2 == 1) {
					cell.setColspan(4);
					certificate.addToTable(cProteinRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				}
			}

			tableReport.addCell(cProteinLeft);
			tableReport.addCell(cProteinLeft);
		}

		// HUMAN IMMUNODEFICIENCY VIRUSES
		QisLaboratoryProcedureService hiv = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("HIV")).findAny().orElse(null);
		if (hiv != null) {
			int item = 0;
			PdfPTable hivLeft = new PdfPTable(new float[] { 1, 4, 4, 4 });
			hivLeft.setWidthPercentage(100f);
			hivLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable hivRight = new PdfPTable(new float[] { 1, 4, 4, 4 });
			hivRight.setWidthPercentage(100f);
			hivRight.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Human Immunodeficiency Viruses ( HIV )",
					certificate.getFontDocValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			boolean withResult = false;
			cell.setColspan(1);
			if (qisSerology.getHiv() != null && qisSerology.getHiv().getTest1() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = hivLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = hivRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Test1", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder,
							appUtility.getReactiveNonReactive(qisSerology.getHiv().getTest1()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (qisSerology.getHiv() != null && qisSerology.getHiv().getTest2() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = hivLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = hivRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Test2", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder,
							appUtility.getReactiveNonReactive(qisSerology.getHiv().getTest2()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(hivLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(hivRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			} else {
				if (item % 2 == 1) {
					cell.setColspan(4);
					certificate.addToTable(hivRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				}
			}
			tableReport.addCell(hivLeft);
			tableReport.addCell(hivRight);
		}

		// ANTIGEN
		QisLaboratoryProcedureService antigen = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("AGEN")).findAny().orElse(null);

		if (antigen != null) {
			size++;
			int item = 0;
			PdfPTable agenLeft = new PdfPTable(new float[] { 1, 4, 4, 4 });
			agenLeft.setWidthPercentage(100f);
			agenLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPTable agenRight = new PdfPTable(new float[] { 1, 4, 4, 4 });
			agenRight.setWidthPercentage(100f);
			agenRight.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Tumor Markers", certificate.getFontDocValue(),
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			QisTransactionLabAntigen agen = qisSerology.getAntigen();

			boolean withResult = false;
			cell.setColspan(1);
			if (agen != null && agen.getPsa() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = agenLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = agenRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Prostate Specific Antigen (PSA)",
							certificate.getFontDocLabel(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(agen.getPsa()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "ng/mL  0-4", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (agen != null && agen.getCea() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = agenLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = agenRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Carcenoembryonic Antigen", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(agen.getCea()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "ng/mL  0 - 5", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (agen != null && agen.getAfp() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = agenLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = agenRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Alpha-Fetoprotein", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(agen.getAfp()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "IU/mL  0.5-5.5", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}
			if (agen != null && agen.getCa125() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = agenLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = agenRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Cancer Antigen 125", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(agen.getCa125()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "U/mL  0 - 35", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (agen != null && agen.getCa199() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = agenLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = agenRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Cancer Antigen 19-9", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(agen.getCa199()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "U/mL  0 - 39", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (agen != null && agen.getCa153() != null) {
				PdfPTable cbcTable = null;
				if (item % 2 == 0) { // LEFT
					cbcTable = agenLeft;
				} else if (item % 2 == 1) { // RIGHT
					cbcTable = agenRight;
				}
				if (cbcTable != null) {
					certificate.addToTable(cbcTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cell, "Cancer Antigen 15-3", certificate.getFontDocLabel(),
							Element.ALIGN_LEFT);
					certificate.addToTable(cbcTable, cellBorder, String.valueOf(agen.getCa153()),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
					certificate.addToTable(cbcTable, cell, "U/mL  0 - 25", certificate.getFontNotesNormal(),
							Element.ALIGN_LEFT);
				}
				item++;
				withResult = true;
			}

			if (!withResult) {
				cell.setColspan(4);
				certificate.addToTable(agenLeft, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
						Element.ALIGN_LEFT);
				certificate.addToTable(agenRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
			} else {
				if (item % 2 == 1) {
					cell.setColspan(4);
					certificate.addToTable(agenRight, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				}
			}

			tableReport.addCell(agenLeft);
			tableReport.addCell(agenRight);
		}

		// COVID
//		QisLaboratoryProcedureService covid = services.stream()
//				.filter(s -> s.getLaboratoryRequest().toString().equals("COVID")).findAny().orElse(null);
//		if (covid != null) {
//			size++;
//			PdfPTable covTable = new PdfPTable(2);
//			covTable.setWidthPercentage(90f);
//			covTable.setHorizontalAlignment(Element.ALIGN_LEFT);
//			covTable.setSpacingBefore(50);
//			covTable.getDefaultCell().setBorder(0);
//			covTable.getDefaultCell().setPadding(0);
//
//			cell.setColspan(1);
//			cell.setFixedHeight(30);
//			certificate.addToTable(covTable, cell, "TEST_PERFORMED:", certificate.getSubLabel(), Element.ALIGN_LEFT);
//			cell.setColspan(1);
//			certificate.addToTable(covTable, cell, "       RESULT", certificate.getSubLabel(), Element.ALIGN_LEFT);
//			cell.setFixedHeight(30);
//
//			boolean withResult = false;
//			
//			if (qisSerology.getCovid() != null && qisSerology.getCovid().getCovigg() != null) {
//				cell.setColspan(1);
//				cell.setFixedHeight(30);
//				certificate.addToTable(covTable, cell, "SARS-Cov2 Ag", certificate.getLabel(),
//						Element.ALIGN_LEFT);
//				cell.setFixedHeight(30);
//				cell.setColspan(1);
//				certificate.addToTable(covTable, cellBorder,
//						appUtility.getReactiveNonReactive(qisSerology.getCovid().getCovigg()),
//						certificate.getSubResult(), Element.ALIGN_CENTER);
//				cell.setFixedHeight(30);
//				withResult = true;
//			}
//			
//
////			if (qisSerology.getCovid() != null && qisSerology.getCovid().getCovigm() != null) {
////				certificate.addToTable(covTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
////				certificate.addToTable(covTable, cell, "SARS-Cov2-IgM", certificate.getFontDocLabel(),
////						Element.ALIGN_LEFT);
////				certificate.addToTable(covTable, cellBorder,
////						appUtility.getReactiveNonReactive(qisSerology.getCovid().getCovigm()),
////						certificate.getFontDocValue(), Element.ALIGN_CENTER);
////				certificate.addToTable(covTable, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
////				withResult = true;
////			}
//
//			if (!withResult) {
//				cell.setColspan(2);
//				certificate.addToTable(covTable, cell, " - NO RESULT - ", certificate.getFontDocAlert(),
//						Element.ALIGN_LEFT);
//			}
//
//			cell.setColspan(4);
//			certificate.addToTable(covTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
//
//			tableReport.addCell(covTable);
//		}
//
//		if (size % 2 == 1) { // odd columns
//			tableReport.addCell("");
//		}

		// OTHER NOTES
		if (qisSerology.getOtherNotes() != null && !"".equals(qisSerology.getOtherNotes().trim())) {
			cell.setColspan(2);
			cell.setFixedHeight(20);
			certificate.addToTable(tableReport, cell, "Remarks / Impressions:", certificate.getFontDocValue(),
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			certificate.addToTable(tableReport, cell, qisSerology.getOtherNotes(), certificate.getFontDocNormal(),
					Element.ALIGN_LEFT);
		}
	}

	public void formatSerologyClassification(QisTransactionSerology qisSerology, PdfPTable tableReport,
			Certificate certificate, PdfPCell cell) {
		QisItem itemDetails = qisSerology.getItemDetails();
		Set<QisLaboratoryProcedureService> services = itemDetails.getServiceRequest();

		QisLaboratoryProcedureService hiv = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("SER")).findAny().orElse(null);
		QisTransactionLabHIV hiv11 = qisSerology.getHiv();
		if (hiv != null) {
			System.out.println(hiv11);
		}
		Boolean test1 = null;
		Boolean test2 = null;
		if (hiv11 != null) {
			test1 = hiv11.getTest1();
			test2 = hiv11.getTest2();
		}

		// SEROLOGY
		QisLaboratoryProcedureService serology = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("SER")).findAny().orElse(null);
		if (serology != null || hiv != null) {
			QisTransactionLabSerology sero = qisSerology.getSerology();
			cell.setBorderColor(Color.BLUE);
			cell.setColspan(4);
			certificate.addToTable(tableReport, cell, "SEROLOGY", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

			cell.setColspan(1);
			certificate.addToTable(tableReport, cell, "  HBsAg", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(3);
			if (sero != null && sero.getHbsAg() != null) {
				certificate.addToTable(tableReport, cell, appUtility.getReactiveNonReactive(sero.getHbsAg()),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(tableReport, cell, "NO RESULT", certificate.getFontDocAlert(),
						Element.ALIGN_CENTER);
			}

			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(1);
			certificate.addToTable(tableReport, cell, "  Other/Notes", certificate.getFontDocLabel(),
					Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setColspan(3);
			if (qisSerology.getOtherNotes() != null && !"".equals(qisSerology.getOtherNotes().trim())) {
				certificate.addToTable(tableReport, cell, qisSerology.getOtherNotes(), certificate.getFontDocValue(),
						Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(tableReport, cell, "", certificate.getFontDocAlert(), Element.ALIGN_CENTER);
			}

			if (test1 != null || test2 != null) {
				cell.setColspan(4);
				cell.setBorder(Rectangle.NO_BORDER);
				certificate.addToTable(tableReport, cell, "Human Immunodeficiency Viruses ( HIV )",
						certificate.getFontDocLabel(), Element.ALIGN_LEFT);

				cell.setColspan(1);
				cell.setBorder(Rectangle.NO_BORDER);
				certificate.addToTable(tableReport, cell, "  Test1", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOTTOM);
				cell.setColspan(3);
				if (test1 != null) {
					certificate.addToTable(tableReport, cell, appUtility.getReactiveNonReactive(test1),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
				} else {
					certificate.addToTable(tableReport, cell, "NO RESULT", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
				}
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, "  Test2", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOTTOM);
				cell.setColspan(3);
				if (test2 != null) {
					certificate.addToTable(tableReport, cell, appUtility.getReactiveNonReactive(test2),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
				} else {
					certificate.addToTable(tableReport, cell, "NO RESULT", certificate.getFontDocAlert(),
							Element.ALIGN_CENTER);
				}
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setColspan(1);
				certificate.addToTable(tableReport, cell, "  Other/Notes", certificate.getFontDocLabel(),
						Element.ALIGN_LEFT);
				cell.setBorder(Rectangle.BOTTOM);
				cell.setColspan(3);
				if (qisSerology.getOtherNotes() != null && !"".equals(qisSerology.getOtherNotes().trim())) {
					certificate.addToTable(tableReport, cell, qisSerology.getOtherNotes(),
							certificate.getFontDocValue(), Element.ALIGN_CENTER);
				} else {
					certificate.addToTable(tableReport, cell, "", certificate.getFontDocAlert(), Element.ALIGN_CENTER);
				}
			}

			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(4);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

		}
	}

	public void formatSerologyConsolidated(QisTransactionSerology qisSerology, PdfPTable tableReport,
			Certificate certificate) {
		QisItem itemDetails = qisSerology.getItemDetails();
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

		Font fontSubTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontSubTitle.setSize(10);
		fontSubTitle.setColor(Color.BLACK);

		Font fontResult = certificate.getFontResult();
		Font fontResultRed = certificate.getFontResultRed();

		Font fontUnit = FontFactory.getFont("GARAMOND");
		fontUnit.setSize(10);
		fontUnit.setColor(Color.BLACK);

		boolean withData = false;

		// SEROLOGY
		QisLaboratoryProcedureService serology = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("SER")).findAny().orElse(null);

		if (serology != null) {
			QisTransactionLabSerology sero = qisSerology.getSerology();
			boolean withResult = false;
			if (sero != null) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
				certificate.addToTable(tableReport, cell, "", fontTest, Element.ALIGN_LEFT);
				if (sero.getHbsAg() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   HBsAg", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getReactiveNonReactive(sero.getHbsAg()),
							certificate.getDisplayFont(sero.getHbsAg(), fontResult, fontResultRed), Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (sero.getAntiHav() != null) {
					cell.setColspan(9);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Anti-Hav", fontSubTitle, Element.ALIGN_LEFT);
					
					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   Patient's Abs", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					if (sero.getAbs() != null) {
						cell.setColspan(1);
						certificate.addToTable(tableReport, cell, sero.getAbs(), fontResult, Element.ALIGN_RIGHT);
						cell.setColspan(5);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					} else {
						cell.setColspan(2);
						certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
						cell.setColspan(4);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					}

					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   Cut Off Value", fontTest, Element.ALIGN_LEFT);
					if (sero.getCutOffValue() != null) {
						cell.setColspan(1);
						certificate.addToTable(tableReport, cell, sero.getCutOffValue(), fontResult,
								Element.ALIGN_RIGHT);
						cell.setColspan(5);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					} else {
						cell.setColspan(2);
						certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
						cell.setColspan(4);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					}
					
					
					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   RESULT", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getReactiveNonReactive(sero.getAntiHav()),
							certificate.getDisplayFont(sero.getAntiHav(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

				if (sero.getAntiHbs() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Anti-HBS", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getReactiveNonReactive(sero.getAntiHbs()),
							certificate.getDisplayFont(sero.getAntiHbs(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

				if (sero.getAntiHbc() != null) {
					cell.setColspan(9);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Anti-Hbc", fontSubTitle, Element.ALIGN_LEFT);
					
					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   Patient's Abs", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					if (sero.getAbs() != null) {
						cell.setColspan(1);
						certificate.addToTable(tableReport, cell, sero.getAbs(), fontResult, Element.ALIGN_RIGHT);
						cell.setColspan(5);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					} else {
						cell.setColspan(2);
						certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
						cell.setColspan(4);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					}

					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   Cut Off Value", fontTest, Element.ALIGN_LEFT);
					if (sero.getCutOffValue() != null) {
						cell.setColspan(1);
						certificate.addToTable(tableReport, cell, sero.getCutOffValue(), fontResult,
								Element.ALIGN_RIGHT);
						cell.setColspan(5);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					} else {
						cell.setColspan(2);
						certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
						cell.setColspan(4);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					}
					
					
					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   RESULT", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getReactiveNonReactive(sero.getAntiHbc()),
							certificate.getDisplayFont(sero.getAntiHbc(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}
				
				if (sero.getAntiHcv() != null) {
					cell.setColspan(9);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Anti-Hcv", fontSubTitle, Element.ALIGN_LEFT);
					
					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   Patient's Abs", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					if (sero.getAbs() != null) {
						cell.setColspan(1);
						certificate.addToTable(tableReport, cell, sero.getAbs(), fontResult, Element.ALIGN_RIGHT);
						cell.setColspan(5);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					} else {
						cell.setColspan(2);
						certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
						cell.setColspan(4);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					}

					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   Cut Off Value", fontTest, Element.ALIGN_LEFT);
					if (sero.getCutOffValue() != null) {
						cell.setColspan(1);
						certificate.addToTable(tableReport, cell, sero.getCutOffValue(), fontResult,
								Element.ALIGN_RIGHT);
						cell.setColspan(5);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					} else {
						cell.setColspan(2);
						certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
						cell.setColspan(4);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					}
					
					
					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   RESULT", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getReactiveNonReactive(sero.getAntiHcv()),
							certificate.getDisplayFont(sero.getAntiHcv(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

				if (sero.getAntiHbe() != null) {
					cell.setColspan(9);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, " Anti-Hbe", fontSubTitle, Element.ALIGN_LEFT);

					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   Patient's Abs", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					if (sero.getAbs() != null) {
						cell.setColspan(1);
						certificate.addToTable(tableReport, cell, sero.getAbs(), fontResult, Element.ALIGN_RIGHT);
						cell.setColspan(5);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					} else {
						cell.setColspan(2);
						certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
						cell.setColspan(4);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					}

					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   Cut Off Value", fontTest, Element.ALIGN_LEFT);
					if (sero.getCutOffValue() != null) {
						cell.setColspan(1);
						certificate.addToTable(tableReport, cell, sero.getCutOffValue(), fontResult,
								Element.ALIGN_RIGHT);
						cell.setColspan(5);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					} else {
						cell.setColspan(2);
						certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
						cell.setColspan(4);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					}

					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   RESULT", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getReactiveNonReactive(sero.getAntiHbe()),
							certificate.getDisplayFont(sero.getAntiHbe(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);

					withResult = true;
				}

				if (sero.getHbeAg() != null) {
					cell.setColspan(9);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, " HBeAG", fontSubTitle, Element.ALIGN_LEFT);

					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   Patient's Abs", fontTest, Element.ALIGN_LEFT);

					if (sero.getAbs() != null) {
						cell.setColspan(1);
						certificate.addToTable(tableReport, cell, sero.getAbs(), fontResult, Element.ALIGN_RIGHT);
						cell.setColspan(5);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					} else {
						cell.setColspan(2);
						certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
						cell.setColspan(4);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					}

					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   Cut Off Value", fontTest, Element.ALIGN_LEFT);

					if (sero.getCutOffValue() != null) {
						cell.setColspan(1);
						certificate.addToTable(tableReport, cell, sero.getCutOffValue(), fontResult,
								Element.ALIGN_RIGHT);
						cell.setColspan(5);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					} else {
						cell.setColspan(2);
						certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
						cell.setColspan(4);
						certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					}

					cell.setColspan(3);
					certificate.addToTable(tableReport, cell, "   RESULT", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getReactiveNonReactive(sero.getHbeAg()),
							certificate.getDisplayFont(sero.getHbeAg(), fontResult, fontResultRed), Element.ALIGN_LEFT);

					withResult = true;
				}

				if (sero.getVdrlRpr() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   VDRL/RPR", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getReactiveNonReactive(sero.getVdrlRpr()),
							certificate.getDisplayFont(sero.getVdrlRpr(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

				if (sero.getTppa() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   TPPA", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getPositiveNegative(sero.getTppa()),
							certificate.getDisplayFont(sero.getTppa(), fontResult, fontResultRed), Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

				if (sero.getPregnancyTest() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Pregnancy Test", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell, appUtility.getPositiveNegative(sero.getPregnancyTest()),
							certificate.getDisplayFont(sero.getPregnancyTest(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// THYROID
		QisLaboratoryProcedureService thyroid = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("THYR")).findAny().orElse(null);
		if (thyroid != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Thyroid", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			QisTransactionLabThyroid thyr = qisSerology.getThyroid();

			boolean withResult = false;
			if (thyr != null) {
				if (thyr.getTsh() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   TSH", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(thyr.getTsh(), 0.3, 4.2),
							certificate.getDisplayFontRange(thyr.getTsh(), 0.3, 4.2, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "mIU/mL", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "0.3 - 4.2", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (thyr.getFt3() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   FT3", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(thyr.getFt3(), 2.8, 7.1),
							certificate.getDisplayFontRange(thyr.getFt3(), 2.8, 7.1, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "pmol / L", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "2.8 - 7.1", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (thyr.getFt4() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   FT4", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(thyr.getFt4(), 12.0, 22.0),
							certificate.getDisplayFontRange(thyr.getFt4(), 12.0, 22.0, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "pmol/L ", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "12 - 22", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (thyr.getT3() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   T3", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(thyr.getT3(), 1.23, 3.07),
							certificate.getDisplayFontRange(thyr.getT3(), 1.23, 3.07, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "nmol/L", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "1.23 - 3.07", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

				if (thyr.getT4() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   T4", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(thyr.getT4(), 66.0, 181.0),
							certificate.getDisplayFontRange(thyr.getT4(), 66.0, 181.0, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "nmol/L", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "66 - 181", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// TYPHIDOT
		QisLaboratoryProcedureService typhidot = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("TYPH")).findAny().orElse(null);
		if (typhidot != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Typhidot", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			boolean withResult = false;
			if (qisSerology.getTyphidot() != null) {
				if (qisSerology.getTyphidot().getIgm() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   IgM", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTyphidot().getIgm()),
							certificate.getDisplayFont(qisSerology.getTyphidot().getIgm(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getTyphidot().getIgg() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   IgG", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTyphidot().getIgg()),
							certificate.getDisplayFont(qisSerology.getTyphidot().getIgg(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// TPHA
		QisLaboratoryProcedureService tpha = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("TPHA")).findAny().orElse(null);
		if (tpha != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "TPHA with Titer", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			boolean withResult = false;
			if (qisSerology.getTphawt() != null) {
				if (qisSerology.getTphawt().getTest1() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:80", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTphawt().getTest1()),
							certificate.getDisplayFont(qisSerology.getTphawt().getTest1(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getTphawt().getTest2() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:160", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTphawt().getTest2()),
							certificate.getDisplayFont(qisSerology.getTphawt().getTest2(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getTphawt().getTest3() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:320", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTphawt().getTest3()),
							certificate.getDisplayFont(qisSerology.getTphawt().getTest3(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getTphawt().getTest4() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:640", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTphawt().getTest4()),
							certificate.getDisplayFont(qisSerology.getTphawt().getTest4(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getTphawt().getTest5() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:1280", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTphawt().getTest5()),
							certificate.getDisplayFont(qisSerology.getTphawt().getTest5(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getTphawt().getTest6() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:2560", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTphawt().getTest6()),
							certificate.getDisplayFont(qisSerology.getTphawt().getTest6(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getTphawt().getTest7() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:5120", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTphawt().getTest7()),
							certificate.getDisplayFont(qisSerology.getTphawt().getTest7(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getTphawt().getTest8() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:10240", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTphawt().getTest8()),
							certificate.getDisplayFont(qisSerology.getTphawt().getTest8(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getTphawt().getTest9() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, " 1:20480", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTphawt().getTest9()),
							certificate.getDisplayFont(qisSerology.getTphawt().getTest9(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// ASO TITER
		QisLaboratoryProcedureService aso = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("ASO")).findAny().orElse(null);
		if (aso != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

//			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "ASO TITER", fontResult, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "		DILUTION", fontTest, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			boolean withResult = false;
			if (qisSerology.getAso() != null) {
				if (qisSerology.getAso().getResult1() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:1 (200) IU/mL", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getAso().getResult1()),
							certificate.getDisplayFont(qisSerology.getAso().getResult1(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getAso().getResult2() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:2 (400) IU/mL", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getAso().getResult2()),
							certificate.getDisplayFont(qisSerology.getAso().getResult2(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getAso().getResult3() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:4 (800) IU/mL", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getAso().getResult3()),
							certificate.getDisplayFont(qisSerology.getAso().getResult3(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getAso().getResult4() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:8 (1600) IU/mL", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getAso().getResult4()),
							certificate.getDisplayFont(qisSerology.getAso().getResult4(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getAso().getResult5() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "	1:16 (3200) IU/mL", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getAso().getResult5()),
							certificate.getDisplayFont(qisSerology.getAso().getResult5(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// RHEUMATOID FACTOR TITER
		QisLaboratoryProcedureService rft = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("RFT")).findAny().orElse(null);
		if (rft != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "RHEUMATOID FACTOR TITER", fontResult, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, " DILUTION", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			boolean withResult = false;
			if (qisSerology.getRft() != null) {
				if (qisSerology.getRft().getFirst() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, " 1:1 (8.0 IU/L)", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getRft().getFirst()),
							certificate.getDisplayFont(qisSerology.getRft().getFirst(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getRft().getSecond() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, " 1:2 (16.0 IU/L)", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getRft().getSecond()),
							certificate.getDisplayFont(qisSerology.getRft().getSecond(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getRft().getThird() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, " 1:4 (32.0 IU/L)", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getRft().getThird()),
							certificate.getDisplayFont(qisSerology.getRft().getThird(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getRft().getFourth() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, " 1:8 (64.0 IU/L)", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getRft().getFourth()),
							certificate.getDisplayFont(qisSerology.getRft().getFourth(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}
				if (qisSerology.getRft().getFifth() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, " 1:16 (128.0 IU/L)", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getRft().getFifth()),
							certificate.getDisplayFont(qisSerology.getRft().getFifth(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// C-REACTIVE PROTEIN
		QisLaboratoryProcedureService cProtein = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("CRP")).findAny().orElse(null);
		if (cProtein != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "C-Reactive Protein", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			boolean withResult = false;
			if (qisSerology.getCrp() != null) {
				if (qisSerology.getCrp().getDilution() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Dilution", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell,
							certificate.getDisplaySymbol(qisSerology.getCrp().getDilution(), 0.4, 5.0),
							certificate.getDisplayFontRange(qisSerology.getCrp().getDilution(), 0.4, 5.0, fontResult,
									fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "uIU/mL", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "0.4 - 5.0", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

				if (qisSerology.getCrp().getResult() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Result", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell,
							certificate.getDisplaySymbol(qisSerology.getCrp().getResult(), 0.0, 6.0),
							certificate.getDisplayFontRange(qisSerology.getCrp().getResult(), 0.0, 6.0, fontResult,
									fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "mg/L", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "< 6", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// HUMAN IMMUNODEFICIENCY VIRUSES
		QisLaboratoryProcedureService hiv = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("HIV")).findAny().orElse(null);
		if (hiv != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Human Immunodeficiency Viruses ( HIV )", fontResult,
					Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			boolean withResult = false;
			if (qisSerology.getHiv() != null) {
				if (qisSerology.getHiv().getTest1() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Test1", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getReactiveNonReactive(qisSerology.getHiv().getTest1()),
							certificate.getDisplayFont(qisSerology.getHiv().getTest1(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getHiv().getTest2() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Test2", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getReactiveNonReactive(qisSerology.getHiv().getTest2()),
							certificate.getDisplayFont(qisSerology.getHiv().getTest2(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// ANTIGEN
		QisLaboratoryProcedureService antigen = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("AGEN")).findAny().orElse(null);
		if (antigen != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Tumor Markers", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			QisTransactionLabAntigen agen = qisSerology.getAntigen();

			boolean withResult = false;
			if (agen != null) {
				if (agen.getPsa() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Prostate Specific Antigen (PSA)", fontTest,
							Element.ALIGN_LEFT);

					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(agen.getPsa(), 0.0, 4.0),
							certificate.getDisplayFontRange(agen.getPsa(), 0.0, 4.0, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "ng/mL", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "0 - 4", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

				if (agen.getCea() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Carcenoembryonic Antigen", fontTest,
							Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(agen.getCea(), 0.0, 5.0),
							certificate.getDisplayFontRange(agen.getCea(), 0.0, 5.0, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "ng/mL", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "0 - 5", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

				if (agen.getAfp() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Alpha-Fetoprotein", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(agen.getAfp(), 0.5, 5.5),
							certificate.getDisplayFontRange(agen.getAfp(), 0.5, 5.5, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "IU/mL", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "0.5-5.5", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

				if (agen.getCa125() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Cancer Antigen 125", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(agen.getCa125(), 0.0, 35.0),
							certificate.getDisplayFontRange(agen.getCa125(), 0.0, 35.0, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "U/mL", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "0 - 35", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);

					withResult = true;
				}

				if (agen.getCa199() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Cancer Antigen 19-9", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(agen.getCa199(), 0.0, 39.0),
							certificate.getDisplayFontRange(agen.getCa199(), 0.0, 39.0, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "U/mL", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "0 - 39", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (agen.getCa153() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   Cancer Antigen 15-3", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(1);
					certificate.addToTable(tableReport, cell, certificate.getDisplaySymbol(agen.getCa153(), 0.0, 25.0),
							certificate.getDisplayFontRange(agen.getCa153(), 0.0, 25.0, fontResult, fontResultRed),
							Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "U/mL", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "0 - 25", fontUnit, Element.ALIGN_RIGHT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// COVID
		QisLaboratoryProcedureService covid = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("COVID")).findAny().orElse(null);
		if (covid != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Covid", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			boolean withResult = false;
			if (qisSerology.getCovid() != null) {
				if (qisSerology.getCovid().getCovigg() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   SARS-Cov2-IgM", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getReactiveNonReactive(qisSerology.getCovid().getCovigg()),
							certificate.getDisplayFont(qisSerology.getCovid().getCovigg(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}

				if (qisSerology.getCovid().getCovigm() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "   SARS-Cov2-IgG", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getReactiveNonReactive(qisSerology.getCovid().getCovigm()),
							certificate.getDisplayFont(qisSerology.getCovid().getCovigm(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// DENGUE
		QisLaboratoryProcedureService dengue = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("DGE")).findAny().orElse(null);
		if (dengue != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			boolean withResult = false;
			if (qisSerology.getDengue() != null) {
				if (qisSerology.getDengue().getResult() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "DENGUE NS1", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getDengue().getResult()),
							certificate.getDisplayFont(qisSerology.getDengue().getResult(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// TROPONIN
		QisLaboratoryProcedureService tpn = services.stream()
				.filter(s -> s.getLaboratoryRequest().toString().equals("TPN")).findAny().orElse(null);
		if (tpn != null) {
			cell.setColspan(9);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			cell.setVerticalAlignment(Element.ALIGN_TOP);
			boolean withResult = false;
			if (qisSerology.getTpn() != null) {
				if (qisSerology.getTpn().getResult() != null) {
					cell.setColspan(3);
					cell.setRowspan(2);
					certificate.addToTable(tableReport, cell, "TROPONIN I (QUALITATIVE)", fontTest, Element.ALIGN_LEFT);
					cell.setColspan(2);
					certificate.addToTable(tableReport, cell,
							appUtility.getPositiveNegative(qisSerology.getTpn().getResult()),
							certificate.getDisplayFont(qisSerology.getTpn().getResult(), fontResult, fontResultRed),
							Element.ALIGN_LEFT);
					cell.setColspan(4);
					certificate.addToTable(tableReport, cell, "", fontUnit, Element.ALIGN_LEFT);
					withResult = true;
				}
			}

			if (!withResult) {
				cell.setColspan(9);
				certificate.addToTable(tableReport, cell, NO_RESULT, fontResultRed, Element.ALIGN_LEFT);
			}
			withData = true;
		}

		// OTHER NOTES
		if (qisSerology.getOtherNotes() != null && !"".equals(qisSerology.getOtherNotes().trim())) {
			cell.setColspan(9);
			cell.setFixedHeight(20);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			certificate.addToTable(tableReport, cell, "Remarks / Impressions:", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);

			certificate.addToTable(tableReport, cell, qisSerology.getOtherNotes(), fontTest, Element.ALIGN_LEFT);
			withData = true;
		}

		if (withData) {
			cell.setColspan(9);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", fontResult, Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
		}
	}

	public Document getCertificateByAntigen(Document document, PdfWriter pdfWriter, QisTransaction qisTransaction,
			QisTransactionSerology qisSerology, boolean withHeaderFooter, String LabRequest)
			throws DocumentException, IOException {

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisSerology.getLabPersonel(), qisSerology.getQualityControl(),
				qisSerology.getMedicalDoctor(), "Physician", qisSerology.getVerifiedDate(), withHeaderFooter, false,
				LabRequest, null);
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
		// RESULTS
		PdfPTable tableReport = new PdfPTable(6);
		tableReport.setSpacingBefore(20);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(0);

		cell.setColspan(6);
		cell.setFixedHeight(30);
		certificate.addToTable(tableReport, cell, "DIAGNOSTIC LABORATORY  TEST RESULT",
				certificate.getFontTitleAntigen(), Element.ALIGN_CENTER);
		cell.setFixedHeight(35);
		certificate.addToTable(tableReport, cell, "IMMUNOLOGY-SEROLOGY", certificate.getFontValueAntigen(),
				Element.ALIGN_LEFT);

		cell.setColspan(2);
		cell.setFixedHeight(30);
		certificate.addToTable(tableReport, cell, "TEST PERFORMED:", certificate.getSubLabel(), Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, "RESULT", certificate.getSubLabel(), Element.ALIGN_LEFT);
		cell.setFixedHeight(30);

		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "SARS-Cov2 Ag", certificate.getLabel(), Element.ALIGN_LEFT);
		cell.setFixedHeight(30);

		if (qisSerology.getRtantigen() != null) {
			if ("NEGATIVE".equals(appUtility.getPositiveNegative(qisSerology.getRtantigen().getCov_ag()))) {
				cell.setColspan(4);
				certificate.addToTable(tableReport, cell,
						appUtility.getPositiveNegative(qisSerology.getRtantigen().getCov_ag()),
						certificate.getSubResult(), Element.ALIGN_LEFT);
				cell.setFixedHeight(25);
			} else {
				cell.setColspan(4);
				certificate.addToTable(tableReport, cell,
						appUtility.getPositiveNegative(qisSerology.getRtantigen().getCov_ag()),
						certificate.getSubResultRed(), Element.ALIGN_LEFT);
				cell.setFixedHeight(25);
			}
		} else {
			cell.setColspan(4);
			certificate.addToTable(tableReport, cell, NO_RESULT, certificate.getSubResultRed(), Element.ALIGN_LEFT);
			cell.setFixedHeight(25);
		}

		cell.setColspan(2);
		cell.setFixedHeight(30);
		certificate.addToTable(tableReport, cell, "SPECIMEN TYPE:", certificate.getLabel13(), Element.ALIGN_LEFT);
		cell.setFixedHeight(30);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, "NASOPHARYNGEAL SWAB", certificate.getLabel13(), Element.ALIGN_LEFT);
		cell.setFixedHeight(10);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel13(), Element.ALIGN_LEFT);

		PdfPCell cell1 = new PdfPCell();
		cell1.setColspan(6);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setBorder(Rectangle.BOX);
		cell1.setBackgroundColor(new Color(184, 204, 228, 255));
		certificate.addToTable(tableReport, cell1, "RESULT INTERPRETATION", certificate.getFontSubTitle(),
				Element.ALIGN_CENTER);

		cell.setFixedHeight(15);
		cell.setColspan(6);
		cell.setBorder(Rectangle.BOX);
		certificate.addToTable(tableReport, cell, "HIGH RISK INDIVIDUAL", certificate.getLabel11Bold(),
				Element.ALIGN_LEFT);

		cell.setFixedHeight(30);
		cell.setColspan(2);
		cell.setBorder(Rectangle.BOX);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "NEGATIVE", certificate.getLabel11(), Element.ALIGN_CENTER);

		cell.setColspan(4);
		cell.setBorder(Rectangle.BOX);
		certificate.addToTable(tableReport, cell,
				"PATIENT NEEDS TO COMPLETE 14-DAY QUARANTINE AND RECOMMEND TO UNDERGO RT-PCR TESTING",
				certificate.getLabel11(), Element.ALIGN_LEFT);

		cell.setColspan(2);
		cell.setBorder(Rectangle.BOX);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "POSITIVE", certificate.getLabel11Red(), Element.ALIGN_CENTER);
		cell.setFixedHeight(15);
		cell.setColspan(4);
		cell.setBorder(Rectangle.BOX);
		certificate.addToTable(tableReport, cell, "PATIENT NEEDS TO BE ISOLATED AND MANAGE AS COVID-19 CASE",
				certificate.getLabel11(), Element.ALIGN_LEFT);

		cell.setFixedHeight(10);
		cell.setColspan(6);
		cell.setBorder(Rectangle.BOX);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Red(), Element.ALIGN_CENTER);

		cell.setFixedHeight(15);
		cell.setColspan(6);
		cell.setBorder(Rectangle.BOX);
		certificate.addToTable(tableReport, cell, "LOW RISK INDIVIDUAL", certificate.getLabel11Bold(),
				Element.ALIGN_LEFT);

		cell.setFixedHeight(30);
		cell.setColspan(2);
		cell.setBorder(Rectangle.BOX);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "NEGATIVE", certificate.getLabel11(), Element.ALIGN_CENTER);

		cell.setColspan(4);
		cell.setBorder(Rectangle.BOX);
		certificate.addToTable(tableReport, cell, "NON-COVID-19 CASE", certificate.getLabel11(), Element.ALIGN_LEFT);

		cell.setColspan(2);
		cell.setBorder(Rectangle.BOX);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "POSITIVE", certificate.getLabel11Red(), Element.ALIGN_CENTER);

		cell.setColspan(4);
		cell.setBorder(Rectangle.BOX);
		certificate.addToTable(tableReport, cell, "PATIENT REQUIRES RT-PCR COVID-19 TESTING FOR CONFIRMATORY",
				certificate.getLabel11(), Element.ALIGN_LEFT);

		PdfPCell cell2 = new PdfPCell();
		cell2.setColspan(6);
		cell2.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell2, "", certificate.getLabel11(), Element.ALIGN_LEFT);

		cell2.setColspan(1);
		certificate.addToTable(tableReport, cell2, "COMMENT:", certificate.getLabel11(), Element.ALIGN_LEFT);

		cell2.setColspan(5);
		certificate.addToTable(tableReport, cell2,
				"RESULT OF RAPID ANTIGEN TESTING SHOULD NOT BE USED AS THE SOLE BASIS TO DIAGNOSE COVID-19. RT-PCR  REMAINS THE GOLD STANDARD FOR THE DIAGNOSIS OF SARS-COV-2",
				certificate.getLabel11(), Element.ALIGN_LEFT);

		cell2.setFixedHeight(20);

		cell2.setColspan(6);
		certificate.addToTable(tableReport, cell2, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(tableReport, cell2, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell2.setColspan(6);
		cell2.setFixedHeight(25);
		certificate.addToTable(tableReport, cell2, "Methodolgy Immunochromatography Assay", certificate.getFontValue(),
				Element.ALIGN_LEFT);

		cell2.setColspan(4);
		certificate.addToTable(tableReport, cell2,
				"Guangzhou Wondfo biotech Co., Ltd. No.8 Lizhishan Road, Science City, Luogang District, 510663, Guangzhou, P.R China",
				certificate.getFontDocLabel(), Element.ALIGN_LEFT);

		cell2.setColspan(2);
		cell2.setFixedHeight(25);
		certificate.addToTable(tableReport, cell2, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
		document.add(tableReport);

		document.close();
		return document;
	}

	public Document getCertificateByCovid(Document document, PdfWriter pdfWriter, QisTransaction qisTransaction,
			QisTransactionSerology qisSerology, boolean withHeaderFooter, String LabRequest) {

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisSerology.getLabPersonel(), qisSerology.getQualityControl(),
				qisSerology.getMedicalDoctor(), "Physician", qisSerology.getVerifiedDate(), withHeaderFooter, false,
				LabRequest, null);

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
		// RESULTS
		PdfPTable tableReport = new PdfPTable(6);
		tableReport.setSpacingBefore(20);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(0);

		PdfPCell cell2 = new PdfPCell();
		cell2.setColspan(6);
		cell2.setBorder(Rectangle.BOX);
		cell2.setBackgroundColor(new Color(54, 96, 146, 255));
		certificate.addToTable(tableReport, cell2, "MEDICAL CERTIFICATE", certificate.getFontTitleAntibodyWhite(),
				Element.ALIGN_CENTER);

		cell.setColspan(6);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitleAntibodyWhite(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitleAntibodyWhite(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitleAntibodyWhite(), Element.ALIGN_CENTER);

		String msORmr = "";
		if ("M".equals(qisTransaction.getPatient().getGender())) {
			msORmr = "MR. ";
		} else {
			msORmr = "MS. ";
		}

		String age = String.valueOf(appUtility.calculateAgeInYear(qisTransaction.getPatient().getDateOfBirth(),
				qisTransaction.getTransactionDate()));

		cell.setColspan(6);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tableReport, cell,
				"           This is to certify that " + msORmr + ""
						+ appUtility.getPatientFullname(qisTransaction.getPatient()) + ", " + age
						+ " years old, was personally ",
				certificate.getLabel12Black(), Element.ALIGN_LEFT);

		certificate.addToTable(tableReport, cell,
				"           seen/ examined/ managed by the undersigned on "
						+ appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(), "MM/dd/yyyy")
						+ " for RAPID ANTIBODY TESTING.",
				certificate.getLabel12Black(), Element.ALIGN_LEFT);
		cell.setFixedHeight(50);
		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "SEROLOGY", certificate.getSubLabelBlackBold11(),
				Element.ALIGN_CENTER);

		cell.setFixedHeight(25);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, "", certificate.getSubLabelBlackBold11(), Element.ALIGN_CENTER);

		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "TEST", certificate.getSubLabelBlackBold11(), Element.ALIGN_CENTER);

		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "RESULT", certificate.getSubLabelBlackBold11(), Element.ALIGN_CENTER);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", certificate.getSubLabelBlackBold11(), Element.ALIGN_CENTER);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);

		cell.setFixedHeight(0);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "SARS-Cov2 IgM", certificate.getLabel11Blue(), Element.ALIGN_CENTER);

		PdfPCell cell3 = new PdfPCell();
		cell3.setColspan(2);
		cell3.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tableReport, cell3,
				appUtility.getReactiveNonReactive(qisSerology.getCovid().getCovigg()), certificate.getLabel11Black(),
				Element.ALIGN_CENTER);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", certificate.getSubLabelBlackBold11(), Element.ALIGN_CENTER);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		;
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);

		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "SARS-Cov2 IgG", certificate.getLabel11Blue(), Element.ALIGN_CENTER);

		certificate.addToTable(tableReport, cell3,
				appUtility.getReactiveNonReactive(qisSerology.getCovid().getCovigm()), certificate.getLabel11Black(),
				Element.ALIGN_CENTER);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", certificate.getSubLabelBlackBold11(), Element.ALIGN_CENTER);

		cell.setColspan(6);
		cell.setFixedHeight(50);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel11Blue(), Element.ALIGN_CENTER);

		cell.setColspan(6);
		cell2.setFixedHeight(25);
		certificate.addToTable(tableReport, cell, "Methodolgy Immunochromatography Assay", certificate.getFontValue(),
				Element.ALIGN_LEFT);

		cell.setColspan(4);
		certificate.addToTable(tableReport, cell,
				"Guangzhou Wondfo biotech Co., Ltd. No.8 Lizhishan Road, Science City, Luogang District, 510663, Guangzhou, P.R China",
				certificate.getFontDocLabel(), Element.ALIGN_LEFT);

		cell.setColspan(2);
		cell.setFixedHeight(25);
		certificate.addToTable(tableReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

		document.add(tableReport);
		document.close();
		return document;

	}

	public Document getCertificateByMedCertAntigen(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionSerology qisSerology, boolean withHeaderFooter,
			String LabRequest) throws ParseException {

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisSerology.getLabPersonel(), qisSerology.getQualityControl(),
				qisSerology.getMedicalDoctor(), "Physician", qisSerology.getVerifiedDate(), withHeaderFooter, false,
				"certAntigen", null);

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
		// RESULTS
		PdfPTable tableReport = new PdfPTable(6);
		tableReport.setSpacingBefore(20);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(0);

		String date = appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(), "MMMM dd, yyyy");
		cell.setColspan(5);
		certificate.addToTable(tableReport, cell, "Examination date at the Quest Phil Diagnostics:  ",
				certificate.getLabel12Black(), Element.ALIGN_RIGHT);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "  " + date, certificate.getFontSubTitle(), Element.ALIGN_LEFT);

		cell.setFixedHeight(20);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setFixedHeight(0);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "MEDICAL CERTIFICATE", certificate.getFont18Boldblack(),
				Element.ALIGN_CENTER);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "This is to certify that", certificate.getLabel12Black(),
				Element.ALIGN_CENTER);

		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Name of person tested:", certificate.getLabel12Black(),
				Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, appUtility.getPatientFullname(qisTransaction.getPatient()),
				certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		String age = String.valueOf(appUtility.calculateAgeInYear(qisTransaction.getPatient().getDateOfBirth(),
				qisTransaction.getTransactionDate()));
		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Age:", certificate.getLabel12Black(), Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, age + " YEARS OLD", certificate.getFont12BoldblackItalic(),
				Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Residing in:", certificate.getLabel12Black(), Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, qisTransaction.getPatient().getAddress(),
				certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Passport Number:", certificate.getLabel12Black(),
				Element.ALIGN_LEFT);
		cell.setColspan(4);
		if (qisTransaction.getPatient().getPassport() != null) {
			certificate.addToTable(tableReport, cell, qisTransaction.getPatient().getPassport(),
					certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);
		} else {
			certificate.addToTable(tableReport, cell, "-", certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);
		}

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat outputformat = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa");
		Date datea = null;
		String output = null;
		datea = df.parse(qisSerology.getRtantigen().getCollectionDate());
		output = outputformat.format(datea);
		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Date and Time of Specimen Collection:",
				certificate.getLabel12Black(), Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, output, certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(6);
		cell.setFixedHeight(0);
		certificate.addToTable(tableReport, cell,
				"At the time of examination, patient has no history of influenza-like symptoms for the past 14 days and ",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "the present physical examination was normal. ",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		cell.setFixedHeight(10);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(6);
		cell.setFixedHeight(0);
		certificate.addToTable(tableReport, cell,
				"The patient at the time of testing is "
						+ appUtility.getPositiveNegative(qisSerology.getRtantigen().getCov_ag())
						+ " for SARS-CoV-2 ANTIGEN TEST (causative ",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "agent of Covid-19) viral detection. ",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		cell.setFixedHeight(25);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setFixedHeight(0);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "This certificate is issued upon the request of the patient",
				certificate.getLabel12Black(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "for " + qisSerology.getRtantigen().getPurpose(),
				certificate.getFont12blackBoldUnderline(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "Remarks and Recommendation: ", certificate.getLabel12Black(),
				Element.ALIGN_LEFT);

		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "•", certificate.getLabel12Black(), Element.ALIGN_RIGHT);
		cell.setColspan(5);
		certificate.addToTable(tableReport, cell, "Observe COVID-19 precautionary measures ",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		cell.setFixedHeight(30);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(6);
		cell.setFixedHeight(25);
		certificate.addToTable(tableReport, cell, "Methodolgy Immunochromatography Assay", certificate.getFontValue(),
				Element.ALIGN_LEFT);

		cell.setColspan(4);
		certificate.addToTable(tableReport, cell,
				"Guangzhou Wondfo biotech Co., Ltd. No.8 Lizhishan Road, Science City, Luogang District, 510663, Guangzhou, P.R China",
				// "by Hangzhou Clongene Biotech Co., Ltd, Shanghai International Holding Corp.
				// GmbH (Europe) Eiffestrasse 80, D-20537 Hamburg, Germany",
				certificate.getFontDocLabel(), Element.ALIGN_LEFT);

		cell.setColspan(2);
		cell.setFixedHeight(25);
		certificate.addToTable(tableReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

		document.add(tableReport);
		document.close();
		return document;

	}

	public Document getCertificateByMedCertAntibody(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionSerology qisSerology, boolean withHeaderFooter,
			String LabRequest) {

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisSerology.getLabPersonel(), qisSerology.getQualityControl(),
				qisSerology.getMedicalDoctor(), "Physician", qisSerology.getVerifiedDate(), withHeaderFooter, false,
				"certAntibody", null);

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
		// RESULTS
		PdfPTable tableReport = new PdfPTable(6);
		tableReport.setSpacingBefore(20);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(0);

		String date = appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(), "MMMM dd, yyyy");
		cell.setColspan(5);
		certificate.addToTable(tableReport, cell, "Examination date at the Quest Phil Diagnostics:  ",
				certificate.getLabel12Black(), Element.ALIGN_RIGHT);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "  " + date, certificate.getFontSubTitle(), Element.ALIGN_LEFT);

		cell.setFixedHeight(20);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setFixedHeight(0);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "MEDICAL CERTIFICATE", certificate.getFont18Boldblack(),
				Element.ALIGN_CENTER);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "This is to certify that", certificate.getLabel12Black(),
				Element.ALIGN_CENTER);

		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Name of person tested:", certificate.getLabel12Black(),
				Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, appUtility.getPatientFullname(qisTransaction.getPatient()),
				certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", certificate.getLabel12Black(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Residing in:", certificate.getLabel12Black(), Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, qisTransaction.getPatient().getAddress(),
				certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(6);
		cell.setFixedHeight(0);
		certificate.addToTable(tableReport, cell,
				"At the time of examination, patient has no history of influenza-like symptoms for the past 14 days and ",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "the present physical examination was normal. ",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		cell.setFixedHeight(25);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setFixedHeight(0);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "This certificate is issued upon the request of the patient",
				certificate.getLabel12Black(), Element.ALIGN_LEFT);

		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "for " + qisSerology.getCovid().getPurpose(),
				certificate.getFont12blackBoldUnderline(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "Remarks and Recommendation: ", certificate.getLabel12Black(),
				Element.ALIGN_LEFT);

		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "•", certificate.getLabel12Black(), Element.ALIGN_RIGHT);
		cell.setColspan(5);
		certificate.addToTable(tableReport, cell, "Observe COVID-19 precautionary measures ",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		cell.setFixedHeight(50);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(6);
		cell.setFixedHeight(25);
		certificate.addToTable(tableReport, cell, "Methodolgy Immunochromatography Assay", certificate.getFontValue(),
				Element.ALIGN_LEFT);

		cell.setColspan(4);
		certificate.addToTable(tableReport, cell,
				"Guangzhou Wondfo biotech Co., Ltd. No.8 Lizhishan Road, Science City, Luogang District, 510663, Guangzhou, P.R China",
				certificate.getFontDocLabel(), Element.ALIGN_LEFT);

		cell.setColspan(2);
		cell.setFixedHeight(25);
		certificate.addToTable(tableReport, cell, "", certificate.getFontDocLabel(), Element.ALIGN_LEFT);

		document.add(tableReport);
		document.close();
		return document;

	}

	public Document getMedCertificateByRTPCR(Document document, PdfWriter pdfWriter, QisTransaction qisTransaction,
			QisTransactionSerology qisSerology, boolean withHeaderFooter, String LabRequest) throws ParseException {

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisSerology.getLabPersonel(), qisSerology.getQualityControl(),
				qisSerology.getMedicalDoctor(), "Physician", qisSerology.getVerifiedDate(), withHeaderFooter, false,
				"certAntigen", null);

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
		// RESULTS
		PdfPTable tableReport = new PdfPTable(6);
		tableReport.setSpacingBefore(20);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(0);

		DateFormat release = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat outputformatRealeas = new SimpleDateFormat("MMMM dd, yyyy");
		Date ReleasingDate = null;
		String outputRelease = null;
		ReleasingDate = release.parse(qisSerology.getRtpcr().getRealeasingDate());
		outputRelease = outputformatRealeas.format(ReleasingDate);

		cell.setColspan(5);
		certificate.addToTable(tableReport, cell, "Examination date at the Quest Phil Diagnostics:  ",
				certificate.getLabel12Black(), Element.ALIGN_RIGHT);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "  " + outputRelease, certificate.getFontSubTitle(),
				Element.ALIGN_LEFT);

		cell.setFixedHeight(20);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setFixedHeight(0);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "MEDICAL CERTIFICATE", certificate.getFont18Boldblack(),
				Element.ALIGN_CENTER);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "This is to certify that", certificate.getLabel12Black(),
				Element.ALIGN_CENTER);

		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Name of person tested:", certificate.getLabel12Black(),
				Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, appUtility.getPatientFullname(qisTransaction.getPatient()),
				certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		String age = String.valueOf(appUtility.calculateAgeInYear(qisTransaction.getPatient().getDateOfBirth(),
				qisTransaction.getTransactionDate()));
		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Age:", certificate.getLabel12Black(), Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, age + " YEARS OLD", certificate.getFont12BoldblackItalic(),
				Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Residing in:", certificate.getLabel12Black(), Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, qisTransaction.getPatient().getAddress(),
				certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Passport Number:", certificate.getLabel12Black(),
				Element.ALIGN_LEFT);
		cell.setColspan(4);
		if (qisTransaction.getPatient().getPassport() != null) {
			certificate.addToTable(tableReport, cell, qisTransaction.getPatient().getPassport(),
					certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);
		} else {
			certificate.addToTable(tableReport, cell, "-", certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);
		}

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat outputformat = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa");
		Date datea = null;
		String output = null;
		datea = df.parse(qisSerology.getRtpcr().getCollectionDate());
		output = outputformat.format(datea);
		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "Date and Time of Specimen Collection:",
				certificate.getLabel12Black(), Element.ALIGN_LEFT);
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, output, certificate.getFont12BoldblackItalic(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(6);
		cell.setFixedHeight(0);
		certificate.addToTable(tableReport, cell,
				"At the time of examination, patient has no history of influenza-like symptoms for the past 14 days and ",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "the present physical examination was normal. ",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "Molecular and Diagnostic Pathology Test Result (see attached)",
				certificate.getFontSubTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "Pathology Test Performed", certificate.getFontSubTitle(),
				Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "by JOSE B. LINGAD MEMORIAL GENERAL HOSPITAL",
				certificate.getFont18Boldblack(), Element.ALIGN_CENTER);

		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setColspan(6);
		cell.setFixedHeight(10);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);
		cell.setFixedHeight(0);
		certificate.addToTable(tableReport, cell,
				"The patient at the time of testing is "
						+ appUtility.getPositiveNegative(qisSerology.getRtpcr().getRtpcrResult())
						+ " for SARS-CoV-2 NUCLEIC ACID TEST (causative agent",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell,
				"of Covid-19) viral detection by REAL-TIME Polymerase Chain Reaction Test. SARS-CoV-2 NUCLEIC ACID",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);
		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "TEST NOT DETECTED", certificate.getFont12blackUnderline(),
				Element.ALIGN_LEFT);

		cell.setColspan(6);
		cell.setFixedHeight(25);
		certificate.addToTable(tableReport, cell, "", certificate.getFontTitle(), Element.ALIGN_CENTER);

		cell.setFixedHeight(0);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "This certificate is issued upon the request of the patient",
				certificate.getLabel12Black(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "for " + qisSerology.getRtpcr().getPurpose(),
				certificate.getFont12blackBoldUnderline(), Element.ALIGN_LEFT);

		cell.setColspan(6);
		certificate.addToTable(tableReport, cell, "Remarks and Recommendation: ", certificate.getLabel12Black(),
				Element.ALIGN_LEFT);

		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "•", certificate.getLabel12Black(), Element.ALIGN_RIGHT);
		cell.setColspan(5);
		certificate.addToTable(tableReport, cell, "Observe COVID-19 precautionary measures ",
				certificate.getFont12blackUnderline(), Element.ALIGN_LEFT);

		document.add(tableReport);
		document.close();
		return document;
	}

}
