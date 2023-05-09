package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabHemoglobin;

@Repository
public interface QisTransactionLabHemoRepository extends JpaRepository<QisTransactionLabHemoglobin, Long> {
	@Query("SELECT tlh FROM QisTransactionLabHemoglobin tlh WHERE tlh.id = :id")
	QisTransactionLabHemoglobin getTransactionLabHemoglobinByLabReqId(@Param("id") Long id);

}
