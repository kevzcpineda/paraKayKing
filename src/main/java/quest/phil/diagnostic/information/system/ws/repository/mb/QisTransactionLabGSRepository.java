package quest.phil.diagnostic.information.system.ws.repository.mb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.mb.QisTransactionLabGS;

public interface QisTransactionLabGSRepository extends JpaRepository<QisTransactionLabGS, Long> {
	@Query("SELECT tlpt FROM QisTransactionLabGS tlpt WHERE tlpt.id = :id")
	QisTransactionLabGS getTransactionLabGramStainByLabReqId(@Param("id") Long id);
}
