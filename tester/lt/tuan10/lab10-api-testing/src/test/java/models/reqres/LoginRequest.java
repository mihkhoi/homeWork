package models.reqres;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) public class LoginRequest {
    @JsonProperty("email") public String email;

    @JsonProperty("password") public String password;

  public
    LoginRequest() {
    }

  public
    LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
