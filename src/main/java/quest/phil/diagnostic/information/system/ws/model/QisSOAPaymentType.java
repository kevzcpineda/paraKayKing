package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisSOAPaymentType {
	CA("CASH"), BNK("BANK DEPOSIT"), CHQ("CHEQUE"), VT("VIRTUAL");

	private String paymentType;

	QisSOAPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	private static final Map<String, String> paymentTypes = new HashMap<>();
	static {
		for (QisSOAPaymentType ptyp : QisSOAPaymentType.values()) {
			paymentTypes.put(String.valueOf(ptyp), ptyp.getPaymentType());
		}
	}

	public static Map<String, String> getPaymentTypes() {
		return paymentTypes;
	}
}
