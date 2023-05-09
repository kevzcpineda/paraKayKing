package quest.phil.diagnostic.information.system.ws.repository.he;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabRCT;

public interface QisTransactionLabRCTRepository  extends JpaRepository<QisTransactionLabRCT, Long> {

	@Query("SELECT tle FROM QisTransactionLabRCT tle WHERE tle.id = :id")
	QisTransactionLabRCT getTransactionLabRctByLabReqId(@Param("id") Long id);
}
