package quest.phil.diagnostic.information.system.ws.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import quest.phil.diagnostic.information.system.ws.model.entity.QisLogout;
import quest.phil.diagnostic.information.system.ws.repository.QisLogoutRepository;

@Service
public class QisLogoutService {

	@Autowired
	private QisLogoutRepository qisLogoutRepository;

	private HttpServletRequest httpRequest;

	@Autowired
	public void setRequest(HttpServletRequest request) {
		this.httpRequest = request;
	}

	public QisLogout logoutUserToken(Long id) {
		final String requestTokenHeader = httpRequest.getHeader("Authorization");

		QisLogout qisLogout = null;
		if (id != null && requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			String jwtToken = requestTokenHeader.substring(7);

			// search database for logout user token
			qisLogout = qisLogoutRepository.findLogoutUserToken(id, jwtToken);
			if (qisLogout == null) {
				// save to database logout user token
				qisLogout = new QisLogout(id, jwtToken);
				qisLogoutRepository.save(qisLogout);
			}
		}

		return qisLogout;
	}
}
