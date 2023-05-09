package quest.phil.diagnostic.information.system.ws.repository.cm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabFecalysis;

@Repository
public interface QisTransactionLabFecalysisRepository extends JpaRepository<QisTransactionLabFecalysis, Long> {
	@Query("SELECT tlf FROM QisTransactionLabFecalysis tlf WHERE tlf.id = :id")
	QisTransactionLabFecalysis getTransactionLabFecalysisByLabReqId(@Param("id") Long id);

}
