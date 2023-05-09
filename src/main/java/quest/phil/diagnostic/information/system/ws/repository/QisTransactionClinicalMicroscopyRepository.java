package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionClinicalMicroscopy;

@Repository
public interface QisTransactionClinicalMicroscopyRepository
		extends JpaRepository<QisTransactionClinicalMicroscopy, Long> {
	@Query("SELECT tlcm FROM QisTransactionClinicalMicroscopy tlcm WHERE tlcm.transactionid = :transactionid AND tlcm.id = :id")
	QisTransactionClinicalMicroscopy getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid,
			@Param("id") Long id);

}
