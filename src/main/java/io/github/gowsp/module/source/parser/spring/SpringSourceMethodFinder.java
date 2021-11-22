package io.github.gowsp.module.source.parser.spring;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaMethod;
import io.github.gowsp.module.source.finder.SourceMethodFinder;
import io.github.gowsp.module.yapi.YApiRequest;
import io.github.gowsp.util.AnnotationUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class SpringSourceMethodFinder implements SourceMethodFinder {
    private static final List<String> CONTROLLER_ANNOTATION = Arrays
            .asList("org.springframework.web.bind.annotation.RestController",
                    "org.springframework.stereotype.Controller");
    private static final Pattern PATTERN = Pattern
            .compile("org.springframework.web.bind.annotation.(Request|Get|Post|Put|Delete|Patch)Mapping");
    private final JavaProjectBuilder builder;

    @Inject
    public SpringSourceMethodFinder(JavaProjectBuilder builder) {
        this.builder = builder;
    }

    @Override
    public JavaMethod findMethod(YApiRequest request) {
        return builder.getClasses().stream()
                .filter(c -> c.getAnnotations().stream().anyMatch(a -> CONTROLLER_ANNOTATION.contains(a.getType().getFullyQualifiedName())))
                .flatMap(c -> c.getMethods().stream()).filter(m -> m.getAnnotations().stream().anyMatch(a -> match(a, m, request)))
                .findAny()
                .orElseThrow(() -> {
                    String msg = String.format("method %s, path %s not found in source", request.getMethod(), request.getPath());
                    return new IllegalArgumentException(msg);
                });
    }

    private boolean match(JavaAnnotation a, JavaMethod m, YApiRequest request) {
        String name = a.getType().getFullyQualifiedName();
        Matcher matcher = PATTERN.matcher(name);
        if (!matcher.find()) {
            return false;
        }
        String method = readMethod(matcher, a);
        String path = findParentPath(m) + readPath(a);
        return Objects.equals(request.getMethod(), method) && Objects.equals(request.getPath(), path);
    }

    private String findParentPath(JavaMethod m) {
        return m.getDeclaringClass().getAnnotations().stream()
                .filter(a -> PATTERN.matcher(a.getType().getFullyQualifiedName()).find())
                .findAny().map(this::readPath).orElse("");
    }

    private String readMethod(Matcher matcher, JavaAnnotation a) {
        String method = matcher.group(1);
        if (Objects.equals(method, "Request")) {
            return Optional.ofNullable(a.getProperty("method"))
                    .map(o -> {
                        String property = Objects.toString(o);
                        return property.split("\\.")[1];
                    }).orElse("GET");
        }
        return method.toUpperCase();
    }

    private String readPath(JavaAnnotation annotation) {
        return AnnotationUtil.stringProperty(annotation, "value", "");
    }
}
