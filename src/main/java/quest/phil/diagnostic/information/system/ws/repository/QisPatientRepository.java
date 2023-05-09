package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;

@Repository
public interface QisPatientRepository extends JpaRepository<QisPatient, Long> {
	QisPatient findByPatientid(String patient);

	Boolean existsByPatientid(String patientid);

	@Query("SELECT p FROM QisPatient p ORDER BY p.id DESC")
	Page<QisPatient> findAllByPatientIdDesc(Pageable pageable);

	@Query("SELECT p FROM QisPatient p WHERE p.firstname = :firstname AND p.lastname = :lastname AND DATE(p.dateOfBirth) = DATE(:dateOfBirth)")
	public QisPatient findByAccount(@Param("firstname") String firstname, @Param("lastname") String lastname,
			@Param("dateOfBirth") String dateOfBirth);

	@Query("SELECT p FROM QisPatient p WHERE p.firstname = :firstname AND p.lastname = :lastname AND DATE(p.dateOfBirth) = DATE(:dateOfBirth) AND p.id <> :id")
	public QisPatient findByDuplicateAccount(@Param("firstname") String firstname, @Param("lastname") String lastname,
			@Param("dateOfBirth") String dateOfBirth, @Param("id") Long id);

	@Query("SELECT ps FROM QisPatient ps WHERE ps.firstname LIKE :searchKey% OR ps.lastname LIKE :searchKey% OR ps.middlename LIKE :searchKey% ORDER BY ps.lastname, ps.firstname, ps.middlename")
	Page<QisPatient> searchPatient(@Param("searchKey") String searchKey, Pageable pageable);
	
	@Query("SELECT ps FROM QisPatient ps WHERE CONCAT(ps.lastname, ', ',ps.firstname) LIKE %:searchKey% OR CONCAT(ps.lastname, ', ',ps.firstname, ' ', ps.middlename) LIKE %:searchKey% ORDER BY ps.lastname, ps.firstname, ps.middlename")
	Page<QisPatient> searchPatientV2(@Param("searchKey") String searchKey, Pageable pageable);

	@Query("SELECT ps FROM QisPatient ps WHERE ps.lastname LIKE :lastName% ORDER BY ps.lastname, ps.firstname, ps.middlename")
	Page<QisPatient> searchPatientLastname(@Param("lastName") String lastName, Pageable pageable);

	@Query("SELECT ps FROM QisPatient ps WHERE ps.firstname LIKE :firstName% ORDER BY ps.lastname, ps.firstname, ps.middlename")
	Page<QisPatient> searchPatientFirstname(@Param("firstName") String firstName, Pageable pageable);

	@Query("SELECT ps FROM QisPatient ps WHERE MONTH(ps.dateOfBirth) = MONTH(:dateOfBirth) AND DAY(ps.dateOfBirth) = DAY(:dateOfBirth) ORDER BY ps.lastname, ps.firstname, ps.middlename")
	Page<QisPatient> searchPatientBirthDay(@Param("dateOfBirth") Calendar dateOfBirth, Pageable pageable);

	@Query("SELECT p FROM QisPatient p WHERE p.updatedAt BETWEEN :dateTimeFrom AND :dateTimeTo ORDER BY p.updatedAt DESC")
	Page<QisPatient> findByTransactionDateAndPatientIdDesc(@Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable);
}
