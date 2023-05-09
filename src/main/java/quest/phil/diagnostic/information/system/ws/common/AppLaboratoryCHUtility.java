package quest.phil.diagnostic.information.system.ws.common;

import java.util.Calendar;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quest.phil.diagnostic.information.system.ws.controller.QisTransactionLaboratoryChemistryController;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratoryItems;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionChemistry;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabBUN;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabBilirubin;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabCPK;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabCreatinine;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabElectrolytes;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabEnzymes;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabFBS;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabHemoglobin;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabLipidProfile;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabOGCT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabOGTT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabPPRBS;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabProtein;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabRBS;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabTIBC;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabUricAcid;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabCHRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHBUNRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHBiliRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHCPKRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHCreaRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHElecRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHEnzyRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHFBSRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHHemoRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHLippRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHOGCTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHOGTTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHPPRBSRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHProtRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHRBSRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransactionLabCHUrAcRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.ch.QisTransationLabCHTIBCRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisReferenceLaboratoryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionChemistryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabBUNRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabBilirubinRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabCPKRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabCreatinineRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabElectrolytesRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabEnzymesRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabFBSRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabHemoRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabLipPRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabOGCTRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabOGTTRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabPPRBSRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabProteinRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabRBSRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabTIBCRepository;
import quest.phil.diagnostic.information.system.ws.repository.ch.QisTransactionLabUricAcidRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@Component
public class AppLaboratoryCHUtility {
	private final String CATEGORY = "CH";
	// CHEMISTRY
	@Autowired
	private QisTransactionChemistryRepository qisTransactionChemistryRepository;

	@Autowired
	private QisTransactionLabFBSRepository qisTransactionLabFBSRepository;

	@Autowired
	private QisTransactionLabRBSRepository qisTransactionLabRBSRepository;

	@Autowired
	private QisTransactionLabPPRBSRepository qisTransactionLabPPRBSRepository;

	@Autowired
	private QisTransactionLabUricAcidRepository qisTransactionLabUricAcidRepository;

	@Autowired
	private QisTransactionLabBUNRepository qisTransactionLabBUNRepository;

	@Autowired
	private QisTransactionLabCreatinineRepository qisTransactionLabCreatinineRepository;

	@Autowired
	private QisTransactionLabHemoRepository qisTransactionLabHemoRepository;

	@Autowired
	private QisTransactionLabLipPRepository qisTransactionLabLipPRepository;

	@Autowired
	private QisTransactionLabOGTTRepository qisTransactionLabOGTTRepository;

	@Autowired
	private QisTransactionLabOGCTRepository qisTransactionLabOGCTRepository;

	@Autowired
	private QisTransactionLabElectrolytesRepository qisTransactionLabElectrolytesRepository;

	@Autowired
	private QisTransactionLabEnzymesRepository qisTransactionLabEnzymesRepository;

	@Autowired
	private QisTransactionLabCPKRepository qisTransactionLabCPKRepository;

	@Autowired
	private QisTransactionLabBilirubinRepository qisTransactionLabBilirubinRepository;

	@Autowired
	private QisTransactionLabProteinRepository qisTransactionLabProteinRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisReferenceLaboratoryRepository qisReferenceLaboratoryRepository;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;

	@Autowired
	private QisTransactionLabTIBCRepository qisTransactionLabTIBCRepository;

