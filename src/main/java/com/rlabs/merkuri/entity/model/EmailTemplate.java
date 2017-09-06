package com.rlabs.merkuri.entity.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Email Template Java Bean.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public class EmailTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTemplate.class);

	private String filename;
	private String content;
	private Map<String, String> replacementParams;

	public EmailTemplate(String filename) {
		this.filename = filename;
		this.content = loadTemplate(filename);
	}

	public String getTemplate(final Map<String, String> replacements) {
		String templateView = this.content;

		if (StringUtils.isNotBlank(templateView)) {
			for (Entry<String, String> entry : replacements.entrySet()) {
				templateView = templateView.replace("{{" + entry.getKey() + "}}", entry.getValue());
			}
		}

		return templateView;
	}

	private String loadTemplate(final String filename) {
		String content = StringUtils.EMPTY;

		try {
			final URL resource = this.getClass().getClassLoader().getResource("templates/" + filename);
			if (null == resource) {
				throw new IOException("Template filename provided not found on resource folder.");
			}

			final String file = resource.getFile();
			content = new String(Files.readAllBytes(new File(file).toPath()));
		} catch (IOException e) {
			LOGGER.error("Could not read template with filename {}", filename);
		}

		return content;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getReplacementParams() {
		return replacementParams;
	}

	public void setReplacementParams(Map<String, String> replacementParams) {
		this.replacementParams = replacementParams;
	}

}
