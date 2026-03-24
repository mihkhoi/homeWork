package models.reqres;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) public class CreateUserRequest {
    @JsonProperty("name") public String name;

    @JsonProperty("job") public String job;

  public
    CreateUserRequest() {
    }

  public
    CreateUserRequest(String name, String job) {
        this.name = name;
        this.job = job;
    }
}
