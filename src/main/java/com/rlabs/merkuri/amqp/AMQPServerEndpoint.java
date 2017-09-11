package com.rlabs.merkuri.amqp;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
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
public abstract class AMQPServerEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(AMQPServerEndpoint.class);

	private final ConnectionFactory factory;
	private Connection connection;
	private Channel channel;

	private String queueName;
	private static int tCounter = 0;
	private ExecutorService executor = Executors.newFixedThreadPool(2);

	public AMQPServerEndpoint() {
		final ConnectionFactory factory = new ConnectionFactory();
		factory.setAutomaticRecoveryEnabled(true);
		factory.setTopologyRecoveryEnabled(true);
		factory.setConnectionTimeout(Constants.AMQP_TIMEOUT);
		factory.setNetworkRecoveryInterval(Constants.AMQP_RECONNECT);

		factory.setHost(Constants.AMQP_HOSTNAME);
		factory.setVirtualHost(Constants.AMQP_USERNAME);
		factory.setUsername(Constants.AMQP_USERNAME);
		factory.setPassword(Constants.AMQP_PASSWORD);
		factory.setThreadFactory(res -> {
			final Thread thread = new Thread(res, "AMQPServerEndpoint-conn-thread-" + tCounter++);
			thread.setDaemon(true);
			return thread;
		});

		this.factory = factory;
	}

	public AMQPServerEndpoint(String queueName) {
		this();
		this.queueName = queueName;
	}

	public void open() throws IOException, TimeoutException {
		this.connection = prepareConnection();
		this.channel = createChannel();
	}

	private void close() {
		try {
			LOGGER.info("Closing the AMQPServerEndpoint Channel...");
			if (null != this.channel && this.channel.isOpen()) {
				this.channel.close();
			}

			LOGGER.info("Closing the AMQPServerEndpoint Connection...");
			if (null != this.connection && this.connection.isOpen()) {
				this.connection.close();
				this.executor.awaitTermination(1, TimeUnit.SECONDS);
				this.executor.shutdownNow();
			}
		} catch (IOException | TimeoutException | InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private Connection prepareConnection() throws IOException, TimeoutException {
		return this.factory.newConnection(this.executor);
	}

	/**
	 * declare a queue for this channel, if not exits, it will created on server
	 *
	 * @return
	 * @throws IOException
	 */
	private Channel createChannel() throws IOException {
		final Channel channelQ = this.connection.createChannel();
		channelQ.queueDeclare(queueName, false, false, false, null);
		return channelQ;
	}

	public void sendMessage(final byte[] message) throws IOException {
		final String exchange = StringUtils.EMPTY;
		final String routingKey = this.queueName;
		final BasicProperties properties = new BasicProperties();

		try {
			this.channel.basicPublish(exchange, routingKey, properties, message);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	public Channel getChannel() {
		return channel;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getQueueName() {
		return queueName;
	}

}