	/*
	 * CHEMISTRY
	 */
	public void validateChemistry(Long laboratoryId, Set<QisLaboratoryProcedureService> serviceRequest,
			QisTransactionLabCHRequest chRequest) throws Exception {

		if (chRequest == null) {
			throw new RuntimeException("Chemistry request is required.",
					new Throwable("chemistry[" + laboratoryId + "]: Chemistry is required."));
		}

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(chRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		for (QisLaboratoryProcedureService service : serviceRequest) {
			switch (service.getLaboratoryRequest().toString()) {
			case "FBS": {
				if (chRequest.getFastingBloodSugar() == null) {
					throw new RuntimeException("Fasting Blood Sugar is required.",
							new Throwable("fastingBloodSugar: Fasting Blood Sugar is required."));
				}
				QisTransactionLabCHFBSRequest fbsRequest = chRequest.getFastingBloodSugar();
				if (fbsRequest.getFbs() != null) {
					Float fbs = appUtility.parseFloatAmount(fbsRequest.getFbs());
					if (fbs == null) {
						throw new RuntimeException("Invalid FBS Rate value[" + fbsRequest.getFbs() + "].",
								new Throwable(
										"fastingBloodSugar.fbs: Invalid FBS Rate value[" + fbsRequest.getFbs() + "]."));
					}
				}

				if (fbsRequest.getConventional() != null) {
					Float conventional = appUtility.parseFloatAmount(fbsRequest.getConventional());
					if (conventional == null) {
						throw new RuntimeException(
								"Invalid FBS conventional value[" + fbsRequest.getConventional() + "].",
								new Throwable("fastingBloodSugar.conventional: Invalid FBS conventional value["
										+ fbsRequest.getConventional() + "]."));
					}
				}
			}
				break;
			case "RBS": {
				if (chRequest.getRandomBloodSugar() == null) {
					throw new RuntimeException("Random Blood Sugar is required.",
							new Throwable("randomBloodSugar: Random Blood Sugar is required."));
				}
				QisTransactionLabCHRBSRequest rbsRequest = chRequest.getRandomBloodSugar();
				if (rbsRequest.getRbs() != null) {
					Float rbs = appUtility.parseFloatAmount(rbsRequest.getRbs());
					if (rbs == null) {
						throw new RuntimeException("Invalid RBS Rate value[" + rbsRequest.getRbs() + "].",
								new Throwable(
										"randomBloodSugar.rbs: Invalid RBS Rate value[" + rbsRequest.getRbs() + "]."));
					}
				}

				if (rbsRequest.getConventional() != null) {
					Float conventional = appUtility.parseFloatAmount(rbsRequest.getConventional());
					if (conventional == null) {
						throw new RuntimeException(
								"Invalid RBS conventional value[" + rbsRequest.getConventional() + "].",
								new Throwable("randomBloodSugar.conventional: Invalid RBS conventional value["
										+ rbsRequest.getConventional() + "]."));
					}
				}
			}
				break;
			case "PPRBS": {
				if (chRequest.getPprbs() == null) {
					throw new RuntimeException("Post Prandial Random Blood Sugar is required.",
							new Throwable("pprbs: Post Prandial Random Blood Sugar is required."));
				}
				QisTransactionLabCHPPRBSRequest pprbsRequest = chRequest.getPprbs();
				if (pprbsRequest.getPprbs() != null) {
					Float pprbs = appUtility.parseFloatAmount(pprbsRequest.getPprbs());
					if (pprbs == null) {
						throw new RuntimeException("Invalid PPRBS Rate value[" + pprbsRequest.getPprbs() + "].",
								new Throwable(
										"pprbs.pprbs: Invalid PPRBS Rate value[" + pprbsRequest.getPprbs() + "]."));
					}
				}

				if (pprbsRequest.getConventional() != null) {
					Float conventional = appUtility.parseFloatAmount(pprbsRequest.getConventional());
					if (conventional == null) {
						throw new RuntimeException(
								"Invalid PPRBS conventional value[" + pprbsRequest.getConventional() + "].",
								new Throwable("pprbs.conventional: Invalid PPRBS conventional value["
										+ pprbsRequest.getConventional() + "]."));
					}
				}
			}
				break;
			case "URAC": {
				if (chRequest.getUricAcid() == null) {
					throw new RuntimeException("Uric Acid is required.",
							new Throwable("uricAcid: Uric Acid is required."));
				}
				QisTransactionLabCHUrAcRequest uaRequest = chRequest.getUricAcid();
				if (uaRequest.getUricAcid() != null) {
					Float ua = appUtility.parseFloatAmount(uaRequest.getUricAcid());
					if (ua == null) {
						throw new RuntimeException("Invalid Uric Acid Rate value[" + uaRequest.getUricAcid() + "].",
								new Throwable("uricAcid.uricAcid: Invalid Uric Acid Rate value["
										+ uaRequest.getUricAcid() + "]."));
					}
				}

				if (uaRequest.getConventional() != null) {
					Float conventional = appUtility.parseFloatAmount(uaRequest.getConventional());
					if (conventional == null) {
						throw new RuntimeException(
								"Invalid Uric Acid conventional value[" + uaRequest.getConventional() + "].",
								new Throwable("uricAcid.conventional: Invalid Uric Acid conventional value["
										+ uaRequest.getConventional() + "]."));
					}
				}
			}
				break;
			case "BUN": {
				if (chRequest.getBloodUreaNitrogen() == null) {
					throw new RuntimeException("Blood Urea Nitrogen is required.",
							new Throwable("bloodUreaNitrogen: Blood Urea Nitrogen is required."));
				}
				QisTransactionLabCHBUNRequest bunRequest = chRequest.getBloodUreaNitrogen();
				if (bunRequest.getBun() != null) {
					Float bun = appUtility.parseFloatAmount(bunRequest.getBun());
					if (bun == null) {
						throw new RuntimeException(
								"Invalid Blood Urea Nitrogen Rate value[" + bunRequest.getBun() + "].",
								new Throwable("bloodUreaNitrogen.bun: Blood Urea Nitrogen Acid Rate value["
										+ bunRequest.getBun() + "]."));
					}
				}

				if (bunRequest.getConventional() != null) {
					Float conventional = appUtility.parseFloatAmount(bunRequest.getConventional());
					if (conventional == null) {
						throw new RuntimeException(
								"Invalid Blood Urea Nitrogen conventional value[" + bunRequest.getConventional() + "].",
								new Throwable(
										"bloodUreaNitrogen.conventional: Invalid Blood Urea Nitrogen conventional value["
												+ bunRequest.getConventional() + "]."));
					}
				}
			}
				break;
			case "CREA": {
				if (chRequest.getCreatinine() == null) {
					throw new RuntimeException("Creatinine is required.",
							new Throwable("creatinine: Creatinine is required."));
				}
				QisTransactionLabCHCreaRequest cRequest = chRequest.getCreatinine();
				if (cRequest.getCreatinine() != null) {
					Float crea = appUtility.parseFloatAmount(cRequest.getCreatinine());
					if (crea == null) {
						throw new RuntimeException("Invalid Creatinine Rate value[" + cRequest.getCreatinine() + "].",
								new Throwable("creatinine.creatinine: Creatinine Rate value[" + cRequest.getCreatinine()
										+ "]."));
					}
				}

				if (cRequest.getConventional() != null) {
					Float conventional = appUtility.parseFloatAmount(cRequest.getConventional());
					if (conventional == null) {
						throw new RuntimeException(
								"Invalid Creatinine conventional value[" + cRequest.getConventional() + "].",
								new Throwable("creatinine.conventional: Invalid Creatinine conventional value["
										+ cRequest.getConventional() + "]."));
					}
				}
			}
				break;
			case "HBA1C": {
				if (chRequest.getHemoglobin() == null) {
					throw new RuntimeException("Hemoglobin is required.",
							new Throwable("hemoglobin: Hemoglobin is required."));
				}
				QisTransactionLabCHHemoRequest hemoRequest = chRequest.getHemoglobin();
				if (hemoRequest.getHemoglobinA1C() != null) {
					Float value = appUtility.parseFloatAmount(hemoRequest.getHemoglobinA1C());
					if (value == null) {
						throw new RuntimeException(
								"Invalid HemoglobinA1C value[" + hemoRequest.getHemoglobinA1C() + "].",
								new Throwable("hemoglobin.hemoglobinA1C: Invalid HemoglobinA1C value["
										+ hemoRequest.getHemoglobinA1C() + "]."));
					}
				}
			}
				break;
			case "LIPP": {
				if (chRequest.getLipidProfile() == null) {
					throw new RuntimeException("Lipid Profile is required.",
							new Throwable("lipidProfile: Lipid Profile is required."));
				}
				QisTransactionLabCHLippRequest lpRequest = chRequest.getLipidProfile();
				if (lpRequest.getCholesterol() != null) {
					Float value = appUtility.parseFloatAmount(lpRequest.getCholesterol());
					if (value == null) {
						throw new RuntimeException("Invalid Cholesterol value[" + lpRequest.getCholesterol() + "].",
								new Throwable("lipidProfile.cholesterol: Invalid Cholesterol value["
										+ lpRequest.getCholesterol() + "]."));
					}
				}
				if (lpRequest.getCholesterolConventional() != null) {
					Float value = appUtility.parseFloatAmount(lpRequest.getCholesterolConventional());
					if (value == null) {
						throw new RuntimeException(
								"Invalid CholesterolConventional value[" + lpRequest.getCholesterolConventional()
										+ "].",
								new Throwable(
										"lipidProfile.cholesterolConventional: Invalid CholesterolConventional value["
												+ lpRequest.getCholesterolConventional() + "]."));
					}
				}
				if (lpRequest.getTriglycerides() != null) {
					Float value = appUtility.parseFloatAmount(lpRequest.getTriglycerides());
					if (value == null) {
						throw new RuntimeException("Invalid Triglycerides value[" + lpRequest.getTriglycerides() + "].",
								new Throwable("lipidProfile.triglycerides: Invalid Triglycerides value["
										+ lpRequest.getTriglycerides() + "]."));
					}
				}
				if (lpRequest.getTriglyceridesConventional() != null) {
					Float value = appUtility.parseFloatAmount(lpRequest.getTriglyceridesConventional());
					if (value == null) {
						throw new RuntimeException(
								"Invalid TriglyceridesConventional value[" + lpRequest.getTriglyceridesConventional()
										+ "].",
								new Throwable(
										"lipidProfile.triglyceridesConventional: Invalid TriglyceridesConventional value["
												+ lpRequest.getTriglyceridesConventional() + "]."));
					}
				}
				if (lpRequest.getHdl() != null) {
					Float value = appUtility.parseFloatAmount(lpRequest.getHdl());
					if (value == null) {
						throw new RuntimeException("Invalid HDL value[" + lpRequest.getHdl() + "].",
								new Throwable("lipidProfile.hdl: Invalid HDL value[" + lpRequest.getHdl() + "]."));
					}
				}
				if (lpRequest.getHdlConventional() != null) {
					Float value = appUtility.parseFloatAmount(lpRequest.getHdlConventional());
					if (value == null) {
						throw new RuntimeException(
								"Invalid HDL Conventional value[" + lpRequest.getHdlConventional() + "].",
								new Throwable("lipidProfile.hdlConventional: Invalid HDL Conventional value["
										+ lpRequest.getHdlConventional() + "]."));
					}
				}
				if (lpRequest.getLdl() != null) {
					Float value = appUtility.parseFloatAmount(lpRequest.getLdl());
					if (value == null) {
						throw new RuntimeException("Invalid LDL value[" + lpRequest.getLdl() + "].",
								new Throwable("lipidProfile.ldl: Invalid LDL value[" + lpRequest.getLdl() + "]."));
					}
				}
				if (lpRequest.getLdlConventional() != null) {
					Float value = appUtility.parseFloatAmount(lpRequest.getLdlConventional());
					if (value == null) {
						throw new RuntimeException(
								"Invalid LDL Conventional value[" + lpRequest.getLdlConventional() + "].",
								new Throwable("lipidProfile.ldlConventional: Invalid LDL Conventional value["
										+ lpRequest.getLdlConventional() + "]."));
					}
				}
				if (lpRequest.getHdlRatio() != null) {
					Float value = appUtility.parseFloatAmount(lpRequest.getHdlRatio());
					if (value == null) {
						throw new RuntimeException("Invalid HDL Ratio value[" + lpRequest.getHdlRatio() + "].",
								new Throwable("lipidProfile.hdlRatio: Invalid Ratio HDL value["
										+ lpRequest.getHdlRatio() + "]."));
					}
				}
				if (lpRequest.getVldl() != null) {
					Float value = appUtility.parseFloatAmount(lpRequest.getVldl());
					if (value == null) {
						throw new RuntimeException("Invalid VLDL value[" + lpRequest.getVldl() + "].",
								new Throwable("lipidProfile.vldl: Invalid VLDL value[" + lpRequest.getVldl() + "]."));
					}
				}
			}
				break;
			case "OGTT": {
				if (chRequest.getOgtt() == null) {
					throw new RuntimeException("Oral Glucose Tolerance Test (OGTT) is required.",
							new Throwable("ogtt: Oral Glucose Tolerance Test (OGTT) is required."));
				}
				QisTransactionLabCHOGTTRequest ogttRequest = chRequest.getOgtt();
				if (ogttRequest.getOgtt1Hr() != null) {
					Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt1Hr());
					if (value == null) {
						throw new RuntimeException("Invalid OGTT 1Hr value[" + ogttRequest.getOgtt1Hr() + "].",
								new Throwable(
										"ogtt.ogtt1Hr: Invalid OGTT 1Hr value[" + ogttRequest.getOgtt1Hr() + "]."));
					}
				}
				if (ogttRequest.getOgtt1HrConventional() != null) {
					Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt1HrConventional());
					if (value == null) {
						throw new RuntimeException(
								"Invalid OGTT 1Hr Conventional value[" + ogttRequest.getOgtt1HrConventional() + "].",
								new Throwable("ogtt.ogtt1Hr: Invalid OGTT 1Hr Conventional value["
										+ ogttRequest.getOgtt1HrConventional() + "]."));
					}
				}
				if (ogttRequest.getOgtt2Hr() != null) {
					Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt2Hr());
					if (value == null) {
						throw new RuntimeException("Invalid OGTT 2Hr value[" + ogttRequest.getOgtt2Hr() + "].",
								new Throwable(
										"ogtt.ogtt1Hr: Invalid OGTT 2Hr value[" + ogttRequest.getOgtt2Hr() + "]."));
					}
				}
				if (ogttRequest.getOgtt2HrConventional() != null) {
					Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt2HrConventional());
					if (value == null) {
						throw new RuntimeException(
								"Invalid OGTT 2Hr Conventional value[" + ogttRequest.getOgtt2HrConventional() + "].",
								new Throwable("ogtt.ogtt1Hr: Invalid OGTT 2Hr Conventional value["
										+ ogttRequest.getOgtt2HrConventional() + "]."));
					}
				}

			}
				break;
			case "OGCT": {
				if (chRequest.getOgct() == null) {
					throw new RuntimeException("OGCT (50G) is required.",
							new Throwable("ogct: OGCT (50G) is required."));
				}
				QisTransactionLabCHOGCTRequest ogctRequest = chRequest.getOgct();
				if (ogctRequest.getOgct() != null) {
					Float rate = appUtility.parseFloatAmount(ogctRequest.getOgct());
					if (rate == null) {
						throw new RuntimeException("Invalid OGCT Result value[" + ogctRequest.getOgct() + "].",
								new Throwable("ogct.creatinine: OGCT Result value[" + ogctRequest.getOgct() + "]."));
					}
				}

				if (ogctRequest.getConventional() != null) {
					Float conventional = appUtility.parseFloatAmount(ogctRequest.getConventional());
					if (conventional == null) {
						throw new RuntimeException(
								"Invalid OGCT conventional value[" + ogctRequest.getConventional() + "].",
								new Throwable("ogct.conventional: Invalid OGCT conventional value["
										+ ogctRequest.getConventional() + "]."));
					}
				}
			}
				break;
			case "ELEC": {
				if (chRequest.getElectrolytes() == null) {
					throw new RuntimeException("Electrolytes is required.",
							new Throwable("electrolytes: Electrolytes is required."));
				}
				QisTransactionLabCHElecRequest elecRequest = chRequest.getElectrolytes();
				if (elecRequest.getSodium() != null) {
					Float value = appUtility.parseFloatAmount(elecRequest.getSodium());
					if (value == null) {
						throw new RuntimeException("Invalid Sodium value[" + elecRequest.getSodium() + "].",
								new Throwable(
										"electrolytes.sodium: Invalid Sodium value[" + elecRequest.getSodium() + "]."));
					}
				}
				if (elecRequest.getPotassium() != null) {
					Float value = appUtility.parseFloatAmount(elecRequest.getPotassium());
					if (value == null) {
						throw new RuntimeException("Invalid Potassium value[" + elecRequest.getPotassium() + "].",
								new Throwable("electrolytes.potassium: Invalid Potassium value["
										+ elecRequest.getPotassium() + "]."));
					}
				}
				if (elecRequest.getChloride() != null) {
					Float value = appUtility.parseFloatAmount(elecRequest.getChloride());
					if (value == null) {
						throw new RuntimeException("Invalid Chloride value[" + elecRequest.getChloride() + "].",
								new Throwable("electrolytes.chloride: Invalid Chloride value["
										+ elecRequest.getChloride() + "]."));
					}
				}
				if (elecRequest.getMagnesium() != null) {
					Float value = appUtility.parseFloatAmount(elecRequest.getMagnesium());
					if (value == null) {
						throw new RuntimeException("Invalid Magnesium value[" + elecRequest.getMagnesium() + "].",
								new Throwable("electrolytes.magnesium: Invalid Magnesium value["
										+ elecRequest.getMagnesium() + "]."));
					}
				}
				if (elecRequest.getIonizedCalcium() != null) {
					Float value = appUtility.parseFloatAmount(elecRequest.getIonizedCalcium());
					if (value == null) {
						throw new RuntimeException(
								"Invalid IonizedCalcium value[" + elecRequest.getIonizedCalcium() + "].",
								new Throwable("electrolytes.ionizedCalcium: Invalid IonizedCalcium value["
										+ elecRequest.getIonizedCalcium() + "]."));
					}
				}
				if (elecRequest.getTotalCalcium() != null) {
					Float value = appUtility.parseFloatAmount(elecRequest.getTotalCalcium());
					if (value == null) {
						throw new RuntimeException("Invalid TotalCalcium value[" + elecRequest.getTotalCalcium() + "].",
								new Throwable("electrolytes.totalCalcium: Invalid TotalCalcium value["
										+ elecRequest.getTotalCalcium() + "]."));
					}
				}
				if (elecRequest.getInorganicPhosphorus() != null) {
					Float value = appUtility.parseFloatAmount(elecRequest.getInorganicPhosphorus());
					if (value == null) {
						throw new RuntimeException(
								"Invalid InorganicPhosphorus value[" + elecRequest.getInorganicPhosphorus() + "].",
								new Throwable("electrolytes.inorganicPhosphorus: Invalid InorganicPhosphorus value["
										+ elecRequest.getInorganicPhosphorus() + "]."));
					}
				}
				
				if (elecRequest.getTotalIron() != null) {
					Float value = appUtility.parseFloatAmount(elecRequest.getTotalIron());
					if (value == null) {
						throw new RuntimeException(
								"Invalid InorganicPhosphorus value[" + elecRequest.getTotalIron() + "].",
								new Throwable("electrolytes.totalIron: Invalid totalIron value["
										+ elecRequest.getTotalIron() + "]."));
					}
				}
			}
				break;
			case "ENZY": {
				if (chRequest.getEnzymes() == null) {
					throw new RuntimeException("Enzymes is required.", new Throwable("enzymes: Enzymes is required."));
				}
				QisTransactionLabCHEnzyRequest enzyRequest = chRequest.getEnzymes();
				if (enzyRequest.getSgptAlt() != null) {
					Float value = appUtility.parseFloatAmount(enzyRequest.getSgptAlt());
					if (value == null) {
						throw new RuntimeException("Invalid SGPT/ALT value[" + enzyRequest.getSgptAlt() + "].",
								new Throwable(
										"enzymes.sgptAlt: Invalid SGPT/ALT value[" + enzyRequest.getSgptAlt() + "]."));
					}
				}
				if (enzyRequest.getSgotAst() != null) {
					Float value = appUtility.parseFloatAmount(enzyRequest.getSgotAst());
					if (value == null) {
						throw new RuntimeException("Invalid SGOT/AST value[" + enzyRequest.getSgotAst() + "].",
								new Throwable(
										"enzymes.sgotAst: Invalid SGOT/AST value[" + enzyRequest.getSgotAst() + "]."));
					}
				}
				if (enzyRequest.getAmylase() != null) {
					Float value = appUtility.parseFloatAmount(enzyRequest.getAmylase());
					if (value == null) {
						throw new RuntimeException("Invalid Amylase value[" + enzyRequest.getAmylase() + "].",
								new Throwable(
										"enzymes.amylase: Invalid Amylase value[" + enzyRequest.getAmylase() + "]."));
					}
				}
				if (enzyRequest.getLipase() != null) {
					Float value = appUtility.parseFloatAmount(enzyRequest.getLipase());
					if (value == null) {
						throw new RuntimeException("Invalid Lipase value[" + enzyRequest.getLipase() + "].",
								new Throwable(
										"enzymes.lipase: Invalid Lipase value[" + enzyRequest.getLipase() + "]."));
					}
				}
				if (enzyRequest.getAlp() != null) {
					Float value = appUtility.parseFloatAmount(enzyRequest.getAlp());
					if (value == null) {
						throw new RuntimeException("Invalid ALP value[" + enzyRequest.getAlp() + "].",
								new Throwable("enzymes.alp: Invalid ALP value[" + enzyRequest.getAlp() + "]."));
					}
				}
				if (enzyRequest.getGgtp() != null) {
					Float value = appUtility.parseFloatAmount(enzyRequest.getGgtp());
					if (value == null) {
						throw new RuntimeException("Invalid GGTP value[" + enzyRequest.getGgtp() + "].",
								new Throwable("enzymes.ggtp: Invalid GGTP value[" + enzyRequest.getGgtp() + "]."));
					}
				}
				if (enzyRequest.getLdh() != null) {
					Float value = appUtility.parseFloatAmount(enzyRequest.getLdh());
					if (value == null) {
						throw new RuntimeException("Invalid LDH value[" + enzyRequest.getLdh() + "].",
								new Throwable("enzymes.ldh: Invalid LDH value[" + enzyRequest.getLdh() + "]."));
					}
				}
			}
				break;
			case "CPK": {
				if (chRequest.getCreatinePhosphokinase() == null) {
					throw new RuntimeException("CPK is required.",
							new Throwable("creatinePhosphokinase: CPK is required."));
				}
				QisTransactionLabCHCPKRequest cpkRequest = chRequest.getCreatinePhosphokinase();
				if (cpkRequest.getCpkMB() != null) {
					Float value = appUtility.parseFloatAmount(cpkRequest.getCpkMB());
					if (value == null) {
						throw new RuntimeException("Invalid CPK-MB value[" + cpkRequest.getCpkMB() + "].",
								new Throwable("creatinePhosphokinase.CpkMB: Invalid CPK-MB value["
										+ cpkRequest.getCpkMB() + "]."));
					}
				}
				if (cpkRequest.getCpkMM() != null) {
					Float value = appUtility.parseFloatAmount(cpkRequest.getCpkMM());
					if (value == null) {
						throw new RuntimeException("Invalid CPK-MM value[" + cpkRequest.getCpkMM() + "].",
								new Throwable("creatinePhosphokinase.CpkMM: Invalid CPK-MM value["
										+ cpkRequest.getCpkMM() + "]."));
					}
				}
				if (cpkRequest.getTotalCpk() != null) {
					Float value = appUtility.parseFloatAmount(cpkRequest.getTotalCpk());
					if (value == null) {
						throw new RuntimeException("Invalid Total CPK value[" + cpkRequest.getTotalCpk() + "].",
								new Throwable("creatinePhosphokinase.totalCpk: Invalid Total CPK value["
										+ cpkRequest.getTotalCpk() + "]."));
					}
				}
			}
				break;
			case "BILI": {
				if (chRequest.getBilirubin() == null) {
					throw new RuntimeException("Bilirubin is required.",
							new Throwable("bilirubin: Bilirubin is required."));
				}
				QisTransactionLabCHBiliRequest bRequest = chRequest.getBilirubin();
				if (bRequest.getTotalAdult() != null) {
					Float value = appUtility.parseFloatAmount(bRequest.getTotalAdult());
					if (value == null) {
						throw new RuntimeException("Invalid Total (Adult) value[" + bRequest.getTotalAdult() + "].",
								new Throwable("bilirubin.totalAdult: Invalid Total (Adult) value["
										+ bRequest.getTotalAdult() + "]."));
					}
				}
				if (bRequest.getDirect() != null) {
					Float value = appUtility.parseFloatAmount(bRequest.getDirect());
					if (value == null) {
						throw new RuntimeException("Invalid Direct value[" + bRequest.getDirect() + "].",
								new Throwable("bilirubin.direct: Invalid Direct value[" + bRequest.getDirect() + "]."));
					}
				}
				if (bRequest.getIndirect() != null) {
					Float value = appUtility.parseFloatAmount(bRequest.getIndirect());
					if (value == null) {
						throw new RuntimeException("Invalid Indirect value[" + bRequest.getIndirect() + "].",
								new Throwable(
										"bilirubin.indirect: Invalid Indirect value[" + bRequest.getIndirect() + "]."));
					}
				}
			}
				break;
			case "PROT": {
				if (chRequest.getProtein() == null) {
					throw new RuntimeException("Serum Protein is required.",
							new Throwable("protein: Serum Protein is required."));
				}
				QisTransactionLabCHProtRequest pRequest = chRequest.getProtein();
				if (pRequest.getTotalProtein() != null) {
					Float value = appUtility.parseFloatAmount(pRequest.getTotalProtein());
					if (value == null) {
						throw new RuntimeException("Invalid TotalProtein value[" + pRequest.getTotalProtein() + "].",
								new Throwable("commonChemistry.totalProtein: Invalid TotalProtein value["
										+ pRequest.getTotalProtein() + "]."));
					}
				}
				if (pRequest.getAlbumin() != null) {
					Float value = appUtility.parseFloatAmount(pRequest.getAlbumin());
					if (value == null) {
						throw new RuntimeException("Invalid Albumin value[" + pRequest.getAlbumin() + "].",
								new Throwable("commonChemistry.albumin: Invalid Albumin value[" + pRequest.getAlbumin()
										+ "]."));
					}
				}
				if (pRequest.getGlobulin() != null) {
					Float value = appUtility.parseFloatAmount(pRequest.getGlobulin());
					if (value == null) {
						throw new RuntimeException("Invalid Globulin value[" + pRequest.getGlobulin() + "].",
								new Throwable("commonChemistry.globulin: Invalid Globulin value["
										+ pRequest.getGlobulin() + "]."));
					}
				}
				if (pRequest.getAGRatio() != null) {
					Float value = appUtility.parseFloatAmount(pRequest.getAGRatio());
					if (value == null) {
						throw new RuntimeException("Invalid AGRatio value[" + pRequest.getAGRatio() + "].",
								new Throwable("commonChemistry.agratio: Invalid AGRatio value[" + pRequest.getAGRatio()
										+ "]."));
					}
				}

			}
				break;
			case "TIBC": {
				if (chRequest.getTibc() == null) {
					throw new RuntimeException("Total Iron Binding Capacity is required.",
							new Throwable("Total Iron Binding Capacity: Total Iron Binding Capacity is required."));
				}
				QisTransationLabCHTIBCRequest pRequest = chRequest.getTibc();
				if (pRequest.getResult() != null) {
					if (pRequest.getResult() == null) {
						throw new RuntimeException(
								"Invalid Total Iron Binding Capacity value[" + pRequest.getResult() + "].",
								new Throwable(
										"commonChemistry.Total Iron Binding Capacity: Invalid Total Iron Binding Capacity value["
												+ pRequest.getResult() + "]."));
					}
				}
			}
				break;
			} // switch
		} // for loop
	}

	public void saveChemistry(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionChemistry qisTransactionChemistry, Set<QisLaboratoryProcedureService> serviceRequest,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, QisDoctor qisDoctor) {
		String referenceLab1 = "";
		if (chRequest.getBilirubin() != null) {
			referenceLab1 = chRequest.getBilirubin().getReferenceLabId();
		}
		if (chRequest.getBloodUreaNitrogen() != null) {
			referenceLab1 = chRequest.getBloodUreaNitrogen().getReferenceLabId();
		}
		if (chRequest.getCreatinePhosphokinase() != null) {
			referenceLab1 = chRequest.getCreatinePhosphokinase().getReferenceLabId();
		}
		if (chRequest.getCreatinine() != null) {
			referenceLab1 = chRequest.getCreatinine().getReferenceLabId();
		}
		if (chRequest.getElectrolytes() != null) {
			referenceLab1 = chRequest.getElectrolytes().getReferenceLabId();
		}
		if (chRequest.getEnzymes() != null) {
			referenceLab1 = chRequest.getEnzymes().getReferenceLabId();
		}
		if (chRequest.getFastingBloodSugar() != null) {
			referenceLab1 = chRequest.getFastingBloodSugar().getReferenceLabId();
		}
		if (chRequest.getHemoglobin() != null) {
			referenceLab1 = chRequest.getHemoglobin().getReferenceLabId();
		}
		if (chRequest.getLipidProfile() != null) {
			referenceLab1 = chRequest.getLipidProfile().getReferenceLabId();
		}
		if (chRequest.getOgct() != null) {
			referenceLab1 = chRequest.getOgct().getReferenceLabId();
		}
		if (chRequest.getOgtt() != null) {
			referenceLab1 = chRequest.getOgtt().getReferenceLabId();
		}
		if (chRequest.getPprbs() != null) {
			referenceLab1 = chRequest.getPprbs().getReferenceLabId();
		}
		if (chRequest.getProtein() != null) {
			referenceLab1 = chRequest.getProtein().getReferenceLabId();
		}
		if (chRequest.getRandomBloodSugar() != null) {
			referenceLab1 = chRequest.getRandomBloodSugar().getReferenceLabId();
		}
		if (chRequest.getUricAcid() != null) {
			referenceLab1 = chRequest.getUricAcid().getReferenceLabId();
		}
		
		if (chRequest.getTibc() != null) {
			referenceLab1 = chRequest.getTibc().getReferenceLabId();
		}

		for (QisLaboratoryProcedureService service : serviceRequest) {
			switch (service.getLaboratoryRequest().toString()) {
			case "FBS": {
				QisTransactionLabFBS fbs = saveFBS(qisTransaction, laboratoryId, chRequest, authUser, referenceLab1,
						qisTransactionChemistry.getItemDetails().getItemid());
				if (fbs != null) {
					qisTransactionChemistry.setFbs(fbs);
				}
			}
				break;
			case "RBS": {
				QisTransactionLabRBS rbs = saveRBS(qisTransaction, laboratoryId, chRequest, authUser, referenceLab1,
						qisTransactionChemistry.getItemDetails().getItemid());
				if (rbs != null) {
					qisTransactionChemistry.setRbs(rbs);
				}
			}
				break;
			case "PPRBS": {
				QisTransactionLabPPRBS pprbs = savePPRBS(qisTransaction, laboratoryId, chRequest, authUser,
						referenceLab1, qisTransactionChemistry.getItemDetails().getItemid());
				if (pprbs != null) {
					qisTransactionChemistry.setPprbs(pprbs);
				}
			}
				break;
			case "URAC": {
				QisTransactionLabUricAcid uricAcid = saveUricAcid(qisTransaction, laboratoryId, chRequest, authUser,
						referenceLab1, qisTransactionChemistry.getItemDetails().getItemid());
				if (uricAcid != null) {
					qisTransactionChemistry.setUricAcid(uricAcid);
				}
			}
				break;
			case "BUN": {
				QisTransactionLabBUN bun = saveBUN(qisTransaction, laboratoryId, chRequest, authUser, referenceLab1,
						qisTransactionChemistry.getItemDetails().getItemid());
				if (bun != null) {
					qisTransactionChemistry.setBun(bun);
				}
			}
				break;
			case "CREA": {
				QisTransactionLabCreatinine crea = saveCreatinine(qisTransaction, laboratoryId, chRequest, authUser,
						referenceLab1, qisTransactionChemistry.getItemDetails().getItemid());
				if (crea != null) {
					qisTransactionChemistry.setCreatinine(crea);
				}
			}
				break;
			case "HBA1C": {
				QisTransactionLabHemoglobin hemo = saveHemoglobin(qisTransaction, laboratoryId, chRequest, authUser,
						referenceLab1, qisTransactionChemistry.getItemDetails().getItemid());
				if (hemo != null) {
					qisTransactionChemistry.setHemoglobin(hemo);
				}
			}
				break;
			case "LIPP": {
				QisTransactionLabLipidProfile lp = saveLipidProfile(qisTransaction, laboratoryId, chRequest, authUser,
						referenceLab1, qisTransactionChemistry.getItemDetails().getItemid());
				if (lp != null) {
					qisTransactionChemistry.setLipidProfile(lp);
				}
			}
				break;
			case "OGTT": {
				QisTransactionLabOGTT ogtt = saveOGTT(qisTransaction, laboratoryId, chRequest, authUser, referenceLab1,
						qisTransactionChemistry.getItemDetails().getItemid());
				if (ogtt != null) {
					qisTransactionChemistry.setOgtt(ogtt);
				}
			}
				break;
			case "OGCT": {
				QisTransactionLabOGCT ogct = saveOGCT(qisTransaction, laboratoryId, chRequest, authUser, referenceLab1,
						qisTransactionChemistry.getItemDetails().getItemid());
				if (ogct != null) {
					qisTransactionChemistry.setOgct(ogct);
				}
			}
				break;
			case "ELEC": {
				QisTransactionLabElectrolytes elec = saveElectrolytes(qisTransaction, laboratoryId, chRequest, authUser,
						referenceLab1, qisTransactionChemistry.getItemDetails().getItemid());
				if (elec != null) {
					qisTransactionChemistry.setElectrolytes(elec);
				}
			}
				break;
			case "ENZY": {
				QisTransactionLabEnzymes enzy = saveEnzymes(qisTransaction, laboratoryId, chRequest, authUser,
						referenceLab1, qisTransactionChemistry.getItemDetails().getItemid());
				if (enzy != null) {
					qisTransactionChemistry.setEnzymes(enzy);
				}
			}
				break;
			case "CPK": {
				QisTransactionLabCPK cpk = saveCPK(qisTransaction, laboratoryId, chRequest, authUser, referenceLab1,
						qisTransactionChemistry.getItemDetails().getItemid());
				if (cpk != null) {
					qisTransactionChemistry.setCpk(cpk);
				}
			}
				break;
			case "BILI": {
				QisTransactionLabBilirubin bili = saveBilirubin(qisTransaction, laboratoryId, chRequest, authUser,
						referenceLab1, qisTransactionChemistry.getItemDetails().getItemid());
				if (bili != null) {
					qisTransactionChemistry.setBilirubin(bili);
				}
			}
				break;
			case "PROT": {
				QisTransactionLabProtein prot = saveProtein(qisTransaction, laboratoryId, chRequest, authUser,
						referenceLab1, qisTransactionChemistry.getItemDetails().getItemid());
				if (prot != null) {
					qisTransactionChemistry.setProtein(prot);
				}
			}
				break;
			case "TIBC": {
				QisTransactionLabTIBC tibc = saveTBIC(qisTransaction, laboratoryId, chRequest, authUser, referenceLab1,
						qisTransactionChemistry.getItemDetails().getItemid());
				if (tibc != null) {
					qisTransactionChemistry.setTibc(tibc);
				}
			}
				break;
			} // switch
		} // for loop

		int isUpdate = 0;
		if (!qisTransactionChemistry.isSubmitted()) {
			qisTransactionChemistry.setSubmitted(true);
			qisTransactionChemistry.setVerifiedBy(authUser.getId());
			qisTransactionChemistry.setVerifiedDate(Calendar.getInstance());
			isUpdate += 1;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					"SUBMITTED", qisTransactionChemistry.getItemDetails().getItemName(), laboratoryId, CATEGORY);
		}

		if (qisTransactionChemistry.getLabPersonelId() == null) {
			qisTransactionChemistry.setLabPersonelId(authUser.getId());
			qisTransactionChemistry.setLabPersonelDate(Calendar.getInstance());
			qisTransactionChemistry.setStatus(2);
			isUpdate += 2;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					"LAB PERSONEL", authUser.getUsername(), laboratoryId, CATEGORY);
		}

		String onLogMsg = "";
		String action = "ADDED";
		if (chRequest.getOtherNotes() != null) {
			if (qisTransactionChemistry.getOtherNotes() == null) {
				qisTransactionChemistry.setOtherNotes(chRequest.getOtherNotes());
				onLogMsg = chRequest.getOtherNotes();
			} else if (!chRequest.getOtherNotes().equals(qisTransactionChemistry.getOtherNotes())) {
				qisTransactionChemistry.setOtherNotes(chRequest.getOtherNotes());
				action = "UPDATED";
				onLogMsg = appUtility.formatUpdateData(onLogMsg, "OtherNotes", qisTransactionChemistry.getOtherNotes(),
						chRequest.getOtherNotes());
			}
		} else {
			if (qisTransactionChemistry.getOtherNotes() != null) {
				qisTransactionChemistry.setOtherNotes(null);
				action = "UPDATED";
				onLogMsg = appUtility.formatUpdateData(onLogMsg, "OtherNotes", qisTransactionChemistry.getOtherNotes(),
						null);
			}
		}

		if (!"".equals(onLogMsg)) {
			isUpdate += 4;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, onLogMsg, laboratoryId, CATEGORY + "-OTHER NOTES");
		}

		onLogMsg = "";
		action = "ADDED";
		if (qisTransactionChemistry.getMedicalDoctorId() == null) {
			qisTransactionChemistry.setMedicalDoctor(qisDoctor);
			qisTransactionChemistry.setMedicalDoctorId(qisDoctor.getId());
			onLogMsg = qisTransactionChemistry.getMedicalDoctor().getDoctorid();
		} else if (qisDoctor.getId() != qisTransactionChemistry.getMedicalDoctorId()) {
			qisTransactionChemistry.setMedicalDoctor(qisDoctor);
			qisTransactionChemistry.setMedicalDoctorId(qisDoctor.getId());

			action = "UPDATED";
			onLogMsg = appUtility.formatUpdateData(onLogMsg, "pathologist",
					qisTransactionChemistry.getMedicalDoctor().getDoctorid(), qisDoctor.getDoctorid());
		}

		if (!"".equals(onLogMsg)) {
			isUpdate += 8;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, onLogMsg, laboratoryId, CATEGORY + "-PATHOLOGIST");
		}

		if (isUpdate > 0) {
			qisTransactionChemistryRepository.save(qisTransactionChemistry);

			QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
			if ((isUpdate & 1) > 0) {
				qisTransactionChemistry.setVerified(labPersonel);

			}
			if ((isUpdate & 2) > 0) {
				qisTransactionChemistry.setLabPersonel(labPersonel);
			}
		}
	}

	public void saveChemistryQC(QisTransactionChemistry qisChemistry, Long laboratoryId, QisUserDetails authUser) {
		qisChemistry.setQcId(authUser.getId());
		qisChemistry.setQcDate(Calendar.getInstance());
		qisChemistry.setStatus(3); // Quality Control
		qisTransactionChemistryRepository.save(qisChemistry);

		QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
		qisChemistry.setQualityControl(labPersonel);

		qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
				"UPDATE", "QC Chemistry", laboratoryId, CATEGORY);
	}

	private QisTransactionLabTIBC saveTBIC(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String tbicLogMsg = "";
		QisTransactionLabTIBC tibc = qisTransactionLabTIBCRepository.getTransactionLabTIBCByLabReqId(laboratoryId);

		QisTransationLabCHTIBCRequest tibcRequest = chRequest.getTibc();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(tibcRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (tibc == null) {
			tibc = new QisTransactionLabTIBC();
			BeanUtils.copyProperties(chRequest.getTibc(), tibc);
			tibc.setId(laboratoryId);
			tibc.setCreatedBy(authUser.getId());
			tibc.setUpdatedBy(authUser.getId());

			if (tibcRequest.getResult() != null) {
				tibc.setResult(tibcRequest.getResult());
			}

			tbicLogMsg = chRequest.getTibc().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			tibc.setUpdatedBy(authUser.getId());
			tibc.setUpdatedAt(Calendar.getInstance());

			if (tibcRequest.getReferenceLabId() != null) {
				if (tibc.getReferenceLab() != null) {
					if (tibcRequest.getReferenceLabId() != tibc.getReferenceLab().getReferenceid()) {
						tbicLogMsg = appUtility.formatUpdateData(tbicLogMsg, "Reference Laboratory",
								String.valueOf(tibc.getReferenceLab().getReferenceid()),
								String.valueOf(tibcRequest.getReferenceLabId()));
						if (referenceLab != null) {
							tibc.setReferenceLab(referenceLab);
							tibc.setReferenceLabId(referenceLab.getId());
						} else {
							tibc.setReferenceLab(null);
							tibc.setReferenceLabId(null);
						}
					}
				} else {
					tbicLogMsg = appUtility.formatUpdateData(tbicLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(tibcRequest.getReferenceLabId()));
					if (referenceLab != null) {
						tibc.setReferenceLab(referenceLab);
						tibc.setReferenceLabId(referenceLab.getId());
					} else {
						tibc.setReferenceLab(null);
						tibc.setReferenceLabId(null);
					}
				}
			} else {
				if (tibc.getReferenceLab() != null) {
					tbicLogMsg = appUtility.formatUpdateData(tbicLogMsg, "Reference Laboratory",
							String.valueOf(tibc.getReferenceLab().getReferenceid()), null);
					tibc.setReferenceLab(null);
					tibc.setReferenceLabId(null);
				}
			}

			if (tibcRequest.getResult() != null) {
				if (!tibcRequest.getResult().equals(tibc.getResult())) {
					tbicLogMsg = appUtility.formatUpdateData(tbicLogMsg, "Result", String.valueOf(tibc.getResult()),
							String.valueOf(tibcRequest.getResult()));
					tibc.setResult(tibcRequest.getResult());
				}
			} else {
				if (tibc.getResult() != null) {
					tbicLogMsg = appUtility.formatUpdateData(tbicLogMsg, "Result", String.valueOf(tibc.getResult()),
							null);
					tibc.setResult(null);
				}
			}
		}

		if (!"".equals(tbicLogMsg)) {
			qisTransactionLabTIBCRepository.save(tibc);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, tbicLogMsg, laboratoryId, CATEGORY + "-FBS");
		}

		if (isAdded) {
			return tibc;
		}

		return null;
	}

	private QisTransactionLabFBS saveFBS(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String fbsLogMsg = "";
		QisTransactionLabFBS fbs = qisTransactionLabFBSRepository.getTransactionLabFBSByLabReqId(laboratoryId);

		QisTransactionLabCHFBSRequest fbsRequest = chRequest.getFastingBloodSugar();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(fbsRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (fbs == null) {
			fbs = new QisTransactionLabFBS();
			BeanUtils.copyProperties(chRequest.getFastingBloodSugar(), fbs);
			fbs.setId(laboratoryId);
			fbs.setCreatedBy(authUser.getId());
			fbs.setUpdatedBy(authUser.getId());

			if (fbsRequest.getFbs() != null) {
				Float rate = appUtility.parseFloatAmount(fbsRequest.getFbs());
				fbs.setFbs(rate);
			}

			if (fbsRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(fbsRequest.getConventional());
				fbs.setConventional(conventional);
			}

			if (referenceLab != null) {
				fbs.setReferenceLab(referenceLab);
				fbs.setReferenceLabId(referenceLab.getId());
			}

			fbsLogMsg = chRequest.getFastingBloodSugar().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			fbs.setUpdatedBy(authUser.getId());
			fbs.setUpdatedAt(Calendar.getInstance());

			if (fbsRequest.getReferenceLabId() != null) {
				if (fbs.getReferenceLab() != null) {
					if (fbsRequest.getReferenceLabId() != fbs.getReferenceLab().getReferenceid()) {
						fbsLogMsg = appUtility.formatUpdateData(fbsLogMsg, "Reference Laboratory",
								String.valueOf(fbs.getReferenceLab().getReferenceid()),
								String.valueOf(fbsRequest.getReferenceLabId()));
						if (referenceLab != null) {
							fbs.setReferenceLab(referenceLab);
							fbs.setReferenceLabId(referenceLab.getId());
						} else {
							fbs.setReferenceLab(null);
							fbs.setReferenceLabId(null);
						}
					}
				} else {
					fbsLogMsg = appUtility.formatUpdateData(fbsLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(fbsRequest.getReferenceLabId()));
					if (referenceLab != null) {
						fbs.setReferenceLab(referenceLab);
						fbs.setReferenceLabId(referenceLab.getId());
					} else {
						fbs.setReferenceLab(null);
						fbs.setReferenceLabId(null);
					}
				}
			} else {
				if (fbs.getReferenceLab() != null) {
					fbsLogMsg = appUtility.formatUpdateData(fbsLogMsg, "Reference Laboratory",
							String.valueOf(fbs.getReferenceLab().getReferenceid()), null);
					fbs.setReferenceLab(null);
					fbs.setReferenceLabId(null);
				}
			}

			if (fbsRequest.getFbs() != null) {
				Float rate = appUtility.parseFloatAmount(fbsRequest.getFbs());
				if (!rate.equals(fbs.getFbs())) {
					fbsLogMsg = appUtility.formatUpdateData(fbsLogMsg, "Rate", String.valueOf(fbs.getFbs()),
							String.valueOf(rate));
					fbs.setFbs(rate);
				}
			} else {
				if (fbs.getFbs() != null) {
					fbsLogMsg = appUtility.formatUpdateData(fbsLogMsg, "Rate", String.valueOf(fbs.getFbs()), null);
					fbs.setFbs(null);

				}
			}

			if (fbsRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(fbsRequest.getConventional());
				if (!conventional.equals(fbs.getConventional())) {
					fbsLogMsg = appUtility.formatUpdateData(fbsLogMsg, "Conventional",
							String.valueOf(fbs.getConventional()), String.valueOf(conventional));
					fbs.setConventional(conventional);
				}
			} else {
				if (fbs.getConventional() != null) {
					fbsLogMsg = appUtility.formatUpdateData(fbsLogMsg, "Conventional",
							String.valueOf(fbs.getConventional()), null);
					fbs.setConventional(null);
				}
			}
		}

		if (!"".equals(fbsLogMsg)) {
			qisTransactionLabFBSRepository.save(fbs);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, fbsLogMsg, laboratoryId, CATEGORY + "-FBS");
		}

		if (isAdded) {
			return fbs;
		}

		return null;
	}

	private QisTransactionLabRBS saveRBS(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String rbsLogMsg = "";
		QisTransactionLabRBS rbs = qisTransactionLabRBSRepository.getTransactionLabRBSByLabReqId(laboratoryId);

		QisTransactionLabCHRBSRequest rbsRequest = chRequest.getRandomBloodSugar();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(rbsRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (rbs == null) {
			rbs = new QisTransactionLabRBS();
			BeanUtils.copyProperties(chRequest.getRandomBloodSugar(), rbs);
			rbs.setId(laboratoryId);
			rbs.setCreatedBy(authUser.getId());
			rbs.setUpdatedBy(authUser.getId());

			if (rbsRequest.getRbs() != null) {
				Float rate = appUtility.parseFloatAmount(rbsRequest.getRbs());
				rbs.setRbs(rate);
			}

			if (rbsRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(rbsRequest.getConventional());
				rbs.setConventional(conventional);
			}

			if (referenceLab != null) {
				rbs.setReferenceLab(referenceLab);
				rbs.setReferenceLabId(referenceLab.getId());
			}

			rbsLogMsg = chRequest.getRandomBloodSugar().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			rbs.setUpdatedBy(authUser.getId());
			rbs.setUpdatedAt(Calendar.getInstance());

			if (rbsRequest.getReferenceLabId() != null) {
				if (rbs.getReferenceLab() != null) {
					if (rbsRequest.getReferenceLabId() != rbs.getReferenceLab().getReferenceid()) {
						rbsLogMsg = appUtility.formatUpdateData(rbsLogMsg, "Reference Laboratory",
								String.valueOf(rbs.getReferenceLab().getReferenceid()),
								String.valueOf(rbsRequest.getReferenceLabId()));
						if (referenceLab != null) {
							rbs.setReferenceLab(referenceLab);
							rbs.setReferenceLabId(referenceLab.getId());
						} else {
							rbs.setReferenceLab(null);
							rbs.setReferenceLabId(null);
						}
					}
				} else {
					rbsLogMsg = appUtility.formatUpdateData(rbsLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(rbsRequest.getReferenceLabId()));
					if (referenceLab != null) {
						rbs.setReferenceLab(referenceLab);
						rbs.setReferenceLabId(referenceLab.getId());
					} else {
						rbs.setReferenceLab(null);
						rbs.setReferenceLabId(null);
					}
				}
			} else {
				if (rbs.getReferenceLab() != null) {
					rbsLogMsg = appUtility.formatUpdateData(rbsLogMsg, "Reference Laboratory",
							String.valueOf(rbs.getReferenceLab().getReferenceid()), null);
					rbs.setReferenceLab(null);
					rbs.setReferenceLabId(null);
				}
			}

			if (rbsRequest.getRbs() != null) {
				Float rate = appUtility.parseFloatAmount(rbsRequest.getRbs());
				if (!rate.equals(rbs.getRbs())) {
					rbsLogMsg = appUtility.formatUpdateData(rbsLogMsg, "Rate", String.valueOf(rbs.getRbs()),
							String.valueOf(rate));
					rbs.setRbs(rate);
				}
			} else {
				if (rbs.getRbs() != null) {
					rbsLogMsg = appUtility.formatUpdateData(rbsLogMsg, "Rate", String.valueOf(rbs.getRbs()), null);
					rbs.setRbs(null);
				}
			}

			if (rbsRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(rbsRequest.getConventional());
				if (!conventional.equals(rbs.getConventional())) {
					rbsLogMsg = appUtility.formatUpdateData(rbsLogMsg, "Conventional",
							String.valueOf(rbs.getConventional()), String.valueOf(conventional));
					rbs.setConventional(conventional);
				}
			} else {
				if (rbs.getConventional() != null) {
					rbsLogMsg = appUtility.formatUpdateData(rbsLogMsg, "Conventional",
							String.valueOf(rbs.getConventional()), null);
					rbs.setConventional(null);
				}
			}

		}

		if (!"".equals(rbsLogMsg)) {
			qisTransactionLabRBSRepository.save(rbs);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, rbsLogMsg, laboratoryId, CATEGORY + "-RBS");
		}

		if (isAdded) {
			return rbs;
		}

		return null;
	}

	private QisTransactionLabPPRBS savePPRBS(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String pprbsLogMsg = "";
		QisTransactionLabPPRBS pprbs = qisTransactionLabPPRBSRepository.getTransactionLabPPRBSByLabReqId(laboratoryId);

		QisTransactionLabCHPPRBSRequest pprbsRequest = chRequest.getPprbs();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(pprbsRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (pprbs == null) {
			pprbs = new QisTransactionLabPPRBS();
			BeanUtils.copyProperties(chRequest.getPprbs(), pprbs);
			pprbs.setId(laboratoryId);
			pprbs.setCreatedBy(authUser.getId());
			pprbs.setUpdatedBy(authUser.getId());

			if (pprbsRequest.getPprbs() != null) {
				Float rate = appUtility.parseFloatAmount(pprbsRequest.getPprbs());
				pprbs.setPprbs(rate);
			}

			if (pprbsRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(pprbsRequest.getConventional());
				pprbs.setConventional(conventional);
			}

			if (referenceLab != null) {
				pprbs.setReferenceLab(referenceLab);
				pprbs.setReferenceLabId(referenceLab.getId());
			}

			pprbsLogMsg = chRequest.getPprbs().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			pprbs.setUpdatedBy(authUser.getId());
			pprbs.setUpdatedAt(Calendar.getInstance());

			if (pprbsRequest.getReferenceLabId() != null) {
				if (pprbs.getReferenceLab() != null) {
					if (pprbsRequest.getReferenceLabId() != pprbs.getReferenceLab().getReferenceid()) {
						pprbsLogMsg = appUtility.formatUpdateData(pprbsLogMsg, "Reference Laboratory",
								String.valueOf(pprbs.getReferenceLab().getReferenceid()),
								String.valueOf(pprbsRequest.getReferenceLabId()));
						if (referenceLab != null) {
							pprbs.setReferenceLab(referenceLab);
							pprbs.setReferenceLabId(referenceLab.getId());
						} else {
							pprbs.setReferenceLab(null);
							pprbs.setReferenceLabId(null);
						}
					}
				} else {
					pprbsLogMsg = appUtility.formatUpdateData(pprbsLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(pprbsRequest.getReferenceLabId()));
					if (referenceLab != null) {
						pprbs.setReferenceLab(referenceLab);
						pprbs.setReferenceLabId(referenceLab.getId());
					} else {
						pprbs.setReferenceLab(null);
						pprbs.setReferenceLabId(null);
					}
				}
			} else {
				if (pprbs.getReferenceLab() != null) {
					pprbsLogMsg = appUtility.formatUpdateData(pprbsLogMsg, "Reference Laboratory",
							String.valueOf(pprbs.getReferenceLab().getReferenceid()), null);
					pprbs.setReferenceLab(null);
					pprbs.setReferenceLabId(null);
				}
			}

			if (pprbsRequest.getPprbs() != null) {
				Float rate = appUtility.parseFloatAmount(pprbsRequest.getPprbs());
				if (!rate.equals(pprbs.getPprbs())) {
					pprbsLogMsg = appUtility.formatUpdateData(pprbsLogMsg, "Rate", String.valueOf(pprbs.getPprbs()),
							String.valueOf(rate));
					pprbs.setPprbs(rate);
				}
			} else {
				if (pprbs.getPprbs() != null) {
					pprbsLogMsg = appUtility.formatUpdateData(pprbsLogMsg, "Rate", String.valueOf(pprbs.getPprbs()),
							null);
					pprbs.setPprbs(null);
				}
			}

			if (pprbsRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(pprbsRequest.getConventional());
				if (!conventional.equals(pprbs.getConventional())) {
					pprbsLogMsg = appUtility.formatUpdateData(pprbsLogMsg, "Conventional",
							String.valueOf(pprbs.getConventional()), String.valueOf(conventional));
					pprbs.setConventional(conventional);
				}
			} else {
				if (pprbs.getConventional() != null) {
					pprbsLogMsg = appUtility.formatUpdateData(pprbsLogMsg, "Conventional",
							String.valueOf(pprbs.getConventional()), null);
					pprbs.setConventional(null);
				}
			}
		}

		if (!"".equals(pprbsLogMsg)) {
			qisTransactionLabPPRBSRepository.save(pprbs);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, pprbsLogMsg, laboratoryId, CATEGORY + "-PPRBS");
		}

		if (isAdded) {
			return pprbs;
		}

		return null;
	}

	private QisTransactionLabUricAcid saveUricAcid(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String uaLogMsg = "";
		QisTransactionLabUricAcid uricAcid = qisTransactionLabUricAcidRepository
				.getTransactionLabUricAcidByLabReqId(laboratoryId);

		QisTransactionLabCHUrAcRequest uaRequest = chRequest.getUricAcid();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(uaRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (uricAcid == null) {
			uricAcid = new QisTransactionLabUricAcid();
			BeanUtils.copyProperties(chRequest.getUricAcid(), uricAcid);
			uricAcid.setId(laboratoryId);
			uricAcid.setCreatedBy(authUser.getId());
			uricAcid.setUpdatedBy(authUser.getId());

			if (uaRequest.getUricAcid() != null) {
				Float rate = appUtility.parseFloatAmount(uaRequest.getUricAcid());
				uricAcid.setUricAcid(rate);
			}

			if (uaRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(uaRequest.getConventional());
				uricAcid.setConventional(conventional);
			}

			if (referenceLab != null) {
				uricAcid.setReferenceLab(referenceLab);
				uricAcid.setReferenceLabId(referenceLab.getId());
			}

			uaLogMsg = chRequest.getUricAcid().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			uricAcid.setUpdatedBy(authUser.getId());
			uricAcid.setUpdatedAt(Calendar.getInstance());

			if (uaRequest.getReferenceLabId() != null) {
				if (uricAcid.getReferenceLab() != null) {
					if (uaRequest.getReferenceLabId() != uricAcid.getReferenceLab().getReferenceid()) {
						uaLogMsg = appUtility.formatUpdateData(uaLogMsg, "Reference Laboratory",
								String.valueOf(uricAcid.getReferenceLab().getReferenceid()),
								String.valueOf(uaRequest.getReferenceLabId()));
						if (referenceLab != null) {
							uricAcid.setReferenceLab(referenceLab);
							uricAcid.setReferenceLabId(referenceLab.getId());
						} else {
							uricAcid.setReferenceLab(null);
							uricAcid.setReferenceLabId(null);
						}
					}
				} else {
					uaLogMsg = appUtility.formatUpdateData(uaLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(uaRequest.getReferenceLabId()));
					if (referenceLab != null) {
						uricAcid.setReferenceLab(referenceLab);
						uricAcid.setReferenceLabId(referenceLab.getId());
					} else {
						uricAcid.setReferenceLab(null);
						uricAcid.setReferenceLabId(null);
					}
				}
			} else {
				if (uricAcid.getReferenceLab() != null) {
					uaLogMsg = appUtility.formatUpdateData(uaLogMsg, "Reference Laboratory",
							String.valueOf(uricAcid.getReferenceLab().getReferenceid()), null);
					uricAcid.setReferenceLab(null);
					uricAcid.setReferenceLabId(null);
				}
			}

			if (uaRequest.getUricAcid() != null) {
				Float rate = appUtility.parseFloatAmount(uaRequest.getUricAcid());
				if (!rate.equals(uricAcid.getUricAcid())) {
					uaLogMsg = appUtility.formatUpdateData(uaLogMsg, "Rate", String.valueOf(uricAcid.getUricAcid()),
							String.valueOf(rate));
					uricAcid.setUricAcid(rate);
				}
			} else {
				if (uricAcid.getUricAcid() != null) {
					uaLogMsg = appUtility.formatUpdateData(uaLogMsg, "Rate", String.valueOf(uricAcid.getUricAcid()),
							null);
					uricAcid.setUricAcid(null);
				}
			}

			if (uaRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(uaRequest.getConventional());
				if (!conventional.equals(uricAcid.getConventional())) {
					uaLogMsg = appUtility.formatUpdateData(uaLogMsg, "Conventional",
							String.valueOf(uricAcid.getConventional()), String.valueOf(conventional));
					uricAcid.setConventional(conventional);
				}
			} else {
				if (uricAcid.getConventional() != null) {
					uaLogMsg = appUtility.formatUpdateData(uaLogMsg, "Conventional",
							String.valueOf(uricAcid.getConventional()), null);
					uricAcid.setConventional(null);
				}
			}
		}

		if (!"".equals(uaLogMsg)) {
			qisTransactionLabUricAcidRepository.save(uricAcid);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, uaLogMsg, laboratoryId, CATEGORY + "-UricAcid");
		}

		if (isAdded) {
			return uricAcid;
		}

		return null;
	}

	private QisTransactionLabBUN saveBUN(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String bunLogMsg = "";
		QisTransactionLabBUN bun = qisTransactionLabBUNRepository.getTransactionLabBUNByLabReqId(laboratoryId);

		QisTransactionLabCHBUNRequest bunRequest = chRequest.getBloodUreaNitrogen();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(bunRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (bun == null) {
			bun = new QisTransactionLabBUN();
			BeanUtils.copyProperties(chRequest.getBloodUreaNitrogen(), bun);
			bun.setId(laboratoryId);
			bun.setCreatedBy(authUser.getId());
			bun.setUpdatedBy(authUser.getId());

			if (bunRequest.getBun() != null) {
				Float rate = appUtility.parseFloatAmount(bunRequest.getBun());
				bun.setBun(rate);
			}

			if (bunRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(bunRequest.getConventional());
				bun.setConventional(conventional);
			}
			if (referenceLab != null) {
				bun.setReferenceLab(referenceLab);
				bun.setReferenceLabId(referenceLab.getId());
			}

			bunLogMsg = chRequest.getBloodUreaNitrogen().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			bun.setUpdatedBy(authUser.getId());
			bun.setUpdatedAt(Calendar.getInstance());

			if (bunRequest.getReferenceLabId() != null) {
				if (bun.getReferenceLab() != null) {
					if (bunRequest.getReferenceLabId() != bun.getReferenceLab().getReferenceid()) {
						bunLogMsg = appUtility.formatUpdateData(bunLogMsg, "Reference Laboratory",
								String.valueOf(bun.getReferenceLab().getReferenceid()),
								String.valueOf(bunRequest.getReferenceLabId()));
						if (referenceLab != null) {
							bun.setReferenceLab(referenceLab);
							bun.setReferenceLabId(referenceLab.getId());
						} else {
							bun.setReferenceLab(null);
							bun.setReferenceLabId(null);
						}
					}
				} else {
					bunLogMsg = appUtility.formatUpdateData(bunLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(bunRequest.getReferenceLabId()));
					if (referenceLab != null) {
						bun.setReferenceLab(referenceLab);
						bun.setReferenceLabId(referenceLab.getId());
					} else {
						bun.setReferenceLab(null);
						bun.setReferenceLabId(null);
					}
				}
			} else {
				if (bun.getReferenceLab() != null) {
					bunLogMsg = appUtility.formatUpdateData(bunLogMsg, "Reference Laboratory",
							String.valueOf(bun.getReferenceLab().getReferenceid()), null);
					bun.setReferenceLab(null);
					bun.setReferenceLabId(null);
				}
			}

			if (bunRequest.getBun() != null) {
				Float rate = appUtility.parseFloatAmount(bunRequest.getBun());
				if (!rate.equals(bun.getBun())) {
					bunLogMsg = appUtility.formatUpdateData(bunLogMsg, "Rate", String.valueOf(bun.getBun()),
							String.valueOf(rate));
					bun.setBun(rate);
				}
			} else {
				if (bun.getBun() != null) {
					bunLogMsg = appUtility.formatUpdateData(bunLogMsg, "Rate", String.valueOf(bun.getBun()), null);
					bun.setBun(null);
				}
			}

			if (bunRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(bunRequest.getConventional());
				if (!conventional.equals(bun.getConventional())) {
					bunLogMsg = appUtility.formatUpdateData(bunLogMsg, "Conventional",
							String.valueOf(bun.getConventional()), String.valueOf(conventional));
					bun.setConventional(conventional);
				}
			} else {
				if (bun.getConventional() != null) {
					bunLogMsg = appUtility.formatUpdateData(bunLogMsg, "Conventional",
							String.valueOf(bun.getConventional()), null);
					bun.setConventional(null);
				}
			}
		}

		if (!"".equals(bunLogMsg)) {
			qisTransactionLabBUNRepository.save(bun);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, bunLogMsg, laboratoryId, CATEGORY + "-BUN");
		}

		if (isAdded) {
			return bun;
		}

		return null;
	}

	private QisTransactionLabCreatinine saveCreatinine(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String creaLogMsg = "";
		QisTransactionLabCreatinine crea = qisTransactionLabCreatinineRepository
				.getTransactionLabCreatinineByLabReqId(laboratoryId);

		QisTransactionLabCHCreaRequest creaRequest = chRequest.getCreatinine();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(creaRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (crea == null) {
			crea = new QisTransactionLabCreatinine();
			BeanUtils.copyProperties(chRequest.getCreatinine(), crea);
			crea.setId(laboratoryId);
			crea.setCreatedBy(authUser.getId());
			crea.setUpdatedBy(authUser.getId());

			if (creaRequest.getCreatinine() != null) {
				Float rate = appUtility.parseFloatAmount(creaRequest.getCreatinine());
				crea.setCreatinine(rate);
			}

			if (creaRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(creaRequest.getConventional());
				crea.setConventional(conventional);
			}
			if (referenceLab != null) {
				crea.setReferenceLab(referenceLab);
				crea.setReferenceLabId(referenceLab.getId());
			}

			creaLogMsg = chRequest.getCreatinine().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			crea.setUpdatedBy(authUser.getId());
			crea.setUpdatedAt(Calendar.getInstance());

			if (creaRequest.getReferenceLabId() != null) {
				if (crea.getReferenceLab() != null) {
					if (creaRequest.getReferenceLabId() != crea.getReferenceLab().getReferenceid()) {
						creaLogMsg = appUtility.formatUpdateData(creaLogMsg, "Reference Laboratory",
								String.valueOf(crea.getReferenceLab().getReferenceid()),
								String.valueOf(creaRequest.getReferenceLabId()));
						if (referenceLab != null) {
							crea.setReferenceLab(referenceLab);
							crea.setReferenceLabId(referenceLab.getId());
						} else {
							crea.setReferenceLab(null);
							crea.setReferenceLabId(null);
						}
					}
				} else {
					creaLogMsg = appUtility.formatUpdateData(creaLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(creaRequest.getReferenceLabId()));
					if (referenceLab != null) {
						crea.setReferenceLab(referenceLab);
						crea.setReferenceLabId(referenceLab.getId());
					} else {
						crea.setReferenceLab(null);
						crea.setReferenceLabId(null);
					}
				}
			} else {
				if (crea.getReferenceLab() != null) {
					creaLogMsg = appUtility.formatUpdateData(creaLogMsg, "Reference Laboratory",
							String.valueOf(crea.getReferenceLab().getReferenceid()), null);
					crea.setReferenceLab(null);
					crea.setReferenceLabId(null);
				}
			}

			if (creaRequest.getCreatinine() != null) {
				Float rate = appUtility.parseFloatAmount(creaRequest.getCreatinine());
				if (!rate.equals(crea.getCreatinine())) {
					creaLogMsg = appUtility.formatUpdateData(creaLogMsg, "Rate", String.valueOf(crea.getCreatinine()),
							String.valueOf(rate));
					crea.setCreatinine(rate);
				}
			} else {
				if (crea.getCreatinine() != null) {
					creaLogMsg = appUtility.formatUpdateData(creaLogMsg, "Rate", String.valueOf(crea.getCreatinine()),
							null);
					crea.setCreatinine(null);
				}
			}

			if (creaRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(creaRequest.getConventional());
				if (!conventional.equals(crea.getConventional())) {
					creaLogMsg = appUtility.formatUpdateData(creaLogMsg, "Conventional",
							String.valueOf(crea.getConventional()), String.valueOf(conventional));
					crea.setConventional(conventional);
				}
			} else {
				if (crea.getConventional() != null) {
					creaLogMsg = appUtility.formatUpdateData(creaLogMsg, "Conventional",
							String.valueOf(crea.getConventional()), null);
					crea.setConventional(null);
				}
			}
		}

		if (!"".equals(creaLogMsg)) {
			qisTransactionLabCreatinineRepository.save(crea);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, creaLogMsg, laboratoryId, CATEGORY + "-CREA");
		}

		if (isAdded) {
			return crea;
		}

		return null;
	}

	private QisTransactionLabHemoglobin saveHemoglobin(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String hemoLogMsg = "";
		QisTransactionLabHemoglobin hemo = qisTransactionLabHemoRepository
				.getTransactionLabHemoglobinByLabReqId(laboratoryId);

		QisTransactionLabCHHemoRequest hemoRequest = chRequest.getHemoglobin();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(hemoRequest.getReferenceLabId());
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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (hemo == null) {
			hemo = new QisTransactionLabHemoglobin();
			BeanUtils.copyProperties(chRequest.getHemoglobin(), hemo);
			hemo.setId(laboratoryId);
			hemo.setCreatedBy(authUser.getId());
			hemo.setUpdatedBy(authUser.getId());

			if (hemoRequest.getHemoglobinA1C() != null) {
				Float value = appUtility.parseFloatAmount(hemoRequest.getHemoglobinA1C());
				hemo.setHemoglobinA1C(value);
			}
			if (referenceLab != null) {
				hemo.setReferenceLab(referenceLab);
				hemo.setReferenceLabId(referenceLab.getId());
			}

			hemoLogMsg = chRequest.getHemoglobin().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			hemo.setUpdatedBy(authUser.getId());
			hemo.setUpdatedAt(Calendar.getInstance());

			if (hemoRequest.getReferenceLabId() != null) {
				if (hemo.getReferenceLab() != null) {
					if (hemoRequest.getReferenceLabId() != hemo.getReferenceLab().getReferenceid()) {
						hemoLogMsg = appUtility.formatUpdateData(hemoLogMsg, "Reference Laboratory",
								String.valueOf(hemo.getReferenceLab().getReferenceid()),
								String.valueOf(hemoRequest.getReferenceLabId()));
						if (referenceLab != null) {
							hemo.setReferenceLab(referenceLab);
							hemo.setReferenceLabId(referenceLab.getId());
						} else {
							hemo.setReferenceLab(null);
							hemo.setReferenceLabId(null);
						}
					}
				} else {
					hemoLogMsg = appUtility.formatUpdateData(hemoLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(hemoRequest.getReferenceLabId()));
					if (referenceLab != null) {
						hemo.setReferenceLab(referenceLab);
						hemo.setReferenceLabId(referenceLab.getId());
					} else {
						hemo.setReferenceLab(null);
						hemo.setReferenceLabId(null);
					}
				}
			} else {
				if (hemo.getReferenceLab() != null) {
					hemoLogMsg = appUtility.formatUpdateData(hemoLogMsg, "Reference Laboratory",
							String.valueOf(hemo.getReferenceLab().getReferenceid()), null);
					hemo.setReferenceLab(null);
					hemo.setReferenceLabId(null);
				}
			}

			if (hemoRequest.getHemoglobinA1C() != null) {
				Float value = appUtility.parseFloatAmount(hemoRequest.getHemoglobinA1C());
				if (!value.equals(hemo.getHemoglobinA1C())) {
					hemoLogMsg = appUtility.formatUpdateData(hemoLogMsg, "HemoglobinA1C",
							String.valueOf(hemo.getHemoglobinA1C()), String.valueOf(value));
					hemo.setHemoglobinA1C(value);
				}
			} else {
				if (hemo.getHemoglobinA1C() != null) {
					hemoLogMsg = appUtility.formatUpdateData(hemoLogMsg, "HemoglobinA1C",
							String.valueOf(hemo.getHemoglobinA1C()), null);
					hemo.setHemoglobinA1C(null);
				}
			}
		}

		if (!"".equals(hemoLogMsg)) {
			qisTransactionLabHemoRepository.save(hemo);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, hemoLogMsg, laboratoryId, CATEGORY + "-HBA1C");
		}

		if (isAdded) {
			return hemo;
		}

		return null;
	}

	private QisTransactionLabLipidProfile saveLipidProfile(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String lpLogMsg = "";
		QisTransactionLabLipidProfile lp = qisTransactionLabLipPRepository
				.getTransactionLabLipidProfileByLabReqId(laboratoryId);

		QisTransactionLabCHLippRequest lpRequest = chRequest.getLipidProfile();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(lpRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}
		if (lp == null) {
			lp = new QisTransactionLabLipidProfile();
			BeanUtils.copyProperties(chRequest.getLipidProfile(), lp);
			lp.setId(laboratoryId);
			lp.setCreatedBy(authUser.getId());
			lp.setUpdatedBy(authUser.getId());

			if (lpRequest.getCholesterol() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getCholesterol());
				lp.setCholesterol(value);
			}

			if (lpRequest.getCholesterolConventional() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getCholesterolConventional());
				lp.setCholesterolConventional(value);
			}

			if (lpRequest.getTriglycerides() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getTriglycerides());
				lp.setTriglycerides(value);
			}

			if (lpRequest.getTriglyceridesConventional() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getTriglyceridesConventional());
				lp.setTriglyceridesConventional(value);
			}

			if (lpRequest.getHdl() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getHdl());
				lp.setHdl(value);
			}

			if (lpRequest.getHdlConventional() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getHdlConventional());
				lp.setHdlConventional(value);
			}

			if (lpRequest.getLdl() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getLdl());
				lp.setLdl(value);
			}

			if (lpRequest.getLdlConventional() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getLdlConventional());
				lp.setLdlConventional(value);
			}

			if (lpRequest.getHdlRatio() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getHdlRatio());
				lp.setHdlRatio(value);
			}

			if (lpRequest.getVldl() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getVldl());
				lp.setVldl(value);
			}
			if (referenceLab != null) {
				lp.setReferenceLab(referenceLab);
				lp.setReferenceLabId(referenceLab.getId());
			}

			lpLogMsg = chRequest.getLipidProfile().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			lp.setUpdatedBy(authUser.getId());
			lp.setUpdatedAt(Calendar.getInstance());

			if (lpRequest.getReferenceLabId() != null) {
				if (lp.getReferenceLab() != null) {
					if (lpRequest.getReferenceLabId() != lp.getReferenceLab().getReferenceid()) {
						lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "Reference Laboratory",
								String.valueOf(lp.getReferenceLab().getReferenceid()),
								String.valueOf(lpRequest.getReferenceLabId()));
						if (referenceLab != null) {
							lp.setReferenceLab(referenceLab);
							lp.setReferenceLabId(referenceLab.getId());
						} else {
							lp.setReferenceLab(null);
							lp.setReferenceLabId(null);
						}
					}
				} else {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(lpRequest.getReferenceLabId()));
					if (referenceLab != null) {
						lp.setReferenceLab(referenceLab);
						lp.setReferenceLabId(referenceLab.getId());
					} else {
						lp.setReferenceLab(null);
						lp.setReferenceLabId(null);
					}
				}
			} else {
				if (lp.getReferenceLab() != null) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "Reference Laboratory",
							String.valueOf(lp.getReferenceLab().getReferenceid()), null);
					lp.setReferenceLab(null);
					lp.setReferenceLabId(null);
				}
			}

			if (lpRequest.getCholesterol() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getCholesterol());
				if (!value.equals(lp.getCholesterol())) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "Cholesterol", String.valueOf(lp.getCholesterol()),
							String.valueOf(value));
					lp.setCholesterol(value);
				}
			} else {
				if (lp.getCholesterol() != null) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "Cholesterol", String.valueOf(lp.getCholesterol()),
							null);
					lp.setCholesterol(null);
				}
			}

			if (lpRequest.getCholesterolConventional() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getCholesterolConventional());
				if (!value.equals(lp.getCholesterolConventional())) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "CholesterolConventional",
							String.valueOf(lp.getCholesterolConventional()), String.valueOf(value));
					lp.setCholesterolConventional(value);
				}
			} else {
				if (lp.getCholesterolConventional() != null) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "CholesterolConventional",
							String.valueOf(lp.getCholesterolConventional()), null);
					lp.setCholesterolConventional(null);
				}
			}

			if (lpRequest.getTriglycerides() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getTriglycerides());
				if (!value.equals(lp.getTriglycerides())) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "Triglycerides",
							String.valueOf(lp.getTriglycerides()), String.valueOf(value));
					lp.setTriglycerides(value);
				}
			} else {
				if (lp.getTriglycerides() != null) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "Triglycerides",
							String.valueOf(lp.getTriglycerides()), null);
					lp.setTriglycerides(null);
				}
			}

			if (lpRequest.getTriglyceridesConventional() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getTriglyceridesConventional());
				if (!value.equals(lp.getTriglyceridesConventional())) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "TriglyceridesConventional",
							String.valueOf(lp.getTriglyceridesConventional()), String.valueOf(value));
					lp.setTriglyceridesConventional(value);
				}
			} else {
				if (lp.getTriglyceridesConventional() != null) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "TriglyceridesConventional",
							String.valueOf(lp.getTriglyceridesConventional()), null);
					lp.setTriglyceridesConventional(null);
				}
			}

			if (lpRequest.getHdl() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getHdl());
				if (!value.equals(lp.getHdl())) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "HDL", String.valueOf(lp.getHdl()),
							String.valueOf(value));
					lp.setHdl(value);
				}
			} else {
				if (lp.getHdl() != null) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "HDL", String.valueOf(lp.getHdl()), null);
					lp.setHdl(null);
				}
			}

			if (lpRequest.getHdlConventional() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getHdlConventional());
				if (!value.equals(lp.getHdlConventional())) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "HDLConventional",
							String.valueOf(lp.getHdlConventional()), String.valueOf(value));
					lp.setHdlConventional(value);
				}
			} else {
				if (lp.getHdlConventional() != null) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "HDLConventional",
							String.valueOf(lp.getHdlConventional()), null);
					lp.setHdlConventional(null);
				}
			}

			if (lpRequest.getLdl() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getLdl());
				if (!value.equals(lp.getLdl())) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "LDL", String.valueOf(lp.getLdl()),
							String.valueOf(value));
					lp.setLdl(value);
				}
			} else {
				if (lp.getLdl() != null) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "LDL", String.valueOf(lp.getLdl()), null);
					lp.setLdl(null);
				}
			}

			if (lpRequest.getLdlConventional() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getLdlConventional());
				if (!value.equals(lp.getLdlConventional())) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "LDLConventional",
							String.valueOf(lp.getLdlConventional()), String.valueOf(value));
					lp.setLdlConventional(value);
				}
			} else {
				if (lp.getLdlConventional() != null) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "LDLConventional",
							String.valueOf(lp.getLdlConventional()), null);
					lp.setLdlConventional(null);
				}
			}

			if (lpRequest.getHdlRatio() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getHdlRatio());
				if (!value.equals(lp.getHdlRatio())) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "HDLRatio", String.valueOf(lp.getHdlRatio()),
							String.valueOf(value));
					lp.setHdlRatio(value);
				}
			} else {
				if (lp.getHdlRatio() != null) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "HDLRatio", String.valueOf(lp.getHdlRatio()),
							null);
					lp.setHdlRatio(null);
				}
			}

			if (lpRequest.getVldl() != null) {
				Float value = appUtility.parseFloatAmount(lpRequest.getVldl());
				if (!value.equals(lp.getVldl())) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "VLDL", String.valueOf(lp.getVldl()),
							String.valueOf(value));
					lp.setVldl(value);
				}
			} else {
				if (lp.getVldl() != null) {
					lpLogMsg = appUtility.formatUpdateData(lpLogMsg, "VLDL", String.valueOf(lp.getVldl()), null);
					lp.setVldl(null);
				}
			}
		}

		if (!"".equals(lpLogMsg)) {
			qisTransactionLabLipPRepository.save(lp);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, lpLogMsg, laboratoryId, CATEGORY + "-LIPP");
		}

		if (isAdded) {
			return lp;
		}

		return null;
	}

	private QisTransactionLabOGTT saveOGTT(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String ogttLogMsg = "";
		QisTransactionLabOGTT ogtt = qisTransactionLabOGTTRepository.getTransactionLabOGTTByLabReqId(laboratoryId);

		QisTransactionLabCHOGTTRequest ogttRequest = chRequest.getOgtt();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(ogttRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (ogtt == null) {
			ogtt = new QisTransactionLabOGTT();
			BeanUtils.copyProperties(chRequest.getOgtt(), ogtt);
			ogtt.setId(laboratoryId);
			ogtt.setCreatedBy(authUser.getId());
			ogtt.setUpdatedBy(authUser.getId());

			if (ogttRequest.getOgtt1Hr() != null) {
				Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt1Hr());
				ogtt.setOgtt1Hr(value);
			}

			if (ogttRequest.getOgtt1HrConventional() != null) {
				Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt1HrConventional());
				ogtt.setOgtt1HrConventional(value);
			}

			if (ogttRequest.getOgtt2Hr() != null) {
				Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt2Hr());
				ogtt.setOgtt2Hr(value);
			}

			if (ogttRequest.getOgtt2HrConventional() != null) {
				Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt2HrConventional());
				ogtt.setOgtt2HrConventional(value);
			}

			if (referenceLab != null) {
				ogtt.setReferenceLab(referenceLab);
				ogtt.setReferenceLabId(referenceLab.getId());
			}

			ogttLogMsg = chRequest.getOgtt().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			ogtt.setUpdatedBy(authUser.getId());
			ogtt.setUpdatedAt(Calendar.getInstance());

			if (ogttRequest.getReferenceLabId() != null) {
				if (ogtt.getReferenceLab() != null) {
					if (ogttRequest.getReferenceLabId() != ogtt.getReferenceLab().getReferenceid()) {
						ogttLogMsg = appUtility.formatUpdateData(ogttLogMsg, "Reference Laboratory",
								String.valueOf(ogtt.getReferenceLab().getReferenceid()),
								String.valueOf(ogttRequest.getReferenceLabId()));
						if (referenceLab != null) {
							ogtt.setReferenceLab(referenceLab);
							ogtt.setReferenceLabId(referenceLab.getId());
						} else {
							ogtt.setReferenceLab(null);
							ogtt.setReferenceLabId(null);
						}
					}
				} else {
					ogttLogMsg = appUtility.formatUpdateData(ogttLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(ogttRequest.getReferenceLabId()));
					if (referenceLab != null) {
						ogtt.setReferenceLab(referenceLab);
						ogtt.setReferenceLabId(referenceLab.getId());
					} else {
						ogtt.setReferenceLab(null);
						ogtt.setReferenceLabId(null);
					}
				}
			} else {
				if (ogtt.getReferenceLab() != null) {
					ogttLogMsg = appUtility.formatUpdateData(ogttLogMsg, "Reference Laboratory",
							String.valueOf(ogtt.getReferenceLab().getReferenceid()), null);
					ogtt.setReferenceLab(null);
					ogtt.setReferenceLabId(null);
				}
			}

			if (ogttRequest.getOgtt1Hr() != null) {
				Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt1Hr());
				if (!value.equals(ogtt.getOgtt1Hr())) {
					ogttLogMsg = appUtility.formatUpdateData(ogttLogMsg, "Ogtt1Hr", String.valueOf(ogtt.getOgtt1Hr()),
							String.valueOf(value));
					ogtt.setOgtt1Hr(value);
				}
			} else {
				if (ogtt.getOgtt1Hr() != null) {
					ogttLogMsg = appUtility.formatUpdateData(ogttLogMsg, "Ogtt1Hr", String.valueOf(ogtt.getOgtt1Hr()),
							null);
					ogtt.setOgtt1Hr(null);
				}
			}

			if (ogttRequest.getOgtt1HrConventional() != null) {
				Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt1HrConventional());
				if (!value.equals(ogtt.getOgtt1HrConventional())) {
					ogttLogMsg = appUtility.formatUpdateData(ogttLogMsg, "Ogtt1HrConventional",
							String.valueOf(ogtt.getOgtt1HrConventional()), String.valueOf(value));
					ogtt.setOgtt1HrConventional(value);
				}
			} else {
				if (ogtt.getOgtt1HrConventional() != null) {
					ogttLogMsg = appUtility.formatUpdateData(ogttLogMsg, "Ogtt1HrConventional",
							String.valueOf(ogtt.getOgtt1HrConventional()), null);
					ogtt.setOgtt1HrConventional(null);
				}
			}

			if (ogttRequest.getOgtt2Hr() != null) {
				Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt2Hr());
				if (!value.equals(ogtt.getOgtt2Hr())) {
					ogttLogMsg = appUtility.formatUpdateData(ogttLogMsg, "Ogtt2Hr", String.valueOf(ogtt.getOgtt2Hr()),
							String.valueOf(value));
					ogtt.setOgtt2Hr(value);
				}
			} else {
				if (ogtt.getOgtt2Hr() != null) {
					ogttLogMsg = appUtility.formatUpdateData(ogttLogMsg, "Ogtt2Hr", String.valueOf(ogtt.getOgtt2Hr()),
							null);
					ogtt.setOgtt2Hr(null);
				}
			}

			if (ogttRequest.getOgtt2HrConventional() != null) {
				Float value = appUtility.parseFloatAmount(ogttRequest.getOgtt2HrConventional());
				if (!value.equals(ogtt.getOgtt2HrConventional())) {
					ogttLogMsg = appUtility.formatUpdateData(ogttLogMsg, "Ogtt2HrConventional",
							String.valueOf(ogtt.getOgtt2HrConventional()), String.valueOf(value));
					ogtt.setOgtt2HrConventional(value);
				}
			} else {
				if (ogtt.getOgtt2HrConventional() != null) {
					ogttLogMsg = appUtility.formatUpdateData(ogttLogMsg, "Ogtt2HrConventional",
							String.valueOf(ogtt.getOgtt2HrConventional()), null);
					ogtt.setOgtt2HrConventional(null);
				}
			}
		}

		if (!"".equals(ogttLogMsg)) {
			qisTransactionLabOGTTRepository.save(ogtt);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, ogttLogMsg, laboratoryId, CATEGORY + "-OGTT");
		}

		if (isAdded) {
			return ogtt;
		}

		return null;
	}

	private QisTransactionLabOGCT saveOGCT(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String ogctLogMsg = "";
		QisTransactionLabOGCT ogct = qisTransactionLabOGCTRepository.getTransactionLabOGCTByLabReqId(laboratoryId);

		QisTransactionLabCHOGCTRequest ogctRequest = chRequest.getOgct();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(ogctRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (ogct == null) {
			ogct = new QisTransactionLabOGCT();
			BeanUtils.copyProperties(chRequest.getOgct(), ogct);
			ogct.setId(laboratoryId);
			ogct.setCreatedBy(authUser.getId());
			ogct.setUpdatedBy(authUser.getId());

			if (ogctRequest.getOgct() != null) {
				Float rate = appUtility.parseFloatAmount(ogctRequest.getOgct());
				ogct.setOgct(rate);
			}

			if (ogctRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(ogctRequest.getConventional());
				ogct.setConventional(conventional);
			}
			if (referenceLab != null) {
				ogct.setReferenceLab(referenceLab);
				ogct.setReferenceLabId(referenceLab.getId());
			}

			ogctLogMsg = chRequest.getOgct().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			ogct.setUpdatedBy(authUser.getId());
			ogct.setUpdatedAt(Calendar.getInstance());

			if (ogctRequest.getReferenceLabId() != null) {
				if (ogct.getReferenceLab() != null) {
					if (ogctRequest.getReferenceLabId() != ogct.getReferenceLab().getReferenceid()) {
						ogctLogMsg = appUtility.formatUpdateData(ogctLogMsg, "Reference Laboratory",
								String.valueOf(ogct.getReferenceLab().getReferenceid()),
								String.valueOf(ogctRequest.getReferenceLabId()));
						if (referenceLab != null) {
							ogct.setReferenceLab(referenceLab);
							ogct.setReferenceLabId(referenceLab.getId());
						} else {
							ogct.setReferenceLab(null);
							ogct.setReferenceLabId(null);
						}
					}
				} else {
					ogctLogMsg = appUtility.formatUpdateData(ogctLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(ogctRequest.getReferenceLabId()));
					if (referenceLab != null) {
						ogct.setReferenceLab(referenceLab);
						ogct.setReferenceLabId(referenceLab.getId());
					} else {
						ogct.setReferenceLab(null);
						ogct.setReferenceLabId(null);
					}
				}
			} else {
				if (ogct.getReferenceLab() != null) {
					ogctLogMsg = appUtility.formatUpdateData(ogctLogMsg, "Reference Laboratory",
							String.valueOf(ogct.getReferenceLab().getReferenceid()), null);
					ogct.setReferenceLab(null);
					ogct.setReferenceLabId(null);
				}
			}

			if (ogctRequest.getOgct() != null) {
				Float rate = appUtility.parseFloatAmount(ogctRequest.getOgct());
				if (!rate.equals(ogct.getOgct())) {
					ogctLogMsg = appUtility.formatUpdateData(ogctLogMsg, "Rate", String.valueOf(ogct.getOgct()),
							String.valueOf(rate));
					ogct.setOgct(rate);
				}
			} else {
				if (ogct.getOgct() != null) {
					ogctLogMsg = appUtility.formatUpdateData(ogctLogMsg, "Rate", String.valueOf(ogct.getOgct()), null);
					ogct.setOgct(null);
				}
			}

			if (ogctRequest.getConventional() != null) {
				Float conventional = appUtility.parseFloatAmount(ogctRequest.getConventional());
				if (!conventional.equals(ogct.getConventional())) {
					ogctLogMsg = appUtility.formatUpdateData(ogctLogMsg, "Conventional",
							String.valueOf(ogct.getConventional()), String.valueOf(conventional));
					ogct.setConventional(conventional);
				}
			} else {
				if (ogct.getConventional() != null) {
					ogctLogMsg = appUtility.formatUpdateData(ogctLogMsg, "Conventional",
							String.valueOf(ogct.getConventional()), null);
					ogct.setConventional(null);
				}
			}
		}

		if (!"".equals(ogctLogMsg)) {
			qisTransactionLabOGCTRepository.save(ogct);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, ogctLogMsg, laboratoryId, CATEGORY + "-OGCT");
		}

		if (isAdded) {
			return ogct;
		}

		return null;
	}

	private QisTransactionLabElectrolytes saveElectrolytes(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String elecLogMsg = "";
		QisTransactionLabElectrolytes elec = qisTransactionLabElectrolytesRepository
				.getTransactionLabElectrolytesByLabReqId(laboratoryId);

		QisTransactionLabCHElecRequest electRequest = chRequest.getElectrolytes();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(electRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (elec == null) {
			elec = new QisTransactionLabElectrolytes();
			BeanUtils.copyProperties(chRequest.getElectrolytes(), elec);
			elec.setId(laboratoryId);
			elec.setCreatedBy(authUser.getId());
			elec.setUpdatedBy(authUser.getId());

			if (electRequest.getSodium() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getSodium());
				elec.setSodium(value);
			}

			if (electRequest.getPotassium() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getPotassium());
				elec.setPotassium(value);
			}

			if (electRequest.getChloride() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getChloride());
				elec.setChloride(value);
			}

			if (electRequest.getTotalCalcium() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getTotalCalcium());
				elec.setTotalCalcium(value);
			}

			if (electRequest.getInorganicPhosphorus() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getInorganicPhosphorus());
				elec.setInorganicPhosphorus(value);
			}

			if (electRequest.getMagnesium() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getMagnesium());
				elec.setMagnesium(value);
			}

			if (electRequest.getIonizedCalcium() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getIonizedCalcium());
				elec.setIonizedCalcium(value);
			}
			if (electRequest.getTotalIron() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getTotalIron());
				elec.setTotalIron(value);
			}
			if (referenceLab != null) {
				elec.setReferenceLab(referenceLab);
				elec.setReferenceLabId(referenceLab.getId());
			}

			elecLogMsg = chRequest.getElectrolytes().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			elec.setUpdatedBy(authUser.getId());
			elec.setUpdatedAt(Calendar.getInstance());

			if (electRequest.getReferenceLabId() != null) {
				if (elec.getReferenceLab() != null) {
					if (electRequest.getReferenceLabId() != elec.getReferenceLab().getReferenceid()) {
						elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Reference Laboratory",
								String.valueOf(elec.getReferenceLab().getReferenceid()),
								String.valueOf(electRequest.getReferenceLabId()));
						if (referenceLab != null) {
							elec.setReferenceLab(referenceLab);
							elec.setReferenceLabId(referenceLab.getId());
						} else {
							elec.setReferenceLab(null);
							elec.setReferenceLabId(null);
						}
					}
				} else {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(electRequest.getReferenceLabId()));
					if (referenceLab != null) {
						elec.setReferenceLab(referenceLab);
						elec.setReferenceLabId(referenceLab.getId());
					} else {
						elec.setReferenceLab(null);
						elec.setReferenceLabId(null);
					}
				}
			} else {
				if (elec.getReferenceLab() != null) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Reference Laboratory",
							String.valueOf(elec.getReferenceLab().getReferenceid()), null);
					elec.setReferenceLab(null);
					elec.setReferenceLabId(null);
				}
			}

			if (electRequest.getSodium() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getSodium());
				if (!value.equals(elec.getSodium())) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Sodium", String.valueOf(elec.getSodium()),
							String.valueOf(value));
					elec.setSodium(value);
				}
			} else {
				if (elec.getSodium() != null) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Sodium", String.valueOf(elec.getSodium()),
							null);
					elec.setSodium(null);
				}
			}

			if (electRequest.getPotassium() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getPotassium());
				if (!value.equals(elec.getPotassium())) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Potassium",
							String.valueOf(elec.getPotassium()), String.valueOf(value));
					elec.setPotassium(value);
				}
			} else {
				if (elec.getPotassium() != null) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Potassium",
							String.valueOf(elec.getPotassium()), null);
					elec.setPotassium(null);
				}
			}

			if (electRequest.getChloride() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getChloride());
				if (!value.equals(elec.getChloride())) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Chloride", String.valueOf(elec.getChloride()),
							String.valueOf(value));
					elec.setChloride(value);
				}
			} else {
				if (elec.getChloride() != null) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Chloride", String.valueOf(elec.getChloride()),
							null);
					elec.setChloride(null);
				}
			}
			
			if (electRequest.getTotalIron() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getTotalIron());
				if (!value.equals(elec.getTotalIron())) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Chloride", String.valueOf(elec.getTotalIron()),
							String.valueOf(value));
					elec.setTotalIron(value);
				}
			} else {
				if (elec.getTotalIron() != null) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Chloride", String.valueOf(elec.getTotalIron()),
							null);
					elec.setTotalIron(null);
				}
			}

			if (electRequest.getInorganicPhosphorus() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getInorganicPhosphorus());
				if (!value.equals(elec.getInorganicPhosphorus())) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "InorganicPhosphorus",
							String.valueOf(elec.getInorganicPhosphorus()), String.valueOf(value));
					elec.setInorganicPhosphorus(value);
				}
			} else {
				if (elec.getInorganicPhosphorus() != null) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "InorganicPhosphorus",
							String.valueOf(elec.getInorganicPhosphorus()), null);
					elec.setInorganicPhosphorus(null);
				}
			}

			if (electRequest.getTotalCalcium() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getTotalCalcium());
				if (!value.equals(elec.getTotalCalcium())) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "TotalCalcium",
							String.valueOf(elec.getTotalCalcium()), String.valueOf(value));
					elec.setTotalCalcium(value);
				}
			} else {
				if (elec.getTotalCalcium() != null) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "TotalCalcium",
							String.valueOf(elec.getTotalCalcium()), null);
					elec.setTotalCalcium(null);
				}
			}

			if (electRequest.getMagnesium() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getMagnesium());
				if (!value.equals(elec.getMagnesium())) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Magnesium",
							String.valueOf(elec.getMagnesium()), String.valueOf(value));
					elec.setMagnesium(value);
				}
			} else {
				if (elec.getMagnesium() != null) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "Magnesium",
							String.valueOf(elec.getMagnesium()), null);
					elec.setMagnesium(null);
				}
			}

			if (electRequest.getIonizedCalcium() != null) {
				Float value = appUtility.parseFloatAmount(electRequest.getIonizedCalcium());
				if (!value.equals(elec.getIonizedCalcium())) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "IonizedCalcium",
							String.valueOf(elec.getIonizedCalcium()), String.valueOf(value));
					elec.setIonizedCalcium(value);
				}
			} else {
				if (elec.getIonizedCalcium() != null) {
					elecLogMsg = appUtility.formatUpdateData(elecLogMsg, "IonizedCalcium",
							String.valueOf(elec.getIonizedCalcium()), null);
					elec.setIonizedCalcium(null);
				}
			}

		}

		if (!"".equals(elecLogMsg)) {
			qisTransactionLabElectrolytesRepository.save(elec);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, elecLogMsg, laboratoryId, CATEGORY + "-ELEC");
		}

		if (isAdded) {
			return elec;
		}
		return null;
	}

	private QisTransactionLabEnzymes saveEnzymes(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String enzyLogMsg = "";
		QisTransactionLabEnzymes enzy = qisTransactionLabEnzymesRepository
				.getTransactionLabEnzymesByLabReqId(laboratoryId);

		QisTransactionLabCHEnzyRequest enzyRequest = chRequest.getEnzymes();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(enzyRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (enzy == null) {
			enzy = new QisTransactionLabEnzymes();
			BeanUtils.copyProperties(chRequest.getEnzymes(), enzy);
			enzy.setId(laboratoryId);
			enzy.setCreatedBy(authUser.getId());
			enzy.setUpdatedBy(authUser.getId());

			if (enzyRequest.getSgptAlt() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getSgptAlt());
				enzy.setSgptAlt(value);
			}

			if (enzyRequest.getSgotAst() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getSgotAst());
				enzy.setSgotAst(value);
			}

			if (enzyRequest.getAmylase() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getAmylase());
				enzy.setAmylase(value);
			}

			if (enzyRequest.getLipase() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getLipase());
				enzy.setLipase(value);
			}

			if (enzyRequest.getAlp() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getAlp());
				enzy.setAlp(value);
			}

			if (enzyRequest.getLdh() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getLdh());
				enzy.setLdh(value);
			}

			if (enzyRequest.getGgtp() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getGgtp());
				enzy.setGgtp(value);
			}
			if (referenceLab != null) {
				enzy.setReferenceLab(referenceLab);
				enzy.setReferenceLabId(referenceLab.getId());
			}

			enzyLogMsg = chRequest.getEnzymes().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			enzy.setUpdatedBy(authUser.getId());
			enzy.setUpdatedAt(Calendar.getInstance());

			if (enzyRequest.getReferenceLabId() != null) {
				if (enzy.getReferenceLab() != null) {
					if (enzyRequest.getReferenceLabId() != enzy.getReferenceLab().getReferenceid()) {
						enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "Reference Laboratory",
								String.valueOf(enzy.getReferenceLab().getReferenceid()),
								String.valueOf(enzyRequest.getReferenceLabId()));
						if (referenceLab != null) {
							enzy.setReferenceLab(referenceLab);
							enzy.setReferenceLabId(referenceLab.getId());
						} else {
							enzy.setReferenceLab(null);
							enzy.setReferenceLabId(null);
						}
					}
				} else {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(enzyRequest.getReferenceLabId()));
					if (referenceLab != null) {
						enzy.setReferenceLab(referenceLab);
						enzy.setReferenceLabId(referenceLab.getId());
					} else {
						enzy.setReferenceLab(null);
						enzy.setReferenceLabId(null);
					}
				}
			} else {
				if (enzy.getReferenceLab() != null) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "Reference Laboratory",
							String.valueOf(enzy.getReferenceLab().getReferenceid()), null);
					enzy.setReferenceLab(null);
					enzy.setReferenceLabId(null);
				}
			}

			if (enzyRequest.getSgptAlt() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getSgptAlt());
				if (!value.equals(enzy.getSgptAlt())) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "SgptAlt", String.valueOf(enzy.getSgptAlt()),
							String.valueOf(value));
					enzy.setSgptAlt(value);
				}
			} else {
				if (enzy.getSgptAlt() != null) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "SgptAlt", String.valueOf(enzy.getSgptAlt()),
							null);
					enzy.setSgptAlt(null);
				}
			}

			if (enzyRequest.getSgotAst() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getSgotAst());
				if (!value.equals(enzy.getSgotAst())) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "SgotAst", String.valueOf(enzy.getSgotAst()),
							String.valueOf(value));
					enzy.setSgotAst(value);
				}
			} else {
				if (enzy.getSgotAst() != null) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "SgotAst", String.valueOf(enzy.getSgotAst()),
							null);
					enzy.setSgotAst(null);
				}
			}

			if (enzyRequest.getAmylase() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getAmylase());
				if (!value.equals(enzy.getAmylase())) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "Amylase", String.valueOf(enzy.getAmylase()),
							String.valueOf(value));
					enzy.setAmylase(value);
				}
			} else {
				if (enzy.getAmylase() != null) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "Amylase", String.valueOf(enzy.getAmylase()),
							null);
					enzy.setAmylase(null);
				}
			}

			if (enzyRequest.getLipase() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getLipase());
				if (!value.equals(enzy.getLipase())) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "Lipase", String.valueOf(enzy.getLipase()),
							String.valueOf(value));
					enzy.setLipase(value);
				}
			} else {
				if (enzy.getLipase() != null) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "Lipase", String.valueOf(enzy.getLipase()),
							null);
					enzy.setLipase(null);
				}
			}

			if (enzyRequest.getAlp() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getAlp());
				if (!value.equals(enzy.getAlp())) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "ALP", String.valueOf(enzy.getAlp()),
							String.valueOf(value));
					enzy.setAlp(value);
				}
			} else {
				if (enzy.getAlp() != null) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "ALP", String.valueOf(enzy.getAlp()), null);
					enzy.setAlp(null);
				}
			}

			if (enzyRequest.getGgtp() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getGgtp());
				if (!value.equals(enzy.getGgtp())) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "GGTP", String.valueOf(enzy.getGgtp()),
							String.valueOf(value));
					enzy.setGgtp(value);
				}
			} else {
				if (enzy.getGgtp() != null) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "GGTP", String.valueOf(enzy.getGgtp()), null);
					enzy.setGgtp(null);
				}
			}

			if (enzyRequest.getLdh() != null) {
				Float value = appUtility.parseFloatAmount(enzyRequest.getLdh());
				if (!value.equals(enzy.getLdh())) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "LDH", String.valueOf(enzy.getLdh()),
							String.valueOf(value));
					enzy.setLdh(value);
				}
			} else {
				if (enzy.getLdh() != null) {
					enzyLogMsg = appUtility.formatUpdateData(enzyLogMsg, "LDH", String.valueOf(enzy.getLdh()), null);
					enzy.setLdh(null);
				}
			}

		}

		if (!"".equals(enzyLogMsg)) {
			qisTransactionLabEnzymesRepository.save(enzy);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, enzyLogMsg, laboratoryId, CATEGORY + "-ENZY");
		}

		if (isAdded) {
			return enzy;
		}

		return null;
	}

	private QisTransactionLabCPK saveCPK(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String cpkLogMsg = "";
		QisTransactionLabCPK cpk = qisTransactionLabCPKRepository.getTransactionLabCPKByLabReqId(laboratoryId);

		QisTransactionLabCHCPKRequest cpkRequest = chRequest.getCreatinePhosphokinase();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(cpkRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (cpk == null) {
			cpk = new QisTransactionLabCPK();
			BeanUtils.copyProperties(chRequest.getCreatinePhosphokinase(), cpk);
			cpk.setId(laboratoryId);
			cpk.setCreatedBy(authUser.getId());
			cpk.setUpdatedBy(authUser.getId());

			if (cpkRequest.getCpkMB() != null) {
				Float value = appUtility.parseFloatAmount(cpkRequest.getCpkMB());
				cpk.setCpkMB(value);
			}

			if (cpkRequest.getCpkMM() != null) {
				Float value = appUtility.parseFloatAmount(cpkRequest.getCpkMM());
				cpk.setCpkMM(value);
			}

			if (cpkRequest.getTotalCpk() != null) {
				Float value = appUtility.parseFloatAmount(cpkRequest.getTotalCpk());
				cpk.setTotalCpk(value);
			}
			if (referenceLab != null) {
				cpk.setReferenceLab(referenceLab);
				cpk.setReferenceLabId(referenceLab.getId());
			}

			cpkLogMsg = chRequest.getCreatinePhosphokinase().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			cpk.setUpdatedBy(authUser.getId());
			cpk.setUpdatedAt(Calendar.getInstance());

			if (cpkRequest.getReferenceLabId() != null) {
				if (cpk.getReferenceLab() != null) {
					if (cpkRequest.getReferenceLabId() != cpk.getReferenceLab().getReferenceid()) {
						cpkLogMsg = appUtility.formatUpdateData(cpkLogMsg, "Reference Laboratory",
								String.valueOf(cpk.getReferenceLab().getReferenceid()),
								String.valueOf(cpkRequest.getReferenceLabId()));
						if (referenceLab != null) {
							cpk.setReferenceLab(referenceLab);
							cpk.setReferenceLabId(referenceLab.getId());
						} else {
							cpk.setReferenceLab(null);
							cpk.setReferenceLabId(null);
						}
					}
				} else {
					cpkLogMsg = appUtility.formatUpdateData(cpkLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(cpkRequest.getReferenceLabId()));
					if (referenceLab != null) {
						cpk.setReferenceLab(referenceLab);
						cpk.setReferenceLabId(referenceLab.getId());
					} else {
						cpk.setReferenceLab(null);
						cpk.setReferenceLabId(null);
					}
				}
			} else {
				if (cpk.getReferenceLab() != null) {
					cpkLogMsg = appUtility.formatUpdateData(cpkLogMsg, "Reference Laboratory",
							String.valueOf(cpk.getReferenceLab().getReferenceid()), null);
					cpk.setReferenceLab(null);
					cpk.setReferenceLabId(null);
				}
			}

			if (cpkRequest.getCpkMB() != null) {
				Float value = appUtility.parseFloatAmount(cpkRequest.getCpkMB());
				if (!value.equals(cpk.getCpkMB())) {
					cpkLogMsg = appUtility.formatUpdateData(cpkLogMsg, "CpkMB", String.valueOf(cpk.getCpkMB()),
							String.valueOf(value));
					cpk.setCpkMB(value);
				}
			} else {
				if (cpk.getCpkMB() != null) {
					cpkLogMsg = appUtility.formatUpdateData(cpkLogMsg, "CpkMB", String.valueOf(cpk.getCpkMB()), null);
					cpk.setCpkMB(null);
				}
			}

			if (cpkRequest.getCpkMM() != null) {
				Float value = appUtility.parseFloatAmount(cpkRequest.getCpkMM());
				if (!value.equals(cpk.getCpkMM())) {
					cpkLogMsg = appUtility.formatUpdateData(cpkLogMsg, "CpkMM", String.valueOf(cpk.getCpkMM()),
							String.valueOf(value));
					cpk.setCpkMM(value);
				}
			} else {
				if (cpk.getCpkMM() != null) {
					cpkLogMsg = appUtility.formatUpdateData(cpkLogMsg, "CpkMM", String.valueOf(cpk.getCpkMM()), null);
					cpk.setCpkMM(null);
				}
			}

			if (cpkRequest.getTotalCpk() != null) {
				Float value = appUtility.parseFloatAmount(cpkRequest.getTotalCpk());
				if (!value.equals(cpk.getTotalCpk())) {
					cpkLogMsg = appUtility.formatUpdateData(cpkLogMsg, "TotalCpk", String.valueOf(cpk.getTotalCpk()),
							String.valueOf(value));
					cpk.setTotalCpk(value);
				}
			} else {
				if (cpk.getTotalCpk() != null) {
					cpkLogMsg = appUtility.formatUpdateData(cpkLogMsg, "TotalCpk", String.valueOf(cpk.getTotalCpk()),
							null);
					cpk.setTotalCpk(null);
				}
			}
		}

		if (!"".equals(cpkLogMsg)) {
			qisTransactionLabCPKRepository.save(cpk);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, cpkLogMsg, laboratoryId, CATEGORY + "-CPK");
		}

		if (isAdded) {
			return cpk;
		}

		return null;
	}

	private QisTransactionLabBilirubin saveBilirubin(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String biliLogMsg = "";
		QisTransactionLabBilirubin bili = qisTransactionLabBilirubinRepository
				.getTransactionLabBilirubinByLabReqId(laboratoryId);

		QisTransactionLabCHBiliRequest biliRequest = chRequest.getBilirubin();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(biliRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (bili == null) {
			bili = new QisTransactionLabBilirubin();
			BeanUtils.copyProperties(chRequest.getBilirubin(), bili);
			bili.setId(laboratoryId);
			bili.setCreatedBy(authUser.getId());
			bili.setUpdatedBy(authUser.getId());

			if (biliRequest.getTotalAdult() != null) {
				Float value = appUtility.parseFloatAmount(biliRequest.getTotalAdult());
				bili.setTotalAdult(value);
			}

			if (biliRequest.getDirect() != null) {
				Float value = appUtility.parseFloatAmount(biliRequest.getDirect());
				bili.setDirect(value);
			}

			if (biliRequest.getIndirect() != null) {
				Float value = appUtility.parseFloatAmount(biliRequest.getIndirect());
				bili.setIndirect(value);
			}
			if (referenceLab != null) {
				bili.setReferenceLab(referenceLab);
				bili.setReferenceLabId(referenceLab.getId());
			}

			biliLogMsg = chRequest.getBilirubin().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			bili.setUpdatedBy(authUser.getId());
			bili.setUpdatedAt(Calendar.getInstance());

			if (biliRequest.getReferenceLabId() != null) {
				if (bili.getReferenceLab() != null) {
					if (biliRequest.getReferenceLabId() != bili.getReferenceLab().getReferenceid()) {
						biliLogMsg = appUtility.formatUpdateData(biliLogMsg, "Reference Laboratory",
								String.valueOf(bili.getReferenceLab().getReferenceid()),
								String.valueOf(biliRequest.getReferenceLabId()));
						if (referenceLab != null) {
							bili.setReferenceLab(referenceLab);
							bili.setReferenceLabId(referenceLab.getId());
						} else {
							bili.setReferenceLab(null);
							bili.setReferenceLabId(null);
						}
					}
				} else {
					biliLogMsg = appUtility.formatUpdateData(biliLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(biliRequest.getReferenceLabId()));
					if (referenceLab != null) {
						bili.setReferenceLab(referenceLab);
						bili.setReferenceLabId(referenceLab.getId());
					} else {
						bili.setReferenceLab(null);
						bili.setReferenceLabId(null);
					}
				}
			} else {
				if (bili.getReferenceLab() != null) {
					biliLogMsg = appUtility.formatUpdateData(biliLogMsg, "Reference Laboratory",
							String.valueOf(bili.getReferenceLab().getReferenceid()), null);
					bili.setReferenceLab(null);
					bili.setReferenceLabId(null);
				}
			}

			if (biliRequest.getTotalAdult() != null) {
				Float value = appUtility.parseFloatAmount(biliRequest.getTotalAdult());
				if (!value.equals(bili.getTotalAdult())) {
					biliLogMsg = appUtility.formatUpdateData(biliLogMsg, "TotalAdult",
							String.valueOf(bili.getTotalAdult()), String.valueOf(value));
					bili.setTotalAdult(value);
				}
			} else {
				if (bili.getTotalAdult() != null) {
					biliLogMsg = appUtility.formatUpdateData(biliLogMsg, "TotalAdult",
							String.valueOf(bili.getTotalAdult()), null);
					bili.setTotalAdult(null);
				}
			}

			if (biliRequest.getDirect() != null) {
				Float value = appUtility.parseFloatAmount(biliRequest.getDirect());
				if (!value.equals(bili.getDirect())) {
					biliLogMsg = appUtility.formatUpdateData(biliLogMsg, "Direct", String.valueOf(bili.getDirect()),
							String.valueOf(value));
					bili.setDirect(value);
				}
			} else {
				if (bili.getDirect() != null) {
					biliLogMsg = appUtility.formatUpdateData(biliLogMsg, "Direct", String.valueOf(bili.getDirect()),
							null);
					bili.setDirect(null);
				}
			}

			if (biliRequest.getIndirect() != null) {
				Float value = appUtility.parseFloatAmount(biliRequest.getIndirect());
				if (!value.equals(bili.getIndirect())) {
					biliLogMsg = appUtility.formatUpdateData(biliLogMsg, "Indirect", String.valueOf(bili.getIndirect()),
							String.valueOf(value));
					bili.setIndirect(value);
				}
			} else {
				if (bili.getIndirect() != null) {
					biliLogMsg = appUtility.formatUpdateData(biliLogMsg, "Indirect", String.valueOf(bili.getIndirect()),
							null);
					bili.setIndirect(null);
				}
			}
		}

		if (!"".equals(biliLogMsg)) {
			qisTransactionLabBilirubinRepository.save(bili);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, biliLogMsg, laboratoryId, CATEGORY + "-BILI");
		}

		if (isAdded) {
			return bili;
		}

		return null;
	}

	private QisTransactionLabProtein saveProtein(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabCHRequest chRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String protLogMsg = "";
		QisTransactionLabProtein prot = qisTransactionLabProteinRepository
				.getTransactionLabProteinByLabReqId(laboratoryId);

		QisTransactionLabCHProtRequest protRequest = chRequest.getProtein();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(protRequest.getReferenceLabId());

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
			} else {
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (prot == null) {
			prot = new QisTransactionLabProtein();
			BeanUtils.copyProperties(chRequest.getProtein(), prot);
			prot.setId(laboratoryId);
			prot.setCreatedBy(authUser.getId());
			prot.setUpdatedBy(authUser.getId());

			if (protRequest.getTotalProtein() != null) {
				Float value = appUtility.parseFloatAmount(protRequest.getTotalProtein());
				prot.setTotalProtein(value);
			}

			if (protRequest.getAlbumin() != null) {
				Float value = appUtility.parseFloatAmount(protRequest.getAlbumin());
				prot.setAlbumin(value);
			}

			if (protRequest.getGlobulin() != null) {
				Float value = appUtility.parseFloatAmount(protRequest.getGlobulin());
				prot.setGlobulin(value);
			}

			if (protRequest.getAGRatio() != null) {
				Float value = appUtility.parseFloatAmount(protRequest.getAGRatio());
				prot.setAGRatio(value);
			}

			if (referenceLab != null) {
				prot.setReferenceLab(referenceLab);
				prot.setReferenceLabId(referenceLab.getId());
			}

			protLogMsg = chRequest.getProtein().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			prot.setUpdatedBy(authUser.getId());
			prot.setUpdatedAt(Calendar.getInstance());

			if (protRequest.getReferenceLabId() != null) {
				if (prot.getReferenceLab() != null) {
					if (protRequest.getReferenceLabId() != prot.getReferenceLab().getReferenceid()) {
						protLogMsg = appUtility.formatUpdateData(protLogMsg, "Reference Laboratory",
								String.valueOf(prot.getReferenceLab().getReferenceid()),
								String.valueOf(protRequest.getReferenceLabId()));
						if (referenceLab != null) {
							prot.setReferenceLab(referenceLab);
							prot.setReferenceLabId(referenceLab.getId());
						} else {
							prot.setReferenceLab(null);
							prot.setReferenceLabId(null);
						}
					}
				} else {
					protLogMsg = appUtility.formatUpdateData(protLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(protRequest.getReferenceLabId()));
					if (referenceLab != null) {
						prot.setReferenceLab(referenceLab);
						prot.setReferenceLabId(referenceLab.getId());
					} else {
						prot.setReferenceLab(null);
						prot.setReferenceLabId(null);
					}
				}
			} else {
				if (prot.getReferenceLab() != null) {
					protLogMsg = appUtility.formatUpdateData(protLogMsg, "Reference Laboratory",
							String.valueOf(prot.getReferenceLab().getReferenceid()), null);
					prot.setReferenceLab(null);
					prot.setReferenceLabId(null);
				}
			}

			if (protRequest.getTotalProtein() != null) {
				Float value = appUtility.parseFloatAmount(protRequest.getTotalProtein());
				if (!value.equals(prot.getTotalProtein())) {
					protLogMsg = appUtility.formatUpdateData(protLogMsg, "TotalProtein",
							String.valueOf(prot.getTotalProtein()), String.valueOf(value));
					prot.setTotalProtein(value);
				}
			} else {
				if (prot.getTotalProtein() != null) {
					protLogMsg = appUtility.formatUpdateData(protLogMsg, "TotalProtein",
							String.valueOf(prot.getTotalProtein()), null);
					prot.setTotalProtein(null);
				}
			}

			if (protRequest.getAlbumin() != null) {
				Float value = appUtility.parseFloatAmount(protRequest.getAlbumin());
				if (!value.equals(prot.getAlbumin())) {
					protLogMsg = appUtility.formatUpdateData(protLogMsg, "Albumin", String.valueOf(prot.getAlbumin()),
							String.valueOf(value));
					prot.setAlbumin(value);
				}
			} else {
				if (prot.getAlbumin() != null) {
					protLogMsg = appUtility.formatUpdateData(protLogMsg, "Albumin", String.valueOf(prot.getAlbumin()),
							null);
					prot.setAlbumin(null);
				}
			}

			if (protRequest.getGlobulin() != null) {
				Float value = appUtility.parseFloatAmount(protRequest.getGlobulin());
				if (!value.equals(prot.getGlobulin())) {
					protLogMsg = appUtility.formatUpdateData(protLogMsg, "Globulin", String.valueOf(prot.getGlobulin()),
							String.valueOf(value));
					prot.setGlobulin(value);
				}
			} else {
				if (prot.getGlobulin() != null) {
					protLogMsg = appUtility.formatUpdateData(protLogMsg, "Globulin", String.valueOf(prot.getGlobulin()),
							null);
					prot.setGlobulin(null);
				}
			}

			if (protRequest.getAGRatio() != null) {
				Float value = appUtility.parseFloatAmount(protRequest.getAGRatio());
				if (!value.equals(prot.getAGRatio())) {
					protLogMsg = appUtility.formatUpdateData(protLogMsg, "AGRatio", String.valueOf(prot.getAGRatio()),
							String.valueOf(value));
					prot.setAGRatio(value);
				}
			} else {
				if (prot.getAGRatio() != null) {
					protLogMsg = appUtility.formatUpdateData(protLogMsg, "AGRatio", String.valueOf(prot.getAGRatio()),
							null);
					prot.setAGRatio(null);
				}
			}
		}

		if (!"".equals(protLogMsg)) {
			qisTransactionLabProteinRepository.save(prot);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, protLogMsg, laboratoryId, CATEGORY + "-PROT");
		}

		if (isAdded) {
			return prot;
		}

		return null;
	}

}
