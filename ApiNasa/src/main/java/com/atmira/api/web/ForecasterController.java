package com.atmira.api.web;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.atmira.api.constants.UrlConstants;
import com.atmira.api.exception.ErrorModel;
import com.atmira.api.exception.ForecasterException;
import com.atmira.api.model.PotentialDangerResponse;
import com.atmira.api.service.ApiNasaService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(UrlConstants.FORECASTER_REQUEST)
@Slf4j
public class ForecasterController {

	@Autowired
	private ApiNasaService apiNasaService;
	
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(UrlConstants.POTENTIAL_DANGER_ASTEROIDS)
	public List<PotentialDangerResponse> getPotentialDanger(@PathParam(value = "days") byte days) throws ForecasterException {
		log.info("Consulting API NASA...");
		return apiNasaService.callApiNasa(days);
	}
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = {ForecasterException.class})
	public ErrorModel foreCasterExceptionHandler(ForecasterException ex) {
		return new ErrorModel("apiNasa", Integer.parseInt(ex.getCode()), ex.getMessage(), ex.getDescription());
		
	}
}
