package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.conversion.CoercionEngine;
import org.javafunk.referee.support.EnrichedClass;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuilderPopulationMechanismFactory implements PopulationMechanismFactory {
    CoercionEngine coercionEngine;

    @Override public <C> boolean canCreateFor(Class<C> targetType) {
        EnrichedClass<C> enrichedClass = new EnrichedClass<>(targetType);
        Option<EnrichedClass<?>> possibleBuilderClass = enrichedClass
                .findInnerClassWithName("Builder");
        return possibleBuilderClass.hasValue();
    }

    @Override public <D> PopulationMechanism<D> forType(Class<D> targetType) {
        return new BuilderPopulationMechanism<>(targetType, coercionEngine);
    }
}
