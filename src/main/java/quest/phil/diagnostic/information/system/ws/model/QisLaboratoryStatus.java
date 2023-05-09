package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisLaboratoryStatus {
	P("PENDING"), OG("ON GOING"), C("COMPLATED");

	private String labStat;

	QisLaboratoryStatus(String labStat) {
		this.labStat = labStat;
	}

	public String getLabStat() {
		return labStat;
	}

	private static final Map<String, String> labStats = new HashMap<>();
	static {
		for (QisLaboratoryStatus pro : QisLaboratoryStatus.values()) {
			labStats.put(String.valueOf(pro), pro.getLabStat());
		}
	}

	public static Map<String, String> getLabStats() {
		return labStats;
	}

}
