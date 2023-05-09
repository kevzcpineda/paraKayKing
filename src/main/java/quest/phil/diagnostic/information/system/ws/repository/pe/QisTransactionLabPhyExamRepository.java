 package quest.phil.diagnostic.information.system.ws.repository.pe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.pe.QisTransactionLabPhysicalExam;

@Repository
public interface QisTransactionLabPhyExamRepository extends JpaRepository<QisTransactionLabPhysicalExam, Long>{
	@Query("SELECT tlpe FROM QisTransactionLabPhysicalExam tlpe WHERE tlpe.id = :id")
	QisTransactionLabPhysicalExam getTransactionLabPhysicalExamByLabReqId(@Param("id") Long id);
}
