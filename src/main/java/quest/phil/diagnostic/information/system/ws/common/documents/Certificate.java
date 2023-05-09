package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;

@Component
public class Certificate {
	private AppUtility appUtility;
	private Font fontTitle;
	private Font fontTitleXray;
	private Font fontSubTitle;
	private Font fontSubTitleXray;
	private Font fontLabel;
	private Font fontValue;
	private Font fontValueNormal;
	private Font fontValueNormalXray;
	private Font fontValueAlert;
	private Font fontDocLabel;
	private Font fontDocValue;
	private Font sfontDocValue;
	private Font fontDocNormal;
	private Font fontDocAlert;
	private Font fontNotes;
	private Font fontNotesNormal;
	private Font fontSubsubTitleXray;
	private Font fontNotesBold;
	private String UPARROW = "\u25B2"; // mataba up
	private String DOWNARROW = "\u25BC"; // mataba down
//	private String UPARROW = "\u2191"; // mapayat up
//	private String DOWNARROW = "\u2193"; // mapayat down

	private Font fontResult;
	private Font fontResultRed;

	// ANTIGEN COVID
	private Font fontTitleAntigen;
	private Font fontValueAntigen;
	private Font SubLabel;
	private Font SubResult;
	private Font SubResultRed;
	private Font Label;
	private Font Label13;
	private Font label11;
	private Font label11Red;
	private Font label11Bold;

	// ANTIGEN MEDCERT
	private Font font18Boldblack;
	private Font font12BoldblackItalic;
	private Font font12blackItalic;
	private Font font12blackUnderline;
	private Font font12blackBoldUnderline;

	// ANTIBODY COVID
	private Font fontTitleAntibodyWhite;
	private Font Label12Black;
	private Font SubLabelBlackBold11;
	private Font Label11Blue;
	private Font Label11Black;
	
	private Font fontNoResult;

