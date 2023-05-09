package quest.phil.diagnostic.information.system.ws.common;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import quest.phil.diagnostic.information.system.ws.model.QisBankType;
import quest.phil.diagnostic.information.system.ws.model.QisCategoryName;
import quest.phil.diagnostic.information.system.ws.model.QisChargeType;
import quest.phil.diagnostic.information.system.ws.model.QisClassifications;
import quest.phil.diagnostic.information.system.ws.model.QisCurrencyType;
import quest.phil.diagnostic.information.system.ws.model.QisDiscountType;
import quest.phil.diagnostic.information.system.ws.model.QisDispatchType;
import quest.phil.diagnostic.information.system.ws.model.QisFecalysisColor;
import quest.phil.diagnostic.information.system.ws.model.QisFecalysisConsistency;
import quest.phil.diagnostic.information.system.ws.model.QisLaboratoryName;
import quest.phil.diagnostic.information.system.ws.model.QisLaboratoryProcedure;
import quest.phil.diagnostic.information.system.ws.model.QisLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.QisMacroColor;
import quest.phil.diagnostic.information.system.ws.model.QisMacroTransparency;
import quest.phil.diagnostic.information.system.ws.model.QisMicroOptions;
import quest.phil.diagnostic.information.system.ws.model.QisPackageType;
import quest.phil.diagnostic.information.system.ws.model.QisPaymentType;
import quest.phil.diagnostic.information.system.ws.model.QisSOAPaymentType;
import quest.phil.diagnostic.information.system.ws.model.QisTransactionStatus;
import quest.phil.diagnostic.information.system.ws.model.QisTransactionType;
import quest.phil.diagnostic.information.system.ws.model.QisUrineChemOptions;
import quest.phil.diagnostic.information.system.ws.model.entity.Country;
import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;
import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPackage;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisRole;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItemLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserProfile;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionChemistry;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionClinicalMicroscopy;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionXRay;
import quest.phil.diagnostic.information.system.ws.repository.CountryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisBranchRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisCorporateRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisDoctorRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisPatientRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionChemistryRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionClinicalMicroscopyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionHematologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionItemLaboratoriesRepositories;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionSerologyRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionXRayRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserPersonelRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserRepository;

@Component
public class AppUtility {
	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
//	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final String SHORTDATE = "yyyyMMdd";
	private final String LONGDATE = "yyyyMMddHHmm";

	@Value("${tax_rate}")
	private String taxRate;

	@Value("${senior_citizen_discount}")
	private String seniorCitizenDiscount;

	@Value("${pwd_discount}")
	private String pwdDiscount;

	@Autowired
	private QisUserRepository qisUserRepository;

	@Autowired
	private QisDoctorRepository qisDoctorRepository;

	@Autowired
	private QisCorporateRepository qisCorporateRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private QisUserPersonelRepository qisUserPersonelRepository;

	@Autowired
	private QisBranchRepository qisBranchRepository;

	@Autowired
	private QisTransactionRepository qisTransactionRepository;

	@Autowired
	private QisTransactionItemLaboratoriesRepositories qisTransactionItemLaboratoriesRepositories;

	@Autowired
	private QisTransactionHematologyRepository qisTransactionHematologyRepository;

	@Autowired
	private QisTransactionChemistryRepository qisTransactionChemistryRepository;

	@Autowired
	private QisTransactionClinicalMicroscopyRepository qisTransactionClinicalMicroscopyRepository;

	@Autowired
	private QisTransactionSerologyRepository qisTransactionSerologyRepository;

	@Autowired
	private QisTransactionXRayRepository qisTransactionXRayRepository;
	
	@Autowired
	private QisPatientRepository qisPatientRepository;

	public String generateUserId(int length) {
		return generateRandomString(length);
	}

