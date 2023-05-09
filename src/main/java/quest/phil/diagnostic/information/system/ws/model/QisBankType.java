package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisBankType {
	BDO("BDO"), CB("CHINA BANK"), SB("SECURITY BANK"), BPI("BPI"), UB("UNION BANK"), UCPB("UCPB"), MB("METRO BANK"), RCBC("RCBC"), PNB("PNB"), DBP("DEVELOPMENT BANK OF THE PHILIPPINES"), LB("LAND BANK"), EWB("EASTWEST BANK");

	private String bankType;

	QisBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getBankType() {
		return bankType;
	}

	private static final Map<String, String> bankTypes = new HashMap<>();
	static {
		for (QisBankType bt : QisBankType.values()) {
			bankTypes.put(String.valueOf(bt), bt.getBankType());
		}
	}

	public static Map<String, String> getBankTypes() {
		return bankTypes;
	}
}
