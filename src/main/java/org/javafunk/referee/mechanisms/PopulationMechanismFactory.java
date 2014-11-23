package org.javafunk.referee.mechanisms;

public interface PopulationMechanismFactory {
    <C> boolean canCreateFor(Class<C> targetType);
    <C> PopulationMechanism<C> forType(Class<C> targetType);
}
