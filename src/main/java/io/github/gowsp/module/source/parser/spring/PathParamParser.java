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

@Singleton
class PathParamParser implements Parser {
    private final String pathAnnotation = "org.springframework.web.bind.annotation.PathVariable";

    @Override
    public void assemble(YApiDocument document, JavaMethod method) {
        List<JavaParameter> parameters = method.getParameters();
        List<YApiField> list = new ArrayList<>();
        parameters.forEach(p -> process(method, list, p));
        document.setPathParams(list);
    }

    private void process(JavaMethod method, List<YApiField> list, JavaParameter p) {
        p.getAnnotations().stream().filter(a -> a.getType().getFullyQualifiedName().equals(pathAnnotation)).forEach(a -> {
            String paramName = p.getName();
            String filedName = AnnotationUtil.stringProperty(a, "value", paramName);
            String desc = TagUtil.paramDesc(method, paramName);
            YApiField field = new YApiField()
                    .setName(filedName)
                    .setDesc(desc);
            list.add(field);
        });
    }
}
