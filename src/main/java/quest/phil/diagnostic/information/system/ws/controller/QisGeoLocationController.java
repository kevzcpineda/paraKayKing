package quest.phil.diagnostic.information.system.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.model.entity.Country;
import quest.phil.diagnostic.information.system.ws.repository.CountryRepository;

@RestController
@RequestMapping("/api/v1/")
public class QisGeoLocationController {
	@Autowired
	private CountryRepository countryRepository;

	@GetMapping("countries")
	public List<Country> getAllCountries(){
		return countryRepository.findAll();
	}

	@GetMapping("nationalities")
	public List<Country> getAllNationalities(){
		return countryRepository.findAllNationalities();
	}
}
