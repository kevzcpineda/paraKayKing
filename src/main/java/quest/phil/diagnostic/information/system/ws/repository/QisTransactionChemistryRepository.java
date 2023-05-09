package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionChemistry;

@Repository
public interface QisTransactionChemistryRepository extends JpaRepository<QisTransactionChemistry, Long> {
	@Query("SELECT tlch FROM QisTransactionChemistry tlch WHERE tlch.transactionid = :transactionid AND tlch.id = :id")
	QisTransactionChemistry getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid,
			@Param("id") Long id);

}
