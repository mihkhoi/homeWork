package models.reqres;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public
class CollectionRecordRequest {
    @JsonProperty("data") public Map<String, Object> data;

  public
    CollectionRecordRequest() {
    }

  public
    CollectionRecordRequest(Map<String, Object> data) {
        this.data = data;
    }
}
