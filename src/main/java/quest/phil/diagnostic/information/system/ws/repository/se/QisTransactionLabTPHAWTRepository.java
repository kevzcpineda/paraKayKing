package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabTPHAwTiter;

public interface QisTransactionLabTPHAWTRepository  extends JpaRepository<QisTransactionLabTPHAwTiter, Long> {

	@Query("SELECT tls FROM QisTransactionLabTPHAwTiter tls WHERE tls.id = :id")
	QisTransactionLabTPHAwTiter getTransactionLabTPHAWTByLabReqId(@Param("id") Long id);
}
