package quest.phil.diagnostic.information.system.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;

@Repository
public interface QisBranchRepository extends JpaRepository<QisBranch, Long> {
	QisBranch findByBranchid(String branchid);
	QisBranch findByBranchCode(String branchCode);
	Boolean existsByBranchid(String branchid);
	Boolean existsByBranchCode(String branchCode);

    @Query("SELECT b FROM QisBranch b WHERE b.branchCode = :branchCode AND b.id <> :id")
    public QisBranch findBranchCodeOnOtherBranch(@Param("branchCode") String branchCode, @Param("id") Long id);
	
}