	public Certificate(AppUtility appUtility) {
		super();
		this.appUtility = appUtility;
		
		this.fontNoResult = FontFactory.getFont("GARAMOND_BOLD");
		fontNoResult.setSize(8);
		fontNoResult.setColor(Color.RED);

		this.fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		this.fontTitle.setSize(14);
		this.fontTitle.setColor(Color.BLACK);
		
		this.fontTitleXray = FontFactory.getFont("GARAMOND_BOLD");
		this.fontTitleXray.setSize(20);
		this.fontTitleXray.setColor(Color.BLACK);

		this.fontSubTitle = FontFactory.getFont("GARAMOND_BOLD");
		this.fontSubTitle.setSize(12);
		this.fontSubTitle.setColor(Color.BLACK);
		
		this.fontSubTitleXray = FontFactory.getFont("GARAMOND_BOLD");
		this.fontSubTitleXray.setSize(18);
		this.fontSubTitleXray.setColor(Color.BLACK);
		
		this.fontSubsubTitleXray = FontFactory.getFont("GARAMOND_BOLD");
		this.fontSubsubTitleXray.setSize(10);
		this.fontSubsubTitleXray.setColor(Color.BLACK);

		this.fontLabel = FontFactory.getFont("GARAMOND");
		this.fontLabel.setSize(10);
		this.fontLabel.setColor(Color.BLUE);

		this.fontValue = FontFactory.getFont("GARAMOND_BOLD");
		this.fontValue.setSize(10);
		this.fontValue.setColor(Color.BLACK);

		this.fontValueNormal = FontFactory.getFont("GARAMOND");
		this.fontValueNormal.setSize(10);
		this.fontValueNormal.setColor(Color.BLACK);
		
		this.fontValueNormalXray = FontFactory.getFont("GARAMOND");
		this.fontValueNormalXray.setSize(14);
		this.fontValueNormalXray.setColor(Color.BLACK);

		this.fontValueAlert = FontFactory.getFont("GARAMOND_BOLD");
		this.fontValueAlert.setSize(10);
		this.fontValueAlert.setColor(Color.RED);

		this.fontDocLabel = FontFactory.getFont("GARAMOND");
		this.fontDocLabel.setSize(9);
		this.fontDocLabel.setColor(Color.BLUE);

		this.sfontDocValue = FontFactory.getFont("GARAMOND_BOLD");
		this.sfontDocValue.setSize(8);
		this.sfontDocValue.setColor(Color.BLACK);
		
		this.fontDocValue = FontFactory.getFont("GARAMOND_BOLD");
		this.fontDocValue.setSize(9);
		this.fontDocValue.setColor(Color.BLACK);

		this.fontDocNormal = FontFactory.getFont("GARAMOND");
		this.fontDocNormal.setSize(9);
		this.fontDocNormal.setColor(Color.BLACK);

		this.fontDocAlert = FontFactory.getFont("GARAMOND_BOLD");
		this.fontDocAlert.setSize(9);
		this.fontDocAlert.setColor(Color.RED);

		this.fontNotes = FontFactory.getFont("GARAMOND");
		this.fontNotes.setSize(8);
		this.fontNotes.setColor(Color.BLUE);

		this.fontNotesNormal = FontFactory.getFont("GARAMOND");
		this.fontNotesNormal.setSize(8);
		this.fontNotesNormal.setColor(Color.BLACK);

		this.fontNotesBold = FontFactory.getFont("GARAMOND_BOLD");
		this.fontNotesBold.setSize(8);
		this.fontNotesBold.setColor(Color.BLACK);

		this.fontTitleAntigen = FontFactory.getFont("GARAMOND_BOLD");
		this.fontTitleAntigen.setSize(18);
		this.fontTitleAntigen.setColor(Color.blue);

		this.fontTitleAntigen = FontFactory.getFont("GARAMOND_BOLD");
		this.fontTitleAntigen.setSize(18);
		this.fontTitleAntigen.setColor(new Color(3, 19, 167));

		this.fontValueAntigen = FontFactory.getFont("GARAMOND_BOLD");
		this.fontValueAntigen.setSize(14);
		this.fontValueAntigen.setColor(Color.BLACK);

		this.SubLabel = FontFactory.getFont("GARAMOND_BOLD");
		this.SubLabel.setSize(14);
		this.SubLabel.setColor(Color.BLACK);

		this.SubResult = FontFactory.getFont("GARAMOND_BOLD");
		this.SubResult.setSize(16);
		this.SubResult.setColor(Color.BLACK);

		this.SubResultRed = FontFactory.getFont("GARAMOND_BOLD");
		this.SubResultRed.setSize(16);
		this.SubResultRed.setColor(Color.red);

		this.Label = FontFactory.getFont("GARAMOND_BOLD");
		this.Label.setSize(16);
		this.Label.setColor(new Color(3, 19, 167));

		this.Label13 = FontFactory.getFont("GARAMOND_BOLD");
		this.Label13.setSize(13);
		this.Label13.setColor(Color.black);

		this.label11 = FontFactory.getFont("GARAMOND");
		this.label11.setSize(11);
		this.label11.setColor(Color.black);

		this.label11Red = FontFactory.getFont("GARAMOND");
		this.label11Red.setSize(11);
		this.label11Red.setColor(Color.red);

		this.label11Bold = FontFactory.getFont("GARAMOND_BOLD");
		this.label11Bold.setSize(11);
		this.label11Bold.setColor(Color.black);

		this.fontTitleAntibodyWhite = FontFactory.getFont("GARAMOND_BOLD");
		this.fontTitleAntibodyWhite.setSize(12);
		this.fontTitleAntibodyWhite.setColor(Color.WHITE);

		this.Label12Black = FontFactory.getFont("GARAMOND");
		this.Label12Black.setSize(12);
		this.Label12Black.setColor(Color.black);

		this.SubLabelBlackBold11 = FontFactory.getFont("GARAMOND_BOLD");
		this.SubLabelBlackBold11.setSize(11);
		this.SubLabelBlackBold11.setColor(Color.black);

		this.Label11Blue = FontFactory.getFont("GARAMOND");
		this.Label11Blue.setSize(11);
		this.Label11Blue.setColor(Color.blue);

		this.Label11Black = FontFactory.getFont("GARAMOND");
		this.Label11Black.setSize(11);
		this.Label11Black.setColor(Color.black);

		this.font18Boldblack = FontFactory.getFont("GARAMOND_BOLD");
		this.font18Boldblack.setSize(18);
		this.font18Boldblack.setColor(Color.black);

		this.font12BoldblackItalic = FontFactory.getFont("GARAMOND", 12, Font.BOLDITALIC);
		this.font12BoldblackItalic.setColor(Color.black);

		this.font12blackItalic = FontFactory.getFont("GARAMOND_ITALIC");
		this.font12blackItalic.setSize(12);
		this.font12blackItalic.setColor(Color.black);

		this.font12blackUnderline = FontFactory.getFont("GARAMOND", 12, Font.UNDERLINE);
		this.font12blackUnderline.setColor(Color.black);

		this.font12blackBoldUnderline = FontFactory.getFont("GARAMOND", 12, Font.UNDERLINE | Font.BOLD);
		this.font12blackBoldUnderline.setColor(Color.black);

		String path = "src/main/resources/garamond";
		File file = new File(path);
		String absolutePath = file.getAbsolutePath();

		try {
			// REQUIRED TO DISPLAY SYMBOL
			BaseFont bf = BaseFont.createFont(absolutePath + File.separator + "Garamond.ttf", BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED);

			fontResult = new Font(bf, 11);
			fontResult.setColor(Color.BLACK);

			fontResultRed = new Font(bf, 11);
			fontResultRed.setColor(Color.RED);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();

			// NORMAL FONT
			fontResult = FontFactory.getFont("GARAMOND_BOLD");
			fontResult.setSize(11);
			fontResult.setColor(Color.BLACK);

			fontResultRed = FontFactory.getFont("GARAMOND_BOLD");
			fontResultRed.setSize(11);
			fontResultRed.setColor(Color.RED);
		}
	}

