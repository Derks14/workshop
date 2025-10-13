package workshop.tools.dtetime;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Component
public class DteTimeTool {

    @Tool(description = "get current date and time in the user's timezone ")
    public String getCurrentDateTime() {
//        this function serves as the ai tool definition for in getting the current date and times
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
