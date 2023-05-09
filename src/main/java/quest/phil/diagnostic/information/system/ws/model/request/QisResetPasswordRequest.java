package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class QisResetPasswordRequest implements Serializable {

	private static final long serialVersionUID = 6542977071550111612L;

	@NotEmpty(message = "Password is required.")
	@Size(min = 6, max = 120, message = "Password must between {min} and {max} characters.")
	private String password;

	public QisResetPasswordRequest() {
		super();
	}
	
	public QisResetPasswordRequest(
			@NotEmpty(message = "Password is required.") @Size(min = 6, max = 120, message = "Password must between {min} and {max} characters.") String password) {
		super();
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
