package models.reqres;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) public class UpdateUserResponse {
    @JsonProperty("name") public String name;

    @JsonProperty("job") public String job;

    @JsonProperty("updatedAt") public String updatedAt;
}
