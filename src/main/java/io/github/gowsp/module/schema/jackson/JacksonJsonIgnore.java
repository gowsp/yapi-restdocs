package io.github.gowsp.module.schema.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.qdox.model.JavaAnnotatedElement;
import io.github.gowsp.util.AnnotationUtil;

import java.lang.annotation.Annotation;

public class JacksonJsonIgnore implements JsonIgnore {
    private final JavaAnnotatedElement element;

    public JacksonJsonIgnore(JavaAnnotatedElement element) {
        this.element = element;
    }

    @Override
    public boolean value() {
        return AnnotationUtil.find(element, "com.fasterxml.jackson.annotation.JsonIgnore")
                .map(annotation -> AnnotationUtil.booleanProperty(annotation, "value", true))
                .orElse(false);
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return JsonIgnore.class;
    }
}
