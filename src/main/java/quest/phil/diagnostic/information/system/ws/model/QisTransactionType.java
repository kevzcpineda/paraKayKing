package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisTransactionType {
	// TACC("ACCOUNT"),
	TWI("WALK-IN"), THS("HOME SERVICE"), TSI("SEND-IN"), TAPE("ANNUAL PHYSICAL EXAMINATION(APE)"), TREF("REFERRAL"),
	TCH("CHARGE"), TCL("CLINICAL TRIAL"), TWL("WLMC"), TDNA("DNA ASIA"), TAC("AESTHETICS & COSMETICS"), TSU("SURGERY"),
	TAS("AMBULATORY SURGERY"), TBH("BIRTHING HOME"), TPS("PHARMACY & SUPPLY"), TMM("MEDICAL MISSION"), TMS("MEDICAL SERVICES");

	private String transactionType;

	QisTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionType() {
		return transactionType;
	}

	private static final Map<String, String> transactionTypes = new HashMap<>();
	static {
		for (QisTransactionType ttyp : QisTransactionType.values()) {
			transactionTypes.put(String.valueOf(ttyp), ttyp.getTransactionType());
		}
	}

	public static Map<String, String> getTransactionTypes() {
		return transactionTypes;
	}
}
