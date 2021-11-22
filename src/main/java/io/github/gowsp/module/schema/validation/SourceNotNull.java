package io.github.gowsp.module.schema.validation;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;

@Singleton
public class SourceNotNull extends AbstractValidation implements NotNull {

    @Override
    public Class<? extends Annotation> annotationType() {
        return NotNull.class;
    }
}
