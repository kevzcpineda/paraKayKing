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
import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.request.QisCorporateRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisCorporateResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisCorporateRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisCorporateController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisCorporateController.class);
	private final String CATEGORY = "CORPORATE";

	@Autowired
	private QisCorporateRepository qisCorporateRepository;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	// CREATE CORPORATE
	@PostMapping("corporate")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisCorporate createCorporate(@Valid @RequestBody QisCorporateRequest corporateRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Create Corporate");

		String corporateId = appUtility.generateUserId(10);
		if (qisCorporateRepository.existsByCompanyName(corporateRequest.getCompanyName()))
			throw new RuntimeException("Duplicate company name.",
					new Throwable("companyName: Duplicate company name."));
		if (qisCorporateRepository.existsBySoaCode(corporateRequest.getSoaCode()))
			throw new RuntimeException("Duplicate company SOA code.",
					new Throwable("soaCode: Duplicate company SOA code."));
		if (qisCorporateRepository.existsByCorporateid(corporateId))
			throw new RuntimeException("Duplicate corporate id.",
					new Throwable("corporateid: Duplicate corporate id."));

		String chargeType = appUtility.getChargeType(corporateRequest.getChargeType());
		if (chargeType == null)
			throw new RuntimeException("Charge Type[" + corporateRequest.getChargeType() + "] not found.",
					new Throwable("chargeType: Charge Type[" + corporateRequest.getChargeType() + "] not found."));

		QisCorporate qisCorporate = new QisCorporate();
		BeanUtils.copyProperties(corporateRequest, qisCorporate);

		if (corporateRequest.isActive() == null) {
			qisCorporate.setActive(true);
		} else {
			qisCorporate.setActive(corporateRequest.isActive());
		}
		qisCorporate.setCorporateid(corporateId);
		qisCorporate.setCreatedBy(authUser.getId());
		qisCorporate.setUpdatedBy(authUser.getId());

		QisCorporateResponse qisCorporateResponse = new QisCorporateResponse();
		BeanUtils.copyProperties(qisCorporate, qisCorporateResponse);
		qisCorporateResponse.setActive(qisCorporate.isActive());

		qisCorporateRepository.save(qisCorporate);

		qisLogService.info(authUser.getId(), QisCorporateController.class.getSimpleName(), "CREATE",
				qisCorporateResponse.toString(), qisCorporate.getId(), CATEGORY);

		return qisCorporate;
	}

	// READ CORPORATE
	@GetMapping("corporate/{corporateId}")
	public QisCorporate getCorporate(@PathVariable String corporateId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-View Corporate:" + corporateId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		return qisCorporate;
	}

	// UPDATE CORPORATE
	@PutMapping("corporate/{corporateId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisCorporate updateCorporate(@PathVariable String corporateId,
			@Valid @RequestBody QisCorporateRequest corporateRequest, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-Update Corporate:" + corporateId);
		QisCorporate qisCorporate = qisCorporateRepository.findByCorporateid(corporateId);
		if (qisCorporate == null) {
			throw new RuntimeException("Record not found.", new Throwable("corporateId: Record not found."));
		}

		if (qisCorporateRepository.findCompanyNameOnOtherCorporate(corporateRequest.getCompanyName(),
				qisCorporate.getId()) != null) {
			throw new RuntimeException("Duplicate company name.",
					new Throwable("companyName: Duplicate company name."));
		}

		if (qisCorporateRepository.findSoaCodeOnOtherCorporate(corporateRequest.getSoaCode(),
				qisCorporate.getId()) != null) {
			throw new RuntimeException("Duplicate company SOA code.",
					new Throwable("soaCode: Duplicate company SOA code."));
		}

		String chargeType = appUtility.getChargeType(corporateRequest.getChargeType());
		if (chargeType == null)
			throw new RuntimeException("Charge Type[" + corporateRequest.getChargeType() + "] not found.",
					new Throwable("chargeType: Charge Type[" + corporateRequest.getChargeType() + "] not found."));

		boolean isUpdate = false;
		String updateData = "";
		if (!qisCorporate.getCompanyName().equals(corporateRequest.getCompanyName())) {
			updateData = appUtility.formatUpdateData(updateData, "Company Name", qisCorporate.getCompanyName(),
					corporateRequest.getCompanyName());
			isUpdate = true;
			qisCorporate.setCompanyName(corporateRequest.getCompanyName());
		}

		if (!qisCorporate.getSoaCode().equals(corporateRequest.getSoaCode())) {
			updateData = appUtility.formatUpdateData(updateData, "SOA Code", qisCorporate.getSoaCode(),
					corporateRequest.getSoaCode());
			isUpdate = true;
			qisCorporate.setSoaCode(corporateRequest.getSoaCode());
		}

		if (corporateRequest.getCompanyAddress() != null && (qisCorporate.getCompanyAddress() == null
				|| !qisCorporate.getCompanyAddress().equals(corporateRequest.getCompanyAddress()))) {
			updateData = appUtility.formatUpdateData(updateData, "Company Address", qisCorporate.getCompanyAddress(),
					corporateRequest.getCompanyAddress());
			isUpdate = true;
			qisCorporate.setCompanyAddress(corporateRequest.getCompanyAddress());
		}

		if (corporateRequest.getContactPerson() != null && (qisCorporate.getContactPerson() == null
				|| !qisCorporate.getContactPerson().equals(corporateRequest.getContactPerson()))) {
			updateData = appUtility.formatUpdateData(updateData, "Contact Person", qisCorporate.getContactPerson(),
					corporateRequest.getContactPerson());
			isUpdate = true;
			qisCorporate.setContactPerson(corporateRequest.getContactPerson());
		}

		if (corporateRequest.getContactNumber() != null && (qisCorporate.getContactNumber() == null
				|| !qisCorporate.getContactNumber().equals(corporateRequest.getContactNumber()))) {
			updateData = appUtility.formatUpdateData(updateData, "Contact Number", qisCorporate.getContactNumber(),
					corporateRequest.getContactNumber());
			isUpdate = true;
			qisCorporate.setContactNumber(corporateRequest.getContactNumber());
		}

		if (corporateRequest.getEmail() != null
				&& (qisCorporate.getEmail() == null || !qisCorporate.getEmail().equals(corporateRequest.getEmail()))) {
			updateData = appUtility.formatUpdateData(updateData, "Email", qisCorporate.getEmail(),
					corporateRequest.getEmail());
			isUpdate = true;
			qisCorporate.setEmail(corporateRequest.getEmail());
		}

		if (corporateRequest.getResultEmail() != null && (qisCorporate.getResultEmail() == null
				|| !qisCorporate.getResultEmail().equals(corporateRequest.getResultEmail()))) {
			updateData = appUtility.formatUpdateData(updateData, "Result Email", qisCorporate.getResultEmail(),
					corporateRequest.getResultEmail());
			isUpdate = true;
			qisCorporate.setResultEmail(corporateRequest.getResultEmail());
		}

		if (corporateRequest.getChargeType() != null && (qisCorporate.getChargeType() == null
				|| !qisCorporate.getChargeType().equals(corporateRequest.getChargeType()))) {
			updateData = appUtility.formatUpdateData(updateData, "Charge Type", qisCorporate.getChargeType(),
					corporateRequest.getChargeType());
			isUpdate = true;
			qisCorporate.setChargeType(corporateRequest.getChargeType());
		}

		if (corporateRequest.isActive() != null && qisCorporate.isActive() != corporateRequest.isActive()) {
			updateData = appUtility.formatUpdateData(updateData, "Active", String.valueOf(qisCorporate.isActive()),
					String.valueOf(corporateRequest.isActive()));
			isUpdate = true;
			qisCorporate.setActive(corporateRequest.isActive());
		}

		if (isUpdate) {
			qisCorporate.setUpdatedBy(authUser.getId());
			qisCorporate.setUpdatedAt(Calendar.getInstance());
			qisCorporateRepository.save(qisCorporate);
			qisLogService.warn(authUser.getId(), QisCorporateController.class.getSimpleName(), "UPDATE", updateData,
					qisCorporate.getId(), CATEGORY);
		}
		return qisCorporate;
	}

	// DELETE CORPORATE
	@DeleteMapping("corporate/{corporateId}")
	public String deleteCorporate(@PathVariable String corporateId, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.warn(authUser.getId() + "-Delete Corporate:" + corporateId);
		return "not implemented";
	}

	// LIST ALL CORPORATES
	@GetMapping("corporates")
	public List<QisCorporate> getAllCorporates() {
		return qisCorporateRepository.findAll();
	}

	// LIST ACTIVE CORPORATES
	@GetMapping("corporates/active")
	public List<QisCorporate> getAllActiveCorporates() {
		return qisCorporateRepository.allActiveCorporates();
	}
	
	@GetMapping("corporates/active/corp")
	public List<QisCorporate> getAllActiveCorporatesCorp() {
		return qisCorporateRepository.allActiveCorporatesCorp();
	}
}
