package quest.phil.diagnostic.information.system.ws.repository.xray;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.xray.QisTransactionLabXRay;

@Repository
public interface QisTransactionLabXRayRepository extends JpaRepository<QisTransactionLabXRay, Long> {
	@Query("SELECT tlx FROM QisTransactionLabXRay tlx WHERE tlx.id = :id")
	QisTransactionLabXRay getTransactionLabXRayByLabReqId(@Param("id") Long id);
}
