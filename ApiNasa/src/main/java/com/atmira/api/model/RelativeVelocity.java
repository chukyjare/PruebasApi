package com.atmira.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelativeVelocity {

	private String kilometersPerSecond;
	private String kilometersPerHour;
	private String milesPerHour;
	
}
