package quest.phil.diagnostic.information.system.ws.controller;

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

import quest.phil.diagnostic.information.system.ws.common.AppLaboratoryTOUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionToxicology;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabToxicologyRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionToxicologyRepository;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/laboratory/{laboratoryId}")
public class QisTransactionLaboratoryToxicologyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratoryXRayController.class);

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	private QisTransactionToxicologyRepository qisTransactionToxicologyRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	private AppLaboratoryTOUtility appLaboratoryTOUtility;

	@Autowired
	AppUtility appUtility;

	@PostMapping("/toxicology")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionToxicology saveTransactionLaboratoryToxicology(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @Valid @RequestBody QisTransactionLabToxicologyRequest toxicologyRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Save TOXICOLOGY:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(toxicologyRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		// SAVING
		appLaboratoryTOUtility.saveToxicology(qisTransaction, laboratoryId, qisTransactionToxicology, toxicologyRequest,
				authUser, qisDoctor);

		return qisTransactionToxicology;
	}

	@PutMapping("/toxicology/qc")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionToxicology saveTransactionLaboratoryToxicologyQC(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Save TOXICOLOGY QC:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		if (qisTransactionToxicology.getLabPersonelId().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to check the same transaction.",
					new Throwable("You are not authorize to check the same transaction."));
		}

		if (qisTransactionToxicology.getStatus() < 2) {
			throw new RuntimeException("Transaction Laboratory Request has no results.",
					new Throwable(qisTransactionToxicology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request has no results."));
		}

		if (qisTransactionToxicology.getQcId() == null) {
			appLaboratoryTOUtility.saveToxicologyQC(qisTransactionToxicology, laboratoryId, authUser);
		} else {
			throw new RuntimeException("Transaction Laboratory Request was already Checked.",
					new Throwable(qisTransactionToxicology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request was already Checked."));
		}

		return qisTransactionToxicology;
	}

	@GetMapping("/toxicology")
	public QisTransactionToxicology getTransactionLaboratoryToxicology(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View TOXICOLOGY:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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
			throw new RuntimeException("Transaction Laboratory Request is not for Toxicology.");
		}

		return qisTransactionToxicology;
	}
}
