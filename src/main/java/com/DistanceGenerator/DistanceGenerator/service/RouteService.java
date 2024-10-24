package com.DistanceGenerator.DistanceGenerator.service;

import com.DistanceGenerator.DistanceGenerator.entity.RouteInfo;
import com.DistanceGenerator.DistanceGenerator.repository.PincodeInfoRepository;
import com.DistanceGenerator.DistanceGenerator.repository.RouteRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RouteService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RouteRepository routeRepository;

    private final String graphHopperApiKey = "b2992482-01f6-4bd7-855b-b8547bb7b2b0";
    private final String geocodingApiKey = "c7d248852f0741629bfefeacb7b2cba9";

    @Cacheable(value = "routeCache", key = "#fromPincode + '_' + #toPincode")
    public RouteInfo getRoute(String fromPincode, String toPincode) {

        String fromCoordinates = getCoordinates(fromPincode);
        String toCoordinates = getCoordinates(toPincode);


        String routeDetails = fetchRouteFromGraphHopper(fromCoordinates, toCoordinates);


        String distance = extractDistanceFromApi(routeDetails);
        String duration = extractDurationFromApi(routeDetails);


        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setFromPincode(fromPincode);
        routeInfo.setToPincode(toPincode);
        routeInfo.setDistance(distance);
        routeInfo.setDuration(duration);
        routeInfo.setRouteDetails(routeDetails);
        RouteInfo save = routeRepository.save(routeInfo);

        return routeInfo;
    }


    private String getCoordinates(String pincode) {

        String url = String.format("https://api.opencagedata.com/geocode/v1/json?q=%s&key=%s", pincode, geocodingApiKey);

        String response = restTemplate.getForObject(url, String.class);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

        JsonArray results = jsonObject.getAsJsonArray("results");
        if (results != null && results.size() > 0) {
            JsonObject firstResult = results.get(0).getAsJsonObject();
            JsonObject geometry = firstResult.getAsJsonObject("geometry");
            return geometry.get("lat").getAsString() + "," + geometry.get("lng").getAsString();
        }
        throw new IllegalArgumentException("Pincode not found: " + pincode);
    }


    private String fetchRouteFromGraphHopper(String fromCoordinates, String toCoordinates) {
        String url = String.format("https://graphhopper.com/api/1/route?point=%s&point=%s&key=%s",
                fromCoordinates, toCoordinates, graphHopperApiKey);

        return restTemplate.getForObject(url, String.class);
    }


    private String extractDistanceFromApi(String apiResponse) {
        JsonObject jsonObject = JsonParser.parseString(apiResponse).getAsJsonObject();
        JsonArray paths = jsonObject.getAsJsonArray("paths");

        if (paths != null && paths.size() > 0) {
            JsonObject firstPath = paths.get(0).getAsJsonObject();
            return firstPath.get("distance").getAsString(); // Distance in meters
        }
        return "0";
    }


    private String extractDurationFromApi(String apiResponse) {
        JsonObject jsonObject = JsonParser.parseString(apiResponse).getAsJsonObject();
        JsonArray paths = jsonObject.getAsJsonArray("paths");

        if (paths != null && paths.size() > 0) {
            JsonObject firstPath = paths.get(0).getAsJsonObject();
            return firstPath.get("time").getAsString(); // Duration in milliseconds
        }
        return "0";
    }
}
