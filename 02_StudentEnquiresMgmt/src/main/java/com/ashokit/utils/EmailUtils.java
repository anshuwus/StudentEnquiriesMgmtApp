package com.ashokit.utils;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {
	@Autowired
	private JavaMailSender mailSender;
	
	public boolean sendEmail(String to,String subject,StringBuffer body) {
		boolean isSent=false;
		try {
			MimeMessage mimeMsg=mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(mimeMsg);
			helper.setFrom("riyavermawus2@gmail.com");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body.toString(),true);
			mailSender.send(mimeMsg);
			isSent=true;
		}
		catch(Exception e) {
			isSent=false;
			e.printStackTrace();
		}
		return isSent;
	}
}
