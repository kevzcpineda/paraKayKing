package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabRFT;

public interface QisTransactionLabRFTRepository extends JpaRepository<QisTransactionLabRFT, Long> {

	@Query("SELECT tlc FROM QisTransactionLabRFT tlc WHERE tlc.id = :id")
	QisTransactionLabRFT getTransactionLabRFTByLabReqId(@Param("id") Long id);
}
