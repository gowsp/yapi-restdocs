package io.github.gowsp.module.source.parser;

import com.thoughtworks.qdox.model.JavaMethod;
import io.github.gowsp.module.yapi.YApiDocument;

public interface Parser {

    void assemble(YApiDocument document, JavaMethod method);
}
