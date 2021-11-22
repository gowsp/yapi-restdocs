package io.github.gowsp.module.schema.validation;

public abstract class AbstractDecimal extends AbstractValidation {

    public String value() {
        return requireString("value");
    }

    public boolean inclusive() {
        return getBooleanProperty("inclusive", false);
    }

}
