package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisDispatchType {
	PU("PICK-UP"), E("EMAIL"), OL("ONLINE");

	private String dispatchType;

	QisDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}

	public String getDispatchType() {
		return dispatchType;
	}

	private static final Map<String, String> dispatchTypes = new HashMap<>();
	static {
		for (QisDispatchType dtyp : QisDispatchType.values()) {
			dispatchTypes.put(String.valueOf(dtyp), dtyp.getDispatchType());
		}
	}

	public static Map<String, String> getDispatchTypes() {
		return dispatchTypes;
	}

}
