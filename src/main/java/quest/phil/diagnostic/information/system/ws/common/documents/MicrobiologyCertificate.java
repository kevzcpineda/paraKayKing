package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.Set;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionMicrobiology;

public class MicrobiologyCertificate {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private String NO_RESULT = "- NO RESULT -";
	public static final String DATEFORMATTRANSACTION = "MMM dd, yyyy";
	
	
	public MicrobiologyCertificate(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}
	
	public Document getTransactionLaboratoryMicrobiologyCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionMicrobiology qisMicrobiology, boolean withHeaderFooter)
			throws DocumentException, IOException {
		
		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisMicrobiology.getLabPersonel(), qisMicrobiology.getQualityControl(),
				qisMicrobiology.getMedicalDoctor(), "Pathologist", qisMicrobiology.getVerifiedDate(), withHeaderFooter, false,
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
		PdfPTable tableReport = new PdfPTable(4);
		tableReport.setSpacingBefore(20);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(0);

		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, "LABORATORY RESULT", certificate.getFontTitle(),
				Element.ALIGN_CENTER);
		certificate.addToTable(tableReport, cell, "MICROBIOLOGY", certificate.getFontValue(), Element.ALIGN_LEFT);

		formatMicrobiologyCertificate(qisMicrobiology, tableReport, certificate, cell, cellBorder);

		document.add(tableReport);

		document.close();
		return document;
	}

	private void formatMicrobiologyCertificate(QisTransactionMicrobiology qisMicrobiology, PdfPTable tableReport,
			Certificate certificate, PdfPCell cell, PdfPCell cellBorder) {
		
		QisItem itemDetails = qisMicrobiology.getItemDetails();
		Set<QisLaboratoryProcedureService> services = itemDetails.getServiceRequest();
		
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);

		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "SPECIMEN", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, qisMicrobiology.getGs().getSpecimen(), certificate.getFontValueNormal(), Element.ALIGN_LEFT);
		
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "TEST DONE", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "GRAM STAIN", certificate.getFontValueNormal(), Element.ALIGN_LEFT);
		
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		
		
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, qisMicrobiology.getGs().getResult(), certificate.getFontValueNormal(), Element.ALIGN_LEFT);
		
		cell.setColspan(4);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		
		
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "INTERPRETATION", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "1-9/HPF= +1", certificate.getFontValueNormal(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, "10-19/HPF= +2", certificate.getFontValueNormal(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tableReport, cell, ">20/HPF= +3", certificate.getFontValueNormal(), Element.ALIGN_LEFT);
		
		
	}

}
