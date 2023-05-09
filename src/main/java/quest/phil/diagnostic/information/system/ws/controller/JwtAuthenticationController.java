package quest.phil.diagnostic.information.system.ws.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLogout;
import quest.phil.diagnostic.information.system.ws.model.request.JwtRequest;
import quest.phil.diagnostic.information.system.ws.model.response.JwtResponse;
import quest.phil.diagnostic.information.system.ws.model.response.ReturnResponse;
import quest.phil.diagnostic.information.system.ws.security.JwtTokenUtil;
import quest.phil.diagnostic.information.system.ws.service.JwtUserDetailsService;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;
import quest.phil.diagnostic.information.system.ws.service.QisLogoutService;

@RestController
@RequestMapping("/api/v1/")
public class JwtAuthenticationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private QisLogService qisLogService;

	@Autowired
	private QisLogoutService qisLogoutService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody JwtRequest authenticationRequest)
			throws Exception {
		LOGGER.info("Log in user:" + authenticationRequest.getUsername());
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsernamePassword(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		QisUserDetails qisUserDetails = (QisUserDetails) userDetails;
		final String token = jwtTokenUtil.generateToken(qisUserDetails);

		qisLogService.info(qisUserDetails.getId(), JwtAuthenticationController.class.getSimpleName(), "LOGIN",
				"User:" + authenticationRequest.getUsername(), "AUTHENTICATE");
		JwtResponse jwtResponse = new JwtResponse(token, qisUserDetails.getUserid(), qisUserDetails.getUsername(),
				qisUserDetails.getEmail(), jwtTokenUtil.getJwtTokenValidity(), qisUserDetails.getAuthorities());
		return ResponseEntity.ok(jwtResponse);
	}

	@PostMapping("/logout/{userId}")
	public ResponseEntity<?> logoutUser(@PathVariable String userId, @AuthenticationPrincipal QisUserDetails authUser)
			throws Exception {

		LOGGER.info("Log out user:" + userId);
		if (authUser.getUserid().equals(userId)) {
			QisLogout qisLogout = qisLogoutService.logoutUserToken(authUser.getId());
			if (qisLogout != null) {
				qisLogService.info(authUser.getId(), JwtAuthenticationController.class.getSimpleName(), "LOGOUT",
						"User:" + authUser.getUsername(), "AUTHENTICATE");
			}
		} else {
			throw new RuntimeException("Invalid request.");
		}

		return ResponseEntity.ok(new ReturnResponse("success", "logout"));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
