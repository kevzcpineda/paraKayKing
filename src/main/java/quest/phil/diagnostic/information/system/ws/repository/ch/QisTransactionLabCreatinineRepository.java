package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabCreatinine;

@Repository
public interface QisTransactionLabCreatinineRepository extends JpaRepository<QisTransactionLabCreatinine, Long> {
	@Query("SELECT tlc FROM QisTransactionLabCreatinine tlc WHERE tlc.id = :id")
	QisTransactionLabCreatinine getTransactionLabCreatinineByLabReqId(@Param("id") Long id);

}
