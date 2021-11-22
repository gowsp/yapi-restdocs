package io.github.gowsp.module.source.loader;

import com.thoughtworks.qdox.JavaProjectBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

@Singleton
public class SourceLoader {

    private final JavaProjectBuilder builder;

    @Inject
    private SourceLoader(JavaProjectBuilder builder) {
        this.builder = builder;
    }

    public SourceLoader addSourceClass(Set<Class> clazz) {
        if (clazz == null) {
            return this;
        }
        clazz.forEach(this::addSourceClass);
        return this;
    }

    private void addSourceClass(Class clazz) {
        URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
        try {
            url = new URL(url, "../../src/main/java");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String file = url.getFile();
        builder.addSourceTree(new File(file));
    }

    public SourceLoader addSourceDir(Set<String> path) {
        if (path == null) {
            return null;
        }
        path.forEach(p -> builder.addSourceTree(new File(p)));
        return this;
    }
}
