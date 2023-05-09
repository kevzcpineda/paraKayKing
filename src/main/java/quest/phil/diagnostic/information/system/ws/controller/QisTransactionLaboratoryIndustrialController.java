package quest.phil.diagnostic.information.system.ws.controller;

import java.util.Calendar;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.common.AppLaboratoryCHUtility;
import quest.phil.diagnostic.information.system.ws.common.AppLaboratoryCMUtility;
import quest.phil.diagnostic.information.system.ws.common.AppLaboratoryHEUtility;
import quest.phil.diagnostic.information.system.ws.common.AppLaboratorySEUtility;
import quest.phil.diagnostic.information.system.ws.common.AppLaboratoryTOUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItemLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryChemistry;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryClinicalMicroscopy;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratorySerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionChemistry;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionClinicalMicroscopy;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionToxicology;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisIndustrialRequirementRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabINDRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionChemistryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionClinicalMicroscopyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionHematologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemLaboratoriesRepositories;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionSerologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionToxicologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserPersonelRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/item/{transactionItemId}")
public class QisTransactionLaboratoryIndustrialController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratoryIndustrialController.class);
	private final String CATEGORY = "IND";

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	private QisTransactionItemLaboratoriesRepositories qisTransactionItemLaboratoriesRepositories;

	@Autowired
	private AppLaboratoryCMUtility appLaboratoryCMUtility;

	@Autowired
	private AppLaboratoryHEUtility appLaboratoryHEUtility;

	@Autowired
	private AppLaboratoryCHUtility appLaboratoryCHUtility;

	@Autowired
	private AppLaboratorySEUtility appLaboratorySEUtility;

	@Autowired
	private AppLaboratoryTOUtility appLaboratoryTOUtility;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	private QisTransactionClinicalMicroscopyRepository qisTransactionClinicalMicroscopyRepository;

	@Autowired
	private QisTransactionHematologyRepository qisTransactionHematologyRepository;

	@Autowired
	private QisTransactionChemistryRepository qisTransactionChemistryRepository;

	@Autowired
	private QisTransactionSerologyRepository qisTransactionSerologyRepository;

	@Autowired
	private QisTransactionToxicologyRepository qisTransactionToxicologyRepository;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisUserPersonelRepository qisUserPersonelRepository;

	@PostMapping("/industrial")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionItemLaboratories saveTransactionLaboratoryIndustrial(@PathVariable String transactionId,
			@PathVariable Long transactionItemId, @Valid @RequestBody QisTransactionLabINDRequest indRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Save IND:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionItemLaboratories qisTxnItemLab = qisTransactionItemLaboratoriesRepositories
				.getTransactionItemLaboratoriesById(qisTransaction.getId(), transactionItemId);
		if (qisTxnItemLab == null) {
			throw new RuntimeException("Transaction Item not found.",
					new Throwable("transactionItemId: Transaction Item not found."));
		}

		if (!"PCK".equals(qisTxnItemLab.getItemType())) {
			throw new RuntimeException("Transaction Item not for industrial.",
					new Throwable("transactionItemId: Transaction Item not for industrial."));
		}

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(indRequest.getPathologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Pathologist not found.",
					new Throwable("pathologistId: Pathologist not found."));
		}

		Set<QisIndustrialRequirementRequest> submittedResults = indRequest.getTransactionItemLaboratories();
		Set<QisTransactionLaboratoryInfo> laboratoryRequests = qisTxnItemLab.getTransactionLabRequests();
		for (QisTransactionLaboratoryInfo laboratory : laboratoryRequests) {
			if ("LAB".equals(laboratory.getItemDetails().getItemCategory())) {
				QisIndustrialRequirementRequest submitted = submittedResults.stream()
						.filter(sb -> sb.getId().equals(laboratory.getId())).findAny().orElse(null);

				if (submitted != null) {
					Set<QisLaboratoryProcedureService> serviceRequest = laboratory.getItemDetails().getServiceRequest();

					switch (laboratory.getItemDetails().getItemLaboratoryProcedure()) {
					case "CM": // clinical microscopy
						appLaboratoryCMUtility.validateClinicalMicroscopyRequest(laboratory.getId(), serviceRequest,
								submitted.getClinicalMicroscopy());
						break;

					case "HE": // hematology
						appLaboratoryHEUtility.validateHematologyRequest(laboratory.getId(), serviceRequest,
								submitted.getHematology());
						break;

					case "CH": // chemistry
						appLaboratoryCHUtility.validateChemistry(laboratory.getId(), serviceRequest,
								submitted.getChemistry());
						break;

					case "SE": // serology
						appLaboratorySEUtility.validateSerology(laboratory.getId(), serviceRequest,
								submitted.getSerology());
						break;

					case "TO": // toxicology
						appLaboratoryTOUtility.validateToxicology(laboratory.getId(), serviceRequest,
								submitted.getToxicology());
						break;

					default:
						break;
					}
				}
			}
		}

		for (QisTransactionLaboratoryInfo laboratory : laboratoryRequests) {
			if ("LAB".equals(laboratory.getItemDetails().getItemCategory())) {
				QisIndustrialRequirementRequest submitted = submittedResults.stream()
						.filter(sb -> sb.getId().equals(laboratory.getId())).findAny().orElse(null);

				if (submitted != null) {
					Set<QisLaboratoryProcedureService> serviceRequest = laboratory.getItemDetails().getServiceRequest();

					switch (laboratory.getItemDetails().getItemLaboratoryProcedure()) {
					case "CM": // clinical microscopy
						QisTransactionClinicalMicroscopy qisTransactionClinicalMicroscopy = qisTransactionClinicalMicroscopyRepository
								.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratory.getId());
						if (qisTransactionClinicalMicroscopy == null) {
							throw new RuntimeException("Transaction Laboratory Request not found.",
									new Throwable("laboratoryId. Transaction Laboratory Request not found."));
						}

						appLaboratoryCMUtility.saveClinicalMicroscopy(qisTransaction, laboratory.getId(),
								qisTransactionClinicalMicroscopy, serviceRequest, submitted.getClinicalMicroscopy(),
								authUser, qisDoctor);

						QisLaboratoryClinicalMicroscopy clinicalMicroscopy = new QisLaboratoryClinicalMicroscopy();
						clinicalMicroscopy.setId(qisTransactionClinicalMicroscopy.getId());
						clinicalMicroscopy.setUrineChemical(qisTransactionClinicalMicroscopy.getUrineChemical());
						clinicalMicroscopy.setFecalysis(qisTransactionClinicalMicroscopy.getFecalysis());
						clinicalMicroscopy.setAfb(qisTransactionClinicalMicroscopy.getAfb());
						clinicalMicroscopy.setUrineChemical(qisTransactionClinicalMicroscopy.getUrineChemical());

						laboratory.setClinicalMicroscopy(clinicalMicroscopy);
						break;

					case "HE": // hematology
						QisTransactionHematology qisTransactionHematology = qisTransactionHematologyRepository
								.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratory.getId());
						if (qisTransactionHematology == null) {
							throw new RuntimeException("Transaction Laboratory Request not found.");
						}

						appLaboratoryHEUtility.saveHematology(qisTransaction, laboratory.getId(),
								qisTransactionHematology, serviceRequest, submitted.getHematology(), authUser,
								qisDoctor);

						QisLaboratoryHematology hematology = new QisLaboratoryHematology();
						hematology.setId(qisTransactionHematology.getId());
						hematology.setAptt(qisTransactionHematology.getAptt());
						hematology.setBloodTyping(qisTransactionHematology.getBloodTyping());
						hematology.setCbc(qisTransactionHematology.getCbc());
						hematology.setCtbt(qisTransactionHematology.getCtbt());
						hematology.setEsr(qisTransactionHematology.getEsr());
						hematology.setPrms(qisTransactionHematology.getPrms());
						hematology.setProthombinTime(qisTransactionHematology.getProthombinTime());

						laboratory.setHematology(hematology);
						break;

					case "CH": // chemistry
						QisTransactionChemistry qisTransactionChemistry = qisTransactionChemistryRepository
								.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratory.getId());
						if (qisTransactionChemistry == null) {
							throw new RuntimeException("Transaction Laboratory Request not found.");
						}

						appLaboratoryCHUtility.saveChemistry(qisTransaction, laboratory.getId(),
								qisTransactionChemistry, serviceRequest, submitted.getChemistry(), authUser, qisDoctor);

						QisLaboratoryChemistry chemistry = new QisLaboratoryChemistry();
						chemistry.setId(qisTransactionChemistry.getId());
						chemistry.setBilirubin(qisTransactionChemistry.getBilirubin());
						chemistry.setBun(qisTransactionChemistry.getBun());
						chemistry.setCpk(qisTransactionChemistry.getCpk());
						chemistry.setCreatinine(qisTransactionChemistry.getCreatinine());
						chemistry.setElectrolytes(qisTransactionChemistry.getElectrolytes());
						chemistry.setEnzymes(qisTransactionChemistry.getEnzymes());
						chemistry.setFbs(qisTransactionChemistry.getFbs());
						chemistry.setHemoglobin(qisTransactionChemistry.getHemoglobin());
						chemistry.setLipidProfile(qisTransactionChemistry.getLipidProfile());
						chemistry.setOgct(qisTransactionChemistry.getOgct());
						chemistry.setOgtt(qisTransactionChemistry.getOgtt());
						chemistry.setPprbs(qisTransactionChemistry.getPprbs());
						chemistry.setProtein(qisTransactionChemistry.getProtein());
						chemistry.setUricAcid(qisTransactionChemistry.getUricAcid());

						laboratory.setChemistry(chemistry);
						break;

					case "SE": // serology
						QisTransactionSerology qisTransactionSerology = qisTransactionSerologyRepository
								.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratory.getId());
						if (qisTransactionSerology == null) {
							throw new RuntimeException("Transaction Laboratory Request not found.");
						}

						appLaboratorySEUtility.saveSerology(qisTransaction, laboratory.getId(), qisTransactionSerology,
								serviceRequest, submitted.getSerology(), authUser, qisDoctor);

						QisLaboratorySerology serology = new QisLaboratorySerology();
						serology.setId(qisTransactionSerology.getId());
						serology.setAntigen(qisTransactionSerology.getAntigen());
						serology.setCovid(qisTransactionSerology.getCovid());
						serology.setCrp(qisTransactionSerology.getCrp());
						serology.setHiv(qisTransactionSerology.getHiv());
						serology.setSerology(qisTransactionSerology.getSerology());
						serology.setThyroid(qisTransactionSerology.getThyroid());
						serology.setTyphidot(qisTransactionSerology.getTyphidot());
						serology.setRtantigen(qisTransactionSerology.getRtantigen());

						laboratory.setSerology(serology);
						break;

					case "TO": // toxicology
						QisTransactionToxicology qisTransactionToxicology = qisTransactionToxicologyRepository
								.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratory.getId());
						if (qisTransactionToxicology == null) {
							throw new RuntimeException("Transaction Laboratory Request not found.");
						}

						appLaboratoryTOUtility.saveToxicology(qisTransaction, laboratory.getId(),
								qisTransactionToxicology, submitted.getToxicology(), authUser, qisDoctor);

						laboratory.setToxicology(qisTransactionToxicology.getToxicology());
						break;

					default:
						break;
					}
				}
			}
		}

		int isUpdate = 0;
		if (qisTxnItemLab.getLabPersonelId() == null) {
			qisTxnItemLab.setLabPersonelId(authUser.getId());
			qisTxnItemLab.setLabPersonel(qisUserPersonelRepository.getUserPersonelByUserId(authUser.getId()));
			qisTxnItemLab.setLabPersonelDate(Calendar.getInstance());
			isUpdate += 1;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryIndustrialController.class.getSimpleName(),
					"LAB PERSONEL", authUser.getUsername(), qisTxnItemLab.getId(), CATEGORY);
		}

		String onLogMsg = "";
		String action = "ADDED";
		if (qisTxnItemLab.getMedicalDoctorId() == null) {
			qisTxnItemLab.setMedicalDoctor(qisDoctor);
			qisTxnItemLab.setMedicalDoctorId(qisDoctor.getId());
			onLogMsg = qisTxnItemLab.getMedicalDoctor().getDoctorid();
		} else if (qisDoctor.getId() != qisTxnItemLab.getMedicalDoctorId()) {
			qisTxnItemLab.setMedicalDoctor(qisDoctor);
			qisTxnItemLab.setMedicalDoctorId(qisDoctor.getId());

			action = "UPDATED";
			onLogMsg = appUtility.formatUpdateData(onLogMsg, "pathologist",
					qisTxnItemLab.getMedicalDoctor().getDoctorid(), qisDoctor.getDoctorid());
		}

		if (!"".equals(onLogMsg)) {
			isUpdate += 2;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryChemistryController.class.getSimpleName(),
					action, onLogMsg, qisTxnItemLab.getId(), CATEGORY + "-PATHOLOGIST");
		}

		if (isUpdate > 0) {
			qisTransactionItemLaboratoriesRepositories.save(qisTxnItemLab);
		}

		qisLogService.info(authUser.getId(), QisTransactionLaboratoryIndustrialController.class.getSimpleName(), "SAVE",
				"INDUSTRIAL TRANSACTION", qisTransaction.getId(), CATEGORY);

		return qisTransactionItemLaboratoriesRepositories.getTransactionItemLaboratoriesById(qisTransaction.getId(),
				transactionItemId);
	}

	@PutMapping("/industrial/qc")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionItemLaboratories saveTransactionLaboratoryIndustrialQC(@PathVariable String transactionId,
			@PathVariable Long transactionItemId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Save IND QC:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.",
					new Throwable("transactionId. Transaction not found."));
		}

		QisTransactionItemLaboratories qisTxnItemLab = qisTransactionItemLaboratoriesRepositories
				.getTransactionItemLaboratoriesById(qisTransaction.getId(), transactionItemId);
		if (qisTxnItemLab == null) {
			throw new RuntimeException("Transaction Item not found.",
					new Throwable("transactionItemId: Transaction Item not found."));
		}

		if (!"PCK".equals(qisTxnItemLab.getItemType())) {
			throw new RuntimeException("Transaction Item not for industrial.",
					new Throwable("transactionItemId: Transaction Item not for industrial."));
		}

		if (qisTxnItemLab.getLabPersonelId().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to check the same transaction.",
					new Throwable("You are not authorize to check the same transaction."));
		}

		if (qisTxnItemLab.getQcId() == null) {
			saveIndustrialQC(qisTxnItemLab, authUser);
		} else {
			throw new RuntimeException("Transaction Laboratory Item was already Checked.",
					new Throwable("transactionItemId: Transaction Laboratory Item was already Checked."));
		}

		return qisTransactionItemLaboratoriesRepositories.getTransactionItemLaboratoriesById(qisTransaction.getId(),
				transactionItemId);
	}

	private void saveIndustrialQC(QisTransactionItemLaboratories qisTxnItemLab, QisUserDetails authUser) {
		qisTxnItemLab.setQcId(authUser.getId());
		qisTxnItemLab.setQcDate(Calendar.getInstance());
		qisTransactionItemLaboratoriesRepositories.save(qisTxnItemLab);

		QisUserPersonel labPersonel = appUtility.getQisUserPersonelByUserId(authUser.getUserid());
		qisTxnItemLab.setQualityControl(labPersonel);

		qisLogService.info(authUser.getId(), QisTransactionLaboratoryIndustrialController.class.getSimpleName(),
				"UPDATE", "QC Industrial", qisTxnItemLab.getId(), CATEGORY);
	}

}
