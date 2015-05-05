package org.javafunk.referee.tree.factories.fromclass;

import org.javafunk.funk.Eagerly;
import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedField;
import org.javafunk.referee.tree.Node;

import java.util.Set;

import static org.javafunk.funk.Literals.setBuilder;
import static org.javafunk.referee.tree.Node.leafNode;
import static org.javafunk.referee.tree.Node.node;
import static org.javafunk.referee.tree.factories.fromclass.EnrichedClassToNode.fromEnrichedClassToNode;

public class EnrichedFieldToNodeByField
        implements UnaryFunction<EnrichedField, Node<String, ElementMetadata>> {
    private static final Set<EnrichedClass<?>> leafClasses = Literals.<EnrichedClass<?>>setBuilder()
            .with(new EnrichedClass<>(String.class))
            .build();

    public static EnrichedFieldToNodeByField fromEnrichedFieldToNode() {
        return new EnrichedFieldToNodeByField();
    }

    @Override public Node<String, ElementMetadata> call(EnrichedField field) {
        String fieldLabel = field.getName();
        EnrichedClass<?> fieldClass = new EnrichedClass<>(field.getType());
        ElementMetadata fieldMetadata = ElementMetadata.forField(fieldClass, field);

        if (leafClasses.contains(fieldClass)) {
            return leafNode(fieldLabel, fieldMetadata);
        } else {
            return node(fieldLabel,
                    fieldMetadata,
                    Eagerly.map(fieldClass.getAllFields(), fromEnrichedFieldToNode()));
        }
    }
}