	public Font getSfontDocValue() {
		return sfontDocValue;
	}

	public Font getFontSubsubTitleXray() {
		return fontSubsubTitleXray;
	}

	public Font getFontNoResult() {
		return fontNoResult;
	}

	public String getUPARROW() {
		return UPARROW;
	}

	public String getDOWNARROW() {
		return DOWNARROW;
	}

	public Font getFontResult() {
		return fontResult;
	}

	public Font getFontResultRed() {
		return fontResultRed;
	}

	public Font getFontTitle() {
		return fontTitle;
	}

	public Font getFontTitleXray() {
		return fontTitleXray;
	}

	public Font getFontSubTitle() {
		return fontSubTitle;
	}
	
	public Font getFontSubTitleXray() {
		return fontSubTitleXray;
	}

	public Font getFontLabel() {
		return fontLabel;
	}

	public Font getFontValue() {
		return fontValue;
	}

	public Font getFontValueNormal() {
		return fontValueNormal;
	}

	public Font getFontValueNormalXray() {
		return fontValueNormalXray;
	}

	public Font getFontDocLabel() {
		return fontDocLabel;
	}

	public Font getFontDocValue() {
		return fontDocValue;
	}

	public Font getFontNotes() {
		return fontNotes;
	}

	public Font getFontNotesNormal() {
		return fontNotesNormal;
	}

	public Font getFontDocNormal() {
		return fontDocNormal;
	}

	public Font getFontNotesBold() {
		return fontNotesBold;
	}

	public Font getFontDocAlert() {
		return fontDocAlert;
	}

