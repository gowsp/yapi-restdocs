package io.github.gowsp.module.source.parser.spring;

import com.thoughtworks.qdox.model.JavaMethod;
import io.github.gowsp.module.schema.builder.SchemaCreator;
import io.github.gowsp.module.source.parser.Parser;
import io.github.gowsp.module.yapi.YApiDocument;
import io.github.gowsp.module.yapi.YApiRequestBody;
import io.github.gowsp.util.AnnotationUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class RequestBodyParser implements Parser {

    private final String TYPE_NAME = "org.springframework.web.bind.annotation.RequestBody";

    private final SchemaCreator creator;

    @Inject
    public RequestBodyParser(SchemaCreator creator) {
        this.creator = creator;
    }

    @Override
    public void assemble(YApiDocument document, JavaMethod method) {
        YApiRequestBody body = new YApiRequestBody();
        method.getParameters().stream()
                .filter(p -> AnnotationUtil.contain(p.getAnnotations(), TYPE_NAME))
                .findFirst()
                .ifPresent(p -> {
                    String requestBody = creator.generate(p.getJavaClass());
                    body.setRequestBody(requestBody);
                    body.setRequestType("json");
                    body.setRequestJsonSchema(true);
                });
        document.setRequestBody(body);
    }
}
