package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.QisRoleName;
import quest.phil.diagnostic.information.system.ws.model.entity.QisRole;

@Repository
public interface QisRoleRepository extends JpaRepository<QisRole, Long> {
	Optional<QisRole> findByName(QisRoleName roleName);
}
