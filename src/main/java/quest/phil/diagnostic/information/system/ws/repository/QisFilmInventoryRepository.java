package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.QisMarkerInventory;


public interface QisFilmInventoryRepository extends JpaRepository<QisMarkerInventory, Long>{
	
	@Query("SELECT ti FROM QisMarkerInventory ti WHERE ti.id = :id")
	QisMarkerInventory getFilmInventory(@Param("id") Long id);
}
