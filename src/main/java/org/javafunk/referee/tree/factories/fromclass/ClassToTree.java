package org.javafunk.referee.tree.factories.fromclass;

import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.tree.Tree;

import static org.javafunk.referee.tree.factories.fromclass.EnrichedClassToTree.fromEnrichedClassToTree;

public class ClassToTree
        implements UnaryFunction<Class<?>, Tree<String, ElementMetadata>> {
    public static ClassToTree fromClassToTree() {
        return new ClassToTree();
    }

    @Override public Tree<String, ElementMetadata> call(Class<?> sourceClass) {
        return fromEnrichedClassToTree().call(new EnrichedClass<>(sourceClass));
    }
}