	private String generateRandomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}

		return new String(returnValue);
	}

	public String formatUpdateData(String source, String tag, String currentValue, String newValue) {
		String formatedString = source;
		String addedString = tag + " [" + currentValue + "]<" + newValue + ">";

		if (source == null || "".equals(source)) {
			formatedString = addedString;
		} else {
			formatedString += "," + addedString;
		}

		return formatedString;
	}

	public String addToFormatedData(String source, String tag, String addString) {
		String formatedString = source;
		String addedString = tag + addString;
		if (source == null || "".equals(source)) {
			formatedString = addedString;
		} else {
			formatedString += "," + addedString;
		}
		return formatedString;
	}

	public Calendar stringToCalendarDate(String strDate, String strFormat) {
		try {
			DateFormat df = new SimpleDateFormat(strFormat);
			Calendar cal = Calendar.getInstance();
			cal.setTime(df.parse(strDate));

			return cal;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public String calendarToFormatedDate(Calendar calendar, String strFormat) {
		if (calendar != null) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(strFormat);
				return format.format(calendar.getTime());
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return null;
	}

	public String dateToFormatedDate(Date date, String strFormat) {
		if (date != null) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(strFormat);
				return format.format(date);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return null;
	}

	public String amountFormat(Double amount, String format) {
		String value = null;

		if (amount != null) {
			DecimalFormat df = new DecimalFormat(format);
			try {
				value = df.format(amount);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return value;
	}

	public String floatFormat(Float amount, String format) {
		String value = null;

		if (amount != null) {
			DecimalFormat df = new DecimalFormat(format);
			try {
				value = df.format(amount);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return value;
	}

	public String numberFormat(Long number, String format) {
		String value = null;

		if (number != null) {
			DecimalFormat df = new DecimalFormat(format);
			try {
				value = df.format(number);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return value;
	}

	public String userShortDate(Object object) {
		if (object != null) {
			return shortDate(object, "yyyy-MMM-dd");
		}
		return null;
	}

	public String appShortDate(Object object) {
		if (object != null) {
			return shortDate(object, "yyyy-MM-dd");
		}
		return null;
	}

	private String shortDate(Object object, String dateFormat) {
		if (object != null) {
			if (object.getClass() == Date.class) {
				return dateToFormatedDate((Date) object, dateFormat);
			} else if (object.getClass() == Calendar.class) {
				return calendarToFormatedDate((Calendar) object, dateFormat);
			} else if (object.getClass() == GregorianCalendar.class) {
				return calendarToFormatedDate((Calendar) object, dateFormat);
			}
		}
		return null;
	}

	public String getTransactionItemDescription(QisTransactionItem item) {
		String itemName = null;

		if (item != null) {
			if ("ITM".equals(item.getItemType())) {
				QisItem itm = (QisItem) item.getItemDetails();
				itemName = itm.getItemDescription();
			} else if ("PCK".equals(item.getItemType())) {
				QisPackage pck = (QisPackage) item.getItemDetails();
				itemName = pck.getPackageDescription();
			}
		}

		return itemName;
	}
	
	public QisPatient getQisPatient(String patientId) {
		return qisPatientRepository.findByPatientid(patientId);
	}

	public QisTransaction getQisTransaction(String transactionid) {
		return qisTransactionRepository.findByTransactionid(transactionid);
	}

	public QisTransactionItemLaboratories getQisTransactionItemLaboratories(Long transactionId,
			Long transactionItemId) {
		return qisTransactionItemLaboratoriesRepositories.getTransactionItemLaboratoriesById(transactionId,
				transactionItemId);
	}

	public QisTransactionHematology getQisTransactionHematology(Long transactionId, Long laboratoryId) {
		return qisTransactionHematologyRepository.getTransactionLaboratoryRequestById(transactionId, laboratoryId);
	}

	public QisTransactionChemistry getQisTransactionChemistry(Long transactionId, Long laboratoryId) {
		return qisTransactionChemistryRepository.getTransactionLaboratoryRequestById(transactionId, laboratoryId);
	}

	public QisTransactionClinicalMicroscopy getQisTransactionClinicalMicroscopy(Long transactionId, Long laboratoryId) {
		return qisTransactionClinicalMicroscopyRepository.getTransactionLaboratoryRequestById(transactionId,
				laboratoryId);
	}

	public QisTransactionSerology getQisTransactionSerology(Long transactionId, Long laboratoryId) {
		return qisTransactionSerologyRepository.getTransactionLaboratoryRequestById(transactionId, laboratoryId);
	}

	public QisTransactionXRay getQisTransactionXRay(Long transactionId, Long laboratoryId) {
		return qisTransactionXRayRepository.getTransactionLaboratoryRequestById(transactionId, laboratoryId);
	}

	public QisUser getQisUserByUserId(String userId) {
		return qisUserRepository.findByUserid(userId);
	}

	public QisDoctor getQisDoctorByDoctorId(String doctorId) {
		return qisDoctorRepository.findByDoctorid(doctorId);
	}

	public QisCorporate getQisCorporateByCorporateId(String corporateId) {
		return qisCorporateRepository.findByCorporateid(corporateId);
	}

	public Country getCountryByCountryId(Long id) {
		return countryRepository.findByCountryId(id);
	}

	public QisUserPersonel getQisUserPersonelByUserId(String userId) {
		return qisUserPersonelRepository.findByUserid(userId);
	}

	public QisBranch getQisBranchByBranchId(String branchId) {
		return qisBranchRepository.findByBranchid(branchId);
	}

	public String getLaboratoryName(String laboratoryCode) {
		Map<String, String> laboratories = QisLaboratoryName.getLaboratories();
		if (laboratories.containsKey(laboratoryCode)) {
			return laboratories.get(laboratoryCode);
		}
		return null;
	}

	public String getLaboratoryProcedure(String procedureCode) {
		Map<String, String> procedures = QisLaboratoryProcedure.getProcedures();
		if (procedures.containsKey(procedureCode)) {
			return procedures.get(procedureCode);
		}
		return null;
	}

	public String getLaboratoryServices(String serviceCode) {
		Map<String, String> services = QisLaboratoryRequest.getServiceRequestList();
		if (services.containsKey(serviceCode)) {
			return services.get(serviceCode);
		}
		return null;
	}

	public String getCategoryName(String categoryCode) {
		Map<String, String> categories = QisCategoryName.getCategories();
		if (categories.containsKey(categoryCode)) {
			return categories.get(categoryCode);
		}
		return null;
	}

	public String getPackageType(String packageCode) {
		Map<String, String> packageTypes = QisPackageType.getPackageTypes();
		if (packageTypes.containsKey(packageCode)) {
			return packageTypes.get(packageCode);
		}
		return null;
	}

	public String getCurrencyType(String currencyCode) {
		Map<String, String> currencyTypes = QisCurrencyType.getCurrencyTypes();
		if (currencyTypes.containsKey(currencyCode)) {
			return currencyTypes.get(currencyCode);
		}
		return null;
	}

	public String getTransactionStatus(String statusCode) {
		Map<String, String> transactionStatus = QisTransactionStatus.getTransactionStatusList();
		if (transactionStatus.containsKey(statusCode)) {
			return transactionStatus.get(statusCode);
		}
		return null;
	}

	public String getTransactionType(String typeCode) {
		Map<String, String> transactionTypes = QisTransactionType.getTransactionTypes();
		if (transactionTypes.containsKey(typeCode)) {
			return transactionTypes.get(typeCode);
		}
		return null;
	}

	public String getPaymentType(String paymentType) {
		Map<String, String> paymentTypes = QisPaymentType.getPaymentTypes();
		if (paymentTypes.containsKey(paymentType)) {
			return paymentTypes.get(paymentType);
		}
		return null;
	}

	public String getSOAPaymentType(String paymentType) {
		Map<String, String> paymentTypes = QisSOAPaymentType.getPaymentTypes();
		if (paymentTypes.containsKey(paymentType)) {
			return paymentTypes.get(paymentType);
		}
		return null;
	}

	public String getPaymentMode(String paymentMode) {
		String mode = null;

		if (paymentMode != null) {
			switch (paymentMode) {
			case "CA":
				mode = "CASH";
				break;
			case "CC":
				mode = "CREDIT CARD";
				break;
			case "DB":
				mode = "BANK DEPOSIT";
				break;
			case "CQ":
				mode = "CHEQUE";
				break;
			case "ACCT":
				mode = "ACCOUNT";
				break;
			case "HMO":
				mode = "HMO";
				break;
			case "APE":
				mode = "APE";
				break;
			case "MMO":
				mode = "MEDICAL MISSION";
				break;
			case "GCA":
				mode = "G-CASH";
				break;
			case "PMA":
				mode = "PAYMAYA";
				break;
			case "WT":
				mode = "WIRE TRANSFER";
				break;
			case "PMO":
				mode = "PAYMONGO";
				break;
			default:
				break;
			}
		}
		return mode;
	}

	public String getDiscountType(String discountType) {
		Map<String, String> discountTypes = QisDiscountType.getDiscountTypes();
		if (discountTypes.containsKey(discountType)) {
			return discountTypes.get(discountType);
		}
		return null;
	}

	public String getDispatchType(String dispatchType) {
		Map<String, String> discountTypes = QisDispatchType.getDispatchTypes();
		if (discountTypes.containsKey(dispatchType)) {
			return discountTypes.get(dispatchType);
		}
		return null;
	}

	public String getMacroColor(String color) {
		Map<String, String> categories = QisMacroColor.getCategories();
		if (categories.containsKey(color)) {
			return categories.get(color);
		}
		return null;
	}

	public String getMacroTransparency(String type) {
		Map<String, String> list = QisMacroTransparency.getTransparencyList();
		if (list.containsKey(type)) {
			return list.get(type);
		}
		return null;
	}

	public String getMicroOptions(String option) {
		Map<String, String> list = QisMicroOptions.getOptionList();
		if (list.containsKey(option)) {
			return list.get(option);
		}
		return null;
	}

	public String getUrineChemOptions(String option) {
		Map<String, String> list = QisUrineChemOptions.getOptionList();
		if (list.containsKey(option)) {
			return list.get(option);
		}
		return null;
	}

	public String getFecalysisColor(String color) {
		Map<String, String> list = QisFecalysisColor.getColorTypeList();
		if (list.containsKey(color)) {
			return list.get(color);
		}
		return null;
	}

	public String getFecalysisConsistency(String type) {
		Map<String, String> list = QisFecalysisConsistency.getConsistencyList();
		if (list.containsKey(type)) {
			return list.get(type);
		}
		return null;
	}

	public String getClassification(String classType) {
		Map<String, String> classes = QisClassifications.getClassTypeLists();
		if (classes.containsKey(classType)) {
			return classes.get(classType);
		}
		return null;
	}

	public String getChargeType(String chargeType) {
		Map<String, String> types = QisChargeType.getChargeTypes();
		if (types.containsKey(chargeType)) {
			return types.get(chargeType);
		}
		return null;
	}

	public String getBankType(String bankCode) {
		Map<String, String> types = QisBankType.getBankTypes();
		if (types.containsKey(bankCode)) {
			return types.get(bankCode);
		}
		return null;
	}

	public Set<String> getStrUserRoles(Set<QisRole> objRoles) {
		Set<String> roles = new HashSet<>();

		if (objRoles != null) {
			objRoles.forEach(role -> {
				String strRole = role.getName().name();
				if (strRole != null && strRole.startsWith("ROLE_")) {
					roles.add(strRole.substring(5));
				}
			});
		}

		return roles;
	}

	public String getUsername(QisUser user) {
		return user.getUsername();
	}

	public String getPatientFullname(QisPatient patient) {
		String middleName = "";
		if (patient.getMiddlename() != null) {
			middleName = " " + patient.getMiddlename();
		}
		return patient.getLastname() + ", " + patient.getFirstname() + middleName;
	}

	public Float parseFloatAmount(String amount) {
		Float value = null;

		try {
			value = Float.parseFloat(amount);
		} catch (Exception e) {
			System.out.println(e);
		}

		return value;
	}

	public Double parseDoubleAmount(String amount) {
		Double value = null;

		try {
			value = Double.parseDouble(amount);
		} catch (Exception e) {
			System.out.println(e);
		}

		return value;
	}

	public Integer parseIngerValue(String amount) {
		Integer value = null;

		try {
			value = Integer.parseInt(amount);
		} catch (Exception e) {
			System.out.println(e);
		}

		return value;
	}

	public int getTaxRate() {
		Integer tr = parseIngerValue(this.taxRate);
		if (tr == null || tr < 0) {
			return 0;
		} else {
			return tr;
		}
	}

	public int getSeniorCitizenDiscount() {
		Integer scd = parseIngerValue(this.seniorCitizenDiscount);
		if (scd == null || scd < 0) {
			return 0;
		} else {
			return scd;
		}
	}

	public int getPWDDiscount() {
		Integer pwd = parseIngerValue(this.pwdDiscount);
		if (pwd == null || pwd < 0) {
			return 0;
		} else {
			return pwd;
		}
	}

	public Integer calculateAgeInYear(Calendar dateOfBirth, Calendar dateNow) {
		Integer age = null;

		if (dateOfBirth != null && dateNow != null) {
			LocalDate local = LocalDate.of(dateOfBirth.get(Calendar.YEAR), dateOfBirth.get(Calendar.MONTH) + 1,
					dateOfBirth.get(Calendar.DATE));
			LocalDate now = LocalDate.of(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH) + 1,
					dateNow.get(Calendar.DATE));
			Period difference = Period.between(local, now);
			age = difference.getYears();
		}

		return age;
	}

	public String getYesNo(Boolean option) {
		String value = "NO";

		if (option != null && option) {
			value = "YES";
		}

		return value;
	}

	public String getPositiveNegative(Boolean option) {
		String value = "--";

		if (option != null) {
			if (option) {
				value = "POSITIVE";
			} else {
				value = "NEGATIVE";
			}
		}

		return value;
	}

	public String getReactiveNonReactive(Boolean option) {
		String value = "--";

		if (option != null) {
			if (option) {
				value = "REACTIVE";
			} else {
				value = "NON-REACTIVE";
			}
		}

		return value;
	}

	public String centimeterToFeetInches(double cm) {
		String value = "";

		if (cm > 0) {
			double inch = cm / 2.54;
			int feet = Math.floorDiv((int) inch, 12);
			inch = inch - ((double) feet * 12);
			value = feet + "ft" + (int) inch + "in";
		}

		return value;
	}

	public double roundToPlaces(double value, double places) {
		double amount = value;

		if (value > 0) {
			amount = Math.round(value * places) / places;
		}

		return amount;
	}

	public double kilogramToPounds(double kilo) {
		double pounds = kilo;

		if (kilo > 0) {
			pounds = kilo / 0.45359237;
			pounds = roundToPlaces(pounds, 10);
		}

		return pounds;
	}

	public String getDoctorsDisplayName(QisDoctor doctor) {
		String name = "";

		if (doctor != null) {
			String middle = "";
			if (doctor.getMiddlename() != null && !"".equals(doctor.getMiddlename().trim())) {
				middle = String.valueOf(doctor.getMiddlename().trim().toUpperCase().charAt(0)) + ".";
			}

			name = doctor.getFirstname();
			if (!"".equals(middle)) {
				name = name + " " + middle;
			}

			name = name + " " + doctor.getLastname();

			if (doctor.getSuffix() != null && !"".equals(doctor.getSuffix())) {
				name = name + ", " + doctor.getSuffix();
			}
		}

		return name;
	}

	public String getMedicalTechnologyDisplayName(QisUserPersonel medTech) {
		String name = "";

		if (medTech != null) {
			QisUserProfile profile = medTech.getProfile();
			if (profile != null) {
				String middle = "";
				if (profile.getMiddlename() != null && !"".equals(profile.getMiddlename().trim())) {
					middle = String.valueOf(profile.getMiddlename().trim().toUpperCase().charAt(0)) + ".";
				}

				name = profile.getFirstname();
				if (!"".equals(middle)) {
					name = name + " " + middle;
				}

				name = name + " " + profile.getLastname();

				if (profile.getSuffix() != null && !"".equals(profile.getSuffix())) {
					name = name + ", " + profile.getSuffix();
				}
			} else {
				name = "Please update user profile [" + medTech.getUsername() + "]";
			}
		}

		return name;
	}

	public String getMedicalTechnologyLicenseNo(QisUserPersonel medTech) {
		String licenseNo = "";

		if (medTech != null) {
			QisUserProfile profile = medTech.getProfile();
			if (profile != null) {
				if (profile.getLicenseNumber() != null && !"".equals(profile.getLicenseNumber().trim())) {
					licenseNo = "LIC NO. " + profile.getLicenseNumber();
				}
			}
		}

		return licenseNo;
	}

	public String getUserPersonelFullName(QisUserPersonel personel) {
		String name = "";

		if (personel != null) {
			QisUserProfile profile = personel.getProfile();
			if (profile != null) {
				String middle = "";
				if (profile.getMiddlename() != null && !"".equals(profile.getMiddlename().trim())) {
					middle = String.valueOf(profile.getMiddlename().trim().toUpperCase().charAt(0)) + ".";
				}

				name = profile.getFirstname();
				if (!"".equals(middle)) {
					name = name + " " + middle;
				}

				name = name + " " + profile.getLastname();
			} else {
				name = "Please update user profile [" + personel.getUsername() + "]";
			}
		}

		return name;
	}

	// VALIDATIONS
	public void validateTransactionDate(String transactionDate) throws Exception {
		if (transactionDate.length() != SHORTDATE.length()) {
			throw new RuntimeException("Invalid Date[" + transactionDate + "] use (YYYYMMDD) format",
					new Throwable("transactionDate: Invalid Date[" + transactionDate + "] use (YYYYMMDD) format"));
		}
		Calendar txnDate = stringToCalendarDate(transactionDate, SHORTDATE);
		if (txnDate == null) {
			throw new RuntimeException("Invalid Date[" + transactionDate + "] use (YYYYMMDD) format",
					new Throwable("transactionDate: Invalid Date[" + transactionDate + "] use (YYYYMMDD) format"));
		}
	}

	public void validateDateFromTo(String dateFrom, String dateTo) throws Exception {
		if (dateFrom == null) {
			throw new RuntimeException("No Date From specified", new Throwable("dateFrom: No Date From specified"));
		}

		if (dateTo == null) {
			throw new RuntimeException("No Date To specified", new Throwable("dateTo: No Date To specified"));
		}

		if (dateFrom.length() != SHORTDATE.length()) {
			throw new RuntimeException("Invalid Date From[" + dateFrom + "] use (" + SHORTDATE + ") format",
					new Throwable("dateFrom: Invalid Date From[" + dateFrom + "] use (" + SHORTDATE + ") format"));
		}

		if (dateTo.length() != SHORTDATE.length()) {
			throw new RuntimeException("Invalid Date To[" + dateTo + "] use (" + SHORTDATE + ") format",
					new Throwable("dateTo: Invalid Date To[" + dateFrom + "] use (" + SHORTDATE + ") format"));
		}

		Calendar txnDateFrom = stringToCalendarDate(dateFrom, SHORTDATE);
		if (txnDateFrom == null) {
			throw new RuntimeException("Invalid Date From[" + dateFrom + "] use (" + SHORTDATE + ") format",
					new Throwable("dateFrom: Invalid Date From[" + dateFrom + "] use (" + SHORTDATE + ") format"));
		}

		Calendar txnDateTo = stringToCalendarDate(dateTo, SHORTDATE);
		if (txnDateTo == null) {
			throw new RuntimeException("Invalid Date To[" + dateTo + "] use (" + SHORTDATE + ") format",
					new Throwable("dateTo: Invalid Date To[" + dateFrom + "] use (" + SHORTDATE + ") format"));
		}
	}

	public void validateDateTimeFromTo(String dateTimeFrom, String dateTimeTo) throws Exception {
		if (dateTimeFrom == null) {
			throw new RuntimeException("No DateTime From specified",
					new Throwable("dateTimeFrom: No DateTime From specified"));
		}

		if (dateTimeTo == null) {
			throw new RuntimeException("No DateTime To specified",
					new Throwable("dateTimeTo: No DateTime To specified"));
		}

		if (dateTimeFrom.length() != LONGDATE.length()) {
			throw new RuntimeException("Invalid DateTime From[" + dateTimeFrom + "] use (" + LONGDATE + ") format",
					new Throwable(
							"dateTimeFrom: Invalid DateTime From[" + dateTimeFrom + "] use (" + LONGDATE + ") format"));
		}

		if (dateTimeTo.length() != LONGDATE.length()) {
			throw new RuntimeException("Invalid DateTime To[" + dateTimeTo + "] use (" + LONGDATE + ") format",
					new Throwable(
							"dateTimeTo: Invalid DateTime From[" + dateTimeTo + "] use (" + LONGDATE + ") format"));
		}

		Calendar txnDateTimeFrom = stringToCalendarDate(dateTimeFrom, LONGDATE);
		if (txnDateTimeFrom == null) {
			throw new RuntimeException("Invalid DateTime From[" + dateTimeFrom + "] use (" + LONGDATE + ") format",
					new Throwable(
							"dateTimeFrom: Invalid DateTime From[" + dateTimeFrom + "] use (" + LONGDATE + ") format"));
		}

		Calendar txnDateTimeTo = stringToCalendarDate(dateTimeTo, LONGDATE);
		if (txnDateTimeTo == null) {
			throw new RuntimeException("Invalid DateTime To[" + dateTimeTo + "] use (" + LONGDATE + ") format",
					new Throwable(
							"dateTimeTo: Invalid DateTime From[" + dateTimeTo + "] use (" + LONGDATE + ") format"));
		}
	}

	public Boolean TrueOrFalse(Boolean data) {
		Boolean trueOrFalse = false;
		if (data == true) {
			trueOrFalse = true;
		}
		return trueOrFalse;

	}

}
