package quest.phil.diagnostic.information.system.ws.common;

import java.util.List;
import java.util.Properties;


import javax.mail.MessagingException;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import quest.phil.diagnostic.information.system.ws.model.QisEmailConfig;

@Component
public class AppEmailUtility {

	public void sendEmail(QisEmailConfig emailConfig, String sendTo, String subjectEmail, String sendCc,
			String bodyEmail, List<MultipartFile> fileInput) throws MessagingException {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(emailConfig.getHost());
		mailSender.setPort(emailConfig.getPort());
		mailSender.setUsername(emailConfig.getUsername());
		mailSender.setPassword(emailConfig.getPassword());

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", emailConfig.getTransportProtocol());
		props.put("mail.smtp.auth", emailConfig.getSmtpAuth());
		props.put("mail.smtp.starttls.enable", emailConfig.getStarttls());
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(emailConfig.getUsername());
		helper.setTo(sendTo.split(";"));
		if (sendCc != null && !sendCc.equals("")) {
			helper.setCc(sendCc.split(";"));
		}
		helper.setSubject(subjectEmail);
		helper.setText(bodyEmail);
		int i; 
		for(i = 0; i < fileInput.size(); i++) { 
			String fileName =
		    fileInput.get(i).getOriginalFilename(); 
			helper.addAttachment(fileName, fileInput.get(i)); 
		}
			 

		mailSender.send(message);
	}
	
	public void sendEmailSoa(QisEmailConfig emailConfig, String sendTo, String subjectEmail, String sendCc,
			String bodyEmail, List<MultipartFile> fileInput) throws MessagingException {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(emailConfig.getHost());
		mailSender.setPort(emailConfig.getPort());
		mailSender.setUsername(emailConfig.getUsernameSoa());
		mailSender.setPassword(emailConfig.getPasswordSoa());

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", emailConfig.getTransportProtocol());
		props.put("mail.smtp.auth", emailConfig.getSmtpAuth());
		props.put("mail.smtp.starttls.enable", emailConfig.getStarttls());
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(emailConfig.getUsername());
		helper.setTo(sendTo.split(";"));
		if (sendCc != null && !sendCc.equals("")) {
			helper.setCc(sendCc.split(";"));
		}
		helper.setSubject(subjectEmail);
		helper.setText(bodyEmail);
		int i; 
		for(i = 0; i < fileInput.size(); i++) { 
			String fileName =
		    fileInput.get(i).getOriginalFilename(); 
			helper.addAttachment(fileName, fileInput.get(i)); 
		}
			 

		mailSender.send(message);
	}


}
