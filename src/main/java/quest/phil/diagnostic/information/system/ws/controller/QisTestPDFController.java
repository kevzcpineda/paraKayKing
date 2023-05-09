package quest.phil.diagnostic.information.system.ws.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.common.documents.TestPDF;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;

@RestController
@RequestMapping("/api/v1")
public class QisTestPDFController {
	@Value("${application.header}")
	private String applicationHeader;

	@Value("${application.footer}")
	private String applicationFooter;

	@Autowired
	AppUtility appUtility;
	
	@GetMapping("testpdf")
	public void getTransactionReceipt(HttpServletResponse response, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("TEST PDF");

		TestPDF test = new TestPDF(applicationHeader, applicationFooter, appUtility);
		test.testPDF(document, pdfWriter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=test.pdf";
		response.setHeader(headerKey, headerValue);
	}
}
