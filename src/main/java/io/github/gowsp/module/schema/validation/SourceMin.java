package io.github.gowsp.module.schema.validation;

import javax.inject.Singleton;
import javax.validation.constraints.Min;
import java.lang.annotation.Annotation;

@Singleton
public class SourceMin extends AbstractNumber implements Min {

    @Override
    public Class<? extends Annotation> annotationType() {
        return Min.class;
    }
}
