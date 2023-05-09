package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabElectrolytes;

@Repository
public interface QisTransactionLabElectrolytesRepository extends JpaRepository<QisTransactionLabElectrolytes, Long> {
	@Query("SELECT tle FROM QisTransactionLabElectrolytes tle WHERE tle.id = :id")
	QisTransactionLabElectrolytes getTransactionLabElectrolytesByLabReqId(@Param("id") Long id);

}
