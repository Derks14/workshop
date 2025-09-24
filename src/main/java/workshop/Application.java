package workshop;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	chat memory is the information that a llm retains and uses to maintain contextula awareness throughout a conversation
//	the entire convo history, including all messages exchanged between the user and the model
}
