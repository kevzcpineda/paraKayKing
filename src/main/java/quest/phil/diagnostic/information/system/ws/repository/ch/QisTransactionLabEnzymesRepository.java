package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabEnzymes;

@Repository
public interface QisTransactionLabEnzymesRepository extends JpaRepository<QisTransactionLabEnzymes, Long> {
	@Query("SELECT tle FROM QisTransactionLabEnzymes tle WHERE tle.id = :id")
	QisTransactionLabEnzymes getTransactionLabEnzymesByLabReqId(@Param("id") Long id);

}
