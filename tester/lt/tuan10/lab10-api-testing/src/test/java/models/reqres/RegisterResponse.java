package models.reqres;

import com.fasterxml.jackson.annotation.JsonProperty;

public
class RegisterResponse {
    @JsonProperty("id") public Integer id;

    @JsonProperty("token") public String token;
}
