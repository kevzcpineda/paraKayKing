package quest.phil.diagnostic.information.system.ws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUser;
import quest.phil.diagnostic.information.system.ws.repository.QisRoleRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisUserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    QisRoleRepository qisRoleRepository;
    
    @Autowired
    QisUserRepository qisUserRepository;

    @Autowired
    PasswordEncoder encoder;
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		QisUser user = qisUserRepository.findByUsername(username);		
		if (user != null && user.isActive()) {
			return QisUserDetails.build(user);
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
	
	public UserDetails loadUserByUsernamePassword(String username, String password) throws UsernameNotFoundException {
		QisUser loginUser = qisUserRepository.findByUsername(username);
		if(loginUser != null && loginUser.isActive() && encoder.matches(password, loginUser.getPassword())) {
			return QisUserDetails.build(loginUser);			
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
	
	public QisUser getQisUser(String username) throws UsernameNotFoundException {
		QisUser user = qisUserRepository.findByUsername(username);		
		if (user != null && user.isActive()) {
			return user;
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}		
	}
}
