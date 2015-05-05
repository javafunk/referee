package org.javafunk.referee.tree.factories.fromclass;

import org.javafunk.funk.Eagerly;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.tree.Node;

import static org.javafunk.referee.tree.factories.fromclass.EnrichedFieldToNodeByField.fromEnrichedFieldToNode;

public class EnrichedClassToNode
        implements UnaryFunction<EnrichedClass<?>, Node<String, ElementMetadata>> {
    public static EnrichedClassToNode fromEnrichedClassToNode() {
        return new EnrichedClassToNode();
    }

    @Override public Node<String, ElementMetadata> call(EnrichedClass<?> enrichedClass) {
        return Node.node("$",
                ElementMetadata.forClass(enrichedClass),
                Eagerly.map(enrichedClass.getAllFields(), fromEnrichedFieldToNode()));
    }
}
