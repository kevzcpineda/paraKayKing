package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class QisAuthorizationRequest implements Serializable {

	private static final long serialVersionUID = 7528210638190442882L;

	@NotEmpty(message = "Username is required.")
	@Size(min = 4, max = 120, message = "Username must between {min} and {max} characters.")
	private String username;

	@NotEmpty(message = "Password is required.")
	@Size(min = 6, max = 120, message = "Password must between {min} and {max} characters.")
	private String password;

	@Size(max = 250, message = "Reason must exceed to {max} characters.")
	private String reason;

	// need default constructor for JSON Parsing
	public QisAuthorizationRequest() {
		super();
	}

	public QisAuthorizationRequest(
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
