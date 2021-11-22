package io.github.gowsp.module.schema.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.thoughtworks.qdox.model.JavaClass;
import io.github.gowsp.module.source.loader.SourceClassLoader;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SchemaCreator {
    private final ObjectMapper mapper;
    private final SourceClassLoader source;
    private final TypeFactory typeFactory;
    private final JsonSchemaGenerator generator;

    @Inject
    public SchemaCreator(ObjectMapper mapper, JsonSchemaGenerator generator, SourceClassLoader source) {
        this.source = source;
        this.mapper = mapper;
        this.generator = generator;
        this.typeFactory = mapper.getTypeFactory().withClassLoader(SourceClassLoader.CLASS_LOADER);
    }

    public String generate(JavaClass javaClass) {
        source.createClass(javaClass);
        try {
            JavaType javaType = typeFactory.constructFromCanonical(javaClass.getGenericCanonicalName());
            JsonSchema value = generator.generateSchema(javaType);
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
