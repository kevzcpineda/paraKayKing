package quest.phil.diagnostic.information.system.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisCorporate;

@Repository
public interface QisCorporateRepository extends JpaRepository<QisCorporate, Long> {
	QisCorporate findByCorporateid(String corporateid);
	QisCorporate findByCompanyName(String companyName);
	QisCorporate findBySoaCode(String soaCode);
	Boolean existsByCorporateid(String corporateid);
	Boolean existsByCompanyName(String companyName);
	Boolean existsBySoaCode(String soaCode);

	@Query("SELECT c FROM QisCorporate c WHERE c.companyName = :companyName AND c.id <> :id")
	public QisCorporate findCompanyNameOnOtherCorporate(@Param("companyName") String companyName, @Param("id") Long id);

	@Query("SELECT c FROM QisCorporate c WHERE c.soaCode = :soaCode AND c.id <> :id")
	public QisCorporate findSoaCodeOnOtherCorporate(@Param("soaCode") String soaCode, @Param("id") Long id);
	
	@Query("SELECT c FROM QisCorporate c WHERE c.isActive = true")
	List<QisCorporate> allActiveCorporates();
	
	@Query("SELECT c FROM QisCorporate c WHERE c.chargeType = 'CORP' OR c.chargeType = 'HMO' OR c.chargeType = 'APE'")
	List<QisCorporate> allActiveCorporatesCorp();
}
