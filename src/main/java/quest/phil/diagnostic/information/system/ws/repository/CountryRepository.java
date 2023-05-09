package quest.phil.diagnostic.information.system.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quest.phil.diagnostic.information.system.ws.model.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long>{
	
	@Query("SELECT c FROM Country c WHERE c.id = :id")
	Country findByCountryId(@Param("id") Long id);
	
	@Query("SELECT c FROM Country c WHERE c.nationality IS NOT NULL ORDER BY c.nationality")
	List<Country> findAllNationalities();
}
