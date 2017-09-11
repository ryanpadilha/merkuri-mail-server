package com.rlabs.merkuri.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rlabs.merkuri.amqp.QueueConsumer;
import com.rlabs.merkuri.amqp.QueueProducer;
import com.rlabs.merkuri.commons.Constants;
import com.rlabs.merkuri.entity.model.MailStructure;
import com.rlabs.merkuri.service.AMQPIntegratorService;

/**
 * AMQP Integrator Service Implementation.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
@Service
public class AMQPIntegratorServiceImpl implements AMQPIntegratorService {

	@Autowired
	private QueueProducer producer;

	@Autowired
	private QueueConsumer consumer;

	private static boolean start = false;

	public void queueMessage(final MailStructure email) {
		if (!start) {
			startConsumer();
		}

		producer.setQueueName(Constants.AMQP_QUEUE_DEFAULT);
		producer.send(email);
	}

	private void startConsumer() {
		consumer.setQueueName(Constants.AMQP_QUEUE_DEFAULT);
		final Thread consumerThread = new Thread(consumer);
		consumerThread.start();
		start = true;
	}
}
