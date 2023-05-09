package quest.phil.diagnostic.information.system.ws.repository.cm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabAFB;

@Repository
public interface QisTransactionLabAFBRepository extends JpaRepository<QisTransactionLabAFB, Long> {
	@Query("SELECT tla FROM QisTransactionLabAFB tla WHERE tla.id = :id")
	QisTransactionLabAFB getTransactionLabAFBByLabReqId(@Param("id") Long id);

}
