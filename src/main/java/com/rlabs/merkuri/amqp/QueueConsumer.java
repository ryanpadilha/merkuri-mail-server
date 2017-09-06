package com.rlabs.merkuri.amqp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rlabs.merkuri.entity.model.Email;
import com.rlabs.merkuri.service.MailSenderService;
import com.rlabs.merkuri.service.impl.MailSenderServiceImpl;

/**
 * AMQP endpoint that consumes messages off to the queue.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public class QueueConsumer extends ServerEndpointAMQP implements Runnable, Consumer {

	public QueueConsumer(String queueName) throws IOException, TimeoutException {
		super(queueName);
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
		System.out.println(String.format("Consumer registered %s", consumerTag));
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
		Email email = null;
		Object object = SerializationUtils.deserialize(body);
		Class<? extends Object> clazz = object.getClass();

		if (clazz.isInstance(object)) {
			email = (Email) object;
		}

		final MailSenderService mailSenderService = new MailSenderServiceImpl();
		mailSenderService.send(email);

		System.out.println(String.format("Processing message email %s", email));
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

	}

	@Override
	public void handleRecoverOk(String consumerTag) {

	}

	@Override
	public void run() {
		try {
			// start consuming messages. Auto acknowledge messages.
			this.channel.basicConsume(queueName, true, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
