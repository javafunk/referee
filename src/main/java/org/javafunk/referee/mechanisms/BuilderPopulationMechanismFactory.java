package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.referee.PopulationMechanism;
import org.javafunk.referee.PopulationMechanismFactory;
import org.javafunk.referee.conversion.CoercionEngine;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuilderPopulationMechanismFactory implements PopulationMechanismFactory {
    CoercionEngine coercionEngine;

    @Override public <D> PopulationMechanism<D> forType(Class<D> targetType) {
        return new BuilderPopulationMechanism<D>(targetType, coercionEngine);
    }
}
