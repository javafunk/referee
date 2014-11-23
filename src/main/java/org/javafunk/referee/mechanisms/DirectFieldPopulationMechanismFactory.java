package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.referee.conversion.CoercionEngine;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DirectFieldPopulationMechanismFactory implements PopulationMechanismFactory {
    CoercionEngine coercionEngine;

    @Override public <C> boolean canCreateFor(Class<C> targetType) {
        return true;
    }

    @Override public <C> PopulationMechanism<C> forType(Class<C> targetType) {
        return new DirectFieldPopulationMechanism<>(targetType, coercionEngine);
    }
}
