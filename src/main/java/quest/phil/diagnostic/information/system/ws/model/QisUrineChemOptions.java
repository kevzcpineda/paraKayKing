package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisUrineChemOptions {
	NEG("NEGATIVE"), TRA("TRACE"), P1("1+"), P2("2+"), P3("3+"), P4("4+");

	private String option;

	QisUrineChemOptions(String option) {
		this.option = option;
	}

	public String getOption() {
		return option;
	}

	private static final Map<String, String> optionList = new HashMap<>();
	static {
		for (QisUrineChemOptions cat : QisUrineChemOptions.values()) {
			optionList.put(String.valueOf(cat), cat.getOption());
		}
	}

	public static Map<String, String> getOptionList() {
		return optionList;
	}
}
