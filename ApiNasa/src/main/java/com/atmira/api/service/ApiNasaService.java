package com.atmira.api.service;

import java.util.List;

import com.atmira.api.exception.ForecasterException;
import com.atmira.api.model.PotentialDangerResponse;

/**
 * The Interface ApiNasaService.
 */
public interface ApiNasaService {

	/**
	 * Call api nasa.
	 *
	 * @param days the days
	 * @return the list
	 * @throws ForecasterException the forecaster exception
	 */
	public List<PotentialDangerResponse> callApiNasa(byte days) throws ForecasterException;
}
