package io.github.gowsp.module.schema.builder;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.module.jsonSchema.factories.VisitorContext;

public class VisitorContextWithoutInlining extends VisitorContext {
    @Override
    public String getSeenSchemaUri(JavaType aSeenSchema) {
        return null;
    }

}