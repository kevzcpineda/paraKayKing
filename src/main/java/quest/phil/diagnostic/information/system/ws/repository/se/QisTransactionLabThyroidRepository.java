package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabThyroid;

@Repository
public interface QisTransactionLabThyroidRepository extends JpaRepository<QisTransactionLabThyroid, Long> {
	@Query("SELECT tlt FROM QisTransactionLabThyroid tlt WHERE tlt.id = :id")
	QisTransactionLabThyroid getTransactionLabThyroidByLabReqId(@Param("id") Long id);

}
