package workshop.output;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/vacation")
public class Vacation  {

    private final ChatClient chat;

    public Vacation(ChatClient.Builder builder) {
        this.chat = builder.build();
    }


    @GetMapping("/unstructured")
    public ResponseEntity<String> unstructured(HttpServletRequest request) {
        String sessionId = request.getSession().getId();

        log.info("[{}] new user request to get unstructured itinery", sessionId);

        String generatedResponse = this.chat.prompt()
                .user("I want to plan a trip to Sydney. Give me a list of things to do ")
                .call()
                .content();
        log.info("[{}] response generated successfully", sessionId);
        return ResponseEntity.ok(generatedResponse);
    }

    @GetMapping("/structured")
    public ResponseEntity<Itinerary> structured(HttpServletRequest request) {
        String sessionId  = request.getSession().getId();
        log.info("[{}] new request to get structured itinery ", sessionId);

        Itinerary generatedResponse = this.chat.prompt()
                .user("I want to plan a simple trip to penrith in Sydney. Give me a list of things to do")
                .call()
                .entity(Itinerary.class);

        log.info("[{}] response generated successfully. Response: {}", sessionId, generatedResponse);
        return ResponseEntity.ok(generatedResponse);
    }

}
