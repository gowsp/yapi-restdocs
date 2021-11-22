package io.github.gowsp.module.schema.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.thoughtworks.qdox.model.JavaAnnotatedElement;
import com.thoughtworks.qdox.model.JavaAnnotation;
import io.github.gowsp.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class JacksonJsonProperty implements JsonProperty {
    private final JavaAnnotatedElement element;
    private final Supplier<Optional<JavaAnnotation>> supplier;

    public JacksonJsonProperty(JavaAnnotatedElement element) {
        this.element = element;
        this.supplier = Suppliers.memoize(() ->
                AnnotationUtil.find(element, "com.fasterxml.jackson.annotation.JsonProperty"));
    }

    @Override
    public String value() {
        return supplier.get()
                .map(annotation -> AnnotationUtil.stringProperty(annotation, "value", USE_DEFAULT_NAME))
                .orElse(USE_DEFAULT_NAME);
    }

    @Override
    public String namespace() {
        return supplier.get()
                .map(annotation -> AnnotationUtil.stringProperty(annotation, "namespace", ""))
                .orElse("");
    }

    @Override
    public boolean required() {
        return supplier.get()
                .map(annotation -> AnnotationUtil.booleanProperty(annotation, "required", false))
                .orElse(false);
    }

    @Override
    public int index() {
        return supplier.get()
                .map(annotation -> AnnotationUtil.numberProperty(annotation, "index", INDEX_UNKNOWN).intValue())
                .orElse(INDEX_UNKNOWN);
    }

    @Override
    public String defaultValue() {
        return supplier.get()
                .map(annotation -> AnnotationUtil.stringProperty(annotation, "defaultValue", ""))
                .orElse("");
    }

    @Override
    public Access access() {
        return Access.AUTO;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return JsonProperty.class;
    }
}
