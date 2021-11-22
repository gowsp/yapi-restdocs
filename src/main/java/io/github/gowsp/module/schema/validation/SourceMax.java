package io.github.gowsp.module.schema.validation;

import javax.inject.Singleton;
import javax.validation.constraints.Max;
import java.lang.annotation.Annotation;

@Singleton
public class SourceMax extends AbstractNumber implements Max {

    @Override
    public Class<? extends Annotation> annotationType() {
        return Max.class;
    }
}
