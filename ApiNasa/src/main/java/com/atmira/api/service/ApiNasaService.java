package com.atmira.api.service;

import java.util.List;

import com.atmira.api.exception.ForecasterException;
import com.atmira.api.model.PotentialDangerResponse;

public interface ApiNasaService {

	public List<PotentialDangerResponse> callApiNasa (byte days) throws ForecasterException;
}
