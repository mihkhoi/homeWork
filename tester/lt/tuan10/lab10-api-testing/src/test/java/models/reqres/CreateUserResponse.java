package models.reqres;

import com.fasterxml.jackson.annotation.JsonProperty;

public
class CreateUserResponse {
    @JsonProperty("name") public String name;

    @JsonProperty("job") public String job;

    @JsonProperty("id") public String id;

    @JsonProperty("createdAt") public String createdAt;
}
