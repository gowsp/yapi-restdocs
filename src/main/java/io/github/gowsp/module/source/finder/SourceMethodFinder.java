package io.github.gowsp.module.source.finder;

import com.thoughtworks.qdox.model.JavaMethod;
import io.github.gowsp.module.yapi.YApiRequest;

public interface SourceMethodFinder {

    JavaMethod findMethod(YApiRequest request);
}
