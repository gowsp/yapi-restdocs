package io.github.gowsp.module.source.loader;

import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Initial;

public interface SourceProcessor {

    Builder<?> process(Valuable<?> builder, JavaField field);

    Builder<?> process(Initial<?> builder, JavaMethod method);
}
