package io.github.gowsp.module.schema.builder;

import com.thoughtworks.qdox.model.JavaAnnotatedElement;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import io.github.gowsp.module.schema.AnnotationResolver;
import io.github.gowsp.module.source.loader.SourceProcessor;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Initial;
import net.bytebuddy.implementation.FixedValue;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class SchemaProcessor implements SourceProcessor {
    private final Set<AnnotationResolver> resolves;

    @Inject
    public SchemaProcessor(Set<AnnotationResolver> resolves) {
        this.resolves = resolves;
    }

    private List<Annotation> getAnnotations(JavaAnnotatedElement field) {
        return resolves.stream()
                .flatMap(resolve -> resolve.resolve(field).stream())
                .collect(Collectors.toList());
    }

    @Override
    public DynamicType.Builder<?> process(Valuable<?> builder, JavaField field) {
        List<Annotation> annotations = getAnnotations(field);
        return builder.annotateField(annotations);
    }


    @Override
    public DynamicType.Builder<?> process(Initial<?> builder, JavaMethod method) {
        List<Annotation> annotations = getAnnotations(method);
        return builder.intercept(FixedValue.nullValue()).annotateMethod(annotations);
    }


}
