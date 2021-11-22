package io.github.gowsp.module.schema.validation;

import javax.inject.Singleton;
import javax.validation.constraints.DecimalMin;
import java.lang.annotation.Annotation;

@Singleton
public class SourceDecimalMin extends AbstractDecimal implements DecimalMin {

    @Override
    public Class<? extends Annotation> annotationType() {
        return DecimalMin.class;
    }
}
