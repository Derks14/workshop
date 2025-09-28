package workshop.byod;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("prompt-stuffed")
public class ModelComparison {

    private final ChatClient client;

    public ModelComparison(ChatClient.Builder builder) {
        this.client = builder.build();
    }

    @GetMapping("/models")
    public ResponseEntity<String> currentModels(@RequestParam(defaultValue = "Can you give me an up to date list of popular large language models and their current context windows") String message, HttpServletRequest request) {

//        you can hit a certain api for this kind of information
        String infomation = """
                if you are asked about up to date language models and their context window here is some information to
                 help you with your response
                 
                [
                  {
                    "company": "Alibaba",
                    "model": "Qwen3-Next",
                    "context_window": "256,000 tokens (extendable to 1 million)"
                  },
                  {
                    "company": "Alibaba",
                    "model": "Qwen3-Max-Preview",
                    "context_window": "258,000 tokens"
                  },
                  {
                    "company": "Anthropic",
                    "model": "Claude 3 (Opus, Sonnet, Haiku)",
                    "context_window": "Initially 200,000 tokens, with the capability for over 1 million for select customers"
                  },
                  {
                    "company": "Anthropic",
                    "model": "Claude 4 Sonnet",
                    "context_window": "1,000,000 tokens"
                  },
                  {
                    "company": "DeepSeek",
                    "model": "DeepSeek R1",
                    "context_window": "64,000 tokens"
                  },
                  {
                    "company": "Google",
                    "model": "Gemini 2.5 Pro",
                    "context_window": "1,000,000 tokens (expected to increase to 2 million)"
                  },
                  {
                    "company": "Google",
                    "model": "Gemini 2.5 Flash",
                    "context_window": "1,000,000 tokens"
                  },
                  {
                    "company": "Magic.dev",
                    "model": "LTM-2-Mini",
                    "context_window": "100,000,000 tokens"
                  },
                  {
                    "company": "Meta",
                    "model": "Llama 3.1",
                    "context_window": "128,000 tokens"
                  },
                  {
                    "company": "Meta",
                    "model": "Llama 4 Scout",
                    "context_window": "10,000,000 tokens"
                  },
                  {
                    "company": "Meta",
                    "model": "Llama 4 Maverick",
                    "context_window": "1,000,000 tokens"
                  },
                  {
                    "company": "Mistral AI",
                    "model": "Mistral Large 2",
                    "context_window": "128,000 tokens"
                  },
                  {
                    "company": "OpenAI",
                    "model": "GPT-4.1",
                    "context_window": "1,000,000 tokens"
                  },
                  {
                    "company": "OpenAI",
                    "model": "GPT-4.1 Mini",
                    "context_window": "1,000,000 tokens"
                  },
                  {
                    "company": "OpenAI",
                    "model": "GPT-4.1 Nano",
                    "context_window": "1,000,000 tokens"
                  },
                  {
                    "company": "OpenAI",
                    "model": "GPT-4o",
                    "context_window": "128,000 tokens"
                  },
                   {
                    "company": "OpenAI",
                    "model": "GPT-4 Turbo",
                    "context_window": "128,000 tokens"
                  },
                  {
                    "company": "OpenAI",
                    "model": "GPT-5",
                    "context_window": "400,000 tokens"
                  }
                ]
                """;


        String generatedResponse = this.client
                .prompt()
                .user(message)
                .system(infomation)
                .call()
                .content();
        return ResponseEntity.ok(generatedResponse);
    }
}
