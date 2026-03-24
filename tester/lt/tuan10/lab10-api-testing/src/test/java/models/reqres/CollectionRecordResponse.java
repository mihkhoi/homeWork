package models.reqres;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true) public class CollectionRecordResponse {
    @JsonProperty("id") public Integer id;

    @JsonProperty("data") public Map<String, Object> data;
}
