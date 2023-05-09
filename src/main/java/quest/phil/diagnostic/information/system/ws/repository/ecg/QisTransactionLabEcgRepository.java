package quest.phil.diagnostic.information.system.ws.repository.ecg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ecg.QisTransactionLabEcg;

public interface QisTransactionLabEcgRepository extends JpaRepository<QisTransactionLabEcg, Long>{

	@Query("SELECT tlx FROM QisTransactionLabEcg tlx WHERE tlx.id = :id")
	QisTransactionLabEcg getTransactionLabReqId(@Param("id") Long id);

}
