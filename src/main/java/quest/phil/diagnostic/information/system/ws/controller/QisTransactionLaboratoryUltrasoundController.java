package quest.phil.diagnostic.information.system.ws.controller;

import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionUltrasound;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ultrasound.QisTransactionLabUltrasound;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabUltrasoundRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisReferenceLaboratoryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryDetailsRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionUltrasoundRepository;
import quest.phil.diagnostic.information.system.ws.repository.ultrasound.QisTransactionLabUltrasoundRepository;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/laboratory/{laboratoryId}")
public class QisTransactionLaboratoryUltrasoundController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratoryEcgController.class);
	
	@Autowired
	private QisTransactionLabUltrasoundRepository qisTransactionLabUltrasound;

	@Autowired
	private QisTransactionUltrasoundRepository qisTransactionUltrasoundRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	AppUtility appUtility;
	
	@Autowired
	private QisTransactionItemRepository qisTransactionItem;
	
	@Autowired
	private QisReferenceLaboratoryRepository qisReferenceLaboratoryRepository;

	@Autowired
	private QisTransactionLaboratoryDetailsRepository qisTransactionLaboratoryDetailsRepository;
	
	@PostMapping("/ultrasound")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public ResponseEntity<String> saveTransactionLaboratoryUltrasound(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @Valid @RequestBody QisTransactionLabUltrasoundRequest ultrasoundRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		
		LOGGER.info(authUser.getId() + "-Save ULTRASOUND:" + transactionId);

		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}
		QisTransactionUltrasound qisTransactionUltrasound = qisTransactionUltrasoundRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);

		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(ultrasoundRequest.getRadiologistId());
		if (qisDoctor == null) {
			throw new RuntimeException("Radiologist not found.",
					new Throwable("radiologistId: Radiologist not found."));
		}

		String action = "ADDED";
		String ultrasoundLogMsg = "";

		QisTransactionLabUltrasound oldUltrasoundData = qisTransactionLabUltrasound
				.getTransactionLabUltrasoundByLabReqId(laboratoryId);
		
		List<QisTransactionItem> listItem = qisTransactionItem.getTransactionItemsByTransactionid(qisTransaction.getId());
		QisReferenceLaboratory referenceLab = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(ultrasoundRequest.getRadiologistId());

		QisTransactionLaboratoryDetails laboratoryDetails = null;
		laboratoryDetails = qisTransactionLaboratoryDetailsRepository
				.getTransactionLaboratoryDetailsById(qisTransaction.getId(), laboratoryId);

