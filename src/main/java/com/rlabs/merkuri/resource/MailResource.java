package com.rlabs.merkuri.resource;

import com.rlabs.merkuri.entity.model.MailWrapper;

/**
 * The Mail Resource Interface.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public interface MailResource {

	MailWrapper sendSampleAsyncMail(String type);

	MailWrapper sendSampleSyncMail(String type);

}
