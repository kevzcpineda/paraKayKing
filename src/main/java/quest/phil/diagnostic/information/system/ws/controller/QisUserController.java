package quest.phil.diagnostic.information.system.ws.controller;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.QisRoleName;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.*;
import quest.phil.diagnostic.information.system.ws.model.request.QisAddUserRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisAuthorizationRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisResetPasswordRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisUpdateUserRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisUserResponse;
import quest.phil.diagnostic.information.system.ws.model.response.ReturnResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisUserRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserRoleRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisRoleRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisUserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisUserController.class);
	private final String CATEGORY = "USERS";

	@Autowired
	private QisUserRepository qisUserRepository;

	@Autowired
	private QisRoleRepository qisRoleRepository;

	@Autowired
	private QisUserRoleRepository qisUserRoleRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	PasswordEncoder encoder;

	// CREATE USER
	@PostMapping("user")
	@PreAuthorize("hasRole('ADMIN')")
	public QisUser createUser(@Valid @RequestBody QisAddUserRequest userRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Create User");
		String userId = appUtility.generateUserId(12);
		if (qisUserRepository.existsByEmail(userRequest.getEmail()))
			throw new RuntimeException("Duplicate email.", new Throwable("email: Duplicate email."));
		if (qisUserRepository.existsByUsername(userRequest.getUsername()))
			throw new RuntimeException("Duplicate username.", new Throwable("username: Duplicate username."));
		if (qisUserRepository.existsByUserid(userId))
			throw new RuntimeException("Duplicate user id.", new Throwable("userid: Duplicate user id."));

		QisUser qisUser = new QisUser();
		BeanUtils.copyProperties(userRequest, qisUser);

		qisUser.setUserid(userId);
		qisUser.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));

		Set<QisRole> roles = getUserRoles(userRequest.getRoles());
		qisUser.setRoles(roles);
		qisUser.setCreatedBy(authUser.getId());
		qisUser.setUpdatedBy(authUser.getId());

		QisUserResponse qisUserResponse = new QisUserResponse();
		BeanUtils.copyProperties(qisUser, qisUserResponse);
		qisUserResponse.setRoles(appUtility.getStrUserRoles(qisUser.getRoles()));

		qisUserRepository.save(qisUser);

		qisLogService.info(authUser.getId(), QisUserController.class.getSimpleName(), "CREATE",
				qisUserResponse.toString(), qisUser.getId(), CATEGORY);

		return qisUser;
	}

	// READ USER
	@GetMapping("user/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public QisUser getUser(@PathVariable String userId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-View User:" + userId);
		QisUser qisUser = qisUserRepository.findByUserid(userId);
		if (qisUser == null) {
			throw new RuntimeException("Record not found.", new Throwable("userId: Record not found."));
		}

		return qisUser;
	}

	// UPDATE USER
	@PutMapping("user/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public QisUser updateUser(@PathVariable String userId, @Valid @RequestBody QisUpdateUserRequest userRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.warn(authUser.getId() + "-Update User:" + userId);
		QisUser qisUser = qisUserRepository.findByUserid(userId);
		if (qisUser == null) {
			throw new RuntimeException("Record not found.", new Throwable("userId: Record not found."));
		}

		if (qisUserRepository.findEmailOnOtherUser(userRequest.getEmail(), qisUser.getId()) != null)
			throw new RuntimeException("Duplicate email.", new Throwable("email: Duplicate email."));

		if (qisUserRepository.findUsernameOnOtherUser(userRequest.getUsername(), qisUser.getId()) != null)
			throw new RuntimeException("Duplicate username.", new Throwable("username: Duplicate email."));

		boolean isUpdate = false;
		String updateData = "";
		if (!qisUser.getEmail().equals(userRequest.getEmail())) {
			updateData = appUtility.formatUpdateData(updateData, "Email", qisUser.getEmail(), userRequest.getEmail());
			isUpdate = true;
			qisUser.setEmail(userRequest.getEmail());
		}

		if (!qisUser.getUsername().equals(userRequest.getUsername())) {
			updateData = appUtility.formatUpdateData(updateData, "Username", qisUser.getUsername(),
					userRequest.getUsername());
			isUpdate = true;
			qisUser.setUsername(userRequest.getUsername());
		}

		if (userRequest.getPassword() != null && !"".equals(userRequest.getPassword().trim())) {
			isUpdate = true;
			updateData = appUtility.addToFormatedData(updateData, "Password", "[Updated]");
			qisUser.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
		}

		if (userRequest.isActive() != null && qisUser.isActive() != userRequest.isActive()) {
			updateData = appUtility.formatUpdateData(updateData, "Active", String.valueOf(qisUser.isActive()),
					String.valueOf(userRequest.isActive()));
			isUpdate = true;
			qisUser.setActive(userRequest.isActive());
		}

		if (userRequest.getRoles() != null) {
			Set<QisRole> roles = getUserRoles(userRequest.getRoles());
			Set<String> changeRoles = getChangeUserRoles(qisUser.getRoles(), roles);

			if (!changeRoles.isEmpty()) {
				isUpdate = true;
				qisUser.setRoles(roles);
				updateData = appUtility.addToFormatedData(updateData, "roles:", String.valueOf(changeRoles));
			}
		}

		if (isUpdate) {
			qisUser.setUpdatedBy(authUser.getId());
			qisUser.setUpdatedAt(Calendar.getInstance());
			qisUserRepository.save(qisUser);
			qisLogService.warn(authUser.getId(), QisUserController.class.getSimpleName(), "UPDATE", updateData,
					qisUser.getId(), CATEGORY);
		}

		return qisUser;
	}

	// DELETE USER
	@DeleteMapping("user/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteUser(@PathVariable String userId, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.warn(authUser.getId() + "-Delete User:" + userId);
		return "not implemented";
	}

	// LIST ALL USERS
	@GetMapping("users")
	@PreAuthorize("hasRole('ADMIN')")
	public Page<QisUser> getAllUsers(Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info("List of User");
		qisLogService.info(authUser.getId(), QisUserController.class.getSimpleName(), "VIEW", "ALL", CATEGORY);
		return qisUserRepository.findAll(pageable);
	}

	// UPDATE USER PASSWORD
	@PutMapping("reset_password/{userId}")
	public ResponseEntity<?> resetPassword(@PathVariable String userId,
			@Valid @RequestBody QisResetPasswordRequest userReset, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.warn("Reset User Password:" + userId);
		QisUser qisUser = qisUserRepository.findByUserid(userId);

		qisUser.setPassword(bCryptPasswordEncoder.encode(userReset.getPassword()));
		qisUser.setUpdatedBy(authUser.getId());
		qisUser.setUpdatedAt(Calendar.getInstance());
		qisUserRepository.save(qisUser);
		qisLogService.warn(authUser.getId(), QisUserController.class.getSimpleName(), "RESET", "User Password",
				qisUser.getId(), CATEGORY);

		return ResponseEntity.ok(new ReturnResponse("success", "password reset"));
	}

	// AUTHORIZATION
	@PostMapping("authorize")
	public ResponseEntity<?> getAUthorization(@Valid @RequestBody QisAuthorizationRequest auth,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.warn(authUser.getId() + "-Authorization");

		QisUser authorize = qisUserRepository.findByUsername(auth.getUsername());
		if (authorize != null && authorize.isActive() && encoder.matches(auth.getPassword(), authorize.getPassword())) {
			Set<String> roles = appUtility.getStrUserRoles(authorize.getRoles());
			if (roles.contains("ADMIN") || roles.contains("AUDITOR")) {
				return ResponseEntity.ok(new ReturnResponse("authorize", authorize.getUserid()));
			}
		}

		throw new RuntimeException("Unauthorize account.", new Throwable("Unauthorize account."));
	}


	// GET USER MENUS
	@GetMapping("user/{userId}/menus")
	public Set<Menu> getUserMenus(@PathVariable String userId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-Get User Menu:" + userId);
		QisUser qisUser = qisUserRepository.findByUserid(userId);
		if (qisUser == null) {
			throw new RuntimeException("Record not found.", new Throwable("userId: Record not found."));
		}

		Set<Menu> menus = new HashSet<>();
		Set<String> roles = appUtility.getStrUserRoles(qisUser.getRoles());
		if (!roles.contains("ADMIN")) {
			for (QisRole role : qisUser.getRoles()) {
				QisUserRole userRole = qisUserRoleRepository.findByRoleId(role.getId());
				Set<Menu> urMenus = userRole.getMenus();
				for (Menu m : urMenus) {
					if (m.getIsActive() && !menus.contains(m)) {
						menus.add(m);
					}
				}
			}
		}

		return menus;
	}

	private Set<QisRole> getUserRoles(Set<String> strRoles) {
		Set<QisRole> roles = new HashSet<>();

		if (strRoles != null) {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					QisRole adminRole = qisRoleRepository.findByName(QisRoleName.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Admin Role not find."));
					roles.add(adminRole);
					break;

				case "cashier":
					QisRole cashierRole = qisRoleRepository.findByName(QisRoleName.ROLE_CASHIER)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Cashier Role not find."));
					roles.add(cashierRole);
					break;

				case "technician":
					QisRole technitianRole = qisRoleRepository.findByName(QisRoleName.ROLE_TECHNICIAN)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Technician Role not find."));
					roles.add(technitianRole);
					break;

				case "doctor":
					QisRole doctorRole = qisRoleRepository.findByName(QisRoleName.ROLE_DOCTOR)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Doctor Role not find."));
					roles.add(doctorRole);
					break;

				case "secretary":
					QisRole secretaryRole = qisRoleRepository.findByName(QisRoleName.ROLE_SECRETARY)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Secretary Role not find."));
					roles.add(secretaryRole);
					break;

				case "nurse":
					QisRole nurseRole = qisRoleRepository.findByName(QisRoleName.ROLE_NURSE)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Nurse Role not find."));
					roles.add(nurseRole);
					break;

				case "auditor":
					QisRole auditorRole = qisRoleRepository.findByName(QisRoleName.ROLE_AUDITOR)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Auditor not find."));
					roles.add(auditorRole);
					break;

				default:
					QisRole userRole = qisRoleRepository.findByName(QisRoleName.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
					roles.add(userRole);
					break;
				}
			});
		}
		return roles;
	}

	private Set<String> getChangeUserRoles(Set<QisRole> currentRoles, Set<QisRole> newRoles) {
		Set<String> changes = new HashSet<>();

		Set<String> added = new HashSet<>();
		Set<String> removed = new HashSet<>();
		Set<String> remained = new HashSet<>();
		if (currentRoles != null && !currentRoles.isEmpty()) {
			currentRoles.forEach(role -> {
				String strRole = role.getName().name();
				if (strRole != null && strRole.startsWith("ROLE_")) {
					if (!isObjectInSet(role, newRoles)) {
						removed.add(strRole.substring(5));
					} else {
						remained.add(strRole.substring(5));
					}
				}
			});
		}

		if (newRoles != null && !newRoles.isEmpty()) {
			newRoles.forEach(role -> {
				String strRole = role.getName().name();
				if (strRole != null && strRole.startsWith("ROLE_")) {
					if (!isObjectInSet(role, currentRoles)) {
						added.add(strRole.substring(5));
					} else {
						remained.add(strRole.substring(5));
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

	private boolean isObjectInSet(QisRole object, Set<QisRole> set) {
		boolean result = false;

		if (set != null && !set.isEmpty()) {
			for (QisRole o : set) {
				if (o == object) {
					result = true;
					break;
				}
			}
		}

		return result;
	}
}
