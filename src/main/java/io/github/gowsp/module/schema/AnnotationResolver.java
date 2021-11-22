package io.github.gowsp.module.schema;

import com.thoughtworks.qdox.model.JavaAnnotatedElement;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotationResolver {

    List<Annotation> resolve(JavaAnnotatedElement element);
}
