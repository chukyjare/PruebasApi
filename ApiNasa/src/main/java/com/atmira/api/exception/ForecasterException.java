package com.atmira.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Gets
 */
@Getter

/**
 * Instantiates a new forecaster exception.
 *
 * @param code        the code
 * @param message     the message
 * @param description the description
 */
@AllArgsConstructor
public class ForecasterException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2419626208122308372L;

	/** The code. */
	private String code;

	/** The message. */
	private String message;

	/** The description. */
	private String description;
}
