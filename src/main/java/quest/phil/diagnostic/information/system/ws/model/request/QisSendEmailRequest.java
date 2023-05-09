package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

public class QisSendEmailRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5120786226369738845L;
	
	@NotEmpty(message = "Send To is required.")
	private String sendTo;
	
	
	private String sendCc;
	
	@NotEmpty(message = "Subject is required.")
	private String emailSubject;
	
	@NotEmpty(message = "Email content is required.")
	private String emailBody;
	
	
	private List<MultipartFile> fileInput;
	
	public QisSendEmailRequest() {
		super();	
	}

	
	public String getSendTo() {
		return sendTo;
	}
	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}
	public String getSendCc() {
		return sendCc;
	}
	public void setSendCc(String sendCc) {
		this.sendCc = sendCc;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public String getEmailBody() {
		return emailBody;
	}
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}
	public List<MultipartFile> getFileInput() {
		return fileInput;
	}
	public void setFileInput(List<MultipartFile> fileInput) {
		this.fileInput = fileInput;
	}

	
	
	
}
