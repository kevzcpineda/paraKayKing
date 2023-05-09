package quest.phil.diagnostic.information.system.ws.repository.se;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.se.QisTransactionLabAntigen;

@Repository
public interface QisTransactionLabAntigenRepository extends JpaRepository<QisTransactionLabAntigen, Long> {
	@Query("SELECT tla FROM QisTransactionLabAntigen tla WHERE tla.id = :id")
	QisTransactionLabAntigen getTransactionLabAntigenByLabReqId(@Param("id") Long id);

}
