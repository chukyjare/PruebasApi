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
 * Instantiates a new diameter.
 *
 * @param estimatedDiameterMin the estimated diameter min
 * @param estimatedDiameterMax the estimated diameter max
 */
@AllArgsConstructor

/**
 * Instantiates a new diameter.
 */
@NoArgsConstructor
public class Diameter {

	/** The estimated diameter min. */
	private BigDecimal estimatedDiameterMin;

	/** The estimated diameter max. */
	private BigDecimal estimatedDiameterMax;
}
