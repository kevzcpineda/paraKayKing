package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabTPN;

public interface QisTransactionLabTPNRepository extends JpaRepository<QisTransactionLabTPN, Long> {
	@Query("SELECT tlt FROM QisTransactionLabTPN tlt WHERE tlt.id = :id")
	QisTransactionLabTPN getTransactionLabTpnByLabReqId(@Param("id") Long id);
}
