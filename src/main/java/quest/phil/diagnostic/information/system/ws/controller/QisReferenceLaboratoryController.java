package quest.phil.diagnostic.information.system.ws.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPackage;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratoryItems;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferencePackageItems;
import quest.phil.diagnostic.information.system.ws.model.request.QisReferenceLaboratoryItemsRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisReferenceLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisReferencePackageRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisItemRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisPackageRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisReferenceLaboratoryRepository;

@RestController
@RequestMapping("/api/v1/")
public class QisReferenceLaboratoryController {
	private final String CATEGORY = "Reference laboratory";

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisItemRepository qisItemRepository;
	
	@Autowired
	private QisPackageRepository qisPackageRepository;

	@Autowired
	QisReferenceLaboratoryRepository qisReferenceLaboratoryRepository;

	// CREATE Reference Laboratory
	@PostMapping("reference_laboratory")
	public QisReferenceLaboratory createReferenceLaboratory(
			@Valid @RequestBody QisReferenceLaboratoryRequest qisReferenceLaboratoryReq,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		String referenceLaboratoryId = appUtility.generateUserId(12);

		QisReferenceLaboratory updateReference = qisReferenceLaboratoryRepository
				.getReferenceLaboratory(qisReferenceLaboratoryReq.getReferenceId());

		QisReferenceLaboratory referenceLaboratory = new QisReferenceLaboratory();
		if (updateReference == null) {
			if (qisReferenceLaboratoryRepository.existsByName(qisReferenceLaboratoryReq.getName()))
				throw new RuntimeException("Duplicate Reference Laboratory name.",
						new Throwable("Reference Laboratory Name: Duplicate package name."));

			if (qisReferenceLaboratoryRepository.existsByReferenceid(referenceLaboratoryId))
				throw new RuntimeException("Duplicate Reference id.",
						new Throwable("Referenceid: Duplicate Reference id."));
			referenceLaboratory.setCreatedBy(authUser.getId());
			referenceLaboratory.setUpdatedBy(authUser.getId());
			referenceLaboratory.setName(qisReferenceLaboratoryReq.getName());
			referenceLaboratory.setReferenceid(referenceLaboratoryId);
			referenceLaboratory.setAddress(qisReferenceLaboratoryReq.getAddress());
			referenceLaboratory.setContactPerson(qisReferenceLaboratoryReq.getContactPerson());
			referenceLaboratory.setContactNumber(qisReferenceLaboratoryReq.getContactNumber());
			referenceLaboratory.setSopEmail(qisReferenceLaboratoryReq.getSopEmail());
			referenceLaboratory.setResultsEmail(qisReferenceLaboratoryReq.getResultsEmail());
			referenceLaboratory.setSopCode(qisReferenceLaboratoryReq.getSopCode());
			referenceLaboratory.setIsActive(true);
			
			Set<QisReferencePackageItems> packageItems = getPackageItems(
					qisReferenceLaboratoryReq.getReferencePackages());
			
			Set<QisReferenceLaboratoryItems> labItems = getLabItems(
					qisReferenceLaboratoryReq.getReferenceItems());
			referenceLaboratory.setCollectionItems(labItems);
			referenceLaboratory.setCollectionPackage(packageItems);
		qisReferenceLaboratoryRepository.save(referenceLaboratory);
		} else {
			if (qisReferenceLaboratoryReq.getName() != null) {
				updateReference.setName(qisReferenceLaboratoryReq.getName());
			}
			if (qisReferenceLaboratoryReq.getAddress() != null) {
				updateReference.setAddress(qisReferenceLaboratoryReq.getAddress());
			}
			if (qisReferenceLaboratoryReq.getContactPerson() != null) {
				updateReference.setContactPerson(qisReferenceLaboratoryReq.getContactPerson());
			}
			if (qisReferenceLaboratoryReq.getContactNumber() != null) {
				updateReference.setContactNumber(qisReferenceLaboratoryReq.getContactNumber());
			}
			if (qisReferenceLaboratoryReq.getSopCode() != null) {
				updateReference.setSopEmail(qisReferenceLaboratoryReq.getSopCode());
			}
			if (qisReferenceLaboratoryReq.getReferenceItems() != null) {
				Set<QisReferenceLaboratoryItems> LabItems = getLabItems(
						qisReferenceLaboratoryReq.getReferenceItems());
				updateReference.setCollectionItems(LabItems);
//				if (packageItems.isEmpty()) {
//					throw new RuntimeException("Package Item(s) must not be empty. Please check your item id list.");
//				}
			}
			if (qisReferenceLaboratoryReq.getReferencePackages() != null) {
				Set<QisReferencePackageItems> Packagetems = getPackageItems(
						qisReferenceLaboratoryReq.getReferencePackages());
				updateReference.setCollectionPackage(Packagetems);		
			}

			updateReference.setUpdatedBy(authUser.getId());
			qisReferenceLaboratoryRepository.save(updateReference);
		}

		return referenceLaboratory;
	}

	@GetMapping("reference_laboratory/list")
	public List<QisReferenceLaboratory> getReferenceLaboratoryMethod(@AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {

		return qisReferenceLaboratoryRepository.getAllReferenceLaboratory();
	}

	private Set<QisReferenceLaboratoryItems> getLabItems(Set<QisReferenceLaboratoryItemsRequest> set) {
		Set<QisReferenceLaboratoryItems> items = new HashSet<>();
		if (set != null) {
			set.forEach(item -> {
				QisItem qisItem = qisItemRepository.findByItemid(item.getItemId());
				if (qisItem != null) {
					QisReferenceLaboratoryItems itm = new QisReferenceLaboratoryItems();
					itm.setMolePrice(item.getMolePrice());
					itm.setOriginalPrice(item.getOriginalPrice());
					itm.setReferenceItemId(qisItem.getId());
					itm.setReferenceLabItems(qisItem);
					items.add(itm);
				}
			});
		}
		return items;
	}
	
	private Set<QisReferencePackageItems> getPackageItems(Set<QisReferencePackageRequest> set) {
		Set<QisReferencePackageItems> Packages = new HashSet<>();
		if (set != null) {
			set.forEach(pck -> {
				QisPackage qisPackage = qisPackageRepository.findByPackageid(pck.getPackageId());
				if (qisPackage != null) {
					QisReferencePackageItems pckk = new QisReferencePackageItems();
					pckk.setMolePrice(pck.getMolePrice());
					pckk.setOriginalPrice(pck.getOriginalPrice());
					pckk.setReferencePackageId(qisPackage.getId());
					pckk.setReferencePackageItems(qisPackage);
					Packages.add(pckk);
				}
			});
		}
		return Packages;
	}
}
