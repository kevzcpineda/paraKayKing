package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;
import java.util.List;

@Repository
public interface QisUserRepository extends JpaRepository<QisUser, Long> {
    QisUser findByUsername(String username);
    QisUser findByEmail(String useremail);
    QisUser findByUserid(String userid);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUserid(String userid);

    @Query("SELECT u FROM QisUser u WHERE u.email = :email AND u.id <> :id")
    public QisUser findEmailOnOtherUser(@Param("email") String email, @Param("id") Long id);
    
    @Query("SELECT u FROM QisUser u WHERE LOWER(u.username) = LOWER(:username) AND u.id <> :id")
    public QisUser findUsernameOnOtherUser(@Param("username") String username, @Param("id") Long id);
}
