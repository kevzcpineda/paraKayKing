package quest.phil.diagnostic.information.system.ws.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.QisPackageType;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPackage;
import quest.phil.diagnostic.information.system.ws.model.request.QisPackageRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisOptionResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisPackageResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisItemRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisPackageRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisPackageController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisPackageController.class);
	private final String CATEGORY = "PACAKGE";

	@Autowired
	private QisPackageRepository qisPackageRepository;

	@Autowired
	private QisItemRepository qisItemRepository;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	// CREATE ITEM
	@PostMapping("package")
	public QisPackage createPackage(@Valid @RequestBody QisPackageRequest packageRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Create Package");

		String packageId = appUtility.generateUserId(12);
		if (qisPackageRepository.existsByPackageName(packageRequest.getPackageName()))
			throw new RuntimeException("Duplicate package name.",
					new Throwable("packageName: Duplicate package name."));
		if (qisPackageRepository.existsByPackageid(packageId))
			throw new RuntimeException("Duplicate package id.", new Throwable("packageid: Duplicate package id."));
		if (appUtility.getPackageType(packageRequest.getPackageType()) == null)
			throw new RuntimeException(
					"Fail! -> Cause: Package Type[" + packageRequest.getPackageType() + "] not found.",
					new Throwable("packageType: Package Type[" + packageRequest.getPackageType() + "] not found."));

		Double packagePrice = appUtility.parseDoubleAmount(packageRequest.getPackagePrice());
		if (packagePrice == null) {
			throw new RuntimeException("Invalid Package Price.", new Throwable("packagePrice: Invalid Package Price."));
		}

		Set<QisItem> packageItems = getPackageItems(packageRequest.getPackageItems());
		if (packageItems.isEmpty()) {
			throw new RuntimeException("Package Item(s) must not be empty. Please check your item id list.");
		}

		QisPackage qisPackage = new QisPackage();
		BeanUtils.copyProperties(packageRequest, qisPackage);

		if (packageRequest.isActive() == null) {
			qisPackage.setActive(true);
		} else {
			qisPackage.setActive(packageRequest.isActive());
		}
		if (packageRequest.isTaxable() == null) {
			qisPackage.setTaxable(true);
		} else {
			qisPackage.setTaxable(packageRequest.isTaxable());
		}
		if (packageRequest.isDiscountable() == null) {
			qisPackage.setDiscountable(true);
		} else {
			qisPackage.setDiscountable(packageRequest.isDiscountable());
		}
		if (packageRequest.isOnMenu() == null) {
			qisPackage.setOnMenu(false);
		} else {
			qisPackage.setOnMenu(packageRequest.isOnMenu());
		}
		qisPackage.setPackageid(packageId);
		qisPackage.setPackagePrice(packagePrice);
		qisPackage.setCreatedBy(authUser.getId());
		qisPackage.setUpdatedBy(authUser.getId());
		qisPackage.setPackageItems(packageItems);

		QisPackageResponse qisPackageResponse = new QisPackageResponse();
		BeanUtils.copyProperties(qisPackage, qisPackageResponse);
		qisPackageResponse.setPackageItems(getStrPacakgeItems(qisPackage.getPackageItems()));

		qisPackageRepository.save(qisPackage);

		qisLogService.info(authUser.getId(), QisPackageController.class.getSimpleName(), "CREATE",
				qisPackageResponse.toString(), qisPackage.getId(), CATEGORY);

		return qisPackage;
	}

	// READ PACKAGE
	@GetMapping("package/{packageId}")
	public QisPackage getPackage(@PathVariable String packageId, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.warn(authUser.getId() + "-View Package:" + packageId);
		QisPackage qisPackage = qisPackageRepository.findByPackageid(packageId);
		if (qisPackage == null) {
			throw new RuntimeException("Record not found.");
		}

		return qisPackage;
	}

	// UPDATE PACKAGE
	@PutMapping("package/{packageId}")
	public QisPackage updatePackage(@PathVariable String packageId,
			@Valid @RequestBody QisPackageRequest packageRequest, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.warn(authUser.getId() + "-View Package:" + packageId);
		QisPackage qisPackage = qisPackageRepository.findByPackageid(packageId);
		if (qisPackage == null) {
			throw new RuntimeException("Record not found.");
		}

		if (qisPackageRepository.findPackageNameOnOtherPackage(packageRequest.getPackageName(),
				qisPackage.getId()) != null)
			throw new RuntimeException("Duplicate package name.",
					new Throwable("packageName: Duplicate package name."));

		if (appUtility.getPackageType(packageRequest.getPackageType()) == null)
			throw new RuntimeException(
					"Fail! -> Cause: Package Type[" + packageRequest.getPackageType() + "] not found.",
					new Throwable("packageType: Package Type[" + packageRequest.getPackageType() + "] not found."));

		Double packagePrice = appUtility.parseDoubleAmount(packageRequest.getPackagePrice());
		if (packagePrice == null) {
			throw new RuntimeException("Invalid Package Price.", new Throwable("packagePrice: Invalid Package Price."));
		}

		Set<QisItem> packageItems = getPackageItems(packageRequest.getPackageItems());
		if (packageItems.isEmpty()) {
			throw new RuntimeException("Package Item(s) must not be empty. Please check your item id list.");
		}

		boolean isUpdate = false;
		String updateData = "";
		if (!qisPackage.getPackageName().equals(packageRequest.getPackageName())) {
			updateData = appUtility.formatUpdateData(updateData, "Package Name", qisPackage.getPackageName(),
					packageRequest.getPackageName());
			isUpdate = true;
			qisPackage.setPackageName(packageRequest.getPackageName());
		}

		if (!qisPackage.getPackageDescription().equals(packageRequest.getPackageDescription())) {
			updateData = appUtility.formatUpdateData(updateData, "Package Description",
					qisPackage.getPackageDescription(), packageRequest.getPackageDescription());
			isUpdate = true;
			qisPackage.setPackageDescription(packageRequest.getPackageDescription());
		}

		if (!qisPackage.getPackagePrice().equals(packagePrice)) {
			updateData = appUtility.formatUpdateData(updateData, "Package Price",
					String.valueOf(qisPackage.getPackagePrice()), String.valueOf(packagePrice));
			isUpdate = true;
			qisPackage.setPackagePrice(packagePrice);
		}

		if (!qisPackage.getPackageType().equals(packageRequest.getPackageType())) {
			updateData = appUtility.formatUpdateData(updateData, "Package Type", qisPackage.getPackageType(),
					packageRequest.getPackageType());
			isUpdate = true;
			qisPackage.setPackageType(packageRequest.getPackageType());
		}
		
		if (!qisPackage.getTypeTestPackage().equals(packageRequest.getTypeTestPackage())) {
			updateData = appUtility.formatUpdateData(updateData, "Type Test Package", qisPackage.getTypeTestPackage(),
					packageRequest.getTypeTestPackage());
			isUpdate = true;
			qisPackage.setTypeTestPackage(packageRequest.getTypeTestPackage());
		}

		if (packageRequest.isActive() != null && qisPackage.isActive() != packageRequest.isActive()) {
			updateData = appUtility.formatUpdateData(updateData, "Active", String.valueOf(qisPackage.isActive()),
					String.valueOf(packageRequest.isActive()));
			isUpdate = true;
			qisPackage.setActive(packageRequest.isActive());
		}

		if (packageRequest.isTaxable() != null && qisPackage.isTaxable() != packageRequest.isTaxable()) {
			updateData = appUtility.formatUpdateData(updateData, "Taxable", String.valueOf(qisPackage.isTaxable()),
					String.valueOf(packageRequest.isTaxable()));
			isUpdate = true;
			qisPackage.setTaxable(packageRequest.isTaxable());
		}

		if (packageRequest.isDiscountable() != null && qisPackage.isDiscountable() != packageRequest.isDiscountable()) {
			updateData = appUtility.formatUpdateData(updateData, "Discountable",
					String.valueOf(qisPackage.isDiscountable()), String.valueOf(packageRequest.isDiscountable()));
			isUpdate = true;
			qisPackage.setDiscountable(packageRequest.isDiscountable());
		}

		if (packageRequest.isOnMenu() != null && qisPackage.isOnMenu() != packageRequest.isOnMenu()) {
			updateData = appUtility.formatUpdateData(updateData, "OnMenu", String.valueOf(qisPackage.isOnMenu()),
					String.valueOf(packageRequest.isOnMenu()));
			isUpdate = true;
			qisPackage.setOnMenu(packageRequest.isOnMenu());
		}

		Set<QisItem> items = getPackageItems(packageRequest.getPackageItems());
		Set<String> changeItems = getChangePackageItems(qisPackage.getPackageItems(), items);

		if (!changeItems.isEmpty()) {
			isUpdate = true;
			qisPackage.setPackageItems(items);
			updateData = appUtility.addToFormatedData(updateData, "pacakge items:", String.valueOf(changeItems));
		}

		if (isUpdate) {
			qisPackage.setUpdatedBy(authUser.getId());
			qisPackage.setUpdatedAt(Calendar.getInstance());
			qisPackageRepository.save(qisPackage);
			qisLogService.warn(authUser.getId(), QisPackageController.class.getSimpleName(), "UPDATE", updateData,
					qisPackage.getId(), CATEGORY);
		}

		return qisPackage;
	}

	// DELETE PACKAGE
	@DeleteMapping("package/{packageId}")
	public String deletePackage(@PathVariable String packageId, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.warn(authUser.getId() + "-Delete Package:" + packageId);
		return "not implemented";
	}

	// LIST ALL PACKAGES
	@GetMapping("packages")
	public Page<QisPackage> getAllPackages(Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info("List of Packages");
		return qisPackageRepository.findAll(pageable);
	}

	// LIST ALL ACTIVE PACKAGES
	@GetMapping("packages/active")
	public Page<QisPackage> getAllActivePackages(Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info("List of active Packages");
		return qisPackageRepository.allActivePackages(pageable);
	}
	
	// SEARCH PACKAGE
	@GetMapping("packages/search")
	public Page<QisPackage> searchPackages(@RequestParam String searchKey, Pageable pageable,
			@AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info("Search of Packages");
		return qisPackageRepository.searchPackages(searchKey, pageable);
	}

	// LIST ALL PACKAGE TYPES
	@GetMapping("package_types")
	public List<QisOptionResponse> getAllPackageTypes() {
		Map<String, String> ptype = QisPackageType.getPackageTypes();

		List<QisOptionResponse> list = new ArrayList<QisOptionResponse>();
		for (Map.Entry<String, String> entry : ptype.entrySet()) {
			list.add(new QisOptionResponse(entry.getKey(), entry.getValue()));
		}

		return list;
	}

	private Set<String> getStrPacakgeItems(Set<QisItem> objItems) {
		Set<String> items = new HashSet<>();

		if (objItems != null) {
			objItems.forEach(item -> {
				String strItem = item.getItemid();
				items.add(strItem);
			});
		}

		return items;
	}

	private Set<QisItem> getPackageItems(Set<String> strItems) {
		Set<QisItem> items = new HashSet<>();

		if (strItems != null) {
			strItems.forEach(itemid -> {
				QisItem qisItem = qisItemRepository.findByItemid(itemid);
				if (qisItem != null) {
					items.add(qisItem);
				}
			});
		}

		return items;
	}

	private Set<String> getChangePackageItems(Set<QisItem> currentItems, Set<QisItem> newItems) {
		Set<String> changes = new HashSet<>();

		Set<String> added = new HashSet<>();
		Set<String> removed = new HashSet<>();
		Set<String> remained = new HashSet<>();
		if (currentItems != null && !currentItems.isEmpty()) {
			currentItems.forEach(item -> {
				String strItem = item.getItemid();
				if (!isObjectInSet(item, newItems)) {
					removed.add(strItem);
				} else {
					remained.add(strItem);
				}
			});
		}

		if (newItems != null && !newItems.isEmpty()) {
			newItems.forEach(item -> {
				String strItem = item.getItemid();
				if (!isObjectInSet(item, currentItems)) {
					added.add(strItem);
				} else {
					remained.add(strItem);
				}
			});
		}

		if (!added.isEmpty() || !removed.isEmpty()) {
			if (!added.isEmpty()) {
				changes.add("added:" + added);
			}

			if (!removed.isEmpty()) {
				changes.add("removed:" + removed);
			}

			if (!remained.isEmpty()) {
				changes.add("remained:" + remained);
			}
		}

		return changes;
	}

	private boolean isObjectInSet(QisItem object, Set<QisItem> set) {
		boolean result = false;

		if (set != null && !set.isEmpty()) {
			for (QisItem o : set) {
				if (o.getId() == object.getId()) {
					result = true;
					break;
				}
			}
		}

		return result;
	}

}
