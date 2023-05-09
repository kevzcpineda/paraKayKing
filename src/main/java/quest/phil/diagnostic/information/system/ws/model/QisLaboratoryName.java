package quest.phil.diagnostic.information.system.ws.model;

import java.util.HashMap;
import java.util.Map;

public enum QisLaboratoryName {
	XR("XRAY"), BL("BLOOD"), UR("URINE"), ST("STOOL"), SP("SPUTUM"), PE("PHYSICAL EXAM"), OS("OTHER SPECIMEN"),
	US("ULTRASOUND"), ECG("ECG"), E2D("2D ECHO"), OTH("OTHERS"), NO("NONE"), FL("FLUIDS"), SL("SALIVA"),
	ORO("OROPHARYNGEAL SWAB"), NAS("NASOPHARYNGEAL SWAB");

	private String laboratory;

	QisLaboratoryName(String laboratory) {
		this.laboratory = laboratory;
	}

	public String getLaboratory() {
		return laboratory;
	}

	private static final Map<String, String> laboratories = new HashMap<>();
	static {
		for (QisLaboratoryName lab : QisLaboratoryName.values()) {
			laboratories.put(String.valueOf(lab), lab.getLaboratory());
		}
	}

	public static Map<String, String> getLaboratories() {
		return laboratories;
	}
}
