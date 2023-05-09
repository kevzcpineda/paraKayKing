package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisUserRole;

@Repository
public interface QisUserRoleRepository extends JpaRepository<QisUserRole, Long>{
	QisUserRole findByName(String name);
	Boolean existsByName(String name);

	@Query("SELECT r FROM QisUserRole r WHERE r.id = :id")
	public QisUserRole findByRoleId(@Param("id") Long id);

	@Query("SELECT r FROM QisUserRole r WHERE r.name = :rolename AND r.id <> :id")
    public QisUserRole findRoleNameOnOtherRoles(@Param("rolename") String rolename, @Param("id") Long id);
	
}
