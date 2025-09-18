package workshop.article;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    private final ChatClient chatClient;

    public ArticleController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/new")
    public ResponseEntity<String> article(@RequestParam(value = "topic", defaultValue = "JDK virtual threads") String topic,
                                          HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("[{}] New article request incoming. topic: {} ", sessionId, topic);

//        A system message in LLMs is a special type of input that provides high level instructions, context or behavioural
//        guidelines to the model before it processes user queries. Think of it as the ""behind-the-scenes instructions
//        that shape how the AI show respond

//        Use it as a guide or a restriction to the model's behaviour.

        String systemInstructions = """
                Blog Post Generator Guidelines
                
                1. Length & Purpose: Generate 500-word blog posts that inform and engage general audiences.
                
                2. Structure:
                    - Introduction: Hook readers and establish the topic's relevance
                    - Body: Develop 3 main points with supporting evidence and examples
                    - Conclusion: Summarise key takeaways and include a call-to-action
                
                3. Content Requirements:
                    - Include real-world applications or case studies
                    - Incorporate relevant statistics or data points when appropriate
                    - Explain benefits/implications clearly for non-experts
                    
                4. Tone & Style:
                    - Write in an informative yet conversational voice
                    - Use accessible language while maintaining authority
                    - Break up text with subheadings and short paragraphs
                 
                5. Response Format: Deliver complete, ready-to-publish posts with a suggested title.
                """;

        String generatedResponse = this.chatClient.prompt()
                .system(systemInstructions)
                .user( u -> {
                    u.text("Write me a blog post about {topic}");
                    u.param("topic", topic);
                })
                .call()
                .content();

        return ResponseEntity.ok(generatedResponse);
    }
}
