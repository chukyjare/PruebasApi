package com.atmira.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ForecasterException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2419626208122308372L;
 
	private String code;
	private String message;
	private String description;
}
