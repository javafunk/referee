package org.javafunk.referee.tree.factories.fromclass;

import lombok.Value;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedField;

@Value
public class ElementMetadata {
    Option<EnrichedClass<?>> possibleClass;
    Option<EnrichedField> possibleField;

    public static ElementMetadata forClass(EnrichedClass<?> enrichedClass) {
        return new ElementMetadata(
                Option.<EnrichedClass<?>>option(enrichedClass),
                Option.<EnrichedField>none());
    }

    public static ElementMetadata forField(
            EnrichedClass<?> enrichedClass,
            EnrichedField enrichedField) {
        return new ElementMetadata(
                Option.<EnrichedClass<?>>option(enrichedClass),
                Option.option(enrichedField));
    }

    public boolean hasNoClass() {
        return possibleClass.hasNoValue();
    }
}
