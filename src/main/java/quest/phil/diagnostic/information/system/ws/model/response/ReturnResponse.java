package quest.phil.diagnostic.information.system.ws.model.response;

import java.io.Serializable;

public class ReturnResponse implements Serializable {

	private static final long serialVersionUID = 5872833703015046591L;
	private final String status;
	private final String message;

	public ReturnResponse(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
	
	
}
