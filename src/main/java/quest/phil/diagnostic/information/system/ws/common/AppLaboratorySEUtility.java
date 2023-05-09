package quest.phil.diagnostic.information.system.ws.common;

import java.util.Calendar;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quest.phil.diagnostic.information.system.ws.controller.QisTransactionLaboratorySerologyController;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratoryItems;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabASO;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabAntigen;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabCRP;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabCovid;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabDengue;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabHIV;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabRFT;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabRtAntigen;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabSerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabTPHAwTiter;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabTPN;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabThyroid;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabTyphidot;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionMedSer;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionRTPCR;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabSERequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSEASORequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSEAntigenRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSECRPRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSECovidRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSEDegueRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSEHIVRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSERFTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSERTPCRRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSERtAtigenRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSESerologyRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSETPNRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSEThyroidRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSETyphidotRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionLabSeTPHAWTRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.se.QisTransactionMedSerRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisReferenceLaboratoryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionSerologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabASORepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabAntigenRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabCRPRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabCovidRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabDengueRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabHIVRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabRFTRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabRTPCRRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabRtAntigenRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabSerologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabTPHAWTRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabTPNRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabThyroidRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionLabTyphidotRepository;
import quest.phil.diagnostic.information.system.ws.repository.se.QisTransactionMedSerRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@Component
public class AppLaboratorySEUtility {
	private final String CATEGORY = "SE";

	// SEROLOGY
	@Autowired
	private QisTransactionSerologyRepository qisTransactionSerologyRepository;

	@Autowired
	private QisTransactionLabSerologyRepository qisTransactionLabSerologyRepository;

	@Autowired
	private QisTransactionLabThyroidRepository qisTransactionLabThyroidRepository;

	@Autowired
	private QisTransactionLabTyphidotRepository qisTransactionLabTyphidotRepository;

	@Autowired
	private QisTransactionLabCRPRepository qisTransactionLabCRPRepository;

	@Autowired
	private QisTransactionLabHIVRepository qisTransactionLabHIVRepository;

	@Autowired
	private QisTransactionLabAntigenRepository qisTransactionLabAntigenRepository;

	@Autowired
	private QisTransactionLabCovidRepository qisTransactionLabCovidRepository;

	@Autowired
	private QisTransactionLabRtAntigenRepository qisTransactionLabRtAntigenRepository;

	@Autowired
	private QisTransactionLabRTPCRRepository qisTransactionLabRTPCRRepository;

	@Autowired
	private QisTransactionLabRFTRepository qisTransactionLabRFTRepository;

	@Autowired
	private QisTransactionLabTPHAWTRepository qisTransactionLabTPHAWTRepository;

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

	@Autowired
	private QisTransactionMedSerRepository qisTransactionMedSer;

	@Autowired
	private QisTransactionLabDengueRepository qisTransactionDengueRepository;

	@Autowired
	private QisTransactionLabASORepository qisTransactionlabASORepository;

	@Autowired
	private QisTransactionLabTPNRepository qisTransactionLabTpnRepository;

	/*
	 * SEROLOGY
	 */
	public void validateSerology(Long laboratoryId, Set<QisLaboratoryProcedureService> serviceRequest,
			QisTransactionLabSERequest seRequest) throws Exception {

		if (seRequest == null) {
			throw new RuntimeException("Serology request is required.",
					new Throwable("serology[" + laboratoryId + "]: Serology is required."));
		}

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(seRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		for (QisLaboratoryProcedureService service : serviceRequest) {
			switch (service.getLaboratoryRequest().toString()) {
			case "SER": {
				if (seRequest.getSerology() == null) {
					throw new RuntimeException("Serology is required.",
							new Throwable("serology: Serology is required."));
				}
			}
				break;
			case "THYR": {
				if (seRequest.getThyroid() == null) {
					throw new RuntimeException("Thyroid is required.", new Throwable("thyroid: Thyroid is required."));
				}
				QisTransactionLabSEThyroidRequest tRequest = seRequest.getThyroid();
				if (tRequest.getTsh() != null) {
					Float value = appUtility.parseFloatAmount(tRequest.getTsh());
					if (value == null) {
						throw new RuntimeException("Invalid TSH value[" + tRequest.getTsh() + "].",
								new Throwable("thyroid.tsh: Invalid TSH value[" + tRequest.getTsh() + "]."));
					}
				}
				if (tRequest.getFt3() != null) {
					Float value = appUtility.parseFloatAmount(tRequest.getFt3());
					if (value == null) {
						throw new RuntimeException("Invalid Ft3 value[" + tRequest.getFt3() + "].",
								new Throwable("thyroid.ft3: Invalid Ft3 value[" + tRequest.getFt3() + "]."));
					}
				}
				if (tRequest.getFt4() != null) {
					Float value = appUtility.parseFloatAmount(tRequest.getFt4());
					if (value == null) {
						throw new RuntimeException("Invalid Ft4 value[" + tRequest.getFt4() + "].",
								new Throwable("thyroid.ft4: Invalid Ft4 value[" + tRequest.getFt4() + "]."));
					}
				}
				if (tRequest.getT3() != null) {
					Float value = appUtility.parseFloatAmount(tRequest.getT3());
					if (value == null) {
						throw new RuntimeException("Invalid T3 value[" + tRequest.getT3() + "].",
								new Throwable("thyroid.t3: Invalid T3 value[" + tRequest.getT3() + "]."));
					}
				}
				if (tRequest.getT4() != null) {
					Float value = appUtility.parseFloatAmount(tRequest.getT4());
					if (value == null) {
						throw new RuntimeException("Invalid T4 value[" + tRequest.getT4() + "].",
								new Throwable("thyroid.t4: Invalid T4 value[" + tRequest.getT4() + "]."));
					}
				}
			}
				break;
			case "TYPH": {
				if (seRequest.getTyphidot() == null) {
					throw new RuntimeException("Typhidot is required.",
							new Throwable("typhidot: Typhidot is required."));
				}
			}
				break;

			case "RFT": {
				if (seRequest.getRft() == null) {
					throw new RuntimeException("RHEUMATOID FACTOR TITER is required.",
							new Throwable("RHEUMATOID FACTOR TITER: RHEUMATOID FACTOR TITER is required."));
				}
			}
				break;
			case "ASO": {
				if (seRequest.getAso() == null) {
					throw new RuntimeException("ASO TITER is required.",
							new Throwable("ASO TITER: ASO TITER is required."));
				}
			}
				break;

			case "CRP": {
				if (seRequest.getCrp() == null) {
					throw new RuntimeException("C-Reactive Protein is required.",
							new Throwable("crp: C-Reactive Protein is required."));
				}
				QisTransactionLabSECRPRequest crpRequest = seRequest.getCrp();
				if (crpRequest.getDilution() != null) {
					Float value = appUtility.parseFloatAmount(crpRequest.getDilution());
					if (value == null) {
						throw new RuntimeException("Invalid Dilution value[" + crpRequest.getDilution() + "].",
								new Throwable(
										"crp.dilution: Invalid Dilution value[" + crpRequest.getDilution() + "]."));
					}
				}
				if (crpRequest.getResult() != null) {
					Float value = appUtility.parseFloatAmount(crpRequest.getResult());
					if (value == null) {
						throw new RuntimeException("Invalid Result value[" + crpRequest.getResult() + "].",
								new Throwable("crp.result: Invalid Result value[" + crpRequest.getResult() + "]."));
					}
				}
			}
				break;
			case "HIV": {
				if (seRequest.getHiv() == null) {
					throw new RuntimeException("HIV is required.", new Throwable("hiv: HIV is required."));
				}
			}
				break;
			case "AGEN": {
				if (seRequest.getAntigen() == null) {
					throw new RuntimeException("Antigen is required.", new Throwable("antigen: Antigen is required."));
				}
				QisTransactionLabSEAntigenRequest antRequest = seRequest.getAntigen();

				if (antRequest.getPsa() != null) {
					Float value = appUtility.parseFloatAmount(antRequest.getPsa());
					if (value == null) {
						throw new RuntimeException(
								"Invalid Prostate Specific Antigen (PSA) value[" + antRequest.getPsa() + "].",
								new Throwable("antigen.psa: Invalid Prostate Specific Antigen (PSA) value["
										+ antRequest.getPsa() + "]."));
					}
				}

				if (antRequest.getCea() != null) {
					Float value = appUtility.parseFloatAmount(antRequest.getCea());
					if (value == null) {
						throw new RuntimeException("Invalid CEA value[" + antRequest.getCea() + "].",
								new Throwable("antigen.cea: Invalid CEA value[" + antRequest.getCea() + "]."));
					}
				}

				if (antRequest.getAfp() != null) {
					Float value = appUtility.parseFloatAmount(antRequest.getAfp());
					if (value == null) {
						throw new RuntimeException("Invalid AFP value[" + antRequest.getAfp() + "].",
								new Throwable("antigen.afp: Invalid AFP value[" + antRequest.getAfp() + "]."));
					}
				}

				if (antRequest.getCa125() != null) {
					Float value = appUtility.parseFloatAmount(antRequest.getCa125());
					if (value == null) {
						throw new RuntimeException("Invalid CA125 value[" + antRequest.getCa125() + "].",
								new Throwable("antigen.ca125: Invalid CA125 value[" + antRequest.getCa125() + "]."));
					}
				}

				if (antRequest.getCa199() != null) {
					Float value = appUtility.parseFloatAmount(antRequest.getCa199());
					if (value == null) {
						throw new RuntimeException("Invalid CA19-9 value[" + antRequest.getCa199() + "].",
								new Throwable("antigen.ca199: Invalid CA19-9 value[" + antRequest.getCa199() + "]."));
					}
				}

				if (antRequest.getCa153() != null) {
					Float value = appUtility.parseFloatAmount(antRequest.getCa153());
					if (value == null) {
						throw new RuntimeException("Invalid CA15-3 value[" + antRequest.getCa153() + "].",
								new Throwable("antigen.ca153: Invalid CA15-3 value[" + antRequest.getCa153() + "]."));
					}
				}
			}
				break;
			case "COVID": {
				if (seRequest.getCovid() == null) {
					throw new RuntimeException("COVID is required.", new Throwable("covid: COVID is required."));
				}
			}
				break;

			case "TPHA": {
				if (seRequest.getTphawt() == null) {
					throw new RuntimeException("TPHA with Titer is required.",
							new Throwable("TPHA: TPHA with Titer is required."));
				}
			}
				break;

			case "ANTIGEN": {
				if (seRequest.getRtantigen() == null) {
					throw new RuntimeException("RAPID ANTIGEN is required.",
							new Throwable("serology: RAPID ANTIGEN is required."));
				}
			}
				break;

			case "RTPCR": {
				if (seRequest.getRtpcr() == null) {
					throw new RuntimeException("RTPCR is required.", new Throwable("serology: RTPCR is required."));
				}
			}
				break;

			case "DGE": {
				if (seRequest.getDengue() == null) {
					throw new RuntimeException("Dengue is required.", new Throwable("serology: Dengue is required."));
				}
			}
				break;
			case "TPN": {
				if (seRequest.getTpn() == null) {
					throw new RuntimeException("TROPONIN is required.",
							new Throwable("serology: TROPONIN is required."));
				}
			}
				break;
			} // switch
		} // for loop
	}

	public void saveSerology(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionSerology qisTransactionSerology, Set<QisLaboratoryProcedureService> serviceRequest,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, QisDoctor qisDoctor) {
		boolean isAntigen = true;

		String referenceLab1 = "";
		if (seRequest.getAntigen() != null) {
			referenceLab1 = seRequest.getAntigen().getReferenceLabId();
		}
		if (seRequest.getCovid() != null) {
			referenceLab1 = seRequest.getCovid().getReferenceLabId();
		}
		if (seRequest.getCrp() != null) {
			referenceLab1 = seRequest.getCrp().getReferenceLabId();
		}
		if (seRequest.getHiv() != null) {
			referenceLab1 = seRequest.getHiv().getReferenceLabId();
		}
		if (seRequest.getRtantigen() != null) {
			referenceLab1 = seRequest.getRtantigen().getReferenceLabId();
		}
		if (seRequest.getRtpcr() != null) {
			referenceLab1 = seRequest.getRtpcr().getReferenceLabId();
		}
		if (seRequest.getSerology() != null) {
			referenceLab1 = seRequest.getSerology().getReferenceLabId();
		}
		if (seRequest.getThyroid() != null) {
			referenceLab1 = seRequest.getThyroid().getReferenceLabId();
		}
		if (seRequest.getTyphidot() != null) {
			referenceLab1 = seRequest.getTyphidot().getReferenceLabId();
		}
		if (seRequest.getRft() != null) {
			referenceLab1 = seRequest.getRft().getReferenceLabId();
		}

		if (seRequest.getMedSer() != null) {
			referenceLab1 = seRequest.getMedSer().getReferenceLabId();
		}

		if (seRequest.getTphawt() != null) {
			referenceLab1 = seRequest.getTphawt().getReferenceLabId();
		}
		if (seRequest.getAso() != null) {
			referenceLab1 = seRequest.getAso().getReferenceLabId();
		}
		if (seRequest.getDengue() != null) {
			referenceLab1 = seRequest.getDengue().getReferenceLabId();
		}
		if (seRequest.getTpn() != null) {
			referenceLab1 = seRequest.getTpn().getReferenceLabId();
		}

		for (QisLaboratoryProcedureService service : serviceRequest) {
			switch (service.getLaboratoryRequest().toString()) {
			case "SER": {
				QisTransactionLabSerology ser = saveSerology(qisTransaction, laboratoryId, seRequest, authUser,
						referenceLab1, qisTransactionSerology.getItemDetails().getItemid());
				if (ser != null) {
					qisTransactionSerology.setSerology(ser);
				}
			}
				break;

			case "THYR": {
				QisTransactionLabThyroid thy = saveThyroid(qisTransaction, laboratoryId, seRequest, authUser,
						referenceLab1, qisTransactionSerology.getItemDetails().getItemid());
				if (thy != null) {
					qisTransactionSerology.setThyroid(thy);
				}
			}
				break;
			case "TYPH": {
				QisTransactionLabTyphidot typ = saveTyphidot(qisTransaction, laboratoryId, seRequest, authUser,
						referenceLab1, qisTransactionSerology.getItemDetails().getItemid());
				if (typ != null) {
					qisTransactionSerology.setTyphidot(typ);
				}
			}
				break;
			case "RFT": {
				QisTransactionLabRFT rft = saveRFT(qisTransaction, laboratoryId, seRequest, authUser, referenceLab1,
						qisTransactionSerology.getItemDetails().getItemid());
				if (rft != null) {
					qisTransactionSerology.setRft(rft);
				}
			}
				break;
			case "ASO": {
				QisTransactionLabASO aso = saveASO(qisTransaction, laboratoryId, seRequest, authUser, referenceLab1,
						qisTransactionSerology.getItemDetails().getItemid());
				if (aso != null) {
					qisTransactionSerology.setAso(aso);
				}
			}
				break;
			case "CRP": {
				QisTransactionLabCRP crp = saveCRP(qisTransaction, laboratoryId, seRequest, authUser, referenceLab1,
						qisTransactionSerology.getItemDetails().getItemid());
				if (crp != null) {
					qisTransactionSerology.setCrp(crp);
				}
			}
				break;
			case "HIV": {
				QisTransactionLabHIV hiv = saveHIV(qisTransaction, laboratoryId, seRequest, authUser, referenceLab1,
						qisTransactionSerology.getItemDetails().getItemid());
				if (hiv != null) {
					qisTransactionSerology.setHiv(hiv);
				}
			}
				break;
			case "AGEN": {
				if (isAntigen) {
					QisTransactionLabAntigen antigen = saveAntigen(qisTransaction, laboratoryId, seRequest, authUser,
							referenceLab1, qisTransactionSerology.getItemDetails().getItemid());
					if (antigen != null) {
						qisTransactionSerology.setAntigen(antigen);
					}
					isAntigen = false;
				}
			}
				break;
			case "ANTIGEN": {
				QisTransactionLabRtAntigen rtAntigen = saveRtAntigen(qisTransaction, laboratoryId, seRequest, authUser,
						referenceLab1, qisTransactionSerology.getItemDetails().getItemid());
				if (rtAntigen != null) {
					qisTransactionSerology.setRtantigen(rtAntigen);
				}
			}
				break;
			case "COVID": {
				QisTransactionLabCovid covid = saveCovid(qisTransaction, laboratoryId, seRequest, authUser,
						referenceLab1, qisTransactionSerology.getItemDetails().getItemid());
				if (covid != null) {
					qisTransactionSerology.setCovid(covid);
				}
			}
				break;

			case "RTPCR": {
				QisTransactionRTPCR rtpcr = saveRTPCR(qisTransaction, laboratoryId, seRequest, authUser, referenceLab1,
						qisTransactionSerology.getItemDetails().getItemid());
				if (rtpcr != null) {
					qisTransactionSerology.setRtpcr(rtpcr);
				}
			}
				break;

			case "MS": {
				QisTransactionMedSer medSer = saveMEDSER(qisTransaction, laboratoryId, seRequest, authUser,
						referenceLab1, qisTransactionSerology.getItemDetails().getItemid());
				if (medSer != null) {
					qisTransactionSerology.setMedSer(medSer);
				}
			}
				break;

			case "DGE": {
				QisTransactionLabDengue dengue = saveDengue(qisTransaction, laboratoryId, seRequest, authUser,
						referenceLab1, qisTransactionSerology.getItemDetails().getItemid());
				if (dengue != null) {
					qisTransactionSerology.setDengue(dengue);
				}
			}
				break;

			case "TPHA": {
				QisTransactionLabTPHAwTiter tphawt = saveTPHAWT(qisTransaction, laboratoryId, seRequest, authUser,
						referenceLab1, qisTransactionSerology.getItemDetails().getItemid());
				if (tphawt != null) {
					qisTransactionSerology.setTphawt(tphawt);
				}
			}
				break;

			case "TPN": {
				QisTransactionLabTPN tpn = saveTPN(qisTransaction, laboratoryId, seRequest, authUser, referenceLab1,
						qisTransactionSerology.getItemDetails().getItemid());
				if (tpn != null) {
					qisTransactionSerology.setTpn(tpn);
				}
			}
				break;
			} // switch
		} // for loop

		int isUpdate = 0;
		if (!qisTransactionSerology.isSubmitted()) {
			qisTransactionSerology.setSubmitted(true);
			qisTransactionSerology.setVerifiedBy(authUser.getId());
			qisTransactionSerology.setVerifiedDate(Calendar.getInstance());
			isUpdate += 1;
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					"SUBMITTED", qisTransactionSerology.getItemDetails().getItemName(), laboratoryId, CATEGORY);
		}

		if (qisTransactionSerology.getLabPersonelId() == null) {
			qisTransactionSerology.setLabPersonelId(authUser.getId());
			qisTransactionSerology.setLabPersonelDate(Calendar.getInstance());
			qisTransactionSerology.setStatus(2);
			isUpdate += 2;
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					"LAB PERSONEL", authUser.getUsername(), laboratoryId, CATEGORY);
		}

