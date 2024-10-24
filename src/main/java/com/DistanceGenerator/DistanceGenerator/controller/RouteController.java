package com.DistanceGenerator.DistanceGenerator.controller;

import com.DistanceGenerator.DistanceGenerator.entity.RouteInfo;
import com.DistanceGenerator.DistanceGenerator.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/from/{fromPincode}/to/{toPincode}")
    public ResponseEntity<RouteInfo> getRoute(@PathVariable String fromPincode, @PathVariable String toPincode) {
        RouteInfo routeInfo = routeService.getRoute(fromPincode, toPincode);
        return new ResponseEntity<>(routeInfo, HttpStatus.OK);
    }
}
