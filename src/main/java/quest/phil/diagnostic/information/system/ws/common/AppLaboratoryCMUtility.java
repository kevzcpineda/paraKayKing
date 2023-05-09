package quest.phil.diagnostic.information.system.ws.common;

import java.util.Calendar;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quest.phil.diagnostic.information.system.ws.controller.QisTransactionLaboratoryCMController;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratoryItems;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionClinicalMicroscopy;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabAFB;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabFecalysis;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabPTOBT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabUrineChemical;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabCMRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm.QisTransactionLabCMAFBRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm.QisTransactionLabCMFecaRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm.QisTransactionLabCMPTOBTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.cm.QisTransactionLabCMUChemRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisReferenceLaboratoryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionClinicalMicroscopyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;
import quest.phil.diagnostic.information.system.ws.repository.cm.QisTransactionLabAFBRepository;
import quest.phil.diagnostic.information.system.ws.repository.cm.QisTransactionLabFecalysisRepository;
import quest.phil.diagnostic.information.system.ws.repository.cm.QisTransactionLabPTOBTRepository;
import quest.phil.diagnostic.information.system.ws.repository.cm.QisTransactionLabUrineChemicalRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@Component
public class AppLaboratoryCMUtility {
	private final String CATEGORY = "CM";
	// CLINICAL MICROSCOPY
	@Autowired
	private QisTransactionClinicalMicroscopyRepository qisTransactionClinicalMicroscopyRepository;

	@Autowired
	private QisTransactionLabUrineChemicalRepository qisTransactionLabUrineChemicalRepository;

	@Autowired
	private QisTransactionLabFecalysisRepository qisTransactionLabFecalysisRepository;

	@Autowired
	private QisTransactionLabPTOBTRepository qisTransactionLabPTOBTRepository;

	@Autowired
	private QisTransactionLabAFBRepository qisTransactionLabAFBRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;
	
	@Autowired
	private QisReferenceLaboratoryRepository qisReferenceLaboratoryRepository;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;

