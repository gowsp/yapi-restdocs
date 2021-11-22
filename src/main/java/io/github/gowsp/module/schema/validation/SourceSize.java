package io.github.gowsp.module.schema.validation;

import javax.inject.Singleton;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;

@Singleton
public class SourceSize extends AbstractValidation implements Size {

    public int min() {
        Number min = getNumber("min", 0);
        return min.intValue();
    }

    public int max() {
        Number max = getNumber("max", Integer.MAX_VALUE);
        return max.intValue();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Size.class;
    }
}