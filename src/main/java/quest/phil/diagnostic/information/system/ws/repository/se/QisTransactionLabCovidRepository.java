package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabCovid;

@Repository
public interface QisTransactionLabCovidRepository extends JpaRepository<QisTransactionLabCovid, Long> {
	@Query("SELECT tlc FROM QisTransactionLabCovid tlc WHERE tlc.id = :id")
	QisTransactionLabCovid getTransactionLabCovidByLabReqId(@Param("id") Long id);

}
