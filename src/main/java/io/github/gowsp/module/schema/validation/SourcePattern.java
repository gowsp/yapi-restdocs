package io.github.gowsp.module.schema.validation;

import javax.inject.Singleton;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Annotation;

@Singleton
public class SourcePattern extends AbstractValidation implements Pattern {

    @Override
    public String regexp() {
        return requireString("regexp");
    }

    @Override
    public Flag[] flags() {
        return new Flag[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Pattern.class;
    }
}
