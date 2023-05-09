package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisChargeType {
	CORP("CORPORATE"), HMO("HMO"), REB("REBATE"), STF("STAFF"), APE("APE"), CASH("CORPORATE CASH"), MMO("MEDICAL MISSION");

	private String chargeType;

	QisChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getChargeType() {
		return chargeType;
	}

	private static final Map<String, String> chargeTypes = new HashMap<>();
	static {
		for (QisChargeType ctyp : QisChargeType.values()) {
			chargeTypes.put(String.valueOf(ctyp), ctyp.getChargeType());
		}
	}

	public static Map<String, String> getChargeTypes() {
		return chargeTypes;
	}
}
