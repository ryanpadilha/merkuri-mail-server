package com.rlabs.merkuri.amqp;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.SerializationUtils;

/**
 * AMQP producer that writes to the queue.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public class QueueProducer extends ServerEndpointAMQP {

	public QueueProducer(String queueName) throws IOException, TimeoutException {
		super(queueName);
	}

	public void sendMessage(Serializable object) throws IOException {
		this.channel.basicPublish("", queueName, null, SerializationUtils.serialize(object));
	}

}
