package quest.phil.diagnostic.information.system.ws.model.response;

import java.io.Serializable;

public class QisOptionResponse implements Serializable {

	private static final long serialVersionUID = 2805184882041627183L;
	private String key;
	private String value;

	public QisOptionResponse() {
		super();
	}

	public QisOptionResponse(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
