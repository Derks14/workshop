package workshop;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {


    @GetMapping
    public ResponseEntity<String> home(HttpServletRequest request) {
        return ResponseEntity.ok("Hey there, welcome to this workshop done with the assistance of dan vega");
    }
}
