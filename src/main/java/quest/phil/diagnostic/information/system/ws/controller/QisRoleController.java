package quest.phil.diagnostic.information.system.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.model.entity.QisRole;
import quest.phil.diagnostic.information.system.ws.repository.QisRoleRepository;

@RestController
@RequestMapping("/api/v1/")
public class QisRoleController {
	@Autowired
	private QisRoleRepository qisRoleRepository;

	// LIST ALL ROLES
	@GetMapping("roles")
	public List<QisRole> getAllRoles() {
		return qisRoleRepository.findAll();
	}
}
