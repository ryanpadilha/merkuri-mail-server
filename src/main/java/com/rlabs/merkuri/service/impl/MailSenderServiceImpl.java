package com.rlabs.merkuri.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.rlabs.merkuri.commons.Constants;
import com.rlabs.merkuri.entity.model.MailStructure;
import com.rlabs.merkuri.entity.model.MailTemplate;
import com.rlabs.merkuri.service.MailSenderService;

/**
 * Mail Sender Service Implementation.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
@Service
public class MailSenderServiceImpl implements MailSenderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailSenderServiceImpl.class);

	@Autowired
	private JavaMailSender sender;

	@Override
	public void send(MailStructure email) {
		Objects.requireNonNull(email, "Email must not be null.");

		if (email.isHtml()) {
			sendHtmlMail(email);
		} else {
			sendPlainTextMail(email);
		}
	}

	/**
	 * Prepare and create a sample mail example for demonstration.<br>
	 * We can use <i>GreenMail</i> Sandbox to JUnit Tests purposes.
	 *
	 * @param filename
	 * @param isHtml
	 * @return
	 */
	@Override
	public MailStructure buildSampleTemplateMail(final String filename, boolean isHtml) {
		final String subject = Constants.SAMPLE_SUBJECT + (isHtml ? " | HTML" : " | TEXT");

		final MailTemplate template = new MailTemplate(filename);
		final Map<String, String> replacements = new HashMap<>();
		replacements.put("user", Constants.SAMPLE_USER);
		replacements.put("today", String.valueOf(new Date()));

		final String message = template.getTemplate(replacements);
		final MailStructure email = new MailStructure(Constants.SAMPLE_FROM, Constants.SAMPLE_TO, subject, message,
				isHtml);
		return email;
	}

	/**
	 * Prepare and create a HTML (complex) message to be send.
	 *
	 * @param email
	 * @throws MessagingException
	 */
	private void sendHtmlMail(final MailStructure email) {
		// FIXME instance is manually for rabbitmq-server
		if (null == sender) {
			sender = new JavaMailSenderImpl();
		}

		final MimeMessage message = sender.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message);

		// TODO for all address, use the object bellow
		// fields: to, from
		// new InternetAddress(address, personal);

		try {
			helper.setTo(email.getTo().toArray(new String[email.getTo().size()]));
			helper.setReplyTo(email.getFrom());
			helper.setFrom(email.getFrom());
			helper.setSubject(email.getSubject());
			helper.setText(email.getMessage(), true);

			if (!email.getCc().isEmpty()) {
				helper.setCc(email.getCc().toArray(new String[email.getCc().size()]));
			}
		} catch (MessagingException e) {
			LOGGER.error("[html] Could not build complex message: {} - error: {}", email, e.getMessage());
		}

		try {
			LOGGER.info("[html] sending mail message...");
			sender.send(message);
		} catch (MailException e) {
			// logging the runtine exception for audit
			LOGGER.error("[html] Error while sending email to: {} - error: {}", email.getTo(), e.getMessage());
		}
	}

	/**
	 * Prepare and create a simple text message to be send.
	 *
	 * @param email
	 */
	private void sendPlainTextMail(final MailStructure email) {
		final SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getTo().toArray(new String[email.getTo().size()]));
		message.setReplyTo(email.getFrom());
		message.setFrom(email.getFrom());
		message.setSubject(email.getSubject());
		message.setText(email.getMessage());

		if (!email.getCc().isEmpty()) {
			message.setCc(email.getCc().toArray(new String[email.getCc().size()]));
		}

		try {
			LOGGER.info("[text] sending mail message...");
			sender.send(message);
		} catch (MailException e) {
			// logging the runtine exception for audit
			LOGGER.error("[text] Error while sending email to: {} - error: {}", email.getTo(), e.getMessage());
		}
	}

}
