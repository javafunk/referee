package org.javafunk.referee.tree.factories.fromclass;

import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.tree.Tree;

import static org.javafunk.referee.tree.factories.fromclass.EnrichedClassToTreeByField.fromEnrichedClassToTreeByField;

public class ClassToTreeByField
        implements UnaryFunction<Class<?>, Tree<String, Object>> {
    public static ClassToTreeByField fromClassToTreeByField() {
        return new ClassToTreeByField();
    }

    @Override public Tree<String, Object> call(Class<?> sourceClass) {
        return fromEnrichedClassToTreeByField().call(new EnrichedClass<>(sourceClass));
    }
}
