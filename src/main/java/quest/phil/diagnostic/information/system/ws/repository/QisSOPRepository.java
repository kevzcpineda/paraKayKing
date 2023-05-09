package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.QisSOA;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOP;

public interface QisSOPRepository extends JpaRepository<QisSOP, Long> {

	@Query("SELECT s FROM QisSOP s WHERE s.referenceId = :referenceId "
			+ "AND sopCount = (SELECT MAX(sopCount) FROM QisSOP WHERE referenceId = :referenceId) "
			+ "ORDER BY sopCount DESC")
	QisSOP getLastSOPStatements(@Param("referenceId") Long referenceId);

	@Query("SELECT s FROM QisSOP s WHERE s.referenceId = :referenceId " + "ORDER BY s.statementDate")
	Page<QisSOP> getListSop(@Param("referenceId") Long referenceId, Pageable pageable);
	
	//By Date
	@Query("SELECT s FROM QisSOP s WHERE s.statementDate BETWEEN :dateTimeFrom AND :dateTimeTo " 
			+ "AND  s.referenceId = :referenceId " + "ORDER BY s.statementDate")
	Page<QisSOP> getListSopByDate(@Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo,@Param("referenceId") Long referenceId, Pageable pageable);

	@Query("SELECT s FROM QisSOP s WHERE s.referenceId = :referenceId")
	QisSOP getSop(@Param("referenceId") Long referenceId);

	@Query("SELECT s FROM QisSOP s WHERE s.referenceId = :referenceId AND s.id = :sopId")
	QisSOP getSopById(@Param("sopId") Long sopId, @Param("referenceId") Long referenceId);

	// REFERENCE LAB TO
	@Query("SELECT s FROM QisSOP s WHERE s.referenceId = :referenceId AND YEAR(s.statementDate) = :year ORDER BY s.statementDate DESC")
	Page<QisSOP> referenceToSOPYear(@Param("referenceId") Long referenceId, @Param("year") int year, Pageable pageable);

}
