package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.classes.QisEODClass;

public interface QisEODOtNoRepository extends JpaRepository<QisEODClass, Long> {
	QisEODClass findByDateFromAndDateToAndBranchId(String dateFrom, String dateTo, Long branchId);
	
	@Query("SELECT i FROM QisEODClass i WHERE i.dateFrom = :date_from AND i.dateTo = :date_to AND i.branchId = :branchid")
	public QisEODClass findEodDateFromDateTo(@Param("date_from") String date_from, @Param("date_to") String date_to,
			@Param("branchid") Long branchid);

}
