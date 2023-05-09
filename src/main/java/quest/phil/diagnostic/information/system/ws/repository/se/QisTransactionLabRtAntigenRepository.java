package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabRtAntigen;

public interface QisTransactionLabRtAntigenRepository extends JpaRepository<QisTransactionLabRtAntigen, Long> {
	@Query("SELECT tlc FROM QisTransactionLabRtAntigen tlc WHERE tlc.id = :id")
	QisTransactionLabRtAntigen getTransactionLabRtAntigenByLabReqId(@Param("id") Long id);

}
