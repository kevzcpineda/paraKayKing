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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferral;
import quest.phil.diagnostic.information.system.ws.model.request.QisReferralRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisReferralRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisReferralController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisReferralController.class);
	private final String CATEGORY = "REFERRAL";

	@Autowired
	private QisReferralRepository qisReferralRepository;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	// CREATE REFERRAL
	@PostMapping("referral")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisReferral createReferral(@Valid @RequestBody QisReferralRequest referralRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Create Referral");

		String referralId = appUtility.generateUserId(6);
		if (qisReferralRepository.existsByReferralid(referralId))
			throw new RuntimeException("Duplicate referral id.", new Throwable("referralid: Duplicate referral id."));

		QisReferral qisReferral = new QisReferral();
		BeanUtils.copyProperties(referralRequest, qisReferral);

		if (referralRequest.isActive() == null) {
			qisReferral.setActive(true);
		} else {
			qisReferral.setActive(referralRequest.isActive());
		}
		qisReferral.setReferralid(referralId);
		qisReferral.setCreatedBy(authUser.getId());
		qisReferral.setUpdatedBy(authUser.getId());

		qisReferralRepository.save(qisReferral);

		qisLogService.info(authUser.getId(), QisReferralController.class.getSimpleName(), "CREATE",
				referralRequest.toString(), qisReferral.getId(), CATEGORY);

		return qisReferral;
	}

	// READ REFERRAL
	@GetMapping("referral/{referralId}")
	public QisReferral getReferral(@PathVariable String referralId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-View Referral:" + referralId);
		QisReferral qisReferral = qisReferralRepository.findByReferralid(referralId);
		if (qisReferral == null) {
			throw new RuntimeException("Record not found.");
		}

		return qisReferral;
	}

	// UPDATE DOCTOR
	@PutMapping("referral/{referralId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisReferral updateDoctor(@PathVariable String referralId,
			@Valid @RequestBody QisReferralRequest referralRequest, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-Update Referral:" + referralId);
		QisReferral qisReferral = qisReferralRepository.findByReferralid(referralId);
		if (qisReferral == null) {
			throw new RuntimeException("Record not found.");
		}

		boolean isUpdate = false;
		String updateData = "";
		if (!qisReferral.getReferral().equals(referralRequest.getReferral())) {
			updateData = appUtility.formatUpdateData(updateData, "Referral", qisReferral.getReferral(),
					referralRequest.getReferral());
			isUpdate = true;
			qisReferral.setReferral(referralRequest.getReferral());
		}

		if (referralRequest.isActive() != null && qisReferral.isActive() != referralRequest.isActive()) {
			updateData = appUtility.formatUpdateData(updateData, "Active", String.valueOf(qisReferral.isActive()),
					String.valueOf(referralRequest.isActive()));
			isUpdate = true;
			qisReferral.setActive(referralRequest.isActive());
		}

		if (isUpdate) {
			qisReferral.setUpdatedBy(authUser.getId());
			qisReferral.setUpdatedAt(Calendar.getInstance());
			qisReferralRepository.save(qisReferral);
			qisLogService.warn(authUser.getId(), QisReferralController.class.getSimpleName(), "UPDATE", updateData,
					qisReferral.getId(), CATEGORY);
		}

		return qisReferral;
	}

	// DELETE DOCTOR
	@DeleteMapping("referral/{referralId}")
	public String deleteReferral(@PathVariable String referralId, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.warn(authUser.getId() + "-Delete Referral:" + referralId);
		return "not implemented";
	}

	// LIST ALL REFERRALS
	@GetMapping("referrals")
	public List<QisReferral> getAllReferrals() {
		return qisReferralRepository.findAll();
	}

	// LIST ALL REFERRALS
	@GetMapping("referrals/active")
	public List<QisReferral> getAllActiveReferrals() {
		return qisReferralRepository.allActiveItems();
	}

}
