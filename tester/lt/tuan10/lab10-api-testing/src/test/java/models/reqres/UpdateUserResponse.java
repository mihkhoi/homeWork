package models.reqres;

import com.fasterxml.jackson.annotation.JsonProperty;

public
class UpdateUserResponse {
    @JsonProperty("name") public String name;

    @JsonProperty("job") public String job;

    @JsonProperty("updatedAt") public String updatedAt;
}
