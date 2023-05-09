package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;

public class TestPDF {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;

	public TestPDF(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document testPDF(Document document, PdfWriter pdfWriter) throws DocumentException, IOException {

		HeaderFooter header = new HeaderFooter(new Phrase("This is a header."), true);
		HeaderFooter footer = new HeaderFooter(new Phrase("This is footer page "), new Phrase("."));
		document.setHeader(header);
		document.setFooter(footer);
		
//		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility);
//		pdfWriter.setPageEvent(event);
		
		document.open();
		
		Font font = FontFactory.getFont("GARAMOND_BOLD");
		font.setSize(12);
		font.setColor(Color.BLACK);

		Paragraph p1 = new Paragraph("TEST PARAGRAPH PAGE 1", font);
		p1.setAlignment(Element.ALIGN_LEFT);
		p1.setSpacingBefore(5);
		document.add(p1);
		
		document.newPage();

		Paragraph p2 = new Paragraph("TEST PARAGRAPH PAGE 2", font);
		p2.setAlignment(Element.ALIGN_LEFT);
		p2.setSpacingBefore(5);
		document.add(p2);
		
		document.close();
		return document;
	}
}
