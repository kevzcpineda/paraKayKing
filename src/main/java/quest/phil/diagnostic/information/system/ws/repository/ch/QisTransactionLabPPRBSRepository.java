package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabPPRBS;

@Repository
public interface QisTransactionLabPPRBSRepository extends JpaRepository<QisTransactionLabPPRBS, Long> {
	@Query("SELECT tlp FROM QisTransactionLabPPRBS tlp WHERE tlp.id = :id")
	QisTransactionLabPPRBS getTransactionLabPPRBSByLabReqId(@Param("id") Long id);

}
