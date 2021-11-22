package io.github.gowsp.module.schema.validation;

import com.thoughtworks.qdox.model.JavaAnnotatedElement;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import io.github.gowsp.module.schema.AnnotationResolver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class ValidationResolver implements AnnotationResolver {
    private final Map<String, Class> annotations;
    private final Set<AbstractValidation> validations;

    @Inject
    public ValidationResolver(Map<String, Class> annotations, Set<AbstractValidation> validations) {
        this.annotations = annotations;
        this.validations = validations;
    }

    @Override
    public List<Annotation> resolve(JavaAnnotatedElement element) {
        return element.getAnnotations().stream()
                .map(this::contain)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<AbstractValidation> contain(JavaAnnotation annotation) {
        JavaClass type = annotation.getType();
        String qualifiedName = type.getFullyQualifiedName();
        Class clazz = annotations.get(qualifiedName);
        if (Objects.isNull(clazz)) {
            return Optional.empty();
        }
        return validations.stream()
                .filter(source -> source.annotationType().equals(clazz))
                .map(source -> source.clone().setSource(annotation))
                .findFirst();
    }
}