	/*
	 * CLINICAL MICROSCOPY
	 */
	public void validateClinicalMicroscopyRequest(Long laboratoryId, Set<QisLaboratoryProcedureService> serviceRequest,
			QisTransactionLabCMRequest cmRequest) throws Exception {

		if (cmRequest == null) {
			throw new RuntimeException("Clinical Microscopy request is required.",
					new Throwable("clinicalMicroscopy[" + laboratoryId + "]: Clinical Microscopy is required."));
		}

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(cmRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		for (QisLaboratoryProcedureService service : serviceRequest) {
			switch (service.getLaboratoryRequest().toString()) {
			case "UCHEM": {
				if (cmRequest.getUrineChemical() == null) {
					throw new RuntimeException("Urine Chemical is required.",
							new Throwable("urineChemical: Urine Chemical is required."));
				}

				QisTransactionLabCMUChemRequest uchemRequest = cmRequest.getUrineChemical();
				if (uchemRequest.getColor() != null) {
					String color = appUtility.getMacroColor(uchemRequest.getColor());
					if (color == null) {
						throw new RuntimeException("Invalid color [" + uchemRequest.getColor() + "]",
								new Throwable("urineChemical.color: Invalid color [" + uchemRequest.getColor() + "]"));
					}
				}

				if (uchemRequest.getTransparency() != null) {
					String transparency = appUtility.getMacroTransparency(uchemRequest.getTransparency());
					if (transparency == null) {
						throw new RuntimeException("Invalid transparency [" + uchemRequest.getTransparency() + "]",
								new Throwable("urineChemical.transparency: Invalid transparency ["
										+ uchemRequest.getTransparency() + "]"));
					}
				}

				if (uchemRequest.geteCells() != null) {
					validateMicroOptions("E.Cells", "eCells", uchemRequest.geteCells());
				}
				if (uchemRequest.getmThreads() != null) {
					validateMicroOptions("M.Threads", "mThreads", uchemRequest.getmThreads());
				}
				if (uchemRequest.getBacteria() != null) {
					validateMicroOptions("Bacteria", "bacteria", uchemRequest.getBacteria());
				}
				if (uchemRequest.getAmorphous() != null) {
					validateMicroOptions("Amorphous", "amorphous", uchemRequest.getAmorphous());
				}
				if (uchemRequest.getCaOX() != null) {
					validateMicroOptions("CaOX", "caOX", uchemRequest.getCaOX());
				}

				if (uchemRequest.getPh() != null) {
					Float ph = appUtility.parseFloatAmount(uchemRequest.getPh());
					if (ph == null) {
						throw new RuntimeException("Invalid pH value[" + uchemRequest.getPh() + "].",
								new Throwable("urineChemical.pH: Invalid pH value[" + uchemRequest.getPh() + "]."));
					} else if (ph < 5f || ph > 9.5f) {
						throw new RuntimeException("Invalid pH value[" + uchemRequest.getPh() + "].",
								new Throwable("urineChemical.pH: Invalid pH value[" + uchemRequest.getPh() + "]."));
					}

				}
				if (uchemRequest.getSpGravity() != null) {
					Float spGravity = appUtility.parseFloatAmount(uchemRequest.getSpGravity());
					if (spGravity == null) {
						throw new RuntimeException("Invalid Sp.Gravity value[" + uchemRequest.getSpGravity() + "].",
								new Throwable("urineChemical.spGravity: Invalid Sp.Gravity value["
										+ uchemRequest.getSpGravity() + "]."));
					} else if (spGravity < 1f || spGravity > 1.03f) {
						throw new RuntimeException("Invalid Sp.Gravity value[" + uchemRequest.getSpGravity() + "].",
								new Throwable("urineChemical.spGravity: Invalid Sp.Gravity value["
										+ uchemRequest.getSpGravity() + "]."));
					}
				}
				if (uchemRequest.getProtien() != null) {
					validateUChemOptions("Protien", "protien", uchemRequest.getProtien());
				}
				if (uchemRequest.getGlucose() != null) {
					validateUChemOptions("Glucose", "glucose", uchemRequest.getGlucose());
				}
				if (uchemRequest.getLeukocyteEsterase() != null) {
					validateUChemOptions("Leukocyte Esterase", "leukocyteEsterase",
							uchemRequest.getLeukocyteEsterase());
				}
				if (uchemRequest.getUrobilinogen() != null) {
					validateUChemOptions("Urobilinogen", "urobilinogen", uchemRequest.getLeukocyteEsterase());
				}
				if (uchemRequest.getBlood() != null) {
					validateUChemOptions("Blood", "blood", uchemRequest.getBlood());
				}
				if (uchemRequest.getKetone() != null) {
					validateUChemOptions("Ketone", "ketone", uchemRequest.getKetone());
				}
				if (uchemRequest.getBilirubin() != null) {
					validateUChemOptions("Bilirubin", "bilirubin", uchemRequest.getBilirubin());
				}

			}
				break;
			case "FECA": {
				if (cmRequest.getFecalysis() == null) {
					throw new RuntimeException("Fecalysis is required.",
							new Throwable("fecalysis: Fecalysis is required."));
				}
				QisTransactionLabCMFecaRequest fecaRequest = cmRequest.getFecalysis();
				if (fecaRequest.getColor() != null) {
					String color = appUtility.getFecalysisColor(fecaRequest.getColor());
					if (color == null) {
						throw new RuntimeException("Invalid color [" + fecaRequest.getColor() + "]",
								new Throwable("fecalysis.color: Invalid color [" + fecaRequest.getColor() + "]"));
					}
				}

				if (fecaRequest.getConsistency() != null) {
					String consistency = appUtility.getFecalysisConsistency(fecaRequest.getConsistency());
					if (consistency == null) {
						throw new RuntimeException("Invalid consistency [" + fecaRequest.getConsistency() + "]",
								new Throwable("fecalysis.consistency: Invalid consistency ["
										+ fecaRequest.getConsistency() + "]"));
					}
				}
			}
				break;
			case "PREGT":
			case "OBT": {
			}
				break;
			case "AFB": {
				if (cmRequest.getAfb() == null) {
					throw new RuntimeException("Acid - Fast Bacilli ( AFB ) is required.",
							new Throwable("afb: Acid - Fast Bacilli ( AFB ) is required."));
				}
			}
				break;
			} // switch
		} // for loop
	}

	private void validateMicroOptions(String label, String variable, String option) throws Exception {
		String microOption = appUtility.getMicroOptions(option);
		if (microOption == null) {
			throw new RuntimeException("Invalid " + label + " [" + option + "]",
					new Throwable("urineChemical." + variable + ": Invalid " + label + " [" + option + "]"));
		}
	}

	private void validateUChemOptions(String label, String variable, String option) throws Exception {
		String urineOption = appUtility.getUrineChemOptions(option);
		if (urineOption == null) {
			throw new RuntimeException("Invalid " + label + " [" + option + "]",
					new Throwable("urineChemical." + variable + ": Invalid " + label + " [" + option + "]"));
		}
	}

	public void saveClinicalMicroscopy(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionClinicalMicroscopy qisTransactionClinicalMicroscopy,
			Set<QisLaboratoryProcedureService> serviceRequest, QisTransactionLabCMRequest cmRequest,
			QisUserDetails authUser, QisDoctor qisDoctor) {
		boolean isPTOBT = true;
		
		String referenceLab1 = "";
		if (cmRequest.getAfb() != null) {
			referenceLab1 = cmRequest.getAfb().getReferenceLabId();
		}
		if (cmRequest.getFecalysis() != null) {
			referenceLab1 = cmRequest.getFecalysis().getReferenceLabId();
		}
		if (cmRequest.getPtobt() != null) {
			referenceLab1 = cmRequest.getPtobt().getReferenceLabId();
		}
		if (cmRequest.getUrineChemical() != null) {
			referenceLab1 = cmRequest.getUrineChemical().getReferenceLabId();
		}
		
		for (QisLaboratoryProcedureService service : serviceRequest) {
			switch (service.getLaboratoryRequest().toString()) {
			case "UCHEM": {
				QisTransactionLabUrineChemical uchem = saveUrineChemical(qisTransaction, laboratoryId, cmRequest,
						authUser, referenceLab1);
				if (uchem != null) {
					qisTransactionClinicalMicroscopy.setUrineChemical(uchem);
				}
			}
				break;
			case "FECA": {
				QisTransactionLabFecalysis feca = saveFecalysis(qisTransaction, laboratoryId, cmRequest, authUser, referenceLab1,
						qisTransactionClinicalMicroscopy.getItemDetails().getItemid());
				if (feca != null) {
					qisTransactionClinicalMicroscopy.setFecalysis(feca);
				}
			}
				break;
			case "PREGT":
			case "OBT": {
				if (isPTOBT) {
					QisTransactionLabPTOBT ptobt = savePTOBT(qisTransaction, laboratoryId, cmRequest, authUser, referenceLab1,
							qisTransactionClinicalMicroscopy.getItemDetails().getItemid());
					if (ptobt != null) {
						qisTransactionClinicalMicroscopy.setPtobt(ptobt);
					}
					isPTOBT = false;
				}
			}
				break;
			case "AFB": {
				QisTransactionLabAFB afb = saveAFB(qisTransaction, laboratoryId, cmRequest, authUser, referenceLab1,
						qisTransactionClinicalMicroscopy.getItemDetails().getItemid());
				if (afb != null) {
					qisTransactionClinicalMicroscopy.setAfb(afb);
				}
			}
				break;
			}// switch
		} // for loop

		int isUpdate = 0;
		if (!qisTransactionClinicalMicroscopy.isSubmitted()) {
			qisTransactionClinicalMicroscopy.setSubmitted(true);
			qisTransactionClinicalMicroscopy.setVerifiedBy(authUser.getId());
			qisTransactionClinicalMicroscopy.setVerifiedDate(Calendar.getInstance());
			isUpdate += 1;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryCMController.class.getSimpleName(),
					"SUBMITTED", qisTransactionClinicalMicroscopy.getItemDetails().getItemName(), laboratoryId,
					CATEGORY);
		}

		if (qisTransactionClinicalMicroscopy.getLabPersonelId() == null) {
			qisTransactionClinicalMicroscopy.setLabPersonelId(authUser.getId());
			qisTransactionClinicalMicroscopy.setLabPersonelDate(Calendar.getInstance());
			qisTransactionClinicalMicroscopy.setStatus(2);
			isUpdate += 2;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryCMController.class.getSimpleName(),
					"LAB PERSONEL", authUser.getUsername(), laboratoryId, CATEGORY);
		}