//		if (laboratoryDetails != null) {
//			if (referenceLab != null) {
//				laboratoryDetails.setReferenceLab(referenceLab);
//				laboratoryDetails.setReferenceLabId(referenceLab.getId());
//				if ("PCK".equals(qisTransactionUltrasound.getTransactionItem().getItemType())) {
//					for (QisReferencePackageItems refPackage : referenceLab.getCollectionPackage()) {
//						for (QisTransactionItem item : listItem) {
//							if (refPackage.getReferencePackageItems().getId() == item.getQisPackageId()) {
//								laboratoryDetails.setMolePriceItem(refPackage.getMolePrice());
//							}
//						}
//					}
//				}else {					
//					for (QisReferenceLaboratoryItems refItems : referenceLab.getCollectionItems()) {
//						if (qisTransactionUltrasound.getItemDetails().getItemid().equals(refItems.getReferenceLabItems().getItemid())) {
//							laboratoryDetails.setMolePriceItem(refItems.getMolePrice());
//						}
//					}
//				}
//				qisTransactionLaboratoryDetailsRepository.save(laboratoryDetails);
//			} else {
//				laboratoryDetails.setReferenceLab(null);
//				laboratoryDetails.setReferenceLabId(null);
//			}
//		}
		
		if (oldUltrasoundData == null) {
			QisTransactionLabUltrasound ultrasound = new QisTransactionLabUltrasound();
			BeanUtils.copyProperties(ultrasoundRequest, ultrasound);
			ultrasound.setId(laboratoryId);
			ultrasound.setCreatedBy(authUser.getId());
			ultrasound.setUpdatedBy(authUser.getId());
			ultrasound.setRadiologist(qisDoctor);
			ultrasound.setRadiologistId(qisDoctor.getId());
			
			
//			if (referenceLab != null) {
//				ultrasound.setReferenceLab(referenceLab);
//				ultrasound.setReferenceLabId(referenceLab.getId());
//			}

			if (qisTransactionUltrasound.getLabPersonelId() == null) {
				qisTransactionUltrasound.setLabPersonelId(authUser.getId());
				qisTransactionUltrasound.setLabPersonelDate(Calendar.getInstance());
				qisTransactionUltrasound.setStatus(2);
			}
			qisTransactionLabUltrasound.save(ultrasound);
		} else {
			action = "UPDATED";
			oldUltrasoundData.setUpdatedBy(authUser.getId());
			oldUltrasoundData.setUpdatedAt(Calendar.getInstance());

			
//			if (ultrasoundRequest.getRadiologistId() != null) {
//				if (oldUltrasoundData.getReferenceLab() != null) {
//					if (ultrasoundRequest.getRadiologistId() != oldUltrasoundData.getReferenceLab().getReferenceid()) {
//						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "Reference Laboratory",
//								String.valueOf(oldUltrasoundData.getReferenceLab().getReferenceid()),
//								String.valueOf(ultrasoundRequest.getRadiologistId()));
//						if (referenceLab != null) {
//							oldUltrasoundData.setReferenceLab(referenceLab);
//							oldUltrasoundData.setReferenceLabId(referenceLab.getId());
//						} else {
//							oldUltrasoundData.setReferenceLab(null);
//							oldUltrasoundData.setReferenceLabId(null);
//						}
//					}
//				} else {
//					ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "Reference Laboratory", String.valueOf(""),
//							String.valueOf(ultrasoundRequest.getRadiologistId()));
//					if (referenceLab != null) {
//						oldUltrasoundData.setReferenceLab(referenceLab);
//						oldUltrasoundData.setReferenceLabId(referenceLab.getId());
//					} else {
//						oldUltrasoundData.setReferenceLab(null);
//						oldUltrasoundData.setReferenceLabId(null);
//					}
//				}
//			} else {
//				if (oldUltrasoundData.getReferenceLab() != null) {
//					ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "Reference Laboratory",
//							String.valueOf(oldUltrasoundData.getReferenceLab().getReferenceid()), null);
//					oldUltrasoundData.setReferenceLab(null);
//					oldUltrasoundData.setReferenceLabId(null);
//				}
//			}
			
			if (ultrasoundRequest.getFindings() != null) {
				if (oldUltrasoundData.getFindings() != null) {					
					if (!ultrasoundRequest.getFindings().equals(oldUltrasoundData.getFindings())) {
						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "findings", oldUltrasoundData.getFindings(),
								ultrasoundRequest.getFindings());
						oldUltrasoundData.setFindings(ultrasoundRequest.getFindings());
					}
				}else {
					oldUltrasoundData.setFindings(ultrasoundRequest.getFindings());
				}
			}else {
				oldUltrasoundData.setFindings(null);
			}
			
			if (ultrasoundRequest.getFinding_header_pelvic() != null) {
				if (oldUltrasoundData.getFinding_header_pelvic() != null) {					
					if (!ultrasoundRequest.getFinding_header_pelvic().equals(oldUltrasoundData.getFinding_header_pelvic())) {
						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "findings", oldUltrasoundData.getFinding_header_pelvic(),
								ultrasoundRequest.getFinding_header_pelvic());
						oldUltrasoundData.setFinding_header_pelvic(ultrasoundRequest.getFinding_header_pelvic());
					}
				}else {
					oldUltrasoundData.setFinding_header_pelvic(ultrasoundRequest.getFinding_header_pelvic());
				}
			}else {
				oldUltrasoundData.setFinding_header_pelvic(null);
			}
			
			if (ultrasoundRequest.getFinding_footer_pelvic() != null) {
				if (oldUltrasoundData.getFinding_footer_pelvic() != null) {					
					if (!ultrasoundRequest.getFinding_footer_pelvic().equals(oldUltrasoundData.getFinding_footer_pelvic())) {
						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "findings", oldUltrasoundData.getFinding_footer_pelvic(),
								ultrasoundRequest.getFinding_footer_pelvic());
						oldUltrasoundData.setFinding_footer_pelvic(ultrasoundRequest.getFinding_footer_pelvic());
					}
				}else {
					oldUltrasoundData.setFinding_footer_pelvic(ultrasoundRequest.getFinding_footer_pelvic());
				}
			}else {
				oldUltrasoundData.setFinding_footer_pelvic(null);
			}
			
			if (ultrasoundRequest.getBpd_size() != null) {
				if (oldUltrasoundData.getBpd_size() != null) {					
					if (!ultrasoundRequest.getBpd_size().equals(oldUltrasoundData.getBpd_size())) {
						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "findings", oldUltrasoundData.getBpd_size(),
								ultrasoundRequest.getBpd_size());
						oldUltrasoundData.setBpd_size(ultrasoundRequest.getBpd_size());
					}
				}else {
					oldUltrasoundData.setBpd_size(ultrasoundRequest.getBpd_size());
				}
			}else {
				oldUltrasoundData.setBpd_size(null);
			}
			
			if (ultrasoundRequest.getBpd_old() != null) {
				if (oldUltrasoundData.getBpd_old() != null) {					
					if (!ultrasoundRequest.getBpd_old().equals(oldUltrasoundData.getBpd_old())) {
						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "findings", oldUltrasoundData.getBpd_old(),
								ultrasoundRequest.getBpd_old());
						oldUltrasoundData.setBpd_old(ultrasoundRequest.getBpd_old());
					}
				}else {
					oldUltrasoundData.setBpd_old(ultrasoundRequest.getBpd_old());
				}
			}else {
				oldUltrasoundData.setBpd_old(null);
			}
			
			if (ultrasoundRequest.getHc_size() != null) {
				if (oldUltrasoundData.getHc_size() != null) {					
					if (!ultrasoundRequest.getHc_size().equals(oldUltrasoundData.getHc_size())) {
						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "findings", oldUltrasoundData.getHc_size(),
								ultrasoundRequest.getHc_size());
						oldUltrasoundData.setHc_size(ultrasoundRequest.getHc_size());
					}
				}else {
					oldUltrasoundData.setHc_size(ultrasoundRequest.getHc_size());
				}
			}else {
				oldUltrasoundData.setHc_size(null);
			}
			
			
			if (ultrasoundRequest.getHc_old() != null) {
				if (oldUltrasoundData.getHc_old() != null) {					
					if (!ultrasoundRequest.getHc_old().equals(oldUltrasoundData.getHc_old())) {
						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "findings", oldUltrasoundData.getHc_old(),
								ultrasoundRequest.getHc_old());
						oldUltrasoundData.setHc_old(ultrasoundRequest.getHc_old());
					}
				}else {
					oldUltrasoundData.setHc_old(ultrasoundRequest.getHc_old());
				}
			}else {
				oldUltrasoundData.setHc_old(null);
			}
			
			if (ultrasoundRequest.getAc_size() != null) {
				if (oldUltrasoundData.getAc_size() != null) {					
					if (!ultrasoundRequest.getAc_size().equals(oldUltrasoundData.getAc_size())) {
						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "findings", oldUltrasoundData.getAc_size(),
								ultrasoundRequest.getAc_size());
						oldUltrasoundData.setAc_size(ultrasoundRequest.getAc_size());
					}
				}else {
					oldUltrasoundData.setAc_size(ultrasoundRequest.getAc_size());
				}
			}else {
				oldUltrasoundData.setAc_size(null);
			}
			
			if (ultrasoundRequest.getAc_old() != null) {
				if (oldUltrasoundData.getAc_old() != null) {					
					if (!ultrasoundRequest.getAc_old().equals(oldUltrasoundData.getAc_old())) {
						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "findings", oldUltrasoundData.getAc_old(),
								ultrasoundRequest.getAc_old());
						oldUltrasoundData.setAc_old(ultrasoundRequest.getAc_old());
					}
				}else {
					oldUltrasoundData.setAc_old(ultrasoundRequest.getAc_old());
				}
			}else {
				oldUltrasoundData.setAc_old(null);
			}
			
			if (ultrasoundRequest.getFl_size() != null) {
				if (oldUltrasoundData.getFl_size() != null) {					
					if (!ultrasoundRequest.getFl_size().equals(oldUltrasoundData.getFl_size())) {
						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "findings", oldUltrasoundData.getFl_size(),
								ultrasoundRequest.getFl_size());
						oldUltrasoundData.setFl_size(ultrasoundRequest.getFl_size());
					}
				}else {
					oldUltrasoundData.setFl_size(ultrasoundRequest.getFl_size());
				}
			}else {
				oldUltrasoundData.setFl_size(null);
			}
			
			if (ultrasoundRequest.getFl_old() != null) {
				if (oldUltrasoundData.getFl_old() != null) {					
					if (!ultrasoundRequest.getFl_old().equals(oldUltrasoundData.getFl_old())) {
						ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "findings", oldUltrasoundData.getFl_old(),
								ultrasoundRequest.getFl_old());
						oldUltrasoundData.setFl_old(ultrasoundRequest.getFl_old());
					}
				}else {
					oldUltrasoundData.setFl_old(ultrasoundRequest.getFl_old());
				}
			}else {
				oldUltrasoundData.setFl_old(null);
			}

			if (!ultrasoundRequest.getImpressions().equals(oldUltrasoundData.getImpressions())) {
				ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "impressions",
						oldUltrasoundData.getImpressions(), ultrasoundRequest.getImpressions());
				oldUltrasoundData.setImpressions(ultrasoundRequest.getImpressions());
			}

			if (qisDoctor.getId() != oldUltrasoundData.getRadiologistId()) {
				ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "radiologist",
						oldUltrasoundData.getRadiologist().getDoctorid(), qisDoctor.getDoctorid());
				oldUltrasoundData.setRadiologist(qisDoctor);
				oldUltrasoundData.setRadiologistId(qisDoctor.getId());
			}

			if (ultrasoundRequest.getRemarks() != oldUltrasoundData.getRemarks()) {
				ultrasoundLogMsg = appUtility.formatUpdateData(ultrasoundLogMsg, "result",
						String.valueOf(oldUltrasoundData.getRemarks()), String.valueOf(ultrasoundRequest.getRemarks()));
				oldUltrasoundData.setRemarks(ultrasoundRequest.getRemarks());
			}
			qisTransactionLabUltrasound.save(oldUltrasoundData);
		}
		return new ResponseEntity<>(action, HttpStatus.OK);
	}

}
