package quest.phil.diagnostic.information.system.ws.repository.he;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabBloodTyping;

@Repository
public interface QisTransactionLabBTypRepository extends JpaRepository<QisTransactionLabBloodTyping, Long> {
	@Query("SELECT tlb FROM QisTransactionLabBloodTyping tlb WHERE tlb.id = :id")
	QisTransactionLabBloodTyping getTransactionLabBloodTypingByLabReqId(@Param("id") Long id);

}
