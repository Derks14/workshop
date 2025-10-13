package workshop.tools.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Points(@JsonProperty("properties") Props properties) {


    @JsonIgnoreProperties
    public record Props(@JsonProperty("forecast") String forecast) {

    }

}