		String onLogMsg = "";
		String action = "ADDED";
		if (cmRequest.getOtherNotes() != null) {
			if (qisTransactionClinicalMicroscopy.getOtherNotes() == null) {
				qisTransactionClinicalMicroscopy.setOtherNotes(cmRequest.getOtherNotes());
				onLogMsg = cmRequest.getOtherNotes();
			} else if (!cmRequest.getOtherNotes().equals(qisTransactionClinicalMicroscopy.getOtherNotes())) {
				qisTransactionClinicalMicroscopy.setOtherNotes(cmRequest.getOtherNotes());
				action = "UPDATED";
				onLogMsg = appUtility.formatUpdateData(onLogMsg, "OtherNotes",
						qisTransactionClinicalMicroscopy.getOtherNotes(), cmRequest.getOtherNotes());
			}
		} else {
			if (qisTransactionClinicalMicroscopy.getOtherNotes() != null) {
				qisTransactionClinicalMicroscopy.setOtherNotes(null);
				action = "UPDATED";
				onLogMsg = appUtility.formatUpdateData(onLogMsg, "OtherNotes",
						qisTransactionClinicalMicroscopy.getOtherNotes(), null);
			}
		}

		if (!"".equals(onLogMsg)) {
			isUpdate += 4;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryCMController.class.getSimpleName(), action,
					onLogMsg, laboratoryId, CATEGORY + "-OTHER NOTES");
		}

		onLogMsg = "";
		action = "ADDED";
		if (qisTransactionClinicalMicroscopy.getMedicalDoctorId() == null) {
			qisTransactionClinicalMicroscopy.setMedicalDoctor(qisDoctor);
			qisTransactionClinicalMicroscopy.setMedicalDoctorId(qisDoctor.getId());
			onLogMsg = qisTransactionClinicalMicroscopy.getMedicalDoctor().getDoctorid();
		} else if (qisDoctor.getId() != qisTransactionClinicalMicroscopy.getMedicalDoctorId()) {
			qisTransactionClinicalMicroscopy.setMedicalDoctor(qisDoctor);
			qisTransactionClinicalMicroscopy.setMedicalDoctorId(qisDoctor.getId());

			action = "UPDATED";
			onLogMsg = appUtility.formatUpdateData(onLogMsg, "pathologist",
					qisTransactionClinicalMicroscopy.getMedicalDoctor().getDoctorid(), qisDoctor.getDoctorid());
		}

		if (!"".equals(onLogMsg)) {
			isUpdate += 8;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryCMController.class.getSimpleName(), action,
					onLogMsg, laboratoryId, CATEGORY + "-PATHOLOGIST");
		}

		if (isUpdate > 0) {
			qisTransactionClinicalMicroscopyRepository.save(qisTransactionClinicalMicroscopy);

			QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
			if ((isUpdate & 1) > 0) {
				qisTransactionClinicalMicroscopy.setVerified(labPersonel);

			}
			if ((isUpdate & 2) > 0) {
				qisTransactionClinicalMicroscopy.setLabPersonel(labPersonel);
			}
		}
	}

	public void saveClinicalMicroscopyQC(QisTransactionClinicalMicroscopy qisClinicalMicroscopy, Long laboratoryId,
			QisUserDetails authUser) {
		qisClinicalMicroscopy.setQcId(authUser.getId());
		qisClinicalMicroscopy.setQcDate(Calendar.getInstance());
		qisClinicalMicroscopy.setStatus(3); // Quality Control
		qisTransactionClinicalMicroscopyRepository.save(qisClinicalMicroscopy);

		QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
		qisClinicalMicroscopy.setQualityControl(labPersonel);

		qisLogService.info(authUser.getId(), QisTransactionLaboratoryCMController.class.getSimpleName(), "UPDATE",
				"QC Clinical Microscopy", laboratoryId, CATEGORY);
	}

	private QisTransactionLabUrineChemical saveUrineChemical(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCMRequest cmRequest, QisUserDetails authUser, String referenceLab1) {
		String action = "ADDED";
		boolean isAdded = false;
		String urineLogMsg = "";
		QisTransactionLabUrineChemical urine = qisTransactionLabUrineChemicalRepository
				.getTransactionLabUrineChemicalByLabReqId(laboratoryId);

		QisTransactionLabCMUChemRequest uchemRequest = cmRequest.getUrineChemical();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(uchemRequest.getReferenceLabId());
		
		if (urine == null) {
			urine = new QisTransactionLabUrineChemical();
			BeanUtils.copyProperties(cmRequest.getUrineChemical(), urine);
			urine.setId(laboratoryId);
			urine.setCreatedBy(authUser.getId());
			urine.setUpdatedBy(authUser.getId());

			if (uchemRequest.getPh() != null) {
				Float ph = appUtility.parseFloatAmount(uchemRequest.getPh());
				urine.setPh(ph);
			}

			if (uchemRequest.getSpGravity() != null) {
				Float spGravity = appUtility.parseFloatAmount(uchemRequest.getSpGravity());
				urine.setSpGravity(spGravity);
			}
			
			if (referenceLab != null) {
				urine.setReferenceLab(referenceLab);
				urine.setReferenceLabId(referenceLab.getId());
			}

			urineLogMsg = cmRequest.getUrineChemical().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			urine.setUpdatedBy(authUser.getId());
			urine.setUpdatedAt(Calendar.getInstance());

			if (uchemRequest.getReferenceLabId() != null) {
				if (urine.getReferenceLab() != null) {
					if (uchemRequest.getReferenceLabId() != urine.getReferenceLab().getReferenceid()) {
						urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Reference Laboratory",
								String.valueOf(urine.getReferenceLab().getReferenceid()),
								String.valueOf(uchemRequest.getReferenceLabId()));
						if (referenceLab != null) {							
							urine.setReferenceLab(referenceLab);
							urine.setReferenceLabId(referenceLab.getId());
						}else {
							urine.setReferenceLab(null);
							urine.setReferenceLabId(null);
						}
					}
				} else {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(uchemRequest.getReferenceLabId()));
					urine.setReferenceLab(null);
					urine.setReferenceLabId(null);
				}
			} else {
				if (urine.getReferenceLab() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Reference Laboratory",
							String.valueOf(urine.getReferenceLab().getReferenceid()), null);
					urine.setReferenceLab(null);
					urine.setReferenceLabId(null);
				}
			}
			
			if (uchemRequest.getColor() != null) {
				if (!uchemRequest.getColor().equals(urine.getColor())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "color", urine.getColor(),
							uchemRequest.getColor());
					urine.setColor(uchemRequest.getColor());
				}
			} else {
				if (urine.getColor() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "color", urine.getColor(), null);
					urine.setColor(null);
				}
			}

			if (uchemRequest.getTransparency() != null) {
				if (!uchemRequest.getTransparency().equals(urine.getTransparency())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "transparency", urine.getTransparency(),
							uchemRequest.getTransparency());
					urine.setTransparency(uchemRequest.getTransparency());
				}
			} else {
				if (urine.getTransparency() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "transparency", urine.getTransparency(),
							null);
					urine.setTransparency(null);
				}
			}

			if (uchemRequest.getRBC() != null) {
				if (!uchemRequest.getRBC().equals(urine.getRBC())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "RBC", urine.getRBC(),
							uchemRequest.getRBC());
					urine.setRBC(uchemRequest.getRBC());
				}
			} else {
				if (urine.getRBC() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "RBC", urine.getRBC(), null);
					urine.setRBC(null);
				}
			}

			if (uchemRequest.getWBC() != null) {
				if (!uchemRequest.getWBC().equals(urine.getWBC())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "WBC", urine.getWBC(),
							uchemRequest.getWBC());
					urine.setWBC(uchemRequest.getWBC());
				}
			} else {
				if (urine.getWBC() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "WBC", urine.getWBC(), null);
					urine.setWBC(null);
				}
			}

			if (uchemRequest.geteCells() != null) {
				if (!uchemRequest.geteCells().equals(urine.geteCells())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "ECells", urine.geteCells(),
							uchemRequest.geteCells());
					urine.seteCells(uchemRequest.geteCells());
				}
			} else {
				if (urine.geteCells() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "ECells", urine.geteCells(), null);
					urine.seteCells(null);
				}
			}

			if (uchemRequest.getmThreads() != null) {
				if (!uchemRequest.getmThreads().equals(urine.getmThreads())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "MThreads", urine.getmThreads(),
							uchemRequest.getmThreads());
					urine.setmThreads(uchemRequest.getmThreads());
				}
			} else {
				if (urine.getmThreads() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "MThreads", urine.getmThreads(), null);
					urine.setmThreads(null);
				}
			}

			if (uchemRequest.getBacteria() != null) {
				if (!uchemRequest.getBacteria().equals(urine.getBacteria())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Bacteria", urine.getBacteria(),
							uchemRequest.getBacteria());
					urine.setBacteria(uchemRequest.getBacteria());
				}
			} else {
				if (urine.getBacteria() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Bacteria", urine.getBacteria(), null);
					urine.setBacteria(null);
				}
			}

			if (uchemRequest.getAmorphous() != null) {
				if (!uchemRequest.getAmorphous().equals(urine.getAmorphous())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Amorphous", urine.getAmorphous(),
							uchemRequest.getAmorphous());
					urine.setAmorphous(uchemRequest.getAmorphous());
				}
			} else {
				if (urine.getAmorphous() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Amorphous", urine.getAmorphous(), null);
					urine.setAmorphous(null);
				}
			}

			if (uchemRequest.getCaOX() != null) {
				if (!uchemRequest.getCaOX().equals(urine.getCaOX())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "CaOX", urine.getCaOX(),
							uchemRequest.getCaOX());
					urine.setCaOX(uchemRequest.getCaOX());
				}
			} else {
				if (urine.getAmorphous() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "CaOX", urine.getCaOX(), null);
					urine.setCaOX(null);
				}
			}

			if (uchemRequest.getPh() != null) {
				Float ph = appUtility.parseFloatAmount(uchemRequest.getPh());
				if (!ph.equals(urine.getPh())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "pH", String.valueOf(urine.getPh()),
							String.valueOf(ph));
					urine.setPh(ph);
				}

			} else {
				if (urine.getPh() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "pH", String.valueOf(urine.getPh()), null);
					urine.setPh(null);
				}
			}

			if (uchemRequest.getSpGravity() != null) {
				Float spGravity = appUtility.parseFloatAmount(uchemRequest.getSpGravity());
				if (!spGravity.equals(urine.getSpGravity())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "SpGravity",
							String.valueOf(urine.getSpGravity()), String.valueOf(spGravity));
					urine.setSpGravity(spGravity);
				}
			} else {
				if (urine.getSpGravity() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "SpGravity", String.valueOf(urine.getPh()),
							null);
					urine.setSpGravity(null);
				}
			}

			if (uchemRequest.getProtien() != null) {
				if (!uchemRequest.getProtien().equals(urine.getProtien())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Protien", urine.getProtien(),
							uchemRequest.getProtien());
					urine.setProtien(uchemRequest.getProtien());
				}
			} else {
				if (urine.getProtien() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Protien", urine.getProtien(), null);
					urine.setProtien(null);
				}
			}

			if (uchemRequest.getGlucose() != null) {
				if (!uchemRequest.getGlucose().equals(urine.getGlucose())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Glucose", urine.getGlucose(),
							uchemRequest.getGlucose());
					urine.setGlucose(uchemRequest.getGlucose());
				}
			} else {
				if (urine.getGlucose() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Glucose", urine.getGlucose(), null);
					urine.setGlucose(null);

				}
			}

			if (uchemRequest.getLeukocyteEsterase() != null) {
				if (!uchemRequest.getLeukocyteEsterase().equals(urine.getLeukocyteEsterase())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "LeukocyteEsterase",
							urine.getLeukocyteEsterase(), uchemRequest.getLeukocyteEsterase());
					urine.setLeukocyteEsterase(uchemRequest.getLeukocyteEsterase());
				}
			} else {
				if (urine.getLeukocyteEsterase() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "LeukocyteEsterase",
							urine.getLeukocyteEsterase(), null);
					urine.setLeukocyteEsterase(null);
				}
			}

			if (uchemRequest.getNitrite() != null) {
				if (!uchemRequest.getNitrite().equals(urine.getNitrite())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Nitrite",
							String.valueOf(urine.getNitrite()), String.valueOf(uchemRequest.getNitrite()));
					urine.setNitrite(uchemRequest.getNitrite());
				}
			} else {
				if (urine.getNitrite() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Nitrite",
							String.valueOf(urine.getNitrite()), null);
					urine.setNitrite(null);

				}
			}

			if (uchemRequest.getUrobilinogen() != null) {
				if (!uchemRequest.getUrobilinogen().equals(urine.getUrobilinogen())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Urobilinogen", urine.getUrobilinogen(),
							uchemRequest.getUrobilinogen());
					urine.setUrobilinogen(uchemRequest.getUrobilinogen());
				}
			} else {
				if (urine.getUrobilinogen() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Urobilinogen", urine.getUrobilinogen(),
							null);
					urine.setUrobilinogen(null);
				}
			}

			if (uchemRequest.getBlood() != null) {
				if (!uchemRequest.getBlood().equals(urine.getBlood())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Blood", urine.getBlood(),
							uchemRequest.getBlood());
					urine.setBlood(uchemRequest.getBlood());
				}
			} else {
				if (urine.getBlood() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Blood", urine.getBlood(), null);
					urine.setBlood(null);
				}
			}

			if (uchemRequest.getKetone() != null) {
				if (!uchemRequest.getKetone().equals(urine.getKetone())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Ketone", urine.getKetone(),
							uchemRequest.getKetone());
					urine.setKetone(uchemRequest.getKetone());
				}
			} else {
				if (urine.getKetone() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Ketone", urine.getKetone(), null);
					urine.setKetone(null);
				}
			}

			if (uchemRequest.getBilirubin() != null) {
				if (!uchemRequest.getBilirubin().equals(urine.getBilirubin())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Bilirubin", urine.getBilirubin(),
							uchemRequest.getBilirubin());
					urine.setBilirubin(uchemRequest.getBilirubin());
				}
			} else {
				if (urine.getBilirubin() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "Bilirubin", urine.getBilirubin(), null);
					urine.setBilirubin(null);
				}
			}

			if (uchemRequest.getOtherNotes() != null) {
				if (!uchemRequest.getOtherNotes().equals(urine.getOtherNotes())) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "OtherNotes", urine.getOtherNotes(),
							uchemRequest.getOtherNotes());
					urine.setOtherNotes(uchemRequest.getOtherNotes());
				}
			} else {
				if (urine.getOtherNotes() != null) {
					urineLogMsg = appUtility.formatUpdateData(urineLogMsg, "OtherNotes", urine.getOtherNotes(), null);
					urine.setOtherNotes(null);
				}
			}
		}

		if (!"".equals(urineLogMsg)) {
			qisTransactionLabUrineChemicalRepository.save(urine);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryCMController.class.getSimpleName(), action,
					urineLogMsg, laboratoryId, CATEGORY + "-URINECHEM");
		}

		if (isAdded) {
			return urine;
		}

		return null;

	}

	private QisTransactionLabFecalysis saveFecalysis(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCMRequest cmRequest, QisUserDetails authUser, String referenceLab1, String string) {
		String action = "ADDED";
		boolean isAdded = false;
		String fecaLogMsg = "";
		QisTransactionLabFecalysis fecalysis = qisTransactionLabFecalysisRepository
				.getTransactionLabFecalysisByLabReqId(laboratoryId);
		
		QisTransactionLabCMFecaRequest fecaRequest = cmRequest.getFecalysis();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(fecaRequest.getReferenceLabId());
		
		QisTransactionLaboratoryDetails laboratoryDetails = null;
		laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);
		
		if (laboratoryDetails != null) {
			if (referenceLab != null) { 
				laboratoryDetails.setReferenceLab(referenceLab);
				laboratoryDetails.setReferenceLabId(referenceLab.getId());
				for (QisReferenceLaboratoryItems refItems : referenceLab.getCollectionItems()) {
					if (string.equals(refItems.getReferenceLabItems().getItemid())) {
						laboratoryDetails.setMolePriceItem(refItems.getMolePrice());
					}
				}
				qisTransactionLaboratoryDetailsRepository.save(laboratoryDetails);
			}else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}
		
		if (fecalysis == null) {
			fecalysis = new QisTransactionLabFecalysis();
			BeanUtils.copyProperties(cmRequest.getFecalysis(), fecalysis);
			fecalysis.setId(laboratoryId);
			fecalysis.setCreatedBy(authUser.getId());
			fecalysis.setUpdatedBy(authUser.getId());
			fecaLogMsg = cmRequest.getFecalysis().toString();
			if (referenceLab != null) {
				fecalysis.setReferenceLab(referenceLab);
				fecalysis.setReferenceLabId(referenceLab.getId());
			}
			isAdded = true;
		} else {
			action = "UPDATED";
			fecalysis.setUpdatedBy(authUser.getId());
			fecalysis.setUpdatedAt(Calendar.getInstance());

			if (fecaRequest.getReferenceLabId() != null) {
				if (fecalysis.getReferenceLab() != null) {
					if (fecaRequest.getReferenceLabId() != fecalysis.getReferenceLab().getReferenceid()) {
						fecaLogMsg = appUtility.formatUpdateData(fecaLogMsg, "Reference Laboratory",
								String.valueOf(fecalysis.getReferenceLab().getReferenceid()),
								String.valueOf(fecaRequest.getReferenceLabId()));
						if (referenceLab != null) {							
							fecalysis.setReferenceLab(referenceLab);
							fecalysis.setReferenceLabId(referenceLab.getId());
						}else {
							fecalysis.setReferenceLab(null);
							fecalysis.setReferenceLabId(null);
						}
					}
				} else {
					fecaLogMsg = appUtility.formatUpdateData(fecaLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(fecaRequest.getReferenceLabId()));
					if (referenceLab != null) {
						fecalysis.setReferenceLab(referenceLab);
						fecalysis.setReferenceLabId(referenceLab.getId());
					} else {
						fecalysis.setReferenceLab(null);
						fecalysis.setReferenceLabId(null);
					}
				}
			} else {
				if (fecalysis.getReferenceLab() != null) {
					fecaLogMsg = appUtility.formatUpdateData(fecaLogMsg, "Reference Laboratory",
							String.valueOf(fecalysis.getReferenceLab().getReferenceid()), null);
					fecalysis.setReferenceLab(null);
					fecalysis.setReferenceLabId(null);
				}
			}
			
			if (fecaRequest.getColor() != null) {
				if (!fecaRequest.getColor().equals(fecalysis.getColor())) {
					fecaLogMsg = appUtility.formatUpdateData(fecaLogMsg, "color", fecalysis.getColor(),
							fecaRequest.getColor());
					fecalysis.setColor(fecaRequest.getColor());
				}
			} else {
				if (fecalysis.getColor() != null) {
					fecaLogMsg = appUtility.formatUpdateData(fecaLogMsg, "color", fecalysis.getColor(), null);
					fecalysis.setColor(null);
				}
			}

			if (fecaRequest.getConsistency() != null) {
				if (!fecaRequest.getConsistency().equals(fecalysis.getConsistency())) {
					fecaLogMsg = appUtility.formatUpdateData(fecaLogMsg, "consistency", fecalysis.getConsistency(),
							fecaRequest.getConsistency());
					fecalysis.setConsistency(fecaRequest.getConsistency());
				}
			} else {
				if (fecalysis.getConsistency() != null) {
					fecaLogMsg = appUtility.formatUpdateData(fecaLogMsg, "consistency", fecalysis.getConsistency(),
							null);
					fecalysis.setConsistency(null);
				}
			}

			if (fecaRequest.getMicroscopicFindings() != null) {
				if (!fecaRequest.getMicroscopicFindings().equals(fecalysis.getMicroscopicFindings())) {
					fecaLogMsg = appUtility.formatUpdateData(fecaLogMsg, "MicroscopicFindings",
							fecalysis.getMicroscopicFindings(), fecaRequest.getMicroscopicFindings());
					fecalysis.setMicroscopicFindings(fecaRequest.getMicroscopicFindings());
				}
			} else {
				if (fecalysis.getMicroscopicFindings() != null) {
					fecaLogMsg = appUtility.formatUpdateData(fecaLogMsg, "MicroscopicFindings",
							fecalysis.getMicroscopicFindings(), null);
					fecalysis.setMicroscopicFindings(null);
				}
			}

			if (fecaRequest.getOtherNotes() != null) {
				if (!fecaRequest.getOtherNotes().equals(fecalysis.getOtherNotes())) {
					fecaLogMsg = appUtility.formatUpdateData(fecaLogMsg, "OtherNotes", fecalysis.getOtherNotes(),
							fecaRequest.getOtherNotes());
					fecalysis.setOtherNotes(fecaRequest.getOtherNotes());
				}
			} else {
				if (fecalysis.getOtherNotes() != null) {
					fecaLogMsg = appUtility.formatUpdateData(fecaLogMsg, "OtherNotes", fecalysis.getOtherNotes(), null);
					fecalysis.setOtherNotes(null);
				}
			}

		}

		if (!"".equals(fecaLogMsg)) {
			qisTransactionLabFecalysisRepository.save(fecalysis);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryCMController.class.getSimpleName(), action,
					fecaLogMsg, laboratoryId, CATEGORY + "-FECALYSIS");
		}

		if (isAdded) {
			return fecalysis;
		}
		return null;
	}

	private QisTransactionLabPTOBT savePTOBT(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCMRequest cmRequest, QisUserDetails authUser, String referenceLab1, String string) {
		String action = "ADDED";
		boolean isAdded = false;
		String ptobtLogMsg = "";
		QisTransactionLabPTOBT ptobt = qisTransactionLabPTOBTRepository.getTransactionLabPTOBTByLabReqId(laboratoryId);
		if (ptobt == null) {
			ptobt = new QisTransactionLabPTOBT();
			BeanUtils.copyProperties(cmRequest.getPtobt(), ptobt);
			ptobt.setId(laboratoryId);
			ptobt.setCreatedBy(authUser.getId());
			ptobt.setUpdatedBy(authUser.getId());
			ptobtLogMsg = cmRequest.getPtobt().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			ptobt.setUpdatedBy(authUser.getId());
			ptobt.setUpdatedAt(Calendar.getInstance());

			QisTransactionLabCMPTOBTRequest ptobtRequest = cmRequest.getPtobt();

			if (ptobtRequest.getPregnancyTest() != null) {
				if (ptobtRequest.getPregnancyTest() != ptobt.getPregnancyTest()) {
					ptobtLogMsg = appUtility.formatUpdateData(ptobtLogMsg, "PregnancyTest",
							String.valueOf(ptobt.getPregnancyTest()), String.valueOf(ptobtRequest.getPregnancyTest()));
					ptobt.setPregnancyTest(ptobtRequest.getPregnancyTest());
				}
			} else {
				if (ptobt.getPregnancyTest() != null) {
					ptobtLogMsg = appUtility.formatUpdateData(ptobtLogMsg, "PregnancyTest",
							String.valueOf(ptobt.getPregnancyTest()), null);
					ptobt.setPregnancyTest(null);

				}
			}

			if (ptobtRequest.getOccultBloodTest() != null) {
				if (ptobtRequest.getOccultBloodTest() != ptobt.getOccultBloodTest()) {
					ptobtLogMsg = appUtility.formatUpdateData(ptobtLogMsg, "OccultBloodTest",
							String.valueOf(ptobt.getOccultBloodTest()),
							String.valueOf(ptobtRequest.getOccultBloodTest()));
					ptobt.setOccultBloodTest(ptobtRequest.getOccultBloodTest());
				}
			} else {
				if (ptobt.getOccultBloodTest() != null) {
					ptobtLogMsg = appUtility.formatUpdateData(ptobtLogMsg, "OccultBloodTest",
							String.valueOf(ptobt.getOccultBloodTest()), null);
					ptobt.setOccultBloodTest(null);
				}
			}
		}

		if (!"".equals(ptobtLogMsg)) {
			qisTransactionLabPTOBTRepository.save(ptobt);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryCMController.class.getSimpleName(), action,
					ptobtLogMsg, laboratoryId, CATEGORY + "-PTOBT");
		}

		if (isAdded) {
			return ptobt;
		}
		return null;
	}

	private QisTransactionLabAFB saveAFB(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCMRequest cmRequest, QisUserDetails authUser, String referenceLab1, String string) {
		String action = "ADDED";
		boolean isAdded = false;
		String afbLogMsg = "";
		QisTransactionLabAFB afb = qisTransactionLabAFBRepository.getTransactionLabAFBByLabReqId(laboratoryId);
		if (afb == null) {
			afb = new QisTransactionLabAFB();
			BeanUtils.copyProperties(cmRequest.getAfb(), afb);
			afb.setId(laboratoryId);
			afb.setCreatedBy(authUser.getId());
			afb.setUpdatedBy(authUser.getId());
			afbLogMsg = cmRequest.getAfb().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			afb.setUpdatedBy(authUser.getId());
			afb.setUpdatedAt(Calendar.getInstance());

			QisTransactionLabCMAFBRequest afbRequest = cmRequest.getAfb();

			if (afbRequest.getVisualAppearance1() != null) {
				if (!afbRequest.getVisualAppearance1().equals(afb.getVisualAppearance1())) {
					afbLogMsg = appUtility.formatUpdateData(afbLogMsg, "VisualAppearance1", afb.getVisualAppearance1(),
							afbRequest.getVisualAppearance1());
					afb.setVisualAppearance1(afbRequest.getVisualAppearance1());
				}
			} else {
				if (afb.getVisualAppearance1() != null) {
					afbLogMsg = appUtility.formatUpdateData(afbLogMsg, "VisualAppearance1", afb.getVisualAppearance1(),
							null);
					afb.setVisualAppearance1(null);
				}
			}

			if (afbRequest.getVisualAppearance2() != null) {
				if (!afbRequest.getVisualAppearance2().equals(afb.getVisualAppearance2())) {
					afbLogMsg = appUtility.formatUpdateData(afbLogMsg, "VisualAppearance2", afb.getVisualAppearance2(),
							afbRequest.getVisualAppearance2());
					afb.setVisualAppearance2(afbRequest.getVisualAppearance2());
				}
			} else {
				if (afb.getVisualAppearance2() != null) {
					afbLogMsg = appUtility.formatUpdateData(afbLogMsg, "VisualAppearance2", afb.getVisualAppearance2(),
							null);
					afb.setVisualAppearance2(null);
				}
			}

			if (afbRequest.getReading1() != null) {
				if (!afbRequest.getReading1().equals(afb.getReading1())) {
					afbLogMsg = appUtility.formatUpdateData(afbLogMsg, "Reading1", afb.getReading1(),
							afbRequest.getReading1());
					afb.setReading1(afbRequest.getReading1());
				}
			} else {
				if (afb.getReading1() != null) {
					afbLogMsg = appUtility.formatUpdateData(afbLogMsg, "Reading1", afb.getReading1(), null);
					afb.setReading1(null);
				}
			}

			if (afbRequest.getReading2() != null) {
				if (!afbRequest.getReading2().equals(afb.getReading2())) {
					afbLogMsg = appUtility.formatUpdateData(afbLogMsg, "Reading2", afb.getReading2(),
							afbRequest.getReading2());
					afb.setReading2(afbRequest.getReading2());
				}
			} else {
				if (afb.getReading2() != null) {
					afbLogMsg = appUtility.formatUpdateData(afbLogMsg, "Reading2", afb.getReading2(), null);
					afb.setReading2(null);
				}
			}

			if (afbRequest.getDiagnosis() != null) {
				if (!afbRequest.getDiagnosis().equals(afb.getDiagnosis())) {
					afbLogMsg = appUtility.formatUpdateData(afbLogMsg, "Diagnosis", afb.getDiagnosis(),
							afbRequest.getDiagnosis());
					afb.setDiagnosis(afbRequest.getDiagnosis());
				}
			} else {
				if (afb.getDiagnosis() != null) {
					afbLogMsg = appUtility.formatUpdateData(afbLogMsg, "Diagnosis", afb.getDiagnosis(), null);
					afb.setDiagnosis(null);
				}
			}
		}

		if (!"".equals(afbLogMsg)) {
			qisTransactionLabAFBRepository.save(afb);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryCMController.class.getSimpleName(), action,
					afbLogMsg, laboratoryId, CATEGORY + "-AFB");
		}

		if (isAdded) {
			return afb;
		}
		return null;
	}
}
