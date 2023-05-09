package quest.phil.diagnostic.information.system.ws.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.web.bind.annotation.RequestParam;

import quest.phil.diagnostic.information.system.ws.common.AppEmailUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.common.documents.ConsolidatedCertificate;
import quest.phil.diagnostic.information.system.ws.model.QisEmailConfig;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.response.ReturnResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisQualityTransactionRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}")

public class QisTransactionSendEmailController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionSendEmailController.class);
	private final String CATEGORY = "SEND";

	@Value("${application.name}")
	private String applicationName;

	@Value("${application.address}")
	private String applicationAddress;

	@Value("${application.header}")
	private String applicationHeader;

	@Value("${application.footer}")
	private String applicationFooter;

	private QisEmailConfig emailConfig;

	public QisTransactionSendEmailController(QisEmailConfig QisEmailConfig) {
		this.emailConfig = QisEmailConfig;
	}

	@Autowired
	private AppEmailUtility emailUtility;

	@Autowired
	private QisQualityTransactionRepository qisQualityTransactionRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	private QisTransactionItemRepository qisTransactionitems;

	@PostMapping("/quest_quality/email")
	public ResponseEntity<?> sendEmailQuestQuality(
			@Valid @RequestParam(name = "file", required = false) List<MultipartFile> fileInput,
			@RequestParam("sendTo") String sendTo, @RequestParam(name = "sendCc", required = false) String sendCc,
			@RequestParam("emailSubject") String emailSubject, @RequestParam("emailBody") String emailBody,
			@RequestParam("sendStatus") Boolean sendStatus, @PathVariable String transactionId,
			@AuthenticationPrincipal QisUserDetails authUser) throws DocumentException, Exception {
		LOGGER.info(authUser.getId() + "-Email Transactions Results:" + transactionId);

		QisQualityTransaction qisTransaction = qisQualityTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		List<QisTransactionItem> transItem = qisTransactionitems.findByTransactionid(qisTransaction.getId());
		for (QisTransactionItem value :transItem) {
			value.setSend(sendStatus);
			qisTransactionitems.save(value);
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);

		document.addTitle(appUtility.getPatientFullname(qisTransaction.getPatient()));

		ConsolidatedCertificate certificate = new ConsolidatedCertificate(applicationName, applicationHeader,
				applicationFooter, appUtility);
		certificate.getTransactionConsolidatedCertificate(document, pdfWriter, qisTransaction, true);

		byte[] bytes = baos.toByteArray();
		boolean ans = fileInput.isEmpty();

		if (ans == true) {
			fileInput = new ArrayList<>();
		}

		MultipartFile multipartFile = new MockMultipartFile("file",
				appUtility.getPatientFullname(qisTransaction.getPatient()) + ".pdf", "text/plain", bytes);

		if (multipartFile != null) {
			fileInput.add(multipartFile);
		}

		emailUtility.sendEmail(emailConfig, sendTo, emailSubject, sendCc, emailBody, fileInput);
		qisLogService.info(authUser.getId(), QisTransactionSendEmailController.class.getSimpleName(), "EMAIL",
				"Results:" + transactionId, qisTransaction.getId(), CATEGORY);
		return ResponseEntity.ok(new ReturnResponse("success", "email send"));
	}

	@PostMapping("/soa/email")
	public ResponseEntity<?> sendEmailSoa(
			@Valid @RequestParam(name = "file", required = false) List<MultipartFile> fileInput,
			@RequestParam("sendTo") String sendTo, @RequestParam(name = "sendCc", required = false) String sendCc,
			@RequestParam("emailSubject") String emailSubject, @RequestParam("emailBody") String emailBody,
			@PathVariable String transactionId, @AuthenticationPrincipal QisUserDetails authUser)
			throws DocumentException, Exception {
		LOGGER.info(authUser.getId() + "-Email Transactions Results:" + transactionId);
		QisQualityTransaction qisTransaction = qisQualityTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);

		document.addTitle(appUtility.getPatientFullname(qisTransaction.getPatient()));

		ConsolidatedCertificate certificate = new ConsolidatedCertificate(applicationName, applicationHeader,
				applicationFooter, appUtility);
		certificate.getTransactionConsolidatedCertificate(document, pdfWriter, qisTransaction, true);

		byte[] bytes = baos.toByteArray();
		boolean ans = fileInput.isEmpty();

		if (ans == true) {
			fileInput = new ArrayList<>();
		}

		MultipartFile multipartFile = new MockMultipartFile("file",
				appUtility.getPatientFullname(qisTransaction.getPatient()) + ".pdf", "text/plain", bytes);

		if (multipartFile != null) {
			fileInput.add(multipartFile);
		}

		emailUtility.sendEmail(emailConfig, sendTo, emailSubject, sendCc, emailBody, fileInput);
		qisLogService.info(authUser.getId(), QisTransactionSendEmailController.class.getSimpleName(), "EMAIL",
				"Results:" + transactionId, qisTransaction.getId(), CATEGORY);
		return ResponseEntity.ok(new ReturnResponse("success", "email send"));
	}

}
