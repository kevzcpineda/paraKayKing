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

import quest.phil.diagnostic.information.system.ws.common.AppLaboratoryHEUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabHERequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionHematologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/laboratory/{laboratoryId}")
public class QisTransactionLaboratoryHematologyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratoryHematologyController.class);

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	private QisTransactionHematologyRepository qisTransactionHematologyRepository;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private AppLaboratoryHEUtility appLaboratoryHEUtility;

	@PostMapping("/hematology")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionLaboratoryDetails saveTransactionLaboratoryHEMA(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @Valid @RequestBody QisTransactionLabHERequest heRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Save HE:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(heRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		Set<QisLaboratoryProcedureService> serviceRequest = qisTransactionHematology.getItemDetails()
				.getServiceRequest();
		appLaboratoryHEUtility.validateHematologyRequest(null, serviceRequest, heRequest);

		// SAVING
		appLaboratoryHEUtility.saveHematology(qisTransaction, laboratoryId, qisTransactionHematology, serviceRequest,
				heRequest, authUser, qisDoctor);

		QisTransactionLaboratoryDetails laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

		return laboratoryDetails;
	}

	@PutMapping("/hematology/qc")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionLaboratoryDetails saveTransactionLaboratoryHEMAQC(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Save HE QC:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		if (qisTransactionHematology.getLabPersonelId().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to check the same transaction.",
					new Throwable("You are not authorize to check the same transaction."));
		}

		if (qisTransactionHematology.getStatus() < 2) {
			throw new RuntimeException("Transaction Laboratory Request has no results.",
					new Throwable(qisTransactionHematology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request has no results."));
		}

		if (qisTransactionHematology.getQcId() == null) {
			appLaboratoryHEUtility.saveHematologyQC(qisTransactionHematology, laboratoryId, authUser);
		} else {
			throw new RuntimeException("Transaction Laboratory Request was already Checked.",
					new Throwable(qisTransactionHematology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request was already Checked."));
		}

		QisTransactionLaboratoryDetails laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

		return laboratoryDetails;
	}

	@GetMapping("/hematology")
	public QisTransactionHematology getTransactionLaboratoryHEMA(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View HE:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		return qisTransactionHematology;
	}

}
