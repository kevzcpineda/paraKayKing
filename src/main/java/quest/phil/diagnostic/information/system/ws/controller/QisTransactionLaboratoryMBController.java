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

import quest.phil.diagnostic.information.system.ws.common.AppLaboratoryMBUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionMicrobiology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabMBRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionMIcrobiologyRepository;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/laboratory/{laboratoryId}")
public class QisTransactionLaboratoryMBController {

	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratoryHematologyController.class);

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	AppLaboratoryMBUtility appLaboratoryMBUtility;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;

	@Autowired
	private QisTransactionMIcrobiologyRepository qisTransactionMicrobiologyRepository;

	@PostMapping("/microbiology")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionLaboratoryDetails saveTransactionLaboratoryHEMA(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @Valid @RequestBody QisTransactionLabMBRequest mbRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Save MB:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(mbRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		Set<QisLaboratoryProcedureService> serviceRequest = qisTransactionMicrobiology.getItemDetails()
				.getServiceRequest();
		appLaboratoryMBUtility.validateMicrobiologyRequest(null, serviceRequest, mbRequest);

		// SAVING
		appLaboratoryMBUtility.saveMicrobiology(qisTransaction, laboratoryId, qisTransactionMicrobiology,
				serviceRequest, mbRequest, authUser, qisDoctor);

		QisTransactionLaboratoryDetails laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

		return laboratoryDetails;
	}
	
	@PutMapping("/microbiology/qc")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionLaboratoryDetails saveTransactionLaboratorySERAQC(@PathVariable String transactionId,
			@PathVariable Long laboratoryId,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Save SE:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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
				|| !"SE".equals(qisTransactionMicrobiology.getItemDetails().getItemLaboratoryProcedure())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Microbiology.",
					new Throwable(qisTransactionMicrobiology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Microbiology."));
		}
	
		if (qisTransactionMicrobiology.getLabPersonelId().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to check the same transaction.",
					new Throwable("You are not authorize to check the same transaction."));
		}

		if (qisTransactionMicrobiology.getStatus() < 2) {
			throw new RuntimeException("Transaction Laboratory Request has no results.",
					new Throwable(qisTransactionMicrobiology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request has no results."));
		}

		if (qisTransactionMicrobiology.getQcId() == null) {
			appLaboratoryMBUtility.saveMicrobiologylogyQC(qisTransactionMicrobiology, laboratoryId, authUser);
		} else {
			throw new RuntimeException("Transaction Laboratory Request was already Checked.",
					new Throwable(qisTransactionMicrobiology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request was already Checked."));
		}
		
		QisTransactionLaboratoryDetails laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

		return laboratoryDetails;
	}

	@GetMapping("/microbiology")
	public QisTransactionMicrobiology getTransactionLaboratoryHEMA(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View MB:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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
				|| !"HE".equals(qisTransactionMicrobiology.getItemDetails().getItemLaboratoryProcedure())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Microbiology.",
					new Throwable(qisTransactionMicrobiology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request is not for Microbiology."));
		}

		return qisTransactionMicrobiology;
	}
}
