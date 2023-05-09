package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionBacteriology;

public class BacteriologyCertificate {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private String NO_RESULT = "- NO RESULT -";
	public static final String DATEFORMATTRANSACTION = "MMM dd, yyyy";
	
	public BacteriologyCertificate(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}
	
	public Document getTransactionLaboratoryBacteriologyCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionBacteriology qisBacteriology, boolean withHeaderFooter)
			throws DocumentException, IOException {
		
		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisBacteriology.getLabPersonel(), qisBacteriology.getQualityControl(),
				qisBacteriology.getMedicalDoctor(), "Pathologist", qisBacteriology.getVerifiedDate(), withHeaderFooter, false,
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
		certificate.addToTable(tableReport, cell, "BACTERIOLOGY", certificate.getFontValue(), Element.ALIGN_LEFT);

		formatBacteriologyCertificate(qisBacteriology, tableReport, certificate, cell, cellBorder);

		document.add(tableReport);

		document.close();
		return document;
	}

	private void formatBacteriologyCertificate(QisTransactionBacteriology qisBacteriology, PdfPTable tableReport,
			Certificate certificate, PdfPCell cell, PdfPCell cellBorder) {
		// TODO Auto-generated method stub
		
	}
}
