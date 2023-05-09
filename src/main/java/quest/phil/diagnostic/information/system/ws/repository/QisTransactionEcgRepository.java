package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionEcg;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ecg.QisTransactionLabEcg;

public interface QisTransactionEcgRepository extends JpaRepository<QisTransactionEcg, Long> {
	@Query("SELECT tlx FROM QisTransactionEcg tlx WHERE tlx.transactionid = :transactionid AND tlx.id = :id")
	QisTransactionEcg getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid,
			@Param("id") Long id);

}
