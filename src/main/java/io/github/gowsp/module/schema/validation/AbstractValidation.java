package io.github.gowsp.module.schema.validation;

import com.thoughtworks.qdox.model.JavaAnnotation;
import io.github.gowsp.util.AnnotationUtil;

import javax.validation.Payload;
import java.lang.annotation.Annotation;

public abstract class AbstractValidation implements Annotation, Cloneable {
    protected JavaAnnotation source;

    public AbstractValidation setSource(JavaAnnotation source) {
        this.source = source;
        return this;
    }

    public String message() {
        return "";
    }

    public Class<?>[] groups() {
        return new Class[0];
    }

    public Class<? extends Payload>[] payload() {
        return new Class[0];
    }

    protected String requireString(String name) {
        return AnnotationUtil.requireString(source, name);
    }

    protected Number getNumber(String name, Number defaultValue) {
        return AnnotationUtil.numberProperty(source, name, defaultValue);
    }

    protected Number requireNumber(String name) {
        return AnnotationUtil.requireNumber(source, name);
    }

    protected Boolean getBooleanProperty(String name, boolean defaultValue) {
        return AnnotationUtil.booleanProperty(source, name, defaultValue);
    }

    @Override
    public AbstractValidation clone() {
        try {
            return (AbstractValidation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
