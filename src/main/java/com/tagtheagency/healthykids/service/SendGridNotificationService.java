package com.tagtheagency.healthykids.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.sendgrid.*;

//@Service
public class SendGridNotificationService implements NotificationService {

	@Override
	public void sendEmail(String to, String from, String subject, String bodyText, String bodyHtml) throws IOException {
		Email fromAddress = new Email(from);
		Email toAddress = new Email(to);
		Content content = new Content("text/plain", bodyText);
		Mail mail = new Mail(fromAddress, subject, toAddress, content);

		SendGrid sg = new SendGrid(System.getenv("TODO API KEY"));
		Request request = new Request();

		request.setMethod(Method.POST);
		request.setEndpoint("mail/send");
		request.setBody(mail.build());
		Response response = sg.api(request);

	}
	
}
