package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisPackage;

@Repository
public interface QisPackageRepository extends JpaRepository<QisPackage, Long> {
	QisPackage findByPackageid(String packageid);

	QisPackage findByPackageName(String packageName);

	Boolean existsByPackageid(String packageid);

	Boolean existsByPackageName(String packageName);

	@Query("SELECT p FROM QisPackage p WHERE p.packageName = :packageName AND p.id <> :id")
	public QisPackage findPackageNameOnOtherPackage(@Param("packageName") String packageName, @Param("id") Long id);

	@Query("SELECT p FROM QisPackage p WHERE p.packageName LIKE :searchKey% OR p.packageDescription LIKE :searchKey%")
	Page<QisPackage> searchPackages(@Param("searchKey") String searchKey, Pageable pageable);

	@Query("SELECT p FROM QisPackage p WHERE p.isActive = true")
	Page<QisPackage> allActivePackages(Pageable pageable);
}
