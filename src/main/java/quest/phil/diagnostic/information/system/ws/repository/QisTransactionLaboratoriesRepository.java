package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionLaboratories;

public interface QisTransactionLaboratoriesRepository extends JpaRepository<QisTransactionLaboratories, Long> {
	QisTransactionLaboratories findByTransactionid(String transactionid);

	Boolean existsByTransactionid(String transactionid);

	@Query("SELECT tl FROM QisTransactionLaboratories tl WHERE tl.id = :id")
	QisTransactionLaboratories getTransactionById(@Param("id") Long id);

	// TRANSACTION DATE
	@Query("SELECT tl FROM QisTransactionLaboratories tl " + "WHERE DATE(tl.transactionDate) = DATE(:transactionDate) "
			+ "AND tl.status = 'SPD' " + "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 "
//			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND ti.itemType = 'PCK') > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisTransactionLaboratories> getTransactionsByTransactionDate(
			@Param("transactionDate") Calendar transactionDate, Pageable pageable);

	@Query("SELECT tl FROM QisTransactionLaboratories tl " + "WHERE DATE(tl.transactionDate) = DATE(:transactionDate) "
			+ "AND tl.status = 'SPD' " + "AND tl.transactionType = :transactionType "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 "
//			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND ti.itemType = 'PCK') > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisTransactionLaboratories> getTransactionsByTransactionDateType(
			@Param("transactionDate") Calendar transactionDate, @Param("transactionType") String transactionType,
			Pageable pageable);

	@Query("SELECT tl FROM QisTransactionLaboratories tl " + "WHERE DATE(tl.transactionDate) = DATE(:transactionDate) "
			+ "AND tl.status = 'SPD' " + "AND tl.transactionType <> :notTransactionType "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 "
//			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND ti.itemType = 'PCK') > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisTransactionLaboratories> getTransactionsByTransactionDateNotType(
			@Param("transactionDate") Calendar transactionDate, @Param("notTransactionType") String notTransactionType,
			Pageable pageable);

	// TRANSACTION DATEFROM AND DATETO
	@Query("SELECT tl FROM QisTransactionLaboratories tl "
			+ "WHERE DATE(tl.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 "
//			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND ti.itemType = 'PCK') > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisTransactionLaboratories> getTransactionsByTransactionDateRange(@Param("dateFrom") Calendar dateFrom,
			@Param("dateTo") Calendar dateTo, Pageable pageable);

	@Query("SELECT tl FROM QisTransactionLaboratories tl "
			+ "WHERE DATE(tl.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND tl.status = 'SPD' "
			+ "AND tl.transactionType = :transactionType "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 "
//			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND ti.itemType = 'PCK') > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisTransactionLaboratories> getTransactionsByTransactionDateRangeType(@Param("dateFrom") Calendar dateFrom,
			@Param("dateTo") Calendar dateTo, @Param("transactionType") String transactionType, Pageable pageable);

	@Query("SELECT tl FROM QisTransactionLaboratories tl "
			+ "WHERE DATE(tl.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) " + "AND tl.status = 'SPD' "
			+ "AND tl.transactionType <> :notTransactionType "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 "
//			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND ti.itemType = 'PCK') > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisTransactionLaboratories> getTransactionsByTransactionDateRangeNotType(@Param("dateFrom") Calendar dateFrom,
			@Param("dateTo") Calendar dateTo, @Param("notTransactionType") String notTransactionType,
			Pageable pageable);

	// TRANSACTION DATETIMEFROM AND DATETIMETO
	@Query("SELECT tl FROM QisTransactionLaboratories tl "
			+ "WHERE tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " + "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 "
//			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND ti.itemType = 'PCK') > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisTransactionLaboratories> getTransactionsByTransactionDateTimeRange(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable);

	// TRANSACTION
	@Query("SELECT tl FROM QisTransactionLaboratories tl "
			+ "WHERE tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND tlr.sopStatus = 0) > 0 "
			+ "ORDER BY tl.transactionDate")
	Page<QisTransactionLaboratories> getTransactionsByTransaction(@Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable);
	
	// TRANSACTION with ReferenceLab
	@Query("SELECT tl FROM QisTransactionLaboratories tl "
			+ "WHERE tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "AND tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "INNER JOIN QisTransactionLabSerology tls ON tls.id = tlr.id "
			+ "INNER JOIN QisTransactionLabThyroid tlt ON tlt.id = tlr.id "
			+ "WHERE (tlr.transactionid = tl.id AND ti.status = 1 AND tls.referenceLabId = :referenceLab) "
			+ "OR (tlr.transactionid = tl.id AND ti.status = 1 AND tlt.referenceLabId = :referenceLab)) > 0 "
			+ "ORDER BY tl.transactionDate")
	Page<QisTransactionLaboratories> getTransactionsByTransactionWithReference(@Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo, @Param("referenceLab") Long referenceLab, Pageable pageable);

	//PER ID TRANSACTION
	@Query("SELECT tl FROM QisTransactionLaboratories tl "
			+ "WHERE tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND tlr.sopStatus = 0 AND tlr.transactionid = :id) > 0 ")
	QisTransactionLaboratories getTransactionsRequestById(@Param("id") Long id);
	
	//LIST PER TRANSACTION REQUEST
	@Query("SELECT tl FROM QisTransactionLaboratories tl "
			+ "WHERE tl.status = 'SPD' "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND tlr.sopStatus = 0 AND tlr.transactionid = :id) > 0 ")
	List<QisTransactionLaboratories> getTransactionsByTransactionById(@Param("id") Long id);

	@Query("SELECT tl FROM QisTransactionLaboratories tl "
			+ "WHERE tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " + "AND tl.status = 'SPD' "
			+ "AND tl.transactionType = :transactionType "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 0) > 0 "
//			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND ti.itemType = 'PCK') > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisTransactionLaboratories> getTransactionsByTransactionDateTimeRangeType(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("transactionType") String transactionType, Pageable pageable);

	@Query("SELECT tl FROM QisTransactionLaboratories tl "
			+ "WHERE tl.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo " + "AND tl.status = 'SPD' "
			+ "AND tl.transactionType <> :notTransactionType "
			+ "AND (SELECT COUNT(*) FROM QisTransactionLaboratoryRequest tlr "
			+ "INNER JOIN QisTransactionItem ti ON ti.id = tlr.transactionItemId "
			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1) > 0 "
//			+ "WHERE tlr.transactionid = tl.id AND ti.status = 1 AND ti.itemType = 'PCK') > 0 " 
			+ "ORDER BY tl.transactionDate")
	Page<QisTransactionLaboratories> getTransactionsByTransactionDateTimeRangeNotType(
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo,
			@Param("notTransactionType") String notTransactionType, Pageable pageable);
	
	@Query(value = "SELECT * FROM qis_transactions t "
			+ "INNER JOIN qis_transaction_payments tp ON t.id = tp.qis_transaction_id "
			+ "WHERE t.status = 'SPD' "
			+ "AND t.id = :id ",
			nativeQuery = true)
	QisTransactionLaboratories getSOPTransactionById(@Param("id") Long id);
}
