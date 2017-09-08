package com.rlabs.merkuri.amqp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rlabs.merkuri.entity.model.MailStructure;
import com.rlabs.merkuri.service.MailSenderService;
import com.rlabs.merkuri.service.impl.MailSenderServiceImpl;

/**
 * AMQP endpoint that consumes messages off to the queue.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public class QueueConsumer extends AMQPServerEndpoint implements Runnable, Consumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueConsumer.class);

	public QueueConsumer(String queueName) {
		super(queueName);
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
		LOGGER.info(String.format("Consumer registered %s", consumerTag));
	}

	@Override
	public void handleCancelOk(String consumerTag) {

	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {

	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties props, byte[] body)
			throws IOException {
		final ObjectReader reader = new ObjectMapper().readerFor(MailStructure.class);
		final MailStructure mailMessage = (MailStructure) reader.readValue(body);

		final MailSenderService mailSenderService = new MailSenderServiceImpl();
		mailSenderService.send(mailMessage);

		LOGGER.info(String.format("Processing message mail %s", mailMessage));
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

	}

	@Override
	public void handleRecoverOk(String consumerTag) {

	}

	/**
	 * start consuming messages. Auto acknowledge messages.
	 */
	@Override
	public void run() {
		try {
			open();
			getChannel().basicConsume(getQueueName(), true, this);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			// close();
		}
	}

}
