package models.reqres;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) public class CreateUserResponse {
    @JsonProperty("name") public String name;

    @JsonProperty("job") public String job;

    @JsonProperty("id") public String id;

    @JsonProperty("createdAt") public String createdAt;
}
