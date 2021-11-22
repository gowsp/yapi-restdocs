package io.github.gowsp.module.schema.fastjson;

import com.thoughtworks.qdox.model.JavaAnnotatedElement;
import io.github.gowsp.module.schema.AnnotationResolver;

import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

@Singleton
public class FastjsonAnnotationResolver implements AnnotationResolver {

    @Override
    public List<Annotation> resolve(JavaAnnotatedElement element) {
        return Collections.emptyList();
    }
}
