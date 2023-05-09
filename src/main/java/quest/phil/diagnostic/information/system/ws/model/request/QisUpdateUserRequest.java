package quest.phil.diagnostic.information.system.ws.model.request;

import java.util.Set;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class QisUpdateUserRequest implements Serializable {

	private static final long serialVersionUID = 6739611020878421592L;

	@NotEmpty(message = "Username is required.")
	@Size(min = 4, max = 120, message = "Username must between {min} and {max} characters.")
	private String username;

	@NotEmpty(message = "Email is required.")
	@Size(max = 120, message = "Email has a maximum {max} characters.")
	@Email(message = "Input should be an email format.")
	private String email;

	@Size(min = 6, max = 120, message = "Password must between {min} and {max} characters.")
	private String password;
	
	private Set<String> roles;

	private Boolean isActive;

	// need default constructor for JSON Parsing
	public QisUpdateUserRequest() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}
