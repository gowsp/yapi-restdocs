package io.github.gowsp.module.source.parser.spring;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import io.github.gowsp.module.source.parser.Parser;
import io.github.gowsp.module.yapi.YApiDocument;
import io.github.gowsp.module.yapi.YApiField;
import io.github.gowsp.util.AnnotationUtil;
import io.github.gowsp.util.TagUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
class QueryParamParser implements Parser {
    private static final List<String> IGNORES = Collections.singletonList("serialVersionUID");
    private static final List<String> EXCLUDE = Arrays.asList("javax.", "java.",
            "org.springframework.security.core.", "org.springframework.validation.");
    private static final String INCLUDE = "javax.validation.constraints";
    private final String Validated = "org.springframework.validation.annotation.Validated";
    private final JavaProjectBuilder builder;

    @Inject
    public QueryParamParser(JavaProjectBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void assemble(YApiDocument document, JavaMethod method) {
        List<JavaParameter> parameters = method.getParameters().stream()
                .filter(p -> {
                    List<JavaAnnotation> annotations = p.getAnnotations();
                    String name = p.getType().getFullyQualifiedName();
                    if (EXCLUDE.stream().anyMatch(name::startsWith)) {
                        return false;
                    }
                    return AnnotationUtil.allStartWith(annotations, INCLUDE) ||
                            AnnotationUtil.allStartWith(annotations, Validated);
                }).collect(Collectors.toList());
        if (parameters.isEmpty()) {
            return;
        }
        List<YApiField> list = parseField(method, parameters);
        document.setQuery(list);
    }

    private List<YApiField> parseField(JavaMethod method, List<JavaParameter> parameters) {
        List<YApiField> list = new ArrayList<>();
        parameters.forEach(parameter -> {
            String qualifiedName = parameter.getFullyQualifiedName();
            JavaClass clazz = builder.getClassByName(qualifiedName);
            if (clazz.isPrimitive() || qualifiedName.startsWith("java.lang.")) {
                baseType(list, method, parameter);
            } else {
                complexType(list, clazz);
            }
        });
        return list;
    }

    private void baseType(List<YApiField> list, JavaMethod method, JavaParameter parameter) {
        String name = parameter.getName();
        String desc = TagUtil.paramDesc(method, name);
        YApiField field = new YApiField()
                .setName(name)
                .setDesc(desc)
                .setType("string")
                .setRequired("0");
        list.add(field);
    }

    private void complexType(List<YApiField> list, JavaClass clazz) {
        for (JavaClass param = clazz; !param.getFullyQualifiedName().startsWith("java.lang.");
             param = param.getSuperJavaClass()) {
            param.getFields().stream().filter(f -> !IGNORES.contains(f.getName())).forEach(f -> {
                String comment = f.getComment();
                YApiField field = new YApiField()
                        .setDesc(comment)
                        .setName(f.getName())
                        .setType("string")
                        .setRequired("0");
                list.add(field);
            });
        }
    }
}
