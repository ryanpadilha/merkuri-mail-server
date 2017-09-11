package com.rlabs.merkuri.service;

import com.rlabs.merkuri.entity.model.MailStructure;

/**
 * Mail Sender Service Interface.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public interface MailSenderService {

	void send(MailStructure email);

	MailStructure buildSampleTemplateMail(String filename, boolean isHtml);

	MailStructure buildStructure(String type);
}
