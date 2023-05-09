package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisDiscountType {
	NRM("NORMAL DISCOUNT"), SCD("SENIOR CITIZEN DISCOUNT"), PWD("PERSON WITH DISABILITY DISCOUNT(PWD)");

	private String discountType;

	QisDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getDiscountType() {
		return discountType;
	}

	private static final Map<String, String> discountTypes = new HashMap<>();
	static {
		for (QisDiscountType dtyp : QisDiscountType.values()) {
			discountTypes.put(String.valueOf(dtyp), dtyp.getDiscountType());
		}
	}

	public static Map<String, String> getDiscountTypes() {
		return discountTypes;
	}
}
