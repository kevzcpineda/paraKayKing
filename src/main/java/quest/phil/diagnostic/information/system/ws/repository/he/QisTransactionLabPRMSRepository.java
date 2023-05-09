package quest.phil.diagnostic.information.system.ws.repository.he;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabPRMS;

@Repository
public interface QisTransactionLabPRMSRepository extends JpaRepository<QisTransactionLabPRMS, Long> {
	@Query("SELECT tlpm FROM QisTransactionLabPRMS tlpm WHERE tlpm.id = :id")
	QisTransactionLabPRMS getTransactionLabPRMSByLabReqId(@Param("id") Long id);
}
