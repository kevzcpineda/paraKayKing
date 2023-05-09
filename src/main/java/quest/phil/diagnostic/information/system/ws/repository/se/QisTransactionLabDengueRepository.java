package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabDengue;

public interface QisTransactionLabDengueRepository extends JpaRepository<QisTransactionLabDengue, Long>  {

	@Query("SELECT tlc FROM QisTransactionLabDengue tlc WHERE tlc.id = :id")
	QisTransactionLabDengue getTransactionLabDengueByLabReqId(@Param("id") Long id);
}
