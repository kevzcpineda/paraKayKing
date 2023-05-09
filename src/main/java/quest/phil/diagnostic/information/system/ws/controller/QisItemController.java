package quest.phil.diagnostic.information.system.ws.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import quest.phil.diagnostic.information.system.ws.model.QisCategoryName;
import quest.phil.diagnostic.information.system.ws.model.QisClassifications;
import quest.phil.diagnostic.information.system.ws.model.QisLaboratoryName;
import quest.phil.diagnostic.information.system.ws.model.QisLaboratoryProcedure;
import quest.phil.diagnostic.information.system.ws.model.QisLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryService;
import quest.phil.diagnostic.information.system.ws.model.request.QisItemRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisItemResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisOptionResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisItemRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisLaboratoryProcedureServiceRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisLaboratoryServiceRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisItemController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisItemController.class);
	private final String CATEGORY = "ITEM";

	@Autowired
	private QisItemRepository qisItemRepository;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	private QisLaboratoryServiceRepository qisLaboratoryServiceRepository;

	@Autowired
	private QisLaboratoryProcedureServiceRepository qisLaboratoryProcedureServiceRepository;

	// CREATE ITEM
	@PostMapping("item")
	public QisItem createItem(@Valid @RequestBody QisItemRequest itemRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Create Item");

		String itemId = appUtility.generateUserId(12);
		if (qisItemRepository.existsByItemName(itemRequest.getItemName()))
			throw new RuntimeException("Duplicate item name.", new Throwable("itemName: Duplicate item name."));
		if (qisItemRepository.existsByItemid(itemId))
			throw new RuntimeException("Duplicate item id.", new Throwable("itemid: Duplicate item id."));
		if (appUtility.getLaboratoryName(itemRequest.getItemLaboratory()) == null)
			throw new RuntimeException(
					"Fail! -> Cause: Laboratory Code[" + itemRequest.getItemLaboratory() + "] not found.",
					new Throwable(
							"itemLaboratory: Laboratory Code[" + itemRequest.getItemLaboratory() + "] not found."));
		if (appUtility.getCategoryName(itemRequest.getItemCategory()) == null)
			throw new RuntimeException(
					"Fail! -> Cause: Category Code[" + itemRequest.getItemCategory() + "] not found.",
					new Throwable("itemCategory: Category Code[" + itemRequest.getItemCategory() + "] not found."));

		if (itemRequest.getItemCategory().equals("LAB")) {
			if (itemRequest.getItemLaboratoryProcedure() == null)
				throw new RuntimeException("Validation Failed [Item Laboratory Procedure is required.].",
						new Throwable("itemLaboratoryProcedure: Item Laboratory Procedure is required."));
			else if (appUtility.getLaboratoryProcedure(itemRequest.getItemLaboratoryProcedure()) == null)
				throw new RuntimeException(
						"Fail! -> Cause: Laboratory Procedure Code[" + itemRequest.getItemLaboratoryProcedure()
								+ "] not found.",
						new Throwable("itemLaboratoryProcedure: Laboratory Procedure Code["
								+ itemRequest.getItemLaboratoryProcedure() + "] not found."));

			switch (itemRequest.getItemLaboratoryProcedure()) {
			case "CM":
			case "MB":
			case "HE":
			case "CH":
			case "SE": {
				if (itemRequest.getServiceRequest() == null) {
					throw new RuntimeException("Fail! -> Cause: Laboratory Services is required.",
							new Throwable("serviceRequest: Laboratory Services is required."));
				} else if (itemRequest.getServiceRequest().isEmpty()) {
					throw new RuntimeException("Fail! -> Cause: Laboratory Services is empty.",
							new Throwable("serviceRequest: Laboratory Services is empty."));
				}
			}
				break;
			default: {
				itemRequest.setServiceRequest(null);
			}
				break;
			}
		}

		Double itemPrice = appUtility.parseDoubleAmount(itemRequest.getItemPrice());
		if (itemPrice == null) {
			throw new RuntimeException("Invalid Item Price.", new Throwable("itemPrice: Invalid Item Price."));
		}

		QisItem qisItem = new QisItem();
		BeanUtils.copyProperties(itemRequest, qisItem);

		if (itemRequest.isActive() == null) {
			qisItem.setActive(true);
		} else {
			qisItem.setActive(itemRequest.isActive());
		}
		if (itemRequest.isTaxable() == null) {
			qisItem.setTaxable(true);
		} else {
			qisItem.setTaxable(itemRequest.isTaxable());
		}
		if (itemRequest.isDiscountable() == null) {
			qisItem.setDiscountable(true);
		} else {
			qisItem.setDiscountable(itemRequest.isDiscountable());
		}
		if (itemRequest.isOnMenu() == null) {
			qisItem.setOnMenu(false);
		} else {
			qisItem.setOnMenu(itemRequest.isOnMenu());
		}
		if (itemRequest.getSpecificTest() != null) {
			String specificTest = itemRequest.getSpecificTest();
			specificTest = specificTest.substring(0, specificTest.length() - 1);
			qisItem.setSpecificTest(specificTest);
		}
		qisItem.setItemid(itemId);
		qisItem.setItemPrice(itemPrice);
		qisItem.setCreatedBy(authUser.getId());
		qisItem.setUpdatedBy(authUser.getId());

		// if with services
		if (itemRequest.getItemLaboratoryProcedure() != null) {
			switch (itemRequest.getItemLaboratoryProcedure()) {
			case "CM":
			case "HE":
			case "MB":
			case "CH":
			case "SE": {
				Set<QisLaboratoryProcedureService> services = getLaboratoryServices(itemRequest.getServiceRequest(),
						itemRequest.getItemLaboratoryProcedure());
				qisItem.setServiceRequest(services);
			}
				break;
			default: {
				qisItem.setServiceRequest(null);
			}
				break;
			}
		} else {
			qisItem.setServiceRequest(null);
		}

		QisItemResponse qisItemResponse = new QisItemResponse();
		BeanUtils.copyProperties(qisItem, qisItemResponse);
		qisItemResponse.setActive(qisItem.isActive());
		qisItemResponse.setTaxable(qisItem.isTaxable());
		qisItemResponse.setDiscountable(qisItem.isDiscountable());
		qisItemResponse.setOnMenu(qisItem.isOnMenu());
		qisItemResponse.setServiceRequest(itemRequest.getServiceRequest());

		qisItemRepository.save(qisItem);

		qisLogService.info(authUser.getId(), QisItemController.class.getSimpleName(), "CREATE",
				qisItemResponse.toString(), qisItem.getId(), CATEGORY);

		return qisItem;
	}

	// READ ITEM
	@GetMapping("item/{itemId}")
	public QisItem getItem(@PathVariable String itemId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-View Item:" + itemId);
		QisItem qisItem = qisItemRepository.findByItemid(itemId);
		if (qisItem == null) {
			throw new RuntimeException("Record not found.");
		}

		return qisItem;
	}

	// UPDATE ITEM
	@PutMapping("item/{itemId}")
	public QisItem updateItem(@PathVariable String itemId, @Valid @RequestBody QisItemRequest itemRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Update Item:" + itemId);
		QisItem qisItem = qisItemRepository.findByItemid(itemId);
		if (qisItem == null) {
			throw new RuntimeException("Record not found.");
		}

		if (appUtility.getLaboratoryName(itemRequest.getItemLaboratory()) == null)
			throw new RuntimeException(
					"Fail! -> Cause: Laboratory Code[" + itemRequest.getItemLaboratory() + "] not found.",
					new Throwable(
							"itemLaboratory: Laboratory Code[" + itemRequest.getItemLaboratory() + "] not found."));
		if (appUtility.getCategoryName(itemRequest.getItemCategory()) == null)
			throw new RuntimeException(
					"Fail! -> Cause: Category Code[" + itemRequest.getItemCategory() + "] not found.",
					new Throwable("itemCategory: Category Code[" + itemRequest.getItemCategory() + "] not found."));

		if (itemRequest.getItemCategory().equals("LAB")) {
			if (itemRequest.getItemLaboratoryProcedure() == null)
				throw new RuntimeException("Validation Failed [Item Laboratory Procedure is required.].",
						new Throwable("itemLaboratoryProcedure: Item Laboratory Procedure is required."));
			else if (appUtility.getLaboratoryProcedure(itemRequest.getItemLaboratoryProcedure()) == null)
				throw new RuntimeException("Fail! -> Cause: Laboratory Procedure Code["
						+ itemRequest.getItemLaboratoryProcedure() + "] not found.");
		}

		if (qisItemRepository.findItemNameOnOtherItem(itemRequest.getItemName(), qisItem.getId()) != null)
			throw new RuntimeException("Duplicate item name.", new Throwable("itemName: Duplicate item name."));

		if (itemRequest.getItemCategory().equals("LAB")) {
			if (itemRequest.getItemLaboratoryProcedure() == null)
				throw new RuntimeException("Validation Failed [Item Laboratory Procedure is required.].",
						new Throwable("itemLaboratoryProcedure: Item Laboratory Procedure is required."));
			else if (appUtility.getLaboratoryProcedure(itemRequest.getItemLaboratoryProcedure()) == null)
				throw new RuntimeException(
						"Fail! -> Cause: Laboratory Procedure Code[" + itemRequest.getItemLaboratoryProcedure()
								+ "] not found.",
						new Throwable("itemLaboratoryProcedure: Laboratory Procedure Code["
								+ itemRequest.getItemLaboratoryProcedure() + "] not found."));

			switch (itemRequest.getItemLaboratoryProcedure()) {
			case "CM":
			case "HE":
			case "MB":
			case "CH":
			case "SE": {
				if (itemRequest.getServiceRequest() == null) {
					throw new RuntimeException("Fail! -> Cause: Laboratory Services is required.",
							new Throwable("serviceRequest: Laboratory Services is required."));
				} else if (itemRequest.getServiceRequest().isEmpty()) {
					throw new RuntimeException("Fail! -> Cause: Laboratory Services is empty.",
							new Throwable("serviceRequest: Laboratory Services is empty."));
				}
			}
				break;
			default: {
				itemRequest.setServiceRequest(null);
			}
				break;
			}
		}

		Double itemPrice = appUtility.parseDoubleAmount(itemRequest.getItemPrice());
		if (itemPrice == null) {
			throw new RuntimeException("Invalid Item Price.", new Throwable("itemPrice: Invalid Item Price."));
		}

		boolean isUpdate = false;
		String updateData = "";
		if (!qisItem.getItemName().equals(itemRequest.getItemName())) {
			updateData = appUtility.formatUpdateData(updateData, "Item Name", qisItem.getItemName(),
					itemRequest.getItemName());
			isUpdate = true;
			qisItem.setItemName(itemRequest.getItemName());
		}

		if (!qisItem.getItemDescription().equals(itemRequest.getItemDescription())) {
			updateData = appUtility.formatUpdateData(updateData, "Item Description", qisItem.getItemDescription(),
					itemRequest.getItemDescription());
			isUpdate = true;
			qisItem.setItemDescription(itemRequest.getItemDescription());
		}

		if (!qisItem.getItemPrice().equals(itemPrice)) {
			updateData = appUtility.formatUpdateData(updateData, "Item Price", String.valueOf(qisItem.getItemPrice()),
					String.valueOf(itemPrice));
			isUpdate = true;
			qisItem.setItemPrice(itemPrice);
		}

		if (!qisItem.getItemCategory().equals(itemRequest.getItemCategory())) {
			updateData = appUtility.formatUpdateData(updateData, "Item Category", qisItem.getItemCategory(),
					itemRequest.getItemCategory());
			isUpdate = true;
			qisItem.setItemCategory(itemRequest.getItemCategory());
		}

		if (!qisItem.getItemLaboratory().equals(itemRequest.getItemLaboratory())) {
			updateData = appUtility.formatUpdateData(updateData, "Item Laboratory", qisItem.getItemLaboratory(),
					itemRequest.getItemLaboratory());
			isUpdate = true;
			qisItem.setItemLaboratory(itemRequest.getItemLaboratory());
		}

		if (itemRequest.getItemLaboratoryProcedure() == null || itemRequest.getItemLaboratoryProcedure().equals("")) {
			if (qisItem.getItemLaboratoryProcedure() != null) {
				updateData = appUtility.formatUpdateData(updateData, "Item Laboratory Procedure",
						qisItem.getItemLaboratoryProcedure(), itemRequest.getItemLaboratoryProcedure());
				isUpdate = true;
				qisItem.setItemLaboratoryProcedure(itemRequest.getItemLaboratoryProcedure());
			}
		} else {
			if (qisItem.getItemLaboratoryProcedure() == null) {
				updateData = appUtility.formatUpdateData(updateData, "Item Laboratory Procedure", null,
						itemRequest.getItemLaboratoryProcedure());
				isUpdate = true;
				qisItem.setItemLaboratoryProcedure(itemRequest.getItemLaboratoryProcedure());
			} else if (!qisItem.getItemLaboratoryProcedure().equals(itemRequest.getItemLaboratoryProcedure())) {
				updateData = appUtility.formatUpdateData(updateData, "Item Laboratory Procedure",
						qisItem.getItemLaboratoryProcedure(), itemRequest.getItemLaboratoryProcedure());
				isUpdate = true;
				qisItem.setItemLaboratoryProcedure(itemRequest.getItemLaboratoryProcedure());
			}
		}

		if (itemRequest.getServiceRequest() != null) {
			Set<QisLaboratoryProcedureService> services = getLaboratoryServices(itemRequest.getServiceRequest(),
					itemRequest.getItemLaboratoryProcedure());
			Set<String> changeService = getChangeItemLaboratoryServices(qisItem.getServiceRequest(), services);
			if (!changeService.isEmpty()) {
				isUpdate = true;
				qisItem.setServiceRequest(services);
				updateData = appUtility.addToFormatedData(updateData, "services:", String.valueOf(changeService));
			}
		} else {
			Set<QisLaboratoryProcedureService> services = new HashSet<>();
			Set<String> changeService = getChangeItemLaboratoryServices(qisItem.getServiceRequest(), services);
			if (!changeService.isEmpty()) {
				isUpdate = true;
				qisItem.setServiceRequest(services);
				updateData = appUtility.addToFormatedData(updateData, "services:", String.valueOf(changeService));
			}
		}

		if (itemRequest.isActive() != null && qisItem.isActive() != itemRequest.isActive()) {
			updateData = appUtility.formatUpdateData(updateData, "Active", String.valueOf(qisItem.isActive()),
					String.valueOf(itemRequest.isActive()));
			isUpdate = true;
			qisItem.setActive(itemRequest.isActive());
		}

		if (itemRequest.isTaxable() != null && qisItem.isTaxable() != itemRequest.isTaxable()) {
			updateData = appUtility.formatUpdateData(updateData, "Taxable", String.valueOf(qisItem.isTaxable()),
					String.valueOf(itemRequest.isTaxable()));
			isUpdate = true;
			qisItem.setTaxable(itemRequest.isTaxable());
		}

		if (itemRequest.isDiscountable() != null && qisItem.isDiscountable() != itemRequest.isDiscountable()) {
			updateData = appUtility.formatUpdateData(updateData, "Discountable",
					String.valueOf(qisItem.isDiscountable()), String.valueOf(itemRequest.isDiscountable()));
			isUpdate = true;
			qisItem.setDiscountable(itemRequest.isDiscountable());
		}

		if (itemRequest.getSpecificTest() != null && qisItem.getSpecificTest() != itemRequest.getSpecificTest()) {
			String specificTest = itemRequest.getSpecificTest();
			specificTest = specificTest.substring(0, specificTest.length() - 1);
			updateData = appUtility.formatUpdateData(updateData, "Specific Test",
					String.valueOf(qisItem.getSpecificTest()), String.valueOf(specificTest));
			isUpdate = true;
			qisItem.setSpecificTest(itemRequest.getSpecificTest());
		}

		if (itemRequest.isOnMenu() != null && qisItem.isOnMenu() != itemRequest.isOnMenu()) {
			updateData = appUtility.formatUpdateData(updateData, "OnMenu", String.valueOf(qisItem.isOnMenu()),
					String.valueOf(itemRequest.isOnMenu()));
			isUpdate = true;
			qisItem.setOnMenu(itemRequest.isOnMenu());
		}

		if (isUpdate) {
			qisItem.setUpdatedBy(authUser.getId());
			qisItem.setUpdatedAt(Calendar.getInstance());
			qisItemRepository.save(qisItem);
			qisLogService.warn(authUser.getId(), QisItemController.class.getSimpleName(), "UPDATE", updateData,
					qisItem.getId(), CATEGORY);
		}

		return qisItem;
	}

	// DELETE ITEM
	@DeleteMapping("item/{itemId}")
	public String deleteItem(@PathVariable String itemId, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.warn(authUser.getId() + "-Delete Item:" + itemId);
		return "not implemented";
	}

	// LIST ALL ITEMS
	@GetMapping("items")
	public Page<QisItem> getAllItems(Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info("List of Items");
		return qisItemRepository.findAll(pageable);
	}

	// LIST ALL ACTIVE ITEMS
	@GetMapping("items/active")
	public Page<QisItem> getAllActoveItems(Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info("List of active Items");
		return qisItemRepository.allActiveItems(pageable);
	}

	// SEARCH ITEM
	@GetMapping("items/search")
	public Page<QisItem> searchItems(@RequestParam String searchKey, Pageable pageable,
			@AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info("Search of Items");
		return qisItemRepository.searchItems(searchKey, pageable);
	}

	// LIST ALL ITEM LABORATORIES
	@GetMapping("item_laboratories")
	public List<QisOptionResponse> getAllItemLaboratoties() {
		Map<String, String> labs = QisLaboratoryName.getLaboratories();

		List<QisOptionResponse> list = new ArrayList<QisOptionResponse>();
		for (Map.Entry<String, String> entry : labs.entrySet()) {
			list.add(new QisOptionResponse(entry.getKey(), entry.getValue()));
		}

		return list;
	}

	// LIST ALL ITEM CATEGORIES
	@GetMapping("item_categories")
	public List<QisOptionResponse> getAllItemCategories() {
		Map<String, String> cats = QisCategoryName.getCategories();

		List<QisOptionResponse> list = new ArrayList<QisOptionResponse>();
		for (Map.Entry<String, String> entry : cats.entrySet()) {
			list.add(new QisOptionResponse(entry.getKey(), entry.getValue()));
		}

		return list;
	}

	// LIST ALL ITEM CATEGORIES
	@GetMapping("item_laboratory_procedures")
	public List<QisOptionResponse> getAllItemLaboratoryProcedures() {
		Map<String, String> pros = QisLaboratoryProcedure.getProcedures();

		List<QisOptionResponse> list = new ArrayList<QisOptionResponse>();
		for (Map.Entry<String, String> entry : pros.entrySet()) {
			list.add(new QisOptionResponse(entry.getKey(), entry.getValue()));
		}

		return list;
	}

	// LIST ALL LABORATORY SERVICES
	@GetMapping("item_laboratory_services")
	public Map<String, List<QisOptionResponse>> getAllLaboratoryServices() {
		Map<String, List<QisOptionResponse>> responseMap = new HashMap<>();

		List<QisLaboratoryService> services = qisLaboratoryServiceRepository.findAll();
		for (QisLaboratoryService service : services) {
			List<QisOptionResponse> list = null;
			if (responseMap.containsKey(service.getLaboratoryProcedure())) {
				list = responseMap.get(service.getLaboratoryProcedure());
			} else {
				list = new ArrayList<QisOptionResponse>();
			}

			list.add(new QisOptionResponse(service.getLaboratoryRequest(),
					QisLaboratoryRequest.valueOf(service.getLaboratoryRequest()).getServiceRequest()));
			responseMap.put(service.getLaboratoryProcedure(), list);
		}

		return responseMap;
	}

	// LIST ALL CLASSIFICATIONS
	@GetMapping("classification_list")
	public List<QisOptionResponse> getAllClassifications() {
		Map<String, String> pros = QisClassifications.getClassTypeLists();

		List<QisOptionResponse> list = new ArrayList<QisOptionResponse>();
		for (Map.Entry<String, String> entry : pros.entrySet()) {
			list.add(new QisOptionResponse(entry.getKey(), entry.getValue()));
		}

		return list;
	}

	// LIST ALL LABORATORY SERVICES BY PROCEDURE
	@GetMapping("item_laboratory_services/{procedure}")
	public List<QisOptionResponse> getLaboratoryServicesByProcedure(@PathVariable String procedure) {
		List<QisOptionResponse> list = new ArrayList<QisOptionResponse>();

		List<QisLaboratoryService> services = qisLaboratoryServiceRepository.getLaboratoryServiceByProcedure(procedure);
		for (QisLaboratoryService service : services) {
			list.add(new QisOptionResponse(service.getLaboratoryRequest(),
					QisLaboratoryRequest.valueOf(service.getLaboratoryRequest()).getServiceRequest()));
		}
		return list;
	}

	private Set<QisLaboratoryProcedureService> getLaboratoryServices(Set<String> strRequests, String procedure) {
		Set<QisLaboratoryProcedureService> services = new HashSet<>();

		if (strRequests != null) {
			strRequests.forEach(req -> {
				if (appUtility.getLaboratoryServices(req) == null)
					throw new RuntimeException("Fail! -> Cause: Service Request Code[" + req + "] not found.",
							new Throwable("serviceRequest: Service Request Code[" + req + "] not found."));

				QisLaboratoryProcedureService service = qisLaboratoryProcedureServiceRepository
						.getLaboratoryProcedureServiceByProcedureServiceRequest(
								QisLaboratoryProcedure.valueOf(procedure), QisLaboratoryRequest.valueOf(req))
						.orElseThrow(
								() -> new RuntimeException("Fail! -> Cause: Service Request [" + req + "] not find.",
										new Throwable("serviceRequest: Service Request [" + req + "] not found.")));
				services.add(service);
			});
		}

		return services;
	}

	private Set<String> getChangeItemLaboratoryServices(Set<QisLaboratoryProcedureService> currentServices,
			Set<QisLaboratoryProcedureService> newServices) {
		Set<String> changes = new HashSet<>();

		Set<String> added = new HashSet<>();
		Set<String> removed = new HashSet<>();
		Set<String> remained = new HashSet<>();
		if (currentServices != null && !currentServices.isEmpty()) {
			currentServices.forEach(srv -> {
				String strSrv = srv.getLaboratoryRequest().name();
				if (strSrv != null) {
					if (!isObjectInSet(srv, newServices)) {
						removed.add(strSrv);
					} else {
						remained.add(strSrv);
					}
				}
			});
		}

		if (newServices != null && !newServices.isEmpty()) {
			newServices.forEach(srv -> {
				String strSrv = srv.getLaboratoryRequest().name();
				if (strSrv != null) {
					if (!isObjectInSet(srv, currentServices)) {
						added.add(strSrv);
					} else {
						remained.add(strSrv);
					}
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

	private boolean isObjectInSet(QisLaboratoryProcedureService object, Set<QisLaboratoryProcedureService> set) {
		boolean result = false;

		if (set != null && !set.isEmpty()) {
			for (QisLaboratoryProcedureService o : set) {
				if (o == object) {
					result = true;
					break;
				}
			}
		}

		return result;
	}
}
