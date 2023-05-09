package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;

@Repository
public interface QisTransactionLaboratoryInfoRepository extends JpaRepository<QisTransactionLaboratoryInfo, Long>{

}
