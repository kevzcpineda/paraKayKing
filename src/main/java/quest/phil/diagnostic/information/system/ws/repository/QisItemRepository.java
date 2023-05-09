package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;

@Repository
public interface QisItemRepository extends JpaRepository<QisItem, Long> {
	QisItem findByItemid(String itemid);

	QisItem findByItemName(String itemName);

	Boolean existsByItemid(String itemid);

	Boolean existsByItemName(String itemName);

	@Query("SELECT i FROM QisItem i WHERE i.itemName = :itemName AND i.id <> :id")
	public QisItem findItemNameOnOtherItem(@Param("itemName") String itemName, @Param("id") Long id);

	@Query("SELECT i FROM QisItem i WHERE i.itemName LIKE :searchKey% OR i.itemDescription LIKE :searchKey%")
	Page<QisItem> searchItems(@Param("searchKey") String searchKey, Pageable pageable);

	@Query("SELECT i FROM QisItem i WHERE i.isActive = true")
	Page<QisItem> allActiveItems(Pageable pageable);
	
	@Query("SELECT i FROM QisItem i WHERE i.id = :id")
	public QisItem findItemById(@Param("id") Long id);
}
