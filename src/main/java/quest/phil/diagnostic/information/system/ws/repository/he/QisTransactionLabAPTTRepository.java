package quest.phil.diagnostic.information.system.ws.repository.he;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabAPTT;

public interface QisTransactionLabAPTTRepository extends JpaRepository<QisTransactionLabAPTT, Long> {
	@Query("SELECT tla FROM QisTransactionLabAPTT tla WHERE tla.id = :id")
	QisTransactionLabAPTT getTransactionLabAPTTByLabReqId(@Param("id") Long id);

}
