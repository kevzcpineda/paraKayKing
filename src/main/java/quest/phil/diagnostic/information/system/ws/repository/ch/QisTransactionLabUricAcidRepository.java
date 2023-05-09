package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabUricAcid;

@Repository
public interface QisTransactionLabUricAcidRepository extends JpaRepository<QisTransactionLabUricAcid, Long> {
	@Query("SELECT tlua FROM QisTransactionLabUricAcid tlua WHERE tlua.id = :id")
	QisTransactionLabUricAcid getTransactionLabUricAcidByLabReqId(@Param("id") Long id);

}
