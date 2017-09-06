package com.rlabs.merkuri.amqp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rlabs.merkuri.commons.Constants;

/**
 * AMQP server endpoint for Asynchronous communication.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public abstract class ServerEndpointAMQP {

	protected Channel channel;
	protected Connection connection;
	protected String queueName;

	public ServerEndpointAMQP(String queueName) throws IOException, TimeoutException {
		this.queueName = queueName;

		final ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Constants.AMQP_HOSTNAME);
		factory.setUsername(Constants.AMQP_USERNAME);
		factory.setPassword(Constants.AMQP_PASSWORD);
		factory.setVirtualHost(Constants.AMQP_USERNAME);

		this.connection = factory.newConnection();
		this.channel = this.connection.createChannel();

		// declare a queue for this channel
		// if not exits, it will created on server
		this.channel.queueDeclare(queueName, false, false, false, null);
	}

	public void close() throws IOException, TimeoutException {
		this.channel.close();
		this.connection.close();
	}

}
