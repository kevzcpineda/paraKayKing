package quest.phil.diagnostic.information.system.ws.common;

import java.util.Calendar;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quest.phil.diagnostic.information.system.ws.controller.QisTransactionLaboratoryHematologyController;
import quest.phil.diagnostic.information.system.ws.controller.QisTransactionLaboratorySerologyController;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratoryItems;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionMicrobiology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabCBC;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.mb.QisTransactionLabGS;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabHERequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabMBRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHECBCRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.mb.QisTransactionLabGSRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisReferenceLaboratoryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionMIcrobiologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.mb.QisTransactionLabGSRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@Component
public class AppLaboratoryMBUtility {
	private final String CATEGORY = "MB";

	// MICROBIOLOGY
	@Autowired
	private QisTransactionMIcrobiologyRepository qisTransactionMicrobiologyRepository;

	@Autowired
	private QisTransactionLabGSRepository qisTransactionLabGSRepository;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	private QisReferenceLaboratoryRepository qisReferenceLaboratoryRepository;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;

	public void validateMicrobiologyRequest(Long laboratoryId, Set<QisLaboratoryProcedureService> serviceRequest,
			QisTransactionLabMBRequest mbRequest) throws Exception {

		if (mbRequest == null) {
			throw new RuntimeException("Microbiology request is required.",
					new Throwable("microbiology[" + laboratoryId + "]: Microbiology is required."));
		}

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(mbRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		for (QisLaboratoryProcedureService service : serviceRequest) {
			switch (service.getLaboratoryRequest().toString()) {
			case "GS": {
				if (mbRequest.getGs() == null) {
					throw new RuntimeException("Gram Stain is required.", new Throwable("gs: Gram Stain is required."));
				}

				QisTransactionLabGSRequest gsRequest = mbRequest.getGs();
				if (gsRequest.getResult() != null) {
					if (gsRequest.getResult() == null) {
						throw new RuntimeException("Invalid White Blood Cells value[" + gsRequest.getResult() + "].",
								new Throwable("gs.gramstain: Invalid White Blood Cells value[" + gsRequest.getResult()
										+ "]."));
					}
				}

				if (gsRequest.getSpecimen() != null) {
					if (gsRequest.getSpecimen() == null) {
						throw new RuntimeException("Invalid Specimen value[" + gsRequest.getSpecimen() + "].",
								new Throwable(
										"gs.gramstain: Invalid Specimen value[" + gsRequest.getSpecimen() + "]."));
					}
				}
			}
				break;
			}
		}
		
		
	}

	public void saveMicrobiology(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionMicrobiology qisTransactionMicrobiology, Set<QisLaboratoryProcedureService> serviceRequest,
			QisTransactionLabMBRequest mbRequest, QisUserDetails authUser, QisDoctor qisDoctor) {

		String referenceLab1 = "";
		if (mbRequest.getGs() != null) {
			referenceLab1 = mbRequest.getGs().getReferenceLabId();
		}

		for (QisLaboratoryProcedureService service : serviceRequest) {
			switch (service.getLaboratoryRequest().toString()) {
			case "GS": {
				QisTransactionLabGS gs = saveGS(qisTransaction, laboratoryId, mbRequest, authUser, referenceLab1,
						qisTransactionMicrobiology.getItemDetails().getItemid());
				if (gs != null) {
					qisTransactionMicrobiology.setGs(gs);
				}
			}
				break;
			}
		}

		int isUpdate = 0;
		if (!qisTransactionMicrobiology.isSubmitted()) {
			qisTransactionMicrobiology.setSubmitted(true);
			qisTransactionMicrobiology.setVerifiedBy(authUser.getId());
			qisTransactionMicrobiology.setVerifiedDate(Calendar.getInstance());
			isUpdate += 1;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					"SUBMITTED", qisTransactionMicrobiology.getItemDetails().getItemName(), laboratoryId, CATEGORY);
		}

		if (qisTransactionMicrobiology.getLabPersonelId() == null) {
			qisTransactionMicrobiology.setLabPersonelId(authUser.getId());
			qisTransactionMicrobiology.setLabPersonelDate(Calendar.getInstance());
			qisTransactionMicrobiology.setStatus(2);
			isUpdate += 2;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					"LAB PERSONEL", authUser.getUsername(), laboratoryId, CATEGORY);
		}

		String onLogMsg = "";
		String action = "ADDED";


		onLogMsg = "";
		action = "ADDED";
		if (qisTransactionMicrobiology.getMedicalDoctorId() == null) {
			qisTransactionMicrobiology.setMedicalDoctor(qisDoctor);
			qisTransactionMicrobiology.setMedicalDoctorId(qisDoctor.getId());
			onLogMsg = qisTransactionMicrobiology.getMedicalDoctor().getDoctorid();
		} else if (qisDoctor.getId() != qisTransactionMicrobiology.getMedicalDoctorId()) {
			qisTransactionMicrobiology.setMedicalDoctor(qisDoctor);
			qisTransactionMicrobiology.setMedicalDoctorId(qisDoctor.getId());

			action = "UPDATED";
			onLogMsg = appUtility.formatUpdateData(onLogMsg, "pathologist",
					qisTransactionMicrobiology.getMedicalDoctor().getDoctorid(), qisDoctor.getDoctorid());
		}

