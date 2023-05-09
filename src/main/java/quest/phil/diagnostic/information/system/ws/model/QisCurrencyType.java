package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisCurrencyType {
	PHP("PHILIPPINE PESO"), USD("UNITED STATED DOLLAR");

	private String currencyType;

	QisCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	private static final Map<String, String> currencyTypes = new HashMap<>();
	static {
		for (QisCurrencyType ctyp : QisCurrencyType.values()) {
			currencyTypes.put(String.valueOf(ctyp), ctyp.getCurrencyType());
		}
	}

	public static Map<String, String> getCurrencyTypes() {
		return currencyTypes;
	}

}
