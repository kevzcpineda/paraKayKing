package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class JwtRequest implements Serializable {

	private static final long serialVersionUID = -711517752381670582L;

	@NotEmpty(message = "Username is required.")
	@Size(min = 4, max = 120, message = "Username must between {min} and {max} characters.")
	private String username;

	@NotEmpty(message = "Password is required.")
	@Size(min = 6, max = 120, message = "Password must between {min} and {max} characters.")
	private String password;

	// need default constructor for JSON Parsing
	public JwtRequest() {
		super();
	}

	public JwtRequest(
			@NotEmpty(message = "Username is required.") @Size(min = 4, max = 120, message = "Username must between {min} and {max} characters.") String username,
			@NotEmpty(message = "Password is required.") @Size(min = 6, max = 120, message = "Password must between {min} and {max} characters.") String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
