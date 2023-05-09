package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.QisAdvancePayment;

public interface QisAdvancePaymentRepository extends JpaRepository<QisAdvancePayment, Long> {

	@Query("SELECT p FROM QisAdvancePayment p WHERE DATE(p.createdAt) BETWEEN DATE(:date_and_time_from) AND DATE(:date_and_time_to)")
	List<QisAdvancePayment> getAdvancePaymentDate(@Param("date_and_time_from") Calendar date_and_time_from,
			@Param("date_and_time_to") Calendar date_and_time_to);
}
