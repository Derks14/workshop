package workshop.rag;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("rag")
@Slf4j
public class ModelsController {

    private final ChatClient client;


    public ModelsController(ChatClient.Builder builder, VectorStore vectorStore, RestTemplate restTemplate) {
        this.client = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();

    }


    @GetMapping("/models")
    public ResponseEntity<Models> getDataFromFile(@RequestParam(value = "message", defaultValue = "Give me a list of all the models from OPENAI along with their context window") String message,
                                                      HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("{} new request for : {}", sessionId, message);
        Models generatedResponse = this.client
                .prompt()
                .user(message)
                .call()
                .entity(Models.class);

        return ResponseEntity.ok(generatedResponse);
    }


}
