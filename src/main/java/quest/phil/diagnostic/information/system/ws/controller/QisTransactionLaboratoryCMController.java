package quest.phil.diagnostic.information.system.ws.controller;

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

import quest.phil.diagnostic.information.system.ws.common.AppLaboratoryCMUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionClinicalMicroscopy;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabCMRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionClinicalMicroscopyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/laboratory/{laboratoryId}")
public class QisTransactionLaboratoryCMController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratoryCMController.class);

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	private QisTransactionItemRepository qisTransactionItemRepository;

	@Autowired
	private QisTransactionClinicalMicroscopyRepository qisTransactionClinicalMicroscopyRepository;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	private AppLaboratoryCMUtility appLaboratoryCMUtility;

	@PostMapping("/clinical_microscopy")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionLaboratoryDetails saveTransactionLaboratoryCM(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @Valid @RequestBody QisTransactionLabCMRequest cmRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Save CM:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		QisTransactionItem transactionItem = qisTransactionItemRepository.getTransactionItemById(qisTransaction.getId(),
				qisTransactionClinicalMicroscopy.getTransactionItemId());
		if (transactionItem == null) {
			throw new RuntimeException("Transaction Item not found.", new Throwable("Transaction Item not found."));
		}

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(cmRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		Set<QisLaboratoryProcedureService> serviceRequest = qisTransactionClinicalMicroscopy.getItemDetails()
				.getServiceRequest();
		// VALIDATION
		appLaboratoryCMUtility.validateClinicalMicroscopyRequest(null, serviceRequest, cmRequest);

		// SAVING
		appLaboratoryCMUtility.saveClinicalMicroscopy(qisTransaction, laboratoryId, qisTransactionClinicalMicroscopy,
				serviceRequest, cmRequest, authUser, qisDoctor);

		QisTransactionLaboratoryDetails laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

		return laboratoryDetails;
	}

	@PutMapping("/clinical_microscopy/qc")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionLaboratoryDetails saveTransactionLaboratoryCMQC(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Save CM QC:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		if (qisTransactionClinicalMicroscopy.getLabPersonelId().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to check the same transaction.",
					new Throwable("You are not authorize to check the same transaction."));
		}
		
		if (qisTransactionClinicalMicroscopy.getStatus() < 2) {
			throw new RuntimeException("Transaction Laboratory Request has no results.",
					new Throwable(qisTransactionClinicalMicroscopy.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request has no results."));
		}

		QisTransactionLaboratoryDetails laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);
				
		if (qisTransactionClinicalMicroscopy.getQcId() == null) {
			appLaboratoryCMUtility.saveClinicalMicroscopyQC(qisTransactionClinicalMicroscopy,
					laboratoryId, authUser);
			laboratoryDetails.setQcId(qisTransactionClinicalMicroscopy.getQcId());
			laboratoryDetails.setQcDate(qisTransactionClinicalMicroscopy.getQcDate());
			laboratoryDetails.setStatus(qisTransactionClinicalMicroscopy.getStatus());
			laboratoryDetails.setQualityControl(qisTransactionClinicalMicroscopy.getQualityControl());
		} else {
			throw new RuntimeException("Transaction Laboratory Request was already Checked.",
					new Throwable(qisTransactionClinicalMicroscopy.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request was already Checked."));
		}

		return laboratoryDetails;
	}

	@GetMapping("/clinical_microscopy")
	public QisTransactionClinicalMicroscopy getTransactionLaboratoryCM(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View CM:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		return qisTransactionClinicalMicroscopy;
	}
}