	public Font getFontValueAlert() {
		return fontValueAlert;
	}

	public Font getFontTitleAntigen() {
		return fontTitleAntigen;
	}

	public Font getFontValueAntigen() {
		return fontValueAntigen;
	}

	public Font getSubLabel() {
		return SubLabel;
	}

	public Font getSubResult() {
		return SubResult;
	}

	public Font getLabel() {
		return Label;
	}

	public Font getLabel13() {
		return Label13;
	}

	public Font getLabel11() {
		return label11;
	}

	public Font getLabel11Red() {
		return label11Red;
	}

	public Font getLabel11Bold() {
		return label11Bold;
	}

	public Font getSubResultRed() {
		return SubResultRed;
	}

	public Font getFontTitleAntibodyWhite() {
		return fontTitleAntibodyWhite;
	}

	public Font getLabel12Black() {
		return Label12Black;
	}

	public Font getSubLabelBlackBold11() {
		return SubLabelBlackBold11;
	}

	public Font getLabel11Blue() {
		return Label11Blue;
	}

	public Font getLabel11Black() {
		return Label11Black;
	}

	public Font getFont18Boldblack() {
		return font18Boldblack;
	}

	public Font getFont12BoldblackItalic() {
		return font12BoldblackItalic;
	}

	public Font getFont12blackItalic() {
		return font12blackItalic;
	}

	public Font getFont12blackUnderline() {
		return font12blackUnderline;
	}

	public Font getFont12blackBoldUnderline() {
		return font12blackBoldUnderline;
	}

	public void addToTable(PdfPTable table, PdfPCell cell, String text, Font font, int align) {
		cell.setPhrase(new Phrase(text, font));
		cell.setHorizontalAlignment(align);
		table.addCell(cell);
	}

	public void addTextToDocument(Document document, String text, Font font, int align, float spacing) {
		Paragraph p = new Paragraph(text, font);
		p.setAlignment(align);
		p.setSpacingBefore(spacing);
		document.add(p);
	}

	public Font getDisplayFont(Boolean option, Font defaultFont, Font errorFont) {
		Font returnFont = defaultFont;
		if (option) {
			returnFont = errorFont;
		}
		return returnFont;
	}

	public Font getDisplayFontRange(Object value, Double min, Double max, Font defaultFont, Font errorFont) {
		Font returnFont = defaultFont;

		if (value != null) {
			Double val = Double.valueOf(String.valueOf(value));
			if (val != null) {
				if (val < min || val > max) {
					returnFont = errorFont;
				}
			}
		}

		return returnFont;
	}

	public String getDisplaySymbol(Object value, Double min, Double max) {
		String symbol = String.valueOf(value);

		if (value != null) {
			Double val = Double.valueOf(String.valueOf(value));

			symbol = appUtility.amountFormat(val, "##0.00");

			if (val != null) {
				if (val < min) {
					symbol = DOWNARROW + symbol;
				} else if (val > max) {
					symbol = UPARROW + symbol;
				}
			}
		}

		return symbol;
	}

	public PdfPTable certificateHeader(Image imgHeader) {
		PdfPTable tableHeader = new PdfPTable(1);
		tableHeader.setWidthPercentage(95f);
		tableHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell cellHeader = new PdfPCell();
		cellHeader.setBorder(Rectangle.NO_BORDER);
		cellHeader.addElement(imgHeader);
		cellHeader.setFixedHeight(70);
		cellHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tableHeader.addCell(cellHeader);

		return tableHeader;
	}

	public PdfPTable certificateFooter(Image imgFooter, float spacingBefore) {
		PdfPTable tableFooter = new PdfPTable(1);
		tableFooter.setSpacingBefore(spacingBefore);
		tableFooter.setWidthPercentage(90f);
		tableFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell cellFooter = new PdfPCell();
		cellFooter.setBorder(Rectangle.NO_BORDER);
		cellFooter.addElement(imgFooter);
		cellFooter.setFixedHeight(50);
		cellFooter.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tableFooter.addCell(cellFooter);

		return tableFooter;
	}