		String onLogMsg = "";
		String action = "ADDED";
		if (seRequest.getOtherNotes() != null) {
			if (qisTransactionSerology.getOtherNotes() == null) {
				qisTransactionSerology.setOtherNotes(seRequest.getOtherNotes());
				onLogMsg = seRequest.getOtherNotes();
			} else if (!seRequest.getOtherNotes().equals(qisTransactionSerology.getOtherNotes())) {
				qisTransactionSerology.setOtherNotes(seRequest.getOtherNotes());
				action = "UPDATED";
				onLogMsg = appUtility.formatUpdateData(onLogMsg, "OtherNotes", qisTransactionSerology.getOtherNotes(),
						seRequest.getOtherNotes());
			}
		} else {
			if (qisTransactionSerology.getOtherNotes() != null) {
				qisTransactionSerology.setOtherNotes(null);
				action = "UPDATED";
				onLogMsg = appUtility.formatUpdateData(onLogMsg, "OtherNotes", qisTransactionSerology.getOtherNotes(),
						null);
			}
		}

		if (!"".equals(onLogMsg)) {
			isUpdate += 4;
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, onLogMsg, laboratoryId, CATEGORY + "-OTHER NOTES");
		}
		onLogMsg = "";
		action = "ADDED";
		for (QisLaboratoryProcedureService service : serviceRequest) {
			switch (service.getLaboratoryRequest().toString()) {
			case "ANTIGEN":
			case "COVID":
			case "RTPCR": {
				if (qisTransactionSerology.getMedicalDoctorId() == null) {
					qisTransactionSerology.setMedicalDoctor(qisDoctor);
					qisTransactionSerology.setMedicalDoctorId((long) 1);
					onLogMsg = qisTransactionSerology.getMedicalDoctor().getDoctorid();
				} else if (qisDoctor.getId() != qisTransactionSerology.getMedicalDoctorId()) {
					qisTransactionSerology.setMedicalDoctor(qisDoctor);
					qisTransactionSerology.setMedicalDoctorId((long) 1);

					action = "UPDATED";
					onLogMsg = appUtility.formatUpdateData(onLogMsg, "pathologist",
							qisTransactionSerology.getMedicalDoctor().getDoctorid(), qisDoctor.getDoctorid());
				}
			}
				break;
			default:
				if (qisTransactionSerology.getMedicalDoctorId() == null) {
					qisTransactionSerology.setMedicalDoctor(qisDoctor);
					qisTransactionSerology.setMedicalDoctorId(qisDoctor.getId());
					onLogMsg = qisTransactionSerology.getMedicalDoctor().getDoctorid();
				} else if (qisDoctor.getId() != qisTransactionSerology.getMedicalDoctorId()) {
					qisTransactionSerology.setMedicalDoctor(qisDoctor);
					qisTransactionSerology.setMedicalDoctorId(qisDoctor.getId());

					action = "UPDATED";
					onLogMsg = appUtility.formatUpdateData(onLogMsg, "pathologist",
							qisTransactionSerology.getMedicalDoctor().getDoctorid(), qisDoctor.getDoctorid());
				}
				break;

			}
		}

		if (!"".equals(onLogMsg)) {
			isUpdate += 8;
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, onLogMsg, laboratoryId, CATEGORY + "-PATHOLOGIST");
		}

		if (isUpdate > 0) {
			qisTransactionSerologyRepository.save(qisTransactionSerology);

			QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
			if ((isUpdate & 1) > 0) {
				qisTransactionSerology.setVerified(labPersonel);

			}
			if ((isUpdate & 2) > 0) {
				qisTransactionSerology.setLabPersonel(labPersonel);
			}
		}
	}

	public void saveSerologyQC(QisTransactionSerology qisSerology, Long laboratoryId, QisUserDetails authUser) {
		qisSerology.setQcId(authUser.getId());
		qisSerology.setQcDate(Calendar.getInstance());
		qisSerology.setStatus(3); // Quality Control
		qisTransactionSerologyRepository.save(qisSerology);

		QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
		qisSerology.setQualityControl(labPersonel);

		qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(), "UPDATE",
				"QC Serology", laboratoryId, CATEGORY);
	}

	private QisTransactionLabTPN saveTPN(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String tpnLogMsg = "";

		QisTransactionLabTPN tpn = qisTransactionLabTpnRepository.getTransactionLabTpnByLabReqId(laboratoryId);
		QisTransactionLabSETPNRequest tpnRequest = seRequest.getTpn();

		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(tpnRequest.getReferenceLabId());

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

		if (tpn == null) {
			tpn = new QisTransactionLabTPN();
			BeanUtils.copyProperties(seRequest.getTpn(), tpn);
			tpn.setId(laboratoryId);
			tpn.setCreatedBy(authUser.getId());
			tpn.setUpdatedBy(authUser.getId());

			if (referenceLab != null) {
				tpn.setReferenceLab(referenceLab);
				tpn.setReferenceLabId(referenceLab.getId());
			}

			if (tpn.getResult() != null) {
				tpn.setResult(tpnRequest.getResult());
			}

			tpnLogMsg = seRequest.getTpn().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			tpn.setUpdatedBy(authUser.getId());
			tpn.setUpdatedAt(Calendar.getInstance());
			if (tpnRequest.getReferenceLabId() != null) {
				if (tpn.getReferenceLab() != null) {
					if (tpnRequest.getReferenceLabId() != tpn.getReferenceLab().getReferenceid()) {
						tpnLogMsg = appUtility.formatUpdateData(tpnLogMsg, "Reference Laboratory",
								String.valueOf(tpn.getReferenceLab().getReferenceid()),
								String.valueOf(tpnRequest.getReferenceLabId()));
						if (referenceLab != null) {
							tpn.setReferenceLab(referenceLab);
							tpn.setReferenceLabId(referenceLab.getId());
						} else {
							tpn.setReferenceLab(null);
							tpn.setReferenceLabId(null);
						}
					}
				} else {
					tpnLogMsg = appUtility.formatUpdateData(tpnLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(tpnRequest.getReferenceLabId()));
					if (referenceLab != null) {
						tpn.setReferenceLab(referenceLab);
						tpn.setReferenceLabId(referenceLab.getId());
					} else {
						tpn.setReferenceLab(null);
						tpn.setReferenceLabId(null);
					}
				}
			} else {
				if (tpn.getReferenceLab() != null) {
					tpnLogMsg = appUtility.formatUpdateData(tpnLogMsg, "Reference Laboratory",
							String.valueOf(tpn.getReferenceLab().getReferenceid()), null);
					tpn.setReferenceLab(null);
					tpn.setReferenceLabId(null);
				}
			}

			if (tpnRequest.getResult() != null) {
				if (tpnRequest.getResult() != tpn.getResult()) {
					tpnLogMsg = appUtility.formatUpdateData(tpnLogMsg, "result", String.valueOf(tpn.getResult()),
							String.valueOf(tpnRequest.getResult()));
					tpn.setResult(tpnRequest.getResult());
				}
			} else {
				if (tpn.getResult() != null) {
					tpnLogMsg = appUtility.formatUpdateData(tpnLogMsg, "result", String.valueOf(tpn.getResult()), null);
					tpn.setResult(null);
				}
			}
		}

		if (!"".equals(tpnLogMsg)) {
			qisTransactionLabTpnRepository.save(tpn);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, tpnLogMsg, laboratoryId, CATEGORY + "-SERO");
		}

		if (isAdded) {
			return tpn;
		}

		return null;
	}

	private QisTransactionLabDengue saveDengue(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String dengueLogMsg = "";

		QisTransactionLabDengue dengue = qisTransactionDengueRepository.getTransactionLabDengueByLabReqId(laboratoryId);
		QisTransactionLabSEDegueRequest dengueReq = seRequest.getDengue();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(dengueReq.getReferenceLabId());

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

		if (dengue == null) {
			dengue = new QisTransactionLabDengue();
			BeanUtils.copyProperties(seRequest.getDengue(), dengue);
			dengue.setId(laboratoryId);
			dengue.setCreatedBy(authUser.getId());
			dengue.setUpdatedBy(authUser.getId());

			if (referenceLab != null) {
				dengue.setReferenceLab(referenceLab);
				dengue.setReferenceLabId(referenceLab.getId());
			}

			if (dengue.getResult() != null) {
				dengue.setResult(dengueReq.getResult());
			}

			dengueLogMsg = seRequest.getDengue().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			dengue.setUpdatedBy(authUser.getId());
			dengue.setUpdatedAt(Calendar.getInstance());
			if (dengueReq.getReferenceLabId() != null) {
				if (dengue.getReferenceLab() != null) {
					if (dengueReq.getReferenceLabId() != dengue.getReferenceLab().getReferenceid()) {
						dengueLogMsg = appUtility.formatUpdateData(dengueLogMsg, "Reference Laboratory",
								String.valueOf(dengue.getReferenceLab().getReferenceid()),
								String.valueOf(dengueReq.getReferenceLabId()));
						if (referenceLab != null) {
							dengue.setReferenceLab(referenceLab);
							dengue.setReferenceLabId(referenceLab.getId());
						} else {
							dengue.setReferenceLab(null);
							dengue.setReferenceLabId(null);
						}
					}
				} else {
					dengueLogMsg = appUtility.formatUpdateData(dengueLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(dengueReq.getReferenceLabId()));
					if (referenceLab != null) {
						dengue.setReferenceLab(referenceLab);
						dengue.setReferenceLabId(referenceLab.getId());
					} else {
						dengue.setReferenceLab(null);
						dengue.setReferenceLabId(null);
					}
				}
			} else {
				if (dengue.getReferenceLab() != null) {
					dengueLogMsg = appUtility.formatUpdateData(dengueLogMsg, "Reference Laboratory",
							String.valueOf(dengue.getReferenceLab().getReferenceid()), null);
					dengue.setReferenceLab(null);
					dengue.setReferenceLabId(null);
				}
			}

			if (dengueReq.getResult() != null) {
				if (dengueReq.getResult() != dengue.getResult()) {
					dengueLogMsg = appUtility.formatUpdateData(dengueLogMsg, "Test 1",
							String.valueOf(dengue.getResult()), String.valueOf(dengueReq.getResult()));
					dengue.setResult(dengueReq.getResult());
				}
			} else {
				if (dengue.getResult() != null) {
					dengueLogMsg = appUtility.formatUpdateData(dengueLogMsg, "Test 1",
							String.valueOf(dengue.getResult()), null);
					dengue.setResult(null);
				}
			}
		}

		if (!"".equals(dengueLogMsg)) {
			qisTransactionDengueRepository.save(dengue);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, dengueLogMsg, laboratoryId, CATEGORY + "-SERO");
		}

		if (isAdded) {
			return dengue;
		}

		return null;
	}

	private QisTransactionLabTPHAwTiter saveTPHAWT(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String tphaLogMsg = "";

		QisTransactionLabTPHAwTiter tphawt = qisTransactionLabTPHAWTRepository
				.getTransactionLabTPHAWTByLabReqId(laboratoryId);

		QisTransactionLabSeTPHAWTRequest tphawtReq = seRequest.getTphawt();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(tphawtReq.getReferenceLabId());

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

		if (tphawt == null) {
			tphawt = new QisTransactionLabTPHAwTiter();
			BeanUtils.copyProperties(seRequest.getTphawt(), tphawt);
			tphawt.setId(laboratoryId);
			tphawt.setCreatedBy(authUser.getId());
			tphawt.setUpdatedBy(authUser.getId());

			if (referenceLab != null) {
				tphawt.setReferenceLab(referenceLab);
				tphawt.setReferenceLabId(referenceLab.getId());
			}

			if (tphawtReq.getTest1() != null) {
				tphawt.setTest1(tphawtReq.getTest1());
			}
			if (tphawtReq.getTest2() != null) {
				tphawt.setTest2(tphawtReq.getTest2());
			}
			if (tphawtReq.getTest3() != null) {
				tphawt.setTest3(tphawtReq.getTest3());
			}
			if (tphawtReq.getTest4() != null) {
				tphawt.setTest4(tphawtReq.getTest4());
			}
			if (tphawtReq.getTest5() != null) {
				tphawt.setTest5(tphawtReq.getTest5());
			}
			if (tphawtReq.getTest6() != null) {
				tphawt.setTest6(tphawtReq.getTest6());
			}
			if (tphawtReq.getTest7() != null) {
				tphawt.setTest7(tphawtReq.getTest7());
			}
			if (tphawtReq.getTest8() != null) {
				tphawt.setTest8(tphawtReq.getTest8());
			}
			if (tphawtReq.getTest9() != null) {
				tphawt.setTest9(tphawtReq.getTest9());
			}
			tphaLogMsg = seRequest.getTphawt().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			tphawt.setUpdatedBy(authUser.getId());
			tphawt.setUpdatedAt(Calendar.getInstance());
			if (tphawtReq.getReferenceLabId() != null) {
				if (tphawt.getReferenceLab() != null) {
					if (tphawtReq.getReferenceLabId() != tphawt.getReferenceLab().getReferenceid()) {
						tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Reference Laboratory",
								String.valueOf(tphawt.getReferenceLab().getReferenceid()),
								String.valueOf(tphawtReq.getReferenceLabId()));
						if (referenceLab != null) {
							tphawt.setReferenceLab(referenceLab);
							tphawt.setReferenceLabId(referenceLab.getId());
						} else {
							tphawt.setReferenceLab(null);
							tphawt.setReferenceLabId(null);
						}
					}
				} else {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(tphawtReq.getReferenceLabId()));
					if (referenceLab != null) {
						tphawt.setReferenceLab(referenceLab);
						tphawt.setReferenceLabId(referenceLab.getId());
					} else {
						tphawt.setReferenceLab(null);
						tphawt.setReferenceLabId(null);
					}
				}
			} else {
				if (tphawt.getReferenceLab() != null) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Reference Laboratory",
							String.valueOf(tphawt.getReferenceLab().getReferenceid()), null);
					tphawt.setReferenceLab(null);
					tphawt.setReferenceLabId(null);
				}
			}

			if (tphawtReq.getTest1() != null) {
				if (tphawtReq.getTest1() != tphawt.getTest1()) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 1", String.valueOf(tphawt.getTest1()),
							String.valueOf(tphawtReq.getTest1()));
					tphawt.setTest1(tphawtReq.getTest1());
				}
			} else {
				if (tphawt.getTest1() != null) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 1", String.valueOf(tphawt.getTest1()),
							null);
					tphawt.setTest1(null);
				}
			}

			if (tphawtReq.getTest2() != null) {
				if (tphawtReq.getTest2() != tphawt.getTest2()) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 2", String.valueOf(tphawt.getTest2()),
							String.valueOf(tphawtReq.getTest2()));
					tphawt.setTest2(tphawtReq.getTest2());
				}
			} else {
				if (tphawt.getTest2() != null) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 2", String.valueOf(tphawt.getTest2()),
							null);
					tphawt.setTest2(null);
				}
			}

			if (tphawtReq.getTest3() != null) {
				if (tphawtReq.getTest3() != tphawt.getTest3()) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 3", String.valueOf(tphawt.getTest3()),
							String.valueOf(tphawtReq.getTest3()));
					tphawt.setTest3(tphawtReq.getTest3());
				}
			} else {
				if (tphawt.getTest3() != null) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 3", String.valueOf(tphawt.getTest3()),
							null);
					tphawt.setTest3(null);
				}
			}

			if (tphawtReq.getTest4() != null) {
				if (tphawtReq.getTest4() != tphawt.getTest4()) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 4", String.valueOf(tphawt.getTest4()),
							String.valueOf(tphawtReq.getTest4()));
					tphawt.setTest4(tphawtReq.getTest4());
				}
			} else {
				if (tphawt.getTest4() != null) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 4", String.valueOf(tphawt.getTest4()),
							null);
					tphawt.setTest4(null);
				}
			}

			if (tphawtReq.getTest5() != null) {
				if (tphawtReq.getTest5() != tphawt.getTest5()) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 5", String.valueOf(tphawt.getTest5()),
							String.valueOf(tphawtReq.getTest5()));
					tphawt.setTest5(tphawtReq.getTest5());
				}
			} else {
				if (tphawt.getTest5() != null) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 5", String.valueOf(tphawt.getTest5()),
							null);
					tphawt.setTest5(null);
				}
			}

			if (tphawtReq.getTest6() != null) {
				if (tphawtReq.getTest6() != tphawt.getTest6()) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 6", String.valueOf(tphawt.getTest6()),
							String.valueOf(tphawtReq.getTest6()));
					tphawt.setTest6(tphawtReq.getTest6());
				}
			} else {
				if (tphawt.getTest6() != null) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 6", String.valueOf(tphawt.getTest6()),
							null);
					tphawt.setTest6(null);
				}
			}

			if (tphawtReq.getTest7() != null) {
				if (tphawtReq.getTest7() != tphawt.getTest7()) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 7", String.valueOf(tphawt.getTest7()),
							String.valueOf(tphawtReq.getTest7()));
					tphawt.setTest7(tphawtReq.getTest7());
				}
			} else {
				if (tphawt.getTest7() != null) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 7", String.valueOf(tphawt.getTest7()),
							null);
					tphawt.setTest7(null);
				}
			}

			if (tphawtReq.getTest8() != null) {
				if (tphawtReq.getTest8() != tphawt.getTest8()) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 8", String.valueOf(tphawt.getTest8()),
							String.valueOf(tphawtReq.getTest8()));
					tphawt.setTest8(tphawtReq.getTest8());
				}
			} else {
				if (tphawt.getTest8() != null) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 8", String.valueOf(tphawt.getTest8()),
							null);
					tphawt.setTest8(null);
				}
			}

			if (tphawtReq.getTest9() != null) {
				if (tphawtReq.getTest9() != tphawt.getTest9()) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 9", String.valueOf(tphawt.getTest9()),
							String.valueOf(tphawtReq.getTest9()));
					tphawt.setTest9(tphawtReq.getTest9());
				}
			} else {
				if (tphawt.getTest9() != null) {
					tphaLogMsg = appUtility.formatUpdateData(tphaLogMsg, "Test 9", String.valueOf(tphawt.getTest9()),
							null);
					tphawt.setTest9(null);
				}
			}
		}
		if (!"".equals(tphaLogMsg)) {
			qisTransactionLabTPHAWTRepository.save(tphawt);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, tphaLogMsg, laboratoryId, CATEGORY + "-TPHA");
		}

		if (isAdded) {
			return tphawt;
		}

		return null;
	}

	private QisTransactionMedSer saveMEDSER(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String medSerLogMsg = "";

		QisTransactionMedSer medSer = qisTransactionMedSer.getTransactionMedSerByLabReqId(laboratoryId);

		QisTransactionMedSerRequest medSerRequest = seRequest.getMedSer();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(medSerRequest.getReferenceLabId());

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

		if (medSer == null) {
			medSer = new QisTransactionMedSer();
			BeanUtils.copyProperties(seRequest.getSerology(), medSer);
			medSer.setId(laboratoryId);
			medSer.setCreatedBy(authUser.getId());
			medSer.setUpdatedBy(authUser.getId());

			if (referenceLab != null) {
				medSer.setReferenceLab(referenceLab);
				medSer.setReferenceLabId(referenceLab.getId());
			}
			medSerLogMsg = seRequest.getMedSer().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			medSer.setUpdatedBy(authUser.getId());
			medSer.setUpdatedAt(Calendar.getInstance());

			if (medSerRequest.getReferenceLabId() != null) {
				if (medSer.getReferenceLab() != null) {
					if (medSerRequest.getReferenceLabId() != medSer.getReferenceLab().getReferenceid()) {
						medSerLogMsg = appUtility.formatUpdateData(medSerLogMsg, "Reference Laboratory",
								String.valueOf(medSer.getReferenceLab().getReferenceid()),
								String.valueOf(medSerRequest.getReferenceLabId()));
						if (referenceLab != null) {
							medSer.setReferenceLab(referenceLab);
							medSer.setReferenceLabId(referenceLab.getId());
						} else {
							medSer.setReferenceLab(null);
							medSer.setReferenceLabId(null);
						}
					}
				} else {
					medSerLogMsg = appUtility.formatUpdateData(medSerLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(medSerRequest.getReferenceLabId()));
					if (referenceLab != null) {
						medSer.setReferenceLab(referenceLab);
						medSer.setReferenceLabId(referenceLab.getId());
					} else {
						medSer.setReferenceLab(null);
						medSer.setReferenceLabId(null);
					}
				}
			} else {
				if (medSer.getReferenceLab() != null) {
					medSerLogMsg = appUtility.formatUpdateData(medSerLogMsg, "Reference Laboratory",
							String.valueOf(medSer.getReferenceLab().getReferenceid()), null);
					medSer.setReferenceLab(null);
					medSer.setReferenceLabId(null);
				}
			}
		}
		if (!"".equals(medSerLogMsg)) {
			qisTransactionMedSer.save(medSer);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, medSerLogMsg, laboratoryId, CATEGORY + "-SERO");
		}

		if (isAdded) {
			return medSer;
		}
		return null;
	}

	private QisTransactionLabSerology saveSerology(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String seroLogMsg = "";
		QisTransactionLabSerology sero = qisTransactionLabSerologyRepository
				.getTransactionLabSerologyByLabReqId(laboratoryId);

		QisTransactionLabSESerologyRequest sRequest = seRequest.getSerology();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(sRequest.getReferenceLabId());

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
		if (sero == null) {
			sero = new QisTransactionLabSerology();
			BeanUtils.copyProperties(seRequest.getSerology(), sero);
			sero.setId(laboratoryId);
			sero.setCreatedBy(authUser.getId());
			sero.setUpdatedBy(authUser.getId());

			if (referenceLab != null) {
				sero.setReferenceLab(referenceLab);
				sero.setReferenceLabId(referenceLab.getId());
			}
			seroLogMsg = seRequest.getSerology().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			sero.setUpdatedBy(authUser.getId());
			sero.setUpdatedAt(Calendar.getInstance());

			if (sRequest.getReferenceLabId() != null) {
				if (sero.getReferenceLab() != null) {
					if (sRequest.getReferenceLabId() != sero.getReferenceLab().getReferenceid()) {
						seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "Reference Laboratory",
								String.valueOf(sero.getReferenceLab().getReferenceid()),
								String.valueOf(sRequest.getReferenceLabId()));
						if (referenceLab != null) {
							sero.setReferenceLab(referenceLab);
							sero.setReferenceLabId(referenceLab.getId());
						} else {
							sero.setReferenceLab(null);
							sero.setReferenceLabId(null);
						}
					}
				} else {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(sRequest.getReferenceLabId()));
					if (referenceLab != null) {
						sero.setReferenceLab(referenceLab);
						sero.setReferenceLabId(referenceLab.getId());
					} else {
						sero.setReferenceLab(null);
						sero.setReferenceLabId(null);
					}
				}
			} else {
				if (sero.getReferenceLab() != null) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "Reference Laboratory",
							String.valueOf(sero.getReferenceLab().getReferenceid()), null);
					sero.setReferenceLab(null);
					sero.setReferenceLabId(null);
				}
			}

			if (sRequest.getHbsAg() != null) {
				if (sRequest.getHbsAg() != sero.getHbsAg()) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "HbsAg", String.valueOf(sero.getHbsAg()),
							String.valueOf(sRequest.getHbsAg()));
					sero.setHbsAg(sRequest.getHbsAg());
				}
			} else {
				if (sero.getHbsAg() != null) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "HbsAg", String.valueOf(sero.getHbsAg()),
							null);
					sero.setHbsAg(null);
				}
			}

