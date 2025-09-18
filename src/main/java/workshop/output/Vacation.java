package workshop.output;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping
public class Vacation  {

    private final ChatClient chat;

    public Vacation(ChatClient.Builder builder) {
        this.chat = builder.build();
    }

    public ResponseEntity<String> unstructured(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return ResponseEntity.ok("")
    }

}
