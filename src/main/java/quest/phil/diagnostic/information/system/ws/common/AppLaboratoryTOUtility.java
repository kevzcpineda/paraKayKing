package quest.phil.diagnostic.information.system.ws.common;

import java.util.Calendar;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quest.phil.diagnostic.information.system.ws.controller.QisTransactionLaboratoryToxicologyController;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionToxicology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.toxi.QisTransactionLabToxicology;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabToxicologyRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionToxicologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.toxi.QisTransactionLabToxicologyRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@Component
public class AppLaboratoryTOUtility {
	private final String CATEGORY = "TO";

	// TOXICOLOGY
	@Autowired
	private QisTransactionToxicologyRepository qisTransactionToxicologyRepository;

	@Autowired
	private QisTransactionLabToxicologyRepository qisTransactionLabToxicologyRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	/*
	 * TOXICOLOGY
	 */
	public void validateToxicology(Long laboratoryId, Set<QisLaboratoryProcedureService> serviceRequest,
			QisTransactionLabToxicologyRequest toRequest) throws Exception {

		if (toRequest == null) {
			throw new RuntimeException("Toxicology request is required.",
					new Throwable("toxicology[" + laboratoryId + "]: Toxicology is required."));
		}

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(toRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}
	}

	public void saveToxicology(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionToxicology qisTransactionToxicology, QisTransactionLabToxicologyRequest toxicologyRequest,
			QisUserDetails authUser, QisDoctor qisDoctor) {
		String action = "ADDED";
		boolean isAddedToxicology = false;
		String toxicologyLogMsg = "";

		QisTransactionLabToxicology toxicology = qisTransactionLabToxicologyRepository
				.getTransactionLabToxicologyByLabReqId(laboratoryId);
		if (toxicology == null) {
			toxicology = new QisTransactionLabToxicology();
			BeanUtils.copyProperties(toxicologyRequest, toxicology);
			toxicology.setId(laboratoryId);
			toxicology.setCreatedBy(authUser.getId());
			toxicology.setUpdatedBy(authUser.getId());
			toxicologyLogMsg = toxicologyRequest.toString();
			isAddedToxicology = true;
		} else {
			action = "UPDATED";
			toxicology.setUpdatedBy(authUser.getId());
			toxicology.setUpdatedAt(Calendar.getInstance());

			if (toxicologyRequest.getMethamphethamine() != toxicology.getMethamphethamine()) {
				toxicologyLogMsg = appUtility.formatUpdateData(toxicologyLogMsg, "methamphethamine",
						String.valueOf(toxicology.getMethamphethamine()),
						String.valueOf(toxicologyRequest.getMethamphethamine()));
				toxicology.setMethamphethamine(toxicologyRequest.getMethamphethamine());
			}

			if (toxicologyRequest.getTetrahydrocanabinol() != toxicology.getTetrahydrocanabinol()) {
				toxicologyLogMsg = appUtility.formatUpdateData(toxicologyLogMsg, "tetrahydrocanabinol",
						String.valueOf(toxicology.getTetrahydrocanabinol()),
						String.valueOf(toxicologyRequest.getTetrahydrocanabinol()));
				toxicology.setTetrahydrocanabinol(toxicologyRequest.getTetrahydrocanabinol());
			}
		}

		if (!"".equals(toxicologyLogMsg)) {
			qisTransactionLabToxicologyRepository.save(toxicology);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryToxicologyController.class.getSimpleName(),
					action, toxicologyLogMsg, laboratoryId, CATEGORY);
		}

		int isUpdate = 0;
		if (!qisTransactionToxicology.isSubmitted()) {
			qisTransactionToxicology.setSubmitted(true);
			qisTransactionToxicology.setVerifiedBy(authUser.getId());
			qisTransactionToxicology.setVerifiedDate(Calendar.getInstance());
			isUpdate += 1;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryToxicologyController.class.getSimpleName(),
					"SUBMITTED", qisTransactionToxicology.getItemDetails().getItemName(), laboratoryId, CATEGORY);
		}

		if (qisTransactionToxicology.getLabPersonelId() == null) {
			qisTransactionToxicology.setLabPersonelId(authUser.getId());
			qisTransactionToxicology.setLabPersonelDate(Calendar.getInstance());
			qisTransactionToxicology.setStatus(2);
			isUpdate += 2;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryToxicologyController.class.getSimpleName(),
					"LAB PERSONEL", authUser.getUsername(), laboratoryId, CATEGORY);
		}

		String onLogMsg = "";
		action = "ADDED";
		if (qisTransactionToxicology.getMedicalDoctorId() == null) {
			qisTransactionToxicology.setMedicalDoctor(qisDoctor);
			qisTransactionToxicology.setMedicalDoctorId(qisDoctor.getId());
			onLogMsg = qisTransactionToxicology.getMedicalDoctor().getDoctorid();
		} else if (qisDoctor.getId() != qisTransactionToxicology.getMedicalDoctorId()) {
			qisTransactionToxicology.setMedicalDoctor(qisDoctor);
			qisTransactionToxicology.setMedicalDoctorId(qisDoctor.getId());

			action = "UPDATED";
			onLogMsg = appUtility.formatUpdateData(onLogMsg, "pathologist",
					qisTransactionToxicology.getMedicalDoctor().getDoctorid(), qisDoctor.getDoctorid());
		}

		if (!"".equals(onLogMsg)) {
			isUpdate += 4;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryToxicologyController.class.getSimpleName(),
					action, onLogMsg, laboratoryId, CATEGORY + "-PATHOLOGIST");
		}

		if (isUpdate > 0) {
			qisTransactionToxicologyRepository.save(qisTransactionToxicology);

			QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
			if ((isUpdate & 1) > 0) {
				qisTransactionToxicology.setVerified(labPersonel);

			}
			if ((isUpdate & 2) > 0) {
				qisTransactionToxicology.setLabPersonel(labPersonel);
			}
		}

		if (isAddedToxicology) {
			qisTransactionToxicology.setToxicology(toxicology);
		}
	}

	public void saveToxicologyQC(QisTransactionToxicology qisToxicology, Long laboratoryId, QisUserDetails authUser) {
		qisToxicology.setQcId(authUser.getId());
		qisToxicology.setQcDate(Calendar.getInstance());
		qisToxicology.setStatus(3); // Quality Control
		qisTransactionToxicologyRepository.save(qisToxicology);

		QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
		qisToxicology.setQualityControl(labPersonel);

		qisLogService.info(authUser.getId(), QisTransactionLaboratoryToxicologyController.class.getSimpleName(), "UPDATE",
				"QC Toxicology", laboratoryId, CATEGORY);
	}
}
