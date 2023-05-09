package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisFecalysisColor {
	GRN("GREEN"), YLW("YELLOW"), LBRN("LIGHT BROWN"), BRN("BROWN"), DBRN("DARK BROWN"), RED("RED");

	private String colorType;

	QisFecalysisColor(String colorType) {
		this.colorType = colorType;
	}

	public String getColorType() {
		return colorType;
	}

	private static final Map<String, String> colorTypeList = new HashMap<>();
	static {
		for (QisFecalysisColor cat : QisFecalysisColor.values()) {
			colorTypeList.put(String.valueOf(cat), cat.getColorType());
		}
	}

	public static Map<String, String> getColorTypeList() {
		return colorTypeList;
	}
}
