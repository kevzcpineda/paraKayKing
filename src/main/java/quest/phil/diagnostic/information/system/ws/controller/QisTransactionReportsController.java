package quest.phil.diagnostic.information.system.ws.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppEmailUtility;
import quest.phil.diagnostic.information.system.ws.common.AppTransactionUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.common.documents.BacteriologyCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.ChemistryCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.ClassificationCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.ClinicalMicroscopyCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.ConsolidatedCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.EcgCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.HematologyCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.IndustrialCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.MedicalCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.MicrobiologyCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.PhysicalExamCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.ReceiptDocument;
import quest.phil.diagnostic.information.system.ws.common.documents.SerologyCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.ToxicologyCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.UltrasoundCertificate;
import quest.phil.diagnostic.information.system.ws.common.documents.XRayCertificate;
import quest.phil.diagnostic.information.system.ws.model.QisEmailConfig;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItemLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionBacteriology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionChemistry;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionClinicalMicroscopy;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionMicrobiology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionPhysicalExamination;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionToxicology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionUltrasound;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionXRay;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ecg.QisTransactionLabEcg;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ultrasound.QisTransactionLabUltrasound;
import quest.phil.diagnostic.information.system.ws.repository.QisQualityTransactionRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionBacteriologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionChemistryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionClinicalMicroscopyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionHematologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemLaboratoriesRepositories;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionMIcrobiologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionPhysicalExaminationRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionSerologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionToxicologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionUltrasoundRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionXRayRepository;
import quest.phil.diagnostic.information.system.ws.repository.ecg.QisTransactionLabEcgRepository;
import quest.phil.diagnostic.information.system.ws.repository.ultrasound.QisTransactionLabUltrasoundRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}")
public class QisTransactionReportsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionReportsController.class);
	private final String CATEGORY = "TRANSACTION_REPORTS";

	@Value("${application.name}")
	private String applicationName;

	@Value("${application.address}")
	private String applicationAddress;

	@Value("${application.header}")
	private String applicationHeader;

	@Value("${application.footer}")
	private String applicationFooter;

	private QisEmailConfig emailConfig;

	public QisTransactionReportsController(QisEmailConfig QisEmailConfig) {
		this.emailConfig = QisEmailConfig;
	}

	@Autowired
	AppUtility appUtility;

	@Autowired
	private QisTransactionLabUltrasoundRepository qisTransactionLabUltrasound;

	@Autowired
	AppTransactionUtility appTransactionUtility;

	@Autowired
	private QisTransactionUltrasoundRepository qisTransactionUltrasoundRepository;

	@Autowired
	private QisTransactionRepository qisTransactionRepository;

	@Autowired
	private QisTransactionPhysicalExaminationRepository qisTransactionPhysicalExaminationRepository;

	@Autowired
	private QisTransactionXRayRepository qisTransactionXRayRepository;

	@Autowired
	private QisTransactionClinicalMicroscopyRepository qisTransactionClinicalMicroscopyRepository;

	@Autowired
	private QisTransactionToxicologyRepository qisTransactionToxicologyRepository;

	@Autowired
	private QisTransactionHematologyRepository qisTransactionHematologyRepository;

	@Autowired
	private QisTransactionChemistryRepository qisTransactionChemistryRepository;

	@Autowired
	private QisTransactionMIcrobiologyRepository qisTransactionMicrobiologyRepository;
	
	@Autowired
	private QisTransactionBacteriologyRepository qisTransactionBacteriologyRepository;
	
	@Autowired
	private QisTransactionSerologyRepository qisTransactionSerologyRepository;

	@Autowired
	private QisTransactionItemLaboratoriesRepositories qisTransactionItemLaboratoriesRepositories;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	private QisQualityTransactionRepository qisQualityTransactionRepository;

	@Autowired
	private QisTransactionItemRepository qisTransactionitems;

	@Autowired
	private QisTransactionLabEcgRepository qisTransactionLabEcgRepository;

	@Autowired
	private AppEmailUtility emailUtility;

	@GetMapping("receipt")
	public void getTransactionReceipt(HttpServletResponse response, @PathVariable String transactionId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Receipt:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		Document document = new Document(PageSize.LEGAL);
		document.open();
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(15, 410, 20, 10);
		document.addTitle("RECEIPT");

		ReceiptDocument receipt = new ReceiptDocument(applicationName, appUtility);
		receipt.getTransactionReceipt(document, pdfWriter, qisTransaction);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid() + "_receipt.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "RECEIPT",
				"Retrieve Receipt:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/laboratory/{laboratoryId}/pe/certificate")
	public void getTransactionPhysicalExaminationCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(
				authUser.getId() + "-View Transaction Laboratory Physical Examination Certificate:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionPhysicalExamination qisTransactionPhysicalExamination = qisTransactionPhysicalExaminationRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionPhysicalExamination == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}

		if (!"PE".equals(qisTransactionPhysicalExamination.getItemDetails().getItemLaboratory())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Physical Examination.",
					new Throwable(qisTransactionPhysicalExamination.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Physical Examination."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		Document document = new Document(PageSize.LETTER);
		if (response != null) {
			PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("MEDICAL EXAMINATION REPORT");

		PhysicalExamCertificate certificate = new PhysicalExamCertificate(applicationName, applicationHeader,
				applicationFooter, appUtility);
		certificate.getTransactionLaboratoryPhysicalExamCertificate(document, qisTransaction,
				qisTransactionPhysicalExamination, withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid()
				+ "_physical_exam_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Phyical Exam:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/laboratory/{laboratoryId}/xray/certificate")
	public void getTransactionXRayCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory XRay Certificate:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionXRay qisTransactionXRay = qisTransactionXRayRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionXRay == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}

		if (!"XR".equals(qisTransactionXRay.getItemDetails().getItemLaboratory())) {
			throw new RuntimeException("Transaction Laboratory Request is not for X-Ray.",
					new Throwable(qisTransactionXRay.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for X-Ray."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		qisTransactionXRay.setPrint(true);
		qisTransactionXRayRepository.save(qisTransactionXRay);

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("RADIOGRAPHIC REPORT");

		XRayCertificate certificate = new XRayCertificate(applicationHeader, applicationFooter, appUtility);
		certificate.getTransactionLaboratoryXRayCertificate(document, pdfWriter, qisTransaction, qisTransactionXRay,
				withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid() + "_xray_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"XRay:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/laboratory/{laboratoryId}/ecg/certificate")
	public void getTransactionEcgCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Ecg Certificate:" + transactionId);

		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionLabEcg qisTransactionEcg = qisTransactionLabEcgRepository.getTransactionLabReqId(laboratoryId);
		if (qisTransactionEcg == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("RADIOGRAPHIC REPORT");

		EcgCertificate certificate = new EcgCertificate(applicationHeader, applicationFooter, appUtility);
		certificate.getTransactionLaboratoryEcgCertificate(document, pdfWriter, qisTransaction, qisTransactionEcg,
				withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid() + "_xray_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"XRay:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/laboratory/{laboratoryId}/ultrasound/certificate")
	public void getTransactionUltrasoundCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Ecg Certificate:" + transactionId);

		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionUltrasound qisTransactionUltrasound = qisTransactionUltrasoundRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);

		QisTransactionLabUltrasound oldUltrasoundData = qisTransactionLabUltrasound
				.getTransactionLabUltrasoundByLabReqId(laboratoryId);
		if (oldUltrasoundData == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("ULTRASOUND REPORT");

		UltrasoundCertificate certificate = new UltrasoundCertificate(applicationHeader, applicationFooter, appUtility);
		certificate.getTransactionLaboratoryUltrasoundCertificate(document, pdfWriter, qisTransaction,
				oldUltrasoundData, withHeaderFooter, qisTransactionUltrasound);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid()
				+ "_ultrasound_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"XRay:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/laboratory/{laboratoryId}/clinical_microscopy/certificate")
	public void getTransactionClinicalMicroscopyCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Clinical Microcopy Certificate:" + transactionId);

		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionClinicalMicroscopy qisTransactionClinicalMicroscopy = qisTransactionClinicalMicroscopyRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionClinicalMicroscopy == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}

		if (qisTransactionClinicalMicroscopy.getItemDetails().getItemLaboratoryProcedure() == null
				|| !"CM".equals(qisTransactionClinicalMicroscopy.getItemDetails().getItemLaboratoryProcedure())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Clinical Microscopy.",
					new Throwable(qisTransactionClinicalMicroscopy.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Clinical Microscopy."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		qisTransactionClinicalMicroscopy.setPrint(true);
		qisTransactionClinicalMicroscopyRepository.save(qisTransactionClinicalMicroscopy);

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("CLINICAL MICROSCOPY REPORT");

		ClinicalMicroscopyCertificate certificate = new ClinicalMicroscopyCertificate(applicationHeader,
				applicationFooter, appUtility);
		certificate.getTransactionLaboratoryClinicalMicroscopyCertificate(document, pdfWriter, qisTransaction,
				qisTransactionClinicalMicroscopy, true);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid()
				+ "_clinical_microscopy_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Clinical Microcopy:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/laboratory/{laboratoryId}/hematology/certificate")
	public void getTransactionHematologyCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Hematology Certificate:" + transactionId);

		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionHematology qisTransactionHematology = qisTransactionHematologyRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionHematology == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}

		if (qisTransactionHematology.getItemDetails().getItemLaboratoryProcedure() == null
				|| !"HE".equals(qisTransactionHematology.getItemDetails().getItemLaboratoryProcedure())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Hematology.",
					new Throwable(qisTransactionHematology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Hematology."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		qisTransactionHematology.setPrint(true);
		qisTransactionHematologyRepository.save(qisTransactionHematology);

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("HEMATOLOGY REPORT");

		HematologyCertificate certificate = new HematologyCertificate(applicationHeader, applicationFooter, appUtility);
		certificate.getTransactionLaboratoryHematologyCertificate(document, pdfWriter, qisTransaction,
				qisTransactionHematology, withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid()
				+ "_hematology_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Hematology:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/laboratory/{laboratoryId}/chemistry/certificate")
	public void getTransactionChemistryCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Chemistry Certificate:" + transactionId);

		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionChemistry qisTransactionChemistry = qisTransactionChemistryRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionChemistry == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}

		if (qisTransactionChemistry.getItemDetails().getItemLaboratoryProcedure() == null
				|| !"CH".equals(qisTransactionChemistry.getItemDetails().getItemLaboratoryProcedure())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Chemistry.",
					new Throwable(qisTransactionChemistry.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Chemistry."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		qisTransactionChemistry.setPrint(true);
		qisTransactionChemistryRepository.save(qisTransactionChemistry);

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("CHEMISTRY REPORT");

		ChemistryCertificate certificate = new ChemistryCertificate(applicationHeader, applicationFooter, appUtility);
		certificate.getTransactionLaboratoryChemistryCertificate(document, pdfWriter, qisTransaction,
				qisTransactionChemistry, withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid() + "_chemistry_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Chemistry:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/laboratory/{laboratoryId}/microbiology/certificate")
	public void getTransactionmicrobiologyCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Microbiology Certificate:" + transactionId);
	
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}
		
		QisTransactionMicrobiology qisTransactionMicrobiology = qisTransactionMicrobiologyRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionMicrobiology == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}
		
		if (qisTransactionMicrobiology.getItemDetails().getItemLaboratoryProcedure() == null
				|| !"MB".equals(qisTransactionMicrobiology.getItemDetails().getItemLaboratoryProcedure())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Microbiology.",
					new Throwable(qisTransactionMicrobiology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Microbiology."));
		}
		
		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		qisTransactionMicrobiology.setPrint(true);
		qisTransactionMicrobiologyRepository.save(qisTransactionMicrobiology);

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		
		MicrobiologyCertificate certificate = new MicrobiologyCertificate(applicationHeader, applicationFooter, appUtility);

		certificate.getTransactionLaboratoryMicrobiologyCertificate(document, pdfWriter, qisTransaction,
				qisTransactionMicrobiology, withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid() + "_microbiology_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Chemistry:" + transactionId, qisTransaction.getId(), CATEGORY);
	}
	
	@GetMapping("/laboratory/{laboratoryId}/bacteriology/certificate")
	public void getTransactionBacteriologyCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Bacteriology Certificate:" + transactionId);
	
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}
		
		QisTransactionBacteriology qisTransactionBacteriology = qisTransactionBacteriologyRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionBacteriology == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}
		
		if (qisTransactionBacteriology.getItemDetails().getItemLaboratoryProcedure() == null
				|| !"BT".equals(qisTransactionBacteriology.getItemDetails().getItemLaboratoryProcedure())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Bacteriology.",
					new Throwable(qisTransactionBacteriology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Microbiology."));
		}
		
		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		qisTransactionBacteriology.setPrint(true);
		qisTransactionBacteriologyRepository.save(qisTransactionBacteriology);

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		
		BacteriologyCertificate certificate = new BacteriologyCertificate(applicationHeader, applicationFooter, appUtility);

		certificate.getTransactionLaboratoryBacteriologyCertificate(document, pdfWriter, qisTransaction,
				qisTransactionBacteriology, withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid() + "_bacteriology_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Chemistry:" + transactionId, qisTransaction.getId(), CATEGORY);
	}
	
	
	@GetMapping("/laboratory/{laboratoryId}/serology/certificate")
	public void getTransactionSerologyCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Serology Certificate:" + transactionId);

		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionSerology qisTransactionSerology = qisTransactionSerologyRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionSerology == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}

		if (qisTransactionSerology.getItemDetails().getItemLaboratoryProcedure() == null
				|| !"SE".equals(qisTransactionSerology.getItemDetails().getItemLaboratoryProcedure())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Serology.",
					new Throwable(qisTransactionSerology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Serology."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		qisTransactionSerology.setPrint(true);
		qisTransactionSerologyRepository.save(qisTransactionSerology);

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		
		

		SerologyCertificate certificate = new SerologyCertificate(applicationHeader, applicationFooter, appUtility);
		for (QisLaboratoryProcedureService serRequest : qisTransactionSerology.getItemDetails().getServiceRequest()) {

			if (serRequest.getLaboratoryRequest().toString() == "ANTIGEN") {
				document.setMargins(10, 10, 45, 10);
				document.addTitle(appUtility.getPatientFullname(qisTransaction.getPatient()) + "_ANTIGEN");
				certificate.getCertificateByAntigen(document, pdfWriter, qisTransaction, qisTransactionSerology, true,
						serRequest.getLaboratoryRequest().toString());
			} else if (serRequest.getLaboratoryRequest().toString() == "COVID") {
				document.setMargins(10, 10, 10, 10);
				document.addTitle(appUtility.getPatientFullname(qisTransaction.getPatient()));
				certificate.getCertificateByCovid(document, pdfWriter, qisTransaction, qisTransactionSerology, true,
						serRequest.getLaboratoryRequest().toString());
			
			} else {
				document.setMargins(10, 10, 10, 10);
				document.addTitle(appUtility.getPatientFullname(qisTransaction.getPatient()));
				certificate.getTransactionLaboratorySerologyCertificate(document, pdfWriter, qisTransaction,
						qisTransactionSerology, true);
			}

		}
		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid() + "_serology_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Serology:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@PostMapping("/laboratory/{laboratoryId}/serology/email")
	public void getTransactionSerologySendEmail(HttpServletResponse response, @RequestParam("sendTo") String sendTo,
			@RequestParam(name = "sendCc", required = false) String sendCc,
			@RequestParam(required = false) Boolean withHeaderFooter,
			@RequestParam("emailSubject") String emailSubject, @RequestParam("emailBody") String emailBody,
			 @PathVariable String transactionId,
			@RequestParam(name = "file", required = false) List<MultipartFile> fileInput,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Serology Certificate:" + transactionId);

		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionSerology qisTransactionSerology = qisTransactionSerologyRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionSerology == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}

		if (qisTransactionSerology.getItemDetails().getItemLaboratoryProcedure() == null
				|| !"SE".equals(qisTransactionSerology.getItemDetails().getItemLaboratoryProcedure())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Serology.",
					new Throwable(qisTransactionSerology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Serology."));
		}

		
		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		
		for (QisLaboratoryProcedureService serRequest : qisTransactionSerology.getItemDetails().getServiceRequest()) {
			if (serRequest.getLaboratoryRequest().toString() == "ANTIGEN") {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Document document = new Document(PageSize.LETTER);
				PdfWriter pdfWriter = null;
				if (response != null) {
					pdfWriter = PdfWriter.getInstance(document, baos);
				}
				document.setMargins(10, 10, 10, 10);

				SerologyCertificate certificate = new SerologyCertificate(applicationHeader, applicationFooter, appUtility);
				
				document.addTitle(appUtility.getPatientFullname(qisTransaction.getPatient()) + "_ANTIGEN");
				certificate.getCertificateByAntigen(document, pdfWriter, qisTransaction, qisTransactionSerology, true,
						serRequest.getLaboratoryRequest().toString());

				byte[] bytes = baos.toByteArray();
				boolean ans = fileInput.isEmpty();

				if (ans == true) {
					fileInput = new ArrayList<>();
				}

				MultipartFile multipartFile = new MockMultipartFile("file",
						appUtility.getPatientFullname(qisTransaction.getPatient()) + "_ANTIGEN" + ".pdf", "text/plain",
						bytes);

				if (multipartFile != null) {
					fileInput.add(multipartFile);
				}

				
				emailUtility.sendEmail(emailConfig, sendTo, emailSubject, sendCc, emailBody, fileInput);
			}
		}

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "Send",
				"Serology:" + transactionId, qisTransaction.getId(), CATEGORY);

	}

	@GetMapping("/laboratory/{laboratoryId}/med_cert/certificate")
	public void getTransactionMedicalCertificateCovid(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Serology Certificate:" + transactionId);

		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionSerology qisTransactionSerology = qisTransactionSerologyRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionSerology == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}

		if (qisTransactionSerology.getItemDetails().getItemLaboratoryProcedure() == null
				|| !"SE".equals(qisTransactionSerology.getItemDetails().getItemLaboratoryProcedure())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Serology.",
					new Throwable(qisTransactionSerology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Serology."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		qisTransactionSerology.setPrint(true);
		qisTransactionSerologyRepository.save(qisTransactionSerology);

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("SEROLOGY REPORT");

		SerologyCertificate certificate = new SerologyCertificate(applicationHeader, applicationFooter, appUtility);
		for (QisLaboratoryProcedureService serRequest : qisTransactionSerology.getItemDetails().getServiceRequest()) {

			if (serRequest.getLaboratoryRequest().toString() == "ANTIGEN") {
				certificate.getCertificateByMedCertAntigen(document, pdfWriter, qisTransaction, qisTransactionSerology,
						true, serRequest.getLaboratoryRequest().toString());
			} else if (serRequest.getLaboratoryRequest().toString() == "COVID") {
				certificate.getCertificateByMedCertAntibody(document, pdfWriter, qisTransaction, qisTransactionSerology,
						true, serRequest.getLaboratoryRequest().toString());
			} else if (serRequest.getLaboratoryRequest().toString() == "RTPCR") {
				certificate.getMedCertificateByRTPCR(document, pdfWriter, qisTransaction, qisTransactionSerology, true,
						serRequest.getLaboratoryRequest().toString());
			} else {
				certificate.getTransactionLaboratorySerologyCertificate(document, pdfWriter, qisTransaction,
						qisTransactionSerology, true);
			}

		}
		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid() + "_serology_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Serology:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/laboratory/{laboratoryId}/toxicology/certificate")
	public void getTransactionToxicologyCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Toxicology Certificate:" + transactionId);

		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionToxicology qisTransactionToxicology = qisTransactionToxicologyRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionToxicology == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.",
					new Throwable("laboratoryId. Transaction Laboratory Request not found."));
		}

		QisItem qisItem = qisTransactionToxicology.getItemDetails();
		if (!"LAB".equals(qisItem.getItemCategory()) && "TO".equals(qisItem.getItemLaboratoryProcedure())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Toxicology.",
					new Throwable(qisTransactionToxicology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Toxicology."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		qisTransactionToxicology.setPrint(true);
		qisTransactionToxicologyRepository.save(qisTransactionToxicology);

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("TOXICOLOGY REPORT");

		ToxicologyCertificate certificate = new ToxicologyCertificate(applicationHeader, applicationFooter, appUtility);
		certificate.getTransactionLaboratoryToxicologyCertificate(document, pdfWriter, qisTransaction,
				qisTransactionToxicology, withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid()
				+ "_toxicology_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Toxicology:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/item/{transactionItemId}/industrial/certificate")
	public void getTransactionIndustrialCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long transactionItemId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View Transaction Laboratory Industrial Certificate:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionItemLaboratories qisTxnItemLab = qisTransactionItemLaboratoriesRepositories
				.getTransactionItemLaboratoriesById(qisTransaction.getId(), transactionItemId);
		if (qisTxnItemLab == null) {
			throw new RuntimeException("Transaction Item not found.",
					new Throwable("transactionItemId: Transaction Item not found."));
		}

		if (!"PCK".equals(qisTxnItemLab.getItemType())) {
			throw new RuntimeException("Transaction Item not for industrial.",
					new Throwable("transactionItemId: Transaction Item not for industrial."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		qisTxnItemLab.setPrint(true);
		qisTransactionItemLaboratoriesRepositories.save(qisTxnItemLab);

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 150);
		document.addTitle("INDUSTRIAL REPORT");

		IndustrialCertificate certificate = new IndustrialCertificate(applicationHeader, applicationFooter, appUtility);
		certificate.getTransactionLaboratoryIndustrialCertificate(document, pdfWriter, qisTransaction, qisTxnItemLab,
				withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid()
				+ "_industrial_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Industrial:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/item/{transactionItemId}/medical/certificate")
	public void getTransactionMedicalCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long transactionItemId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View Transaction Medical Certificate:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionItemLaboratories qisTxnItemLab = qisTransactionItemLaboratoriesRepositories
				.getTransactionItemLaboratoriesById(qisTransaction.getId(), transactionItemId);
		if (qisTxnItemLab == null) {
			throw new RuntimeException("Transaction Item not found.",
					new Throwable("transactionItemId: Transaction Item not found."));
		}

		if (!"PCK".equals(qisTxnItemLab.getItemType())) {
			throw new RuntimeException("Transaction Item not for industrial.",
					new Throwable("transactionItemId: Transaction Item not for industrial."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		qisTxnItemLab.setPrint(true);
		qisTransactionItemLaboratoriesRepositories.save(qisTxnItemLab);

		Document document = new Document(PageSize.LETTER);
		if (response != null) {
			PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("MEDICAL CERTIFICATE REPORT");

		MedicalCertificate certificate = new MedicalCertificate(applicationName, applicationHeader, applicationFooter,
				appUtility);
		certificate.getTransactionMedicalCertificate(document, qisTransaction, qisTxnItemLab, withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid() + "_medical_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Medical:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/item/{transactionItemId}/classification/certificate")
	public void getTransactionClassificationCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@PathVariable Long transactionItemId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View Transaction Classification Certificate:" + transactionId);
		QisTransaction qisTransaction = qisTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionItemLaboratories qisTxnItemLab = qisTransactionItemLaboratoriesRepositories
				.getTransactionItemLaboratoriesById(qisTransaction.getId(), transactionItemId);
		if (qisTxnItemLab == null) {
			throw new RuntimeException("Transaction Item not found.",
					new Throwable("transactionItemId: Transaction Item not found."));
		}

		if (!"PCK".equals(qisTxnItemLab.getItemType())) {
			throw new RuntimeException("Transaction Item not for industrial.",
					new Throwable("transactionItemId: Transaction Item not for industrial."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.setMargins(10, 10, 10, 10);
		document.addTitle("CLASSIFICATION REPORT");

		ClassificationCertificate certificate = new ClassificationCertificate(applicationName, applicationHeader,
				applicationFooter, appUtility);
		certificate.getTransactionClassificationCertificate(document, pdfWriter, qisTransaction, qisTxnItemLab,
				withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid()
				+ "_classification_certificate.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Classification:" + transactionId, qisTransaction.getId(), CATEGORY);
	}

	@GetMapping("/consolidated/certificate")
	public void getTransactionConsolidatedCertificate(HttpServletResponse response,
			@RequestParam(required = false) Boolean withHeaderFooter, @PathVariable String transactionId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View Transaction Consolidated Certificate:" + transactionId);
		QisQualityTransaction qisTransaction = qisQualityTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		if (withHeaderFooter == null) {
			withHeaderFooter = true;
		}

		List<QisTransactionItem> transItem = qisTransactionitems.findByTransactionid(qisTransaction.getId());
		for (QisTransactionItem value : transItem) {
			value.setPrint(true);
			qisTransactionitems.save(value);
		}

		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = null;
		if (response != null) {
			pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		}
		document.addTitle(appUtility.getPatientFullname(qisTransaction.getPatient()));

		ConsolidatedCertificate certificate = new ConsolidatedCertificate(applicationName, applicationHeader,
				applicationFooter, appUtility);
		certificate.getTransactionConsolidatedCertificate(document, pdfWriter, qisTransaction, withHeaderFooter);

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + qisTransaction.getTransactionid() + "_results.pdf";
		response.setHeader(headerKey, headerValue);

		qisLogService.info(authUser.getId(), QisTransactionReportsController.class.getSimpleName(), "CERTIFICATE",
				"Consolidated:" + transactionId, qisTransaction.getId(), CATEGORY);
	}
}
