package io.github.gowsp.module.yapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YApiRequestBody {
    @JsonProperty("req_body_other")
    private String requestBody;
    @JsonProperty("req_body_type")
    /**
     * 请求类型
     */
    private String requestType = "form";
    @JsonProperty("req_body_is_json_schema")
    private boolean requestJsonSchema;

    public String getRequestBody() {
        return requestBody;
    }

    public YApiRequestBody setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public YApiRequestBody setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public boolean isRequestJsonSchema() {
        return requestJsonSchema;
    }

    public YApiRequestBody setRequestJsonSchema(boolean requestJsonSchema) {
        this.requestJsonSchema = requestJsonSchema;
        return this;
    }
}