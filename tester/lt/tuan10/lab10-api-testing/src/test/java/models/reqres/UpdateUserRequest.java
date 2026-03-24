package models.reqres;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) public class UpdateUserRequest {
    @JsonProperty("name") public String name;

    @JsonProperty("job") public String job;

  public
    UpdateUserRequest() {
    }

  public
    UpdateUserRequest(String name, String job) {
        this.name = name;
        this.job = job;
    }
}
