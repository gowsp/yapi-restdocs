package io.github.gowsp.module.config;

import com.google.common.base.Preconditions;

public class YApiConfig {
    private String url;
    private String token;
    private Integer projectId;

    public void check() {
        Preconditions.checkNotNull(url, "config url not null");
        Preconditions.checkNotNull(token, "config token not nul");
        Preconditions.checkNotNull(projectId, "projectId not null");
    }

    public String getSavePath() {
        return getUrl() + "/api/interface/save?token=" + getToken();
    }

    // ------------------------ get set ------------------------

    public String getUrl() {
        return url;
    }

    public YApiConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getToken() {
        return token;
    }

    public YApiConfig setToken(String token) {
        this.token = token;
        return this;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public YApiConfig setProjectId(Integer projectId) {
        this.projectId = projectId;
        return this;
    }
}
