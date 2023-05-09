package quest.phil.diagnostic.information.system.ws.common.documents;

import java.io.IOException;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class QisHeaderFooterImageEvent extends PdfPageEventHelper {
	private String applicationHeader;
	private String applicationFooter;
	private Certificate certificate;
	private boolean withHeaderFooter;

	public QisHeaderFooterImageEvent(String applicationHeader, String applicationFooter, Certificate certificate,
			boolean withHeaderFooter) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.certificate = certificate;
		this.withHeaderFooter = withHeaderFooter;
	}

	public void onStartPage(PdfWriter writer, Document document) {
		try {
			Image imgHeader = null;
			if (withHeaderFooter) {
				imgHeader = Image.getInstance("src/main/resources/images/" + applicationHeader);
			}
			document.add(certificate.certificateHeader(imgHeader));
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
	}

	public void onEndPage(PdfWriter writer, Document document) {
		try {
			Image imgFooter = null;
			if (withHeaderFooter) {
				imgFooter = Image.getInstance("src/main/resources/images/" + applicationFooter);
			}
			PdfPTable tableFooter = certificate.certificateFooter(imgFooter, 0);
			tableFooter.setTotalWidth(600);
			tableFooter.writeSelectedRows(0, -1, 20, 50, writer.getDirectContent());
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
	}

}
