package com.atmira.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstimatedDiameter {

	private Diameter kilometers;
	private Diameter meters;
	private Diameter miles;
	private Diameter feet;
	
	
}
