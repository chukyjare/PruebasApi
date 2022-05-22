package com.atmira.api.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atmira.api.model.Diameter;
import com.atmira.api.model.PotentialDangerResponse;
import com.atmira.api.service.ApiNasaService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ApiNasaServiceImpl implements ApiNasaService {

	private WebClient webClient;

	public ApiNasaServiceImpl(WebClient.Builder webClientBuilder) {
		super();
		this.webClient = webClientBuilder.build();
	}

	@Override
	public List<PotentialDangerResponse> callApiNasa(byte days) {

		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("https://api.nasa.gov");
		webClient = WebClient.builder().uriBuilderFactory(factory).baseUrl("https://api.nasa.gov").build();
		HttpHeaders header = new HttpHeaders();
		MediaType applicationJson = MediaType.APPLICATION_JSON;
		header.setContentType(applicationJson);
		String responseApi = this.webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/neo/rest/v1/feed").queryParam("start_date", "2021-12-09")
						.queryParam("end_date", "2021-12-12")
						.queryParam("api_key", "zdUP8ElJv1cehFM0rsZVSQN7uBVxlDnu4diHlLSb").build())
				.accept(applicationJson)
				.headers(httpHeadersOnWebClientBeingBuilt -> httpHeadersOnWebClientBeingBuilt.addAll(header)).retrieve()
				.onStatus(HttpStatus::isError,
						response -> response.bodyToMono(String.class)
								.flatMap(error -> Mono.error(new RuntimeException(error))))
				.bodyToMono(String.class).block();
		List<PotentialDangerResponse> potentialDangerResponse = getAsteroidsWithPotentialRisk(responseApi, days);
		return potentialDangerResponse;
	}

	private List<PotentialDangerResponse> getAsteroidsWithPotentialRisk(String responseApi, byte days) {
		List<PotentialDangerResponse> listPotentialDanger = new ArrayList<PotentialDangerResponse>();
		JSONObject obj = JSONObject.parseObject(responseApi);
		JSONObject nearEarthObjects = (JSONObject) obj.get("near_earth_objects");
		String[] dates = { "2021-12-12", "2021-12-11", "2021-12-10", "2021-12-09" };
		for (int i = 0; i < days; i++) {
			JSONArray date = (JSONArray) nearEarthObjects.get(dates[i]);
			for (int j = 0; j < date.size(); j++) {
				JSONObject objAsteroid = (JSONObject) date.get(i);
				boolean isDangerous = (boolean) objAsteroid.get("is_potentially_hazardous_asteroid");
				if (isDangerous) {
					String name = objAsteroid.getString("name");
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
		return listPotentialDanger;
	}

	private String getPlanet(JSONObject objAsteroid) {
		JSONArray objEstimated = (JSONArray) objAsteroid.get("close_approach_data");
		JSONObject objCloseApproachData = (JSONObject) objEstimated.get(0);
		return objCloseApproachData.getString("orbiting_body");
	}

	private String getApproadDate(JSONObject objAsteroid) {
		JSONArray objEstimated = (JSONArray) objAsteroid.get("close_approach_data");
		JSONObject objCloseApproachData = (JSONObject) objEstimated.get(0);
		return objCloseApproachData.getString("close_approach_date");
	}

	private String getRelativeVelocityPerHour(JSONObject objAsteroid) {
		JSONArray objEstimated = (JSONArray) objAsteroid.get("close_approach_data");
		JSONObject objCloseApproachData = (JSONObject) objEstimated.get(0);
		JSONObject objRelativeVelocity = (JSONObject) objCloseApproachData.get("relative_velocity");
		return objRelativeVelocity.getString("kilometers_per_hour");

	}

	private Diameter geKilometers(JSONObject objAsteroid) {
		JSONObject objEstimated = (JSONObject) objAsteroid.get("estimated_diameter");
		JSONObject objKilometer = (JSONObject) objEstimated.get("kilometers");
		return new Diameter(objKilometer.getBigDecimal("estimated_diameter_min"),
				objKilometer.getBigDecimal("estimated_diameter_max"));
	}

	private BigDecimal getDiameter(BigDecimal min, BigDecimal max) {
		BigDecimal roundValue = min.add(max).divide(new BigDecimal(2));
		return roundValue;
	}

}
