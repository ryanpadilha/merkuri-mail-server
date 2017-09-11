package com.rlabs.merkuri.resource.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rlabs.merkuri.entity.model.MailStructure;
import com.rlabs.merkuri.entity.model.MailWrapper;
import com.rlabs.merkuri.resource.MailResource;
import com.rlabs.merkuri.service.AMQPIntegratorService;
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

	@Autowired
	private AMQPIntegratorService integratorService;

	@RequestMapping(value = "/sample/asyncsend/{type}", method = RequestMethod.POST)
	@Override
	public MailWrapper sendSampleAsyncMail(@PathVariable(name = "type", required = true) String type) {
		final MailStructure sampleMail = service.buildStructure(type);

		if (null != sampleMail) {
			integratorService.queueMessage(sampleMail);
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
		final MailStructure sampleMail = service.buildStructure(type);
		service.send(sampleMail);

		final MailWrapper mailWrapper = new MailWrapper();
		mailWrapper.setRequestType("sync");
		// TODO implement other fields

		return mailWrapper;
	}

}
