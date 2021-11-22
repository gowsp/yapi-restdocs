package io.github.gowsp.util;

import com.thoughtworks.qdox.model.JavaAnnotatedElement;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.expression.Expression;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AnnotationUtil {

    public static Optional<JavaAnnotation> find(JavaAnnotatedElement element, String fullTypeName) {
        return element.getAnnotations().stream().filter(a -> isAnnotation(a, fullTypeName)).findAny();
    }

    public static boolean allStartWith(List<JavaAnnotation> annotations, String prefix) {
        return annotations.stream().allMatch(a -> a.getType().getFullyQualifiedName().startsWith(prefix));
    }

    public static boolean contain(List<JavaAnnotation> annotations, String fullTypeName) {
        return annotations.stream().anyMatch(a -> isAnnotation(a, fullTypeName));
    }

    private static boolean isAnnotation(JavaAnnotation a, String fullTypeName) {
        return a.getType().getFullyQualifiedName().equals(fullTypeName);
    }

    private static Optional<Object> getParameter(JavaAnnotation source, String name) {
        return Optional.ofNullable(source)
                .map(annotation -> annotation.getProperty(name))
                .map(Expression::getParameterValue);
    }

    public static String stringProperty(JavaAnnotation source, String name, String defaultValue) {
        return getParameter(source, name).map(parameter -> {
            String val = Objects.toString(parameter);
            int lastIndexOf = val.lastIndexOf("\"");
            lastIndexOf = lastIndexOf == -1 ? val.length() : lastIndexOf;
            return val.substring(val.indexOf("\"") + 1, lastIndexOf);
        }).orElse(defaultValue);
    }

    public static String requireString(JavaAnnotation source, String name) {
        Optional<Object> parameter = getParameter(source, name);
        String val = Objects.toString(parameter.get());
        return val.substring(1, val.length() - 1);
    }

    public static Number numberProperty(JavaAnnotation source, String name, Number defaultValue) {
        return getParameter(source, name).map(parameter -> {
            String val = Objects.toString(parameter);
            return (Number) new BigDecimal(val);
        }).orElse(defaultValue);
    }

    public static Number requireNumber(JavaAnnotation source, String name) {
        Object parameter = getParameter(source, name).get();
        String val = Objects.toString(parameter);
        return new BigDecimal(val);
    }

    public static Boolean booleanProperty(JavaAnnotation source, String name, boolean defaultValue) {
        return getParameter(source, name).map(parameter -> {
            String val = Objects.toString(parameter);
            return Boolean.valueOf(val);
        }).orElse(defaultValue);
    }
}
