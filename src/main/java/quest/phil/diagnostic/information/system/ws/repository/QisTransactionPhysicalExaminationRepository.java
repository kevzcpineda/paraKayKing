package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionPhysicalExamination;

@Repository
public interface QisTransactionPhysicalExaminationRepository
		extends JpaRepository<QisTransactionPhysicalExamination, Long> {
	@Query("SELECT tlpe FROM QisTransactionPhysicalExamination tlpe WHERE tlpe.transactionid = :transactionid AND tlpe.id = :id")
	QisTransactionPhysicalExamination getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid,
			@Param("id") Long id);
}
