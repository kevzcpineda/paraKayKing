package quest.phil.diagnostic.information.system.ws.repository.cm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabPTOBT;

@Repository
public interface QisTransactionLabPTOBTRepository extends JpaRepository<QisTransactionLabPTOBT, Long> {
	@Query("SELECT tlpo FROM QisTransactionLabPTOBT tlpo WHERE tlpo.id = :id")
	QisTransactionLabPTOBT getTransactionLabPTOBTByLabReqId(@Param("id") Long id);

}
