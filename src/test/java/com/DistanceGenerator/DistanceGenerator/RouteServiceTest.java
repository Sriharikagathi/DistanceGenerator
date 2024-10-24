package com.DistanceGenerator.DistanceGenerator;

import com.DistanceGenerator.DistanceGenerator.entity.RouteInfo;
import com.DistanceGenerator.DistanceGenerator.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RouteServiceTest {

    @InjectMocks
    private RouteService routeService;

    @Mock
    private RestTemplate restTemplate;

    private final String fromPincode = "141106";
    private final String toPincode = "110060";

    private final String fromCoordinates = "30.900965,75.857277";  // Mocked coordinates
    private final String toCoordinates = "28.635308,77.22496";     // Mocked coordinates

    private final String mockRouteResponse = "{ \"paths\": [{ \"distance\": 12345, \"time\": 67890 }] }"; // Mocked route response

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoute_CallsApiOnFirstRequest() {
        // Mock the responses from external APIs
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("{ \"results\": [{ \"geometry\": { \"lat\": 30.900965, \"lng\": 75.857277 } }] }")  // Mock OpenCage response for fromPincode
                .thenReturn("{ \"results\": [{ \"geometry\": { \"lat\": 28.635308, \"lng\": 77.22496 } }] }")   // Mock OpenCage response for toPincode
                .thenReturn(mockRouteResponse); // Mock GraphHopper response

        // Call the service method for the first time
        RouteInfo routeInfo = routeService.getRoute(fromPincode, toPincode);

        // Verify that external API is called
        verify(restTemplate, times(3)).getForObject(anyString(), eq(String.class));

        // Assert that the returned RouteInfo is correct
        assertNotNull(routeInfo);
        assertEquals("12345", routeInfo.getDistance());
        assertEquals("67890", routeInfo.getDuration());
    }

    @Test
    void testGetRoute_UsesCacheOnSubsequentRequests() {
        // Mock the responses from external APIs
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("{ \"results\": [{ \"geometry\": { \"lat\": 30.900965, \"lng\": 75.857277 } }] }")  // Mock OpenCage response for fromPincode
                .thenReturn("{ \"results\": [{ \"geometry\": { \"lat\": 28.635308, \"lng\": 77.22496 } }] }")   // Mock OpenCage response for toPincode
                .thenReturn(mockRouteResponse); // Mock GraphHopper response

        // Call the service method for the first time (API call should happen)
        RouteInfo routeInfoFirstCall = routeService.getRoute(fromPincode, toPincode);

        // Call the service method for the second time (should use cache)
        RouteInfo routeInfoSecondCall = routeService.getRoute(fromPincode, toPincode);

        // Verify that external API is only called once, as the second request should use the cache
        verify(restTemplate, times(3)).getForObject(anyString(), eq(String.class)); // 3 API calls for 1st request (2 for coordinates, 1 for GraphHopper)

        // Assert that the second call returns the same result (from cache)
        assertNotNull(routeInfoSecondCall);
        assertEquals(routeInfoFirstCall.getDistance(), routeInfoSecondCall.getDistance());
        assertEquals(routeInfoFirstCall.getDuration(), routeInfoSecondCall.getDuration());
    }
}
