package quest.phil.diagnostic.information.system.ws.repository.he;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabProthrombinTime;

@Repository
public interface QisTransactionLabPTimeRepository extends JpaRepository<QisTransactionLabProthrombinTime, Long> {
	@Query("SELECT tlpt FROM QisTransactionLabProthrombinTime tlpt WHERE tlpt.id = :id")
	QisTransactionLabProthrombinTime getTransactionLabProthrombinTimeByLabReqId(@Param("id") Long id);

}
