package workshop.multimodal.image;


import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("multimodal")
public class ImageGeneration {
    private final OpenAiImageModel model;

    public ImageGeneration(OpenAiImageModel imageModel) {
        this.model = imageModel;
    }

    @GetMapping("/generate-image")
    public ResponseEntity<Map<String, String>> generateImage(@RequestParam(defaultValue = "A beautiful sunset over mountains") String prompt) {
//        use this options builder to specify features of the image you want to produce
        ImageOptions options = OpenAiImageOptions.builder()
                .model("dall-e-3")
                .width(1024).height(1024)
                .quality("hd")
                .style("natural") // or natural or vivid
                .build();

//        use this image prompt to combine both the text and the image options
        ImagePrompt imagePrompt = new ImagePrompt(prompt, options);

//        use this response to from the model
        ImageResponse response = this.model.call(imagePrompt);

        String url = response.getResult().getOutput().getUrl();

        return ResponseEntity.ok(Map.of(
                "prompt", prompt,
                "imageUrl", url
        ));
    }





}
