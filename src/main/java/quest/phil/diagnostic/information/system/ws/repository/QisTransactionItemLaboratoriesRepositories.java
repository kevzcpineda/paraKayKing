package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItemLaboratories;

public interface QisTransactionItemLaboratoriesRepositories extends JpaRepository<QisTransactionItemLaboratories, Long>{

	@Query("SELECT til FROM QisTransactionItemLaboratories til WHERE til.transactionid = :transactionid AND til.id = :id")
	QisTransactionItemLaboratories getTransactionItemLaboratoriesById(@Param("transactionid") Long transactionid, @Param("id") Long id);
	
	
	// TRANSACTION DATE
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE DATE(t.transactionDate) = DATE(:transactionDate) "
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDate(
			@Param("transactionDate") Calendar transactionDate, Pageable pageable);

	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE DATE(t.transactionDate) = DATE(:transactionDate) "
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateType(
			@Param("transactionDate") Calendar transactionDate, @Param("itemType") String itemType, Pageable pageable);


	// TRANSACTION DATEFROM AND DATETO
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE DATE(t.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateRange(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo, Pageable pageable);

	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE DATE(t.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateRangeType(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo, 
			@Param("itemType") String itemType, Pageable pageable);
	
	// TRANSACTION DATETIMEFROM AND DATETIMETO
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE t.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateTimeRange(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable);

	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE t.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateTimeRangeType(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			@Param("itemType") String itemType, Pageable pageable);
	
	
	// BRANCH
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE DATE(t.transactionDate) = DATE(:transactionDate) "
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateBranch(
			@Param("transactionDate") Calendar transactionDate,  
			@Param("branchId") Long branchId, Pageable pageable);
	
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE DATE(t.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateRangeBranch(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo, 
			@Param("branchId") Long branchId, Pageable pageable);
	
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE t.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateTimeRangeBranch(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			@Param("branchId") Long branchId, Pageable pageable);
	
	
	
	// BILLER
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE DATE(t.transactionDate) = DATE(:transactionDate) "
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateBiller(
			@Param("transactionDate") Calendar transactionDate,  
			@Param("billerId") Long billerId, Pageable pageable);

	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE DATE(t.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateRangeBiller(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo, 
			@Param("billerId") Long billerId, Pageable pageable);
	
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE t.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateTimeRangeBiller(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			@Param("billerId") Long billerId, Pageable pageable);
	
	
	
	// BRANCH & BILLER
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE DATE(t.transactionDate) = DATE(:transactionDate) "
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateBranchBiller(
			@Param("transactionDate") Calendar transactionDate, @Param("branchId") Long branchId,
			@Param("billerId") Long billerId, Pageable pageable);
	
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE DATE(t.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateRangeBranchBiller(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo, 
			@Param("branchId") Long branchId, @Param("billerId") Long billerId, Pageable pageable);
	
	
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE t.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateTimeRangeBranchBiller(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			@Param("branchId") Long branchId, @Param("billerId") Long billerId, Pageable pageable);
	
	
	// BRANCH & ITEMTYPE
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE DATE(t.transactionDate) = DATE(:transactionDate) "
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateBranchType(
			@Param("transactionDate") Calendar transactionDate, @Param("itemType") String itemType, 
			@Param("branchId") Long branchId, Pageable pageable);

	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE DATE(t.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateRangeBranchType(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo, 
			@Param("itemType") String itemType, @Param("branchId") Long branchId, Pageable pageable);
	
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "WHERE t.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateTimeRangeBranchType(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			@Param("itemType") String itemType, @Param("branchId") Long branchId, Pageable pageable);
	
	
	// BILLER & ITEMTYPE
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE DATE(t.transactionDate) = DATE(:transactionDate) "
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateBillerType(
			@Param("transactionDate") Calendar transactionDate, @Param("itemType") String itemType, 
			@Param("billerId") Long billerId, Pageable pageable);

	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE DATE(t.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateRangeBillerType(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo, 
			@Param("itemType") String itemType, @Param("billerId") Long billerId, Pageable pageable);
	
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE t.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateTimeRangeBillerType(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			@Param("itemType") String itemType, @Param("billerId") Long billerId, Pageable pageable);
	
	
	// BRANCH & BILLER & ITEMTYPE
	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE DATE(t.transactionDate) = DATE(:transactionDate) "
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateBranchBillerType(
			@Param("transactionDate") Calendar transactionDate, @Param("itemType") String itemType, 
			@Param("branchId") Long branchId, @Param("billerId") Long billerId, Pageable pageable);

	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE DATE(t.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateRangeBranchBillerType(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo, 
			@Param("itemType") String itemType, @Param("branchId") Long branchId, 
			@Param("billerId") Long billerId, Pageable pageable);

	@Query("SELECT til FROM QisTransactionItemLaboratories til " 
			+ "INNER JOIN QisTransactionInfo t ON t.id = til.transactionid "
			+ "INNER JOIN QisTransactionPayment tp ON til.transactionid = tp.transactionid "
			+ "WHERE t.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND t.status = 'SPD' " 
			+ "AND til.itemType = :itemType "
			+ "AND til.status = 1 " 			
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "WHERE tlr.transactionItemId = til.id) > 0 " 
			+ "AND t.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY t.transactionDate")
	Page<QisTransactionItemLaboratories> getTransactionItemsByTransactionDateTimeRangeBranchBillerType(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, 
			@Param("itemType") String itemType, @Param("branchId") Long branchId, 
			@Param("billerId") Long billerId, Pageable pageable);
	
	
}
