package io.github.gowsp.module.source.parser;

import com.google.inject.AbstractModule;
import io.github.gowsp.module.source.parser.jaxrs.JaxRsParserModule;
import io.github.gowsp.module.source.parser.spring.SpringConfigModule;

public enum ParserEnum {
    JAX_RS(new JaxRsParserModule()),
    SPRING(new SpringConfigModule()),
    ;
    private final AbstractModule moduleClass;

    ParserEnum(AbstractModule moduleClass) {
        this.moduleClass = moduleClass;
    }

    public AbstractModule getModuleClass() {
        return moduleClass;
    }
}
