package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisLogout;

@Repository
public interface QisLogoutRepository  extends JpaRepository<QisLogout, Long>{

    @Query("SELECT l FROM QisLogout l WHERE l.qisUserId = :id AND l.token = :token")
    public QisLogout findLogoutUserToken(@Param("id") Long id, @Param("token") String token);	
}
