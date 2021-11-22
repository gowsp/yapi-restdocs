package io.github.gowsp.module.schema.validation;

public abstract class AbstractNumber extends AbstractValidation {

    public long value() {
        Number value = requireNumber("value");
        return value.longValue();
    }
}
