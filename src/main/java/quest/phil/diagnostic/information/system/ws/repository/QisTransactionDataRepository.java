package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionData;

public interface QisTransactionDataRepository  extends JpaRepository<QisTransactionData, Long>{
	QisTransactionData findByTransactionid(String transactionid);

	Boolean existsByTransactionid(String transactionid);

	@Query("SELECT ts FROM QisTransactionData ts WHERE ts.id = :id")
	QisTransactionData getTransactionById(@Param("id") Long id);

	@Query(value = "SELECT * FROM qis_transactions t "
			+ "INNER JOIN qis_transaction_payments tp ON t.id = tp.qis_transaction_id "
			+ "WHERE t.status = 'SPD' "
			+ " AND NOT EXISTS (SELECT NULL FROM qis_soa_transactions soa WHERE soa.qis_transaction_id = t.id) "
			+ "AND tp.biller_id = :billerId "
			+ "AND t.id = :id ",
			nativeQuery = true)
	QisTransactionData getSOATransactionById(@Param("id") Long id, @Param("billerId") Long billerId);

}
