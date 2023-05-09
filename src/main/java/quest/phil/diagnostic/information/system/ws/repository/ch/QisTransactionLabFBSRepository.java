package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabFBS;

@Repository
public interface QisTransactionLabFBSRepository extends JpaRepository<QisTransactionLabFBS, Long> {
	@Query("SELECT tlf FROM QisTransactionLabFBS tlf WHERE tlf.id = :id")
	QisTransactionLabFBS getTransactionLabFBSByLabReqId(@Param("id") Long id);

}
