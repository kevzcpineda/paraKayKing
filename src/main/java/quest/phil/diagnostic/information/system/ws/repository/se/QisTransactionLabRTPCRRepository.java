package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionRTPCR;

public interface QisTransactionLabRTPCRRepository extends JpaRepository<QisTransactionRTPCR, Long> {

	@Query("SELECT tlc FROM QisTransactionRTPCR tlc WHERE tlc.id = :id")
	QisTransactionRTPCR getTransactionLabRtAntigenByLabReqId(@Param("id") Long id);
	
	
}
