package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabBUN;

@Repository
public interface QisTransactionLabBUNRepository extends JpaRepository<QisTransactionLabBUN, Long> {
	@Query("SELECT tlb FROM QisTransactionLabBUN tlb WHERE tlb.id = :id")
	QisTransactionLabBUN getTransactionLabBUNByLabReqId(@Param("id") Long id);

}
