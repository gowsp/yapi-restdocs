package io.github.gowsp.module.schema.validation;

import com.google.common.reflect.ClassPath;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ValidationModule extends AbstractModule {

    @Override
    protected void configure() {
        MapBinder<String, Class> mapBinder = MapBinder.newMapBinder(binder(), String.class, Class.class);
        Multibinder<AbstractValidation> setBinder = Multibinder.newSetBinder(binder(), AbstractValidation.class);
        getClassPath().getTopLevelClasses(getClass().getPackage().getName())
                .stream()
                .map(ClassPath.ClassInfo::load)
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .filter(AbstractValidation.class::isAssignableFrom)
                .forEach(load -> {
                    setBinder.addBinding().to((Class<? extends AbstractValidation>) load);
                    Arrays.stream(load.getInterfaces())
                            .forEach(clazz -> mapBinder.addBinding(clazz.getName()).toInstance(clazz));
                });
    }

    private ClassPath getClassPath() {
        ClassPath path;
        try {
            path = ClassPath.from(ClassLoader.getSystemClassLoader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }
}
