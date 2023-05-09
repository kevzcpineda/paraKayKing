package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabHIV;

@Repository
public interface QisTransactionLabHIVRepository extends JpaRepository<QisTransactionLabHIV, Long> {
	@Query("SELECT tlh FROM QisTransactionLabHIV tlh WHERE tlh.id = :id")
	QisTransactionLabHIV getTransactionLabHIVByLabReqId(@Param("id") Long id);

}
