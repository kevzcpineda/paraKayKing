package quest.phil.diagnostic.information.system.ws.model.request.laboratory;

import java.io.Serializable;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QisTransactionLabUltrasoundRequest implements Serializable {

	private static final long serialVersionUID = -4251415563713421877L;

	@Size(max = 10000, message = "Findings must not exceed to {max} characters.")
	private String findings;

	@Size(max = 10000, message = "Impressions must not exceed to {max} characters.")
	private String impressions;

	@NotNull(message = "Radiologist Id is required.")
	@NotEmpty(message = "Radiologist Id must not be empty.")
	@Size(max = 20, message = "Radiologist Id should not exceed {max} characters.")
	private String radiologistId;

	@NotNull(message = "Remarks value is required.")
	private Boolean remarks;
	
	@Size(max = 11, message = "Impressions must not exceed to {max} characters.")
	private String bpd_size = null;
	
	@Size(max = 255, message = "Impressions must not exceed to {max} characters.")
	private String bpd_old = null;
	
	@Size(max = 11, message = "Impressions must not exceed to {max} characters.")
	private String hc_size = null;
	
	@Size(max = 255, message = "Impressions must not exceed to {max} characters.")
	private String hc_old = null;
	
	@Size(max = 11, message = "Impressions must not exceed to {max} characters.")
	private String ac_size = null;
	
	@Size(max = 255, message = "Impressions must not exceed to {max} characters.")
	private String ac_old = null;
	
	@Size(max = 11, message = "Impressions must not exceed to {max} characters.")
	private String fl_size = null;
	
	@Size(max = 255, message = "Impressions must not exceed to {max} characters.")
	private String fl_old = null;
	
	@Size(max = 1000, message = "Impressions must not exceed to {max} characters.")
	private String finding_header_pelvic = null;
	
	@Size(max = 1000, message = "Impressions must not exceed to {max} characters.")
	private String finding_footer_pelvic = null;
	
	public QisTransactionLabUltrasoundRequest() {
		super();
	}

	public String getFindings() {
		return findings;
	}

	public String getImpressions() {
		return impressions;
	}

	public String getRadiologistId() {
		return radiologistId;
	}

	public Boolean getRemarks() {
		return remarks;
	}

	public void setFindings(String findings) {
		this.findings = findings;
	}

	public void setImpressions(String impressions) {
		this.impressions = impressions;
	}

	public void setRadiologistId(String radiologistId) {
		this.radiologistId = radiologistId;
	}

	public void setRemarks(Boolean remarks) {
		this.remarks = remarks;
	}

	public String getBpd_size() {
		return bpd_size;
	}

	public String getBpd_old() {
		return bpd_old;
	}

	public String getHc_size() {
		return hc_size;
	}

	public String getHc_old() {
		return hc_old;
	}

	public String getAc_size() {
		return ac_size;
	}

	public String getAc_old() {
		return ac_old;
	}

	public String getFl_size() {
		return fl_size;
	}

	public String getFl_old() {
		return fl_old;
	}

	public void setBpd_size(String bpd_size) {
		this.bpd_size = bpd_size;
	}

	public void setBpd_old(String bpd_old) {
		this.bpd_old = bpd_old;
	}

	public void setHc_size(String hc_size) {
		this.hc_size = hc_size;
	}

	public void setHc_old(String hc_old) {
		this.hc_old = hc_old;
	}

	public void setAc_size(String ac_size) {
		this.ac_size = ac_size;
	}

	public void setAc_old(String ac_old) {
		this.ac_old = ac_old;
	}

	public void setFl_size(String fl_size) {
		this.fl_size = fl_size;
	}

	public void setFl_old(String fl_old) {
		this.fl_old = fl_old;
	}

	public String getFinding_header_pelvic() {
		return finding_header_pelvic;
	}

	public String getFinding_footer_pelvic() {
		return finding_footer_pelvic;
	}

	public void setFinding_header_pelvic(String finding_header_pelvic) {
		this.finding_header_pelvic = finding_header_pelvic;
	}

	public void setFinding_footer_pelvic(String finding_footer_pelvic) {
		this.finding_footer_pelvic = finding_footer_pelvic;
	}
	
	
}
