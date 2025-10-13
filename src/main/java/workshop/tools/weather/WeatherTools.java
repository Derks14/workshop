package workshop.tools.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.stream.Collectors;

@Slf4j
@Service
public class WeatherTools {

    private static final String BASE_URL = "https://api.weather.gov";
    private final RestClient rest;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Enable pretty-printing
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }



    public WeatherTools(RestClient.Builder builder) {
        this.rest = builder
                .baseUrl(BASE_URL)
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "WeatherApiClient/1.0 (hey@keteku.dev)")
                .build();

    }



    /*
    * Get forecast for a specific latitude/longitude
    * @param latitude
    * @param longitude
    * @return the forecast for the given location
    * @throws RestClientException if the request fails
    */
    @Tool(description = "get weather forecast for a specific latitude/longitude")
    public String getWeatherForecastByLocation(double latitude, double longitude) {
        log.info("system getting real time weather updates based on specific location");

        Points points = rest.get()
                .uri("/points/{latitude},{longitude}", latitude, longitude)
                .retrieve()
                .body(Points.class);

        assert points != null;

        Forecast forecast = rest.get()
                .uri(points.properties().forecast())
                .retrieve()
                .body(Forecast.class);


        assert forecast != null;

        try {
            log.info("current data collected: %s".formatted(objectMapper.writeValueAsString(forecast)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String forecastText = forecast.properties().periods().stream().map(
                period -> {
                    return String.format("""
                            %s:
                            Temperature: %s %s
                            Wind: %s %s
                            Forecast: %s
                            """, period.name(),
                            period.temperature(),
                            period.temperatureUnit(),
                            period.windSpeed(),
                            period.windDirection(),
                            period.detailedForecast());
                }).collect(Collectors.joining());


        return forecastText;

    }

    @Tool(description = "get weather alerts for a US state. Input is Two-letter US state code(e.g CA, NY)")
    public String getAlerts(@ToolParam(description = "two letter US state code(e.g CA, NY)") String state) {

        log.info("system getting real time weather updates for a based on us state code");


        Alert alert = rest.get()
                .uri("/alerts/active/area/{state}", state)
                .retrieve()
                .body(Alert.class);

        assert alert != null;

        try {
            log.info("current data collected: %s".formatted(objectMapper.writeValueAsString(alert)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return alert.features()
                .stream()
                .map( feature -> {
                    return String.format("""
                                Event: %s
                                Area: %s
                                Severity: %s
                                Description: %s
                                Instructions: %s
                                """, feature.properties().event(),
                                feature.properties().areaDesc(),
                                feature.properties().severity(),
                                feature.properties().description(),
                                feature.properties().instruction()
                                );
                }).collect(Collectors.joining("\n"));
    }


}
