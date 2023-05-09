package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisMacroTransparency {
	CLR("CLEAR"), HZY("HAZY"), SLT("SL. TURBID"), TBD("TURBID");

	private String transparency;

	QisMacroTransparency(String transparency) {
		this.transparency = transparency;
	}

	public String getTransparency() {
		return transparency;
	}

	private static final Map<String, String> transparencyList = new HashMap<>();
	static {
		for (QisMacroTransparency cat : QisMacroTransparency.values()) {
			transparencyList.put(String.valueOf(cat), cat.getTransparency());
		}
	}

	public static Map<String, String> getTransparencyList() {
		return transparencyList;
	}
}
