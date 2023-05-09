package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabBilirubin;

@Repository
public interface QisTransactionLabBilirubinRepository extends JpaRepository<QisTransactionLabBilirubin, Long> {
	@Query("SELECT tlb FROM QisTransactionLabBilirubin tlb WHERE tlb.id = :id")
	QisTransactionLabBilirubin getTransactionLabBilirubinByLabReqId(@Param("id") Long id);

}
