package io.github.gowsp.util;

import com.thoughtworks.qdox.model.JavaMethod;

public abstract class TagUtil {

    public static String paramDesc(JavaMethod method, String paramName) {
        return method.getTagsByName("param").stream()
                .filter(d -> d.getValue().split("\\s")[0].equals(paramName))
                .findFirst()
                .map(d -> d.getValue().replaceFirst(paramName, "").trim())
                .orElse(paramName);
    }

}
