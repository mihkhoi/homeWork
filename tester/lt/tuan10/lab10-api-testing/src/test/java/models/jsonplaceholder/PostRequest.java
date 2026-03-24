package models.jsonplaceholder;

import com.fasterxml.jackson.annotation.JsonProperty;

public
class PostRequest {
    @JsonProperty("title") public String title;

    @JsonProperty("body") public String body;

    @JsonProperty("userId") public Integer userId;

  public
    PostRequest() {
    }

  public
    PostRequest(String title, String body, Integer userId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
    }
}
