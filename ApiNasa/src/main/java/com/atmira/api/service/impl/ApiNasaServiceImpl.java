package com.atmira.api.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atmira.api.constants.GlobalConstants;
import com.atmira.api.constants.HttpConstants;
import com.atmira.api.exception.ForecasterException;
import com.atmira.api.model.Diameter;
import com.atmira.api.model.PotentialDangerResponse;
import com.atmira.api.service.ApiNasaService;
import com.atmira.api.util.DateUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ApiNasaServiceImpl implements ApiNasaService {

	private WebClient webClient;
	
	@Value("${env.base-url-endpoint}")
	private String baseUrl;
	
	@Value("${env.path-endpoint}")
	private String path;;
	
	@Value("${env.apykey-endpoint}")
	private String apiKey;
	
	private List<String> dates;
	
	public static final String START_DATE_PARAM = "start_date";
	public static final String END_DATE_PARAM = "end_date";
	public static final String APY_KEY_PARAM = "api_key";

	public ApiNasaServiceImpl(WebClient.Builder webClientBuilder) {
		super();
		this.webClient = webClientBuilder.build();
	}

	@Override
	public List<PotentialDangerResponse> callApiNasa(byte days) throws ForecasterException {

		Pattern pattern = Pattern.compile(GlobalConstants.PATTERN_REGGEX);
		Matcher match = pattern.matcher(String.valueOf(days));
		boolean valid = match.matches();
		if (valid) {
			DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
			webClient = WebClient.builder().uriBuilderFactory(factory).baseUrl(baseUrl).build();
			HttpHeaders header = new HttpHeaders();
			MediaType applicationJson = MediaType.APPLICATION_JSON;
			header.setContentType(applicationJson);
			this.dates = DateUtil.getDates(days);
			String responseApi = this.webClient.get()
					.uri(uriBuilder -> uriBuilder.path(path).queryParam(START_DATE_PARAM, dates.get(0))
							.queryParam(END_DATE_PARAM, dates.get(dates.size()-1))
							.queryParam(APY_KEY_PARAM, apiKey).build())
					.accept(applicationJson)
					.headers(httpHeadersOnWebClientBeingBuilt -> httpHeadersOnWebClientBeingBuilt.addAll(header)).retrieve()
					.onStatus(HttpStatus::isError,
							response -> response.bodyToMono(String.class)
							.flatMap(error -> Mono.error(new RuntimeException(error))))
					.bodyToMono(String.class).block();
			List<PotentialDangerResponse> potentialDangerResponse = getAsteroidsWithPotentialRisk(responseApi, days);
			return potentialDangerResponse;
		}
		log.error(GlobalConstants.INVALID_NUMBER_DAYS_DESCRIPTION);
		throw new ForecasterException(HttpConstants.BAD_REQUEST_CODE, GlobalConstants.INVALID_NUMBER_DAYS_MESSAGE, GlobalConstants.INVALID_NUMBER_DAYS_DESCRIPTION);
	}

	private List<PotentialDangerResponse> getAsteroidsWithPotentialRisk(String responseApi, byte days) {
		List<PotentialDangerResponse> listPotentialDanger = new ArrayList<PotentialDangerResponse>();
		JSONObject obj = JSONObject.parseObject(responseApi);
		JSONObject nearEarthObjects = (JSONObject) obj.get(GlobalConstants.NEAR_EARTH_OBJECTS);
		for (int i = 0; i < days; i++) {
			JSONArray date = (JSONArray) nearEarthObjects.get(dates.get(i));
			for (int j = 0; j < date.size(); j++) {
				JSONObject objAsteroid = (JSONObject) date.get(j);
				boolean isDangerous = (boolean) objAsteroid.get(GlobalConstants.IS_POTENTIALLY_HAZARDOUS_ASTEROID);
				if (isDangerous) {
					String name = objAsteroid.getString(GlobalConstants.ASTEROID_NAME);
					Diameter kilometers = geKilometers(objAsteroid);
					String velocity = getRelativeVelocityPerHour(objAsteroid);
					String approadDate = getApproadDate(objAsteroid);
					String planet = getPlanet(objAsteroid);
					listPotentialDanger.add(new PotentialDangerResponse(name,
							getDiameter(kilometers.getEstimatedDiameterMin(), kilometers.getEstimatedDiameterMax()),
							velocity, approadDate, planet));
				}
			}
		}
		
		return getTopListPotentialDanger(listPotentialDanger);
	}

	private List<PotentialDangerResponse> getTopListPotentialDanger(List<PotentialDangerResponse> listPotentialDanger) {
		int topNumber = 0;
		if (listPotentialDanger.size()>2) {
			topNumber = 3;
		}else {
			topNumber = listPotentialDanger.size();
		}
		Collections.sort(listPotentialDanger, (o1, o2) -> o2.getDiameter().compareTo(o1.getDiameter()));
		List<PotentialDangerResponse> topListPotentialDanger = new ArrayList<PotentialDangerResponse>();
		for (int i = 0; i < topNumber; i++) {
			topListPotentialDanger.add(listPotentialDanger.get(i));
		}
		return topListPotentialDanger;
	}

	private String getPlanet(JSONObject objAsteroid) {
		JSONArray objEstimated = (JSONArray) objAsteroid.get(GlobalConstants.CLOSE_APPROACH_DATA);
		JSONObject objCloseApproachData = (JSONObject) objEstimated.get(0);
		return objCloseApproachData.getString(GlobalConstants.ORBITING_BODY);
	}

	private String getApproadDate(JSONObject objAsteroid) {
		JSONArray objEstimated = (JSONArray) objAsteroid.get(GlobalConstants.CLOSE_APPROACH_DATA);
		JSONObject objCloseApproachData = (JSONObject) objEstimated.get(0);
		return objCloseApproachData.getString(GlobalConstants.CLOSE_APPROACH_DATE);
	}

	private String getRelativeVelocityPerHour(JSONObject objAsteroid) {
		JSONArray objEstimated = (JSONArray) objAsteroid.get(GlobalConstants.CLOSE_APPROACH_DATA);
		JSONObject objCloseApproachData = (JSONObject) objEstimated.get(0);
		JSONObject objRelativeVelocity = (JSONObject) objCloseApproachData.get(GlobalConstants.RELATIVE_VELOCITY);
		return objRelativeVelocity.getString(GlobalConstants.KILOMETERS_PER_HOUR);

	}

	private Diameter geKilometers(JSONObject objAsteroid) {
		JSONObject objEstimated = (JSONObject) objAsteroid.get(GlobalConstants.ESTIMATED_DIAMETER);
		JSONObject objKilometer = (JSONObject) objEstimated.get(GlobalConstants.KILOMETERS);
		return new Diameter(objKilometer.getBigDecimal(GlobalConstants.ESTIMATED_DIAMETER_MIN),
				objKilometer.getBigDecimal(GlobalConstants.ESTIMATED_DIAMETER_MAX));
	}

	private BigDecimal getDiameter(BigDecimal min, BigDecimal max) {
		BigDecimal roundValue = min.add(max).divide(new BigDecimal(2));
		return roundValue;
	}

}
