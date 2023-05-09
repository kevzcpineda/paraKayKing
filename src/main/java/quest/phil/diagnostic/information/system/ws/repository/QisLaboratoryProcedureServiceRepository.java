package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.QisLaboratoryProcedure;
import quest.phil.diagnostic.information.system.ws.model.QisLaboratoryRequest;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;

@Repository
public interface QisLaboratoryProcedureServiceRepository extends JpaRepository<QisLaboratoryProcedureService, Long> {
	@Query("SELECT lps FROM QisLaboratoryProcedureService lps WHERE lps.laboratoryProcedure = :procedure AND lps.laboratoryRequest = :serviceRequest")
	public Optional<QisLaboratoryProcedureService> getLaboratoryProcedureServiceByProcedureServiceRequest(
			@Param("procedure") QisLaboratoryProcedure procedure, @Param("serviceRequest") QisLaboratoryRequest serviceRequest);

}
