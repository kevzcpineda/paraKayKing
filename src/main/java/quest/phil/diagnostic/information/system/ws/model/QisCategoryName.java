package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisCategoryName {
	ECG("CARDIOGRAM"), HS("HOME SERVICE"), INS("INSURANCE"), LAB("LABORATORY"), MS("MEDICAL SERVICE"),
	SUP("SUPPLY"), US("ULTRASOUND"), XR("XRAY"), PHA("PHARMACY");

	private String category;

	QisCategoryName(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	private static final Map<String, String> categories = new HashMap<>();
	static {
		for (QisCategoryName cat : QisCategoryName.values()) {
			categories.put(String.valueOf(cat), cat.getCategory());
		}
	}

	public static Map<String, String> getCategories() {
		return categories;
	}
}
