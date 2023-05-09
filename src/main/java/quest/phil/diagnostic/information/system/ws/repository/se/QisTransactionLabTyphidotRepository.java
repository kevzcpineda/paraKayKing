package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabTyphidot;

@Repository
public interface QisTransactionLabTyphidotRepository extends JpaRepository<QisTransactionLabTyphidot, Long> {
	@Query("SELECT tlt FROM QisTransactionLabTyphidot tlt WHERE tlt.id = :id")
	QisTransactionLabTyphidot getTransactionLabTyphidotByLabReqId(@Param("id") Long id);

}
