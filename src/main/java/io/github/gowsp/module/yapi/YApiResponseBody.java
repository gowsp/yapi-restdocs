package io.github.gowsp.module.yapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YApiResponseBody {
    @JsonProperty("res_body")
    private String responseBody;
    @JsonProperty("res_body_type")
    private String responseType;
    @JsonProperty("res_body_is_json_schema")
    private boolean responseJsonSchema;

    public String getResponseBody() {
        return responseBody;
    }

    public YApiResponseBody setResponseBody(String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public String getResponseType() {
        return responseType;
    }

    public YApiResponseBody setResponseType(String responseType) {
        this.responseType = responseType;
        return this;
    }

    public boolean isResponseJsonSchema() {
        return responseJsonSchema;
    }

    public YApiResponseBody setResponseJsonSchema(boolean responseJsonSchema) {
        this.responseJsonSchema = responseJsonSchema;
        return this;
    }
}