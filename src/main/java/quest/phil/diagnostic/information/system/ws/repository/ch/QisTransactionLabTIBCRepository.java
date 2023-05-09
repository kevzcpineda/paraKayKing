package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabTIBC;

public interface QisTransactionLabTIBCRepository extends JpaRepository<QisTransactionLabTIBC, Long> {
	
	@Query("SELECT tlf FROM QisTransactionLabTIBC tlf WHERE tlf.id = :id")
	QisTransactionLabTIBC getTransactionLabTIBCByLabReqId(@Param("id") Long id);
	
}
