package quest.phil.diagnostic.information.system.ws.common;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quest.phil.diagnostic.information.system.ws.controller.QisTransactionLaboratoryHematologyController;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratoryItems;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabAPTT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabBloodTyping;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabCBC;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabCTBT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabESR;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabFerritin;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabPRMS;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabProthrombinTime;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabRCT;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabHERequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHEAPTTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHEBTypRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHECBCRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHECTBTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHEESRRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHEPMTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHEPRMSRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHERCTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.he.QisTransactionLabHeFerritinRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisReferenceLaboratoryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionHematologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;
import quest.phil.diagnostic.information.system.ws.repository.he.QisTransactionLabAPTTRepository;
import quest.phil.diagnostic.information.system.ws.repository.he.QisTransactionLabBTypRepository;
import quest.phil.diagnostic.information.system.ws.repository.he.QisTransactionLabCBCRepository;
import quest.phil.diagnostic.information.system.ws.repository.he.QisTransactionLabCTBTRepository;
import quest.phil.diagnostic.information.system.ws.repository.he.QisTransactionLabESRRepository;
import quest.phil.diagnostic.information.system.ws.repository.he.QisTransactionLabFerritinRepository;
import quest.phil.diagnostic.information.system.ws.repository.he.QisTransactionLabPRMSRepository;
import quest.phil.diagnostic.information.system.ws.repository.he.QisTransactionLabPTimeRepository;
import quest.phil.diagnostic.information.system.ws.repository.he.QisTransactionLabRCTRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@Component
public class AppLaboratoryHEUtility {
	private final String CATEGORY = "HE";

	// HEMATOLOGY
	@Autowired
	private QisTransactionHematologyRepository qisTransactionHematologyRepository;

	@Autowired
	private QisTransactionLabCBCRepository qisTransactionLabCBCRepository;

	@Autowired
	private QisTransactionLabBTypRepository qisTransactionLabBTypRepository;

	@Autowired
	private QisTransactionLabCTBTRepository qisTransactionLabCTBTRepository;

	@Autowired
	private QisTransactionLabPTimeRepository qisTransactionLabPTimeRepository;

	@Autowired
	private QisTransactionLabPRMSRepository qisTransactionLabPRMSRepository;

	@Autowired
	private QisTransactionLabAPTTRepository qisTransactionLabAPTTRepository;

	@Autowired
	private QisTransactionLabESRRepository qisTransactionLabESRRepository;

	@Autowired
	private QisTransactionLabFerritinRepository qisTransactionFerritinRepository;

