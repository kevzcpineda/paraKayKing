package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisUserInfo;

@Repository
public interface QisUserInfoRespository extends JpaRepository<QisUserInfo, Long> {
	QisUserInfo findByUserid(String userid);

	@Query("SELECT ui FROM QisUserInfo ui WHERE ui.id = :id")
	QisUserInfo getUserInfoByUserId(@Param("id") Long id);
}
