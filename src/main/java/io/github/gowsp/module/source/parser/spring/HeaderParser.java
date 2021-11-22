package io.github.gowsp.module.source.parser.spring;

import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import io.github.gowsp.module.source.parser.Parser;
import io.github.gowsp.module.yapi.YApiDocument;
import io.github.gowsp.module.yapi.YApiField;
import io.github.gowsp.util.AnnotationUtil;
import io.github.gowsp.util.TagUtil;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Singleton
class HeaderParser implements Parser {
   private final String header = "org.springframework.web.bind.annotation.RequestHeader";

    @Override
    public void assemble(YApiDocument document, JavaMethod method) {
        List<YApiField> list = new ArrayList<>();
        method.getParameters().forEach(p -> process(method, list, p));
        document.setHeaders(list);
    }

    private void process(JavaMethod method, List<YApiField> list, JavaParameter p) {
        p.getAnnotations().stream().filter(a -> Objects.equals(a.getType().getFullyQualifiedName(), header)).forEach(a -> {
            String paramName = p.getName();
            String filedName = AnnotationUtil.stringProperty(a, "value", paramName);
            Boolean required = AnnotationUtil.booleanProperty(a, "required", true);
            YApiField field = new YApiField().setName(filedName);
            field.setRequired(required.toString());
            String desc = TagUtil.paramDesc(method, paramName);
            field.setDesc(desc);
            list.add(field);
        });
    }
}
