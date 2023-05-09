package quest.phil.diagnostic.information.system.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryService;

@Repository
public interface QisLaboratoryServiceRepository extends JpaRepository<QisLaboratoryService, Long> {
	@Query("SELECT ls FROM QisLaboratoryService ls WHERE ls.laboratoryProcedure = :procedure")
	public List<QisLaboratoryService> getLaboratoryServiceByProcedure(@Param("procedure") String procedure);
}
