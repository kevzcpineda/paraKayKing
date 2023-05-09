package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabOGCT;

@Repository
public interface QisTransactionLabOGCTRepository extends JpaRepository<QisTransactionLabOGCT, Long> {
	@Query("SELECT tlo FROM QisTransactionLabOGCT tlo WHERE tlo.id = :id")
	QisTransactionLabOGCT getTransactionLabOGCTByLabReqId(@Param("id") Long id);

}
