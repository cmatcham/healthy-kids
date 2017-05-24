package com.tagtheagency.healthykids.service;

import java.io.IOException;

public interface NotificationService {

	public void sendEmail(String to, String from, String subject, String bodyText, String bodyHtml) throws IOException;
}
