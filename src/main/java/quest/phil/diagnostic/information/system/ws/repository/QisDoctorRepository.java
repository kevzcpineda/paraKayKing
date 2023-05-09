package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;

@Repository
public interface QisDoctorRepository extends JpaRepository<QisDoctor, Long> {
	QisDoctor findByDoctorid(String doctorid);
	QisDoctor findByLicenseNumber(String licenseNumber);
	Boolean existsByDoctorid(String doctorid);
	Boolean existsByLicenseNumber(String licenseNumber);
	
    @Query("SELECT d FROM QisDoctor d WHERE d.licenseNumber = :licenseNumber AND d.id <> :id")
    public QisDoctor findLicenseNumberOnOtherDoctor(@Param("licenseNumber") String licenseNumber, @Param("id") Long id);
	
}
