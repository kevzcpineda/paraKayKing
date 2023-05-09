package quest.phil.diagnostic.information.system.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quest.phil.diagnostic.information.system.ws.model.entity.QisReferenceLaboratory;

@Repository
public interface QisReferenceLaboratoryRepository extends JpaRepository<QisReferenceLaboratory, Long> {
	
	Boolean existsByReferenceid(String referenceId);
	Boolean existsByName(String referemceName);
	

	@Query("SELECT p FROM QisReferenceLaboratory p WHERE p.isActive = true")
	List<QisReferenceLaboratory> getAllReferenceLaboratory();
	
	
	@Query("SELECT p FROM QisReferenceLaboratory p WHERE p.referenceid = :referenceId")
	QisReferenceLaboratory getReferenceLaboratory(String referenceId);


}
