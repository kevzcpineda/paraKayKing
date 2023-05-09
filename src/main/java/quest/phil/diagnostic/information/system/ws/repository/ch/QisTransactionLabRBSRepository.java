package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabRBS;

@Repository
public interface QisTransactionLabRBSRepository extends JpaRepository<QisTransactionLabRBS, Long> {
	@Query("SELECT tlr FROM QisTransactionLabRBS tlr WHERE tlr.id = :id")
	QisTransactionLabRBS getTransactionLabRBSByLabReqId(@Param("id") Long id);

}
