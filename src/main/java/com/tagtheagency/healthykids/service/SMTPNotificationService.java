package com.tagtheagency.healthykids.service;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SMTPNotificationService implements NotificationService {

	@Autowired
    private JavaMailSender javaMailSender;

	@Override
	public void sendEmail(String to, String from, String subject, String bodyText, String bodyHtml) throws IOException {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setReplyTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(bodyText);
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {}
        javaMailSender.send(mail);
        //return helper;
		
	}

	
}
