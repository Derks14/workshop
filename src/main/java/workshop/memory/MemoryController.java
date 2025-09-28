package workshop.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("memory")
public class MemoryController {

    private  final ChatClient client;

    public MemoryController(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.client = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
//        this.chatMemory = chatMemory;
    }

    @GetMapping("/stateless")
    public ResponseEntity<String> stateless(@RequestParam(defaultValue = "My name is 4teen") String message) {
        log.info("new request to test stateless llms conversations ");
        String generatedText = this.client
                .prompt()
                .user(message)
                .call()
                .content();

        return ResponseEntity.ok(generatedText);
    }

    @GetMapping("/stateful")
    public ResponseEntity<String> stateful(@RequestParam(required = true) String message) {
        log.info("new stateful request {}", message);
//        Advisor advisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        String generatedResponse = this.client
                .prompt()
                .user(message)
                .call()
                .content();

        return ResponseEntity.ok(generatedResponse);
    }
}
