package org.javafunk.referee.tree.factories.fromclass;

import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.support.EnrichedField;
import org.javafunk.referee.tree.Node;

public class EnrichedFieldToNodeByField implements UnaryFunction<EnrichedField, Node<String, Object>> {
    public static EnrichedFieldToNodeByField fromEnrichedFieldToNodeByField() {
        return new EnrichedFieldToNodeByField();
    }

    @Override public Node<String, Object> call(EnrichedField enrichedField) {
        return Node.<String, Object>leafNode(enrichedField.getName(), enrichedField);
    }
}
