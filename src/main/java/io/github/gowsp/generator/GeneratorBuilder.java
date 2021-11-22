package io.github.gowsp.generator;

import com.google.common.base.Preconditions;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.gowsp.module.config.Config;
import io.github.gowsp.module.config.ConfigModule;
import io.github.gowsp.module.config.YApiConfig;
import io.github.gowsp.module.schema.SchemaModule;
import io.github.gowsp.module.schema.validation.ValidationModule;
import io.github.gowsp.module.source.loader.SourceLoader;
import io.github.gowsp.module.source.parser.ParserEnum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class GeneratorBuilder {
    private ParserEnum parser;
    private final YApiConfig config;
    private Set<String> sourceDirs;
    private Set<Class> sourceClasses;

    GeneratorBuilder(YApiConfig config) {
        this.config = config;
    }

    public GeneratorBuilder setParser(ParserEnum parser) {
        this.parser = parser;
        return this;
    }

    public GeneratorBuilder addSourcePath(String... sourcePath) {
        if (sourceDirs == null) {
            sourceDirs = new HashSet<>();
        }
        sourceDirs.addAll(Arrays.asList(sourcePath));
        return this;
    }

    public GeneratorBuilder setUrl(String url) {
        config.setUrl(url);
        return this;
    }

    public GeneratorBuilder setToken(String token) {
        config.setToken(token);
        return this;
    }

    public GeneratorBuilder setProjectId(Integer projectId) {
        config.setProjectId(projectId);
        return this;
    }

    public GeneratorBuilder addSourceClass(Class... sourceClass) {
        if (sourceClasses == null) {
            sourceClasses = new HashSet<>();
        }
        sourceClasses.addAll(Arrays.asList(sourceClass));
        return this;
    }


    public Generator build() {
        config.check();
        Config config = new Config().setYapi(this.config);
        Preconditions.checkNotNull(parser, "parser not null");
        Preconditions.checkState(sourceDirs != null || sourceClasses != null, "source not null");
        Injector injector = Guice.createInjector(new ConfigModule(config), new SchemaModule(),
                new ValidationModule(), parser.getModuleClass());
        SourceLoader loader = injector.getInstance(SourceLoader.class);
        loader.addSourceDir(sourceDirs).addSourceClass(sourceClasses);
        return injector.getInstance(Generator.class);
    }
}