		if (!"".equals(onLogMsg)) {
			isUpdate += 8;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, onLogMsg, laboratoryId, CATEGORY + "-PATHOLOGIST");
		}

		if (isUpdate > 0) {
			qisTransactionMicrobiologyRepository.save(qisTransactionMicrobiology);

			QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
			if ((isUpdate & 1) > 0) {
				qisTransactionMicrobiology.setVerified(labPersonel);

			}
			if ((isUpdate & 2) > 0) {
				qisTransactionMicrobiology.setLabPersonel(labPersonel);
			}
		}
	}
	
	public void saveMicrobiologylogyQC(QisTransactionMicrobiology qisMicrobiology, Long laboratoryId, QisUserDetails authUser) {
		qisMicrobiology.setQcId(authUser.getId());
		qisMicrobiology.setQcDate(Calendar.getInstance());
		qisMicrobiology.setStatus(3); // Quality Control
		qisTransactionMicrobiologyRepository.save(qisMicrobiology);

		QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
		qisMicrobiology.setQualityControl(labPersonel);

		qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(), "UPDATE",
				"QC Serology", laboratoryId, CATEGORY);
	}

	private QisTransactionLabGS saveGS(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabMBRequest mbRequest, QisUserDetails authUser, String referenceLab1, String itemid) {

		String action = "ADDED";
		boolean isAdded = false;
		String gsLogMsg = "";
		QisTransactionLabGS gs = qisTransactionLabGSRepository.getTransactionLabGramStainByLabReqId(laboratoryId);

		QisTransactionLabGSRequest gsRequest = mbRequest.getGs();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(gsRequest.getReferenceLabId());

		QisTransactionLaboratoryDetails laboratoryDetails = null;
		laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

		if (laboratoryDetails != null) {
			if (referenceLab != null) {
				laboratoryDetails.setReferenceLab(referenceLab);
				laboratoryDetails.setReferenceLabId(referenceLab.getId());
				for (QisReferenceLaboratoryItems refItems : referenceLab.getCollectionItems()) {
					if (itemid.equals(refItems.getReferenceLabItems().getItemid())) {
						laboratoryDetails.setMolePriceItem(refItems.getMolePrice());
					}
				}
				qisTransactionLaboratoryDetailsRepository.save(laboratoryDetails);
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (gs == null) {
			gs = new QisTransactionLabGS();
			BeanUtils.copyProperties(mbRequest.getGs(), gs);
			gs.setId(laboratoryId);
			gs.setCreatedBy(authUser.getId());
			gs.setUpdatedBy(authUser.getId());

			if (gsRequest.getResult() != null) {
				gs.setResult(gsRequest.getResult());
			}

			if (gsRequest.getSpecimen() != null) {
				gs.setSpecimen(gsRequest.getSpecimen());
			}

			gsLogMsg = mbRequest.getGs().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			gs.setUpdatedBy(authUser.getId());
			gs.setUpdatedAt(Calendar.getInstance());
			
			if (gsRequest.getReferenceLabId() != null) {
				if (gs.getReferenceLab() != null) {
					if (gsRequest.getReferenceLabId() != gs.getReferenceLab().getReferenceid()) {
						gsLogMsg = appUtility.formatUpdateData(gsLogMsg, "Reference Laboratory",
								String.valueOf(gs.getReferenceLab().getReferenceid()),
								String.valueOf(gsRequest.getReferenceLabId()));
						if (referenceLab != null) {
							gs.setReferenceLab(referenceLab);
							gs.setReferenceLabId(referenceLab.getId());
						} else {
							gs.setReferenceLab(null);
							gs.setReferenceLabId(null);
						}
					}
				} else {
					gsLogMsg = appUtility.formatUpdateData(gsLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(gsRequest.getReferenceLabId()));
					if (referenceLab != null) {
						gs.setReferenceLab(referenceLab);
						gs.setReferenceLabId(referenceLab.getId());
					} else {
						gs.setReferenceLab(null);
						gs.setReferenceLabId(null);
					}
				}
			} else {
				if (gs.getReferenceLab() != null) {
					gsLogMsg = appUtility.formatUpdateData(gsLogMsg, "Reference Laboratory",
							String.valueOf(gs.getReferenceLab().getReferenceid()), null);
					gs.setReferenceLab(null);
					gs.setReferenceLabId(null);
				}
			}
			
			if (gsRequest.getSpecimen() != null) {
				if (!gsRequest.getSpecimen().equals(gs.getSpecimen())) {
					System.out.println("ASasd");
					gsLogMsg = appUtility.formatUpdateData(gsLogMsg, "Specimen",
							String.valueOf(gs.getSpecimen()), String.valueOf(gsRequest.getSpecimen()));
					gs.setSpecimen(gsRequest.getSpecimen());
				}

			} else {
				if (gs.getSpecimen() != null) {
					gsLogMsg = appUtility.formatUpdateData(gsLogMsg, "Specimen",
							String.valueOf(gs.getSpecimen()), null);
					gs.setSpecimen(null);
				}
			}
			
			if (gsRequest.getResult() != null) {
				if (!gsRequest.getResult().equals(gs.getResult())) {
					gsLogMsg = appUtility.formatUpdateData(gsLogMsg, "Result",
							String.valueOf(gs.getResult()), String.valueOf(gsRequest.getResult()));
					gs.setResult(gsRequest.getResult());
				}

			} else {
				if (gs.getResult() != null) {
					gsLogMsg = appUtility.formatUpdateData(gsLogMsg, "Result",
							String.valueOf(gs.getResult()), null);
					gs.setResult(null);
				}
			}
		}
		
		if (!"".equals(gsLogMsg)) {
			
			qisTransactionLabGSRepository.save(gs);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, gsLogMsg, laboratoryId, CATEGORY + "-Gram Stain");
		}

		if (isAdded) {
			return gs;
		}
		return null;
	}
}
