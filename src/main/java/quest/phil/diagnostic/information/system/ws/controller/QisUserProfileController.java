package quest.phil.diagnostic.information.system.ws.controller;

import java.io.InputStream;
import java.util.Calendar;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserProfile;
import quest.phil.diagnostic.information.system.ws.model.request.QisUserProfileRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisUserInfoRespository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserProfileRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisUserProfileController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisUserProfileController.class);
	private final String CATEGORY = "USER PROFILES";

	@Autowired
	private QisUserInfoRespository qisUserInfoRespository;

	@Autowired
	private QisUserProfileRepository qisUserProfileRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	// LIST ALL USERS
	@GetMapping("users_info")
	@PreAuthorize("hasRole('ADMIN')")
	public Page<QisUserInfo> getAllUsersInfo(Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.info("List of User Info");
		qisLogService.info(authUser.getId(), QisUserProfileController.class.getSimpleName(), "VIEW", "ALL", CATEGORY);
		return qisUserInfoRespository.findAll(pageable);
	}

	@PostMapping("/user/{userId}/profile")
	public QisUserInfo saveUserProfile(@PathVariable String userId,
			@Valid @RequestBody QisUserProfileRequest profileRequest, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {

		LOGGER.info(authUser.getId() + "-Save User Profile:" + userId);
		QisUserInfo qisUserInfo = qisUserInfoRespository.findByUserid(userId);
		if (qisUserInfo == null) {
			throw new RuntimeException("Record not found.", new Throwable("userId: Record not found."));
		}

		Calendar dob = appUtility.stringToCalendarDate(profileRequest.getDateOfBirth(), "yyyy-MM-dd");
		if (dob == null) {
			throw new RuntimeException("Invalid date of birth.", new Throwable("dateOfBirth: Invalid date of birth."));
		}

		if (profileRequest.getGender() == null)
			throw new RuntimeException("Invalid Gender input ('M'-Male, 'F'-Female)",
					new Throwable("gender: Invalid Gender input ('M'-Male, 'F'-Female)."));

		String action = "ADDED";
		boolean isAddedProfile = false;
		String profileLogMsg = "";

		QisUserProfile profile = qisUserProfileRepository.getUserProfileByUserId(qisUserInfo.getId());
		if (profile == null) {
			profile = new QisUserProfile();
			BeanUtils.copyProperties(profileRequest, profile);
			profile.setId(qisUserInfo.getId());
			profile.setDateOfBirth(dob);
			profile.setCreatedBy(authUser.getId());
			profile.setUpdatedBy(authUser.getId());
			profileLogMsg = profileRequest.toString();
			isAddedProfile = true;
		} else {
			action = "UPDATED";
			profile.setUpdatedBy(authUser.getId());
			profile.setUpdatedAt(Calendar.getInstance());

			if (!profile.getFirstname().equals(profileRequest.getFirstname())) {
				profileLogMsg = appUtility.formatUpdateData(profileLogMsg, "Firstname", profile.getFirstname(),
						profileRequest.getFirstname());
				profile.setFirstname(profileRequest.getFirstname());
			}

			if (!profile.getLastname().equals(profileRequest.getLastname())) {
				profileLogMsg = appUtility.formatUpdateData(profileLogMsg, "Lastname", profile.getLastname(),
						profileRequest.getLastname());
				profile.setLastname(profileRequest.getLastname());
			}

			if (profileRequest.getMiddlename() != null && (profile.getMiddlename() == null
					|| !profile.getMiddlename().equals(profileRequest.getMiddlename()))) {
				profileLogMsg = appUtility.formatUpdateData(profileLogMsg, "Middlename", profile.getMiddlename(),
						profileRequest.getMiddlename());
				profile.setMiddlename(profileRequest.getMiddlename());
			}

			if (!profileRequest.getDateOfBirth().equals(appUtility.appShortDate(profile.getDateOfBirth()))) {
				profileLogMsg = appUtility.formatUpdateData(profileLogMsg, "BirthDate",
						appUtility.appShortDate(profile.getDateOfBirth()), profileRequest.getDateOfBirth());
				profile.setDateOfBirth(appUtility.stringToCalendarDate(profileRequest.getDateOfBirth(), "yyyy-MM-dd"));
			}

			if (profileRequest.getContactNumber() != null && (profile.getContactNumber() == null
					|| !profile.getContactNumber().equals(profileRequest.getContactNumber()))) {
				profileLogMsg = appUtility.formatUpdateData(profileLogMsg, "Contact Number", profile.getContactNumber(),
						profileRequest.getContactNumber());
				profile.setContactNumber(profileRequest.getContactNumber());
			}

			if (profileRequest.getEmail() != null
					&& (profile.getEmail() == null || !profile.getEmail().equals(profileRequest.getEmail()))) {
				profileLogMsg = appUtility.formatUpdateData(profileLogMsg, "Email", profile.getEmail(),
						profileRequest.getEmail());
				profile.setEmail(profileRequest.getEmail());
			}

			if (!profile.getGender().equals(profileRequest.getGender())) {
				profileLogMsg = appUtility.formatUpdateData(profileLogMsg, "Gender",
						String.valueOf(profile.getGender()), String.valueOf(profileRequest.getGender()));
				profile.setGender(profileRequest.getGender());
			}

			if (profileRequest.getAddress() != null
					&& (profile.getAddress() == null || !profile.getAddress().equals(profileRequest.getAddress()))) {
				profileLogMsg = appUtility.formatUpdateData(profileLogMsg, "Address", profile.getAddress(),
						profileRequest.getAddress());
				profile.setAddress(profileRequest.getAddress());
			}

			if (profileRequest.getLicenseNumber() != null) {
				if (profile.getLicenseNumber() == null
						|| !profile.getLicenseNumber().equals(profileRequest.getLicenseNumber())) {
					profileLogMsg = appUtility.formatUpdateData(profileLogMsg, "License Number",
							profile.getLicenseNumber(), profileRequest.getLicenseNumber());
					profile.setLicenseNumber(profileRequest.getLicenseNumber());
				}
			} else {
				if (profile.getLicenseNumber() != null) {
					profileLogMsg = appUtility.formatUpdateData(profileLogMsg, "License Number",
							profile.getLicenseNumber(), null);
					profile.setLicenseNumber(null);
				}
			}

			if (profileRequest.getSuffix() != null && (!profile.getSuffix().equals(profileRequest.getSuffix()))) {
				profileLogMsg = appUtility.formatUpdateData(profileLogMsg, "Suffix", profile.getSuffix(),
						profileRequest.getSuffix());
				profile.setSuffix(profileRequest.getSuffix());
			}
		}

		if (!"".equals(profileLogMsg)) {
			qisUserProfileRepository.save(profile);
			qisLogService.info(authUser.getId(), QisUserProfileController.class.getSimpleName(), action, profileLogMsg,
					qisUserInfo.getId(), CATEGORY);
		}

		if (isAddedProfile) {
			qisUserInfo.setProfile(profile);
		}

		return qisUserInfo;
	}

	@PutMapping("/user/{userId}/upload_signature")
	public QisUserInfo uploadUserSignature(@PathVariable String userId,
			@RequestParam(name = "uploadFile", required = false) MultipartFile uploadFile,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Upload User Signature:" + userId);
		QisUserInfo qisUserInfo = qisUserInfoRespository.findByUserid(userId);
		if (qisUserInfo == null) {
			throw new RuntimeException("Record not found.", new Throwable("userId: Record not found."));
		}

		QisUserProfile profile = qisUserProfileRepository.getUserProfileByUserId(qisUserInfo.getId());
		if (profile == null) {
			throw new RuntimeException("Please update user profile first.",
					new Throwable("userId: Please update user profile first."));
		}

		if (uploadFile != null) {
			if (uploadFile.getSize() > 64000) {
				throw new RuntimeException("File size should not exceed to 64Kb.",
						new Throwable("File size should not exceed to 64Kb."));
			}
			InputStream inputStream = uploadFile.getInputStream();
			byte[] signature = StreamUtils.copyToByteArray(inputStream);
			profile.setSignature(signature);
			profile.setUpdatedBy(authUser.getId());
			profile.setUpdatedAt(Calendar.getInstance());
			qisUserProfileRepository.save(profile);
			qisLogService.warn(authUser.getId(), QisUserProfileController.class.getSimpleName(), "UPDATE", "SIGNATURE",
					profile.getId(), CATEGORY);
		} else {
			if (profile.getSignature() != null) {
				profile.setSignature(null);
				profile.setUpdatedBy(authUser.getId());
				profile.setUpdatedAt(Calendar.getInstance());
				qisUserProfileRepository.save(profile);
				qisLogService.warn(authUser.getId(), QisUserProfileController.class.getSimpleName(), "UPDATE",
						"REMOVE SIGNATURE", profile.getId(), CATEGORY);
			}
		}

		return qisUserInfo;
	}

	@GetMapping("/user/{userId}/profile")
	public QisUserInfo getUserProfile(@PathVariable String userId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {

		LOGGER.info(authUser.getId() + "-View User Profile:" + userId);
		QisUserInfo qisUserInfo = qisUserInfoRespository.findByUserid(userId);
		if (qisUserInfo == null) {
			throw new RuntimeException("Record not found.");
		}

		return qisUserInfo;
	}
}
