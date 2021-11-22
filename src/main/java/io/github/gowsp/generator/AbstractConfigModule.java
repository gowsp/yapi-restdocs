package io.github.gowsp.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.customProperties.ValidationSchemaFactoryWrapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.thoughtworks.qdox.JavaProjectBuilder;
import io.github.gowsp.module.schema.builder.SchemaCreator;
import io.github.gowsp.module.schema.builder.VisitorContextWithoutInlining;
import io.github.gowsp.module.source.finder.CatalogFinder;
import io.github.gowsp.module.source.finder.impl.CatalogFinderImpl;
import io.github.gowsp.module.source.loader.SourceLoader;
import io.github.gowsp.module.source.parser.Parser;
import io.github.gowsp.module.source.finder.SourceMethodFinder;

import javax.inject.Singleton;
import java.util.List;

public abstract class AbstractConfigModule extends AbstractModule {

    protected abstract List<Class<? extends Parser>> parsers();

    protected abstract Class<? extends SourceMethodFinder> sourceMethodFinder();

    @Override
    protected void configure() {
        super.configure();
        bind(ObjectMapper.class).in(Singleton.class);
        bind(JavaProjectBuilder.class).in(Singleton.class);

        bind(Generator.class);
        bind(SourceLoader.class);
        bind(SchemaCreator.class);

        bind(CatalogFinder.class).to(CatalogFinderImpl.class);
        bind(SourceMethodFinder.class).to(sourceMethodFinder());
        Multibinder<Parser> binder = Multibinder.newSetBinder(binder(), Parser.class);
        parsers().forEach(parser -> binder.addBinding().to(parser));
    }


    @Provides
    @Singleton
    JsonSchemaGenerator jsonSchemaGenerator(ObjectMapper mapper) {
        ValidationSchemaFactoryWrapper visitor = new ValidationSchemaFactoryWrapper();
        visitor.setVisitorContext(new VisitorContextWithoutInlining());
        return new JsonSchemaGenerator(mapper, visitor);
    }
}
