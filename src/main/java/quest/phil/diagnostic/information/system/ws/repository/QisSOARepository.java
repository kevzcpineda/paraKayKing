package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisSOA;

@Repository
public interface QisSOARepository extends JpaRepository<QisSOA, Long> {
	@Query("SELECT s FROM QisSOA s WHERE s.chargeToId = :chargeToId AND s.id = :id")
	QisSOA getSOAById(@Param("chargeToId") Long chargeToId, @Param("id") Long id);

	@Query("SELECT s FROM QisSOA s WHERE s.chargeToId = :chargeToId "
			+ "AND soaCount = (SELECT MAX(soaCount) FROM QisSOA WHERE chargeToId = :chargeToId) "
			+ "ORDER BY soaCount DESC")
	QisSOA getLastSOAStatements(@Param("chargeToId") Long chargeToId);

	// ALL SOA
	@Query("SELECT s FROM QisSOA s WHERE YEAR(s.statementDate) = :year ORDER BY s.statementDate DESC")
	Page<QisSOA> allChargeToSOAYear(@Param("year") int year, Pageable pageable);

	// ALL SOA PER MONTH
	@Query("SELECT s FROM QisSOA s WHERE s.createdAt BETWEEN :dateTimeFrom AND :dateTimeTo ORDER BY s.statementDate DESC")
	Page<QisSOA> allChargeToSOAMonth(@Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable);

	// CHARGE TO
	@Query("SELECT s FROM QisSOA s WHERE s.chargeToId = :chargeToId AND YEAR(s.statementDate) = :year ORDER BY s.statementDate DESC")
	Page<QisSOA> chargeToSOAYear(@Param("chargeToId") Long chargeToId, @Param("year") int year, Pageable pageable);

	// CHARGE TO PER MONTH
	@Query("SELECT s FROM QisSOA s WHERE s.chargeToId = :chargeToId AND s.createdAt BETWEEN :dateTimeFrom AND :dateTimeTo ORDER BY s.statementDate DESC")
	Page<QisSOA> chargeToSOAMonth(@Param("chargeToId") Long chargeToId, @Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable);

	// CHARGE TO LIST
	@Query("SELECT s FROM QisSOA s WHERE s.chargeToId = :chargeToId AND YEAR(s.statementDate) = :year ORDER BY s.statementDate DESC")
	List<QisSOA> chargeToSOAYearList(@Param("chargeToId") Long chargeToId, @Param("year") int year);
}
