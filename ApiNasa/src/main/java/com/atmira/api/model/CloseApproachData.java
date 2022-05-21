package com.atmira.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CloseApproachData {

	private String closeApproachDate;
	private String closeApproachDateFull;
	private Long epochDateCloseApproach;
	private RelativeVelocity relativeVelocity;
	private MissDistance missDistance;
	private String orbitingBody;
}
