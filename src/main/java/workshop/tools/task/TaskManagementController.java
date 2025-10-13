package workshop.tools.task;

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
public class TaskManagementController {

    private final ChatClient client;
    private final TaskManagementTool tools;

    public TaskManagementController(ChatClient.Builder builder, TaskManagementTool tool) {
        this.client = builder.build();
        this.tools = tool;
    }

    @GetMapping("/task")
    public ResponseEntity<String> createTask(@RequestParam(required = true) String message, HttpServletRequest request) {
        log.info("[{}] ", request.getSession().getId());

        String generatedResponse = this.client.prompt()
                .tools(tools)
                .user(message)
                .call()
                .content();
        return ResponseEntity.ok(generatedResponse);
    }
}
