package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisLaboratoryProcedure {
	CM("CLINICAL MICROSCOPY"), HE("HEMATOLOGY"), CH("CHEMISTRY"), SE("SEROLOGY"), TO("TOXICOLOGY"), MB("MICROBIOLOGY"), BT("BACTERIOLOGY");

	private String procedure;

	QisLaboratoryProcedure(String procedure) {
		this.procedure = procedure;
	}

	public String getProcedure() {
		return procedure;
	}

	private static final Map<String, String> procedures = new HashMap<>();
	static {
		for (QisLaboratoryProcedure pro : QisLaboratoryProcedure.values()) {
			procedures.put(String.valueOf(pro), pro.getProcedure());
		}
	}

	public static Map<String, String> getProcedures() {
		return procedures;
	}

}
