package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionBacteriology;

public interface QisTransactionBacteriologyRepository extends JpaRepository<QisTransactionBacteriology, Long> {

	@Query("SELECT tlhe FROM QisTransactionBacteriology tlhe WHERE tlhe.transactionid = :transactionid AND tlhe.id = :id")
	QisTransactionBacteriology getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid,
			@Param("id") Long id);
}
