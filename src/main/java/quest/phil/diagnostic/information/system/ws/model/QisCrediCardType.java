package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisCrediCardType {
	MC("MASTER CARD"), VC("VISA CARD"), AMEX("AMERICAN EXPRESS"), DC("DISCOVER"), JCB("JAPAN CREDIT BUREAU(JCB)"),
	MA("MAESTRO");

	private String creditCardType;

	QisCrediCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	public String getCreditCardType() {
		return creditCardType;
	}

	private static final Map<String, String> creditCardTypes = new HashMap<>();
	static {
		for (QisCrediCardType cctyp : QisCrediCardType.values()) {
			creditCardTypes.put(String.valueOf(cctyp), cctyp.getCreditCardType());
		}
	}

	public static Map<String, String> getCreditCardTypes() {
		return creditCardTypes;
	}
}
