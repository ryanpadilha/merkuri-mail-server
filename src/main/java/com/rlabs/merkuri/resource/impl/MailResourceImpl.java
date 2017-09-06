package com.rlabs.merkuri.resource.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rlabs.merkuri.entity.model.Email;
import com.rlabs.merkuri.entity.model.EmailTemplate;
import com.rlabs.merkuri.resource.MailResource;
import com.rlabs.merkuri.service.MailSenderService;

/**
 * The Mail Resource REST API.<br>
 * API deifinition documented by Swagger 2.0. <br>
 * <br>
 * Methods of sampleMail are using FakeSMTP to emulate SMTP server locally.<br>
 * More details on {@link http://nilhcem.com/FakeSMTP/index.html}
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
@RestController
@RequestMapping(value = "/api/v1/email")
public class MailResourceImpl implements MailResource {

	private static String SAMPLE_HTML = "sample-mail.html";
	private static String SAMPLE_TEXT = "sample-mail.txt";

	@Autowired
	private MailSenderService service;

	@RequestMapping(value = "/sample/send/html", method = RequestMethod.GET)
	public void sampleHtmlMail() {
		sampleMail(SAMPLE_HTML, true);
	}

	@RequestMapping(value = "/sample/send/text", method = RequestMethod.GET)
	public void sampleTextMail() {
		sampleMail(SAMPLE_TEXT, false);
	}

	private void sampleMail(String filename, boolean isHtml) {
		String from = "ryan@localhost";
		String to = "padilha@localhost";
		String subject = "Sample Mail Sender Service" + (isHtml ? " | HTML" : " | TEXT");

		final EmailTemplate template = new EmailTemplate(filename);
		final Map<String, String> replacements = new HashMap<>();
		replacements.put("user", "Ryan Padilha");
		replacements.put("today", String.valueOf(new Date()));

		final String message = template.getTemplate(replacements);
		final Email email = new Email(from, to, subject, message, isHtml);
		service.send(email);
	}
}
