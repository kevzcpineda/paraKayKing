package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;

@Repository
public interface QisTransactionRepository extends JpaRepository<QisTransaction, Long> {
	QisTransaction findByTransactionid(String transactionid);

	Boolean existsByTransactionid(String transactionid);

	@Query("SELECT ts FROM QisTransaction ts WHERE ts.id = :id")
	QisTransaction getTransactionById(@Param("id") Long id);
	

	@Query("SELECT ts FROM QisTransaction ts WHERE DATE(ts.transactionDate) = DATE(:transactionDate) ORDER BY ts.transactionDate")
	Page<QisTransaction> getTransactionsByTransactionDate(@Param("transactionDate") Calendar transactionDate,
			Pageable pageable);

	@Query("SELECT ts FROM QisTransaction ts WHERE DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) ORDER BY ts.transactionDate")
	Page<QisTransaction> getTransactionsByTransactionDateRange(@Param("dateFrom") Calendar dateFrom,
			@Param("dateTo") Calendar dateTo, Pageable pageable);

	@Query("SELECT ts FROM QisTransaction ts WHERE ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo ORDER BY ts.transactionDate")
	Page<QisTransaction> getTransactionsByTransactionDateTimeRange(@Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable);

	@Query("SELECT ts FROM QisTransaction ts WHERE ts.status = :status ORDER BY ts.transactionDate")
	Page<QisTransaction> getTransactionsByStatus(@Param("status") String status, Pageable pageable);

	@Query("SELECT ts FROM QisTransaction ts WHERE ts.status = 'SPD' AND ts.id = :id")
	Page<QisTransaction> getTransactionsByTransactionId(@Param("id") Long id, Pageable pageable);

	
	// STATUS & DATE
	@Query("SELECT ts FROM QisTransaction ts WHERE ts.status = :status AND DATE(ts.transactionDate) = DATE(:transactionDate) ORDER BY ts.transactionDate")
	Page<QisTransaction> getTransactionsByStatusTransactionDate(@Param("status") String status, @Param("transactionDate") Calendar transactionDate,
			Pageable pageable);

	@Query("SELECT ts FROM QisTransaction ts WHERE ts.status = :status AND DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) ORDER BY ts.transactionDate")
	Page<QisTransaction> getTransactionsByStatusTransactionDateRange(@Param("status") String status, @Param("dateFrom") Calendar dateFrom,
			@Param("dateTo") Calendar dateTo, Pageable pageable);

	@Query("SELECT ts FROM QisTransaction ts WHERE ts.status = :status AND ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo ORDER BY ts.transactionDate")
	Page<QisTransaction> getTransactionsByStatusTransactionDateTimeRange(@Param("status") String status, @Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable);


	// BRANCH & DATE
	@Query("SELECT ts FROM QisTransaction ts WHERE ts.branchId = :branchId AND ts.transactionDate BETWEEN :dateTimeFrom AND :dateTimeTo ORDER BY ts.transactionDate")
	Page<QisTransaction> getTransactionsByTransactionBranchDateTimeRange(@Param("branchId") Long branchId,
			@Param("dateTimeFrom") Calendar dateTimeFrom, @Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable);
	
	@Query("SELECT ts FROM QisTransaction ts WHERE ts.branchId = :branchId AND DATE(ts.transactionDate) = DATE(:transactionDate) ORDER BY ts.transactionDate")
	Page<QisTransaction> getTransactionsByTransactionBranchDate(@Param("branchId") Long branchId, @Param("transactionDate") Calendar transactionDate,
			Pageable pageable);

	@Query("SELECT ts FROM QisTransaction ts WHERE ts.branchId = :branchId AND DATE(ts.transactionDate) BETWEEN DATE(:dateFrom) AND DATE(:dateTo) ORDER BY ts.transactionDate")
	Page<QisTransaction> getTransactionsByTransactionBranchDateRange(@Param("branchId") Long branchId, 
			@Param("dateFrom") Calendar dateFrom, @Param("dateTo") Calendar dateTo,
			Pageable pageable);
	
	
	@Query("SELECT ts FROM QisTransaction ts " 
			+ "INNER JOIN QisPatient ps ON ps.id = ts.patientId "
			+ "WHERE ts.status = 'SPD' AND " 
			+ "ps.firstname LIKE :searchName% OR "
			+ "ps.lastname LIKE :searchName% OR " 
			+ "ps.middlename LIKE :searchName%")
	Page<QisTransaction> getTransactionsByPatientName(@Param("searchName") String searchName, Pageable pageable);

