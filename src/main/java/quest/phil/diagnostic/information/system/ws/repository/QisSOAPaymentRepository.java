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
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOAPayment;

@Repository
public interface QisSOAPaymentRepository extends JpaRepository<QisSOAPayment, Long> {
	@Query("SELECT p FROM QisSOAPayment p WHERE p.chargeToId = :chargeToId AND p.id = :id")
	QisSOAPayment getSOAPaymentById(@Param("chargeToId") Long chargeToId, @Param("id") Long id);

	@Query("SELECT p FROM QisSOAPayment p WHERE p.chargeToId = :chargeToId AND YEAR(p.paymentDate) = :year ORDER BY p.paymentDate")
	Page<QisSOAPayment> getSOAPaymentYear(@Param("chargeToId") Long chargeToId, @Param("year") int year,
			Pageable pageable);

	@Query("SELECT p FROM QisSOAPayment p WHERE DATE(p.createdAt) BETWEEN DATE(:date_and_time_from) AND DATE(:date_and_time_to)")
	List<QisSOAPayment> getSOAPaymentDate(@Param("date_and_time_from") Calendar date_and_time_from,
			@Param("date_and_time_to") Calendar date_and_time_to);

	// CHARGE TO PER MONTH
	@Query("SELECT s FROM QisSOAPayment s WHERE s.chargeToId = :chargeToId AND s.paymentDate BETWEEN :dateTimeFrom AND :dateTimeTo ORDER BY s.paymentDate")
	Page<QisSOAPayment> chargeToSOAPaymentMonth(@Param("chargeToId") Long chargeToId, @Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable);

	@Query("SELECT p FROM QisSOAPayment p WHERE p.chargeToId = :chargeToId AND YEAR(p.paymentDate) = :year ORDER BY p.paymentDate")
	List<QisSOAPayment> getSOAPaymentYearList(@Param("chargeToId") Long chargeToId, @Param("year") int year);
}
