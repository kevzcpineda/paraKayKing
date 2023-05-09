package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisPaymentType {
	// HMO("HEALTH MAINTENANCE ORGANIZATION(HMO)")
	// APE("ANNUAL PHYSICAL EXAMINATION(APE)")
	
	//CC("CREDIT CARD"), HMO("HMO"), ACCT("ACCOUNT"),APE("APE"), GCA("G-CASH")
	CA("CASH"), B("BANK"), C("CHARGE"), VR ("VIRTUAL");
	
	// CASH (CA-CASH)
	// BANK (CC - CREDITCARD, DB - DEBIT CARD, CQ - CHEQUE)
	// CHARGE (ACCT - ACCOUNT, HMO - HMO, APE - APE)
	// VIRTUAL (GCA - GCASH, PMA - PAYMAYA, WT - WIRE TRANSFER, PMO - PAYMONGO)

	private String paymentType;

	QisPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	private static final Map<String, String> paymentTypes = new HashMap<>();
	static {
		for (QisPaymentType ptyp : QisPaymentType.values()) {
			paymentTypes.put(String.valueOf(ptyp), ptyp.getPaymentType());
		}
	}

	public static Map<String, String> getPaymentTypes() {
		return paymentTypes;
	}
}
