package com.rlabs.merkuri.resource.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rlabs.merkuri.amqp.QueueConsumer;
import com.rlabs.merkuri.amqp.QueueProducer;
import com.rlabs.merkuri.commons.Constants;
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

	@Autowired
	private MailSenderService service;

	@RequestMapping(value = "/sample/asyncsend/{type}", method = RequestMethod.POST)
	public void sendSampleAsyncMail(@PathVariable(name = "type", required = true) String type) {

		// TODO refactoring it!
		try {
			final QueueConsumer consumer = new QueueConsumer(Constants.AMQP_QUEUE_DEFAULT);
			final Thread consumerThread = new Thread(consumer);
			consumerThread.start();

			Email sampleMail = null;
			if ("html".equalsIgnoreCase(type)) {
				sampleMail = sampleMail(Constants.SAMPLE_HTML, true);
			} else {
				sampleMail = sampleMail(Constants.SAMPLE_TEXT, false);
			}

			if (null != sampleMail) {
				final QueueProducer producer = new QueueProducer(Constants.AMQP_QUEUE_DEFAULT);
				producer.sendMessage(sampleMail);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Synchronous sample email sender.
	 *
	 * @param type
	 */
	@RequestMapping(value = "/sample/syncsend/{type}", method = RequestMethod.POST)
	public void sendSampleSyncMail(@PathVariable(name = "type", required = true) String type) {
		Email sampleMail = null;

		if ("html".equalsIgnoreCase(type)) {
			sampleMail = sampleMail(Constants.SAMPLE_HTML, true);
		} else {
			sampleMail = sampleMail(Constants.SAMPLE_TEXT, false);
		}

		if (null != sampleMail) {
			sendEmail(sampleMail);
		}
	}

	private Email sampleMail(String filename, boolean isHtml) {
		final String subject = Constants.SAMPLE_SUBJECT + (isHtml ? " | HTML" : " | TEXT");

		final EmailTemplate template = new EmailTemplate(filename);
		final Map<String, String> replacements = new HashMap<>();
		replacements.put("user", Constants.SAMPLE_USER);
		replacements.put("today", String.valueOf(new Date()));

		final String message = template.getTemplate(replacements);
		final Email email = new Email(Constants.SAMPLE_FROM, Constants.SAMPLE_TO, subject, message, isHtml);
		return email;
	}

	private void sendEmail(final Email email) {
		service.send(email);
	}
}
