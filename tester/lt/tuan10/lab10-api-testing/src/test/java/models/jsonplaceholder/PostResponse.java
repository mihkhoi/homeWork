package models.jsonplaceholder;

import com.fasterxml.jackson.annotation.JsonProperty;

public
class PostResponse {
    @JsonProperty("id") public Integer id;

    @JsonProperty("title") public String title;

    @JsonProperty("body") public String body;

    @JsonProperty("userId") public Integer userId;
}
