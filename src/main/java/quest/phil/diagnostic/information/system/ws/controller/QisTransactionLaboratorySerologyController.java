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

import quest.phil.diagnostic.information.system.ws.common.AppLaboratorySEUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabSERequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionSerologyRepository;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/laboratory/{laboratoryId}")
public class QisTransactionLaboratorySerologyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratorySerologyController.class);
	

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	private QisTransactionSerologyRepository qisTransactionSerologyRepository;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private AppLaboratorySEUtility appLaboratorySEUtility;

	@PostMapping("/serology")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionLaboratoryDetails saveTransactionLaboratorySERA(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @Valid @RequestBody QisTransactionLabSERequest seRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Save SE:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(seRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		
		Set<QisLaboratoryProcedureService> serviceRequest = qisTransactionSerology.getItemDetails().getServiceRequest();
		appLaboratorySEUtility.validateSerology(null, serviceRequest, seRequest);

		// SAVING
		appLaboratorySEUtility.saveSerology(qisTransaction, laboratoryId, qisTransactionSerology, serviceRequest,
				seRequest, authUser, qisDoctor);

		QisTransactionLaboratoryDetails laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

		return laboratoryDetails;
	}

	@PutMapping("/serology/qc")
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
	
		if (qisTransactionSerology.getLabPersonelId().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to check the same transaction.",
					new Throwable("You are not authorize to check the same transaction."));
		}

		if (qisTransactionSerology.getStatus() < 2) {
			throw new RuntimeException("Transaction Laboratory Request has no results.",
					new Throwable(qisTransactionSerology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request has no results."));
		}

		if (qisTransactionSerology.getQcId() == null) {
			appLaboratorySEUtility.saveSerologyQC(qisTransactionSerology, laboratoryId, authUser);
		} else {
			throw new RuntimeException("Transaction Laboratory Request was already Checked.",
					new Throwable(qisTransactionSerology.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request was already Checked."));
		}
		
		QisTransactionLaboratoryDetails laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

		return laboratoryDetails;
	}

		
	@GetMapping("/serology")
	public QisTransactionSerology getTransactionLaboratorySERA(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View SE:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
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

		return qisTransactionSerology;
	}

}
