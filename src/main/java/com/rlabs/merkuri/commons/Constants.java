package com.rlabs.merkuri.commons;

/**
 * Constants of the application.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public final class Constants {

	private Constants() {

	}

	/**
	 * Resources
	 */
	public static final String SAMPLE_HTML = "sample-mail.html";
	public static final String SAMPLE_TEXT = "sample-mail.txt";

	/**
	 * Sample Data
	 */
	public static final String SAMPLE_FROM = "ryan@localhost";
	public static final String SAMPLE_TO = "padilha@localhost";
	public static final String SAMPLE_SUBJECT = "Sample Mail Merkuri Sender";
	public static final String SAMPLE_USER = "Ryan Padilha";

	/**
	 * AMQP communication
	 */
	public static final String AMQP_HOSTNAME = "rhino.rmq.cloudamqp.com";
	public static final String AMQP_USERNAME = "tvvmwcrt";
	public static final String AMQP_PASSWORD = "UAAQXvnzAeI0A7o1PrW3TsS9QSOVSK0D";
	public static final String AMQP_QUEUE_DEFAULT = "merkuri-queue";
	public static final int AMQP_TIMEOUT = 30000;
	public static final int AMQP_RECONNECT = 2000;

}
