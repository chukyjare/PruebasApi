package com.atmira.api.model;

import java.math.BigDecimal;

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
 * Instantiates a new potential danger response.
 *
 * @param name     the name
 * @param diameter the diameter
 * @param velocity the velocity
 * @param date     the date
 * @param planet   the planet
 */
@AllArgsConstructor

/**
 * Instantiates a new potential danger response.
 */
@NoArgsConstructor
public class PotentialDangerResponse {

	/** The name. */
	private String name;

	/** The diameter. */
	private BigDecimal diameter;

	/** The velocity. */
	private String velocity;

	/** The date. */
	private String date;

	/** The planet. */
	private String planet;
}
