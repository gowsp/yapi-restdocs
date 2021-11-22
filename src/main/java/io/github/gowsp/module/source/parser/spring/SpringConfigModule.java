package io.github.gowsp.module.source.parser.spring;

import io.github.gowsp.generator.AbstractConfigModule;
import io.github.gowsp.module.source.parser.Parser;
import io.github.gowsp.module.source.finder.SourceMethodFinder;

import java.util.Arrays;
import java.util.List;

public class SpringConfigModule extends AbstractConfigModule {

    @Override
    protected List<Class<? extends Parser>> parsers() {
        return Arrays.asList(
                HeaderParser.class,
                PathParamParser.class,
                QueryParamParser.class,
                RequestBodyParser.class,
                ResponseBodyParser.class
        );
    }

    @Override
    protected Class<? extends SourceMethodFinder> sourceMethodFinder() {
        return SpringSourceMethodFinder.class;
    }
}