//			if (sRequest.getAntiHbc() != null) {
//				if (sRequest.getAntiHbc() != sero.getAntiHbc()) {
//					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "HBC", String.valueOf(sero.getAntiHbc()),
//							String.valueOf(sRequest.getAntiHbc()));
//					sero.setAntiHbc(sRequest.getAntiHbc());
//				}
//			} else {
//				if (sero.getAntiHbc() != null) {
//					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "HBC", String.valueOf(sero.getAntiHbc()),
//							null);
//					sero.setAntiHbc(null);
//				}
//			}

			if (sRequest.getAntiHav() != null) {
				if (sRequest.getAntiHav() != sero.getAntiHav()) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "AntiHav", String.valueOf(sero.getAntiHav()),
							String.valueOf(sRequest.getAntiHav()));
					sero.setAntiHav(sRequest.getAntiHav());
				}
			} else {
				if (sero.getAntiHav() != null) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "AntiHav", String.valueOf(sero.getAntiHav()),
							null);
					sero.setAntiHav(null);
				}
			}

			if (sRequest.getVdrlRpr() != null) {
				if (sRequest.getVdrlRpr() != sero.getVdrlRpr()) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "VdrlRpr", String.valueOf(sero.getVdrlRpr()),
							String.valueOf(sRequest.getVdrlRpr()));
					sero.setVdrlRpr(sRequest.getVdrlRpr());
				}
			} else {
				if (sero.getVdrlRpr() != null) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "VdrlRpr", String.valueOf(sero.getVdrlRpr()),
							null);
					sero.setVdrlRpr(null);
				}
			}

			if (sRequest.getAntiHbs() != null) {
				if (sRequest.getAntiHbs() != sero.getAntiHbs()) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "AntiHbs", String.valueOf(sero.getAntiHbs()),
							String.valueOf(sRequest.getAntiHbs()));
					sero.setAntiHbs(sRequest.getAntiHbs());
				}
			} else {
				if (sero.getAntiHbs() != null) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "AntiHbs", String.valueOf(sero.getAntiHbs()),
							null);
					sero.setAntiHbs(null);
				}
			}

			if (sRequest.getHbeAg() != null) {
				if (sRequest.getHbeAg() != sero.getHbeAg()) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "HbeAg", String.valueOf(sero.getHbeAg()),
							String.valueOf(sRequest.getHbeAg()));
					sero.setHbeAg(sRequest.getHbeAg());
				}
			} else {
				if (sero.getHbeAg() != null) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "HbeAg", String.valueOf(sero.getHbeAg()),
							null);
					sero.setHbeAg(null);
				}
			}

			if (sRequest.getAntiHbe() != null) {
				if (sRequest.getAntiHbe() != sero.getAntiHbe()) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "AntiHbe", String.valueOf(sero.getAntiHbe()),
							String.valueOf(sRequest.getAntiHbe()));
					sero.setAntiHbe(sRequest.getAntiHbe());
				}
			} else {
				if (sero.getAntiHbe() != null) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "AntiHbe", String.valueOf(sero.getAntiHbe()),
							null);
					sero.setAntiHbe(null);
				}
			}

			if (sRequest.getAbs() == null) {
				seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "Abs", String.valueOf(sero.getAbs()), null);
			}

			if (sRequest.getCutOffValue() == null) {
				seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "CutOffValue",
						String.valueOf(sero.getCutOffValue()), null);
			}

			if (sRequest.getAntiHbc() != null) {
				if (sRequest.getAntiHbc() != sero.getAntiHbc()) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "AntiHbc", String.valueOf(sero.getAntiHbc()),
							String.valueOf(sRequest.getAntiHbc()));
					sero.setAntiHbc(sRequest.getAntiHbc());
				}
			} else {
				if (sero.getAntiHbc() != null) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "AntiHbc", String.valueOf(sero.getAntiHbc()),
							null);
					sero.setAntiHbc(null);
				}
			}

			if (sRequest.getAntihcv() != null) {
				if (sRequest.getAntihcv() != sero.getAntiHcv()) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "AntiHcv", String.valueOf(sero.getAntiHcv()),
							String.valueOf(sRequest.getAntihcv()));
					sero.setAntiHcv(sRequest.getAntihcv());
				}
			} else {
				if (sero.getAntiHcv() != null) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "AntiHcv", String.valueOf(sero.getAntiHcv()),
							null);
					sero.setAntiHcv(null);
				}
			}

			if (sRequest.getTppa() != null) {
				if (sRequest.getTppa() != sero.getTppa()) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "Tppa", String.valueOf(sero.getTppa()),
							String.valueOf(sRequest.getTppa()));
					sero.setTppa(sRequest.getTppa());
				}
			} else {
				if (sero.getTppa() != null) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "Tppa", String.valueOf(sero.getTppa()), null);
					sero.setTppa(null);
				}
			}

			if (sRequest.getPregnancyTest() != null) {
				if (sRequest.getPregnancyTest() != sero.getPregnancyTest()) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "PT", String.valueOf(sero.getPregnancyTest()),
							String.valueOf(sRequest.getPregnancyTest()));
					sero.setPregnancyTest(sRequest.getPregnancyTest());
				}
			} else {
				if (sero.getPregnancyTest() != null) {
					seroLogMsg = appUtility.formatUpdateData(seroLogMsg, "PT", String.valueOf(sero.getPregnancyTest()),
							null);
					sero.setPregnancyTest(null);
				}
			}

		}

		if (!"".equals(seroLogMsg)) {
			qisTransactionLabSerologyRepository.save(sero);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, seroLogMsg, laboratoryId, CATEGORY + "-SERO");
		}

		if (isAdded) {
			return sero;
		}

		return null;
	}

	private QisTransactionLabThyroid saveThyroid(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String thyLogMsg = "";
		QisTransactionLabThyroid thy = qisTransactionLabThyroidRepository
				.getTransactionLabThyroidByLabReqId(laboratoryId);
		QisTransactionLabSEThyroidRequest tRequest = seRequest.getThyroid();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(tRequest.getReferenceLabId());

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

		if (thy == null) {
			thy = new QisTransactionLabThyroid();
			BeanUtils.copyProperties(seRequest.getThyroid(), thy);
			thy.setId(laboratoryId);
			thy.setCreatedBy(authUser.getId());
			thy.setUpdatedBy(authUser.getId());

			if (tRequest.getTsh() != null) {
				Float value = appUtility.parseFloatAmount(tRequest.getTsh());
				thy.setTsh(value);
			}

			if (tRequest.getFt3() != null) {
				Float value = appUtility.parseFloatAmount(tRequest.getFt3());
				thy.setFt3(value);
			}

			if (tRequest.getFt4() != null) {
				Float value = appUtility.parseFloatAmount(tRequest.getFt4());
				thy.setFt4(value);
			}

			if (tRequest.getT3() != null) {
				Float value = appUtility.parseFloatAmount(tRequest.getT3());
				thy.setT3(value);
			}

			if (tRequest.getT4() != null) {
				Float value = appUtility.parseFloatAmount(tRequest.getT4());
				thy.setT4(value);
			}

			if (referenceLab != null) {
				thy.setReferenceLab(referenceLab);
				thy.setReferenceLabId(referenceLab.getId());
			}

			thyLogMsg = seRequest.getThyroid().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			thy.setUpdatedBy(authUser.getId());
			thy.setUpdatedAt(Calendar.getInstance());

			if (tRequest.getReferenceLabId() != null) {
				if (thy.getReferenceLab() != null) {
					if (tRequest.getReferenceLabId() != thy.getReferenceLab().getReferenceid()) {
						thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "Reference Laboratory",
								String.valueOf(thy.getReferenceLab().getReferenceid()),
								String.valueOf(tRequest.getReferenceLabId()));
						if (referenceLab != null) {
							thy.setReferenceLab(referenceLab);
							thy.setReferenceLabId(referenceLab.getId());
						} else {
							thy.setReferenceLab(null);
							thy.setReferenceLabId(null);
						}
					}
				} else {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(tRequest.getReferenceLabId()));
					if (referenceLab != null) {
						thy.setReferenceLab(referenceLab);
						thy.setReferenceLabId(referenceLab.getId());
					} else {
						thy.setReferenceLab(null);
						thy.setReferenceLabId(null);
					}
				}
			} else {
				if (thy.getReferenceLab() != null) {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "Reference Laboratory",
							String.valueOf(thy.getReferenceLab().getReferenceid()), null);
					thy.setReferenceLab(null);
					thy.setReferenceLabId(null);
				}
			}

			if (tRequest.getTsh() != null) {
				Float value = appUtility.parseFloatAmount(tRequest.getTsh());
				if (!value.equals(thy.getTsh())) {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "Tsh", String.valueOf(thy.getTsh()),
							String.valueOf(value));
					thy.setTsh(value);
				}
			} else {
				if (thy.getTsh() != null) {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "Tsh", String.valueOf(thy.getTsh()), null);
					thy.setTsh(null);
				}
			}

			if (tRequest.getFt3() != null) {
				Float value = appUtility.parseFloatAmount(tRequest.getFt3());
				if (!value.equals(thy.getFt3())) {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "Ft3", String.valueOf(thy.getFt3()),
							String.valueOf(value));
					thy.setFt3(value);
				}
			} else {
				if (thy.getFt3() != null) {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "Ft3", String.valueOf(thy.getFt3()), null);
					thy.setFt3(null);
				}
			}

			if (tRequest.getFt4() != null) {
				Float value = appUtility.parseFloatAmount(tRequest.getFt4());
				if (!value.equals(thy.getFt4())) {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "Ft4", String.valueOf(thy.getFt4()),
							String.valueOf(value));
					thy.setFt4(value);
				}
			} else {
				if (thy.getFt4() != null) {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "Ft4", String.valueOf(thy.getFt4()), null);
					thy.setFt4(null);
				}
			}

			if (tRequest.getT3() != null) {
				Float value = appUtility.parseFloatAmount(tRequest.getT3());
				if (!value.equals(thy.getT3())) {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "getT3", String.valueOf(thy.getT3()),
							String.valueOf(value));
					thy.setT3(value);
				}
			} else {
				if (thy.getT3() != null) {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "getT3", String.valueOf(thy.getT3()), null);
					thy.setT3(null);
				}
			}

			if (tRequest.getT4() != null) {
				Float value = appUtility.parseFloatAmount(tRequest.getT4());
				if (!value.equals(thy.getT4())) {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "getT4", String.valueOf(thy.getT4()),
							String.valueOf(value));
					thy.setT4(value);
				}
			} else {
				if (thy.getT4() != null) {
					thyLogMsg = appUtility.formatUpdateData(thyLogMsg, "getT4", String.valueOf(thy.getT4()), null);
					thy.setT4(null);
				}
			}

		}

		if (!"".equals(thyLogMsg)) {
			qisTransactionLabThyroidRepository.save(thy);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, thyLogMsg, laboratoryId, CATEGORY + "-THYR");
		}

		if (isAdded) {
			return thy;
		}

		return null;
	}

	private QisTransactionLabTyphidot saveTyphidot(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String typLogMsg = "";
		QisTransactionLabTyphidot typ = qisTransactionLabTyphidotRepository
				.getTransactionLabTyphidotByLabReqId(laboratoryId);

		QisTransactionLabSETyphidotRequest tRequest = seRequest.getTyphidot();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(tRequest.getReferenceLabId());

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

		if (typ == null) {
			typ = new QisTransactionLabTyphidot();
			BeanUtils.copyProperties(seRequest.getTyphidot(), typ);
			typ.setId(laboratoryId);
			typ.setCreatedBy(authUser.getId());
			typ.setUpdatedBy(authUser.getId());

			if (referenceLab != null) {
				typ.setReferenceLab(referenceLab);
				typ.setReferenceLabId(referenceLab.getId());
			}
			typLogMsg = seRequest.getTyphidot().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			typ.setUpdatedBy(authUser.getId());
			typ.setUpdatedAt(Calendar.getInstance());

			if (tRequest.getReferenceLabId() != null) {
				if (typ.getReferenceLab() != null) {
					if (tRequest.getReferenceLabId() != typ.getReferenceLab().getReferenceid()) {
						typLogMsg = appUtility.formatUpdateData(typLogMsg, "Reference Laboratory",
								String.valueOf(typ.getReferenceLab().getReferenceid()),
								String.valueOf(tRequest.getReferenceLabId()));
						if (referenceLab != null) {
							typ.setReferenceLab(referenceLab);
							typ.setReferenceLabId(referenceLab.getId());
						} else {
							typ.setReferenceLab(null);
							typ.setReferenceLabId(null);
						}
					}
				} else {
					typLogMsg = appUtility.formatUpdateData(typLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(tRequest.getReferenceLabId()));
					if (referenceLab != null) {
						typ.setReferenceLab(referenceLab);
						typ.setReferenceLabId(referenceLab.getId());
					} else {
						typ.setReferenceLab(null);
						typ.setReferenceLabId(null);
					}
				}
			} else {
				if (typ.getReferenceLab() != null) {
					typLogMsg = appUtility.formatUpdateData(typLogMsg, "Reference Laboratory",
							String.valueOf(typ.getReferenceLab().getReferenceid()), null);
					typ.setReferenceLab(null);
					typ.setReferenceLabId(null);
				}
			}

			if (tRequest.getIgm() != null) {
				if (tRequest.getIgm() != typ.getIgm()) {
					typLogMsg = appUtility.formatUpdateData(typLogMsg, "Igm", String.valueOf(typ.getIgm()),
							String.valueOf(tRequest.getIgm()));
					typ.setIgm(tRequest.getIgm());
				}
			} else {
				if (typ.getIgm() != null) {
					typLogMsg = appUtility.formatUpdateData(typLogMsg, "Igm", String.valueOf(typ.getIgm()), null);
					typ.setIgm(null);
				}
			}

			if (tRequest.getIgg() != null) {
				if (tRequest.getIgg() != typ.getIgg()) {
					typLogMsg = appUtility.formatUpdateData(typLogMsg, "Igg", String.valueOf(typ.getIgg()),
							String.valueOf(tRequest.getIgg()));
					typ.setIgg(tRequest.getIgg());
				}
			} else {
				if (typ.getIgg() != null) {
					typLogMsg = appUtility.formatUpdateData(typLogMsg, "Igg", String.valueOf(typ.getIgg()), null);
					typ.setIgg(null);
				}
			}
		}

		if (!"".equals(typLogMsg)) {
			qisTransactionLabTyphidotRepository.save(typ);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, typLogMsg, laboratoryId, CATEGORY + "-TYPH");
		}

		if (isAdded) {
			return typ;
		}

		return null;
	}

	private QisTransactionLabASO saveASO(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String asoLogMsg = "";

		QisTransactionLabASO aso = qisTransactionlabASORepository.getTransactionLabASOByLabReqId(laboratoryId);

		QisTransactionLabSEASORequest asoRequest = seRequest.getAso();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(asoRequest.getReferenceLabId());

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

		if (aso == null) {
			aso = new QisTransactionLabASO();
			BeanUtils.copyProperties(seRequest.getAso(), aso);
			aso.setId(laboratoryId);
			aso.setCreatedBy(authUser.getId());
			aso.setUpdatedBy(authUser.getId());

			if (asoRequest.getResult1() != null) {
				aso.setResult1(asoRequest.getResult1());
			}

			if (asoRequest.getResult2() != null) {
				aso.setResult2(asoRequest.getResult2());
			}

			if (asoRequest.getResult3() != null) {
				aso.setResult3(asoRequest.getResult3());
			}

			if (asoRequest.getResult4() != null) {
				aso.setResult4(asoRequest.getResult4());
			}

			if (asoRequest.getResult5() != null) {
				aso.setResult4(asoRequest.getResult5());
			}

			if (referenceLab != null) {
				aso.setReferenceLab(referenceLab);
				aso.setReferenceLabId(referenceLab.getId());
			}

			asoLogMsg = seRequest.getAso().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			aso.setUpdatedBy(authUser.getId());
			aso.setUpdatedAt(Calendar.getInstance());

			if (asoRequest.getReferenceLabId() != null) {
				if (aso.getReferenceLab() != null) {
					if (asoRequest.getReferenceLabId() != aso.getReferenceLab().getReferenceid()) {
						asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "Reference Laboratory",
								String.valueOf(aso.getReferenceLab().getReferenceid()),
								String.valueOf(asoRequest.getReferenceLabId()));
						if (referenceLab != null) {
							aso.setReferenceLab(referenceLab);
							aso.setReferenceLabId(referenceLab.getId());
						} else {
							aso.setReferenceLab(null);
							aso.setReferenceLabId(null);
						}
					}
				} else {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(asoRequest.getReferenceLabId()));
					if (referenceLab != null) {
						aso.setReferenceLab(referenceLab);
						aso.setReferenceLabId(referenceLab.getId());
					} else {
						aso.setReferenceLab(null);
						aso.setReferenceLabId(null);
					}
				}
			} else {
				if (aso.getReferenceLab() != null) {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "Reference Laboratory",
							String.valueOf(aso.getReferenceLab().getReferenceid()), null);
					aso.setReferenceLab(null);
					aso.setReferenceLabId(null);
				}
			}

			if (asoRequest.getResult1() != null) {
				if (!asoRequest.getResult1().equals(aso.getResult1())) {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "First", String.valueOf(aso.getResult1()),
							String.valueOf(asoRequest.getResult1()));
					aso.setResult1(asoRequest.getResult1());
				}
			} else {
				if (aso.getResult1() != null) {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "First", String.valueOf(aso.getResult1()), null);
					aso.setResult1(null);
				}
			}

			if (asoRequest.getResult2() != null) {
				if (!asoRequest.getResult2().equals(aso.getResult2())) {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "Second", String.valueOf(aso.getResult2()),
							String.valueOf(asoRequest.getResult2()));
					aso.setResult1(asoRequest.getResult1());
				}
			} else {
				if (aso.getResult2() != null) {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "Second", String.valueOf(aso.getResult2()),
							null);
					aso.setResult2(null);
				}
			}

			if (asoRequest.getResult3() != null) {
				if (!asoRequest.getResult3().equals(aso.getResult3())) {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "third", String.valueOf(aso.getResult3()),
							String.valueOf(asoRequest.getResult3()));
					aso.setResult3(asoRequest.getResult3());
				}
			} else {
				if (aso.getResult3() != null) {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "third", String.valueOf(aso.getResult3()), null);
					aso.setResult3(null);
				}
			}

			if (asoRequest.getResult4() != null) {
				if (!asoRequest.getResult4().equals(aso.getResult4())) {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "fourth", String.valueOf(aso.getResult4()),
							String.valueOf(asoRequest.getResult4()));
					aso.setResult4(asoRequest.getResult4());
				}
			} else {
				if (aso.getResult4() != null) {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "fourth", String.valueOf(aso.getResult4()),
							null);
					aso.setResult4(null);
				}
			}

			if (asoRequest.getResult5() != null) {
				if (!asoRequest.getResult5().equals(aso.getResult5())) {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "fifth", String.valueOf(aso.getResult5()),
							String.valueOf(asoRequest.getResult5()));
					aso.setResult5(asoRequest.getResult5());
				}
			} else {
				if (aso.getResult5() != null) {
					asoLogMsg = appUtility.formatUpdateData(asoLogMsg, "fifth", String.valueOf(aso.getResult5()), null);
					aso.setResult5(null);
				}
			}
		}

		if (!"".equals(asoLogMsg)) {
			qisTransactionlabASORepository.save(aso);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, asoLogMsg, laboratoryId, CATEGORY + "-ASO");
		}

		if (isAdded) {
			return aso;
		}

		return null;
	}

	private QisTransactionLabRFT saveRFT(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String rftLogMsg = "";

		QisTransactionLabRFT rft = qisTransactionLabRFTRepository.getTransactionLabRFTByLabReqId(laboratoryId);

		QisTransactionLabSERFTRequest rftRequest = seRequest.getRft();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(rftRequest.getReferenceLabId());

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

		if (rft == null) {
			rft = new QisTransactionLabRFT();
			BeanUtils.copyProperties(seRequest.getRft(), rft);
			rft.setId(laboratoryId);
			rft.setCreatedBy(authUser.getId());
			rft.setUpdatedBy(authUser.getId());

			if (rftRequest.getFirst() != null) {
				rft.setFirst(rftRequest.getFirst());
			}

			if (rftRequest.getSecond() != null) {
				rft.setSecond(rftRequest.getSecond());
			}

			if (rftRequest.getThird() != null) {
				rft.setThird(rftRequest.getThird());
			}

			if (rftRequest.getFourth() != null) {
				rft.setFourth(rftRequest.getFourth());
			}

			if (rftRequest.getFifth() != null) {
				rft.setFifth(rftRequest.getFifth());
			}

			if (referenceLab != null) {
				rft.setReferenceLab(referenceLab);
				rft.setReferenceLabId(referenceLab.getId());
			}

			rftLogMsg = seRequest.getRft().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			rft.setUpdatedBy(authUser.getId());
			rft.setUpdatedAt(Calendar.getInstance());

			if (rftRequest.getReferenceLabId() != null) {
				if (rft.getReferenceLab() != null) {
					if (rftRequest.getReferenceLabId() != rft.getReferenceLab().getReferenceid()) {
						rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "Reference Laboratory",
								String.valueOf(rft.getReferenceLab().getReferenceid()),
								String.valueOf(rftRequest.getReferenceLabId()));
						if (referenceLab != null) {
							rft.setReferenceLab(referenceLab);
							rft.setReferenceLabId(referenceLab.getId());
						} else {
							rft.setReferenceLab(null);
							rft.setReferenceLabId(null);
						}
					}
				} else {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(rftRequest.getReferenceLabId()));
					if (referenceLab != null) {
						rft.setReferenceLab(referenceLab);
						rft.setReferenceLabId(referenceLab.getId());
					} else {
						rft.setReferenceLab(null);
						rft.setReferenceLabId(null);
					}
				}
			} else {
				if (rft.getReferenceLab() != null) {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "Reference Laboratory",
							String.valueOf(rft.getReferenceLab().getReferenceid()), null);
					rft.setReferenceLab(null);
					rft.setReferenceLabId(null);
				}
			}

			if (rftRequest.getFirst() != null) {
				if (!rftRequest.getFirst().equals(rft.getFirst())) {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "First", String.valueOf(rft.getFirst()),
							String.valueOf(rftRequest.getFirst()));
					rft.setFirst(rftRequest.getFirst());
				}
			} else {
				if (rft.getFirst() != null) {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "First", String.valueOf(rft.getFirst()), null);
					rft.setFirst(null);
				}
			}

			if (rftRequest.getSecond() != null) {
				if (!rftRequest.getSecond().equals(rft.getSecond())) {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "Second", String.valueOf(rft.getSecond()),
							String.valueOf(rftRequest.getSecond()));
					rft.setSecond(rftRequest.getSecond());
				}
			} else {
				if (rft.getSecond() != null) {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "Second", String.valueOf(rft.getSecond()), null);
					rft.setSecond(null);
				}
			}

			if (rftRequest.getThird() != null) {
				if (!rftRequest.getThird().equals(rft.getThird())) {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "Third", String.valueOf(rft.getThird()),
							String.valueOf(rftRequest.getThird()));
					rft.setThird(rftRequest.getThird());
				}
			} else {
				if (rft.getThird() != null) {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "Third", String.valueOf(rft.getThird()), null);
					rft.setThird(null);
				}
			}

			if (rftRequest.getFourth() != null) {
				if (!rftRequest.getFourth().equals(rft.getFourth())) {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "Fourth", String.valueOf(rft.getFourth()),
							String.valueOf(rftRequest.getFourth()));
					rft.setFourth(rftRequest.getFourth());
				}
			} else {
				if (rft.getFourth() != null) {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "Fourth", String.valueOf(rft.getFourth()), null);
					rft.setFourth(null);
				}
			}

			if (rftRequest.getFifth() != null) {
				if (!rftRequest.getFifth().equals(rft.getFifth())) {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "Fifth", String.valueOf(rft.getFifth()),
							String.valueOf(rftRequest.getFifth()));
					rft.setFifth(rftRequest.getFifth());
				}
			} else {
				if (rft.getFifth() != null) {
					rftLogMsg = appUtility.formatUpdateData(rftLogMsg, "Fifth", String.valueOf(rft.getFifth()), null);
					rft.setFifth(null);
				}
			}
		}

		if (!"".equals(rftLogMsg)) {
			qisTransactionLabRFTRepository.save(rft);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, rftLogMsg, laboratoryId, CATEGORY + "-RFT");
		}

		if (isAdded) {
			return rft;
		}

		return null;
	}

	private QisTransactionLabCRP saveCRP(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String crpLogMsg = "";
		QisTransactionLabCRP crp = qisTransactionLabCRPRepository.getTransactionLabCRPByLabReqId(laboratoryId);

		QisTransactionLabSECRPRequest crpRequest = seRequest.getCrp();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(crpRequest.getReferenceLabId());

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

		if (crp == null) {
			crp = new QisTransactionLabCRP();
			BeanUtils.copyProperties(seRequest.getCrp(), crp);
			crp.setId(laboratoryId);
			crp.setCreatedBy(authUser.getId());
			crp.setUpdatedBy(authUser.getId());

			if (crpRequest.getDilution() != null) {
				Float value = appUtility.parseFloatAmount(crpRequest.getDilution());
				crp.setDilution(value);
			}

			if (crpRequest.getResult() != null) {
				Float value = appUtility.parseFloatAmount(crpRequest.getResult());
				crp.setResult(value);
			}

			if (referenceLab != null) {
				crp.setReferenceLab(referenceLab);
				crp.setReferenceLabId(referenceLab.getId());
			}

			crpLogMsg = seRequest.getCrp().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			crp.setUpdatedBy(authUser.getId());
			crp.setUpdatedAt(Calendar.getInstance());

			if (crpRequest.getReferenceLabId() != null) {
				if (crp.getReferenceLab() != null) {
					if (crpRequest.getReferenceLabId() != crp.getReferenceLab().getReferenceid()) {
						crpLogMsg = appUtility.formatUpdateData(crpLogMsg, "Reference Laboratory",
								String.valueOf(crp.getReferenceLab().getReferenceid()),
								String.valueOf(crpRequest.getReferenceLabId()));
						if (referenceLab != null) {
							crp.setReferenceLab(referenceLab);
							crp.setReferenceLabId(referenceLab.getId());
						} else {
							crp.setReferenceLab(null);
							crp.setReferenceLabId(null);
						}
					}
				} else {
					crpLogMsg = appUtility.formatUpdateData(crpLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(crpRequest.getReferenceLabId()));
					if (referenceLab != null) {
						crp.setReferenceLab(referenceLab);
						crp.setReferenceLabId(referenceLab.getId());
					} else {
						crp.setReferenceLab(null);
						crp.setReferenceLabId(null);
					}
				}
			} else {
				if (crp.getReferenceLab() != null) {
					crpLogMsg = appUtility.formatUpdateData(crpLogMsg, "Reference Laboratory",
							String.valueOf(crp.getReferenceLab().getReferenceid()), null);
					crp.setReferenceLab(null);
					crp.setReferenceLabId(null);
				}
			}

			if (crpRequest.getDilution() != null) {
				Float value = appUtility.parseFloatAmount(crpRequest.getDilution());
				if (!value.equals(crp.getDilution())) {
					crpLogMsg = appUtility.formatUpdateData(crpLogMsg, "Dilution", String.valueOf(crp.getDilution()),
							String.valueOf(value));
					crp.setDilution(value);
				}
			} else {
				if (crp.getDilution() != null) {
					crpLogMsg = appUtility.formatUpdateData(crpLogMsg, "Dilution", String.valueOf(crp.getDilution()),
							null);
					crp.setDilution(null);
				}
			}

			if (crpRequest.getResult() != null) {
				Float value = appUtility.parseFloatAmount(crpRequest.getResult());
				if (!value.equals(crp.getResult())) {
					crpLogMsg = appUtility.formatUpdateData(crpLogMsg, "Result", String.valueOf(crp.getResult()),
							String.valueOf(value));
					crp.setResult(value);
				}
			} else {
				if (crp.getResult() != null) {
					crpLogMsg = appUtility.formatUpdateData(crpLogMsg, "Result", String.valueOf(crp.getResult()), null);
					crp.setResult(null);
				}
			}
		}

		if (!"".equals(crpLogMsg)) {
			qisTransactionLabCRPRepository.save(crp);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, crpLogMsg, laboratoryId, CATEGORY + "-CRP");
		}

		if (isAdded) {
			return crp;
		}

		return null;
	}

	private QisTransactionLabHIV saveHIV(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String hivLogMsg = "";
		QisTransactionLabHIV hiv = qisTransactionLabHIVRepository.getTransactionLabHIVByLabReqId(laboratoryId);
		QisTransactionLabSEHIVRequest hivRequest = seRequest.getHiv();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(hivRequest.getReferenceLabId());

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

		if (hiv == null) {
			hiv = new QisTransactionLabHIV();
			BeanUtils.copyProperties(seRequest.getHiv(), hiv);
			hiv.setId(laboratoryId);
			hiv.setCreatedBy(authUser.getId());
			hiv.setUpdatedBy(authUser.getId());

			if (referenceLab != null) {
				hiv.setReferenceLab(referenceLab);
				hiv.setReferenceLabId(referenceLab.getId());
			}
			hivLogMsg = seRequest.getHiv().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			hiv.setUpdatedBy(authUser.getId());
			hiv.setUpdatedAt(Calendar.getInstance());

			if (hivRequest.getReferenceLabId() != null) {
				if (hiv.getReferenceLab() != null) {
					if (hivRequest.getReferenceLabId() != hiv.getReferenceLab().getReferenceid()) {
						hivLogMsg = appUtility.formatUpdateData(hivLogMsg, "Reference Laboratory",
								String.valueOf(hiv.getReferenceLab().getReferenceid()),
								String.valueOf(hivRequest.getReferenceLabId()));
						if (referenceLab != null) {
							hiv.setReferenceLab(referenceLab);
							hiv.setReferenceLabId(referenceLab.getId());
						} else {
							hiv.setReferenceLab(null);
							hiv.setReferenceLabId(null);
						}
					}
				} else {
					hivLogMsg = appUtility.formatUpdateData(hivLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(hivRequest.getReferenceLabId()));
					if (referenceLab != null) {
						hiv.setReferenceLab(referenceLab);
						hiv.setReferenceLabId(referenceLab.getId());
					} else {
						hiv.setReferenceLab(null);
						hiv.setReferenceLabId(null);
					}
				}
			} else {
				if (hiv.getReferenceLab() != null) {
					hivLogMsg = appUtility.formatUpdateData(hivLogMsg, "Reference Laboratory",
							String.valueOf(hiv.getReferenceLab().getReferenceid()), null);
					hiv.setReferenceLab(null);
					hiv.setReferenceLabId(null);
				}
			}

			if (hivRequest.getTest1() != null) {
				if (hivRequest.getTest1() != hiv.getTest1()) {
					hivLogMsg = appUtility.formatUpdateData(hivLogMsg, "Test1", String.valueOf(hiv.getTest1()),
							String.valueOf(hivRequest.getTest1()));
					hiv.setTest1(hivRequest.getTest1());
				}
			} else {
				if (hiv.getTest1() != null) {
					hivLogMsg = appUtility.formatUpdateData(hivLogMsg, "Test1", String.valueOf(hiv.getTest1()), null);
					hiv.setTest1(null);
				}
			}

			if (hivRequest.getTest2() != null) {
				if (hivRequest.getTest2() != hiv.getTest2()) {
					hivLogMsg = appUtility.formatUpdateData(hivLogMsg, "Test2", String.valueOf(hiv.getTest2()),
							String.valueOf(hivRequest.getTest2()));
					hiv.setTest2(hivRequest.getTest2());
				}
			} else {
				if (hiv.getTest2() != null) {
					hivLogMsg = appUtility.formatUpdateData(hivLogMsg, "Test2", String.valueOf(hiv.getTest2()), null);
					hiv.setTest2(null);
				}
			}
		}

		if (!"".equals(hivLogMsg)) {
			qisTransactionLabHIVRepository.save(hiv);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, hivLogMsg, laboratoryId, CATEGORY + "-HIV");
		}

		if (isAdded) {
			return hiv;
		}

		return null;
	}

	private QisTransactionLabAntigen saveAntigen(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String antLogMsg = "";
		QisTransactionLabAntigen anti = qisTransactionLabAntigenRepository
				.getTransactionLabAntigenByLabReqId(laboratoryId);

		QisTransactionLabSEAntigenRequest antpRequest = seRequest.getAntigen();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(antpRequest.getReferenceLabId());

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

		if (anti == null) {
			anti = new QisTransactionLabAntigen();
			BeanUtils.copyProperties(seRequest.getAntigen(), anti);
			anti.setId(laboratoryId);
			anti.setCreatedBy(authUser.getId());
			anti.setUpdatedBy(authUser.getId());

			if (antpRequest.getPsa() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getPsa());
				anti.setPsa(value);
			}

			if (antpRequest.getCea() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getCea());
				anti.setCea(value);
			}

			if (antpRequest.getAfp() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getAfp());
				anti.setAfp(value);
			}

			if (antpRequest.getCa125() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getCa125());
				anti.setCa125(value);
			}

			if (antpRequest.getCa199() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getCa199());
				anti.setCa199(value);
			}

			if (antpRequest.getCa153() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getCa153());
				anti.setCa153(value);
			}

			if (referenceLab != null) {
				anti.setReferenceLab(referenceLab);
				anti.setReferenceLabId(referenceLab.getId());
			}

			antLogMsg = seRequest.getAntigen().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			anti.setUpdatedBy(authUser.getId());
			anti.setUpdatedAt(Calendar.getInstance());

			if (antpRequest.getReferenceLabId() != null) {
				if (anti.getReferenceLab() != null) {
					if (antpRequest.getReferenceLabId() != anti.getReferenceLab().getReferenceid()) {
						antLogMsg = appUtility.formatUpdateData(antLogMsg, "Reference Laboratory",
								String.valueOf(anti.getReferenceLab().getReferenceid()),
								String.valueOf(antpRequest.getReferenceLabId()));
						if (referenceLab != null) {
							anti.setReferenceLab(referenceLab);
							anti.setReferenceLabId(referenceLab.getId());
						} else {
							anti.setReferenceLab(null);
							anti.setReferenceLabId(null);
						}
					}
				} else {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(antpRequest.getReferenceLabId()));
					if (referenceLab != null) {
						anti.setReferenceLab(referenceLab);
						anti.setReferenceLabId(referenceLab.getId());
					} else {
						anti.setReferenceLab(null);
						anti.setReferenceLabId(null);
					}
				}
			} else {
				if (anti.getReferenceLab() != null) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "Reference Laboratory",
							String.valueOf(anti.getReferenceLab().getReferenceid()), null);
					anti.setReferenceLab(null);
					anti.setReferenceLabId(null);
				}
			}

			if (antpRequest.getPsa() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getPsa());
				if (!value.equals(anti.getPsa())) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "Psa", String.valueOf(anti.getPsa()),
							String.valueOf(value));
					anti.setPsa(value);
				}
			} else {
				if (anti.getPsa() != null) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "Psa", String.valueOf(anti.getPsa()), null);
					anti.setPsa(null);
				}
			}

			if (antpRequest.getCea() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getCea());
				if (!value.equals(anti.getCea())) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "cea", String.valueOf(anti.getCea()),
							String.valueOf(value));
					anti.setCea(value);
				}
			} else {
				if (anti.getCea() != null) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "cea", String.valueOf(anti.getCea()), null);
					anti.setCea(null);
				}
			}

			if (antpRequest.getAfp() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getAfp());
				if (!value.equals(anti.getAfp())) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "afp", String.valueOf(anti.getAfp()),
							String.valueOf(value));
					anti.setAfp(value);
				}
			} else {
				if (anti.getAfp() != null) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "afp", String.valueOf(anti.getAfp()), null);
					anti.setAfp(null);
				}
			}

			if (antpRequest.getCa125() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getCa125());
				if (!value.equals(anti.getCa125())) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "ca125", String.valueOf(anti.getCa125()),
							String.valueOf(value));
					anti.setCa125(value);
				}
			} else {
				if (anti.getCa125() != null) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "ca125", String.valueOf(anti.getCa125()), null);
					anti.setCa125(null);
				}
			}

			if (antpRequest.getCa199() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getCa199());
				if (!value.equals(anti.getCa199())) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "ca199", String.valueOf(anti.getCa199()),
							String.valueOf(value));
					anti.setCa199(value);
				}
			} else {
				if (anti.getCa199() != null) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "ca199", String.valueOf(anti.getCa199()), null);
					anti.setCa199(null);
				}
			}

			if (antpRequest.getCa153() != null) {
				Float value = appUtility.parseFloatAmount(antpRequest.getCa153());
				if (!value.equals(anti.getCa153())) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "ca153", String.valueOf(anti.getCa153()),
							String.valueOf(value));
					anti.setCa153(value);
				}
			} else {
				if (anti.getCa153() != null) {
					antLogMsg = appUtility.formatUpdateData(antLogMsg, "ca153", String.valueOf(anti.getCa153()), null);
					anti.setCa153(null);
				}
			}
		}

		if (!"".equals(antLogMsg)) {
			qisTransactionLabAntigenRepository.save(anti);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, antLogMsg, laboratoryId, CATEGORY + "-ANTIGEN");
		}

		if (isAdded) {
			return anti;
		}

		return null;
	}

	private QisTransactionLabCovid saveCovid(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String covLogMsg = "";
		QisTransactionLabCovid covid = qisTransactionLabCovidRepository.getTransactionLabCovidByLabReqId(laboratoryId);

		QisTransactionLabSECovidRequest covRequest = seRequest.getCovid();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(covRequest.getReferenceLabId());

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

		if (covid == null) {
			covid = new QisTransactionLabCovid();
			BeanUtils.copyProperties(seRequest.getCovid(), covid);
			covid.setId(laboratoryId);
			covid.setCreatedBy(authUser.getId());
			covid.setUpdatedBy(authUser.getId());

			if (referenceLab != null) {
				covid.setReferenceLab(referenceLab);
				covid.setReferenceLabId(referenceLab.getId());
			}

			covLogMsg = seRequest.getCovid().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			covid.setUpdatedBy(authUser.getId());
			covid.setUpdatedAt(Calendar.getInstance());

			if (covRequest.getReferenceLabId() != null) {
				if (covid.getReferenceLab() != null) {
					if (covRequest.getReferenceLabId() != covid.getReferenceLab().getReferenceid()) {
						covLogMsg = appUtility.formatUpdateData(covLogMsg, "Reference Laboratory",
								String.valueOf(covid.getReferenceLab().getReferenceid()),
								String.valueOf(covRequest.getReferenceLabId()));
						if (referenceLab != null) {
							covid.setReferenceLab(referenceLab);
							covid.setReferenceLabId(referenceLab.getId());
						} else {
							covid.setReferenceLab(null);
							covid.setReferenceLabId(null);
						}
					}
				} else {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(covRequest.getReferenceLabId()));
					if (referenceLab != null) {
						covid.setReferenceLab(referenceLab);
						covid.setReferenceLabId(referenceLab.getId());
					} else {
						covid.setReferenceLab(null);
						covid.setReferenceLabId(null);
					}
				}
			} else {
				if (covid.getReferenceLab() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Reference Laboratory",
							String.valueOf(covid.getReferenceLab().getReferenceid()), null);
					covid.setReferenceLab(null);
					covid.setReferenceLabId(null);
				}
			}

			if (covRequest.getCovigg() != null) {
				if (covRequest.getCovigg() != covid.getCovigg()) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Igg", String.valueOf(covid.getCovigg()),
							String.valueOf(covRequest.getCovigg()));
					covid.setCovigg(covRequest.getCovigg());
				}
			} else {
				if (covid.getCovigg() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Igg", String.valueOf(covid.getCovigg()), null);
					covid.setCovigg(null);
				}
			}

			if (covRequest.getCovigm() != null) {
				if (covRequest.getCovigm() != covid.getCovigm()) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Igm", String.valueOf(covid.getCovigm()),
							String.valueOf(covRequest.getCovigm()));
					covid.setCovigm(covRequest.getCovigm());
				}
			} else {
				if (covid.getCovigm() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Igm", String.valueOf(covid.getCovigm()), null);
					covid.setCovigm(null);
				}
			}

			if (covRequest.getPurpose() != null) {
				if (covRequest.getPurpose() != covid.getPurpose()) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "purpose", String.valueOf(covid.getPurpose()),
							String.valueOf(covRequest.getPurpose()));
					covid.setPurpose(covRequest.getPurpose());
				}
			} else {
				if (covid.getPurpose() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "purpose", String.valueOf(covid.getPurpose()),
							null);
					covid.setPurpose(null);
				}
			}
		}

		if (!"".equals(covLogMsg)) {
			qisTransactionLabCovidRepository.save(covid);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, covLogMsg, laboratoryId, CATEGORY + "-COVID");
		}

		if (isAdded) {
			return covid;
		}

		return null;
	}

	private QisTransactionLabRtAntigen saveRtAntigen(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String covLogMsg = "";
		QisTransactionLabRtAntigen rtAntigen = qisTransactionLabRtAntigenRepository
				.getTransactionLabRtAntigenByLabReqId(laboratoryId);

		QisTransactionLabSERtAtigenRequest rtAntigenReq = seRequest.getRtantigen();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(rtAntigenReq.getReferenceLabId());

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

		if (rtAntigen == null) {
			rtAntigen = new QisTransactionLabRtAntigen();
			BeanUtils.copyProperties(seRequest.getRtantigen(), rtAntigen);
			rtAntigen.setId(laboratoryId);
			rtAntigen.setCreatedBy(authUser.getId());
			rtAntigen.setUpdatedBy(authUser.getId());

			if (referenceLab != null) {
				rtAntigen.setReferenceLab(referenceLab);
				rtAntigen.setReferenceLabId(referenceLab.getId());
			}

			covLogMsg = seRequest.getRtantigen().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			rtAntigen.setUpdatedBy(authUser.getId());
			rtAntigen.setUpdatedAt(Calendar.getInstance());

			if (rtAntigenReq.getReferenceLabId() != null) {
				if (rtAntigen.getReferenceLab() != null) {
					if (rtAntigenReq.getReferenceLabId() != rtAntigen.getReferenceLab().getReferenceid()) {
						covLogMsg = appUtility.formatUpdateData(covLogMsg, "Reference Laboratory",
								String.valueOf(rtAntigen.getReferenceLab().getReferenceid()),
								String.valueOf(rtAntigenReq.getReferenceLabId()));
						if (referenceLab != null) {
							rtAntigen.setReferenceLab(referenceLab);
							rtAntigen.setReferenceLabId(referenceLab.getId());
						} else {
							rtAntigen.setReferenceLab(null);
							rtAntigen.setReferenceLabId(null);
						}
					}
				} else {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(rtAntigenReq.getReferenceLabId()));
					if (referenceLab != null) {
						rtAntigen.setReferenceLab(referenceLab);
						rtAntigen.setReferenceLabId(referenceLab.getId());
					} else {
						rtAntigen.setReferenceLab(null);
						rtAntigen.setReferenceLabId(null);
					}
				}
			} else {
				if (rtAntigen.getReferenceLab() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Reference Laboratory",
							String.valueOf(rtAntigen.getReferenceLab().getReferenceid()), null);
					rtAntigen.setReferenceLab(null);
					rtAntigen.setReferenceLabId(null);
				}
			}

			if (rtAntigenReq.getCov_ag() != null) {
				if (rtAntigenReq.getCov_ag() != rtAntigen.getCov_ag()) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Ag", String.valueOf(rtAntigen.getCov_ag()),
							String.valueOf(rtAntigenReq.getCov_ag()));
					rtAntigen.setCov_ag(rtAntigenReq.getCov_ag());
				}
			} else {
				if (rtAntigenReq.getCov_ag() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Ag", String.valueOf(rtAntigen.getCov_ag()),
							null);
					rtAntigen.setCov_ag(null);
				}
			}

			if (rtAntigenReq.getCollectionDate() != null) {
				if (rtAntigenReq.getCollectionDate() != rtAntigen.getCollectionDate()) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Collection Date",
							String.valueOf(rtAntigen.getCollectionDate()),
							String.valueOf(rtAntigenReq.getCollectionDate()));
					rtAntigen.setCollectionDate(rtAntigenReq.getCollectionDate());
				}
			} else {
				if (rtAntigenReq.getCollectionDate() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Collection Date",
							String.valueOf(rtAntigen.getCollectionDate()), null);
					rtAntigen.setCollectionDate(null);
				}
			}

			if (rtAntigenReq.getPurpose() != null) {
				if (rtAntigenReq.getPurpose() != rtAntigen.getPurpose()) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "purpose",
							String.valueOf(rtAntigen.getPurpose()), String.valueOf(rtAntigenReq.getPurpose()));
					rtAntigen.setPurpose(rtAntigenReq.getPurpose());
				}
			} else {
				if (rtAntigenReq.getPurpose() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "purpose",
							String.valueOf(rtAntigen.getPurpose()), null);
					rtAntigen.setPurpose(null);
				}
			}
		}

		if (!"".equals(covLogMsg)) {
			qisTransactionLabRtAntigenRepository.save(rtAntigen);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, covLogMsg, laboratoryId, CATEGORY + "-antigen");
		}

		if (isAdded) {
			return rtAntigen;
		}

		return null;
	}

	private QisTransactionRTPCR saveRTPCR(QisTransactionInfo qisTransaction, Long laboratoryId,
			QisTransactionLabSERequest seRequest, QisUserDetails authUser, String referenceLab1, String string) {

		String action = "ADDED";
		boolean isAdded = false;
		String covLogMsg = "";
		QisTransactionRTPCR rtpcr = qisTransactionLabRTPCRRepository.getTransactionLabRtAntigenByLabReqId(laboratoryId);

		QisTransactionLabSERTPCRRequest rtpcrReq = seRequest.getRtpcr();
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(rtpcrReq.getReferenceLabId());

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

		if (rtpcr == null) {
			rtpcr = new QisTransactionRTPCR();
			BeanUtils.copyProperties(seRequest.getRtpcr(), rtpcr);
			rtpcr.setId(laboratoryId);
			rtpcr.setCreatedBy(authUser.getId());
			rtpcr.setUpdatedBy(authUser.getId());

			if (referenceLab != null) {
				rtpcr.setReferenceLab(referenceLab);
				rtpcr.setReferenceLabId(referenceLab.getId());
			}
			covLogMsg = seRequest.getRtpcr().toString();
			isAdded = true;
		} else {
			action = "UPDATED";
			rtpcr.setUpdatedBy(authUser.getId());
			rtpcr.setUpdatedAt(Calendar.getInstance());

			if (rtpcrReq.getReferenceLabId() != null) {
				if (rtpcr.getReferenceLab() != null) {
					if (rtpcrReq.getReferenceLabId() != rtpcr.getReferenceLab().getReferenceid()) {
						covLogMsg = appUtility.formatUpdateData(covLogMsg, "Reference Laboratory",
								String.valueOf(rtpcr.getReferenceLab().getReferenceid()),
								String.valueOf(rtpcrReq.getReferenceLabId()));
						if (referenceLab != null) {
							rtpcr.setReferenceLab(referenceLab);
							rtpcr.setReferenceLabId(referenceLab.getId());
						} else {
							rtpcr.setReferenceLab(null);
							rtpcr.setReferenceLabId(null);
						}
					}
				} else {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(rtpcrReq.getReferenceLabId()));
					if (referenceLab != null) {
						rtpcr.setReferenceLab(referenceLab);
						rtpcr.setReferenceLabId(referenceLab.getId());
					} else {
						rtpcr.setReferenceLab(null);
						rtpcr.setReferenceLabId(null);
					}
				}
			} else {
				if (rtpcr.getReferenceLab() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Reference Laboratory",
							String.valueOf(rtpcr.getReferenceLab().getReferenceid()), null);
					rtpcr.setReferenceLab(null);
					rtpcr.setReferenceLabId(null);
				}
			}

			if (rtpcrReq.getRtpcrResult() != null) {
				if (rtpcrReq.getRtpcrResult() != rtpcr.getRtpcrResult()) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "rtpcr", String.valueOf(rtpcr.getRtpcrResult()),
							String.valueOf(rtpcrReq.getRtpcrResult()));
					rtpcr.setRtpcrResult(rtpcrReq.getRtpcrResult());
				}
			} else {
				if (rtpcrReq.getRtpcrResult() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "rtpcr", String.valueOf(rtpcr.getRtpcrResult()),
							null);
					rtpcr.setRtpcrResult(null);
				}
			}

			if (rtpcrReq.getCollectionDate() != null) {
				if (rtpcrReq.getCollectionDate() != rtpcr.getCollectionDate()) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Collection Date",
							String.valueOf(rtpcr.getCollectionDate()), String.valueOf(rtpcrReq.getCollectionDate()));
					rtpcr.setCollectionDate(rtpcrReq.getCollectionDate());
				}
			} else {
				if (rtpcrReq.getCollectionDate() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Collection Date",
							String.valueOf(rtpcr.getCollectionDate()), null);
					rtpcr.setCollectionDate(null);
				}
			}

			if (rtpcrReq.getPurpose() != null) {
				if (rtpcrReq.getPurpose() != rtpcr.getPurpose()) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "purpose", String.valueOf(rtpcr.getPurpose()),
							String.valueOf(rtpcrReq.getPurpose()));
					rtpcr.setPurpose(rtpcrReq.getPurpose());
				}
			} else {
				if (rtpcrReq.getPurpose() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "purpose", String.valueOf(rtpcr.getPurpose()),
							null);
					rtpcr.setPurpose(null);
				}
			}
			if (rtpcrReq.getRealeasingDate() != null) {
				if (rtpcrReq.getRealeasingDate() != rtpcr.getRealeasingDate()) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Realesing Date",
							String.valueOf(rtpcr.getPurpose()), String.valueOf(rtpcrReq.getRealeasingDate()));
					rtpcr.setRealeasingDate(rtpcrReq.getRealeasingDate());
				}
			} else {
				if (rtpcrReq.getRealeasingDate() != null) {
					covLogMsg = appUtility.formatUpdateData(covLogMsg, "Realesing Date",
							String.valueOf(rtpcr.getRealeasingDate()), null);
					rtpcr.setRealeasingDate(null);
				}
			}
		}

		if (!"".equals(covLogMsg)) {
			qisTransactionLabRTPCRRepository.save(rtpcr);
			qisLogService.info(authUser.getId(), QisTransactionLaboratorySerologyController.class.getSimpleName(),
					action, covLogMsg, laboratoryId, CATEGORY + "-antigen");
		}

		if (isAdded) {
			return rtpcr;
		}

		return null;
	}

}
