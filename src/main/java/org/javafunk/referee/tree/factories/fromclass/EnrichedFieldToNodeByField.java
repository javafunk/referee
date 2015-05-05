package org.javafunk.referee.tree.factories.fromclass;

import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedField;
import org.javafunk.referee.tree.Node;

public class EnrichedFieldToNodeByField
        implements UnaryFunction<EnrichedField, Node<String, ElementMetadata>> {
    public static EnrichedFieldToNodeByField fromEnrichedFieldToNode() {
        return new EnrichedFieldToNodeByField();
    }

    @Override public Node<String, ElementMetadata> call(EnrichedField enrichedField) {
        return Node.leafNode(enrichedField.getName(), ElementMetadata.forField(new EnrichedClass<>(enrichedField.getType()), enrichedField));
    }
}
