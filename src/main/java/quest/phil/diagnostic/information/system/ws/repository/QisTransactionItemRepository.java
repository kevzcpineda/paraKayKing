package quest.phil.diagnostic.information.system.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItem;

@Repository
public interface QisTransactionItemRepository extends JpaRepository<QisTransactionItem, Long> {
	
	@Query("SELECT ti FROM QisTransactionItem ti WHERE ti.transactionid = :transactionid")
	List<QisTransactionItem> getTransactionItemsByTransactionid(@Param("transactionid") Long transactionid);

	@Query("SELECT ti FROM QisTransactionItem ti WHERE ti.transactionid = :transactionid AND ti.id = :id")
	QisTransactionItem getTransactionItemById(@Param("transactionid") Long transactionid, @Param("id") Long id);

	List<QisTransactionItem> findByTransactionid(Long id);


}
