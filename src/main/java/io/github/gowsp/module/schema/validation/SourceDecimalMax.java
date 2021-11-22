package io.github.gowsp.module.schema.validation;

import javax.inject.Singleton;
import javax.validation.constraints.DecimalMax;
import java.lang.annotation.Annotation;

@Singleton
public class SourceDecimalMax extends AbstractDecimal implements DecimalMax {

    @Override
    public Class<? extends Annotation> annotationType() {
        return DecimalMax.class;
    }
}