	@Autowired
	private QisTransactionLabRCTRepository qisTransactionRctRepository;
	
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
	 * HEMATOLOGY
	 */
	public void validateHematologyRequest(Long laboratoryId, Set<QisLaboratoryProcedureService> serviceRequest,
			QisTransactionLabHERequest heRequest) throws Exception {

		if (heRequest == null) {
			throw new RuntimeException("Hematology request is required.",
					new Throwable("hematology[" + laboratoryId + "]: Hematology is required."));
		}

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(heRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		for (QisLaboratoryProcedureService service : serviceRequest) {
			switch (service.getLaboratoryRequest().toString()) {
			case "CBC": {
				if (heRequest.getCbc() == null) {
					throw new RuntimeException("Complete Blood Count is required.",
							new Throwable("cbc: Complete Blood Count is required."));
				}

				QisTransactionLabHECBCRequest cbcRequest = heRequest.getCbc();
				if (cbcRequest.getWhiteBloodCells() != null) {
					Float wbc = appUtility.parseFloatAmount(cbcRequest.getWhiteBloodCells());
					if (wbc == null) {
						throw new RuntimeException(
								"Invalid White Blood Cells value[" + cbcRequest.getWhiteBloodCells() + "].",
								new Throwable("cbc.whiteBloodCells: Invalid White Blood Cells value["
										+ cbcRequest.getWhiteBloodCells() + "]."));
					}
				}
				if (cbcRequest.getBasophils() != null) {
					Float bas = appUtility.parseFloatAmount(cbcRequest.getBasophils());
					if (bas == null) {
						throw new RuntimeException("Invalid Basophils value[" + cbcRequest.getBasophils() + "].",
								new Throwable("cbc.whiteBloodCells: Invalid Basophils value["
										+ cbcRequest.getBasophils() + "]."));
					}
				}
				if (cbcRequest.getNeutrophils() != null) {
					Float neu = appUtility.parseFloatAmount(cbcRequest.getNeutrophils());
					if (neu == null) {
						throw new RuntimeException("Invalid Neutrophils value[" + cbcRequest.getNeutrophils() + "].",
								new Throwable("cbc.whiteBloodCells: Invalid Neutrophils value["
										+ cbcRequest.getNeutrophils() + "]."));
					}
				}
				if (cbcRequest.getRedBloodCells() != null) {
					Float rbc = appUtility.parseFloatAmount(cbcRequest.getRedBloodCells());
					if (rbc == null) {
						throw new RuntimeException(
								"Invalid Red Blood Cells value[" + cbcRequest.getRedBloodCells() + "].",
								new Throwable("cbc.whiteBloodCells: Invalid Red Blood Cells value["
										+ cbcRequest.getRedBloodCells() + "]."));
					}
				}
				if (cbcRequest.getLymphocytes() != null) {
					Float lymp = appUtility.parseFloatAmount(cbcRequest.getLymphocytes());
					if (lymp == null) {
						throw new RuntimeException("Invalid Lymphocytes value[" + cbcRequest.getLymphocytes() + "].",
								new Throwable("cbc.whiteBloodCells: Invalid Lymphocytes value["
										+ cbcRequest.getLymphocytes() + "]."));
					}
				}
				if (cbcRequest.getHemoglobin() != null) {
					Float hemo = appUtility.parseFloatAmount(cbcRequest.getHemoglobin());
					if (hemo == null) {
						throw new RuntimeException("Invalid Hemoglobin value[" + cbcRequest.getHemoglobin() + "].",
								new Throwable("cbc.whiteBloodCells: Invalid Hemoglobin value["
										+ cbcRequest.getHemoglobin() + "]."));
					}
				}
				if (cbcRequest.getMonocytes() != null) {
					Float mono = appUtility.parseFloatAmount(cbcRequest.getMonocytes());
					if (mono == null) {
						throw new RuntimeException("Invalid Monocytes value[" + cbcRequest.getMonocytes() + "].",
								new Throwable("cbc.whiteBloodCells: Invalid Monocytes value["
										+ cbcRequest.getMonocytes() + "]."));
					}
				}
				if (cbcRequest.getHematocrit() != null) {
					Float hema = appUtility.parseFloatAmount(cbcRequest.getHematocrit());
					if (hema == null) {
						throw new RuntimeException("Invalid Hematocrit value[" + cbcRequest.getHematocrit() + "].",
								new Throwable("cbc.whiteBloodCells: Invalid Hematocrit value["
										+ cbcRequest.getHematocrit() + "]."));
					}
				}
				if (cbcRequest.getEosinophils() != null) {
					Float eos = appUtility.parseFloatAmount(cbcRequest.getEosinophils());
					if (eos == null) {
						throw new RuntimeException("Invalid Eosinophils value[" + cbcRequest.getEosinophils() + "].",
								new Throwable("cbc.whiteBloodCells: Invalid Eosinophils value["
										+ cbcRequest.getEosinophils() + "]."));
					}
				}
				if (cbcRequest.getPlatelet() != null) {
					Float pla = appUtility.parseFloatAmount(cbcRequest.getPlatelet());
					if (pla == null) {
						throw new RuntimeException("Invalid Platelet value[" + cbcRequest.getPlatelet() + "].",
								new Throwable("cbc.whiteBloodCells: Invalid Platelet value[" + cbcRequest.getPlatelet()
										+ "]."));
					}
				}
			}
				break;
			case "BTYP": {
				if (heRequest.getBloodTyping() == null) {
					throw new RuntimeException("Blood Typing is required.",
							new Throwable("bloodTyping: Blood Typing is required."));
				}

				QisTransactionLabHEBTypRequest btRequest = heRequest.getBloodTyping();
				if (btRequest.getBloodType() != null) {
					String[] strings = { "A", "O", "B", "AB" };
					if (!Arrays.stream(strings).anyMatch(t -> t.equals(btRequest.getBloodType()))) {
						throw new RuntimeException("Invalid Blood Type value[" + btRequest.getBloodType() + "].",
								new Throwable(
										"bloodTyping: Invalid Blood Type value[" + btRequest.getBloodType() + "]."));
					}

				}
			}
				break;

			case "CTM":
			case "BTM": {
				if (heRequest.getCtbt() == null) {
					throw new RuntimeException("Clotting/Bleeding Time is required.",
							new Throwable("ctbt: Clotting/Bleeding Time is required."));
				}

				QisTransactionLabHECTBTRequest ctbtRequest = heRequest.getCtbt();

				if ("CTM".equals(service.getLaboratoryRequest().toString())) {
					if (ctbtRequest.getClottingTimeMin() != null) {
						Integer min = appUtility.parseIngerValue(ctbtRequest.getClottingTimeMin());
						if (min == null) {
							throw new RuntimeException(
									"Invalid Clotting Time Minute value[" + ctbtRequest.getClottingTimeMin() + "].",
									new Throwable("ctbt.clottingTimeMin: Invalid Clotting Time Minute value["
											+ ctbtRequest.getClottingTimeMin() + "]."));
						}
					}
				} else if ("BTM".equals(service.getLaboratoryRequest().toString())) {
					if (ctbtRequest.getBleedingTimeMin() != null) {
						Integer min = appUtility.parseIngerValue(ctbtRequest.getBleedingTimeMin());
						if (min == null) {
							throw new RuntimeException(
									"Invalid Bleeding Time Minute value[" + ctbtRequest.getBleedingTimeMin() + "].",
									new Throwable("ctbt.bleedingTimeMin: Invalid Bleeding Time Minute value["
											+ ctbtRequest.getBleedingTimeMin() + "]."));
						}
					}
				}
			}
				break;
			case "PTM": {
				if (heRequest.getProthrombinTime() == null) {
					throw new RuntimeException("Prothrombin Time is required.",
							new Throwable("prothrombinTime: Prothrombin Time is required."));
				}

				QisTransactionLabHEPMTRequest pmtRequest = heRequest.getProthrombinTime();
				if (pmtRequest.getPatientTime() != null) {
					Float pt = appUtility.parseFloatAmount(pmtRequest.getPatientTime());
					if (pt == null) {
						throw new RuntimeException("Invalid Patient Time value[" + pmtRequest.getPatientTime() + "].",
								new Throwable("prothrombinTime.patientTime: Invalid Patient Time value["
										+ pmtRequest.getPatientTime() + "]."));
					}
				}

				if (pmtRequest.getControl() != null) {
					Float con = appUtility.parseFloatAmount(pmtRequest.getControl());
					if (con == null) {
						throw new RuntimeException("Invalid Control value[" + pmtRequest.getControl() + "].",
								new Throwable("prothrombinTime.control: Invalid Control value["
										+ pmtRequest.getControl() + "]."));
					}
				}

				if (pmtRequest.getPercentActivity() != null) {
					Float per = appUtility.parseFloatAmount(pmtRequest.getPercentActivity());
					if (per == null) {
						throw new RuntimeException(
								"Invalid Percent Activity value[" + pmtRequest.getPercentActivity() + "].",
								new Throwable("prothrombinTime.percentActivity: Invalid Percent Activity value["
										+ pmtRequest.getPercentActivity() + "]."));
					}
				}

				if (pmtRequest.getInr() != null) {
					Float inr = appUtility.parseFloatAmount(pmtRequest.getInr());
					if (inr == null) {
						throw new RuntimeException("Invalid INR value[" + pmtRequest.getInr() + "].",
								new Throwable("prothrombinTime.inr: Invalid INR value[" + pmtRequest.getInr() + "]."));
					}
				}
			}
				break;
			case "PR131":
			case "MASM": {
				if (heRequest.getPrms() == null) {
					throw new RuntimeException("PR 1.31/Malarial Smear is required.",
							new Throwable("prms: PR 1.31/Malarial Smear is required."));
				}

				QisTransactionLabHEPRMSRequest prmsRequest = heRequest.getPrms();
				if ("PR131".equals(service.getLaboratoryRequest().toString())) {
					if (prmsRequest.getPr131() != null) {
						Float pr = appUtility.parseFloatAmount(prmsRequest.getPr131());
						if (pr == null) {
							throw new RuntimeException("Invalid PR 1.31 value[" + prmsRequest.getPr131() + "].",
									new Throwable(
											"prms.pr131: Invalid PR 1.31 value[" + prmsRequest.getPr131() + "]."));
						}
					}
				} else if ("MASM".equals(service.getLaboratoryRequest().toString())) {
				}
			}
				break;
			case "APTT": {
				if (heRequest.getAptt() == null) {
					throw new RuntimeException("Activated Partial Thromboplastin Time ( APTT ) is required.",
							new Throwable("aptt: Activated Partial Thromboplastin Time ( APTT ) is required."));
				}

				QisTransactionLabHEAPTTRequest apttRequest = heRequest.getAptt();
				if (apttRequest.getPatientTime() != null) {
					Float pt = appUtility.parseFloatAmount(apttRequest.getPatientTime());
					if (pt == null) {
						throw new RuntimeException("Invalid Patient Time value[" + apttRequest.getPatientTime() + "].",
								new Throwable("aptt.patientTime: Invalid Patient Time value["
										+ apttRequest.getPatientTime() + "]."));
					}
				}

				if (apttRequest.getControl() != null) {
					Float con = appUtility.parseFloatAmount(apttRequest.getControl());
					if (con == null) {
						throw new RuntimeException("Invalid Control value[" + apttRequest.getControl() + "].",
								new Throwable(
										"aptt.control: Invalid Control value[" + apttRequest.getControl() + "]."));
					}
				}
			}
				break;
			case "ESR": {
				if (heRequest.getEsr() == null) {
					throw new RuntimeException("Erythrocyte Sedimentation Rate ( ESR ) is required.",
							new Throwable("esr: Erythrocyte Sedimentation Rate ( ESR ) is required."));
				}

				QisTransactionLabHEESRRequest esrRequest = heRequest.getEsr();
				if (esrRequest.getRate() != null) {
					Float ra = appUtility.parseFloatAmount(esrRequest.getRate());
					if (ra == null) {
						throw new RuntimeException("Invalid Rate value[" + esrRequest.getRate() + "].",
								new Throwable("esr.rate: Invalid Rate value[" + esrRequest.getRate() + "]."));
					}
				}

				if (esrRequest.getMethod() != null) {
					String[] strings = { "WINTROBE", "WESTERGREN" };
					if (!Arrays.stream(strings).anyMatch(t -> t.equals(esrRequest.getMethod()))) {
						throw new RuntimeException("Invalid Method value[" + esrRequest.getMethod() + "].",
								new Throwable("esr.method: Invalid Method value[" + esrRequest.getMethod() + "]."));
					}
				}
			}
				break;
			case "FERR": {
				if (heRequest.getFerritin() == null) {
					throw new RuntimeException("Ferritin is required.",
							new Throwable("Ferritin: Ferritin is required."));
				}

				QisTransactionLabHeFerritinRequest esrRequest = heRequest.getFerritin();
				if (esrRequest.getResult() != null) {
					if (esrRequest.getResult() == null) {
						throw new RuntimeException("Invalid Rate value[" + esrRequest.getResult() + "].",
								new Throwable("Ferritin: Invalid Ferritin value[" + esrRequest.getResult() + "]."));
					}
				}
			}
				break;

			case "RCT": {
				if (heRequest.getRtc() == null) {
					throw new RuntimeException("RETICULOCYTE COUNT is required.",
							new Throwable("RETICULOCYTE COUNT: RETICULOCYTE COUNT is required."));
				}

				QisTransactionLabHERCTRequest rctRequest = heRequest.getRtc();
				if (rctRequest.getResult() != null) {
					if (rctRequest.getResult() == null) {
						throw new RuntimeException("Invalid Rate value[" + rctRequest.getResult() + "].", new Throwable(
								"RETICULOCYTE COUNT: Invalid Ferritin value[" + rctRequest.getResult() + "]."));
					}
				}
			}
				break;
			} // switch
		} // for loop
	}

	public void saveHematology(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionHematology qisTransactionHematology, Set<QisLaboratoryProcedureService> serviceRequest,
			QisTransactionLabHERequest heRequest, QisUserDetails authUser, QisDoctor qisDoctor) {
		boolean isCTBT = true;
		boolean isPRMS = true;

		String referenceLab1 = "";
		if (heRequest.getAptt() != null) {
			referenceLab1 = heRequest.getAptt().getReferenceLabId();
		}
		if (heRequest.getBloodTyping() != null) {
			referenceLab1 = heRequest.getBloodTyping().getReferenceLabId();
		}
		if (heRequest.getCbc() != null) {
			referenceLab1 = heRequest.getCbc().getReferenceLabId();
		}
		if (heRequest.getCtbt() != null) {
			referenceLab1 = heRequest.getCtbt().getReferenceLabId();
		}
		if (heRequest.getEsr() != null) {
			referenceLab1 = heRequest.getEsr().getReferenceLabId();
		}
		if (heRequest.getPrms() != null) {
			referenceLab1 = heRequest.getPrms().getReferenceLabId();
		}
		if (heRequest.getProthrombinTime() != null) {
			referenceLab1 = heRequest.getProthrombinTime().getReferenceLabId();
		}
		if (heRequest.getFerritin() != null) {
			referenceLab1 = heRequest.getFerritin().getReferenceLabId();
		}

		for (QisLaboratoryProcedureService service : serviceRequest) {
			switch (service.getLaboratoryRequest().toString()) {
			case "CBC": {
				QisTransactionLabCBC cbc = saveCBC(qisTransaction, laboratoryId, heRequest, authUser, referenceLab1,
						qisTransactionHematology.getItemDetails().getItemid());
				if (cbc != null) {
					qisTransactionHematology.setCbc(cbc);
				}
			}
				break;
			case "BTYP": {
				QisTransactionLabBloodTyping bloodTyping = saveBloodTyping(qisTransaction, laboratoryId, heRequest,
						authUser, referenceLab1, qisTransactionHematology.getItemDetails().getItemid());
				if (bloodTyping != null) {
					qisTransactionHematology.setBloodTyping(bloodTyping);
				}
			}
				break;
			case "CTM":
			case "BTM": {
				if (isCTBT) {
					QisTransactionLabCTBT ctbt = saveCTBT(qisTransaction, laboratoryId, heRequest, authUser,
							referenceLab1, qisTransactionHematology.getItemDetails().getItemid());
					if (ctbt != null) {
						qisTransactionHematology.setCtbt(ctbt);
					}
					isCTBT = false;
				}
			}
				break;
			case "PTM": {
				QisTransactionLabProthrombinTime ptime = saveProthrombinTime(qisTransaction, laboratoryId, heRequest,
						authUser, referenceLab1, qisTransactionHematology.getItemDetails().getItemid());
				if (ptime != null) {
					qisTransactionHematology.setProthombinTime(ptime);
				}
			}
				break;
			case "PR131":
			case "MASM": {
				if (isPRMS) {
					QisTransactionLabPRMS prms = savePRMS(qisTransaction, laboratoryId, heRequest, authUser,
							referenceLab1, qisTransactionHematology.getItemDetails().getItemid());
					if (prms != null) {
						qisTransactionHematology.setPrms(prms);
					}
					isPRMS = false;
				}
			}
				break;
			case "APTT": {
				QisTransactionLabAPTT aptt = saveAPTT(qisTransaction, laboratoryId, heRequest, authUser, referenceLab1,
						qisTransactionHematology.getItemDetails().getItemid());
				if (aptt != null) {
					qisTransactionHematology.setAptt(aptt);
				}
			}
				break;
			case "ESR": {
				QisTransactionLabESR esr = saveESR(qisTransaction, laboratoryId, heRequest, authUser, referenceLab1,
						qisTransactionHematology.getItemDetails().getItemid());
				if (esr != null) {
					qisTransactionHematology.setEsr(esr);
				}
			}
				break;

			case "FERR": {
				QisTransactionLabFerritin ferritin = saveFERR(qisTransaction, laboratoryId, heRequest, authUser,
						referenceLab1, qisTransactionHematology.getItemDetails().getItemid());
				if (ferritin != null) {
					qisTransactionHematology.setFerritin(ferritin);
				}
			}
				break;

			case "RCT": {
				QisTransactionLabRCT rct = saveRCT(qisTransaction, laboratoryId, heRequest, authUser,
						referenceLab1, qisTransactionHematology.getItemDetails().getItemid());
				if (rct != null) {
					qisTransactionHematology.setRct(rct);
				}
			}
				break;
			} // switch
		} // for loop

		int isUpdate = 0;
		if (!qisTransactionHematology.isSubmitted()) {
			qisTransactionHematology.setSubmitted(true);
			qisTransactionHematology.setVerifiedBy(authUser.getId());
			qisTransactionHematology.setVerifiedDate(Calendar.getInstance());
			isUpdate += 1;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					"SUBMITTED", qisTransactionHematology.getItemDetails().getItemName(), laboratoryId, CATEGORY);
		}

		if (qisTransactionHematology.getLabPersonelId() == null) {
			qisTransactionHematology.setLabPersonelId(authUser.getId());
			qisTransactionHematology.setLabPersonelDate(Calendar.getInstance());
			qisTransactionHematology.setStatus(2);
			isUpdate += 2;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					"LAB PERSONEL", authUser.getUsername(), laboratoryId, CATEGORY);
		}

