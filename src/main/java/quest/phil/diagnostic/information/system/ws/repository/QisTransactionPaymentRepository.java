package quest.phil.diagnostic.information.system.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionPayment;

@Repository
public interface QisTransactionPaymentRepository extends JpaRepository<QisTransactionPayment, Long> {
	@Query("SELECT tp FROM QisTransactionPayment tp WHERE tp.transactionid = :transactionid")
	List<QisTransactionPayment> getTransactionPaymentsByTransactionid(@Param("transactionid") Long transactionid);

	@Query("SELECT tp FROM QisTransactionPayment tp WHERE tp.transactionid = :transactionid AND tp.id = :id")
	QisTransactionPayment getTransactionPaymentById(@Param("transactionid") Long transactionid, @Param("id") Long id);

}
