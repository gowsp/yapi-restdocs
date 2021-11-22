package io.github.gowsp.module.yapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class YApiDocument {
    /**
     * 工程ID
     */
    @JsonProperty("project_id")
    private Integer projectId;
    /**
     * 目录ID
     */
    @JsonProperty("catid")
    private Number catId;
    private String title;
    @JsonUnwrapped
    private YApiRequest request;
    /**
     * 路径参数
     */
    @JsonProperty("req_params")
    private List<YApiField> pathParams = Collections.emptyList();
    @JsonProperty("req_query")
    private List<YApiField> query = Collections.emptyList();
    @JsonProperty("req_headers")
    private List<YApiField> headers = Collections.emptyList();
    @JsonProperty("req_body_form")
    private List<YApiField> forms = Collections.emptyList();
    @JsonUnwrapped
    private YApiRequestBody requestBody;
    @JsonUnwrapped
    private YApiResponseBody responseBody;

    public boolean match(String method, String path) {
        return Objects.equals(getRequest().getMethod(), method) && Objects.equals(getRequest().getPath(), path);
    }

    public void addPathParam(YApiField field) {
        if (getPathParams().isEmpty()) {
            setPathParams(new ArrayList<>());
        }
        getPathParams().add(field);
    }

    public void addQuery(YApiField field) {
        if (getPathParams().isEmpty()) {
            setPathParams(new ArrayList<>());
        }
        getPathParams().add(field);
    }


    public Integer getProjectId() {
        return projectId;
    }

    public YApiDocument setProjectId(Integer projectId) {
        this.projectId = projectId;
        return this;
    }

    public Number getCatId() {
        return catId;
    }

    public YApiDocument setCatId(Number catId) {
        this.catId = catId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public YApiDocument setTitle(String title) {
        this.title = title;
        return this;
    }

    public YApiRequest getRequest() {
        return request;
    }

    public YApiDocument setRequest(YApiRequest request) {
        this.request = request;
        return this;
    }

    public List<YApiField> getPathParams() {
        return pathParams;
    }

    public YApiDocument setPathParams(List<YApiField> pathParams) {
        this.pathParams = pathParams;
        return this;
    }

    public List<YApiField> getQuery() {
        return query;
    }

    public YApiDocument setQuery(List<YApiField> query) {
        this.query = query;
        return this;
    }

    public List<YApiField> getHeaders() {
        return headers;
    }

    public YApiDocument setHeaders(List<YApiField> headers) {
        this.headers = headers;
        return this;
    }

    public List<YApiField> getForms() {
        return forms;
    }

    public YApiDocument setForms(List<YApiField> forms) {
        this.forms = forms;
        return this;
    }

    public YApiRequestBody getRequestBody() {
        return requestBody;
    }

    public YApiDocument setRequestBody(YApiRequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public YApiResponseBody getResponseBody() {
        return responseBody;
    }

    public YApiDocument setResponseBody(YApiResponseBody responseBody) {
        this.responseBody = responseBody;
        return this;
    }
}
