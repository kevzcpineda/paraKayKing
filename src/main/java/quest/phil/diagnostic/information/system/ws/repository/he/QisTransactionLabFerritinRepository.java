package quest.phil.diagnostic.information.system.ws.repository.he;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabFerritin;

public interface QisTransactionLabFerritinRepository extends JpaRepository<QisTransactionLabFerritin, Long> {

	@Query("SELECT tle FROM QisTransactionLabFerritin tle WHERE tle.id = :id")
	QisTransactionLabFerritin getTransactionLabFerritinByLabReqId(@Param("id") Long id);
}
