package com.atmira.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiNasaResponse {
	
	private Link links;
	private Integer elementCount;
	private NearEarthObjects nearEarthObjects;
}
