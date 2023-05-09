package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionToxicology;

@Repository
public interface QisTransactionToxicologyRepository extends JpaRepository<QisTransactionToxicology, Long> {
	@Query("SELECT tlt FROM QisTransactionToxicology tlt WHERE tlt.transactionid = :transactionid AND tlt.id = :id")
	QisTransactionToxicology getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid,
			@Param("id") Long id);
}
