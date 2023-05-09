package quest.phil.diagnostic.information.system.ws.repository.pe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabMedicalHistory;

@Repository
public interface QisTransactionLabMedHisRepository extends JpaRepository<QisTransactionLabMedicalHistory, Long> {
	@Query("SELECT tlmh FROM QisTransactionLabMedicalHistory tlmh WHERE tlmh.id = :id")
	QisTransactionLabMedicalHistory getTransactionLabMedicalHistoryByLabReqId(@Param("id") Long id);

}
