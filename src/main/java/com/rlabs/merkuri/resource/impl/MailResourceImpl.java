package com.rlabs.merkuri.resource.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rlabs.merkuri.amqp.QueueConsumer;
import com.rlabs.merkuri.amqp.QueueProducer;
import com.rlabs.merkuri.commons.Constants;
import com.rlabs.merkuri.entity.model.MailStructure;
import com.rlabs.merkuri.entity.model.MailWrapper;
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
	@Override
	public MailWrapper sendSampleAsyncMail(@PathVariable(name = "type", required = true) String type) {

		// TODO refactoring it!
		final QueueConsumer consumer = new QueueConsumer(Constants.AMQP_QUEUE_DEFAULT);
		final Thread consumerThread = new Thread(consumer);
		consumerThread.start();

		final MailStructure sampleMail = buildStructure(type);

		if (null != sampleMail) {
			final QueueProducer producer = new QueueProducer(Constants.AMQP_QUEUE_DEFAULT);
			producer.send(sampleMail);
		}

		final MailWrapper mailWrapper = new MailWrapper();
		mailWrapper.setRequestType("async");
		// TODO implement other fields

		return mailWrapper;
	}

	/**
	 * Synchronous sample email sender.
	 *
	 * @param type
	 */
	@RequestMapping(value = "/sample/syncsend/{type}", method = RequestMethod.POST)
	@Override
	public MailWrapper sendSampleSyncMail(@PathVariable(name = "type", required = true) String type) {
		service.send(buildStructure(type));

		final MailWrapper mailWrapper = new MailWrapper();
		mailWrapper.setRequestType("sync");
		// TODO implement other fields

		return mailWrapper;
	}

	private MailStructure buildStructure(final String type) {
		MailStructure sampleMail = null;

		if ("html".equalsIgnoreCase(type)) {
			sampleMail = service.buildSampleTemplateMail(Constants.SAMPLE_HTML, true);
		} else {
			sampleMail = service.buildSampleTemplateMail(Constants.SAMPLE_TEXT, false);
		}

		return sampleMail;
	}

}
