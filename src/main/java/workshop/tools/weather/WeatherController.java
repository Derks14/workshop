package workshop.tools.weather;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("tool")
public class WeatherController {

    private final ChatClient client;
    private final WeatherTools weatherTools;

    public WeatherController(ChatClient.Builder builder, WeatherTools weatherTools) {
        this.client = builder.build();
        this.weatherTools = weatherTools;
    }

    @GetMapping("/weather/alerts")
    public String getAlerts(@RequestParam String message, HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("[{}] new request to get weather alerts ", sessionId);

        return this.client.prompt()
                .tools(weatherTools)
                .user(message)
                .call()
                .content();
    }
}
