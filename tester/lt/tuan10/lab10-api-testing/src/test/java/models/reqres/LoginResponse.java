package models.reqres;

import com.fasterxml.jackson.annotation.JsonProperty;

public
class LoginResponse {
    @JsonProperty("token") public String token;
}
