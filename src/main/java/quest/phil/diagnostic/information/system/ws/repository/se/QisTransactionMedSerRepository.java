package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionMedSer;

public interface QisTransactionMedSerRepository extends JpaRepository<QisTransactionMedSer, Long> {

	@Query("SELECT tlt FROM QisTransactionMedSer tlt WHERE tlt.id = :id")
	QisTransactionMedSer getTransactionMedSerByLabReqId(@Param("id") Long id);
}
