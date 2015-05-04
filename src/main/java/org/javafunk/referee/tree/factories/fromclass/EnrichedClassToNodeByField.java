package org.javafunk.referee.tree.factories.fromclass;

import org.javafunk.funk.Eagerly;
import org.javafunk.funk.Lazily;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedField;
import org.javafunk.referee.tree.Node;

import static org.javafunk.referee.tree.factories.fromclass.EnrichedFieldToNodeByField.fromEnrichedFieldToNodeByField;

public class EnrichedClassToNodeByField
        implements UnaryFunction<EnrichedClass<?>, Node<String, EnrichedField>> {
    public static EnrichedClassToNodeByField fromEnrichedClassToNodeByField() {
        return new EnrichedClassToNodeByField();
    }

    @Override public Node<String, EnrichedField> call(EnrichedClass<?> enrichedClass) {
        return Node.branchNode("$", Eagerly.map(enrichedClass.getAllFields(), fromEnrichedFieldToNodeByField()));
    }
}
