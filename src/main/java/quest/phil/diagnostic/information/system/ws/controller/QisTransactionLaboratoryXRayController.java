package quest.phil.diagnostic.information.system.ws.controller;

import java.util.Calendar;
import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratoryItems;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferencePackageItems;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionXRay;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.xray.QisTransactionLabXRay;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabXRayRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisReferenceLaboratoryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionXRayRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserPersonelRepository;
import quest.phil.diagnostic.information.system.ws.repository.xray.QisTransactionLabXRayRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/laboratory/{laboratoryId}")
public class QisTransactionLaboratoryXRayController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratoryXRayController.class);
	private final String CATEGORY = "XRAY";

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	private QisTransactionXRayRepository qisTransactionXRayRepository;

	@Autowired
	private QisTransactionLabXRayRepository qisTransactionLabXRayRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;
	
	@Autowired
	private QisReferenceLaboratoryRepository qisReferenceLaboratoryRepository;

	@Autowired
	private QisUserPersonelRepository qisUserPersonelRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;
	
	@Autowired
	private QisLogService qisLogService;
	
	@Autowired
	private QisTransactionItemRepository qisTransactionItem;

	@PostMapping("/xray")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionXRay saveTransactionLaboratoryXRay(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @Valid @RequestBody QisTransactionLabXRayRequest xrayRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Save XRAY:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionXRay qisTransactionXRay = qisTransactionXRayRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);

		if (qisTransactionXRay == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.");
		}

		if (!"XR".equals(qisTransactionXRay.getItemDetails().getItemLaboratory())) {
			throw new RuntimeException("Transaction Laboratory Request is not for XRay.");
		}

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(xrayRequest.getRadiologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Radiologist not found.",
					new Throwable("radiologistId: Radiologist not found."));
		}

		String action = "ADDED";
		boolean isAddedXRay = false;
		String xrayLogMsg = "";

		QisTransactionLabXRay xRay = qisTransactionLabXRayRepository.getTransactionLabXRayByLabReqId(laboratoryId);
		
		List<QisTransactionItem> listItem = qisTransactionItem.getTransactionItemsByTransactionid(qisTransaction.getId());
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(xrayRequest.getRadiologistId());

		QisTransactionLaboratoryDetails laboratoryDetails = null;
		laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

		if (laboratoryDetails != null) {
			System.out.println("laboratoryDetails != null");
			if (referenceLab != null) {
				System.out.println("referenceLab != null");
				laboratoryDetails.setReferenceLab(referenceLab);
				laboratoryDetails.setReferenceLabId(referenceLab.getId());
				if ("PCK".equals(qisTransactionXRay.getTransactionItem().getItemType())) {
					System.out.println("PCK");
					for (QisReferencePackageItems refPackage : referenceLab.getCollectionPackage()) {
						for (QisTransactionItem item : listItem) {
							if (refPackage.getReferencePackageItems().getId() == item.getQisPackageId()) {
								System.out.println("efPackage.getReferencePackageItems().getId() == item.getQisPack");
								laboratoryDetails.setMolePriceItem(refPackage.getMolePrice());
							}
						}
					}
				}else {
					System.out.println("else");
					for (QisReferenceLaboratoryItems refItems : referenceLab.getCollectionItems()) {
						if (qisTransactionXRay.getItemDetails().getItemid().equals(refItems.getReferenceLabItems().getItemid())) {
							System.out.println("ransactionXRay.getItemDetails().getItemid().equals(refIte");
							laboratoryDetails.setMolePriceItem(refItems.getMolePrice());
						}
					}
				}
				qisTransactionLaboratoryDetailsRepository.save(laboratoryDetails);
			} else {
				System.out.println("elseelse");
				laboratoryDetails.setReferenceLab(null);
				laboratoryDetails.setReferenceLabId(null);
			}
		}

		if (xRay == null) {
			xRay = new QisTransactionLabXRay();
			BeanUtils.copyProperties(xrayRequest, xRay);
			

			xRay.setId(laboratoryId);
			xRay.setCreatedBy(authUser.getId());
			xRay.setUpdatedBy(authUser.getId());
			xRay.setRadiologist(qisDoctor);
			xRay.setRadiologistId(qisDoctor.getId());
			xrayLogMsg = xrayRequest.toString();
			if (referenceLab != null) {
				xRay.setReferenceLab(referenceLab);
				xRay.setReferenceLabId(referenceLab.getId());
			}

			isAddedXRay = true;
		} else {
			System.out.println("elseelseelse");
			action = "UPDATED";
			xRay.setUpdatedBy(authUser.getId());
			xRay.setUpdatedAt(Calendar.getInstance());

			if (xrayRequest.getRadiologistId() != null) {
				if (xRay.getReferenceLab() != null) {
					if (xrayRequest.getRadiologistId() != xRay.getReferenceLab().getReferenceid()) {
						xrayLogMsg = appUtility.formatUpdateData(xrayLogMsg, "Reference Laboratory",
								String.valueOf(xRay.getReferenceLab().getReferenceid()),
								String.valueOf(xrayRequest.getRadiologistId()));
						if (referenceLab != null) {
							xRay.setReferenceLab(referenceLab);
							xRay.setReferenceLabId(referenceLab.getId());
						} else {
							xRay.setReferenceLab(null);
							xRay.setReferenceLabId(null);
						}
					}
				} else {
					xrayLogMsg = appUtility.formatUpdateData(xrayLogMsg, "Reference Laboratory", String.valueOf(""),
							String.valueOf(xrayRequest.getRadiologistId()));
					if (referenceLab != null) {
						xRay.setReferenceLab(referenceLab);
						xRay.setReferenceLabId(referenceLab.getId());
					} else {
						xRay.setReferenceLab(null);
						xRay.setReferenceLabId(null);
					}
				}
			} else {
				if (xRay.getReferenceLab() != null) {
					xrayLogMsg = appUtility.formatUpdateData(xrayLogMsg, "Reference Laboratory",
							String.valueOf(xRay.getReferenceLab().getReferenceid()), null);
					xRay.setReferenceLab(null);
					xRay.setReferenceLabId(null);
				}
			}

			if (!xrayRequest.getFindings().equals(xRay.getFindings())) {
				xrayLogMsg = appUtility.formatUpdateData(xrayLogMsg, "findings", xRay.getFindings(),
						xrayRequest.getFindings());
				xRay.setFindings(xrayRequest.getFindings());
			}

			if (!xrayRequest.getImpressions().equals(xRay.getImpressions())) {
				xrayLogMsg = appUtility.formatUpdateData(xrayLogMsg, "impressions", xRay.getImpressions(),
						xrayRequest.getImpressions());
				xRay.setImpressions(xrayRequest.getImpressions());
			}

			if (qisDoctor.getId() != xRay.getRadiologistId()) {
				xrayLogMsg = appUtility.formatUpdateData(xrayLogMsg, "radiologist", xRay.getRadiologist().getDoctorid(),
						qisDoctor.getDoctorid());
				xRay.setRadiologist(qisDoctor);
				xRay.setRadiologistId(qisDoctor.getId());
			}

			if (xrayRequest.getRemarks() != xRay.getRemarks()) {
				xrayLogMsg = appUtility.formatUpdateData(xrayLogMsg, "result", String.valueOf(xRay.getRemarks()),
						String.valueOf(xrayRequest.getRemarks()));
				xRay.setRemarks(xrayRequest.getRemarks());
			}
		}

		if (!"".equals(xrayLogMsg)) {
			qisTransactionLabXRayRepository.save(xRay);
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryXRayController.class.getSimpleName(), action,
					xrayLogMsg, laboratoryId, CATEGORY);
		}

		int isUpdate = 0;
		if (!qisTransactionXRay.isSubmitted()) {
			qisTransactionXRay.setSubmitted(true);
			qisTransactionXRay.setVerifiedBy(authUser.getId());
			qisTransactionXRay.setVerifiedDate(Calendar.getInstance());
			isUpdate += 1;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryXRayController.class.getSimpleName(),
					"SUBMITTED", qisTransactionXRay.getItemDetails().getItemName(), laboratoryId, CATEGORY);
		}

		if (qisTransactionXRay.getLabPersonelId() == null) {
			qisTransactionXRay.setLabPersonelId(authUser.getId());
			qisTransactionXRay.setLabPersonelDate(Calendar.getInstance());
			qisTransactionXRay.setStatus(2);
			isUpdate += 2;
			qisLogService.info(authUser.getId(), QisTransactionLaboratoryXRayController.class.getSimpleName(),
					"LAB PERSONEL", authUser.getUsername(), laboratoryId, CATEGORY);
		}

		if (isUpdate > 0) {
			qisTransactionXRayRepository.save(qisTransactionXRay);

			QisUserPersonel labPersonel = qisUserPersonelRepository.findByUserid(authUser.getUserid());
			if ((isUpdate & 1) > 0) {
				qisTransactionXRay.setVerified(labPersonel);

			}
			if ((isUpdate & 2) > 0) {
				qisTransactionXRay.setLabPersonel(labPersonel);
			}
		}

		if (isAddedXRay) {
			qisTransactionXRay.setXray(xRay);
		}
		System.out.println("qisTransactionXRay");
		return qisTransactionXRay;
	}

	@PutMapping("/xray/qc")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public QisTransactionXRay saveTransactionLaboratoryXRayQC(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Save XRAY:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionXRay qisTransactionXRay = qisTransactionXRayRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);

		if (qisTransactionXRay == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.");
		}

		if (!"XR".equals(qisTransactionXRay.getItemDetails().getItemLaboratory())) {
			throw new RuntimeException("Transaction Laboratory Request is not for XRay.");
		}

		
		if (qisTransactionXRay.getLabPersonelId().equals(authUser.getId())) {
			throw new RuntimeException("You are not authorize to check the same transaction.",
					new Throwable("You are not authorize to check the same transaction."));
		}
		
		if (qisTransactionXRay.getStatus() < 2) {
			throw new RuntimeException("Transaction Laboratory Request has no results.",
					new Throwable(qisTransactionXRay.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request has no results."));
		}
		
		
		if (qisTransactionXRay.getQcId() == null) {
			qisTransactionXRay.setQcId(authUser.getId());
			qisTransactionXRay.setQcDate(Calendar.getInstance());
			qisTransactionXRay.setStatus(3); // Quality Control
			qisTransactionXRayRepository.save(qisTransactionXRay);

			QisUserPersonel labPersonel = qisUserPersonelRepository.findByUserid(authUser.getUserid());
			qisTransactionXRay.setQualityControl(labPersonel);
		} else {
			throw new RuntimeException("Transaction Laboratory Request was already Checked.",
					new Throwable(qisTransactionXRay.getItemDetails().getItemName()
							+ ":Transaction Laboratory Request was already Checked."));
		}

		return qisTransactionXRay;
	}

	@GetMapping("/xray")
	public QisTransactionXRay getTransactionLaboratoryXRay(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-View XRAY:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}

		QisTransactionXRay qisTransactionXRay = qisTransactionXRayRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);

		if (qisTransactionXRay == null) {
			throw new RuntimeException("Transaction Laboratory Request not found.");
		}

		if (!"XR".equals(qisTransactionXRay.getItemDetails().getItemLaboratory())) {
			throw new RuntimeException("Transaction Laboratory Request is not for XRay.");
		}

		return qisTransactionXRay;
	}
}
