package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import quest.phil.diagnostic.information.system.ws.model.entity.QisMarker;

public interface QisMarkerRepository extends JpaRepository<QisMarker, Long> {

	QisMarker findById(String referralid);

	@Query("SELECT ti FROM QisMarker ti WHERE ti.qisTransaction = :qisTransaction AND ti.xrayType = :xrayType AND ti.spoiled = :isSpoiled")
	QisMarker getTxnIdAndByXrayType(@Param("qisTransaction") String qisTransaction, @Param("xrayType") String xrayType, Boolean isSpoiled);
	
	
	@Query("SELECT ts FROM QisMarker ts WHERE ts.createdAt BETWEEN :dateTimeFrom AND :dateTimeTo ORDER BY ts.createdAt")
	List<QisMarker> getMarkserByDateTimeRange(@Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo);
}
