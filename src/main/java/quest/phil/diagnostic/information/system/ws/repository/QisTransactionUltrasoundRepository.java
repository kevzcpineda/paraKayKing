package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionUltrasound;

public interface QisTransactionUltrasoundRepository extends JpaRepository<QisTransactionUltrasound, Long> {

	@Query("SELECT tlu FROM QisTransactionUltrasound tlu WHERE tlu.transactionid = :transactionid AND tlu.id = :id")
	QisTransactionUltrasound getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid,
			@Param("id") Long id);
}
