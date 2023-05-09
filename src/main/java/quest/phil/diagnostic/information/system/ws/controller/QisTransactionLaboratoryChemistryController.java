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

import quest.phil.diagnostic.information.system.ws.common.AppLaboratoryCHUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionChemistry;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabCHRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionChemistryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/laboratory/{laboratoryId}")
public class QisTransactionLaboratoryChemistryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratoryChemistryController.class);

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	private QisTransactionChemistryRepository qisTransactionChemistryRepository;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private AppLaboratoryCHUtility appLaboratoryCHUtility;

	@PostMapping("/chemistry")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionLaboratoryDetails saveTransactionLaboratoryCHEM(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @Valid @RequestBody QisTransactionLabCHRequest chRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Save CH:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(chRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		Set<QisLaboratoryProcedureService> serviceRequest = qisTransactionChemistry.getItemDetails()
				.getServiceRequest();
		appLaboratoryCHUtility.validateChemistry(null, serviceRequest, chRequest);

		// SAVING
		appLaboratoryCHUtility.saveChemistry(qisTransaction, laboratoryId, qisTransactionChemistry, serviceRequest,
				chRequest, authUser, qisDoctor);

		QisTransactionLaboratoryDetails laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

		return laboratoryDetails;
	}

	@PutMapping("/chemistry/qc")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionLaboratoryDetails saveTransactionLaboratoryCHEMQC(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Save CH QC:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		if (qisTransactionChemistry.getLabPersonelId().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to check the same transaction.",
					new Throwable("You are not authorize to check the same transaction."));
		}

		if (qisTransactionChemistry.getStatus() < 2) {
			throw new RuntimeException("Transaction Laboratory Request has no results.",
					new Throwable(qisTransactionChemistry.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request has no results."));
		}

		if (qisTransactionChemistry.getQcId() == null) {
			appLaboratoryCHUtility.saveChemistryQC(qisTransactionChemistry, laboratoryId, authUser);
		} else {
			throw new RuntimeException("Transaction Laboratory Request was already Checked.",
					new Throwable(qisTransactionChemistry.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request was already Checked."));
		}

		QisTransactionLaboratoryDetails laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

		return laboratoryDetails;
	}

	@GetMapping("/chemistry")
	public QisTransactionChemistry getTransactionLaboratoryCHEM(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View CH:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		return qisTransactionChemistry;
	}
}
