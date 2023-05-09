package quest.phil.diagnostic.information.system.ws.repository.pe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabVitalSign;

@Repository
public interface QisTransactionLabVitSigRepository extends JpaRepository<QisTransactionLabVitalSign, Long> {
	@Query("SELECT tlvs FROM QisTransactionLabVitalSign tlvs WHERE tlvs.id = :id")
	QisTransactionLabVitalSign getTransactionLabVitalSignByLabReqId(@Param("id") Long id);
}
