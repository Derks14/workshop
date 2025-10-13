package workshop.tools.dtetime;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/tool")
public class DteTimeController {

    private final ChatClient client;
    private final DteTimeTool tool;

    public DteTimeController(ChatClient.Builder builder, DteTimeTool tool) {
        this.client = builder.build();
        this.tool = tool;
    }

    @GetMapping("/tomorrow")
    public ResponseEntity<String> response(@RequestParam String message, HttpServletRequest request) {
        String sessionId = request.getSession().getId();

        log.info("{} new request has been made ", sessionId);

        String generatedResponse = this.client.prompt()
                .user(message)
//                .tools(new DteTimeTool())
                .tools(tool)
                .call()
                .content();

        return ResponseEntity.ok(generatedResponse);
    }
}
