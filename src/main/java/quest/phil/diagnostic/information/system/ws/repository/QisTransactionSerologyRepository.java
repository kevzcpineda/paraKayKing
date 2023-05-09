package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;

@Repository
public interface QisTransactionSerologyRepository extends JpaRepository<QisTransactionSerology, Long>{
	@Query("SELECT tlse FROM QisTransactionSerology tlse WHERE tlse.transactionid = :transactionid AND tlse.id = :id")
	QisTransactionSerology getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid,
			@Param("id") Long id);

}
