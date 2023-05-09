package quest.phil.diagnostic.information.system.ws.common;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;
import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransaction;
import quest.phil.diagnostic.information.system.ws.repository.QisQualityTransactionRepository;

@Component
public class AppQuestQualityUtility {
	private final String SHORTDATE = "yyyyMMdd";
	private final String LONGDATE = "yyyyMMddHHmm";

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisQualityTransactionRepository qisQualityTransactionRepository;

	public Page<QisQualityTransaction> getQisQualityTransactionList(String branchId, String chargeTo, String company,
			String patientId, String transactionDate, String dateFrom, String dateTo, String dateTimeFrom,
			String dateTimeTo, Pageable pageable) throws Exception {
		QisBranch branch = null;
		if (branchId != null) {
			branch = appUtility.getQisBranchByBranchId(branchId);
			if (branch == null) {
				throw new RuntimeException("Invalid branch id.", new Throwable("branchId: Invalid branch id."));
			}
		}

		QisCorporate biller = null;
		if (chargeTo != null) {
			biller = appUtility.getQisCorporateByCorporateId(chargeTo);
			if (biller == null) {
				throw new RuntimeException("Invalid charge to account.",
						new Throwable("chargeTo: Invalid charge to account."));
			}
		}

		QisCorporate byComapny = null;
		if (company != null) {
			byComapny = appUtility.getQisCorporateByCorporateId(company);
			if (byComapny == null) {
				throw new RuntimeException("Invalid Company.", new Throwable("company: Invalid Company."));
			}
		}

		QisPatient patient = null;
		if (patientId != null && !"".equals(patientId)) {
			patient = appUtility.getQisPatient(patientId);
			if (patient == null) {
				throw new RuntimeException("Patient", new Throwable("patient: Invalid Patient."));
			}
		}

		int myQuery = 0;
		if (branch != null) {
			myQuery += 1;
		}

		if (biller != null) {
			myQuery += 2;
		}

		if (byComapny != null) {
			myQuery += 4;
		}

		if (patientId != null && !"".equals(patientId)) {
			return qisQualityTransactionRepository.getTransactionsByPatient(patient.getId(), pageable);
		} else if (transactionDate != null) {
			appUtility.validateTransactionDate(transactionDate);
			Calendar txnDate = appUtility.stringToCalendarDate(transactionDate, SHORTDATE);

			switch (myQuery) {
			case 1: // branch only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateBranch(txnDate, branch.getId(),
						pageable);

			case 2: // biller only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateBiller(txnDate, biller.getId(),
						pageable);

			case 3: // branch & biller only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateBranchBiller(txnDate,
						branch.getId(), biller.getId(), pageable);

			case 4: // company only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateCompany(txnDate,
						byComapny.getId(), pageable);

			case 5: // branch & company only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateBranchCompany(txnDate,
						branch.getId(), byComapny.getId(), pageable);

			default:
				return qisQualityTransactionRepository.getTransactionsByTransactionDate(txnDate, pageable);
			}
		} else if (dateFrom != null || dateTo != null) {
			appUtility.validateDateFromTo(dateFrom, dateTo);
			Calendar txnDateFrom = appUtility.stringToCalendarDate(dateFrom, SHORTDATE);
			Calendar txnDateTo = appUtility.stringToCalendarDate(dateTo, SHORTDATE);

			switch (myQuery) {
			case 1: // branch only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateRangeBranch(txnDateFrom,
						txnDateTo, branch.getId(), pageable);

			case 2: // biller only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateRangeBiller(txnDateFrom,
						txnDateTo, biller.getId(), pageable);

			case 3: // branch & biller only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateRangeBranchBiller(txnDateFrom,
						txnDateTo, branch.getId(), biller.getId(), pageable);
			case 4: // company only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateRangeCompany(txnDateFrom,
						txnDateTo, byComapny.getId(), pageable);

			case 5: // branch & company only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateRangeBranchComapny(txnDateFrom,
						txnDateTo, branch.getId(), byComapny.getId(), pageable);

			default:
				return qisQualityTransactionRepository.getTransactionsByTransactionDateRange(txnDateFrom, txnDateTo,
						pageable);
			}
		} else if (dateTimeFrom != null || dateTimeTo != null) {
			appUtility.validateDateTimeFromTo(dateTimeFrom, dateTimeTo);
			Calendar txnDateTimeFrom = appUtility.stringToCalendarDate(dateTimeFrom, LONGDATE);
			Calendar txnDateTimeTo = appUtility.stringToCalendarDate(dateTimeTo, LONGDATE);

			switch (myQuery) {
			case 1: // branch only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateTimeRangeBranch(txnDateTimeFrom,
						txnDateTimeTo, branch.getId(), pageable);

			case 2: // biller only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateTimeRangeBiller(txnDateTimeFrom,
						txnDateTimeTo, biller.getId(), pageable);

			case 3: // branch & biller only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateTimeRangeBranchBiller(
						txnDateTimeFrom, txnDateTimeTo, branch.getId(), biller.getId(), pageable);

			case 4: // company only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateTimeRangeCompany(txnDateTimeFrom,
						txnDateTimeTo, pageable);

			case 5: // branch & company only
				return qisQualityTransactionRepository.getTransactionsByTransactionDateTimeRangeBranchCompany(
						txnDateTimeFrom, txnDateTimeFrom, branch.getId(), byComapny.getId(), pageable);

			default:
				return qisQualityTransactionRepository.getTransactionsByTransactionDateTimeRange(txnDateTimeFrom,
						txnDateTimeTo, pageable);
			}
		}

		throw new RuntimeException("No parameters added on the request.",
				new Throwable("No parameters added on the request."));
	}
}
