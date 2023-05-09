package quest.phil.diagnostic.information.system.ws.controller;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import quest.phil.diagnostic.information.system.ws.model.entity.Menu;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserRole;
import quest.phil.diagnostic.information.system.ws.model.request.QisUserRoleMenuRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisUserRoleRequest;
import quest.phil.diagnostic.information.system.ws.repository.MenuRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserRoleRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisUserRoleController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisUserRoleController.class);
	private final String CATEGORY = "USER_ROLES";

	@Autowired
	private QisUserRoleRepository qisUserRoleRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	AppUtility appUtility;

	// CREATE USER ROLE
	@PostMapping("user_role")
	@PreAuthorize("hasRole('ADMIN')")
	public QisUserRole createUserRole(@Valid @RequestBody QisUserRoleRequest roleRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Create User Role");

		if (qisUserRoleRepository.existsByName(roleRequest.getRolename()))
			throw new RuntimeException("Duplicate role name.", new Throwable("rolename: Duplicate role name."));

		QisUserRole role = new QisUserRole();
		role.setName(roleRequest.getRolename());
		role.setCreatedBy(authUser.getId());
		role.setUpdatedBy(authUser.getId());

		qisUserRoleRepository.save(role);

		qisLogService.info(authUser.getId(), QisUserRoleController.class.getSimpleName(), "CREATE",
				roleRequest.toString(), role.getId(), CATEGORY);

		return role;
	}

	// READ USER ROLE
	@GetMapping("user_role/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public QisUserRole getUserRole(@PathVariable Long id, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-View Role:" + id);
		QisUserRole role = qisUserRoleRepository.findByRoleId(id);
		if (role == null) {
			throw new RuntimeException("Record not found.");
		}

		return role;
	}

	// UPDATE USER ROLE
	@PutMapping("user_role/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public QisUserRole updateUserRole(@PathVariable Long id, @Valid @RequestBody QisUserRoleRequest roleRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Update Role:" + id);
		QisUserRole role = qisUserRoleRepository.findByRoleId(id);
		if (role == null) {
			throw new RuntimeException("Record not found.", new Throwable("id: Record not found."));
		}

		if (qisUserRoleRepository.findRoleNameOnOtherRoles(roleRequest.getRolename(), role.getId()) != null) {
			throw new RuntimeException("Duplicate role name.", new Throwable("rolename: Duplicate role name."));
		}

		boolean isUpdate = false;
		String updateData = "";

		if (!role.getName().equals(roleRequest.getRolename())) {
			updateData = appUtility.formatUpdateData(updateData, "Role Name", role.getName(),
					roleRequest.getRolename());
			isUpdate = true;
			role.setName(roleRequest.getRolename());
		}

		if (isUpdate) {
			role.setUpdatedBy(authUser.getId());
			role.setUpdatedAt(Calendar.getInstance());
			qisUserRoleRepository.save(role);
			qisLogService.warn(authUser.getId(), QisUserRoleController.class.getSimpleName(), "UPDATE", updateData,
					role.getId(), CATEGORY);
		}

		return role;
	}

	@PostMapping("user_role/{id}/menu")
	@PreAuthorize("hasRole('ADMIN')")
	public QisUserRole updateUserRoleMenu(@PathVariable Long id, @Valid @RequestBody QisUserRoleMenuRequest menuRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Update Role:" + id);
		QisUserRole role = qisUserRoleRepository.findByRoleId(id);
		if (role == null) {
			throw new RuntimeException("Record not found.", new Throwable("id: Record not found."));
		}

		Set<Menu> menus = getUserRoleMenus(menuRequest.getMenus());
		Set<String> changeMenus = getChangeUserRoleMenus(role.getMenus(), menus);

		String updateData = "";
		if (!changeMenus.isEmpty()) {
			role.setMenus(menus);
			qisUserRoleRepository.save(role);
			updateData = appUtility.addToFormatedData(updateData, "menus:", String.valueOf(changeMenus));

			qisLogService.warn(authUser.getId(), QisUserRoleController.class.getSimpleName(), "UPDATE", updateData,
					role.getId(), CATEGORY);
		}

		return role;
	}

	// LIST ALL USERS
	@GetMapping("user_roles")
	@PreAuthorize("hasRole('ADMIN')")
	public List<QisUserRole> getAllUserRoles() {
		return qisUserRoleRepository.findAll();
	}

	// LIST ALL MENUS
	@GetMapping("menus")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Menu> getAllMenus() {
		return menuRepository.findAll();
	}

	private Set<Menu> getUserRoleMenus(Set<Long> menuList) {
		Set<Menu> menus = new HashSet<>();

		if (menuList != null) {
			menuList.forEach(id -> {
				Menu menu = menuRepository.findByMenuId(id);
				if (menu != null) {
					menus.add(menu);
				}
			});
		}

		return menus;
	}

	private Set<String> getChangeUserRoleMenus(Set<Menu> currentMenus, Set<Menu> newMenus) {
		Set<String> changes = new HashSet<>();

		Set<String> added = new HashSet<>();
		Set<String> removed = new HashSet<>();
		Set<String> remained = new HashSet<>();
		if (currentMenus != null && !currentMenus.isEmpty()) {
			currentMenus.forEach(role -> {
				if (!isObjectInSet(role, newMenus)) {
					removed.add(role.getMenuName());
				} else {
					remained.add(role.getMenuName());
				}
			});
		}

		if (newMenus != null && !newMenus.isEmpty()) {
			newMenus.forEach(role -> {
				if (!isObjectInSet(role, currentMenus)) {
					added.add(role.getMenuName());
				} else {
					remained.add(role.getMenuName());
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

	private boolean isObjectInSet(Menu object, Set<Menu> set) {
		boolean result = false;

		if (set != null && !set.isEmpty()) {
			for (Menu o : set) {
				if (o == object) {
					result = true;
					break;
				}
			}
		}

		return result;
	}
}
