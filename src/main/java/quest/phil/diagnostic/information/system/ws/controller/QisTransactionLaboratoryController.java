package quest.phil.diagnostic.information.system.ws.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItemLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisLaboratoryRequirementRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabClassificationRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabRequirementRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisQualityTransactionRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemLaboratoriesRepositories;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryRequestRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserPersonelRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}")
public class QisTransactionLaboratoryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratoryController.class);
	private final String CATEGORY = "TRANSACTION_LABORATORY";

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	private QisTransactionLaboratoryRequestRepository qisTransactionLaboratoryRequestRepository;

	@Autowired
	private QisTransactionLaboratoryInfoRepository qisTransactionLaboratoryInfoRepository;

	@Autowired
	private QisUserPersonelRepository qisUserPersonelRepository;

	@Autowired
	private QisTransactionItemLaboratoriesRepositories qisTransactionItemLaboratoriesRepositories;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	private QisQualityTransactionRepository qisQualityTransactionRepository;

	@GetMapping("laboratory/{laboratoryId}")
	public QisTransactionLaboratoryRequest getTransactionLaboratory(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction Laboratory:" + transactionId + " [" + laboratoryId + "]");
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionLaboratoryRequest qisTransactionLaboratoryRequest = qisTransactionLaboratoryRequestRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionLaboratoryRequest == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.");
		}

		return qisTransactionLaboratoryRequest;
	}

	// LIST ALL TRANSCTION ITEMS
	@GetMapping("laboratory_requests")
	public List<QisTransactionLaboratoryRequest> getAllTransactionLaboratoryRequest(@PathVariable String transactionId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View Transaction All Laboratories:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		return qisTransactionLaboratoryRequestRepository
				.getTransactionLaboratoryRequestByTransactionid(qisTransaction.getId());
	}

	@PostMapping("laboratory_requests/submit")
	public QisQualityTransaction submitTransactionLaboratoryRequirements(
			@Valid @RequestBody QisTransactionLabRequirementRequest labRequest, @PathVariable String transactionId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Submit Transaction Laboratory Requirements:" + transactionId);
		QisQualityTransaction qisTransaction = qisQualityTransactionRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.", new Throwable("transactionId:Transaction not found."));
		}

		Set<QisLaboratoryRequirementRequest> submitRequirements = labRequest.getSubmitRequirements();
		List<QisTransactionLaboratoryRequest> laboratoryList = qisTransactionLaboratoryRequestRepository
				.getTransactionLaboratoryRequestByTransactionid(qisTransaction.getId());

		boolean isUpdate = false;
		String updateData = "";
		for (QisTransactionLaboratoryRequest laboratory : laboratoryList) {
			QisLaboratoryRequirementRequest submitted = submitRequirements.stream()
					.filter(sb -> sb.getId().equals(laboratory.getId())
							&& sb.getItemLaboratory().equals(laboratory.getItemLaboratory()))
					.findAny().orElse(null);

			if (submitted != null) {
				if (!submitted.getIsSubmitted().equals(laboratory.isSubmitted())) {
					String action = "UNVERIFIED";
					if (submitted.getIsSubmitted()) {
						laboratory.setSubmitted(true);
						laboratory.setStatus(1);
						laboratory.setVerifiedBy(authUser.getId());
						laboratory.setVerifiedDate(Calendar.getInstance());
						action = "VERIFIED";
					} else {
						laboratory.setSubmitted(false);
						laboratory.setStatus(0);
						laboratory.setVerifiedBy(null);
						laboratory.setVerifiedDate(null);
					}
					updateData = appUtility.formatUpdateData(updateData,
							action + ":" + laboratory.getItemDetails().getItemName(),
							String.valueOf(!laboratory.isSubmitted()), String.valueOf(submitted.getIsSubmitted()));
					laboratory.setUpdatedAt(Calendar.getInstance());
					laboratory.setUpdatedBy(authUser.getId());
					isUpdate = true;
				}
			}
		}

		if (isUpdate) {
			qisTransactionLaboratoryRequestRepository.saveAll(laboratoryList);
			qisLogService.warn(authUser.getId(), QisTransactionLaboratoryController.class.getSimpleName(), "UPDATE",
					updateData, qisTransaction.getId(), CATEGORY);
		}

		return qisTransaction;
	}

	@PostMapping("laboratory_requirements/{transactionItemId}/submit")
	public QisTransactionItemLaboratories submitLaboratoryRequirements(
			@Valid @RequestBody QisTransactionLabRequirementRequest labRequest, @PathVariable String transactionId,
			@PathVariable Long transactionItemId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Submit Transaction Laboratory Requirements:" + transactionId + "["
				+ transactionItemId + "]");
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.", new Throwable("transactionId:Transaction not found."));
		}

		QisTransactionItemLaboratories qisTxnItemLab = qisTransactionItemLaboratoriesRepositories
				.getTransactionItemLaboratoriesById(qisTransaction.getId(), transactionItemId);
		if (qisTxnItemLab == null) {
			throw new RuntimeException("Transaction Item not found.",
					new Throwable("transactionItemId. Transaction Item not found."));
		}

		boolean isUpdate = false;
		String updateData = "";
		Set<QisLaboratoryRequirementRequest> submitRequirements = labRequest.getSubmitRequirements();
		Set<QisTransactionLaboratoryInfo> laboratoryList = qisTxnItemLab.getTransactionLabRequests();
		for (QisTransactionLaboratoryInfo laboratory : laboratoryList) {
			QisLaboratoryRequirementRequest submitted = submitRequirements.stream()
					.filter(sb -> sb.getId().equals(laboratory.getId())
							&& sb.getItemLaboratory().equals(laboratory.getItemLaboratory()))
					.findAny().orElse(null);

			if (submitted != null) {
				if (!submitted.getIsSubmitted().equals(laboratory.isSubmitted())) {
					String action = "UNVERIFIED";
					if (submitted.getIsSubmitted()) {
						laboratory.setSubmitted(true);
						laboratory.setStatus(1);
						laboratory.setVerifiedBy(authUser.getId());
						laboratory.setVerified(qisUserPersonelRepository.findByUserid(authUser.getUserid()));
						laboratory.setVerifiedDate(Calendar.getInstance());
						action = "VERIFIED";
					} else {
						laboratory.setSubmitted(false);
						laboratory.setStatus(0);
						laboratory.setVerifiedBy(null);
						laboratory.setVerifiedDate(null);
					}
					updateData = appUtility.formatUpdateData(updateData,
							action + ":" + laboratory.getItemDetails().getItemName(),
							String.valueOf(!laboratory.isSubmitted()), String.valueOf(submitted.getIsSubmitted()));
					laboratory.setUpdatedAt(Calendar.getInstance());
					laboratory.setUpdatedBy(authUser.getId());
					isUpdate = true;
				}
			}
		}

		if (isUpdate) {
			qisTransactionLaboratoryInfoRepository.saveAll(laboratoryList);
			qisLogService.warn(authUser.getId(), QisTransactionLaboratoryController.class.getSimpleName(), "UPDATE",
					updateData, qisTransaction.getId(), "ITEM REQUEST");
		}

		return qisTxnItemLab;
	}

	@PostMapping("laboratory/{transactionItemId}/call")
	public QisQualityTransaction calledPendingPatient(@PathVariable String transactionId,
			@PathVariable Long transactionItemId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.", new Throwable("transactionId:Transaction not found."));
		}

		QisTransactionItemLaboratories qisTxnItemLab = qisTransactionItemLaboratoriesRepositories
				.getTransactionItemLaboratoriesById(qisTransaction.getId(), transactionItemId);
		if (qisTxnItemLab == null) {
			throw new RuntimeException("Transaction Item not found.",
					new Throwable("transactionItemId. Transaction Item not found."));
		}
		
		qisTxnItemLab.setCalled(1);
		qisTransactionItemLaboratoriesRepositories.save(qisTxnItemLab);
		return null;
	}

	@PostMapping("laboratory/{transactionItemId}/classify")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisQualityTransaction classifyLaboratoryRequest(@PathVariable String transactionId,
			@PathVariable Long transactionItemId,
			@Valid @RequestBody QisTransactionLabClassificationRequest classRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Classify Transaction Laboratory Request:" + transactionId + "["
				+ transactionItemId + "]");

		String classify = appUtility.getClassification(classRequest.getClassification());
		if (classify == null)
			throw new RuntimeException(
					"Fail! -> Cause: Classification Type[" + classRequest.getClassification() + "] not found.",
					new Throwable("classification: Classification Type[" + classRequest.getClassification()
							+ "] not found."));

		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.", new Throwable("transactionId:Transaction not found."));
		}

		QisTransactionItemLaboratories qisTxnItemLab = qisTransactionItemLaboratoriesRepositories
				.getTransactionItemLaboratoriesById(qisTransaction.getId(), transactionItemId);
		if (qisTxnItemLab == null) {
			throw new RuntimeException("Transaction Item not found.",
					new Throwable("transactionItemId. Transaction Item not found."));
		}

		if (qisTxnItemLab.getLabPersonelId().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to check the same transaction.",
					new Throwable("You are not authorize to check the same transaction."));
		}

		QisDoctor qisDoctor = appUtility.getQisDoctorByDoctorId(classRequest.getDoctorId());
		if (qisDoctor == null) {
			throw new RuntimeException("Doctor not found.", new Throwable("doctorId. Doctor not found."));
		}

		String classification = classRequest.getClassification();
		boolean isUpdate = false;
		String updateData = "";
		String action = "ADDED";
		if (qisTxnItemLab.getClassification() == null) {
			qisTxnItemLab.setClassification(classification);
			qisTxnItemLab.setOverAllFindings(classRequest.getOverAllFindings());
			qisTxnItemLab.setClassifiedId(authUser.getId());
			qisTxnItemLab.setClassifiedDate(Calendar.getInstance());
			qisTxnItemLab.setClassifiedBy(qisUserPersonelRepository.findByUserid(authUser.getUserid()));

			qisTxnItemLab.setClassifyDoctorId(qisDoctor.getId());
			qisTxnItemLab.setClassifyDoctor(qisDoctor);

			isUpdate = true;
			updateData = classification;
		} else {
			if (!classification.equals(qisTxnItemLab.getClassification())) {
				isUpdate = true;
				action = "UPDATED";
				updateData = appUtility.formatUpdateData(updateData, "Class", qisTxnItemLab.getClassification(),
						classification);
				qisTxnItemLab.setClassification(classification);
				qisTxnItemLab.setClassifiedDate(Calendar.getInstance());
			}

			if (classRequest.getOverAllFindings() != null) {
				if (!classRequest.getOverAllFindings().equals(qisTxnItemLab.getOverAllFindings())) {
					isUpdate = true;
					action = "UPDATED";
					updateData = appUtility.formatUpdateData(updateData, "OverAllFindings",
							qisTxnItemLab.getOverAllFindings(), classRequest.getOverAllFindings());
					qisTxnItemLab.setOverAllFindings(classRequest.getOverAllFindings());
				}
			} else {
				if (qisTxnItemLab.getOverAllFindings() != null) {
					isUpdate = true;
					action = "UPDATED";
					updateData = appUtility.formatUpdateData(updateData, "OverAllFindings",
							qisTxnItemLab.getOverAllFindings(), null);
					qisTxnItemLab.setOverAllFindings(null);
				}
			}

			if (qisTxnItemLab.getClassifyDoctorId() == null
					|| qisDoctor.getId() != qisTxnItemLab.getClassifyDoctorId()) {
				isUpdate = true;
				action = "UPDATED";

				qisTxnItemLab.setClassifyDoctorId(qisDoctor.getId());
				qisTxnItemLab.setClassifyDoctor(qisDoctor);

				updateData = appUtility.formatUpdateData(updateData, "physician",
						qisTxnItemLab.getClassifyDoctor().getDoctorid(), qisDoctor.getDoctorid());

			}
		}

		if (isUpdate) {
			qisTransactionItemLaboratoriesRepositories.save(qisTxnItemLab);

			qisLogService.info(authUser.getId(), QisTransactionLaboratoryController.class.getSimpleName(), action,
					updateData, qisTransaction.getId(), "ITEM REQUEST-CLASSIFY");
		}

		return qisQualityTransactionRepository.findByTransactionid(transactionId);
	}

	@PutMapping("laboratory/{transactionItemId}/qc")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisQualityTransaction qcLaboratoryRequest(@PathVariable String transactionId,
			@PathVariable Long transactionItemId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-QC Transaction Laboratory Request:" + transactionId + "[" + transactionItemId
				+ "]");

		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.", new Throwable("transactionId:Transaction not found."));
		}

		QisTransactionItemLaboratories qisTxnItemLab = qisTransactionItemLaboratoriesRepositories
				.getTransactionItemLaboratoriesById(qisTransaction.getId(), transactionItemId);
		if (qisTxnItemLab == null) {
			throw new RuntimeException("Transaction Item not found.",
					new Throwable("transactionItemId. Transaction Item not found."));
		}

//		if (qisTxnItemLab.getClassifiedId().equals(authUser.getId())) {
//			throw new RuntimeException("You are not authorize to check the same transaction.",
//					new Throwable("You are not authorize to check the same transaction."));
//		}

		if (qisTxnItemLab.getQcId() == null) {
			qisTxnItemLab.setQcId(authUser.getId());
			qisTxnItemLab.setQcDate(Calendar.getInstance());
			qisTransactionItemLaboratoriesRepositories.save(qisTxnItemLab);

			QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
			qisTxnItemLab.setQualityControl(labPersonel);
		} else {
			throw new RuntimeException("Transaction Item was already Checked.",
					new Throwable("Transaction Item Request was already Checked."));
		}

		return qisQualityTransactionRepository.findByTransactionid(transactionId);
	}

}
