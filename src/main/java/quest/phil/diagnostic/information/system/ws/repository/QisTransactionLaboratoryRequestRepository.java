package quest.phil.diagnostic.information.system.ws.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryRequest;

@Repository
public interface QisTransactionLaboratoryRequestRepository extends JpaRepository<QisTransactionLaboratoryRequest, Long>{
	@Query("SELECT tl FROM QisTransactionLaboratoryRequest tl WHERE tl.transactionid = :transactionid")
	List<QisTransactionLaboratoryRequest> getTransactionLaboratoryRequestByTransactionid(@Param("transactionid") Long transactionid);

	@Query("SELECT tl FROM QisTransactionLaboratoryRequest tl WHERE tl.transactionid = :transactionid AND tl.id = :id")
	QisTransactionLaboratoryRequest getTransactionLaboratoryRequestById(@Param("transactionid") Long transactionid, @Param("id") Long id);

	@Query("SELECT tl FROM QisTransactionLaboratoryRequest tl WHERE tl.transactionid = :transactionid AND tl.transactionItemId = :transactionItemId")
	List<QisTransactionLaboratoryRequest> getTransactionLaboratoryRequestByTransactionidItemid(@Param("transactionid") Long transactionid, @Param("transactionItemId") Long transactionItemId);	


	@Query("SELECT tl FROM QisTransactionLaboratoryRequest tl WHERE tl.transactionid = :transactionid AND tl.itemLaboratory = :itemLaboratory")
	QisTransactionLaboratoryRequest getTransactionLaboratoryRequestByIdAndLabReq(@Param("transactionid") Long transactionid, @Param("itemLaboratory") String itemLaboratory);
	
	@Query("SELECT tl FROM QisTransactionLaboratoryRequest tl WHERE tl.createdAt BETWEEN :dateTimeFrom AND :dateTimeTo AND tl.itemLaboratory = :itemLaboratory")
	Page<QisTransactionLaboratoryRequest> getLaboratoryRequestByTransactionDateTimeRange(@Param("dateTimeFrom") Calendar dateTimeFrom,
			@Param("dateTimeTo") Calendar dateTimeTo, Pageable pageable, @Param("itemLaboratory") String itemLaboratory);
	
	@Query("SELECT tl FROM QisTransactionLaboratoryRequest tl WHERE tl.transactionid = :transactionid AND tl.itemLaboratory = :itemLaboratory")
	List<QisTransactionLaboratoryRequest> getTransactionLaboratoryRequestByIdAndLabReqList(@Param("transactionid") Long transactionid, @Param("itemLaboratory") String itemLaboratory);
	
	@Query("SELECT tl FROM QisTransactionLaboratoryRequest tl WHERE tl.transactionid = :id")
	QisTransactionLaboratoryRequest getTransactionLaboratoryRequestById(@Param("id") Long id);
}
