package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;

@Repository
public interface QisUserPersonelRepository extends JpaRepository<QisUserPersonel, Long> {
	QisUserPersonel findByUserid(String userid);

	@Query("SELECT up FROM QisUserPersonel up WHERE up.id = :id")
	QisUserPersonel getUserPersonelByUserId(@Param("id") Long id);

}
