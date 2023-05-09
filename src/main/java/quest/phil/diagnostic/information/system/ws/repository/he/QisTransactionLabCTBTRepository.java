package quest.phil.diagnostic.information.system.ws.repository.he;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabCTBT;

@Repository
public interface QisTransactionLabCTBTRepository extends JpaRepository<QisTransactionLabCTBT, Long> {
	@Query("SELECT tlc FROM QisTransactionLabCTBT tlc WHERE tlc.id = :id")
	QisTransactionLabCTBT getTransactionLabCTBTByLabReqId(@Param("id") Long id);

}
