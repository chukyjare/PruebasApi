package com.atmira.api.service;

import com.atmira.api.model.ApiNasaResponse;

public interface ApiNasaService {

	public ApiNasaResponse callApiNasa (byte days);
}