	public PdfPTable certificatePatientInformation(QisTransaction qisTransaction, Calendar verifiedDate,
			float spaceBefore) {
		PdfPTable tablePatient = new PdfPTable(new float[] { 1, 2, 1, 2, 1, 2 });
		tablePatient.setSpacingBefore(spaceBefore);
		tablePatient.setWidthPercentage(90f);
		tablePatient.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);

		QisPatient patient = qisTransaction.getPatient();
		addToTable(tablePatient, cell, "Name:", getFontLabel(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		addToTable(tablePatient, cell, appUtility.getPatientFullname(patient), getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		addToTable(tablePatient, cell, "SR#:", getFontLabel(), Element.ALIGN_LEFT);
		addToTable(tablePatient, cell, appUtility.numberFormat(qisTransaction.getId(), "000000"), getFontValue(),
				Element.ALIGN_LEFT);

		String gender = "MALE";
		if ("F".equals(patient.getGender())) {
			gender = "FEMALE";
		}
		String age = String
				.valueOf(appUtility.calculateAgeInYear(patient.getDateOfBirth(), qisTransaction.getTransactionDate()));

		addToTable(tablePatient, cell, "Age/Gender:", getFontLabel(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		addToTable(tablePatient, cell, age + "/" + gender, getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		addToTable(tablePatient, cell, "QuestID:", getFontLabel(), Element.ALIGN_LEFT);
		addToTable(tablePatient, cell, appUtility.numberFormat(patient.getId(), "0000"), getFontValue(),
				Element.ALIGN_LEFT);

		String transactionType = appUtility.getTransactionType(qisTransaction.getTransactionType());
		if (qisTransaction.getReferral() != null) {
			transactionType += " : " + qisTransaction.getReferral().getReferral();
		}

		addToTable(tablePatient, cell, "Transaction:", getFontLabel(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		addToTable(tablePatient, cell, transactionType, getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		addToTable(tablePatient, cell, "Dispatch:", getFontLabel(), Element.ALIGN_LEFT);
		addToTable(tablePatient, cell, appUtility.getDispatchType(qisTransaction.getDispatch()), getFontValue(),
				Element.ALIGN_LEFT);

		addToTable(tablePatient, cell, "Received:", getFontDocLabel(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		addToTable(tablePatient, cell,
				appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(), "MMM-dd-yyyy hh:mm:ss"),
				getFontDocValue(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		addToTable(tablePatient, cell, "Reported:", getFontDocLabel(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		String reportedDate = "";
		if (verifiedDate != null) {
			reportedDate = appUtility.calendarToFormatedDate(verifiedDate, "MMM-dd-yyyy hh:mm:ss");
		}
		addToTable(tablePatient, cell, reportedDate, getFontDocValue(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		addToTable(tablePatient, cell, "Printed:", getFontDocLabel(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		addToTable(tablePatient, cell,
				appUtility.calendarToFormatedDate(Calendar.getInstance(), "MMM-dd-yyyy hh:mm:ss"), getFontDocValue(),
				Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);

		return tablePatient;
	}

	public PdfPTable certificateSinature(boolean withNotication, QisUserPersonel labPersonel,
			QisUserPersonel qualityControl, QisDoctor doctor, QisDoctor classifyDoctor, String doctorType,
			float spaceBefore) {
		PdfPTable tableSignature = new PdfPTable(new float[] { 1, 6, 1, 6, 1, 6, 1 });
		tableSignature.setSpacingBefore(spaceBefore);
		tableSignature.setWidthPercentage(95f);
		tableSignature.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);

		if (withNotication) {
			cell.setColspan(7);
			addToTable(tableSignature, cell, "Note: Specimen rechecked, result/s verified.", getFontNotesNormal(),
					Element.ALIGN_LEFT);
		}

		cell.setColspan(1);
		cell.setFixedHeight(40);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		addToTable(tableSignature, cell, "", getFontDocValue(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, appUtility.getMedicalTechnologyDisplayName(labPersonel), getFontDocValue(),
				Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "", getFontDocValue(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, appUtility.getMedicalTechnologyDisplayName(qualityControl), getFontDocValue(),
				Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "", getFontDocValue(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, appUtility.getDoctorsDisplayName(doctor), getFontDocValue(),
				Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "", getFontDocValue(), Element.ALIGN_CENTER);

		cell.setFixedHeight(0);
		addToTable(tableSignature, cell, "", getFontNotesNormal(), Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		addToTable(tableSignature, cell, appUtility.getMedicalTechnologyLicenseNo(labPersonel), getFontNotesNormal(),
				Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		addToTable(tableSignature, cell, "", getFontNotesNormal(), Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		addToTable(tableSignature, cell, appUtility.getMedicalTechnologyLicenseNo(qualityControl), getFontNotesNormal(),
				Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		addToTable(tableSignature, cell, "", getFontNotesNormal(), Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		addToTable(tableSignature, cell,
				doctor != null && doctor.getLicenseNumber() != null ? "LIC NO. " + doctor.getLicenseNumber() : "",
				getFontNotesNormal(), Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		addToTable(tableSignature, cell, "", getFontNotesNormal(), Element.ALIGN_CENTER);

		addToTable(tableSignature, cell, "", getFontNotes(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "Registered Medical Technologist", getFontNotes(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "", getFontNotes(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "Quality Control", getFontNotes(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "", getFontNotes(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, doctorType, getFontNotes(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "", getFontNotes(), Element.ALIGN_CENTER);

		return tableSignature;
	}

	public PdfPTable certificateSinatureXrayClassification(boolean withNotication, QisUserPersonel labPersonel,
			QisDoctor doctor, String doctorType, float spaceBefore) {
		PdfPTable tableSignature = new PdfPTable(new float[] { 1, 6, 1, 6, 1 });
		tableSignature.setSpacingBefore(spaceBefore);
		tableSignature.setWidthPercentage(95f);
		tableSignature.setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);

		if (withNotication) {
			cell.setColspan(7);
			addToTable(tableSignature, cell, "Note: Specimen rechecked, result/s verified.", getFontNotesNormal(),
					Element.ALIGN_LEFT);
		}

		cell.setColspan(1);
		cell.setFixedHeight(20);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		addToTable(tableSignature, cell, "", getFontDocValue(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, appUtility.getMedicalTechnologyDisplayName(labPersonel), getFontDocValue(),
				Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "", getFontDocValue(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, appUtility.getDoctorsDisplayName(doctor), getFontDocValue(),
				Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "", getFontDocValue(), Element.ALIGN_CENTER);

		cell.setFixedHeight(0);
		addToTable(tableSignature, cell, "", getFontNotesNormal(), Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		addToTable(tableSignature, cell, appUtility.getMedicalTechnologyLicenseNo(labPersonel), getFontNotesNormal(),
				Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		addToTable(tableSignature, cell, "", getFontNotesNormal(), Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		addToTable(tableSignature, cell,
				doctor != null && doctor.getLicenseNumber() != null ? "LIC NO. " + doctor.getLicenseNumber() : "",
				getFontNotesNormal(), Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		addToTable(tableSignature, cell, "", getFontNotesNormal(), Element.ALIGN_CENTER);

		addToTable(tableSignature, cell, "", getFontNotes(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "Quality Assurance", getFontNotes(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "", getFontNotes(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, doctorType, getFontNotes(), Element.ALIGN_CENTER);
		addToTable(tableSignature, cell, "", getFontNotes(), Element.ALIGN_CENTER);

		return tableSignature;
	}
}
