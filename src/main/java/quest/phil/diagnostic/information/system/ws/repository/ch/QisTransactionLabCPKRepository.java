package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabCPK;

@Repository
public interface QisTransactionLabCPKRepository extends JpaRepository<QisTransactionLabCPK, Long> {
	@Query("SELECT tlc FROM QisTransactionLabCPK tlc WHERE tlc.id = :id")
	QisTransactionLabCPK getTransactionLabCPKByLabReqId(@Param("id") Long id);

}
