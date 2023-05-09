package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QisTransactionAuthorizeRequest  implements Serializable{
	private static final long serialVersionUID = 1434101437299666953L;

	@NotNull(message = "Authorization Id is required.")
	@Size(max = 20, message = "Authorization Id should not exceed {max} characters.")
	private String authorizeId;

	@NotNull(message = "Description is required.")
	@NotEmpty(message = "Description must not empty.")
	@Size(max = 250, message = "Description should not exceed {max} characters.")
	private String description;

	public QisTransactionAuthorizeRequest() {
		super();
	}

	public String getAuthorizeId() {
		return authorizeId;
	}

	public void setAuthorizeId(String authorizeId) {
		this.authorizeId = authorizeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
