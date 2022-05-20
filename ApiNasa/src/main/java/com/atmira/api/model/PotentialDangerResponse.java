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
public class PotentialDangerResponse {
	
	private String name;
	private BigDecimal diameter;
	private String velocity;
	private String date;
	private String planet;
}
