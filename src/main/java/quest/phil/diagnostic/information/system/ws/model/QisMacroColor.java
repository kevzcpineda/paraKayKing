package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisMacroColor {
	STR("STRAW"), LYW("LIGHT YELLOW"), YLW("YELLOW"), DYW("DARK YELLOW"), RED("RED"), ORG("ORANGE"), AMB("AMBER");

	private String colorType;

	QisMacroColor(String colorType) {
		this.colorType = colorType;
	}

	public String getColorType() {
		return colorType;
	}

	private static final Map<String, String> colorTypeList = new HashMap<>();
	static {
		for (QisMacroColor cat : QisMacroColor.values()) {
			colorTypeList.put(String.valueOf(cat), cat.getColorType());
		}
	}

	public static Map<String, String> getCategories() {
		return colorTypeList;
	}
}
