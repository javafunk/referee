package org.javafunk.referee.tree.factories.fromclass;

import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedField;
import org.javafunk.referee.tree.Tree;

import static org.javafunk.referee.tree.Tree.tree;
import static org.javafunk.referee.tree.factories.fromclass.EnrichedClassToNodeByField.fromEnrichedClassToNodeByField;

public class EnrichedClassToTreeByField
        implements UnaryFunction<EnrichedClass<?>, Tree<String, EnrichedField>> {
    public static EnrichedClassToTreeByField fromEnrichedClassToTreeByField() {
        return new EnrichedClassToTreeByField();
    }

    @Override public Tree<String, EnrichedField> call(EnrichedClass<?> enrichedClass) {
        return tree(fromEnrichedClassToNodeByField().call(enrichedClass));
    }
}
