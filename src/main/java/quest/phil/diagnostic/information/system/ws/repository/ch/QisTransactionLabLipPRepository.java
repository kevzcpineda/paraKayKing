package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabLipidProfile;

@Repository
public interface QisTransactionLabLipPRepository extends JpaRepository<QisTransactionLabLipidProfile, Long> {
	@Query("SELECT tll FROM QisTransactionLabLipidProfile tll WHERE tll.id = :id")
	QisTransactionLabLipidProfile getTransactionLabLipidProfileByLabReqId(@Param("id") Long id);

}
