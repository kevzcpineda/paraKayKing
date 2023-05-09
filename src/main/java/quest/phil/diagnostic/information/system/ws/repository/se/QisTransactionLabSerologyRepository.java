package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabSerology;

@Repository
public interface QisTransactionLabSerologyRepository extends JpaRepository<QisTransactionLabSerology, Long> {
	@Query("SELECT tls FROM QisTransactionLabSerology tls WHERE tls.id = :id")
	QisTransactionLabSerology getTransactionLabSerologyByLabReqId(@Param("id") Long id);

}
