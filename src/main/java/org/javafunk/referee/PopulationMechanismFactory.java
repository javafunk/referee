package org.javafunk.referee;

public interface PopulationMechanismFactory {
    <C> PopulationMechanism<C> forType(Class<C> targetType);
}
