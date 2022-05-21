package com.atmira.api.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Diameter {

	private BigDecimal estimatedDiameterMin;
	private BigDecimal estimatedDiameterMax;
}
