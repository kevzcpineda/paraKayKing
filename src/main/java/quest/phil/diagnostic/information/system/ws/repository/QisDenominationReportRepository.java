package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.classes.QisDenominationReportsClass;

public interface QisDenominationReportRepository extends JpaRepository<QisDenominationReportsClass, Long> {

	@Query("Select i From QisDenominationReportsClass i where i.coverageDateAndTimeFrom = :date_and_time_from AND i.coverageDateAndTimeTo = :date_and_time_to AND i.branchId = :branchid")
	public QisDenominationReportsClass findDenominationReport(@Param("date_and_time_from") Calendar date_and_time_from,
			@Param("date_and_time_to") Calendar date_and_time_to, @Param("branchid") Long branchid);

	@Query("Select i From QisDenominationReportsClass i where DATE(i.createdAt) BETWEEN DATE(:date_and_time_from) AND DATE(:date_and_time_to) AND i.branchId = :branchid ORDER BY i.createdAt ASC")
	public List<QisDenominationReportsClass> findByCreatedDate(@Param("date_and_time_from") Calendar date_and_time_from,
			@Param("date_and_time_to") Calendar date_and_time_to, @Param("branchid") Long branchid);
}
