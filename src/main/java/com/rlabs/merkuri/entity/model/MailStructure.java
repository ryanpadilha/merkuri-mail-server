package com.rlabs.merkuri.entity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Email Structure Model Java Bean.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public final class MailStructure implements Serializable {

	private static final long serialVersionUID = -2854635895650514709L;

	private String from;
	private List<String> to = new ArrayList<>();
	private List<String> cc = new ArrayList<>();
	private String subject;
	private String message;
	private boolean isHtml;

	public MailStructure() {

	}

	public MailStructure(String from, String toList, String subject, String message, boolean isHtml) {
		this.from = from;
		this.to.addAll(Arrays.asList(toList.split(",")));
		this.subject = subject;
		this.message = message;
		this.isHtml = isHtml;
	}

	public MailStructure(String from, String toList, String ccList, String subject, String message, boolean isHtml) {
		this(from, toList, subject, message, isHtml);
		this.cc.addAll(Arrays.asList(ccList.split(",")));
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isHtml() {
		return isHtml;
	}

	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	@Override
	public String toString() {
		return "MailStructure [from=" + from + ", to=" + to + ", cc=" + cc + ", subject=" + subject + ", message="
				+ message + ", isHtml=" + isHtml + "]";
	}

}
