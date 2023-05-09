package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransaction;

public interface QisQualityTransactionRepository extends JpaRepository<QisQualityTransaction, Long>{
	QisQualityTransaction findByTransactionid(String transactionid);

	Boolean existsByTransactionid(String transactionid);

	// TRANSACTION DATE
	@Query("SELECT tl FROM QisQualityTransaction tl " 
			+ "WHERE DATE(tl.transactionDate) = DATE(:transactionDate) "
			+ "AND tl.status = 'SPD' " 
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDate(
			@Param("transactionDate") Calendar transactionDate, Pageable pageable);
	
	// TRANSACTION DATEFROM AND DATETO
	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "WHERE DATE(tl.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateRange(@Param("dateFrom") Calendar dateFrom,
			@Param("dateTo") Calendar dateTo, Pageable pageable);

	// TRANSACTION DATETIMEFROM AND DATETIMETO
	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "WHERE tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateTimeRange(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable);
	
	
	// BY PATIENT
		@Query("SELECT tl FROM QisQualityTransaction tl " 
				+ "WHERE tl.patientId = :patientId "
				+ "AND tl.status = 'SPD' " 
				+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
				+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
				+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
				+ "ORDER BY tl.transactionDate")
		Page<QisQualityTransaction> getTransactionsByPatient(@Param("patientId") Long patientId,
				Pageable pageable);
	
	
	// BY LABORATORY & BRANCH
	@Query("SELECT tl FROM QisQualityTransaction tl " 
			+ "WHERE DATE(tl.transactionDate) = DATE(:transactionDate) "
			+ "AND tl.status = 'SPD' " 
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tl.branchId = :branchId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateBranch(
			@Param("transactionDate") Calendar transactionDate, @Param("branchId") Long branchId,
			Pageable pageable);

	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "WHERE DATE(tl.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tl.branchId = :branchId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateRangeBranch(@Param("dateFrom") Calendar dateFrom,
			@Param("dateTo") Calendar dateTo, @Param("branchId") Long branchId, Pageable pageable);

	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "WHERE tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tl.branchId = :branchId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateTimeRangeBranch(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			@Param("branchId") Long branchId, Pageable pageable);
	
	
	
	// BY LABORATORY & BILLER
	@Query("SELECT tl FROM QisQualityTransaction tl " 
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE DATE(tl.transactionDate) = DATE(:transactionDate) "
			+ "AND tl.status = 'SPD' " 
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateBiller(
			@Param("transactionDate") Calendar transactionDate, @Param("billerId") Long billerId, 
			Pageable pageable);
	
	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE DATE(tl.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateRangeBiller(@Param("dateFrom") Calendar dateFrom,
			@Param("dateTo") Calendar dateTo, @Param("billerId") Long billerId, Pageable pageable);

	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateTimeRangeBiller(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			@Param("billerId") Long billerId, Pageable pageable);
	
	//BY LABORATORY	& COMPANY
	@Query("SELECT tl FROM QisQualityTransaction tl " 
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE DATE(tl.transactionDate) = DATE(:transactionDate) "
			+ "AND tl.status = 'SPD' " 
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateCompany(
			@Param("transactionDate") Calendar transactionDate, @Param("billerId") Long billerId, 
			Pageable pageable);
	
	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE DATE(tl.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateRangeCompany(@Param("dateFrom") Calendar dateFrom,
			@Param("dateTo") Calendar dateTo, @Param("billerId") Long billerId, Pageable pageable);
	
	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateTimeRangeCompany(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			Pageable pageable);
	
	// BY LABORATORY & BRANCH & COMPANY
	@Query("SELECT tl FROM QisQualityTransaction tl " 
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE DATE(tl.transactionDate) = DATE(:transactionDate) "
			+ "AND tl.status = 'SPD' " 
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tl.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateBranchCompany(
			@Param("transactionDate") Calendar transactionDate, @Param("branchId") Long branchId,
			@Param("billerId") Long companyId, Pageable pageable);
	
	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE DATE(tl.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tl.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateRangeBranchComapny(@Param("dateFrom") Calendar dateFrom,
			@Param("dateTo") Calendar dateTo, @Param("branchId") Long branchId, @Param("billerId") Long billerId, 
			Pageable pageable);
	
	
	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tl.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateTimeRangeBranchCompany(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			@Param("branchId") Long branchId, @Param("billerId") Long billerId, Pageable pageable);
	
	
	
	// BY LABORATORY & BRANCH & BILLER
	@Query("SELECT tl FROM QisQualityTransaction tl " 
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE DATE(tl.transactionDate) = DATE(:transactionDate) "
			+ "AND tl.status = 'SPD' " 
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tl.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateBranchBiller(
			@Param("transactionDate") Calendar transactionDate, @Param("branchId") Long branchId,
			@Param("billerId") Long billerId, Pageable pageable);
	
	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE DATE(tl.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tl.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateRangeBranchBiller(@Param("dateFrom") Calendar dateFrom,
			@Param("dateTo") Calendar dateTo, @Param("branchId") Long branchId, @Param("billerId") Long billerId, 
			Pageable pageable);
	
	@Query("SELECT tl FROM QisQualityTransaction tl "
			+ "INNER JOIN QisTransactionPayment tp ON tl.id = tp.transactionid "
			+ "WHERE tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "AND tl.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY tl.transactionDate")
	Page<QisQualityTransaction> getTransactionsByTransactionDateTimeRangeBranchBiller(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			@Param("branchId") Long branchId, @Param("billerId") Long billerId, Pageable pageable);
	
	
	@Query("SELECT tl FROM QisQualityTransaction tl " 
			+ "WHERE tl.status = 'SPD' " 
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE ti.classification = 'E' AND tlr.transactionid = tl.id AND ti.status = 1 ) > 0 " 
			+ "ORDER BY tl.transactionDate")
	List<QisQualityTransaction> getAllPendingTransaction();
	
	@Query("SELECT tl FROM QisQualityTransaction tl " 
			+ "WHERE tl.status = 'SPD' AND tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo  " 
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE ti.classification = 'E' AND tlr.transactionid = tl.id AND ti.status = 1) > 0 " 
			+ "ORDER BY tl.transactionDate")
	List<QisQualityTransaction> getPendingTransactionByDate(@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo);

}
