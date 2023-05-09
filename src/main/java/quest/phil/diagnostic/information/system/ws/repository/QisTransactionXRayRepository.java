package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionXRay;

@Repository
public interface QisTransactionXRayRepository extends JpaRepository<QisTransactionXRay, Long>{
	@Query("SELECT tlx FROM QisTransactionXRay tlx WHERE tlx.transactionid = :transactionid AND tlx.id = :id")
	QisTransactionXRay getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid,
			@Param("id") Long id);
}
