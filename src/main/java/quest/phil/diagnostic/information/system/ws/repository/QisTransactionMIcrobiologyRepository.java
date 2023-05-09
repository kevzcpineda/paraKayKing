package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionMicrobiology;

public interface QisTransactionMIcrobiologyRepository extends JpaRepository<QisTransactionMicrobiology, Long>{
	@Query("SELECT tlhe FROM QisTransactionMicrobiology tlhe WHERE tlhe.transactionid = :transactionid AND tlhe.id = :id")
	QisTransactionMicrobiology getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid,
			@Param("id") Long id);
}
