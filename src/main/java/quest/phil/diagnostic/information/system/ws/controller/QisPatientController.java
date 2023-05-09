package quest.phil.diagnostic.information.system.ws.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.validation.Valid;

import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import quest.phil.diagnostic.information.system.ws.common.AppTransactionUtility;
import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.Country;
import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;
import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionDiscount;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionPayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisPatientRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionItemRequest;
import quest.phil.diagnostic.information.system.ws.model.request.QisTransactionPaymentRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabPERequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe.QisTransactionLabMedHisRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe.QisTransactionLabPhyExamRequest;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.pe.QisTransactionLabVitalSignRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisPatientResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionItemResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionPaymentResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisTransactionResponse;
import quest.phil.diagnostic.information.system.ws.model.response.ReturnResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisBranchRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisPatientRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionLaboratoryRequestRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisPatientController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisPatientController.class);
	private final String CATEGORY = "DOCTOR";
	private final String SHORTDATE = "yyyyMMdd";
	private final String LONGDATE = "yyyyMMddHHmm";

	@Autowired
	private QisPatientRepository qisPatientRepository;

	@Autowired
	AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	AppTransactionUtility appTransactionUtility;

	@Autowired
	private QisBranchRepository qisBranchRepository;

	@Autowired
	private QisUserRepository qisUserRepository;

	@Autowired
	private QisTransactionRepository qisTransactionRepository;

	@Autowired
	private QisTransactionController qisTransactionCreate;

	@Autowired
	private QisTransactionItemRepository qisTransactionItemRepository;

	@Autowired
	private QisTransactionLaboratoryRequestRepository qisTransactionRequestRepository;

	@Autowired
	private QisTransactionLaboratoryPEController addPe;

//	@Autowired
//	private QisTransactionLabPERequest physicalExamination;

	// CREATE PATIENT
	@PostMapping("patient")
	public QisPatient createPatient(@Valid @RequestBody QisPatientRequest patientRequest,
			@RequestParam(name = "image", required = false) MultipartFile image,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Create Patient");

		String patientId = appUtility.generateUserId(16);
		if (qisPatientRepository.existsByPatientid(patientId))
			throw new RuntimeException("Duplicate patient id.", new Throwable("patientid: Duplicate patient id."));
		if (qisPatientRepository.findByAccount(patientRequest.getFirstname(), patientRequest.getLastname(),
				patientRequest.getDateOfBirth()) != null)
			throw new RuntimeException("Duplicate account.", new Throwable("Duplicate account."));

		if (patientRequest.getGender() == null)
			throw new RuntimeException("Invalid Gender input ('M'-Male, 'F'-Female)",
					new Throwable("gender: Invalid Gender input ('M'-Male, 'F'-Female)."));

		Country nationality = null;
		if (patientRequest.getNationalityId() != null) {
			nationality = appUtility.getCountryByCountryId(patientRequest.getNationalityId());
			if (nationality == null) {
				throw new RuntimeException("Invalid Nationality Id.",
						new Throwable("nationalityId: Invalid Nationality Id."));
			}
		}

		QisPatient qisPatient = new QisPatient();
		BeanUtils.copyProperties(patientRequest, qisPatient);

		if (patientRequest.isActive() == null) {
			qisPatient.setActive(true);
		} else {
			qisPatient.setActive(patientRequest.isActive());
		}
		qisPatient.setPatientid(patientId);
		qisPatient.setCreatedBy(authUser.getId());
		qisPatient.setUpdatedBy(authUser.getId());
		qisPatient.setDateOfBirth(appUtility.stringToCalendarDate(patientRequest.getDateOfBirth(), "yyyy-MM-dd"));
		qisPatient.setNationality(nationality);

		if (patientRequest.getCorporateId() != null) {
			QisCorporate qisCorporate = appUtility.getQisCorporateByCorporateId(patientRequest.getCorporateId());
			if (qisCorporate != null) {
				qisPatient.setCorporateId(qisCorporate.getId());
				qisPatient.setCorporate(qisCorporate);
			}
		}

		QisPatientResponse qisPatientResponse = new QisPatientResponse();
		BeanUtils.copyProperties(qisPatient, qisPatientResponse);
		qisPatientResponse.setActive(qisPatient.isActive());
		qisPatientResponse.setDateOfBirth(appUtility.userShortDate(qisPatient.getDateOfBirth()));

		Float height = appUtility.parseFloatAmount(patientRequest.getHeight());
		qisPatient.setHeight(height);

		Float weight = appUtility.parseFloatAmount(patientRequest.getWeight());
		qisPatient.setWeight(weight);
		qisPatientRepository.save(qisPatient);

		qisLogService.info(authUser.getId(), QisPatientController.class.getSimpleName(), "CREATE",
				qisPatientResponse.toString(), qisPatient.getId(), CATEGORY);

		return qisPatient;
	}

	// CREATE PATIENT REGISTRATION
	@PostMapping("patient_registration")
	public QisPatient createPatientRegistration(
			@Valid @RequestParam(name = "image", required = false) MultipartFile image,
			@RequestParam(name = "firstname", required = false) String firstname,
			@RequestParam(name = "lastname", required = false) String lastname,
			@RequestParam(name = "middlename", required = false) String middlename,
			@RequestParam(name = "dateOfBirth", required = false) String dateOfBirth,
			@RequestParam(name = "contactNumber", required = false) String contactNumber,
			@RequestParam(name = "gender", required = false) String gender,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "address", required = false) String address,
			@RequestParam(name = "seniorId", required = false) String seniorId,
			@RequestParam(name = "pwdId", required = false) String pwdId,
			@RequestParam(name = "corporateId", required = false) String corporateId,
			@RequestParam(name = "nationalityId", required = false) Long nationalityId,
			@RequestParam(name = "passport", required = false) String passport,
			@RequestParam(name = "weight", required = false) String weight,
			@RequestParam(name = "height", required = false) String height,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Create Patient");

		String patientId = appUtility.generateUserId(16);

		if ("".equals(firstname)) {
			throw new RuntimeException("First Name is required.");
		}
		if ("".equals(lastname)) {
			throw new RuntimeException("Last Name is required.");
		}
		if (dateOfBirth.matches("^\\\\d{4}-\\\\d{2}-\\\\d{2}$")) {
			throw new RuntimeException("Invalid date format(yyyy-MM-DD)");
		}
		if ("".equals(contactNumber)) {
			throw new RuntimeException("Contact is required.");
		}
		if ("".equals(address)) {
			throw new RuntimeException("Address is required.");
		}

		if (qisPatientRepository.existsByPatientid(patientId))
			throw new RuntimeException("Duplicate patient id.", new Throwable("patientid: Duplicate patient id."));
		if (qisPatientRepository.findByAccount(firstname, lastname, dateOfBirth) != null)
			throw new RuntimeException("Duplicate account.", new Throwable("Duplicate account."));

		if (gender == null)
			throw new RuntimeException("Invalid Gender input ('M'-Male, 'F'-Female)",
					new Throwable("gender: Invalid Gender input ('M'-Male, 'F'-Female)."));

		Country nationality = null;
		if (nationalityId != null) {
			nationality = appUtility.getCountryByCountryId(nationalityId);
			if (nationality == null) {
				throw new RuntimeException("Invalid Nationality Id.",
						new Throwable("nationalityId: Invalid Nationality Id."));
			}
		}

		QisPatient qisPatient = new QisPatient();
