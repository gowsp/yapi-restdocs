package io.github.gowsp.module.schema.jackson;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.thoughtworks.qdox.model.JavaAnnotatedElement;
import io.github.gowsp.util.AnnotationUtil;

import java.lang.annotation.Annotation;

public class JacksonJsonDescription implements JsonPropertyDescription {
    private final JavaAnnotatedElement element;

    public JacksonJsonDescription(JavaAnnotatedElement javaField) {
        this.element = javaField;
    }

    @Override
    public String value() {
        return AnnotationUtil.find(element, "com.fasterxml.jackson.annotation.JsonPropertyDescription")
                .map(annotation -> AnnotationUtil.stringProperty(annotation, "value", ""))
                .orElse(element.getComment());
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return JsonPropertyDescription.class;
    }
}
