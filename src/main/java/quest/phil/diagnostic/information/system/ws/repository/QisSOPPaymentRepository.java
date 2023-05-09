package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisSOAPayment;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOPPayment;

@Repository
public interface QisSOPPaymentRepository extends JpaRepository<QisSOPPayment, Long> {
	
	@Query("SELECT p FROM QisSOPPayment p WHERE p.referenceId = :referenceId AND YEAR(p.paymentDate) = :year ORDER BY p.paymentDate")
	Page<QisSOPPayment> getSOPPaymentYear(@Param("referenceId") Long referenceId, @Param("year") int year,
			Pageable pageable);
	
	@Query("SELECT p FROM QisSOPPayment p WHERE p.referenceId = :referenceId AND p.id = :id")
	QisSOPPayment getSOPPaymentById(@Param("referenceId") Long referenceId, @Param("id") Long id);
	
}