	@Query("SELECT ts FROM QisTransaction ts " 
			+ "INNER JOIN QisPatient ps ON ps.id = ts.patientId "
			+ "WHERE ts.status = 'SPD' AND " 
			+ "ps.firstname LIKE :firstName% AND " 
			+ "ps.lastname LIKE :lastName%")
	Page<QisTransaction> getTransactionsByPatientLastFirstName(@Param("lastName") String lastName,
			@Param("firstName") String firstName, Pageable pageable);

	// ALL UNBILLED TRANSACTIONS
	@Query(value = "SELECT * FROM qis_transactions t "
			+ "INNER JOIN qis_transaction_payments tp ON t.id = tp.qis_transaction_id "
			+ "WHERE t.status = 'SPD' "
			+ " AND NOT EXISTS (SELECT NULL FROM qis_soa_transactions soa WHERE soa.qis_transaction_id = t.id) "
			+ "AND tp.biller_id IS NOT NULL "
			+ "ORDER BY t.transaction_date",
			nativeQuery = true)
	Page<QisTransaction> getAllUnbilledTransactions(Pageable pageable);
	
	// ALL UNBILLED TRANSACTIONS BY DATE
	@Query(value = "SELECT * FROM qis_transactions t "
			+ "INNER JOIN qis_transaction_payments tp ON t.id = tp.qis_transaction_id "
			+ "WHERE t.status = 'SPD' "
			+ " AND NOT EXISTS (SELECT NULL FROM qis_soa_transactions soa WHERE soa.qis_transaction_id = t.id) "
			+ "AND tp.biller_id IS NOT NULL AND t.transaction_date BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "ORDER BY t.transaction_date",
			nativeQuery = true)
	Page<QisTransaction> getAllUnbilledTransactionsByDate( @Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable );
	

	// UNBILLED TRANSACTIONS PER CHARGE
	@Query(value = "SELECT * FROM qis_transactions t "
			+ "INNER JOIN qis_transaction_payments tp ON t.id = tp.qis_transaction_id "
			+ "WHERE t.status = 'SPD' "
			+ " AND NOT EXISTS (SELECT NULL FROM qis_soa_transactions soa WHERE soa.qis_transaction_id = t.id) "
			+ "AND tp.biller_id = :billerId "
			+ "ORDER BY t.transaction_date",
			nativeQuery = true)
	Page<QisTransaction> getUnbilledTransactions(@Param("billerId") Long billerId, Pageable pageable);
	
	// UNBILLED TRANSACTIONS PER CHARGE BY DATE
	@Query(value = "SELECT * FROM qis_transactions t "
			+ "INNER JOIN qis_transaction_payments tp ON t.id = tp.qis_transaction_id "
			+ "WHERE t.status = 'SPD' "
			+ " AND NOT EXISTS (SELECT NULL FROM qis_soa_transactions soa WHERE soa.qis_transaction_id = t.id) "
			+ "AND tp.biller_id = :billerId AND t.transaction_date BETWEEN :dateTimeFrom AND :dateTimeTo "
			+ "ORDER BY t.transaction_date",
			nativeQuery = true)
	Page<QisTransaction> getUnbilledTransactionsByDate( @Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo, @Param("billerId") Long billerId, Pageable pageable);
	
	
	@Query(value = "SELECT * FROM qis_transactions t "
			+ "INNER JOIN qis_transaction_payments tp ON t.id = tp.qis_transaction_id "
			+ "WHERE t.status = 'SPD' "
			+ " AND NOT EXISTS (SELECT NULL FROM qis_soa_transactions soa WHERE soa.qis_transaction_id = t.id) "
			+ "AND tp.biller_id = :billerId "
			+ "AND t.id = :id ",
			nativeQuery = true)
	QisTransaction getSOATransactionById(@Param("id") Long id, @Param("billerId") Long billerId);
	
	
	@Query(value = "SELECT * FROM qis_transactions t "
			+ "INNER JOIN qis_transaction_payments tp ON t.id = tp.qis_transaction_id "
			+ "WHERE t.status = 'SPD' "
			+ "AND t.id = :id ",
			nativeQuery = true)
	QisTransaction getSOPTransactionById(@Param("id") Long id);
	
}
