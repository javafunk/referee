package org.javafunk.referee.tree.factories.fromclass;

import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.tree.Tree;

import static org.javafunk.referee.tree.Tree.tree;
import static org.javafunk.referee.tree.factories.fromclass.EnrichedClassToNode.fromEnrichedClassToNode;

public class EnrichedClassToTree
        implements UnaryFunction<EnrichedClass<?>, Tree<String, ElementMetadata>> {
    public static EnrichedClassToTree fromEnrichedClassToTree() {
        return new EnrichedClassToTree();
    }

    @Override public Tree<String, ElementMetadata> call(EnrichedClass<?> enrichedClass) {
        return tree(fromEnrichedClassToNode().call(enrichedClass));
    }
}
