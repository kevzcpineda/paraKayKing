package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisFecalysisConsistency {
	FRM("FORMED"), SFRM("SEMI-FORMED"), SFT("SOFT"), WTR("WATERY"), SMCD("SLIGHTLY MUCOID"), MCD("MUCOID");

	private String consistency;

	QisFecalysisConsistency(String consistency) {
		this.consistency = consistency;
	}

	public String getConsistency() {
		return consistency;
	}

	private static final Map<String, String> consistencyList = new HashMap<>();
	static {
		for (QisFecalysisConsistency cat : QisFecalysisConsistency.values()) {
			consistencyList.put(String.valueOf(cat), cat.getConsistency());
		}
	}

	public static Map<String, String> getConsistencyList() {
		return consistencyList;
	}
}
