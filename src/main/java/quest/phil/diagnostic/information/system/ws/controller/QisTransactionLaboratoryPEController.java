package quest.phil.diagnostic.information.system.ws.controller;

import java.util.Calendar;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionPhysicalExamination;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabMedicalHistory;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabPhysicalExam;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabVitalSign;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabPERequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe.QisTransactionLabMedHisRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe.QisTransactionLabPhyExamRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe.QisTransactionLabVitalSignRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionPhysicalExaminationRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserPersonelRepository;
import quest.phil.diagnostic.information.system.ws.repository.pe.QisTransactionLabMedHisRepository;
import quest.phil.diagnostic.information.system.ws.repository.pe.QisTransactionLabPhyExamRepository;
import quest.phil.diagnostic.information.system.ws.repository.pe.QisTransactionLabVitSigRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/laboratory/{laboratoryId}")
public class QisTransactionLaboratoryPEController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratoryPEController.class);
	private final String CATEGORY = "PE";

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	private QisTransactionLabMedHisRepository qisTransactionLabMedHisRepository;

	@Autowired
	private QisTransactionLabVitSigRepository qisTransactionLabVitSigRepository;

	@Autowired
	private QisTransactionLabPhyExamRepository qisTransactionLabPhyExamRepository;

	@Autowired
	private QisTransactionPhysicalExaminationRepository qisTransactionPhysicalExaminationRepository;

	@Autowired
	private QisUserPersonelRepository qisUserPersonelRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	@PostMapping("/pe")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN') or hasRole('NURSE')")
	public QisTransactionPhysicalExamination saveTransactionLaboratoryPE(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @Valid @RequestBody QisTransactionLabPERequest peRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Save PE:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}
		QisTransactionPhysicalExamination qisTransactionPhysicalExamination = qisTransactionPhysicalExaminationRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionPhysicalExamination == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.");
		}

		if (!"PE".equals(qisTransactionPhysicalExamination.getItemDetails().getItemLaboratory())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Physical Examination.");
		}

		Float height = appUtility.parseFloatAmount(peRequest.getVitalSign().getHeight());
		if (height == null) {
			throw new RuntimeException("Invalid Height.", new Throwable("vitalSign.height: Invalid Height."));
		}

		Float weight = appUtility.parseFloatAmount(peRequest.getVitalSign().getWeight());
		if (weight == null) {
			throw new RuntimeException("Invalid Weight.", new Throwable("vitalSign.weight: Invalid Weight."));
		}

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(peRequest.getPhysicalExam().getDoctorId());
		if (qisDoctor == null) {
			throw new RuntimeException("Doctor not found.", new Throwable("physicalExam.doctorId: Doctor not found."));
		}

		String action = "ADDED";
		boolean isAddedMedHis = false;
		String medHisLogMsg = "";
		QisTransactionLabMedicalHistory medicalHistory = qisTransactionLabMedHisRepository
				.getTransactionLabMedicalHistoryByLabReqId(laboratoryId);
		if (medicalHistory == null) {
			medicalHistory = new QisTransactionLabMedicalHistory();
			BeanUtils.copyProperties(peRequest.getMedicalHistory(), medicalHistory);
			medicalHistory.setId(laboratoryId);
			medicalHistory.setCreatedBy(authUser.getId());
			medicalHistory.setUpdatedBy(authUser.getId());
			medHisLogMsg = peRequest.getMedicalHistory().toString();
			isAddedMedHis = true;
		} else {
			action = "UPDATED";
			medicalHistory.setUpdatedBy(authUser.getId());
			medicalHistory.setUpdatedAt(Calendar.getInstance());

			QisTransactionLabMedHisRequest medHisReq = peRequest.getMedicalHistory();

			if (medHisReq.getAsthma() != medicalHistory.getAsthma()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "asthma",
						String.valueOf(medicalHistory.getAsthma()), String.valueOf(medHisReq.getAsthma()));
				medicalHistory.setAsthma(medHisReq.getAsthma());
			}

			if (medHisReq.getAsthma() != medicalHistory.getAsthma()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "asthma",
						String.valueOf(medicalHistory.getAsthma()), String.valueOf(medHisReq.getAsthma()));
				medicalHistory.setAsthma(medHisReq.getAsthma());
			}

			if (medHisReq.getTuberculosis() != medicalHistory.getTuberculosis()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "tuberculosis",
						String.valueOf(medicalHistory.getTuberculosis()), String.valueOf(medHisReq.getTuberculosis()));
				medicalHistory.setTuberculosis(medHisReq.getTuberculosis());
			}

			if (medHisReq.getDiabetesMellitus() != medicalHistory.getDiabetesMellitus()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "diabetesMellitus",
						String.valueOf(medicalHistory.getDiabetesMellitus()),
						String.valueOf(medHisReq.getDiabetesMellitus()));
				medicalHistory.setDiabetesMellitus(medHisReq.getDiabetesMellitus());
			}

			if (medHisReq.getHighBloodPressure() != medicalHistory.getHighBloodPressure()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "highBloodPressure",
						String.valueOf(medicalHistory.getHighBloodPressure()),
						String.valueOf(medHisReq.getHighBloodPressure()));
				medicalHistory.setHighBloodPressure(medHisReq.getHighBloodPressure());
			}

			if (medHisReq.getHeartProblem() != medicalHistory.getHeartProblem()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "heartProblem",
						String.valueOf(medicalHistory.getHeartProblem()), String.valueOf(medHisReq.getHeartProblem()));
				medicalHistory.setHeartProblem(medHisReq.getHeartProblem());
			}

			if (medHisReq.getKidneyProblem() != medicalHistory.getKidneyProblem()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "kidneyProblem",
						String.valueOf(medicalHistory.getKidneyProblem()),
						String.valueOf(medHisReq.getKidneyProblem()));
				medicalHistory.setKidneyProblem(medHisReq.getKidneyProblem());
			}

			if (medHisReq.getAbdominalHernia() != medicalHistory.getAbdominalHernia()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "abdominalHernia",
						String.valueOf(medicalHistory.getAbdominalHernia()),
						String.valueOf(medHisReq.getAbdominalHernia()));
				medicalHistory.setAbdominalHernia(medHisReq.getAbdominalHernia());
			}

			if (medHisReq.getJointBackScoliosis() != medicalHistory.getJointBackScoliosis()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "jointBackScoliosis",
						String.valueOf(medicalHistory.getJointBackScoliosis()),
						String.valueOf(medHisReq.getJointBackScoliosis()));
				medicalHistory.setJointBackScoliosis(medHisReq.getJointBackScoliosis());
			}

			if (medHisReq.getJointBackScoliosis() != medicalHistory.getJointBackScoliosis()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "jointBackScoliosis",
						String.valueOf(medicalHistory.getJointBackScoliosis()),
						String.valueOf(medHisReq.getJointBackScoliosis()));
				medicalHistory.setJointBackScoliosis(medHisReq.getJointBackScoliosis());
			}

			if (medHisReq.getPsychiatricProblem() != medicalHistory.getPsychiatricProblem()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "psychiatricProblem",
						String.valueOf(medicalHistory.getPsychiatricProblem()),
						String.valueOf(medHisReq.getPsychiatricProblem()));
				medicalHistory.setPsychiatricProblem(medHisReq.getPsychiatricProblem());
			}

			if (medHisReq.getMigraineHeadache() != medicalHistory.getMigraineHeadache()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "migraineHeadache",
						String.valueOf(medicalHistory.getMigraineHeadache()),
						String.valueOf(medHisReq.getMigraineHeadache()));
				medicalHistory.setMigraineHeadache(medHisReq.getMigraineHeadache());
			}

			if (medHisReq.getFaintingSeizures() != medicalHistory.getFaintingSeizures()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "faintingSeizures",
						String.valueOf(medicalHistory.getFaintingSeizures()),
						String.valueOf(medHisReq.getFaintingSeizures()));
				medicalHistory.setFaintingSeizures(medHisReq.getFaintingSeizures());
			}

			if (medHisReq.getAllergies() != medicalHistory.getAllergies()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "allergies",
						String.valueOf(medicalHistory.getAllergies()), String.valueOf(medHisReq.getAllergies()));
				medicalHistory.setAllergies(medHisReq.getAllergies());
			}

			if (medHisReq.getCancerTumor() != medicalHistory.getCancerTumor()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "cancerTumor",
						String.valueOf(medicalHistory.getCancerTumor()), String.valueOf(medHisReq.getCancerTumor()));
				medicalHistory.setCancerTumor(medHisReq.getCancerTumor());
			}

			if (medHisReq.getHepatitis() != medicalHistory.getHepatitis()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "hepatitis",
						String.valueOf(medicalHistory.getHepatitis()), String.valueOf(medHisReq.getHepatitis()));
				medicalHistory.setHepatitis(medHisReq.getHepatitis());
			}

			if (medHisReq.getStdplhiv() != medicalHistory.getStdplhiv()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "stdplhiv",
						String.valueOf(medicalHistory.getStdplhiv()), String.valueOf(medHisReq.getStdplhiv()));
				medicalHistory.setStdplhiv(medHisReq.getStdplhiv());
			}
			
			if (medHisReq.getTravelhistory() != medicalHistory.getTravelhistory()) {
				medHisLogMsg = appUtility.formatUpdateData(medHisLogMsg, "travelHistory",
						String.valueOf(medicalHistory.getTravelhistory()), String.valueOf(medHisReq.getTravelhistory()));
				medicalHistory.setTravelhistory(medHisReq.getTravelhistory());
			}
		}

		if (!"".equals(medHisLogMsg)) {
			qisTransactionLabMedHisRepository.save(medicalHistory);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryPEController.class.getSimpleName(), action,
					medHisLogMsg, laboratoryId, CATEGORY + "-MEDICAL HISORY");
		}

		action = "ADDED";
		boolean isAddedVitSig = false;
		String vitSigLogMsg = "";
		QisTransactionLabVitalSign vitalSign = qisTransactionLabVitSigRepository
				.getTransactionLabVitalSignByLabReqId(laboratoryId);
		if (vitalSign == null) {
			vitalSign = new QisTransactionLabVitalSign();
			BeanUtils.copyProperties(peRequest.getVitalSign(), vitalSign);
			vitalSign.setId(laboratoryId);
			vitalSign.setCreatedBy(authUser.getId());
			vitalSign.setUpdatedBy(authUser.getId());
			vitalSign.setHeight(height);
			vitalSign.setWeight(weight);
			vitalSign.computeBMI();
			peRequest.getVitalSign().setBmi(vitalSign.getBmi());
			peRequest.getVitalSign().setBmiCategory(vitalSign.getBmiCategory());
			vitSigLogMsg = peRequest.getVitalSign().toString();
			isAddedVitSig = true;
		} else {
			action = "UPDATED";
			vitalSign.setUpdatedBy(authUser.getId());
			vitalSign.setUpdatedAt(Calendar.getInstance());

			QisTransactionLabVitalSignRequest vitSigReq = peRequest.getVitalSign();

			if (!height.equals(vitalSign.getHeight())) {
				vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "height",
						String.valueOf(vitalSign.getHeight()), String.valueOf(vitSigReq.getHeight()));
				vitalSign.setHeight(height);
			}

			if (!weight.equals(vitalSign.getWeight())) {
				vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "weight",
						String.valueOf(vitalSign.getWeight()), String.valueOf(weight));
				vitalSign.setWeight(weight);
			}
			vitalSign.computeBMI();

			if (!vitSigReq.getBloodPressure().equals(vitalSign.getBloodPressure())) {
				vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "bloodPressure", vitalSign.getBloodPressure(),
						vitSigReq.getBloodPressure());
				vitalSign.setBloodPressure(vitSigReq.getBloodPressure());
			}

			if (vitSigReq.getPulseRate() != vitalSign.getPulseRate()) {
				vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "pulseRate",
						String.valueOf(vitalSign.getPulseRate()), String.valueOf(vitSigReq.getPulseRate()));
				vitalSign.setPulseRate(vitSigReq.getPulseRate());
			}

			if (vitSigReq.getRespiratoryRate() != vitalSign.getRespiratoryRate()) {
				vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "respiratoryRate",
						String.valueOf(vitalSign.getRespiratoryRate()), String.valueOf(vitSigReq.getRespiratoryRate()));
				vitalSign.setRespiratoryRate(vitSigReq.getRespiratoryRate());
			}
			
			
				if (!vitSigReq.getUncorrectedOS().equals(vitalSign.getUncorrectedOS())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "uncorrectedOS",
							vitalSign.getUncorrectedOS(), vitSigReq.getUncorrectedOS());
					
						vitalSign.setUncorrectedOS(vitSigReq.getUncorrectedOS());
					
				}
				if (!vitSigReq.getUncorrectedOD().equals(vitalSign.getUncorrectedOD())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "uncorrectedOD",
							vitalSign.getUncorrectedOD(), vitSigReq.getUncorrectedOD());					
						vitalSign.setUncorrectedOD(vitSigReq.getUncorrectedOD());			
				}

				if (!vitSigReq.getCorrectedOD().equals(vitalSign.getCorrectedOD())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "correctedOD", vitalSign.getCorrectedOD(),
							vitSigReq.getCorrectedOD());
					vitalSign.setCorrectedOD(vitSigReq.getCorrectedOD());
				}

				if (!vitSigReq.getCorrectedOS().equals(vitalSign.getCorrectedOS())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "correctedOS", vitalSign.getCorrectedOS(),
							vitSigReq.getCorrectedOS());
					vitalSign.setCorrectedOS(vitSigReq.getCorrectedOS());
				}

			if (vitSigReq.getIshihara() == null) {
				if (vitalSign.getIshihara() != null) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "ishihara", vitalSign.getIshihara(),
							vitSigReq.getIshihara());
					vitalSign.setIshihara(vitSigReq.getIshihara());
				}
			} else {
				if (!vitSigReq.getIshihara().equals(vitalSign.getIshihara())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "ishihara", vitalSign.getIshihara(),
							vitSigReq.getIshihara());
					vitalSign.setIshihara(vitSigReq.getIshihara());
				}
			}

			if (vitSigReq.getHearing() == null) {
				if (vitalSign.getHearing() != null) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "hearing", vitalSign.getHearing(),
							vitSigReq.getHearing());
					vitalSign.setHearing(vitSigReq.getHearing());
				}
			} else {
				if (!vitSigReq.getHearing().equals(vitalSign.getHearing())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "hearing", vitalSign.getHearing(),
							vitSigReq.getHearing());
					vitalSign.setHearing(vitSigReq.getHearing());
				}
			}

			if (vitSigReq.getHospitalization() == null) {
				if (vitalSign.getHospitalization() != null) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "hospitalization",
							vitalSign.getHospitalization(), vitSigReq.getHospitalization());
					vitalSign.setHospitalization(vitSigReq.getHospitalization());
				}
			} else {
				if (!vitSigReq.getHospitalization().equals(vitalSign.getHospitalization())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "hospitalization",
							vitalSign.getHospitalization(), vitSigReq.getHospitalization());
					vitalSign.setHospitalization(vitSigReq.getHospitalization());
				}
			}

			if (vitSigReq.getOpearations() == null) {
				if (vitalSign.getOpearations() != null) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "opearations", vitalSign.getOpearations(),
							vitSigReq.getOpearations());
					vitalSign.setOpearations(vitSigReq.getOpearations());
				}
			} else {
				if (!vitSigReq.getOpearations().equals(vitalSign.getOpearations())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "opearations", vitalSign.getOpearations(),
							vitSigReq.getOpearations());
					vitalSign.setOpearations(vitSigReq.getOpearations());
				}
			}

			if (vitSigReq.getMedications() == null) {
				if (vitalSign.getMedications() != null) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "medications", vitalSign.getMedications(),
							vitSigReq.getMedications());
					vitalSign.setMedications(vitSigReq.getMedications());
				}
			} else {
				if (!vitSigReq.getMedications().equals(vitalSign.getMedications())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "medications", vitalSign.getMedications(),
							vitSigReq.getMedications());
					vitalSign.setMedications(vitSigReq.getMedications());
				}
			}

			if (vitSigReq.getSmoker() == null) {
				if (vitalSign.getSmoker() != null) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "smoker", vitalSign.getSmoker(),
							vitSigReq.getSmoker());
					vitalSign.setSmoker(vitSigReq.getSmoker());
				}
			} else {
				if (!vitSigReq.getSmoker().equals(vitalSign.getSmoker())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "smoker", vitalSign.getSmoker(),
							vitSigReq.getSmoker());
					vitalSign.setSmoker(vitSigReq.getSmoker());
				}
			}

			if (vitSigReq.getAlcoholic() == null) {
				if (vitalSign.getAlcoholic() != null) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "alcoholic", vitalSign.getAlcoholic(),
							vitSigReq.getAlcoholic());
					vitalSign.setAlcoholic(vitSigReq.getAlcoholic());
				}
			} else {
				if (!vitSigReq.getAlcoholic().equals(vitalSign.getAlcoholic())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "alcoholic", vitalSign.getAlcoholic(),
							vitSigReq.getAlcoholic());
					vitalSign.setAlcoholic(vitSigReq.getAlcoholic());
				}
			}

			if (vitSigReq.getLastMenstrual() == null) {
				if (vitalSign.getLastMenstrual() != null) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "lastMenstrual",
							vitalSign.getLastMenstrual(), vitSigReq.getLastMenstrual());
					vitalSign.setLastMenstrual(vitSigReq.getLastMenstrual());
				}
			} else {
				if (!vitSigReq.getLastMenstrual().equals(vitalSign.getLastMenstrual())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "lastMenstrual",
							vitalSign.getLastMenstrual(), vitSigReq.getLastMenstrual());
					vitalSign.setLastMenstrual(vitSigReq.getLastMenstrual());
				}
			}

			if (vitSigReq.getPregnant() == null) {
				if (vitalSign.getPregnant() != null) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "pregnant",
							String.valueOf(vitalSign.getPregnant()), String.valueOf(vitSigReq.getPregnant()));
					vitalSign.setPregnant(vitSigReq.getPregnant());
				}
			} else {
				if (!vitSigReq.getPregnant().equals(vitalSign.getPregnant())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "pregnant",
							String.valueOf(vitalSign.getPregnant()), String.valueOf(vitSigReq.getPregnant()));
					vitalSign.setPregnant(vitSigReq.getPregnant());
				}
			}

			if (vitSigReq.getNotes() == null) {
				if (vitalSign.getNotes() != null) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "notes", vitalSign.getNotes(),
							vitSigReq.getNotes());
					vitalSign.setNotes(vitSigReq.getNotes());
				}
			} else {
				if (!vitSigReq.getNotes().equals(vitalSign.getNotes())) {
					vitSigLogMsg = appUtility.formatUpdateData(vitSigLogMsg, "notes", vitalSign.getNotes(),
							vitSigReq.getNotes());
					vitalSign.setNotes(vitSigReq.getNotes());
				}
			}
		}

		if (!"".equals(vitSigLogMsg)) {
			qisTransactionLabVitSigRepository.save(vitalSign);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryPEController.class.getSimpleName(), action,
					vitSigLogMsg, laboratoryId, CATEGORY + "-VISTAL SIGN");
		}

		action = "ADDED";
		boolean isAddedPhyExm = false;
		String phyExaLogMsg = "";
		QisTransactionLabPhysicalExam physicalExam = qisTransactionLabPhyExamRepository
				.getTransactionLabPhysicalExamByLabReqId(laboratoryId);
		if (physicalExam == null) {
			physicalExam = new QisTransactionLabPhysicalExam();
			BeanUtils.copyProperties(peRequest.getPhysicalExam(), physicalExam);
			physicalExam.setId(laboratoryId);
			physicalExam.setCreatedBy(authUser.getId());
			physicalExam.setUpdatedBy(authUser.getId());
			physicalExam.setDoctorId(qisDoctor.getId());
			physicalExam.setDoctor(qisDoctor);
			phyExaLogMsg = peRequest.getPhysicalExam().toString();
			isAddedPhyExm = true;
		} else {
			action = "UPDATED";
			physicalExam.setUpdatedBy(authUser.getId());
			physicalExam.setUpdatedAt(Calendar.getInstance());

			QisTransactionLabPhyExamRequest phyExamReq = peRequest.getPhysicalExam();

			if (phyExamReq.getSkin() != physicalExam.getSkin()) {
				phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "skin", String.valueOf(physicalExam.getSkin()),
						String.valueOf(phyExamReq.getSkin()));
				physicalExam.setSkin(phyExamReq.getSkin());
			}

			if (phyExamReq.getHeadNeck() != physicalExam.getHeadNeck()) {
				phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "headNeck",
						String.valueOf(physicalExam.getHeadNeck()), String.valueOf(phyExamReq.getHeadNeck()));
				physicalExam.setHeadNeck(phyExamReq.getHeadNeck());
			}

			if (phyExamReq.getChestBreastLungs() != physicalExam.getChestBreastLungs()) {
				phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "chestBreastLungs",
						String.valueOf(physicalExam.getChestBreastLungs()),
						String.valueOf(phyExamReq.getChestBreastLungs()));
				physicalExam.setChestBreastLungs(phyExamReq.getChestBreastLungs());
			}

			if (phyExamReq.getCardiacHeart() != physicalExam.getCardiacHeart()) {
				phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "cardiacHear",
						String.valueOf(physicalExam.getCardiacHeart()), String.valueOf(phyExamReq.getCardiacHeart()));
				physicalExam.setCardiacHeart(phyExamReq.getCardiacHeart());
			}

			if (phyExamReq.getAbdomen() != physicalExam.getAbdomen()) {
				phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "abdomen",
						String.valueOf(physicalExam.getAbdomen()), String.valueOf(phyExamReq.getAbdomen()));
				physicalExam.setAbdomen(phyExamReq.getAbdomen());
			}

			if (phyExamReq.getExtremities() != physicalExam.getExtremities()) {
				phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "extremities",
						String.valueOf(physicalExam.getExtremities()), String.valueOf(phyExamReq.getExtremities()));
				physicalExam.setExtremities(phyExamReq.getExtremities());
			}
			
			if (phyExamReq.getFatigueachespains() != physicalExam.getFatigueachespains()) {
				phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "fatigue",
						String.valueOf(physicalExam.getFatigueachespains()), String.valueOf(phyExamReq.getFatigueachespains()));
				physicalExam.setFatigueachespains(phyExamReq.getFatigueachespains());
			}

			if (phyExamReq.getNotes() == null) {
				if (physicalExam.getNotes() != null) {
					phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "notes", physicalExam.getNotes(),
							phyExamReq.getNotes());
					physicalExam.setNotes(phyExamReq.getNotes());
				}
			} else {
				if (!phyExamReq.getNotes().equals(physicalExam.getNotes())) {
					phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "notes", physicalExam.getNotes(),
							phyExamReq.getNotes());
					physicalExam.setNotes(phyExamReq.getNotes());
				}
			}

			if (phyExamReq.getFindings() == null) {
				if (physicalExam.getFindings() != null) {
					phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "findings", physicalExam.getFindings(),
							phyExamReq.getFindings());
					physicalExam.setFindings(phyExamReq.getFindings());
				}
			} else {
				if (!phyExamReq.getFindings().equals(physicalExam.getFindings())) {
					phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "findings", physicalExam.getFindings(),
							phyExamReq.getFindings());
					physicalExam.setFindings(phyExamReq.getFindings());
				}
			}

			if (qisDoctor.getId() != physicalExam.getDoctorId()) {
				phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "doctor",
						physicalExam.getDoctor().getDoctorid(), qisDoctor.getDoctorid());
				physicalExam.setDoctor(qisDoctor);
				physicalExam.setDoctorId(qisDoctor.getId());
			}

			if (!phyExamReq.getLicense().equals(physicalExam.getLicense())) {
				phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "license", physicalExam.getLicense(),
						phyExamReq.getLicense());
				physicalExam.setLicense(phyExamReq.getLicense());
			}

			if (phyExamReq.getRemarks() != physicalExam.getRemarks()) {
				phyExaLogMsg = appUtility.formatUpdateData(phyExaLogMsg, "result",
						String.valueOf(physicalExam.getRemarks()), String.valueOf(phyExamReq.getRemarks()));
				physicalExam.setRemarks(phyExamReq.getRemarks());
			}
		}

		if (!"".equals(phyExaLogMsg)) {
			qisTransactionLabPhyExamRepository.save(physicalExam);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryPEController.class.getSimpleName(), action,
					phyExaLogMsg, laboratoryId, CATEGORY + "-PHYSICAL EXAM");
		}

		int isUpdate = 0;
		if (!qisTransactionPhysicalExamination.isSubmitted()) {
			qisTransactionPhysicalExamination.setSubmitted(true);
			qisTransactionPhysicalExamination.setVerifiedBy(authUser.getId());
			qisTransactionPhysicalExamination.setVerifiedDate(Calendar.getInstance());
			isUpdate += 1;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryPEController.class.getSimpleName(),
					"SUBMITTED", qisTransactionPhysicalExamination.getItemDetails().getItemName(), laboratoryId,
					CATEGORY);
		}

		if (qisTransactionPhysicalExamination.getLabPersonelId() == null) {
			qisTransactionPhysicalExamination.setLabPersonelId(authUser.getId());
			qisTransactionPhysicalExamination.setLabPersonelDate(Calendar.getInstance());
			qisTransactionPhysicalExamination.setStatus(2);
			isUpdate += 2;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryPEController.class.getSimpleName(),
					"LAB PERSONEL", authUser.getUsername(), laboratoryId, CATEGORY);
		}

		if (isUpdate > 0) {
			qisTransactionPhysicalExaminationRepository.save(qisTransactionPhysicalExamination);

			QisUserPersonel labPersonel = qisUserPersonelRepository.findByUserid(authUser.getUserid());
			if ((isUpdate & 1) > 0) {
				qisTransactionPhysicalExamination.setVerified(labPersonel);

			}
			if ((isUpdate & 2) > 0) {
				qisTransactionPhysicalExamination.setLabPersonel(labPersonel);
			}
		}

		if (isAddedMedHis) {
			qisTransactionPhysicalExamination.setMedicalHistory(medicalHistory);
		}
		if (isAddedVitSig) {
			qisTransactionPhysicalExamination.setVitalSign(vitalSign);
		}
		if (isAddedPhyExm) {
			qisTransactionPhysicalExamination.setPhysicalExam(physicalExam);
		}

		return qisTransactionPhysicalExamination;
	}

	@GetMapping("/pe")
	public QisTransactionPhysicalExamination getTransactionLaboratoryPE(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-View PE:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionPhysicalExamination qisTransactionPhysicalExamination = qisTransactionPhysicalExaminationRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		if (qisTransactionPhysicalExamination == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.");
		}

		if (!"PE".equals(qisTransactionPhysicalExamination.getItemDetails().getItemLaboratory())) {
			throw new RuntimeException("Transaction Laboratory Request is not for Physical Examination.");
		}

		return qisTransactionPhysicalExamination;
	}

}
