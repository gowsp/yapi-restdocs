package io.github.gowsp.module.schema.jackson;

import com.thoughtworks.qdox.model.JavaAnnotatedElement;
import io.github.gowsp.module.schema.AnnotationResolver;

import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

@Singleton
public class JacksonAnnotationResolver implements AnnotationResolver {

    @Override
    public List<Annotation> resolve(JavaAnnotatedElement element) {
        return Arrays.asList(
                new JacksonJsonIgnore(element),
                new JacksonJsonProperty(element),
                new JacksonJsonDescription(element)
        );
    }

}
