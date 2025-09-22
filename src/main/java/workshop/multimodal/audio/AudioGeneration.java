package workshop.multimodal.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechMessage;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("multimodal")
public class AudioGeneration {

    private static final Logger log = LoggerFactory.getLogger(AudioGeneration.class);
    private final OpenAiAudioSpeechModel speechModel;
    private final ChatModel chatModel;

    public AudioGeneration(OpenAiAudioSpeechModel speechModel, ChatModel chatModel) {
        this.speechModel = speechModel;
        this.chatModel = chatModel;
    }

    @GetMapping("/speak")
    public ResponseEntity<byte[]> generateSpeech(@RequestParam(defaultValue = "Its a great time to be a Java and Spring Developer") String text) {
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .model("tts-1-hd") // or "tts-1-hd" for higher quality
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY) // ALLOY, ECHO, FABLE, ONYX, NOVA, SHIMMER
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0f) // 0.25 to 4.0
                .build();

        String systemInstruction = "System: You are a calm narrator with an indian accent.";
        String userMessage = "Hello, this is a text-to-speech example.";

        String combinedPrompt = systemInstruction + "\n\n" + userMessage;


        log.info("[{}] Show me the text ",combinedPrompt);


        SpeechPrompt speechPrompt = new SpeechPrompt(combinedPrompt, options);
        SpeechResponse speechResponse = speechModel.call(speechPrompt);


        byte[] audioBytes = speechResponse.getResult().getOutput();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .body(audioBytes);
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\speech.mp3\"") // this forces the browser to download the audio file
//                .body(new ByteArrayResource(audioBytes));
//                .body(new InputStreamResource(new ByteArrayInputStream(audioBytes)));

    }


    @GetMapping("/talk")
    public ResponseEntity<InputStreamResource> talk(@RequestParam(defaultValue = "Its a great time to be a Java and Spring Developer") String text) {
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .model("tts-1-hd") // or "tts-1-hd" for higher quality
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY) // ALLOY, ECHO, FABLE, ONYX, NOVA, SHIMMER
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0f) // 0.25 to 4.0
                .build();
        Prompt prompt = Prompt.builder().messages(
                new SystemMessage("You are an indian narrator"),
                new UserMessage(text)
        ).build();



        SpeechPrompt speechPrompt = new SpeechPrompt(prompt.getContents(), options);
        SpeechResponse speechResponse = speechModel.call(speechPrompt);


        byte[] audioBytes = speechResponse.getResult().getOutput();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .body(new InputStreamResource(new ByteArrayInputStream(audioBytes)));

//                .body(audioBytes);
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\speech.mp3\"") // this forces the browser to download the audio file
//                .body(new ByteArrayResource(audioBytes));

    }


}
