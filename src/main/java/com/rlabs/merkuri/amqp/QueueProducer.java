package com.rlabs.merkuri.amqp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rlabs.merkuri.entity.model.MailStructure;

/**
 * AMQP producer that writes to the queue.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public class QueueProducer extends AMQPServerEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueProducer.class);

	public QueueProducer(String queueName) {
		super(queueName);
	}

	public void send(final MailStructure message) {
		try {
			open();
			sendMessage(new ObjectMapper().writeValueAsBytes(message));
		} catch (IOException | TimeoutException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			// close();
		}
	}
}
