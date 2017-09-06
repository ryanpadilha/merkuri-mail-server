package com.rlabs.merkuri.service.impl;

import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.rlabs.merkuri.entity.model.Email;
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
	public void send(Email email) {
		Objects.requireNonNull(email, "Email must not be null.");

		if (email.isHtml()) {
			try {
				buildHtmlMail(email);
			} catch (MessagingException e) {
				LOGGER.error("Could not send email to: {} - error: {}", email.getTo(), e.getMessage());
			}
		} else {
			buildPlainTextMail(email);
		}

	}

	private void buildHtmlMail(Email email) throws MessagingException {
		final MimeMessage message = sender.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setTo(email.getTo().toArray(new String[email.getTo().size()]));
		helper.setReplyTo(email.getFrom());
		helper.setFrom(email.getFrom());
		helper.setSubject(email.getSubject());
		helper.setText(email.getMessage(), true);

		if (!email.getCc().isEmpty()) {
			helper.setCc(email.getCc().toArray(new String[email.getCc().size()]));
		}

		sender.send(message);
	}

	private void buildPlainTextMail(Email email) {
		final SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getTo().toArray(new String[email.getTo().size()]));
		message.setReplyTo(email.getFrom());
		message.setFrom(email.getFrom());
		message.setSubject(email.getSubject());
		message.setText(email.getMessage());

		if (!email.getCc().isEmpty()) {
			message.setCc(email.getCc().toArray(new String[email.getCc().size()]));
		}

		sender.send(message);
	}

}
