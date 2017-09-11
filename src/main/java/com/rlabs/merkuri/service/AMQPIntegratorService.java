package com.rlabs.merkuri.service;

import com.rlabs.merkuri.entity.model.MailStructure;

/**
 * AMQP Integrator Service Interface.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public interface AMQPIntegratorService {

	void queueMessage(MailStructure email);
}
