package workshop.multimodal.image;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("multimodal")
public class ImageDetection {

    private final ChatClient client;

    @Value("classpath:/images/unsplash.jpg")
    Resource unsplashImage;

    public ImageDetection(ChatClient.Builder builder) {
        this.client = builder.build();
    }


    @GetMapping("/image-to-text")
    public String imageToText(HttpServletRequest request) {
        String sessionId = request.getSession().getId();

        log.info("[{}] new request to process an image", sessionId);

        return this.client.prompt().user( u -> {
            u.text("Can you please describe what you see in the following image ? ");
            u.media(MimeTypeUtils.IMAGE_JPEG, unsplashImage);
        }).call().content();
    }
}
