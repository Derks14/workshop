package workshop.chat;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @PostMapping("/chat")
    public ResponseEntity<String> entry (@RequestBody ChatRequest chatRequest,  HttpServletRequest request) {
        String sessionId = request.getSession().getId();

        log.info("[{}] User sending in new request: {} ", sessionId, chatRequest.message());

        String generatedResponse = this.chatClient.prompt()
                .user(chatRequest.message())
                .call()
                .content();

        return ResponseEntity.ok(generatedResponse);
    }


    @GetMapping("/stream")
    public ResponseEntity<Flux<String>> stream(@RequestBody ChatRequest chatRequest, HttpServletRequest request) {
        String sessionId = request.getSession().getId();

        log.info("[{}] request requires long response: {}", sessionId, chatRequest.message());

        Flux<String> generatedResponse = this.chatClient.prompt()
                .user(chatRequest.message())
                .stream()
                .content();
        return ResponseEntity.ok(generatedResponse);
    }

    @GetMapping("/joke")
    public ResponseEntity<ChatResponse> joke(@RequestBody ChatRequest chatRequest, HttpServletRequest request) {
        String sessionId = request.getSession().getId();

        log.info("[{}] this request returns an entire response object from the llm", sessionId);

        ChatResponse generatedResponse = this.chatClient.prompt()
                .user(chatRequest.message())
                .call()
                .chatResponse();


        return ResponseEntity.ok(generatedResponse);
    }


}
