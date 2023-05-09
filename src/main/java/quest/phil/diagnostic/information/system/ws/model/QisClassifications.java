package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisClassifications {
	A("CLASS A - Physically Fit"),
	B("CLASS B - Physically Fit but with minor condition curable within a short period of time, that will not adversely affect the workers efficiency."),
	C("CLASS C - With abnormal findings generally not accepted for employment."), D("CLASS D - UNEMPLOYABLE"), E("Incomplete"), F("Pending with findings"),
	P("PENDING");

	private String classType;

	QisClassifications(String classType) {
		this.classType = classType;
	}

	public String getClassType() {
		return classType;
	}

	private static final Map<String, String> classTypeLists = new HashMap<>();
	static {
		for (QisClassifications ct : QisClassifications.values()) {
			classTypeLists.put(String.valueOf(ct), ct.getClassType());
		}
	}

	public static Map<String, String> getClassTypeLists() {
		return classTypeLists;
	}

}
