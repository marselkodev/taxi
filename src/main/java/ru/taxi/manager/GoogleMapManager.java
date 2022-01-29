package ru.taxi.manager;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.taxi.model.RouteInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GoogleMapManager {
    private final GeoApiContext context;

    public GoogleMapManager() {
        log.info("Method GoogleMapManager starting");
        context = new GeoApiContext.Builder()
                .apiKey("YOUR_API_KEY")
                .build();
    }

    public List<RouteInfo> query(String from, String to) {
        log.info("Method query starting with params from = {}, to = {}", from, to);
        try {
            final DirectionsResult result = DirectionsApi
                    .getDirections(context, from, to)
                    .await();
            final List<RouteInfo> infos = new ArrayList<>(result.routes.length);
            for (DirectionsRoute route : result.routes) {
                long distance = 0;
                long duration = 0;
                for (DirectionsLeg leg : route.legs) {
                    distance += leg.distance.inMeters;
                    duration += leg.duration.inSeconds;
                }
                final RouteInfo info = new RouteInfo(distance, duration);
                infos.add(info);
            }
            log.info("Method query finished");
            return infos;
        } catch (ApiException | IOException | InterruptedException e) {
            log.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
