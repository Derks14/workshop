package workshop.acme;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import workshop.chat.ChatRequest;

@Slf4j
@RestController
@RequestMapping("/acme")
public class AcmeController {

    private final ChatClient chatClient;

    public AcmeController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }


    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody ChatRequest chatRequest, HttpServletRequest request) {
        String sessionId = request.getSession().getId();

        log.info("[{}] user message sent", sessionId);

        String systemInstructions = """
                You are a customer service assistant for AcmeBank.
                You can ONLY discuss:
                    - Account balances and transactions
                    - Branch locations and hours
                    -General banking services
                
                If asked about anything else, respond: "I can only help with banking-related questions"
                """;


        String generatedResponse = this.chatClient.prompt()
                .system(systemInstructions)
                .user(chatRequest.message())
                .call()
                .content();

        return ResponseEntity.ok(generatedResponse);

    }

}
