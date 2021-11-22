package io.github.gowsp.module.source.finder.impl;

import com.google.common.base.Preconditions;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import io.github.gowsp.module.source.finder.CatalogFinder;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Optional;

@Singleton
public class CatalogFinderImpl implements CatalogFinder {

    @Override
    public Number findCatalogId(JavaMethod method) {
        DocletTag catId = Optional.ofNullable(method.getTagByName("catId")).orElseGet(() -> {
            JavaClass javaClass = method.getDeclaringClass();
            return javaClass.getTagByName("catId");
        });
        Preconditions.checkNotNull(catId, "not found @catId in method or class comment");
        return new BigDecimal(catId.getValue());
    }
}
