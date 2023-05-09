package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionDiscount;

@Repository
public interface QisTransactionDiscountRepository extends JpaRepository<QisTransactionDiscount, Long> {
	@Query("SELECT td FROM QisTransactionDiscount td WHERE td.transactionid = :transactionid")
	QisTransactionDiscount getTransactionDiscountByTransactionId(@Param("transactionid") Long transactionid);

}
