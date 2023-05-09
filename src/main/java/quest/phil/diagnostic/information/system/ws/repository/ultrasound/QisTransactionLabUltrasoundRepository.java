package quest.phil.diagnostic.information.system.ws.repository.ultrasound;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ultrasound.QisTransactionLabUltrasound;

@Repository
public interface QisTransactionLabUltrasoundRepository extends JpaRepository<QisTransactionLabUltrasound, Long> {
	@Query("SELECT tlu FROM QisTransactionLabUltrasound tlu WHERE tlu.id = :id")
	QisTransactionLabUltrasound getTransactionLabUltrasoundByLabReqId(@Param("id") Long id);
}
