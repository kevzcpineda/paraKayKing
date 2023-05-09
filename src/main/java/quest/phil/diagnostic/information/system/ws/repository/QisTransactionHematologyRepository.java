package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionHematology;

@Repository
public interface QisTransactionHematologyRepository extends JpaRepository<QisTransactionHematology, Long>{
	@Query("SELECT tlhe FROM QisTransactionHematology tlhe WHERE tlhe.transactionid = :transactionid AND tlhe.id = :id")
	QisTransactionHematology getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid,
			@Param("id") Long id);

}
