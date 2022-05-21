package com.atmira.api.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Asteroid {
	
	private Link links;
	private String id;
	private String neoReferenceId;
	private String name;
	private String nasaJplUrl;
	private BigDecimal absoluteMagnitudeH;
	private EstimatedDiameter estimatedDiameter;
	private boolean isPotentiallyHazardousAsteroid;
	private List<CloseApproachData> closeApproachData;
	private boolean isEntryObject;
}
