package quest.phil.diagnostic.information.system.ws.repository.cm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabUrineChemical;

@Repository
public interface QisTransactionLabUrineChemicalRepository extends JpaRepository<QisTransactionLabUrineChemical, Long> {
	@Query("SELECT tluc FROM QisTransactionLabUrineChemical tluc WHERE tluc.id = :id")
	QisTransactionLabUrineChemical getTransactionLabUrineChemicalByLabReqId(@Param("id") Long id);

}
