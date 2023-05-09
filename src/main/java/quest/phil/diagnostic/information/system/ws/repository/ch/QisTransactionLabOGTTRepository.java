package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabOGTT;

@Repository
public interface QisTransactionLabOGTTRepository extends JpaRepository<QisTransactionLabOGTT, Long> {
	@Query("SELECT tlo FROM QisTransactionLabOGTT tlo WHERE tlo.id = :id")
	QisTransactionLabOGTT getTransactionLabOGTTByLabReqId(@Param("id") Long id);

}
