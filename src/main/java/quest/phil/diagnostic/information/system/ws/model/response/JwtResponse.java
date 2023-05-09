package quest.phil.diagnostic.information.system.ws.model.response;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = 6423258906891779817L;

	private final String token;
	private final String userid;
	private final String username;
	private final String email;
	private final Long expiresIn;
	private final Collection<? extends GrantedAuthority> authorities;

	public JwtResponse(String token, String userid, String username, String email, Long expiresIn,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.token = token;
		this.userid = userid;
		this.username = username;
		this.email = email;
		this.expiresIn = expiresIn;
		this.authorities = authorities;
	}

	public String getToken() {
		return token;
	}

	public String getUserid() {
		return userid;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
}
