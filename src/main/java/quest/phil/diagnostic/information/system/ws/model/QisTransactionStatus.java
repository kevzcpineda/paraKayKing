package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisTransactionStatus {
	SPD("PROCESSED"), SHO("HOLD"), SRE("REFUND"), SCA("CANCELLED");

	private String transactionStatus;

	QisTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	private static final Map<String, String> transactionStausList = new HashMap<>();
	static {
		for (QisTransactionStatus tsts : QisTransactionStatus.values()) {
			transactionStausList.put(String.valueOf(tsts), tsts.getTransactionStatus());
		}
	}

	public static Map<String, String> getTransactionStatusList() {
		return transactionStausList;
	}

}
