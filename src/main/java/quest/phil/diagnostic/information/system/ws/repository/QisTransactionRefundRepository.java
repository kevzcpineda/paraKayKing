package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionRefund;

@Repository
public interface QisTransactionRefundRepository extends JpaRepository<QisTransactionRefund, Long> {

}