		String onLogMsg = "";
		String action = "ADDED";
		if (heRequest.getOtherNotes() != null) {
			if (qisTransactionHematology.getOtherNotes() == null) {
				qisTransactionHematology.setOtherNotes(heRequest.getOtherNotes());
				onLogMsg = heRequest.getOtherNotes();
			} else if (!heRequest.getOtherNotes().equals(qisTransactionHematology.getOtherNotes())) {
				qisTransactionHematology.setOtherNotes(heRequest.getOtherNotes());
				action = "UPDATED";
				onLogMsg = appUtility.formatUpdateData(onLogMsg, "OtherNotes", qisTransactionHematology.getOtherNotes(),
						heRequest.getOtherNotes());
			}
		} else {
			if (qisTransactionHematology.getOtherNotes() != null) {
				qisTransactionHematology.setOtherNotes(null);
				action = "UPDATED";
				onLogMsg = appUtility.formatUpdateData(onLogMsg, "OtherNotes", qisTransactionHematology.getOtherNotes(),
						null);
			}
		}

		if (!"".equals(onLogMsg)) {
			isUpdate += 4;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, onLogMsg, laboratoryId, CATEGORY + "-OTHER NOTES");
		}

		onLogMsg = "";
		action = "ADDED";
		if (qisTransactionHematology.getMedicalDoctorId() == null) {
			qisTransactionHematology.setMedicalDoctor(qisDoctor);
			qisTransactionHematology.setMedicalDoctorId(qisDoctor.getId());
			onLogMsg = qisTransactionHematology.getMedicalDoctor().getDoctorid();
		} else if (qisDoctor.getId() != qisTransactionHematology.getMedicalDoctorId()) {
			qisTransactionHematology.setMedicalDoctor(qisDoctor);
			qisTransactionHematology.setMedicalDoctorId(qisDoctor.getId());

			action = "UPDATED";
			onLogMsg = appUtility.formatUpdateData(onLogMsg, "pathologist",
					qisTransactionHematology.getMedicalDoctor().getDoctorid(), qisDoctor.getDoctorid());
		}

		if (!"".equals(onLogMsg)) {
			isUpdate += 8;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, onLogMsg, laboratoryId, CATEGORY + "-PATHOLOGIST");
		}

		if (isUpdate > 0) {
			qisTransactionHematologyRepository.save(qisTransactionHematology);

			QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
			if ((isUpdate & 1) > 0) {
				qisTransactionHematology.setVerified(labPersonel);

			}
			if ((isUpdate & 2) > 0) {
				qisTransactionHematology.setLabPersonel(labPersonel);
			}
		}

	}

	

	public void saveHematologyQC(QisTransactionHematology qisHematology, Long laboratoryId, QisUserDetails authUser) {
		qisHematology.setQcId(authUser.getId());
		qisHematology.setQcDate(Calendar.getInstance());
		qisHematology.setStatus(3); // Quality Control
		qisTransactionHematologyRepository.save(qisHematology);

		QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
		qisHematology.setQualityControl(labPersonel);

		qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
				"UPDATE", "QC Hematology", laboratoryId, CATEGORY);
	}
	
	private QisTransactionLabRCT saveRCT(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabHERequest heRequest, QisUserDetails authUser, String referenceLab1, String itemid) {

		String action = "ADDED";
		boolean isAdded = false;
		String rctLogMsg = "";

		QisTransactionLabRCT rct = qisTransactionRctRepository
				.getTransactionLabRctByLabReqId(laboratoryId);

		QisTransactionLabHERCTRequest rctRequest = heRequest.getRtc();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(rctRequest.getReferenceLabId());

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

		if (rct == null) {
			rct = new QisTransactionLabRCT();
			BeanUtils.copyProperties(heRequest.getRtc(), rct);
			rct.setId(laboratoryId);
			rct.setCreatedBy(authUser.getId());
			rct.setUpdatedBy(authUser.getId());

			if (rctRequest.getResult() != null) {
				rct.setResult(rctRequest.getResult());
			}
			if (referenceLab != null) {
				rct.setReferenceLab(referenceLab);
				rct.setReferenceLabId(referenceLab.getId());
			}
			rctLogMsg = heRequest.getRtc().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			rct.setUpdatedBy(authUser.getId());
			rct.setUpdatedAt(Calendar.getInstance());

			if (rctRequest.getReferenceLabId() != null) {
				if (rct.getReferenceLab() != null) {
					if (rctRequest.getReferenceLabId() != rct.getReferenceLab().getReferenceid()) {
						rctLogMsg = appUtility.formatUpdateData(rctLogMsg, "Reference Laboratory",
								String.valueOf(rct.getReferenceLab().getReferenceid()),
								String.valueOf(rctRequest.getReferenceLabId()));
						if (referenceLab != null) {
							rct.setReferenceLab(referenceLab);
							rct.setReferenceLabId(referenceLab.getId());
						} else {
							rct.setReferenceLab(null);
							rct.setReferenceLabId(null);
						}
					}
				} else {
					rctLogMsg = appUtility.formatUpdateData(rctLogMsg, "Reference Laboratory",
							String.valueOf(""), String.valueOf(rctRequest.getReferenceLabId()));
					if (referenceLab != null) {
						rct.setReferenceLab(referenceLab);
						rct.setReferenceLabId(referenceLab.getId());
					} else {
						rct.setReferenceLab(null);
						rct.setReferenceLabId(null);
					}
				}
			} else {
				if (rct.getReferenceLab() != null) {
					rctLogMsg = appUtility.formatUpdateData(rctLogMsg, "Reference Laboratory",
							String.valueOf(rct.getReferenceLab().getReferenceid()), null);
					rct.setReferenceLab(null);
					rct.setReferenceLabId(null);
				}
			}

			if (rctRequest.getResult() != null) {
				if (!rctRequest.getResult().equals(rct.getResult())) {
					rctLogMsg = appUtility.formatUpdateData(rctLogMsg, "Ferritin",
							String.valueOf(rct.getResult()), String.valueOf(rctRequest.getResult()));
					rct.setResult(rctRequest.getResult());
				}

			} else {
				if (rct.getResult() != null) {
					rctLogMsg = appUtility.formatUpdateData(rctLogMsg, "Ferritin",
							String.valueOf(rct.getResult()), null);
					rct.setResult(null);
				}
			}

		}

		if (!"".equals(rctLogMsg)) {
			qisTransactionRctRepository.save(rct);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, rctLogMsg, laboratoryId, CATEGORY + "-Ferritin");
		}

		if (isAdded) {
			return rct;
		}
		return null;
	}

	private QisTransactionLabFerritin saveFERR(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabHERequest heRequest, QisUserDetails authUser, String referenceLab1, String itemid) {
		String action = "ADDED";
		boolean isAdded = false;
		String ferritinLogMsg = "";

		QisTransactionLabFerritin ferritin = qisTransactionFerritinRepository
				.getTransactionLabFerritinByLabReqId(laboratoryId);

		QisTransactionLabHeFerritinRequest ferrRequest = heRequest.getFerritin();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(ferrRequest.getReferenceLabId());

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

		if (ferritin == null) {
			ferritin = new QisTransactionLabFerritin();
			BeanUtils.copyProperties(heRequest.getFerritin(), ferritin);
			ferritin.setId(laboratoryId);
			ferritin.setCreatedBy(authUser.getId());
			ferritin.setUpdatedBy(authUser.getId());

			if (ferrRequest.getResult() != null) {
				ferritin.setResult(ferrRequest.getResult());
			}
			if (referenceLab != null) {
				ferritin.setReferenceLab(referenceLab);
				ferritin.setReferenceLabId(referenceLab.getId());
			}
			ferritinLogMsg = heRequest.getFerritin().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			ferritin.setUpdatedBy(authUser.getId());
			ferritin.setUpdatedAt(Calendar.getInstance());

			if (ferrRequest.getReferenceLabId() != null) {
				if (ferritin.getReferenceLab() != null) {
					if (ferrRequest.getReferenceLabId() != ferritin.getReferenceLab().getReferenceid()) {
						ferritinLogMsg = appUtility.formatUpdateData(ferritinLogMsg, "Reference Laboratory",
								String.valueOf(ferritin.getReferenceLab().getReferenceid()),
								String.valueOf(ferrRequest.getReferenceLabId()));
						if (referenceLab != null) {
							ferritin.setReferenceLab(referenceLab);
							ferritin.setReferenceLabId(referenceLab.getId());
						} else {
							ferritin.setReferenceLab(null);
							ferritin.setReferenceLabId(null);
						}
					}
				} else {
					ferritinLogMsg = appUtility.formatUpdateData(ferritinLogMsg, "Reference Laboratory",
							String.valueOf(""), String.valueOf(ferrRequest.getReferenceLabId()));
					if (referenceLab != null) {
						ferritin.setReferenceLab(referenceLab);
						ferritin.setReferenceLabId(referenceLab.getId());
					} else {
						ferritin.setReferenceLab(null);
						ferritin.setReferenceLabId(null);
					}
				}
			} else {
				if (ferritin.getReferenceLab() != null) {
					ferritinLogMsg = appUtility.formatUpdateData(ferritinLogMsg, "Reference Laboratory",
							String.valueOf(ferritin.getReferenceLab().getReferenceid()), null);
					ferritin.setReferenceLab(null);
					ferritin.setReferenceLabId(null);
				}
			}

			if (ferrRequest.getResult() != null) {
				if (!ferrRequest.getResult().equals(ferritin.getResult())) {
					ferritinLogMsg = appUtility.formatUpdateData(ferritinLogMsg, "Ferritin",
							String.valueOf(ferritin.getResult()), String.valueOf(ferrRequest.getResult()));
					ferritin.setResult(ferrRequest.getResult());
				}

			} else {
				if (ferritin.getResult() != null) {
					ferritinLogMsg = appUtility.formatUpdateData(ferritinLogMsg, "Ferritin",
							String.valueOf(ferritin.getResult()), null);
					ferritin.setResult(null);
				}
			}

		}

		if (!"".equals(ferritinLogMsg)) {
			qisTransactionFerritinRepository.save(ferritin);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, ferritinLogMsg, laboratoryId, CATEGORY + "-Ferritin");
		}

		if (isAdded) {
			return ferritin;
		}
		return null;
	}

	private QisTransactionLabCBC saveCBC(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabHERequest heRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String cbcLogMsg = "";
		QisTransactionLabCBC cbc = qisTransactionLabCBCRepository.getTransactionLabCBCByLabReqId(laboratoryId);

		QisTransactionLabHECBCRequest cbcRequest = heRequest.getCbc();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(cbcRequest.getReferenceLabId());

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

		if (cbc == null) {
			cbc = new QisTransactionLabCBC();
			BeanUtils.copyProperties(heRequest.getCbc(), cbc);
			cbc.setId(laboratoryId);
			cbc.setCreatedBy(authUser.getId());
			cbc.setUpdatedBy(authUser.getId());

			if (cbcRequest.getWhiteBloodCells() != null) {
				Float wbc = appUtility.parseFloatAmount(cbcRequest.getWhiteBloodCells());
				cbc.setWhiteBloodCells(wbc);
			}
			if (cbcRequest.getBasophils() != null) {
				Float bas = appUtility.parseFloatAmount(cbcRequest.getBasophils());
				cbc.setBasophils(bas);
			}
			if (cbcRequest.getNeutrophils() != null) {
				Float neu = appUtility.parseFloatAmount(cbcRequest.getNeutrophils());
				cbc.setNeutrophils(neu);
			}
			if (cbcRequest.getRedBloodCells() != null) {
				Float rbc = appUtility.parseFloatAmount(cbcRequest.getRedBloodCells());
				cbc.setRedBloodCells(rbc);
			}
			if (cbcRequest.getLymphocytes() != null) {
				Float lymp = appUtility.parseFloatAmount(cbcRequest.getLymphocytes());
				cbc.setLymphocytes(lymp);
			}
			if (cbcRequest.getHemoglobin() != null) {
				Float hemo = appUtility.parseFloatAmount(cbcRequest.getHemoglobin());
				cbc.setHemoglobin(hemo);
			}
			if (cbcRequest.getMonocytes() != null) {
				Float mono = appUtility.parseFloatAmount(cbcRequest.getMonocytes());
				cbc.setMonocytes(mono);
			}
			if (cbcRequest.getHematocrit() != null) {
				Float hema = appUtility.parseFloatAmount(cbcRequest.getHematocrit());
				cbc.setHematocrit(hema);
			}
			if (cbcRequest.getEosinophils() != null) {
				Float eos = appUtility.parseFloatAmount(cbcRequest.getEosinophils());
				cbc.setEosinophils(eos);
			}
			if (cbcRequest.getPlatelet() != null) {
				Float pla = appUtility.parseFloatAmount(cbcRequest.getPlatelet());
				cbc.setPlatelet(pla);
			}
			if (referenceLab != null) {
				cbc.setReferenceLab(referenceLab);
				cbc.setReferenceLabId(referenceLab.getId());
			}

			cbcLogMsg = heRequest.getCbc().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			cbc.setUpdatedBy(authUser.getId());
			cbc.setUpdatedAt(Calendar.getInstance());

			if (cbcRequest.getReferenceLabId() != null) {
				if (cbc.getReferenceLab() != null) {
					if (cbcRequest.getReferenceLabId() != cbc.getReferenceLab().getReferenceid()) {
						cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Reference Laboratory",
								String.valueOf(cbc.getReferenceLab().getReferenceid()),
								String.valueOf(cbcRequest.getReferenceLabId()));
						if (referenceLab != null) {
							cbc.setReferenceLab(referenceLab);
							cbc.setReferenceLabId(referenceLab.getId());
						} else {
							cbc.setReferenceLab(null);
							cbc.setReferenceLabId(null);
						}
					}
				} else {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(cbcRequest.getReferenceLabId()));
					if (referenceLab != null) {
						cbc.setReferenceLab(referenceLab);
						cbc.setReferenceLabId(referenceLab.getId());
					} else {
						cbc.setReferenceLab(null);
						cbc.setReferenceLabId(null);
					}
				}
			} else {
				if (cbc.getReferenceLab() != null) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Reference Laboratory",
							String.valueOf(cbc.getReferenceLab().getReferenceid()), null);
					cbc.setReferenceLab(null);
					cbc.setReferenceLabId(null);
				}
			}

			if (cbcRequest.getWhiteBloodCells() != null) {
				Float wbc = appUtility.parseFloatAmount(cbcRequest.getWhiteBloodCells());
				if (!wbc.equals(cbc.getWhiteBloodCells())) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "WhiteBloodCells",
							String.valueOf(cbc.getWhiteBloodCells()), String.valueOf(wbc));
					cbc.setWhiteBloodCells(wbc);
				}

			} else {
				if (cbc.getWhiteBloodCells() != null) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "WhiteBloodCells",
							String.valueOf(cbc.getWhiteBloodCells()), null);
					cbc.setWhiteBloodCells(null);
				}
			}

			if (cbcRequest.getBasophils() != null) {
				Float bas = appUtility.parseFloatAmount(cbcRequest.getBasophils());
				if (!bas.equals(cbc.getBasophils())) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Basophils", String.valueOf(cbc.getBasophils()),
							String.valueOf(bas));
					cbc.setBasophils(bas);
				}

			} else {
				if (cbc.getBasophils() != null) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Basophils", String.valueOf(cbc.getBasophils()),
							null);
					cbc.setBasophils(null);
				}
			}

			if (cbcRequest.getNeutrophils() != null) {
				Float neu = appUtility.parseFloatAmount(cbcRequest.getNeutrophils());
				if (!neu.equals(cbc.getNeutrophils())) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Neutrophils",
							String.valueOf(cbc.getNeutrophils()), String.valueOf(neu));
					cbc.setNeutrophils(neu);
				}

			} else {
				if (cbc.getNeutrophils() != null) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Neutrophils",
							String.valueOf(cbc.getNeutrophils()), null);
					cbc.setNeutrophils(null);
				}
			}

			if (cbcRequest.getRedBloodCells() != null) {
				Float rbc = appUtility.parseFloatAmount(cbcRequest.getRedBloodCells());
				if (!rbc.equals(cbc.getRedBloodCells())) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "RedBloodCells",
							String.valueOf(cbc.getRedBloodCells()), String.valueOf(rbc));
					cbc.setRedBloodCells(rbc);
				}

			} else {
				if (cbc.getRedBloodCells() != null) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "RedBloodCells",
							String.valueOf(cbc.getRedBloodCells()), null);
					cbc.setRedBloodCells(null);
				}
			}

			if (cbcRequest.getLymphocytes() != null) {
				Float lymp = appUtility.parseFloatAmount(cbcRequest.getLymphocytes());
				if (!lymp.equals(cbc.getLymphocytes())) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Lymphocytes",
							String.valueOf(cbc.getLymphocytes()), String.valueOf(lymp));
					cbc.setLymphocytes(lymp);
				}

			} else {
				if (cbc.getLymphocytes() != null) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Lymphocytes",
							String.valueOf(cbc.getLymphocytes()), null);
					cbc.setLymphocytes(null);
				}
			}

			if (cbcRequest.getHemoglobin() != null) {
				Float hemo = appUtility.parseFloatAmount(cbcRequest.getHemoglobin());
				if (!hemo.equals(cbc.getHemoglobin())) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Hemoglobin",
							String.valueOf(cbc.getHemoglobin()), String.valueOf(hemo));
					cbc.setHemoglobin(hemo);
				}

			} else {
				if (cbc.getHemoglobin() != null) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Hemoglobin",
							String.valueOf(cbc.getHemoglobin()), null);
					cbc.setHemoglobin(null);
				}
			}

			if (cbcRequest.getMonocytes() != null) {
				Float mono = appUtility.parseFloatAmount(cbcRequest.getMonocytes());
				if (!mono.equals(cbc.getMonocytes())) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Monocytes", String.valueOf(cbc.getMonocytes()),
							String.valueOf(mono));
					cbc.setMonocytes(mono);
				}

			} else {
				if (cbc.getMonocytes() != null) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Monocytes", String.valueOf(cbc.getMonocytes()),
							null);
					cbc.setMonocytes(null);
				}
			}

			if (cbcRequest.getHematocrit() != null) {
				Float hema = appUtility.parseFloatAmount(cbcRequest.getHematocrit());
				if (!hema.equals(cbc.getHematocrit())) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Hematocrit",
							String.valueOf(cbc.getHematocrit()), String.valueOf(hema));
					cbc.setHematocrit(hema);
				}

			} else {
				if (cbc.getHematocrit() != null) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Hematocrit",
							String.valueOf(cbc.getHematocrit()), null);
					cbc.setHematocrit(null);
				}
			}

			if (cbcRequest.getEosinophils() != null) {
				Float eos = appUtility.parseFloatAmount(cbcRequest.getEosinophils());
				if (!eos.equals(cbc.getEosinophils())) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Eosinophils",
							String.valueOf(cbc.getEosinophils()), String.valueOf(eos));
					cbc.setEosinophils(eos);
				}

			} else {
				if (cbc.getEosinophils() != null) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Eosinophils",
							String.valueOf(cbc.getEosinophils()), null);
					cbc.setEosinophils(null);
				}
			}

			if (cbcRequest.getPlatelet() != null) {
				Float pla = appUtility.parseFloatAmount(cbcRequest.getPlatelet());
				if (!pla.equals(cbc.getPlatelet())) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Platelet", String.valueOf(cbc.getPlatelet()),
							String.valueOf(pla));
					cbc.setPlatelet(pla);
				}

			} else {
				if (cbc.getPlatelet() != null) {
					cbcLogMsg = appUtility.formatUpdateData(cbcLogMsg, "Platelet", String.valueOf(cbc.getPlatelet()),
							null);
					cbc.setPlatelet(null);
				}
			}
		}

		if (!"".equals(cbcLogMsg)) {
			qisTransactionLabCBCRepository.save(cbc);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, cbcLogMsg, laboratoryId, CATEGORY + "-CBC");
		}

		if (isAdded) {
			return cbc;
		}

		return null;
	}

	private QisTransactionLabBloodTyping saveBloodTyping(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabHERequest heRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String btLogMsg = "";
		QisTransactionLabBloodTyping bloodTyping = qisTransactionLabBTypRepository
				.getTransactionLabBloodTypingByLabReqId(laboratoryId);

		QisTransactionLabHEBTypRequest btRequest = heRequest.getBloodTyping();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(btRequest.getReferenceLabId());

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
		if (bloodTyping == null) {
			bloodTyping = new QisTransactionLabBloodTyping();
			BeanUtils.copyProperties(heRequest.getBloodTyping(), bloodTyping);
			bloodTyping.setId(laboratoryId);
			bloodTyping.setCreatedBy(authUser.getId());
			bloodTyping.setUpdatedBy(authUser.getId());
			if (referenceLab != null) {
				bloodTyping.setReferenceLab(referenceLab);
				bloodTyping.setReferenceLabId(referenceLab.getId());
			}
			btLogMsg = heRequest.getBloodTyping().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			bloodTyping.setUpdatedBy(authUser.getId());
			bloodTyping.setUpdatedAt(Calendar.getInstance());

//			QisTransactionLabHEBTypRequest btRequest = heRequest.getBloodTyping();

			if (!btRequest.getBloodType().equals(bloodTyping.getBloodType())) {
				btLogMsg = appUtility.formatUpdateData(btLogMsg, "BloodType", bloodTyping.getBloodType(),
						btRequest.getBloodType());
				bloodTyping.setBloodType(btRequest.getBloodType());
			}

			if (btRequest.getReferenceLabId() != null) {
				if (bloodTyping.getReferenceLab() != null) {
					if (btRequest.getReferenceLabId() != bloodTyping.getReferenceLab().getReferenceid()) {
						btLogMsg = appUtility.formatUpdateData(btLogMsg, "Reference Laboratory",
								String.valueOf(bloodTyping.getReferenceLab().getReferenceid()),
								String.valueOf(btRequest.getReferenceLabId()));
						if (referenceLab != null) {
							bloodTyping.setReferenceLab(referenceLab);
							bloodTyping.setReferenceLabId(referenceLab.getId());
						} else {
							bloodTyping.setReferenceLab(null);
							bloodTyping.setReferenceLabId(null);
						}
					}
				} else {
					btLogMsg = appUtility.formatUpdateData(btLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(btRequest.getReferenceLabId()));
					if (referenceLab != null) {
						bloodTyping.setReferenceLab(referenceLab);
						bloodTyping.setReferenceLabId(referenceLab.getId());
					} else {
						bloodTyping.setReferenceLab(null);
						bloodTyping.setReferenceLabId(null);
					}
				}
			} else {
				if (bloodTyping.getReferenceLab() != null) {
					btLogMsg = appUtility.formatUpdateData(btLogMsg, "Reference Laboratory",
							String.valueOf(bloodTyping.getReferenceLab().getReferenceid()), null);
					bloodTyping.setReferenceLab(null);
					bloodTyping.setReferenceLabId(null);
				}
			}

			if (btRequest.getRhesusFactor() != bloodTyping.getRhesusFactor()) {
				btLogMsg = appUtility.formatUpdateData(btLogMsg, "RhesusFactor",
						String.valueOf(bloodTyping.getRhesusFactor()), String.valueOf(btRequest.getRhesusFactor()));
				bloodTyping.setRhesusFactor(btRequest.getRhesusFactor());
			}
		}

		if (!"".equals(btLogMsg)) {
			qisTransactionLabBTypRepository.save(bloodTyping);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, btLogMsg, laboratoryId, CATEGORY + "-BLOODTYPING");
		}

		if (isAdded) {
			return bloodTyping;
		}

		return null;

	}

	private QisTransactionLabCTBT saveCTBT(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabHERequest heRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String ctbtLogMsg = "";
		QisTransactionLabCTBT ctbt = qisTransactionLabCTBTRepository.getTransactionLabCTBTByLabReqId(laboratoryId);

		QisTransactionLabHECTBTRequest ctbtRequest = heRequest.getCtbt();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(ctbtRequest.getReferenceLabId());

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

		if (ctbt == null) {
			ctbt = new QisTransactionLabCTBT();
			BeanUtils.copyProperties(heRequest.getCtbt(), ctbt);
			ctbt.setId(laboratoryId);
			ctbt.setCreatedBy(authUser.getId());
			ctbt.setUpdatedBy(authUser.getId());

			if (ctbtRequest.getClottingTimeMin() != null) {
				Integer min = appUtility.parseIngerValue(ctbtRequest.getClottingTimeMin());
				ctbt.setClottingTimeMin(min);

				if (ctbtRequest.getClottingTimeSec() == null) {
					ctbt.setClottingTimeSec(0);
				} else {
					Integer sec = appUtility.parseIngerValue(ctbtRequest.getClottingTimeSec());
					if (sec != null && sec >= 0 && sec <= 59) {
						ctbt.setClottingTimeSec(sec);
					} else {
						ctbt.setClottingTimeSec(0);
					}
				}
			}

			if (ctbtRequest.getBleedingTimeMin() != null) {
				Integer min = appUtility.parseIngerValue(ctbtRequest.getBleedingTimeMin());
				ctbt.setBleedingTimeMin(min);

				if (ctbtRequest.getBleedingTimeSec() == null) {
					ctbt.setBleedingTimeSec(0);
				} else {
					Integer sec = appUtility.parseIngerValue(ctbtRequest.getBleedingTimeSec());
					if (sec != null && sec >= 0 && sec <= 59) {
						ctbt.setBleedingTimeSec(sec);
					} else {
						ctbt.setBleedingTimeSec(0);
					}
				}
			}

			if (referenceLab != null) {
				ctbt.setReferenceLab(referenceLab);
				ctbt.setReferenceLabId(referenceLab.getId());
			}

			ctbtLogMsg = heRequest.getCtbt().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			ctbt.setUpdatedBy(authUser.getId());
			ctbt.setUpdatedAt(Calendar.getInstance());

			if (ctbtRequest.getReferenceLabId() != null) {
				if (ctbt.getReferenceLab() != null) {
					if (ctbtRequest.getReferenceLabId() != ctbt.getReferenceLab().getReferenceid()) {
						ctbtLogMsg = appUtility.formatUpdateData(ctbtLogMsg, "Reference Laboratory",
								String.valueOf(ctbt.getReferenceLab().getReferenceid()),
								String.valueOf(ctbtRequest.getReferenceLabId()));
						if (referenceLab != null) {
							ctbt.setReferenceLab(referenceLab);
							ctbt.setReferenceLabId(referenceLab.getId());
						} else {
							ctbt.setReferenceLab(null);
							ctbt.setReferenceLabId(null);
						}
					}
				} else {
					ctbtLogMsg = appUtility.formatUpdateData(ctbtLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(ctbtRequest.getReferenceLabId()));
					if (referenceLab != null) {
						ctbt.setReferenceLab(referenceLab);
						ctbt.setReferenceLabId(referenceLab.getId());
					} else {
						ctbt.setReferenceLab(null);
						ctbt.setReferenceLabId(null);
					}
				}
			} else {
				if (ctbt.getReferenceLab() != null) {
					ctbtLogMsg = appUtility.formatUpdateData(ctbtLogMsg, "Reference Laboratory",
							String.valueOf(ctbt.getReferenceLab().getReferenceid()), null);
					ctbt.setReferenceLab(null);
					ctbt.setReferenceLabId(null);
				}
			}

			if (ctbtRequest.getClottingTimeMin() != null) {
				Integer min = appUtility.parseIngerValue(ctbtRequest.getClottingTimeMin());
				if (min != ctbt.getClottingTimeMin()) {
					ctbtLogMsg = appUtility.formatUpdateData(ctbtLogMsg, "ClottingTimeMin",
							String.valueOf(ctbt.getClottingTimeMin()), String.valueOf(min));
					ctbt.setClottingTimeMin(min);
				}

				Integer sec = 0;
				if (ctbtRequest.getClottingTimeSec() != null) {
					sec = appUtility.parseIngerValue(ctbtRequest.getClottingTimeSec());
					if (sec != null && sec >= 0 && sec <= 59) {
					} else {
						sec = 0;
					}
				}

				if (sec != ctbt.getClottingTimeSec()) {
					ctbtLogMsg = appUtility.formatUpdateData(ctbtLogMsg, "ClottingTimeSec",
							String.valueOf(ctbt.getClottingTimeSec()), String.valueOf(sec));
					ctbt.setClottingTimeSec(sec);
				}
			}

			if (ctbtRequest.getBleedingTimeMin() != null) {
				Integer min = appUtility.parseIngerValue(ctbtRequest.getBleedingTimeMin());
				if (min != ctbt.getBleedingTimeMin()) {
					ctbtLogMsg = appUtility.formatUpdateData(ctbtLogMsg, "BleedingTimeMin",
							String.valueOf(ctbt.getBleedingTimeMin()), String.valueOf(min));
					ctbt.setBleedingTimeMin(min);
				}

				Integer sec = 0;
				if (ctbtRequest.getBleedingTimeSec() != null) {
					sec = appUtility.parseIngerValue(ctbtRequest.getBleedingTimeSec());
					if (sec != null && sec >= 0 && sec <= 59) {
					} else {
						sec = 0;
					}
				}

				if (sec != ctbt.getBleedingTimeSec()) {
					ctbtLogMsg = appUtility.formatUpdateData(ctbtLogMsg, "BleedingTimeSec",
							String.valueOf(ctbt.getBleedingTimeSec()), String.valueOf(sec));
					ctbt.setBleedingTimeSec(sec);
				}
			}
		}

		if (!"".equals(ctbtLogMsg)) {
			qisTransactionLabCTBTRepository.save(ctbt);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, ctbtLogMsg, laboratoryId, CATEGORY + "-CTBT");
		}

		if (isAdded) {
			return ctbt;
		}

		return null;
	}

	private QisTransactionLabProthrombinTime saveProthrombinTime(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabHERequest heRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String ptLogMsg = "";
		QisTransactionLabProthrombinTime ptime = qisTransactionLabPTimeRepository
				.getTransactionLabProthrombinTimeByLabReqId(laboratoryId);

		QisTransactionLabHEPMTRequest ptRequest = heRequest.getProthrombinTime();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(ptRequest.getReferenceLabId());

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

		if (ptime == null) {
			ptime = new QisTransactionLabProthrombinTime();
			BeanUtils.copyProperties(heRequest.getProthrombinTime(), ptime);
			ptime.setId(laboratoryId);
			ptime.setCreatedBy(authUser.getId());
			ptime.setUpdatedBy(authUser.getId());

			if (ptRequest.getPatientTime() != null) {
				Float tm = appUtility.parseFloatAmount(ptRequest.getPatientTime());
				ptime.setPatientTime(tm);
			}

			if (ptRequest.getControl() != null) {
				Float tm = appUtility.parseFloatAmount(ptRequest.getControl());
				ptime.setControl(tm);
			}

			if (ptRequest.getPercentActivity() != null) {
				Float tm = appUtility.parseFloatAmount(ptRequest.getPercentActivity());
				ptime.setPercentActivity(tm);
			}

			if (ptRequest.getInr() != null) {
				Float tm = appUtility.parseFloatAmount(ptRequest.getInr());
				ptime.setInr(tm);
			}

			if (referenceLab != null) {
				ptime.setReferenceLab(referenceLab);
				ptime.setReferenceLabId(referenceLab.getId());
			}
			ptLogMsg = heRequest.getProthrombinTime().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			ptime.setUpdatedBy(authUser.getId());
			ptime.setUpdatedAt(Calendar.getInstance());

			if (ptRequest.getReferenceLabId() != null) {
				if (ptime.getReferenceLab() != null) {
					if (ptRequest.getReferenceLabId() != ptime.getReferenceLab().getReferenceid()) {
						ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "Reference Laboratory",
								String.valueOf(ptime.getReferenceLab().getReferenceid()),
								String.valueOf(ptRequest.getReferenceLabId()));
						if (referenceLab != null) {
							ptime.setReferenceLab(referenceLab);
							ptime.setReferenceLabId(referenceLab.getId());
						} else {
							ptime.setReferenceLab(null);
							ptime.setReferenceLabId(null);
						}
					}
				} else {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(ptRequest.getReferenceLabId()));
					if (referenceLab != null) {
						ptime.setReferenceLab(referenceLab);
						ptime.setReferenceLabId(referenceLab.getId());
					} else {
						ptime.setReferenceLab(null);
						ptime.setReferenceLabId(null);
					}
				}
			} else {
				if (ptime.getReferenceLab() != null) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "Reference Laboratory",
							String.valueOf(ptime.getReferenceLab().getReferenceid()), null);
					ptime.setReferenceLab(null);
					ptime.setReferenceLabId(null);
				}
			}

			if (ptRequest.getPatientTime() != null) {
				Float tm = appUtility.parseFloatAmount(ptRequest.getPatientTime());
				if (!tm.equals(ptime.getPatientTime())) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "PatientTime",
							String.valueOf(ptime.getPatientTime()), String.valueOf(tm));
					ptime.setPatientTime(tm);
				}
			} else {
				if (ptime.getPatientTime() != null) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "PatientTime",
							String.valueOf(ptime.getPatientTime()), null);
					ptime.setPatientTime(null);
				}
			}

			if (ptRequest.getPatientTimeNV() != null) {
				if (!ptRequest.getPatientTimeNV().equals(ptime.getPatientTimeNV())) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "PatientTimeNV", ptime.getPatientTimeNV(),
							ptRequest.getPatientTimeNV());
					ptime.setPatientTimeNV(ptRequest.getPatientTimeNV());

				}
			} else {
				if (ptime.getPatientTimeNV() != null) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "PatientTimeNV", ptime.getPatientTimeNV(), null);
					ptime.setPatientTimeNV(null);
				}
			}

			if (ptRequest.getControl() != null) {
				Float tm = appUtility.parseFloatAmount(ptRequest.getControl());
				if (!tm.equals(ptime.getControl())) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "Control", String.valueOf(ptime.getControl()),
							String.valueOf(tm));
					ptime.setControl(tm);
				}
			} else {
				if (ptime.getControl() != null) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "Control", String.valueOf(ptime.getControl()),
							null);
					ptime.setControl(null);
				}
			}

			if (ptRequest.getControlNV() != null) {
				if (!ptRequest.getControlNV().equals(ptime.getControlNV())) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "ControlNV", ptime.getControlNV(),
							ptRequest.getControlNV());
					ptime.setControlNV(ptRequest.getControlNV());

				}
			} else {
				if (ptime.getControlNV() != null) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "ControlNV", ptime.getControlNV(), null);
					ptime.setControlNV(null);
				}
			}

			if (ptRequest.getPercentActivity() != null) {
				Float tm = appUtility.parseFloatAmount(ptRequest.getPercentActivity());
				if (!tm.equals(ptime.getPercentActivity())) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "PercentActivity",
							String.valueOf(ptime.getPercentActivity()), String.valueOf(tm));
					ptime.setPercentActivity(tm);
				}
			} else {
				if (ptime.getPercentActivity() != null) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "PercentActivity",
							String.valueOf(ptime.getPercentActivity()), null);
					ptime.setPercentActivity(null);
				}
			}

			if (ptRequest.getPercentActivityNV() != null) {
				if (!ptRequest.getPercentActivityNV().equals(ptime.getPercentActivityNV())) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "PercentActivityNV", ptime.getPercentActivityNV(),
							ptRequest.getPercentActivityNV());
					ptime.setPercentActivityNV(ptRequest.getPercentActivityNV());

				}
			} else {
				if (ptime.getPercentActivityNV() != null) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "PercentActivityNV", ptime.getPercentActivityNV(),
							null);
					ptime.setPercentActivityNV(null);
				}
			}

			if (ptRequest.getInr() != null) {
				Float tm = appUtility.parseFloatAmount(ptRequest.getInr());
				if (!tm.equals(ptime.getInr())) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "INR", String.valueOf(ptime.getInr()),
							String.valueOf(tm));
					ptime.setInr(tm);
				}
			} else {
				if (ptime.getInr() != null) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "INR", String.valueOf(ptime.getInr()), null);
					ptime.setInr(null);
				}
			}

			if (ptRequest.getInrNV() != null) {
				if (!ptRequest.getInrNV().equals(ptime.getInrNV())) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "InrNV", ptime.getInrNV(), ptRequest.getInrNV());
					ptime.setInrNV(ptRequest.getInrNV());

				}
			} else {
				if (ptime.getInrNV() != null) {
					ptLogMsg = appUtility.formatUpdateData(ptLogMsg, "InrNV", ptime.getInrNV(), null);
					ptime.setInrNV(null);
				}
			}
		}

		if (!"".equals(ptLogMsg)) {
			qisTransactionLabPTimeRepository.save(ptime);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, ptLogMsg, laboratoryId, CATEGORY + "-PROTHROMBIN");
		}

		if (isAdded) {
			return ptime;
		}

		return null;
	}

	private QisTransactionLabPRMS savePRMS(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabHERequest heRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String prmsLogMsg = "";
		QisTransactionLabPRMS prms = qisTransactionLabPRMSRepository.getTransactionLabPRMSByLabReqId(laboratoryId);

		QisTransactionLabHEPRMSRequest prmsRequest = heRequest.getPrms();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(prmsRequest.getReferenceLabId());

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

		if (prms == null) {
			prms = new QisTransactionLabPRMS();
			BeanUtils.copyProperties(heRequest.getPrms(), prms);
			prms.setId(laboratoryId);
			prms.setCreatedBy(authUser.getId());
			prms.setUpdatedBy(authUser.getId());

			if (prmsRequest.getPr131() != null) {
				Float tm = appUtility.parseFloatAmount(prmsRequest.getPr131());
				prms.setPr131(tm);
			}

			if (referenceLab != null) {
				prms.setReferenceLab(referenceLab);
				prms.setReferenceLabId(referenceLab.getId());
			}
			prmsLogMsg = heRequest.getPrms().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			prms.setUpdatedBy(authUser.getId());
			prms.setUpdatedAt(Calendar.getInstance());

			if (prmsRequest.getReferenceLabId() != null) {
				if (prms.getReferenceLab() != null) {
					if (prmsRequest.getReferenceLabId() != prms.getReferenceLab().getReferenceid()) {
						prmsLogMsg = appUtility.formatUpdateData(prmsLogMsg, "Reference Laboratory",
								String.valueOf(prms.getReferenceLab().getReferenceid()),
								String.valueOf(prmsRequest.getReferenceLabId()));
						if (referenceLab != null) {
							prms.setReferenceLab(referenceLab);
							prms.setReferenceLabId(referenceLab.getId());
						} else {
							prms.setReferenceLab(null);
							prms.setReferenceLabId(null);
						}
					}
				} else {
					prmsLogMsg = appUtility.formatUpdateData(prmsLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(prmsRequest.getReferenceLabId()));
					if (referenceLab != null) {
						prms.setReferenceLab(referenceLab);
						prms.setReferenceLabId(referenceLab.getId());
					} else {
						prms.setReferenceLab(null);
						prms.setReferenceLabId(null);
					}
				}
			} else {
				if (prms.getReferenceLab() != null) {
					prmsLogMsg = appUtility.formatUpdateData(prmsLogMsg, "Reference Laboratory",
							String.valueOf(prms.getReferenceLab().getReferenceid()), null);
					prms.setReferenceLab(null);
					prms.setReferenceLabId(null);
				}
			}

			if (prmsRequest.getPr131() != null) {
				Float pr = appUtility.parseFloatAmount(prmsRequest.getPr131());
				if (!pr.equals(prms.getPr131())) {
					prmsLogMsg = appUtility.formatUpdateData(prmsLogMsg, "pr131", String.valueOf(prms.getPr131()),
							String.valueOf(pr));
					prms.setPr131(pr);
				}
			} else {
				if (prms.getPr131() != null) {
					prmsLogMsg = appUtility.formatUpdateData(prmsLogMsg, "pr131", String.valueOf(prms.getPr131()),
							null);
					prms.setPr131(null);
				}
			}

			if (prmsRequest.getMalarialSmear() != null) {
				if (prmsRequest.getMalarialSmear() != prms.getMalarialSmear()) {
					prmsLogMsg = appUtility.formatUpdateData(prmsLogMsg, "MalarialSmear",
							String.valueOf(prms.getMalarialSmear()), String.valueOf(prmsRequest.getMalarialSmear()));
					prms.setMalarialSmear(prmsRequest.getMalarialSmear());
				}
			} else {
				if (prms.getMalarialSmear() != null) {
					prmsLogMsg = appUtility.formatUpdateData(prmsLogMsg, "MalarialSmear",
							String.valueOf(prms.getMalarialSmear()), null);
					prms.setMalarialSmear(null);
				}
			}
		}

		if (!"".equals(prmsLogMsg)) {
			qisTransactionLabPRMSRepository.save(prms);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, prmsLogMsg, laboratoryId, CATEGORY + "-PRMS");
		}

		if (isAdded) {
			return prms;
		}

		return null;
	}

	private QisTransactionLabAPTT saveAPTT(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabHERequest heRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String apttLogMsg = "";
		QisTransactionLabAPTT aptt = qisTransactionLabAPTTRepository.getTransactionLabAPTTByLabReqId(laboratoryId);

		QisTransactionLabHEAPTTRequest apttRequest = heRequest.getAptt();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(apttRequest.getReferenceLabId());

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

		if (aptt == null) {
			aptt = new QisTransactionLabAPTT();
			BeanUtils.copyProperties(heRequest.getAptt(), aptt);
			aptt.setId(laboratoryId);
			aptt.setCreatedBy(authUser.getId());
			aptt.setUpdatedBy(authUser.getId());

			if (apttRequest.getPatientTime() != null) {
				Float tm = appUtility.parseFloatAmount(apttRequest.getPatientTime());
				aptt.setPatientTime(tm);
			}

			if (apttRequest.getControl() != null) {
				Float tm = appUtility.parseFloatAmount(apttRequest.getControl());
				aptt.setControl(tm);
			}

			if (referenceLab != null) {
				aptt.setReferenceLab(referenceLab);
				aptt.setReferenceLabId(referenceLab.getId());
			}

			apttLogMsg = heRequest.getAptt().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			aptt.setUpdatedBy(authUser.getId());
			aptt.setUpdatedAt(Calendar.getInstance());

			if (apttRequest.getReferenceLabId() != null) {
				if (aptt.getReferenceLab() != null) {
					if (apttRequest.getReferenceLabId() != aptt.getReferenceLab().getReferenceid()) {
						apttLogMsg = appUtility.formatUpdateData(apttLogMsg, "Reference Laboratory",
								String.valueOf(aptt.getReferenceLab().getReferenceid()),
								String.valueOf(apttRequest.getReferenceLabId()));
						if (referenceLab != null) {
							aptt.setReferenceLab(referenceLab);
							aptt.setReferenceLabId(referenceLab.getId());
						} else {
							aptt.setReferenceLab(null);
							aptt.setReferenceLabId(null);
						}
					}
				} else {
					apttLogMsg = appUtility.formatUpdateData(apttLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(apttRequest.getReferenceLabId()));
					if (referenceLab != null) {
						aptt.setReferenceLab(referenceLab);
						aptt.setReferenceLabId(referenceLab.getId());
					} else {
						aptt.setReferenceLab(null);
						aptt.setReferenceLabId(null);
					}
				}
			} else {
				if (aptt.getReferenceLab() != null) {
					apttLogMsg = appUtility.formatUpdateData(apttLogMsg, "Reference Laboratory",
							String.valueOf(aptt.getReferenceLab().getReferenceid()), null);
					aptt.setReferenceLab(null);
					aptt.setReferenceLabId(null);
				}
			}

			if (apttRequest.getPatientTime() != null) {
				Float tm = appUtility.parseFloatAmount(apttRequest.getPatientTime());
				if (!tm.equals(aptt.getPatientTime())) {
					apttLogMsg = appUtility.formatUpdateData(apttLogMsg, "PatientTime",
							String.valueOf(aptt.getPatientTime()), String.valueOf(tm));
					aptt.setPatientTime(tm);
				}
			} else {
				if (aptt.getPatientTime() != null) {
					apttLogMsg = appUtility.formatUpdateData(apttLogMsg, "PatientTime",
							String.valueOf(aptt.getPatientTime()), null);
					aptt.setPatientTime(null);
				}
			}

			if (apttRequest.getPatientTimeNV() != null) {
				if (!apttRequest.getPatientTimeNV().equals(aptt.getPatientTimeNV())) {
					apttLogMsg = appUtility.formatUpdateData(apttLogMsg, "PatientTimeNV", aptt.getPatientTimeNV(),
							apttRequest.getPatientTimeNV());
					aptt.setPatientTimeNV(apttRequest.getPatientTimeNV());

				}
			} else {
				if (aptt.getPatientTimeNV() != null) {
					apttLogMsg = appUtility.formatUpdateData(apttLogMsg, "PatientTimeNV", aptt.getPatientTimeNV(),
							null);
					aptt.setPatientTimeNV(null);
				}
			}

			if (apttRequest.getControl() != null) {
				Float tm = appUtility.parseFloatAmount(apttRequest.getControl());
				if (!tm.equals(aptt.getControl())) {
					apttLogMsg = appUtility.formatUpdateData(apttLogMsg, "Control", String.valueOf(aptt.getControl()),
							String.valueOf(tm));
					aptt.setControl(tm);
				}
			} else {
				if (aptt.getControl() != null) {
					apttLogMsg = appUtility.formatUpdateData(apttLogMsg, "Control", String.valueOf(aptt.getControl()),
							null);
					aptt.setControl(null);
				}
			}

			if (apttRequest.getControlNV() != null) {
				if (!apttRequest.getControlNV().equals(aptt.getControlNV())) {
					apttLogMsg = appUtility.formatUpdateData(apttLogMsg, "ControlNV", aptt.getControlNV(),
							apttRequest.getControlNV());
					aptt.setControlNV(apttRequest.getControlNV());

				}
			} else {
				if (aptt.getControlNV() != null) {
					apttLogMsg = appUtility.formatUpdateData(apttLogMsg, "ControlNV", aptt.getControlNV(), null);
					aptt.setControlNV(null);
				}
			}
		}

		if (!"".equals(apttLogMsg)) {
			qisTransactionLabAPTTRepository.save(aptt);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, apttLogMsg, laboratoryId, CATEGORY + "-APTT");
		}

		if (isAdded) {
			return aptt;
		}

		return null;
	}

	private QisTransactionLabESR saveESR(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabHERequest heRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String esrLogMsg = "";
		QisTransactionLabESR esr = qisTransactionLabESRRepository.getTransactionLabESRByLabReqId(laboratoryId);

		QisTransactionLabHEESRRequest esrRequest = heRequest.getEsr();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(esrRequest.getReferenceLabId());

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

		if (esr == null) {
			esr = new QisTransactionLabESR();
			BeanUtils.copyProperties(heRequest.getEsr(), esr);
			esr.setId(laboratoryId);
			esr.setCreatedBy(authUser.getId());
			esr.setUpdatedBy(authUser.getId());

			Float ra = appUtility.parseFloatAmount(esrRequest.getRate());
			esr.setRate(ra);

			if (referenceLab != null) {
				esr.setReferenceLab(referenceLab);
				esr.setReferenceLabId(referenceLab.getId());
			}

			esrLogMsg = heRequest.getEsr().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			esr.setUpdatedBy(authUser.getId());
			esr.setUpdatedAt(Calendar.getInstance());

			if (esrRequest.getReferenceLabId() != null) {
				if (esr.getReferenceLab() != null) {
					if (esrRequest.getReferenceLabId() != esr.getReferenceLab().getReferenceid()) {
						esrLogMsg = appUtility.formatUpdateData(esrLogMsg, "Reference Laboratory",
								String.valueOf(esr.getReferenceLab().getReferenceid()),
								String.valueOf(esrRequest.getReferenceLabId()));
						if (referenceLab != null) {
							esr.setReferenceLab(referenceLab);
							esr.setReferenceLabId(referenceLab.getId());
						} else {
							esr.setReferenceLab(null);
							esr.setReferenceLabId(null);
						}
					}
				} else {
					esrLogMsg = appUtility.formatUpdateData(esrLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(esrRequest.getReferenceLabId()));
					if (referenceLab != null) {
						esr.setReferenceLab(referenceLab);
						esr.setReferenceLabId(referenceLab.getId());
					} else {
						esr.setReferenceLab(null);
						esr.setReferenceLabId(null);
					}
				}
			} else {
				if (esr.getReferenceLab() != null) {
					esrLogMsg = appUtility.formatUpdateData(esrLogMsg, "Reference Laboratory",
							String.valueOf(esr.getReferenceLab().getReferenceid()), null);
					esr.setReferenceLab(null);
					esr.setReferenceLabId(null);
				}
			}

			Float ra = appUtility.parseFloatAmount(esrRequest.getRate());
			if (!ra.equals(esr.getRate())) {
				esrLogMsg = appUtility.formatUpdateData(esrLogMsg, "rate", String.valueOf(esr.getRate()),
						String.valueOf(ra));
				esr.setRate(ra);
			}

			if (!esrRequest.getMethod().equals(esr.getMethod())) {
				esrLogMsg = appUtility.formatUpdateData(esrLogMsg, "Method", esr.getMethod(), esrRequest.getMethod());
				esr.setMethod(esrRequest.getMethod());

			}
		}

		if (!"".equals(esrLogMsg)) {
			qisTransactionLabESRRepository.save(esr);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryHematologyController.class.getSimpleName(),
					action, esrLogMsg, laboratoryId, CATEGORY + "-ESR");
		}

		if (isAdded) {
			return esr;
		}

		return null;
	}
}
