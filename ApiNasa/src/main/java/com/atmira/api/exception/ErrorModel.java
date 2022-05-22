package com.atmira.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Gets
 */
@Getter

/**
 * Sets
 */
@Setter

/**
 * Instantiates a new error model.
 *
 * @param nameApp     the name app
 * @param code        the code
 * @param message     the message
 * @param description the description
 */
@AllArgsConstructor

/**
 * Instantiates a new error model.
 */
@NoArgsConstructor
public class ErrorModel {

	/** The name app. */
	private String nameApp;

	/** The code. */
	private int code;

	/** The message. */
	private String message;

	/** The description. */
	private String description;
}