//			BeanUtils.copyProperties(patientRequest, qisPatient);
		qisPatient.setFirstname(firstname.toUpperCase());
		qisPatient.setMiddlename(middlename.toUpperCase());
		qisPatient.setLastname(lastname.toUpperCase());
		qisPatient.setContactNumber(contactNumber);
		qisPatient.setEmail(email);
		qisPatient.setAddress(address.toUpperCase());
		qisPatient.setGender(gender);
		qisPatient.setActive(true);
		qisPatient.setPatientid(patientId);
		qisPatient.setCreatedBy(authUser.getId());
		qisPatient.setUpdatedBy(authUser.getId());
		qisPatient.setDateOfBirth(appUtility.stringToCalendarDate(dateOfBirth, "yyyy-MM-dd"));
		qisPatient.setNationality(nationality);
		qisPatient.setNationalityId(nationality.getId());

		if (!"".equals(passport) && !"null".equals(passport)) {
			qisPatient.setPassport(passport);
		}
		if (!"".equals(pwdId) && !"null".equals(pwdId)) {
			qisPatient.setPwdId(pwdId);
		}
		if (!"".equals(seniorId) && !"null".equals(seniorId)) {
			qisPatient.setSeniorId(seniorId);
		}
		if (corporateId != null) {
			QisCorporate qisCorporate = appUtility.getQisCorporateByCorporateId(corporateId);
			if (qisCorporate != null) {
				qisPatient.setCorporateId(qisCorporate.getId());
				qisPatient.setCorporate(qisCorporate);
			}
		}

	
		if (image != null) {
			if (image.getSize() > 500000) {
				throw new RuntimeException("File size should not exceed to 50KB.",
						new Throwable("File size should not exceed to 50KB."));
			}
			InputStream inputStream = image.getInputStream();
			byte[] receipt = StreamUtils.copyToByteArray(inputStream);
			qisPatient.setImage(receipt);
			qisPatient.setImageType(image.getContentType());
		}

		QisPatientResponse qisPatientResponse = new QisPatientResponse();
		BeanUtils.copyProperties(qisPatient, qisPatientResponse);
		qisPatientResponse.setActive(qisPatient.isActive());
		qisPatientResponse.setDateOfBirth(appUtility.userShortDate(qisPatient.getDateOfBirth()));

		Float heightF = appUtility.parseFloatAmount(height);
		qisPatient.setHeight(heightF);

		Float weightF = appUtility.parseFloatAmount(weight);
		qisPatient.setWeight(weightF);
		qisPatientRepository.save(qisPatient);

		qisLogService.info(authUser.getId(), QisPatientController.class.getSimpleName(), "CREATE",
				qisPatientResponse.toString(), qisPatient.getId(), CATEGORY);

		return qisPatient;
	}

	// READ PATIENT
	@GetMapping("patient/{patientId}")
	public QisPatient getPatient(@PathVariable String patientId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-View Patient:" + patientId);

		QisPatient qisPatient = qisPatientRepository.findByPatientid(patientId);
		if (qisPatient == null) {
			throw new RuntimeException("Record not found.");
		}
		return qisPatient;
	}

	// UPDATE PATIENT
	@PutMapping("patient/{patientId}")
	public QisPatient updatePatient(@PathVariable String patientId,
			@Valid @RequestBody QisPatientRequest patientRequest, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {
		LOGGER.info(authUser.getId() + "-Update Patient:" + patientId);
		QisPatient qisPatient = qisPatientRepository.findByPatientid(patientId);
		if (qisPatient == null) {
			throw new RuntimeException("Record not found.");
		}

		if (patientRequest.getGender() == null)
			throw new RuntimeException("Invalid Gender input ('M'-Male, 'F'-Female)",
					new Throwable("gender: Invalid Gender input ('M'-Male, 'F'-Female)."));

		if (qisPatientRepository.findByDuplicateAccount(patientRequest.getFirstname(), patientRequest.getLastname(),
				patientRequest.getDateOfBirth(), qisPatient.getId()) != null)
			throw new RuntimeException("Duplicate Account.", new Throwable("Duplicate Account."));

		Country reqNationality = null;
		if (patientRequest.getNationalityId() != null) {
			reqNationality = appUtility.getCountryByCountryId(patientRequest.getNationalityId());
			if (reqNationality == null) {
				throw new RuntimeException("Invalid Nationality Id.",
						new Throwable("nationalityId: Invalid Nationality Id."));
			}
		}

		boolean isUpdate = false;
		String updateData = "";
		if (!qisPatient.getFirstname().equals(patientRequest.getFirstname())) {
			updateData = appUtility.formatUpdateData(updateData, "Firstname", qisPatient.getFirstname(),
					patientRequest.getFirstname());
			isUpdate = true;
			qisPatient.setFirstname(patientRequest.getFirstname());
		}

		if (!qisPatient.getLastname().equals(patientRequest.getLastname())) {
			updateData = appUtility.formatUpdateData(updateData, "Lastname", qisPatient.getLastname(),
					patientRequest.getLastname());
			isUpdate = true;
			qisPatient.setLastname(patientRequest.getLastname());
		}

		if (patientRequest.getMiddlename() != null) {
			if (qisPatient.getMiddlename() != null) {
				if (!qisPatient.getMiddlename().equals(patientRequest.getMiddlename())) {
					updateData = appUtility.formatUpdateData(updateData, "Middlename", qisPatient.getMiddlename(),
							patientRequest.getMiddlename());
					isUpdate = true;
					qisPatient.setMiddlename(patientRequest.getMiddlename());
				}
			} else {
				updateData = appUtility.formatUpdateData(updateData, "Middlename", qisPatient.getMiddlename(),
						patientRequest.getMiddlename());
				isUpdate = true;
				qisPatient.setMiddlename(patientRequest.getMiddlename());
			}
		} else {
			if (qisPatient.getMiddlename() != null) {
				updateData = appUtility.formatUpdateData(updateData, "Middlename", qisPatient.getMiddlename(), null);
				isUpdate = true;
				qisPatient.setMiddlename(null);
			}
		}

		if (!patientRequest.getDateOfBirth().equals(appUtility.appShortDate(qisPatient.getDateOfBirth()))) {
			updateData = appUtility.formatUpdateData(updateData, "BirthDate",
					appUtility.appShortDate(qisPatient.getDateOfBirth()), patientRequest.getDateOfBirth());
			isUpdate = true;
			qisPatient.setDateOfBirth(appUtility.stringToCalendarDate(patientRequest.getDateOfBirth(), "yyyy-MM-dd"));
		}

		if (patientRequest.getContactNumber() != null && (qisPatient.getContactNumber() == null
				|| !qisPatient.getContactNumber().equals(patientRequest.getContactNumber()))) {
			updateData = appUtility.formatUpdateData(updateData, "Contact Number", qisPatient.getContactNumber(),
					patientRequest.getContactNumber());
			isUpdate = true;
			qisPatient.setContactNumber(patientRequest.getContactNumber());
		}

		if (patientRequest.getEmail() != null
				&& (qisPatient.getEmail() == null || !qisPatient.getEmail().equals(patientRequest.getEmail()))) {
			updateData = appUtility.formatUpdateData(updateData, "Email", qisPatient.getEmail(),
					patientRequest.getEmail());
			isUpdate = true;
			qisPatient.setEmail(patientRequest.getEmail());
		}

		if (patientRequest.getAddress() != null
				&& (qisPatient.getAddress() == null || !qisPatient.getAddress().equals(patientRequest.getAddress()))) {
			updateData = appUtility.formatUpdateData(updateData, "Address", qisPatient.getAddress(),
					patientRequest.getAddress());
			isUpdate = true;
			qisPatient.setAddress(patientRequest.getAddress());
		}

		if (patientRequest.getSeniorId() != null && (qisPatient.getSeniorId() == null
				|| !qisPatient.getSeniorId().equals(patientRequest.getSeniorId()))) {
			updateData = appUtility.formatUpdateData(updateData, "Senior Id", qisPatient.getSeniorId(),
					patientRequest.getSeniorId());
			isUpdate = true;
			qisPatient.setSeniorId(patientRequest.getSeniorId());
		}

		if (patientRequest.getPwdId() != null
				&& (qisPatient.getPwdId() == null || !qisPatient.getPwdId().equals(patientRequest.getPwdId()))) {
			updateData = appUtility.formatUpdateData(updateData, "PWD Id", qisPatient.getPwdId(),
					patientRequest.getPwdId());
			isUpdate = true;
			qisPatient.setPwdId(patientRequest.getPwdId());
		}

		if (patientRequest.getPassport() != null && (qisPatient.getPassport() == null
				|| !qisPatient.getPassport().equals(patientRequest.getPassport()))) {
			updateData = appUtility.formatUpdateData(updateData, "Passport", qisPatient.getPwdId(),
					patientRequest.getPassport());
			isUpdate = true;
			qisPatient.setPassport(patientRequest.getPassport());
		}

		if (patientRequest.getCorporateId() != null) {
			QisCorporate qisCorporate = appUtility.getQisCorporateByCorporateId(patientRequest.getCorporateId());
			if (qisCorporate == null) {
				if (qisPatient.getCorporateId() != null) {
					updateData = appUtility.formatUpdateData(updateData, "Corporate Id",
							String.valueOf(qisPatient.getCorporateId()), null);
					isUpdate = true;
					qisPatient.setCorporateId(null);
					qisPatient.setCorporate(null);
				}
			} else {
				if (qisPatient.getCorporateId() == null || qisPatient.getCorporateId() != qisCorporate.getId()) {
					updateData = appUtility.formatUpdateData(updateData, "Corporate Id",
							String.valueOf(qisPatient.getCorporateId()), String.valueOf(qisCorporate.getId()));
					isUpdate = true;
					qisPatient.setCorporateId(qisCorporate.getId());
					qisPatient.setCorporate(qisCorporate);
				}
			}
		}

		if (!qisPatient.getGender().equals(patientRequest.getGender())) {
			updateData = appUtility.formatUpdateData(updateData, "Gender", String.valueOf(qisPatient.getGender()),
					String.valueOf(patientRequest.getGender()));
			isUpdate = true;
			qisPatient.setGender(patientRequest.getGender());
		}

		if (!qisPatient.getNationalityId().equals(patientRequest.getNationalityId())) {
			isUpdate = true;
			updateData = appUtility.formatUpdateData(updateData, "Nationality",
					qisPatient.getNationality().getNationality(), reqNationality.getNationality());
			qisPatient.setNationalityId(reqNationality.getId());
			qisPatient.setNationality(reqNationality);
		}

		if (patientRequest.isActive() != null && qisPatient.isActive() != patientRequest.isActive()) {
			updateData = appUtility.formatUpdateData(updateData, "Active", String.valueOf(qisPatient.isActive()),
					String.valueOf(patientRequest.isActive()));
			isUpdate = true;
			qisPatient.setActive(patientRequest.isActive());
		}
		Float height = appUtility.parseFloatAmount(patientRequest.getHeight());
		if (patientRequest.getHeight() != null && qisPatient.getHeight() != height) {
			isUpdate = true;
			qisPatient.setHeight(height);
		}

		Float weight = appUtility.parseFloatAmount(patientRequest.getWeight());
		if (patientRequest.getHeight() != null && qisPatient.getHeight() != weight) {
			isUpdate = true;
			qisPatient.setWeight(weight);
		}

		if (isUpdate) {
			qisPatient.setUpdatedBy(authUser.getId());
			qisPatient.setUpdatedAt(Calendar.getInstance());
			qisPatientRepository.save(qisPatient);
			qisLogService.warn(authUser.getId(), QisDoctorController.class.getSimpleName(), "UPDATE", updateData,
					qisPatient.getId(), CATEGORY);
		}

		return qisPatient;
	}

	// DELETE PATIENT
	@DeleteMapping("patient/{patientId}")
	public String deletePatient(@PathVariable String patientId, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.warn(authUser.getId() + "-Delete Patient:" + patientId);
		return "not implemented";
	}

	// LIST ALL PATIENTS
	@GetMapping("patients")
	public Page<QisPatient> getAllPatients(Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) {
		return qisPatientRepository.findAllByPatientIdDesc(pageable);
	}

	// LIST PATIENTS BY DATE
	@GetMapping("date/patients")
	public Page<QisPatient> getPatientsByDate(Pageable pageable, @AuthenticationPrincipal QisUserDetails authUser) {

		String startDate = appUtility.calendarToFormatedDate(Calendar.getInstance(), "YYYYMMdd");
		String endDate = appUtility.calendarToFormatedDate(Calendar.getInstance(), "YYYYMMdd");

		Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(startDate + "0500", LONGDATE);
		Calendar txnDateTimeTo = appUtility.stringToCalendarDate(endDate + "2100", LONGDATE);

		return qisPatientRepository.findByTransactionDateAndPatientIdDesc(txnDateTimeFrom, txnDateTimeTo, pageable);

	}

	@GetMapping("patients/search")
	public Page<QisPatient> searchPatients(@RequestParam(required = false) String searchKey,
			@RequestParam(required = false) String lastname, @RequestParam(required = false) String firstname,
			@RequestParam(required = false) String birthDay, Pageable pageable,
			@AuthenticationPrincipal QisUserDetails authUser) {

		if (searchKey != null) {
			return qisPatientRepository.searchPatientV2(searchKey, pageable);
		} else if (lastname != null) {
			return qisPatientRepository.searchPatientLastname(lastname, pageable);
		} else if (firstname != null) {
			return qisPatientRepository.searchPatientFirstname(firstname, pageable);
		} else if (birthDay != null) {
			if (birthDay.length() != SHORTDATE.length()) {
				throw new RuntimeException("Invalid Birth Day[" + birthDay + "] use (YYYYMMDD) format");
			}
			Calendar birthDate = appUtility.stringToCalendarDate(birthDay, SHORTDATE);
			if (birthDate == null) {
				throw new RuntimeException("Invalid Birth Day[" + birthDay + "] use (YYYYMMDD) format");
			}
			return qisPatientRepository.searchPatientBirthDay(birthDate, pageable);
		}

		throw new RuntimeException("No parameters added on the request.");
	}

	@SuppressWarnings({ "unused" })
	@PostMapping("patientList")
	public ResponseEntity<?> createPatientList(
			@Valid @RequestParam(name = "csvFiles", required = false) MultipartFile csvFiles,
			@RequestParam(name = "transactionItems", required = false) String items,
			@RequestParam(name = "corporate", required = false) String company,
			@RequestParam(name = "transactionType", required = false) String transactionType,
			@RequestParam(name = "branchId", required = false) String branchId,
			@RequestParam(name = "cashierId", required = false) String cashierId,
			@RequestParam(name = "transactionPayments", required = false) String paymentData,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception, HibernateException {
		int newTransaction = 0;
		QisCorporate qisCorporate = null;
		if (company != null) {
			qisCorporate = appUtility.getQisCorporateByCorporateId(company);
		}

		QisBranch qisBranch = qisBranchRepository.findByBranchid(branchId);
		if (qisBranch == null) {
			throw new RuntimeException("Invalid branch id[" + branchId + "]",
					new Throwable("branchId: Invalid branch id[" + branchId + "]"));
		}

		QisUser qisCashier = qisUserRepository.findByUserid(cashierId);
		if (qisCashier == null) {
			throw new RuntimeException("Invalid cashier id[" + cashierId + "]",
					new Throwable("cashierId: Invalid cashier id[" + cashierId + "]"));
		} else {
			Set<String> roles = appUtility.getStrUserRoles(qisCashier.getRoles());
			if (!roles.contains("CASHIER") && !roles.contains("ADMIN")) {
				throw new RuntimeException("Invalid cashier [" + cashierId + "]",
						new Throwable("cashierId: Invalid cashier id[" + cashierId + "]"));
			}
		}

		int success = 0;
		int failed = 0;
		if (!csvFiles.isEmpty()) {
			try {
				byte[] bytes = csvFiles.getBytes();
				InputStream inputStream = csvFiles.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				String completeData = new String(bytes);
				BufferedReader br = new BufferedReader(inputStreamReader);
				br.readLine();
				int lines = 0;
				Country nationality = null;
				nationality = appUtility.getCountryByCountryId((long) 167);
				if (nationality == null) {
					throw new RuntimeException("Invalid Nationality Id.",
							new Throwable("nationalityId: Invalid Nationality Id."));
				}
				while ((completeData = br.readLine()) != null) {
					QisTransaction qisTransaction = new QisTransaction();
					String status = appUtility.getTransactionStatus("SPD");
					JSONArray arrayItems = new JSONArray(items);
					qisTransaction.setTaxRate(appUtility.getTaxRate());

					JSONArray arraypayment = new JSONArray(paymentData);
					Set<QisTransactionPayment> transactionPayments = null;
					QisTransactionPaymentRequest paymentRequest = new QisTransactionPaymentRequest();
					Set<QisTransactionPaymentRequest> setPayments = new HashSet<QisTransactionPaymentRequest>();
					QisTransactionResponse qisTransactionResponse = new QisTransactionResponse();

					Set<QisTransactionItem> transactionItems = null;
					Set<QisTransactionItemRequest> setItems = new HashSet<QisTransactionItemRequest>();
					if (items.length() != 2) {

						for (int i = 0; i < arraypayment.length(); ++i) {
							JSONObject pay = arraypayment.getJSONObject(i);
							String amount = pay.getString("amount");
							Double amountDouble = pay.getDouble("amount");
							String billerId = pay.getString("billerId");
							String currency = pay.getString("currency");
							String paymentMode = pay.getString("paymentMode");
							String paymentType = pay.getString("paymentType");

							qisTransaction.specialDiscountAmount = 0d;
							qisTransaction.totalCashAmount = 0d;
							qisTransaction.totalCashOut = 0d;
							qisTransaction.totalChangeAmount = 0d;
							qisTransaction.totalDiscountableAmount = 0d;
							qisTransaction.totalItemAmountDue = amountDouble;
							qisTransaction.totalItemDiscountAmount = 0d;
							qisTransaction.totalItemGrossAmount = amountDouble;

							paymentRequest.amount = amount;
							paymentRequest.billerId = billerId;
							paymentRequest.currency = currency;
							paymentRequest.paymentMode = paymentMode;
							paymentRequest.paymentType = paymentType;

							setPayments.add(paymentRequest);
						}
						transactionPayments = getTransactionPayments(setPayments);

						for (int i = 0; i < arrayItems.length(); ++i) {
							QisTransactionItemRequest transItemReq = new QisTransactionItemRequest();
							JSONObject rec = arrayItems.getJSONObject(i);
							String itemid = rec.getString("itemid");
							String itemType = rec.getString("itemType");
							int quantity = rec.getInt("quantity");
							int discountRate = rec.getInt("discountRate");
							String discountType = rec.getString("discountType");

							transItemReq.itemid = itemid;
							transItemReq.itemType = itemType;
							transItemReq.quantity = quantity;
							transItemReq.discountType = discountType;
							transItemReq.discountRate = discountRate;
							transItemReq.id = null;
							setItems.add(transItemReq);
						}
						transactionItems = getTransactionItems(qisTransaction, setItems);
					}

					String transactionId = appUtility.generateUserId(16);
					lines++;
					String[] cols = completeData.split(",");
					long pk = 0;
					String vital = null;
					String medHis = null;
					String physicalExam = null;
					QisPatient patient = null;
					if (cols.length != 0) {
						String patientId = appUtility.generateUserId(16);
						QisPatient qisPatient = new QisPatient();
						QisPatient patientReq = qisPatientRepository.findByAccount(cols[0], cols[2], cols[4]);
						if (patientReq == null) {

							success++;
							qisPatient.setActive(true);
							qisPatient.setPatientid(patientId);
							qisPatient.setCreatedBy(authUser.getId());
							qisPatient.setUpdatedBy(authUser.getId());
							qisPatient.setNationalityId((long) 167);
							qisPatient.setNationality(nationality);
							if (qisCorporate != null) {
								qisPatient.setCorporateId(qisCorporate.getId());
								qisPatient.setCorporate(qisCorporate);
							}
							if (!"".equals(cols[4])) {
								if (appUtility.stringToCalendarDate(cols[4], "yyyy-MM-dd") == null) {
									throw new RuntimeException("Required Format for BirthDate '2021-12-31'");
								} else {
									qisPatient.setDateOfBirth(appUtility.stringToCalendarDate(cols[4], "yyyy-MM-dd"));
								}
							}

							if (cols[0] != null || !cols[0].trim().equals("")) {
								qisPatient.setFirstname(cols[0].replace("_", ","));
							}

							if (cols[1] != null || !cols[1].trim().equals("")) {
								qisPatient.setMiddlename(cols[1]);
							} else {
								qisPatient.setMiddlename(null);
							}

							if (cols[2] != null || !cols[2].trim().equals("")) {
								qisPatient.setLastname(cols[2].replace("_", ","));
							}

							if (cols[3] != null || !cols[3].trim().equals("")) {
								qisPatient.setAddress(cols[3].replace("_", ","));
							}

							if (cols[5] != null || !cols[5].trim().equals("")) {
								qisPatient.setEmail(cols[5]);
							} else {
								qisPatient.setEmail("");
							}

							if (cols[7] != null || !cols[7].trim().equals("")) {
								if ("MALE".equals(cols[7]) || "male".equals(cols[7]) || "M".equals(cols[7])
										|| "m".equals(cols[7])) {
									qisPatient.setGender("M");
								} else {
									qisPatient.setGender("F");
								}
							}

							if (cols[6].length() != 0) {
								qisPatient.setContactNumber(cols[6]);
							} else {
								qisPatient.setContactNumber("0");
							}

							qisPatientRepository.save(qisPatient);

							pk = qisPatient.getId();
							patient = qisPatient;
						} else {
							failed++;
							pk = patientReq.getId();
							patient = patientReq;
						}
						if (cols[8].length() != 0) {
							vital = cols[8];
						}
						if (cols[9].length() != 0) {
							medHis = cols[9];
						}
						if (cols[10].length() != 0) {
							physicalExam = cols[10];
						}
					}

					if (items.length() != 2) {
						if (patient != null) {
							newTransaction++;
							qisTransaction.computeItems();
							qisTransaction.computePayments();
							qisTransaction.computeChangeAmount();

							QisTransactionDiscount qisTransactionDiscount = null;
							qisTransactionDiscount = new QisTransactionDiscount();
							qisTransaction.setTransactionDate(Calendar.getInstance());
							qisTransaction.setTransactionItems(transactionItems);
							qisTransaction.setTransactionPayments(transactionPayments);
							qisTransaction.setPatientId(pk);
							qisTransaction.setPatient(patient);
							qisTransaction.setSpecialDiscountAmount(0d);
							qisTransaction.setBranchId(qisBranch.getId());
							qisTransaction.setBranch(qisBranch);
							qisTransaction.setCashierId(qisCashier.getId());
							qisTransaction.setCashier(qisCashier);
							qisTransaction.setCreatedBy(authUser.getId());
							qisTransaction.setUpdatedBy(authUser.getId());
							qisTransaction.setTransactionid(transactionId);
							qisTransaction.setTransactionType(transactionType);
							qisTransaction.setTransactionDate(Calendar.getInstance());
							qisTransaction.setStatus("SPD");

							qisTransactionRepository.save(qisTransaction);

							Set<QisTransactionLaboratoryRequest> transactionLabRequests = saveQisTransactionItem(
									transactionItems, qisTransaction.getId(), authUser.getId());
							qisTransactionResponse.setTransactionDate(appUtility.calendarToFormatedDate(
									qisTransaction.getTransactionDate(), AppTransactionUtility.DATEFORMAT));
							qisTransactionResponse.setBranchId(qisBranch.getBranchCode());
							qisTransactionResponse.setPatientId(patient.getPatientid());
							qisTransactionResponse.setPatientName(appUtility.getPatientFullname(patient));
							qisTransactionResponse.setCashierId(qisCashier.getUserid());
							qisTransactionResponse.setCashierName(appUtility.getUsername(qisCashier));
							qisTransactionResponse.setItemTransactions(
									getTransactionItemsResponse(qisTransaction.getTransactionItems()));
							qisTransactionResponse.setPaymentTransactions(
									getTransactionPaymentsResponse(qisTransaction.getTransactionPayments()));

							qisTransactionCreate.saveQisTransactionItem(transactionItems, qisTransaction.getId(),
									authUser.getId());
							qisTransactionCreate.saveQisTransactionPayment(transactionPayments, qisTransaction.getId(),
									authUser.getId());
							qisTransactionCreate.saveQisTransactionLaboratoryRequest(transactionLabRequests,
									qisTransaction.getId(), authUser.getId());

							QisTransactionLabPERequest physicalExaminationdata = new QisTransactionLabPERequest();

							QisTransactionLabVitalSignRequest vitalSign = new QisTransactionLabVitalSignRequest();
							String[] vitalItems = null;
							if (vital != null) {
								vitalItems = vital.split(":");
							}

							if (vitalItems != null) {
								vitalSign.alcoholic = vitalItems[0].replace("_", ",");
								vitalSign.bloodPressure = vitalItems[1].replace("_", ",");
								vitalSign.bmi = Float.parseFloat(vitalItems[2]);
								vitalSign.bmiCategory = vitalItems[3];
								vitalSign.correctedOD = vitalItems[4];
								vitalSign.correctedOS = vitalItems[5];
								vitalSign.hearing = vitalItems[6].replace("_", ",");
								vitalSign.height = vitalItems[7];
								vitalSign.hospitalization = vitalItems[8].replace("_", ",");
								vitalSign.ishihara = vitalItems[9].replace("_", ",");
								vitalSign.lastMenstrual = vitalItems[10].replace("_", ",");
								vitalSign.medications = vitalItems[11].replace("_", ",");
								vitalSign.notes = vitalItems[12].replace("_", ",");
								vitalSign.opearations = vitalItems[13].replace("_", ",");
								vitalSign.pregnant = Boolean.parseBoolean(vitalItems[14]);
								vitalSign.pulseRate = Integer.parseInt(vitalItems[15]);
								vitalSign.respiratoryRate = Integer.parseInt(vitalItems[16]);
								vitalSign.smoker = vitalItems[17].replace("_", ",");
								vitalSign.uncorrectedOD = vitalItems[18];
								vitalSign.uncorrectedOS = vitalItems[19];
								vitalSign.weight = vitalItems[20];
							}
							physicalExaminationdata.setVitalSign(vitalSign);

							QisTransactionLabMedHisRequest medHistoryReq = new QisTransactionLabMedHisRequest();
							String[] medHisItems = null;
							if (medHis != null) {
								medHisItems = medHis.split(":");
							}
							if (medHisItems != null) {
								medHistoryReq.abdominalHernia = Boolean.parseBoolean(medHisItems[0]);
								medHistoryReq.allergies = Boolean.parseBoolean(medHisItems[1]);
								medHistoryReq.asthma = Boolean.parseBoolean(medHisItems[2]);
								medHistoryReq.cancerTumor = Boolean.parseBoolean(medHisItems[3]);
								medHistoryReq.diabetesMellitus = Boolean.parseBoolean(medHisItems[4]);
								medHistoryReq.faintingSeizures = Boolean.parseBoolean(medHisItems[5]);
								medHistoryReq.heartProblem = Boolean.parseBoolean(medHisItems[6]);
								medHistoryReq.hepatitis = Boolean.parseBoolean(medHisItems[7]);
								medHistoryReq.highBloodPressure = Boolean.parseBoolean(medHisItems[8]);
								medHistoryReq.jointBackScoliosis = Boolean.parseBoolean(medHisItems[9]);
								medHistoryReq.kidneyProblem = Boolean.parseBoolean(medHisItems[10]);
								medHistoryReq.migraineHeadache = Boolean.parseBoolean(medHisItems[11]);
								medHistoryReq.psychiatricProblem = Boolean.parseBoolean(medHisItems[12]);
								medHistoryReq.stdplhiv = Boolean.parseBoolean(medHisItems[13]);
								medHistoryReq.tuberculosis = Boolean.parseBoolean(medHisItems[14]);
								medHistoryReq.travelhistory = Boolean.parseBoolean(medHisItems[15]);
							}
							physicalExaminationdata.setMedicalHistory(medHistoryReq);

							QisTransactionLabPhyExamRequest physicalExamReq = new QisTransactionLabPhyExamRequest();
							String[] physicalExamItems = null;
							if (physicalExam != null) {
								physicalExamItems = physicalExam.split(":");
							}
							if (physicalExamItems != null) {
								physicalExamReq.abdomen = Boolean.parseBoolean(physicalExamItems[0]);
								physicalExamReq.cardiacHeart = Boolean.parseBoolean(physicalExamItems[1]);
								physicalExamReq.chestBreastLungs = Boolean.parseBoolean(physicalExamItems[2]);
								physicalExamReq.extremities = Boolean.parseBoolean(physicalExamItems[3]);
								physicalExamReq.findings = physicalExamItems[4].replace("_", ",");
								physicalExamReq.headNeck = Boolean.parseBoolean(physicalExamItems[5]);
								physicalExamReq.license = physicalExamItems[6];
								physicalExamReq.notes = physicalExamItems[7].replace("_", ",");
								physicalExamReq.skin = Boolean.parseBoolean(physicalExamItems[8]);
								physicalExamReq.doctorId = "MlZiCQKmt5";
								physicalExamReq.remarks = Boolean.parseBoolean(physicalExamItems[9]);
								physicalExamReq.fatigueachespains = Boolean.parseBoolean(physicalExamItems[10]);
							}
							physicalExaminationdata.setPhysicalExam(physicalExamReq);

							QisTransactionLaboratoryRequest req = qisTransactionRequestRepository
									.getTransactionLaboratoryRequestByIdAndLabReq(qisTransaction.getId(), "PE");

							if (medHis != null && physicalExam != null && vital != null) {
								addPe.saveTransactionLaboratoryPE(qisTransaction.getTransactionid(), req.getId(),
										physicalExaminationdata, authUser);
							}
						}

					}
				}

			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		String added = "";
		String notDone = "";
		String addedTransaction = "";
		if (success != 0) {
			if (failed == 0) {
				added = "\"" + success + "\" New Patient added";
			} else {
				added = "\"" + success + "\" New Patient added ,";
			}
		}
		if (failed != 0) {
			if (success == 0) {
				if (items.length() == 2) {
					throw new RuntimeException("\"" + failed + "\" patient has already info");
				}

			} else {
				notDone = "\"" + failed + "\" patient has already info";
			}
		}

		if (newTransaction != 0) {
			addedTransaction = "\"" + newTransaction + "\" New Transaction has been added";
		}
		LOGGER.info(authUser.getId() + "-Create Patient");
		return ResponseEntity.ok(new ReturnResponse("success", added + notDone + addedTransaction));

	}

	private Set<QisTransactionPayment> getTransactionPayments(Set<QisTransactionPaymentRequest> setPayments) {
		Set<QisTransactionPayment> payments = new HashSet<>();
		if (setPayments != null) {
			setPayments.forEach(payment -> {
				QisTransactionPayment qisTransactionPayment = appTransactionUtility.getQisTransactionPayment(payment);
				if (qisTransactionPayment != null) {
					payments.add(qisTransactionPayment);
				} else {
					throw new IllegalArgumentException("Invalid Payment Type: " + payment.getPaymentType());
				}
			});
		}
		return payments;
	}

	private Set<QisTransactionItem> getTransactionItems(QisTransaction qisTransaction,
			Set<QisTransactionItemRequest> setItems) {
		Set<QisTransactionItem> items = new HashSet<>();
		if (setItems != null) {
			setItems.forEach(item -> {
				QisTransactionItem qisTransactionItem = appTransactionUtility.getQisTransactionItem(item);
				if (qisTransactionItem != null) {
					appTransactionUtility.computeTransactionItemAmount(item, qisTransaction, qisTransactionItem);
					items.add(qisTransactionItem);
				} else {
					throw new RuntimeException("Item Not Found.");
				}
			});
		}
		return items;
	}

	private Set<QisTransactionItemResponse> getTransactionItemsResponse(Set<QisTransactionItem> txnItems) {
		Set<QisTransactionItemResponse> itemsResponse = new HashSet<>();
		if (txnItems != null) {
			txnItems.forEach(txnItem -> {
				QisTransactionItemResponse itemRes = new QisTransactionItemResponse();
				BeanUtils.copyProperties(txnItem, itemRes);
				itemsResponse.add(itemRes);
			});
		}
		return itemsResponse;
	}

	private Set<QisTransactionPaymentResponse> getTransactionPaymentsResponse(Set<QisTransactionPayment> txnPayments) {
		Set<QisTransactionPaymentResponse> paymentsResponse = new HashSet<>();
		if (txnPayments != null) {
			txnPayments.forEach(txnPayment -> {
				QisTransactionPaymentResponse paymentRes = new QisTransactionPaymentResponse();
				BeanUtils.copyProperties(txnPayment, paymentRes);
				if (txnPayment.getBiller() != null) {
					paymentRes.setBillerId(txnPayment.getBiller().getCorporateid());
					paymentRes.setBillerName(txnPayment.getBiller().getCompanyName());
				}
				paymentsResponse.add(paymentRes);
			});
		}
		return paymentsResponse;
	}

	public Set<QisTransactionLaboratoryRequest> saveQisTransactionItem(Set<QisTransactionItem> transactionItems,
			Long transactionId, Long userId) throws Exception {
		Set<QisTransactionLaboratoryRequest> labRequests = new HashSet<>();
		if (transactionItems != null) {
			transactionItems.forEach(item -> {
				item.setTransactionid(transactionId);
				item.setCreatedBy(userId);
				item.setUpdatedBy(userId);
				qisTransactionItemRepository.save(item);
				appTransactionUtility.getLaboratoryRequestList(labRequests, item);
			});
		}
		return labRequests;
	}

}
