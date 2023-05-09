package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabASO;

public interface QisTransactionLabASORepository extends JpaRepository<QisTransactionLabASO, Long>  {


	@Query("SELECT tlc FROM QisTransactionLabASO tlc WHERE tlc.id = :id")
	QisTransactionLabASO getTransactionLabASOByLabReqId(@Param("id") Long id);
}
