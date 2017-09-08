package com.rlabs.merkuri.entity.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Mail Wrapper used on REST API integration.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public class MailWrapper {

	private static final String SUCCESS = "success";
	private static final String ERROR = "error";

	private String requestType;
	private String status;
	private String errorMessage = StringUtils.EMPTY;

	public MailWrapper() {

	}

	public MailWrapper success() {
		this.status = SUCCESS;
		return this;
	}

	public MailWrapper error(String message) {
		this.status = ERROR;
		this.errorMessage = message;
		return this;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
