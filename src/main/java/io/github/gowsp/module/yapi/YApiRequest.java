package io.github.gowsp.module.yapi;

public class YApiRequest {
    private final String path;
    private final String method;

    public YApiRequest(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }
}