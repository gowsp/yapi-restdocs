package io.github.gowsp.module.source.parser.spring;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import io.github.gowsp.module.schema.builder.SchemaCreator;
import io.github.gowsp.module.source.parser.Parser;
import io.github.gowsp.module.yapi.YApiDocument;
import io.github.gowsp.module.yapi.YApiResponseBody;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ResponseBodyParser implements Parser {
    private final SchemaCreator creator;

    @Inject
    public ResponseBodyParser(SchemaCreator creator) {
        this.creator = creator;
    }

    @Override
    public void assemble(YApiDocument document, JavaMethod method) {
        JavaClass returns = method.getReturns();
        YApiResponseBody response = new YApiResponseBody();
        String responseJson = creator.generate(returns);
        response.setResponseBody(responseJson);
        response.setResponseType("json");
        response.setResponseJsonSchema(true);
        document.setResponseBody(response);
    }
}
