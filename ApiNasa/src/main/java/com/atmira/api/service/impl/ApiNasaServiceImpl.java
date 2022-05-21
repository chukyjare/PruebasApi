package com.atmira.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.atmira.api.model.ApiNasaResponse;
import com.atmira.api.service.ApiNasaService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiNasaServiceImpl implements ApiNasaService {
	
	private WebClient webClient;

	public ApiNasaServiceImpl(WebClient.Builder webClientBuilder) {
		super();
		this.webClient = webClientBuilder.build();
	}

	@Override
	public ApiNasaResponse callApiNasa(byte days) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
