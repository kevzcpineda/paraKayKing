package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisMicroOptions {
	NON("NONE"), RAR("RARE"), FEW("FEW"), MOD("MODERATE"), MNY("MANY");

	private String option;

	QisMicroOptions(String option) {
		this.option = option;
	}

	public String getOption() {
		return option;
	}

	private static final Map<String, String> optionList = new HashMap<>();
	static {
		for (QisMicroOptions opt : QisMicroOptions.values()) {
			optionList.put(String.valueOf(opt), opt.getOption());
		}
	}

	public static Map<String, String> getOptionList() {
		return optionList;
	}
}
