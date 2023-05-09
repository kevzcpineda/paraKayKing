package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QisUserRoleRequest implements Serializable {

	private static final long serialVersionUID = 9047526612277109801L;

	@NotEmpty(message = "Role name is required.")
	@Size(min = 4, max = 40, message = "Role name must between {min} and {max} characters.")
	private String rolename;

	public QisUserRoleRequest() {
		super();
	}

	public String getRolename() {
		return "ROLE_" + rolename.trim().toUpperCase();
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	@Override
	public String toString() {
		String serialized = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			jpe.printStackTrace();
		}
		return serialized;
	}
}
