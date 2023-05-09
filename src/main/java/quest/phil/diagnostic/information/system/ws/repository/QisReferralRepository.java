package quest.phil.diagnostic.information.system.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisReferral;

@Repository
public interface QisReferralRepository extends JpaRepository<QisReferral, Long> {
	QisReferral findByReferralid(String referralid);

	Boolean existsByReferralid(String referralid);

	@Query("SELECT r FROM QisReferral r WHERE r.isActive = true")
	List<QisReferral> allActiveItems();
}
