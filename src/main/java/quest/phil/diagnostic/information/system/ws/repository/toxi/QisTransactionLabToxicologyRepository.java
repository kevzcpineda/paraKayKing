package quest.phil.diagnostic.information.system.ws.repository.toxi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.toxi.QisTransactionLabToxicology;

@Repository
public interface QisTransactionLabToxicologyRepository extends JpaRepository<QisTransactionLabToxicology, Long> {
	@Query("SELECT tlt FROM QisTransactionLabToxicology tlt WHERE tlt.id = :id")
	QisTransactionLabToxicology getTransactionLabToxicologyByLabReqId(@Param("id") Long id);
}
