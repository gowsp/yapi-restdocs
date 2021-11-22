package io.github.gowsp.module.source.loader;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaType;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameterizedType;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeDescription.ForLoadedType;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ByteArrayClassLoader;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.pool.TypePool;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Singleton
public class SourceClassLoader {

    public static final ByteArrayClassLoader CLASS_LOADER = new ByteArrayClassLoader(ClassLoader.getSystemClassLoader(),
            Collections.emptyMap());
    private final JavaProjectBuilder builder;
    private final SourceProcessor processor;
    private final Cache<JavaClass, Class> cache = CacheBuilder.newBuilder().build();

    @Inject
    public SourceClassLoader(JavaProjectBuilder builder, SourceProcessor processor) {
        this.builder = builder;
        this.processor = processor;
    }

    public Class createClass(JavaType clazz) {
        if (clazz instanceof DefaultJavaParameterizedType) {
            DefaultJavaParameterizedType type = (DefaultJavaParameterizedType) clazz;
            if (clazz.getFullyQualifiedName().startsWith("java.util.")) {
                type.getActualTypeArguments().forEach(this::createClass);
                return null;
            }
        }
        JavaClass javaClass = builder.getClassByName(clazz.getFullyQualifiedName());
        return createClass(javaClass, javaField -> !Strings.isNullOrEmpty(javaField.getComment()));
    }

    public Class createClass(JavaClass clazz, Predicate<JavaField> predicate) {
        if (clazz instanceof DefaultJavaParameterizedType) {
            DefaultJavaParameterizedType parameterizedType = (DefaultJavaParameterizedType) clazz;
            parameterizedType.getActualTypeArguments().forEach(this::createClass);
        }
        if (clazz.getFullyQualifiedName().startsWith("java.lang.")) {
            try {
                return CLASS_LOADER.loadClass(clazz.getFullyQualifiedName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Class present = cache.getIfPresent(clazz);
        if (present != null) {
            return present;
        }
        DynamicType.Builder<?> builder = buildClass(clazz, predicate);
        builder = buildFiled(clazz, predicate, builder);
        builder = buildMethod(clazz, predicate, builder);
        Class<?> loaded = builder.make().load(CLASS_LOADER, ClassLoadingStrategy.Default.INJECTION).getLoaded();
        cache.put(clazz, loaded);
        return loaded;
    }

    private DynamicType.Builder<?> buildClass(JavaClass clazz, Predicate<JavaField> predicate) {
        Class parentClass = Object.class;
        JavaClass javaClass = clazz.getSuperJavaClass();
        if (!Objects.equals(javaClass.getFullyQualifiedName(), Object.class.getName())) {
            parentClass = createClass(javaClass, predicate);
        }
        String qualifiedName = clazz.getFullyQualifiedName();
        return new ByteBuddy().subclass(parentClass).name(qualifiedName);
    }

    private DynamicType.Builder<?> buildFiled(JavaClass clazz, Predicate<JavaField> predicate,
                                              DynamicType.Builder<?> builder) {
        List<JavaField> fields = clazz.getFields().stream().filter(predicate).collect(Collectors.toList());
        for (JavaField field : fields) {
            JavaClass type = field.getType();
            TypeDefinition definition = getTypeDefinition(predicate, type);
            builder = processor.process(builder.defineField(field.getName(), definition, Visibility.PUBLIC), field);
        }
        return builder;
    }

    private DynamicType.Builder<?> buildMethod(JavaClass clazz, Predicate<JavaField> predicate,
                                               DynamicType.Builder<?> builder) {
        List<JavaMethod> methods = clazz.getMethods().stream()
                .filter(method -> method.isPropertyAccessor() && !Strings.isNullOrEmpty(method.getComment()))
                .collect(Collectors.toList());
        for (JavaMethod method : methods) {
            JavaClass type = method.getReturns();
            TypeDefinition definition = getTypeDefinition(predicate, type);
            builder = processor.process(builder.defineMethod(method.getName(), definition, Visibility.PUBLIC), method);
        }
        return builder;
    }


    private TypeDefinition getTypeDefinition(Predicate<JavaField> predicate, JavaClass type) {
        try {
            String fullyQualifiedName = type.getFullyQualifiedName();
            TypeDescription description = TypePool.Default.of(CLASS_LOADER).describe(fullyQualifiedName)
                    .resolve();
            if (description.isAssignableTo(Collection.class) && (type instanceof DefaultJavaParameterizedType)) {
                return getGeneric((DefaultJavaParameterizedType) type, description);
            }
            return description;
        } catch (Exception e) {
            Class filedClass = createClass(type, predicate);
            return new ForLoadedType(filedClass);
        }
    }

    private TypeDescription.Generic getGeneric(DefaultJavaParameterizedType type, TypeDescription resolve) {
        List<JavaType> arguments = type.getActualTypeArguments();
        List<TypeDescription> list = arguments.stream()
                .map(javaType -> new ForLoadedType(createClass(javaType)))
                .collect(Collectors.toList());
        return TypeDescription.Generic.Builder.parameterizedType(resolve, list).build();
    }

}
