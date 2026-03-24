package models.reqres;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) public class RegisterResponse {
    @JsonProperty("id") public Integer id;

    @JsonProperty("token") public String token;
}
