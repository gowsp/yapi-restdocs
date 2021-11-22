package io.github.gowsp.module.schema;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import io.github.gowsp.module.schema.builder.SchemaProcessor;
import io.github.gowsp.module.schema.jackson.JacksonAnnotationResolver;
import io.github.gowsp.module.schema.validation.ValidationResolver;
import io.github.gowsp.module.source.loader.SourceClassLoader;
import io.github.gowsp.module.source.loader.SourceProcessor;

public class SchemaModule extends AbstractModule {
    @Override
    protected void configure() {
        super.configure();
        Multibinder<AnnotationResolver> multibinder = Multibinder.newSetBinder(binder(), AnnotationResolver.class);
        multibinder.addBinding().to(ValidationResolver.class);
        multibinder.addBinding().to(JacksonAnnotationResolver.class);

        bind(SourceProcessor.class).to(SchemaProcessor.class);
        bind(SourceClassLoader.class);
        bind(SourceClassLoader.class);
    }
}
