package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisUserProfile;

@Repository
public interface QisUserProfileRepository extends JpaRepository<QisUserProfile, Long> {
	@Query("SELECT up FROM QisUserProfile up WHERE up.id = :id")
	QisUserProfile getUserProfileByUserId(@Param("id") Long id);
}
