package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryDetails;

@Repository
public interface QisTransactionLaboratoryDetailsRepository
		extends JpaRepository<QisTransactionLaboratoryDetails, Long> {

	@Query("SELECT tl FROM QisTransactionLaboratoryDetails tl WHERE tl.transactionid = :transactionid AND tl.id = :id")
	QisTransactionLaboratoryDetails getTransactionLaboratoryDetailsById(@Param("transactionid") Long transactionid, @Param("id") Long id);
	
	// BY LABORATORY
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateLaboratory(
			@Param("transactionDate") Calendar transactionDate, @Param("laboratory") String laboratory,
			Pageable pageable);

	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeLaboratory(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("laboratory") String laboratory, Pageable pageable);
	
	//Get Transaction per id
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE ts.status = 'SPD' "
			+ "AND tlr.id =:id "
			+ "AND ti.status = 1 "
			+ "ORDER BY ts.transactionDate")
	QisTransactionLaboratoryDetails getTransactionsById(@Param("id") Long id);

	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeLaboratory(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("laboratory") String laboratory, Pageable pageable);

	
	// BY LABORATORY & BRANCH
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateBranchLaboratory(
			@Param("transactionDate") Calendar transactionDate, @Param("laboratory") String laboratory,
			@Param("branchId") Long branchId, Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeBranchLaboratory(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("laboratory") String laboratory, @Param("branchId") Long branchId, Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeBranchLaboratory(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("laboratory") String laboratory, @Param("branchId") Long branchId, Pageable pageable);
	
	
	// BY LABORATORY & BILLER
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateBillerLaboratory(
			@Param("transactionDate") Calendar transactionDate, @Param("laboratory") String laboratory,
			@Param("billerId") Long billerId, Pageable pageable);

	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeBillerLaboratory(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("laboratory") String laboratory, @Param("billerId") Long billerId, Pageable pageable);

	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeBillerLaboratory(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("laboratory") String laboratory, @Param("billerId") Long billerId, Pageable pageable);
	
	
	// BY LABORATORY & BRANCH & BILLER
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateBranchBillerLaboratory(
			@Param("transactionDate") Calendar transactionDate, @Param("laboratory") String laboratory,
			@Param("branchId") Long branchId, @Param("billerId") Long billerId, Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeBranchBillerLaboratory(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("laboratory") String laboratory, @Param("branchId") Long branchId, @Param("billerId") Long billerId, 
			Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemLaboratory = :laboratory "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeBranchBillerLaboratory(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("laboratory") String laboratory, @Param("branchId") Long branchId, @Param("billerId") Long billerId, Pageable pageable);

	
	// BY CATEGORY	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateCategory(
			@Param("transactionDate") Calendar transactionDate, @Param("category") String category, Pageable pageable);

	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeCategory(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("category") String category, Pageable pageable);

	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeCategory(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("category") String category, Pageable pageable);
	

	// BY CATEGORY AND PROCEDURE
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateCategoryProcedure(
			@Param("transactionDate") Calendar transactionDate, @Param("category") String category, 
			@Param("procedure") String procedure, Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeCategoryProcedure(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("category") String category, @Param("procedure") String procedure, Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeCategoryProcedure(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("category") String category, @Param("procedure") String procedure, Pageable pageable);	

	// BRANCH
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateBranchCategory(
			@Param("transactionDate") Calendar transactionDate, @Param("category") String category, 
			@Param("branchId") Long branchId, Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeBranchCategory(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("category") String category, 
			@Param("branchId") Long branchId, Pageable pageable);
	
	// BILLER
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateBillerCategory(
			@Param("transactionDate") Calendar transactionDate, @Param("category") String category, 
			@Param("billerId") Long billerId, Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeBillerCategory(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("category") String category,  
			@Param("billerId") Long billerId, Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeBranchCategory(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("category") String category,  
			@Param("branchId") Long branchId, Pageable pageable);	
	
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeBillerCategory(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("category") String category,  
			@Param("billerId") Long billerId, Pageable pageable);	
	
	
	// BRANCH & BILLER
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateBranchBillerCategory(
			@Param("transactionDate") Calendar transactionDate, @Param("category") String category, 
			@Param("branchId") Long branchId, @Param("billerId") Long billerId, Pageable pageable);
	
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeBranchBillerCategory(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("category") String category, @Param("branchId") Long branchId,
			@Param("billerId") Long billerId, Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeBranchBillerCategory(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("category") String category, @Param("branchId") Long branchId,
			@Param("billerId") Long billerId, Pageable pageable);	
	
	// BRANCH & PROCEDURE
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateBranchCategoryProcedure(
			@Param("transactionDate") Calendar transactionDate, @Param("category") String category, 
			@Param("procedure") String procedure, @Param("branchId") Long branchId, Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeBranchCategoryProcedure(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("category") String category, @Param("procedure") String procedure, 
			@Param("branchId") Long branchId, Pageable pageable);
	

	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeBranchCategoryProcedure(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("category") String category, @Param("procedure") String procedure, 
			@Param("branchId") Long branchId, Pageable pageable);	
	
	// BILLER & PROCEDURE
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateBillerCategoryProcedure(
			@Param("transactionDate") Calendar transactionDate, @Param("category") String category, 
			@Param("procedure") String procedure, @Param("billerId") Long billerId, Pageable pageable);

	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeBillerCategoryProcedure(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("category") String category, @Param("procedure") String procedure, 
			@Param("billerId") Long billerId, Pageable pageable);
	
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeBillerCategoryProcedure(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("category") String category, @Param("procedure") String procedure, 
			@Param("billerId") Long billerId, Pageable pageable);	
	
	// BRANCH & BILLER & PROCEDURE
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) = DATE(:transactionDate) "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateBranchBillerCategoryProcedure(
			@Param("transactionDate") Calendar transactionDate, @Param("category") String category, 
			@Param("procedure") String procedure, @Param("branchId") Long branchId, 
			@Param("billerId") Long billerId, Pageable pageable);
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateRangeBranchBillerCategoryProcedure(
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			@Param("category") String category, @Param("procedure") String procedure, 
			@Param("branchId") Long branchId, @Param("billerId") Long billerId, Pageable pageable);
	
	
	@Query("SELECT tlr FROM QisTransactionLaboratoryDetails tlr "
			+ "INNER JOIN QisTransaction ts ON ts.id = tlr.transactionid "
			+ "INNER JOIN QisItem i ON tlr.itemId = i.id "
			+ "INNER JOIN QisTransactionItem ti ON tlr.transactionItemId = ti.id "
			+ "INNER JOIN QisTransactionPayment tp ON tlr.transactionid = tp.transactionid "
			+ "WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND ts.status = 'SPD' "
			+ "AND i.itemCategory = :category "
			+ "AND i.itemLaboratoryProcedure = :procedure "
			+ "AND ti.itemType = 'ITM' "
			+ "AND ti.status = 1 "
			+ "AND ts.branchId = :branchId "
			+ "AND tp.billerId = :billerId "
			+ "ORDER BY ts.transactionDate ")
	Page<QisTransactionLaboratoryDetails> getTransactionsByTransactionDateTimeRangeBranchBillerCategoryProcedure(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("category") String category, @Param("procedure") String procedure, 
			@Param("branchId") Long branchId, @Param("billerId") Long billerId, Pageable pageable);	
	
}
