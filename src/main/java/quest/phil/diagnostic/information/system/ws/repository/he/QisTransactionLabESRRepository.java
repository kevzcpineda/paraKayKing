package quest.phil.diagnostic.information.system.ws.repository.he;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabESR;

public interface QisTransactionLabESRRepository extends JpaRepository<QisTransactionLabESR, Long> {
	@Query("SELECT tle FROM QisTransactionLabESR tle WHERE tle.id = :id")
	QisTransactionLabESR getTransactionLabESRByLabReqId(@Param("id") Long id);

}
