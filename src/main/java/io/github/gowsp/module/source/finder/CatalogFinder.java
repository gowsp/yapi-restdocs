package io.github.gowsp.module.source.finder;

import com.thoughtworks.qdox.model.JavaMethod;

public interface CatalogFinder {

    Number findCatalogId(JavaMethod method);
}
