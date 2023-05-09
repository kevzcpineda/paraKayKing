package quest.phil.diagnostic.information.system.ws.repository.ch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ch.QisTransactionLabProtein;

@Repository
public interface QisTransactionLabProteinRepository extends JpaRepository<QisTransactionLabProtein, Long> {
	@Query("SELECT tlp FROM QisTransactionLabProtein tlp WHERE tlp.id = :id")
	QisTransactionLabProtein getTransactionLabProteinByLabReqId(@Param("id") Long id);
}
