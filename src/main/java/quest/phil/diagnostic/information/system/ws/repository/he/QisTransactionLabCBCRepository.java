package quest.phil.diagnostic.information.system.ws.repository.he;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.he.QisTransactionLabCBC;

@Repository
public interface QisTransactionLabCBCRepository extends JpaRepository<QisTransactionLabCBC, Long> {
	@Query("SELECT tlc FROM QisTransactionLabCBC tlc WHERE tlc.id = :id")
	QisTransactionLabCBC getTransactionLabCBCByLabReqId(@Param("id") Long id);

}
