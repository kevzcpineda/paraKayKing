package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabCRP;

@Repository
public interface QisTransactionLabCRPRepository extends JpaRepository<QisTransactionLabCRP, Long> {
	@Query("SELECT tlc FROM QisTransactionLabCRP tlc WHERE tlc.id = :id")
	QisTransactionLabCRP getTransactionLabCRPByLabReqId(@Param("id") Long id);

}
