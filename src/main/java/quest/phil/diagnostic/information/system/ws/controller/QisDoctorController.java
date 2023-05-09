package quest.phil.diagnostic.information.system.ws.controller;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.request.QisDoctorRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisDoctorResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisDoctorController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisDoctorController.class);
	private final String CATEGORY = "DOCTOR";

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	// CREATE DOCTOR
	@PostMapping("doctor")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisDoctor createDoctor(@Valid @RequestBody QisDoctorRequest doctorRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Create Doctor");

		String doctorId = appUtility.generateUserId(10);
		if (qisDoctorRepository.existsByLicenseNumber(doctorRequest.getLicenseNumber()))
			throw new RuntimeException("Duplicate license number.",
					new Throwable("licenseNumber: Duplicate license number."));
		if (qisDoctorRepository.existsByDoctorid(doctorId))
			throw new RuntimeException("Duplicate doctor id.", new Throwable("doctorid: Duplicate doctor id."));

		QisDoctor qisDoctor = new QisDoctor();
		BeanUtils.copyProperties(doctorRequest, qisDoctor);

		if (doctorRequest.isActive() == null) {
			qisDoctor.setActive(true);
		} else {
			qisDoctor.setActive(doctorRequest.isActive());
		}
		qisDoctor.setDoctorid(doctorId);
		qisDoctor.setCreatedBy(authUser.getId());
		qisDoctor.setUpdatedBy(authUser.getId());

		int doctorType = 0;
		if (doctorRequest.getDoctorType() != null) {
			doctorType = doctorRequest.getDoctorType();
		}
		qisDoctor.setDoctorType(doctorType);

		QisDoctorResponse qisDoctorResponse = new QisDoctorResponse();
		BeanUtils.copyProperties(qisDoctor, qisDoctorResponse);
		qisDoctorResponse.setActive(qisDoctor.isActive());

		qisDoctorRepository.save(qisDoctor);

		qisLogService.info(authUser.getId(), QisDoctorController.class.getSimpleName(), "CREATE",
				qisDoctorResponse.toString(), qisDoctor.getId(), CATEGORY);

		return qisDoctor;
	}

	// READ DOCTOR
	@GetMapping("doctor/{doctorId}")
	public QisDoctor getDoctor(@PathVariable String doctorId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-View Doctor:" + doctorId);
		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(doctorId);
		if (qisDoctor == null) {
			throw new RuntimeException("Record not found.", new Throwable("doctorId: Record not found."));
		}

		return qisDoctor;
	}

	// UPDATE DOCTOR
	@PutMapping("doctor/{doctorId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisDoctor updateDoctor(@PathVariable String doctorId, @Valid @RequestBody QisDoctorRequest doctorRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Update Doctor:" + doctorId);
		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(doctorId);
		if (qisDoctor == null) {
			throw new RuntimeException("Record not found.", new Throwable("doctorId: Record not found."));
		}

		if (qisDoctorRepository.findLicenseNumberOnOtherDoctor(doctorRequest.getLicenseNumber(),
				qisDoctor.getId()) != null) {
			throw new RuntimeException("Duplicate license number.",
					new Throwable("licenseNumber: Duplicate license number."));
		}

		int doctorType = 0;
		if (doctorRequest.getDoctorType() != null) {
			doctorType = doctorRequest.getDoctorType();
		}

		boolean isUpdate = false;
		String updateData = "";
		if (!qisDoctor.getFirstname().equals(doctorRequest.getFirstname())) {
			updateData = appUtility.formatUpdateData(updateData, "Firstname", qisDoctor.getFirstname(),
					doctorRequest.getFirstname());
			isUpdate = true;
			qisDoctor.setFirstname(doctorRequest.getFirstname());
		}

		if (!qisDoctor.getLastname().equals(doctorRequest.getLastname())) {
			updateData = appUtility.formatUpdateData(updateData, "Lastname", qisDoctor.getLastname(),
					doctorRequest.getLastname());
			isUpdate = true;
			qisDoctor.setLastname(doctorRequest.getLastname());
		}

		if (doctorRequest.getMiddlename() != null && (qisDoctor.getMiddlename() == null
				|| !qisDoctor.getMiddlename().equals(doctorRequest.getMiddlename()))) {
			updateData = appUtility.formatUpdateData(updateData, "Middlename", qisDoctor.getMiddlename(),
					doctorRequest.getMiddlename());
			isUpdate = true;
			qisDoctor.setMiddlename(doctorRequest.getMiddlename());
		}

		if (!qisDoctor.getLicenseNumber().equals(doctorRequest.getLicenseNumber())) {
			updateData = appUtility.formatUpdateData(updateData, "License Number", qisDoctor.getLicenseNumber(),
					doctorRequest.getLicenseNumber());
			isUpdate = true;
			qisDoctor.setLicenseNumber(doctorRequest.getLicenseNumber());
		}

		if (!qisDoctor.getSuffix().equals(doctorRequest.getSuffix())) {
			updateData = appUtility.formatUpdateData(updateData, "Suffix", qisDoctor.getSuffix(),
					doctorRequest.getSuffix());
			isUpdate = true;
			qisDoctor.setSuffix(doctorRequest.getSuffix());
		}

		if (doctorRequest.getSpecialization() != null && (qisDoctor.getSpecialization() == null
				|| !qisDoctor.getSpecialization().equals(doctorRequest.getSpecialization()))) {
			updateData = appUtility.formatUpdateData(updateData, "Specialization", qisDoctor.getSpecialization(),
					doctorRequest.getSpecialization());
			isUpdate = true;
			qisDoctor.setSpecialization(doctorRequest.getSpecialization());
		}

		if (doctorRequest.getContactNumber() != null && (qisDoctor.getContactNumber() == null
				|| !qisDoctor.getContactNumber().equals(doctorRequest.getContactNumber()))) {
			updateData = appUtility.formatUpdateData(updateData, "Contact Number", qisDoctor.getContactNumber(),
					doctorRequest.getContactNumber());
			isUpdate = true;
			qisDoctor.setContactNumber(doctorRequest.getContactNumber());
		}

		if (doctorRequest.getEmail() != null
				&& (qisDoctor.getEmail() == null || !qisDoctor.getEmail().equals(doctorRequest.getEmail()))) {
			updateData = appUtility.formatUpdateData(updateData, "Email", qisDoctor.getContactNumber(),
					doctorRequest.getEmail());
			isUpdate = true;
			qisDoctor.setEmail(doctorRequest.getEmail());
		}

		if (qisDoctor.getDoctorType() != doctorType) {
			updateData = appUtility.formatUpdateData(updateData, "Type", String.valueOf(qisDoctor.getDoctorType()),
					String.valueOf(doctorType));
			isUpdate = true;
			qisDoctor.setDoctorType(doctorType);
		}

		if (doctorRequest.isActive() != null && qisDoctor.isActive() != doctorRequest.isActive()) {
			updateData = appUtility.formatUpdateData(updateData, "Active", String.valueOf(qisDoctor.isActive()),
					String.valueOf(doctorRequest.isActive()));
			isUpdate = true;
			qisDoctor.setActive(doctorRequest.isActive());
		}

		if (isUpdate) {
			qisDoctor.setUpdatedBy(authUser.getId());
			qisDoctor.setUpdatedAt(Calendar.getInstance());
			qisDoctorRepository.save(qisDoctor);
			qisLogService.warn(authUser.getId(), QisDoctorController.class.getSimpleName(), "UPDATE", updateData,
					qisDoctor.getId(), CATEGORY);
		}

		return qisDoctor;
	}

	// UPLOAD DOCTOR SIGNATURE
	@PutMapping("doctor/{doctorId}/upload_signature")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisDoctor uploadDoctorSignature(@PathVariable String doctorId,
			@RequestParam(name = "uploadFile", required = false) MultipartFile uploadFile,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Upload Doctor Signature:" + doctorId);
		QisDoctor qisDoctor = qisDoctorRepository.findByDoctorid(doctorId);
		if (qisDoctor == null) {
			throw new RuntimeException("Record not found.", new Throwable("doctorId: Record not found."));
		}

		if (uploadFile != null) {
			if (uploadFile.getSize() > 64000) {
				throw new RuntimeException("File size should not exceed to 64Kb.",
						new Throwable("File size should not exceed to 64Kb."));
			}
			InputStream inputStream = uploadFile.getInputStream();
			byte[] signature = StreamUtils.copyToByteArray(inputStream);
			qisDoctor.setSignature(signature);
			qisDoctor.setUpdatedBy(authUser.getId());
			qisDoctor.setUpdatedAt(Calendar.getInstance());
			qisDoctorRepository.save(qisDoctor);
			qisLogService.warn(authUser.getId(), QisDoctorController.class.getSimpleName(), "UPDATE", "SIGNATURE",
					qisDoctor.getId(), CATEGORY);
		} else {
			if (qisDoctor.getSignature() != null) {
				qisDoctor.setSignature(null);
				qisDoctor.setUpdatedBy(authUser.getId());
				qisDoctor.setUpdatedAt(Calendar.getInstance());
				qisDoctorRepository.save(qisDoctor);
				qisLogService.warn(authUser.getId(), QisDoctorController.class.getSimpleName(), "UPDATE",
						"REMOVE SIGNATURE", qisDoctor.getId(), CATEGORY);
			}
		}

		return qisDoctor;
	}

	// DELETE DOCTOR
	@DeleteMapping("doctor/{doctorId}")
	public String deleteDoctor(@PathVariable String doctorId, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.warn(authUser.getId() + "-Delete Doctor:" + doctorId);
		return "not implemented";
	}

	// LIST ALL DOCTORS
	@GetMapping("doctors")
	public List<QisDoctor> getAllDoctors() {
		return qisDoctorRepository.findAll();
	}
}
