package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisPackageType {
	CDNA("CASH DNA"), CLAB("CASH LABORATORY"), CIND("CASH INDUSTRIAL"), CIMG("CASH IMAGING"), CPHA("CASH PHARMACY"),
	CPSY("CASH PSYCH"), CSUR("CASH SURGEON"), CWLM("CASH WLMC"), AHMO("ACCOUNT HMO"), AIND("ACCOUNT INDUSTRIAL"),
	APHA("ACCOUNT PHARMACY"), ASUR("ACCOUNT SURGEON"), APSY("ACCOUNT PSYCH");

	private String packageType;

	QisPackageType(String packageType) {
		this.packageType = packageType;
	}

	public String getPacakgeType() {
		return packageType;
	}

	private static final Map<String, String> packageTypes = new HashMap<>();
	static {
		for (QisPackageType ptyp : QisPackageType.values()) {
			packageTypes.put(String.valueOf(ptyp), ptyp.getPacakgeType());
		}
	}

	public static Map<String, String> getPackageTypes() {
		return packageTypes;
	}
}
